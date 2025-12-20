// Service Worker for mapBH PWA
// Version: BUILD_TIMESTAMP (replaced during build)

const CACHE_VERSION = 'BUILD_TIMESTAMP';
const CACHE_NAME = `mapbh-app-${CACHE_VERSION}`;
const RUNTIME_CACHE = `mapbh-runtime-${CACHE_VERSION}`;
const TILE_CACHE = `mapbh-tiles`; // Persistent across versions!

// Core assets to cache immediately on install
const PRECACHE_ASSETS = [
  '/',
  '/en/',
  '/ar/',
  '/en/map',
  '/en/catalogue',
  '/css/app.css',
  '/css/all.min.css',
  '/css/bulma.min.css',
  '/css/leaflet.css',
  '/css/webfonts/noto-sans-arabic-subset.woff2',
  '/css/webfonts/roboto-v30-cyrillic_latin-regular.woff2',
  '/js/main.js',
  '/img/ogbrand.png',
  '/android-chrome-192x192.png',
  '/android-chrome-512x512.png',
  '/favicon.ico',
  '/site.webmanifest',
  '/offline.html'
];

// Install event - precache core assets
self.addEventListener('install', event => {
  console.log('[Service Worker] Installing...');

  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('[Service Worker] Precaching core assets');
        // Add assets one by one to handle failures gracefully
        return Promise.allSettled(
          PRECACHE_ASSETS.map(url =>
            cache.add(url).catch(err =>
              console.warn(`[Service Worker] Failed to cache ${url}:`, err)
            )
          )
        );
      })
      .then(() => self.skipWaiting()) // Activate immediately
  );
});

// Activate event - clean up old caches (but preserve tiles!)
self.addEventListener('activate', event => {
  console.log('[Service Worker] Activating...');

  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          // Keep current caches and ALWAYS preserve tile cache
          if (cacheName.startsWith('mapbh-') &&
              cacheName !== CACHE_NAME &&
              cacheName !== RUNTIME_CACHE &&
              cacheName !== TILE_CACHE) {
            console.log('[Service Worker] Deleting old cache:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    }).then(() => {
      console.log('[Service Worker] Activated with version:', CACHE_VERSION);
      return self.clients.claim(); // Take control immediately
    })
  );
});

// Fetch event - implement cache strategies
self.addEventListener('fetch', event => {
  const { request } = event;
  const url = new URL(request.url);

  // Skip non-GET requests
  if (request.method !== 'GET') return;

  // Skip cross-origin requests except for tiles and CDN assets
  if (url.origin !== location.origin) {
    // Handle map tiles from tile server
    if (url.hostname === 'map.mapbh.org' || url.hostname.includes('tile')) {
      event.respondWith(handleTileRequest(request));
      return;
    }
    // Handle CDN assets
    if (url.hostname.includes('cdn') || url.hostname.includes('cloudflare')) {
      event.respondWith(handleCDNRequest(request));
      return;
    }
    return;
  }

  // Route internal requests to appropriate handlers
  if (url.pathname.includes('/maps/') || url.pathname.endsWith('.mbtiles')) {
    event.respondWith(handleMapDataRequest(request));
  } else if (url.pathname.match(/\.(css|js|woff2?|ttf|eot)$/)) {
    event.respondWith(handleStaticAssets(request));
  } else if (url.pathname.match(/\.(png|jpg|jpeg|gif|svg|ico)$/)) {
    event.respondWith(handleImageRequest(request));
  } else {
    event.respondWith(handleNavigationRequest(request));
  }
});

// Cache strategies

// Network First - for HTML pages and navigation
async function handleNavigationRequest(request) {
  try {
    const networkResponse = await fetch(request);

    // Cache successful responses
    if (networkResponse.ok) {
      const cache = await caches.open(RUNTIME_CACHE);
      cache.put(request, networkResponse.clone());
    }

    return networkResponse;
  } catch (error) {
    // Try cache on network failure
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
      return cachedResponse;
    }

    // Return offline page for navigation requests
    if (request.mode === 'navigate') {
      return caches.match('/offline.html');
    }

    throw error;
  }
}

// Cache First - for static assets
async function handleStaticAssets(request) {
  const cachedResponse = await caches.match(request);
  if (cachedResponse) {
    return cachedResponse;
  }

  try {
    const networkResponse = await fetch(request);

    if (networkResponse.ok) {
      const cache = await caches.open(CACHE_NAME);
      cache.put(request, networkResponse.clone());
    }

    return networkResponse;
  } catch (error) {
    console.error('[Service Worker] Static asset fetch failed:', error);
    throw error;
  }
}

// Stale While Revalidate - for images
async function handleImageRequest(request) {
  const cache = await caches.open(RUNTIME_CACHE);
  const cachedResponse = await cache.match(request);

  const fetchPromise = fetch(request)
    .then(networkResponse => {
      if (networkResponse.ok) {
        cache.put(request, networkResponse.clone());
      }
      return networkResponse;
    })
    .catch(() => cachedResponse);

  return cachedResponse || fetchPromise;
}

// Stale While Revalidate with size limit - for map tiles
async function handleTileRequest(request) {
  const cache = await caches.open(TILE_CACHE);
  const cachedResponse = await cache.match(request);

  const fetchPromise = fetch(request)
    .then(async networkResponse => {
      if (networkResponse.ok) {
        // Implement cache size management
        await manageTileCacheSize();
        cache.put(request, networkResponse.clone());
      }
      return networkResponse;
    })
    .catch(() => cachedResponse);

  return cachedResponse || fetchPromise;
}

// Network First with timeout - for CDN assets
async function handleCDNRequest(request) {
  try {
    const networkResponse = await Promise.race([
      fetch(request),
      new Promise((_, reject) =>
        setTimeout(() => reject(new Error('Network timeout')), 5000)
      )
    ]);

    if (networkResponse.ok) {
      const cache = await caches.open(RUNTIME_CACHE);
      cache.put(request, networkResponse.clone());
    }

    return networkResponse;
  } catch (error) {
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
      return cachedResponse;
    }
    throw error;
  }
}

// Network First - for map data files
async function handleMapDataRequest(request) {
  try {
    const networkResponse = await fetch(request);

    // Only cache if response is ok and under 50MB
    if (networkResponse.ok) {
      const contentLength = networkResponse.headers.get('content-length');
      if (!contentLength || parseInt(contentLength) < 50 * 1024 * 1024) {
        const cache = await caches.open(RUNTIME_CACHE);
        cache.put(request, networkResponse.clone());
      }
    }

    return networkResponse;
  } catch (error) {
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
      return cachedResponse;
    }
    throw error;
  }
}

// Manage tile cache size (keep under 100MB)
async function manageTileCacheSize() {
  const cache = await caches.open(TILE_CACHE);
  const requests = await cache.keys();

  // Simple FIFO strategy - remove oldest tiles if over 500 tiles cached
  if (requests.length > 500) {
    const deleteCount = requests.length - 400; // Keep 400 tiles
    for (let i = 0; i < deleteCount; i++) {
      await cache.delete(requests[i]);
    }
    console.log(`[Service Worker] Cleaned ${deleteCount} old tiles from cache`);
  }
}

// Listen for skip waiting message
self.addEventListener('message', event => {
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting();
  }
});
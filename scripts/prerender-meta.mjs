#!/usr/bin/env node

/**
 * Pre-render meta tags for article pages.
 *
 * After a production build (shadow-cljs release), this script:
 * 1. Starts a temporary static HTTP server on public/
 * 2. Uses Puppeteer to visit the article index and discover article routes
 * 3. Visits each article route (both /en and /ar) to extract meta tags set by the SPA
 * 4. Writes per-route HTML files (copies of index.html with correct <head> tags)
 *
 * Nginx's `try_files $uri $uri/ /index.html` naturally serves these files
 * to crawlers that don't execute JavaScript.
 */

import { createServer } from 'http';
import { readFileSync, mkdirSync, writeFileSync } from 'fs';
import { join, extname } from 'path';
import puppeteer from 'puppeteer';

const PUBLIC_DIR = join(import.meta.dirname, '..', 'public');
const PRODUCTION_ORIGIN = 'https://www.mapbh.org';
const PORT = 9222;

// Minimal MIME type map for the static server
const MIME_TYPES = {
  '.html': 'text/html',
  '.js':   'application/javascript',
  '.css':  'text/css',
  '.json': 'application/json',
  '.png':  'image/png',
  '.jpg':  'image/jpeg',
  '.svg':  'image/svg+xml',
  '.ico':  'image/x-icon',
  '.woff': 'font/woff',
  '.woff2':'font/woff2',
  '.ttf':  'font/ttf',
  '.eot':  'application/vnd.ms-fontobject',
  '.webmanifest': 'application/manifest+json',
};

/**
 * Start a minimal static file server that mirrors nginx's try_files behaviour:
 * serve the file if it exists, otherwise fall back to /index.html.
 */
function startStaticServer() {
  const indexHtml = readFileSync(join(PUBLIC_DIR, 'index.html'));

  const server = createServer((req, res) => {
    const urlPath = req.url.split('?')[0];
    const filePath = join(PUBLIC_DIR, urlPath === '/' ? 'index.html' : urlPath);

    try {
      const data = readFileSync(filePath);
      const ext = extname(filePath);
      res.writeHead(200, { 'Content-Type': MIME_TYPES[ext] || 'application/octet-stream' });
      res.end(data);
    } catch {
      // SPA fallback – serve index.html for any route
      res.writeHead(200, { 'Content-Type': 'text/html' });
      res.end(indexHtml);
    }
  });

  return new Promise((resolve) => {
    server.listen(PORT, () => {
      console.log(`Static server listening on http://localhost:${PORT}`);
      resolve(server);
    });
  });
}

/**
 * Extract the meta tags that the SPA set via JS from the current page.
 */
async function extractMeta(page) {
  return page.evaluate(() => {
    const get = (sel) => {
      const el = document.querySelector(sel);
      return el ? (el.content || el.href || el.textContent) : null;
    };
    return {
      title:              document.title,
      description:        get('meta[name="description"]'),
      keywords:           get('meta[name="keywords"]'),
      ogTitle:            get('meta[property="og:title"]'),
      ogDescription:      get('meta[property="og:description"]'),
      ogUrl:              get('meta[property="og:url"]'),
      ogImage:            get('meta[property="og:image"]'),
      ogImageAlt:         get('meta[property="og:image:alt"]'),
      twitterTitle:       get('meta[name="twitter:title"]'),
      twitterDescription: get('meta[name="twitter:description"]'),
      twitterImage:       get('meta[name="twitter:image"]'),
      twitterImageAlt:    get('meta[name="twitter:image:alt"]'),
      canonical:          get('link[rel="canonical"]'),
    };
  });
}

/**
 * Discover article routes by visiting the English article index page and
 * collecting all <a> hrefs inside `.articles-container`.
 */
async function discoverArticleRoutes(page) {
  await page.goto(`http://localhost:${PORT}/en/articles/`, { waitUntil: 'load' });
  // Wait for the SPA to render the article list
  await page.waitForSelector('.articles-container a', { timeout: 15000 });

  const slugs = await page.evaluate(() => {
    const links = document.querySelectorAll('.articles-container a');
    return Array.from(links).map((a) => {
      // href might be relative (e.g. "fairey") or absolute
      const href = a.getAttribute('href');
      // Extract just the slug (last path segment)
      return href.replace(/^\/?(en|ar)\/articles\//, '').replace(/\/$/, '');
    });
  });

  console.log(`Discovered article slugs: ${slugs.join(', ')}`);
  return slugs;
}

/**
 * Replace meta tags in the template HTML with article-specific values.
 */
function renderHtml(template, meta) {
  let html = template;

  // Fix URLs: replace localhost references with production origin
  const fixUrl = (url) => {
    if (!url) return url;
    return url.replace(/http:\/\/localhost:\d+/g, PRODUCTION_ORIGIN);
  };

  const ogUrl = fixUrl(meta.ogUrl);
  const canonical = fixUrl(meta.canonical);

  // Replace <title>
  html = html.replace(
    /<title>[^<]*<\/title>/,
    `<title>${escapeHtml(meta.title)}</title>`
  );

  // Replace meta[name="description"]
  html = replaceMeta(html, 'name', 'description', meta.description);

  // Replace meta[name="keywords"]
  if (meta.keywords) {
    html = replaceMeta(html, 'name', 'keywords', meta.keywords);
  }

  // Replace OG tags
  html = replaceMeta(html, 'property', 'og:title', meta.ogTitle);
  html = replaceMeta(html, 'property', 'og:description', meta.ogDescription);
  html = replaceMeta(html, 'property', 'og:url', ogUrl);
  if (meta.ogImage) html = replaceMeta(html, 'property', 'og:image', meta.ogImage);
  if (meta.ogImageAlt) html = replaceMeta(html, 'property', 'og:image:alt', meta.ogImageAlt);

  // Replace Twitter tags
  html = replaceMeta(html, 'name', 'twitter:title', meta.twitterTitle);
  html = replaceMeta(html, 'name', 'twitter:description', meta.twitterDescription);
  if (meta.twitterImage) html = replaceMeta(html, 'name', 'twitter:image', meta.twitterImage);
  if (meta.twitterImageAlt) html = replaceMeta(html, 'name', 'twitter:image:alt', meta.twitterImageAlt);

  // Replace canonical link
  if (canonical) {
    html = html.replace(
      /<link\s+rel="canonical"\s+href="[^"]*"\s*\/?>/,
      `<link rel="canonical" href="${escapeHtml(canonical)}">`
    );
  }

  return html;
}

function replaceMeta(html, attr, value, content) {
  if (!content) return html;
  // Match both self-closing and non-self-closing meta tags
  const regex = new RegExp(
    `<meta\\s+${attr}="${escapeRegex(value)}"\\s+content="[^"]*"\\s*/?>`,
    'i'
  );
  const replacement = `<meta ${attr}="${value}" content="${escapeHtml(content)}">`;

  if (regex.test(html)) {
    return html.replace(regex, replacement);
  }

  // Try alternate attribute order: content before property/name
  const regexAlt = new RegExp(
    `<meta\\s+content="[^"]*"\\s+${attr}="${escapeRegex(value)}"\\s*/?>`,
    'i'
  );
  if (regexAlt.test(html)) {
    return html.replace(regexAlt, replacement);
  }

  return html;
}

function escapeHtml(str) {
  return str.replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function escapeRegex(str) {
  return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

// ── Main ────────────────────────────────────────────────────────────────

async function main() {
  const template = readFileSync(join(PUBLIC_DIR, 'index.html'), 'utf-8');
  const server = await startStaticServer();

  const browser = await puppeteer.launch({ headless: true });
  const page = await browser.newPage();

  try {
    // Discover article routes from the rendered article index
    const slugs = await discoverArticleRoutes(page);

    // Build the full list of routes to prerender:
    // - Article index pages: /en/articles/, /ar/articles/
    // - Individual articles: /en/articles/<slug>, /ar/articles/<slug>
    const routes = [
      { path: '/en/articles/', lang: 'en' },
      { path: '/ar/articles/', lang: 'ar' },
    ];
    for (const slug of slugs) {
      routes.push({ path: `/en/articles/${slug}`, lang: 'en' });
      routes.push({ path: `/ar/articles/${slug}`, lang: 'ar' });
    }

    for (const route of routes) {
      const url = `http://localhost:${PORT}${route.path}`;
      console.log(`Visiting ${url} ...`);

      await page.goto(url, { waitUntil: 'load' });
      // Wait for the SPA to boot and set meta tags
      await new Promise((r) => setTimeout(r, 3000));

      const meta = await extractMeta(page);
      console.log(`  title: ${meta.title}`);

      const html = renderHtml(template, meta);

      // Write to e.g. public/en/articles/fairey/index.html
      const outDir = join(PUBLIC_DIR, route.path.replace(/\/$/, ''));
      mkdirSync(outDir, { recursive: true });
      const outFile = join(outDir, 'index.html');
      writeFileSync(outFile, html, 'utf-8');
      console.log(`  → ${outFile}`);
    }

    console.log('\nPre-rendering complete.');
  } finally {
    await browser.close();
    server.close();
  }
}

main().catch((err) => {
  console.error('Pre-render failed:', err);
  process.exit(1);
});

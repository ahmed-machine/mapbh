// Service Worker Registration for mapBH PWA
// Extracted from index.html for better organization

// Register service worker for PWA functionality
if ('serviceWorker' in navigator) {
  // Auto-reload when a new service worker takes control
  let refreshing = false;
  navigator.serviceWorker.addEventListener('controllerchange', function() {
    if (refreshing) return;
    refreshing = true;
    window.location.reload();
  });

  window.addEventListener('load', function() {
    navigator.serviceWorker.register('/sw.js', {
      scope: '/',
      // Force browser to check for SW updates by bypassing HTTP cache
      updateViaCache: 'none'
    })
    .then(function(registration) {
      console.log('SW registered: ', registration.scope);

      // Check for updates periodically (every hour)
      setInterval(function() {
        registration.update();
      }, 60 * 60 * 1000);

      // Check for updates on page load
      registration.update();

      // Check for updates
      registration.addEventListener('updatefound', function() {
        const newWorker = registration.installing;
        if (newWorker) {
          newWorker.addEventListener('statechange', function() {
            if (newWorker.state === 'installed' && navigator.serviceWorker.controller) {
              // New version available, tell it to activate immediately
              newWorker.postMessage({ type: 'SKIP_WAITING' });
            }
          });
        }
      });
    })
    .catch(function(registrationError) {
      console.log('SW registration failed: ', registrationError);
    });
  });
}

// Install prompt handling
let deferredPrompt;
let installButton;

window.addEventListener('beforeinstallprompt', function(e) {
  e.preventDefault();
  deferredPrompt = e;

  // Show install button
  showInstallButton();
});

window.addEventListener('appinstalled', function(evt) {
  console.log('App installed successfully');
  hideInstallButton();
  deferredPrompt = null;
});

function showInstallButton() {
  // Create install button if it doesn't exist
  if (!installButton) {
    installButton = document.createElement('button');
    installButton.innerHTML = '📱 Install mapBH';
    installButton.style.cssText = `
      position: fixed;
      bottom: 20px;
      right: 20px;
      z-index: 10000;
      background: #3273dc;
      color: white;
      border: none;
      padding: 12px 20px;
      border-radius: 25px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      box-shadow: 0 4px 12px rgba(50, 115, 220, 0.4);
      transition: all 0.3s ease;
    `;

    installButton.addEventListener('mouseenter', function() {
      this.style.transform = 'translateY(-2px)';
      this.style.boxShadow = '0 6px 20px rgba(50, 115, 220, 0.5)';
    });

    installButton.addEventListener('mouseleave', function() {
      this.style.transform = 'translateY(0)';
      this.style.boxShadow = '0 4px 12px rgba(50, 115, 220, 0.4)';
    });

    installButton.addEventListener('click', function() {
      if (deferredPrompt) {
        deferredPrompt.prompt();
        deferredPrompt.userChoice.then(function(choiceResult) {
          if (choiceResult.outcome === 'accepted') {
            console.log('User accepted the install prompt');
          } else {
            console.log('User dismissed the install prompt');
          }
          deferredPrompt = null;
          hideInstallButton();
        });
      }
    });

    document.body.appendChild(installButton);
  }

  installButton.style.display = 'block';
}

function hideInstallButton() {
  if (installButton) {
    installButton.style.display = 'none';
  }
}

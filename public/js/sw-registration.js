// Service Worker Registration for mapBH PWA
// Extracted from index.html for better organization

// Register service worker for PWA functionality
if ('serviceWorker' in navigator) {
  window.addEventListener('load', function() {
    navigator.serviceWorker.register('/sw.js', {
      scope: '/'
    })
    .then(function(registration) {
      console.log('SW registered: ', registration.scope);

      // Check for updates
      registration.addEventListener('updatefound', function() {
        const newWorker = registration.installing;
        if (newWorker) {
          newWorker.addEventListener('statechange', function() {
            if (newWorker.state === 'installed' && navigator.serviceWorker.controller) {
              // New version available, show update notification
              showUpdateNotification(newWorker);
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

function showUpdateNotification(worker) {
  // Create update notification
  const updateNotification = document.createElement('div');
  updateNotification.innerHTML = `
    <div style="
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 10000;
      background: white;
      border: 2px solid #3273dc;
      padding: 15px 20px;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      max-width: 300px;
    ">
      <p style="margin: 0 0 10px 0; font-weight: 500;">New version available!</p>
      <button id="update-btn" style="
        background: #3273dc;
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
        margin-right: 10px;
      ">Update</button>
      <button id="dismiss-btn" style="
        background: transparent;
        color: #666;
        border: 1px solid #ddd;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
      ">Later</button>
    </div>
  `;

  document.body.appendChild(updateNotification);

  // Handle update button click
  updateNotification.querySelector('#update-btn').addEventListener('click', function() {
    worker.postMessage({ type: 'SKIP_WAITING' });
    window.location.reload();
  });

  // Handle dismiss button click
  updateNotification.querySelector('#dismiss-btn').addEventListener('click', function() {
    updateNotification.remove();
  });
}
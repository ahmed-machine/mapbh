#!/bin/bash
# Inject build timestamp into service worker for cache versioning

# Generate version string (timestamp + short git hash)
VERSION=$(date +%Y%m%d-%H%M%S)
GIT_HASH=$(git rev-parse --short HEAD 2>/dev/null || echo "dev")
BUILD_VERSION="${VERSION}-${GIT_HASH}"

SW_FILE="public/sw.js"

if [ ! -f "$SW_FILE" ]; then
  echo "Error: Service worker file not found at $SW_FILE"
  exit 1
fi

# Replace BUILD_TIMESTAMP with actual version
sed -i.bak "s/BUILD_TIMESTAMP/${BUILD_VERSION}/g" "$SW_FILE"

# Remove backup file
rm -f "${SW_FILE}.bak"

echo "✓ Service worker versioned: $BUILD_VERSION"
echo "  Updated: $SW_FILE"

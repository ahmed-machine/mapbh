#!/usr/bin/env bash
set -e

# Sync MBTiles from R2 CDN to local server
# This script should be run on the production server to download/sync mbtiles
# Usage: ./scripts/sync-mbtiles-from-r2.sh

# Get the script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MBTILES_DIR="$PROJECT_ROOT/public/maps"
R2_REMOTE="mapbh:mapbh/mbtiles"

echo "=== Syncing MBTiles from R2 to local server ==="
echo ""
echo "Source: $R2_REMOTE"
echo "Destination: $MBTILES_DIR"
echo ""

# Create directory if it doesn't exist
mkdir -p "$MBTILES_DIR"

# Sync mbtiles from R2 to local
# Using --checksum to verify file integrity
# Using --update to only download newer/missing files
rclone sync "$R2_REMOTE/" "$MBTILES_DIR/" \
  --include "*.mbtiles" \
  --checksum \
  --progress \
  --transfers 4 \
  --stats 10s

echo ""
echo "=== Sync complete ==="
echo ""

# Show summary
echo "Local MBTiles:"
find "$MBTILES_DIR" -name "*.mbtiles" -exec ls -lh {} \; | awk '{print $9, $5}'
echo ""
echo "Total size:"
du -sh "$MBTILES_DIR"/*.mbtiles | tail -1

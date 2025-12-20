#!/usr/bin/env bash
set -e

# Master sync script for production server
# This should be run on the production server after deploying code
# Usage: ./scripts/server-sync-all.sh

echo "========================================="
echo "  mapBH Production Server Sync"
echo "========================================="
echo ""

# Check if rclone is configured
if ! rclone listremotes | grep -q "mapbh:"; then
    echo "Error: rclone not configured with 'mapbh' remote"
    echo "Please configure rclone with R2 credentials first:"
    echo "  rclone config"
    exit 1
fi

# Step 1: Sync MBTiles from R2
echo "Step 1/2: Syncing MBTiles from R2..."
echo ""
./scripts/sync-mbtiles-from-r2.sh

echo ""
echo "========================================="
echo ""

# Step 2: Restart tileserver if running
echo "Step 2/2: Checking tileserver status..."
echo ""

if systemctl is-active --quiet tileserver-gl; then
    echo "Restarting tileserver-gl..."
    sudo systemctl restart tileserver-gl
    echo "✓ Tileserver-gl restarted"
else
    echo "Note: tileserver-gl is not running"
    echo "Start it with: sudo systemctl start tileserver-gl"
fi

echo ""
echo "========================================="
echo "  Sync Complete!"
echo "========================================="
echo ""
echo "Summary:"
echo "  - MBTiles synced from R2 CDN"
echo "  - Source files served from: https://cdn.mapbh.org/"
echo "  - Thumbnails served from: https://cdn.mapbh.org/thumbnails/"
echo "  - Tiles served from local tileserver"
echo ""

#!/usr/bin/env bash
set -e

# Upload MBTiles to R2 CDN
# Usage: ./scripts/upload-mbtiles-to-r2.sh <mbtiles-file> [mbtiles-file2 ...]
# Example: ./scripts/upload-mbtiles-to-r2.sh public/maps/2025-NewMap.mbtiles

if [ $# -eq 0 ]; then
    echo "Usage: $0 <mbtiles-file> [mbtiles-file2 ...]"
    echo "Example: $0 public/maps/2025-NewMap.mbtiles"
    exit 1
fi

R2_REMOTE="mapbh:mapbh/mbtiles"

echo "=== Uploading MBTiles to R2 CDN ==="
echo ""

# Process each mbtiles file
for mbtiles_file in "$@"; do
    if [[ ! -f "$mbtiles_file" ]]; then
        echo "Error: File not found: $mbtiles_file"
        continue
    fi

    if [[ ! "$mbtiles_file" =~ \.mbtiles$ ]]; then
        echo "Error: Not an mbtiles file: $mbtiles_file"
        continue
    fi

    filename=$(basename "$mbtiles_file")
    echo "Uploading: $filename"
    echo "Size: $(du -h "$mbtiles_file" | cut -f1)"

    # Upload to R2
    if rclone copy "$mbtiles_file" "$R2_REMOTE/" --progress; then
        echo "✓ Uploaded: $filename"
    else
        echo "✗ Failed to upload: $filename"
        continue
    fi

    echo ""
done

echo "=== Upload complete ==="
echo ""
echo "Next steps:"
echo "1. On production server, run: ./scripts/sync-mbtiles-from-r2.sh"
echo "2. Restart tileserver-gl if needed: sudo systemctl restart tileserver-gl"

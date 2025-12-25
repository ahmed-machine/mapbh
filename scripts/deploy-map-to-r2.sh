#!/usr/bin/env bash
set -e

# Deploy new map files to R2 CDN
# Usage: ./scripts/deploy-map-to-r2.sh <map-file>
# Example: ./scripts/deploy-map-to-r2.sh public/maps/2025-NewMap.tif

if [ $# -eq 0 ]; then
    echo "Usage: $0 <map-file> [map-file2 ...]"
    echo "Example: $0 public/maps/2025-NewMap.tif"
    exit 1
fi

MAPS_DIR="public/maps"
THUMBNAILS_DIR="public/thumbnails"
R2_REMOTE="mapbh:mapbh"

echo "=== Deploying maps to R2 CDN ==="
echo ""

# Create thumbnails directory if it doesn't exist
mkdir -p "$THUMBNAILS_DIR"

# Process each map file
for map_file in "$@"; do
    if [[ ! -f "$map_file" ]]; then
        echo "Error: File not found: $map_file"
        continue
    fi

    filename=$(basename "$map_file")
    map_id=$(echo "$filename" | sed -E 's/\.(tif|tiff|jpg|jpeg|png|jp2)$//')
    thumbnail_path="$THUMBNAILS_DIR/${map_id}.png"

    echo "Processing: $filename"
    echo "Map ID: $map_id"

    # Step 1: Generate thumbnail if it doesn't exist
    if [[ -f "$thumbnail_path" ]]; then
        echo "  ✓ Thumbnail already exists: $thumbnail_path"
    else
        echo "  → Generating thumbnail..."
        if convert "$map_file[0]" -resize 1200x\> -quality 85 -strip -interlace Plane "$thumbnail_path" 2>/dev/null; then
            echo "  ✓ Generated thumbnail: $thumbnail_path"
        elif convert "$map_file" -resize 1200x\> -quality 85 -strip -interlace Plane "$thumbnail_path" 2>/dev/null; then
            echo "  ✓ Generated thumbnail (fallback): $thumbnail_path"
        else
            echo "  ✗ Failed to generate thumbnail for $map_file"
            continue
        fi
    fi

    # Step 2: Upload source file to R2
    echo "  → Uploading source file to R2..."
    if rclone copy --s3-no-check-bucket "$map_file" "$R2_REMOTE/" --progress; then
        echo "  ✓ Uploaded: $filename"
    else
        echo "  ✗ Failed to upload source file"
        continue
    fi

    # Step 3: Upload thumbnail to R2
    echo "  → Uploading thumbnail to R2..."
    if rclone copy --s3-no-check-bucket "$thumbnail_path" "$R2_REMOTE/thumbnails/" --progress; then
        echo "  ✓ Uploaded thumbnail: ${map_id}.png"
    else
        echo "  ✗ Failed to upload thumbnail"
        continue
    fi

    echo "  ✓ Successfully deployed: $filename"
    echo ""
done

echo "=== Deployment complete ==="
echo ""
echo "Next steps:"
echo "1. Add map metadata to src/app/data.cljs"
echo "2. Compile: npx shadow-cljs compile app"
echo "3. Deploy code to production"
echo ""
echo "Note: Source files are served from https://cdn.mapbh.org/"
echo "      Thumbnails are served from https://cdn.mapbh.org/thumbnails/"

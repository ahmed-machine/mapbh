#!/usr/bin/env bash
set -e

echo "=== Generating thumbnails for map entries ==="

MAPS_DIR="./public/maps"
THUMBNAILS_DIR="./public/thumbnails"

mkdir -p "$THUMBNAILS_DIR"

# Function to extract filename without extension (preserve all suffixes)
get_map_id() {
    local filename="$1"
    # Remove only extensions, preserve all suffixes to match ClojureScript thumbnail path generation
    echo "$filename" | sed -E 's/\.(tif|tiff|jpg|jpeg|png)$//'
}


# Create temporary files to track processed map IDs
TEMP_DIR="/tmp/thumbnails_$$"
mkdir -p "$TEMP_DIR"

# Collect all valid image files
for file in "$MAPS_DIR"/*.{tif,tiff,jpg,jpeg,png}; do
    [[ -f "$file" ]] || continue

    filename=$(basename "$file")

    # Skip aux files and Git LFS pointers
    [[ "$filename" == *".aux."* ]] && continue
    [[ "$filename" == *".tfw" ]] && continue

    # Skip Git LFS pointer files
    if [[ -f "$file" && $(stat -f%z "$file" 2>/dev/null) -lt 1000 ]]; then
        if head -1 "$file" 2>/dev/null | grep -q "version https://git-lfs"; then
            echo "Skipping Git LFS pointer: $filename"
            continue
        fi
    fi

    map_id=$(get_map_id "$filename")

    # Each unique filename (without extension) gets its own thumbnail
    echo "$file" > "$TEMP_DIR/$map_id"
done

# Generate thumbnails for best files
for map_file in "$TEMP_DIR"/*; do
    [[ -f "$map_file" ]] || continue

    map_id=$(basename "$map_file")
    file=$(cat "$map_file")
    filename=$(basename "$file")
    thumbnail_path="$THUMBNAILS_DIR/${map_id}.png"

    # Skip if thumbnail already exists for this map ID
    if [[ -f "$thumbnail_path" ]]; then
        echo "Thumbnail already exists for map: $map_id"
        continue
    fi

    echo "Generating thumbnail for map '$map_id': $filename -> $(basename "$thumbnail_path")"

    # Generate compressed thumbnail with ImageMagick
    if convert "$file[0]" -resize 1200x\> -quality 85 -strip -interlace Plane "$thumbnail_path" 2>/dev/null; then
        echo "✓ Generated thumbnail: $thumbnail_path"
    elif convert "$file" -resize 1200x\> -quality 85 -strip -interlace Plane "$thumbnail_path" 2>/dev/null; then
        echo "✓ Generated thumbnail (fallback): $thumbnail_path"
    else
        echo "✗ Failed to process $file"
        echo "  File info: $(file "$file" 2>/dev/null || echo "unknown")"
        echo "  File size: $(ls -lh "$file" | awk '{print $5}')"
    fi
done

# Cleanup
rm -rf "$TEMP_DIR"

echo ""
echo "=== Thumbnail generation complete ==="
echo "Thumbnails saved to: $THUMBNAILS_DIR"
echo "Total thumbnails: $(find "$THUMBNAILS_DIR" -name "*.png" | wc -l)"

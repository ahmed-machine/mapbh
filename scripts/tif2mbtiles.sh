#!/bin/bash
set -e
INPUT=$1
OUTPUT=$2

gdal_translate "$INPUT" "$OUTPUT" -if GTiff -of MBTILES -co TILE_FORMAT=PNG -co ZOOM_LEVEL_STRATEGY=UPPER -co QUALITY=100 -co RESAMPLING=LANCZOS -scale 0 255 -a_nodata 0.0
gdaladdo -oo QUALITY=100 -oo ZOOM_LEVEL_AUTO=YES "$OUTPUT" 2 4 8 16 32 64

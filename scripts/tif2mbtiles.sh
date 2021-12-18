#!/bin/bash
set -e
INPUT=$1
OUTPUT=$2

gdal_translate "$INPUT" "$OUTPUT" -if GTiff -of MBTILES
gdaladdo -r cubic "$OUTPUT" 2 4 8 16 32 64 128 256 512 1024

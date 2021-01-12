#!/bin/bash
set -e
INPUT=$1
OUTPUT=$2

gdal_translate "$INPUT" "$OUTPUT" -of MBTILES
gdaladdo -r average "$OUTPUT" 2 4 8 16 32

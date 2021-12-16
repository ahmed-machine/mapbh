#!/bin/bash

# Converts all tifs to mbtiles and saves us the bandwidth

declare -a arr=($(ls ~/Code/openmaps/public/maps/*.tif))
## now loop through the above array
for i in "${arr[@]}"
do
   echo "$i ${i%.*}.mbtiles"
   # or do whatever with individual element of the array
   f=$(basename $i)
   ./scripts/tif2mbtiles.sh $i "${i%.*}.mbtiles"
done

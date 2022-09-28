import csv
import os

## Translates a set of sheets in directory

## Inputs:
## * Expects a CSV named 'coords.csv' consisting of:
##    sheet #, top left easting, top left northing, bottom right easting, bottom right northing

## * File path expected: <sheet number>.tif

## Outputs:
## * Set of files named <sheet number>w.tif warped in Web Mercator

## Source projection: UTM Zone 39, Ain Al Abed
source_projection = "'+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs'"

## Target: Web Mercator
target_projection = "'+proj=webmerc'"

## GDAL commands, install separately
translate_cmd = "gdal_translate -a_srs " + source_projection + " -co COMPRESS=LZW -co PREDICTOR=2 -co TILED=YES -a_ullr"
warp_cmd = "gdalwarp -s_srs " + source_projection + " -t_srs " + target_projection + " -r 'sum' -co 'COMPRESS=LZW' -co 'PREDICTOR=2'"

sheets = {}
with open('coords.csv') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',', quotechar='|')
    for row in spamreader:
        sheets[row[0]] = {'tle': row[1], 'tln': row[2],
                          'bre': row[3], 'brn': row[4]}

for k, sheet in sheets.items():
    if os.path.isfile(k + ".tif"):
        print("Sheet " + k)
        os.system(' '.join([translate_cmd, sheet['tle'], sheet['tln'], sheet['bre'], sheet['brn'], k + ".tif", k + "t.tif"]))
        if os.path.isfile(k + "w.tif"):
            os.remove(k + "w.tif")
        os.system(' '.join([warp_cmd, k + "t.tif", k + "w.tif"]))
        if os.path.isfile(k + "t.tif"):
            os.remove(k + "t.tif")

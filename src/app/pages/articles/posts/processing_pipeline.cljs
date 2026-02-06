(ns app.pages.articles.posts.processing-pipeline
  (:require [re-frame.core :as rf]))

(defn en []
  [:div.container.about
   {}
   [:h1.title {} "how to georeference a historical map"]
   [:div.content
    [:p
     {}
     "This guide walks through the complete process of taking a historical map — often a scanned image from a library or archive — and transforming it into a georeferenced, web-ready overlay that can be compared interactively with modern satellite imagery. It assumes comfort with the command line but no prior experience with geospatial tools." "The techniques described here were developed while building " [:a {:href "https://www.mapbh.org"} "mapBH"] ". Every map on the site passed through some version of this pipeline."]
    [:div {:style {:display :flex :justify-content :center :gap "1rem" :margin "1.5rem 0"}}
     [:img {:src "https://cdn.mapbh.org/thumbnails/1976.50k.Bahrain%20Geomorphology%20and%20superficial%20materials-source-scan.png"
            :alt "1976 Bahrain Geomorphology and Superficial Materials"
            :style {:max-height "600px" :object-fit :contain}}]
     [:img {:src "https://cdn.mapbh.org/thumbnails/1976.50k.Bahrain%20Geology-source-scan.png"
            :alt "1976 Bahrain Geology"
            :style {:max-height "600px" :object-fit :contain}}]]

    [:h2 {} "Prerequisites"]
    [:p {} "You will need the following tools installed:"]
    [:ul
     [:li [:a {:href "https://gdal.org/"} [:strong "GDAL"]] " (" [:code "gdal_translate"] ", " [:code "gdalwarp"] ", " [:code "gdaladdo"] ", " [:code "gdalinfo"] ", " [:code "gdal_merge.py"] ") — the Swiss army knife of geospatial processing"]
     [:li [:a {:href "https://imagemagick.org/"} [:strong "ImageMagick"]] " (" [:code "magick"] ") — for image format conversion"]
     [:li [:a {:href "https://qgis.org/download/"} [:strong "QGIS"]] " (optional) — a free desktop GIS application, useful for visual verification. Install the " [:em "Raster Freehand Georeferencer"] " plugin for manual alignment."]
     [:li [:a {:href "https://github.com/lovasoa/dezoomify-rs"} [:strong "dezoomify-rs"]] " (optional) — for downloading high-resolution images from tiled image servers (museum archives, Qatar Digital Library, etc.)"]
     [:li [:a {:href "https://www.gimp.org/"} [:strong "An image editor"]] " (optional) — Photoshop, GIMP, or similar, for cleaning up scans"]]

    [:h2 {} "Processing Pipeline"]
    [:p {} "The pipeline has nine steps. Not every map requires all of them, but most do."]

    [:h3 {} [:strong "Step 1: "] [:span {:style {:font-weight :normal}} "get the map"]]
    [:div {:style {:display :flex :justify-content :center :margin "1.5rem 0"}}
     [:img {:src "https://cdn.mapbh.org/thumbnails/1977_ManamaMuharraq_USSRMap.png"
            :alt "1977 USSR Military Map of Manama and Muharraq"
            :style {:max-height "600px" :object-fit :contain}}]]
    [:p
     {}
     "Maps have a certain way of showing up in your life. When I launched mapBH, I only had six maps on hand. Today, I have hundreds of maps. I've found some online, I've found some on dusty library shelves. I've found them in Canada, I've found them in France. Many have come from you, the reader, who have gracefully shared your scans or documents with me. If the data exists digitally somewhere, there's a method for you to scrape it."]
    [:p
     {}
     "Scan or download the highest resolution version of the map available. Resolution matters — a low-res scan will produce blurry tiles at higher zoom levels. We can't change a printed map's scale, but we can make sure it's clear at the scale it's available in."]
    [:p
     {}
     "For sources that serve images as tiles (common with digital archives like the Qatar Digital Library), use " [:code "dezoomify-rs"] " to reconstruct the full image:"]
    [:pre [:code "dezoomify-rs \"<tile-url>\" output.jpg"]]
    [:p {} "The tool's README covers usage in detail."]

    [:h3 {} [:strong "Step 2a: "] [:span {:style {:font-weight :normal}} "extract geographic metadata"]]
    [:p
     {}
     "Most maps of Bahrain in the 20th century are reasonably accurate and provide sufficient projection information for us to automate the process and minimise inaccuracies. Examine the map's margins carefully, most include:"]
    [:div {:style {:display :flex :justify-content :center :gap "1rem" :margin "1.5rem 0" :flex-wrap :wrap}}
     [:img {:src "/img/posts/processing-pipeline/map-projection-info.png"
            :alt "Map margin showing projection information: Mecca-Muscat Zone, Conical Orthomorphic, Spheroid Clarke 1880"
            :style {:max-height "200px" :object-fit :contain}}]
     [:img {:src "/img/posts/processing-pipeline/map-corner-top-left.png"
            :alt "Top-left corner of a map showing UTM coordinates: 452000E, 2908000N"
            :style {:max-height "200px" :object-fit :contain}}]
     [:img {:src "/img/posts/processing-pipeline/map-corner-bottom-right.png"
            :alt "Bottom-right corner of a map showing UTM coordinates: 468000E, 2893000N"
            :style {:max-height "200px" :object-fit :contain}}]]
    [:ul
     [:li [:strong "Corner coordinates"] " — latitude/longitude or UTM easting/northing for the map boundaries"]
     [:li [:strong "Projection information"] " — the coordinate reference system (CRS) the map was drawn in (e.g. Universal Transverse Mercator Zone 39)"]
     [:li [:strong "Datum"] " — the geodetic datum (e.g., Ain Al Abed, Nahrawan)"]
     [:li [:strong "Spheroid"] " — Clarke 1880 spheroid, Universal, etc"]
     [:li [:strong "Scale"] " — useful for sanity-checking your geo-referenced output"]]
    [:p
     {}
     "Record all of this. If coordinates are in UTM format (easting/northing in meters), you will feed them directly to " [:code "gdal_translate"] " later. If they are in degrees/minutes/seconds, convert them to decimal degrees first. There are "
     [:a {:href "https://www.latlong.net/degrees-minutes-seconds-to-decimal-degrees"} "online tools"]
     " to help with this."]
    [:p
     {}
     "If no geographic information is provided at all, you can still georeference the map by setting ground control points (GCPs) on landmarks against a modern base-map in software like QGIS or "
     [:a {:href "http://mapwarper.net"} "MapWarper"]
     " — though the results may be significantly less precise. (see step 2b)"]

    [:h3 {} [:strong "Step 2b: "] [:span {:style {:font-weight :normal}} "straight to QGIS's georeferencer"]]
    [:p
     {}
     "With some older maps, projection information is just not available or lost to history or the inaccuracies in the actual map are too great to overcome. These maps can't be transformed programmatically, and we must attempt to georectify them manually. Try "
     [:a {:href "https://qgis.org/download/"} "QGIS's"]
     " Georeferencer tool to set ground control points (GCPs) between known landmarks on the historical map and a modern basemap. "
     [:a {:href "http://mapwarper.net"} "MapWarper"]
     " can also serve as a tool to do the same and serve as a temporary tile server for testing. "
     [:i "Note: exhaust all attempts to do this programmatically (step 2a, 3-8) for maximum precision. Manually georeferenced maps are less accurate."]]

    [:h3 {} [:strong "Step 3: "] [:span {:style {:font-weight :normal}} "prepare the image"]]
    [:p
     {}
     "I won't dive too deeply here, but Photoshop skills are very useful in salvaging poor scans. Distorted images, overexposed, and unclear images can often be cleaned up with some careful masking, curve manipulation, and colour profiles."]
    [:p {} "Before georeferencing, clean up the scan:"]
    [:ul
     [:li [:strong "Contrast and levels"] " — if the image is overexposed or washed out, adjust levels."]
     [:li [:strong "Rotation"] " — straighten the image if the scan is tilted. I like to use the 'Straighten' tool in the Photoshop crop toolbox menu or Perspective Warp."]
     [:li [:strong "Border removal"] " — crop off margins, borders, and non-map content. This is important because " [:code "gdal_translate"] " assigns coordinates to the " [:em "corners of the image file itself"] ". If there is a wide border, GDAL will place the border area where the map edge should be, causing misalignment. There are ways to assign coordinates to a pixel (x,y) rather than the corner, but that's left as an exercise for the reader."]]
    [:p
     {}
     [:strong "Honourable mention: "]
     "Photoshop's photomerge automation (" [:code "File > Automate > Photomerge"] ") has saved me a tonne of time in spots where I didn't have access to large format scanners and had to manually stitch several images/scans together."]

    [:h3 {} [:strong "Step 4: "] [:span {:style {:font-weight :normal}} "convert to TIFF"]]
    [:p {} "GDAL works best with TIFF files. Convert your cleaned image:"]
    [:pre [:code "magick input.jpg output.tif"]]
    [:p {} "There are other tools, but this is the fastest."]

    [:h3 {} [:strong "Step 5: "] [:span {:style {:font-weight :normal}} "assign geographic data"]]
    [:p
     {}
     "Now we tell GDAL what projection the map is in and where its corners fall geographically. This is done with " [:code "gdal_translate"] " using two key options:"]
    [:ul
     [:li [:code "-a_srs"] " — assigns the source coordinate reference system"]
     [:li [:code "-a_ullr"] " — assigns the geographic bounding box as: " [:code "<upper-left-X> <upper-left-Y> <lower-right-X> <lower-right-Y>"]]]
    [:p {} [:strong "For maps in latitude/longitude (EPSG:4326):"]]
    [:pre [:code "gdal_translate -a_srs EPSG:4326 \\\n  -a_ullr <top-left-long> <top-left-lat> <bottom-right-long> <bottom-right-lat> \\\n  ./input.tif ./georeferenced.tif"]]
    [:p {} [:strong "For maps in UTM coordinates:"]]
    [:p
     {}
     "The " [:code "-a_ullr"] " values are easting/northing in meters, and " [:code "-a_srs"] " is the PROJ.4 string for that UTM zone and datum:"]
    [:pre [:code "gdal_translate \\\n  -a_srs '+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs' \\\n  -a_ullr <top-left-easting> <top-left-northing> <bottom-right-easting> <bottom-right-northing> \\\n  ./input.tif ./georeferenced.tif"]]
    [:p
     {}
     "After this step, opening the file in QGIS or any GIS software should place it in roughly the correct location — but still in its original projection."]

    [:h3 {} [:strong "Step 6: "] [:span {:style {:font-weight :normal}} "warp to web mercator"]]
    [:p
     {}
     "Maps on the web often use the Web Mercator projection (EPSG:3857). We need to re-project our georeferenced map to match. "
     "This is where the math matters: if the source map uses a Transverse Mercator projection (common for UTM-based maps), it uses ellipsoidal formulas, while Web Mercator uses spherical formulas. On small-scale maps (country or region level) this difference can be negligible, but on large-scale maps (detailed city plans) you may notice slight distortions — particularly a subtle horizontal stretch — as the projection math reconciles the two models and datums (or lack of)."]

    [:pre [:code "gdalwarp \\\n  -s_srs '+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs' \\\n  -t_srs EPSG:3857 \\\n  -r lanczos \\\n  ./georeferenced.tif ./warped.tif"]]
    [:p {} "Options:"]
    [:ul
     [:li [:code "-s_srs"] " — source projection (only needed if not already embedded in the file from Step 5, or if you want to override it)"]
     [:li [:code "-t_srs"] " — target projection (Web Mercator)"]
     [:li [:code "-r lanczos"] " — resampling algorithm. The default (" [:code "nearest_neighbor"] ") is fast but produces blocky results. " [:code "cubicspline"] " is slower but yields smoother output, and " [:code "lanczos"] " is generally best."]]

    [:h3 {} [:strong "Step 7: "] [:span {:style {:font-weight :normal}} "verify alignment"]]
    [:p
     {}
     "Load the warped TIFF in QGIS alongside satellite imagery and check whether coastlines, roads, and landmarks line up."]
    [:ul
     [:li [:strong "If it is correctly positioned"] " — you are done with georeferencing."]
     [:li [:strong "If it is slightly offset"] " — use the " [:em "Raster Freehand Georeferencer"] " plugin in QGIS to nudge it into place, then export as GeoTIFF. Alternatively, open the TIFF in Photoshop and use transform tools for precise adjustments."]
     [:li [:strong "If it needs significant warping or distortion"] " — something may be wrong with your coordinate or projection assumptions. Particularly at higher scale ratios (i.e. 1:10,000 and higher), mistakes show up more drastically. Examine your original geographic metadata for accuracy and the source image for any visible distortion (sometimes map scans aren't flat and need to be manually corrected in post)"]]

    [:h3 {} [:strong "Step 8: "] [:span {:style {:font-weight :normal}} "(optional) stitch multi-sheet maps"]]
    [:p
     {}
     "Many historical map sets cover a region across multiple sheets. If you have georeferenced each sheet individually, you can merge them into a single file:"]
    [:pre [:code "gdal_merge.py -co BIGTIFF=YES -co COMPRESS=LZW -n 0.0 -o merged_output.tif sheet1.tif sheet2.tif sheet3.tif"]]
    [:ul
     [:li [:code "-co BIGTIFF=YES"] " — enables files larger than 4 GB"]
     [:li [:code "-co COMPRESS=LZW"] " — lossless compression to manage file size"]
     [:li [:code "-n 0.0"] " — treats pure black (0,0,0) as nodata/transparent, which helps when sheets have black borders"]]

    [:h3 {} [:strong "Step 9: "] [:span {:style {:font-weight :normal}} "convert for tile serving"]]
    [:p
     {}
     "The final georeferenced TIFF needs to be converted into a format suitable for a tile server. MBTiles is a common choice:"]
    [:pre [:code "gdal_translate input.tif output.mbtiles -of MBTILES\ngdaladdo -r average output.mbtiles 2 4 8 16 32"]]
    [:p
     {}
     [:code "gdaladdo"] " generates overview levels (zoom-out tiles) so the tile server does not have to serve the full-resolution image at every zoom level."]

    [:h2 {} "It didn't work?!"]
    [:p
     {}
     "The fact we can programmatically transform maps from 100 years ago and accurately compare them with modern satellite imagery is a miracle. I've found that no two maps are the same. Each era of cartography introduces variations, each cartographer introduces their own touch, and there's an art to transforming maps. Some maps require a few takes and research (it took me over a year to land the USSR military maps transformations satisfactorily) so don't lose heart. There's a lot of resources available and very helpful people around if you ask. When in doubt, QGIS's Georeferencer (step 2b) works in a pinch but it's not as accurate as the programmatic transformation (steps 2a, 3-8). Learning is a process, struggle through it."]

    [:h2 {} "Visualisation"]
    [:p
     {}
     "Both GeoTIFFs and MBTiles can be displayed using a variety of software. Software such as QGIS can display both, and MBTiles can be served over the web with mapping libraries such as "
     [:a {:href "https://leafletjs.com/"} "leaflet"]
     ". mapBH self-hosts our own tile server and displays it with leaflet on a clojure/script stack (see "
     [:a {:href "https://github.com/ahmed-machine/mapbh"} "here"]
     "). Generally, MBTiles are optimised to reduce bandwidth and rendering costs and GeoTIFF are useful for manipulation in GIS software."]
    [:p
     {}
     "For brevity, I encourage you to check out the " [:a {:href "https://github.com/ahmed-machine/mapbh"} "mapBH code repository"] " or this "
     [:a {:href "https://kokoalberti.com/articles/georeferencing-and-digitizing-old-maps-with-gdal/"} "article"]
     " which takes a similar approach."]

    [:h2 {} [:strong "Worked Example: "] [:span {:style {:font-weight :normal}} "UTM Zone 39 (Ain El Abd Datum)"]]
    [:p
     {}
     "This example walks through processing a map of Manama from the 1977 Bahrain 1:25,000 series, which uses a UTM Zone 39 projection with the Ain El Abd datum."]
    [:figure.image {:style {:text-align :center}}
     [:img {:src "https://cdn.mapbh.org/thumbnails/1977.Manama.1.original.png"
            :alt "1977 Manama 1:25,000 source map"}]]

    [:p {} [:strong "1. Acquire and prepare the image"]]
    [:p
     {}
     "Downloaded the source image. From the map's margins, noted the projection (UTM Zone 39, Ain Al Abed) and the corner coordinates in easting/northing. In Photoshop: removed wide margins, cropped to the map content, rotated until level, and applied a levels adjustment to correct overexposure. Converted to TIFF:"]
    [:pre [:code "magick manama_scan.jpg manamatest.tif"]]

    [:p {} [:strong "2. Assign geographic metadata"]]
    [:p
     {}
     "We have a UTM Zone 39 projection with the International 1924 ellipsoid and a datum shift to WGS84. We look up the "
     [:a {:href "https://epsg.io/20439"} "PROJ.4 definition"]
     " for EPSG:20439 and use it as our source CRS. The " [:code "-a_ullr"] " values are the easting/northing of the map corners: top-left easting, top-left northing, bottom-right easting, bottom-right northing."]
    [:pre [:code "gdal_translate \\\n  -a_srs '+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs' \\\n  -a_ullr 452000 2908000 468000 2893000 \\\n  manamatest.tif manamatranslated.tif"]]
    [:p
     {}
     "Opening " [:code "manamatranslated.tif"] " in QGIS confirms it lands in the correct general area of Bahrain — but it is still in the UTM projection."]

    [:p {} [:strong "3. Warp to Web Mercator"]]
    [:p
     {}
     "We reproject to Web Mercator. We specify the source projection explicitly with " [:code "-s_srs"] " (matching the PROJ.4 string from above) and the target as Web Mercator. We use " [:code "cubicspline"] " resampling for quality."]
    [:pre [:code "gdalwarp \\\n  -s_srs '+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs' \\\n  -t_srs '+proj=webmerc' \\\n  -r cubicspline \\\n  manamatranslated.tif final_manama.tif"]]

    [:p {} [:strong "4. Result"]]
    [:p
     {}
     "The output file " [:code "final_manama.tif"] " is correctly positioned and projected. It can be loaded in any GIS software, converted to MBTiles for a tile server, or explored locally."]
    [:figure.image {:style {:text-align :center}}
     [:img {:src "/img/posts/processing-pipeline/manama-1977-result.png"
            :alt "1977 Manama georeferenced result overlaid on satellite imagery"}]]

    [:h2 {} "The world is your oyster"]
    [:p
     {}
     "This isn't an exhaustive guide, but a starting point. These concepts and tools generalise to several applications which I encourage you to explore. Tile servers work in a similar manner to large document viewers. Weather systems utilise similar GDAL workflows. If you're inclined, there's a few maps in mapBH's "
     [:a {:href "https://mapbh.org/en/catalogue"} "catalogue"]
     " that still need transformation. mapBH's community would be immensely grateful."]
    [:p {} "I hope you enjoyed this guide, and I'm excited to see what you make with this."]
    [:hr]
    [:h2 {} "Useful Links"]
    [:ul
     [:li [:a {:href "http://epsg.io"} [:strong "EPSG.io"]] " — look up PROJ.4 definitions and CRS parameters by EPSG code"]
     [:li [:strong "UTM to Lat/Long converter"] " — verify that your easting/northing values land where you expect on a map: "
      [:a {:href "https://www.engineeringtoolbox.com/utm-latitude-longitude-d_1370.html"} "Engineering Toolbox UTM Converter"]]
     [:li [:strong "Coordinate converter (DMS to UTM, multiple datums)"] " — convert between degree/minute/second notation and easting/northing for different datums like Clarke 1880: "
      [:a {:href "http://rcn.montana.edu/Resources/Converter.aspx"} "Montana RCN Converter"]]
     [:li [:strong "UTM to Geographic coordinate converter"] " — another useful converter: "
      [:a {:href "https://franzpc.com/apps/coordinate-converter-utm-to-geographic-latitude-longitude.html"} "FranzPC Coordinate Converter"]]

     [:li [:a {:href "https://kokoalberti.com/articles/georeferencing-and-digitizing-old-maps-with-gdal/"} [:strong "Georeferencing and digitizing old maps with GDAL"]] " — basically this article, but slightly different."]]
    [:hr]
    [:h2 {} [:strong "Addendum A: "] [:span {:style {:font-weight :normal}} "processed map boundaries reference"]]
    [:p
     {}
     "This section collects the projection and geographic boundary data for common maps processed through the pipeline above for future reference. Each entry includes the PROJ.4 definition for the source projection and the " [:code "-a_ullr"] " values (easting/northing or longitude/latitude) used with " [:code "gdal_translate"] "."]

    [:h3 {} [:strong "1977 Bahrain 1:25,000 "] [:span {:style {:font-weight :normal}} "(UTM zone 39, Ain El Abd)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs"]]
    [:table.table
     [:thead
      [:tr
       [:th "Sheet"]
       [:th "Bounds (UL-easting UL-northing LR-easting LR-northing)"]]]
     [:tbody
      [:tr [:td "Manama"] [:td [:code "452000 2908000 468000 2893000"]]]
      [:tr [:td "Budaiya"] [:td [:code "436000 2908000 452000 2893000"]]]
      [:tr [:td "Mamtalah"] [:td [:code "436000 2878000 452000 2863000"]]]
      [:tr [:td "Riffa"] [:td [:code "452000 2893000 468000 2878000"]]]
      [:tr [:td "ArRumaythah"] [:td [:code "452000 2878000 468000 2863000"]]]
      [:tr [:td "Zallaq"] [:td [:code "436000 2893000 452000 2878000"]]]
      [:tr [:td "Ras Al Bar"] [:td [:code "452000 2863000 468000 2848000"]]]]]

    [:h3 {} [:strong "1969 Manama & Al Jufayr 1:5,000 "] [:span {:style {:font-weight :normal}} "(UTM zone 39, Ain El Abd)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs"]]
    [:table.table
     [:thead
      [:tr
       [:th "Sheet"]
       [:th "Bounds (UL-easting UL-northing LR-easting LR-northing)"]]]
     [:tbody
      [:tr [:td "1-1"] [:td [:code "456600 2901900 459800 2899500"]]]
      [:tr [:td "1-2"] [:td [:code "459800 2899500 462000 2897100"]]]
      [:tr [:td "1-3"] [:td [:code "456600 2899500 459800 2897100"]]]
      [:tr [:td "1-4"] [:td [:code "453000 2898900 456600 2896500"]]]
      [:tr [:td "1-5"] [:td [:code "453000 2901300 456600 2898900"]]]]]

    [:h3 {} [:strong "1973 Bahrain 1:50,000 "] [:span {:style {:font-weight :normal}} "(UTM zone 39, Nahrawan)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=clrk80 +towgs84=-242.2,-144.9,370.3,0,0,0,0 +units=m +no_defs"]]
    [:table.table
     [:thead
      [:tr
       [:th "Sheet"]
       [:th "Corner Coordinates (Deg)"]
       [:th "Bounds (Easting/Northing)"]]]
     [:tbody
      [:tr [:td "North (1-1)"] [:td "50.383 26.3 → 50.683 26.033"] [:td [:code "438300.4 2908710 468316.7 2879179.2"]]]
      [:tr [:td "South (1-2)"] [:td "50.383 26.033 → 50.683 25.767"] [:td [:code "438300.4 2879286.5 468245.4 2849649.5"]]]]]

    [:h3 {} [:strong "1956 Bahrain 1:63,360 "] [:span {:style {:font-weight :normal}} "(UTM zone 39R, Clarke 1880)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=clrk80 +towgs84=-242.2,-144.9,370.3,0,0,0,0 +units=m +no_defs"]]
    [:ul
     [:li [:strong "Projection: "] "UTM Zone 39R, Spheroid: Clarke 1880, Origin: Long. 51°E, Lat. Equator"]
     [:li [:strong "Top-left: "] "50°22'E, 26°18'N (50.367, 26.3)"]
     [:li [:strong "Bottom-right: "] "50°46'E, 25°47'N (50.767, 25.783)"]
     [:li [:strong "Easting/Northing: "] [:code "436777 2908826 476605 2851478"]]]

    [:h3 {} [:strong "1962 Bahrain 1:63,360 "] [:span {:style {:font-weight :normal}} "(UTM zone 39R, Clarke 1880)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=clrk80 +towgs84=-242.2,-144.9,370.3,0,0,0,0 +units=m +no_defs"]]
    [:ul
     [:li [:strong "Projection: "] "UTM Zone 39R, Spheroid: Clarke 1880, Origin: Long. 51°E, Lat. Equator"]
     [:li [:strong "Top-left: "] "50°22'E, 26°18'N (50.367, 26.3)"]
     [:li [:strong "Bottom-right: "] "50°46'E, 25°47'N (50.767, 25.783)"]
     [:li [:strong "Easting/Northing: "] [:code "436777 2908826 476605 2851478"]]]

    [:h3 {} [:strong "1977 Bahrain 1:50,000 "] [:span {:style {:font-weight :normal}} "(UTM zone 39, Ain El Abd)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=intl +towgs84=-143,-236,7,0,0,0,0 +units=m +no_defs"]]
    [:table.table
     [:thead
      [:tr
       [:th "Sheet"]
       [:th "Corner Coordinates (Deg)"]
       [:th "Bounds (Easting/Northing)"]]]
     [:tbody
      [:tr [:td "1-1"] [:td "50.367 26.3 → 50.367 26.033"] [:td [:code "436776 2909102 468316 2879453"]]]
      [:tr [:td "1-2"] [:td "50.367 26.033 → 50.683 25.767"] [:td [:code "436633 2879295 468245 2849650"]]]
      [:tr [:td "1-3"] [:td "50.517 25.85 → 50.833 25.533"] [:td [:code "451566 2858928 483255 2823785"]]]]]

    [:h3 {} [:strong "1968 Al Jufayr "] [:span {:style {:font-weight :normal}} "(UTM zone 39, Nahrawan / Clarke 1880)"]]
    [:p {} "PROJ.4: " [:code "+proj=utm +zone=39 +ellps=clrk80 +towgs84=-242.2,-144.9,370.3,0,0,0,0 +units=m +no_defs"]]
    [:table.table
     [:thead
      [:tr
       [:th "Sheet"]
       [:th "Corner Coordinates (DMS)"]
       [:th "Bounds (Easting/Northing)"]]]
     [:tbody
      [:tr [:td "Sheet 2"] [:td "26°13'03.26\"N 50°33'42.18\"E → 26°11'39.35\"N 50°35'26.82\"E"] [:td [:code "456217.2 2899617.2 459112.8 2897026.5"]]]
      [:tr [:td "Sheet 1"] [:td "26°13'03.56\"N 50°35'26.53\"E → 26°11'39.64\"N 50°37'11.15\"E"] [:td [:code "459112.9 2899616.9 462008.4 2897026.6"]]]]]

    [:hr]

    [:h2 {} [:strong "Addendum B: "] [:span {:style {:font-weight :normal}} "Bahrain government spatial reference"]]
    [:p
     {}
     "The spatial reference system used by Bahrain government maps, provided here for reference when working with official cartographic data."]

    [:h3 {} [:strong "Spatial Reference "] [:span {:style {:font-weight :normal}} "(WKT)"]]
    [:pre [:code "PROJCS[\"UTM_Zone_39_Northern_Hemisphere\",\n  GEOGCS[\"GCS_Ain_el_Abd_1970\",\n    DATUM[\"D_Ain_el_Abd_1970\",\n      SPHEROID[\"International_1924\", 6378388.0, 297.0]],\n    PRIMEM[\"Greenwich\", 0.0],\n    UNIT[\"Degree\", 0.0174532925199433]],\n  PROJECTION[\"Transverse_Mercator\"],\n  PARAMETER[\"false_easting\", 500000.0],\n  PARAMETER[\"false_northing\", 0.0],\n  PARAMETER[\"central_meridian\", 51.0],\n  PARAMETER[\"scale_factor\", 0.9996],\n  PARAMETER[\"latitude_of_origin\", 0.0],\n  UNIT[\"Meter\", 1.0]]"]]

    [:h3 {} "Initial Extent"]
    [:ul
     [:li [:strong "XMin: "] "398065.274"]
     [:li [:strong "YMin: "] "2878749.950"]
     [:li [:strong "XMax: "] "501533.305"]
     [:li [:strong "YMax: "] "2915811.925"]]

    [:h3 {} "Full Extent"]
    [:ul
     [:li [:strong "XMin: "] "437103.333"]
     [:li [:strong "YMin: "] "2837185.795"]
     [:li [:strong "XMax: "] "482677.953"]
     [:li [:strong "YMax: "] "2911494.158"]]]])

(defn ar [] [en])

(defn article
  []
  (let [language* (rf/subscribe [:app.model/language])]
    (fn []
      (let [language @language*]
        (if (= language :ar) [ar] [en])))))

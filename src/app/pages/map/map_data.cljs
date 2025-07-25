(ns app.pages.map.map-data)

;;;;;;;;;;;;;;;;;;;;;;;
;; Map data and opts ;;
;;;;;;;;;;;;;;;;;;;;;;;

(def base-opts {:attribution "© OpenStreetMap, Mapbox"
                :maxZoom 20
                :minZoom 9
                :zoomOffset -1
                :detectRetina true
                :hq true
                :id "Standard Style"
                :tileSize 512})

(def tileserver-url #_"http://localhost:8080" "https://map.mapbh.org")
(defn form-tile-url [tile-name] (str tileserver-url "/data/" tile-name "/{z}/{x}/{y}.png"))

(def access-tokens ["pk.eyJ1IjoiYWhtZWRhbG11dGF3YSIsImEiOiJja2s1a3JqdWMwbHJiMnVzM2U5bHZleGNwIn0.icIlmsN_TY4zztxqC__e8Q"
                    "pk.eyJ1IjoidG9sb2NvcmUiLCJhIjoiY2trNWtuamxjMDY3ZzJubnhybjlscXRyNCJ9.ug82VbeEK-sPDD_YgJLBLw"])

(def map-id "satellite-streets-v11")

(def base-satellite {"Terrain" {:url "https://a.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                :opts (assoc base-opts :zoomOffset -1)}
                     "Satellite" {:url (str "https://api.mapbox.com/styles/v1/mapbox/" map-id "/tiles/{z}/{x}/{y}?access_token=" (rand-nth access-tokens))
                                  :opts (assoc base-opts :zoomOffset -1)}})

;; Default values for map state used in URL parameters and initialization
(def default-map-state
  {:map-id "1956 - Bahrain"
   :group "Bahrain"
   :lat 26.045
   :lng 50.54603
   :zoom 10.3
   :mode "transparency"
   :base "Satellite"
   :transparency 0.65})

(def other-layers
  {"1963 - Awali" {:title "Awali"
                   :url (form-tile-url "1963-Awali")
                   :description "Prepared from existing mapping dated 1953. Revised from Air Photography dated 1956 by 42 Survey Engineer Regiment January 1957. Edition 2 revised from by 42 Survey Engineer Regiment and additional street names supplied by 19 Topographic Squadron RE. Edition 2 GSGS 1963. Published by D. Survey, War Office and Air Ministry, 1963."
                   :notes "Well-preserved scan, some drift towards the edges. Incomplete grid data meant transformation is based on estimated coordinates and datums, hand-referenced after. Likely some errors."
                   :issuer-link "/maps/1963.5000.Awali.tif"
                   :source "42 Survey Engineer Regiment"
                   :source-link "/maps/1963.5000.Awali.warped.tif"
                   :issuer "Army Map Service, Corps of Engineers"
                   :scale "1:5,000"
                   :opts (merge base-opts {:minNativeZoom 13
                                           :maxNativeZoom 19
                                           :opacity 0.7})}})

(def manama-layers
  {"1937 - Manama" {:title "PLAN OF PORT AND TOWN - Manama 1937"
                    :url (form-tile-url "1937-Manama")
                    :description "Plan of Manamah, Bahrain. The plan indicates settlements, streets, buildings and landmarks. Prepared by naval staff in the Admiralty's Naval Intelligence Division."
                    :notes "Some map drift along the southern and western ends of the document. Mostly accurate."
                    :source-link "/maps/1937-Manama.tif"
                    :source "British Library: India Office Records and Private Papers"
                    :scale "1:3,639"
                    :issuer "Qatar Digital Library"
                    :issuer-link "https://www.qdl.qa/en/archive/81055/vdc_100043097172.0x000079"
                    :opts (merge base-opts {:minNativeZoom 13
                                            :maxNativeZoom 19
                                            :opacity 0.7})}
   "1957 - Manama" {:title "Manama"
                    :url (form-tile-url "1957-Manama")
                    :description "Prepared from existing mapping dated 1953. Revised from Air Photography dated 1956 by 42 Survey Engineer Regiment January 1957. Series GSGS 4880 Edition 1 GSGS 1957. Published by D. Survey, War Office and Air Ministry, 1957."
                    :notes "Well-preserved scan, some drift towards the edges. Incomplete grid data meant transformation is based on estimated coordinates and datums, hand-referenced after. Likely some errors."
                    :issuer-link "/maps/1957.5000.Manama.tif"
                    :source "42 Survey Engineer Regiment"
                    :source-link "/maps/1957.5000.Manama.warped.tif"
                    :issuer "D. Survey War Office and Air Ministry, 1957"
                    :scale "1:5,000"
                    :opts (merge base-opts {:minNativeZoom 13
                                            :maxNativeZoom 19
                                            :opacity 0.7})}
   "1968 - Al Jufayr" {:title "Al Jufayr"
                       :url (form-tile-url "1968-AlJufayr")
                       :description "Produced by SPC RE in 1968 from existing mapping and other sources with full revision from RAF Air Photography dated 1966."
                       :notes "Well-preserved."
                       :issuer-link "/maps/1968.Al Jufayr.5000.zip"
                       :source "SPC RE"
                       :source-link "/maps/1968.Al Jufayr.5000.tiff"
                       :issuer "D Survey, Ministry of Defence, United Kingdom, 1968"
                       :scale "1:5,000"
                       :opts (merge base-opts {:minNativeZoom 13
                                               :maxNativeZoom 19
                                               :opacity 0.7})}
   "1969 - Manama & AlJufayr" {:title "AL MANAMAH & AL JUFAYR - 1969"
                               :url (form-tile-url "1969-Manama")
                               :description "Mosaic prepared by SPC RE in 1969 from air photography dated October 1968 and trig control. Names taken from K962 sheet Manama dated 1963. Names and cultural details raised from various sources 1969. This plan supersedes series K962 edit on 3.GSGS sheet Manama."
                               :notes "Excellent condition 5-part map series. Required color corrections, some warping, stitching, and coordinates were offset from real location. "
                               :source-link "/maps/1969-Manama-stitched.zip"
                               :source "Ministry of Defence, United Kingdom, 1969"
                               :scale "1:5,000"
                               :issuer "Historic Maps of Bahrain 1817 - 1970 - Jarman"
                               :issuer-link "/maps/1969-Manama.zip"
                               :submitted-by "Ali Karimi"
                               :opts (merge base-opts {:minNativeZoom 13
                                                       :maxNativeZoom 18
                                                       :opacity 0.7})}
   "1977 - USSR - Manama/Muharraq" {:title "Manama and Muharraq - 1977"
                                    :url (form-tile-url "1977-USSR-Bahrain")
                                    :description "Description in Russian can be found in the bottom-right corner of the map. Produced in 1977 and 1980, in a factory in Sverdlovsk (now Ekaterinburg). There are many exciting features in these maps that are sorely missing in other contemporary maps. NATO mappers intentionally obfuscated sensitive areas like airports and navy yards from their maps but the Soviets include these on theirs. To produce these details, the Soviets had to rely on ground intelligence to obtain both aerial and ground measurements. Discovered in second-hand thrift stores following the collapse of the Soviet Union."
                                    :notes "While we know this is a SK-42 projection (also known as Pulkovo) with a PZ-90 datum, tracking down the exact datum was difficult as few maps of this sort have been translated for our part of the world. In the end, we had to adjust it by hand using ground control points. Special shoutout to Tristan Bobin for his contributions in rectifying this map."
                                    :source-link "/maps/1977_ManamaMuharraq_USSRMap.tif"
                                    :source "USSR"
                                    :scale "1:10,000"
                                    :issuer "USSR"
                                    :issuer-link "/maps/1977 - USSR - Manama & Muharraq.zip"
                                    :opts (merge base-opts {:minNativeZoom 12
                                                            :maxNativeZoom 18
                                                            :opacity 0.8})}
   "1977 - Manama" {:title "Manama - State of Bahrain - 1977 (Edition 1)"
                    :url (form-tile-url "1977-Manama.1")
                    :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/1977-Manama.1.tif"
                    :source "Fairey Surveys Ltd"
                    :scale "1:25,000"
                    :issuer "Unknown"
                    :issuer-link "/maps/1977.Manama.1.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}
   "1992 - Manama" {:title "Manama - State of Bahrain - 1992 (Edition 4)"
                    :url (form-tile-url "1992-Manama.4")
                    :description "Original photogrammetric compilation by Fairey Surveys Ltd 1977. Subsequent revision by Survey Directorate, Ministry of Housing, State of Bahrain from Photography dated Feb. 1980, Jan. 1983, and Oct. 1986. Field surveys from 1986 to 1991."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/1992-Manama.4.tif"
                    :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                    :scale "1:25,000"
                    :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                    :issuer-link "/maps/1992.Manama.4.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}
   "1998 - Manama" {:title "Manama - State of Bahrain - 1998 (Edition 5)"
                    :url (form-tile-url "1998-Manama.5")
                    :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1985, 1988, and 1992 from air photography and field surveys by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 1998 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/1998-Manama.5.tif"
                    :source "Ministry of Housing, Municipalities and Environment, State of Bahrain."
                    :scale "1:25,000"
                    :issuer "Ministry of Housing, Municipalities and Environment, State of Bahrain."
                    :issuer-link "/maps/1998.Manama.5.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}
   "2006 - Manama" {:title "Manama - State of Bahrain - 2006 (Edition 6)"
                    :url (form-tile-url "2006-Manama.6")
                    :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1985, 1988, 1992, 1998 and 2006 from air photography and field surveys by the Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/2006-Manama.6.tif"
                    :source "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                    :scale "1:25,000"
                    :issuer "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                    :issuer-link "/maps/2006.Manama.6.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}})
(def muharraq-layers
  {"1931 - Muharraq" {:title "MOHARRAK TOWN - March 1931"
                      :url (form-tile-url "1931-Muharraq")
                      :description nil
                      :notes "Accurate in Old Muharraq. Drifts in the southern part of the map near Bu Maher."
                      :issuer-link "/maps/1931-Muharraq.jpg"
                      :source "Unknown"
                      :source-link "/maps/1931-Muharraq.tif"
                      :submitted-by "Ali Karimi"
                      :scale "1:2,400"
                      :issuer "Historic Maps of Bahrain 1817 - 1970 - Jarman"
                      :opts (merge base-opts {:minNativeZoom 13
                                              :maxNativeZoom 19
                                              :opacity 0.7})}
   "1937 - Muharraq" {:title "MUHARRAQ ISLAND - Jan 1937"
                      :url (form-tile-url "1937-Muharraq")
                      :description "The map shows Muhurraq Island, and the causeway linking Muharraq to Manama. Also shown are principal roads, tracks, buildings (including the Political Agency in Manana), islands, and the location of several underwater freshwater springs. A rectangle on Muharraq Island indicates the area specified for the landing ground."
                      :notes "Well-preserved and decently accurate."
                      :source-link "/maps/1937-Muharraq.tif"
                      :source "British Library: India Office Records and Private Papers"
                      :scale "1:20,000"
                      :issuer "Qatar Digital Library"
                      :issuer-link "https://www.qdl.qa/en/archive/81055/vdc_100025608108.0x000080"
                      :opts (merge base-opts {:maxZoom 20
                                              :minZoom 9
                                              :minNativeZoom 13
                                              :maxNativeZoom 16
                                              :opacity 0.7})}
   "1957 - Muharraq" {:title "Muharraq"
                      :url (form-tile-url "1957-Muharraq")
                      :description "Prepared from existing mapping dated 1953. Revised from Air Photography dated 1956 by 42 Survey Engineer Regiment January 1957. Series GSGS 4880 Edition 1 GSGS 1957. Published by D. Survey, War Office and Air Ministry, 1957."
                      :notes "Well-preserved scan, some drift towards the edges. Incomplete grid data meant transformation is based on estimated coordinates and datums, hand-referenced after. Likely some errors."
                      :issuer-link "/maps/1957.5000.Muharraq2.tif"
                      :source "42 Survey Engineer Regiment"
                      :source-link "/maps/1957.5000.Manama.warped.tif"
                      :issuer "D. Survey War Office and Air Ministry, 1957"
                      :scale "1:5,000"
                      :opts (merge base-opts {:minNativeZoom 13
                                              :maxNativeZoom 19
                                              :opacity 0.7})}
   "1977 - USSR - Manama/Muharraq" {:title "Manama and Muharraq - 1977"
                                    :url (form-tile-url "1977-USSR-Bahrain")
                                    :description "Description in Russian can be found in the bottom-right corner of the map. Produced in 1977 and 1980, in a factory in Sverdlovsk (now Ekaterinburg). There are many exciting features in these maps that are sorely missing in other contemporary maps. NATO mappers intentionally obfuscated sensitive areas like airports and navy yards from their maps but the Soviets include these on theirs. To produce these details, the Soviets had to rely on ground intelligence to obtain both aerial and ground measurements. Discovered in second-hand thrift stores following the collapse of the Soviet Union."
                                    :notes "While we know this is a SK-42 projection (also known as Pulkovo) with a PZ-90 datum, tracking down the exact datum was difficult as few maps of this sort have been translated for our part of the world. In the end, we had to adjust it by hand using ground control points. Special shoutout to Tristan Bobin for his contributions in rectifying this map."
                                    :source-link "/maps/1977_ManamaMuharraq_USSRMap.tif"
                                    :source "USSR"
                                    :scale "1:10,000"
                                    :issuer "USSR"
                                    :issuer-link "/maps/1977 - USSR - Manama & Muharraq.zip"
                                    :opts (merge base-opts {:minNativeZoom 12
                                                            :maxNativeZoom 18
                                                            :opacity 0.8})}
   "1977 - Manama" {:title "Manama - State of Bahrain - 1977 (Edition 1)"
                    :url (form-tile-url "1977-Manama.1")
                    :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/1977-Manama.1.tif"
                    :source "Fairey Surveys Ltd"
                    :scale "1:25,000"
                    :issuer "Unknown"
                    :issuer-link "/maps/1977.Manama.1.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}
   "1992 - Manama" {:title "Manama - State of Bahrain - 1992 (Edition 4)"
                    :url (form-tile-url "1992-Manama.4")
                    :description "Original photogrammetric compilation by Fairey Surveys Ltd 1977. Subsequent revision by Survey Directorate, Ministry of Housing, State of Bahrain from Photography dated Feb. 1980, Jan. 1983, and Oct. 1986. Field surveys from 1986 to 1991."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/1992-Manama.4.tif"
                    :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                    :scale "1:25,000"
                    :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                    :issuer-link "/maps/1992.Manama.4.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}
   "1998 - Manama" {:title "Manama - State of Bahrain - 1998 (Edition 5)"
                    :url (form-tile-url "1998-Manama.5")
                    :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1985, 1988, and 1992 from air photography and field surveys by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 1998 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/1998-Manama.5.tif"
                    :source "Ministry of Housing, Municipalities and Environment, State of Bahrain."
                    :scale "1:25,000"
                    :issuer "Ministry of Housing, Municipalities and Environment, State of Bahrain."
                    :issuer-link "/maps/1998.Manama.5.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}
   "2006 - Manama" {:title "Manama - State of Bahrain - 2006 (Edition 6)"
                    :url (form-tile-url "2006-Manama.6")
                    :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1985, 1988, 1992, 1998 and 2006 from air photography and field surveys by the Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                    :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                    :source-link "/maps/2006-Manama.6.tif"
                    :source "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                    :scale "1:25,000"
                    :issuer "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                    :issuer-link "/maps/2006.Manama.6.original.tif"
                    :opts (merge base-opts {:minNativeZoom 11
                                            :maxNativeZoom 17
                                            :opacity 0.8})}})
(def bahrain-layers
  {"1914 - Bahrain Harbour" {:title "1914 - Bahrain Harbour"
                             :url (form-tile-url "1914-Harbour")
                             :description nil
                             :notes "Gets less accurate in the south."
                             :issuer-link "/maps/1914-Harbour.png"
                             :source "Marine Survey of India - Officers of R.I.M.S Investigator"
                             :source-link "/maps/1914-Harbour.tif"
                             :submitted-by "Ali Karimi"
                             :scale "1:73,000"
                             :issuer "Historic Maps of Bahrain 1817 - 1970 - Jarman"
                             :opts (merge base-opts {:minNativeZoom 10
                                                     :maxNativeZoom 15
                                                     :opacity 0.8})}
   "1937 - Bahrain" {;; English
                     :title "BAHRAIN ISLAND - 1937"
                     :url (form-tile-url "1937-Bahrain")
                     :description "Plan of Bahrain. The plan indicates hydrology, settlements and  as well as providing some indication of relief. Included is a table of reference of symbols used. Prepared by naval staff in the Admiralty's Naval Intelligence Division."
                     :notes "Original scan of document was distorted and outlines begin to diverge greatly further and further south.  This is one of my favorite maps because it shows the borders of shallow waters around Bahrain. Those borders now correspond almost exactly to the borders of reclaimed land around the island. It costs less to reclaim shallow water. "
                     :issuer-link "https://www.qdl.qa/en/archive/8955/vdc_90043097172.0x000077"
                     :source "British Library: India Office Records and Private Papers"
                     :source-link "/maps/1937-Bahrain.tif"
                     :issuer "Qatar Digital Library"
                     :scale "1:63,360"

                     :opts (merge base-opts {:minNativeZoom 11
                                             :maxNativeZoom 15
                                             :opacity 0.7})}
   "1943 - Bahrain" {:title "BAHRAIN ISLAND - 1943"
                     :url (form-tile-url "1943-Bahrain")
                     :description "Series GSGS 4035 Edition 2. Shows physical features, residential places, cultivated land, selected buildings, marsh land, and major roads.
  Relief shown by landform drawings, contours, and spot heights. Compiled from Air photographs in A.H.Q. Drawing Office, Hinaidi, 1937. Drawn and Heliographed by O.S. 1939."
                     :notes "Well-preserved, undistorted scan. Map has relatively few inaccuracies."
                     :issuer-link "https://legacy.lib.utexas.edu/maps/ams/bahrein_island/txu-pclmaps-oclc-6559195-bahrein-island.jpg"
                     :source "US Army Map Service"
                     :source-link "/maps/1943-Bahrain.tif"
                     :issuer "University of Texas Library"
                     :scale "1:63,360"
                     :opts (merge base-opts {:minNativeZoom 10
                                             :maxNativeZoom 15
                                             :opacity 0.7})}
   "1956 - Bahrain" {:title "BAHRAIN ISLAND - 1956"
                     :url (form-tile-url "1956-Bahrain")
                     :description "Series GSGS 4035 Edition 4. Compiled, drawn, reproduced by Huntings Aerosurveys Ltd., from Air Photographs dated 1952, 1953. Reproduced and Printed by 42 Survey Engineer Regiment May 1956. Published by D. Survey War Office and Air Ministry, 1956."
                     :notes "Well-preserved, undistorted scan. There are some inaccuracies particularly with tiny islands, but relatively precise."
                     :issuer-link "/maps/1956-Bahrain.jpg"
                     :source "Huntings Aerosurveys Ltd."
                     :source-link "/maps/1956-Bahrain.tif"
                     :submitted-by "Ali Karimi"
                     :issuer "D. Survey War Office and Air Ministry, 1956"
                     :scale "1:63,360"
                     :opts (merge base-opts {:minNativeZoom 9
                                             :maxNativeZoom 15
                                             :opacity 0.7})}
   "1968 - Bahrain" {:title "BAHRAIN ISLANDS - 1968"
                     :url (form-tile-url "1968-Bahrain")
                     :description "Compiled, drawn, reproduced from Air Photographs dated 1952-1953. Revised by 42 Survey Engineer Regiment from Air Photography dated 1962. Revised and Printed by FAIREY SURVEYS LTD., Reform Road, Maidenhead, Berkshire, England, 1968. Published by PUBLIC WORKS DEPT., BAHRAIN GOVERNMENT."
                     :notes "Well-preserved."
                     :issuer-link "/maps/1968.63360.Bahrain.tif"
                     :source "Fairey Surveys"
                     :source-link "/maps/1968.63360.Bahrain.warped2.tiff"

                     :issuer "PUBLIC WORKS DEPT., BAHRAIN GOVERNMENT."
                     :scale "1:63,360"
                     :opts (merge base-opts {:minNativeZoom 9
                                             :maxNativeZoom 15
                                             :opacity 0.7})}

   "1973 - Bahrain" {:title "Bahrain - 1973"
                     :url (form-tile-url "1973-Bahrain")
                     :description "Compiled and drawn by 42 Survey Engineer Regiment in 1971-2 from air photography dated 1969 and other sources. Control supplied by MCE RE and No 1 A(S)LS RE. Field check 1971 and revision in 1973 from air photography dated 1970. Names rendered in accordance with BGN/PCGN system of transliteration from Arabic. Projection: Traverse Mercator, Spheroid: Clarke 1880, Horizontal Datum: Nahrwan, Vertical Datum: MSL. Series K7610, Edition 1-GSGS. "
                     :notes "High resolution, with slight alignment issues. Projection is based off the Clarke 1880 Spheroid with a Datum in Nahrwan. Required post-processing/alignment work after translation and warping. Provided parameters appear to be off."
                     :source-link "/maps/1973-Bahrain.tif"
                     :source "Ministry of Defence, United Kingdom, 1973"
                     :scale "1:50,000"
                     :issuer "Unknown"
                     :issuer-link "/maps/1973-Bahrain.zip"
                     :opts (merge base-opts {:minNativeZoom 11
                                             :maxNativeZoom 16
                                             :opacity 0.8})}
   "1977 - Bahrain (25k)" {:title "State of Bahrain - 1977"
                           :url (form-tile-url "1977-Bahrain")
                           :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                           :notes "This is a 7-sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                           :source-link "/maps/1977-Bahrain-v2.tif"
                           :source "Fairey Surveys Ltd"
                           :scale "1:25,000"
                           :issuer "Unknown"
                           :issuer-link "/maps/1977-Bahrain.zip"
                           :opts (merge base-opts {:minNativeZoom 11
                                                   :maxNativeZoom 16
                                                   :opacity 0.8})}
   "1977 - Bahrain (50k)" {:title "State of Bahrain - 1977"
                           :url (form-tile-url "1977-Bahrain.50k")
                           :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain. Edition 1."
                           :notes "This is a 3-sheet series produced for the State of Bahrain. One of several series, including the 1:25,000 and 1:10,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                           :source-link "/maps/1977-Bahrain-50k.compressed.tif"
                           :source "Fairey Surveys Ltd"
                           :scale "1:50,000"
                           :issuer "Unknown"
                           :issuer-link "/maps/1977.50k.zip"
                           :opts (merge base-opts {:minNativeZoom 11
                                                   :maxNativeZoom 17
                                                   :opacity 0.8})}
   "1985 - Bahrain (10k)" {:title "State of Bahrain - 1985"
                           :url (form-tile-url "1985-Bahrain.10k")
                           :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD. Additional information compiled by the Survey Directorate, Ministry of Housing, State of Bahrain from air photography taken in 1983 by B.K.S. Surveys LTD. and field survey revisions 1984."
                           :notes "This is a 28-sheet series produced for the State of Bahrain. One of several series, including the 1:25,000 and 1:50,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching. Please note that sheet 73 and 94 are from 1977. Sheet 73 is missing and sheet 94 is available but lower quality. This is the holy grail of Bahrain maps."
                           :source-link "/maps/1985-Bahrain(10k).tif"
                           :source "Fairey Surveys Ltd."
                           :scale "1:10,000"
                           :issuer "Ministry of Information, Bahrain"
                           :issuer-link "/maps/1985-Bahrain.10k.zip"
                           :opts (merge base-opts {:minNativeZoom 13
                                                   :maxNativeZoom 19
                                                   :opacity 0.8})}
  "1986 - Bahrain (50k)" {:title "State of Bahrain - 1986"
                           :url (form-tile-url "1986-Bahrain.50k")
                           :description "Produced under the direction of the Director of Military Survey, Ministry of Defence, United Kingdom 1986. Revised 1985 from air photography and mapping dated 1983 and 1985 supplied by the Survey Directorate of the Ministry of Housing, Bahrain. Edition 2."
                           :notes "This is a 3 map series produced for the State of Bahrain. One of several series, including the 1:25,000 and 1:10,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                           :source-link "/maps/1986-Bahrain(50k).tif"
                           :source "Fairey Surveys Ltd"
                           :scale "1:50,000"
                           :issuer "Ministry of Defence, UK"
                           :issuer-link "/maps/1986-Bahrain.zip"
                           :opts (merge base-opts {:minNativeZoom 8
                                                   :maxNativeZoom 16
                                                   :opacity 0.8})}
   "1990 - Bahrain (50k)" {:title "State of Bahrain - 1990"
                           :url (form-tile-url "1990-Bahrain.50k")
                           :description "Produced under the direction of the Director of Military Survey, Ministry of Defence, United Kingdom 1990. Revised 1985 from air photography and mapping dated 1983 and 1985 supplied by the Survey Directorate of the Ministry of Housing, Bahrain. Major Revision 1989 by Survey Directorate, Ministry of Housing from air photography and field surveys 1987-1989. Partial revision by MCE RE in 1990. Edition 4."
                           :notes "Note: The Hawar sheet is actually from 1986 since a 1990 edition wasn't produced. This is a 3 sheet-series produced for the State of Bahrain. One of several series, including the 1:25,000 and 1:10,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                           :source-link "/maps/1990-Bahrain(50k).tif"
                           :source "Ministry of Defence, UK"
                           :scale "1:50,000"
                           :issuer "Ministry of Defence, UK"
                           :issuer-link "/maps/1990-Bahrain.zip"
                           :opts (merge base-opts {:minNativeZoom 9
                                                   :maxNativeZoom 16
                                                   :opacity 0.8})}
   "1997 - Bahrain (50k)" {:title "State of Bahrain - 1997"
                           :url (form-tile-url "1997-Bahrain.50k")
                           :description "Produced in 1977 under the direction of the  Military Survey Directorate, Ministry of Defence, United Kingdom. Revised in 1985, 1989, and 1998 by Survey Directorate, Ministry of Housing, State of Bahrain, from air photography and field surveys. Revised in 1996 by the Survey Directorate of the Ministry of Housing, Municipalities and Environment, State of Bahrain from air photography and field surveys. Edition 5."
                           :notes "This is a 3-sheet series produced for the State of Bahrain. One of several series, including the 1:25,000 and 1:10,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                           :source-link "/maps/1997-Bahrain(50k).tif"
                           :source "Ministry of Housing"
                           :scale "1:50,000"
                           :issuer "Ministry of Housing"
                           :issuer-link "/maps/1997-Bahrain.zip"
                           :opts (merge base-opts {:minNativeZoom 8
                                                   :maxNativeZoom 16})}
   "2004 - Bahrain (50k)" {:title "State of Bahrain - 2004"
                           :url (form-tile-url "2004-Bahrain.50k")
                           :description "Produced in 1977 under the direction of the Military Survey Directorate, Ministry of Defence, United Kingdom. Revised in 1985, 1989, 1993, 1996, and 2002 by Survey Directorate from air photography and field surveys. Revised in 1996 by the Survey Directorate of the Ministry of Housing, Municipalities and Environment, State of Bahrain from air photography and field surveys. Edition 6."
                           :notes "This is a 3 map series produced for the State of Bahrain. One of several series, including the 1:25,000 and 1:10,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching. North sheet was published 2003."
                           :source-link "/maps/2004-Bahrain(50k).tiff"
                           :source "Ministry of Housing"
                           :scale "1:50,000"
                           :issuer "Dar Akbar Al Khaleej"
                           :issuer-link "/maps/2004-Bahrain.zip"
                           :opts (merge base-opts {:minNativeZoom 8
                                                   :maxNativeZoom 16})}
   "2030 - Bahrain" {:title "Vision 2030"
                     :url (form-tile-url "2030-Bahrain")
                     :description "National Detail Land Use map 2030 is the second version of the National Plan and Development Strategy updated Map illustrating detailed Land Use. Data and Relevant Information produced by GIS in GDUP. Issue Date: 11/1/2016."
                     :notes "No scale or boundaries provided, hand matched."
                     :source-link "/maps/2030-Bahrain.tif"
                     :submitted-by "Maitham Almubarak"
                     :source "National Plan and Development"
                     :scale "N/A, probably 1:50,000"
                     :issuer "Urban Planning & Development Authority"
                     :issuer-link "https://upda.gov.bh/Media/ZonningMappings/20190911081035456_n0adrtkg_vek.jpg"
                     :opts (merge base-opts {:minNativeZoom 10
                                             :maxNativeZoom 15
                                             :opacity 0.8})}})

(def fairey {"Manama - 1977" {:title "Manama - State of Bahrain - 1977 (Edition 1)"
                              :url (form-tile-url "1977-Manama.1")
                              :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/1977-Manama.1.tif"
                              :source "Fairey Surveys Ltd"
                              :scale "1:25,000"
                              :issuer "Unknown"
                              :issuer-link "/maps/1977.Manama.1.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Manama - 1992" {:title "Manama - State of Bahrain - 1992 (Edition 4)"
                              :url (form-tile-url "1992-Manama.4")
                              :description "Original photogrammetric compilation by Fairey Surveys Ltd 1977. Subsequent revision by Survey Directorate, Ministry of Housing, State of Bahrain from Photography dated Feb. 1980, Jan. 1983, and Oct. 1986. Field surveys from 1986 to 1991."
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/1992-Manama.4.tif"
                              :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                              :scale "1:25,000"
                              :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                              :issuer-link "/maps/1992.Manama.4.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Manama - 1998" {:title "Manama - State of Bahrain - 1998 (Edition 5)"
                              :url (form-tile-url "1998-Manama.5")
                              :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1985, 1988, and 1992 from air photography and field surveys by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 1998 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/1998-Manama.5.tif"
                              :source "Ministry of Housing, Municipalities and Environment, State of Bahrain."
                              :scale "1:25,000"
                              :issuer "Ministry of Housing, Municipalities and Environment, State of Bahrain."
                              :issuer-link "/maps/1998.Manama.5.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Manama - 2006" {:title "Manama - State of Bahrain - 2006 (Edition 6)"
                              :url (form-tile-url "2006-Manama.6")
                              :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1985, 1988, 1992, 1998 and 2006 from air photography and field surveys by the Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/2006-Manama.6.tif"
                              :source "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                              :scale "1:25,000"
                              :issuer "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                              :issuer-link "/maps/2006.Manama.6.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Budaiya - 1977" {:title "Budaiya - State of Bahrain - 1977 (Edition 1)"
                               :url (form-tile-url "1977-Budaiya.1")
                               :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1977-Budaiya.1.tif"
                               :source "Fairey Surveys Ltd"
                               :scale "1:25,000"
                               :issuer "Unknown"
                               :issuer-link "/maps/1977.Budaiya.1.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Budaiya - 1987" {:title "Budaiya - State of Bahrain - 1987 (Edition 2)"
                               :url (form-tile-url "1987-Budaiya.2")
                               :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD. Additional information compiled by the Survey Directorate, Ministry of Housing, State of Bahrain from Air Photography taken in Jan. 1983 by B.K.S. Surveys LTD. And field surveys Revisions 1986"
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1987-Budaiya.2.tif"
                               :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                               :scale "1:25,000"
                               :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                               :issuer-link "/maps/1987.Budaiya.2.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Budaiya - 1990" {:title "Budaiya - State of Bahrain - 1990 (Edition 3)"
                               :url (form-tile-url "1990-Budaiya.3")
                               :description "Original photogrammetric compilation by Fairey Surveys Ltd 1977. Subsequent revision by Survey Directorate, Ministry of Housing, State of Bahrain from Photography dated 1983 and 1986. Field surveys from 1984 to 1990."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1990-Budaiya.3.tif"
                               :source "Fairey Surveys Ltd"
                               :scale "1:25,000"
                               :issuer "Unknown"
                               :issuer-link "/maps/1990.Budaiya.3.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Budaiya - 1997" {:title "Budaiya - State of Bahrain - 1997 (Edition 4)"
                               :url (form-tile-url "1997-Budaiya.4")
                               :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1987 and 1990 from air photography and field surveys by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 1997 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1997-Budaiya.4.tif"
                               :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                               :scale "1:25,000"
                               :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                               :issuer-link "/maps/1997.Budaiya.4.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Budaiya - 2007" {:title "Budaiya - State of Bahrain - 2007 (Edition 5)"
                               :url (form-tile-url "2007-Budaiya.5")
                               :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1987, 1990, 1997, and 2007 from air photography and field surveys by the Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/2007-Budaiya.5.tif"
                               :source "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                               :scale "1:25,000"
                               :issuer "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                               :issuer-link "/maps/2007.Budaiya.5.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}

             "Riffa - 1977" {:title "Riffa - State of Bahrain - 1977 (Edition 1)"
                             :url (form-tile-url "1977-Riffa.1")
                             :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                             :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                             :source-link "/maps/1977-Riffa.1.tif"
                             :source "Fairey Surveys Ltd"
                             :scale "1:25,000"
                             :issuer "Unknown"
                             :issuer-link "/maps/1977.Riffa.1.original.tif"
                             :opts (merge base-opts {:minNativeZoom 11
                                                     :maxNativeZoom 17
                                                     :opacity 0.8})}
             "Riffa - 1987" {:title "Riffa - State of Bahrain - 1987 (Edition 2)"
                             :url (form-tile-url "1987-Riffa.2")
                             :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD. Additional information compiled by the Survey Directorate, Ministry of Housing, State of Bahrain from Air Photography taken in Jan. 1983 by B.K.S. Surveys LTD. And field surveys Revisions 1986"
                             :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                             :source-link "/maps/1987-Riffa.2.tif"
                             :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                             :scale "1:25,000"
                             :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                             :issuer-link "/maps/1987.Riffa.2.original.tif"
                             :opts (merge base-opts {:minNativeZoom 11
                                                     :maxNativeZoom 17
                                                     :opacity 0.8})}
             "Riffa - 1997" {:title "Riffa - State of Bahrain - 1997 (Edition 4)"
                             :url (form-tile-url "1997-Riffa.4")
                             :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1987 and 1991 from air photography and field survey by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 1997 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                             :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                             :source-link "/maps/1997-Riffa.4.tif"
                             :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                             :scale "1:25,000"
                             :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                             :issuer-link "/maps/1997.Riffa.4.original.tif"
                             :opts (merge base-opts {:minNativeZoom 11
                                                     :maxNativeZoom 17
                                                     :opacity 0.8})}
             "Riffa - 2005" {:title "Riffa - State of Bahrain - 2005 (Edition 5)"
                             :url (form-tile-url "2005-Riffa.5")
                             :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1986, 1991,  and 2004 from air photography and field surveys by the Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                             :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                             :source-link "/maps/2005-Riffa.5.tif"
                             :source "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                             :scale "1:25,000"
                             :issuer "Topographic Survey Directorate, Survey and Land Registration Bureau, Kingdom of Bahrain."
                             :issuer-link "/maps/2005.Riffa.5.original.tif"
                             :opts (merge base-opts {:minNativeZoom 11
                                                     :maxNativeZoom 17
                                                     :opacity 0.8})}
             "Zallaq - 1977" {:title "Zallaq - State of Bahrain - 1977 (Edition 1)"
                              :url (form-tile-url "1977-Zallaq.1")
                              :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/1977-Zallaq.1.tif"
                              :source "Fairey Surveys Ltd"
                              :scale "1:25,000"
                              :issuer "Unknown"
                              :issuer-link "/maps/1977.Zallaq.1.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Zallaq - 1995" {:title "Zallaq - State of Bahrain - 1995 (Edition 3)"
                              :url (form-tile-url "1995-Zallaq.3")
                              :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD. Additional information compiled by the Survey Directorate, Ministry of Housing, State of Bahrain from Air Photography taken in Jan. 1983 by B.K.S. Surveys LTD. And field surveys Revisions 1986"
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/1995-Zallaq.3.tif"
                              :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                              :scale "1:25,000"
                              :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                              :issuer-link "/maps/1995.Zallaq.3.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Zallaq - 2006" {:title "Zallaq - State of Bahrain - 2006 (Edition 4)"
                              :url (form-tile-url "2006-Zallaq.4")
                              :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1987 and 1991 from air photography and field survey by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 2006 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                              :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                              :source-link "/maps/2006-Zallaq.4.tif"
                              :source "Survey Directorate, Ministry of Housing, State of Bahrain"
                              :scale "1:25,000"
                              :issuer "Survey Directorate, Ministry of Housing, State of Bahrain"
                              :issuer-link "/maps/2006.Zallaq.4.original.tif"
                              :opts (merge base-opts {:minNativeZoom 11
                                                      :maxNativeZoom 17
                                                      :opacity 0.8})}
             "Mamtala - 1977" {:title "Mamtala - State of Bahrain - 1977 (Edition 1)"
                               :url (form-tile-url "1977-Mamtala.1")
                               :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1977-Mamtala.1.tif"
                               :source "Fairey Surveys Ltd"
                               :scale "1:25,000"
                               :issuer "Unknown"
                               :issuer-link "/maps/1977.Mamtala.1.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Mamtala - 1990" {:title "Mamtala - State of Bahrain - 1990 (Edition 2)"
                               :url (form-tile-url "1990-Mamtala.2")
                               :description "Original photogrammetric compilation by Fairey Surveys Ltd 1977. Subsequent revision by Survey Directorate, Ministry of Housing, State of Bahrain from Photography dated Feb. 1980, Jan. 1983 and Oct. 1986. Field surveys from 1986 to 1990."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1990-Mamtala.2.tif"
                               :source "Fairey Surveys Ltd"
                               :scale "1:25,000"
                               :issuer "Unknown"
                               :issuer-link "/maps/1990.Mamtala.2.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Mamtala - 1998" {:title "Mamtala - State of Bahrain - 1998 (Edition 3)"
                               :url (form-tile-url "1998-Mamtala.3")
                               :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1990 from air photography and field survey by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 1998 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                               :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                               :source-link "/maps/1998-Mamtala.3.tif"
                               :source "Fairey Surveys Ltd"
                               :scale "1:25,000"
                               :issuer "Unknown"
                               :issuer-link "/maps/1998.Mamtala.3.original.tif"
                               :opts (merge base-opts {:minNativeZoom 11
                                                       :maxNativeZoom 17
                                                       :opacity 0.8})}
             "Rumaytha - 1977" {:title "Rumaytha - State of Bahrain - 1977 (Edition 1)"
                                :url (form-tile-url "1977-Rumaytha.1")
                                :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                                :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                                :source-link "/maps/1977-Rumaytha.1.tif"
                                :source "Fairey Surveys Ltd"
                                :scale "1:25,000"
                                :issuer "Unknown"
                                :issuer-link "/maps/1977.Rumaytha.1.original.tif"
                                :opts (merge base-opts {:minNativeZoom 11
                                                        :maxNativeZoom 17
                                                        :opacity 0.8})}
             "Rumaytha - 1991" {:title "Rumaytha - State of Bahrain - 1990 (Edition 2)"
                                :url (form-tile-url "1991-Rumaytha.2")
                                :description "Original photogrammetric compilation by Fairey Surveys Ltd in 1977. Subsequent revision by Survey Directorate, Ministry of Housing, State of Bahrain from Photography dated Feb. 1980, Jan. 1983 and Oct. 1986. Field surveys from 1986 to 1991"
                                :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                                :source-link "/maps/1991-Rumaytha.2.tif"
                                :source "Fairey Surveys Ltd"
                                :scale "1:25,000"
                                :issuer "Unknown"
                                :issuer-link "/maps/1991.Rumaytha.2.original.tif"
                                :opts (merge base-opts {:minNativeZoom 11
                                                        :maxNativeZoom 17
                                                        :opacity 0.8})}
             "Rumaytha - 2000" {:title "Rumaytha - State of Bahrain - 1998 (Edition 3)"
                                :url (form-tile-url "2000-Rumaytha.3")
                                :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 1991 from air photography and field survey by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in year 2000 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                                :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                                :source-link "/maps/2000-Rumaytha.3.tif"
                                :source "Fairey Surveys Ltd"
                                :scale "1:25,000"
                                :issuer "Unknown"
                                :issuer-link "/maps/2000.Rumaytha.3.original.tif"
                                :opts (merge base-opts {:minNativeZoom 11
                                                        :maxNativeZoom 17
                                                        :opacity 0.8})}
             "Ras Al Barr - 1977" {:title "Ras Al Barr - State of Bahrain - 1977 (Edition 1)"
                                   :url (form-tile-url "1977-Ras Al Barr.1")
                                   :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                                   :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                                   :source-link "/maps/1977-Ras Al Barr.1.tif"
                                   :source "Fairey Surveys Ltd"
                                   :scale "1:25,000"
                                   :issuer "Unknown"
                                   :issuer-link "/maps/1977.Ras Al Barr.1.original.tif"
                                   :opts (merge base-opts {:minNativeZoom 11
                                                           :maxNativeZoom 17
                                                           :opacity 0.8})}
             "Ras Al Barr - 2000" {:title "Ras Al Barr - State of Bahrain - 2000 (Edition 2)"
                                   :url (form-tile-url "2000-Ras Al Barr.2")
                                   :description "Original photogrammetric compilation in 1977 by Fairey Surveys Ltd. Revised in 2000 from air photography and field survey by the Survey Directorate, Ministry of Housing, State of Bahrain. Revised in 2006 by the Ministry of Housing, Municipalities and Environment, State of Bahrain."
                                   :notes "This is a 7 sheet series produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                                   :source-link "/maps/2000-Ras Al Barr.2.tif"
                                   :source "Fairey Surveys Ltd"
                                   :scale "1:25,000"
                                   :issuer "Unknown"
                                   :issuer-link "/maps/2000.Ras Al Barr.2.original.tif"
                                   :opts (merge base-opts {:minNativeZoom 11
                                                           :maxNativeZoom 17
                                                           :opacity 0.8})}
             "Hawar - 1993" {:title "Hawar - State of Bahrain - 1993 (Edition 1)"
                             :url (form-tile-url "1993-Hawar.1")
                             :description "Compiled from 1:10,000 maps published in 1981. Additional information from 1991 air photography and field surveys 1993. Produced by the Survey Directorate, Ministry of Housing, Bahrain"
                             :notes "This is a 2-part addendum to the 7 sheet series (1:25,000 scale) produced for the State of Bahrain. One of 7 series, including series in the 1:100,000, 1:50,000, 1:10,000, 1:2,000, and 1:1,000. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching. Originally commissioned along with the original set in 1977, the first edition would only appear in 1993."
                             :source-link "/maps/1993-Hawar.1.tif"
                             :source "Fairey Surveys Ltd"
                             :scale "1:25,000"
                             :issuer "Unknown"
                             :issuer-link "/maps/1993.Hawar.1.zip"
                             :opts (merge base-opts {:minNativeZoom 11
                                                     :maxNativeZoom 17
                                                     :opacity 0.8})}})


(def layers {"Bahrain" bahrain-layers
             "Manama" manama-layers
             "Muharraq" muharraq-layers
             "Fairey Surveys 1:25k (1977-2007)" fairey
             "Other" other-layers})

(def ar-layers
  {"Bahrain"
   {"1914 - Bahrain Harbour" {:title "١٩١٤ - ميناء البحرين"
                              :description nil
                              :submitted-by "علي كريمي"
                              :notes "أقل دقة في الجنوب."}
    "1937 - Bahrain" {:title "جزيرة البحرين - ١٩٣٧ "
                      :description "تشير الخطة إلى الهيدرولوجيا والأماكن السكنية و ميزات جغرافية كمستويات الارتفاع."
                      :notes "الوثيقة الاصلية لم تنسخ و ترقم بشكل جيد. الخارطة تنحرف من الساحل الاصلي بشكل متزايد في جنوب الجزيرة. هذه الخارطة احد خرائطي المفضله لأنه توحي لنا حدود المياه الضحلة حول الجزيرة. هذه الحدود اليوم هي أيضآ حدود الأراضي المدفونه. دفان المياه الضحلة أسهل و اقل تكلفة فصارت هذه الملامح الجغرافية شبه حدود للدفان المستمر."}
    "1977 - Bahrain (25k)" {:title "البحرين - ١٩٧٧"
                            :description "تم تجميع ورسم هذه الخريطة من صور فوتوغرافية تم التقاطه خلال يناير/ فبراير١٩٧٧ بواسطة Fairey Surveys LTD من مقره على ريفورم رود ، مايدنهيد ، بيركشاير ، إنجلترا لدولة البحرين."
                            :notes " هذه الخارطة سلسلة فيها ٧ خرائط رسمت في ١٩٧٧. هذه السلسلة احد ثلاث تغطي البحرين انتجت في ١٩٧٧. ترجمة مباشرة من اسقاط UTM Zone 39 إلى Web Mercator مع معالجة بسيطة."}
    "1977 - Bahrain (50k)" {:title "البحرين - ١٩٧٧"
                            :description "تم تجميع ورسم هذه الخريطة من صور فوتوغرافية تم التقاطه خلال يناير/ فبراير١٩٧٧ بواسطة Fairey Surveys LTD من مقره على ريفورم رود ، مايدنهيد ، بيركشاير ، إنجلترا لدولة البحرين."
                            :notes " هذه الخارطة سلسلة فيها ٣ خرائط رسمت في ١٩٧٧. هذه السلسلة احد ثلاث تغطي البحرين انتجت في ١٩٧٧. ترجمة مباشرة من اسقاط UTM Zone 39 إلى Web Mercator مع معالجة بسيطة."}

    "1985 - Bahrain (10k)" {:title "البحرين - ١٩٨٥"
                            :description "تم تجميع ورسم هذه الخريطة من صور فوتوغرافية تم التقاطه خلال يناير/ فبراير١٩٧٧ بواسطة Fairey Surveys LTD. اضافت مديرية المساحةتحت وزارة الاسكان معلومات من صور جوية التقت في عام ١٩٨٣ بواسطة B.K.S. Surveys LTD و تعديلات ميدانية في ١٩٨٤."
                            :notes " هذه الخارطة سلسلة فيها ٢٨ خرائط رسمت في ١٩٧٧. هذه السلسلة احد ثلاث تغطي البحرين انتجت في ١٩٧٧. ترجمة مباشرة من اسقاط UTM Zone 39 إلى Web Mercator مع معالجة بسيطة. يرجى ملاحظة أن الورقة ٧٣ و ٩٤ من ضمن خرائط عام ١٩٧٧ وليس ١٩٨٥. الورقة ٧٣ مفقودة والورقة ٩٤ متوفرة ولكن بجودة ضعيفة.  هذه الخرائط عبارة عن كنز ثمين اي انها تعطي نظرة لمنتصف الثمانينات والتحولات الجذرية للدولة في فترة قلت فيها الوثائق"}

    "1973 - Bahrain" {:title "البحرين - ١٩٧٣"
                      :description nil
                      :notes "خرائط عالية الجودة فيها مشاكل بسيطة في دقة المحاذة. الإسقاط الاصلي يعتمد على حسابات كلارك ١٨٨٠ و موقع مرجعي في نهروان. تم محاذة اضافية بعد الترجمة و التحوير بسبب اخطاء في البيانات المدونة في الخارطة"}
    "1943 - Bahrain" {:title "جزيرة البحرين - ١٩٤٣"
                      :description "الخارطة تُظهر الميزات الجغرافية والأماكن السكنية والأراضي الزراعية والمباني والأهوار والطرق الرئيسية. موضحة بالخارطة الطبوغرافية. تم جمعها من صور جوية في A.H.Q. مكتب الرسم ، هنيدي، ١٩٣٧. رسم وطبع بالهليوغراف بواسطة O.S. 1939
"
                      :notes "خارطة دقيقة بأخطاء بسيطة"}
    "1956 - Bahrain" {:title "جزيرة البحرين - ١٩٥٦"
                      :description "سلسلة  جي اس جي اس ٤٠٣٥ النسخة الرابعة. جُمعت ورُسمت وأُنتجت من قبل شركة هانتنجز ايروسورفيز من صور جوية أخذت في عاميّ ١٩٥٢ و١٩٥٣. أعاد انتاجها وطباعتها فوج ٤٢ للجهاز الهندسي لمساحة الأراضي البريطانية في مايو ١٩٥٦ ونُشرت من قبل مديرية جهاز المساحة لمكتب الحرب ووزارة الملاحة الجوية البريطانية."
                      :notes "خارطة انتجت رقمياً بجودة عالية. توجد اخطاء بسيطة بالخارطة خاصة بما يتعلق بالجزر الصغيرة."}
    "2030 - Bahrain"  {:title "رؤية البحرين ‫-‬ ٢٠٣٠"
                       :description "ﺧﺎرﻃﺔ اﻟﻤﺨﻄﻂ اﻟﮭﯿﻜﻠﻲ اﻻﺳﺘﺮاﺗﯿﺠﻲ ﻟﻤﻤﻠﻜﺔ اﻟﺒﺤﺮﯾﻦـ لاستخدام الأراضي ٢٠٣٠، النسخة الثانية. الخطة الوطنية توضح استخدامات الأراضي بالتفصيل. البيانات والمعلومات ذات انتجت بـ GIS في GDUP. تاريخ الإصدار: ١١/١/٢٠١٦"
                       :notes "تم التنسيق باليد لعدم وجود البيانات الجيوغرافية"}}
   "Manama" {"1937 - Manama"  {:title "المنامة - ١٩٣٧"
                               :description nil
                               :notes "خارطة متوسطة الدقة. أخطاء بسيطة في غرب و جنوب الخارطة الأصلية"}
             "1969 - Manama & AlJufayr" {:title "المنامة و الجفير - ١٩٦٩"
                                         :description nil
                                         :submitted-by "علي كريمي"
                                         :notes "سلسلة خرائط فيها ٥ أجزاء. تطلبت تصحيحات لالألوان، تصحيحات تشكيلية، ولم تطابق الإسقاط المحدد"}
             "1977 - USSR - Manama/Muharraq"  {:title "المنامة و المحرق ١٩٧٧"
                                               :description "يوجد الوصف باللغة الروسية في الركن الأيمن السفلي من الخريطة.  تم إنتاج هذه الخريطة بين عامي   ١٩٧٧ و ١٩٨٠ في مصنع في سفيردلوفسك (إيكاترينبرج اليوم). تم اكتشاف هذه الخرائط في متاجر سيكوند هاند في الفترة ما بعد انهيار الاتحاد السوفيتي وتم جمعها عشوائياً. هناك العديد من المميزات في هذه الخرائط الغير موجودة في خرائط معاصرة لها. حينما تعمد رسامو الخرائط من حلف ناتو إخفاء مناطق حساسة مثل المطارات والموانئ من خرائطهم، السوفيتين لم يخفوها. لإنتاج هذه التفاصيل، اضطروا السوفيتيين للاعتماد على  مخابرات وجواسيس في الارض و الجو للحصول على ادق التفاصيل."
                                               :notes " بينما نعلم أن هذه الخارطة إسقاطها سيستم كوردينت ٤٢ (المعروف أيضًا ببولكوفو) مع مسند براميتري زيميل او بي زي ٩٠، لم ننجح في معرفة معلومات كافية لتعديل الخارطة بهذه الطريقة فاضطرينا لتعديله يدويًا باستخدام نقاط التحكم الأرضي."}}
   "Muharraq" {"1937 - Muharraq" {:title "جزيرة المحرق - ١٩٣٧"
                                  :description nil
                                  :notes "خارطة دقيقة من دون اي اشكاليات"}
               "1931 - Muharraq" {:title "مدينة المحرق - ١٩٣١"
                                  :description nil
                                  :submitted-by "علي كريمي"
                                  :notes "الخارطة دقيقة قرب سوق المحرق لكنها تنحذر بقرب بو ماهر في الجنوب "}

               "1977 - USSR - Manama/Muharraq"  {:title "المنامة و المحرق ١٩٧٧"
                                                 :description "يوجد الوصف باللغة الروسية في الركن الأيمن السفلي من الخريطة.  تم إنتاج هذه الخريطة بين عامي   ١٩٧٧ و ١٩٨٠ في مصنع في سفيردلوفسك (إيكاترينبرج اليوم). تم اكتشاف هذه الخرائط في متاجر سيكوند هاند في الفترة ما بعد انهيار الاتحاد السوفيتي وتم جمعها عشوائياً. هناك العديد من المميزات في هذه الخرائط الغير موجودة في خرائط معاصرة لها. حينما تعمد رسامو الخرائط من حلف ناتو إخفاء مناطق حساسة مثل المطارات والموانئ من خرائطهم، السوفيتين لم يخفوها. لإنتاج هذه التفاصيل، اضطروا السوفيتيين للاعتماد على  مخابرات وجواسيس في الارض و الجو للحصول على ادق التفاصيل."
                                                 :notes " بينما نعلم أن هذه الخارطة إسقاطها سيستم كوردينت ٤٢ (المعروف أيضًا ببولكوفو) مع مسند براميتري زيميل او بي زي ٩٠، لم ننجح في معرفة معلومات كافية لتعديل الخارطة بهذه الطريقة فاضطرينا لتعديله يدويًا باستخدام نقاط التحكم الأرضي."}}
   "Other" {}})

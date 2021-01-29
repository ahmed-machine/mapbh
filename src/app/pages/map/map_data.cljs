(ns app.pages.map.map-data)

;;;;;;;;;;;;;;;;;;;;;;;
;; Map data and opts ;;
;;;;;;;;;;;;;;;;;;;;;;;

(def base-opts {:attribution "© OpenStreetMap, Mapbox"
                :maxZoom 20
                :minZoom 9
                :zoomOffset -1
                :detectRetina true
                :id "Standard Style"
                :tileSize 512})

(def access-tokens ["pk.eyJ1IjoiYWhtZWRhbG11dGF3YSIsImEiOiJja2s1a3JqdWMwbHJiMnVzM2U5bHZleGNwIn0.icIlmsN_TY4zztxqC__e8Q"
                    "pk.eyJ1IjoidG9sb2NvcmUiLCJhIjoiY2trNWtuamxjMDY3ZzJubnhybjlscXRyNCJ9.ug82VbeEK-sPDD_YgJLBLw"])

(def map-id "satellite-streets-v11")

(def base-satellite {"Terrain" {:url "https://a.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                :opts (assoc base-opts :zoomOffset -1)}
                     "Satellite" {:url (str "https://api.mapbox.com/styles/v1/mapbox/" map-id "/tiles/{z}/{x}/{y}?access_token=" (rand-nth access-tokens))
                                  :opts (assoc base-opts :zoomOffset -1)}})

(def layers
  {"1937 - BAHRAIN" {;; English
                     :title "BAHRAIN ISLAND - 1937"
                     :url "https://api.maptiler.com/tiles/1c3695ec-61ea-4e60-be6e-87bf60ceca38/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "Plan of Bahrain. The plan indicates hydrology, settlements and  as well as providing some indication of relief. Included is a table of reference of symbols used. Prepared by naval staff in the Admiralty's Naval Intelligence Division."
                     :notes "Original scan of document was distorted and outlines begin to diverge greatly further and further south."
                     :source-link "https://www.qdl.qa/en/archive/8955/vdc_90043097172.0x000077"
                     :source "British Library: India Office Records and Private Papers"
                     :issuer "Qatar Digital Library"
                     :scale "1:63,360"

                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 9
                                             :maxNativeZoom 14
                                             :opacity 0.7})}
   "1943 - BAHRAIN" {:title "BAHRAIN ISLAND - 1943"
                     :url "https://api.maptiler.com/tiles/48f771c1-2059-4d94-a863-7510c9434f2c/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description " Shows physical features, residential places, cultivated land, selected buildings, marsh land, and major roads.
  Relief shown by landform drawings, contours, and spot heights. Compiled from Air photographs in A.H.Q. Drawing Office, Hinaidi, 1937. Drawn and Heliographed by O.S. 1939."
                     :notes "Well-preserved, undistorted scan. Map has relatively few inaccuracies."
                     :source-link "https://legacy.lib.utexas.edu/maps/ams/bahrein_island/txu-pclmaps-oclc-6559195-bahrein-island.jpg"
                     :source "US Army Map Service"
                     :issuer "University of Texas Library"
                     :scale "1:63,360"
                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 9
                                             :maxNativeZoom 14
                                             :opacity 0.7})}
   "1931 - MUHARRAQ" {:title "MOHARRAK TOWN - March 1931"
                      :url "https://api.maptiler.com/tiles/a77144f2-7690-4c07-8852-3ac78dca0f35/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                      :description nil
                      :notes "Accurate in Old Muharraq. Drifts in the southern part of the map near Bu Maher."
                      :source-link "/maps/1931-Muharraq.jpg"
                      :source "Unknown"
                      :scale "1:200ft"
                      :issuer "Historic Maps of Bahrain 1817 - 1970"
                      :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 13
                                             :maxNativeZoom 18
                                             :opacity 0.7})}
   "1937 - MUHARRAQ" {:title "MUHARRAQ ISLAND - Jan 1937"
                      :url "https://api.maptiler.com/tiles/7e37e696-7889-461f-81d3-8a3b5e5e0097/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                      :description "The map shows Muhurraq Island, and the causeway linking Muharraq to Manama. Also shown are principal roads, tracks, buildings (including the Political Agency in Manana), islands, and the location of several underwater freshwater springs. A rectangle on Muharraq Island indicates the area specified for the landing ground."
                      :notes "Well-preserved and decently accurate."
                      :source-link "https://www.qdl.qa/en/archive/8955/vdc_9002560898.0x000080"
                      :source "British Library: India Office Records and Private Papers"
                      :scale "1:20,000"
                      :issuer "Qatar Digital Library"
                      :opts (merge base-opts {:maxZoom 20
                                              :minZoom 9
                                              :minNativeZoom 11
                                              :maxNativeZoom 16
                                              :opacity 0.7})}
   "1937 - MANAMAH" {:title "PLAN OF PORT AND TOWN"
                     :url "https://api.maptiler.com/tiles/56815264-b7fe-4b4f-900d-979cb50ff777/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "Plan of Manamah, Bahrain. The plan indicates settlements, streets, buildings and landmarks. Prepared by naval staff in the Admiralty's Naval Intelligence Division."
                     :notes "Some map drift along the southern and western ends of the document. Mostly accurate."
                     :source-link "https://www.qdl.qa/en/archive/81055/vdc_100043097172.0x000079"
                     :source "British Library: India Office Records and Private Papers"
                     :scale "1:3,639"
                     :issuer "Qatar Digital Library"
                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 13
                                             :maxNativeZoom 18
                                             :opacity 0.7})}
   "1969 - MANAMAH" {:title "AL MANAMAH & AL JUFAYR"
                     :url "https://api.maptiler.com/tiles/9d3b9e4f-aa2a-4e5e-9301-d1aec153a104/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "Mosaic prepared by SPC RE in 1969 from air photography dated October 1968 and trig control. Names taken from K962 sheet Manama dated 1963. Names and cultural details raised from various sources 1969. This plan supersedes series K962 edit on 3.GSGS sheet Manama."
                     :notes "Excellent."
                     :source-link "/maps/1969-Manama.jpg"

                     :source "Ministry of Defence, United Kingdom, 1969"
                     :scale "1:5,000"
                     :issuer "Historic Maps of Bahrain 1817 - 1970"
                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 13
                                             :maxNativeZoom 18
                                             :opacity 0.7})}})

(def ar-layers
  {"1937 - BAHRAIN" {:title "جزيرة البحرين - ١٩٣٧ "
                     :description nil
                     :notes "الوثيقة الاصلية لم تنسخ و ترقم بشكل جيد. الخارطة تنحرف من الساحل الاصلي بشكل متزايد في جنوب الجزيرة"}
   "1969 - MANAMAH" {:title "المنامة و الجفير - ١٩٦٩"
                     :description nil
                     :notes "خارطة دقيقة من دون اي اشكاليات"}
   "1943 - BAHRAIN" {:title "جزيرة البحرين - ١٩٤٣"
                     :description nil
                     :notes "خارطة دقيقة بأخطاء بسيطة"}
   "1937 - MUHARRAQ" {:title "جزيرة المحرق - ١٩٣٧"
                      :description nil
                      :notes "خارطة دقيقة من دون اي اشكاليات"}
   "1931 - MUHARRAQ" {:title "مدينة المحرق - ١٩٣١"
                      :description nil
                      :notes "الخارطة دقيقة قرب سوق المحرق لكنها تنحذر بقرب بو ماهر في الجنوب "}
   "1937 - MANAMAH"  {:title "المنامة - ١٩٣٧"
                      :description nil
                      :notes "خارطة متوسطة الدقة. أخطاء بسيطة في غرب و جنوب الخارطة الأصلية"}})

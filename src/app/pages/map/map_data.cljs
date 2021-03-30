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
  {"1931 - Muharraq" {:title "MOHARRAK TOWN - March 1931"
                      :url "https://api.maptiler.com/tiles/a77144f2-7690-4c07-8852-3ac78dca0f35/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                      :description nil
                      :notes "Accurate in Old Muharraq. Drifts in the southern part of the map near Bu Maher."
                      :issuer-link "/maps/1931-Muharraq.jpg"
                      :source "Unknown"
                      :source-link "/maps/1931-Muharraq.tif"
                      :submitted-by "Ali Karimi"
                      :scale "1:2,400"
                      :issuer "Historic Maps of Bahrain 1817 - 1970 - Jarman"
                      :opts (merge base-opts {:maxZoom 20
                                              :minZoom 9
                                              :minNativeZoom 13
                                              :maxNativeZoom 18
                                              :opacity 0.7})}
   "1937 - Bahrain" {;; English
                     :title "BAHRAIN ISLAND - 1937"
                     :url "https://api.maptiler.com/tiles/1c3695ec-61ea-4e60-be6e-87bf60ceca38/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "Plan of Bahrain. The plan indicates hydrology, settlements and  as well as providing some indication of relief. Included is a table of reference of symbols used. Prepared by naval staff in the Admiralty's Naval Intelligence Division."
                     :notes "Original scan of document was distorted and outlines begin to diverge greatly further and further south."
                     :issuer-link "https://www.qdl.qa/en/archive/8955/vdc_90043097172.0x000077"
                     :source "British Library: India Office Records and Private Papers"
                     :source-link "/maps/1937-Bahrain.tif"
                     :issuer "Qatar Digital Library"
                     :scale "1:63,360"

                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 9
                                             :maxNativeZoom 14
                                             :opacity 0.7})}
   "1937 - Muharraq" {:title "MUHARRAQ ISLAND - Jan 1937"
                      :url "https://api.maptiler.com/tiles/7e37e696-7889-461f-81d3-8a3b5e5e0097/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                      :description "The map shows Muhurraq Island, and the causeway linking Muharraq to Manama. Also shown are principal roads, tracks, buildings (including the Political Agency in Manana), islands, and the location of several underwater freshwater springs. A rectangle on Muharraq Island indicates the area specified for the landing ground."
                      :notes "Well-preserved and decently accurate."
                      :source-link "/maps/1937-Muharraq.tif"
                      :source "British Library: India Office Records and Private Papers"
                      :scale "1:20,000"
                      :issuer "Qatar Digital Library"
                      :issuer-link "https://www.qdl.qa/en/archive/81055/vdc_100025608108.0x000080"
                      :opts (merge base-opts {:maxZoom 20
                                              :minZoom 9
                                              :minNativeZoom 11
                                              :maxNativeZoom 16
                                              :opacity 0.7})}
   "1937 - Manama" {:title "PLAN OF PORT AND TOWN - Manama 1937"
                    :url "https://api.maptiler.com/tiles/56815264-b7fe-4b4f-900d-979cb50ff777/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                    :description "Plan of Manamah, Bahrain. The plan indicates settlements, streets, buildings and landmarks. Prepared by naval staff in the Admiralty's Naval Intelligence Division."
                    :notes "Some map drift along the southern and western ends of the document. Mostly accurate."
                    :source-link "/maps/1937-Manama.tif"
                    :source "British Library: India Office Records and Private Papers"
                    :scale "1:3,639"
                    :issuer "Qatar Digital Library"
                    :issuer-link "https://www.qdl.qa/en/archive/81055/vdc_100043097172.0x000079"
                    :opts (merge base-opts {:maxZoom 20
                                            :minZoom 9
                                            :minNativeZoom 13
                                            :maxNativeZoom 18
                                            :opacity 0.7})}
   "1943 - Bahrain" {:title "BAHRAIN ISLAND - 1943"
                     :url "https://api.maptiler.com/tiles/48f771c1-2059-4d94-a863-7510c9434f2c/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "Shows physical features, residential places, cultivated land, selected buildings, marsh land, and major roads.
  Relief shown by landform drawings, contours, and spot heights. Compiled from Air photographs in A.H.Q. Drawing Office, Hinaidi, 1937. Drawn and Heliographed by O.S. 1939."
                     :notes "Well-preserved, undistorted scan. Map has relatively few inaccuracies."
                     :issuer-link "https://legacy.lib.utexas.edu/maps/ams/bahrein_island/txu-pclmaps-oclc-6559195-bahrein-island.jpg"
                     :source "US Army Map Service"
                     :source-link "/maps/1943-Bahrain.tif"
                     :issuer "University of Texas Library"
                     :scale "1:63,360"
                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 9
                                             :maxNativeZoom 14
                                             :opacity 0.7})}
   "1969 - Manama & AlJufayr" {:title "AL MANAMAH & AL JUFAYR - 1969"
                               :url "https://api.maptiler.com/tiles/99fd4a1f-991b-4f09-98f0-b2fc398f14a8/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                               :description "Mosaic prepared by SPC RE in 1969 from air photography dated October 1968 and trig control. Names taken from K962 sheet Manama dated 1963. Names and cultural details raised from various sources 1969. This plan supersedes series K962 edit on 3.GSGS sheet Manama."
                               :notes "Excellent condition 5-part map series. Required color corrections, some warping, stitching, and coordinates were offset from real location. "
                               :source-link "/maps/1969-Manama-stitched.zip"
                               :source "Ministry of Defence, United Kingdom, 1969"
                               :scale "1:5,000"
                               :issuer "Historic Maps of Bahrain 1817 - 1970 - Jarman"
                               :issuer-link "/maps/1969-Manama.zip"
                               :submitted-by "Ali Karimi"
                               :opts (merge base-opts {:maxZoom 20
                                                       :minZoom 9
                                                       :minNativeZoom 13
                                                       :maxNativeZoom 18
                                                       :opacity 0.7})}
   "1973 - Bahrain" {:title "Bahrain - 1973"
                     :url "https://api.maptiler.com/tiles/dd069021-aba4-4152-a013-2a7a242cfe00/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "Compiled and drawn by 42 Survey Engineer Regiment in 1971-2 from air photography dated 1969 and other sources. Control supplied by MCE RE and No 1 A(S)LS RE. Field check 1971 and revision in 1973 from air photography dated 1970. Names rendered in accordance with BGN/PCGN system of transliteration from Arabic. Projection: Traverse Mercator, Spheroid: Clarke 1880, Horizontal Datum: Nahrwan, Vertical Datum: MSL. Series K7610, Edition 1-GSGS. "
                     :notes "High resolution, with slight alignment issues. Projection is based off the Clarke 1880 Spheroid with a Datum in Nahrwan. Required post-processing/alignment work after translation and warping. Provided parameters appear to be off."
                     :source-link "/maps/1973-Bahrain.tif"
                     :source "Ministry of Defence, United Kingdom, 1973"
                     :scale "1:50,000"
                     :issuer "Unknown"
                     :issuer-link "/maps/1973-Bahrain.zip"
                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 10
                                             :maxNativeZoom 15
                                             :opacity 0.8})}
   "1977 - Bahrain" {:title "State of Bahrain - 1977"
                     :url "https://api.maptiler.com/tiles/7c1f9fdd-4d72-4dc2-a122-1c23bcc4057a/{z}/{x}/{y}.png?key=cAEzKPncCfFpXo43gfxY"
                     :description "This map has been compiled and drawn from photography taken during Jan/Feb 1977 by Fairey Surveys LTD., Reform Road, Maidenhead, Berkshire, England. for The State of Bahrain."
                     :notes "This is a 7 map series produced for the State of Bahrain. One of 3 series, the other two being the 1:50,000 and 1:10,000 series. Direct translation from UTM Zone 39 to Web Mercator with some raster massaging and stitching."
                     :source-link "/maps/1977-Bahrain-stitched.zip"
                     :source "Fairey Surveys Ltd"
                     :scale "1:25,000"
                     :issuer "Unknown"
                     :issuer-link "/maps/1977-Bahrain.zip"
                     :opts (merge base-opts {:maxZoom 20
                                             :minZoom 9
                                             :minNativeZoom 10
                                             :maxNativeZoom 15
                                             :opacity 0.8})}
   #_#_"1977 - Manama + Muharraq " {:title "Manama and Muharraq - 1977"
                                :url "https://mapwarper.net/maps/tile/55373/{z}/{x}/{y}.png"
                                :description "Description in Russian can be found in the bottom-right corner of the map."
                                :notes "Hand-rectified with control points. This map is two-parts manually stitched together. It's in the SK-42 projection, however we lacked the exact projection metadata required to translate to Web Mercactor. The hand rectified approach results in slight deviations all over the map, most pronounced in the Muharraq portion of the map."
                                :source-link "/maps/1977 - USSR - Manama & Muharraq.tif"
                                :source "USSR"
                                :scale "1:10,000"
                                :issuer "USSR"
                                :issuer-link "/maps/1977 - USSR - Manama & Muharraq.zip"
                                :opts (merge base-opts {:maxZoom 20
                                                        :minZoom 9
                                        #_  #_#_              :minNativeZoom 10
                                          #_              :maxNativeZoom 15
                                                        :opacity 0.8})}})

(def ar-layers
  {"1937 - Bahrain" {:title "جزيرة البحرين - ١٩٣٧ "
                     :description nil
                     :notes "الوثيقة الاصلية لم تنسخ و ترقم بشكل جيد. الخارطة تنحرف من الساحل الاصلي بشكل متزايد في جنوب الجزيرة"}
   "1969 - Manama & AlJufayr" {:title "المنامة و الجفير - ١٩٦٩"
                               :description nil
                               :submitted-by "علي كريمي"
                               :notes "سلسلة خرائط فيها ٥ أجزاء. تطلبت تصحيحات لالألوان، تصحيحات تشكيلية، ولم تطابق الإسقاط المحدد"}
   "1977 - Bahrain" {:title "البحرين - ١٩٧٧"
                     :description "تم تجميع ورسم هذه الخريطة من صور فوتوغرافية تم التقاطه خلال يناير/ فبراير١٩٧٧ بواسطة Fairey Surveys LTD من مقره على ريفورم رود ، مايدنهيد ، بيركشاير ، إنجلترا لدولة البحرين."
                     :notes " هذه الخارطة سلسلة فيها ٧ خرائط رسمت في ١٩٧٧. هذه السلسلة احد ثلاث تغطي البحرين انتجت في ١٩٧٧. ترجمة مباشرة من اسقاط UTM Zone 39 إلى Web Mercator مع معالجة بسيطة."}
   "1973 - Bahrain" {:title "البحرين - ١٩٧٣"
                     :description nil
                     :notes "خرائط عالية الجودة فيها مشاكل بسيطة في دقة المحاذة. الإسقاط الاصلي يعتمد على حسابات كلارك ١٨٨٠ و موقع مرجعي في نهروان. تم محاذة اضافية بعد الترجمة و التحوير بسبب اخطاء في البيانات المدونة في الخارطة"}
   "1943 - Bahrain" {:title "جزيرة البحرين - ١٩٤٣"
                     :description nil
                     :notes "خارطة دقيقة بأخطاء بسيطة"}
   "1937 - Muharraq" {:title "جزيرة المحرق - ١٩٣٧"
                      :description nil
                      :notes "خارطة دقيقة من دون اي اشكاليات"}
   "1931 - Muharraq" {:title "مدينة المحرق - ١٩٣١"
                      :description nil
                      :submitted-by "علي كريمي"
                      :notes "الخارطة دقيقة قرب سوق المحرق لكنها تنحذر بقرب بو ماهر في الجنوب "}
   "1937 - Manama"  {:title "المنامة - ١٩٣٧"
                     :description nil
                     :notes "خارطة متوسطة الدقة. أخطاء بسيطة في غرب و جنوب الخارطة الأصلية"}})

(ns app.pages.articles.index
  (:require [app.pages.articles.posts.wadi :as wadi]
            [app.pages.articles.posts.fairey :as fairey]
            [app.pages.articles.posts.processing-pipeline :as processing-pipeline]
            [app.util.core :refer [arabic-attrs bilingual-component]]))

(def entries
  [{:en-title "Wadi AlBuhair"
    :ar-title "وادي البحير"
    :en-description "The story of Wadi AlBuhair - a 45-million-year-old natural valley in Bahrain awaiting conservation amid urban destruction."
    :ar-description "قصة وادي البحير الذي ينتظر انتصار رغم كثرة أنصاره - واحة طبيعية في قلب البحرين تواجه تحدي التطوير العمراني."
    :en-keywords ["article" "Bahrain history" "historical maps" "Wadi AlBuhair"]
    :ar-keywords ["مقال" "تاريخ البحرين" "خرائط تاريخية" "وادي البحير"]
    :date #inst "2022-06-06T17:53:59.000Z"
    :route "wadi"
    :component wadi/article}
   {:en-title "Fairey Surveys — history of modern map-making in Bahrain"
    :ar-title "فايري سورڤيز - صناع خرائط البحرين الحديثة"
    :en-description "The history of Fairey Surveys and the creation of modern maps of independent Bahrain in the 1970s, shaping contemporary urban planning."
    :ar-description "تاريخ شركة فايري سورڤيز وإنتاج أول الخرائط الحديثة للبحرين بعد الاستقلال في السبعينيات، ودورها في تشكيل التخطيط العمراني الحديث."
    :en-keywords ["article" "Bahrain history" "historical maps" "Fairey Surveys"]
    :ar-keywords ["مقال" "تاريخ البحرين" "خرائط تاريخية" "فايري سورڤيز"]
    :date #inst "2024-04-09T20:53:59.000Z"
    :route "fairey"
    :component fairey/article}
   {:en-title "how to georeference a historical map"
    :ar-title "how to georeference a historical map"
    :en-description "A complete guide to transforming scanned historical maps into georeferenced, web-ready overlays using GDAL, QGIS, and related tools."
    :ar-description "A complete guide to transforming scanned historical maps into georeferenced, web-ready overlays using GDAL, QGIS, and related tools."
    :en-keywords ["article" "georeferencing" "GDAL" "QGIS" "map processing" "GeoTIFF" "MBTiles"]
    :ar-keywords ["article" "georeferencing" "GDAL" "QGIS" "map processing" "GeoTIFF" "MBTiles"]
    :date #inst "2026-02-06T00:00:00.000Z"
    :route "processing-pipeline"
    :component processing-pipeline/article}])

;; Generate article routes from entries - single source of truth
;; Pattern: {:route "wadi"} → {"wadi" :article-wadi}
(def article-routes
  (merge
    {""  :article-index}  ; Index route
    (into {}
      (map (fn [entry]
             [(:route entry) (keyword (str "article-" (:route entry)))])
           entries))))

(defn en []
  [:div.container.about
   [:h1.title "Articles"]
   [:div.articles-container
    (for [entry (sort-by :date #(compare %2 %1) entries)]
      [:div.box {:key (:route entry)}
       [:h3.title.is-4 [:a {:href (str (:route entry))}
                        (:en-title entry)]]
       [:p.subtitle.is-6
        [:span.icon [:i.far.fa-calendar-alt]]
        (.toLocaleDateString (:date entry) "en-US" #js{:year "numeric" :month "long" :day "numeric"})]
       [:p (:en-description entry)]])]])

(defn ar []
  [:div.container.about (arabic-attrs)
   [:h1.title "مقالات"]
   [:div.articles-container
    (for [entry (sort-by :date #(compare %2 %1) entries)]
      [:div.box {:key (:route entry)}
       [:h3.title.is-4 [:a {:href (str (:route entry))}
                        (:ar-title entry)]]
       [:p.subtitle.is-6
        [:span.icon [:i.far.fa-calendar-alt]]
        (.toLocaleDateString (:date entry) "ar-BH" #js{:year "numeric" :month "long" :day "numeric"})]
       [:p (:ar-description entry)]])]])

(def article-index
  (bilingual-component en ar))

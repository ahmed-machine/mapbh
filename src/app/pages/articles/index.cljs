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
   {:en-title "Fairey Surveys"
    :ar-title "فايري سورڤيز"
    :en-subtitle "history of modern map-making in Bahrain"
    :ar-subtitle "صناع خرائط البحرين الحديثة"
    :en-description "The history of Fairey Surveys and the creation of modern maps of independent Bahrain in the 1970s"
    :ar-description "تاريخ شركة فايري سورڤيز وإنتاج أول الخرائط الحديثة للبحرين بعد الاستقلال في السبعينيات، ودورها في تشكيل التخطيط العمراني الحديث."
    :en-keywords ["article" "Bahrain history" "historical maps" "Fairey Surveys"]
    :ar-keywords ["مقال" "تاريخ البحرين" "خرائط تاريخية" "فايري سورڤيز"]
    :date #inst "2024-04-09T20:53:59.000Z"
    :route "fairey"
    :component fairey/article}
   {:en-title "how to georeference a historical map"
    :ar-title "حوّل الخرائط التاريخية لبرامج اليوم"
    :en-description "A complete guide to transforming scanned historical maps into georeferenced, web-ready overlays using GDAL, QGIS, and related tools."
    :ar-description "دليل شامل لتحويل الخرائط التاريخية إلى طبقات جغرافية مرجعية جاهزة للعرض على الويب باستخدام GDAL وQGIS."
    :en-keywords ["article" "georeferencing" "GDAL" "QGIS" "map processing" "GeoTIFF" "MBTiles"]
    :ar-keywords ["مقال" "إسناد جغرافي" "GDAL" "QGIS" "معالجة خرائط" "GeoTIFF" "MBTiles"]
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
   [:div.columns.is-multiline.articles-container
    (for [entry (sort-by :date #(compare %2 %1) entries)]
      [:div.column.is-one-third-desktop.is-half-tablet {:key (:route entry)}
       [:div.article-card
        [:a {:href (str (:route entry))}
         [:div.article-card-content
          [:h3.article-card-title (:en-title entry)]
          [:p.article-card-subtitle (or (:en-subtitle entry) "\u00a0")]
          [:p.article-card-date
           [:span.icon.is-small [:i.far.fa-calendar-alt]]
           (.toLocaleDateString (:date entry) "en-US" #js{:year "numeric" :month "long" :day "numeric"})]
          [:p.article-card-description (:en-description entry)]]]]])]])

(defn ar []
  [:div.container.about (arabic-attrs)
   [:h1.title "مقالات"]
   [:div.columns.is-multiline.articles-container
    (for [entry (sort-by :date #(compare %2 %1) entries)]
      [:div.column.is-one-third-desktop.is-half-tablet {:key (:route entry)}
       [:div.article-card
        [:a {:href (str (:route entry))}
         [:div.article-card-content
          [:h3.article-card-title (:ar-title entry)]
          [:p.article-card-subtitle (or (:ar-subtitle entry) "\u00a0")]
          [:p.article-card-date
           [:span.icon.is-small [:i.far.fa-calendar-alt]]
           (.toLocaleDateString (:date entry) "ar-BH" #js{:year "numeric" :month "long" :day "numeric"})]
          [:p.article-card-description (:ar-description entry)]]]]])]])

(def article-index
  (bilingual-component en ar))

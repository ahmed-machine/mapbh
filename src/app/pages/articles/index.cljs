(ns app.pages.articles.index
  (:require [app.pages.articles.posts.wadi :as wadi]
            [app.pages.articles.posts.fairey :as fairey]
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
    :ar-description "تاريخ شركة فايري سورڤيز وإنتاج أول الخرائط الحديثة للبحرين المستقلة في السبعينيات، ودورها في تشكيل التخطيط العمراني الحديث."
    :en-keywords ["article" "Bahrain history" "historical maps" "Fairey Surveys"]
    :ar-keywords ["مقال" "تاريخ البحرين" "خرائط تاريخية" "فايري سورڤيز"]
    :date #inst "2024-04-09T20:53:59.000Z"
    :route "fairey"
    :component fairey/article}])

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
  [:div.container.articles
   [:h1.title "Articles"]
   (into [:ul] (for [entry entries]
                 [:li [:a {:href (str (:route entry))} (:en-title entry)]
                  " — " (.toLocaleDateString (:date entry))]))])

(defn ar []
  [:div.container.articles (arabic-attrs)
   [:h1.title "مقالات"]
   (into [:ul] (for [entry entries]
                 [:li [:a {:href (str (:route entry))} (:ar-title entry)]
                  " — " (.toLocaleDateString (:date entry))]))])

(def article-index
  (bilingual-component en ar))

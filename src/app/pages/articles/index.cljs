(ns app.pages.articles.index
  (:require [app.pages.articles.posts.wadi :as wadi]
            [app.pages.articles.posts.fairey :as fairey]))

(def entries
  [{:en-title "Wadi AlBuhair"
    :ar-title "وادي البحير"
    :date #inst "2022-06-06T17:53:59.000Z"
    :route "wadi"
    :ns app.pages.articles.posts.wadi}
   {:en-title "Fairey Surveys — history of modern map-making in Bahrain"
    :ar-title "فايري سورڤيز - صناع خرائط البحرين الحديثة"
    :date #inst "2024-04-09T20:53:59.000Z"
    :route "fairey"
    :ns app.pages.articles.posts.fairey}])

(defn en []
  [:div.container.articles
   [:h1.title "Articles"]
   (into [:ul] (for [entry entries]
                 [:li [:a {:href (str (:route entry))} (:en-title entry)]
                  " — " (.toLocaleDateString (:date entry))]))])

(defn ar []
  [:div.container.articles {:dir "rtl" :lang "ar" :style {:font-size "125%"}}
   [:h1.title "مقالات"]
   (into [:ul] (for [entry entries]
                 [:li [:a {:href (str (:route entry))} (:ar-title entry)]
                  " — " (.toLocaleDateString (:date entry))]))])

(ns app.pages.articles.index
  (:require [app.pages.articles.posts.wadi :as wadi]))

(def entries
  [{:en-title "Wadi AlBuhair"
    :ar-title "وادي البحير"
    :date #inst "2022-06-06T17:53:59.000Z"
    :route "wadi"
    :ns app.pages.articles.posts.wadi}])

(defn en []
  [:div.container.articles
   [:h1.title "Articles"]
   (into [:ul] (for [entry entries]
                 [:li [:a {:href (str (:route entry))} (:en-title entry)]
                  " — " (.toLocaleDateString (:date entry))]))])

(defn ar [] (en))

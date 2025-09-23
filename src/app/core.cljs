(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [app.pages.about :as about]
            [app.pages.contribute :as contribute]
            [app.pages.homepage :as homepage]
            [app.pages.catalogue :as catalogue]
            [app.pages.map-info :as map-info]
            [app.pages.articles.index :as article-index]
            [app.pages.map :refer [historical-map]]
            [app.components.nav :as nav]
            [app.routes :as routes]
            [app.model :as model]
            [re-frame.core :as rf]))

(def articles-map (->> (for [post article-index/entries]
                     [(keyword (str "article-" (:route post)))
                      (:ns post)])
                   (into [])
                   (map (fn [[kw n]]
                          [kw (->> (js->clj n :keywordize-keys true)
                                   (map (fn [[lng-kw object]]
                                          [lng-kw [object]]))
                                   (into {}))]))
                   (into {})))


(defn- panels [panel-name route-params]
  (-> {:home [homepage/homepage]
       :map [historical-map]
       :about [about/about]
       :catalogue [catalogue/catalogue]
       :map-info (let [group (get route-params :group)
                       map-id (get route-params :map-id)]
                   [map-info/map-info group map-id])
       :contribute [contribute/contribute]
       :article-index [article-index/article-index]}
      (merge (into {} (for [[k v] articles-map] [k (first (vals v))])))
      (get panel-name [homepage/homepage])))


(defn ui []
  (let [language (rf/subscribe [::model/language])
        ap       (rf/subscribe [::model/active-panel])
        rp       (rf/subscribe [::model/route-params])]
    (fn []
      [:<>
       (if (some #{@ap} `(:home)) nil [nav/top @language])
       [panels @ap @rp]
       (if (some #{@ap} `(:map :home)) nil [nav/footer @language])])))


(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [ui]
            (.getElementById js/document "app")))


(defn ^:export main
  "Run application startup logic."
  []
  (routes/app-routes)
  (render))

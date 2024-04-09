(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [app.util :as util]
            [app.pages.about :as about]
            [app.pages.contribute :as contribute]
            [app.pages.homepage :as homepage]
            [app.pages.articles.index :as article-index]
            [app.pages.map.map-history :refer [historical-map]]
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


(defn- panels [panel-name language]
  (-> {:home  {:en [homepage/en] :ar [homepage/ar]}
       :map   {:en [historical-map] :ar [historical-map]}
       :about {:en [about/en] :ar [about/ar]}
       :contribute {:en [contribute/en] :ar [contribute/ar]}
       :article-index {:en [article-index/en] :ar [article-index/ar]}}
      (merge articles-map)
      (get panel-name {:en [homepage/en] :ar [homepage/ar]})
      (get language)))


(defn ui []
  (let [language (rf/subscribe [::model/language])
        ap       (rf/subscribe [::model/active-panel])]
    (fn []
      [:<>
       (if (some #{@ap} `(:home)) nil [nav/top @language])
       [panels  @ap @language]
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

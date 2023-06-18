(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [app.util :as util]
            [app.pages.about :as about]
            [app.pages.contribute :as contribute]
            [app.pages.homepage :as homepage]
            [app.pages.catalogue :as catalogue]
            [app.pages.blog.wadi :as blog-wadi]
            [app.pages.blog.index :as blog-index]
            ;;[app.pages.blog.fairey :as blog-fairey]
            [app.pages.map.map-history :refer [historical-map]]
            [app.components.nav :as nav]
            [app.routes :as routes]
            [app.model :as model]
            [re-frame.core :as rf]))

(defn- panels [panel-name] ;; TODO: Simplify this
  (condp = @(rf/subscribe [::model/language])
    "en"  (case panel-name
      :home [homepage/en]
      :map [historical-map]
      :about [about/en]
      :contribute [contribute/en]
      :catalogue [catalogue/en]
      ;; BLOG
      :blog-index [blog-index/en]
      :blog-wadi [blog-wadi/en]
      ;;:blog-fairey [blog-fairey/en]
      [homepage/en])
    (case panel-name
      :home [homepage/ar]
      :map [historical-map]
      :about [about/ar]
      :contribute [contribute/ar]
      :catalogue [catalogue/ar]
      ;; BLOG
      :blog-index [blog-index/ar]
      :blog-wadi [blog-wadi/ar]
      ;;:blog-fairey [blog-fairey/ar
      [homepage/ar])))



(defn show-panel [panel-name]
  [panels panel-name])


(defn main-panel []
  (let [active-panel* (rf/subscribe [::model/active-panel])]
    [show-panel @active-panel*]))


(defn ui []
  (let [language (or @(rf/subscribe [::model/language]) "ar")
        ap       (or @(rf/subscribe [::model/active-panel]) :home)]
    [:<>
     (when (and (not= ap nil) (not= ap :home))
       (if (= "ar" language) [nav/top-ar] [nav/top-en]))
     [main-panel]
     (when (and (not= ap nil) (not= ap :home) (not= ap :map))
       (if (= "ar" language) [nav/footer-ar] [nav/footer-en]))]))


(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [ui] (.getElementById js/document "app")))


(defn ^:export main
  "Run application startup logic."
  []
  (routes/app-routes)
  (render))

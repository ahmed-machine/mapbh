(ns app.model
  (:require [re-frame.core :as rf]
            [app.pages.articles.index :as article-index]))

(def routes ["/" {[:language "/"] {""           :home
                                   "about"      :about
                                   "map"        :map
                                   "map-info"   :map-info
                                   "catalogue"  :catalogue
                                   "contribute" :contribute
                                   "articles/"  article-index/article-routes}}])

(rf/reg-sub
 ::active-panel
 (fn [db _]
   (or (:active-panel db) :home)))

(rf/reg-sub
 ::language
 (fn [db _]
   (or (:language db) :ar)))

(rf/reg-sub
 ::route-params
 (fn [db _]
   (:route-params db)))

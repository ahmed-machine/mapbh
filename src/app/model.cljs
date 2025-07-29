(ns app.model
  (:require [re-frame.core :as rf]))

;; Slugs for URLs
(def article-routes
  {""       :article-index
   "wadi"   :article-wadi
   "fairey" :article-fairey})

(def routes ["/" {"wadi"  {"" :article-wadi ;; To be deprecated once traffic drops
                           ["/" :language] :article-wadi}
                  [:language "/"] {""           :home
                                   "about"      :about
                                   "dialects"   :dialects
                                   "map"        :map
                                   "map-info"   :map-info
                                   "catalogue"  :catalogue
                                   "contribute" :contribute
                                   "articles/"   article-routes}}])

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

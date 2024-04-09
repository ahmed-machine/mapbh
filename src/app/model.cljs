(ns app.model
  (:require [re-frame.core :as rf]))

;; Slugs for URLs
(def article-routes
  {""      :article-index
   "wadi"  :article-wadi})

(def routes ["/" {"wadi"  {"" :article-wadi ;; To be deprecated once traffic drops
                           ["/" :language] :article-wadi}
                  [:language "/"] {""           :home
                                   "about"      :about
                                   "dialects"   :dialects
                                   "map"        :map
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

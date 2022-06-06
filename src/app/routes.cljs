(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]))

(def routes ["/" {"wadi" {"" :wadi
                          ["/" :language] :wadi}
                  [:language "/"] {""           :home
                                   "about"      :about
                                   "dialects"   :dialects
                                   "map"        :map
                                   "contribute" :contribute}}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route))))
        language (-> matched-route :route-params :language)]
    (rf/dispatch [::events/set-language language])
    (rf/dispatch [::events/set-active-panel panel-name])))

(defn app-routes[]
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (fn [route] (bidi/path-for routes route :language (or @(rf/subscribe [::model/language]) "ar"))))

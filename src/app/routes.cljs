(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]))

(def url-for (fn [route] (bidi/path-for model/routes route :language @(rf/subscribe [::model/language]))))

(defn- dispatch-route [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route))))
        language (-> matched-route :route-params :language)]
    (rf/dispatch [::events/set-language language])
    (rf/dispatch [::events/set-active-panel panel-name])))

(def history
  (pushy/pushy dispatch-route (partial bidi/match-route model/routes)))

(defn app-routes []
  (pushy/start! history))

(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]
            [app.util.url :as url]))

(def url-for (fn [route] (bidi/path-for model/routes route :language @(rf/subscribe [::model/language]))))

(defn- dispatch-route [matched-route]
  (if matched-route
    (let [panel-name (keyword (str (name (:handler matched-route))))
          route-params (:route-params matched-route)
          ;; Parse query parameters for routes that need them
          query-params (url/get-query-params)
          final-params (if query-params
                         (merge route-params query-params)
                         route-params)]
      (rf/dispatch [::events/set-route-params final-params])
      (rf/dispatch [::events/set-active-panel panel-name]))
    (rf/dispatch [::events/set-active-panel :home])))

(def history
  (pushy/pushy dispatch-route (partial bidi/match-route model/routes)))

(defn app-routes []
  (pushy/start! history))

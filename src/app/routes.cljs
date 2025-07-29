(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]))

(def url-for (fn [route] (bidi/path-for model/routes route :language @(rf/subscribe [::model/language]))))

(defn- parse-query-params
  "Parse query parameters into a map for any route that needs them"
  []
  (try
    (when-let [search (.-search js/window.location)]
      (when (and (> (.-length search) 0) (.startsWith search "?"))
        (let [url-params (js/URLSearchParams. search)
              params (js/Object.fromEntries url-params)]
          (js->clj params :keywordize-keys true))))
    (catch js/Error e
      nil)))

(defn- dispatch-route [matched-route]
  (if matched-route
    (let [panel-name (keyword (str (name (:handler matched-route))))
          route-params (:route-params matched-route)
          ;; Parse query parameters for routes that need them
          query-params (parse-query-params)
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

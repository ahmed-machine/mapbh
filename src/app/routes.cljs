(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]))

(def url-for (fn [route] (bidi/path-for model/routes route :language @(rf/subscribe [::model/language]))))

(defn- dispatch-route [matched-route]
  (if matched-route
    (let [panel-name (keyword (str (name (:handler matched-route))))
          route-params (:route-params matched-route)
          ;; Extract query parameters specifically for map-info page
          final-params (if (and (= (:handler matched-route) :map-info)
                               (.-search js/window.location)
                               (> (.-length (.-search js/window.location)) 0)
                               (let [search (.-search js/window.location)]
                                 (and (.includes search "group=")
                                      (.includes search "map-id="))))
                         (try
                           (let [url-params (js/URLSearchParams. (.-search js/window.location))
                                 group (.get url-params "group")
                                 map-id (.get url-params "map-id")]
                             (if (and group map-id)
                               (assoc route-params :group group :map-id map-id)
                               route-params))
                           (catch js/Error e
                             route-params))
                         route-params)]
      (rf/dispatch [::events/set-route-params final-params])
      (rf/dispatch [::events/set-active-panel panel-name]))
    (rf/dispatch [::events/set-active-panel :home])))

(def history
  (pushy/pushy dispatch-route (partial bidi/match-route model/routes)))

(defn app-routes []
  (pushy/start! history))

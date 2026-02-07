(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [clojure.string :as str]
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

(defn- match-route
  "Match a URL path against the app routes. If no match is found and the path
   has a trailing slash, retry without it. This handles nginx's automatic
   trailing-slash redirect when serving pre-rendered article HTML files."
  [path]
  (or (bidi/match-route model/routes path)
      (when (and (> (count path) 1) (str/ends-with? path "/"))
        (bidi/match-route model/routes (subs path 0 (dec (count path)))))))

(def history
  (pushy/pushy dispatch-route match-route))

(defn app-routes []
  (pushy/start! history))

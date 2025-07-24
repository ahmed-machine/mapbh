(ns app.util.url
  "URL parameter utilities for shareable map links"
  (:require [app.pages.map.map-data :refer [default-map-state]]))


(defn get-query-params
  "Parse URL query parameters into a map"
  []
  (let [search (-> js/window .-location .-search)
        params-str (if (.startsWith search "?")
                     (.substring search 1)
                     search)]
    (if (empty? params-str)
      {}
      (->> (.split params-str "&")
           (map #(.split % "="))
           (filter #(= 2 (count %)))
           (map (fn [[k v]]
                  [(keyword k) (js/decodeURIComponent v)]))
           (into {})))))

(defn set-query-params!
  "Update URL query parameters without page reload"
  [params]
  (let [search-params (js/URLSearchParams.)
        current-url (-> js/window .-location .-href)]
    (doseq [[k v] params]
      (when (some? v)
        (.set search-params (name k) (str v))))
    (let [new-url (str (-> js/window .-location .-origin)
                       (-> js/window .-location .-pathname)
                       "?" (.toString search-params))]
      (.replaceState js/window.history nil "" new-url))))

(defn parse-coords
  "Parse coordinate string 'lat,lng' to [lat lng]"
  [coord-str]
  (when coord-str
    (try
      (let [[lat lng] (map js/parseFloat (.split coord-str ","))]
        (when (and (not (js/isNaN lat)) (not (js/isNaN lng)))
          [lat lng]))
      (catch js/Error e
        nil))))

(defn parse-zoom
  "Parse zoom level string to number"
  [zoom-str]
  (when zoom-str
    (try
      (let [zoom (js/parseFloat zoom-str)]
        (when (and (not (js/isNaN zoom)) (>= zoom 0) (<= zoom 20))
          zoom))
      (catch js/Error e
        nil))))

(defn parse-transparency
  "Parse transparency value string to number between 0 and 1"
  [transparency-str]
  (when transparency-str
    (try
      (let [transparency (js/parseFloat transparency-str)]
        (when (and (not (js/isNaN transparency)) (>= transparency 0) (<= transparency 1))
          transparency))
      (catch js/Error e
        nil))))

(defn url-decode-map-id
  "URL decode and validate map ID"
  [map-id-str]
  (when map-id-str
    (try
      (js/decodeURIComponent map-id-str)
      (catch js/Error e
        nil))))

(defn url-encode-map-id
  "URL encode map ID for safe inclusion in URL"
  [map-id]
  (when map-id
    (js/encodeURIComponent map-id)))


(defn find-layer-by-map-id
  "Find layer group and map ID that matches the given map-id string"
  [map-data map-id]
  (when map-id
    (some (fn [[group-name layers]]
            (some (fn [[layer-key layer-data]]
                    (when (= (name layer-key) map-id)
                      [group-name (name layer-key)]))
                  layers))
          map-data)))

(defn parse-url-params
  "Parse URL parameters into map state, with fallbacks to defaults"
  [map-data]
  (try
    (let [params (get-query-params)]
      (if (empty? params)
        {} ; Return empty map if no URL params, let component use its defaults
        (let [coords (parse-coords (:coords params))
              [lat lng] (or coords [(:lat default-map-state) (:lng default-map-state)])
              zoom (or (parse-zoom (:zoom params)) (:zoom default-map-state))
              map-id (url-decode-map-id (:map params))
              selected-layer (or (find-layer-by-map-id map-data map-id)
                                [(:group default-map-state) (:map-id default-map-state)])
              mode (get params :mode (:mode default-map-state))
              base (get params :base (:base default-map-state))
              transparency (or (parse-transparency (:transparency params))
                              (:transparency default-map-state))]
          (cond-> {}
            selected-layer (assoc :selected selected-layer)
            lat (assoc :lat lat)
            lng (assoc :lng lng)
            zoom (assoc :zoom zoom)
            mode (assoc :mode mode)
            base (assoc :base base)
            transparency (assoc :transparency transparency)))))
    (catch js/Error e
      (.warn js/console "Error parsing URL parameters:" e)
      {})))

(defn truncate-decimal
  "Truncate a number to specified decimal places"
  [n decimal-places]
  (when n
    (let [multiplier (js/Math.pow 10 decimal-places)]
      (/ (js/Math.floor (* n multiplier)) multiplier))))

(defn serialize-map-state
  "Serialize current map state to URL parameters"
  [state]
  (let [{:keys [selected lat lng zoom mode base transparency]} state
        [group map-id] selected
        ;; Truncate lat/lng to 6 decimal places for cleaner URLs
        truncated-lat (truncate-decimal lat 5)
        truncated-lng (truncate-decimal lng 5)]
    (cond-> {}
      map-id (assoc :map (url-encode-map-id map-id))
      (and truncated-lat truncated-lng) (assoc :coords (str truncated-lat "," truncated-lng))
      zoom (assoc :zoom zoom)
      (not= mode (:mode default-map-state)) (assoc :mode mode)
      base (assoc :base base)
      (not= transparency (:transparency default-map-state)) (assoc :transparency transparency))))

(defn update-url-from-map-state!
  "Update browser URL with current map state"
  [state]
  (let [params (serialize-map-state state)]
    (set-query-params! params)))

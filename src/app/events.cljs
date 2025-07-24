(ns app.events
  (:require [re-frame.core :as re-frame]
             [app.model :as model]
             [bidi.bidi :as bidi]
             [clojure.string :as str]))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel (keyword active-panel))))

(defn ^:private get-current-panel-from-url
  "Extract current panel from URL as fallback when active-panel is unreliable"
  []
  (try
    (let [pathname (-> js/window .-location .-pathname)
          matched (bidi/match-route model/routes pathname)]
      (when matched (:handler matched)))
    (catch js/Error _
      :home)))

(defn ^:private filter-relevant-params
  "Filter query parameters to only include map-related ones"
  [search-string]
  (when (and search-string (not (empty? search-string)))
    (let [params-str (if (.startsWith search-string "?")
                       (.substring search-string 1)
                       search-string)
          relevant-params #{"map" "coords" "zoom" "mode" "base" "transparency"}]
      (when (not (empty? params-str))
        (let [filtered-pairs (->> (.split params-str "&")
                                  (map #(.split % "="))
                                  (filter #(= 2 (count %)))
                                  (filter #(contains? relevant-params (first %))))]
          (when (seq filtered-pairs)
            (str "?" (->> filtered-pairs
                          (map #(str/join "=" %))
                          (str/join "&")))))))))

(re-frame/reg-event-fx
 ::set-language
 (fn [{:keys [db]} [_ language]]
   (let [current-panel (or (get-current-panel-from-url)
                           (:active-panel db)
                           :home)
         new-db (assoc db :language (keyword language))]
     (try
       (let [current-search (-> js/window .-location .-search)
             filtered-search (filter-relevant-params current-search)
             new-url (str (bidi/path-for model/routes current-panel :language (keyword language))
                         (or filtered-search ""))]
         {:db new-db
          :history-push new-url})
       (catch js/Error e
         (.warn js/console "Language switch failed:" e)
         {:db new-db})))))

;; Effect handler for navigation using History API
(re-frame/reg-fx
 :history-push
 (fn [url]
   ;; Just update the URL without triggering navigation
   (.pushState js/window.history nil "" url)))

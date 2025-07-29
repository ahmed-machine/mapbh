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


(re-frame/reg-event-fx
 ::set-route-params
 (fn [{:keys [db]} [_ rp]]
   (let [current-panel (or (get-current-panel-from-url)
                           (:active-panel db)
                           :home)
         language (:language rp)
         new-db (assoc db :language (keyword language) :route-params rp)]
     (try
       (let [current-search (-> js/window .-location .-search)
             new-url (str (bidi/path-for model/routes current-panel :language (keyword language))
                         (or current-search ""))]
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

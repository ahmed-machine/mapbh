(ns app.events
  (:require [re-frame.core :as re-frame]
             [app.model :as model]))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel (keyword active-panel))))

(re-frame/reg-event-db
 ::set-language
 (fn [db [_ language]]
   (assoc db :language (keyword language))))

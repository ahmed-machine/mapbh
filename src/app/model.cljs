(ns app.model
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(rf/reg-sub
 ::language
 (fn [db _]
   (:language db)))

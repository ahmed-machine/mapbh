(ns app.util.core
  (:require [clojure.string :as str]))

(defn ->js [var-name]
      (-> var-name
          (str/replace #"/" ".")
          (str/replace #"-" "_")))


(defn invoke [function-name & args]
      (let [fun (js/eval (->js function-name))]
           (apply fun args)))

(defn arabic-attrs
  "Returns standard RTL attributes for Arabic content with 1.4rem font size.
   Optional style map can be provided to merge with base attributes."
  ([]
   {:dir "rtl" :lang "ar" :style {:font-size "1.4rem"}})
  ([extra-style]
   {:dir "rtl" :lang "ar" :style (merge {:font-size "1.4rem"} extra-style)}))
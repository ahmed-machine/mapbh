(ns app.util.core
  (:require [clojure.string :as str]))

(defn ->js [var-name]
      (-> var-name
          (str/replace #"/" ".")
          (str/replace #"-" "_")))


(defn invoke [function-name & args]
      (let [fun (js/eval (->js function-name))]
           (apply fun args)))
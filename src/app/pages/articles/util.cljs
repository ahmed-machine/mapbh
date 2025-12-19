(ns app.pages.articles.util
  (:require [clojure.string :as str]))

(defn image
  [src alt caption]
  [:figure.image
   {:style {:text-align :center}}
   [:a {:href src} [:img {:alt alt :src src}]]
   [:figcaption caption]])


(defn text->paragraphs
  [s]
  (let [s (str/split s "\n")]
    (for [x s]
      (println (str "[:p " x "] \n")))))

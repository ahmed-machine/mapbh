(ns app.util.core
  (:require [clojure.string :as str]
            [re-frame.core :as rf]))

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

(defn rtl-attrs
  "Returns RTL attributes for Arabic content without font-size styling.
   Use this for elements that should inherit font-size from parent.
   Optional style map can be provided to merge with base attributes."
  ([]
   {:dir "rtl" :lang "ar"})
  ([extra-style]
   {:dir "rtl" :lang "ar" :style extra-style}))

(defn bilingual-component
  "Creates a Form-2 component that renders different content based on language.

   Args:
     en-component - Function that returns English content (hiccup)
     ar-component - Function that returns Arabic content (hiccup)

   Returns:
     A Form-2 Reagent component that subscribes to language state internally
     and renders the appropriate language component.

   Example:
     (def my-page
       (bilingual-component
         (fn [] [:div.container.en \"English content\"])
         (fn [] [:div.container.ar (arabic-attrs) \"Arabic content\"])))"
  [en-component ar-component]
  (fn []
    (let [language* (rf/subscribe [:app.model/language])]
      (fn []
        (let [language @language*]
          (if (= language :ar)
            [ar-component]
            [en-component]))))))
(ns app.util.core
  (:require [re-frame.core :as rf]))

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

(defn icon-margin
  "Returns appropriate margin for icons based on language direction.
   In English (LTR), icons have margin-right.
   In Arabic (RTL), icons have margin-left.

   Args:
     language - Current language keyword (:en or :ar)
     size - Optional margin size (default \"0.5rem\")

   Returns:
     Style map with appropriate margin

   Example:
     [:i.fas.fa-download {:style (icon-margin language)}]"
  ([language]
   (icon-margin language "0.5rem"))
  ([language size]
   (if (= language :ar)
     {:margin-left size :padding "0.2rem"}
     {:margin-right size :padding "0.2rem"})))
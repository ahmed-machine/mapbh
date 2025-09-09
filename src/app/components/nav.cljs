(ns app.components.nav
  (:require [app.routes :as routes]
            [app.events :as events]
            [app.model :as model]
            [re-frame.core :as rf]))

(def nav-items-en
  [{:key :about :label "About" :route :about}
   {:key :article-index :label "Articles" :route :article-index}
   {:key :catalogue :label "Catalogue" :route :catalogue}
   {:key :contribute :label "Contribute" :route :contribute :class "contribute-button"}
   {:key :map :label "Map" :route :map}])

(def nav-items-ar
  [{:key :map :label "الخارطة" :route :map}
   {:key :about :label "نبذة" :route :about}
   {:key :article-index :label "مقالات" :route :article-index}
   {:key :catalogue :label "فهرس" :route :catalogue}
   {:key :contribute :label "ساهم" :route :contribute :class "contribute-button"}])

(defn render-nav-item
  [active-panel {:keys [key label route class]}]
  (let [base-classes "navbar-item"
        classes (cond-> base-classes
                  class (str " " class)
                  (= active-panel key) (str " is-active"))]
    [:a {:href (routes/url-for route)
         :class classes}
     label]))

(defn language-switcher
  [is-arabic]
  (if is-arabic
    [:a.navbar-item {:style {:font-family "Roboto, Helvetica, san serif" :font-size "0.8em" :display :flex :align-items :center}
                     :on-click #(rf/dispatch [::events/set-route-params {:language "en"}])} "English"]
    [:a.navbar-item {:style {:font-family "Amiri, Scheherazade, serif" :display :flex :align-items :center}
                     :on-click #(rf/dispatch [::events/set-route-params {:language "ar"}])} "العربية"]))

(defn top-navbar
  [is-arabic]
  (let [active-panel @(rf/subscribe [::model/active-panel])
        nav-items (if is-arabic nav-items-ar nav-items-en)
        nav-class (if is-arabic "navbar-end" "navbar-start is-vcentered")]
    [:nav.navbar.is-fixed-top (when is-arabic {:lang "ar" :direction "rtl"})
     [:div.navbar-brand
      [:a.nav-logo.is-vcentered {:href (routes/url-for :home)}
       [:p.column.logo (merge {:style {:color :black :z-index 2}}
                              (when is-arabic {:style {:color :black :font-family "Comfortaa" :z-index 2}}))
        "mapBH"]]]
     [:div.navbar-menu.is-active {:style {:font-size (if is-arabic "1.1em" "0.8em")}}
      [:div {:class nav-class}
       (when is-arabic [language-switcher is-arabic])
       (for [item nav-items]
         ^{:key (:key item)} [render-nav-item active-panel item])
       (when-not is-arabic [language-switcher is-arabic])]]]))

(defn top-en []
  [top-navbar false])

(defn top-ar []
  [top-navbar true])

(defn top
  [language]
  (condp = language
    :ar [top-ar]
    :en [top-en]
    [top-ar]))

(def social-links
  [{:href "https://twitter.com/map_bh" :icon "fab fa-twitter"}
   {:href "https://github.com/ahmed-machine/mapbh" :icon "fab fa-github"}
   {:href "mailto:mapbh.org@gmail.com" :icon "fas fa-envelope"}
   {:href "https://instagram.com/map_bh" :icon "fab fa-instagram"}])

(defn footer-content []
  [:div.content.has-text-centered
   (for [link social-links]
     ^{:key (:href link)}
     [:span.icon [:a {:style {:color :black}
                      :href (:href link)} [:i {:class (:icon link)}]]])])

(defn footer-en []
  [:footer.footer
   [footer-content]])

(defn footer-ar []
  [:footer.footer {:lang "ar" :direction "rtl"}
   [footer-content]])

(defn footer
  [language]
  (condp = language
    :ar [footer-ar]
    :en [footer-en]
    [footer-ar]))
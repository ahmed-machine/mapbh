(ns app.components.nav
  (:require [app.routes :as routes]
            [app.events :as events]
            [app.model :as model]
            [app.util.core :refer [rtl-attrs]]
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
  [:a.language-toggle
   {:on-click #(rf/dispatch [::events/set-route-params
                            {:language (if is-arabic "en" "ar")}])
    :style {:cursor "pointer"
            :padding-left "1rem"
            :padding-right "1rem"
            :background "none"
            :border "none"
            :font-size "1.5rem"}}
   [:span {:style (merge {:opacity (if is-arabic 0.5 1)}
                         (when-not is-arabic {:font-weight 700}))} "en"]
   [:span {:style {:opacity 0.5}} "/" ]
   [:span.arabic-text {:style (merge {:opacity (if is-arabic 1 0.5)}
                                     (when is-arabic {:font-weight 700}))} "ع"]])

(defn top
  "Top navigation bar - Form-2 component with internal language subscription"
  []
  (let [language* (rf/subscribe [::model/language])
        active-panel* (rf/subscribe [::model/active-panel])]
    (fn []
      (let [language @language*
            active-panel @active-panel*
            is-arabic (= language :ar)
            nav-items (if is-arabic nav-items-ar nav-items-en)
            nav-class (if is-arabic "navbar-end" "navbar-start is-vcentered")]
        [:nav.navbar.is-fixed-top (when is-arabic (merge (rtl-attrs) {:style {:direction "rtl"}}))
         [:div.navbar-brand
          [:a.nav-logo.is-vcentered {:href (routes/url-for :home)}
           [:p.column.logo (merge {:style {:color :black :z-index 2}}
                                  (when is-arabic {:style {:color :black :font-family "Comfortaa" :z-index 2}}))
            "mapBH"]]]
         [:div.navbar-menu.is-active {:style {:font-size (if is-arabic "1.1em" "0.8em")}}
          ;; Main nav items
          [:div {:class nav-class}
           (for [item nav-items]
             ^{:key (:key item)} [render-nav-item active-panel item])]
          ;; Language switcher in opposite corner
          [:div {:class (if is-arabic "navbar-start" "navbar-end")}
           [language-switcher is-arabic]]]]))))

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

(defn footer
  "Footer navigation - Form-2 component with internal language subscription"
  []
  (let [language* (rf/subscribe [::model/language])]
    (fn []
      (let [is-arabic (= @language* :ar)]
        [:footer.footer (when is-arabic (merge (rtl-attrs) {:style {:direction "rtl"}}))
         [footer-content]]))))

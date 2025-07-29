(ns app.components.nav
  (:require [app.routes :as routes]
            [app.events :as events]
            [app.model :as model]
            [re-frame.core :as rf]))

(defn top-en
  []
  (let [active-panel @(rf/subscribe [::model/active-panel])]
    [:nav.navbar.is-fixed-top
     [:div.navbar-brand
      [:a.nav-logo.is-vcentered {:href (routes/url-for :home)}
       [:p.column.logo {:style {:color :black
                                :z-index 2}} "mapBH"]]]
     [:div.navbar-menu.is-active {:style {:font-size "0.8em"}}
      [:div.navbar-start.is-vcentered
       [:a.navbar-item {:href (routes/url-for :about)
                        :class (when (= active-panel :about) "is-active")} "About"]
       [:a.navbar-item {:href (routes/url-for :article-index)
                        :class (when (= active-panel :article-index) "is-active")} "Articles"]
       [:a.navbar-item {:href (routes/url-for :catalogue)
                        :class (when (= active-panel :catalogue) "is-active")} "Catalogue"]
       [:a.navbar-item.contribute-button {:href (routes/url-for :contribute)
                                          :class (when (= active-panel :contribute) "is-active")} "Contribute"]
       [:a.navbar-item {:href (routes/url-for :map)
                        :class (when (= active-panel :map) "is-active")} "Map"]
       [:a.navbar-item {:style {:font-family "Amiri, Scheherazade, serif" :display :flex :align-items :center}
                        :on-click #(rf/dispatch [::events/set-route-params {:language "ar"}])} "العربية"]]]]))


(defn top-ar
  []
  (let [active-panel @(rf/subscribe [::model/active-panel])]
    [:nav.navbar.is-fixed-top {:lang "ar" :direction "rtl"}
     [:div.navbar-brand
      [:a.nav-logo.is-vcentered {:href (routes/url-for :home)}
       [:p.column.logo {:style {:color :black
                                :font-family "Comfortaa"
                                :z-index 2}} "mapBH"]]]
     [:div.navbar-menu.is-active {:style {:font-size "1.1em"}}
      [:div.navbar-end
       [:a.navbar-item {:style {:font-family "Roboto, Helvetica, san serif" :font-size "0.8em" :display :flex :align-items :center}
                        :on-click #(rf/dispatch [::events/set-route-params {:language "en"}])} "English"]
       [:a.navbar-item.contribute-button {:href (routes/url-for :contribute)
                                          :class (when (= active-panel :contribute) "is-active")} "ساهم"]
       [:a.navbar-item {:href (routes/url-for :catalogue)
                        :class (when (= active-panel :catalogue) "is-active")} "فهرس"]
       [:a.navbar-item {:href (routes/url-for :article-index)
                        :class (when (= active-panel :article-index) "is-active")} "مقالات"]
       [:a.navbar-item {:href (routes/url-for :about)
                        :class (when (= active-panel :about) "is-active")} "نبذة"]
       [:a.navbar-item {:href (routes/url-for :map)
                        :class (when (= active-panel :map) "is-active")} "الخارطة"]]]]))

(defn top
  [language]
  (condp = language
    :ar [top-ar]
    :en [top-en]
    [top-ar]))

(defn footer-en
  []
  [:footer.footer
    [:div.content.has-text-centered
     [:span.icon [:a {:style {:color :black}
                      :href "https://twitter.com/map_bh"} [:i.fab.fa-twitter]]]
     [:span.icon [:a {:style {:color :black}
                      :href "https://github.com/AHAAAAAAA/mapbh"} [:i.fab.fa-github]]]
     [:span.icon [:a {:style {:color :black}
                      :href "mailto:mapbh.org@gmail.com"} [:i.fas.fa-envelope]]]
     [:span.icon [:a {:style {:color :black}
                      :href "https://instagram.com/map_bh"} [:i.fab.fa-instagram]]]]])

(defn footer-ar
  []
  [:footer.footer {:lang "ar" :direction "rtl"}
    [:div.content.has-text-centered
     [:span.icon [:a {:style {:color :black}
                      :href "https://twitter.com/map_bh"} [:i.fab.fa-twitter]]]
     [:span.icon [:a {:style {:color :black}
                      :href "https://github.com/AHAAAAAAA/mapbh"} [:i.fab.fa-github]]]
     [:span.icon [:a {:style {:color :black}
                      :href "mailto:mapbh.org@gmail.com"} [:i.fas.fa-envelope]]]
     [:span.icon [:a {:style {:color :black}
                      :href "https://instagram.com/map_bh"} [:i.fab.fa-instagram]]]]])

(defn footer
  [language]
  (condp = language
    :ar [footer-ar]
    :en [footer-en]
    [footer-ar]))

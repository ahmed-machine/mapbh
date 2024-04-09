(ns app.pages.homepage
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]
            [app.routes :as routes]))


(def tagline "A visualization of Bahrain through historic maps.")
(def ar-tagline "رحلة في تاريخ البحرين عبر الخرائط")

(defn en []
  [:div.container.home
   [:p.column.is-full.logo {:style {:font-size "5em"
                                    :z-index 2
                                    :margin-top "9%"
                                    :margin-bottom "1%"
                                    :text-align :center}} "mapBH"]
   [:p.column.is-full {:style {:z-index 2
                               :margin-bottom "1%"
                               :text-align :center}} tagline]
   [:nav.navbar.columns.is-centered.is-vcentered {:role "navigation" :style {:padding "1rem"}}
    [:a.navbar-item.column.button.is-rounded.is-2.is-centered.is-vcentered {:href (routes/url-for :about)} "About"]
    [:a.navbar-item.column.button.is-rounded.is-2.is-centered.is-vcentered {:href (routes/url-for :contribute)} "Contribute"]]
   [:nav.navbar.columns.is-centered.is-vcentered {:role "navigation" :style {:padding "1rem"}}
    [:a.navbar-item.column.button.is-rounded.is-2.is-centered.is-vcentered {:href (routes/url-for :map)} "Map"]
    [:a.navbar-item.column.is-rounded.is-2.is-centered.is-vcentered.button {:href (routes/url-for :article-index)} "Articles"]]
   [:div.content.has-text-centered
    [:span.icon.home [:a {:style {:color :black}
                          :href "https://twitter.com/map_bh"} [:i.fab.fa-twitter]]]
    [:span.icon.home [:a {:style {:color :black}
                          :href "https://github.com/AHAAAAAAA/mapbh"} [:i.fab.fa-github]]]
    [:span.icon.home [:a {:style {:color :black}
                          :href "mailto:mapbh.org@gmail.com"} [:i.fas.fa-envelope]]]
    [:span.icon.home [:a {:style {:color :black}
                          :href "https://instagram.com/map_bh"} [:i.fab.fa-instagram]]]]
   [:a.column.is-full {:role "navigation"
                       :lang "ar"
                       :style {:text-align :center}
                       :on-click #(rf/dispatch [::events/set-language "ar"])}
    [:button.button.is-outlined.is-rounded.is-small {:style {:font-size "1em"}}  "العربية"]]])


(defn ar []
  [:div.container.home
   [:p.column.is-full.logo {:style {:font-size "5em"
                                    :z-index 2
                                    :margin-top "9%"
                                    :margin-bottom "1%"
                                    :text-align :center}} "mapBH"]
   [:div {:dir "rtl" :lang "ar" :style {:font-size "1.25em"}}
    [:p.column.is-full {:style {:z-index 2
                                :margin-bottom "1%"
                                :text-align :center}} ar-tagline]
    [:nav.navbar.columns.is-centered.is-vcentered {:role "navigation" :style {:padding "1rem"}}
     [:a.navbar-item.column.button.is-rounded.is-2.is-centered.is-vcentered {:href (routes/url-for :about)} "نبذة"]
     [:a.navbar-item.column.button.is-rounded.is-2.is-centered.is-vcentered {:href (routes/url-for :contribute)} "ساهم"]]]
   [:nav.navbar.columns.is-centered.is-vcentered {:role "navigation" :style {:padding "1rem"}}
    [:a.navbar-item.column.is-rounded.is-2.is-centered.is-vcentered.button {:href (routes/url-for :article-index)} "مقالات"]
    [:a.navbar-item.column.button.is-rounded.is-2.is-centered.is-vcentered {:href (routes/url-for :map)} "الخارطة"]]
   [:div.content.has-text-centered {:style {:margin-top "1.1rem"}}
    [:span.icon.home [:a {:style {:color :black}
                          :href "https://twitter.com/map_bh"} [:i.fab.fa-twitter]]]
    [:span.icon.home [:a {:style {:color :black}
                          :href "https://github.com/AHAAAAAAA/mapbh"} [:i.fab.fa-github]]]
    [:span.icon.home [:a {:style {:color :black}
                          :href "mailto:mapbh.org@gmail.com"} [:i.fas.fa-envelope]]]
    [:span.icon.home [:a {:style {:color :black}
                          :href "https://instagram.com/map_bh"} [:i.fab.fa-instagram]]]]
   [:a.column.is-full {:role "navigation"
                       :style {:text-align :center}
                       :on-click #(rf/dispatch [::events/set-language "en"])}
    [:button.button.is-outlined.is-rounded.is-small "English"]]])

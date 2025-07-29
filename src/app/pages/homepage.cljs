(ns app.pages.homepage
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]
            [app.routes :as routes]))


(def tagline "Visualising Bahrain through historic maps.")
(def ar-tagline "رحلة في تاريخ البحرين عبر الخرائط")

(defn en []
  [:div.container.home
   [:div.main-content
    [:div.has-text-centered
     [:h1.logo "mapBH"]
     [:p {:style {:color "var(--text-muted)" :margin-top "0.5rem"}} tagline]]
    [:nav.navbar {:role "navigation"}
     [:div.navbar-menu
      [:div.primary-button-container
       [:a.navbar-item.button.is-primary {:href (routes/url-for :map)} "Explore Maps"]]
      [:div.secondary-buttons-container
       [:a.navbar-item.button {:href (routes/url-for :about)} "About"]
       [:a.navbar-item.button {:href (routes/url-for :article-index)} "Articles"]
       [:a.navbar-item.button {:href (routes/url-for :catalogue)} "Catalogue"]
       [:a.navbar-item.button {:href (routes/url-for :contribute)} "Contribute"]]]]]
   [:div.footer-content
    [:div.content.has-text-centered {:style {:margin-top "var(--spacing-md)"}}
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "https://twitter.com/map_bh"
                           :aria-label "Twitter"} [:i.fab.fa-twitter]]]
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "https://github.com/AHAAAAAAA/mapbh"
                           :aria-label "GitHub"} [:i.fab.fa-github]]]
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "mailto:mapbh.org@gmail.com"
                           :aria-label "Email"} [:i.fas.fa-envelope]]]
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "https://instagram.com/map_bh"
                           :aria-label "Instagram"} [:i.fab.fa-instagram]]]]
    [:div.has-text-centered {:style {:margin-top "var(--spacing-lg)"}}
     [:button.button.is-outlined.is-rounded.language-switch
      {:role "navigation"
       :lang "ar"
       :style {:font-family "Amiri, Scheherazade, serif"}
       :on-click #(rf/dispatch [::events/set-route-params {:language "ar"}])}
      "العربية"]]]])


(defn ar []
  [:div.container.home {:dir "rtl" :lang "ar"}
   [:div.main-content
    [:div.has-text-centered
     [:h1.logo "mapBH"]
     [:p {:style {:color "var(--text-muted)" :margin-top "0.5rem"}} ar-tagline]]
    [:nav.navbar {:role "navigation"}
     [:div.navbar-menu
      [:div.primary-button-container
       [:a.navbar-item.button.is-primary {:href (routes/url-for :map)} "استكشف الخرائط"]]
      [:div.secondary-buttons-container
       [:a.navbar-item.button {:href (routes/url-for :about)} "نبذة عن المشروع"]
       [:a.navbar-item.button {:href (routes/url-for :article-index)} "مقالات"]
       [:a.navbar-item.button {:href (routes/url-for :catalogue)} "فهرس الخرائط"]
       [:a.navbar-item.button {:href (routes/url-for :contribute)} "ساهم"]]]]]
   [:div.footer-content
    [:div.content.has-text-centered {:style {:margin-top "var(--spacing-md)"}}
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "https://twitter.com/map_bh"
                           :aria-label "تويتر"} [:i.fab.fa-twitter]]]
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "https://github.com/AHAAAAAAA/mapbh"
                           :aria-label "جيت هب"} [:i.fab.fa-github]]]
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "mailto:mapbh.org@gmail.com"
                           :aria-label "بريد إلكتروني"} [:i.fas.fa-envelope]]]
     [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                           :href "https://instagram.com/map_bh"
                           :aria-label "إنستغرام"} [:i.fab.fa-instagram]]]]
    [:div.has-text-centered {:style {:margin-top "var(--spacing-lg)"}}
     [:button.button.is-outlined.is-rounded.language-switch
      {:role "navigation"
       :style {:font-family "Roboto, Helvetica, sans-serif"}
       :on-click #(rf/dispatch [::events/set-route-params {:language "en"}])}
      "English"]]]])

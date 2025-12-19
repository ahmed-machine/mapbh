(ns app.pages.homepage
  (:require [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]
            [app.routes :as routes]))

(defn text
  [arabic?]
  (if arabic?
    {:tagline "رحلة في تاريخ البحرين عبر الخرائط"
     :buttons {:explore "استكشف الخرائط"
               :about "نبذة عن المشروع"
               :articles "مقالات"
               :catalogue "فهرس الخرائط"
               :contribute "ساهم"
               :language "English"}
     :aria {:twitter "تويتر"
            :github "جيت هب"
            :email "بريد إلكتروني"
            :instagram "إنستغرام"}}
    {:tagline "Visualising Bahrain through historic maps."
     :buttons {:explore "Explore Maps"
               :about "About"
               :articles "Articles"
               :catalogue "Catalogue"
               :contribute "Contribute"
               :language "العربية"}
     :aria {:twitter "Twitter"
            :github "GitHub"
            :email "Email"
            :instagram "Instagram"}}))

(defn homepage
  []
  (let [language* (rf/subscribe [::model/language])]
    (fn []
      (let [language @language*
            arabic? (= language :ar)
            txt (text arabic?)]
        [:div.container.home (when arabic? {:dir "rtl" :lang "ar"})
         [:div.main-content
          [:div.has-text-centered
           [:h1.logo "mapBH"]
           [:p {:style {:color "var(--text-muted)" :margin-top "0.5rem"}} (:tagline txt)]]
          [:nav.navbar {:role "navigation"}
           [:div.navbar-menu
            [:div.primary-button-container
             [:a.navbar-item.button.is-primary {:href (routes/url-for :map)} (get-in txt [:buttons :explore])]]
            [:div.secondary-buttons-container
             [:a.navbar-item.button {:href (routes/url-for :about)} (get-in txt [:buttons :about])]
             [:a.navbar-item.button {:href (routes/url-for :article-index)} (get-in txt [:buttons :articles])]
             [:a.navbar-item.button {:href (routes/url-for :catalogue)} (get-in txt [:buttons :catalogue])]
             [:a.navbar-item.button {:href (routes/url-for :contribute)} (get-in txt [:buttons :contribute])]]]]]
         [:div.footer-content
          [:div.content.has-text-centered {:style {:margin-top "var(--spacing-md)"}}
           [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                                 :href "https://twitter.com/map_bh"
                                 :aria-label (get-in txt [:aria :twitter])} [:i.fab.fa-twitter]]]
           [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                                 :href "https://github.com/ahmed-machine/mapbh"
                                 :aria-label (get-in txt [:aria :github])} [:i.fab.fa-github]]]
           [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                                 :href "mailto:mapbh.org@gmail.com"
                                 :aria-label (get-in txt [:aria :email])} [:i.fas.fa-envelope]]]
           [:span.icon.home [:a {:style {:color "var(--text-muted)"}
                                 :href "https://instagram.com/map_bh"
                                 :aria-label (get-in txt [:aria :instagram])} [:i.fab.fa-instagram]]]]
          [:div.has-text-centered {:style {:margin-top "var(--spacing-lg)"}}
           [:button.button.is-outlined.is-rounded.language-switch
            {:role "navigation"
             :style {:font-family "Roboto, Helvetica, sans-serif"}
             :on-click #(rf/dispatch [::events/set-route-params {:language (if arabic? "en" "ar")}])}
            (get-in txt [:buttons :language])]]]]))))


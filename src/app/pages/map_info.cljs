(ns app.pages.map-info
  (:require [reagent.core :as r]
            [app.pages.map.map-data :as map-data :refer [ar-layers get-thumbnail-path]]
            [app.pages.catalogue :as catalogue]
            [app.routes :as routes]
            [clojure.string :as str]
            [re-frame.core :as rf]
            [app.model :as model]))

(defn find-map-by-group-and-id
  "Find map information by group and map ID using direct lookup with language support"
  [group map-id language]
  (when (and group map-id)
    ;; Find all groups that contain this map
    (let [all-entries (for [[group-name group-maps] map-data/layers
                           :when (contains? group-maps map-id)]
                       {:group group-name
                        :map-info (get group-maps map-id)})
          first-entry (first all-entries)
          all-groups (map :group all-entries)]
      (when first-entry
        (let [english-data (:map-info first-entry)
              ;; Try to get Arabic data from any of the groups (use first group as primary)
              primary-group (first all-groups)
              arabic-data (when (= language :ar)
                           (get-in ar-layers [primary-group map-id]))
              ;; Merge Arabic data over English, falling back to English for missing fields
              merged-data (if arabic-data
                           (merge english-data arabic-data)
                           english-data)]
          (merge merged-data {:map-id map-id
                             :group primary-group
                             :all-groups all-groups}))))))

(defn info-section
  "Render an information section if content exists"
  [title content language]
  (when (and content (not (str/blank? content)))
    [:div.content
     [:h4.title.is-5 {:style {:margin-bottom "0.5rem"}} title]
     [:div.box {:style {:background-color "#fafafa"}}
      [:p content]]]))

(defn metadata-grid
  "Render metadata in a clean grid layout"
  [map-info language]
  (let [is-arabic (= language :ar)
        metadata-items (cond-> []
                        (:scale map-info) (conj {:label (if is-arabic "المقياس" "Scale")
                                                :value (:scale map-info)})
                        (:source map-info) (conj {:label (if is-arabic "المصدر" "Source")
                                                 :value (:source map-info)})
                        (:issuer map-info) (conj {:label (if is-arabic "الناشر" "Issuer")
                                                 :value (:issuer map-info)})
                        (:year map-info) (conj {:label (if is-arabic "السنة" "Year")
                                               :value (:year map-info)}))]
    (when (seq metadata-items)
      [:div.columns.is-multiline
       (for [item metadata-items]
         [:div.column.is-half {:key (:label item)}
          [:div.field
           [:label.label.is-small (:label item)]
           [:div.control
            [:p.has-text-weight-medium (:value item)]]]])])))

(defn action-buttons
  "Render action buttons for viewing map and downloading files"
  [map-info language]
  (let [is-arabic (= language :ar)
        bounds (catalogue/get-layer-bounds (:group map-info) (:map-id map-info) (:title map-info))
        is-viewable (not= false (:viewable map-info))]
    [:div.buttons
     (when is-viewable
       [:a.button.is-light.is-small
        {:href (str (routes/url-for :map)
                   "?map=" (js/encodeURIComponent (:map-id map-info))
                   "&coords=" (:lat bounds) "," (:lng bounds)
                   "&zoom=" (:zoom bounds))}
        [:i.fas.fa-map {:style (if is-arabic
                                  {:margin-left "0.5rem" :padding "0.2rem"}
                                  {:margin-right "0.5rem" :padding "0.2rem"})}]
        (if is-arabic "عرض الخريطة" "View Map")])

     (when (:source-link map-info)
       [:a.button.is-light.is-small
        {:href (:source-link map-info)
         :target "_blank"}
        [:i.fas.fa-download {:style (if is-arabic
                                       {:margin-left "0.5rem" :padding "0.2rem"}
                                       {:margin-right "0.5rem" :padding "0.2rem"})}]
        (if is-arabic "تحميل المصدر" "Download Source")])

     (when (:issuer-link map-info)
       [:a.button.is-light.is-small
        {:href (:issuer-link map-info)
         :target "_blank"}
        [:i.fas.fa-file-image {:style (if is-arabic
                                         {:margin-left "0.5rem" :padding "0.2rem"}
                                         {:margin-right "0.5rem" :padding "0.2rem"})}]
        (if is-arabic "الملف الأصلي" "Original File")])

     (when (:link-1 map-info)
       [:a.button.is-light.is-small
        {:href (:link-1 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (if is-arabic
                                                {:margin-left "0.5rem" :padding "0.2rem"}
                                                {:margin-right "0.5rem" :padding "0.2rem"})}]
        (or (:link-1-label map-info)
            (if is-arabic "رابط إضافي" "Additional Link"))])

     (when (:link-2 map-info)
       [:a.button.is-light.is-small
        {:href (:link-2 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (if is-arabic
                                                {:margin-left "0.5rem" :padding "0.2rem"}
                                                {:margin-right "0.5rem" :padding "0.2rem"})}]
        (or (:link-2-label map-info)
            (if is-arabic "رابط إضافي ثاني" "Additional Link 2"))])]))

(defn map-thumbnail
  "Render map thumbnail if available"
  [map-info]
  (let [thumbnail-path (get-thumbnail-path map-info)]
    (when thumbnail-path
      [:div.content {:style {:margin-bottom "2rem"}}
       [:figure.image
        [:img {:src thumbnail-path
               :alt (:title map-info)
               :style {:max-width "600px"
                       :height "auto"
                       :border "1px solid #ddd"
                       :border-radius "4px"
                       :box-shadow "0 2px 4px rgba(0,0,0,0.1)"}
               :on-error "this.parentElement.parentElement.style.display='none'"}]]])))

(defn breadcrumb-nav
  "Render breadcrumb navigation"
  [map-info language]
  (let [is-arabic (= language :ar)]
    [:nav.breadcrumb {:aria-label "breadcrumbs"}
     [:ul
      [:li [:a {:href (routes/url-for :home)}
            (if is-arabic "الرئيسية" "Home")]]
      [:li [:a {:href (routes/url-for :catalogue)}
            (if is-arabic "فهرس الخرائط" "Catalogue")]]
      [:li.is-active [:a {:aria-current "page"} (:title map-info)]]]]))

(defn map-info
  "Combined map info page component with language support"
  [language group map-id]
  (let [is-arabic (= language :ar)
        map-info (when (and group map-id) (find-map-by-group-and-id group map-id language))]
    (if map-info
      (let [year (:year map-info)]
        [:div.container (cond-> {:style {:margin-top "6rem" :margin-bottom "3rem"}}
                               is-arabic (assoc :lang "ar" :dir "rtl"))
         [breadcrumb-nav map-info language]

         [:div.content
          [:div
           [:h1.title.is-2 (:title map-info)]
           [:h2.subtitle.is-4
            (if (and (:all-groups map-info) (> (count (:all-groups map-info)) 1))
              [:div.field.is-grouped.is-grouped-multiline
               (for [group (:all-groups map-info)]
                 [:div.control {:key group}
                  [:span.tag.is-primary.is-medium group]])]
              [:span.tag.is-primary.is-medium (:group map-info)])
            (when year [:span.tag.is-info.is-medium {:style (if is-arabic
                                                               {:margin-right "0.5rem"}
                                                               {:margin-left "0.5rem"})} year])]
           [:div.content {:style {:margin-top "1rem"}}
            [action-buttons map-info language]]]

          [:hr]
          [metadata-grid map-info language]
          [map-thumbnail map-info]
          [info-section (if is-arabic "الوصف" "Description") (:description map-info) language]
          [info-section (if is-arabic "ملاحظات" "Notes") (:notes map-info) language]
          [info-section (if is-arabic "مساهمة" "Submitted by") (:submitted-by map-info) language]]])

      [:div.container (cond-> {:style {:margin-top "6rem"}}
                             is-arabic (assoc :lang "ar" :dir "rtl"))
       [:div.content
        [:h1.title.is-2 (if is-arabic "الخريطة غير موجودة" "Map Not Found")]
        [:p (if is-arabic "لم يتم العثور على الخريطة المطلوبة." "The requested map could not be found.")]
        [:a.button.is-primary {:href (routes/url-for :catalogue)}
         (if is-arabic "العودة للفهرس" "Return to Catalogue")]]])))

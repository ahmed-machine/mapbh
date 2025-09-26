(ns app.pages.map-info
  (:require [re-frame.core :as rf]
            [app.data :refer [get-thumbnail-path maps get-map-text]]
            [app.routes :as routes]
            [app.model :as model]
            [clojure.string :as str]))

(defn text
  [arabic?]
  (if arabic?
    {:metadata {:scale "المقياس"
                :source "المصدر"
                :issuer "الناشر"
                :year "السنة"}
     :sections {:description "الوصف"
                :notes "ملاحظات"
                :submitted-by "مساهمة"}
     :buttons {:view-map "عرض الخريطة"
               :download-source "الملف الأصلي"
               :download-issuer "تحميل المصدر"
               :additional-link "رابط إضافي"
               :additional-link-2 "رابط إضافي ثاني"
               :additional-link-3 "رابط إضافي ثالث"}
     :navigation {:home "الرئيسية"
                  :catalogue "فهرس الخرائط"}
     :errors {:not-found "الخريطة غير موجودة"
              :not-found-desc "لم يتم العثور على الخريطة المطلوبة."
              :return-catalogue "العودة للفهرس"}}
    {:metadata {:scale "Scale"
                :source "Source"
                :issuer "Issuer"
                :year "Year"}
     :sections {:description "Description"
                :notes "Notes"
                :submitted-by "Submitted by"}
     :buttons {:view-map "View Map"
               :download-source "Download Original"
               :download-issuer "Download Georectified"
               :additional-link "Additional Link"
               :additional-link-2 "Additional Link 2"
               :additional-link-3 "Additional Link 3"}
     :navigation {:home "Home"
                  :catalogue "Catalogue"}
     :errors {:not-found "Map Not Found"
              :not-found-desc "The requested map could not be found."
              :return-catalogue "Return to Catalogue"}}))

(defn find-map-by-group-and-id
  "Find map information by group and map ID with language support"
  [group map-id language]
  (when (and group map-id)
    ;; Get map data
    (when-let [map-info (get maps map-id)]
      (let [groups (:groups map-info)
            title (get-map-text map-info language :title)
            description (get-map-text map-info language :description)
            notes (get-map-text map-info language :notes)
            labels (get-map-text map-info language :labels)
            submitted-by (get-map-text map-info language :submitted-by)]
        (merge map-info
               {:map-id map-id
                :title title
                :description description
                :notes notes
                :labels labels
                :submitted-by submitted-by
                :group (first groups)  ; Primary group for backward compatibility
                :all-groups (vec groups)})))))

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
        txt (text is-arabic)
        metadata-items (cond-> []
                        (:scale map-info) (conj {:label (get-in txt [:metadata :scale])
                                                :value (:scale map-info)})
                        (:source map-info) (conj {:label (get-in txt [:metadata :source])
                                                 :value (:source map-info)})
                        (:issuer map-info) (conj {:label (get-in txt [:metadata :issuer])
                                                 :value (:issuer map-info)})
                        (:year map-info) (conj {:label (get-in txt [:metadata :year])
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
        is-viewable (not= false (:viewable map-info))]
    [:div.buttons
     (when is-viewable
       [:a.button.is-light.is-small
        {:href (str (routes/url-for :map)
                   "?map=" (js/encodeURIComponent (:map-id map-info))
                   "&flyTo=true")}
        [:i.fas.fa-map {:style (if is-arabic
                                  {:margin-left "0.5rem" :padding "0.2rem"}
                                  {:margin-right "0.5rem" :padding "0.2rem"})}]
        (get-in (text is-arabic) [:buttons :view-map])])

     (when (:source-link map-info)
       [:a.button.is-light.is-small
        {:href (:source-link map-info)
         :target "_blank"}
        [:i.fas.fa-download {:style (if is-arabic
                                       {:margin-left "0.5rem" :padding "0.2rem"}
                                       {:margin-right "0.5rem" :padding "0.2rem"})}]
        (get-in (text is-arabic) [:buttons :download-source])])

     (when (:issuer-link map-info)
       [:a.button.is-light.is-small
        {:href (:issuer-link map-info)
         :target "_blank"}
        [:i.fas.fa-file-image {:style (if is-arabic
                                         {:margin-left "0.5rem" :padding "0.2rem"}
                                         {:margin-right "0.5rem" :padding "0.2rem"})}]
        (get-in (text is-arabic) [:buttons :download-issuer])])

     (when (:link-1 map-info)
       [:a.button.is-light.is-small
        {:href (:link-1 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (if is-arabic
                                                {:margin-left "0.5rem" :padding "0.2rem"}
                                                {:margin-right "0.5rem" :padding "0.2rem"})}]
        (or (:link-1-label map-info)
            (get-in (text is-arabic) [:buttons :additional-link]))])

     (when (:link-2 map-info)
       [:a.button.is-light.is-small
        {:href (:link-2 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (if is-arabic
                                                {:margin-left "0.5rem" :padding "0.2rem"}
                                                {:margin-right "0.5rem" :padding "0.2rem"})}]
        (or (:link-2-label map-info)
            (get-in (text is-arabic) [:buttons :additional-link-2]))])

     (when (:link-3 map-info)
       [:a.button.is-light.is-small
        {:href (:link-3 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (if is-arabic
                                                {:margin-left "0.5rem" :padding "0.2rem"}
                                                {:margin-right "0.5rem" :padding "0.2rem"})}]
        (or (:link-3-label map-info)
            (get-in (text is-arabic) [:buttons :additional-link-3]))])]))

(defn map-thumbnail
  "Render map thumbnail with RDFa structured data for Google rich results"
  [map-info]
  (let [thumbnail-path (get-thumbnail-path map-info)
        credit-text (str (:source map-info)
                        (when (and (:source map-info) (:issuer map-info)) " / ")
                        (:issuer map-info))]
    (when thumbnail-path
      [:div.content {:style {:margin-bottom "2rem"}
                     :vocab "https://schema.org/"
                     :typeof "ImageObject"}
       [:figure.image
        [:img {:src thumbnail-path
               :property "contentUrl"
               :alt (:title map-info)
               :style {:max-width "600px"
                       :height "auto"
                       :border "1px solid #ddd"
                       :border-radius "4px"
                       :box-shadow "0 2px 4px rgba(0,0,0,0.1)"}
               :on-error "this.parentElement.parentElement.style.display='none'"}]]
       ;; Hidden structured data elements
       [:div {:style {:display "none"}}
        ;; Creator information
        [:span {:property "creator" :typeof "Organization"}
         [:span {:property "name"} "mapBH"]]
        ;; Credit text combining source and issuer
        (when (or (:source map-info) (:issuer map-info))
          [:span {:property "creditText"} credit-text])
        ;; Additional metadata
        (when (:title map-info)
          [:span {:property "name"} (:title map-info)])
        (when (:description map-info)
          [:span {:property "description"} (:description map-info)])
        (when (:year map-info)
          [:span {:property "datePublished"} (str (:year map-info))])
        (when (:notes map-info)
          [:span {:property "caption"} (:notes map-info)])
        (when (:scale map-info)
          [:meta {:property "additionalProperty"
                  :content (str "Scale: " (:scale map-info))}])
        ;; License and acquisition links if available
        (when (:source-link map-info)
          [:span {:property "acquireLicensePage"} (:source-link map-info)])
        (when (:issuer-link map-info)
          [:span {:property "url"} (:source-link map-info)])]])))

(defn breadcrumb-nav
  "Render breadcrumb navigation"
  [map-info language]
  (let [is-arabic (= language :ar)]
    [:nav.breadcrumb {:aria-label "breadcrumbs"}
     [:ul
      [:li [:a {:href (routes/url-for :home)}
            (get-in (text is-arabic) [:navigation :home])]]
      [:li [:a {:href (routes/url-for :catalogue)}
            (get-in (text is-arabic) [:navigation :catalogue])]]
      [:li.is-active [:a {:aria-current "page"} (:title map-info)]]]]))

(defn map-info
  [group map-id]
  (fn []
    (let [language* (rf/subscribe [::model/language])
          language @language*
          is-arabic (= language :ar)
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
            [info-section (get-in (text is-arabic) [:sections :description]) (:description map-info) language]
            [info-section (get-in (text is-arabic) [:sections :notes]) (:notes map-info) language]
            [info-section (get-in (text is-arabic) [:sections :submitted-by]) (:submitted-by map-info) language]]])

        [:div.container (cond-> {:style {:margin-top "6rem"}}
                          is-arabic (assoc :lang "ar" :dir "rtl"))
         [:div.content
          [:h1.title.is-2 (get-in (text is-arabic) [:errors :not-found])]
          [:p (get-in (text is-arabic) [:errors :not-found-desc])]
          [:a.button.is-primary {:href (routes/url-for :catalogue)}
           (get-in (text is-arabic) [:errors :return-catalogue])]]]))))

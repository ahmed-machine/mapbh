(ns app.pages.map-info
  (:require [re-frame.core :as rf]
            [app.data :refer [get-thumbnail-path get-collection-thumbnail-paths maps get-map-text get-cdn-url]]
            [app.routes :as routes]
            [app.model :as model]
            [app.util.core :refer [rtl-attrs icon-margin]]
            [clojure.string :as str]
            [app.util.meta :as meta]))

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
     :navigation {:home "الصفحة الرئيسية"
                  :catalogue "الفهرس"}
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

(defn generate-breadcrumb-ld
  "Generate BreadcrumbList structured data for map info pages"
  [map-info language]
  (let [is-arabic (= language :ar)
        base-url (if is-arabic "https://www.mapbh.org/ar" "https://www.mapbh.org/en")
        breadcrumb-data {"@context" "https://schema.org"
                        "@type" "BreadcrumbList"
                        "itemListElement" [{"@type" "ListItem"
                                          "position" 1
                                          "name" (if is-arabic "الرئيسية" "Home")
                                          "item" (str base-url "/")}
                                         {"@type" "ListItem"
                                          "position" 2
                                          "name" (if is-arabic "فهرس الخرائط" "Catalogue")
                                          "item" (str base-url "/catalogue")}
                                         {"@type" "ListItem"
                                          "position" 3
                                          "name" (:title map-info)}]}]
    (js/JSON.stringify (clj->js breadcrumb-data) nil 2)))

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
  [title content _]
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
        is-viewable (not= false (:viewable map-info))
        issuer-link (:issuer-link map-info)
        is-collection (and issuer-link (str/ends-with? issuer-link "/"))]
    [:div.buttons
     (when is-viewable
       [:a.button.is-light.is-small
        {:href (str (routes/url-for :map)
                   "?map=" (js/encodeURIComponent (:map-id map-info))
                   "&flyTo=true")}
        [:i.fas.fa-map {:style (icon-margin language)}]
        (get-in (text is-arabic) [:buttons :view-map])])

     (when (:source-link map-info)
       [:a.button.is-light.is-small
        {:href (get-cdn-url (:source-link map-info))
         :target "_blank"}
        [:i.fas.fa-download {:style (icon-margin language)}]
        (get-in (text is-arabic) [:buttons :download-source])])

     ;; Only show issuer-link button if it's NOT a collection directory
     (when (and issuer-link (not is-collection))
       [:a.button.is-light.is-small
        {:href (get-cdn-url issuer-link)
         :target "_blank"}
        [:i.fas.fa-file-image {:style (icon-margin language)}]
        (get-in (text is-arabic) [:buttons :download-issuer])])

     (when (:link-1 map-info)
       [:a.button.is-light.is-small
        {:href (:link-1 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (icon-margin language)}]
        (or (:link-1-label map-info)
            (get-in (text is-arabic) [:buttons :additional-link]))])

     (when (:link-2 map-info)
       [:a.button.is-light.is-small
        {:href (:link-2 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (icon-margin language)}]
        (or (:link-2-label map-info)
            (get-in (text is-arabic) [:buttons :additional-link-2]))])

     (when (:link-3 map-info)
       [:a.button.is-light.is-small
        {:href (:link-3 map-info)
         :target "_blank"}
        [:i.fas.fa-external-link-alt {:style (icon-margin language)}]
        (or (:link-3-label map-info)
            (get-in (text is-arabic) [:buttons :additional-link-3]))])]))

(defn map-thumbnail
  "Render map thumbnail(s) with RDFa structured data for Google rich results.
   For collections, displays all individual thumbnails."
  [map-info]
  (let [collection-thumbnails (get-collection-thumbnail-paths map-info)
        single-thumbnail (get-thumbnail-path map-info)
        thumbnail-path (or single-thumbnail (first collection-thumbnails))
        full-thumbnail-url (str (meta/get-current-domain) thumbnail-path)
        credit-text (str (:source map-info)
                         (when (and (:source map-info) (:issuer map-info)) " / ")
                         (:issuer map-info))]
    (when (or single-thumbnail collection-thumbnails)
      [:div.content {:style {:margin-bottom "2rem"}
                     :vocab "https://schema.org/"
                     :typeof "ImageObject"}
       ;; Display thumbnails - multiple for collections, single otherwise
       (if collection-thumbnails
         ;; Collection: display all thumbnails in a grid
         [:div.columns.is-multiline {:style {:margin-top "1rem"}}
          (for [thumb-path collection-thumbnails]
            [:div.column.is-one-third {:key thumb-path}
             [:figure.image
              [:img {:src thumb-path
                     :alt (:title map-info)
                     :style {:width "100%"
                             :height "auto"
                             :border "1px solid #ddd"
                             :border-radius "4px"
                             :box-shadow "0 2px 4px rgba(0,0,0,0.1)"}
                     :on-error "this.style.display='none'"}]]])]
         ;; Single map: display one thumbnail
         [:figure.image
          [:img {:src single-thumbnail
                 :alt (:title map-info)
                 :style {:max-width "600px"
                         :height "auto"
                         :border "1px solid #ddd"
                         :border-radius "4px"
                         :box-shadow "0 2px 4px rgba(0,0,0,0.1)"}
                 :on-error "this.parentElement.parentElement.style.display='none'"}]])

       ;; Hidden structured data elements
       [:div {:style {:display "none"}}
        ;; Proper contentUrl with full URL
        [:meta {:property "contentUrl" :content full-thumbnail-url}]
        ;; Creator information
        [:span {:property "creator" :typeof "Organization"}
         [:span {:property "name"} "mapBH"]]
        ;; Credit text combining source and issuer
        (when (or (:source map-info) (:issuer map-info))
          [:span {:property "creditText"} credit-text])
        ;; Copyright notice (required field)
        [:span {:property "copyrightNotice"}
         (or credit-text "© mapBH - Digital Map Archive")]
        ;; License information (required field)
        [:span {:property "license"} "https://creativecommons.org/licenses/by-sa/4.0/"]
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
        ;; License and acquisition links if available - using meta tags for URLs
        (when (:source-link map-info)
          [:meta {:property "acquireLicensePage" :content (get-cdn-url (:source-link map-info))}])
        (when (:issuer-link map-info)
          [:meta {:property "url" :content (get-cdn-url (or (:source-link map-info) (:issuer-link map-info)))}])]])))

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

      ;; Set page meta tags when map-info is available
      (when map-info
        (let [thumbnail-path (get-thumbnail-path map-info)
              map-data (assoc map-info :thumbnail-path thumbnail-path)
              title (:title map-data)
                year (:year map-data)
                scale (:scale map-data)
                source (:source map-data)
                description (:description map-data)
                thumbnail-path (:thumbnail-path map-data)
                is-arabic (= language :ar)

                page-title (str title (when year (str " (" year ")")))
                page-description (or description
                                   (str title
                                        (when year (str (if is-arabic " من عام " " from ") year))
                                        (when scale (str (if is-arabic ". المقياس: " ". Scale: ") scale))
                                        (when source (str (if is-arabic ". المصدر: " ". Source: ") source))))
                page-image (when thumbnail-path (str "https://www.mapbh.org" thumbnail-path))]
          (meta/set-page-meta! {:title page-title
                                :description page-description
                                :image (or page-image "https://mapbh.org/img/ogbrand.png")
                                :image-alt (str title (if is-arabic " - خريطة تاريخية" " - Historical Map"))
                                :keywords (if is-arabic
                                            ["خريطة تاريخية" "البحرين" (str "خريطة " year) title]
                                            ["historical map" "Bahrain" (str year " map") title])})))
      (if map-info
        (let [year (:year map-info)
              breadcrumb-ld (generate-breadcrumb-ld map-info language)]
          [:div.container (merge {:style {:margin-top "6rem" :margin-bottom "3rem"}}
                                 (when is-arabic (rtl-attrs)))
           ;; BreadcrumbList structured data
           [:script {:type "application/ld+json"
                     :dangerouslySetInnerHTML {:__html breadcrumb-ld}}]
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

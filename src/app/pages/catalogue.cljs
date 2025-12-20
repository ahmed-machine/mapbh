(ns app.pages.catalogue
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.data :as data :refer [backlog maps get-map-text]]
            [app.routes :as routes]
            [app.util.url :as url]
            [app.util.core :refer [rtl-attrs icon-margin]]
            [app.model :as model]
            [clojure.string :as str]))

(defn text
  [arabic?]
  (if arabic?
    {:page {:title "فهرس الخرائط"
            :subtitle "تصفح وابحث جميع الخرائط"
            :search-placeholder "ابحث في الخرائط..."
            :include-backlog "خرائط لم تنشر"
            :sort-by "ترتيب حسب:"
            :showing-count (fn [filtered total] (str "عرض " filtered " من " total " خريطة"))}
     :buttons {:info "تفاصيل"
               :view "عرض"}
     :table {:title "العنوان"
             :year "السنة"
             :group "المجموعة"
             :scale "المقياس"
             :source "المصدر"
             :issuer "الناشر"
             :needs-processing "في الارشيف"}}
    {:page {:title "Catalogue"
            :subtitle "Browse and search through maps"
            :search-placeholder "Search maps..."
            :include-backlog "Include backlog"
            :sort-by "Sort by:"
            :showing-count (fn [filtered total] (str "Showing " filtered " of " total " maps"))}
     :buttons {:info "Info"
               :view "View"}
     :table {:title "Title"
             :year "Year"
             :group "Group"
             :scale "Scale"
             :source "Source"
             :issuer "Issuer"
             :needs-processing "Needs Processing"}}))

(defn generate-json-ld
  "Generate JSON-LD structured data for the map catalogue dataset. Primarily used for SEO indexing."
  [language all-data]
  (let [arabic? (= language :ar)
        ;; Calculate temporal coverage from years
        years (remove nil? (map :year all-data))
        min-year (when (seq years) (apply min years))
        max-year (when (seq years) (apply max years))
        ;; Create the main dataset structure
        dataset (merge
                 {"@context" "https://schema.org/"
                  "@type" "Dataset"
                  "name" (if arabic?
                           "ارشيف  خرائط البحرين الرقمي - mapBH"
                           "mapBH - Digital Map Archive")
                  "description" (if arabic?
                                  "أرشيف رقمي شامل للخرائط التاريخية للبحرين من القرن العشرين، يوفر أدوات تفاعلية للمقارنة والبحث في التطور العمراني والجغرافي للمملكة"
                                  "A comprehensive digital archive of historical maps of Bahrain from the 19th century, providing interactive tools for comparing and researching the urban and geographic evolution of Bahrain")
                  "keywords" (if arabic?
                               ["خرائط البحرين" "خرائط تاريخية" "الخليج العربي" "التراث العمراني" "أرشيف رقمي" "المنامة" "المحرق"]
                               ["Bahrain maps" "historical cartography" "Persian Gulf" "Arabian Gulf" "urban heritage" "digital archive" "Manama" "Muharraq"])
                  "creator" {"@type" "Organization"
                            "name" "mapBH"
                            "url" "https://www.mapbh.org"}
                  "license" "https://creativecommons.org/licenses/by-nc/4.0/"
                  "spatialCoverage" {"@type" "Place"
                                    "name" (if arabic? "البحرين" "Bahrain")
                                    "geo" {"@type" "GeoCoordinates"
                                          "latitude" 26.066700
                                          "longitude" 50.557700}}
                  "url" "https://www.mapbh.org/catalogue"
                  "includedInDataCatalog" {"@type" "DataCatalog"
                                          "name" "mapBH Digital Archive"}
                  "distribution" {"@type" "DataDownload"
                                 "encodingFormat" "application/geo+json"
                                 "contentUrl" "https://map.mapbh.org"}}
                 ;; Add temporal coverage if we have years
                 (when (and min-year max-year)
                   {"temporalCoverage" (if (= min-year max-year)
                                         (str min-year)
                                         (str min-year "/" max-year))})
                 ;; Add individual maps as hasPart subdatasets
                 {"hasPart" (->> all-data
                                (filter :title)
                                (map (fn [map-item]
                                       (merge
                                        {"@type" "Dataset"
                                         "name" (:title map-item)}
                                        (when (:description map-item)
                                          {"description" (:description map-item)})
                                        (when (:year map-item)
                                          {"temporalCoverage" (str (:year map-item))})
                                        (when (:source map-item)
                                          {"creator" {"@type" "Organization"
                                                     "name" (:source map-item)}})
                                        (when (:scale map-item)
                                          {"additionalProperty" {"@type" "PropertyValue"
                                                                "name" "Scale"
                                                                "value" (:scale map-item)}})
                                        (when (:issuer map-item)
                                          {"publisher" {"@type" "Organization"
                                                       "name" (:issuer map-item)}}))))
                                vec)})]
    ;; Return as JSON string for script tag
    (js/JSON.stringify (clj->js dataset) nil 2)))

(defn safe-parse-int
  "Safely parse integer with radix, returning nil if invalid"
  [s]
  (when s
    (let [parsed (js/parseInt s 10)]
      (when-not (js/isNaN parsed) parsed))))

(defn flatten-map-data
  "Convert map data structure to flat list for table display"
  [language include-backlog?]
  (let [;; Process maps structure
        unique-maps (for [[map-id map-info] maps]
                      (let [groups (:groups map-info)
                            title (get-map-text map-info language :title)
                            description (get-map-text map-info language :description)
                            notes (get-map-text map-info language :notes)]
                        {:map-id map-id
                         :title title
                         :description description
                         :notes notes
                         :year (:year map-info)
                         :scale (:scale map-info)
                         :source (:source map-info)
                         :issuer (:issuer map-info)
                         :viewable (:viewable map-info)
                         :group (if (> (count groups) 1)
                                 (str/join ", " groups)
                                 (first groups))
                         :all-groups (vec groups)
                         :has-description (not (str/blank? description))
                         :has-notes (not (str/blank? notes))
                         :has-english true
                         :has-arabic (not (str/blank? (get-map-text map-info :ar :title)))
                         :is-backlog false}))

        ;; Add backlog entries
        backlog-entries (for [[map-id map-info] backlog]
                         (merge map-info
                                {:map-id map-id
                                 :group "Backlog"
                                 :all-groups ["Backlog"]
                                 :year (:year map-info)
                                 :scale (:scale map-info)
                                 :has-description false
                                 :has-notes (not (str/blank? (:notes map-info)))
                                 :has-english true
                                 :has-arabic false
                                 :is-backlog true
                                 :source (:source-file map-info)
                                 :issuer "Pending Processing"}))]

    ;; Combine regular maps with backlog entries (conditionally)
    (if include-backlog?
      (concat unique-maps backlog-entries)
      unique-maps)))

(defn clean-and-parse-number
  "Clean comma-separated number and parse safely"
  [number-str]
  (-> number-str (str/replace #"," "") safe-parse-int))

(defn parse-scale-ratio
  "Parse scale ratio string to numerical value for sorting (e.g., '1:25,000' -> 25000)"
  [scale-str]
  (when scale-str
    (try
      (let [scale-str (str scale-str)
            ;; Match patterns like "1:25,000", "1:25000", "1/25000", etc.
            ratio-match (re-find #"1[:\/]\s*([0-9,]+)" scale-str)]
        (or
         (when ratio-match
           (clean-and-parse-number (second ratio-match)))
         (when-let [number-match (re-find #"([0-9,]+)" scale-str)]
           (clean-and-parse-number (first number-match)))
         999999)) ;; Default large number for non-parseable scales
      (catch js/Error _ 999999))))

(defn sort-data
  "Sort data by given key and direction"
  [data sort-key sort-dir]
  (let [sorted (case sort-key
                 :year (sort-by #(or (:year %) 0) data)
                 :title (sort-by #(str/lower-case (or (:title %) "")) data)
                 :group (sort-by #(str/lower-case (or (:group %) "")) data)
                 :scale (sort-by #(parse-scale-ratio (:scale %)) data)
                 :source (sort-by #(str/lower-case (or (:source %) "")) data)
                 :issuer (sort-by #(str/lower-case (or (:issuer %) "")) data)
                 data)]
    (if (= sort-dir :desc)
      (reverse sorted)
      sorted)))

(defn catalogue-table
  "Render the catalogue table"
  [data sort-state language selected-group-filter update-url-fn]
  (let [{:keys [sort-key sort-dir]} @sort-state
        sorted-data (sort-data data sort-key sort-dir)
        arabic? (= language :ar)
        txt (text arabic?)

        header-click (fn [key]
                       (swap! sort-state
                              (fn [state]
                                (if (= (:sort-key state) key)
                                  (assoc state :sort-dir (if (= (:sort-dir state) :asc) :desc :asc))
                                  (assoc state :sort-key key :sort-dir :asc))))
                       (update-url-fn))

        sort-icon (fn [key]
                    (when (= sort-key key)
                      (if (= sort-dir :asc) "↑" "↓")))]

    [:div.table-container {:style {:overflow-x "auto"}}
     [:table.table.is-striped.is-hoverable.is-fullwidth.is-narrow-mobile
      [:thead
       [:tr
        [:th {:style {:width "120px"}}] ;; Actions column - no text, fixed width
        [:th {:on-click #(header-click :title)
              :style {:cursor "pointer"}}
         (str (get-in txt [:table :title]) " ") [:span.has-text-grey-light (sort-icon :title)]]
        [:th {:on-click #(header-click :year)
              :style {:cursor "pointer"}}
         (str (get-in txt [:table :year]) " ") [:span.has-text-grey-light (sort-icon :year)]]
        [:th {:on-click #(header-click :group)
              :style {:cursor "pointer"}}
         (str (get-in txt [:table :group]) " ") [:span.has-text-grey-light (sort-icon :group)]]
        [:th {:on-click #(header-click :scale)
              :style {:cursor "pointer"}}
         (str (get-in txt [:table :scale]) " ") [:span.has-text-grey-light (sort-icon :scale)]]
        [:th {:on-click #(header-click :source)
              :style {:cursor "pointer"}}
         (str (get-in txt [:table :source]) " ") [:span.has-text-grey-light (sort-icon :source)]]
        [:th {:on-click #(header-click :issuer)
              :style {:cursor "pointer"}}
         (str (get-in txt [:table :issuer]) " ") [:span.has-text-grey-light (sort-icon :issuer)]]]]
      [:tbody
       (doall
        (for [item sorted-data]
          [:tr {:key (:map-id item)
                :style (when (:is-backlog item)
                         {:background-color "#f5f5f5"})}
           ;; Actions column moved to first position
           [:td
            (if (:is-backlog item)
              ;; Show status message for backlog items instead of buttons
              [:span.tag.is-light.is-small
               {:style {:color "#666"}}
               (get-in txt [:table :needs-processing])]
              ;; Regular action buttons for non-backlog items
              (let [primary-group (if (:all-groups item)
                                    (first (:all-groups item))
                                    (:group item))
                    is-viewable (not= false (:viewable item))]
                [:div.buttons.are-small {:style (when (= language :ar) {:justify-content "flex-end"})}
                 [:a.button.is-light.is-small
                  {:href (str "/" (if (= language :ar) "ar" "en")
                              "/map-info"
                              "?group=" (js/encodeURIComponent primary-group)
                              "&map-id=" (js/encodeURIComponent (:map-id item)))}
                  [:i.fas.fa-info-circle {:style (icon-margin language)}]
                  (get-in (text (= language :ar)) [:buttons :info])]
                 (when is-viewable
                   [:a.button.is-light.is-small
                    {:href (str (routes/url-for :map)
                                "?map=" (js/encodeURIComponent (:map-id item))
                                "&flyTo=true")}
                    [:i.fas.fa-map {:style (icon-margin language)}]
                    (get-in (text (= language :ar)) [:buttons :view])])
]))]
           [:td [:strong (:title item)]]
           [:td (when (:year item) (:year item))]
           [:td
            (if (:all-groups item)
              [:div.field.is-grouped.is-grouped-multiline
               (for [group (:all-groups item)]
                 [:div.control {:key group}
                  [:span.tag.is-info.is-light.is-clickable
                   {:style {:cursor "pointer"}
                    :on-click #(do
                                (reset! selected-group-filter group)
                                (update-url-fn))}
                   group]])]
              [:span.tag.is-info.is-light.is-clickable
               {:style {:cursor "pointer"}
                :on-click #(do
                             (reset! selected-group-filter (:group item))
                             (update-url-fn))}
               (:group item)])]
           [:td (:scale item)]
           [:td (:source item)]
           [:td (:issuer item)]]))]]]))

(defn search-filter
  "Filter data based on search term, matching all words"
  [search-term data]
  (if (str/blank? search-term)
    data
    (let [search-words (->> (str/split (str/lower-case search-term) #"\s+")
                            (remove str/blank?))]
      (filter (fn [item]
                (let [searchable-text (str/lower-case
                                       (str/join " "
                                                 (filter identity
                                                         [(:title item) (:group item) (:source item) (:issuer item)
                                                          (:scale item) (:description item) (:notes item) (str (:year item))])))]
                  (every? #(str/includes? searchable-text %) search-words)))
              data))))

(defn group-filter
  "Filter data based on selected group"
  [selected-group data]
  (if (str/blank? selected-group)
    data
    (filter (fn [item]
              (if (:all-groups item)
                (some #(= % selected-group) (:all-groups item))
                (= (:group item) selected-group)))
            data)))

(defn parse-catalogue-params
  "Parse URL parameters for catalogue page"
  []
  (let [params (url/get-query-params)]
    {:search (or (:search params) "")
     :group (or (:group params) "")
     :sort (keyword (or (:sort params) "year"))
     :dir (keyword (or (:dir params) "asc"))
     :backlog (not= "false" (:backlog params))}))

(defn update-catalogue-url!
  "Update URL with current catalogue filters without page reload"
  [search group sort-key sort-dir include-backlog]
  (let [params (cond-> {}
                 (not (str/blank? search)) (assoc :search search)
                 (not (str/blank? group)) (assoc :group group)
                 (and sort-key (not= sort-key :year)) (assoc :sort (name sort-key))
                 (and sort-dir (not= sort-dir :asc)) (assoc :dir (name sort-dir))
                 (not include-backlog) (assoc :backlog "false"))]
    (url/set-query-params! params)))

(defn catalogue
  []
  (let [language* (rf/subscribe [::model/language])
        initial-params (parse-catalogue-params)
        search-term (r/atom (:search initial-params))
        selected-group-filter (r/atom (:group initial-params))
        sort-state (r/atom {:sort-key (:sort initial-params)
                           :sort-dir (:dir initial-params)})
        include-backlog (r/atom (:backlog initial-params))]
    (fn []
      (let [language @language*
            arabic? (= language :ar)
            txt (text arabic?)
            update-url-fn (fn []
                            (update-catalogue-url!
                             @search-term
                             @selected-group-filter
                             (:sort-key @sort-state)
                             (:sort-dir @sort-state)
                             @include-backlog))
            all-data (flatten-map-data language @include-backlog)
            group-filtered-data (group-filter @selected-group-filter all-data)
            filtered-data (search-filter @search-term group-filtered-data)
            json-ld (generate-json-ld language all-data)]
        [:div.container (merge {:style {:margin-top "4rem" :margin-bottom "2rem" :padding "0 1rem"}}
                               (when arabic? (rtl-attrs)))
         ;; JSON-LD structured data script tag (SEO)
         [:script {:type "application/ld+json"
                   :dangerouslySetInnerHTML {:__html json-ld}}]
         [:div.content
          [:h1.title.is-2.has-text-centered-mobile (get-in txt [:page :title])]
          [:p.subtitle.has-text-centered-mobile (get-in txt [:page :subtitle])]

          ;; Mobile-first search field
          [:div.field
           [:div.control (if arabic? {:class "has-icons-right"} {:class "has-icons-left"})
            [:input.input
             {:type "text"
              :placeholder (get-in txt [:page :search-placeholder])
              :value @search-term
              :on-change #(do
                           (reset! search-term (-> % .-target .-value))
                           (update-url-fn))}]
            [:span.icon (if arabic? {:class "is-right"} {:class "is-left"})
             [:i.fas.fa-search]]]]

          ;; Include Backlog checkbox
          [:div.field
           [:div.control
            [:label.checkbox (when arabic? {:style {:direction "rtl"}})
             (if arabic?
               [:<>
                [:span {:style {:margin-right "0.5rem"}} (get-in txt [:page :include-backlog])]
                [:input {:type "checkbox"
                         :checked @include-backlog
                         :on-change #(do
                                      (reset! include-backlog (-> % .-target .-checked))
                                      (update-url-fn))}]]
               [:<>
                [:input {:type "checkbox"
                         :checked @include-backlog
                         :on-change #(do
                                      (reset! include-backlog (-> % .-target .-checked))
                                      (update-url-fn))}]
                [:span {:style {:margin-left "0.5rem"}} (get-in txt [:page :include-backlog])]])]]]

          ;; Group filter button (mobile-friendly)
          (when (not (str/blank? @selected-group-filter))
            [:div.field
             [:div.control
              [:button.button.is-small.is-info.is-light
               {:on-click #(do
                            (reset! selected-group-filter "")
                            (update-url-fn))}
               (if arabic?
                 [:<>
                  [:span.icon.is-small [:i.fas.fa-times {:style {:margin-right "0.3rem"}}]]
                  [:span @selected-group-filter]]
                 [:<>
                  [:span @selected-group-filter]
                  [:span.icon.is-small [:i.fas.fa-times {:style {:margin-left "0.3rem"}}]]])]]])

          ;; Mobile-responsive level
          [:div.level.is-mobile
           [:div (if arabic? {:class "level-right"} {:class "level-left"})
            [:div.level-item
             [:p.has-text-grey.is-size-7-mobile
              ((get-in txt [:page :showing-count]) (count filtered-data) (count all-data))]]]
           ;; Sort controls - hidden on mobile, shown on tablet+
           [:div (if arabic? {:class "level-left is-hidden-mobile"} {:class "level-right is-hidden-mobile"})
            [:div.level-item
             [:div.field.is-grouped
              [:div.control
               [:div.tags.has-addons
                [:span.tag.is-small (get-in txt [:page :sort-by])]
                [:span.tag.is-info.is-small (name (:sort-key @sort-state))]]]]]]]
          [catalogue-table filtered-data sort-state language selected-group-filter update-url-fn]]]))))

(ns app.pages.catalogue
  (:require [reagent.core :as r]
            [app.pages.map.map-data :as map-data :refer [ar-layers backlog]]
            [app.routes :as routes]
            [clojure.string :as str]))

(defn extract-year
  "Extract year from title string"
  [title]
  (let [year-match (re-find #"\b(19|20)\d{2}\b" title)]
    (when year-match (js/parseInt year-match))))

(defn get-layer-bounds
  "Get appropriate bounds and zoom for different map types"
  [group-name map-id title]
  (cond
    ;; Specific area maps - higher zoom
    (or (str/includes? (str/lower-case group-name) "manama")
        (str/includes? (str/lower-case (str title)) "manama"))
    {:lat 26.2281 :lng 50.5822 :zoom 13}

    (or (str/includes? (str/lower-case group-name) "muharraq")
        (str/includes? (str/lower-case (str title)) "muharraq"))
    {:lat 26.2572 :lng 50.6119 :zoom 13}

    ;; Harbor/port specific
    (str/includes? (str/lower-case (str title)) "harbour")
    {:lat 26.2200 :lng 50.6100 :zoom 14}

    ;; Awali area
    (str/includes? (str/lower-case (str title)) "awali")
    {:lat 26.0659 :lng 50.4817 :zoom 13}

    ;; Other specific locations
    (str/includes? (str/lower-case (str title)) "jufayr")
    {:lat 26.2300 :lng 50.6200 :zoom 13}

    (str/includes? (str/lower-case (str title)) "budaiya")
    {:lat 26.2000 :lng 50.4500 :zoom 13}

    (str/includes? (str/lower-case (str title)) "riffa")
    {:lat 26.1300 :lng 50.5500 :zoom 13}

    (str/includes? (str/lower-case (str title)) "hawar")
    {:lat 25.6500 :lng 50.7500 :zoom 12}

    ;; Geological/specialized maps - medium zoom to see detail
    (or (str/includes? (str/lower-case group-name) "geological")
        (str/includes? (str/lower-case (str title)) "geology")
        (str/includes? (str/lower-case (str title)) "geomorphology")
        (str/includes? (str/lower-case (str title)) "drainage")
        (str/includes? (str/lower-case (str title)) "agriculture"))
    {:lat 26.0450 :lng 50.5460 :zoom 11}

    ;; Fairey surveys - medium zoom for topographic detail
    (str/includes? (str/lower-case group-name) "fairey")
    {:lat 26.0450 :lng 50.5460 :zoom 12}

    ;; Default to Bahrain center with appropriate zoom
    :else
    {:lat 26.0450 :lng 50.5460 :zoom 10}))

(defn flatten-map-data
  "Convert nested map data structure to flat list for table display"
  [language include-backlog?]
  (let [;; First collect all map entries with their groups
        all-entries (for [[group-name group-maps] map-data/layers
                         [map-id map-info] group-maps]
                     {:map-id map-id
                      :group group-name
                      :map-info map-info})

        ;; Group by map-id to find maps that appear in multiple groups
        grouped-by-id (group-by :map-id all-entries)
        ;; Create final entries with all groups listed
        unique-maps (for [[map-id entries] grouped-by-id]
                     (let [first-entry (first entries)
                           all-groups (map :group entries)
                           map-info (:map-info first-entry)]
                       (let [primary-group (first all-groups)
                             has-arabic-translation (get-in ar-layers [primary-group map-id])
                             ;; Use Arabic data when available and language is Arabic
                             display-data (if (and (= language :ar) has-arabic-translation)
                                            (merge map-info has-arabic-translation)
                                            map-info)]
                         (merge display-data
                                {:map-id map-id
                                 :group (if (> (count all-groups) 1)
                                         (str/join ", " all-groups)
                                         (first all-groups))
                                 :all-groups all-groups
                                 :year (or (:year display-data) (extract-year (:title display-data)))
                                 :has-description (not (str/blank? (:description display-data)))
                                 :has-notes (not (str/blank? (:notes display-data)))
                                 :has-english true  ; All items in the main layers have English
                                 :has-arabic (boolean has-arabic-translation)
                                 :is-backlog false}))))

        ;; Add backlog entries
        backlog-entries (for [[map-id map-info] backlog]
                         (merge map-info
                                {:map-id map-id
                                 :group "Backlog"
                                 :all-groups ["Backlog"]
                                 :year (:year map-info)
                                 :has-description false
                                 :has-notes (not (str/blank? (:notes map-info)))
                                 :has-english true
                                 :has-arabic false
                                 :is-backlog true
                                 :scale nil
                                 :source (:source-file map-info)
                                 :issuer "Pending Processing"}))]

    ;; Combine regular maps with backlog entries (conditionally)
    (if include-backlog?
      (concat unique-maps backlog-entries)
      unique-maps)))

(defn parse-scale-ratio
  "Parse scale ratio string to numerical value for sorting (e.g., '1:25,000' -> 25000)"
  [scale-str]
  (when scale-str
    (try
      (let [scale-str (str scale-str)
            ;; Match patterns like "1:25,000", "1:25000", "1/25000", etc.
            ratio-match (re-find #"1[:\/]\s*([0-9,]+)" scale-str)]
        (if ratio-match
          (let [number-str (second ratio-match)
                ;; Remove commas and parse as integer
                clean-number (-> number-str (str/replace #"," "") js/parseInt)]
            (if (js/isNaN clean-number) 999999 clean-number))
          ;; If no ratio pattern found, try to extract any number
          (let [number-match (re-find #"([0-9,]+)" scale-str)]
            (if number-match
              (let [clean-number (-> (first number-match) (str/replace #"," "") js/parseInt)]
                (if (js/isNaN clean-number) 999999 clean-number))
              999999)))) ;; Default large number for non-parseable scales
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
  [data sort-state language selected-group-filter]
  (let [{:keys [sort-key sort-dir]} @sort-state
        sorted-data (sort-data data sort-key sort-dir)

        header-click (fn [key]
                       (swap! sort-state
                              (fn [state]
                                (if (= (:sort-key state) key)
                                  (assoc state :sort-dir (if (= (:sort-dir state) :asc) :desc :asc))
                                  (assoc state :sort-key key :sort-dir :asc)))))

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
         (if (= language :ar) "العنوان " "Title ") [:span.has-text-grey-light (sort-icon :title)]]
        [:th {:on-click #(header-click :year)
              :style {:cursor "pointer"}}
         (if (= language :ar) "السنة " "Year ") [:span.has-text-grey-light (sort-icon :year)]]
        [:th {:on-click #(header-click :group)
              :style {:cursor "pointer"}}
         (if (= language :ar) "المجموعة " "Group ") [:span.has-text-grey-light (sort-icon :group)]]
        [:th {:on-click #(header-click :scale)
              :style {:cursor "pointer"}}
         (if (= language :ar) "المقياس " "Scale ") [:span.has-text-grey-light (sort-icon :scale)]]
        [:th {:on-click #(header-click :source)
              :style {:cursor "pointer"}}
         (if (= language :ar) "المصدر " "Source ") [:span.has-text-grey-light (sort-icon :source)]]
        [:th {:on-click #(header-click :issuer)
              :style {:cursor "pointer"}}
         (if (= language :ar) "الناشر " "Issuer ") [:span.has-text-grey-light (sort-icon :issuer)]]]]
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
               (if (= language :ar) "قيد المعالجة" "Needs Processing")]
              ;; Regular action buttons for non-backlog items
              (let [primary-group (if (:all-groups item)
                                    (first (:all-groups item))
                                    (:group item))
                    bounds (get-layer-bounds primary-group (:map-id item) (:title item))]
                [:div.buttons.are-small {:style (when (= language :ar) {:justify-content "flex-end"})}
                 [:a.button.is-light.is-small
                  {:href (str "/" (if (= language :ar) "ar" "en")
                              "/map-info"
                              "?group=" (js/encodeURIComponent primary-group)
                              "&map-id=" (js/encodeURIComponent (:map-id item)))}
                  [:i.fas.fa-info-circle {:style (if (= language :ar)
                                                          {:margin-left "0.5rem" :padding "0.2rem"}
                                                          {:margin-right "0.5rem" :padding "0.2rem"})}]
                  (if (= language :ar) "تفاصيل" "Info")]
                 [:a.button.is-light.is-small
                  {:href (str (routes/url-for :map)
                              "?map=" (js/encodeURIComponent (:map-id item))
                              "&coords=" (:lat bounds) "," (:lng bounds)
                              "&zoom=" (:zoom bounds))}
                  [:i.fas.fa-map {:style (if (= language :ar)
                                                 {:margin-left "0.5rem" :padding "0.2rem"}
                                                 {:margin-right "0.5rem" :padding "0.2rem"})}]
                  (if (= language :ar) "عرض" "View")]]))]
           [:td [:strong (:title item)]]
           [:td (when (:year item) (:year item))]
           [:td
            (if (:all-groups item)
              [:div.field.is-grouped.is-grouped-multiline
               (for [group (:all-groups item)]
                 [:div.control {:key group}
                  [:span.tag.is-info.is-light.is-clickable
                   {:style {:cursor "pointer"}
                    :on-click #(reset! selected-group-filter group)}
                   group]])]
              [:span.tag.is-info.is-light.is-clickable
               {:style {:cursor "pointer"}
                :on-click #(reset! selected-group-filter (:group item))}
               (:group item)])]
           [:td (:scale item)]
           [:td (:source item)]
           [:td (:issuer item)]]))]]]))

(defn search-filter
    "Filter data based on search term"
    [search-term data]
    (if (str/blank? search-term)
      data
      (filter (fn [item]
                (some #(and %
                            (str/includes? (str/lower-case (str %))
                                           (str/lower-case search-term)))
                      [(:title item) (:group item) (:source item) (:issuer item)
                       (:scale item) (:description item) (:notes item)]))
              data)))

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

(defn catalogue-en
  "English version of catalogue page"
  []
  (let [search-term (r/atom "")
        selected-group-filter (r/atom "")
        sort-state (r/atom {:sort-key :year :sort-dir :desc})
        include-backlog (r/atom true)]

    (fn []
      (let [all-data (flatten-map-data :en @include-backlog)
            group-filtered-data (group-filter @selected-group-filter all-data)
            filtered-data (search-filter @search-term group-filtered-data)]
        [:div.container {:style {:margin-top "4rem" :margin-bottom "2rem" :padding "0 1rem"}}
         [:div.content
          [:h1.title.is-2.has-text-centered-mobile "Catalogue"]
          [:p.subtitle.has-text-centered-mobile "Browse and search through all maps on the site"]

          ;; Mobile-first search field
          [:div.field
           [:div.control.has-icons-left
            [:input.input
             {:type "text"
              :placeholder "Search maps..."
              :value @search-term
              :on-change #(reset! search-term (-> % .-target .-value))}]
            [:span.icon.is-left
             [:i.fas.fa-search]]]]

          ;; Include Backlog checkbox
          [:div.field
           [:div.control
            [:label.checkbox
             [:input {:type "checkbox"
                      :checked @include-backlog
                      :on-change #(reset! include-backlog (-> % .-target .-checked))}]
             [:span {:style {:margin-left "0.5rem"}} "Include Backlog Maps"]]]]

          ;; Group filter button (mobile-friendly)
          (when (not (str/blank? @selected-group-filter))
            [:div.field
             [:div.control
              [:button.button.is-small.is-info.is-light
               {:on-click #(reset! selected-group-filter "")}
               [:span @selected-group-filter]
               [:span.icon.is-small [:i.fas.fa-times {:style {:margin-left "0.3rem"}}]]]]])

          ;; Mobile-responsive level
          [:div.level.is-mobile
           [:div.level-left
            [:div.level-item
             [:p.has-text-grey.is-size-7-mobile
              (str "Showing " (count filtered-data) " of " (count all-data) " maps")]]]
           ;; Sort controls - hidden on mobile, shown on tablet+
           [:div.level-right.is-hidden-mobile
            [:div.level-item
             [:div.field.is-grouped
              [:div.control
               [:div.tags.has-addons
                [:span.tag.is-small "Sort by:"]
                [:span.tag.is-info.is-small (name (:sort-key @sort-state))]]]]]]]

          [catalogue-table filtered-data sort-state :en selected-group-filter]]]))))

(defn catalogue-ar
  "Arabic version of catalogue page"
  []
  (let [search-term (r/atom "")
        selected-group-filter (r/atom "")
        sort-state (r/atom {:sort-key :year :sort-dir :desc})
        include-backlog (r/atom true)]

    (fn []
      (let [all-data (flatten-map-data :ar @include-backlog)
            group-filtered-data (group-filter @selected-group-filter all-data)
            filtered-data (search-filter @search-term group-filtered-data)]
        [:div.container {:style {:margin-top "4rem" :margin-bottom "2rem" :padding "0 1rem"}
                         :lang "ar" :dir "rtl"}
         [:div.content
          [:h1.title.is-2.has-text-centered-mobile "فهرس الخرائط"]
          [:p.subtitle.has-text-centered-mobile "تصفح وابحث جميع الخرائط"]

          ;; Mobile-first search field (RTL)
          [:div.field
           [:div.control.has-icons-right
            [:input.input
             {:type "text"
              :placeholder "ابحث في الخرائط..."
              :value @search-term
              :on-change #(reset! search-term (-> % .-target .-value))}]
            [:span.icon.is-right
             [:i.fas.fa-search]]]]

          ;; Include Backlog checkbox (RTL)
          [:div.field
           [:div.control
            [:label.checkbox {:style {:direction "rtl"}}
             [:span {:style {:margin-right "0.5rem"}} "تضمين خرائط قائمة الانتظار"]
             [:input {:type "checkbox"
                      :checked @include-backlog
                      :on-change #(reset! include-backlog (-> % .-target .-checked))}]]]]

          ;; Group filter button (mobile-friendly, RTL)
          (when (not (str/blank? @selected-group-filter))
            [:div.field
             [:div.control
              [:button.button.is-small.is-info.is-light
               {:on-click #(reset! selected-group-filter "")}
               [:span.icon.is-small [:i.fas.fa-times {:style {:margin-right "0.3rem"}}]]
               [:span @selected-group-filter]]]])

          ;; Mobile-responsive level (RTL)
          [:div.level.is-mobile
           [:div.level-right
            [:div.level-item
             [:p.has-text-grey.is-size-7-mobile
              (str "عرض " (count filtered-data) " من " (count all-data) " خريطة")]]]
           ;; Sort controls - hidden on mobile, shown on tablet+
           [:div.level-left.is-hidden-mobile
            [:div.level-item
             [:div.field.is-grouped
              [:div.control
               [:div.tags.has-addons
                [:span.tag.is-small "ترتيب حسب:"]
                [:span.tag.is-info.is-small (name (:sort-key @sort-state))]]]]]]]

          [catalogue-table filtered-data sort-state :ar selected-group-filter]]]))))

(defn catalogue
  "Main catalogue component with language switching"
  [language]
  (condp = language
    :en [catalogue-en]
    :ar [catalogue-ar]
    [catalogue-en]))

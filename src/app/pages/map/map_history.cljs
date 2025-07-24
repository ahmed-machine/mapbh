(ns app.pages.map.map-history
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [app.model :as model]
            [app.pages.map.map-data :refer [layers ar-layers base-satellite default-map-state]]
            [app.util.url :as url]))


(defn text
  [details ar-details arabic?]
  (get
   {:en {:description (merge {:panel "Description"
                              :title-header "Title"
                              :scale-header "Scale"
                              :notes-header "Notes"
                              :source-header "Source"
                              :issuer-header "Issuer"
                              :submitter-header "Submitted by"} details)
         :buttons {:switch-mode {:transparency "Transparency Mode"
                                 :split "Split Mode"}
                   :description "Description"}}
    :ar {:description (merge {:panel  "تفاصيل"
                              :title-header "العنوان"
                              :scale-header "مقياس"
                              :notes-header "ملاحظات"
                              :source-header  "المصدر"
                              :issuer-header "الناشر"
                              :submitter-header " مساهمة"} ar-details)
         :buttons {:switch-mode {:transparency "شفاف"
                                 :split "ابو قسمين"}
                   :description  "تفاصيل"}}} (if arabic? :ar :en)))


(defn modal-description
  [state* arabic?]
  (let [details (get-in layers (:selected @state*))
        ar-details (merge details (get-in ar-layers (:selected @state*)))
        txt (:description (text details ar-details arabic?))]
    [:div.modal {:id "modal-description" :lang (if arabic? "ar" "en") :dir (if arabic? "rtl" "ltr")}
     [:div.modal-content
      [:p.panel-block [:strong (:title-header txt)] ": "
       (:title txt)]
      [:div.panel-block [:strong (:scale-header txt)] ": " (:scale txt)]
      (when (:description txt)
        [:p.panel-block.description-text
         (:description txt)])
      [:p.panel-block.description-text
       [:strong (:notes-header txt)] ": " (:notes txt)]
      [:a {:href (:source-link details) :style {:color "#DA291C"}}
       [:div.panel-block {:style {:color "#DA291C"}} [:strong (:source-header txt)] ": " (:source txt) " - "
        [:span.icon.home [:i.fas.fa-download]] "(georectified)"]]
      (when (:issuer txt)
        [:a {:href (:issuer-link txt)}
         [:div.panel-block {:style {:color "#DA291C" :padding-bottom "10px"}}
          [:strong (:issuer-header txt)] ": " (str " " (:issuer txt)) " - "
          [:span.icon.home [:i.fas.fa-download]] "(original)"]])
      (when (:submitted-by txt)
        [:div.panel-block {:style {:padding-bottom "10px"}} [:strong (:submitter-header txt)] ": "
         (if (:submitted-by-url details)
           [:a {:href (:submitted-by-url txt)}
            (str " " (:submitted-by txt))]
           (str " " (:submitted-by txt)))])
      [:button.modal-close.is-large.is-danger {:aria-label "close"
                                               :style (merge {} (if arabic? {:left "0"} {:right "0"}))
                                               :on-click (fn [e] (swap! state* update :show-description? not)
                                                           (-> js/document (.getElementById "modal-description") .-classList (.toggle "is-active")))}]]]))

(defn map-container
  []
  [:div#mapid {:style {:height (str "calc(" js/window.screen.availHeight "px - 10rem)")}}])

(defn get-layer
  [state*]
  (if-let [layers (:layers @state*)]
    (get-in layers (:selected  @state*))))


(defn update-transparency
  [layer v]
  (-> layer (.setOpacity (/ v 100))))


(defn transparency-slider
  [state* arabic?]
  (if-let [layer (get-layer state*)]
    (if-let [transparency (-> layer (aget "options") (aget "opacity"))]
      [:input {:title "Adjust Transparency" :style (merge {:position :absolute :background "transparent" :opacity 0.6 :bottom "35px"
                                                           :width "120px"
                                                           :z-index 998}
                                                          (if arabic? {:left "12px"} {:right "12px"}))
               :on-change (fn [e v]
                            (let [new-transparency (/ (.. e -target -value) 100)]
                              (update-transparency layer (.. e -target -value))
                              (swap! state* assoc :transparency new-transparency)
                              ;; Update URL when transparency changes
                              (js/setTimeout #(when (:map @state*) (update-url-from-current-state! (:map @state*) state*)) 100)))
               :class (str "slider " (:selected @state*)) :step 1 :min 0 :max 100 :default-value (* transparency 100) :type "range"}])))


(defn pan-map
  [lat long zoom map]
  (when (and zoom lat long) (.flyTo map (-> js/L (.latLng lat long)) zoom #js {:animate false})))


(defn update-url-from-current-state!
  "Update URL with current map state including position"
  [map state*]
  (when map
    (try
      (let [center (.getCenter map)
            zoom (.getZoom map)
            lat (.-lat center)
            lng (.-lng center)
            current-state (merge @state* {:lat lat :lng lng :zoom zoom})]
        (url/update-url-from-map-state! current-state))
      (catch js/Error e
        (.warn js/console "Failed to update URL from map state:" e)))))

(defn base-layer-change
  [map state*]
  (.on map "overlayadd" (fn [layer]
                          (let [gname (-> layer js->clj (get "group") (get "name"))
                                lname (-> layer js->clj (get "name"))]
                            (when (and (not= lname "Terrain") (not= lname "Satellite"))
                              (swap! state* assoc :selected [gname lname])
                              ;; Update URL when layer changes
                              (js/setTimeout #(when map (update-url-from-current-state! map state*)) 100)))))
  (.on map "baselayerchange" (fn [layer]
                               (let [lname (-> layer js->clj (get "name"))]
                                 (when (or (= lname "Terrain") (= lname "Satellite"))
                                   (swap! state* assoc :base (get (js->clj layer) "name"))
                                   ;; Update URL when base layer changes
                                   (js/setTimeout #(when map (update-url-from-current-state! map state*)) 100)))))
  ;; Update URL when map is moved or zoomed
  (.on map "moveend" (fn [] (when map (update-url-from-current-state! map state*))))
  nil)


(defn download-button
  [state*]
  (let [map (:map @state*)]
  ;; Add option to save map
    [:button.button.is-success.is-small.is-light.is-outlined {:style {:position :absolute :height "30px" :border-radius "2px" :font-size "0.6rem" :top "180px" :left "12px" :z-index 997}
                                                    :on-click (fn [] (-> (new js/LeafletExporter. map 4.0) .Export))}
     [:i.fas.fa-download]]))


(defn init-map
  [state*]
  (let [map (:map @state*)]
    (when map (do (.off map) (.remove map)
                  (-> js/document (.getElementById "mapid") (aset "innerHTML" "")))))
  (let [base-layers (->> base-satellite
                         (mapv (fn [[k selected-layer]]
                                 [k (-> js/L (.tileLayer (:url selected-layer) (-> selected-layer :opts clj->js)))]))
                         (into {}))
        process-layers (fn [layers] (->> layers (mapv (fn [[k selected-layer]] [k (-> js/L (.tileLayer (:url selected-layer) (-> selected-layer :opts clj->js)))]))
                                         (sort-by first)
                                         (into (sorted-map))))
        overlay-layers (->> layers (map (fn [[k v]] [k (process-layers v)])) (into (sorted-map)))
        {:keys [zoom lat lng]} @state*
        ;; Use coordinates from state (which may come from URL params) or defaults
        init-lat (or lat (:lat default-map-state))
        init-lng (or lng (:lng default-map-state))
        init-zoom (or zoom (:zoom default-map-state))
        map (-> js/L (.map "mapid" (clj->js {:maxBounds (-> js/L (.latLngBounds (-> js/L (.latLng 25.5 50))
                                                                                (-> js/L (.latLng 26.5 51))))}))

                (.setView #js [init-lat init-lng] init-zoom))
        base (or (-> base-layers (get (:base @state*)))
                 (-> base-layers (get (:base default-map-state)))  ; fallback to default
                 (first (vals base-layers)))        ; ultimate fallback
        selected (or (get-in overlay-layers (:selected @state*))
                    (get-in overlay-layers [(:group default-map-state) (:map-id default-map-state)])  ; fallback
                    (first (vals (first (vals overlay-layers)))))        ; ultimate fallback
        ]
    ;; Add all layers in control
    (-> js/L .-control (.groupedLayers (clj->js base-layers)
                                       (clj->js overlay-layers)
                                       (clj->js {"exclusiveGroups" (keys overlay-layers)
                                                 "allExclusive" true
                                                 "groupCheckboxes" false
                                                 "groupsCollapsible" true}))
        (.addTo map))

    ;; Add option to locate user
    (-> js/L .-control (.locate (clj->js {:keepCurrentZoomLevel true
                                          :locateOptions (clj->js {:enableHighAccuracy true})
                                          :followMarkerStyle (clj->js {:radius 5 :fillColor "rgb(241, 70, 104, 0.7)" :weight 5})
                                          :markerStyle (clj->js {:radius 5 :fillColor "rgb(241, 70, 104, 0.7)" :weight 5})})) (.addTo map));

     ;; Register handlers to update state with new layer so other components can pull the data
    (when map (base-layer-change map state*))

    ;; Zoom to location (pan-map expects lat lng zoom map)
    (pan-map init-lat init-lng init-zoom map)

    ;; Add Base
    (when (and map base)
      (try
        (-> map (.addLayer base))
        (catch js/Error e
          (.error js/console "Failed to add base layer:" e base))))
    ;; Add pre-selected default map
    (when (and map selected)
      (try
        (-> map (.addLayer selected))
        (catch js/Error e
          (.error js/console "Failed to add selected layer:" e selected))))
    ;; Store state
    (swap! state* assoc :layers overlay-layers :map map)

    {:base-layers base-layers
     :overlay-layers overlay-layers
     :zoom zoom :lat lat :long long
     :map map
     :base base
     :selected selected}))


(defn transparency-init-map
  [state*]
  (init-map state*))


(defn sbs-init-map
  [state*]
  (let [_  (swap! state* assoc :transparency 1.0)
        {:keys [map base selected]} (init-map state*)]
    ;; Create side-by-side mode
    (-> js/L .-control (.sideBySide base selected) (.addTo map))
    (.setOpacity selected 1.0)))

(defn switch-mode
  [state* arabic?]
  (let [mode (:mode @state*)
        map (:map @state*)
        txt (get-in (text nil nil arabic?) [:buttons :switch-mode])]
    [:button.button.is-danger.is-small.is-rounded
     {:style {:position :absolute :top "65px" :left "60px" :z-index 997 :font-size (when arabic? "105%")}
      :on-click (fn []
                  (let [zoom-level (.getZoom map)
                        {:keys [lat lng]} (js->clj (.getCenter map) :keywordize-keys true)]
                    (swap! state* assoc :zoom zoom-level :lat lat :lng lng)
                    (if (= mode "transparency")
                      (do (swap! state* assoc :mode "side-by-side" :transparency 1.0)
                          (sbs-init-map state*)
                          ;; Update URL after mode change
                          (js/setTimeout #(when (:map @state*) (update-url-from-current-state! (:map @state*) state*)) 200))
                      (do (swap! state* assoc :mode "transparency")
                          (transparency-init-map state*)
                          ;; Update URL after mode change
                          (js/setTimeout #(when (:map @state*) (update-url-from-current-state! (:map @state*) state*)) 200)))))}
     (if (= mode "transparency") (:split txt) (:transparency txt))]))

(defn modal-button
  [state* arabic?]
  [:button.button.is-light
   {:style (merge (if arabic? {:right "12px"} {:left "12px"}) {:position :absolute :bottom "23px"  :z-index 997 :font-size (when arabic? "105%")})
    :on-click (fn [e] (swap! state* update :show-description? not)
                (-> js/document (.getElementById "modal-description") .-classList (.toggle "is-active")))}

   [:i.fa.fa-list {:style {:margin-right "1rem"}}]
   (get-in (text nil nil arabic?) [:buttons :description])])


(defn historical-map []
  (let [;; Start with safe defaults, URL parsing will happen in component-did-mount
        initial-state (merge {:show-description? false
                             :selected [(:group default-map-state) (:map-id default-map-state)]}
                            (dissoc default-map-state :group :map-id))
        state* (reagent/atom initial-state)
        language* (rf/subscribe [::model/language])]
    (reagent/create-class
     {:component-did-mount
      (fn [] ;; Setup Map
        ;; Parse URL parameters and update state safely after component mount
        (try
          (let [url-state (url/parse-url-params layers)]
            (when (seq url-state)
              (swap! state* merge url-state)))
          (catch js/Error e
            (.warn js/console "Failed to parse URL parameters:" e)))

        ;; Initialize map with current state
        (if (= "transparency" (:mode @state*))
          (transparency-init-map state*)
          (sbs-init-map state*)))
      :render
      (fn []
        (let [arabic? (= :ar @language*)]
          [:div#map-history {:style {:overflow-y :none}}
           [map-container]
           [modal-button state* arabic?]
           [modal-description state* arabic?]
           [switch-mode state* arabic?]
           (when (= "transparency" (:mode @state*)) [download-button state*])
           (when (= "transparency" (:mode @state*)) [transparency-slider state* arabic?])]))})))

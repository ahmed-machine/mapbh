(ns app.pages.map
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [app.model :as model]
            [app.data :refer [base-satellite default-map-state maps get-map-text get-grouped-maps get-viewable-maps tileserver-url]]
            [app.util.url :as url]
            [clojure.string :as str]))

;; Constants
(def ^:private transparency-mode "transparency")
(def ^:private side-by-side-mode "side-by-side")

;; Helper function to get map data
(defn get-map-from-maps
  "Get map data by [group map-id] path"
  [[_group map-id]]
  (get maps map-id))

(defn fetch-tilejson-center
  "Fetch center coordinates from TileJSON metadata"
  [map-id]
  (let [map-info (get maps map-id)
        map-url (:url map-info)
        ;; Extract the tile name from the URL pattern like /data/TILE-NAME/{z}/{x}/{y}.png
        ;; and convert it to /data/TILE-NAME.json
        tilejson-url (when map-url
                       (-> map-url
                           (.replace "/{z}/{x}/{y}.png" ".json")))]
    (-> (js/fetch tilejson-url)
        (.then (fn [response]
                 (.json response)))
        (.then (fn [tilejson]
                 (let [center (.-center tilejson)
                       minzoom (.-minzoom tilejson)]
                   (if center
                     {:lat (aget center 1)
                      :lng (aget center 0)
                      :zoom (+ 1 minzoom)}
                     {:lat 26.0450 :lng 50.5460 :zoom 10}))))
        (.catch (fn [_]
                  {:lat 26.0450 :lng 50.5460 :zoom 10})))))

(defn auto-center-map-from-tilejson
  "Auto-center map using TileJSON data for the selected layer"
  [map map-id]
  (when (and map map-id)
    (-> (fetch-tilejson-center map-id)
        (.then (fn [center]
                 (.flyTo map
                         (-> js/L (.latLng (:lat center) (:lng center)))
                         (:zoom center)
                         #js {:animate true :duration 1.5})))
        (.catch (fn [_])))))


(defn text
  [details ar-details arabic?]
  (get
   {:en {:description (merge {:panel "Description"
                              :title-header "Title"
                              :scale-header "Scale"
                              :notes-header "Notes"
                              :source-header "Source"
                              :issuer-header "Issuer"
                              :submitter-header "Submitted by"
                              :additional-link "Additional Link"
                              :additional-link-2 "Additional Link 2"
                              :additional-link-3 "Additional Link 3"} details)
         :buttons {:switch-mode {:transparency "Transparency Mode"
                                 :split "Split Mode"}
                   :description "Description"}}
    :ar {:description (merge {:panel  "تفاصيل"
                              :title-header "العنوان"
                              :scale-header "مقياس"
                              :notes-header "ملاحظات"
                              :source-header  "المصدر"
                              :issuer-header "الناشر"
                              :submitter-header " مساهمة"
                              :additional-link "رابط إضافي"
                              :additional-link-2 "رابط إضافي ثاني"
                              :additional-link-3 "رابط إضافي ثالث"} ar-details)
         :buttons {:switch-mode {:transparency "شفاف"
                                 :split "ابو قسمين"}
                   :description  "تفاصيل"}}} (if arabic? :ar :en)))

(defn close-modal
  "Close modal with proper transition back to button state"
  [state*]
  ;; Immediately close modal without opacity fade
  (swap! state* assoc :show-description? false :morphing? false))

(defn modal-description-content
  "Reusable modal content component that can be embedded or used in traditional modal"
  [state* arabic?]
  (let [;; Get map data
        selected-map-id (second (:selected @state*))
        details (when-let [map-info (get maps selected-map-id)]
                  (merge map-info
                         {:title (get-map-text map-info (if arabic? :ar :en) :title)
                          :description (get-map-text map-info (if arabic? :ar :en) :description)
                          :notes (get-map-text map-info (if arabic? :ar :en) :notes)
                          :submitted-by (get-map-text map-info (if arabic? :ar :en) :submitted-by)}))
        txt (:description (text details details arabic?))]
    [:div.modal-inner-content {:lang (if arabic? "ar" "en") :dir (if arabic? "rtl" "ltr")}
     [:p.panel-block [:strong (:title-header txt)] ": "
      (:title txt)]
     [:div.panel-block [:strong (:scale-header txt)] ": " (:scale txt)]
     (when (:description txt)
       [:p.panel-block.description-text
        (:description txt)])
     (when (:link-1 details)
       [:a {:href (:link-1 details)
            :target "_blank"}
        [:div.panel-block.modal-link-block
         [:span.icon.home [:i.fas.fa-external-link-alt]]
         (or (:link-1-label details)
             (:additional-link txt))]])
     (when (:link-2 details)
       [:a {:href (:link-2 details)
            :target "_blank"}
        [:div.panel-block.modal-link-block
         [:span.icon.home [:i.fas.fa-external-link-alt]]
         (or (:link-2-label details)
             (:additional-link-2 txt))]])
     (when (:link-3 details)
       [:a {:href (:link-3 details)
            :target "_blank"}
        [:div.panel-block.modal-link-block
         [:span.icon.home [:i.fas.fa-external-link-alt]]
         (or (:link-3-label details)
             (:additional-link-3 txt))]])
     [:p.panel-block.description-text
      [:strong (:notes-header txt)] ": " (:notes txt)]
     [:a.modal-link {:href (:source-link details)}
      [:div.panel-block.modal-link-block [:strong (:source-header txt)] ": " (:source txt) " - "
       [:span.icon.home [:i.fas.fa-download]] "(original)"]]
     (when (:issuer txt)
       [:a {:href (:issuer-link txt)}
        [:div.panel-block.modal-issuer-block
         [:strong (:issuer-header txt)] ": " (str " " (:issuer txt)) " - "
         [:span.icon.home [:i.fas.fa-download]] "(georectified)"]])
     (when (:submitted-by txt)
       [:div.panel-block.submitter-block [:strong (:submitter-header txt)] ": "
        (if (:submitted-by-url details)
          [:a {:href (:submitted-by-url txt)}
           (str " " (:submitted-by txt))]
          (str " " (:submitted-by txt)))])
     ;; Close button
     [:button.modal-close.is-large.modal-close-positioned {:aria-label "close"
                                    :on-click (fn [e]
                                                (.preventDefault e)
                                                (.stopPropagation e)
                                                (close-modal state*))}]]))

(defn map-container
  []
  [:div#mapid])

(defn get-layer
  [state*]
  (if-let [layers (:layers @state*)]
    (get-in layers (:selected  @state*))))

(defn get-pinned-layer
  "Get the pinned base layer if base is a pinned layer"
  ([state*] (get-pinned-layer state* nil))
  ([state* overlay-layers]
    (let [base (:base @state*)
          layers (or overlay-layers (:layers @state*))]
      (when-let [[group map-id] (url/parse-pinned-base-string base)]
        (when layers
          (get-in layers [group map-id]))))))

(defn update-transparency
  [layer v]
  (-> layer (.setOpacity (/ v 100))))

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

(defn transparency-slider
  [state* arabic?]
  (if-let [layer (get-layer state*)]
    (if-let [transparency (-> layer (aget "options") (aget "opacity"))]
      [:input {:title "Adjust Transparency"
               :class (str "slider transparency-slider " (if arabic? "arabic" "english") " " (:selected @state*))
               :on-change (fn [e]
                            (let [new-transparency (/ (.. e -target -value) 100)]
                              (update-transparency layer (.. e -target -value))
                              (swap! state* assoc :transparency new-transparency)
                              ;; Update URL when transparency changes
                              (js/setTimeout #(when (:map @state*) (update-url-from-current-state! (:map @state*) state*)) 100)))
               :step 1 :min 0 :max 100 :default-value (* transparency 100) :type "range"}])))



(defn pan-map
  [lat long zoom map]
  (when (and zoom lat long) (.flyTo map (-> js/L (.latLng lat long)) zoom #js {:animate false})))

(defn download-button
  [state*]
  (let [export-state (reagent/atom :ready)]  ; :ready, :exporting
    (fn []
      (let [map (:map @state*)]  ; Get map from current state
        [:button.download-button.button.is-small.is-light.is-outlined
         {:class (if (= @export-state :exporting) "is-warning" "is-success") ; yellow when loading, green when ready
          :on-click (fn []
                      (when map  ; Only proceed if map exists
                        (reset! export-state :exporting)
                        ;; Small delay to ensure UI updates before export
                        (js/setTimeout
                         (fn []
                           (try
                             (let [current-layer-data (get-map-from-maps (:selected @state*))
                                   layer-title (str (or (get-map-text current-layer-data :en :title) "exported"))
                                   ;; Clean filename for safe file download
                                   safe-filename (-> layer-title
                                                    (str/replace #"[/\\:*?\"<>|]" "-")
                                                    (str/replace #"\s+" "_"))
                                   exporter (new js/LeafletExporter. map 1.0)
                                   result (.Export exporter safe-filename)]
                               (if (and result (.-then result))
                                 (-> result
                                     (.then (fn [] (reset! export-state :ready)))
                                     (.catch (fn [e]
                                               (.error js/console "Export failed:" e)
                                               (reset! export-state :ready))))
                                 (js/setTimeout #(reset! export-state :ready) 3000)))
                             (catch js/Error e
                               (.error js/console "Export failed:" e)
                               (reset! export-state :ready))))
                         100)))}
         (case @export-state
           :exporting [:i.fa.fa-cog.fa-spin]
           :ready [:i.fas.fa-download])]))))

(defn base-layer-change
  [map state*]
  (.on map "overlayadd" (fn [layer]
                          (let [gname (-> layer js->clj (get "group") (get "name"))
                                lname (-> layer js->clj (get "name"))
                                layer-obj (get (js->clj layer) "layer")]
                            (when (and (not= lname "Terrain") (not= lname "Satellite"))
                              (swap! state* assoc :selected [gname lname])
                              ;; Auto-center map based on TileJSON if should-auto-center flag is set
                              (when (:should-auto-center @state*)
                                (auto-center-map-from-tilejson map lname)
                                ;; Clear the auto-center flag after first use
                                (swap! state* dissoc :should-auto-center))
                              ;; In side-by-side mode, recreate the control with current layers
                              (when (and (get-pinned-layer state*) (= (:mode @state*) side-by-side-mode))
                                ;; Use setTimeout to let the initial control creation finish first
                                (js/setTimeout
                                  (fn []
                                    ;; Remove any existing control
                                    (when-let [existing-sbs (:sbs-control @state*)]
                                      (try
                                        (.remove existing-sbs)
                                        (catch js/Error _))
                                      (swap! state* dissoc :sbs-control))

                                    ;; Find and remove any orphaned side-by-side controls from DOM
                                    (let [orphaned-controls (.querySelectorAll js/document ".leaflet-sbs")]
                                      (doseq [control (array-seq orphaned-controls)]
                                        (when (.-parentNode control)
                                          (.removeChild (.-parentNode control) control))))

                                    (let [layers (:layers @state*)
                                          base-string (:base @state*)]
                                      (when-let [[group map-id] (url/parse-pinned-base-string base-string)]
                                        (let [pinned-base (get-in layers [group map-id])]
                                          (when (and pinned-base layer-obj (.hasLayer map layer-obj))
                                            ;; Ensure pinned base is on the map
                                            (when (not (.hasLayer map pinned-base))
                                              (.addLayer map pinned-base))
                                            ;; Create side-by-side control
                                            (let [new-sbs (-> js/L .-control (.sideBySide pinned-base layer-obj) (.addTo map))]
                                              (swap! state* assoc :sbs-control new-sbs)
                                              (.setOpacity pinned-base 1.0)
                                              (.setOpacity layer-obj 1.0))))))) 100))
                              ;; If we have a pinned base in transparency mode, ensure the new overlay appears on top
                              (when (and (get-pinned-layer state*) (= (:mode @state*) transparency-mode))
                                (.bringToFront layer-obj))
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

(defn init-map
  [state*]
  (let [map (:map @state*)]
    (when map (do (.off map) (.remove map)
                  (-> js/document (.getElementById "mapid") (aset "innerHTML" "")))))
  (let [standard-base-layers (->> base-satellite
                                      (mapv (fn [[k selected-layer]]
                                              [k (-> js/L (.tileLayer (:url selected-layer) (-> selected-layer :opts clj->js)))]))
                                      (into {}))
        process-layers (fn [layers] (->> layers (mapv (fn [[k selected-layer]] [k (-> js/L (.tileLayer (:url selected-layer) (-> selected-layer :opts clj->js)))]))
                                         (sort-by first)
                                         (into (sorted-map))))
        overlay-layers ;; Create grouped layers from maps
                       (->> (get-grouped-maps (get-viewable-maps maps))
                            (map (fn [[k v]] [k (process-layers v)]))
                            (into (sorted-map)))
        ;; Now create base-layers with potential pinned layer
        base-layers (if-let [pinned (get-pinned-layer state* overlay-layers)]
                      (let [[group map-id] (:selected @state*)
                            layer-key (str group " - " map-id)
                            pinned-name (str "📌 " layer-key " (Pinned)")]
                        (assoc standard-base-layers pinned-name pinned))
                      standard-base-layers)
        {:keys [zoom lat lng]} @state*
        ;; Use coordinates from state (which may come from URL params) or defaults
        init-lat (or lat (:lat default-map-state))
        init-lng (or lng (:lng default-map-state))
        init-zoom (or zoom (:zoom default-map-state))
        map (-> js/L (.map "mapid" (clj->js {:maxBounds (-> js/L (.latLngBounds (-> js/L (.latLng 25.5 50))
                                                                                (-> js/L (.latLng 26.5 51))))}))

                (.setView #js [init-lat init-lng] init-zoom))
        pinned-layer (get-pinned-layer state* overlay-layers)
        base (if pinned-layer
               pinned-layer  ; Use pinned layer as base
               (or (-> base-layers (get (:base @state*)))
                   (-> base-layers (get (:base default-map-state)))  ; fallback to default
                   (first (vals base-layers))))
        selected-layer-data (or (get-in overlay-layers (:selected @state*))
                               (get-in overlay-layers [(:group default-map-state) (:map-id default-map-state)])  ; fallback
                               (first (vals (first (vals overlay-layers)))))        ; ultimate fallback
        ;; Create separate layer instance if selected is same as pinned base
        selected (if (and pinned-layer selected-layer-data (= pinned-layer selected-layer-data))
                   ;; Create new instance of the same layer for overlay
                   (let [layer-config (get-map-from-maps (:selected @state*))]
                     (-> js/L (.tileLayer (:url layer-config) (-> layer-config :opts clj->js))))
                   selected-layer-data)
        ]
    ;; Add all layers in control
    (let [all-exclusive (not (boolean pinned-layer))]
      (-> js/L .-control (.groupedLayers (clj->js base-layers)
                                         (clj->js overlay-layers)
                                         (clj->js {"exclusiveGroups" (keys overlay-layers)
                                                   "allExclusive" all-exclusive
                                                   "groupCheckboxes" false
                                                   "groupsCollapsible" true}))
          (.addTo map)))

    ;; Add option to locate user
    (-> js/L .-control (.locate (clj->js {:keepCurrentZoomLevel true
                                          :locateOptions (clj->js {:enableHighAccuracy true})
                                          :followMarkerStyle (clj->js {:radius 5 :fillColor "rgb(241, 70, 104, 0.7)" :weight 5})
                                          :markerStyle (clj->js {:radius 5 :fillColor "rgb(241, 70, 104, 0.7)" :weight 5})})) (.addTo map));

     ;; Register handlers to update state with new layer so other components can pull the data
    (when map (base-layer-change map state*))

    ;; Zoom to location (pan-map expects lat lng zoom map)
    (pan-map init-lat init-lng init-zoom map)

    ;; Add base layer first (which could be standard base or pinned layer)
    (when (and map base)
      (try
        (-> map (.addLayer base))
        (when pinned-layer
          ;; If base is a pinned layer, set opacity to 1.0
          (.setOpacity base 1.0))
        (catch js/Error e
          (.error js/console "Failed to add base layer:" e base))))

    ;; Add pre-selected overlay AFTER base layer (only if different from base)
    ;; This ensures overlay appears on top since Leaflet renders last-added layer on top
    (when (and map selected (not= selected base))
      (try
        (-> map (.addLayer selected))
        ;; Apply transparency from state if it exists
        (when-let [transparency (:transparency @state*)]
          (.setOpacity selected transparency))
        (catch js/Error e
          (.error js/console "Failed to add selected layer:" e selected))))
    ;; Store state
    (swap! state* assoc :layers overlay-layers :map map)

    {:base-layers base-layers
     :overlay-layers overlay-layers
     :zoom zoom :lat lat :lng lng
     :map map
     :base base
     :selected selected}))

(defn sbs-init-map
  [state*]
  (let [_  (swap! state* assoc :transparency 1.0)
        ;; Clean up existing side-by-side control before initializing
        _ (when-let [existing-sbs (:sbs-control @state*)]
            (try
              (.remove existing-sbs)
              (catch js/Error _))
            (swap! state* dissoc :sbs-control))
        {:keys [map base selected base-layers]} (init-map state*)
        is-pinned-base (get-pinned-layer state*)
        standard-base (or (-> base-layers (get (:base default-map-state)))
                         (first (vals base-layers)))]
    ;; Create side-by-side mode
    (cond
      ;; Case 1: Pinned base + different overlay selected
      (and is-pinned-base selected (not= selected base))
      (do
        ;; For side-by-side with pinned base, we need to ensure the selected layer
        ;; is properly added to the map but not conflicting with the base
        (when (and map selected)
          ;; Remove the selected layer if it was already added by init-map
          (when (.hasLayer map selected)
            (.removeLayer map selected))
          ;; Add it back to ensure proper layering
          (.addLayer map selected))

        ;; Create side-by-side control with base on left, selected on right
        (let [sbs-control (-> js/L .-control (.sideBySide base selected) (.addTo map))]
          (swap! state* assoc :sbs-control sbs-control))
        (.setOpacity base 1.0)
        (.setOpacity selected 1.0)
        ;; Ensure transparency state is set to 1.0
        (swap! state* assoc :transparency 1.0))

      ;; Case 2: Pinned base but same as selected (or no different overlay)
      is-pinned-base
      (do
        (let [sbs-control (-> js/L .-control (.sideBySide standard-base base) (.addTo map))]
          (swap! state* assoc :sbs-control sbs-control))
        (.setOpacity standard-base 1.0)
        (.setOpacity base 1.0)
        ;; Ensure transparency state is set to 1.0
        (swap! state* assoc :transparency 1.0))

      ;; Case 3: No pinned base, standard mode
      selected
      (do
        (let [sbs-control (-> js/L .-control (.sideBySide base selected) (.addTo map))]
          (swap! state* assoc :sbs-control sbs-control))
        (.setOpacity base 1.0)
        (.setOpacity selected 1.0)
        ;; Ensure transparency state is set to 1.0
        (swap! state* assoc :transparency 1.0))

      ;; Case 4: Nothing to show
      :else
      nil)))

(defn pin-current-as-base
  "Pin the currently selected overlay as base layer"
  [state*]
  (when-let [selected (:selected @state*)]
    (let [[group map-id] selected
          pinned-base-str (str "pinned-" group "__" map-id)  ; Use __ as delimiter
          map (:map @state*)
          current-zoom (when map (.getZoom map))
          current-center (when map (.getCenter map))]
      ;; Store current position in state before reinitializing
      (when (and current-zoom current-center)
        (swap! state* assoc
               :zoom current-zoom
               :lat (.-lat current-center)
               :lng (.-lng current-center)))
      ;; Store the pinned layer info in base parameter and keep selected for overlay
      (swap! state* assoc :base pinned-base-str)
      ;; Reinitialize map to apply pinned base
      (if (= (:mode @state*) transparency-mode)
        (init-map state*)
        (sbs-init-map state*)))))

(defn unpin-base
  "Remove pinned base layer and revert to standard base"
  [state*]
  (let [map (:map @state*)
        current-zoom (when map (.getZoom map))
        current-center (when map (.getCenter map))]
    ;; Store current position in state before reinitializing
    (when (and current-zoom current-center)
      (swap! state* assoc
             :zoom current-zoom
             :lat (.-lat current-center)
             :lng (.-lng current-center)))
    ;; Revert to default base layer
    (swap! state* assoc :base (:base default-map-state))
    ;; Reinitialize map to remove pinned base
    (if (= (:mode @state*) transparency-mode)
      (init-map state*)
      (sbs-init-map state*))))


(defn pin-button
  "Button to pin/unpin current overlay as base layer"
  [state* _]
  (let [base (:base @state*)
        is-pinned (and base (string? base) (.startsWith base "pinned-"))
        selected (:selected @state*)]
    (if (or is-pinned selected)  ; Return button if there's something to show
      (if is-pinned
        ;; Show unpin button if there's a pinned base
        [:button.pin-button.button.is-warning.is-small.is-light.is-outlined
         {:on-click #(do (unpin-base state*)
                        (js/setTimeout (fn [] (when (:map @state*)
                                               (update-url-from-current-state! (:map @state*) state*))) 100))}
         [:i.fas.fa-unlink]]
        ;; Show pin button if no pinned base and there's a selected layer
        [:button.pin-button.button.is-success.is-small.is-light.is-outlined
         {:on-click #(do (pin-current-as-base state*)
                        (js/setTimeout (fn [] (when (:map @state*)
                                               (update-url-from-current-state! (:map @state*) state*))) 100))}
         [:i.fas.fa-thumbtack]])
      ;; Return empty div if nothing to show (valid Hiccup)
      [:div])))

(defn switch-mode
  [state* arabic?]
  (let [mode (:mode @state*)
        map (:map @state*)
        txt (get-in (text nil nil arabic?) [:buttons :switch-mode])]
    [:button.button.is-danger.is-small.is-rounded.switch-mode-button
     {:lang (when arabic? "ar")
      :on-click (fn []
                  (let [zoom-level (.getZoom map)
                        {:keys [lat lng]} (js->clj (.getCenter map) :keywordize-keys true)]
                    (swap! state* assoc :zoom zoom-level :lat lat :lng lng)
                    (if (= mode transparency-mode)
                      (do
                        (swap! state* assoc :mode side-by-side-mode :transparency 1.0)
                        (sbs-init-map state*)
                        ;; Update URL after mode change
                        (js/setTimeout #(when (:map @state*) (update-url-from-current-state! (:map @state*) state*)) 200))
                      (do
                        ;; Clean up side-by-side control when switching to transparency mode
                        (when-let [existing-sbs (:sbs-control @state*)]
                          (try
                            (.remove existing-sbs)
                            (catch js/Error _))
                          (swap! state* dissoc :sbs-control))
                        (swap! state* assoc :mode transparency-mode :transparency 0.65)
                        (init-map state*)
                        ;; Update URL after mode change
                        (js/setTimeout #(when (:map @state*) (update-url-from-current-state! (:map @state*) state*)) 200)))))}
     (if (= mode transparency-mode) (:split txt) (:transparency txt))]))

(defn modal-button
  [state* arabic?]
  (let [morphing? (:morphing? @state*)
        show-description? (:show-description? @state*)]
    [:<>
     ;; Backdrop element - only visible when modal is open
     (when show-description?
       [:div.modal-backdrop
        {:on-click (fn [e]
                     (.preventDefault e)
                     (.stopPropagation e)
                     (close-modal state*))}])

     [:button.button.is-light.modal-button
      {:class (str (if arabic? "arabic " "english ")
                   (if morphing? "morphing " "normal ")
                   (when arabic? "large-font ")
                   (when morphing? "expanding-to-modal ")
                   (when show-description? "expanded-modal"))
       :disabled morphing?
       :on-click (fn [_]
                   (when-not (or morphing? show-description?)
                     ;; Start expansion animation
                     (swap! state* assoc :morphing? true)
                     ;; After animation completes, show modal content
                     (js/setTimeout
                       #(swap! state* assoc :show-description? true :morphing? false)
                       300)))}

      [:div.button-content.button-content-fade {:class (if morphing? "hidden" "visible")}
       [:i.fa.fa-list.description-icon]
       [:span (get-in (text nil nil arabic?) [:buttons :description])]]

      (when show-description?
        [:div.modal-content-embedded
         {:ref (fn [el]
                 (when el
                   ;; Set up scroll detection
                   (let [check-scroll (fn []
                                       (let [inner (.querySelector el ".modal-inner-content")]
                                         (when inner
                                           (let [scrollable? (> (.-scrollHeight inner) (.-clientHeight inner))
                                                 at-bottom? (<= (- (.-scrollHeight inner) (.-scrollTop inner))
                                                               (+ (.-clientHeight inner) 5))]
                                             (if scrollable?
                                               (do
                                                 (.add (.-classList el) "has-scroll")
                                                 (if at-bottom?
                                                   (.add (.-classList el) "at-bottom")
                                                   (.remove (.-classList el) "at-bottom")))
                                               (.remove (.-classList el) "has-scroll"))))))]
                     ;; Initial check
                     (js/setTimeout check-scroll 100)
                     ;; Set up scroll listener
                     (when-let [inner (.querySelector el ".modal-inner-content")]
                       (.addEventListener inner "scroll" check-scroll)))))}
         [modal-description-content state* arabic?]])]]))


(defn historical-map []
  (let [;; Start with safe defaults, URL parsing will happen in component-did-mount
        initial-state (merge {:show-description? false
                             :morphing? false
                             :selected [(:group default-map-state) (:map-id default-map-state)]}
                            (dissoc default-map-state :group :map-id))
        state* (reagent/atom initial-state)
        language* (rf/subscribe [::model/language])]
    (reagent/create-class
     {:component-did-mount
      (fn [] ;; Setup Map
        ;; Add map-page class to body to prevent scrolling
        (-> js/document .-body .-classList (.add "map-page"))

        ;; Parse URL parameters and update state safely after component mount
        (try
          (let [url-state (url/parse-url-params (get-grouped-maps (get-viewable-maps maps)))]
            (when (seq url-state)
              (swap! state* merge url-state)))
          (catch js/Error e
            (.warn js/console "Failed to parse URL parameters:" e)))

        ;; Initialize map with current state
        (if (= transparency-mode (:mode @state*))
          (init-map state*)
          (sbs-init-map state*)))

      :component-will-unmount
      (fn []
        ;; Remove map-page class from body when leaving map page
        (-> js/document .-body .-classList (.remove "map-page")))
      :render
      (fn []
        (let [arabic? (= :ar @language*)]
          [:div#map-history.map-history-container
           [map-container]
           [modal-button state* arabic?]
           [switch-mode state* arabic?]
           [pin-button state* arabic?]
           (when (= transparency-mode (:mode @state*)) [download-button state*])
           (when (= transparency-mode (:mode @state*)) [transparency-slider state* arabic?])]))})))
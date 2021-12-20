(ns app.pages.map.map-history
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [app.model :as model]
            [app.pages.map.map-data :refer [layers ar-layers base-satellite]]
            ["leaflet" :as l]))


(defn map-description
  [state* arabic?]
  (let [active-key (:selected @state*)
        details (get layers active-key)
        ar-details (merge details (get ar-layers active-key))]
    [:nav.panel.is-collapsible {:lang (if arabic? "ar" "en") :dir (if arabic? "rtl" "ltr") :style (merge {:position :absolute :bottom 0 :z-index 1000 :width 225 :height :max-content :background "whitesmoke" :font-size 12} (if arabic? {:right "12px"} {:left "12px"}))}
     [:p.panel-heading {:on-click (fn [e] (swap! state* update :show-description? not)
                                    (-> js/document (.getElementById "description") .-classList (.toggle "is-hidden"))) :class " is-hidden-fullscreen" :aria-label "more options"}(if arabic? "تفاصيل" "Description")
      (if (:show-description? @state*) "   -" "   +")]
     [:div {:id "description" :class "is-hidden"}
      [:p.panel-block (if arabic? "العنوان" "Title") ": "
       (or (when arabic? (:title ar-details)) (:title details))]
      [:div.panel-block (if arabic? "مقياس" "Scale") ": " (:scale details)]
      (when (:description details)
        [:p.panel-block.description-text
         (or (when arabic? (:description ar-details)) (:description details))])
      [:p.panel-block.description-text
       (if arabic? "ملاحظات" "Notes") ": "
       (or (when arabic? (:notes ar-details)) (:notes details))]
      [:a {:href (:source-link details) :style {:color "#DA291C"}}
       [:div.panel-block {:style {:color "#DA291C"}} (if arabic? "المصدر" "Source") ": " (:source details) " - "
        [:span.icon.home [:i.fas.fa-download]] "(georectified)"]]
      (when (:issuer details)
        [:a {:href (:issuer-link details)}
         [:div.panel-block {:style {:color "#DA291C" :padding-bottom "10px"}}
          (if arabic? "الناشر" "Issuer") ": "
          (str " " (:issuer details)) " - "
          [:span.icon.home [:i.fas.fa-download]] "(original)"] ])
      (when (:submitted-by details)
        [:div.panel-block {:style {:padding-bottom "10px"}} (if arabic? " مساهمة" "Submitted by") ": "
         (if (:submitted-by-url details)
           [:a {:href (:submitted-by-url details)}
            (if arabic? (str " " (:submitted-by ar-details)) (str " " (:submitted-by details)))]
           (if arabic? (str " " (:submitted-by ar-details)) (str " " (:submitted-by details))))])]]))


(defn map-container
  []
  [:div#mapid {:style {:height (str "calc(" js/window.screen.availHeight "px - 10rem)")}}])

(defn get-layer
  [state*]
  (if-let [layers (:layers @state*)]
    (get layers (:selected  @state*))))


(defn update-transparency
  [layer v]
  (-> layer (.setOpacity (/ v 100))))


(defn transparency-slider
  [state* arabic?]
  (if-let [layer (get-layer state*)]
    (if-let [transparency (-> layer (aget "options") (aget "opacity"))]
      [:input {:title "Adjust Transparency" :style (merge {:position :absolute :background "transparent" :opacity 0.6 :bottom "35px"

                                                           :z-index 998}
                                                          (if arabic? {:left "12px"} {:right "12px"}))
               :on-change (fn [e v] (update-transparency layer (.. e -target -value)))
               :class (str "slider " (:selected @state*)) :step 1 :min 0 :max 100 :default-value (* transparency 100) :type "range"}])))


(defn pan-map
  [lat long zoom map]
  (when (and zoom lat long) (.flyTo map (-> l (.latLng lat long)) zoom #js {:animate false})))


(defn get-location
  [map state* position]
  (let [{:keys [circle]} @state*
        lat  (-> position .-coords .-latitude)
        long  (-> position .-coords .-longitude)
        accuracy (-> position .-coords .-accuracy)]
    (when circle (.removeLayer map circle))
    (let [circle  (-> l (.circleMarker (clj->js [lat long]) (clj->js {:radius 2})) (.setStyle (clj->js {:color "rgb(241, 70, 104, 0.7)" :weight 5})))]
      (swap! state* assoc :circle circle)
      (.addTo circle map))))

(defn polling-request
  [handler timeout]
  ;; Will call the handler every timeout interval
  (js/setInterval handler timeout))


(defn base-layer-change
  [map state*]
  (when map (-> map
              (.on "overlayadd" (fn [layer]
                                  (let [lname (-> layer js->clj (get "name"))]
                                    (when (and (not= lname "Terrain") (not= lname "Satellite"))
                                      (swap! state* assoc :selected (get (js->clj layer) "name"))))))
              (.on "baselayerchange" (fn [layer]
                                       (let [lname (-> layer js->clj (get "name"))]
                                         (when (or (= lname "Terrain") (= lname "Satellite"))
                                           (swap! state* assoc :base (get (js->clj layer) "name")))))))))

(defn init-map
  [state*]
  (let [map (:map @state*)]
    (when map (do (.off map) (.remove map)
                  (-> js/document (.getElementById "mapid") (aset "innerHTML" "")))))
  (let [base-layers (->> base-satellite
                         (mapv (fn [[k selected-layer]]
                                 [k (-> l (.tileLayer (:url selected-layer) (-> selected-layer :opts clj->js)))]))
                         (into {}))
        overlay-layers (->> layers
                            (mapv (fn [[k selected-layer]]
                                    [k (-> l (.tileLayer (:url selected-layer) (-> selected-layer :opts clj->js)))]))
                            (sort-by first)
                            (into (sorted-map)))
        {:keys [zoom lat long]} @state*
        map (-> l (.map "mapid" (clj->js {:maxBounds (-> l (.latLngBounds (-> l (.latLng 25.5 50))
                                                                          (-> l (.latLng 26.5 51))))}))

                (.setView #js [26.05 50.4849414] 10.3))
        base (-> base-layers (get (:base @state*)))
        selected (get overlay-layers (:selected @state*))]
    (-> l .-control (.groupedLayers (clj->js base-layers) (clj->js {"Maps" overlay-layers}) (clj->js {"exclusiveGroups" ["Maps"] "groupCheckboxes" false})) (.addTo map))
    ;; Zoom to location
    (pan-map lat long zoom map)
    ;; Show user's location
    (polling-request
     (-> js/navigator .-geolocation (.getCurrentPosition
                                     (fn [position] (get-location map state* position)))) 5000)
    ;; Add Base
    (-> map (.addLayer base))
    ;; Add pre-selected default map
    (-> map (.addLayer selected))
    ;; Update state with new layer so other components can pull the data
    (base-layer-change map state*)
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
    (-> l .-control (.sideBySide base selected) (.addTo map))
    (.setOpacity selected 1.0)))

(defn switch-mode
  [state* arabic?]
  (let [mode (:mode @state*)
        map (:map @state*)]
    [:button.button.is-danger.is-small.is-rounded
     {:style {:position :absolute :top "65px" :left "60px" :z-index 997 :font-size (when arabic? "105%")}
      :on-click (fn []
                  (when map
                    (let [zoom-level (when map (.getZoom map))
                          {:keys [lat lng]} (when map (js->clj (.getCenter map) :keywordize-keys true))]
                      (swap! state* assoc :zoom zoom-level :lat lat :long lng)
                      (if (= mode "transparency")
                        (do (swap! state* assoc :mode "side-by-side" :transparency 1.0)
                            (sbs-init-map state*))
                        (do (swap! state* assoc :mode "transparency")
                            (transparency-init-map state*))))))}
     (if (= mode "transparency")
       (if arabic? "ابو قسمين" "Split Mode")
       (if arabic? "شفاف"  "Transparency Mode"))]))


(defn historical-map []
  (let [state* (reagent/atom {:selected "1977 - USSR - Manama/Muharraq"
                              :mode "transparency"
                              :base "Satellite"
                              :show-description? false
                              :transparency 0.65})
        language* (rf/subscribe [::model/language])]
    (reagent/create-class
      {:component-did-mount
       (fn [] ;; Setup Map
         (if (= "transparency" (:mode @state*))
           (transparency-init-map state*)
           (sbs-init-map state*)))
       :render
       (fn []
         (let [arabic? (= "ar" @language*)]
           [:div#map-history {:style {:overflow-y :none}}
            [map-container]
            [map-description state* arabic?]
            [switch-mode state* arabic?]
            (when (= "transparency" (:mode @state*)) [transparency-slider state* arabic?])]))})))

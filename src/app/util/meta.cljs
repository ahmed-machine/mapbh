(ns app.util.meta
  "Centralized meta tag management utility"
  (:require [re-frame.core :as rf]
            [app.model :as model]))

(defn get-current-language
  "Get current language from re-frame state"
  []
  @(rf/subscribe [::model/language]))

(defn get-current-url
  "Get current URL from browser"
  []
  (.-href js/window.location))

(defn get-current-domain
  "Get current domain dynamically based on environment"
  []
  (let [location js/window.location
        protocol (.-protocol location)
        host (.-host location)]
    (str protocol "//" host)))

(defn get-base-url
  "Get base URL with language prefix"
  [language]
  (if (= language :ar)
    "https://www.mapbh.org/ar"
    "https://www.mapbh.org/en"))

(def default-meta
  "Default meta tag values"
  {:title "mapBH - Digital Map Archive"
   :description "Explore Bahrain's history through interactive historical maps from the 19th century onwards. Highlighting urban development, land reclamation, green belt, and geographical changes over time."
   :image "https://mapbh.org/img/ogbrand.png"
   :image-alt "mapBH - Digital Map Archive"
   :keywords ["Bahrain historical maps" "historical cartography" "Bahrain geography" "land reclamation" "urban development" "Middle East history" "interactive maps" "GIS Bahrain" "historical research" "Arabian Gulf maps"]})

(defn set-page-meta!
  "Set all meta tags for a page. Takes a config map and merges with defaults.
  Automatically infers current URL and language.

  Usage: (set-page-meta! {:title 'Custom Title' :description 'Custom description'})
  "
  [config]
  (let [language (get-current-language)
        current-url (get-current-url)
        base-url (get-base-url language)
        ;; Merge config with defaults
        meta-config (merge default-meta config)
        {:keys [title description image image-alt keywords]} meta-config
        title (str title " - mapBH")]

    ;; Set document title
    (set! (.-title js/document) title)

    ;; Helper functions to update or create meta tags
    (letfn [(update-meta-tag! [property content]
              (if-let [existing-tag (.querySelector js/document (str "meta[property='" property "']"))]
                (set! (.-content existing-tag) content)
                (let [meta-tag (.createElement js/document "meta")]
                  (set! (.-property meta-tag) property)
                  (set! (.-content meta-tag) content)
                  (.appendChild (.-head js/document) meta-tag))))

            (update-name-meta-tag! [name content]
              (if-let [existing-tag (.querySelector js/document (str "meta[name='" name "']"))]
                (set! (.-content existing-tag) content)
                (let [meta-tag (.createElement js/document "meta")]
                  (set! (.-name meta-tag) name)
                  (set! (.-content meta-tag) content)
                  (.appendChild (.-head js/document) meta-tag))))]

      ;; Update basic meta tags
      (update-name-meta-tag! "description" description)
      (when keywords
        (update-name-meta-tag! "keywords" (if (string? keywords) keywords (clojure.string/join ", " keywords))))

      ;; Update OpenGraph meta tags
      (update-meta-tag! "og:title" title)
      (update-meta-tag! "og:description" description)
      (update-meta-tag! "og:url" current-url)
      (update-meta-tag! "og:image" image)
      (update-meta-tag! "og:image:alt" image-alt)

      ;; Update Twitter Card meta tags
      (update-name-meta-tag! "twitter:title" title)
      (update-name-meta-tag! "twitter:description" description)
      (update-name-meta-tag! "twitter:image" image)
      (update-name-meta-tag! "twitter:image:alt" image-alt)

      ;; Update canonical URL
      (if-let [canonical-link (.querySelector js/document "link[rel='canonical']")]
        (set! (.-href canonical-link) current-url)
        (let [link-tag (.createElement js/document "link")]
          (set! (.-rel link-tag) "canonical")
          (set! (.-href link-tag) current-url)
          (.appendChild (.-head js/document) link-tag))))))

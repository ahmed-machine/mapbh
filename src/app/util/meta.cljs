(ns app.util.meta
  "Centralized meta tag management utility"
  (:require [re-frame.core :as rf]
            [app.model :as model]
            [app.pages.articles.index :as article-index]
            [clojure.string :as str]))

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

;; Static page meta configurations
(def page-meta-configs
  "Meta tag configurations for static pages"
  {:home {:en {}  ; Uses defaults
          :ar {}}

   :about {:en {:title "About"
                :description "mapBH project is a digital archive documenting the history of Bahrain."
                :keywords ["about project" "digital archive" "heritage" "Bahrain"]}
           :ar {:title "نبذة"
                :description "mapBH مشروع ارشفة يوثق تاريخ البحرين"
                :keywords ["حول المشروع" "أرشيف رقمي" "التراث" "البحرين"]}}

   :catalogue {:en {:title "Catalogue"
                    :description "Browse our comprehensive collection of historical maps of Bahrain from the 19th century onwards. Search, filter, and discover the history of Bahrain."
                    :keywords ["map catalogue" "Bahrain maps" "historical archive" "cartography" "geography"]}
               :ar {:title "فهرس"
                    :description "تصفح مجموعة شاملة من الخرائط التاريخية للبحرين من القرن التاسع عشر وما بعده. ابحث وصنف واكتشف التطور العمراني والجغرافي."
                    :keywords ["فهرس الخرائط" "خرائط البحرين" "أرشيف رقمي" "التاريخ" "الجغرافيا"]}}

   :contribute {:en {:title "Contribute"
                     :description "Contribute to the mapBH project for archiving Bahrain's historical maps. All types of contributions welcome including maps, translations, and development."
                     :keywords ["contribute" "open source" "historical maps" "Bahrain"]}
                :ar {:title "المساهمة في المشروع - mapBH"
                     :description "ساهم في مشروع mapBH لأرشفة الخرائط التاريخية للبحرين. نرحب بجميع أنواع المساهمات من خرائط وترجمات وتطوير."
                     :keywords ["مساهمة" "مشروع مفتوح المصدر" "خرائط تاريخية" "البحرين"]}}

   :map {:en {:title "Explore"
              :description "Explore Bahrain's historical maps interactively. Compare historical and modern satellite imagery with transparency controls and side-by-side viewing."
              :keywords ["interactive maps" "historical maps" "Bahrain maps" "map viewer" "cartography"]}
         :ar {:title "استكشف"
              :description "استكشف خرائط البحرين التاريخية بشكل تفاعلي. قارن بين الخرائط التاريخية وصور الأقمار الصناعية الحديثة مع إمكانية التحكم في الشفافية والعرض جنباً إلى جنب."
              :keywords ["خرائط تفاعلية" "خرائط تاريخية" "خرائط البحرين" "عارض الخرائط" "رسم الخرائط"]}}

   :article-index {:en {:title "Articles"
                        :description "In-depth articles about Bahrain's history, historical maps, urban development, and cartographic heritage."
                        :keywords ["articles" "Bahrain history" "historical maps" "heritage"]}
                   :ar {:title "المقالات - mapBH"
                        :description " مقالات متعمقة حول تاريخ البحرين والخرائط التاريخية والتطور العمراني والتراث الخرائطي."
                        :keywords ["مقالات" "تاريخ البحرين" "خرائط تاريخية" "تراث"]}}})

;; Generate article configurations from article index
(def article-meta-configs
  (->> article-index/entries
       (map (fn [entry]
              [(keyword (str "article-" (:route entry)))
               {:en {:title (:en-title entry)
                     :description (:en-description entry)
                     :keywords (:en-keywords entry)}
                :ar {:title (:ar-title entry)
                     :description (:ar-description entry)
                     :keywords (:ar-keywords entry)}}]))
       (into {})))

(defn get-meta-config-for-route
  "Get meta configuration for a given route and language.
   Retrieves configs for static and article pages."
  [route language]
  (or (get-in page-meta-configs [route language])
      (get-in article-meta-configs [route language])
      {}))

(defn set-page-meta!
  "Set all meta tags for a page. Takes a config map and merges with defaults.
  Automatically infers current URL and language.

  Usage: (set-page-meta! {:title 'Custom Title' :description 'Custom description'})
  "
  [config]
  (let [current-url (get-current-url)
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
        (update-name-meta-tag! "keywords" (if (string? keywords) keywords (str/join ", " keywords))))

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

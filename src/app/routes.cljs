(ns app.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [app.events :as events]
            [app.model :as model]
            [app.pages.articles.index :as article-index]
            [app.util.url :as url]))

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
                        :keywords ["مقالات" "تاريخ البحرين" "خرائط تاريخية" "تراث"]}}

})

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

(def url-for (fn [route] (bidi/path-for model/routes route :language @(rf/subscribe [::model/language]))))

(defn- dispatch-route [matched-route]
  (if matched-route
    (let [panel-name (keyword (str (name (:handler matched-route))))
          route-params (:route-params matched-route)
          ;; Parse query parameters for routes that need them
          query-params (url/get-query-params)
          final-params (if query-params
                         (merge route-params query-params)
                         route-params)]
      (rf/dispatch [::events/set-route-params final-params])
      (rf/dispatch [::events/set-active-panel panel-name]))
    (rf/dispatch [::events/set-active-panel :home])))

(def history
  (pushy/pushy dispatch-route (partial bidi/match-route model/routes)))

(defn app-routes []
  (pushy/start! history))

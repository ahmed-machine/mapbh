(ns app.pages.articles.util)

(defn image
  [src alt caption]
  [:figure.image
   {:style {:text-align :center}}
   [:a {:href src} [:img {:alt alt :src src}]]
   [:figcaption caption]])

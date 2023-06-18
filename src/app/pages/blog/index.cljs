(ns app.pages.blog.index)

(defn en []
  [:div.container.about
   [:h1.title "Blog"]
   [:ul
    [:li [:a {:href "/blog/en/wadi"} "Wadi AlBuhair"]]
    ;;[:li [:a {:href "/blog/en/fairey"} "Fairey Surveys"]]
    ]])

(defn ar [] (en))

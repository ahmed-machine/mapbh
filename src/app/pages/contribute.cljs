(ns app.pages.contribute)

(defn en []
  [:div.container.contribute
   [:h1.title "How to contribute"]
   [:p "All contributions are welcome! We're currently working on open sourcing the code and dataset as time allows. The current needs primarily revolve adding as many interesting maps as possible. This involves working on:"]
   [:ul {:style {:margin "2% 12%"}}
    [:li "• Acquistion/Digitization of historic maps."]
    [:li "• Translating old map projections to Web Mercator."]
    [:li "• Fixing alignment issues on existing maps."]
    [:li "• Creating compelling social media content."]
    [:li "• Cleanup of artifacts on older maps."]
    [:li "• Arabic translation of map metadata.\n"]]
   [:p "If this interests you or you have any other suggestions or maps to share, reach out to us by " [:a {:href "mailto:mapbh.org@gmail.com"} "email"] ". All experience levels are welcome whether you're an expert or a complete novice. You will be empowered to make changes and/or supported in your learning. General reminder when making requests that this project is done entirely in my free time and funded with my own money, both of which exist in limited supply. Please excuse any slowness to respond."]
   [:p "In the information age, we're subjected to an increasing barrage of data that tells fewer stories every day. It's my wish for this one-man project to grow into a team that tells those stories of Bahrain's oft-forgotten history currently tucked away in dusty library shelves and dog-earred journals."]
   [:p "Special thanks to Abrar Hasan and Mohamed Toorani for their help in sourcing and cataloging these maps, Majd Al-Shihabi, Maryam Zainal, Ali Karimi, Ahmad Zakaria and Ali Al-Jamri for sharing valuable insights and translations, and everyone else that put up with me talking about maps and street intersections way too much."]])


(defn ar []
  [:div.container.contribute {:dir "rtl" :lang "ar" :style {:font-size "125%"}}
   [:h1.title "كيفية المساهمة في المشروع"]
   [:p "نرحب بجميع المساهمات! نعمل حاليًا على نشر مصدر الموقع ومجموعة البيانات بقدر ما يسمح به الوقت. الاحتياجات الحالية تتمحور حول إضافة أكبر عدد ممكن من الخرائط المهمة، هذا يتضمن العمل التالي:"]
   [:ul {:style {:margin "2% 12%"}}
    [:li "• اقتناء وترقيم خرائط تاريخية"]
    [:li "• ترجمة إسقاطات خرائطية قديمة إلى إسقاط «مركاتور» الشبكي"]
    [:li "• تعديل ومحاذاة الخرائط المتاحة"]
    [:li "• تصميم محتوى لوسائل التواصل الاجتماعي"]
    [:li "• معالجة الخرائط من الآثار المطبعية"]
    [:li "• ترجمة البيانات الوصفية للخرائط للعربية"]]
   [:p "إن كان لديك اقتراحات أو مساهمات أو خرائط قديمة ترغب بمشاركتها، فيمكنك التواصل معنا عبر" [:a {:href "mailto:mapbh.org@gmail.com"} " الإيميل"] ". نرحب بجميع مستويات الخبرة سواءً كنت مبتدئًا أو خبيرًا. هذا المشروع عبارة عن مجهود شخصي بتمويل ذاتي لذا اعتذر مسبقاً على اي تأخير في الرد على استفساراتكم و مقترحاتكم."]
   [:p "في العصر الحديث، زادت البيانات و قلت المعلومات و خاصة الصحيحة منها. أتمنى أن يصبح هذا المجهود الفردي المتواضع مشروعاً جماعياً يحكي حكايات البحرين المنسية على أرفف المكتبات المهملة وبين أوراق المجلات القديمة."]
   [:p "شكرٌ خاص الى أبرار حسن و محمد توراني على مساهمتهما في جمع و فهرسة الخرائط كما اتقدم بالشكر الحار لكل من مجد الشهابي و مريم زينل وعلي الجمري و علي كريمي وأحمد زكريا لما قدموه من رؤى قيّمة وترجمة لإنجاز هذا المشروع والى كل من تحملني اثناء العام الماضي. "]])

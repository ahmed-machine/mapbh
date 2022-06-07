(ns app.pages.wadi
  (:require [reagent.core :as r]
            [app.routes :as routes]
            [app.events :as events]
            [re-frame.core :as rf]))


(defn image
  [src alt caption]
  [:figure.image
   {:style {:text-align :center}}
   [:a {:href src} [:img {:alt alt :src src}]]
   [:figcaption caption]])


(defn en
  []
  [:div.container.about
   [:h1.title "Wadi AlBuhair - a valley awaits a small victory"]
   [:button.button.is-clickable.is-rounded.is-info.is-light
    {:style {:margin-left "4.5rem"}
     :on-click #(rf/dispatch [::events/set-language "ar"])} "اقرأ باللغة العربية"]
   [:p "There is a persistent idea in our society that pits our environment against business interests. It supposes our advancement as a nation can only come at the destruction of our nature. Whether it's the sea to be reclaimed, valleys to be buried, or green belt to be bulldozed, nature has repeatedly lost this fight in Bahrain."]
   [:p "One such case is Wadi AlBuhair. The long-neglected area, a forty-five million year old natural valley, holds immense value as a unique geological site in Bahrain. For decades, it was misused in various forms as a trash dump, drainage for sewage run-off, and even as a short-lived farming project. Despite negligence, it has still managed to provide a site of natural beauty for residents, a safe harbor for rare migratory birds, and an astonishing range of natural biodiversity. With AlLawzi lake mostly reclaimed, Sakhir largely developed for oil infrastructure, and Wadi AlSail’s real estate development, Wadi AlBuhair’s geologic value as one of the last remaining valleys still standing is incalculable with its dolomitic limestone, chalk, marls, shale, and fossils providing a unique glimpse into Bahrain’s natural geological qualities. This is the story of a resilient valley that "
    [:a {:target "_blank" :href "https://albiladpress.com/news/2022/4953/bahrain/756704.html"} "awaits a small victory"] " with its " [:a {:target "_blank" :href "http://www.alwasatnews.com/news/415700.html"} "many supporters."]]
   [:span.level
    [image "/img/buhair/geology1.jpg"]
    [image "/img/buhair/geology2.jpg"]]
   [:span.level
    [image "/img/buhair/2.jpg"]
    [image "/img/buhair/12.jpg"]]
   [:p "In December 2020, " [:a {:target "_blank" :href "https://alwatannews.net/Bahrain/article/915655/%D8%A8%D9%84%D8%AF%D9%8A-%D8%A7%D9%84%D8%AC%D9%86%D9%88%D8%A8%D9%8A%D8%A9-%D8%A5%D9%86%D8%B4%D8%A7%D8%A1-%D9%85%D9%86%D8%AA%D8%B2%D9%87-%D9%81%D9%8A-%D9%88%D8%A7%D8%AF%D9%8A-%D8%A7%D9%84%D8%A8%D8%AD%D9%8A%D8%B1"} "municipal plans were approved"] " to rehabilitate Wadi AlBuhair into a protected natural public park, finally signaling an end to the neglect of this area. Unfortunately, this soon turned out to not be the case. In March 2021, " [:a {:target "_blank" :href "https://www.gdnonline.com/Details/940595/Buhair-plan-withdrawn"} "the municipal plans were withdrawn"] " after rehabilitation was deemed “too expensive” and vetoed. A new plan was later approved for commercial space and housing projects. Construction has already begun and has quickly enveloped large portions of the valley."]
   [image "/img/buhair/overview-flipped.png" "Satellite imagery of Wadi AlBuhair. Left - June 2010, Middle - October 2021, Right - March 2022" "Satellite imagery of Wadi AlBuhair. Left - June 2010, Middle - October 2021, Right - March 2022"]
   [:p "Ecological destruction cannot be quantified and Wadi AlBuhair cannot be later substituted with a green manicured park. This site plays a role in maintaining the health of our broader ecosystem, and without it there could be far-reaching unintended consequences cascading across the greater ecosystem. As a flood-prone valley where seasonal rainwater naturally gathers, it serves a vital role as a habitat for rare migrating birds and natural fauna and flora. Disruptions to these ecosystems often reveal serious health and quality of life costs on residents over the ensuing decades, as well as high infrastructure costs. Recent floods in the housing projects near AlLawzi provide a small glimpse that nature can fight back. Beyond ecological concerns, "
    [:a {:target "_blank" :href "https://alwatannews.net/article/130636/%D9%87%D9%84-%D9%88%D8%A7%D8%AF%D9%8A-%D8%A7%D9%84%D8%A8%D8%AD%D9%8A%D8%B1-%D9%85%D8%AD%D9%85%D9%8A%D8%A9-%D8%B7%D8%A8%D9%8A%D8%B9%D9%8A%D8%A9!"} "prior environmental studies"] " have also shown additional " [:a {:target "_blank" :href "http://www.alwasatnews.com/news/547170.html"} "safety risks related to methane leaks"] " due to prior misuse of the valley as a trash dump. There lies a cost in not preserving nature, too."]
   [:p "In light of the COP26 goals set by Bahrain, we believe not only is Wadi AlBuhair a necessary part of the inner island ecology, but it is also a case study for a natural preserve in the middle of the city: a public park with a natural landscape. Wadi AlBuhair serves a key function as a breathing space being a natural landscape in the heart of an urban environment. On any given weekend one can see residents use it for walking, hiking, football, and cycling."]
   [:span.level
    [image "/img/buhair/4.jpg" "" ""]
    [image "/img/buhair/6.jpg" "" ""]]
   [:p "Residents require free access to natural breathing space just as they require shelter. This is plain to see in the sheer numbers of residents that crowd the very few natural outlets left publicly available along the coastline, green belt, and desert. Yet where nature remains, walls and guards restrict access and extract exorbitant fees. It doesn't have to be this way. The right of free public access to all natural beauty and sea is guaranteed across the globe from the Bahamas to Belarus, extending even as far back as the Byzantines, where resorts and private entities cannot restrict access to nature. All of Bahrain’s nature belongs in the public domain for the health of its public, regardless of their background or financial capabilities."]
   [:p "The rehabilitation of Wadi AlBuhair into a natural protected park continues to be a very popular proposal across the community. We stress this is not a matter of spending or investment, but a matter of policy and protection. We call on relevant authorities to halt the current construction projects in Wadi AlBuhair and revisit the plan to protect this area as natural space for future generations."]
   [:p "We may not be able to turn back the clock, but we always have a choice on how to move forward. After the natural ecosystem is lost, or changed beyond recognition, nothing can be done to restore it. Wadi Al Buhair is a place worth saving, if not for today then for future generations."]
   [:p " — concerned members of the community"]
   [:p [:a {:target "_blank" :href "https://www.instagram.com/explore/tags/savewadialbuhair/"} [:i "#SaveWadiAlBuhair"]]]
   [image "/img/buhair/10.jpg" "" ""]
   [image "/img/buhair/areas-to-save.jpg" "Areas that can still be saved" "Areas that can still be saved"]
   [:span.level
    [image "/img/buhair/11.jpg" "" ""]
    [image "/img/buhair/8.jpg" "" ""]]
   [:span.level
    [image "/img/buhair/9.jpg" "" ""]
    [image "/img/buhair/7.jpg" "" ""]]])

(defn ar
  []
  [:div.container.about  {:dir "rtl" :lang "ar" :style {:font-size "115%"}}
   [:h1.title "وادي البحير — ينتظر انتصار رغم كثرة أنصاره"]
   [:button.button.is-clickable.is-rounded.is-info.is-light
    {:style {:margin-right "4.5rem"}
     :on-click #(rf/dispatch [::events/set-language "en"])} "Read in English"]
   [:p "هنالك عقلية متفشية في البحرين تُظهر البيئة كأنها عدوة وعائق أمام المصالح التجارية. هذه العقلية تعتقد أن تقدم المجتمع لا يمكن أن يأتي إلا على حساب الطبيعة والحياة الفطرية سواءً كان ذلك عبر ردم البحر أو دفن الوديان أو اقتلاع ما تبقى من الحزام الأخضر. وفي نهاية المطاف تكون الطبيعة البحرينية الطرف الخاسر مراراً وتكراراً في هذه المعركة."]
   [:p "إحدى هذه الأمثلة هو وادي البحير الذي يمتاز بتفرده الجيولوجي كونه وادي طبيعي لا يقل عمره عن ٤٥ مليون سنة. ومع ذلك، لطالما عانى هذا الوادي على مر العقود من الإهمال  وسوء الاستخدام. فقد استخدم مكباً للنفايات ومصباً لمياه المجاري وحتى كمزرعة لفترة قصيرة جداً. ورغم هذا الإهمال الكبير لإحدى أهم المواقع الجيولوجية في بحريننا الحبيبة، إلا أن هذا الوادي الصامد لا يزال يحتفظ بجماله الطبيعي ودوره كملجأ آمن للطيور المهاجرة النادرة وتجمع مذهل للتنوع البيولوجي الطبيعي. قلوبنا جميعاً لا زالت تتحسر على ما خسرناه من تراث طبيعي بعد ردم بحيرة اللوزي، وتشييد المشاريع النفطية في بر الصخير واستخدام وادي السيل كذلك لإقامة عدد من المشاريع العقارية - فلم يتبقى لنا من مواقع جيولوجية طبيعية نوعية سوى وادي البحير.
وادي البحير الذي يعد الوادي الوحيد المتبقي من وديان البحرين لا يزال شامخاً بحجارته الجيرية الدولوميتية والأحافير والمارل وصخوره الطينية وطبقات الطباشير وغيرها التي تقدم لنا نافذة  إلى عالم الجيولوجيا الطبيعية الرائعة في البحرين. هذه قصة وادي البحير الذي "
    [:a {:target "_blank" :href "https://albiladpress.com/news/2022/4953/bahrain/756704.html"} "ينتظر ل'ينتصر'"] " رغم " [:a {:target "_blank" :href "http://www.alwasatnews.com/news/415700.html"} "كثرة أنصاره."]]
   [:span.level
    [image "/img/buhair/geology1.jpg"]
    [image "/img/buhair/geology2.jpg"]]
   [:span.level
    [image "/img/buhair/2.jpg"]
    [image "/img/buhair/12.jpg"]]
   [:p "في ديسمبر ٢.٢٠ تمت "
    [:a {:target "_blank" :href "https://alwatannews.net/Bahrain/article/915655/%D8%A8%D9%84%D8%AF%D9%8A-%D8%A7%D9%84%D8%AC%D9%86%D9%88%D8%A8%D9%8A%D8%A9-%D8%A5%D9%86%D8%B4%D8%A7%D8%A1-%D9%85%D9%86%D8%AA%D8%B2%D9%87-%D9%81%D9%8A-%D9%88%D8%A7%D8%AF%D9%8A-%D8%A7%D9%84%D8%A8%D8%AD%D9%8A%D8%B1"} "الموافقة على مقترح إعادة تأهيل وادي البحير إلى محمية طبيعية"]
    " وإنشاء منتزه للعامة من قبل مجلس بلدي المحافظة الجنوبية، والذي اعتبر مؤشراً إيجابياً لإنهاء كابوس البحير المتواصل ولكن سرعان ما تغيرت مشاعر الفرحة والاطمئنان إلى خوف على مستقبل الوادي بعد رفض الجهات المختصة في مارس ٢٠٢١ مقترح المجلس البلدي بحجة التكلفة المرتفعة، وترتب على ذلك "
    [:a {:target "_blank" :href "https://www.gdnonline.com/Details/940595/Buhair-plan-withdrawn"} "سحب المقترح وموافقة الجهات المختصة على مشروع وحدات سكنية وتجارية"]
    " بعد ذلك. بدأت عمليات الردم والحفر بسرعة حتى باتت  تشمل أجزاء كبيرة من الوادي."]
   [image "/img/buhair/overview-flipped.png" "صورـ جوية لوادي البحير، اليسار - يونيو ٢٠١٠، النص: اكتوبر ٢٠٢١، اليمين: مارس ٢٠٢٢" "صور جوية لوادي البحير، اليسار - يونيو ٢٠١٠، النص: اكتوبر ٢٠٢١، اليمين: مارس ٢٠٢٢"]
   [:p "لا يمكن حصر جميع آثار التدمير البيئي  على حياة الإنسان ولا يمكن استبدال وادي البحير لاحقاً بمنتزه حديث ومزيف لا يرتقي بقيمته التراثية والجيولوجية . هذه المنطقة تلعب دوراً مهماً في المحافظة على النظام الإيكولوجي الأشمل وفي حالة فقدانه قد تكون الآثار السلبية بعيدة المدى بحيث لا يمكن إدراكها في الوقت الحالي. وقد تؤدي إلى انهيارات أكبر في النظام البيئي البحريني والعالمي، فعلى سبيل المثال، يعد الوادي موطناً للطيور المهاجرة النادرة والحياة الفطرية النباتية والحيوانية. فكيف سيكون تأثير طمس الوادي عليها؟ "]
   [:p "الإخلال بالأنظمة البيئية يتسبب عادةً في أضرار صحية ومعيشية على الإنسان والفيضانات الأخيرة في إسكان اللوزي عبرة لنا بضرورة عدم الاستخفاف والاعتناء بالخصائص الطبيعية والبيئية ودورها على النظام البيئي ككل. وهذا ما "
    [:a {:target "_blank" :href "https://alwatannews.net/article/130636/%D9%87%D9%84-%D9%88%D8%A7%D8%AF%D9%8A-%D8%A7%D9%84%D8%A8%D8%AD%D9%8A%D8%B1-%D9%85%D8%AD%D9%85%D9%8A%D8%A9-%D8%B7%D8%A8%D9%8A%D8%B9%D9%8A%D8%A9!"} "بينته الدراسات البيئية"]
    " السابقة بمخاطر تتعلق بالسلامة والصحة العامة منها "
    [:a {:target "_blank" :href "http://www.alwasatnews.com/news/547170.html"} "تسرب غاز الميثان"]
    " الموجود في الوادي بسبب استعماله سابقاً كمكب للنفايات. لو كان هناك ثمن للحفاظ على البيئة فهناك ايضاً ثمن باهظ لعدم المحافظة عليها. "]
   [:p "على ضوء الأهداف التي وضعتها مملكة البحرين في COP26، نرى أن الوادي ليس فقط جزء لا يتجزأ من النظام الإيكولوجي الداخلي للجزيرة، بل يمثل حالة واقعية لمحمية طبيعية كائنة في وسط المدينة ومتنفس طبيعي في قلب البيئة الحضرية. في كل يوم يتمتع سكان المنطقة بالتنزه ولعب الكرة وركوب الدراجات وغيرها من الأنشطة في هذا الوادي. "]
   [:span.level
    [image "/img/buhair/4.jpg" "" ""]
    [image "/img/buhair/6.jpg" "" ""]]
   [:p "يحتاج المرء أن يستنشق الهواء الطلق كما يحتاج للمسكن. وهذه الحقيقة واضحة للعيان عندما نرى الأعداد الهائلة من المواطنين والمقيمين الذين يتوجهون للمتنفسات العامة القليلة المتبقية في السواحل والبر والحزام الأخضر. أما ما تبقى من الطبيعة فهو محاط بالجدران العالية والحراس وتذاكر الدخول الباهظة. حق المواطن للأستمتاع بالسواحل والطبيعة من دون اي عوائق هو حق يمتد اليوم من سواحل البهامس الى مزارع بيلروس ويمتد حتى لعصر البيزنطيين، حيث لا يحق لأي فندق أو جهة خاصة احتكار المنافذ الطبيعية. الطبيعة في البحرين يجب أن تكون للملك العام، لكافة الناس بصرف النظر عن قدراتهم المادية وخلفياتهم الاجتماعية."]
   [:p "لا يزال   مقترح إعادة تأهيل وادي البحير وتحويله إلى محمية طبيعية يحظى بشعبية واسعة بين أوساط المجتمع البحريني. نناشد الجهات الرسمية المختصة بإيقاف المشاريع الإنشائية القائمة والمستقبلية في وادي البحير وإعادة النظر بشكل جدي في مشروع إعادة تأهيل الوادي كمتنزه ومحمية طبيعية للأجيال القادمة."]
   [:p "قد لا نستطيع تغيير الماضي لكننا نملك خيار كيفية المضي إلى الأمام. لن يكون هناك جدوى  للندم بعد ضياع بيئتنا وتغيير كل ملامحها، لكن علينا أن نتخذ موقفاً هنا ونقف وقفة لحماية ما تبقى منها للأجيال القادمة."]
   [:p "— افراد  مهتمون في البيئة"]
   [:p
    [:a {:target "_blank" :href "https://www.instagram.com/explore/tags/%D8%A7%D9%86%D9%82%D8%B0%D9%88%D8%A7_%D9%88%D8%A7%D8%AF%D9%8A_%D8%A7%D9%84%D8%A8%D8%AD%D9%8A%D8%B1/"} [:i " #انقذوا ـ وادي ـ البحير"]]]
   [image "/img/buhair/10.jpg" "" ""]
   [image "/img/buhair/areas-to-save.jpg" "مناطق يجب الحفاظ عليها" "مناطق يجب الحفاظ عليها"]
   [:span.level
    [image "/img/buhair/11.jpg" "" ""]
    [image "/img/buhair/8.jpg" "" ""]]
   [:span.level
    [image "/img/buhair/9.jpg" "" ""]
    [image "/img/buhair/7.jpg" "" ""]]])

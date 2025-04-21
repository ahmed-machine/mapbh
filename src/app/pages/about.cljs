(ns app.pages.about
  (:require [reagent.core :as r]
            [app.routes :as routes]))

(defn image
  [src alt caption]
  [:figure.image
   {:style { :text-align :center}}
   [:a {:href src} [:img {:alt alt :src src}]]
   [:figcaption caption]])


(defn en []
  [:div.container.about
   [:h1.title "About this project"]
   [:p  "Bahrain has always been an archipelago in a state of flux. In antiquity, residents terraformed this landscape with burial mounds spanning one of the world's largest cemeteries and extensive, centuries-old irrigation canals flowing from once-plentiful freshwater springs as recently as the 1980s. Bahrain's green belt and many villages formed around springs and along canals on the northern, western, and eastern coasts. In the sea, many of Bahrain's deep harbours were dredged up thousands of years ago to allow ships to dock in capital cities now in ruins. Bahrain's many towns transformed the islands' shorelines in pursuit of limestone cliffs to build temples and homes. No part of Bahrain was unloved, even its seabed was actively maintained where there were pearl oyster beds, bountiful coral reefs, and bubbling freshwater springs (چواچب)."]
   [:p "Change is a constant in these islands and has only accelerated since the 1970s with explosive population growth. As irrigation canals flowed dry and springs drained empty, Bahrain's plantations, ancient coral reefs, and seabeds were reclaimed by an all-encompassing urban sprawl. Bahrain's numerous villages would disappear with depopulation, absorption into the urban sprawl, and the bulldozing of the green belt surrounding them for commercial and residental projects. With their lifeblood dry and way of life gone, few villages retain any of their historic agricultural signifiers or trades today."]
   [:p "To accommodate its growth, Bahrain's landmass grew with land reclamation from 620km² in 1965 to 780km² in 2020 (roughly a ~25% increase) which accompanied a near doubling in urbanized land cover. Bahrain's largest cities, Manama and Muharraq, virtually retain none of their original shoreline as the coastline now plays host to many artifical islands of varying shapes and sizes. Yet, as climate change marches on at an increasingly rapid pace, Bahrain is once again on the cusp of drastic change as one of the world's most at-risk nations in danger of losing vast swaths of coastlines to rising sea levels."]
   [image "/img/side-by-side land reclamation '43.png"
    "Satellite image of Bahrain in 2020 on the left, Map of Bahrain in 1943 on the right"
    "Northern Bahrain in 2020 (left) and 1943 (right)."]
   [image "/img/side-by-side land reclamation '37.png"
    "Satellite image of Bahrain in 2020 on the left, Map of Bahrain in 1937 on the right"
    "Shallow water boundaries shown in this 1937 map have been almost entirely reclaimed in 2020."]
   [image "/img/Land reclamation '43.png"
    "Satellite image of Bahrain in 2020 overlayed on a map of Bahrain in 1943"
    "Map of Bahrain in 1943 overlayed atop satellite imagery from 2020."]
   [:p "This project consists of three lanes of work: building visualization tools to explore maps, open sourcing a database of high-resolution maps with their geographic metadata, and processing them into a uniform standard projection. Many older maps were drawn in unique projections that make direct comparisons with modern maps of Bahrain quite difficult. To standardize the maps, we attempt to warp and rectify that data to match the Web Mercator projection (which can result in inaccuracies due to past human error or imprecise transformation). That work also depends on collecting high-resolution maps that are often hard to discover, improperly digitized, or only exists in physical forms at libraries globally. While several archival projects have made these maps more available in recent years, more effort is needed to surface these maps in a freely-available, accessible manner. This project attempts to bridge this gap by making modern and historic maps of Bahrain freely available and providing tools to make them directly comparable by aligning them and providing tools to explore them."]
   [image "/img/Streets of Muharraq.png"
    "Split view of a terrain map of Muharraq in 2020 and a map of the streets of Muharraq in 1931."
    "Muharraq in 2020 (left) versus Muharraq in 1931 (right)"]
   [:p "This project came out of discussions of Bahrain's fading agricultural and maritime cultures. It's become impossible for newer residents and generations to imagine the radically-changed geographic features that shaped the traditions, culture, cuisine, language, and genetics of the people of Bahrain. The marshes, plantations, dunes, canals, and springs of Bahrain now only live on in the memories of a rapidly aging generation and the cusp of being forgotten. It is our hope for this project to become part of a series of tools and resources that visualize and explore Bahrain's history and culture. By knowing the past lives of Bahrain, we can strive to build a more sustainable future in Bahrain's own mold."]

   [:p "— Ahmed AlMutawa"]])


(defn ar []
  [:div.container.about.ar {:dir "rtl" :lang "ar" :style {:font-size "1.4rem"}}
   [:h1.title "نبذة عن المشروع"]
   [:p  "لم تزل البحرين جزرًا في تبديل دائم وتغيير. في سالف الزمان قلّب أهلها الأرض يشيّدون جثىً فجعلوا منها مقابر دلمون من اعظم ما يكون في العالم وفجّروا فيها قنوات ريّ تجري من عيون عذبة لم ينضب ماؤها حتى الثمانينيات. ومن تلك العيون والقنوات (السيبان) أزهر نطاق البحرين الأخضر على سواحلها شمالا وشرقا وغربا. وفي البحر جُرّفت مواني البحرين العميقة من آلاف السنين لترسو السفن في عواصم صارت اليوم اطلالا. وغيّر حضر البحرين تركيبة سواحل جزرها سعيًا وراء أجراف الحجر الجيري ليتخذوا منها بيوتًا ومعابد. فليس في البحرين شبر لم يغمره الحب والاهتمام، حتى قيعان البحار بمحارها ومرجانها وينابيعها النضّاخة العذبة (الچواچب) كانت تحت رعاية دائمة." ]
   [:p "التغيير سنة الحياة على هذه الجزر ولم يزده التفجّر السكانيّ في السبعينيات الا سرعةً. فلمّا جفّت القنوات ونضبت العيون غمر الزحف العمراني الأخضر واليابس في البحرين من المرجان العتيق والمزارع الخصبة وقيعان البحر، وقفرت القرى بعد الهجر السكاني والتمدين وهدْم ما حولها من نطاق البحرين الأخضر لخدمة مشاريع سكنية وتجارية. بجفاف شريان حياتها وتلاشي أسلوب عيشها لم تبقى سوى قرىً معدودة استطاعت الحفاظ على سماتها الزراعية والحرفية العتيقة."]
   [:p "لاستيعاب هذا النمو كبرت مساحة البحرين الأرضية مع ردم البحر من ٦٢٠ كم² في ١٩٦٥ الى  ٧٨٠ كم² في ٢٠٢٠ اي بنسبة ٢٥٪، صاحبه تضاعفٌ في الغطاء المدني. ولا تكاد مدن البحرين الكبرى المنامة والمحرق تحتفظ على سواحلها الأصلية لما تحمله السواحل من جزر اصطناعية على شتى الاشكال والاحجام. ونتيجة تسارع التحوّل المناخي وارتفاع مستوى البحر أضحت البحرين مجددًا على ابواب تحوّل جذريّ يهدّد بضياع مساحات واسعة من سواحلها."]
   [image "/img/side-by-side land reclamation '43.png"
    "Satellite image of Bahrain in 2020 on the left, Map of Bahrain in 1943 on the right"
   "شمال البحرين في ١٩٤٣ (يمين) و ٢٠٢٠ (يسار)"]
   [image "/img/side-by-side land reclamation '37.png"
    "Satellite image of Bahrain in 2020 on the left, Map of Bahrain in 1937 on the right"
    "تم ردم المياه الضحلة الموضحة في الخريطة من عام ١٩٣٧ بشكل شبه كلي"]
   [image "/img/Land reclamation '43.png"
    "Satellite image of Bahrain in 2020 overlayed on a map of Bahrain in 1943"
    "خارطة للبحرين من ١٩٤٣ فوق صورة جوية من ٢٠٢٠"]
   [:p "لهذا المشروع ثلاث خصال: بناء ادوات تصوير لتصفّح الخرائط، وعمل قاعدة بيانات، مفتوحة المصدر، لخرائط عالية الجودة مصحوبة ببياناتها الوصفية الجغرافية، وتنسيقها في إسقاط معياري. كانت العديد من الخرائط القديمة ترسم بإسقاطات فريدة ممّا يجعل المقارنة بالخرائط الحديثة أمرا فيه إشكال. في سبيل توحيد نسق الخرائط، نقوم بتحوير وتصحيح تلك البيانات لتنتظم على إسقاط «مركاتور» الشبكي. هذا العمل يعتمد ايضًا على تجميع خرائط عالية الجودة لطالما كانت صعبة المنال، او مغلوطة الرقمنة، او قد لا توجد الا ماديًّا في مكاتب حول العالم. وإن حقّقت عدة مشاريع أرشيفية حديثة نجاحًا محدودًا في توفير تلك الخرائط، لم تزل الحاجة الى جهدٍ اكبر في اظهار تلك الخرائط ووضعها بين يدي الجميع. يهدف هذا المشروع الى سدّ هذا الفراغ بإبراز خرائط البحرين العتيق منها والحديث لتكون سهلة المنال منظومة النسق تمكّن المقارنة المباشرة بينها، والى توفير الأدوات اللازمة لاستكشافها."]
   [image "/img/Streets of Muharraq.png"
    "Split view of a terrain map of Muharraq in 2020 and a map of the streets of Muharraq in 1931."
    "المحرق في ١٩٣١ (يمين) و ٢٠٢٠ (يسار)"]
   [:p "برزت معالم هذا المشروع من نقاشات حول تلاشي ثقافتي البحرين الزراعية والبحرية، فإنّه يستحيل على حديثي العهد في البحرين تخيّل المعالم الجغرافية التي شهدت تغييراتٍ جذرية والتي كوّنت أهل البحرين فشكّلت عاداتهم وتقاليدهم ومطبخهم ولغتهم وجيناتهم. كم من هور ومزرعة وقناة وعيون في البحرين لم يعد له أثر الا في ذاكرة المشايخ فأصبح على مشارف النسيان. أملنا في هذا المشروع أن يكون حلقةً في سلسلة موارد وأدواتٍ تساعد على تصوير واستكشاف تاريخ البحرين وثقافتها، لأن بمعرفة ماضي البحرين يتأتّى لنا صناعة مستقبلٍ له على نهج تاريخه أكثر ثباتًا ودواما."]
   [:p  "— أحمد المطوع"]])

(ns app.pages.articles.posts.open-data
  (:require [app.pages.articles.util :refer [image]]
            [re-frame.core :as rf]))

(defn linked-image
  [src alt caption-text caption-href]
  [:figure.image
   {:style {:text-align :center}}
   [:a {:href src} [:img {:alt alt :src src}]]
   [:figcaption [:a {:href caption-href} caption-text]]])

(defn en []
  [:div.container.about
   {}
   [:h1.title {} "mapping bahrain: the politics of open data"]
   [:div.content
    [:p {:style {:text-align :center}} [:em "decommodification is the point"]]

    [:p
     {}
     "Maps are political, and it follows that any mapping project is political. As we mark five years of mapBH, I wanted to revisit the structural barriers that this project set out to overcome. The motivation behind mapBH was always that knowledge should be free, and information should be accessible. This project never came from a noble technical interest in maps \u2014 everything I know about maps and GIS has been self-taught. It began as a lockdown project, an attempt to record the history of these islands for future generations, in service of my community."]

    [:p
     {}
     "I grew up by the sea. My childhood memories retrace farms that are no longer there, swimming on shorelines that no longer exist, and playing in now-demolished homes. Even my childhood dreams, a farm and a fishing boat, now live on a "
     [:a {:href "https://eros.usgs.gov/earthshots/bahrain"} "shoreline awash in asphalt"]
     ". I was born in the 90s. My parents\u2019 and grandparents\u2019 memories are even further removed; the fresh water springs, fertile fields, and hills that animated the scenes of their childhood live on only in their stories. Every few months, a map or photograph goes viral on Bahrain\u2019s social media tugging at the heartstrings of those burdened by decades of loss: of land, and of memories. These maps have long acted as canvases for the stories that colour the lives and aspirations of our people."]

    [image "/img/posts/open-data/bahrain-farms.jpg" "Bahrain farmland and palm trees" "Cultivations in the North of Bahrain, 1986"]

    [:p
     {}
     "This project isn\u2019t an ode to empty nostalgia. The purpose was never to display black-white maps in art exhibitions or to write tragic sonnets: it\u2019s to organise for a future that resembles this land and its people. The future we strive for is a land of a million palm trees, an endless public shoreline, the preservation of our cultural heritage, and children swimming again in the ancient water springs that defined Dilmun, Tylos, Awal, and Bahrain for centuries. We reject the destruction of this land and sea; we want a Bahrain we can live in. This is just one era, of many, in history: we can define the next one."]

    [:h2 {:style {:font-size "2.25em" :font-weight "600" :margin-top "1.5em" :margin-bottom "0.5em"}} "the terrain"]

    [:p
     {}
     "This project launched in January 2021 with five maps, found largely in the Qatar Digital Library and sourced from the British colonial archives with modest aspirations to add ten more. Maps of Bahrain, both digital and physical, were tightly controlled and inaccessible. There were no official map open data initiatives and private access to (analogue) archives remained difficult. Officials initially "
     [:a {:href "https://www.ft.com/content/d14d3576-7bfa-11db-b1c6-0000779e2340"} "struggled"]
     " with public access to new technology, but later embraced "
     [:a {:href "https://www.slrb.gov.bh/en/news-265"} "digitisation"]
     " with a brief opening up of the map archives in the 2000s (with "
     [:a {:href "https://web.archive.org/web/20100614184321/http://www.slrb.gov.bh:80/Commerce/Category/23-4-hypbird-map-bahrain-between-past-present.aspx"} "on-demand paid access"]
     ") that was quietly shuttered in the 2010s. Private collectors prized and guarded individual sheets of maps, often displayed as art in their homes and offices. Researchers and students had to rely on patrons and networks for access, which sharply curtailed serious investigation into modern history. Often, your best academic source was a peer sending a shaky image of a map captured on a phone from a library abroad. Relevant books, such as Jarman\u2019s "
     [:a {:href "https://www.ebay.com/itm/365931848307"} "Historic Maps of Bahrain"]
     ", were sold second-hand online for upwards of $1000. With village historians, academics, and writers, resource guarding was also instinctive after decades of battle scars vying for access and a minefield of obstacles and pressures. All active participants in the field dutifully recounted their horror stories. Maps were a valuable commodity to be traded and bartered. Without access, most local enthusiasts and students were shut out from conducting rudimentary research. To change the status quo, we must dig up the past, assess existing conditions, and construct new blueprints for the present."]

    [linked-image "/img/posts/open-data/fairey-line-map.png" "Fairey Surveys line map of Bahrain" "State of Bahrain, 1985 \u2014 Fairey Surveys 1:10,000 line map" "https://mapbh.org/en/map-info?group=Bahrain&map-id=1985%20-%20Bahrain%20(10k)"]

    [:h2 {:style {:font-size "2.25em" :font-weight "600" :margin-top "1.5em" :margin-bottom "0.5em"}} "the trek"]

    [:p
     {}
     "In most societies, maps are free to access in archives and libraries serve the purpose of decommodification by providing and structuring access to these materials freely. It\u2019s an abdication of responsibility of both our civic society and national institutions that libraries and national archives still don\u2019t fulfill this role. While the responsibility largely lies with state institutions to provide these resources, it's not uncommon for community and private archives to also fill the gap. For decades, we\u2019ve been able to freely access historic maps and transit data for major cities across the world. I\u2019ve made "
     [:a {:href "https://howfar.nyc"} "maps"]
     " of other cities. Even spy satellite images are "
     [:a {:href "https://www.usgs.gov/centers/eros/science/usgs-eros-archive-declassified-data-declassified-satellite-imagery-1"} "declassified after a few decades"]
     ". There was a widely felt desire in the community to change the existing terrain, but we had to first incentivise and build a new prevailing consensus ourselves:"]

    [:h3 {:style {:font-size "1.35em" :font-weight "600" :margin-top "1.25em" :margin-bottom "0.5em"}} "1. high-quality data, free forever"]

    [:p
     {}
     "This isn\u2019t a business. Every map is scanned in as high of a quality as possible and provided as easily as possible, for free, forever. While historic prints of maps will continue to retain value for collectors, we lower the accessibility barrier by providing the information in them for free. When everyone has access to a digital copy, it separates the utility from the speculative value. Professionals and students can use it for research, and the collectors\u2019 inclination to hoard the physical prints becomes less destructive on our collective history."]

    [:p
     {}
     "Maps of Bahrain are scattered across global archives. Archives in the US, UK, Canada, France, India, and beyond each filled gaps in the catalogue. To find Bahrain, we had to inexplicably look everywhere except Bahrain for " [:i "public"] " access. Most maps in Bahrain prior to the \u201980s were created by foreign surveying companies, worked on by academics and professionals from those countries, who often donated their materials to their local libraries and archives. Bahrain has also long-relied on foreign companies to fill expertise gaps in key national industries who donated their annotated maps back to their home countries\u2019 archives. Some material, such as the reconnaissance missions of the US and USSR, were captured independently and declassified in their countries to public archives or discovered second-hand. Many now-defunct UK private companies from the colonial era donated their archives to the respective national British archives. Do you see the pattern?"]

    [:figure.image
     {:style {:text-align :center}}
     [:a {:href "/img/posts/open-data/1960-lawzi-RAF.jpg"}
      [:img {:alt "RAF aerial photograph of Al Lawzi, Hamad Town, 1960"
             :src "/img/posts/open-data/1960-lawzi-RAF.jpg"
             :style {:max-width "60%" :margin "0 auto"}}]]
     [:figcaption [:a {:href "https://airphotofinder.ncap.org/image/1646340"} "RAF aerial photograph of Al Lawzi, Hamad Town, 1960 \u2014 NCAP"]]]

    [:p
     {}
     "Maps are representations of a location in time. They depict a place, and a place is a set of immutable representations of locations in time. Bahrain in \u201962 is not Bahrain in \u201926. It may bear a resemblance (exercise for the reader: identify any continuity in Muharraq\u2019s shoreline), but cities and places never remain the same over time. If buildings and roads are the bones of a place, people and the natural world they tend to are its blood. You never truly visit the same place twice. Maps, like most things, have a validity date. After they serve their \u201cworking\u201d purpose, it\u2019s best to publicly release them for research. They are far more valuable to our society as subjects of collective study than safeguarded in dusty archives."]

    [:p
     {}
     "For example, US reconnaissance satellite imagery from the \u201960-70s was declassified and released by the US Geological Survey with the specific purpose of "
     [:a {:href "https://www.usgs.gov/centers/eros/science/usgs-eros-archive-declassified-data-declassified-satellite-imagery-3"} "filling gaps in scientific research on land cover change"]
     " and the effects of climate change. The British "
     [:a {:href "https://airphotofinder.ncap.org/map?c=5648721.09:254527.89&z=3.00"} "National Collection of Aerial Photography"]
     " released aerial photographs covering over a hundred countries from the "
     [:a {:href "https://mapbh.org/ar/articles/fairey"} "defunct British private surveying companies"]
     " and Royal Air Force into the public record for historical research and climate change studies. Much of this data is freely available to download, but the majority pertaining to Bahrain still needs to be digitised and made available on-demand for $30/film (USGS) or \u00a333/film (NCAP). National archival efforts like "
     [:a {:href "https://www.qdl.qa/en"} "Qatar Digital Library"]
     ", "
     [:a {:href "https://www.agda.ae"} "Arabian Gulf Digital Archive"]
     ", or a new effort extending Bahrain\u2019s "
     [:a {:href "https://data.gov.bh"} "Open Data portal"]
     " are best situated to make these materials public by signing partnerships with those agencies, commissioning the digitisation of these materials, and publicly releasing this data to the Gulf\u2019s general public for research and cultural preservation."]

    [linked-image "/img/posts/open-data/spy-satellite-bahrain-1.png" "Declassified CORONA satellite image of Bahrain" "CORONA-LANYARD satellite imagery, August 11, 1968" "https://mapbh.org/en/map-info?group=Bahrain&map-id=1968%20-%20Bahrain%20-%20August%20-%20A"]

    [:h3 {:style {:font-size "1.35em" :font-weight "600" :margin-top "1.25em" :margin-bottom "0.5em"}} "2. high-quality tools, free forever"]

    [:p
     {}
     "mapBH provides an open database of all modern maps of Bahrain, with high quality tools such as the "
     [:a {:href "https://mapbh.org/en/catalogue"} "catalogue"]
     " and "
     [:a {:href "https://mapbh.org/en/map"} "interactive map tool"]
     " to compare, export, and analyse them. Today, researchers proactively share their map material to have it added to the database and unlock the free tools available to use on the website. There\u2019s a persistent knowledge and skill gap to digitally transform historic maps and present them on the web which mapBH resolves. Each map on the website has been laboured over by me and members of the community to "
     [:a {:href "https://mapbh.org/en/articles/processing-pipeline"} "scan, stitch, and georeference"]
     " it to a professional standard. We\u2019ve happily taken requests regarding materials that are difficult to work with and provided our expertise in making them available for researchers and students."]

    [:div.columns.is-centered
     [:div.column
      [linked-image "/img/posts/open-data/satellite-modern.png" "Satellite imagery of Bilad al Qadim" "Bilad al Qadim, 2026" "https://mapbh.org/en/map?transparency=0"]]
     [:div.column
      [linked-image "/img/posts/open-data/orthophoto-map.png" "Orthophoto map of Bilad al Qadim" "Bilad al Qadim, 1969" "https://mapbh.org/en/map-info?group=Manama&map-id=1969%20-%20Manama%20%26%20AlJufayr"]]]

    [:h3 {:style {:font-size "1.35em" :font-weight "600" :margin-top "1.25em" :margin-bottom "0.5em"}} "3. open source is a fort"]

    [:p
     {}
     "None of the data or tools in this project have ever been secret. All of the code, all of the data, is freely available. You can self-host this project today (seriously, "
     [:a {:href "https://github.com/ahmed-machine/mapbh"} "do it"]
     "). Thousands of copies of this codebase have been made and the entire map collection has been downloaded hundreds of times. The materials are in the public domain; not a single part of the code or data is proprietary. All of it is legal and intentional. This project would not be as successful had it come with a price tag for users. It would also be far more brittle and prone to takedown if it was reliant on a single person. With any open source project, we must note there are frequent attempts to capitalise off our free labour and resources to advance private careers and financial interests. Goodwill is built over years, but can be demolished in days. Everyone should clearly credit work which would allow others the same access they were given."]

    [:h3 {:style {:font-size "1.35em" :font-weight "600" :margin-top "1.25em" :margin-bottom "0.5em"}} "4. build the institute"]

    [:p
     {}
     "There\u2019s no singular bottleneck for mapBH. The maps are replicated and hosted globally. The community around it has developed legs to "
     [:a {:href "https://mapbh.org/en/articles/wadi"} "advocate for conservation"]
     ", to build their own projects, and to develop a self-sustaining ecosystem. This required frequent engagement via social media, personal outreach, cultivation, and training. To survive, open source projects always need to build a community that will sustain it for years to come beyond the original founders. From the start, this project was always reliant on the free labour of several people who catalogued, scanned, translated, and digitised each of the maps. Where there were gaps in expertise (such as in GIS and navigating archives), friends and other similar projects provided invaluable advice to help push through plateaus. In time, we\u2019ve grown our local expertise considerably as well. Over the years, we\u2019ve shared our knowledge with other nascent projects in the region as well ("
     [:a {:href "mailto:mapbh.org@gmail.com"} "get in touch!"]
     "). This is a small part of a global effort."]

    [:p
     {}
     "Archives have long occupied the front lines in battles for transparency. "
     [:a {:href "https://www.wikipedia.org/"} "Wikipedia"]
     ", "
     [:a {:href "https://archive.org/"} "Internet Archive"]
     ", "
     [:a {:href "https://en.wikipedia.org/wiki/Sci-Hub"} "sci-hub"]
     ", "
     [:a {:href "https://palopenmaps.org/en"} "Palestine Open Maps"]
     ", and other free open data initiatives have long served to neutralise the sharpest political attacks to guarantee the right to knowledge and transparency. Free open source software is the backbone of all modern digital infrastructure and, at risk of giving away my \u201cprofessional\u201d hand, "
     [:a {:href "https://www.youtube.com/watch?v=-6BsiVyC1kM"} "code " [:em "is"] " data"]
     ". We owe an irreparable debt to those that steward the world\u2019s free access to knowledge today or "
     [:a {:href "https://en.wikipedia.org/wiki/Aaron_Swartz"} "fought to keep it free ahead of their time"]
     ". Our modern world runs on institutions and foundations built for shared collective benefit and free access to knowledge. Bahrain is no exception."]

    [:p
     {}
     "People will always try to capitalise on gaps in access in pursuit of profit-making enterprises, whether through exorbitantly priced courses or towards private career interests, but we wanted to demonstrate an alternative model in Bahrain that remains true to our starting principles: knowledge should be free, and information should be accessible."]

    [:h2 {:style {:font-size "2.25em" :font-weight "600" :margin-top "1.5em" :margin-bottom "0.5em"}} "the summit"]

    [:p
     {}
     "As we look back on five years of mapBH, we\u2019re glad to see a changed landscape. The site has hundreds of daily users; traffic spikes faithfully around the University of Bahrain\u2019s midterms and finals schedules; and these maps have been frequently cited in books, academic papers, podcasts, and comics. The maps have been displayed in art exhibitions, from official cultural events to peoples\u2019 living rooms. The infrastructure runs on a modest server (7BD/mo) and we make periodic releases of new maps working off the catalogue as capacity allows. We receive regular new contributions of source map material from users."]

    [:p
     {}
     "The map catalogue has grown from 5 maps to 140 combined map sets. It now ranges from "
     [:a {:href "https://mapbh.org/en/map-info?group=Bahrain&map-id=1875%20-%20Bahrain%20Harbour%20v2"} "British maps from the 19th century"]
     " to "
     [:a {:href "https://mapbh.org/en/map-info?group=Bahrain&map-id=1967%20-%20Bahrain"} "declassified US spy satellite images from the 1960s"]
     " to "
     [:a {:href "https://mapbh.org/en/map-info?group=Manama&map-id=1977%20-%20USSR%20-%20Manama%2FMuharraq"} "Soviet Union military maps from the 1970s"]
     ". We\u2019ve catalogued and stitched together "
     [:a {:href "https://mapbh.org/en/map-info?group=Bahrain&map-id=1985%20-%20Bahrain%20(10k)"} "30-sheet map sets"]
     ", and reconstructed "
     [:a {:href "https://mapbh.org/en/map-info?group=Bahrain&map-id=1875%20-%20Rough%20Sketch%20Survey%20of%20Bahreyn%20Islands"} "map scans from Indian governmental archives"]
     ". The catalogue includes "
     [:a {:href "https://mapbh.org/en/map-info?group=Gulf&map-id=1154%20-%20Idrisi"} "al-Idrisi\u2019s world map from the 12th century"]
     " and "
     [:a {:href "https://mapbh.org/en/map-info?group=Bahrain&map-id=1991%20-%20Life%20in%20Bahrain"} "1990s cartoon maps of Bahrain"]
     ". Every part of Bahrain is faithfully mapped across eras."]

    [:div.columns.is-centered
     [:div.column
      [linked-image "/img/posts/open-data/soviet-map-manama.png" "Soviet military map of Manama" "Soviet military map of Manama and Muharraq, 1977" "https://mapbh.org/en/map-info?group=Manama&map-id=1977%20-%20USSR%20-%20Manama%2FMuharraq"]]
     [:div.column
      [linked-image "/img/posts/open-data/satellite-bahrain-bw.png" "KH-9 Hexagon satellite image of Bahrain" "US reconaissance KH-9 Hexagon satellite imagery, May 23, 1982" "https://mapbh.org/en/map-info?group=Bahrain&map-id=1982%20-%20Bahrain%20-%20May"]]]

    [:p
     {}
     "While mapBH has never been a solo project, one of its weaknesses remains that it\u2019s reliant and bottlenecked on my personal capacity to grow the catalogue in my free time. I continue to extend an invite to "
     [:a {:href "https://github.com/ahmed-machine/mapbh"} "open source contributors"]
     " and GIS experts to "
     [:a {:href "https://mapbh.org/en/contribute"} "get involved"]
     ". If you live abroad, you might have access to materials that can help fill in "
     [:a {:href "https://mapbh.org/en/catalogue"} "our catalogue"]
     " so check your local library or national archives for maps or aerial images of Bahrain and "
     [:a {:href "mailto:mapbh.org@gmail.com"} "get in touch"]
     ". Beyond technical expertise, this project is kept alive by dozens and hundreds of users who regularly and freely contribute material, feedback, research, and social media content."]

    [:p
     {}
     "Finally, maps of Bahrain aren\u2019t worth a " [:em "fils"] " today. Even initial hold-outs now proactively share their materials with the project. With every addition to the catalogue, it becomes less rational to hoard. You may keep a single sheet private, but if the other 29 are freely available and comparable maps exist from the same year, how valuable is the information on it really? Scarcity may be the name of the game for luxury collectibles and high-end art, but knowledge, like housing and food, shouldn\u2019t be subject to the same market forces."]

    [:h2 {:style {:font-size "2.25em" :font-weight "600" :margin-top "1.5em" :margin-bottom "0.5em"}} "descent"]

    [:p
     {}
     "A few months ago, I was in conversation with a recent college graduate I\u2019ve known since childhood. As we talked through career changes, I briefly mentioned a mapBH project I had recently completed. Unknowingly, she had used the site extensively in the past assuming it was an established institutional resource. Its existence had coincided with the entire duration of her undergraduate studies and early professional career \u2013 she doesn\u2019t know a time before the project. Unlike previous cohorts of students and enthusiasts, today\u2019s graduating cohorts don\u2019t have battle scars from trying to gain access to map materials. Students of all ages should regularly ask for help, and it felt natural to students that this repository of data on Bahrain was already easily and freely available online so they could focus on the hard part: understanding the historic and cultural developments at hand."]

    [:p
     {}
     "That was the point. It's normal for this information to be available. Our culture and history aren\u2019t commodities for speculation nor should information be used as a bartering chip. It belongs to everyone interested in cultivating and developing these small islands we call home, with the click of a button."]

    [:p
     {}
     "Over the course of the past five years, I\u2019ve met hundreds of Bahrainis who each shared phases of their lives portrayed in each map: constructing today\u2019s landmarks, working in those farms, fishing on that coastline, and what roads they walked every day over the course of their lives. Even my father\u2019s stories came alive when I saw the small hill where he\u2019d lay after working in the field with his grandfather, surrounded by greenery. With almost every natural feature and landmark destroyed, these memories have long felt disembodied and increasingly a figment of our collective imagination. On these maps, they\u2019ve become real once again."]

    [linked-image "/img/posts/open-data/bahrain-1956.png" "Topographic map of Bahrain Island" "Bahrain Island, 1956" "https://mapbh.org/en/map-info?group=Bahrain&map-id=1956%20-%20Bahrain"]

    [:p
     {}
     "As for the next five years, there still is a lot of work left to do. Whether it\u2019s completing the existing catalogue or opening up new areas of knowledge beyond maps for access, we hope you\u2019ll join us in our efforts."]

    [:p
     {}
     [:em "P.S: I created a new section of the site called "
      [:a {:href "https://people.mapbh.org"} "people\u2019s mapBH"]
      " where anyone can submit place definitions or share their personal stories. Feel free to add missing features and landmarks. Using these maps, many people have been able to locate various historical references and depict their family stories and they deserve a place to live beyond my DMs."]]]])

(defn ar [] [en])

(defn article
  []
  (let [language* (rf/subscribe [:app.model/language])]
    (fn []
      (let [language @language*]
        (if (= language :ar) [ar] [en])))))

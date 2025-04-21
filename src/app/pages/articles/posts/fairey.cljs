(ns app.pages.articles.posts.fairey
  (:require [reagent.core :as r]
            [app.routes :as routes]
            [app.events :as events]
            [app.pages.articles.util :refer [image]]
            [re-frame.core :as rf]))

(defn en []
  [:div.container.about
   {}
   [:h1.title {} "Fairey Surveys:"]
   [:h4.title {} "History of modern map-making in Bahrain"]
   [:p
    {}
    "Manama is a city of contrasts. As one walks through its streets, you can easily believe its development to be a matter of inevitability.. Emerging from one alley, thorough-way right back into another alley, alley-turns tuck-in between bakery ovens and modest tailoring shops, effortlessly guiding you towards a seafront that’s no longer there. A few times a year, these sleepy alleys fill to the brim in a crescendo of mourners and rejoicers of the many ethnicities and beliefs that make their home here. The annual marches lead you down well-trodden loops along curved streets like a metronome. The city gently breathes in and out this constant flow of pedestrians as if its every step was carefully and painstakingly constructed to accommodate life in its quietest nights and its loudest days. This is the natural ebb and flow of the once port-city now engulfed in concrete. Manama is a sleepy city, built-up over centuries, to accommodate many lifestyles as they bustled to and from the port and from the rest of the island on foot or donkey. As Manama gently carries you through its arteries, it’s hard not to hear the clanking of its roads and alleys slamming against the monstrous motorway today. Manama's old city is bounded by honking cars; these winding alleys and narrow streets were only the most natural way for it to develop organically for centuries."]
   [:iframe.video {:src "https://www.youtube-nocookie.com/embed/kcEjewNdHyo?si=_uk_7nYsGVJo8wpL" :title "YouTube video player" :frameborder "0" :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share; fullscreen" :referrerpolicy "strict-origin-when-cross-origin" :allowfullscreen true}]
   [:p
    {}
    "Today, a ten minute walk in any direction from the city centre provides evidence of the transformation from an organic web of collapsing histories into regimented grids of homes, business and  neatly ordered zoning categories. While it might take a moment for a wandering guest to notice this sudden change in scenery from the street-level, from space old and new Manama are vividly distinguishable without a single label or place-mark. The old meandering alleys and roads, densely built around the different historic loci of power and trade, clash dramatically against a wall of ordered intersections and uniform rows of eight identical houses, each with a car garage, stretching out over many identical blocks. The how’s and why’s of Bahrain’s puzzling development in the post-independence era might seem inexplicable but it is carefully tucked in the margins of the aerial photos taken by the pilots tasked with surveying Bahrain in the mid-seventies. It’s only when you soar high above the city that you begin to glean an idea of how this story could have unfolded."]
   [:p {} "From a pilot’s birds-eye view, travelling on a uniform surveying path along an imagined grid, one can imagine a grid around an ancient city and through the green belt cradling it. Yes, one can even draw a grid of intersections and eight-a-row houses over a green-blue seabed. From five hundred feet above ground, you’re almost forgiven for not noticing the creatures living in the natural divisions of city, meadow, and sea. On such a map, you could design a grid where only a single creature could freely roam with four wheels and a black cough. There are people today who will proudly proclaim that this, too, was the only manner in which Bahrain could’ve possibly developed. Yet, fifty years ago this trajectory was still only the subject of debate and conjecture."]
   [:span.level {:style {:display :block}}
    [image "/img/posts/fairey/02 77  148 R7.jpg" "1977, West Manama, greenbelt against the recently buried sea" "1977, West Manama, recently buried sea against the greenbelt"]
    [image "/img/posts/fairey/03 77  050 R5.jpg" "1977, East Manama, recently buried sea from Ras Rumman to Adliya" "1977, East Manama, recently buried sea from Ras Rumman to Adliya"]]
   [:p
    {}
    "In the 1970s, a newly-independent Bahrain faced a dilemma that many colonies like Malaysia, India, and Jamaica faced before. After centuries of British rule where a large foreign administrative bureaucracy managed all functions of the state, independent Bahrain now faced the task of state-making with little existing expertise. A deliberate remnant of the ‘post-colonial’ era, colonial subjects were regularly excluded from meaningful administrative positions and any self-organisation was severely curtailed by the colonial rulers to prevent any effective forms of independent governance emerging in the colonies. Thrust into this weak position, many newly-independent colonies chose to off-load the creation of new state apparatuses right back onto a global network of technical advisors and former colonial bureaucrats, taught in British and French universities and trained in colonial offices, that could repackage their decades of colonial administration into lucrative administrative contracts with those same former colonies. The new state apparatus would now come to resemble the old colonial state apparatus: an army of handsomely-paid foreign consultants leading large local teams that are still routinely denied the agency to lead, innovate, and take credit for their work."]
   [:p
    {}
    "Surveying wasn’t any different. With the creation of large national plans to radically transform Bahrain, the new state required a set of detailed surveys to build a new country off of. The colonial state apparatus would be called upon once more to establish a new basis."]
   [:p
    {}
    [image "/img/posts/fairey/Screenshot_2022-08-07_at_10.51.32.png" "" ""]]
   [:p
    {}
    "In November 1976, Fairey Surveys "
    [:a
     {:href "https://www.faireysurveys.co.uk/wp-content/uploads/Fairey-Surveys-Newsletter-16-Nov-1976.pdf"}
     "would announce in a newsletter"]
    " that it signed a contract with the State of Bahrain to produce topographic maps in “1:100,000 [scale] designed as a single sheet; 1:50,000 in two sheets; 1:25,000 in a nine sheet series” and would also include “larger scales selected for detailed planning and engineering purposes” consisting of “1:10,000 in line-map and orthophotomap forms; 1:2,000 and 1:1,000 for the developed areas defined by the State Government”. "]
   [:span.level
    {}
    [image "/img/posts/fairey/map-scale-extracts.png" "" ""]
    [image  "/img/posts/fairey/overview-scale.png" "" ""]]
   [:p
    {}
    "Fairey Surveys began its life in 1923 as the Air Survey Company to carry out an aerial survey of the Irrawaddy Delta for the Forestry Department in Burma. The Company worked as a private concern under contract to the British Indian Government. After close development under the War Office and Royal Airforce, it would become a private entity to “to increase the slender monetary provision on which the Committee relies, and so to hasten the day when the Empire at large may obtain full profit from the ability to carry out air-surveys of its more remote parts with certainty and with all necessary exactitude”. In 1929, the Air Survey Company became a subsidiary of a large British conglomerate called the Fairey Aviation Company which manufactured military aircraft, bombers, boats, and included numerous subsidiaries. This reorganisation would herald in Fairey Survey's first venture into the Middle East: "
    [:a
     {:href "https://sci-hub.se/https://www.jstor.org/stable/1780766"}
     "“with greatly increased capital resources, its activities spread to include surveys in Africa, Egypt and other parts of the Middle East. By 1939 aerial photography had established its value for a variety of purposes other than map-making when a temporary halt was called by the war years. The India Company was reorganised and modernised after 1945 when its activities were resumed, and its name was changed to the Air Survey Company of India in 1948.”"]
    " It would endure once more as Clyde Surveys until it became a subsidiary of Halcrow before being effectively nationalised by the UK government in 1993. The tight-knit former personnel of the company would continue to host socials and maintain an archive of Fairey Surveys’ history with many stories and documents shared at "
    [:a {:href "https://www.faireysurveys.co.uk"} "faireysurveys.co.uk"]
    ". Most of the "
    [:a
     {:href "https://ncap.org.uk/search?bbox=49.88410710156191%2C25.56081652821732%2C51.202466476562726%2C26.37566832649041"}
     "company’s imagery copyrights was transferred to the National Collection of Aerial Photography (NCAP)"]
    "."]
   [:p
    {}
    [image "/img/posts/fairey/NCAP-000-000-303-444.jpg" "(Malkiya, 1991)" "(Malkiya, 1991)"]]
   [:p
    {}
    "State bureaucracy is a revolving door between military, industry, and administration, but at its core is a set of highly-trained bureaucrats and technical specialists wresting control on a domain of knowledge that strongly excluded colonial subjects from participation due to the adverse relationship between coloniser and colony. This close-knit group of experts would be instrumental in mapping and surveying several nascent states including Qatar, UAE, Egypt, Jamaica, Zambia, and many others. A parallel group of foreign consultants would also be instrumental in developing new masterplans for capital cities and urban planning, such as one Mr. A.M. Munro that’s credited with developing the Manama Master Plan. Fairey Surveys would now come to play a large role in Bahrain’s urban history as these maps came to be used as the canvas for a country-wide blueprint, made up of many smaller master plans. "]
   [:p
    {}
    "While it’s unclear when contact between the Bahraini state and Fairey Surveys began, it’s safe to assume that, much like other British protectorates and colonies, it would’ve been facilitated by the British Political Agency in Bahrain as the state transitioned into an independent state. One can extrapolate off the process that led to the hiring of a town planner in 1968:"]
   [:p.quote
    [:blockquote.content
     {:style {:margin "1rem" :padding "1rem" :background-color "#efefef"}}
     " There was no government agency for Master Planning or Structural Planning. There was no qualified Town Planner in the Government or indeed in the country. The Government requested the services of a qualified Town Planner from the British Government and the British Ministry of Overseas Development sent Mr. A.M. Munro as Physical Planning Advisor to the Government of Bahrain in 1968. Mr. Munro was a qualified and well experienced Town planner. Before coming to Bahrain he worked in Malaysia and several other foreign countries as an Advisor of British Ministry of Overseas Development. "]
    [:em {:style {:display :block :text-align :right :margin-right "1rem"}} "(The History of Land Use and Development in Bahrain, Mohammed Noor Al-Nabi, 2012)"]]
   [:p
    {}
    "Prior to Fairey Survey's 1977 maps, the most recent maps of Bahrain had been issued by the British Ministry of Defence in 1973 and various British governmental apparatus. Nearly all 20th century modern maps of pre-independence Bahrain were created by the British Ministry of Defence, the United States Military, and a host of private corporations like Standard Oil (American) and the surveying companies contracted by the British. Yet, it " [:i {} "should"] " serve as an unfortunate surprise that the transition into post-independence would continue to involve many of the same British figures, norms, and bodies that existed pre-independence."]
   [:p {} [image "/img/posts/fairey/tw756pv9245_0001.jpg" "Fairey Surveys, Zambia" "Fairey Surveys, Zambia"]]
   [:p {}]
   [:p
    {}
    [image "/img/posts/fairey/174283_sv.jpg" "Fairey Surveys, Qatar" "Fairey Surveys, Qatar"]]
   [:p
    {}
    "As expressed in the original founding reports from the Air Survey company, the separation between the British Empire’s government and its private companies such as Fairey Surveys allows the possibility “to hasten the day when the Empire at large may obtain full profit from the ability to carry out air-surveys of its more remote parts”. This relationship isn’t accidental as the post-colonial world continued to maintain this set of relations under new titles with money flowing back to the imperial core uninterrupted. The advancement of various scientific fields ranging from computing to surveying often originated inside the colonial war machinery as a means of exerting control and power. The advancements made in modern mapping cyclically facilitated funnelling the wealth extracted from the colonies right back into the war machine that worked to subdue the colony’s resistance to the extraction of this wealth (e.g. oil strikes in Bahrain). Inclusion of colonial subjects in these sensitive departments, given the oft-adverse nature of British interests to its colonies, necessitated a security risk. The privatisation of these departments and offices into private corporations coincided with the rise of independence movements; concentrating knowledge and expertise outside the public sphere just as the colonies began to gain access to it. Decades later, foreign consultants, particularly from France and Britain, continue to dominate various governmental fields and command large contracts in former colonial states to this day with little attention given to developing a domestic sector in those fields. As nascent states developed local expertise via scholarships in leading global institutes and universities, there have also been many stumbling blocks, both internal and external, to the emergence of a leading domestic industry. In many ways, the practical knowledge and experience is still closely guarded inside multinational private corporations in the imperial core to lock out competition arising from developing states."]
   [:p
    {}
    "Maps are rarely ever created from scratch nor do they ever exist in a vacuum. Britain’s initial maps of Bahrain were drawn atop Dutch maps which had relied on Sanson’s French maps that were based off the Portugese maps before them, which had referenced Al-Idrissi’s 13th century maps of the region and built off advances in cartography from the Islamic world. Each empire that has come and gone has imprinted on these maps its ambitions and desires. Today’s maps are no different; they continue as living memory of the history of these islands. All maps are necessarily political; the names of places and landmarks, inclusion and exclusion of details, censorship of areas, and delineations are ever-contested. The Soviets produced military maps of Bahrain via spies in the country that peered into the military installations on the islands (completely unmarked in contemporary British maps). Infamously, "
    [:a
     {:href "http://mahmood.tv/2006/08/07/google-earth-blocked-in-bahrain/"}
     "the release of Google Earth led to a local ban in 2006"]
    " as people hungrily consumed this newly-democratised platform. Simultaneously, a brief opening up of the ability to buy historic maps via the Survey and Land Registration Bureau (SLRB) online would be shuttered around the same time. Throughout Bahrain’s history, the contested politics of maps and space has shifted from colonial offices into interagency and international bodies and then into an open arena where regular citizens now contest the naming of islands, hills, gulfs, and straits and its ownership. Even multinational companies, ever sensitive to a loss in profits, now privately maintain multiple sets of annotations to satisfy the ambitions and sensitivities of multiple groups of people and governments. Videos and images of maps go viral in Bahrain’s social media every few months as citizens contend with issues such as land reclamation, ecological destruction, and the bulldozing of the green belt. It should serve as little surprise that our maps remain heavily contested sites for public debate and ideological battles today. "]
   [:p
    {}
    "While little insight into the local decision-making process exists, many often cite colonial underdevelopment across centuries, instability, corruption, and continued aggression/pressure radiating from the imperial core towards its colonies as primary obstacles in nationalising these sectors and developing homegrown talent. Given this background, the responsibility remains solely with the local government to counter this reality with a set of strong national priorities and policies set forward to secure its domestic interests. Heavy reliance on foreign consultancies is a security risk, undermines development, and serves as a primary contributor to economic malaise. Continued failure to develop a homegrown self-sufficient industry in this particular arena is itself negligent policy and not an inevitability."]
   [:h1 {} [:strong "The Commissioned Maps"]]
   [:p
    {}
    "Given the heavy reliance on British map-makers and a non-existent domestic sector, the first edition of this Fairey Surveys series would come to constitute the concrete base of nearly all surveys used by Bahrain’s government and ministries into the modern era and to this day. The commission from 1977 would eventually come to include one large national map in 1:100,000 scale, three colour sheets in 1:50,000 scale (titled: North, South, Hawar Islands), seven colour sheets in 1:25,000 scale with an additional two sheets including Hawar Islands, and twenty-eight black-white line-map sheets covering the main Awal islands in 1:10,000 scale with an additional twelve sheets covering Hawar. Notably, the delayed timing of the release of the Hawar Islands sheets in 1993 coincides with the start of "
    [:a
     {:href "https://www.icj-cij.org/case/87/press-releases"}
     "the timeline for the Bahrain-Qatar case (1991-2001) in the International Court of Justice (ICJ)"]
    " which determined Bahraini sovereignty on the islands. Bahrain’s Survey Directorate would be established in 1978 to continue maintaining these surveys as part of the Ministry of Housing."]
   [:p
    {}
    "Each of the different scales commissioned served different purposes for the urban planners. One can very easily \"miss the forest for the trees\" looking at a small-enough scale. The green belt and changing shorelines is most easily traced in the less-detailed 1:50,000 series which displays farmland as blocks while the development of urban sprawl is most easily captured in the more-detailed 1:25,000 scale as minor roads and buildings could be legibly depicted. With the 1:10,000 scale, we’re able to track changes on a street level such as the exact borders of a subdivided farm and the expansion of an oil refinery. With 1:2,000/1,000 scale, you’re able to track individual plot-level changes which could resolve disputes over the exact delineation of a plot between brothers, a plot-line that many Khaleeji dramas would suggest that brothers are eager to press."]
   [:p
    {}
    [image "/img/posts/fairey/Jidhafs.png"  "1981, National Addressing Project, Jidhafs"  "1981, National Addressing Project, Jidhafs"]]
   [:p
    {}
    "With the rapid pace of development following the seventies, new editions were soon required to depict new plots raised from the seabed with land reclamation and infilling. Field revisions would be required as sprawling urban development would remake the landscape and highways would  now cut through green fields. Even sheet names would change as the ‘Az Zallaq’ sheet would be renamed to ‘Hamad Town’ in subsequent editions owing to the changing demographics and changes in the urban landscape following the creation of the large new town over what was once burial mounds. Each map sheet would end up being updated independently of the set as necessary. While economic and pragmatic, this meant that specific sheets depicting northern Bahrain, which underwent rapid change, would be updated more frequently than sheets depicting southern Bahrain. Between 1977 and 2000, five editions of the 1:25,000 Manama sheet would be produced. In the same period, there would only be two editions of “Ras Al Barr” (published in 1977, 2000 respectively). This serves as a crude measure of change in the respective areas but also presents a challenge today in depicting an accurate snapshot of the entirety of Bahrain in any given year as the available information consists of a patchwork of sheets and missing editions depicting different years. The detailed 1:10,000 scale series would be updated as a complete set in 1985 for a second edition and there likely exists a third edition produced in the mid-nineties, but its existence is complete speculation as little information has been made publicly available. Unfortunately, this is the nature of research into the development of Bahrain in the post-independence era: one characterised by global far-reaching map-hunts and an over-reliance on a private network of scholars sharing access to prized reference material that’s publicly unavailable."]
   [:p
    {}
    "New editions of the Fairey Surveys set continued to be produced into the years 2006/7. While satellite imagery began to be captured in the early 80s, it wasn’t until 2003 that high-resolution imagery became widely and regularly available to the public. Yet, maps represent more than just imagery, they represent an annotated collection of details describing a region and provide tools on how to understand that physical space. One can understand this simply by looking at a satellite image of their hometown without any labels and attempting to find their home or street from above. Digital maps such as Google Earth/Maps provide more than satellite imagery, they provide tools for the general public to crowdsource information about their region."]
   [:p
    {}
    "Developments in distributed crowd-sourcing on the internet wrested control away from professional and governmental bodies that previously held a stranglehold on the creation and distribution of this information. These developments coincided with an era of modernisation in the ministries where the original Fairey Surveys were translated into MicroStation CAD files for use in Bahrain’s governmental bodies which could be updated plot-by-plot more regularly and asynchronously by different ministry bodies. While there likely remains no more editions of these Fairey Surveys series produced today, a cursory look at the topographic CAD maps used by the ministry shows familiar traces of the initial surveys from 1977 still alive in-between every alley, plot, and tree digitised. This modernisation period also represents the end of our insight into modern map-making in the Bahraini state as data is not publicly available past this point in 2007."]
   [:p
    {}
    "When mapBH started as a project 3 years ago, we were primarily focused on geo-referencing colonial maps being surfaced from the British national archives via the "
    [:a {:href "https://www.qdl.qa/en"} "Qatar Digital Library"]
    " and the "
    [:a {:href "https://www.agda.ae/"} "Arabian Gulf Digital Archives"]
    ". This was an unintentional choice owing largely to the lack of public access to maps from the post-independence era. A chance discovery of the 1:25,000 series from 1977 would kick off an obsession with uncovering as much information about these maps as possible. Over the past three years and with the help of many friends across the globe, we were able to describe the breadth of maps and consequent editions produced, locate them in libraries all over the world, digitise them, and geo-reference them. While there remains some small gaps in this collection we still hope to crowdsource, we’re pleased to announce the release of this treasure trove of information from 1977 to 2007. This includes the complete 1:50,000 series and all subsequent six editions, the 1:25,000 series spanning five to six editions (minor gaps), and the full second edition of the 1:10,000 scale map produced in 1985. We hope this set serves as a resource to the study of Bahrain’s development in the post-independence era and as an archive of the many forgotten boundaries, buried shorelines, dry freshwater springs, collapsed irrigation channels, demolished homes, changed area names, and our bulldozed green belt."]
   [:p
    {}
    "We welcome everyone to explore this collection on "
    [:a {:href "https://mapbh.org/en/map"} "mapBH"]
    " or to clone the entire site/database "
    [:a {:href "https://github.com/AHAAAAAAA/mapbh"} "from Github"]
    " and host their own copy."]
   [:p
    {} "DISCLAIMER: "
    [:em
     {}
     "We do not work for any official state bodies. Given that there exists little information available to the public, we can only speculate and make informed guesses to extrapolate based on known information."]]
   [:p {:style {:margin :auto :text-align :center}}
    [:h3 [:strong "Missing Maps"]]
    [:table.table {:style {:margin :auto}}
     [:thead
      [:th "Scale"]
      [:th "Sheet"]
      [:th "Edition"]]
     [:tbody
      [:tr
       [:td "1:10,000"]
       [:td "All except sheets (73, 94)"]
       [:td "1 (1977)"]]
      [:tr
       [:td ""]
       [:td "73 and all of Hawar"]
       [:td "2 (1985)"]]
      [:tr
       [:td ""]
       [:td "All"]
       [:td "3 (likely mid-nineties)"]]
      [:tr
       [:td "1:25,000"]
       [:td "Manama"]
       [:td "2 (likely published in early 80s)"]]
      [:tr
       [:td ""]
       [:td ""]
       [:td "3 (likely published in ‘86/’87)"]]
      [:tr
       [:td ""]
       [:td "Zallaq/Madinat Hamad"]
       [:td "2 (likely published in ‘86/’87)"]]
      [:tr
       [:td ""]
       [:td "Riffa"]
       [:td "3 (likely published in early 1990s, 92/93)"]]]]]])

(defn ar [] (en))

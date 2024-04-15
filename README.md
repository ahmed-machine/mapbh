# MapBH ![Deploy](https://github.com/AHAAAAAAA/mapbh/workflows/Deploy/badge.svg)

Minimal frontend with set of tools to explore old maps of Bahrain and interface with warped/rectified maps hosted on our tile server.

### Setup

Requirements: openjdk, node, npm, internet
1. `npm install` to install packages
2. In CIDER (emacs), `M-x cider-jack-in-cljs` and select shadow and :app. Non-CIDER and emacs, `npx shadow-cljs watch app`.


Local dev http://localhost:1212

https://www.mapbh.org


### Stack

Simple clojurescript, shadow-cljs static site stack. Bulma for CSS, npm packages, and leaflet.js for maps. Hosted on a Linode server. Served by an nginx reverse proxy. Cloudflare nameserver for caching/DDoS protection. Tileserver-GL for our hosted maps.

Map processing steps primarily with GDAL tools.

### Deployment

Github Actions builds the shadow-cljs site which is a single `main.js` file. It then ssh's into the server, and `cp`s the files into place.

### Scripts
Converts GeoTIFF to MBTiles for use on tile server. Handy for converting rectified/corrected tif maps to host on our tile server.
`scripts/tif2mbtiles.sh <input-path> <output-path>`

Translate and warp a map in a UTM Zone 39 map
`scripts/utm-zone39-translate.sh <input-path-without-extension-type> <top-left-coordinates/bottom-right-coordinates>`
e.g. `./scripts/utm-zone39-translate.sh "1969.5000.Manama & AlJufayr.1-5" "453000 2901300 456600 2898900"`

## Articles

Lazy blog. I write in Markdown then convert it into hiccup syntax using `markdown-to-hiccup` (included in deps.edn). I then copy paste this into a name space and do a bunch of cleanup. See `src/app/pages/articles/`.

```
bash => clj
Clojure 1.10.3
user=> (require '[markdown-to-hiccup.core :as m])
nil
user=> (m/file->hiccup "file-path")
[:html {} [:head {}] [:body {} [:h1 {} "...."]
```

### Tileserver

We use `tileserver-gl` from MapTiler to host our processed tiles. To see maps locally, you need to download the mbtiles and run it locally.

#### Usage

`tileserver-gl -c tile-config.json`


### The really big maps

Uploading 60gb and storing it somewhere costs money and github caps out at 2gb files. Those files are transferred directly to the server with `scp -r ~/bigmaps/ user@hostname:bigmaps/` then symlinked `ln -s ./* /mnt/maps/mapbh/public/maps/`

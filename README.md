# MapBH ![Deploy](https://github.com/AHAAAAAAA/mapbh/workflows/Deploy/badge.svg)

Minimal frontend to host set of tools to explore old maps of Bahrain and interface with wrapped/rectified maps hosted on our tile server.

### Setup

1. `npm install` to install packages
2. In CIDER (emacs), `M-x cider-jack-in-cljs` and select shadow and :app. Non-CIDER and emacs, `npx shadow-cljs watch app`.


Local dev http://localhost:1212

https://www.mapbh.org


### Stack

Simple clojurescript, shadow-cljs static site stack. Bulma for CSS, npm packages, and leaflet.js for maps. Hosted on a Linode server. Served by an nginx reverse proxy. Cloudflare nameserver for caching/DDoS protection.

Map processing tools, documentation, and tile server hosted in separate repo.

### Deployment

Github Actions builds the shadow-cljs site which is a single `main.js` file. It then ssh's into the server, and `cp`s the files into place.

### Scripts
Converts GeoTIFF to MBTiles for use on tile server. Handy for converting rectified/corrected tif maps to host on our tile server.
`scripts/tif2mbtiles.sh <input-path> <output-path>`

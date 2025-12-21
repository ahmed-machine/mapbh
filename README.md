# mapBH ![Deploy](https://github.com/ahmed-machine/mapbh/workflows/Deploy/badge.svg)

Interactive site to explore historical maps of Bahrain from the 20th century. This is a comprehensive digital archive of Bahrain's recent cartographic history with tools for detailed exploration, comparison, and research.

**Live Site**: [https://www.mapbh.org](https://www.mapbh.org)

**Tileserver**: [https://map.mapbh.org](https://map.mapbh.org)

**Local Dev**: [localhost:1212](http://localhost:1212)

## Overview

mapBH serves as a digital repository and visualization platform for historical maps of Bahrain, featuring:

- **Interactive Map Viewer**: two modes (transparency, side-by-side)
- **Bilingual**: Arabic and English
- **Articles**
- **Catalogue**: all maps in archive with metadata
## Stack

### Frontend
- **ClojureScript**: Primary application language
- **Shadow-CLJS**: Build tool and development environment
- **Reagent**: React wrapper for ClojureScript
- **Re-frame**: State management framework
- **Bulma CSS**: Modern CSS framework for styling
- **Leaflet.js**: Interactive mapping library
	- Note: Includes several modified Leaflet plugins as well
### Backend & Infrastructure
- **Tileserver-GL**: Map tile server for hosting georeferenced maps
- **Nginx**: Reverse proxy server
- **Linode VPS**: Hosting platform at `/var/www/mapbh`
- **Cloudflare R2**: CDN storage for source files, thumbnails, and mbtiles. (cdn.mapbh.org)
- **GitHub Actions**: Continuous deployment


## Development Setup

### Prerequisites
- **Java 8+** (OpenJDK recommended)
- **Node.js 14+** and npm
- **Git**
- Internet

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/ahmed-machine/mapbh.git
   cd mapbh
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash

   # Option 1: Direct shadow-cljs command
   npx shadow-cljs watch app

   # Option 2: For Emacs/CIDER users
	M-x cider-jack-in-cljs, select shadow, then :app
   ```
   - Server: http://localhost:1212

## Map Processing Pipeline

### Georeferencing Workflow
Historical maps are processed through a georeferencing pipeline:

1. **Preparation**: Scan physical map, and cleanup in photoshop
2. **Ground Control Points (GCPs)**: Identification of reference coordinates, typically corner coordinates or points on the map itself
3. **Geometric Transformation**: Using GDAL for spatial rectification from source projection to target projection using GCPs, with additional cutlines and stitching for multi-sheet sets. Generates a GeoTiff file.
4. **Tile Generation**: Converting to MBTiles format for web serving

Where coordinates and projection information are unavailable, we get creative with landmarks, research, and approximation. Each map is a puzzle to be solved whether in pre-processing or research or code. Due to these reasons as well as historical inaccuracy in maps, not all maps align perfectly. Most maps on the site are a combination of preprocessing tricks in Photoshop, projection translation with GDAL via CLI, and fine-tuning in QGIS.

### Processing Scripts

**Convert GeoTIFF to MBTiles (to serve on tileserver)**
```bash
./scripts/tif2mbtiles.sh <input-path> <output-path>
```

**UTM Zone 39 (Ain al Abd) Projection (most common Bahrain standard)**
```bash
./scripts/utm-zone39-translate.sh "map-name" "x1 y1 x2 y2"

# Example:
./scripts/utm-zone39-translate.sh "1969.5000.Manama & AlJufayr.1-5" "453000 2901300 456600 2898900"
```

## Articles
It's a lazy blog:
1. **Write in Markdown**
2. **Convert to Hiccup**: Using the `markdown-to-hiccup` library
3. **Integration**: Articles are compiled into ClojureScript namespaces

```clojure
;; Converting markdown to hiccup syntax
user=> (require '[markdown-to-hiccup.core :as m])
user=> (m/file->hiccup "article.md")
[:div {} [:h1 {} "Article Title"] ...]
```

### Map Metadata (data.cljs)
All map entries are in `data.cljs` with associated translations, URLs, and descriptions.

## Deployment (.github/workflows/main.yml)
GitHub Actions automatically:
1. Builds the Shadow-CLJS application (produces single main.js file)
2. SSHs into the production server
3. Copies files to the web server directory
4. Restarts necessary services

### Server Configuration
- **Nginx**: Reverse proxy configuration in `server-config/`
- **Tileserver-GL**: Map tile serving with `tile-config.json`
- **Systemd**: Service management for background processes on server

### Large File Management with R2 CDN

Large files are stored in **Cloudflare R2** and served via CDN (`https://cdn.mapbh.org/`):
- **Source files** (TIF, PDF, JP2) and **thumbnails** (PNG) served directly from R2
- **MBTiles** synced from R2 to production server (`/var/www/mapbh/public/maps/`) for local tile serving

**Workflow for new maps:**
```bash
# 1. Upload source + generate thumbnail → R2
./scripts/deploy-map-to-r2.sh public/maps/2025-NewMap.tif

# 2. Generate mbtiles + upload → R2
./scripts/tif2mbtiles.sh public/maps/2025-NewMap.tif public/maps/2025-NewMap.mbtiles
./scripts/upload-mbtiles-to-r2.sh public/maps/2025-NewMap.mbtiles

# 3. Add entry to data.cljs. Pushing to main will automatically sync the server.
Alternatively:
ssh user@server "cd /var/www/mapbh && ./scripts/server-sync-all.sh"
```

## Running Local Tile Server

To view maps locally with full functionality:

```bash
# Install tileserver-gl globally
npm install -g tileserver-gl
# Download mbtiles from R2 (./scripts/sync-mbtiles-from-r2.sh)
# Comment out the remote url in data.cljs and use localhost
# Start local tile server
tileserver-gl -c tile-config.json
# Access at http://localhost:8080

```
---

*Last updated: 2025*

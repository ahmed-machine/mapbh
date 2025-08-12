# mapBH - Historical Maps of Bahrain ![Deploy](https://github.com/AHAAAAAAA/mapbh/workflows/Deploy/badge.svg)

An interactive web application for exploring historical maps of Bahrain from the 20th centuries. This project provides a comprehensive digital archive of Bahrain's cartographic history with tools for detailed exploration, comparison, and research.

**Live Site**: [https://www.mapbh.org](https://www.mapbh.org)
**Tileserver**: [https://map.mapbh.org](https://map.mapbh.org)
**Local Dev**: [localhost:1212](localhost:1212)

## Overview

mapBH serves as a digital repository and visualization platform for historical maps of Bahrain, featuring:

- **Interactive Map Viewer**: Explore over dozens of historical maps in two modes (transparency, side-by-side)
- **Multilingual Support**: Full Arabic and English language support
- **Articles**: Articles with embedded historical context
- **Map Catalogue**: Comprehensive database of available maps with metadata
## Stack

### Frontend
- **ClojureScript**: Primary application language
- **Shadow-CLJS**: Build tool and development environment
- **Reagent**: React wrapper for ClojureScript
- **Re-frame**: State management framework
- **Bulma CSS**: Modern CSS framework for styling
- **Leaflet.js**: Interactive mapping library
	- Note: There's several modified Leaflet plugins included as well 
### Backend & Infrastructure
- **Tileserver-GL**: Map tile server for hosting georeferenced maps
- **Nginx**: Reverse proxy server
- **Linode VPS**: Hosting platform
- **Cloudflare**: CDN and DDoS protection
- **GitHub Actions**: Continuous deployment

## Project Structure

```
├── public/                     # Static assets and built files
│   ├── css/                    # Stylesheets and UI components
│   ├── js/                     # Compiled JavaScript and runtime
│   ├── maps/                   # Historical map files (.mbtiles, .tif)
│   ├── img/                    # Images and article assets
│   └── index.html              # Main HTML entry point
├── src/app/                    # ClojureScript source code
│   ├── components/             # Reusable UI components
│   ├── pages/                  # Route-specific page components
│   │   ├── map/                # Map viewer functionality
│   │   └── articles/           # Article content and routing
│   ├── util/                   # Utility functions
│   ├── core.cljs               # Application entry point
│   ├── routes.cljs             # Client-side routing
│   ├── events.cljs             # Re-frame event handlers
│   └── model.cljs              # Application state model
├── scripts/                    # Map processing utilities
├── server-config/              # Server configuration files
├── shadow-cljs.edn             # Build configuration
├── deps.edn                    # Clojure dependencies
└── package.json                # Node.js dependencies
```

## Development Setup

### Prerequisites
- **Java 8+** (OpenJDK recommended)
- **Node.js 14+** and npm
- **Git**
- Internet

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/AHAAAAAAA/mapbh.git
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
   # M-x cider-jack-in-cljs, select shadow, then :app
   ```
   - Server: http://localhost:1212

## Map Processing Pipeline

### Georeferencing Workflow
Historical maps are processed through a georeferencing pipeline:

1. **Preparation**: Scan physical map, and cleanup in photoshop
2. **Ground Control Points (GCPs)**: Identification of reference coordinates
3. **Geometric Transformation**: Using GDAL for spatial rectification from source projection to target projection using GCPs, with additional cutlines and stitching for multi-sheet sets.
4. **Tile Generation**: Converting to MBTiles format for web serving

### Processing Scripts

**Convert GeoTIFF to MBTiles**
```bash
./scripts/tif2mbtiles.sh <input-path> <output-path>
```

**UTM Zone 39 Projection (Bahrain standard)**
```bash
./scripts/utm-zone39-translate.sh "map-name" "x1 y1 x2 y2"

# Example:
./scripts/utm-zone39-translate.sh "1969.5000.Manama & AlJufayr.1-5" "453000 2901300 456600 2898900"
```

## Articles 
Lazy blog:
1. **Write in Markdown**: Standard markdown format for easy editing
2. **Convert to Hiccup**: Using the `markdown-to-hiccup` library
3. **Integration**: Articles are compiled into ClojureScript namespaces

```clojure
;; Converting markdown to hiccup syntax
user=> (require '[markdown-to-hiccup.core :as m])
user=> (m/file->hiccup "article.md")
[:div {} [:h1 {} "Article Title"] ...]
```

### Map Metadata (map_data.cljs)
Each map entry includes:
- **Title**
- **Date**
- **Scale**
- **Description**
- **Source Details**: Creator, publisher, series,etc
- **Processing Notes**: Georeferencing accuracy, transformations applied

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

### Large File Management
Maps exceeding GitHub's 2GB limit are handled separately:
```bash
# Transfer large files directly to server
scp -r ~/bigmaps/ user@hostname:bigmaps/

# Create symlinks in public directory
ln -s ./* /mnt/maps/mapbh/public/maps/
```

## Running Local Tile Server

To view maps locally with full functionality:

```bash
# Install tileserver-gl globally
npm install -g tileserver-gl

# Start local tile server
tileserver-gl -c tile-config.json

# Access at http://localhost:8080
# Comment out the remote url in map_data.cljs
# Comment out the >2gb large maps in tile_config.json (1985)
```
---

*Last updated: 2025*

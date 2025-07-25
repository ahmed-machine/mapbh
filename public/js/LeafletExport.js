/*
 * LeafletExport
 * Copyright (c) 2020 Artyom Lebedev, Vasyl Pasichnyk, pasichnykvasyl (Oswald)
 * Licensed under MIT license.
 * This code is derived from https://github.com/pasichnykvasyl/Leaflet.BigImage
 */


/** Export currently displayed content from the provided Leaflet map instance. The function loads
 * all referenced resources so there is no need to synchronize with the map full loading.
 * @param map L.map instance to export image from.
 * @param crop Exported image crop factor (1 and above values are supported). Image with crop factor
 * greater than 1 includes additional area around input map borders. Image size is scaled up
 * correspondingly.
 * @return Image object:
 * {
 *     image: Exported image blob (PNG)
 *     imageWidth
 *     imageHeight
 * }
 */

class LeafletExporter {
    constructor(map, crop) {
        this.map = map
        this.crop = crop
        this.tileLayers = []
        this.markers = {}
        this.path = {}
        this.circles = {}

        let dimensions = map.getSize()
        this.canvas = document.createElement("canvas")
        this.canvas.width = dimensions.x
        this.canvas.height = dimensions.y
        this.ctx = this.canvas.getContext("2d")
        this.zoom = map.getZoom()
        this.bounds = map.getPixelBounds()

        this._SetCrop(crop)
    }

    async Export() {
        await this._FetchLayers()
        for (let tileLayer of this.tileLayers) {
            for (let tile of Object.values(tileLayer.tileImages)) {
                this.ctx.globalAlpha = tile.opacity;
                // Scale tiles up if they're from a lower zoom level
                const drawSize = tileLayer.tileSize * tileLayer.scaleFactor;
                this.ctx.drawImage(tile.img, tile.x, tile.y, drawSize, drawSize);
            }
        }
        for (let path of Object.values(this.path)) {
            this._DrawPath(path)
        }
        for (let marker of Object.values(this.markers)) {
            this.ctx.drawImage(marker.img, marker.x, marker.y)
        }
        for (let circle of Object.values(this.circles)) {
            this._DrawCircle(circle)
        }
        return this.canvas.toBlob(function(blob) {
            var newImg = document.createElement('img'),
                url = URL.createObjectURL(blob),
                a = document.createElement("a");
            a.href = url;
            a.download = "exported.png";
            newImg.onload = function() {
                // no longer need to read the blob so it's revoked
                URL.revokeObjectURL(url);
            };
            newImg.src = url;
            document.body.appendChild(newImg);
            a.click();
            document.body.removeChild(newImg);
        })
    }

    _SetCrop(crop) {
        if (!crop || crop <= 1) {
            return
        }

        let addX = (this.bounds.max.x - this.bounds.min.x) / 2 * (crop - 1)
        let addY = (this.bounds.max.y - this.bounds.min.y) / 2 * (crop - 1)

        this.bounds.min.x -= addX
        this.bounds.min.y -= addY
        this.bounds.max.x += addX
        this.bounds.max.y += addY

        this.canvas.width *= crop
        this.canvas.height *= crop
    }

    async _FetchLayers() {
        let promises = []
        
        this.map.eachLayer(layer => {
            let promise
            try {
                if (layer instanceof L.Marker && layer._icon && layer._icon.src) {
                    promise = this._FetchMarkerLayer(layer)

                } else if (layer instanceof L.TileLayer) {
                    promise = this._FetchTileLayer(layer)

                } else if (layer instanceof L.CircleMarker) {
                    if (!this.circles[layer._leaflet_id]) {
                        this.circles[layer._leaflet_id] = layer
                    }
                    promise = Promise.resolve()

                } else if (layer instanceof L.Path) {
                    this._FetchPathLayer(layer)
                    promise = Promise.resolve()

                } else {
                    promise = Promise.resolve()
                }

            } catch (e) {
                promise = Promise.resolve()
            }
            promises.push(promise)
        });
        
        return await Promise.all(promises)
    }

    async _FetchMarkerLayer(layer) {
        if (this.markers[layer._leaflet_id]) {
            return
        }

        let pixelPoint = this.map.project(layer._latlng)
        pixelPoint = pixelPoint.subtract(new L.Point(this.bounds.min.x, this.bounds.min.y))

        if (layer.options.icon && layer.options.icon.options &&
            layer.options.icon.options.iconAnchor) {

            pixelPoint.x -= layer.options.icon.options.iconAnchor[0]
            pixelPoint.y -= layer.options.icon.options.iconAnchor[1]
        }

        if (this._IsPointInViewport(pixelPoint)) {
            let image = new Image()
            image.crossOrigin = "Anonymous"
            let url = layer._icon.src
            let promise = new Promise(resolve => {
                image.onload = () => {
                    this.markers[layer._leaflet_id] = {
                        img: image,
                        x: pixelPoint.x,
                        y: pixelPoint.y
                    };
                    resolve()
                }
                image.onerror = () => {
                    resolve()
                }
            })
            image.src = url
            await promise
        }
    }

    async _FetchTileLayer(layer) {
        const tileSize = layer._tileSize.x
        
        // Use appropriate zoom level - respect layer's min/maxNativeZoom
        const minNativeZoom = layer.options?.minNativeZoom || 0;
        const maxNativeZoom = layer.options?.maxNativeZoom || 20;
        const effectiveZoom = Math.max(minNativeZoom, Math.min(this.zoom, maxNativeZoom));
        
        // Calculate bounds at the effective zoom level
        const zoomDifference = this.zoom - effectiveZoom;
        const scaleFactor = Math.pow(2, zoomDifference);
        
        // Calculate bounds at the effective zoom level
        const effectiveBounds = zoomDifference !== 0 
            ? this._calculateScaledBounds(scaleFactor)
            : this.bounds;
        
        const tileLayer = {
            tiles: [],
            tileSize,
            tileBounds: L.bounds(effectiveBounds.min.divideBy(tileSize)._floor(),
                                 effectiveBounds.max.divideBy(tileSize)._floor()),
            tileImages: [],
            layerUrl: layer._url,
            effectiveZoom: effectiveZoom,
            scaleFactor: scaleFactor
        }
        this.tileLayers.push(tileLayer)

        for (let j = tileLayer.tileBounds.min.y; j <= tileLayer.tileBounds.max.y; j++) {
            for (let i = tileLayer.tileBounds.min.x; i <= tileLayer.tileBounds.max.x; i++) {
                tileLayer.tiles.push(new L.Point(i, j))
            }
        }

        
        let promises = []
        tileLayer.tiles.forEach(tilePoint => {
            let originalTilePoint = tilePoint.clone()
            if (layer._adjustTilePoint) {
                layer._adjustTilePoint(tilePoint)
            }

            // Calculate tile position accounting for zoom difference
            const basePos = originalTilePoint.scaleBy(new L.Point(tileLayer.tileSize, tileLayer.tileSize));
            const tilePos = tileLayer.scaleFactor !== 1
                ? basePos.scaleBy(new L.Point(tileLayer.scaleFactor, tileLayer.scaleFactor)).subtract(this.bounds.min)
                : basePos.subtract(this.bounds.min);

            if (tilePoint.y < 0) {
                return
            }

            promises.push(this._FetchTile(tilePoint, tilePos, layer, tileLayer, effectiveZoom))
        })

        return await Promise.all(promises);
    }

    _calculateScaledBounds(scaleFactor) {
        // Calculate bounds that cover the same geographic area when scaled
        const centerX = (this.bounds.min.x + this.bounds.max.x) / 2;
        const centerY = (this.bounds.min.y + this.bounds.max.y) / 2;
        const halfWidth = (this.bounds.max.x - this.bounds.min.x) / 2 / scaleFactor;
        const halfHeight = (this.bounds.max.y - this.bounds.min.y) / 2 / scaleFactor;
        
        return L.bounds(
            new L.Point(centerX / scaleFactor - halfWidth, centerY / scaleFactor - halfHeight),
            new L.Point(centerX / scaleFactor + halfWidth, centerY / scaleFactor + halfHeight)
        );
    }

    _FetchPathLayer(layer) {
        let correct = false
        let parts = []

        if (layer._mRadius || !layer._latlngs) {
            return
        }

        let latlngs = layer.options.fill ? layer._latlngs[0] : layer._latlngs
        latlngs.forEach((latLng) => {
            let pixelPoint = this.map.project(latLng)
            pixelPoint = pixelPoint.subtract(new L.Point(this.bounds.min.x, this.bounds.min.y))
            parts.push(pixelPoint)
            if (pixelPoint.x < this.canvas.width && pixelPoint.y < this.canvas.height) {
                correct = true
            }
        })

        if (correct) {
            this.path[layer._leaflet_id] = {
                parts: parts,
                closed: layer.options.fill,
                options: layer.options
            }
        }
    }

    async _FetchTile(tilePoint, tilePos, layer, tileLayer, effectiveZoom) {
        let image = new Image()
        image.crossOrigin = "Anonymous"
        let url
        // Get tile URL using effective zoom if different from current zoom
        if (layer.getTileUrlAsync) {
            url = await layer.getTileUrlAsync(tilePoint);
        } else if (effectiveZoom !== this.zoom) {
            url = layer._url.replace('{z}', effectiveZoom)
                           .replace('{x}', tilePoint.x)
                           .replace('{y}', tilePoint.y);
        } else {
            url = layer.getTileUrl(tilePoint);
        }
        let promise = new Promise(resolve => {
            let loaded = false;
            
            // Add timeout for tile loading (10 seconds)
            const timeout = setTimeout(() => {
                if (!loaded) {
                    loaded = true;
                    resolve()
                }
            }, 10000);
            
            image.onload = () => {
                if (loaded) return;
                loaded = true;
                clearTimeout(timeout);
                
                let promise
                if (layer.transformTileImage) {
                    promise = layer.transformTileImage(image)
                } else {
                    promise = Promise.resolve()
                }
                promise.then(() => {

                    // Get opacity with fallbacks for different Leaflet versions
                    const currentOpacity = layer.options.opacity ?? layer._opacity ?? 1.0;
                    
                    tileLayer.tileImages.push({
                        img: image,
                        x: tilePos.x,
                        y: tilePos.y,
                        opacity: currentOpacity})
                    resolve()

                }).catch(error => {
                    resolve()
                })
            }
            image.onerror = () => {
                if (loaded) return;
                loaded = true;
                clearTimeout(timeout);
                resolve()
            }
        })
        image.src = url
        await promise
    }

    _IsPointInViewport(point) {
        return (point.x >= 0 && point.y >= 0 &&
            point.x < this.canvas.width && point.y <= this.canvas.height)
    }

    _DrawPath(path) {
        this.ctx.beginPath()
        let count = 0
        path.parts.forEach(point => {
            this.ctx[count++ ? "lineTo" : "moveTo"](point.x, point.y)
        })
        if (path.closed) {
            this.ctx.closePath()
        }
        this._FeelPath(path.options)
    }

    _DrawCircle(layer) {
        if (layer._empty()) {
            return
        }

        let point = this.map.project(layer._latlng)
        point = point.subtract(new L.Point(this.bounds.min.x, this.bounds.min.y))

        let r, s

        if (layer._radius !== undefined) {
            r = Math.max(Math.round(layer._radius), 1)
            s = (Math.max(Math.round(layer._radiusY), 1) || r) / r
        } else {
            r = layer.options.radius
            s = 1
        }

        if (s !== 1) {
            this.ctx.save()
            this.ctx.scale(1, s)
        }

        this.ctx.beginPath()
        this.ctx.arc(point.x, point.y / s, r, 0, Math.PI * 2, false)

        if (s !== 1) {
            this.ctx.restore()
        }

        this._FeelPath(layer.options)
    }

    _FeelPath(options) {
        if (options.fill) {
            this.ctx.globalAlpha = options.fillOpacity
            this.ctx.fillStyle = options.fillColor || options.color
            this.ctx.fill(options.fillRule || "evenodd")
        }
        if (options.stroke && options.weight !== 0) {
            if (this.ctx.setLineDash) {
                this.ctx.setLineDash(options && options._dashArray || [])
            }
            this.ctx.globalAlpha = options.opacity
            this.ctx.lineWidth = options.weight
            this.ctx.strokeStyle = options.color
            this.ctx.lineCap = options.lineCap
            this.ctx.lineJoin = options.lineJoin
            this.ctx.stroke()
        }
        }
}

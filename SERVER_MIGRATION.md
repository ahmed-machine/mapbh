# Server Migration: Block Volume to Main Volume

With the R2 migration complete, the storage requirements have been reduced enough to fit on the main 80GB volume. This guide helps migrate from `/mnt/maps/mapbh` to `/var/www/mapbh`.

## Prerequisites

- SSH access to the production server
- Sudo privileges
- Updated repository code deployed (with new config paths)

## Migration Steps

### 1. Stop Services

```bash
sudo systemctl stop tileserver-gl
sudo systemctl stop nginx
```

### 2. Create New Directory and Move Files

```bash
# Create target directory
sudo mkdir -p /var/www/mapbh

# Move application files (use mv to avoid re-downloading)
sudo mv /mnt/maps/mapbh/* /var/www/mapbh/

# Set proper ownership
sudo chown -R $(whoami):$(whoami) /var/www/mapbh
```

### 3. Create Symlink from ~/mapbh (for deployment script)

```bash
# The GitHub Actions deployment uses ~/mapbh
ln -s /var/www/mapbh ~/mapbh
```

### 4. Update Nginx Configuration

```bash
# Copy new nginx config
sudo cp /var/www/mapbh/server-config/mapbh.org.conf /etc/nginx/sites-available/mapbh.org.conf

# Test nginx configuration
sudo nginx -t

# Reload nginx if test passes
sudo systemctl reload nginx
```

### 5. Update Systemd Service

```bash
# Copy new systemd service file
sudo cp /var/www/mapbh/server-config/tileserver-gl.service /etc/systemd/system/tileserver-gl.service

# Reload systemd daemon
sudo systemctl daemon-reload

# Verify service configuration
systemctl cat tileserver-gl
```

### 6. Sync MBTiles from R2

```bash
cd /var/www/mapbh
./scripts/server-sync-all.sh
```

This will:
- Download all mbtiles from R2 to local cache
- Restart tileserver-gl service

### 7. Start Services

```bash
sudo systemctl start nginx
sudo systemctl start tileserver-gl

# Verify services are running
sudo systemctl status nginx
sudo systemctl status tileserver-gl
```

### 8. Verify Deployment

```bash
# Check that sites are accessible
curl -I https://www.mapbh.org
curl -I https://map.mapbh.org

# Check tile server is responding
curl http://localhost:8080/
```

### 9. Clean Up Old Block Volume (Optional)

**Only after verifying everything works:**

```bash
# Check block volume mount
df -h /mnt/maps

# Remove old files if migration successful
sudo rm -rf /mnt/maps/mapbh/*

# Unmount block volume (optional)
# sudo umount /mnt/maps

# Remove from fstab if you want to detach the volume completely
# sudo nano /etc/fstab
```

## Rollback Plan

If something goes wrong:

```bash
# Stop services
sudo systemctl stop tileserver-gl nginx

# Restore old paths in configs
sudo sed -i 's|/var/www/mapbh|/mnt/maps/mapbh|g' /etc/nginx/sites-available/mapbh.org.conf
sudo sed -i 's|/var/www/mapbh|/mnt/maps/mapbh|g' /etc/systemd/system/tileserver-gl.service

# Reload configurations
sudo systemctl daemon-reload
sudo nginx -t && sudo systemctl reload nginx

# Move files back
sudo mv /var/www/mapbh/* /mnt/maps/mapbh/

# Restart services
sudo systemctl start nginx tileserver-gl
```

## Post-Migration Checklist

- [ ] www.mapbh.org loads correctly
- [ ] map.mapbh.org serves tiles
- [ ] Interactive maps display properly
- [ ] Thumbnails load from CDN
- [ ] Source files download from CDN
- [ ] GitHub Actions deployment succeeds
- [ ] Disk usage is under 80GB: `df -h /`

## Disk Usage

After migration, expected usage on main volume:
- Application code: ~500MB
- MBTiles cache: ~40GB
- Total: ~41GB (fits comfortably in 80GB volume)

Files served from R2:
- Source files: ~73GB
- Thumbnails: ~498MB

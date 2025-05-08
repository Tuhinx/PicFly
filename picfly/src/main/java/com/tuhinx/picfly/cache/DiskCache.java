package com.tuhinx.picfly.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tuhinx.picfly.util.BitmapUtils;
import com.tuhinx.picfly.util.ThreadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Disk cache implementation for PicFly.
 */
public class DiskCache {
    private static final String TAG = "PicFly-DiskCache";
    private static final String CACHE_DIR_NAME = "picfly_cache";
    private static final int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024; // 250MB
    private static final int DISK_CACHE_QUALITY = 90; // JPEG quality for disk cache
    
    private final File cacheDir;
    private final long maxCacheSize;
    private final ReentrantLock writeLock = new ReentrantLock();
    
    /**
     * Creates a new disk cache with the default size.
     *
     * @param context The context
     */
    public DiskCache(Context context) {
        this(context, DEFAULT_DISK_CACHE_SIZE);
    }
    
    /**
     * Creates a new disk cache with a custom size.
     *
     * @param context The context
     * @param maxCacheSize The maximum cache size in bytes
     */
    public DiskCache(Context context, long maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        this.cacheDir = new File(context.getCacheDir(), CACHE_DIR_NAME);
        
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }
    
    /**
     * Gets a bitmap from the disk cache.
     *
     * @param key The cache key
     * @return The cached bitmap or null if not found
     */
    public Bitmap get(String key) {
        String cacheKey = generateCacheKey(key);
        File cacheFile = new File(cacheDir, cacheKey);
        
        if (!cacheFile.exists()) {
            return null;
        }
        
        try (FileInputStream fis = new FileInputStream(cacheFile)) {
            return BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            Log.e(TAG, "Error reading from disk cache", e);
            return null;
        }
    }
    
    /**
     * Interface for disk cache callbacks.
     */
    public interface DiskCacheCallback {
        void onSuccess(Bitmap bitmap);
        void onFailure(Exception e);
    }
    
    /**
     * Gets a bitmap from the disk cache asynchronously.
     *
     * @param key The cache key
     * @param callback The callback to notify of success or failure
     */
    public void getAsync(String key, DiskCacheCallback callback) {
        ThreadUtils.executeInBackground(() -> {
            try {
                final Bitmap bitmap = get(key);
                
                if (bitmap != null) {
                    ThreadUtils.executeOnMainThread(() -> callback.onSuccess(bitmap));
                } else {
                    ThreadUtils.executeOnMainThread(() -> 
                            callback.onFailure(new IOException("Bitmap not found in disk cache")));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error reading from disk cache", e);
                ThreadUtils.executeOnMainThread(() -> callback.onFailure(e));
            }
        });
    }
    
    /**
     * Puts a bitmap in the disk cache.
     *
     * @param key The cache key
     * @param bitmap The bitmap to cache
     */
    public void put(String key, Bitmap bitmap) {
        if (key == null || bitmap == null) {
            return;
        }
        
        String cacheKey = generateCacheKey(key);
        File cacheFile = new File(cacheDir, cacheKey);
        
        writeLock.lock();
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, DISK_CACHE_QUALITY, fos);
            fos.flush();
            fos.close();
            
            // Check if we need to trim the cache
            trimCacheIfNeeded();
        } catch (IOException e) {
            Log.e(TAG, "Error writing to disk cache", e);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Puts a bitmap in the disk cache asynchronously.
     *
     * @param key The cache key
     * @param bitmap The bitmap to cache
     */
    public void putAsync(String key, Bitmap bitmap) {
        ThreadUtils.executeInBackground(() -> put(key, bitmap));
    }
    
    /**
     * Removes a bitmap from the disk cache.
     *
     * @param key The cache key
     */
    public void remove(String key) {
        String cacheKey = generateCacheKey(key);
        File cacheFile = new File(cacheDir, cacheKey);
        
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
    }
    
    /**
     * Clears the disk cache.
     */
    public void clear() {
        writeLock.lock();
        try {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Trims the cache if it exceeds the maximum size.
     */
    private void trimCacheIfNeeded() {
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return;
        }
        
        long currentSize = 0;
        for (File file : files) {
            currentSize += file.length();
        }
        
        if (currentSize > maxCacheSize) {
            // Sort files by last modified time
            java.util.Arrays.sort(files, (f1, f2) -> 
                    Long.compare(f1.lastModified(), f2.lastModified()));
            
            // Delete files until we're under the limit
            for (File file : files) {
                if (currentSize <= maxCacheSize) {
                    break;
                }
                
                long fileSize = file.length();
                if (file.delete()) {
                    currentSize -= fileSize;
                }
            }
        }
    }
    
    /**
     * Generates a cache key for a URL.
     *
     * @param url The URL
     * @return The cache key
     */
    private String generateCacheKey(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(url.getBytes());
            byte[] digest = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback to a simple hash if MD5 is not available
            return String.valueOf(url.hashCode());
        }
    }
}

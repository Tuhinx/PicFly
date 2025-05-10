package com.github.picfly.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.github.picfly.util.BitmapUtils;

/**
 * Memory cache implementation using LruCache for PicFly.
 */
public class MemoryCache {
    private static final String TAG = "PicFly-MemoryCache";
    private static final int DEFAULT_MEMORY_CACHE_PERCENTAGE = 25;
    
    private final LruCache<String, Bitmap> memoryCache;
    
    /**
     * Creates a new memory cache with the default size.
     *
     * @param context The context used to determine the cache size
     */
    public MemoryCache(Context context) {
        this(context, DEFAULT_MEMORY_CACHE_PERCENTAGE);
    }
    
    /**
     * Creates a new memory cache with a custom size.
     *
     * @param context The context used to determine the cache size
     * @param percentageOfAvailableMemory The percentage of available memory to use for the cache
     */
    public MemoryCache(Context context, int percentageOfAvailableMemory) {
        int cacheSize = calculateMemoryCacheSize(context, percentageOfAvailableMemory);
        
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return BitmapUtils.getBitmapByteSize(bitmap);
            }
        };
    }
    
    /**
     * Calculates the memory cache size based on the device's available memory.
     *
     * @param context The context
     * @param percentageOfAvailableMemory The percentage of available memory to use
     * @return The calculated cache size in bytes
     */
    private int calculateMemoryCacheSize(Context context, int percentageOfAvailableMemory) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
        return memoryClassBytes * percentageOfAvailableMemory / 100;
    }
    
    /**
     * Puts a bitmap in the cache.
     *
     * @param key The cache key
     * @param bitmap The bitmap to cache
     */
    public void put(String key, Bitmap bitmap) {
        if (key == null || bitmap == null) {
            return;
        }
        
        memoryCache.put(key, bitmap);
    }
    
    /**
     * Gets a bitmap from the cache.
     *
     * @param key The cache key
     * @return The cached bitmap or null if not found
     */
    public Bitmap get(String key) {
        if (key == null) {
            return null;
        }
        
        return memoryCache.get(key);
    }
    
    /**
     * Removes a bitmap from the cache.
     *
     * @param key The cache key
     */
    public void remove(String key) {
        if (key == null) {
            return;
        }
        
        memoryCache.remove(key);
    }
    
    /**
     * Clears the cache.
     */
    public void clear() {
        memoryCache.evictAll();
    }
    
    /**
     * Gets the current size of the cache in bytes.
     *
     * @return The current size of the cache in bytes
     */
    public int size() {
        return memoryCache.size();
    }
    
    /**
     * Gets the maximum size of the cache in bytes.
     *
     * @return The maximum size of the cache in bytes
     */
    public int maxSize() {
        return memoryCache.maxSize();
    }
}

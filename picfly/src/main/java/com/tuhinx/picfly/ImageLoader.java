package com.tuhinx.picfly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.tuhinx.picfly.cache.DiskCache;
import com.tuhinx.picfly.cache.MemoryCache;
import com.tuhinx.picfly.network.NetworkFetcher;
import com.tuhinx.picfly.target.Target;
import com.tuhinx.picfly.transformation.Transformation;
import com.tuhinx.picfly.util.BitmapUtils;
import com.tuhinx.picfly.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Core image loading class for PicFly.
 */
public class ImageLoader {
    private static final String TAG = "PicFly-ImageLoader";

    private final Context context;
    private final MemoryCache memoryCache;
    private final DiskCache diskCache;
    private final NetworkFetcher networkFetcher;

    /**
     * Creates a new ImageLoader.
     *
     * @param context        The context
     * @param memoryCache    The memory cache to use
     * @param diskCache      The disk cache to use
     * @param networkFetcher The network fetcher to use
     */
    public ImageLoader(Context context, MemoryCache memoryCache, DiskCache diskCache, NetworkFetcher networkFetcher) {
        this.context = context.getApplicationContext();
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
        this.networkFetcher = networkFetcher;
    }

    /**
     * Loads an image from a URL into a target.
     *
     * @param url                 The URL to load the image from
     * @param target              The target to load the image into
     * @param placeholderDrawable The placeholder drawable to display while loading
     * @param errorDrawable       The error drawable to display if loading fails
     * @param transformations     The transformations to apply to the image
     * @param width               The target width for resizing, or 0 for original
     *                            size
     * @param height              The target height for resizing, or 0 for original
     *                            size
     */
    public void loadImage(String url, Target target, Drawable placeholderDrawable,
            Drawable errorDrawable, List<Transformation> transformations,
            int width, int height) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "Cannot load image with null or empty URL");
            if (target != null) {
                target.onLoadFailed(errorDrawable);
            }
            return;
        }

        if (target != null) {
            target.onLoadStarted(placeholderDrawable);
        }

        // Generate a cache key that includes transformations and resize dimensions
        String cacheKey = generateCacheKey(url, transformations, width, height);

        // Try to get the image from the memory cache first
        Bitmap memCachedBitmap = memoryCache.get(cacheKey);
        if (memCachedBitmap != null) {
            Log.d(TAG, "Image loaded from memory cache: " + url);
            if (target != null) {
                target.onResourceReady(memCachedBitmap);
            }
            return;
        }

        // If not in memory cache, try to get it from the disk cache
        ThreadUtils.executeInBackground(() -> {
            // Check disk cache
            Bitmap diskCachedBitmap = diskCache.get(cacheKey);
            if (diskCachedBitmap != null) {
                Log.d(TAG, "Image loaded from disk cache: " + url);

                // Put in memory cache
                memoryCache.put(cacheKey, diskCachedBitmap);

                // Deliver to target on main thread
                if (target != null) {
                    ThreadUtils.executeOnMainThread(() -> target.onResourceReady(diskCachedBitmap));
                }
                return;
            }

            // If not in any cache, fetch from network
            networkFetcher.fetchImage(url, new NetworkFetcher.FetchCallback() {
                @Override
                public void onSuccess(Bitmap originalBitmap) {
                    // Process the bitmap (resize and apply transformations)
                    Bitmap processedBitmap = processBitmap(originalBitmap, transformations, width, height);

                    // Cache the processed bitmap
                    memoryCache.put(cacheKey, processedBitmap);
                    diskCache.putAsync(cacheKey, processedBitmap);

                    if (target != null) {
                        target.onResourceReady(processedBitmap);
                    }

                    Log.d(TAG, "Image loaded from network: " + url);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "Failed to load image: " + url, e);
                    if (target != null) {
                        target.onLoadFailed(errorDrawable);
                    }
                }
            });
        });
    }

    /**
     * Loads an image from a URL into a target without transformations or resizing.
     *
     * @param url                 The URL to load the image from
     * @param target              The target to load the image into
     * @param placeholderDrawable The placeholder drawable to display while loading
     * @param errorDrawable       The error drawable to display if loading fails
     */
    public void loadImage(String url, Target target, Drawable placeholderDrawable, Drawable errorDrawable) {
        loadImage(url, target, placeholderDrawable, errorDrawable, new ArrayList<>(), 0, 0);
    }

    /**
     * Processes a bitmap by resizing it and applying transformations.
     *
     * @param original        The original bitmap
     * @param transformations The transformations to apply
     * @param width           The target width, or 0 for original size
     * @param height          The target height, or 0 for original size
     * @return The processed bitmap
     */
    private Bitmap processBitmap(Bitmap original, List<Transformation> transformations, int width, int height) {
        if (original == null) {
            return null;
        }

        Bitmap result = original;

        // Resize if needed
        if (width > 0 && height > 0 && (original.getWidth() != width || original.getHeight() != height)) {
            result = Bitmap.createScaledBitmap(original, width, height, true);

            // Recycle the original if it's not the same as the result
            if (original != result) {
                original.recycle();
            }
        }

        // Apply transformations
        if (transformations != null && !transformations.isEmpty()) {
            for (Transformation transformation : transformations) {
                Bitmap transformed = transformation.transform(result);

                // Recycle the previous result if it's not the same as the transformed
                if (transformed != result) {
                    result.recycle();
                    result = transformed;
                }
            }
        }

        return result;
    }

    /**
     * Generates a cache key for a URL with transformations and resize dimensions.
     *
     * @param url             The URL
     * @param transformations The transformations
     * @param width           The target width
     * @param height          The target height
     * @return The cache key
     */
    private String generateCacheKey(String url, List<Transformation> transformations, int width, int height) {
        StringBuilder sb = new StringBuilder(url);

        // Add dimensions to the key if resizing
        if (width > 0 && height > 0) {
            sb.append("_").append(width).append("x").append(height);
        }

        // Add transformation keys
        if (transformations != null && !transformations.isEmpty()) {
            for (Transformation transformation : transformations) {
                sb.append("_").append(transformation.key());
            }
        }

        return sb.toString();
    }

    /**
     * Preloads an image into the cache.
     *
     * @param url             The URL to preload
     * @param transformations The transformations to apply
     * @param width           The target width, or 0 for original size
     * @param height          The target height, or 0 for original size
     */
    public void preloadImage(String url, List<Transformation> transformations, int width, int height) {
        if (url == null || url.isEmpty()) {
            return;
        }

        // Generate a cache key
        String cacheKey = generateCacheKey(url, transformations, width, height);

        // Check if the image is already in memory cache
        if (memoryCache.get(cacheKey) != null) {
            return;
        }

        // Check if the image is in disk cache
        ThreadUtils.executeInBackground(() -> {
            Bitmap diskCachedBitmap = diskCache.get(cacheKey);
            if (diskCachedBitmap != null) {
                // Put in memory cache
                memoryCache.put(cacheKey, diskCachedBitmap);
                return;
            }

            // If not in any cache, fetch from network
            networkFetcher.fetchImage(url, new NetworkFetcher.FetchCallback() {
                @Override
                public void onSuccess(Bitmap originalBitmap) {
                    // Process the bitmap (resize and apply transformations)
                    Bitmap processedBitmap = processBitmap(originalBitmap, transformations, width, height);

                    // Cache the processed bitmap
                    memoryCache.put(cacheKey, processedBitmap);
                    diskCache.putAsync(cacheKey, processedBitmap);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "Failed to preload image: " + url, e);
                }
            });
        });
    }

    /**
     * Preloads an image into the cache without transformations or resizing.
     *
     * @param url The URL to preload
     */
    public void preloadImage(String url) {
        preloadImage(url, new ArrayList<>(), 0, 0);
    }

    /**
     * Clears the memory cache.
     */
    public void clearMemoryCache() {
        memoryCache.clear();
    }

    /**
     * Clears the disk cache.
     */
    public void clearDiskCache() {
        diskCache.clear();
    }

    /**
     * Cancels all ongoing network requests.
     */
    public void cancelAll() {
        networkFetcher.cancelAll();
    }
}

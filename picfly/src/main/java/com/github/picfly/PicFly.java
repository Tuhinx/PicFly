package com.github.picfly;

import android.content.Context;

import com.github.picfly.cache.DiskCache;
import com.github.picfly.cache.MemoryCache;
import com.github.picfly.network.NetworkFetcher;
import com.github.picfly.recyclerview.RecyclerViewPreloader;
import com.github.picfly.recyclerview.ViewTargetAdapter;

/**
 * Main entry point for the PicFly image loading library.
 */
public class PicFly {
    private static PicFly instance;

    private final Context context;
    private final ImageLoader imageLoader;
    private final MemoryCache memoryCache;
    private final DiskCache diskCache;
    private final NetworkFetcher networkFetcher;

    /**
     * Gets the singleton instance of PicFly.
     *
     * @param context The context
     * @return The PicFly instance
     */
    public static synchronized PicFly get(Context context) {
        if (instance == null) {
            instance = new PicFly(context);
        }
        return instance;
    }

    /**
     * Creates a new PicFly instance.
     *
     * @param context The context
     */
    private PicFly(Context context) {
        this.context = context.getApplicationContext();
        this.memoryCache = new MemoryCache(this.context);
        this.diskCache = new DiskCache(this.context);
        this.networkFetcher = new NetworkFetcher();
        this.imageLoader = new ImageLoader(this.context, memoryCache, diskCache, networkFetcher);
    }

    /**
     * Creates a new request builder for loading images.
     *
     * @return A new RequestBuilder
     */
    public RequestBuilder load(String url) {
        return new RequestBuilder(this, context, imageLoader).load(url);
    }

    /**
     * Clears the memory cache.
     */
    public void clearMemoryCache() {
        imageLoader.clearMemoryCache();
    }

    /**
     * Clears the disk cache.
     */
    public void clearDiskCache() {
        imageLoader.clearDiskCache();
    }

    /**
     * Clears all caches.
     */
    public void clearAllCaches() {
        clearMemoryCache();
        clearDiskCache();
    }

    /**
     * Cancels all ongoing network requests.
     */
    public void cancelAll() {
        imageLoader.cancelAll();
    }

    /**
     * Creates a new RecyclerViewPreloader for preloading images in a RecyclerView.
     *
     * @param modelProvider The provider of models to preload
     * @param <T>           The type of data displayed in the RecyclerView
     * @return A new RecyclerViewPreloader
     */
    public <T> RecyclerViewPreloader<T> getRecyclerViewPreloader(
            RecyclerViewPreloader.PreloadModelProvider<T> modelProvider) {
        return new RecyclerViewPreloader<>(context, modelProvider);
    }

    /**
     * Creates a new RecyclerViewPreloader for preloading images in a RecyclerView.
     *
     * @param modelProvider The provider of models to preload
     * @param maxPreload    The maximum number of items to preload
     * @param <T>           The type of data displayed in the RecyclerView
     * @return A new RecyclerViewPreloader
     */
    public <T> RecyclerViewPreloader<T> getRecyclerViewPreloader(
            RecyclerViewPreloader.PreloadModelProvider<T> modelProvider, int maxPreload) {
        return new RecyclerViewPreloader<>(context, modelProvider, maxPreload);
    }

    /**
     * Clears all RecyclerView targets.
     */
    public void clearRecyclerViewTargets() {
        ViewTargetAdapter.clearAll();
    }

    /**
     * Gets the memory cache.
     *
     * @return The memory cache
     */
    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    /**
     * Gets the disk cache.
     *
     * @return The disk cache
     */
    public DiskCache getDiskCache() {
        return diskCache;
    }

    /**
     * Gets the network fetcher.
     *
     * @return The network fetcher
     */
    public NetworkFetcher getNetworkFetcher() {
        return networkFetcher;
    }

    /**
     * Gets the image loader.
     *
     * @return The image loader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

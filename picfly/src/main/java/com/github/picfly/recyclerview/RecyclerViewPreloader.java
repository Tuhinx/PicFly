package com.github.picfly.recyclerview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.picfly.PicFly;

import java.util.List;

/**
 * A utility class that helps with preloading images for RecyclerView.
 * @param <T> The type of data displayed in the RecyclerView
 */
public class RecyclerViewPreloader<T> extends RecyclerView.OnScrollListener {
    private static final int DEFAULT_PRELOAD_DISTANCE = 3;
    
    private final PicFly picFly;
    private final PreloadModelProvider<T> preloadModelProvider;
    private final int maxPreload;
    private int lastFirstVisible = -1;
    private int lastLastVisible = -1;
    
    /**
     * Interface for providing models to preload.
     * @param <U> The type of data displayed in the RecyclerView
     */
    public interface PreloadModelProvider<U> {
        /**
         * Returns a list of URLs to preload for the given item.
         *
         * @param item The item to get URLs for
         * @return A list of URLs to preload
         */
        @NonNull
        List<String> getPreloadUrls(@NonNull U item);
        
        /**
         * Returns the dimensions to use when preloading the image.
         *
         * @param item The item to get dimensions for
         * @return An array containing the width and height
         */
        int[] getPreloadDimensions(@NonNull U item);
    }
    
    /**
     * Creates a new RecyclerViewPreloader with the default preload distance.
     *
     * @param context The context
     * @param preloadModelProvider The provider of models to preload
     */
    public RecyclerViewPreloader(Context context, PreloadModelProvider<T> preloadModelProvider) {
        this(context, preloadModelProvider, DEFAULT_PRELOAD_DISTANCE);
    }
    
    /**
     * Creates a new RecyclerViewPreloader with a custom preload distance.
     *
     * @param context The context
     * @param preloadModelProvider The provider of models to preload
     * @param maxPreload The maximum number of items to preload
     */
    public RecyclerViewPreloader(Context context, PreloadModelProvider<T> preloadModelProvider, int maxPreload) {
        this.picFly = PicFly.get(context);
        this.preloadModelProvider = preloadModelProvider;
        this.maxPreload = maxPreload;
    }
    
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return;
        }
        
        int firstVisible = findFirstVisibleItemPosition(layoutManager);
        int lastVisible = findLastVisibleItemPosition(layoutManager);
        
        // Skip if the visible range hasn't changed
        if (firstVisible == lastFirstVisible && lastVisible == lastLastVisible) {
            return;
        }
        
        lastFirstVisible = firstVisible;
        lastLastVisible = lastVisible;
        
        // Determine the direction of scrolling
        boolean scrollingDown = dy > 0;
        
        // Preload items ahead of the visible range
        int start, end;
        if (scrollingDown) {
            start = lastVisible + 1;
            end = Math.min(start + maxPreload, adapter.getItemCount() - 1);
        } else {
            end = Math.max(0, firstVisible - 1);
            start = Math.max(0, end - maxPreload);
        }
        
        // Preload the items
        for (int i = start; i <= end; i++) {
            preloadItem(recyclerView, i);
        }
    }
    
    private void preloadItem(RecyclerView recyclerView, int position) {
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter == null || position >= adapter.getItemCount()) {
            return;
        }
        
        // Get the item from the adapter
        Object item = getItemAt(recyclerView, position);
        if (item == null) {
            return;
        }
        
        // Cast to the correct type
        @SuppressWarnings("unchecked")
        T typedItem = (T) item;
        
        // Get the URLs to preload
        List<String> urls = preloadModelProvider.getPreloadUrls(typedItem);
        if (urls.isEmpty()) {
            return;
        }
        
        // Get the dimensions to use
        int[] dimensions = preloadModelProvider.getPreloadDimensions(typedItem);
        int width = dimensions[0];
        int height = dimensions[1];
        
        // Preload each URL
        for (String url : urls) {
            picFly.load(url)
                    .resize(width, height)
                    .preload();
        }
    }
    
    private Object getItemAt(RecyclerView recyclerView, int position) {
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter == null || position >= adapter.getItemCount()) {
            return null;
        }
        
        // Try to get the item using reflection
        try {
            java.lang.reflect.Method getItemMethod = adapter.getClass().getMethod("getItem", int.class);
            return getItemMethod.invoke(adapter, position);
        } catch (Exception e) {
            // If reflection fails, return null
            return null;
        }
    }
    
    private int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof androidx.recyclerview.widget.LinearLayoutManager) {
            return ((androidx.recyclerview.widget.LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager) {
            int[] positions = ((androidx.recyclerview.widget.StaggeredGridLayoutManager) layoutManager)
                    .findFirstVisibleItemPositions(null);
            return positions.length > 0 ? positions[0] : 0;
        }
        return 0;
    }
    
    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof androidx.recyclerview.widget.LinearLayoutManager) {
            return ((androidx.recyclerview.widget.LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager) {
            int[] positions = ((androidx.recyclerview.widget.StaggeredGridLayoutManager) layoutManager)
                    .findLastVisibleItemPositions(null);
            return positions.length > 0 ? positions[positions.length - 1] : 0;
        }
        return 0;
    }
}

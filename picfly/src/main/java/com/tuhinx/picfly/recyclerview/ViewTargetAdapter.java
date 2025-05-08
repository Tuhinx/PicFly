package com.tuhinx.picfly.recyclerview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuhinx.picfly.PicFly;
import com.tuhinx.picfly.target.Target;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that helps with loading images into RecyclerView items.
 */
public class ViewTargetAdapter {
    private static final Map<Integer, Target> targets = new HashMap<>();
    
    /**
     * Loads an image into a view in a RecyclerView.ViewHolder.
     *
     * @param picFly The PicFly instance
     * @param url The URL to load
     * @param view The view to load the image into
     * @param viewHolder The ViewHolder containing the view
     */
    public static void loadInto(PicFly picFly, String url, View view, RecyclerView.ViewHolder viewHolder) {
        // Generate a unique key for this view in this ViewHolder
        int targetKey = generateTargetKey(viewHolder, view);
        
        // Cancel any existing load for this view
        Target existingTarget = targets.get(targetKey);
        if (existingTarget != null) {
            existingTarget.onCleared();
        }
        
        // Create a new target
        ViewTarget target = new ViewTarget(view, viewHolder, targetKey);
        targets.put(targetKey, target);
        
        // Load the image
        picFly.load(url).into(target);
    }
    
    /**
     * Clears all targets.
     */
    public static void clearAll() {
        for (Target target : targets.values()) {
            target.onCleared();
        }
        targets.clear();
    }
    
    /**
     * Generates a unique key for a view in a ViewHolder.
     *
     * @param viewHolder The ViewHolder
     * @param view The view
     * @return A unique key
     */
    private static int generateTargetKey(RecyclerView.ViewHolder viewHolder, View view) {
        return 31 * viewHolder.hashCode() + view.getId();
    }
    
    /**
     * A target implementation for views in RecyclerView items.
     */
    private static class ViewTarget implements Target {
        private final WeakReference<View> viewRef;
        private final WeakReference<RecyclerView.ViewHolder> viewHolderRef;
        private final int targetKey;
        
        /**
         * Creates a new ViewTarget.
         *
         * @param view The view to load the image into
         * @param viewHolder The ViewHolder containing the view
         * @param targetKey The unique key for this target
         */
        public ViewTarget(View view, RecyclerView.ViewHolder viewHolder, int targetKey) {
            this.viewRef = new WeakReference<>(view);
            this.viewHolderRef = new WeakReference<>(viewHolder);
            this.targetKey = targetKey;
        }
        
        @Override
        public void onLoadStarted(Drawable placeholder) {
            View view = viewRef.get();
            if (view != null && isViewHolderValid()) {
                if (view instanceof android.widget.ImageView) {
                    ((android.widget.ImageView) view).setImageDrawable(placeholder);
                } else {
                    view.setBackground(placeholder);
                }
            }
        }
        
        @Override
        public void onLoadFailed(Drawable errorDrawable) {
            View view = viewRef.get();
            if (view != null && isViewHolderValid()) {
                if (view instanceof android.widget.ImageView) {
                    ((android.widget.ImageView) view).setImageDrawable(errorDrawable);
                } else {
                    view.setBackground(errorDrawable);
                }
            }
            
            // Remove this target from the map
            targets.remove(targetKey);
        }
        
        @Override
        public void onResourceReady(Bitmap bitmap) {
            View view = viewRef.get();
            if (view != null && isViewHolderValid()) {
                if (view instanceof android.widget.ImageView) {
                    ((android.widget.ImageView) view).setImageBitmap(bitmap);
                } else {
                    view.setBackground(new android.graphics.drawable.BitmapDrawable(
                            view.getResources(), bitmap));
                }
            }
            
            // Remove this target from the map
            targets.remove(targetKey);
        }
        
        @Override
        public void onCleared() {
            View view = viewRef.get();
            if (view != null) {
                if (view instanceof android.widget.ImageView) {
                    ((android.widget.ImageView) view).setImageDrawable(null);
                } else {
                    view.setBackground(null);
                }
            }
            
            // Remove this target from the map
            targets.remove(targetKey);
        }
        
        /**
         * Checks if the ViewHolder is still valid.
         *
         * @return true if the ViewHolder is still valid, false otherwise
         */
        private boolean isViewHolderValid() {
            RecyclerView.ViewHolder viewHolder = viewHolderRef.get();
            return viewHolder != null && viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION;
        }
    }
}

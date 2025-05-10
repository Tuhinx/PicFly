package com.github.picfly.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Interface for targets that can receive loaded images.
 */
public interface Target {
    /**
     * Called when the image load starts.
     *
     * @param placeholder The placeholder drawable to display while loading
     */
    void onLoadStarted(Drawable placeholder);
    
    /**
     * Called when the image load fails.
     *
     * @param errorDrawable The error drawable to display
     */
    void onLoadFailed(Drawable errorDrawable);
    
    /**
     * Called when the image load succeeds.
     *
     * @param bitmap The loaded bitmap
     */
    void onResourceReady(Bitmap bitmap);
    
    /**
     * Called when the target is cleared.
     */
    void onCleared();
}

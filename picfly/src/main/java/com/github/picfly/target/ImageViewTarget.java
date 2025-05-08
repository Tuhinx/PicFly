package com.github.picfly.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Target implementation for ImageView.
 */
public class ImageViewTarget implements Target {
    private final WeakReference<ImageView> imageViewRef;
    
    /**
     * Creates a new ImageViewTarget.
     *
     * @param imageView The ImageView to load the image into
     */
    public ImageViewTarget(ImageView imageView) {
        this.imageViewRef = new WeakReference<>(imageView);
    }
    
    @Override
    public void onLoadStarted(Drawable placeholder) {
        ImageView imageView = imageViewRef.get();
        if (imageView != null) {
            imageView.setImageDrawable(placeholder);
        }
    }
    
    @Override
    public void onLoadFailed(Drawable errorDrawable) {
        ImageView imageView = imageViewRef.get();
        if (imageView != null) {
            imageView.setImageDrawable(errorDrawable);
        }
    }
    
    @Override
    public void onResourceReady(Bitmap bitmap) {
        ImageView imageView = imageViewRef.get();
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
    
    @Override
    public void onCleared() {
        ImageView imageView = imageViewRef.get();
        if (imageView != null) {
            imageView.setImageDrawable(null);
        }
    }
    
    /**
     * Gets the ImageView associated with this target.
     *
     * @return The ImageView or null if it has been garbage collected
     */
    public ImageView getImageView() {
        return imageViewRef.get();
    }
}

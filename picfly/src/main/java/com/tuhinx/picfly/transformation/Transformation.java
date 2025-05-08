package com.tuhinx.picfly.transformation;

import android.graphics.Bitmap;

/**
 * Interface for image transformations.
 */
public interface Transformation {
    /**
     * Transforms a bitmap.
     *
     * @param source The source bitmap
     * @return The transformed bitmap
     */
    Bitmap transform(Bitmap source);
    
    /**
     * Gets a unique key for this transformation.
     * This is used for caching purposes.
     *
     * @return A unique key for this transformation
     */
    String key();
}

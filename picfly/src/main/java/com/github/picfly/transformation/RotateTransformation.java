package com.github.picfly.transformation;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Transformation that rotates images by a specified angle.
 */
public class RotateTransformation implements Transformation {
    private final float degrees;

    /**
     * Creates a new RotateTransformation.
     *
     * @param degrees The rotation angle in degrees
     */
    public RotateTransformation(float degrees) {
        this.degrees = degrees;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(
                source,
                0,
                0,
                source.getWidth(),
                source.getHeight(),
                matrix,
                true
        );

        // If the source bitmap is not the same as the output, recycle it
        if (source != rotatedBitmap) {
            source.recycle();
        }

        return rotatedBitmap;
    }

    @Override
    public String key() {
        return "rotate_" + degrees;
    }
}

package com.github.picfly.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Transformation that adjusts the brightness of images.
 */
public class BrightnessTransformation implements Transformation {
    private final float brightness;

    /**
     * Creates a new BrightnessTransformation.
     *
     * @param brightness The brightness adjustment value (-1.0f to 1.0f)
     *                  where -1.0f is completely dark and 1.0f is completely bright
     */
    public BrightnessTransformation(float brightness) {
        this.brightness = brightness;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source == null) {
            return null;
        }

        // Create a new bitmap with the same dimensions
        Bitmap outputBitmap = Bitmap.createBitmap(
                source.getWidth(),
                source.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a canvas to draw on the new bitmap
        Canvas canvas = new Canvas(outputBitmap);

        // Create a color matrix for brightness adjustment
        ColorMatrix colorMatrix = new ColorMatrix();
        // Scale the brightness (multiply by a factor)
        // brightness = -1 (black), 0 (no change), 1 (white)
        colorMatrix.set(new float[]{
                1, 0, 0, 0, brightness * 255, // Red
                0, 1, 0, 0, brightness * 255, // Green
                0, 0, 1, 0, brightness * 255, // Blue
                0, 0, 0, 1, 0                 // Alpha
        });

        // Create a paint with the brightness color filter
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        // Draw the source bitmap onto the new bitmap using the brightness paint
        canvas.drawBitmap(source, 0, 0, paint);

        // If the source bitmap is not the same as the output, recycle it
        if (source != outputBitmap) {
            source.recycle();
        }

        return outputBitmap;
    }

    @Override
    public String key() {
        return "brightness_" + brightness;
    }
}

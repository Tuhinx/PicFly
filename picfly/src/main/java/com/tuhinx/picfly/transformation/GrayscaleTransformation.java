package com.tuhinx.picfly.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Transformation that converts images to grayscale.
 */
public class GrayscaleTransformation implements Transformation {
    
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
        
        // Create a grayscale color matrix
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        
        // Create a paint with the grayscale color filter
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        
        // Draw the source bitmap onto the new bitmap using the grayscale paint
        canvas.drawBitmap(source, 0, 0, paint);
        
        // If the source bitmap is not the same as the output, recycle it
        if (source != outputBitmap) {
            source.recycle();
        }
        
        return outputBitmap;
    }
    
    @Override
    public String key() {
        return "grayscale";
    }
}

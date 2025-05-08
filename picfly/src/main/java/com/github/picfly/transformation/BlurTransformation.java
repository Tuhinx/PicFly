package com.github.picfly.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Transformation that applies a Gaussian blur to images.
 */
public class BlurTransformation implements Transformation {
    private static final float DEFAULT_RADIUS = 10f;
    private static final float MAX_RADIUS = 25f;
    
    private final Context context;
    private final float radius;
    
    /**
     * Creates a new BlurTransformation with the default radius.
     *
     * @param context The context
     */
    public BlurTransformation(Context context) {
        this(context, DEFAULT_RADIUS);
    }
    
    /**
     * Creates a new BlurTransformation with a custom radius.
     *
     * @param context The context
     * @param radius The blur radius (0-25)
     */
    public BlurTransformation(Context context, float radius) {
        this.context = context.getApplicationContext();
        this.radius = Math.min(radius, MAX_RADIUS);
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
        
        // Initialize RenderScript
        RenderScript renderScript = RenderScript.create(context);
        
        // Create allocations for input and output
        Allocation inputAllocation = Allocation.createFromBitmap(renderScript, source);
        Allocation outputAllocation = Allocation.createFromBitmap(renderScript, outputBitmap);
        
        // Create the blur script
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blurScript.setRadius(radius);
        blurScript.setInput(inputAllocation);
        blurScript.forEach(outputAllocation);
        
        // Copy the output to the output bitmap
        outputAllocation.copyTo(outputBitmap);
        
        // Clean up
        renderScript.destroy();
        
        // If the source bitmap is not the same as the output, recycle it
        if (source != outputBitmap) {
            source.recycle();
        }
        
        return outputBitmap;
    }
    
    @Override
    public String key() {
        return "blur_" + radius;
    }
}

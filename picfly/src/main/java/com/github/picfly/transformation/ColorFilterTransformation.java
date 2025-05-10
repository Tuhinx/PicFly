package com.github.picfly.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

/**
 * Transformation that applies a color filter to images.
 */
public class ColorFilterTransformation implements Transformation {
    private final int color;

    /**
     * Creates a new ColorFilterTransformation.
     *
     * @param color The color to apply as a filter (e.g., Color.RED, 0xFFFF0000)
     */
    public ColorFilterTransformation(int color) {
        this.color = color;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source == null) {
            return null;
        }

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0, 0, paint);

        source.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return "color_filter_" + color;
    }
}

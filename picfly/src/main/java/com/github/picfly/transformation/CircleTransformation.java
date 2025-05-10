package com.github.picfly.transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * Transformation that crops images into a circle.
 */
public class CircleTransformation implements Transformation {
    private final int borderColor;
    private final float borderWidth;

    /**
     * Creates a new CircleTransformation without a border.
     */
    public CircleTransformation() {
        this(0, 0);
    }

    /**
     * Creates a new CircleTransformation with a border.
     *
     * @param borderColor The border color
     * @param borderWidth The border width in pixels
     */
    public CircleTransformation(int borderColor, float borderWidth) {
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source == null) {
            return null;
        }

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // Create a square bitmap from the source
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);

        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float radius = size / 2f;
        float centerX = size / 2f;
        float centerY = size / 2f;

        // Draw the circular bitmap
        canvas.drawCircle(centerX, centerY, radius - borderWidth, paint);

        // Draw the border if needed
        if (borderWidth > 0) {
            Paint borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setColor(borderColor);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setAntiAlias(true);
            canvas.drawCircle(centerX, centerY, radius - borderWidth / 2, borderPaint);
        }

        squaredBitmap.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return "circle" + (borderWidth > 0 ? "_border_" + borderColor + "_" + borderWidth : "");
    }
}

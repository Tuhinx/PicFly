package com.github.picfly.transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Transformation that rounds the corners of images.
 */
public class RoundedCornersTransformation implements Transformation {
    private final float radius;
    private final int margin;
    private final CornerType cornerType;

    /**
     * Corner types for the transformation.
     */
    public enum CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT,
        OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    /**
     * Creates a new RoundedCornersTransformation with the specified radius for all corners.
     *
     * @param radius The corner radius in pixels
     */
    public RoundedCornersTransformation(float radius) {
        this(radius, 0, CornerType.ALL);
    }

    /**
     * Creates a new RoundedCornersTransformation with the specified radius and margin for all corners.
     *
     * @param radius The corner radius in pixels
     * @param margin The margin around the corners in pixels
     */
    public RoundedCornersTransformation(float radius, int margin) {
        this(radius, margin, CornerType.ALL);
    }

    /**
     * Creates a new RoundedCornersTransformation with the specified radius, margin, and corner type.
     *
     * @param radius     The corner radius in pixels
     * @param margin     The margin around the corners in pixels
     * @param cornerType The corner type
     */
    public RoundedCornersTransformation(float radius, int margin, CornerType cornerType) {
        this.radius = radius;
        this.margin = margin;
        this.cornerType = cornerType;
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
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        drawRoundRect(canvas, paint, width, height);

        source.recycle();

        return bitmap;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - margin;
        float bottom = height - margin;

        switch (cornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
            case TOP_LEFT:
                drawTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case TOP_RIGHT:
                drawTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_LEFT:
                drawBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_RIGHT:
                drawBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case TOP:
                drawTopRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            case LEFT:
                drawLeftRoundRect(canvas, paint, right, bottom);
                break;
            case RIGHT:
                drawRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_LEFT:
                drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_RIGHT:
                drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_LEFT:
                drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_RIGHT:
                drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + 2 * radius, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, margin + radius, bottom), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - 2 * radius, margin, right, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, margin + radius, right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - 2 * radius, margin + 2 * radius, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, margin + radius, bottom - radius), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - 2 * radius, bottom - 2 * radius, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right, bottom - radius), paint);
        canvas.drawRect(new RectF(margin, bottom - radius, right - radius, bottom), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right, bottom), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - 2 * radius, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right, bottom - radius), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + 2 * radius, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - 2 * radius, margin, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - 2 * radius, right, bottom),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(right - 2 * radius, margin, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + 2 * radius, bottom),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(margin, bottom - 2 * radius, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom - radius), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(right - 2 * radius, margin, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right - radius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(margin, margin, margin + 2 * radius, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + 2 * radius, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(right - 2 * radius, bottom - 2 * radius, right, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right - radius, bottom), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom - radius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - 2 * radius, margin, right, margin + 2 * radius),
                radius, radius, paint);
        canvas.drawRoundRect(new RectF(margin, bottom - 2 * radius, margin + 2 * radius, bottom),
                radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
        canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
    }

    @Override
    public String key() {
        return "rounded_" + radius + "_" + margin + "_" + cornerType.name();
    }
}

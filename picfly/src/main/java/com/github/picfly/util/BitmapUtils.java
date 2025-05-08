package com.github.picfly.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Utility class for bitmap operations in PicFly.
 */
public class BitmapUtils {
    private static final String TAG = "PicFly-BitmapUtils";

    /**
     * Converts an InputStream to a Bitmap.
     *
     * @param inputStream The input stream containing image data
     * @return The decoded Bitmap or null if decoding fails
     */
    public static Bitmap decodeStream(InputStream inputStream) {
        if (inputStream == null) {
            Log.e(TAG, "Cannot decode bitmap from null input stream");
            return null;
        }

        try {
            Log.d(TAG, "Decoding bitmap from input stream");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap == null) {
                Log.e(TAG, "BitmapFactory.decodeStream returned null");
            } else {
                Log.d(TAG, "Bitmap decoded successfully: " + bitmap.getWidth() + "x" + bitmap.getHeight());
            }

            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error decoding bitmap from stream", e);
            return null;
        }
    }

    /**
     * Converts a Bitmap to a Drawable.
     *
     * @param context The context
     * @param bitmap  The bitmap to convert
     * @return The drawable created from the bitmap
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Calculates the byte size of a bitmap.
     *
     * @param bitmap The bitmap to calculate size for
     * @return The size in bytes
     */
    public static int getBitmapByteSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }

        return bitmap.getAllocationByteCount();
    }

    /**
     * Compresses a bitmap to a byte array.
     *
     * @param bitmap  The bitmap to compress
     * @param quality The compression quality (0-100)
     * @return The compressed bitmap as a byte array
     */
    public static byte[] compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}

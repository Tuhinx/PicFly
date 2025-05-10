package com.github.picfly;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.github.picfly.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.picfly.recyclerview.ViewTargetAdapter;
import com.github.picfly.target.ImageViewTarget;
import com.github.picfly.target.Target;
import com.github.picfly.transformation.BlurTransformation;
import com.github.picfly.transformation.BrightnessTransformation;
import com.github.picfly.transformation.CircleTransformation;
import com.github.picfly.transformation.ColorFilterTransformation;
import com.github.picfly.transformation.GrayscaleTransformation;
import com.github.picfly.transformation.RotateTransformation;
import com.github.picfly.transformation.RoundedCornersTransformation;
import com.github.picfly.transformation.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for configuring image load requests.
 */
public class RequestBuilder {
    private final PicFly picFly;
    private final Context context;
    private final ImageLoader imageLoader;

    private String url;
    private Drawable placeholderDrawable;
    private Drawable errorDrawable;
    private final List<Transformation> transformations = new ArrayList<>();
    private int width;
    private int height;

    /**
     * Creates a new RequestBuilder.
     *
     * @param picFly      The PicFly instance
     * @param context     The context
     * @param imageLoader The image loader to use
     */
    RequestBuilder(PicFly picFly, Context context, ImageLoader imageLoader) {
        this.picFly = picFly;
        this.context = context;
        this.imageLoader = imageLoader;
    }

    /**
     * Sets the URL to load the image from.
     *
     * @param url The URL
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder load(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the placeholder drawable to display while loading.
     *
     * @param placeholderDrawable The placeholder drawable
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder placeholder(Drawable placeholderDrawable) {
        this.placeholderDrawable = placeholderDrawable;
        return this;
    }

    /**
     * Sets the placeholder drawable resource to display while loading.
     *
     * @param placeholderResId The placeholder drawable resource ID
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder placeholder(@DrawableRes int placeholderResId) {
        this.placeholderDrawable = ContextCompat.getDrawable(context, placeholderResId);
        return this;
    }

    /**
     * Sets the error drawable to display if loading fails.
     *
     * @param errorDrawable The error drawable
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder error(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
        return this;
    }

    /**
     * Sets the error drawable resource to display if loading fails.
     *
     * @param errorResId The error drawable resource ID
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder error(@DrawableRes int errorResId) {
        this.errorDrawable = ContextCompat.getDrawable(context, errorResId);
        return this;
    }

    /**
     * Loads the image into the specified ImageView.
     *
     * @param imageView The ImageView to load the image into
     */
    public void into(ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("ImageView cannot be null");
        }

        into(new ImageViewTarget(imageView));
    }

    /**
     * Applies a transformation to the image.
     *
     * @param transformation The transformation to apply
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder transform(Transformation transformation) {
        if (transformation != null) {
            this.transformations.add(transformation);
        }
        return this;
    }

    /**
     * Applies a blur transformation to the image.
     *
     * @param radius The blur radius (0-25)
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder blur(float radius) {
        return transform(new BlurTransformation(context, radius));
    }

    /**
     * Applies a blur transformation to the image with the default radius.
     *
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder blur() {
        return transform(new BlurTransformation(context));
    }

    /**
     * Applies a grayscale transformation to the image.
     *
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder grayscale() {
        return transform(new GrayscaleTransformation());
    }

    /**
     * Applies a rotation transformation to the image.
     *
     * @param degrees The rotation angle in degrees
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder rotate(float degrees) {
        return transform(new RotateTransformation(degrees));
    }

    /**
     * Applies a circle crop transformation to the image.
     *
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder circleCrop() {
        return transform(new CircleTransformation());
    }

    /**
     * Applies a circle crop transformation with a border to the image.
     *
     * @param borderColor The border color
     * @param borderWidth The border width in pixels
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder circleCrop(int borderColor, float borderWidth) {
        return transform(new CircleTransformation(borderColor, borderWidth));
    }

    /**
     * Applies a rounded corners transformation to the image.
     *
     * @param radius The corner radius in pixels
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder roundedCorners(float radius) {
        return transform(new RoundedCornersTransformation(radius));
    }

    /**
     * Applies a rounded corners transformation to the image with a margin.
     *
     * @param radius The corner radius in pixels
     * @param margin The margin around the corners in pixels
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder roundedCorners(float radius, int margin) {
        return transform(new RoundedCornersTransformation(radius, margin));
    }

    /**
     * Applies a rounded corners transformation to specific corners of the image.
     *
     * @param radius     The corner radius in pixels
     * @param margin     The margin around the corners in pixels
     * @param cornerType The corner type
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder roundedCorners(float radius, int margin, RoundedCornersTransformation.CornerType cornerType) {
        return transform(new RoundedCornersTransformation(radius, margin, cornerType));
    }

    /**
     * Applies a color filter transformation to the image.
     *
     * @param color The color to apply as a filter
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder colorFilter(int color) {
        return transform(new ColorFilterTransformation(color));
    }

    /**
     * Applies a brightness transformation to the image.
     *
     * @param brightness The brightness adjustment value (-1.0f to 1.0f)
     *                   where -1.0f is completely dark and 1.0f is completely
     *                   bright
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder brightness(float brightness) {
        return transform(new BrightnessTransformation(brightness));
    }

    /**
     * Resizes the image to the specified dimensions.
     *
     * @param width  The target width
     * @param height The target height
     * @return This RequestBuilder for chaining
     */
    public RequestBuilder resize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Loads the image into the specified target.
     *
     * @param target The target to load the image into
     */
    public void into(Target target) {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }

        imageLoader.loadImage(url, target, placeholderDrawable, errorDrawable,
                transformations, width, height);
    }

    /**
     * Loads the image into a view in a RecyclerView.ViewHolder.
     *
     * @param view       The view to load the image into
     * @param viewHolder The ViewHolder containing the view
     */
    public void into(ImageView view, RecyclerView.ViewHolder viewHolder) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null");
        }
        if (viewHolder == null) {
            throw new IllegalArgumentException("ViewHolder cannot be null");
        }

        ViewTargetAdapter.loadInto(picFly, url, view, viewHolder);
    }

    /**
     * Preloads the image into the cache.
     */
    public void preload() {
        imageLoader.preloadImage(url, transformations, width, height);
    }
}

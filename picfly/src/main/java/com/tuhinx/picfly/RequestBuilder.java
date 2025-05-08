package com.tuhinx.picfly;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tuhinx.picfly.recyclerview.ViewTargetAdapter;
import com.tuhinx.picfly.target.ImageViewTarget;
import com.tuhinx.picfly.target.Target;
import com.tuhinx.picfly.transformation.BlurTransformation;
import com.tuhinx.picfly.transformation.GrayscaleTransformation;
import com.tuhinx.picfly.transformation.Transformation;

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

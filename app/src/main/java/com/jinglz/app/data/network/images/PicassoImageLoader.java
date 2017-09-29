package com.jinglz.app.data.network.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.jinglz.app.utils.image.PicassoCircularTransformation;
import com.jinglz.app.utils.rx.RetryWithDelay;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * class that implements {@link ImageLoader} interface to load
 * arbitrary images to specified views.
 * it needs to define methods in ImageLoader class. and can be used to
 * load images from different sources, with different transformations.
 */
public class PicassoImageLoader implements ImageLoader {

    private final Picasso picasso;
    private final PicassoCircularTransformation transformation;

    /**
     * constructs new PicassoImageLoader with specified context.
     *
     * @param context to use application-specific resources
     */
    public PicassoImageLoader(Context context) {
        picasso = new Picasso.Builder(context).build();
        transformation = new PicassoCircularTransformation();
    }

    /**
     * overridden method to load image from specified url with retry duration.
     *
     * @param url string variable, contains url for loading image
     * @return Observable object of {@link Bitmap}
     */
    @Override
    public Observable<Bitmap> getBitmapObservable(@NonNull String url) {
        return Observable.fromCallable(() -> picasso.load(url).get())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(5, 300));
    }

    /**
     * overridden method with specified url and imageView.
     *
     * @param url url to load image data
     * @param imageView to display an arbitrary image from specified {@code url}
     */
    @Override
    public void displayImage(@NonNull String url, @NonNull ImageView imageView) {
        picasso.load(url).into(imageView);
    }

    /**
     * overridden method with specified url and imageView. an arbitrary image will
     * be loaded using {@see picasso} with {@code transformation} to display circular image
     * in image view
     *
     * @param url to load image data
     * @param imageView to display an arbitrary image from specified {@code url}
     */
    @Override
    public void displayCircularImage(@NonNull String url, @NonNull ImageView imageView) {
        picasso.load(url).transform(transformation).into(imageView);
    }

    /**
     * overridden method with specified url, imageView. an arbitrary image will
     * be loaded using {@see picasso} with {@code transformation} to display circular image
     * in image view. it provides image with specified {@code sizeRes}.
     * {@code placeholder} is used to display thumbnail if error occurs to load image or
     * until the image loads.
     *
     * @param url to load image data
     * @param imageView to display an arbitrary image from specified {@code url}
     * @param sizeRes defines fixed size for image
     * @param placeholder works as a thumbnail until the image loads
     */
    @Override
    public void displayCircularImage(@NonNull String url,
                                     @NonNull ImageView imageView,
                                     @DimenRes int sizeRes,
                                     @DrawableRes int placeholder) {
        int size = imageView.getResources().getDimensionPixelSize(sizeRes);
        picasso.load(url)
                .transform(transformation)
                .resize(size, size)
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView);
    }

    /**
     * overridden method provides image of specified size.
     * image will be fetched from {@code uri} and displayed in {@code imageView}.
     *
     * @param uri to load image
     * @param imageView to display an arbitrary image from specified {@code uri}
     * @param size defines fixed size for image
     */
    @Override
    public void displayCircularImage(@NonNull Uri uri, @NonNull ImageView imageView, int size) {
        picasso.load(uri).transform(transformation).into(imageView);
    }

    /**
     * clear the picasso cache.
     */
    @Override
    public void invalidateCache() {
        PicassoTools.clearCache(picasso);
    }
}

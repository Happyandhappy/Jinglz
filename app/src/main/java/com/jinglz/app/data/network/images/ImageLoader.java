package com.jinglz.app.data.network.images;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import rx.Observable;

/**
 * Interface to display Images from different sources.
 * used in different classes for displaying images in specified image view.
 * image loading functionality of the class is handled by the class implementing
 * <i>ImageLoader</i> interface.
 *
 */
public interface ImageLoader {

    Observable<Bitmap> getBitmapObservable(@NonNull String url);

    /**
     * method with specified url and image view.
     *
     * @param url url to load image data
     * @param imageView to display an arbitrary image from specified {@code url}
     */
    void displayImage(@NonNull String url, @NonNull ImageView imageView);

    /**
     * method with specified url and image view. defining this method with transformation
     * will display image in circular form.
     *
     * @param url to load image data
     * @param imageView to display an arbitrary image from specified {@code url}
     */
    void displayCircularImage(@NonNull String url, @NonNull ImageView imageView);

    /**
     * defining this method provides image of particular size.
     * image will be fetched from string url and displayed.
     *
     * @param url to load image data
     * @param imageView to display an arbitrary image from specified {@code url}
     * @param size defines fixed size for image
     * @param placeholder works as a thumbnail until the image loads
     */
    void displayCircularImage(@NonNull String url,
                              @NonNull ImageView imageView,
                              @DimenRes int size,
                              @DrawableRes int placeholder);

    /**
     * defining this method provides image of particular size.
     * image will be fetched from uri and displayed.
     *
     * @param uri to load image
     * @param imageView to display an arbitrary image from specified {@code uri}
     * @param size defines fixed size for image
     */
    void displayCircularImage(@NonNull Uri uri, @NonNull ImageView imageView, int size);

    /**
     * to clear in memory cache
     */
    void invalidateCache();
}

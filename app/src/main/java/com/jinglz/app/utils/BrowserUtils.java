package com.jinglz.app.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

public final class BrowserUtils {

    private static final String TAG = "BrowserUtils";

    /**
     * Sole constructor
     */
    private BrowserUtils() {
    }

    /**
     * method with specified context and url to launch custom tabs activity.
     *
     * @param context The source Context
     * @param url The URL to load in the Custom Tab
     */
    public static void openLink(Context context, @Nullable String url) {
        Log.d(TAG, "openLink: " + url);
        if (url != null) {
            final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            final CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }
}

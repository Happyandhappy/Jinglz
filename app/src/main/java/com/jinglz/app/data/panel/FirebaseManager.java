package com.jinglz.app.data.panel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.data.network.models.log.Loggable;
import com.jinglz.app.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Liuwei on 5/13/2017.
 */

@Singleton
public class FirebaseManager {

    private final FirebaseAnalytics mFirebaseAnalytics;
    private final Gson mGson;

    public FirebaseManager(FirebaseAnalytics firebaseAnalytics, Gson gson) {
        mFirebaseAnalytics = firebaseAnalytics;
        mGson = gson;
    }

    public void trackEvent(Event event) {
        trackEvent(event, null);
    }

    public void trackEvent(Event event, @Nullable Loggable loggable)
    {
        Bundle bundle =  loggable != null ? JsonUtils.jsonStringToBundle(mGson.toJson(loggable)) : null;

        //event names must consist of letters, digits or _(underscores)
        String eventname = event.toString();
        eventname = eventname.toLowerCase().replace(" ", "_");
        mFirebaseAnalytics.logEvent(eventname, bundle);
    }
}

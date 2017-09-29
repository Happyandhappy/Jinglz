package com.jinglz.app.data.panel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.data.network.models.log.Loggable;
import com.jinglz.app.utils.JsonUtils;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

public class MixpanelManager {

    private final MixpanelAPI mMixpanelAPI;
    private final Gson mGson;
    private final String mSenderId;

    public MixpanelManager(MixpanelAPI mixpanelAPI, Gson gson, String senderId) {
        mMixpanelAPI = mixpanelAPI;
        mGson = gson;
        mSenderId = senderId;
    }

    /**
     * method with specified userId and loggable information.
     * identify userId by calling {@link #identify(String)}.
     * retrieves people by using {@link MixpanelAPI#getPeople()}
     * parse data from Json and set {@code userJson} to {@code people}
     *
     * @param userId String value contains user id
     * @param loggable Loggable instance
     */
    public void trackUser(String userId, @NonNull Loggable loggable) {
        identify(userId);
        final MixpanelAPI.People people = mMixpanelAPI.getPeople();
        final JSONObject userJson = JsonUtils.parseSave(mGson.toJson(loggable));
        people.set(userJson);
    }

    public void trackEvent(Event event) {
        trackEvent(event, null);
    }

    /**
     * method with specified event and loggable information.
     * parse data from Json and set {@code loggableJson},
     * return null if {@code loggable} is null, parsed data otherwise
     * and track event by calling {@link MixpanelManager#trackEvent(Event, Loggable)}
     *
     * @param event enum for passing event which to perform
     * @param loggable Loggable instance
     */
    public void trackEvent(Event event, @Nullable Loggable loggable) {
        final JSONObject loggableJson = loggable != null ? JsonUtils.parseSave(mGson.toJson(loggable)) : null;
        mMixpanelAPI.track(event.toString(), loggableJson);
    }

    /**
     * method with specified userId. retrieve people by calling {@code MixpanelManager#getPeople()}
     * then it identify with userId by calling {@link MixpanelManager#identify(String)} and
     * push data using {@link #initPushHandling()}
     *
     * @param userId
     */
    private void identify(String userId) {
        final MixpanelAPI.People people = mMixpanelAPI.getPeople();
        mMixpanelAPI.identify(userId);
        people.identify(userId);
        initPushHandling();
    }

    private void initPushHandling() {
        mMixpanelAPI.getPeople().initPushHandling(mSenderId);
    }
}

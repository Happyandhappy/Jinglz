package com.jinglz.app.business.analytics;

import com.jinglz.app.data.network.models.log.Loggable;
import com.jinglz.app.data.panel.CrashlyticsManager;
import com.jinglz.app.data.panel.FirebaseManager;
import com.jinglz.app.data.panel.MixpanelManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalyticsFacade {

    private final MixpanelManager mMixpanelManager;
    private final CrashlyticsManager mCrashlyticsManager;
    private final FirebaseManager mFirebaseManager;
    /**
     * constructor new AnalyticsFacade with specified mMixpanelManager
     * and mCrashlyticsManager
     *
     * @param mixpanelManager parameter to initialize mMixpanelManager to track event
     * @param crashlyticsManager parameter to initialize CrashlyticsManager to log exceptions
     */
    @Inject
    public AnalyticsFacade(final MixpanelManager mixpanelManager,
                           final CrashlyticsManager crashlyticsManager,
                           final FirebaseManager firebaseManager) {
        mMixpanelManager = mixpanelManager;
        mCrashlyticsManager = crashlyticsManager;
        mFirebaseManager = firebaseManager;
    }


    /**
     * Provides value to track the triggered event
     *
     * @param event assign event to trigger for mMixpanelManager
     */
    public void trackEvent(Event event)
    {
        trackEvent(event, null);
    }

    /**
     * Provides value to track the triggered event with information to be logged
     *
     * @param event assign event to trigger for mMixpanelManager
     * @param loggable Loggable variable to be logged
     */
    public void trackEvent(Event event, Loggable loggable)
    {
        if(event.isLogMixpanel())
            mMixpanelManager.trackEvent(event, loggable);

        if(event.isLogFirebase())
            mFirebaseManager.trackEvent(event, loggable);
    }


    /**
     * Register logException of type Throwable
     *
     * @param logException Throwable variable for logging exception
     */
    public void logException(Throwable logException) {
        mCrashlyticsManager.logException(logException);
    }
}

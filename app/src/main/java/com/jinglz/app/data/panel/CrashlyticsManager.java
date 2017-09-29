package com.jinglz.app.data.panel;

import com.crashlytics.android.Crashlytics;
import com.jinglz.app.data.network.models.user.UserResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CrashlyticsManager {

    @Inject
    public CrashlyticsManager() {
    }

    /**
     * Registers the userResponse for setting userIdentifier and userEmail
     *
     * @param userResponse for fetching id and email
     */
    public void trackUser(UserResponse userResponse) {
        Crashlytics.setUserIdentifier(userResponse.id());
        Crashlytics.setUserEmail(userResponse.email());
    }

    public void releaseUser() {
        Crashlytics.setUserIdentifier(null);
        Crashlytics.setUserEmail(null);
    }

    public void logException(Throwable throwable) {
        Crashlytics.logException(throwable);
    }
}

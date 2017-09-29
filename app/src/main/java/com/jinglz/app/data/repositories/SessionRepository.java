package com.jinglz.app.data.repositories;

import com.jinglz.app.App;
import com.jinglz.app.Constants;
import com.jinglz.app.data.local.PreferencesHelper;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

@PerSession
public class SessionRepository {

    private final Api mApi;
    private final PreferencesHelper mPreferences;
    private final ImageLoader mImageLoader;

    /**
     * Constructs new SessionRepository with specified api, preferences and image loader.
     * basically it is used to keep track of user sessions details.
     *
     * @param api Api to retrieve current user
     * @param preferences SharedPreference to deal with user and sessions
     * @param imageLoader ImageLoader for creating and clearing caches.
     */
    @Inject
    public SessionRepository(Api api, PreferencesHelper preferences, ImageLoader imageLoader) {
        mApi = api;
        mPreferences = preferences;
        mImageLoader = imageLoader;
    }

    /**
     * This method is used to retrieve the current session details. first user details will be
     * fetched from sharedPreference the current session will be loaded using {@link #getCurrentSession()}
     * if {@code userResponse} is neither null nor expired. for expiration it uses
     * {@link #isCacheLifetimeExpired(Date)}.
     *
     * @return Observable object of type UserResponse
     */
    public Observable<UserResponse> getCurrentSession() {
        return Single.just(mPreferences.getUserData())
                .flatMapObservable(userResponse -> {
                    if (userResponse != null
                            && !isCacheLifetimeExpired(DateUtils.fromServerFormat(userResponse.lastActivityDate()))) {
                        return Observable.just(userResponse);
                    } else {
                        return Single.merge(Single.just(userResponse), loadCurrentSession());
                    }
                })
                .distinctUntilChanged();
    }

    /**
     * This method is used to test if session is existed or not.
     * to check it use sharedPreference and calls {@link PreferencesHelper#isSessionExist()}.
     *
     * @return Boolean variable true if exist, false otherwise.
     */
    public Observable<Boolean> isSessionExist() {
        return mPreferences.isSessionExist();
    }

    /**
     * This method is used to retrieve details of cached session. From {@link PreferencesHelper#getUserData()}
     * itt return user information in {@link UserResponse}.
     *
     * @return UserResponse instance
     */
    public Single<UserResponse> getCachedSession() {
        return Single.just(mPreferences.getUserData());
    }

    /**
     *
     * @return
     */
    public UserResponse getUser() {
        return mPreferences.getUserData();
    }

    public void logout() {
        mPreferences.clear();
        App.get().releaseSessionComponent();
    }

    public Single<UserResponse> loadCurrentSession() {
        return mApi.currentUser()
                .map(DataResponse::data)
                .doOnSuccess(mPreferences::setUserData)
                .doOnSuccess(response -> mImageLoader.invalidateCache());
    }

    /**
     *
     * @param lastActivityDate
     * @return
     */
    private boolean isCacheLifetimeExpired(Date lastActivityDate) {
        if (lastActivityDate == null) {
            return true;
        }
        final long delta = Calendar.getInstance().getTimeInMillis() - lastActivityDate.getTime();
        return delta >= TimeUnit.HOURS.toMillis(Constants.SESSION_CACHE_LIFETIME_HOURS);
    }
}

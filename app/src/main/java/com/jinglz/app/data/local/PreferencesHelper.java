package com.jinglz.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jinglz.app.Constants;
import com.jinglz.app.data.network.models.coin.CoinsHistoryResponse;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.data.network.models.user.UserResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Emitter;
import rx.Observable;

@Singleton
public final class PreferencesHelper {

    private static final String KEY_AUTH_TOKEN = Constants.PACKAGE_NAME + ".AUTH_TOKEN";
    private static final String KEY_USER_DATA = Constants.PACKAGE_NAME + ".USER_DATA";
    private static final String KEY_COINS_HISTORY = Constants.PACKAGE_NAME + ".COINS_HISTORY";
    private static final String KEY_COINS = Constants.PACKAGE_NAME + ".COINS";

    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    /**
     * construct new PreferencesHelper with specified context and gson
     *
     * @param context for access application-specific data
     * @param gson for JSON parsing using gson
     */
    @Inject
    public PreferencesHelper(Context context, Gson gson) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mGson = gson;
    }

    /**
     * method used to retrieve Authentication token from {@see mSharedPreferences}
     * by using {@see KEY_AUTH_TOKEN}
     *
     * @return String variable contains authentication token
     */
    @Nullable
    public String getAuthToken() {
        return mSharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * method with specified token, used to update Authentication {@code token} in {@see mSharedPreferences}
     * by using {@see KEY_AUTH_TOKEN}
     *
     * @param token String variable contains token to update
     */
    public void setAuthToken(@Nullable String token) {
        mSharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    /**
     * method is used to retrieve {@code userDataJson} from {@see mSharedPreferences} by using
     * {@see KEY_USER_DATA}. then {@code userDataJson} will be used to fetch {@link UserResponse}
     *
     * @return UserResponse if {@code userDataJson} parsed successfully, null otherwise.
     */
    @Nullable
    public UserResponse getUserData() {
        final String userDataJson = mSharedPreferences.getString(KEY_USER_DATA, null);
        return userDataJson == null ? null : mGson.fromJson(userDataJson, UserResponse.class);
    }

    /**
     * method with specified response, to store parsed Json data in {@see mSharedPreferences} using
     * {@see KEY_USER_DATA}. response will be parsed by using {@see mGson}
     *
     * @param response {@link UserResponse} contains detail of particular user
     */
    public void setUserData(@Nullable UserResponse response) {
        mSharedPreferences.edit().putString(KEY_USER_DATA, mGson.toJson(response)).apply();
    }

    /**
     * method is used to retrieve {@code coinHistoryJson} from {@see mSharedPreferences} by using
     * {@see KEY_COINS_HISTORY}. then {@code coinHistoryJson} will be used to
     * fetch {@link CoinsHistoryResponse}
     *
     * @return CoinsHistoryResponse if {@code coinHistoryJson} parsed successfully, null otherwise.
     */
    @Nullable
    public CoinsHistoryResponse getCoinsHistory() {
        final String coinHistoryJson = mSharedPreferences.getString(KEY_COINS_HISTORY, null);
        return coinHistoryJson == null ? null : mGson.fromJson(coinHistoryJson, CoinsHistoryResponse.class);
    }

    /**
     * method with specified response, to store parsed Json data in {@see mSharedPreferences} using
     * {@see KEY_COINS_HISTORY}. response will be parsed by using {@see mGson}
     *
     * @param response {@link CoinsHistoryResponse} contains information of coin history limit
     *                                             that can be all, monthly, weekly and today
     */
    public void setCoinsHistory(CoinsHistoryResponse response) {
        mSharedPreferences.edit().putString(KEY_COINS_HISTORY, mGson.toJson(response)).apply();
    }

    /**
     * method is used to retrieve {@code coinsJson} from {@see mSharedPreferences} by using
     * {@see KEY_COINS}. then {@code coinsJson} will be used to fetch {@link CurrentCoinsResponse}
     *
     * @return CurrentCoinsResponse if {@code coinsJson} parsed successfully, null otherwise.
     */
    public CurrentCoinsResponse getCoins() {
        final String coinsJson = mSharedPreferences.getString(KEY_COINS, null);
        return coinsJson == null ? null : mGson.fromJson(coinsJson, CurrentCoinsResponse.class);
    }

    /**
     * method with specified response, to store parsed Json data in {@see mSharedPreferences} using
     * {@see KEY_COINS}. response will be parsed by using {@see mGson}
     *
     * @param response {@link CurrentCoinsResponse} contains information of coin
     */
    public void setCoins(CurrentCoinsResponse response) {
        mSharedPreferences.edit().putString(KEY_COINS, mGson.toJson(response)).apply();
    }

    /**
     * method used to clear the {@code mSharedPreferences}.
     */
    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * method is used to check if session is exist or not.
     * @return
     */
    public Observable<Boolean> isSessionExist() {
        return Observable.fromEmitter(emitter -> {
            final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener
                    = (sharedPreferences, key) -> {
                if (key.equals(KEY_AUTH_TOKEN)) {
                    final String token = sharedPreferences.getString(key, null);
                    emitter.onNext(token != null);
                }
            };
            mSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
            emitter.setCancellation(() -> mSharedPreferences.unregisterOnSharedPreferenceChangeListener(
                    onSharedPreferenceChangeListener));
        }, Emitter.BackpressureMode.LATEST);
    }
}

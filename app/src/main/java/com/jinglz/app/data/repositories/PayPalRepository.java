package com.jinglz.app.data.repositories;

import android.content.Context;
import android.net.Uri;

import com.jinglz.app.BuildConfig;
import com.jinglz.app.R;
import com.jinglz.app.data.local.PreferencesHelper;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.PayPalCode;
import com.jinglz.app.data.network.models.RedeemRequest;
import com.jinglz.app.data.network.models.RedeemResponse;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.injection.session.PerSession;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import rx.Single;

@PerSession
public class PayPalRepository {

    private final Context mContext;
    private final Api mApi;
    private final PreferencesHelper mPreferencesHelper;

    /**
     * Constructs new PayPalRepository with specified context, api and preferencesHelper.
     * It is used for redeem coins with PayPal.
     *
     * @param context used for application-specified resources
     * @param api Api to deal with user and related coins detail
     * @param preferencesHelper SharedPreference to save userData
     */
    @Inject
    public PayPalRepository(Context context, Api api, PreferencesHelper preferencesHelper) {
        mContext = context;
        mApi = api;
        mPreferencesHelper = preferencesHelper;
    }

    /**
     * This method is used to retrieve configuration for PayPal integration.
     * in this {@link BuildConfig#PAY_PAL_ENVIRONMENT}, {@link BuildConfig#PAY_PAL_CLIENT_ID} will
     * be used to retrieve the configuration.
     *
     * @return PayPalConfiguration instance
     */
    public Single<PayPalConfiguration> getConfiguration() {
        return Single.just(new PayPalConfiguration()
                .environment(BuildConfig.PAY_PAL_ENVIRONMENT)
                .clientId(BuildConfig.PAY_PAL_CLIENT_ID)
                .merchantName(mContext.getString(R.string.app_name))
                .merchantPrivacyPolicyUri(Uri.parse(BuildConfig.HOST + "privacy-policy"))
                .merchantUserAgreementUri(Uri.parse(BuildConfig.HOST + "terms-and-conditions")));
    }

    /**
     * This method is used to retrieve PayPal Authentication scopes.
     * @return PayPalOAuthScopes instance
     */
    public Single<PayPalOAuthScopes> getOAuthScopes() {
        final Set<String> scopes = new HashSet<>(Collections.singletonList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL));
        return Single.just(new PayPalOAuthScopes(scopes));
    }

    /**
     * method with specified code to link with PayPal account.
     * first it verify user by sending {@code code} to {@link Api#verifyUser(PayPalCode)},
     * if succeed it will save user data in {@code mPreferencesHelper} by calling
     * {@link PreferencesHelper#setUserData(UserResponse)}.
     *
     * @param code PayPalCode that contains code using in linking.
     * @return UserResponse instance.
     */
    public Single<UserResponse> linkPayPalAccount(PayPalCode code) {
        return mApi.verifyUser(code)
                .doOnSuccess(mPreferencesHelper::setUserData);
    }

    /**
     * method with specified request to redeem coins. {@link RedeemRequest} contains coins
     * to send request {@link Api#redeemCoins(RedeemRequest)}.
     *
     * @param request RedeemRequest that contains coins to redeemed.
     * @return String variable contains response.
     */
    public Single<String> redeemCoins(RedeemRequest request) {
        return mApi.redeemCoins(request)
                .map(DataResponse::data)
                .map(RedeemResponse::getPaypalEmail);
    }

    /**
     * This method is used to unBind PayPal by hitting api {@link Api#unbindPayPal()}.
     * on success data will be saved in sharedPreference by {@link PreferencesHelper#setUserData(UserResponse)}
     *
     * @return new instance of UserResponse.
     */
    public Single<UserResponse> unlinkPayPal() {
        return mApi.unbindPayPal()
                .doOnSuccess(mPreferencesHelper::setUserData);
    }
}

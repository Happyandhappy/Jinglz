package com.jinglz.app.data.repositories;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.jinglz.app.data.local.PreferencesHelper;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.ForgotPasswordRequest;
import com.jinglz.app.data.network.models.ForgotPasswordResponse;
import com.jinglz.app.data.network.models.PhoneVerificationCodeRequest;
import com.jinglz.app.data.network.models.PhoneVerificationRequest;
import com.jinglz.app.data.network.models.ResetPasswordRequest;
import com.jinglz.app.data.network.models.user.AlreadyRegisterResponse;
import com.jinglz.app.data.network.models.user.SignInRequest;
import com.jinglz.app.data.network.models.user.SignInSocialRequest;
import com.jinglz.app.data.network.models.user.SignUpSocialRequest;
import com.jinglz.app.data.network.models.user.SignUpSocialUser;
import com.jinglz.app.data.network.models.user.UserResponse;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Single;

@Singleton
public class AuthRepository {

    private final PreferencesHelper mPreferencesHelper;
    private final Api mApi;
    private final Context mContext;

    /**
     * Construct new AuthRepository with specified preferencesHelper, api and context.
     *
     * @param preferencesHelper to handle shared preference data
     * @param api to hit apis
     * @param context  to retrieve application-specific resources
     */
    @Inject
    public AuthRepository(PreferencesHelper preferencesHelper, Api api, Context context) {
        mPreferencesHelper = preferencesHelper;
        mApi = api;
        mContext = context;
    }

    /**
     * Returns authentication token.
     *
     * @return String variable contains authentication token
     */
    public String getAuthToken() {
        return mPreferencesHelper.getAuthToken();
    }

    /**
     * method with specified request. Return new instance of UserResponse.
     * hits {@link Api#signIn(SignInRequest)} with specified {@code request}.
     * save data in preference on success, using {@link PreferencesHelper#setUserData(UserResponse)}
     *
     * @param request SignInRequest instance that contains clientId, clientSecret, userName, password
     *                pushToken and platform.
     * @return new instance of UserResponse
     */
    public Single<UserResponse> sendSignInManualRequest(SignInRequest request) {
        return mApi.signIn(request)
                .map(DataResponse::data)
                .doOnSuccess(mPreferencesHelper::setUserData);
    }

    /**
     * method with specified request and provider. Return new instance of UserResponse.
     * hits {@link Api#signInSocial(SignInSocialRequest, String)} with specified {@code request}
     * and {@code provider}. save data in preference on success, using
     * {@link PreferencesHelper#setUserData(UserResponse)}
     *
     * @param request SignInSocialRequest that contains clientId, clientSecret, accessToken,
     *                pushToken and platform
     * @param provider String variable that contains provider name
     * @return new instance of UserResponse
     */
    public Single<UserResponse> sendSocialSignInRequest(SignInSocialRequest request, String provider) {
        return mApi.signInSocial(request, provider)
                .map(DataResponse::data)
                .doOnSuccess(mPreferencesHelper::setUserData);
    }

    /**
     * method with specified request and provider. Return new instance of UserResponse.
     * hits {@link Api#signUpSocial(SignUpSocialRequest, String)} with specified {@code request}
     * and {@code provider}. save data in preference on success, using
     * {@link PreferencesHelper#setUserData(UserResponse)}
     *
     * @param request SignUpSocialRequest that contains clientId, clientSecret,
     *                accessToken and {@link SignUpSocialUser}
     * @param provider String variable that contains provider name
     * @return new instance of UserResponse
     */
    public Single<UserResponse> sendSocialSignUpRequest(SignUpSocialRequest request, String provider) {
        return mApi.signUpSocial(request,provider)
                .map(DataResponse::data)
                .doOnSuccess(mPreferencesHelper::setUserData);
    }

    /**
     * method with specified request. Returns new instance of ForgotPasswordResponse on success, null
     * otherwise.
     * hits {@link Api#forgotPassword(ForgotPasswordRequest)} with specified {@code request}.
     *
     *
     * @param request contains email id of user
     * @return new instance of ForgotPasswordResponse if success, null otherwise
     */
    public Single<ForgotPasswordResponse> sendForgotPasswordRequest(ForgotPasswordRequest request) {
        return mApi.forgotPassword(request)
                .map(response -> response.data().isEmpty() ? null : response.data().get(0));
    }

    /**
     * method with specified token. it is used retrieve user data from facebook.
     * {@code request} will used to retrieve data from
     * {@link GraphRequest#newMeRequest(AccessToken, GraphRequest.GraphJSONObjectCallback)}.
     * after that, new bundle will be attached with request and {@link GraphRequest#executeAndWait()}
     * will be called.
     *
     * @param token Access token, used new request
     * @return new instance of GraphResponse
     */
    public Observable<GraphResponse> requestFacebookUserData(AccessToken token) {
        final GraphRequest request = GraphRequest.newMeRequest(token, null);
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,picture.type(large),gender,birthday");
        request.setParameters(parameters);
        return Observable.just(request.executeAndWait());
    }

    /**
     * method is used to check if facebook is already logged in. it use facebookId as an input parameter.
     * calls api {@link Api#checkFacebookAlreadyRegister(String)} and return {@link AlreadyRegisterResponse}.
     *
     * @param facebookId String variable contains facebook id.
     * @return new instance of AlreadyRegisterResponse
     */
    public Single<AlreadyRegisterResponse> checkFacebookAlreadyRegister(String facebookId) {
        return mApi.checkFacebookAlreadyRegister(facebookId);
    }

    /**
     * method with specified code and email. this method is used for validation process.
     * returns boolean value after validating using {@link Api#validateCode(String, String)}.
     *
     * @param code String variable containing code to be validated
     * @param email String variable containing email to be validated
     * @return boolean variable to check if validated
     */
    public Single<Boolean> validateCode(String code, String email) {
        return mApi.validateCode(code, email)
                .map(DataResponse::data);
    }

    /**
     * method with specified code and phone. this method is used to verify phone number.
     * returns {@link UserResponse} instance after validating using
     * {@link Api#verifyPhone(PhoneVerificationRequest)}. On  success user data will
     * be saved in shared preference using {@link PreferencesHelper#setUserData(UserResponse)}.
     *
     * @param code String variable that contains code to be verified.
     * @param phone String variable that contains phone number to be verified for.
     * @return new instance of UserResponse.
     */
    public Single<UserResponse> verifyPhone(String code, String phone) {
        return mApi.verifyPhone(PhoneVerificationRequest.create(phone, code))
                .doOnSuccess(mPreferencesHelper::setUserData);
    }

    /**
     * This method is used for requesting verification code. it is using {@code email}
     * for requesting code using {@link Api#requestPhoneVerificationCode(PhoneVerificationCodeRequest)}.
     * verification code will be sent to the requested email address.
     *
     * @param email String variable that contains user email.
     * @return new instance of DataResponse
     */
    public Single<DataResponse<String>> requestPhoneVerificationCode(String email) {
        return mApi.requestPhoneVerificationCode(PhoneVerificationCodeRequest.create(email));
    }

    /**
     * method with specified {@code request}, to send request for resetting password.
     * it fetches details from {@link ResetPasswordRequest} and send to api
     * {@link Api#resetPassword(ResetPasswordRequest)}. It returns a boolean value true, if sent successfully.
     *
     * @param request cintains email, password and code to for resetting password.o
     * @return Returns boolean instance
     */
    public Single<Boolean> resetPassword(ResetPasswordRequest request) {
        return mApi.resetPassword(request)
                .map(DataResponse::data);
    }

    /**
     * method with specified account that contains an email for generating Google token.
     *
     * @param account
     * @return String value containing Google token.
     * @throws IOException if input/output error occurred.
     * @throws GoogleAuthException if authentication fails.
     */
    public Single<String> getGoogleToken(String account) {
        try {
            return Single.just(GoogleAuthUtil.getToken(mContext, account, "oauth2:email profile"));
        } catch (IOException | GoogleAuthException e) {
            return Single.error(e);
        }
    }
}

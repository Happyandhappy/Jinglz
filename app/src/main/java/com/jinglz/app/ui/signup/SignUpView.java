package com.jinglz.app.ui.signup;

import android.net.Uri;

import com.jinglz.app.data.network.models.google.GoogleUserData;
import com.jinglz.app.data.network.models.facebook.FacebookUserData;
import com.jinglz.app.ui.base.BaseView;

/**
 * An interface to handle signup Success
 * to start home activity
 * to set various errors of signup process
 * to clean errors
 * to fill facebook and google signin form
 */
public interface SignUpView extends BaseView {

    void startHome();

    void onSignUpSuccess(String phone, String email);

    void setFirstNameError(String error);

    void setLastNameError(String error);

    void setEmailError(String error);

    void setPhoneNumberError(String error);

    void setPasswordError(String error);

    void setConfirmPasswordError(String error);

    void setZipCodeError(String error);

    void setTermsError(String error);

    void setYearOfBirthError(String error);

    void setAvatar(Uri uri);

    void setReferralCode(String code);

    void fillViewFormFacebook(FacebookUserData data);

    void fillViewFormGoogle(GoogleUserData data);

    void cleanErrors();
}

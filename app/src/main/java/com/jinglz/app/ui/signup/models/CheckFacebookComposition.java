package com.jinglz.app.ui.signup.models;

import com.google.auto.value.AutoValue;
import com.jinglz.app.data.network.models.facebook.FacebookUserData;

/**
 * an abstract class to check facebook composition
 * to handle facebook integration actions
 */
@AutoValue
public abstract class CheckFacebookComposition {

    public static CheckFacebookComposition create(FacebookUserData facebookUserData, Boolean facebookAlreadyRegister) {
        return new AutoValue_CheckFacebookComposition(facebookAlreadyRegister, facebookUserData);
    }

    public abstract Boolean facebookAlreadyRegister();

    public abstract FacebookUserData facebookUserData();

}

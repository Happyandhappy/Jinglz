package com.jinglz.app.data.network.models.user;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.jinglz.app.data.network.models.SignUpModel;

@AutoValue
public abstract class SignUpSocialUser {

    /**
     * static method with specified gson, for constructing new AutoValue_SignUpSocialUser.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_SignUpSocialUser.GsonTypeAdapter}
     * @return TypeAdapter object of SignUpSocialUser type
     */
    public static TypeAdapter<SignUpSocialUser> typeAdapter(Gson gson) {
        return new AutoValue_SignUpSocialUser.GsonTypeAdapter(gson);
    }

    /**
     * method with specified model, pushToken, platform and vendorUniqueId to
     * construct new AutoValue_SignUpSocialUser.
     *
     * @param model to retrieve user detail
     * @param pushToken unique token for push notification
     * @param platform specify the platform on which application is going to run
     * @param vendorUniqueId unique id of vendor
     * @return SignUpSocialUser object
     */
    public static SignUpSocialUser create(SignUpModel model, String pushToken, String platform, String vendorUniqueId) {
        return new AutoValue_SignUpSocialUser(model.email(),
                                              model.phoneNumber(),
                                              model.gender() != null ? model.gender().getGender() : null,
                                              model.yearOfBirth(),
                                              model.firstName(),
                                              model.lastName(),
                                              model.referrerCode(),
                                              model.image(),
                                              model.zipCode(),
                                              pushToken,
                                              platform,
                                              vendorUniqueId);
    }

    public abstract String email();

    public abstract String phone();

    @Nullable
    public abstract String gender();

    @Nullable
    public abstract Integer yearOfBirth();

    public abstract String firstName();

    public abstract String lastName();

    @Nullable
    public abstract String referrerCode();

    @Nullable
    public abstract String image();

    public abstract String zipCode();

    @Nullable
    public abstract String pushToken();

    public abstract String platform();

    @Nullable
    public abstract String vendorUniqueId();
}

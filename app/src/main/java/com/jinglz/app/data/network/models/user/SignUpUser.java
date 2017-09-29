package com.jinglz.app.data.network.models.user;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.jinglz.app.ui.signup.models.SignUpModel;

@AutoValue
public abstract class SignUpUser {

    /**
     * static method with specified gson, for constructing new AutoValue_SignUpUser.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_SignUpUser.GsonTypeAdapter}
     * @return TypeAdapter object of SignUpUser type
     */
    public static TypeAdapter<SignUpUser> typeAdapter(Gson gson) {
        return new AutoValue_SignUpUser.GsonTypeAdapter(gson);
    }

    /**
     * method with specified model, pushToken, platform and vendorUniqueId to
     * construct new AutoValue_SignUpUser.
     *
     * @param model to retrieve user detail
     * @param pushToken unique token for push notification
     * @param platform specify the platform on which application is going to run
     * @param vendorUniqueId unique id of vendor
     * @return SignUpUser object
     */
    public static SignUpUser create(SignUpModel model, String pushToken, String platform, String vendorUniqueId) {
        return new AutoValue_SignUpUser(model.getPassword(), model.getEmail(),
                                        model.getPhoneNumber(),
                                        model.getGender() == null ? null : model.getGender().getGender(),
                                        model.getYearOfBirth(),
                                        model.getFirstName(),
                                        model.getLastName(),
                                        model.getReferrerCode(),
                                        model.getImage(),
                                        model.getZipCode(),
                                        pushToken,
                                        platform,
                                        vendorUniqueId);
    }

    public abstract String password();

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

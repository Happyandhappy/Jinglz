package com.jinglz.app.data.network.models.google;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GoogleUserData {

    @Nullable
    public abstract String firstName();

    @Nullable
    public abstract String lastName();

    @Nullable
    public abstract String avatar();

    public abstract String token();

    public abstract String email();

    /**
     * static method with specified firstName, lastName, avatar, token and email, to construct
     * new AutoValue_GoogleUserData
     *
     * @param firstName String variable containing first name of user
     * @param lastName String variable containing last name of user
     * @param avatar String variable containing avatar detail
     * @param token String variable containing unique token
     * @param email String variable containing email of user
     * @return GoogleUserData object
     */
    public static GoogleUserData create(String firstName, String lastName, String avatar, String token, String email) {
        return new AutoValue_GoogleUserData(firstName, lastName, avatar, token, email);
    }
}

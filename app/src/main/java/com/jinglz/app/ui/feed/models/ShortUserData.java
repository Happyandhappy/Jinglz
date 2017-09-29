package com.jinglz.app.ui.feed.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ShortUserData {

    /**
     * It is used to construct new AutoValue_ShortUserData with specified email, name and avatarUrl.
     *
     * @param email
     * @param firstName
     * @param lastName
     * @param avatarUrl
     * @return AutoValue_ShortUserData instance
     */
    public static ShortUserData create(String email, String firstName, String lastName, String avatarUrl) {
        return new AutoValue_ShortUserData(email, firstName + " " + lastName, avatarUrl);
    }

    public abstract String email();

    public abstract String name();

    @Nullable
    public abstract String avatarUrl();

}

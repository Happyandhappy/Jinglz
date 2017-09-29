package com.jinglz.app.data.network.models.user;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.data.network.models.TutorialResponse;

import javax.annotation.Nullable;

@AutoValue
public abstract class UserResponse {

    @Nullable
    @SerializedName("gender")
    public abstract String gender();

    @SerializedName("email")
    public abstract String email();

    @SerializedName("lastName")
    public abstract String lastName();

    @SerializedName("firstName")
    public abstract String firstName();

    @Nullable
    @SerializedName("zipCode")
    public abstract String zipCode();

    @Nullable
    @SerializedName("phone")
    public abstract String phone();

    @Nullable
    @SerializedName("yearOfBirth")
    public abstract Integer yearOfBirth();

    @SerializedName("_id")
    public abstract String id();

    @Nullable
    @SerializedName("image")
    public abstract String image();

    @Nullable
    @SerializedName("referrerId")
    public abstract String referrerId();

    @Nullable
    @SerializedName("inviteCode")
    public abstract String inviteCode();

    @SerializedName("v1")
    public abstract boolean isPhoneVerified();

    @SerializedName("v2")
    public abstract boolean isPayPalLinked();

    @SerializedName("v3")
    public abstract boolean isTaxId();

    @SerializedName("tutorial")
    public abstract TutorialResponse tutorial();

    @SerializedName("lastActivityDate")
    public abstract String lastActivityDate();

    @SerializedName("v3sent")
    public abstract boolean isTaxIdRequestSend();

    /**
     * static method with specified gson, for constructing new AutoValue_UserResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_UserResponse.GsonTypeAdapter}
     * @return TypeAdapter object of UserResponse type
     */
    public static TypeAdapter<UserResponse> typeAdapter(Gson gson) {
        return new AutoValue_UserResponse.GsonTypeAdapter(gson);
    }
}

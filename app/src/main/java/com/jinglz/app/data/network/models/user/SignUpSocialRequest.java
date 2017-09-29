package com.jinglz.app.data.network.models.user;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class SignUpSocialRequest {

    /**
     * static method with specified gson, for constructing new AutoValue_SignUpSocialRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_SignUpSocialRequest.GsonTypeAdapter}
     * @return TypeAdapter object of SignUpSocialRequest type
     */
    public static TypeAdapter<SignUpSocialRequest> typeAdapter(Gson gson) {
        return new AutoValue_SignUpSocialRequest.GsonTypeAdapter(gson);
    }

    /**
     * static method with specified clientId, clientSecret, accessToken and user to construct new
     * AutoValue_SignUpSocialRequest
     *
     * @param clientId     that is used for unique id of the client
     * @param clientSecret that contains unique client secret
     * @param accessToken  contains unique access token
     * @param user contains details of user for social signUp
     * @return SignUpSocialRequest object
     */
    public static SignUpSocialRequest create(String clientId,
                                             String clientSecret,
                                             String accessToken,
                                             SignUpSocialUser user) {

        return new AutoValue_SignUpSocialRequest(clientId, clientSecret, accessToken, user);
    }

    @SerializedName("client_id")
    public abstract String clientId();

    @SerializedName("client_secret")
    public abstract String clientSecret();

    @Nullable
    @SerializedName("access_token")
    public abstract String accessToken();

    @SerializedName("user")
    public abstract SignUpSocialUser user();
}

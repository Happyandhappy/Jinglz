package com.jinglz.app.data.network.models.user;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class SignInSocialRequest {

    /**
     * static method with specified clientId, clientSecret, accessToken,
     * pushToken and platform, to construct new AutoValue_SignInSocialRequest.
     *
     * @param clientId     that is used for unique id of the client
     * @param clientSecret that contains unique client secret
     * @param accessToken  contains unique access token
     * @param pushToken    unique token generated to push notifications
     * @param platform     specify the platform on which application is going to run
     * @return
     */
    public static SignInSocialRequest create(String clientId,
                                             String clientSecret,
                                             String accessToken,
                                             @Nullable String pushToken,
                                             String platform) {
        return new AutoValue_SignInSocialRequest(clientId, clientSecret, accessToken, pushToken, platform);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_SignInSocialRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_SignInSocialRequest.GsonTypeAdapter}
     * @return TypeAdapter object of SignInSocialRequest type
     */
    public static TypeAdapter<SignInSocialRequest> typeAdapter(Gson gson) {
        return new AutoValue_SignInSocialRequest.GsonTypeAdapter(gson);
    }

    @SerializedName("client_id")
    public abstract String clientId();

    @SerializedName("client_secret")
    public abstract String clientSecret();

    @SerializedName("access_token")
    public abstract String accessToken();

    @Nullable
    @SerializedName("pushToken")
    public abstract String pushToken();

    @SerializedName("platform")
    public abstract String platform();
}

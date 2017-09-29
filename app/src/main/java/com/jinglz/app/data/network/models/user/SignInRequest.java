package com.jinglz.app.data.network.models.user;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class SignInRequest {

    /**
     * method with specified clientId, clientSecret, userName, password,
     * pushToken and platform to consturct new AutoValue_SignInRequest.
     *
     * @param clientId that is used for unique id of the client
     * @param clientSecret that contains unique client secret
     * @param userName contains name of the user
     * @param password contains password of the user
     * @param pushToken unique token generated to push notifications
     * @param platform specify the platform on which application is going to run
     * @return
     */
    public static SignInRequest create(String clientId,
                                       String clientSecret,
                                       String userName,
                                       String password,
                                       @Nullable String pushToken,
                                       String platform) {
        return new AutoValue_SignInRequest(clientId, clientSecret, userName.toLowerCase(),
                                           password, pushToken, platform);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_SignInRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_SignInRequest.GsonTypeAdapter}
     * @return TypeAdapter object of SignInRequest type
     */
    public static TypeAdapter<SignInRequest> typeAdapter(Gson gson) {
        return new AutoValue_SignInRequest.GsonTypeAdapter(gson);
    }

    @SerializedName("client_id")
    public abstract String clientId();

    @SerializedName("client_secret")
    public abstract String clientSecret();

    @SerializedName("username")
    public abstract String userName();

    @SerializedName("password")
    public abstract String password();

    @Nullable
    @SerializedName("pushToken")
    public abstract String pushToken();

    @SerializedName("platform")
    public abstract String platform();
}

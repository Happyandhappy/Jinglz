package com.jinglz.app.data.network.models.user;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class SignUpRequest {

    /**
     * static method with specified gson, for constructing new AutoValue_SignUpRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_SignUpRequest.GsonTypeAdapter}
     * @return TypeAdapter object of SignUpRequest type
     */
    public static TypeAdapter<SignUpRequest> typeAdapter(Gson gson) {
        return new AutoValue_SignUpRequest.GsonTypeAdapter(gson);
    }

    /**
     * static method with specified clientId, clientSecret and user to new AutoValue_SignUpRequest
     *
     * @param clientId     that is used for unique id of the client
     * @param clientSecret that contains unique client secret
     * @param user that contains user details
     * @return SignUpRequest object
     */
    public static SignUpRequest create(String clientId, String clientSecret, SignUpUser user) {
        return new AutoValue_SignUpRequest(clientId, clientSecret, user);
    }

    @SerializedName("client_id")
    public abstract String clientId();

    @SerializedName("client_secret")
    public abstract String clientSecret();

    @SerializedName("user")
    public abstract SignUpUser user();
}

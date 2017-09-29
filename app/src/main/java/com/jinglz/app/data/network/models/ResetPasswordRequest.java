package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.data.network.models.user.UserResponse;

@AutoValue
public abstract class ResetPasswordRequest {

    /**
     * returns new ResetPasswordRequest object by constructing
     * new AutoValue_ResetPasswordRequest with specified code, email and password.
     *
     *
     * @param code it holds code to reset password
     * @param email String variable contains email of user
     * @param password String variable contains password of user
     * @return Returns a new instance of ResetPasswordRequest class
     */
    public static ResetPasswordRequest create(String code, String email, String password) {
        return new AutoValue_ResetPasswordRequest(code, email, password);
    }

    /**
     * create new instance of ResetPasswordRequest to pass TypeAdapter. TypeAdapter is used to retrieve
     * response of the ResetPasswordRequest the object is being created in.it uses {@code gson} to parse
     * data from Json.
     *
     * @param gson to parse data from Json
     * @return {@code new AutoValue_ResetPasswordRequest.GsonTypeAdapter(gson)}
     */
    public static TypeAdapter<ResetPasswordRequest> typeAdapter(Gson gson) {
        return new AutoValue_ResetPasswordRequest.GsonTypeAdapter(gson);
    }

    @SerializedName("code")
    public abstract String code();

    @SerializedName("email")
    public abstract String email();

    @SerializedName("password")
    public abstract String password();

}

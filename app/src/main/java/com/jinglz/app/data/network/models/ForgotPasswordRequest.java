package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ForgotPasswordRequest {

    /**
     * method to retrieve a new ForgotPasswordRequest object by constructing
     * new AutoValue_ForgotPasswordRequest with specified email
     *
     * @param email String variable contains email of user
     * @return new ForgotPasswordRequest object
     */
    public static ForgotPasswordRequest create(String email) {
        return new AutoValue_ForgotPasswordRequest(email);
    }

    /**
     * create new instance of ForgotPasswordRequest to pass TypeAdapter. TypeAdapter is used to retrieve
     * response of the ForgotPasswordRequest the object is being created in.it uses {@code gson} to parse
     * data from Json.
     *
     * @param gson to parse data from Json
     * @return {@code new ForgotPasswordRequest.GsonTypeAdapter(gson)}
     */
    public static TypeAdapter<ForgotPasswordRequest> typeAdapter(Gson gson) {
        return new AutoValue_ForgotPasswordRequest.GsonTypeAdapter(gson);
    }

    @SerializedName("email")
    public abstract String email();

}

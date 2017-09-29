package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ForgotPasswordResponse {

    @SerializedName("email")
    public abstract String email();

    /**
     * create new instance of ForgotPasswordResponse to pass TypeAdapter. TypeAdapter is used to retrieve
     * response of the ForgotPasswordResponse the object is being created in.it uses {@code gson} to parse
     * data from Json.
     *
     * @param gson to parse data from Json
     * @return {@code new AutoValue_ForgotPasswordResponse.GsonTypeAdapter(gson)}
     */
    public static TypeAdapter<ForgotPasswordResponse> typeAdapter(Gson gson) {
        return new AutoValue_ForgotPasswordResponse.GsonTypeAdapter(gson);
    }

}

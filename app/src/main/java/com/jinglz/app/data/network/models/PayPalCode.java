package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class PayPalCode {

    @SerializedName("code")
    public abstract String code();

    /**
     * returns new PayPalCode object by constructing new AutoValue_PayPalCode with specified code
     *
     * @param code String variable that contains code to connect paypal
     * @return {@code new AutoValue_PayPalCode(code)}
     */
    public static PayPalCode create(String code) {
        return new AutoValue_PayPalCode(code);
    }

    /**
     * create new instance of ForgotPasswordResponse to pass TypeAdapter. TypeAdapter is used to retrieve
     * response of the ForgotPasswordResponse, the object is being created in.it uses {@code gson} to parse
     * data from Json.
     *
     * @param gson to parse data from Json
     * @return {@code new AutoValue_ForgotPasswordResponse.GsonTypeAdapter(gson)}
     */
    public static TypeAdapter<PayPalCode> typeAdapter(Gson gson) {
        return new AutoValue_PayPalCode.GsonTypeAdapter(gson);
    }

}

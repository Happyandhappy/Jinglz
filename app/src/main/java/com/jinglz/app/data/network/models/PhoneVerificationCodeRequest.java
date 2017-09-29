package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class PhoneVerificationCodeRequest {

    /**
     * returns new PhoneVerificationCodeRequest object by constructing
     * new AutoValue_PhoneVerificationCodeRequest with specified email
     *
     * @param email string variable contains user email used for phone verification process.
     * @return Returns a new instance of PhoneVerificationCodeRequest class
     */
    public static PhoneVerificationCodeRequest create(String email) {
        return new AutoValue_PhoneVerificationCodeRequest(email);
    }

    /**
     * Create a new instance of the PhoneVerificationCodeRequest class, constructing
     * new AutoValue_PhoneVerificationCodeRequest.GsonTypeAdapter with specified {@code gson}
     *
     *
     * @param gson to parse data from Json
     * @return Returns a new instance of PhoneVerificationCodeRequest class
     */
    public static TypeAdapter<PhoneVerificationCodeRequest> typeAdapter(Gson gson) {
        return new AutoValue_PhoneVerificationCodeRequest.GsonTypeAdapter(gson);
    }

    public abstract String email();

}

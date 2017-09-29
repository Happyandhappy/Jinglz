package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class PhoneVerificationRequest {

    /**
     * returns new PhoneVerificationRequest object by constructing
     * new AutoValue_PhoneVerificationRequest with specified email and smsCode.
     * it will be used to request verification code.
     *
     * @param email String variable that contains user email
     * @param smsCode String variable that contains sms code for phone verification
     * @return Returns a new instance of PhoneVerificationRequest class
     */
    public static PhoneVerificationRequest create(String email, String smsCode) {
        return new AutoValue_PhoneVerificationRequest(email, smsCode);
    }

    /**
     * Create a new instance of the PhoneVerificationRequest class, constructing
     * new AutoValue_PhoneVerificationRequest.GsonTypeAdapter with specified {@code gson}
     *
     *
     * @param gson to parse data from Json
     * @return Returns a new instance of PhoneVerificationRequest class
     */
    public static TypeAdapter<PhoneVerificationRequest> typeAdapter(Gson gson) {
        return new AutoValue_PhoneVerificationRequest.GsonTypeAdapter(gson);
    }

    public abstract String email();

    public abstract String smsCode();


}

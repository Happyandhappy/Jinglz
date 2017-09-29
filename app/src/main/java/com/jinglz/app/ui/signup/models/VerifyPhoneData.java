package com.jinglz.app.ui.signup.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class VerifyPhoneData {

    public abstract String phone();

    public abstract String email();

    /**
     * Returns new instance of AutoValue_VerifyPhoneData with specified phone and email.
     *
     * @param phone String contains phone number of user
     * @param email String contains email of user
     * @return AutoValue_VerifyPhoneData instance
     */
    public static VerifyPhoneData create(String phone, String email) {
        return new AutoValue_VerifyPhoneData(phone, email);
    }

}

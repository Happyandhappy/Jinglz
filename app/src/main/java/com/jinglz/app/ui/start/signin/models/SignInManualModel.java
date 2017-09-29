package com.jinglz.app.ui.start.signin.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SignInManualModel {

    /**
     *
     * @param email String contain input email by user
     * @param password String contain input password by user
     * @return AutoValue_SignInManualModel Signin data requested
     */
    public static SignInManualModel create(String email, String password) {
        return new AutoValue_SignInManualModel(email, password);
    }

    public abstract String email();

    public abstract String password();

}

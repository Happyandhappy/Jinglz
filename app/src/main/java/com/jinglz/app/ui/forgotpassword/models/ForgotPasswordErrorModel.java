package com.jinglz.app.ui.forgotpassword.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ForgotPasswordErrorModel {

    /**
     * This method is used to construct new AutoValue_ForgotPasswordErrorModel
     * with specified description.
     *
     * @param description String variable contains description to be set.
     * @return AutoValue_ForgotPasswordErrorModel instance
     */
    public static ForgotPasswordErrorModel create(String description) {
        return new AutoValue_ForgotPasswordErrorModel(description);
    }

    public abstract String description();

}

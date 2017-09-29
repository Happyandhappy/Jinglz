package com.jinglz.app.ui.start.signin.models;

import com.google.auto.value.AutoValue;

/**
 * An abstract class to handle Signing Errors
 */
@AutoValue
public abstract class SignInErrorModel {

    /**
     *
     * @param description String contains description of errors message
     * @return AutoValue_SignInErrorModel with description message
     */
    public static SignInErrorModel create(String description) {
        return new AutoValue_SignInErrorModel(description);
    }

    public abstract String description();

}

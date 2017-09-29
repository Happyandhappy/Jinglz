package com.jinglz.app.business.base.validation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.List;

import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField
        .CONFIRM_PASSWORD;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.NEW_PASSWORD;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.NONE;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.OLD_PASSWORD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class PasswordValidationException extends RuntimeException {

    /**
     * used to initialize fields {@code NONE, NEW_PASSWORD, CONFIRM_PASSWORD, OLD_PASSWORD} respectively.
     *
     */
    @Retention(SOURCE)
    @IntDef({NONE, NEW_PASSWORD, CONFIRM_PASSWORD, OLD_PASSWORD})
    public @interface ResetPasswordField {

        int NONE = -1;

        int NEW_PASSWORD = 0;

        int CONFIRM_PASSWORD = 1;

        int OLD_PASSWORD = 2;

    }

    private final List<PasswordValidationError> mErrors;

    /**
     * method with specified errors. contains list of PasswordValidationError
     * for initializing {@see mErrors}
     *
     * @param errors
     */
    public PasswordValidationException(List<PasswordValidationError> errors) {
        mErrors = errors;
    }

    public List<PasswordValidationError> getErrors() {
        return mErrors;
    }
}

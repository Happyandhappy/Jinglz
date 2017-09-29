package com.jinglz.app.business.base.validation;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PasswordValidationError {

    /**
     * static method that creates new PasswordValidationError with specified field and message.
     *
     * @param field integer variable contains field number
     *         {@link com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField}
     *
     * @param message String message to displayed.
     * @return {@code new AutoValue_PasswordValidationError(field, message)}
     */
    public static PasswordValidationError create(@PasswordValidationException.ResetPasswordField int field,
                                                 String message) {
        return new AutoValue_PasswordValidationError(field, message);
    }

    @PasswordValidationException.ResetPasswordField
    public abstract int field();

    public abstract String message();

}

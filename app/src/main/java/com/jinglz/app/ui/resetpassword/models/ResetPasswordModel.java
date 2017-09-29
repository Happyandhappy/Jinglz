package com.jinglz.app.ui.resetpassword.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ResetPasswordModel {
    /**
     * Constructs new ResetPasswordModel with specified code, email, newPassword, oldPassword,
     * @param code  String contain verification code for security purpose.
     * @param email String contains user email.
     * @param newPassword String contain new password .
     * @param confirmPassword String contain confirm new password
     * @return
     */

    public static ResetPasswordModel create(String code, String email, String newPassword, String confirmPassword) {
        return new AutoValue_ResetPasswordModel(code, email, newPassword, confirmPassword);
    }

    public abstract String code();

    public abstract String email();

    public abstract String newPassword();

    public abstract String confirmPassword();

}

package com.jinglz.app.business.base.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jinglz.app.Constants;
import com.jinglz.app.R;
import com.jinglz.app.data.network.models.ChangePasswordModel;
import com.jinglz.app.ui.resetpassword.models.ResetPasswordModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class PasswordValidation {

    private final Context mContext;

    @Inject
    public PasswordValidation(Context context) {
        mContext = context;
    }

    /**
     * The method takes model of {@link ResetPasswordModel} type as input and perform validation.
     * it uses {@link #passwordsValidate(ResetPasswordModel)} to validate {@link ResetPasswordModel}.
     * returns {@code model} if validation succeeded.
     *
     * @param model ResetPasswordModel to be validated
     * @return ResetPasswordModel object
     */
    public Single<ResetPasswordModel> validate(@NonNull ResetPasswordModel model) {
        return Single.just(model)
                .flatMap(this::passwordsValidate)
                .map(r -> model);
    }

    /**
     * The method takes model of {@link ChangePasswordModel} type as input and perform password validation.
     * it uses {@link #passwordsValidate(ChangePasswordModel)} to validate {@link ChangePasswordModel}.
     * returns {@code model} if validation succeeded.
     *
     * @param model ChangePasswordModel to be validated
     * @return ChangePasswordModel object
     */
    public Single<ChangePasswordModel> validate(@NonNull ChangePasswordModel model) {
        return Single.just(model)
                .flatMap(this::passwordsValidate)
                .map(r -> model);
    }

    /**
     * method with specified model. this method is called for validating password before Resetting password.
     * uses {@link #lengthNewPasswordValidate(String)} to check valid length for new password,
     * uses {@link #lengthConfirmPasswordValidate(String)} to check valid length for confirm password,
     * uses {@link #matchPasswordsValidate(String, String)} to check if both strings matches.
     * return true and add {@code error} to list {@code errors} if error found.
     *
     * @param model ResetPasswordModel contains email,new password, confirm password and code
     * @return boolean value {@code Single.just(true)}
     */
    private Single<Boolean> passwordsValidate(@NonNull ResetPasswordModel model) {
        final List<PasswordValidationError> errors = new ArrayList<>();

        PasswordValidationError error;

        error = lengthNewPasswordValidate(model.newPassword());
        if (error != null) {
            errors.add(error);
        }

        error = lengthConfirmPasswordValidate(model.confirmPassword());
        if (error != null) {
            errors.add(error);
        }

        if (!errors.isEmpty()) {
            return Single.error(new PasswordValidationException(errors));
        }

        error = matchPasswordsValidate(model.newPassword(), model.confirmPassword());
        if (error != null) {
            errors.add(error);
        }

        if (!errors.isEmpty()) {
            return Single.error(new PasswordValidationException(errors));
        }

        return Single.just(true);
    }

    /**
     * method with specified model. this method is called for validating password before changing password.
     * uses {@link #lengthNewPasswordValidate(String)} to check valid length for new password,
     * uses {@link #lengthConfirmPasswordValidate(String)} to check valid length for confirm password,
     * uses {@link #lengthOldPasswordValidate(String)} to check valid length for old password,
     * uses {@link #matchPasswordsValidate(String, String)} to check if both strings matches.
     * return true and add {@code error} to list {@code errors} if error found.
     *
     * @param model ChangePasswordModel contains email,new password, confirm password and code
     * @return boolean value {@code Single.just(true)}
     */
    private Single<Boolean> passwordsValidate(@NonNull ChangePasswordModel model) {
        final List<PasswordValidationError> errors = new ArrayList<>();

        PasswordValidationError error;

        error = lengthNewPasswordValidate(model.newPassword());
        if (error != null) {
            errors.add(error);
        }

        error = lengthConfirmPasswordValidate(model.confPassword());
        if (error != null) {
            errors.add(error);
        }

        error = lengthOldPasswordValidate(model.oldPassword());
        if (error != null) {
            errors.add(error);
        }

        if (!errors.isEmpty()) {
            return Single.error(new PasswordValidationException(errors));
        }

        error = matchPasswordsValidate(model.newPassword(), model.confPassword());
        if (error != null) {
            errors.add(error);
        }

        if (!errors.isEmpty()) {
            return Single.error(new PasswordValidationException(errors));
        }

        return Single.just(true);
    }

    /**
     * method takes String values as input. that checks length of the password string by comparing
     * with {@link Constants#MIN_PASSWORD_LENGTH}. return {@link PasswordValidationError} if fails, null otherwise.
     *
     * @param oldPassword String variable contains old password
     * @return create error message if fails, null otherwise
     */
    @Nullable
    private PasswordValidationError lengthOldPasswordValidate(@NonNull String oldPassword) {
        if (oldPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            return PasswordValidationError.create(PasswordValidationException.ResetPasswordField.OLD_PASSWORD, mContext.getString(R.string.error_password_length,
                                                                                                                                  Constants.MIN_PASSWORD_LENGTH));
        }
        return null;
    }

    /**
     * method takes String values as input. that checks length of the password string by comparing
     * with {@link Constants#MIN_PASSWORD_LENGTH}. return {@link PasswordValidationError} if fails, null otherwise.
     *
     * @param newPassword String variable contains new password
     * @return {@link PasswordValidationError} if fails, null otherwise
     */
    @Nullable
    private PasswordValidationError lengthNewPasswordValidate(@NonNull String newPassword) {
        if (newPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            return PasswordValidationError.create(PasswordValidationException.ResetPasswordField.NEW_PASSWORD, mContext.getString(R.string.error_password_length,
                                                                                                                                  Constants.MIN_PASSWORD_LENGTH));
        }
        return null;
    }

    /**
     * method takes String values as input. that checks length of the password string by comparing
     * with {@link Constants#MIN_PASSWORD_LENGTH}. return {@link PasswordValidationError} if fails, null otherwise.
     *
     * @param confirmPassword String variable contains confirm password
     * @return {@link PasswordValidationError} if fails, null otherwise
     */
    @Nullable
    private PasswordValidationError lengthConfirmPasswordValidate(@NonNull String confirmPassword) {
        if (confirmPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            return PasswordValidationError.create(PasswordValidationException.ResetPasswordField.CONFIRM_PASSWORD,
                                                  mContext.getString(R.string.error_password_length,
                                                                     Constants.MIN_PASSWORD_LENGTH));
        }
        return null;
    }

    /**
     * method with specified newPassword and confirmPassword. performs comparison between two strings.
     * return {@link PasswordValidationError} if match fails, null otherwise.
     *
     * @param newPassword String variable contains new password
     * @param confirmPassword String variable contains confirm password
     * @return {@link PasswordValidationError} if match fails, null otherwise
     */
    @Nullable
    private PasswordValidationError matchPasswordsValidate(@NonNull String newPassword,
                                                           @NonNull String confirmPassword) {
        if (!TextUtils.equals(newPassword, confirmPassword)) {
            return PasswordValidationError.create(PasswordValidationException.ResetPasswordField.CONFIRM_PASSWORD,
                                                  mContext.getString(R.string.error_password_match));
        }
        return null;
    }

}

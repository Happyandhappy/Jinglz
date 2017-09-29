package com.jinglz.app.business.auth.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

import com.jinglz.app.R;
import com.jinglz.app.ui.forgotpassword.models.ForgotPasswordErrorModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class ForgotPasswordValidation {

    private final Context mContext;

    @Inject
    public ForgotPasswordValidation(Context context) {
        mContext = context;
    }

    /**
     * The method checks whether email of type String is filled and in valid format.
     * it uses {@link #emailValidate(String)} to validate email.returns {@code email}
     * if validation succeeded.
     *
     * @param email String variable to be validated
     * @return String value that contains email
     */
    public Single<String> validate(@NonNull String email) {
        return Single.just(email)
                .flatMap(this::emailValidate)
                .map(validate -> email);
    }

    /**
     * The method checks whether email of type String is filled and in valid format.
     * it uses two methods {@code missingFieldsValidate(email)}
     * and {@code incorrectFieldsValidate(email)} for returning the desired result:
     * {@code Single.just(true)}
     *
     * @param email String variable to be validated
     * @return boolean value {@code Single.just(true)}
     */
    private Single<Boolean> emailValidate(@NonNull String email) {
        ForgotPasswordErrorModel error;

        error = missingFieldsValidate(email);

        if (error == null) {
            error = incorrectFieldsValidate(email);
        }

        if (error != null) {
            return Single.error(new ForgotPasswordValidationException(error.description()));
        }

        return Single.just(true);
    }

    /**
     * The method checks if field is empty and returns corresponding message
     *
     * @param email String variable to be checked
     * @return message if field is empty
     */
    @Nullable
    private ForgotPasswordErrorModel missingFieldsValidate(@NonNull String email) {
        if (email.isEmpty()) {
            return ForgotPasswordErrorModel.create(mContext.getString(R.string.error_field_missing));
        }
        return null;
    }

    /**
     * This method checks the pattern of email and return invalid message
     * if String pattern does not matched with Patterns.EMAIL_ADDRESS
     *
     * @param email String variable to be validated
     * @return message if email patter is not valid
     */
    @Nullable
    private ForgotPasswordErrorModel incorrectFieldsValidate(@NonNull String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ForgotPasswordErrorModel.create(mContext.getString(R.string.error_invalid_email));
        }
        return null;
    }

}

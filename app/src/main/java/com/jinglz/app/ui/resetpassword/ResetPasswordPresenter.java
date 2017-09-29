package com.jinglz.app.ui.resetpassword;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.business.base.validation.PasswordValidationError;
import com.jinglz.app.business.base.validation.PasswordValidationException;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.resetpassword.models.ResetPasswordModel;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.CONFIRM_PASSWORD;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.NEW_PASSWORD;

@InjectViewState
public class ResetPasswordPresenter extends BasePresenter<ResetPasswordView> {

    @Inject
    AuthInteractor mAuthInteractor;

    private String mCode;
    private String mEmail;
    /**
     * Constructor to define ResetPasswordPresenter
     */
    public ResetPasswordPresenter() {
        App.get().getComponent().inject(this);
    }

    public void init(String code, String email) {
        mCode = code;
        mEmail = email;
    }

    /**
     * Method with specified newPassword, confirmPassword, to send request for change password.
     * this is used to create new instance of {@link ResetPasswordModel#create(String, String, String, String)}.
     * on success calls {@link #handleSuccess()}, {@link #handleError(Throwable)} otherwise.
     *
     * @param newPassword String variable containing new password to set.
     * @param confirmPassword String variable containing password to confirm.
     */
    public void onClickChangePassword(String newPassword, String confirmPassword) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        getViewState().cleanErrors();
        addSubscription(mAuthInteractor.resetPassword(ResetPasswordModel.create(mCode,
                                                                                mEmail,
                                                                                newPassword.trim(),
                                                                                confirmPassword.trim()))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::handleSuccess, this::handleError));
    }

    /**
     * method to handle success, used to hide progress dialog and change password.
     */
    private void handleSuccess() {
        getViewState().hideProgress();
        getViewState().passwordChanged();
    }

    private void handleError(Throwable throwable) {
        getViewState().hideProgress();
        if (throwable instanceof AuthException) {
            getViewState().showError(throwable.getMessage());
        } else if (throwable instanceof PasswordValidationException) {
            handleValidationExceptions(((PasswordValidationException) throwable).getErrors());
        }
    }

    /**
     * method to handle validation exceptions, use to show dialog corresponding
     * to error.
     */
    private void handleValidationExceptions(List<PasswordValidationError> errors) {
        for (PasswordValidationError error : errors) {
            switch (error.field()) {
                case CONFIRM_PASSWORD:
                    getViewState().showConfirmPasswordError(error.message());
                    break;
                case NEW_PASSWORD:
                    getViewState().showNewPasswordError(error.message());
                    break;
            }
        }
    }

}

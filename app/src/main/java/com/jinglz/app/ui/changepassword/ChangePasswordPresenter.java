package com.jinglz.app.ui.changepassword;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.base.validation.PasswordValidationError;
import com.jinglz.app.business.base.validation.PasswordValidationException;
import com.jinglz.app.business.profile.ProfileInteractor;
import com.jinglz.app.business.base.exception.NetworkException;
import com.jinglz.app.data.network.models.ChangePasswordModel;
import com.jinglz.app.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.CONFIRM_PASSWORD;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.NEW_PASSWORD;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.NONE;
import static com.jinglz.app.business.base.validation.PasswordValidationException.ResetPasswordField.OLD_PASSWORD;

@InjectViewState
public class ChangePasswordPresenter extends BasePresenter<ChangePasswordView> {

    private static final String TAG = "ChangePasswordPresenter";

    @Inject ProfileInteractor mProfileInteractor;

    /**
     * constructs new ChangePasswordPresenter.
     */
    public ChangePasswordPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    /**
     * method with specified model to change password. it uses
     * {@link ProfileInteractor#changePassword(ChangePasswordModel)}.
     *
     * @param model ChangePasswordModel contains new password, old password and confirm password.
     */
    public void changePassword(ChangePasswordModel model) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        getViewState().cleanErrors();
        mProfileInteractor.changePassword(model)
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getViewState().hideProgress();
                    getViewState().onPasswordChanged();
                }, this::handleError);
    }

    /**
     * this method is used to handle Exception related to PasswordValidationException. and show error
     * message if encounter NetworkException.
     * @param throwable  Throwable instance
     */
    private void handleError(Throwable throwable) {
        getViewState().hideProgress();
        if (throwable instanceof NetworkException) {
            getViewState().showError(throwable.getMessage());
        } else if (throwable instanceof PasswordValidationException) {
            handleValidationExceptions(((PasswordValidationException) throwable).getErrors());
        }
    }

    /**
     * method with specified list of errors. This is basically used handle validation error
     * @param errors list of {@link PasswordValidationError}
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
                case OLD_PASSWORD:
                    getViewState().showOldPasswordError(error.message());
                    break;
                case NONE:
                default:
                    getViewState().showError(error.message());
                    break;
            }
        }
    }
}


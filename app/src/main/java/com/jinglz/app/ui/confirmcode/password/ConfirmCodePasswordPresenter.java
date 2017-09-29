package com.jinglz.app.ui.confirmcode.password;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ConfirmCodePasswordPresenter extends BasePresenter<ConfirmCodePasswordView> {

    @Inject
    AuthInteractor mAuthInteractor;

    private String mEmail;

    /**
     * constructs new ConfirmCodePasswordPresenter.
     */
    public ConfirmCodePasswordPresenter() {
        App.get().getComponent().inject(this);
    }

    /**
     * used to initialize {@see mEmail}
     * @param email String contains email to be assigned.
     */
    public void init(String email) {
        mEmail = email;
        getViewState().setEmail(email);
    }

    /**
     * This method is used to validate code. it calls
     * {@link AuthInteractor#confirmPasswordResetCode(String, String)} by passing code and {@see mEmail}
     *  it call {@link #handleSuccess(String)} if succeed, {@link #handleError(Throwable)} otherwise.
     *
     * @param code String variable containing code to be verified.
     * @throws AuthException if authentication fails
     */
    public void validateCode(String code) {
        getViewState().showProgress();
        mAuthInteractor.confirmPasswordResetCode(code, mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> handleSuccess(code), this::handleError);
    }

    /**
     * This method will be called after validation process. in this progress dialog and keyboard will be
     * hide and calls {@link ConfirmCodePasswordView#startResetPassword(String, String)}.
     *
     * @param code String containing code for verification.
     */
    private void handleSuccess(String code) {
        getViewState().hideProgress();
        getViewState().hideKeyboard();
        getViewState().startResetPassword(code, mEmail);
    }

    /**
     * This method will be called after validation process. in this progress dialog and keyboard will be
     * hide and display error message if {@code throwable} is an instance of AuthException.
     *
     * @param throwable Throwable contains exception to be handled
     */
    private void handleError(Throwable throwable) {
        getViewState().hideProgress();
        getViewState().hideKeyboard();
        if (throwable instanceof AuthException) {
            getViewState().showError(throwable.getMessage());
        }
    }

}

package com.jinglz.app.ui.confirmcode.phone;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.confirmcode.password.ConfirmCodePasswordView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ConfirmCodePhonePresenter extends BasePresenter<ConfirmCodePhoneView> {

    private static final String TAG = "ConfirmCodePhonePresent";

    @Inject AuthInteractor mAuthInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    private String mPhone;
    private String mEmail;

    /**
     * constructs new ConfirmCodePhonePresenter.
     */
    public ConfirmCodePhonePresenter() {
        App.get().getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    /**
     * used to initialize {@see mEmail} and {@see mPhone}.
     * it calls {@link #sendVerifyCodeRequest()} to send request for verification code.
     *
     * @param email contains email to be assigned
     * @param phone contains phone number to be assigned.
     */
    public void init(String email, String phone) {
        mEmail = email;
        mPhone = phone;
        sendVerifyCodeRequest();
        getViewState().setPhone(phone);
    }
    /**
     * This method is used to validate code. it calls
     * {@link AuthInteractor#verifyPhone(String, String)} by passing {@code code} and {@see mEmail}
     *  it call {@link #handleSuccess()} if succeed, {@link #handleError(Throwable)} otherwise.
     *
     * @param code String variable containing code to be verified.
     * @throws AuthException if authentication fails
     */
    public void validateCode(String code) {
        getViewState().showProgress();
        mAuthInteractor.verifyPhone(code, mEmail)
                .compose(bindToLifecycle().forCompletable())
                .doOnCompleted(() -> mAnalyticsFacade.trackEvent(Event.VERIFIED_PHONE_NUMBER))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleError);
    }

    /**
     * This method is used to send request for verification code.
     * it calls {@link AuthInteractor#requestPhoneVerificationCode(String)}
     *
     */
    public void sendVerifyCodeRequest() {
        getViewState().showProgress();
        mAuthInteractor.requestPhoneVerificationCode(mEmail.toLowerCase())
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getViewState().hideProgress();
                }, throwable -> {
                    getViewState().hideProgress();
                    Log.e(TAG, "sendVerifyCodeRequest: ", throwable);
                });
    }

    /**
     * This method will be called after validation process. in this progress dialog and keyboard will be
     * hide and calls {@link ConfirmCodePhoneView#onSuccess()}.
     *
     */
    private void handleSuccess() {
        getViewState().hideProgress();
        getViewState().hideKeyboard();
        getViewState().onSuccess();
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

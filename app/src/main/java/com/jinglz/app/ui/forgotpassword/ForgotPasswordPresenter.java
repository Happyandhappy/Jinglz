package com.jinglz.app.ui.forgotpassword;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.business.auth.validation.ForgotPasswordValidationException;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordView> {

    @Inject AuthInteractor mAuthInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    public ForgotPasswordPresenter() {
        App.get().getComponent().inject(this);
    }

    /**
     * method with specified email used to send request for forgot password.
     * if succeed, it tracks the event. handles success method by calling
     * {@link #handleSuccess(String)}, handle  error by calling {@link #handleError(Throwable)}
     * otherwise.
     *
     * @param email String variable contains email
     * @throws AuthException if authentication fails.
     * @throws ForgotPasswordValidationException if email address not valid
     */
    public void onClickRequestPassword(String email) {
        getViewState().showProgress();
        addSubscription(mAuthInteractor.forgotPassword(email)
                                .doOnSuccess(ignored -> mAnalyticsFacade.trackEvent(Event.RESET_PASSWORD))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::handleSuccess, this::handleError));
    }

    /**
     * this method is called after sending request to change password successfully.
     * progress dialog and keyboard will be hidden, errors text will be
     * removed and opens confirmation dialog o confirm email account.
     *
     * @param email String contains email address.
     */
    private void handleSuccess(String email) {
        getViewState().hideProgress();
        getViewState().hideKeyboard();
        getViewState().cleanErrors();
        //open code confirmation
        getViewState().startConfirmCode(email);
    }

    /**
     * This method will be performed to handle exception raised during th e process
     * of requesting forgot password.progress dialog and keyboard will be hidden,
     * errors text will be removed.
     *
     * @param throwable contains throwable instance to handle exception.
     */
    private void handleError(Throwable throwable) {
        getViewState().hideProgress();
        getViewState().hideKeyboard();
        getViewState().cleanErrors();
        if (throwable instanceof AuthException) {
            getViewState().showError(throwable.getMessage());
        } else if (throwable instanceof ForgotPasswordValidationException) {
            getViewState().showEmailError(throwable.getMessage());
        }
    }
}

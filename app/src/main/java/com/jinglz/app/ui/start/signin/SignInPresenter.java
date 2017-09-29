package com.jinglz.app.ui.start.signin;

import com.arellomobile.mvp.InjectViewState;
import com.facebook.FacebookException;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.start.signin.models.SignInManualModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SignInPresenter extends BasePresenter<SignInView> {

    private static final int LOGIN_ATTEMPT_TILL_HELPER_DIALOG = 2;

    @Inject AuthInteractor mAuthInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    private int mLoginAttempt;

    /**
     * Constructs new SignInPresenter.
     */
    public SignInPresenter() {
        App.get().getComponent().inject(this);
    }

    /**
     *
     * @param email String contains email input by user
     * @param password String contains password input by user
     */
    public void onClickSignIn(String email, String password) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        mAuthInteractor.signInManual(SignInManualModel.create(email.trim(), password.trim()))
                .doOnCompleted(() -> mAnalyticsFacade.trackEvent(Event.LOGIN_WITH_PASSWORD))
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleError);
    }

    /**
     * Handle facebook login process
     * @param token when user logged in using facebook will return a String token
     * if success {@link #handleSuccess()} is called
     * if fail {@link #handleError(Throwable)} is called and throw a error message
     */
    public void onSignInFacebook(String token) {
        getViewState().hideKeyboard();
        mAuthInteractor.signInFacebook(token)
                .doOnCompleted(() -> mAnalyticsFacade.trackEvent(Event.LOGIN_WITH_FACEBOOK))
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleError);
    }

    /**
     *
     * @param signInResult Google sign in result will contain data and token of successfully logged in
     */

    public void onSignInGoogle(GoogleSignInResult signInResult) {
        getViewState().hideKeyboard();
        mAuthInteractor.signInGoogle(signInResult)
                .doOnCompleted(() -> mAnalyticsFacade.trackEvent(Event.LOGIN_WITH_GOOGLE))
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleError);
    }

    public void onSignInFacebookError(FacebookException error) {
        getViewState().showError(error.getMessage());
    }

    public void onLocationPermission(boolean granted) {
        if (!granted) {
            getViewState().showError("You have to enable location");
        }
    }

    private void handleSuccess() {
        getViewState().hideProgress();
        getViewState().startFeed();
    }

    private void handleError(Throwable throwable) {
        getViewState().hideProgress();
        if (throwable instanceof AuthException) {
            if (mLoginAttempt < LOGIN_ATTEMPT_TILL_HELPER_DIALOG) {
                getViewState().showError(throwable.getMessage());
                mLoginAttempt++;
            } else {
                getViewState().showLoginHelperDialog();
                mLoginAttempt = 0;
            }
        }
    }
}

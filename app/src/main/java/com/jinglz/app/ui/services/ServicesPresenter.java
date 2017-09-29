package com.jinglz.app.ui.services;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.redeem.RedeemInteractor;
import com.jinglz.app.business.redeem.validation.RedeemVerificationException;
import com.jinglz.app.ui.base.BasePresenter;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ServicesPresenter extends BasePresenter<ServicesView> {

    private static final String TAG = "ServicesPresenter";

    @Inject RedeemInteractor mRedeemInteractor;

    private PayPalConfiguration mPayPalConfiguration;
    private boolean mIsPayPalLinked;

    /**
     * Constructs new ServicesPresenter
     */
    public ServicesPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadUserData();
        mRedeemInteractor.getPayPalConfiguration()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configuration -> mPayPalConfiguration = configuration,
                           throwable -> {
                               Log.e(TAG, "onFirstViewAttach: ", throwable);
                           });
    }

    public void onCheckedChange(boolean checked) {
        if (checked == mIsPayPalLinked) {
            return;
        }
        if (checked) {
            startPayPalAuthorization();
        } else {
            unlinkPayPal();
        }
    }

    /**
     * method with specified authCode to link with payPal.
     * {@link RedeemInteractor#linkPayPal(String)} is used to
     * link. on success it sets {@see mIsPayPalLinked} true,
     * {@link #handleLinkPayPalErrors(Throwable)} otherwise.
     */
    public void linkPayPalAccount(String authCode) {
        getViewState().showProgress();
        mRedeemInteractor.linkPayPal(authCode)
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(isLinked -> mIsPayPalLinked = isLinked)
                .toCompletable()
                .subscribe(() -> getViewState().hideProgress(), this::handleLinkPayPalErrors);
    }


    public void onLinkCanceled() {
        getViewState().setSwitchChecked(false);
    }

    private void startPayPalAuthorization() {
        mRedeemInteractor.getPayPalOAuthScopes()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        oAuthScopes -> getViewState().startLinkPayPalAuthorization(mPayPalConfiguration, oAuthScopes),
                        throwable -> {
                            Log.e(TAG, "onCheckedChange: ", throwable);
                        });
    }

    /**
     * method to send request to unlink PayPal
     */
    private void unlinkPayPal() {
        getViewState().showProgress();
        mRedeemInteractor.unlinkPayPal()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(isLinked -> mIsPayPalLinked = isLinked)
                .toCompletable()
                .subscribe(() -> getViewState().hideProgress(),
                           throwable -> {
                               getViewState().hideProgress();
                               getViewState().setSwitchChecked(true);
                               Log.e(TAG, "unlinkPayPal: ", throwable);
                           });
    }

    /**
     * It will load user data to check linking state of PayPal.
     */
    private void loadUserData() {
        mRedeemInteractor.isPayPalLinked()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(isLinked -> mIsPayPalLinked = isLinked)
                .subscribe(isLinked -> getViewState().setSwitchChecked(isLinked),
                           throwable -> {
                               Log.e(TAG, "loadUserData: ", throwable);
                           });
    }

    /**
     * to handle failure exception of paypal integration
     * @param throwable  Throwable instance to handle RedeemVerificationException
     */
    private void handleLinkPayPalErrors(Throwable throwable) {
        getViewState().hideProgress();
        onLinkCanceled();
        if (throwable instanceof RedeemVerificationException) {
            switch (((RedeemVerificationException) throwable).getVerificationType()) {
                case RedeemVerificationException.VerificationType.NONE:
                    getViewState().showError(throwable.getMessage());
                    break;
            }
        }
    }
}

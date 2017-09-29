package com.jinglz.app.ui.redeem;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.feed.FeedInteractor;
import com.jinglz.app.business.profile.ProfileInteractor;
import com.jinglz.app.business.redeem.RedeemInteractor;
import com.jinglz.app.business.redeem.validation.RedeemCoinValidationException;
import com.jinglz.app.business.redeem.validation.RedeemVerificationException;
import com.jinglz.app.business.share.ShareInteractor;
import com.jinglz.app.data.local.event.UpdateBalanceEvent;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.utils.NumberUtils;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class RedeemPresenter extends BasePresenter<RedeemView> {

    private static final String TAG = "RedeemPresenter";

    @Inject RedeemInteractor mRedeemInteractor;
    @Inject FeedInteractor mFeedInteractor;
    @Inject ProfileInteractor mProfileInteractor;
    @Inject ShareInteractor mShareInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    private PayPalConfiguration mPayPalConfiguration;
    private CurrentCoinsResponse mCoinsResponse;
    /**
     * Constructs new RedeemPresenter.
     */
    public RedeemPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    /**
     * Call when view attach
     */
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadPayPalConfiguration();
        loadBalance(false);
    }

    @Override
    public void onDestroy() {
        getViewState().stopPayPalService();
        super.onDestroy();
    }

    /**
     * method with specified coins to perform redeem functionality.
     *
     * @param coins total number of coins to be redeemed.
     */
    public void onRedeemClick(String coins) {
        getViewState().showProgress();
        final int coinValue = parseCoins(coins);
        mRedeemInteractor.redeemUserCoins(coinValue)
                .doOnSuccess(ignored -> mAnalyticsFacade.trackEvent(Event.COIN_REDEMPTION))
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(email -> onRedeemSuccess(coinValue, email), this::handleRedeemError);
    }

    /**
     * Method with specified coins and authCode, For linking user with paypal account.
     *
     * @param coins number of coins to redeem.
     * @param authCode authentication code for security.
     */
    public void linkPayPalAccount(String coins, String authCode) {
        getViewState().showProgress();
        final int coinValue = parseCoins(coins);
        mRedeemInteractor.linkPayPalAndRedeem(coinValue, authCode)
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(email -> onRedeemSuccess(coinValue, email), this::handleRedeemError);
    }

    public void onShareClick(int coins) {
        mShareInteractor.getShareWonCoinMessage(coins)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shareString -> {
                    getViewState().openShareChooser(shareString);
                }, throwable -> {
                    Log.e(TAG, "onShareClick: ", throwable);
                });
    }

    /**
     * Method with specified amount, to setRedeemSum by calculating sum of the
     * coins using {@link RedeemPresenter#calculateRedeemSum(int)}.
     *
     * @param amount amount will convert and calculate sum of redeemed balance
     */

    public void setAmount(CharSequence amount) {
        final int coins = parseCoins(amount.toString());
        getViewState().setRedeemSum(calculateRedeemSum(coins));
    }

    /**
     * When redeem is successful @onRedeemSuccess is called
     * @param coins number of coins user have in account
     * @param email email address of user
     */
    private void onRedeemSuccess(int coins, String email) {
        getViewState().hideProgress();
        loadBalance(true);
        getViewState().cleanCoinSum();
        getViewState().openShareDialog(email, coins);
    }


    @Nullable
    private String calculateRedeemSum(int coins) {
        if (mCoinsResponse == null) {
            return null;
        }
        return coins >= mCoinsResponse.minimumToRedeem()
                ? NumberUtils.toPrettyNumber(coins / mCoinsResponse.ratio())
                : null;
    }

    private int parseCoins(String coins) {
        int coinsInt;
        try {
            coinsInt = Integer.parseInt(coins);
        } catch (NumberFormatException e) {
            coinsInt = 0;
        }
        return coinsInt;
    }


    /**
     * handling redeemed errors
     * to verify users paypal account
     */
    private void openLinkPayPal() {
        mRedeemInteractor.getPayPalOAuthScopes()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle().forSingle())
                .subscribe(oAuthScopes -> {
                    getViewState().startLinkPayPalAuthorization(mPayPalConfiguration, oAuthScopes);
                }, throwable -> {
                    getViewState().hideProgress();
                    Log.e(TAG, "openLinkPayPal: ", throwable);
                });
    }

    /**
     * It will show the total balance available in user account
     * @param sendEvent
     * new UpdateBalanceEvent with specified value balance will be return
00     */

    private void loadBalance(boolean sendEvent) {
        mRedeemInteractor.getCoins()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(coinsResponse -> mCoinsResponse = coinsResponse)
                .doOnNext(coinsResponse -> {
                    getViewState().setRedeemRatio(NumberUtils.toPrettyNumber(1 / coinsResponse.ratio()));
                    if (sendEvent) {
                        RxBus.get().post(UpdateBalanceEvent.create(coinsResponse.currentCoins()));
                    }
                })
                .subscribe(coinsHistoryModel -> getViewState().setBalance(NumberUtils.toPrettyNumber(coinsHistoryModel.currentCoins())), throwable -> Log.e(TAG, "loadBalance: ", throwable));
    }

    private void loadPayPalConfiguration() {
        mRedeemInteractor.getPayPalConfiguration()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle().forSingle())
                .doOnSuccess(configuration -> mPayPalConfiguration = configuration)
                .subscribe(this::startPayPalService, throwable -> Log.e(TAG, "loadPayPalConfiguration: ", throwable));
    }

    private void startPayPalService(PayPalConfiguration config) {
        getViewState().startPayPalService(config);
    }

    /**
     * for handling redeemed errors and success response
     * if user mobile number is not verified this error will be show @showFillNumberError
     */
    private void openPhoneVerification() {
        mProfileInteractor.getUserData()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(response -> {
                    if (TextUtils.isEmpty(response.phone())) {
                        getViewState().showFillNumberError();
                        return;
                    }
                    getViewState().openPhoneVerification(response.email(), response.phone());
                }, throwable -> {
                    Log.e(TAG, "openPhoneVerification: ", throwable);
                });
    }

    /**
     * handle error while redeeming coins
     * @param throwable show message found in each error.
     */

    private void handleRedeemError(Throwable throwable) {
        getViewState().hideProgress();
        if (throwable instanceof RedeemCoinValidationException) {
            getViewState().showError(throwable.getMessage());
        }
        if (throwable instanceof RedeemVerificationException) {
            final int verificationType = ((RedeemVerificationException) throwable).getVerificationType();
            switch (verificationType) {
                case RedeemVerificationException.VerificationType.NONE:
                    getViewState().showError(throwable.getMessage());
                    break;
                case RedeemVerificationException.VerificationType.PHONE:
                    openPhoneVerification();
                    break;
                case RedeemVerificationException.VerificationType.PAY_PAL:
                    openLinkPayPal();
                    break;
                case RedeemVerificationException.VerificationType.COIN_LIMIT:
                    getViewState().showError(throwable.getMessage());
                    break;
            }
        }
    }
}

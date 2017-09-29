package com.jinglz.app.business.redeem;

import android.content.Context;

import com.jinglz.app.R;
import com.jinglz.app.business.redeem.validation.RedeemLocalValidation;
import com.jinglz.app.business.redeem.validation.RedeemVerificationException;
import com.jinglz.app.data.network.models.PayPalCode;
import com.jinglz.app.data.network.models.RedeemRequest;
import com.jinglz.app.data.network.models.RedeemValidationModel;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.data.repositories.CoinsRepository;
import com.jinglz.app.data.repositories.PayPalRepository;
import com.jinglz.app.data.repositories.SessionRepository;
import com.jinglz.app.injection.session.PerSession;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class RedeemInteractor {

    private final PayPalRepository mPayPalRepository;
    private final SessionRepository mSessionRepository;
    private final RedeemLocalValidation mLocalValidation;
    private final CoinsRepository mCoinsRepository;
    private final Context mContext;

    /**
     * constructs new RedeemInteractor with specified payPalRepository, sessionRepository,
     * localValidation, coinsRepository and context
     *
     * @param payPalRepository handle paypal integration
     * @param sessionRepository keep track of coins for particular session
     * @param localValidation for handling validation process
     * @param coinsRepository keeps track of coins
     * @param context handle application specific content
     */
    @Inject
    public RedeemInteractor(PayPalRepository payPalRepository,
            SessionRepository sessionRepository,
            RedeemLocalValidation localValidation,
            CoinsRepository coinsRepository, Context context) {
        mPayPalRepository = payPalRepository;
        mSessionRepository = sessionRepository;
        mLocalValidation = localValidation;
        mCoinsRepository = coinsRepository;
        mContext = context;
    }

    /**
     * method to retrieve paypal configuration from {@link PayPalRepository#getConfiguration()}
     *
     * @return {@code mPayPalRepository.getConfiguration()}
     */
    public Single<PayPalConfiguration> getPayPalConfiguration() {
        return mPayPalRepository.getConfiguration();
    }

    /**
     * method to retrieve paypal authentication scopes from {@link PayPalRepository#getOAuthScopes()}
     *
     * @return {@code mPayPalRepository.getOAuthScopes()}
     */
    public Single<PayPalOAuthScopes> getPayPalOAuthScopes() {
        return mPayPalRepository.getOAuthScopes();
    }

    /**
     * method that receives coins and authCode as input parameter. performs the redeem operation
     * and generates authCode. it handles paypal linking exception by calling {@link #handleLinkPayPalHttpException}
     *
     * @param coins value of coins to be redeemed
     * @param authCode authentication code to be generated
     * @return
     */
    public Single<String> linkPayPalAndRedeem(int coins, String authCode) {
        return mPayPalRepository.linkPayPalAccount(PayPalCode.create(authCode))
                .flatMap(response -> redeemUserCoins(coins))
                .onErrorResumeNext(this::handleLinkPayPalHttpException)
                .subscribeOn(Schedulers.io());
    }

    /**
     * method with specified authCode. to perform linking of paypal.return true if linked.
     *
     * @param authCode String variable contains authCode to be generated
     * @return Single object of boolean type
     */
    public Single<Boolean> linkPayPal(String authCode) {
        return mPayPalRepository.linkPayPalAccount(PayPalCode.create(authCode))
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(this::handleLinkPayPalHttpException)
                .map(UserResponse::isPayPalLinked);
    }

    /**
     * This method is used to unlink from paypal account
     *
     * @return Single object of boolean type
     */
    public Single<Boolean> unlinkPayPal() {
        return mPayPalRepository.unlinkPayPal()
                .subscribeOn(Schedulers.io())
                .map(UserResponse::isPayPalLinked);
    }

    /**
     * method with specified value of coins for redeem user coins.
     *
     * @param coins holds coin count to redeem
     * @return Single object of String
     */
    public Single<String> redeemUserCoins(int coins) {
        return Single.zip(mSessionRepository.loadCurrentSession(), mCoinsRepository.getCoinsUpToDay(),
                (response, coinsResponse) -> RedeemValidationModel.create(coinsResponse, coins, response))
                .subscribeOn(Schedulers.io())
                .flatMap(mLocalValidation::validate)
                .flatMap(result -> mPayPalRepository.redeemCoins(RedeemRequest.create(coins)));
    }

    /**
     * This method is used to check linking of paypal.return true if linked.
     *
     * @return Single object of boolean type
     */
    public Single<Boolean> isPayPalLinked() {
        return mSessionRepository.loadCurrentSession()
                .subscribeOn(Schedulers.io())
                .map(UserResponse::isPayPalLinked);
    }

    /**
     * This method is used to retrieve coins by calling {@link CoinsRepository#getCoins()}.
     *
     * @return Observable object of CurrentCoinsResponse
     */
    public Observable<CurrentCoinsResponse> getCoins() {
        return mCoinsRepository.getCoins()
                .subscribeOn(Schedulers.io());
    }

    /**
     * this method is used to check HttpException. Throws new Exception {@code throwable} is
     * instance of HttpException.
     *
     * @param throwable object to be thrown for testing Exceptions
     * @return Single object
     */
    private Single handleLinkPayPalHttpException(Throwable throwable) {
        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).code() == 400) {
                return Single.error(
                        new RedeemVerificationException(mContext.getString(R.string.error_paypal_already_linked),
                                RedeemVerificationException.VerificationType.NONE));
            }
        }
        return Single.error(throwable);
    }
}

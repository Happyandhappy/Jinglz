package com.jinglz.app.business.redeem.validation;

import android.content.Context;

import com.jinglz.app.R;
import com.jinglz.app.data.network.models.RedeemValidationModel;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.injection.session.PerSession;

import javax.inject.Inject;

import rx.Single;

@PerSession
public class RedeemLocalValidation {

    private final Context mContext;

    @Inject
    public RedeemLocalValidation(Context context) {
        mContext = context;
    }

    /**
     * method with specified model to check different validation related to coins.
     * create new runtime exception if {@link #validateCoinsCount(CurrentCoinsResponse, int)}
     * and {@link #validateVerificationStatuses(UserResponse)} returns exception.
     * returns true if valid, exception otherwise.
     *
     * @param model to be validated
     * @return Single object of boolean
     */
    public Single<Boolean> validate(RedeemValidationModel model) {
        RuntimeException exception = validateCoinsCount(model.coinResponse(), model.coinsSum());
        if (exception == null) {
            exception = validateVerificationStatuses(model.userData());
        }
        if (exception != null) {
            return Single.error(exception);
        } else {
            return Single.just(true);
        }
    }

    /**
     * method that receives coinsResponse and coinCount for validating count of coins.
     * constructs {@code new RedeemCoinValidationException(coinsResponse.messageMinimumRedeem())}
     * if {@code coinCount} is less than {@code coinsResponse.minimumToRedeem()}, constructs
     * {@code new RedeemCoinValidationException(mContext.getString(R.string.error_no_enough_coins))} otherwise.
     *
     * @param coinsResponse contains response for validating coins count
     * @param coinCount int value that will be compared with the response
     * @return
     */
    private RedeemCoinValidationException validateCoinsCount(CurrentCoinsResponse coinsResponse, int coinCount) {
        if (coinCount < coinsResponse.minimumToRedeem()) {
            return new RedeemCoinValidationException(coinsResponse.messageMinimumRedeem());
        }
        if (coinCount > coinsResponse.currentCoins()) {
            return new RedeemCoinValidationException(mContext.getString(R.string.error_no_enough_coins));
        }
        return null;
    }

    /**
     * method with specified userResponse. checks whether phone number is verified by
     * calling {@link UserResponse#isPhoneVerified()}. checks paypal linking by calling
     * {@link UserResponse#isPayPalLinked()} and test for the tax id. create its corresponding exceptions,
     * null otherwise.
     *
     * @param userResponse that contains user details to tested for
     * @return RedeemVerificationException
     */
    private RedeemVerificationException validateVerificationStatuses(UserResponse userResponse) {
        if (!userResponse.isPhoneVerified()) {
            return new RedeemVerificationException(RedeemVerificationException.VerificationType.PHONE);
        }
        if (!userResponse.isPayPalLinked()) {
            return new RedeemVerificationException(RedeemVerificationException.VerificationType.PAY_PAL);
        }
        if (!userResponse.isTaxId() && userResponse.isTaxIdRequestSend()) {
            return new RedeemVerificationException(mContext.getString(R.string.error_earned_coins_limit),
                                                   RedeemVerificationException.VerificationType.COIN_LIMIT);
        }
        return null;
    }
}

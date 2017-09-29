package com.jinglz.app.ui.redeem;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;

/**
 * An interface for {@link RedeemFragment}
 * it will handel start and stop actions of paypal service, Authorization, linking
 *
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface RedeemView extends BaseView {

    void startPayPalService(PayPalConfiguration configuration);

    void stopPayPalService();

    void startLinkPayPalAuthorization(PayPalConfiguration configuration, PayPalOAuthScopes oAuthScopes);

    void setBalance(String balance);

    void openPhoneVerification(String email, String phone);

    void setRedeemSum(String sum);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void openShareDialog(String email, int coins);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void openShareChooser(String text);

    void cleanCoinSum();

    void setRedeemRatio(String ratio);

    void showFillNumberError();
}

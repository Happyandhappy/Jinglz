package com.jinglz.app.ui.services;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;

/**
 * This interface can be implemented to handle
 * payPal integration. classes implementing this interface needs to
 * override its method such as setSwitchChecked, startPayPalService,
 * stopPayPalService and startLinkPayPalAuthorization.
 */

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ServicesView extends BaseView {

    void setSwitchChecked(boolean checked);

    void startPayPalService(PayPalConfiguration configuration);

    void stopPayPalService();

    void startLinkPayPalAuthorization(PayPalConfiguration configuration, PayPalOAuthScopes oAuthScopes);
}

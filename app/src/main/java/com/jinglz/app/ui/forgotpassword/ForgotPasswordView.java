package com.jinglz.app.ui.forgotpassword;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * This can be implemented for handling forgot password operations.
 * it is use to show an error encountered in email, to confirm code via
 * email and to empty error text.
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface ForgotPasswordView extends BaseView {

    void showEmailError(String error);

    void cleanErrors();

    void startConfirmCode(String email);

}

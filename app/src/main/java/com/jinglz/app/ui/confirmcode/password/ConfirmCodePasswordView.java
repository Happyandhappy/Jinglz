package com.jinglz.app.ui.confirmcode.password;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * this interface can be implemented to reset password.
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface ConfirmCodePasswordView extends BaseView {

    void setEmail(String email);

    void startResetPassword(String code, String email);
}

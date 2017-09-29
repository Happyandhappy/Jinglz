package com.jinglz.app.ui.resetpassword;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;


/**
 * This interface an be implemented by the classes to
 * work with password reset method. classes implementing this
 * interface needs to override showNewPasswordError, showConfirmPasswordError,
 * cleanErrors and passwordChanged.
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface ResetPasswordView extends BaseView {

    void showNewPasswordError(String error);

    void showConfirmPasswordError(String error);

    void cleanErrors();

    void passwordChanged();

}

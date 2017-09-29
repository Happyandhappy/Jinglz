package com.jinglz.app.ui.changepassword;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * extends by the where need to handle error messages and perform function on
 * password change.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ChangePasswordView extends BaseView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onPasswordChanged();

    /**
     * for displaying error message for old password
     * @param message String variable containing message.
     */
    void showOldPasswordError(String message);

    /**
     * for displaying error message for new password
     * @param message String variable containing message.
     */
    void showNewPasswordError(String message);

    /**
     * for displaying error message for confirm password
     * @param message String variable containing message.
     */
    void showConfirmPasswordError(String message);

    /**
     * used to clean all error mesages
     */
    void cleanErrors();
}

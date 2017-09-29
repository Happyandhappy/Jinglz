package com.jinglz.app.ui.settings;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;


/**
 * This interface is used to handle click events while changing passwords
 * classes implementing this interface can override openEditProfile, openChangePassword,
 * openServices and onLogoutSuccess.
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface SettingsView extends BaseView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void openEditProfile();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void openChangePassword();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void openServices();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void onLogoutSuccess();

}

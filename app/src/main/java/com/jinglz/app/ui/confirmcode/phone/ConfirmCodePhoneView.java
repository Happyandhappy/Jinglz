package com.jinglz.app.ui.confirmcode.phone;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * This interface is used to perform operation for confirmation of code
 * that is used to handle success and to set phone number
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface ConfirmCodePhoneView extends BaseView {

    void setPhone(String phone);

    void onSuccess();
}

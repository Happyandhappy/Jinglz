package com.jinglz.app.ui.contactus;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * Interface is used to perform operation on sending success message and
 * to enable send button.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ContactUsView extends BaseView {

    void onSendSuccess();

    void enableSendButton(boolean enable);

}

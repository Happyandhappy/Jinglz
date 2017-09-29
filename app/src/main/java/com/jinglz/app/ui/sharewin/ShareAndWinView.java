package com.jinglz.app.ui.sharewin;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * An interface to setReferral code from user data
 * And startShareIntent to start intent.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ShareAndWinView extends BaseView {

    void setReferralCode(String code);

    @StateStrategyType(value = SkipStrategy.class)
    void startShareIntent(String message);

}

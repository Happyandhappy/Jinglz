package com.jinglz.app.ui.start;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * An interface to handle button views and clicks
 * skip boarding
 * and highlight indicator or not
 *
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface StartView extends BaseView {

    void showNextButton(boolean show);

    void showBackButton(boolean show);

    void setSelectedPage(int page);

    void highlightIndicator(boolean highlight);

    void highlightBackButton(boolean highlight);

    void skipOnBoarding();

}

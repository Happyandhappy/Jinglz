package com.jinglz.app.ui.howitworks;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * Interface can be implemented to override method for
 * open legalNotice and to set text on view interface.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface HowItWorksView extends BaseView {

    void setText(CharSequence text);

    void openLegalNotice();
}

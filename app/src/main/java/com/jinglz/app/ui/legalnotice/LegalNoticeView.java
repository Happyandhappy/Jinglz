package com.jinglz.app.ui.legalnotice;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * This interface can be implemented to set
 * text on view interface for legalNotice response.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface LegalNoticeView extends BaseView {

    void setText(String text);

}

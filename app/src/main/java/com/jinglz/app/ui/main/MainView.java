package com.jinglz.app.ui.main;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.ui.base.BaseView;
import com.jinglz.app.ui.feed.models.ShortUserData;

import java.util.List;

/**
 * interface extended BaseView, can be implemented by the activities to setUserData and
 * showEntriesAndWinningsDialog with specified list of {@link ContestResult}.
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface MainView extends BaseView {

    void setUserData(ShortUserData data);

    void showEntriesAndWinningsDialog(List<ContestResult> result);

    void onUnauthorized();

}

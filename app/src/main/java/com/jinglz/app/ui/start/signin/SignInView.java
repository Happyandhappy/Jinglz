package com.jinglz.app.ui.start.signin;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * An interface to start feed {@link com.jinglz.app.ui.feed.FeedFragment}
 * and showLoginHelperDialog() to prompt user for invalid inputs
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface SignInView extends BaseView {

    void startFeed();

    void showLoginHelperDialog();
}

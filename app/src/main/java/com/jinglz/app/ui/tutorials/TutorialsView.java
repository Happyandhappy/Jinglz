package com.jinglz.app.ui.tutorials;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * An interface to show Video Rules and FeedTutorials
 * #FeedTutorials() {@link com.jinglz.app.data.local.event.FeedTutorialsEvent}
 * {@link #showVideoRules()} {@link com.jinglz.app.ui.videorules.VideoRulesActivity}
 */
@StateStrategyType(SkipStrategy.class)
public interface TutorialsView extends BaseView {

    void showFeedTutorials();

    void showVideoRules();
}

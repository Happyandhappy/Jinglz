package com.jinglz.app.ui.videorules;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;

/**
 * This interface is used to handle click events of next, back buttons
 *
 * to set selected page Index(an integer value)
 * opens a video id that belongs to particular contest
 * close() to close the video and finish {@link VideoRulesActivity}
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface VideoRulesView extends BaseView {

    void showNextButton(boolean show);

    void showBackButton(boolean show);

    void setSelectedPage(int page);

    void openVideo(String videoId, String contestId);

    void close();

}

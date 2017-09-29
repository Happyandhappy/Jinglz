package com.jinglz.app.ui.feed;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;
import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;
import com.jinglz.app.ui.feed.models.TutorialSectionModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;

import java.util.List;

/**
 * This interface is is used to handle coins redeem functionality. it can be
 * implemented by the classes to set coins details.
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface FeedView extends BaseView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void animateTotalBalance(double coins);

    void setTotalBalance(double coins);

    void setTodayCoins(double coins);

    void setWeekCoins(double coins);

    void setMonthCoins(double coins);

    void setVideos(List<VideoSectionModel> sections);

    void setRefreshing(boolean refreshing);

    void clearBalance();

    void showNoInternetError(boolean show);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showTutorialSection(TutorialSectionModel section);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void closeTutorial();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showVideoRules(AvailableVideoItemModel item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openVideoWithRules(AvailableVideoItemModel item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openVideoWithoutRules(AvailableVideoItemModel item);

}

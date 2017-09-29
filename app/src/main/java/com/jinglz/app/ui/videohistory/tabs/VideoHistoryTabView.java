package com.jinglz.app.ui.videohistory.tabs;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.ui.base.BaseView;
import com.jinglz.app.ui.feed.models.VideoItemModel;

import java.util.List;

/**
 * An interface for {@link VideoHistoryTabFragment}
 * to handle Videos in list showVideos is called
 * to handle visibility of Load more indicator showLoadMoreIndicator() and hideLoadMoreIndicator()  will be called
 * to handle visibility of List placeholder showListPlaceholder() and hideistPlaceholder()  will be called
 */
@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface VideoHistoryTabView extends BaseView {

    void showVideos(List<VideoItemModel> videos, boolean clear);

    void showLoadMoreIndicator();

    void hideLoadMoreIndicator();

    void showListPlaceholder();

    void hideListPlaceholder();

    void setHeader(List<GameStatistic> statistics);

}

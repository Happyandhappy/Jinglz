package com.jinglz.app.ui.videohistory.tabs.week;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.videohistory.VideoHistoryInteractor;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.videohistory.tabs.VideoHistoryTabPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Single;

/**
 * Presenter class for {@link WeekGameHistoryTabFragment}
 */
@InjectViewState
public class WeekVideoHistoryTabPresenter extends VideoHistoryTabPresenter {

    @Inject VideoHistoryInteractor mHistoryInteractor;

    public WeekVideoHistoryTabPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    @Override
    protected Single<List<VideoItemModel>> getVideos(int page) {
        return mHistoryInteractor.getWeekHistory(page);
    }

    @Override
    protected Single<List<GameStatistic>> getHeader() {
        return mHistoryInteractor.getWeekCoinsHistory();
    }
}

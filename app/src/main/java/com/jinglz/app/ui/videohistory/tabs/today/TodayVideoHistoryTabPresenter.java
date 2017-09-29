package com.jinglz.app.ui.videohistory.tabs.today;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.videohistory.VideoHistoryInteractor;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.videohistory.tabs.VideoHistoryTabPresenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Single;

/**
 * Presenter class for {@link TodayGameHistoryTabFragment}
 */
@InjectViewState
public class TodayVideoHistoryTabPresenter extends VideoHistoryTabPresenter {

    @Inject VideoHistoryInteractor mHistoryInteractor;

    /**
     * public constructor for TodayVideoHistoryTabPresenter
     */
    public TodayVideoHistoryTabPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    /**
     * Get list of videos of page number
     * @param page Integer value that contains page count
     * @return list contains {@link VideoItemModel} videos data
     */
    @Override
    protected Single<List<VideoItemModel>> getVideos(int page) {
        return mHistoryInteractor.getTodayHistory(page);
    }


    @Override
    protected Single<List<GameStatistic>> getHeader() {
        return Single.just(Collections.EMPTY_LIST); //TODO
    }
}

package com.jinglz.app.business.videohistory;

import com.jinglz.app.Constants;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.data.repositories.VideoRepository;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.videohistory.models.NotTodayWatchedVideoItemModel;
import com.jinglz.app.ui.videohistory.models.TodayWatchedVideoItemModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class VideoHistoryInteractor {

    private final VideoRepository mVideoRepository;

    /**
     * constructs new VideoHistoryInteractor with specified videoRepository
     *
     * @param videoRepository contains video details
     */
    @Inject
    public VideoHistoryInteractor(VideoRepository videoRepository) {
        mVideoRepository = videoRepository;
    }

    /**
     * return list of {@link VideoItemModel} for today. needs to pass page number for retrieving history.
     * <p>Note there is a need to provide limit for the number of pages that will be retrieved from
     * {@link Constants#PAGE_SIZE}</p>. calls {@link VideoRepository#getTodayHistory(int, int)}
     * method and transform to retrieve VideoItemModel. returned list of VideoItemModel.
     *
     *
     * @param page number value to retrieve history
     * @return list of VideoItemModel
     */
    public Single<List<VideoItemModel>> getTodayHistory(int page) {
        return mVideoRepository.getTodayHistory(page, Constants.PAGE_SIZE)
                .toObservable()
                .flatMap(Observable::from)
                .map(this::transformToday)
                .toList()
                .toSingle()
                .subscribeOn(Schedulers.io());
    }

    /**
     * return list of {@link VideoItemModel} for weekly detail. needs to pass page number for retrieving history.
     * <p>Note there is a need to provide limit for the number of pages that will be retrieved from
     * {@link Constants#PAGE_SIZE}</p>. calls {@link VideoRepository#getWeekHistory(int, int)}
     * method and transform to retrieve VideoItemModel. returned list of VideoItemModel.
     *
     *
     * @param page number value to retrieve history
     * @return list of VideoItemModel
     */
    public Single<List<VideoItemModel>> getWeekHistory(int page) {
        return mVideoRepository.getWeekHistory(page, Constants.PAGE_SIZE)
                .toObservable()
                .flatMap(Observable::from)
                .map(this::transformNotToday)
                .toList()
                .toSingle()
                .subscribeOn(Schedulers.io());
    }

    /**
     * return list of {@link VideoItemModel} for monthly detail. needs to pass page number for retrieving history.
     * <p>Note there is a need to provide limit for the number of pages that will be retrieved from
     * {@link Constants#PAGE_SIZE}</p>. calls {@link VideoRepository#getMonthHistory(int, int)}
     * method and transform to retrieve VideoItemModel. returned list of VideoItemModel.
     *
     *
     * @param page number value to retrieve history
     * @return list of VideoItemModel
     */
    public Single<List<VideoItemModel>> getMonthHistory(int page) {
        return mVideoRepository.getMonthHistory(page, Constants.PAGE_SIZE)
                .toObservable()
                .flatMap(Observable::from)
                .map(this::transformNotToday)
                .toList()
                .toSingle()
                .subscribeOn(Schedulers.io());
    }

    /**
     * return list of {@link GameStatistic} for weekly coin statistics. calls
     * {@link VideoRepository#getWeekStatistic} for retrieving list
     *
     *
     * @return list of GameStatistic
     * */
    public Single<List<GameStatistic>> getWeekCoinsHistory() {
        return mVideoRepository.getWeekStatistic()
                .subscribeOn(Schedulers.io());
    }

    /**
     * return list of {@link GameStatistic} for monthly coin statistics. calls
     * {@link VideoRepository#getMonthStatistic} for retrieving list
     *
     *
     * @return list of GameStatistic
     * */
    public Single<List<GameStatistic>> getMonthCoinsHistory() {
        return mVideoRepository.getMonthStatistic()
                .subscribeOn(Schedulers.io());
    }

    /**
     * This method with specified response, used to parse data from response using Gson and
     * transform in VideoItemModel. It transforms data for other than today
     *
     * @param response VideoItemModel object that contains information regarding video
     * @return
     */
    private VideoItemModel transformNotToday(VideoResponse response) {
        return NotTodayWatchedVideoItemModel.create(response);
    }

    /**
     * This method with specified response, used to parse data from response using Gson and
     * transform in VideoItemModel. It transforms data for today
     *
     * @param response VideoItemModel object that contains information regarding video
     * @return
     */
    private VideoItemModel transformToday(VideoResponse response) {
        return TodayWatchedVideoItemModel.create(response);
    }
}

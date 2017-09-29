package com.jinglz.app.data.repositories;

import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.history.GameHistoryResponse;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.data.network.models.video.ShowVideoResponse;
import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.utils.DateUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Single;

@PerSession
public class VideoRepository {

    private static final String HISTORY_TYPE_WEEK = "weekly";
    private static final String HISTORY_TYPE_MONTH = "monthly";

    private final Api mApi;
    private final long mTimeZoneOffset;

    @Inject
    public VideoRepository(Api api) {
        mApi = api;
        mTimeZoneOffset = DateUtils.getTimeZoneOffset();
    }

    public Single<List<VideoResponse>> getAvailableVideos() {
        return mApi.availablesVideos()
                .map(DataResponse::data);
    }

    /**
     * This method is used to retrieve video according to particular id.
     * {@link Api#video(String)} is used to retrieve video.
     *
     * @param id String variable containing id to fetch video.
     * @return ShowVideoResponse instance
     */
    public Single<ShowVideoResponse> getVideo(String id) {
        return mApi.video(id)
                .map(DataResponse::data);
    }

    /**
     * method with specified page and limit to retrieve today's history.
     * for {@link VideoRepository#getHistory(long, int, int)}, we requires startTime
     * that will be retrieved using {@link DateUtils#getStartOfDay()}.
     *
     * @param page page number to retrieve
     * @param limit limit of pages to retrieve
     * @return list of {@link VideoResponse}
     */
    public Single<List<VideoResponse>> getTodayHistory(int page, int limit) {
        final long startTime = DateUtils.getStartOfDay();
        return getHistory(startTime, page, limit);
    }

    /**
     * method with specified page and limit to retrieve weekly history.
     * for {@link VideoRepository#getHistory(String, long, int, int)}, we requires {@code mTimeZoneOffset}
     * and {@see HISTORY_TYPE_WEEK}
     *
     * @param page page number to retrieve
     * @param limit limit of pages to retrieve
     * @return list of {@link VideoResponse}
     */
    public Single<List<VideoResponse>> getWeekHistory(int page, int limit) {
        return getHistory(HISTORY_TYPE_WEEK, mTimeZoneOffset, page, limit)
                .map(GameHistoryResponse::videos);
    }

    /**
     * method with specified page and limit to retrieve monthly history.
     * for {@link VideoRepository#getHistory(String, long, int, int)}, we requires {@code mTimeZoneOffset}
     * and {@see HISTORY_TYPE_MONTH}
     *
     * @param page page number to retrieve
     * @param limit limit of pages to retrieve
     * @return list of {@link VideoResponse}
     */
    public Single<List<VideoResponse>> getMonthHistory(int page, int limit) {
        return getHistory(HISTORY_TYPE_MONTH, mTimeZoneOffset, page, limit)
                .map(GameHistoryResponse::videos);
    }

    /**
     * Returns list of GameStatistic that gives information about weekly statistic.
     * it will done using {@link VideoRepository#getHistory(String, long, int, int)} that will use
     * {@see HISTORY_TYPE_WEEK}, {@code mTimeZoneOffset}, one as page count and zero as limit.
     *
     * @return list of {@link GameStatistic}
     */
    public Single<List<GameStatistic>> getWeekStatistic() {
        return getHistory(HISTORY_TYPE_WEEK, mTimeZoneOffset, 1, 0)
                .map(GameHistoryResponse::statistic);
    }

    /**
     * Returns list of GameStatistic that gives information about monthly statistic.
     * it will done using {@link VideoRepository#getHistory(String, long, int, int)} that will use
     * {@see HISTORY_TYPE_MONTH}, {@code mTimeZoneOffset}, one as page count and zero as limit.
     *
     * @return list of {@link GameStatistic}
     */
    public Single<List<GameStatistic>> getMonthStatistic() {
        return getHistory(HISTORY_TYPE_MONTH, mTimeZoneOffset, 1, 0)
                .map(GameHistoryResponse::statistic);
    }

    /**
     * method with specified type, timeZoneOffset, page and limit that returns {@link GameHistoryResponse}
     * instance. this is basically used to retrieve history of video according to {@code type} by calling
     * {@link Api#history(String, long, int, int)}.
     *
     * @param type String value contains the type of history that can be for today, weekly and monthly
     * @param timeZoneOffset it contains time zone
     * @param page it contains page number to retrieve history
     * @param limit it contains limit of pages to be downloaded
     * @return GameHistoryResponse instance
     */
    private Single<GameHistoryResponse> getHistory(String type, long timeZoneOffset, int page, int limit) {
        return mApi.history(type, timeZoneOffset, page, limit);
    }

    /**
     * method with specified startTime, page and limit that returns {@link VideoResponse}
     * instance. this is basically used to retrieve history of video according to {@code startTime} by calling
     * {@link Api#history(String, int, int)}.
     *
     * @param startTime it contains the time from which to retrieve videos
     * @param page it contains page number to retrieve history
     * @param limit it contains limit of pages to be downloaded
     * @return list of VideoResponse
     */
    @Deprecated
    private Single<List<VideoResponse>> getHistory(long startTime, int page, int limit) {
        return mApi.history(DateUtils.toServerFormat(startTime), page, limit)
                .map(DataResponse::data);
    }
}

package com.jinglz.app.business.feed;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.jinglz.app.R;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.data.network.models.coin.CoinsHistoryResponse;
import com.jinglz.app.data.network.models.log.VideoCountLogRequest;
import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.data.repositories.CoinsRepository;
import com.jinglz.app.data.repositories.ContestRepository;
import com.jinglz.app.data.repositories.DeviceRepository;
import com.jinglz.app.data.repositories.SessionRepository;
import com.jinglz.app.data.repositories.VideoRepository;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;
import com.jinglz.app.ui.feed.models.CoinsHistoryModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;
import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class FeedInteractor {

    private final SessionRepository mSessionRepository;
    private final CoinsRepository mCoinsRepository;
    private final VideoRepository mVideoRepository;
    private final DeviceRepository mDeviceRepository;
    private final ContestRepository mContestRepository;
    private final Context mContext;
    private final AnalyticsFacade mAnalitycsFacade;

    /**
     * construct new FeedInteractor with sessionRepository, coinsRepository,
     * videoRepository, deviceRepository, contestRepository, context, analyticsFacade
     * @param sessionRepository that keeps track of user sessions
     * @param coinsRepository   keeps track of coins history
     * @param videoRepository   keeps track of video history that can be weekly or monthly
     * @param deviceRepository  keeps track of device unique token
     * @param contestRepository keeps track of running contest, ranking and closed contests
     * @param context           for application specific resources
     * @param analyticsFacade   for tracking events and logging exceptions
     */

    @Inject
    public FeedInteractor(SessionRepository sessionRepository, CoinsRepository coinsRepository,
                          VideoRepository videoRepository,
                          DeviceRepository deviceRepository,
                          ContestRepository contestRepository,
                          Context context,
                          AnalyticsFacade analyticsFacade) {
        mSessionRepository = sessionRepository;
        mCoinsRepository = coinsRepository;
        mVideoRepository = videoRepository;
        mDeviceRepository = deviceRepository;
        mContestRepository = contestRepository;
        mContext = context;
        mAnalitycsFacade = analyticsFacade;
    }

    /**
     * method retrieve coins history. calls {@link CoinsRepository#getCoins()}
     * calls api for coins history by using {@link CoinsRepository#getCoinsHistory(int)}
     * @return Observable object of CoinsHistoryModel type
     */
    public Observable<CoinsHistoryModel> getCoinsHistory() {
        return mCoinsRepository.getCoins().zipWith(mCoinsRepository.getCoinsHistory(TimeZone.getDefault().getRawOffset()),
                        (currentCoinsResponse, coinsHistoryResponse) ->
                                CoinsHistoryModel.create(coinsHistoryResponse.today(),
                                        coinsHistoryResponse.week(),
                                        coinsHistoryResponse.month(),
                                        currentCoinsResponse.currentCoins()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * method retrieve videos. that calls {@link #transformVideosResponse(List, List)} with two lists
     * as its input parameters, one is for log available videos calls {@link VideoRepository#getAvailableVideos()},
     * other is for retrieving contests calls {@link ContestRepository#getClosedContests()}. if error occurred
     * it log exception using {@link AnalyticsFacade#logException(Throwable)}
     * @return List of VideoSectionModel
     */
    public Single<List<VideoSectionModel>> getVideos() {
        return Single.zip(mVideoRepository.getAvailableVideos().doOnSuccess(this::logVideos),
                mContestRepository.getClosedContests(),
                this::transformVideosResponse)
                .doOnError(mAnalitycsFacade::logException)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> checkInternet() {
        return mDeviceRepository.checkInternet().subscribeOn(Schedulers.io());
    }

    /**
     * method retrieve user ranking by passing contestId. calls {@link ContestRepository#getContestUserRanking(String)}
     * fetch ranking according to the contest id
     * @return List of ContestLeadershipRecord that contains user, amount and position
     */
    public Observable<List<ContestLeadershipRecord>> getContestUserRanking(String contestId) {
        return mContestRepository.getContestUserRanking(contestId)
                .toObservable()
                .zipWith(mSessionRepository.getCurrentSession(), (contestLeadershipRecords, response) -> {
                    final Optional<ContestLeadershipRecord> recordOptional = Stream.of(contestLeadershipRecords).filter(value -> value.user().getId().equals(response.id())).findFirst();

                    if (recordOptional.isPresent()) {
                        final ContestLeadershipRecord myRecord = recordOptional.get();
                        myRecord.user().setFirstName(mContext.getResources().getString(R.string.text_you));
                        myRecord.user().setLastName("");
                        if (contestLeadershipRecords.size() != 1) {
                            contestLeadershipRecords.add(0, myRecord);
                        }
                    }

                    return contestLeadershipRecords;
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * method retrieve all contests. calls {@link ContestRepository#getContestResults}
     * @return List of ContestResult that contains user, amount and position
     */
    public Single<List<ContestResult>> getContestResults() {
        return mContestRepository.getContestResults().subscribeOn(Schedulers.io());
    }

    /**
     * this method with specified list of availables and historyResponse.
     * returns list of VideoSectionModel
     *
     * @param availables      list of available videos
     * @param historyResponse list of closed contests
     * @return {@code sections} which is list of VideoSectionModel
     */
    private List<VideoSectionModel> transformVideosResponse(List<VideoResponse> availables,
                                                            List<VideoResponse> historyResponse) {
        final Map<Boolean, List<AvailableVideoItemModel>> map = Stream.of(availables).map(AvailableVideoItemModel::create)
                .collect(Collectors.groupingBy(AvailableVideoItemModel::getViewed));

        final List<AvailableVideoItemModel> viewed = map.get(true);
        final List<AvailableVideoItemModel> news = map.get(false);
        final List<WatchedVideoItemModel> history = Stream.of(historyResponse).map(WatchedVideoItemModel::create).collect(Collectors.toList());
        final List<VideoSectionModel> sections = new ArrayList<>();

        if (viewed != null && !viewed.isEmpty()) {
            sections.add(VideoSectionModel.create(VideoSectionModel.VIDEOS_ENTRY, mContext.getString(R.string.text_your_entries), viewed));
        }
        if (news != null && !news.isEmpty()) {
            sections.add(VideoSectionModel.create(VideoSectionModel.VIDEOS_NEW, mContext.getString(R.string.text_lets_jinglz), news));
        }
        if (history != null && !history.isEmpty()) {
            sections.add(VideoSectionModel.create(VideoSectionModel.VIDEOS_HISTORY, mContext.getString(R.string.text_watched), history));
        }


        return sections;
    }

    /**
     * method with specified data. to create abstract class {@link CoinsHistoryModel}
     * @param data of CoinsHistoryResponse type
     * @return {@code CoinsHistoryModel.create(data.today(), data.week(), data.month(), data.total())}
     */
    private CoinsHistoryModel mapCoinsResponse(CoinsHistoryResponse data) {
        return CoinsHistoryModel.create(data.today(), data.week(), data.month(), data.total());
    }

    /**
     * track event for list of {@code videoResponses}.
     *
     * @param videoResponses
     */
    private void logVideos(List<VideoResponse> videoResponses) {
        mAnalitycsFacade.trackEvent(Event.VIDEO_LIST_RESPONSE, VideoCountLogRequest.create(videoResponses));
    }
}

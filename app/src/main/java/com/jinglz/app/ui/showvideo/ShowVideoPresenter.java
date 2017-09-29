package com.jinglz.app.ui.showvideo;

import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.showvideo.ShowVideoInteractor;
import com.jinglz.app.data.local.event.CancelVideoDialogEvent;
import com.jinglz.app.data.local.event.VideoWatchedSuccessEvent;
import com.jinglz.app.data.network.models.log.Loggable;
import com.jinglz.app.data.network.models.log.VideoLogRequest;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.showvideo.models.ShowVideoModel;
import com.longtailvideo.jwplayer.core.PlayerState;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class ShowVideoPresenter extends BasePresenter<ShowVideoView> {

    @Inject ShowVideoInteractor mShowVideoInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    private final String mVideoId;

    private final String mContestId;
    private final boolean mWithRules;
    private ShowVideoModel mVideo;
    private Loggable mLoggableVideo;
    private boolean mLowVolumeShowed;

    private boolean mBadFaceShowed;
    private boolean mVideoCompleted;
    private boolean mIsVideoStarted = false;

    private boolean mLowVolume;

    private boolean mBadFace;
    private Subscription mSubscribeFace;

    /**
     * Constructs new ShowVideoPresenter with specified videoId, contestId and withRules parameters.
     * its is used to initialize {@see mVideoId},{@see mContestId} and {@see mWithRules}.
     *
     * @param videoId String contains video id
     * @param contestId String contains contest id
     * @param withRules boolean if true show video with rules, without rules otherwise
     */
    public ShowVideoPresenter(String videoId, String contestId, boolean withRules) {
        App.get().getSessionComponent().inject(this);
        RxBus.get().register(this);
        mVideoId = videoId;
        mContestId = contestId;
        mWithRules = withRules;
    }

    /**
     * Note, unregister RxBus on destroy.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWithRules && !mVideoCompleted && mIsVideoStarted) {
            mAnalyticsFacade.trackEvent(Event.ABANDON_VIDEO, mLoggableVideo);
        }

        RxBus.get().unregister(this);
    }

    /**
     * Overridden method to fetch video of specified video and contest id by calling
     * {@link ShowVideoInteractor#getVideo(String, String)}. on success assign values to
     * {@see mVideo}, {@see mLoggableVideo} and show video by {@link #showVideo(ShowVideoModel)},
     * {@link #handleError(Throwable)} otherwise
     */
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mShowVideoInteractor.getVideo(mVideoId, mContestId)
                .compose(bindToLifecycle().forSingle())
                .doOnSuccess(video -> mVideo = video)
                .doOnSuccess(video -> mLoggableVideo = VideoLogRequest.create(video))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showVideo, this::handleError);
    }

    @Subscribe
    public void onDialogCanceled(CancelVideoDialogEvent event) {
        getViewState().close();
    }

    /**
     * it will check if {@see mWithRules} is true, it will check for
     * {@link ShowVideoInteractor#lowVolumeLevelObservable()}. if true
     * {@link #handleLowVolumeLevel(boolean)} will be called to handle volume,
     * {@link #handleError(Throwable)} otherwise.
     */
    public void onVideoReady() {
        if (mWithRules) {
            mShowVideoInteractor.lowVolumeLevelObservable()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleLowVolumeLevel, this::handleError);
        }
    }

    /**
     * Check for face if {@code permission} and {@see mWithRules} is true. it checks {@link ShowVideoInteractor#badFaceObservable()}
     * and handle face functionality by calling {@link #handleBadFace(boolean)} if true, {@link #handleError(Throwable)}
     * otherwise.
     *
     * @param permission contains true if camera permissions are granted
     */
    public void onCameraReady(boolean permission) {
        if (permission) {
            if (mWithRules) {
                if (mSubscribeFace != null && !mSubscribeFace.isUnsubscribed()) {
                    mSubscribeFace.unsubscribe();
                }
                mSubscribeFace = mShowVideoInteractor.badFaceObservable()
                        .compose(bindToLifecycle())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleBadFace, this::handleError);
            }
        } else {
            getViewState().showPermissionError();
        }
    }

    /**
     * on video completion calls {@link ShowVideoInteractor#takePartInContest(ShowVideoModel)}. it will show
     * contest screen corresponding to that video and handle process by calling {@link #handleTakePartInContest()}
     * if true, {@link #handleError(Throwable)} otherwise.
     */
    public void onVideoCompeted() {
        mVideoCompleted = true;
        if (mWithRules) {
            mAnalyticsFacade.trackEvent(Event.FINISH_WATCHING_VIDEO, mLoggableVideo);
            getViewState().contestSuccess(mVideo);
            mShowVideoInteractor.takePartInContest(mVideo)
                    .compose(bindToLifecycle().forCompletable())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleTakePartInContest, this::handleError);
        } else {
            getViewState().close();
        }
    }

    public void onBufferChange(PlayerState playerState) {
        if (playerState == PlayerState.BUFFERING) {
            mAnalyticsFacade.trackEvent(Event.VIDEO_PAUSED_TO_BUFFER, mLoggableVideo);
        }
    }

    /**
     * method to post new instance of {@link VideoWatchedSuccessEvent}
     */
    private void handleTakePartInContest() {
        RxBus.get().post(new VideoWatchedSuccessEvent());
    }

    /**
     * method with specified lowLevel boolean to play and pause video if volume is low.
     * it will show lowVolume alert and show eye contact alert if {@see mBadFace} and
     * {@see mBadFaceShowed} are true and {@code lowLevel} is false.
     * it will call {@link ShowVideoView#showLowVolumeAlert(boolean)} if {@see mLowVolumeShowed}
     * is true, {@link ShowVideoView#showFirstLowVolumeAlert(boolean)} otherwise.
     *
     * @param lowLevel true if volume is low
     */
    private void handleLowVolumeLevel(boolean lowLevel) {
        if (mVideoCompleted) {
            return;
        }
        mLowVolume = lowLevel;
        toggleVideo();
        if (mLowVolumeShowed) {
            getViewState().showLowVolumeAlert(lowLevel);
            if (!lowLevel && mBadFace && mBadFaceShowed) {
                getViewState().showEyeContactAlert(true);
            }
        } else {
            getViewState().showFirstLowVolumeAlert(lowLevel);
            mLowVolumeShowed = lowLevel;
        }
    }

    /**
     * This method is used to handle bad face functionality. video will play and pause accordingly.
     * it will show eye contact alert and low volume alert if {@see mLowVolume} and {@see mLowVolumeShowed}
     * are true and {@code badFace} is false.
     * it will call {@link ShowVideoView#showEyeContactAlert(boolean)} if {@see mBadFaceShowed}
     * is true, {@link ShowVideoView#showFirstEyeContactAlert(boolean)} otherwise.
     *
     * @param badFace true if bad face detected
     */
    private void handleBadFace(boolean badFace) {
        if (mVideoCompleted) {
            return;
        }
        mBadFace = badFace;
        toggleVideo();
        if (mBadFaceShowed) {
            getViewState().showEyeContactAlert(badFace);
            if (!badFace && mLowVolume && mLowVolumeShowed) {
                getViewState().showLowVolumeAlert(true);
            }
            mAnalyticsFacade.trackEvent(Event.GAZE_NOT_DETECTED, mLoggableVideo);
        } else {
            mAnalyticsFacade.trackEvent(Event.FACE_NOT_DETECTED, mLoggableVideo);
            getViewState().showFirstEyeContactAlert(badFace);
            mBadFaceShowed = badFace;
        }
    }

    /**
     * call this method to play and pause video after testing conditions of {@see mLowVolume}
     * and {@see mBadFace}.
     */
    private void toggleVideo() {
        if (mIsVideoStarted) {
            mAnalyticsFacade.trackEvent(Event.START_PLAYING_VIDEO, mLoggableVideo);
            mIsVideoStarted = true;
        }

        getViewState().playVideo(!mLowVolume && !mBadFace);
    }

    /**
     * call this method to show video.
     * @param video {@link ShowVideoModel} object
     */
    private void showVideo(ShowVideoModel video) {
        getViewState().showVideo(video);
    }

    /**
     * call this method to handle errors
     * @param throwable Throwable instance
     */
    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }
}

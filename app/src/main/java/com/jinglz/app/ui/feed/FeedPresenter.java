package com.jinglz.app.ui.feed;

import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.feed.FeedInteractor;
import com.jinglz.app.business.tutorial.TutorialInteractor;
import com.jinglz.app.data.local.event.FeedTutorialsEvent;
import com.jinglz.app.data.local.event.UpdateBalanceEvent;
import com.jinglz.app.data.local.event.VideoWatchedSuccessEvent;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;
import com.jinglz.app.ui.feed.models.CoinsHistoryModel;
import com.jinglz.app.ui.feed.models.TutorialSectionModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class FeedPresenter extends BasePresenter<FeedView> {

    private static final String TAG = "FeedPresenter";

    @Inject FeedInteractor mFeedInteractor;
    @Inject TutorialInteractor mTutorialInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    private Iterator<TutorialSectionModel> mTutorialSections;

    public FeedPresenter() {
        App.get().getSessionComponent().inject(this);
        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mFeedInteractor.checkInternet()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(status -> getViewState().showNoInternetError(!status))
                .filter(status -> status)
                .subscribe(status -> loadFeed(false), this::handleErrors);
    }

    @Subscribe
    public void onShowAllFeedTutorials(FeedTutorialsEvent event) {
        mTutorialInteractor.getAllFeedTutorials()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sections -> {
                    if (!sections.isEmpty()) {
                        mTutorialSections = sections.iterator();
                        showTutorialSection();
                    }
                }, this::handleErrors);
    }

    @Subscribe
    public void onVideoWatchedSuccess(VideoWatchedSuccessEvent event) {
        loadVideos();
    }

    @Subscribe
    public void onBalanceUpdated(UpdateBalanceEvent event) {
        getViewState().setTotalBalance(event.balance());
    }

    public void loadFeed(boolean isRefresh) {
        if (isRefresh) {
            mAnalyticsFacade.trackEvent(Event.REFRESH);
        }
        loadBalance();
        loadVideos();
    }

    public void loadBalance() {
        mFeedInteractor.getCoinsHistory()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showBalance, this::handleErrors);
    }

    /**
     *
     */
    public void loadVideos() {
        mAnalyticsFacade.trackEvent(Event.VIDEO_LIST_REQUEST);
        getViewState().setRefreshing(true);
        mFeedInteractor.getVideos()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showVideos, this::handleErrors);
    }

    public void onFeedLoaded() {
        mTutorialInteractor.getAvailableFeedTutorials()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sections -> {
                    if (!sections.isEmpty() && (mTutorialSections == null || !mTutorialSections.hasNext())) {
                        mTutorialSections = sections.iterator();
                        showTutorialSection();
                    }
                }, this::handleErrors);
    }

    public void nextTutorialSection(@VideoSectionModel.SectionType int type, boolean completeSection) {
        if (completeSection) {
            mTutorialInteractor.completeSection(type)
                    .compose(bindToLifecycle().forCompletable())
                    .subscribe(() -> {
                    }, this::handleErrors);
        }
        showTutorialSection();
    }

    public void completeAllTutorials() {
        getViewState().closeTutorial();
        mTutorialInteractor.completeAllSections()
                .compose(bindToLifecycle().forCompletable())
                .subscribe(() -> {
                }, this::handleErrors);
    }

    public void onClickVideo(AvailableVideoItemModel item) {
        if (item.getViewed()) {
            getViewState().openVideoWithoutRules(item);
        } else {
            mTutorialInteractor.videoRulesViewed()
                    .compose(bindToLifecycle().forSingle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(viewed -> {
                        if (viewed) {
                            getViewState().openVideoWithRules(item);
                        } else {
                            getViewState().showVideoRules(item);
                        }
                    }, this::handleErrors);
        }
    }

    public void onTimerFinished(AvailableVideoItemModel item) {
        loadVideos();
    }

    private void showTutorialSection() {
        getViewState().closeTutorial();
        if (mTutorialSections != null && mTutorialSections.hasNext()) {
            getViewState().showTutorialSection(mTutorialSections.next());
        }
    }

    private void showVideos(List<VideoSectionModel> sections) {
        getViewState().setRefreshing(false);
        getViewState().setVideos(sections);
    }

    private void handleErrors(Throwable throwable) {
        throwable.printStackTrace();
        getViewState().setRefreshing(false);
    }

    private void showBalance(CoinsHistoryModel coinsHistory) {
        getViewState().clearBalance();
        getViewState().animateTotalBalance(coinsHistory.current());
        getViewState().setTotalBalance(coinsHistory.current());
        getViewState().setTodayCoins(coinsHistory.today());
        getViewState().setWeekCoins(coinsHistory.week());
        getViewState().setMonthCoins(coinsHistory.month());
    }
}

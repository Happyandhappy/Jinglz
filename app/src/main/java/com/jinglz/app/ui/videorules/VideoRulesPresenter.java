package com.jinglz.app.ui.videorules;

import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.jinglz.app.App;
import com.jinglz.app.business.tutorial.TutorialInteractor;
import com.jinglz.app.data.local.event.VideoRulesGotItEvent;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class VideoRulesPresenter extends BasePresenter<VideoRulesView> {

    @Inject
    TutorialInteractor mTutorialInteractor;

    private final boolean mShowVideoAfterRules;
    private final String mVideoId;
    private final String mContestId;
    private int mPageCount;
    private int mCurrentPage;

    /**
     * Constructor of VideoRulesPresenter
     * @param showVideoAfterRules a boolean value to show rules after playing video.
     * @param videoId String value that contains video id which will play.
     * @param contestId String value which contain contest id.
     * @param pageCount Integer value that contains number of pages
     */
    public VideoRulesPresenter(boolean showVideoAfterRules,
                               String videoId,
                               String contestId,
                               int pageCount) {
        App.get().getSessionComponent().inject(this);
        RxBus.get().register(this);
        mShowVideoAfterRules = showVideoAfterRules;
        mVideoId = videoId;
        mContestId = contestId;
        mPageCount = pageCount;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Subscribe
    public void onVideoRulseGot(VideoRulesGotItEvent event) {
        if (mShowVideoAfterRules) {
            getViewState().openVideo(mVideoId, mContestId);
        } else {
            getViewState().close();
        }
        addSubscription(mTutorialInteractor.completeVideoRules()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                }, this::handleErrors));
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showBackButton(false);
    }

    /**
     * onNextClick() will select next page
     * onBackClick() will redirect to previously selected page.
     *
     * onPageChanged() contains a currently selected page.
     * Handle what action will be perform when a new page is select
     **/
    public void onNextClick() {
        getViewState().setSelectedPage(mCurrentPage + 1);
    }

    public void onBackClick() {
        getViewState().setSelectedPage(mCurrentPage - 1);
    }

    public void onPageChanged(int currentPage) {
        mCurrentPage = currentPage;
        getViewState().showBackButton(mCurrentPage > 0);
        getViewState().showNextButton(mCurrentPage < mPageCount - 1);
    }


    /**
     * It will handle errors found while playing video rules
     * @param throwable Contains a String value which will show an error message
     */
    private void handleErrors(Throwable throwable) {
        throwable.printStackTrace();
    }
}

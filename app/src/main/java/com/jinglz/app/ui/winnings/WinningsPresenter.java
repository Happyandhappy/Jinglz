package com.jinglz.app.ui.winnings;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.feed.FeedInteractor;
import com.jinglz.app.business.profile.ProfileInteractor;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A presenter class fot {@link WinningsActivity}
 */
@InjectViewState
public class WinningsPresenter extends BasePresenter<WinningsView> {

    private static final String TAG = "WinningsPresenter";
    @Inject FeedInteractor mFeedInteractor;
    @Inject ProfileInteractor mProfileInteractor;

    private final String mVideoId;
    private final String mContestId;

    /**
     * Constructor for WinningsPresenter
     * @param videoId String contains value of video which will play
     * @param contestId String contains contest id with every video id
     */
    public WinningsPresenter(String videoId, String contestId) {
        App.get().getSessionComponent().inject(this);
        mVideoId = videoId;
        mContestId = contestId;
    }


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadUsersRank(mContestId);
    }

    public void onFooterClick() {
        getViewState().openVideo(mVideoId, mContestId);
    }

    /**
     * It will show user winning rank.
     * @param contestId String contains value of contest id.
     */
    private void loadUsersRank(String contestId) {
        final Subscription subscription = mFeedInteractor.getContestUserRanking(contestId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(action -> getViewState().hideProgressBar())
                .subscribe(contestLeadershipRecords -> getViewState().setRanking(contestLeadershipRecords),
                           throwable -> Log.e(TAG, "loadUsersRank: ", throwable));
        addSubscription(subscription);
    }
}

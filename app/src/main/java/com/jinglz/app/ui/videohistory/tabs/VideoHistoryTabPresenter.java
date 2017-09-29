package com.jinglz.app.ui.videohistory.tabs;

import com.jinglz.app.Constants;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.feed.models.VideoItemModel;

import java.util.List;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Presenter class for {@link VideoHistoryTabFragment} fragment
 */
public abstract class VideoHistoryTabPresenter extends BasePresenter<VideoHistoryTabView> {

    private int mCurrentPage = Constants.FIRST_SERVER_PAGE;
    private boolean mIsLoading;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadHeader();
    }

    public void onRefresh() {
        mCurrentPage = Constants.FIRST_SERVER_PAGE;
        loadVideos();
        loadHeader();
    }

    public void onLoadMore() {
        getViewState().showLoadMoreIndicator();
        loadVideos();
    }

    protected abstract Single<List<VideoItemModel>> getVideos(int page);

    protected abstract Single<List<GameStatistic>> getHeader();

    private void loadHeader() {
        getHeader()
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleHeaderLoaded, this::handleError);
    }

    /**
     * Load videos with progress bar
     * to handle successful response {@link #handleVideoLoaded(List)} is called
     * to handle errors {@link #handleError(Throwable)} is called. It will throw an error message.
     * to handle videos header for coins {@link #handleHeaderLoaded(List)} is called
     */
    private void loadVideos() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (mCurrentPage == Constants.FIRST_SERVER_PAGE) {
            getViewState().showProgress();
        }
        getVideos(mCurrentPage).compose(bindToLifecycle().forSingle()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleVideoLoaded, this::handleError);
    }

    private void handleVideoLoaded(List<VideoItemModel> videos) {
        loadingFinished();
        if (videos.isEmpty() && mCurrentPage == Constants.FIRST_SERVER_PAGE) {
            getViewState().showListPlaceholder();
        } else if (!videos.isEmpty()) {
            getViewState().hideListPlaceholder();
            getViewState().showVideos(videos, mCurrentPage == Constants.FIRST_SERVER_PAGE);
            mCurrentPage++;
        }
    }

    private void handleHeaderLoaded(List<GameStatistic> coins) {
        if (coins.isEmpty()) {
            return;
        }
        getViewState().setHeader(coins);
    }

    private void handleError(Throwable throwable) {
        loadingFinished();
        throwable.printStackTrace();
    }

    private void loadingFinished() {
        mIsLoading = false;
        getViewState().hideProgress();
        getViewState().hideLoadMoreIndicator();
    }
}

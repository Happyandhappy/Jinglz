package com.jinglz.app.ui.videohistory.tabs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.feed.adapters.WatchedVideoListener;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;
import com.jinglz.app.ui.videohistory.adapters.VideoHistoryAdapter;
import com.jinglz.app.ui.winnings.WinningsActivity;
import com.jinglz.app.utils.recycler.PaginationScrollListener;

import java.util.List;

public abstract class VideoHistoryTabFragment extends BaseFragment implements VideoHistoryTabView, SwipeRefreshLayout.OnRefreshListener,
        PaginationScrollListener.OnRecyclerViewScrolledToPageListener,
        WatchedVideoListener {

    @InjectPresenter VideoHistoryTabPresenter mPresenter;

    protected VideoHistoryAdapter mAdapter;
    protected PaginationScrollListener mPaginationScrollListener;

    @ProvidePresenter
    public abstract VideoHistoryTabPresenter providePresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showVideos(List<VideoItemModel> videos, boolean clear) {
        mAdapter.setItems(videos, clear);
    }

    @Override
    public void onRefresh() {
        mPaginationScrollListener.reset();
        mPresenter.onRefresh();
    }

    @Override
    public void onRecyclerViewScrolledToEnd() {
        mPresenter.onLoadMore();
    }

    /**
     * showProgress is called to show progress bar and
     * and hideProgress is called to hide progress bar and
     */
    @Override
    public void showProgress() {
        if (!isRefreshing()) {
            setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (isRefreshing()) {
            setRefreshing(false);
        }
    }

    @Override
    public void showLoadMoreIndicator() {
        mAdapter.setFooterVisibility(true);
    }

    @Override
    public void hideLoadMoreIndicator() {
        mAdapter.setFooterVisibility(false);
    }

    /**
     * start activity with intent from {@link WinningsActivity}
     * @param item {@link WatchedVideoItemModel } contains Watched video data
     */
    @Override
    public void onClickWinnings(WatchedVideoItemModel item) {
        startActivity(WinningsActivity.getIntent(getContext(), item.getId(), item.getContestId()));
    }

    /**
     * @param refreshing a boolean value to set list refreshing true/false.
     */
    protected abstract void setRefreshing(boolean refreshing);

    /**
     * @return a boolean value if list is refreshing true/false.
     */
    protected abstract boolean isRefreshing();
}

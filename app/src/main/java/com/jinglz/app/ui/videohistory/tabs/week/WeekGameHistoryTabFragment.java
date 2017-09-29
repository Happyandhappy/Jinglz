package com.jinglz.app.ui.videohistory.tabs.week;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jinglz.app.App;
import com.jinglz.app.Constants;
import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.ui.videohistory.widgets.WeekCoinHistoryLayout;
import com.jinglz.app.ui.videohistory.adapters.VideoHistoryAdapter;
import com.jinglz.app.ui.videohistory.tabs.VideoHistoryTabFragment;
import com.jinglz.app.ui.videohistory.tabs.VideoHistoryTabPresenter;
import com.jinglz.app.utils.recycler.GridSpaceItemDecoration;
import com.jinglz.app.utils.recycler.PaginationScrollListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Tab fragment for Week history of watched videos.
 */
public class WeekGameHistoryTabFragment extends VideoHistoryTabFragment {

    @BindView(R.id.list_videos) RecyclerView mListVideos;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.text_empty) TextView mTextEmpty;
    @BindView(R.id.layout_no_history) FrameLayout mLayoutNoHistory;
    @BindDimen(R.dimen.list_divider_size) int mDividerSize;

    @Inject ImageLoader mImageLoader;

    /**
     * Constructor of WeekGameHistoryTabFragment
     * @return new Instance of WeekGameHistoryTabFragment
     */
    public static WeekGameHistoryTabFragment newInstance() {
        return new WeekGameHistoryTabFragment();
    }

    /**
     *
     * @return WeekVideoHistoryTabPresenter
     */
    @Override
    public VideoHistoryTabPresenter providePresenter() {
        return new WeekVideoHistoryTabPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getSessionComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_game_history_tab, container, false);
        bind(root);
        initViews();
        return root;
    }

    /**
     * To show Place holder of list showListPlaceholder() is called.
     * To hide Place holder of list hideListPlaceholder() is called.
     */
    @Override
    public void showListPlaceholder() {
        mLayoutNoHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListPlaceholder() {
        mLayoutNoHistory.setVisibility(View.GONE);
    }

    @Override
    public void setHeader(List<GameStatistic> days) {
        mAdapter.setCoinsHistory(days);
    }

    /**
     * Set list refreshing
     * @param refreshing a Boolean value that set list refreshable true or false
     */
    @Override
    protected void setRefreshing(boolean refreshing) {
        mRefreshLayout.setRefreshing(refreshing);
    }

    /**
     * @return a boolean true if list is refreshing
     *         and return false if list is not refreshing
     */
    @Override
    protected boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }

    /**
     * Initialize view of WeekGameHistoryTabFragment
     */
    private void initViews() {
        mTextEmpty.setText(R.string.text_video_history_week_empty);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Constants.COLUMNS_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isFooterPosition(position) || mAdapter.isHeaderPosition(position))
                        ? Constants.COLUMNS_COUNT
                        : 1;
            }
        });
        mListVideos.setLayoutManager(layoutManager);
        mListVideos.setHasFixedSize(true);
        mAdapter = new VideoHistoryAdapter(mImageLoader, this);
        mListVideos.setAdapter(mAdapter);

        mListVideos.addItemDecoration(new GridSpaceItemDecoration(mDividerSize));

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.accent);
        mPaginationScrollListener = new PaginationScrollListener(this);
        mListVideos.addOnScrollListener(mPaginationScrollListener);

        final WeekCoinHistoryLayout header = new WeekCoinHistoryLayout(getContext());
        mAdapter.setHeaderView(header);
    }
}

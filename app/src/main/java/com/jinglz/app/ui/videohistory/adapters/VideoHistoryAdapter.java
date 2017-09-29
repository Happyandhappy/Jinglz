package com.jinglz.app.ui.videohistory.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.ui.base.UltimateAdapter;
import com.jinglz.app.ui.feed.adapters.WatchedVideoListener;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.videohistory.adapters.delegates.NotTodayVideoHistoryAdapterDelegate;
import com.jinglz.app.ui.videohistory.adapters.delegates.TodayVideoHistoryAdapterDelegate;
import com.jinglz.app.ui.videohistory.widgets.PeriodicCoinHistoryLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoHistoryAdapter extends UltimateAdapter implements UltimateAdapter.HeaderInterface,
                                                                    UltimateAdapter.FooterInterface {

    private static final int ITEM_TODAY = 2;
    private static final int ITEM_NOT_TODAY = 3;

    private final ArrayList<VideoItemModel> mItems = new ArrayList<>();
    private final ArrayList<GameStatistic> mCoinsHistory = new ArrayList<>();

    private final TodayVideoHistoryAdapterDelegate mTodayVideoHistoryAdapterDelegate;
    private final NotTodayVideoHistoryAdapterDelegate mNotTodayVideoHistoryAdapterDelegate;

    private PeriodicCoinHistoryLayout mHeaderView;

    /**
     * Constructs new VideoHistoryAdapter with specified  imageLoader and watchedListener.
     * it is used to initialize {@see imageLoader} and {@see watchedListener}.
     * @param imageLoader to display Images from different sources
     * @param watchedListener to handle watched video click events
     */
    public VideoHistoryAdapter(ImageLoader imageLoader, WatchedVideoListener watchedListener) {
        mTodayVideoHistoryAdapterDelegate = new TodayVideoHistoryAdapterDelegate(ITEM_TODAY, imageLoader, watchedListener);
        mNotTodayVideoHistoryAdapterDelegate = new NotTodayVideoHistoryAdapterDelegate(ITEM_NOT_TODAY, imageLoader, watchedListener);
    }

    public void setItems(List<VideoItemModel> items, boolean clear) {
        if (clear) {
            mItems.clear();
        }
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void setHeaderView(PeriodicCoinHistoryLayout headerView) {
        mHeaderView = headerView;
    }

    public void setCoinsHistory(List<GameStatistic> statistics) {
        mCoinsHistory.clear();
        mCoinsHistory.addAll(statistics);
        setHeaderVisibility(true);
    }

    /**
     * method to clear list
     */
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getDataViewType(int dataPosition) {
        if (mTodayVideoHistoryAdapterDelegate.isForViewType(mItems, dataPosition)) {
            return mTodayVideoHistoryAdapterDelegate.getViewType();
        } else if (mNotTodayVideoHistoryAdapterDelegate.isForViewType(mItems, dataPosition)) {
            return mNotTodayVideoHistoryAdapterDelegate.getViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder getDataViewHolder(@NonNull ViewGroup parent, int dataViewType) {
        if (mTodayVideoHistoryAdapterDelegate.getViewType() == dataViewType) {
            return mTodayVideoHistoryAdapterDelegate.onCreateViewHolder(parent);
        } else if (mNotTodayVideoHistoryAdapterDelegate.getViewType() == dataViewType) {
            return mNotTodayVideoHistoryAdapterDelegate.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("No Delegate Found");
    }

    @Override
    public void bindDataViewHolder(@NonNull RecyclerView.ViewHolder vh, int dataPosition) {
        final int viewType = getDataViewType(dataPosition);
        if (mTodayVideoHistoryAdapterDelegate.getViewType() == viewType) {
            mTodayVideoHistoryAdapterDelegate.onBindViewHolder(mItems, dataPosition, vh);
        } else if (mNotTodayVideoHistoryAdapterDelegate.getViewType() == viewType) {
            mNotTodayVideoHistoryAdapterDelegate.onBindViewHolder(mItems, dataPosition, vh);
        }
    }

    @Override
    public HeaderViewHolder getHeaderViewHolder(ViewGroup parent) {
        //Gone header and swipeRefreshLayout issue
        if (mHeaderView == null) {
            final View view = new View(parent.getContext());
            view.setMinimumHeight(1);
            return new HeaderHolder(view);
        }
        return new HeaderHolder(mHeaderView);
    }

    @Override
    public void bindHeaderViewHolder(RecyclerView.ViewHolder vh) {
        if (!mCoinsHistory.isEmpty()) {
            ((HeaderHolder) vh).bind(mCoinsHistory);
        }
    }

    @Override
    public FooterViewHolder getFooterViewHolder(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FooterHolder(inflater.inflate(R.layout.footer_load_more, parent, false));
    }

    @Override
    public void bindFooterViewHolder(RecyclerView.ViewHolder vh) {
        //no-op
    }

    /**
     * @return Size of winning list
     */
    @Override
    public int getDataSize() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public long getDataId(int dataPosition) {
        return dataPosition;
    }

    /**
     * Footer view holder of winning list
     */
    static class FooterHolder extends FooterViewHolder {

        @BindView(R.id.progress_bar) ProgressBar progressBar;

        public FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void hideFooter(boolean hide) {
            progressBar.setVisibility(hide ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Header view holder of winning list
     */
    static class HeaderHolder extends HeaderViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(List<GameStatistic> statistics) {
            ((PeriodicCoinHistoryLayout) itemView).setCoins(statistics);
        }

        @Override
        public void hideHeader(boolean hide) {
        }
    }
}

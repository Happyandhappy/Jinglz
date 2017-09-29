package com.jinglz.app.ui.feed.adapters.delegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.ui.base.AdapterDelegate;
import com.jinglz.app.ui.feed.adapters.VideoTutorial;
import com.jinglz.app.ui.feed.adapters.WatchedVideoListener;
import com.jinglz.app.ui.feed.models.TutorialModel;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;
import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;
import com.jinglz.app.utils.NumberUtils;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchedVideoAdapterDelegate implements AdapterDelegate<List<VideoItemModel>> {

    private final ImageLoader mImageLoader;
    private final int mViewType;
    private final WatchedVideoListener mListener;

    /**
     * Constructs new WatchedVideoAdapterDelegate with specified viewType, imageLoader and listener.
     * it is used to initialize {@see mImageLoader}, {@see mViewType} and {@see mListener}.
     *
     * @param viewType it is used to set the type of view to be displayed
     * @param imageLoader  to display Images from different sources
     * @param listener to handle video click events
     */
    public WatchedVideoAdapterDelegate(@VideoSectionModel.SectionType int viewType,
                                       ImageLoader imageLoader,
                                       WatchedVideoListener listener) {
        mImageLoader = imageLoader;
        mViewType = viewType;
        mListener = listener;
    }

    public int getViewType() {
        return mViewType;
    }

    @Override
    public boolean isForViewType(@NonNull List<VideoItemModel> items, int position) {
        final VideoItemModel item = items.get(position);
        return item instanceof WatchedVideoItemModel;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new WatchedVideoViewHolder(LayoutInflater.from(parent.getContext())
                                                  .inflate(R.layout.item_feed_video_watched, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<VideoItemModel> items,
                                 int position,
                                 @NonNull RecyclerView.ViewHolder holder) {
        final WatchedVideoViewHolder vh = (WatchedVideoViewHolder) holder;
        final WatchedVideoItemModel item = (WatchedVideoItemModel) items.get(position);
        Log.d("as", "onBindViewHolder: " + position + " " + item);
        vh.bind(item);
    }

    class WatchedVideoViewHolder extends SectioningAdapter.ItemViewHolder
            implements VideoTutorial {

        @BindView(R.id.image_preview) ImageView imagePreview;
        @BindView(R.id.text_name) TextView textName;
        @BindView(R.id.text_sponsor) TextView textSponsor;
        @BindView(R.id.text_badge_place) TextView textBadgePlace;
        @BindView(R.id.text_badge_coins) TextView textBadgeCoins;
        @BindString(R.string.text_pending_placeholder) String mPendingPlaceholder;

        WatchedVideoItemModel item;

        public WatchedVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initListener();
        }

        public void bind(WatchedVideoItemModel item) {
            this.item = item;
            mImageLoader.displayImage(item.getImageUrl(), imagePreview);
            textName.setText(item.getName());
            textSponsor.setText(item.getSponsor());
            textBadgeCoins.setText(item.getCoins() != null ?
                                           NumberUtils.toPrettyNumber(this.item.getCoins())
                                           : mPendingPlaceholder);
            textBadgePlace.setText(item.getPosition() != null ?
                                           String.valueOf(this.item.getPosition())
                                           : mPendingPlaceholder);
        }

        @Override
        public View getTutorialView(@TutorialModel.TutorialType int type) {
            switch (type) {
                case TutorialModel.TUTORIAL_TYPE_COINS:
                    return textBadgeCoins;
                case TutorialModel.TUTORIAL_TYPE_POSITION:
                    return textBadgePlace;
            }
            return null;
        }

        /**
         * This method is used to initialize listeners.
         */
        private void initListener() {
            itemView.setOnClickListener(v -> mListener.onClickWinnings(item));
        }
    }
}

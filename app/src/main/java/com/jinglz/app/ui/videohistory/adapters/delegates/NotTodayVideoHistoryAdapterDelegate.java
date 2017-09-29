package com.jinglz.app.ui.videohistory.adapters.delegates;

import android.support.annotation.NonNull;

import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.ui.feed.adapters.WatchedVideoListener;
import com.jinglz.app.ui.feed.adapters.delegates.WatchedVideoAdapterDelegate;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.videohistory.models.NotTodayWatchedVideoItemModel;

import java.util.List;

public class NotTodayVideoHistoryAdapterDelegate extends WatchedVideoAdapterDelegate {

    /**
     * Constructs new NotTodayVideoHistoryAdapterDelegate with specified viewType, imageLoader and listener.
     * it is used to initialize {@see mImageLoader}, {@see mViewType} and {@see mListener}.
     *
     * @param viewType it is used to set the type of view to be displayed
     * @param imageLoader  to display Images from different sources
     * @param listener to handle video click events
     */
    public NotTodayVideoHistoryAdapterDelegate(int viewType,
                                               ImageLoader imageLoader,
                                               WatchedVideoListener listener) {
        super(viewType, imageLoader, listener);
    }

    @Override
    public boolean isForViewType(@NonNull List<VideoItemModel> items, int position) {
        final VideoItemModel item = items.get(position);
        return item instanceof NotTodayWatchedVideoItemModel;
    }
}

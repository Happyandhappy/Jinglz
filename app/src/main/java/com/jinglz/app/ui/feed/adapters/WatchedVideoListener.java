package com.jinglz.app.ui.feed.adapters;

import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;

/**
 * This interface can be implemented to deal with click events by passing
 * specified item from {@link WatchedVideoItemModel} {@link com.jinglz.app.ui.videohistory.tabs.VideoHistoryTabFragment}
 */
public interface WatchedVideoListener {

    void onClickWinnings(WatchedVideoItemModel item);

}

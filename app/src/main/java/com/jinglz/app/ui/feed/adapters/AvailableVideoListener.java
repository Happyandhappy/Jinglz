package com.jinglz.app.ui.feed.adapters;

import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;

/**
 * Interface is used to handle different listeners for video.
 * it can be implemented by class that needs to deal with video, their click events and
 * video timer completion.
 */
public interface AvailableVideoListener {

    void onClickLearnMore(AvailableVideoItemModel item);

    void onClickVideo(AvailableVideoItemModel item);

    void onTimerFinished(AvailableVideoItemModel item);
}

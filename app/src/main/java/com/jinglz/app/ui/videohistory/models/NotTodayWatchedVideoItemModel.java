package com.jinglz.app.ui.videohistory.models;

import android.support.annotation.Nullable;

import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;
import com.jinglz.app.utils.DateUtils;

import java.util.Date;

public class NotTodayWatchedVideoItemModel extends WatchedVideoItemModel {

    /**
     * Constructors for NotTodayWatchedVideoItemModel
     * @param response Contains VideoResponse from server
     * @return new NotTodayWatchedVideoItemModel
     */
    public static NotTodayWatchedVideoItemModel create(VideoResponse response) {
        return NotTodayWatchedVideoItemModel.create(response.id(),
                                                    response.videoUrl(),
                                                    response.fileName(),
                                                    response.name(),
                                                    response.imageUrl(),
                                                    response.infoLink(),
                                                    DateUtils.fromServerFormat(response.contestDate()),
                                                    response.contestParticipants(),
                                                    response.contestId(),
                                                    response.jackPot(),
                                                    response.sponsor(),
                                                    DateUtils.fromServerFormat(response.viewedDate()),
                                                    response.position(),
                                                    response.coins());
    }

    public static NotTodayWatchedVideoItemModel create(String id,
                                                       String videoUrl,
                                                       String fileName,
                                                       String name,
                                                       String imageUrl,
                                                       String infoLink,
                                                       Date contestDate,
                                                       int contestParticipants,
                                                       String contestId,
                                                       double jackPot,
                                                       String sponsor,
                                                       Date viewedDate,
                                                       Integer position,
                                                       Integer coins) {
        return new NotTodayWatchedVideoItemModel(id, videoUrl, fileName, name, imageUrl, infoLink, contestDate,
                                                 contestParticipants,
                                                 contestId, jackPot, sponsor, viewedDate, position, coins);
    }

    public NotTodayWatchedVideoItemModel(String id,
                                         String videoUrl,
                                         String fileName,
                                         String name,
                                         String imageUrl,
                                         String infoLink,
                                         Date contestDate,
                                         int contestParticipants,
                                         String contestId,
                                         double jackPot,
                                         @Nullable String sponsor,
                                         Date viewedDate,
                                         Integer position, Integer coins) {
        super(id, videoUrl, fileName, name, imageUrl, infoLink, contestDate, contestParticipants, contestId, jackPot,
              sponsor, viewedDate, position, coins);
    }
}

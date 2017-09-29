package com.jinglz.app.ui.feed.models;

import android.support.annotation.Nullable;

import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.utils.DateUtils;

import java.util.Date;

public class WatchedVideoItemModel extends VideoItemModel {

    /**
     * it is used to create new instance of WatchedVideoItemModel with specified response.
     * @param response VideoResponse contains video details
     * @return WatchedVideoItemModel instance
     */
    public static WatchedVideoItemModel create(VideoResponse response) {
        return WatchedVideoItemModel.create(response.id(),
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

    /**
     * it is used to create new instance of WatchedVideoItemModel with specified id,
     * videoUrl, fileName, name, imageUrl, infoLink, contestDate, contestParticipants,
     * contestId, jackPot, sponsor, viewedDate, position and coins.
     *
     * @param id String variable contains id of watched video.
     * @param videoUrl String variable contains video url
     * @param fileName String variable contains name of the image file
     * @param name String variable contains name
     * @param imageUrl String variable contains image url
     * @param infoLink String variable contains information link
     * @param contestDate String variable contains contest date
     * @param contestParticipants it contains number participants in contest
     * @param contestId String variable contains id of contest
     * @param jackPot it contains amount of jackpot
     * @param sponsor String variable contains sponsor name
     * @param viewedDate contains date for video viewed at
     * @param position contains position
     * @param coins number of coins
     * @return
     */
    public static WatchedVideoItemModel create(String id,
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
        return new WatchedVideoItemModel(id, videoUrl, fileName, name, imageUrl, infoLink, contestDate, contestParticipants, contestId, jackPot, sponsor, viewedDate, position, coins);
    }

    private final Date viewedDate;
    private final Integer position;
    private final Integer coins;

    public WatchedVideoItemModel(
            String id,
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
            Integer position,
            Integer coins) {
        super(id, videoUrl, fileName, name, imageUrl, infoLink, sponsor, contestDate, contestParticipants, contestId,
              jackPot);

        this.viewedDate = viewedDate;
        this.position = position;
        this.coins = coins;
    }

    public Date getViewedDate() {
        return viewedDate;
    }

    @Nullable
    public Integer getPosition() {
        return position;
    }

    @Nullable
    public Integer getCoins() {
        return coins;
    }
}

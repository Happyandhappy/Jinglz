package com.jinglz.app.ui.feed.models;

import android.support.annotation.Nullable;

import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.utils.DateUtils;

import java.util.Date;

public class AvailableVideoItemModel extends VideoItemModel {

    /**
     * it is used to create new instance of AvailableVideoItemModel with specified response.
     * response will be used to fetch video details to create new AvailableVideoItemModel.
     * @param response VideoResponse containing video information
     * @return new instance of AvailableVideoItemModel
     */

    public static AvailableVideoItemModel create(VideoResponse response) {
        return AvailableVideoItemModel.create(response.id(),
                                              response.videoUrl(),
                                              response.fileName(),
                                              response.name(),
                                              response.imageUrl(),
                                              response.infoLink(),
                                              DateUtils.fromServerFormat(response.contestDate()),
                                              DateUtils.fromServerFormat(response.currentDate()),
                                              response.contestParticipants(),
                                              response.contestId(),
                                              response.jackPot(),
                                              response.viewed(),
                                              response.sponsor());
    }

    /**
     * This method is used to construct new AvailableVideoItemModel with specified id, videoUrl,
     * fileName, name, imageUrl, infoLink, contestDate, currentDate, contestParticipants,
     * contestId, jackPot, viewed and sponsor.
     *
     * @param id
     * @param videoUrl
     * @param fileName
     * @param name
     * @param imageUrl
     * @param infoLink
     * @param contestDate
     * @param currentDate
     * @param contestParticipants
     * @param contestId
     * @param jackPot
     * @param viewed
     * @param sponsor
     * @return AvailableVideoItemModel instance
     */
    public static AvailableVideoItemModel create(String id,
                                                 String videoUrl,
                                                 String fileName,
                                                 String name,
                                                 String imageUrl,
                                                 String infoLink,
                                                 Date contestDate,
                                                 Date currentDate,
                                                 int contestParticipants,
                                                 String contestId,
                                                 double jackPot,
                                                 boolean viewed,
                                                 String sponsor) {
        return new AvailableVideoItemModel(id, videoUrl, fileName, name, imageUrl, infoLink, contestDate, currentDate, contestParticipants,
                                           contestId, jackPot, viewed, sponsor);
    }

    private final Date currentDate;
    private final boolean viewed;
    private long lifeTime;
    private boolean showTimer = true;

    public AvailableVideoItemModel(
            String id,
            String videoUrl,
            String fileName,
            String name,
            String imageUrl,
            String infoLink,
            Date contestDate,
            Date currentDate,
            int contestParticipants,
            String contestId,
            double jackPot,
            boolean viewed,
            @Nullable String sponsor) {
        super(id, videoUrl, fileName, name, imageUrl, infoLink, sponsor, contestDate, contestParticipants, contestId, jackPot);
        this.currentDate = currentDate;
        this.viewed = viewed;
        this.lifeTime = DateUtils.subtractDatesInMillis(currentDate, contestDate);
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public boolean getViewed() {
        return viewed;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public boolean isShowTimer() {
        return showTimer;
    }

    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }
}
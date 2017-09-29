package com.jinglz.app.ui.showvideo.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.jinglz.app.data.network.models.contest.ContestResponse;
import com.jinglz.app.data.network.models.video.ShowVideoResponse;
import com.jinglz.app.utils.DateUtils;

import java.util.Date;

@AutoValue
public abstract class ShowVideoModel {

    public abstract String id();

    public abstract String contestId();

    public abstract Date contestDate();

    public abstract String videoUrl();

    public abstract String imageUrl();

    public abstract String name();

    @Nullable
    public abstract String sponsor();

    public abstract double jackpot();

    @Nullable
    public abstract String infoLink();

    /**
     * method to create new instance of AutoValue_ShowVideoModel with specified id, contestId, contestDate, videoUrl,
     * imageUrl, name, sponsor, jackpot and infoLink.
     *
     * @param id String variable contains video id
     * @param contestId String variable contains contest id
     * @param contestDate Date of contest
     * @param videoUrl String variable contains video url to play
     * @param imageUrl String variable contains image url
     * @param name String variable contains name of the video
     * @param sponsor String variable contains name of the sponsor
     * @param jackpot holds jackpot amount
     * @param infoLink String variable contains information link
     * @return AutoValue_ShowVideoModel instance
     */
    public static ShowVideoModel create(String id,
                                        String contestId,
                                        Date contestDate,
                                        String videoUrl,
                                        String imageUrl,
                                        String name,
                                        @Nullable String sponsor,
                                        double jackpot,
                                        @Nullable String infoLink) {
        return new AutoValue_ShowVideoModel(id, contestId, contestDate, videoUrl, imageUrl,
                                            name, sponsor, jackpot, infoLink);
    }

    /**
     * Method with specified videoResponse and contestResponse to creates new
     * instance of AutoValue_ShowVideoModel.
     *
     * @param videoResponse {@link ShowVideoResponse} contains video details
     * @param contestResponse {@link ContestResponse} contains contest details
     * @return AutoValue_ShowVideoModel instance
     */
    public static ShowVideoModel create(ShowVideoResponse videoResponse, ContestResponse contestResponse) {
        return ShowVideoModel.create(videoResponse.id(),
                                     contestResponse.id(),
                                     DateUtils.fromServerFormat(contestResponse.date()),
                                     videoResponse.videoUrl(),
                                     videoResponse.imageUrl(),
                                     videoResponse.name(),
                                     videoResponse.sponsor(),
                                     contestResponse.pot(),
                                     videoResponse.infoLink());
    }
}

package com.jinglz.app.ui.videohistory.models;

import android.support.annotation.Nullable;

import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;
import com.jinglz.app.utils.DateUtils;

import java.util.Date;

public class TodayWatchedVideoItemModel extends WatchedVideoItemModel {


    /**
     * @param response contains {@link VideoResponse} data from server and create a new TodayWatchedVideoItemModel
     * @return new TodayWatchedVideoItemModel
     */
    public static TodayWatchedVideoItemModel create(VideoResponse response) {
        return TodayWatchedVideoItemModel.create(response.id(),
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
     * @param id String contains video id
     * @param videoUrl String contains video url
     * @param fileName String contains file name/ext.
     * @param name String contains name of video
     * @param imageUrl String contains image url with video
     * @param infoLink String contains url to open information link
     * @param contestDate String contains date of contest
     * @param contestParticipants Integer contains number of participants
     * @param contestId String contains Contest id
     * @param jackPot double value contains amount.
     * @param sponsor  String contains sponsor name
     * @param viewedDate Date contains date when video is viewed
     * @param position Integer contains position of video item
     * @param coins Integer Contains number of coins
     * @return new  TodayWatchedVideoItemModel data
     */
    public static TodayWatchedVideoItemModel create(String id,
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
        return new TodayWatchedVideoItemModel(id, videoUrl, fileName, name, imageUrl, infoLink, contestDate,
                                              contestParticipants,
                                              contestId, jackPot, sponsor, viewedDate, position, coins);
    }

    public TodayWatchedVideoItemModel(String id,
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

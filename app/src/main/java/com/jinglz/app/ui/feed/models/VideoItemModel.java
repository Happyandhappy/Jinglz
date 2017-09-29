package com.jinglz.app.ui.feed.models;

import android.support.annotation.Nullable;

import java.util.Date;

public abstract class VideoItemModel {

    private final String id;
    private final String videoUrl;
    private final String fileName;
    private final String name;
    private final String imageUrl;
    private final String infoLink;
    private final String sponsor;
    private final Date contestDate;
    private final int contestParticipants;
    private final String contestId;
    private final double jackPot;

    /**
     * Constructs new VideoItemModel with specified id, videoUrl, fileName, name, imageUrl,
     * infoLink, sponsor, contestDate, contestParticipants, contestId, jackPot and id.
     *
     * @param videoUrl contains url for video
     * @param fileName contains name of the image file to initialize {@see fileName}
     * @param name contains name to initialize {@see name}
     * @param imageUrl contains url for image
     * @param infoLink  contains information link
     * @param sponsor it is used to initialize {@see sponsor}
     * @param contestDate contains contest date
     * @param contestParticipants contains contest participants number
     * @param contestId contains contest id
     * @param jackPot contains jackpot amount
     * @throws NullPointerException if any of the string is null
     */
    public VideoItemModel(
            String id,
            String videoUrl,
            String fileName,
            String name,
            String imageUrl,
            @Nullable String infoLink,
            @Nullable String sponsor,
            Date contestDate,
            int contestParticipants,
            String contestId,
            double jackPot) {
        if (id == null) {
            throw new NullPointerException("Null id");
        }
        this.id = id;
        if (videoUrl == null) {
            throw new NullPointerException("Null videoUrl");
        }
        this.videoUrl = videoUrl;
        if (fileName == null) {
            throw new NullPointerException("Null fileName");
        }
        this.fileName = fileName;
        if (name == null) {
            throw new NullPointerException("Null name");
        }
        this.name = name;
        if (imageUrl == null) {
            throw new NullPointerException("Null imageUrl");
        }
        this.imageUrl = imageUrl;
        this.infoLink = infoLink;
        this.sponsor = sponsor;
        if (contestDate == null) {
            throw new NullPointerException("Null contestDate");
        }
        this.contestDate = contestDate;
        this.contestParticipants = contestParticipants;
        if (contestId == null) {
            throw new NullPointerException("Null contestId");
        }
        this.contestId = contestId;
        this.jackPot = jackPot;
    }

    public String getId() {
        return id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public String getSponsor() {
        return sponsor;
    }

    public Date getContestDate() {
        return contestDate;
    }

    public int getContestParticipants() {
        return contestParticipants;
    }

    public String getContestId() {
        return contestId;
    }

    public double getJackPot() {
        return jackPot;
    }
}

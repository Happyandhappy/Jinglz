package com.jinglz.app.data.network.models.log;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.ui.showvideo.models.ShowVideoModel;

@AutoValue
public abstract class VideoLogRequest implements Loggable {

    /**
     * static method with specified gson, for constructing new AutoValue_VideoLogRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_VideoLogRequest.GsonTypeAdapter}
     * @return TypeAdapter object of VideoLogRequest type
     */
    public static TypeAdapter<VideoLogRequest> typeAdapter(Gson gson) {
        return new AutoValue_VideoLogRequest.GsonTypeAdapter(gson);
    }

    /**
     * static method with specified model to construct new AutoValue_VideoLogRequest which will used to
     * log video details using {@link AutoValue_VideoLogRequest}.
     *
     * @param model ShowVideoModel object to retrieve video details such as its id, name, sponsor and jackpot
     * @return VideoLogRequest object
     */
    public static VideoLogRequest create(ShowVideoModel model) {
        return new AutoValue_VideoLogRequest(model.id(), model.name(), model.sponsor(), model.jackpot());
    }

    @SerializedName("Video ID")
    public abstract String videoId();

    @SerializedName("Video Title")
    public abstract String videoTitle();

    @Nullable
    @SerializedName("Video Sponsor")
    public abstract String videoSponsor();

    @SerializedName("Jackpot")
    public abstract double jackpot();
}

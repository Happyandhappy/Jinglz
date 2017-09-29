package com.jinglz.app.data.network.models.video;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class VideoResponse {

    /**
     * static method with specified gson, for constructing new AutoValue_VideoResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_VideoResponse.GsonTypeAdapter}
     * @return TypeAdapter object of VideoResponse type
     */
    public static TypeAdapter<VideoResponse> typeAdapter(Gson gson) {
        return new AutoValue_VideoResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("_id")
    public abstract String id();

    @SerializedName("videoUrl")
    public abstract String videoUrl();

    @SerializedName("fileName")
    public abstract String fileName();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("imageUrl")
    public abstract String imageUrl();

    @Nullable
    @SerializedName("infoLink")
    public abstract String infoLink();

    @SerializedName("contestDate")
    public abstract String contestDate();

    @Nullable
    @SerializedName("currentDate")
    public abstract String currentDate();

    @SerializedName("contestParticipants")
    public abstract int contestParticipants();

    @SerializedName("contestId")
    public abstract String contestId();

    @SerializedName("pot")
    public abstract double jackPot();

    @Nullable
    @SerializedName("viewed")
    public abstract Boolean viewed();

    @Nullable
    @SerializedName("sponsor")
    public abstract String sponsor();

    @Nullable
    @SerializedName("viewedDate")
    public abstract String viewedDate();

    @Nullable
    @SerializedName("coins")
    public abstract Integer coins();

    @Nullable
    @SerializedName("position")
    public abstract Integer position();
}

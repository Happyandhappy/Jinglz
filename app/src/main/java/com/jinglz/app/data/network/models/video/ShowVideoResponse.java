package com.jinglz.app.data.network.models.video;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ShowVideoResponse {

    /**
     * method with specified id, language, fileName, videoUrl, imageUrl, name,
     * infoLink and sponsor to construct new AutoValue_ShowVideoResponse
     *
     * @param id unique id to retrieve video response
     * @param language string value, contains language
     * @param fileName string value, contains fileName
     * @param videoUrl contains url for video
     * @param imageUrl contains url for image
     * @param name contains name of the particular video
     * @param infoLink contains information link
     * @param sponsor contains sponsor detail
     * @return ShowVideoResponse object
     */
    public static ShowVideoResponse create(String id,
                                           String language,
                                           String fileName,
                                           String videoUrl,
                                           String imageUrl,
                                           String name,
                                           @Nullable String infoLink,
                                           @Nullable String sponsor) {
        return new AutoValue_ShowVideoResponse(id, language, fileName, videoUrl, imageUrl, name, infoLink, sponsor);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_ShowVideoResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_ShowVideoResponse.GsonTypeAdapter}
     * @return TypeAdapter object of ShowVideoResponse type
     */
    public static TypeAdapter<ShowVideoResponse> typeAdapter(Gson gson) {
        return new AutoValue_ShowVideoResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("_id")
    public abstract String id();

    @SerializedName("language")
    public abstract String language();

    @SerializedName("fileName")
    public abstract String fileName();

    @SerializedName("videoUrl")
    public abstract String videoUrl();

    @SerializedName("imageUrl")
    public abstract String imageUrl();

    @SerializedName("name")
    public abstract String name();

    @Nullable
    @SerializedName("infoLink")
    public abstract String infoLink();

    @Nullable
    @SerializedName("sponsor")
    public abstract String sponsor();
}

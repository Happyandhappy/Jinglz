package com.jinglz.app.data.network.models.history;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.data.network.models.video.VideoResponse;

import java.util.List;

@AutoValue
public abstract class GameHistoryResponse {

    /**
     * static method with specified gson, for constructing new AutoValue_GameHistoryResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_GameHistoryResponse.GsonTypeAdapter}
     * @return TypeAdapter object of GameHistoryResponse type
     */
    public static TypeAdapter<GameHistoryResponse> typeAdapter(Gson gson) {
        return new AutoValue_GameHistoryResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("data")
    public abstract List<VideoResponse> videos();

    @SerializedName("statistic")
    public abstract List<GameStatistic> statistic();

}

package com.jinglz.app.data.network.models.contest;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class TakePartInContestRequest {

    @SerializedName("video")
    public abstract String videoId();

    @SerializedName("contest")
    public abstract String contestId();

    /**
     * static method with specified videoId and contestId to construct new AutoValue_TakePartInContestRequest.
     *
     * @param videoId String variable contains video id.
     * @param contestId String variable contains contest id.
     * @return {@link TakePartInContestRequest} object
     */
    public static TakePartInContestRequest create(String videoId, String contestId) {
        return new AutoValue_TakePartInContestRequest(videoId, contestId);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_TakePartInContestRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_TakePartInContestRequest.GsonTypeAdapter}
     * @return TypeAdapter object of TakePartInContestRequest type
     */
    public static TypeAdapter<TakePartInContestRequest> typeAdapter(Gson gson) {
        return new AutoValue_TakePartInContestRequest.GsonTypeAdapter(gson);
    }
}

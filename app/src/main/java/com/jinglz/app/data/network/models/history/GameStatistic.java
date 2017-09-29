package com.jinglz.app.data.network.models.history;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@AutoValue
public abstract class GameStatistic {

    /**
     * static method with specified gson, for constructing new AutoValue_GameStatistic.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_GameStatistic.GsonTypeAdapter}
     * @return TypeAdapter object of GameStatistic type
     */
    public static TypeAdapter<GameStatistic> typeAdapter(Gson gson) {
        return new AutoValue_GameStatistic.GsonTypeAdapter(gson);
    }

    @SerializedName("total")
    public abstract int total();

    @SerializedName(value = "date", alternate = "startDate")
    public abstract Date date();
}

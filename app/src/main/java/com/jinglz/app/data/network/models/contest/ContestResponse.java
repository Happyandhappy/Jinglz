package com.jinglz.app.data.network.models.contest;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ContestResponse {

    @SerializedName("_id")
    public abstract String id();

    @SerializedName("date")
    public abstract String date();

    @SerializedName("pot")
    public abstract double pot();

    /**
     * static method with specified id, date and pot, to construct new AutoValue_ContestResponse
     *
     * @param id to retrieve response of specific contest
     * @param date to retrieve response of specific date
     * @param pot double variable to specify pot number
     * @return {@ling ContestResponse} object
     */
    public static ContestResponse create(String id, String date, double pot) {
        return new AutoValue_ContestResponse(id, date, pot);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_ContestResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_ContestResponse.GsonTypeAdapter}
     * @return TypeAdapter object of ContestResponse type
     */
    public static TypeAdapter<ContestResponse> typeAdapter(Gson gson) {
        return new AutoValue_ContestResponse.GsonTypeAdapter(gson);
    }
}

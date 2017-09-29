package com.jinglz.app.data.network.models.coin;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class CoinsHistoryResponse {

    /**
     * method used to create new AutoValue_CoinsHistoryResponse with specified today, week, month and
     * total.
     *
     * @param today double variable, to be used for fetching history of today
     * @param week double variable, to be used for fetching history weekly
     * @param month double variable, to be used for fetching history monthly
     * @param total double variable, to be used for fetching  total history
     * @return {@link CoinsHistoryResponse} object
     */
    public static CoinsHistoryResponse create(double today, double week, double month, double total) {
        return new AutoValue_CoinsHistoryResponse(today, week, month, total);
    }

    /**
     * method with specified gson, for constructing new AutoValue_CoinsHistoryResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_CoinsHistoryResponse.GsonTypeAdapter}
     * @return TypeAdapter object of CoinsHistoryResponse type
     */

    public static TypeAdapter<CoinsHistoryResponse> typeAdapter(Gson gson) {
        return new AutoValue_CoinsHistoryResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("today")
    public abstract double today();

    @SerializedName("week")
    public abstract double week();

    @SerializedName("month")
    public abstract double month();

    @SerializedName("total")
    public abstract double total();

}

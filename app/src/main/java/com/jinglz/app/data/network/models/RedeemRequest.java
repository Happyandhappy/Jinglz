package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class RedeemRequest {

    @SerializedName("coins")
    public abstract int coins();

    /**
     * returns new RedeemRequest object by constructing
     * new AutoValue_RedeemRequest with specified coins to redeem coins
     *
     * @param coins int variable contains coins used for redeem process.
     * @return Returns a new instance of RedeemRequest class
     */
    public static RedeemRequest create(int coins) {
        return new AutoValue_RedeemRequest(coins);
    }

    /**
     * Create a new instance of the RedeemRequest class, constructing
     * new AutoValue_RedeemRequest.GsonTypeAdapter with specified {@code gson}
     *
     *
     * @param gson to parse data from Json
     * @return Returns a new instance of RedeemRequest class
     */
    public static TypeAdapter<RedeemRequest> typeAdapter(Gson gson) {
        return new AutoValue_RedeemRequest.GsonTypeAdapter(gson);
    }
}

package com.jinglz.app.data.network.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ReferralData {

    /**
     * create new instance of ReferralData to pass TypeAdapter. TypeAdapter is used to retrieve
     * response of the ReferralData the object is being created in.it uses {@code gson} to parse
     * data from Json.
     *
     * @param gson to parse data from Json
     * @return {@code new AutoValue_ReferralData.GsonTypeAdapter(gson)}
     */
    public static TypeAdapter<ReferralData> typeAdapter(Gson gson) {
        return new AutoValue_ReferralData.GsonTypeAdapter(gson);
    }

    @Nullable
    @SerializedName("r")
    public abstract String referralCode();

    @SerializedName("+clicked_branch_link")
    public abstract boolean clickedBranchLink();

}
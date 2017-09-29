package com.jinglz.app.data.network.models.user;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class AlreadyRegisterResponse {

    @SerializedName("data")
    public abstract boolean data();

    /**
     * static method with specified gson, for constructing new AutoValue_AlreadyRegisterResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_AlreadyRegisterResponse.GsonTypeAdapter}
     * @return TypeAdapter object of AlreadyRegisterResponse type
     */
    public static TypeAdapter<AlreadyRegisterResponse> typeAdapter(Gson gson) {
        return new AutoValue_AlreadyRegisterResponse.GsonTypeAdapter(gson);
    }
}

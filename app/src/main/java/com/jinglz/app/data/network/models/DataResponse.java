package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

@AutoValue
public abstract class DataResponse<T> {

    @SerializedName("data")
    public abstract T data();

    /**
     * create new instance of DataResponse. it can be instantiated to retrieve
     * response of the DataResponse the object is being created in.
     *
     * @param gson to parse Json data
     * @param typeToken Specialization of DataResponse that allows you to receive the
     *                  Data the object is being created in.
     * @param <T>
     * @return {@code new AutoValue_DataResponse.GsonTypeAdapter(gson, typeToken)}
     */
    @SuppressWarnings("unchecked")
    public static <T> TypeAdapter<DataResponse<T>> typeAdapter(Gson gson, TypeToken<? extends DataResponse<T>> typeToken) {
        return new AutoValue_DataResponse.GsonTypeAdapter(gson, typeToken);
    }
}

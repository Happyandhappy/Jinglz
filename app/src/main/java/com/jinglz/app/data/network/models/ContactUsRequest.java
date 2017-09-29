package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ContactUsRequest {

    @SerializedName("message")
    public abstract String message();

    @SerializedName("environmentInfo")
    public abstract EnvironmentInfo environment();

    /**
     * method with two input fields message and environment respectively, to construct
     * new AutoValue_ContactUsRequest. it is used to request for sending feedback.
     *
     * @param message string variable that contains message to send
     * @param environment it contains information regarding app oldVersion, newVersion and device type.
     * @return ContactUsRequest object
     */
    public static ContactUsRequest create(String message, EnvironmentInfo environment) {
        return new AutoValue_ContactUsRequest(message, environment);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_ContactUsRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_ContactUsRequest.GsonTypeAdapter}
     * @return TypeAdapter object of ContactUsRequest type
     */
    public static TypeAdapter<ContactUsRequest> typeAdapter(Gson gson) {
        return new AutoValue_ContactUsRequest.GsonTypeAdapter(gson);
    }
}

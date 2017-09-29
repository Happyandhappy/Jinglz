package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ContactUsResponse {

    public abstract boolean sent();

    /**
     * method with specified boolean value {@code sent} to construct new AutoValue_ContactUsResponse.
     * this method is used to handle response to check whether feedback is sent.
     *
     * @param sent boolean value to check feedback is sent successfully.
     * @return ContactUsResponse object
     */
    public static ContactUsResponse create(boolean sent) {
        return new AutoValue_ContactUsResponse(sent);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_ContactUsResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_ContactUsResponse.GsonTypeAdapter}
     * @return TypeAdapter object of ContactUsResponse type
     */
    public static TypeAdapter<ContactUsResponse> typeAdapter(Gson gson) {
        return new AutoValue_ContactUsResponse.GsonTypeAdapter(gson);
    }
}

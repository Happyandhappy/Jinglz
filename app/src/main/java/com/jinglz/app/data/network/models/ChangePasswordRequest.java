package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class ChangePasswordRequest {

    @SerializedName("currentPassword")
    public abstract String currentPassword();

    @SerializedName("password")
    public abstract String newPassword();

    /**
     * method with specified currentPassword and newPassword to construct
     * new AutoValue_ChangePasswordRequest. it is used to request for changing password.
     *
     * @param currentPassword string variable contains current password
     * @param newPassword string variable contains new password
     * @return
     */
    public static ChangePasswordRequest create(String currentPassword, String newPassword) {
        return new AutoValue_ChangePasswordRequest(currentPassword, newPassword);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_ChangePasswordRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_ChangePasswordRequest.GsonTypeAdapter}
     * @return TypeAdapter object of ChangePasswordRequest type
     */
    public static TypeAdapter<ChangePasswordRequest> typeAdapter(Gson gson) {
        return new AutoValue_ChangePasswordRequest.GsonTypeAdapter(gson);
    }
}
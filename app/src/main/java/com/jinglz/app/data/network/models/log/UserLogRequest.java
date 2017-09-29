package com.jinglz.app.data.network.models.log;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.jinglz.app.data.network.models.user.UserResponse;

import javax.annotation.Nullable;

@AutoValue
public abstract class UserLogRequest implements Loggable {

    /**
     * static method with userResponse, pushEnabled, isCameraPermissionGranted,
     * isLocationPermissionGranted and geolocationFlag to construct new new AutoValue_UserLogRequest
     *
     * @param userResponse contains response of user
     * @param pushEnabled to check if receive notification is enabled.
     * @param isCameraPermissionGranted to check whether camera permission is granted
     * @param isLocationPermissionGranted to check if location service is enabled
     * @param geolocationFlag to determine the location is in US
     * @return UserLogRequest object
     */
    public static UserLogRequest create(UserResponse userResponse,
            boolean pushEnabled,
            boolean isCameraPermissionGranted,
            boolean isLocationPermissionGranted,
            String geolocationFlag) {
        return new AutoValue_UserLogRequest(userResponse.email(),
                userResponse.firstName(),
                userResponse.lastName(),
                userResponse.phone(),
                userResponse.gender(),
                userResponse.zipCode(),
                userResponse.yearOfBirth(),
                pushEnabled,
                isCameraPermissionGranted,
                isLocationPermissionGranted,
                geolocationFlag);
    }

    @SerializedName("$email")
    public abstract String email();

    @SerializedName("$first_name")
    public abstract String firstName();

    @SerializedName("$last_name")
    public abstract String lastName();

    @Nullable
    @SerializedName("$phone")
    public abstract String phone();

    @Nullable
    @SerializedName("Gender")
    public abstract String gender();

    @Nullable
    @SerializedName("Zip Code")
    public abstract String zipCode();

    @Nullable
    @SerializedName("Year of Birth")
    public abstract Integer yearOfBirth();

    @SerializedName("Notifications Enabled")
    public abstract boolean pushEnabled();

    @SerializedName("Camera Access enabled")
    public abstract boolean isCameraPermissionGranted();

    @SerializedName("Location Access enabled")
    public abstract boolean isLocationPermissionGranted();

    @SerializedName("Geolocation Flag")
    public abstract String countryCode();

    /**
     * static method with specified gson, for constructing new AutoValue_UserLogRequest.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_UserLogRequest.GsonTypeAdapter}
     * @return TypeAdapter object of UserLogRequest type
     */
    public static TypeAdapter<UserLogRequest> typeAdapter(Gson gson) {
        return new AutoValue_UserLogRequest.GsonTypeAdapter(gson);
    }
}

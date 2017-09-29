package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class EnvironmentInfo {

    @SerializedName("osVersion")
    public abstract String version();

    @SerializedName("buildVersion")
    public abstract String buildVersion();

    @SerializedName("deviceType")
    public abstract String deviceType();

    /**
     * method to retrieve a new EnvironmentInfo object by constructing
     * new AutoValue_EnvironmentInfo with specified version, buildVersion and deviceType.
     *
     * @param version string variable contains version information.
     * @param buildVersion string variable contains build version.
     * @param deviceType string variable contains device type.
     * @return new EnvironmentInfo object
     */
    public static EnvironmentInfo create(String version, String buildVersion, String deviceType) {
        return new AutoValue_EnvironmentInfo(version, buildVersion, deviceType);
    }

    /**
     * create new instance of EnvironmentInfo to pass TypeAdapter. TypeAdapter used to retrieve
     * response of the EnvironmentInfo the object is being created in.it uses {@code gson} to parse
     * data from Json.
     *
     * @param gson to parse data from Json
     * @return {@code new AutoValue_EnvironmentInfo.GsonTypeAdapter(gson)}
     */
    public static TypeAdapter<EnvironmentInfo> typeAdapter(Gson gson) {
        return new AutoValue_EnvironmentInfo.GsonTypeAdapter(gson);
    }
}

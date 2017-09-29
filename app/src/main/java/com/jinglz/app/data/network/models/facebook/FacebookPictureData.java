package com.jinglz.app.data.network.models.facebook;

import com.google.gson.annotations.SerializedName;


public class FacebookPictureData {
    @SerializedName("is_silhouette")
    private Boolean isSilhouette;
    private String url;

    /**
     * returns boolean value for {@see isSilhouette}
     *
     * @return boolean variable
     */
    public Boolean getIsSilhouette() {
        return isSilhouette;
    }

    /**
     * method with specified isSilhouette to set {@see isSilhouette}
     *
     * @param isSilhouette boolean variable
     */
    public void setIsSilhouette(Boolean isSilhouette) {
        this.isSilhouette = isSilhouette;
    }

    /**
     * method used to retrieve url of facebook image
     *
     * @return String variable containing url
     */
    public String getUrl() {
        return url;
    }

    /**
     * method with specified url to initialize {@see url}
     *
     * @param url String variable that contains url of facebook image
     */
    public void setUrl(String url) {
        this.url = url;
    }
}

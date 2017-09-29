package com.jinglz.app.data.network.models.facebook;


public class FacebookPicture {
    private FacebookPictureData data;

    /**
     * method to retrieve {@link FacebookPictureData}
     *
     * @return FacebookPictureData object
     */
    public FacebookPictureData getData() {
        return data;
    }

    /**
     * method with specified data to set {@see data}
     *
     * @param data to initialize {@see data}
     */
    public void setData(FacebookPictureData data) {
        this.data = data;
    }
}

package com.jinglz.app.config;

public interface AWSConfig {

    /**
     * for uploading image file, it requires name of the specific bucket.
     * it is used in {@link com.jinglz.app.data.repositories.FilesRepository} for
     * constructing new UploadFileResponse.
     */
    String PROFILES_IMAGES_BUCKET = "jinglz-images-profiles";

}

package com.jinglz.app.data.network.models;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;

public class UploadFileResponse {

    private final String mId;
    private final TransferObserver mObserver;

    /**
     * Constructs new UploadFileResponse with specified id and observer.
     *
     * @param id String value contains file key
     * @param observer contains bucket information {@link TransferObserver}
     */
    public UploadFileResponse(String id, TransferObserver observer) {
        mId = id;
        mObserver = observer;
    }

    public String getId() {
        return mId;
    }

    public TransferObserver getObserver() {
        return mObserver;
    }
}

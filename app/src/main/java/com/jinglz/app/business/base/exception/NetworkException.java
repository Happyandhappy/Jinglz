package com.jinglz.app.business.base.exception;

public class NetworkException extends RuntimeException {

    /** Constructs a new network exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public NetworkException(String message) {
        super(message);
    }

}

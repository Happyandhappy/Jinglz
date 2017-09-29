package com.jinglz.app.business.auth;

public class AuthException extends RuntimeException {

    /**
     *Constructs a new runtime exception with {@code message} as its
     * detail message.  The cause is not initialized, and may subsequently be initialized.
     *
     * @param message the detail message. The detail message is saved for
     *          later retrieval.
     */
    public AuthException(String message) {
        super(message);
    }
}

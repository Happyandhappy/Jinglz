package com.jinglz.app.business.redeem.validation;

public class RedeemCoinValidationException extends RuntimeException {

    /** Constructs a new RedeemCoinValidationException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public RedeemCoinValidationException(String message) {
        super(message);
    }
}

package com.jinglz.app.business.redeem.validation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.jinglz.app.business.redeem.validation.RedeemVerificationException.VerificationType.COIN_LIMIT;
import static com.jinglz.app.business.redeem.validation.RedeemVerificationException.VerificationType.NONE;
import static com.jinglz.app.business.redeem.validation.RedeemVerificationException.VerificationType.PAY_PAL;
import static com.jinglz.app.business.redeem.validation.RedeemVerificationException.VerificationType.PHONE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class RedeemVerificationException extends RuntimeException {

    @Retention(SOURCE)
    @IntDef({NONE, PHONE, PAY_PAL, COIN_LIMIT})
    public @interface VerificationType {

        int NONE = -1;

        int PHONE = 0;

        int PAY_PAL = 1;

        int COIN_LIMIT = 2;
    }

    @VerificationType
    private final int verificationType;

    /**
     * construct new RedeemVerificationException with specified verificationType.
     *
     * @param verificationType value used to initialize {@see verificationType}
     */
    public RedeemVerificationException(@VerificationType int verificationType) {
        super();
        this.verificationType = verificationType;
    }

    /**
     * constructs new RedeemVerificationException with specified detail message and verificationType.
     *
     * @param message a detailed message
     * @param verificationType value used to initialize {@see verificationType}
     */
    public RedeemVerificationException(String message, @VerificationType int verificationType) {
        super(message);
        this.verificationType = verificationType;
    }

    public int getVerificationType() {
        return verificationType;
    }
}

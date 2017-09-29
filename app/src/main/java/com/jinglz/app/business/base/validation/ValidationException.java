package com.jinglz.app.business.base.validation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.List;

import static com.jinglz.app.business.base.validation.ValidationException.Field.CONFIRM_PASSWORD;
import static com.jinglz.app.business.base.validation.ValidationException.Field.EMAIL;
import static com.jinglz.app.business.base.validation.ValidationException.Field.FIRST_NAME;
import static com.jinglz.app.business.base.validation.ValidationException.Field.LAST_NAME;
import static com.jinglz.app.business.base.validation.ValidationException.Field.NONE;
import static com.jinglz.app.business.base.validation.ValidationException.Field.PASSWORD;
import static com.jinglz.app.business.base.validation.ValidationException.Field.PHONE_NUMBER;
import static com.jinglz.app.business.base.validation.ValidationException.Field.TERMS;
import static com.jinglz.app.business.base.validation.ValidationException.Field.YEAR_OF_BIRTH;
import static com.jinglz.app.business.base.validation.ValidationException.Field.ZIP_CODE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ValidationException extends RuntimeException {

    /**
     * used to initialize fields {@code NONE, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER,
     * ZIP_CODE, PASSWORD, CONFIRM_PASSWORD, TERMS, YEAR_OF_BIRTH} respectively.
     *
     */
    @Retention(SOURCE)
    @IntDef({NONE, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, ZIP_CODE, PASSWORD, CONFIRM_PASSWORD,
            TERMS, YEAR_OF_BIRTH})
    public @interface Field {

        int NONE = -1;

        int FIRST_NAME = 0;

        int LAST_NAME = 1;

        int EMAIL = 2;

        int PHONE_NUMBER = 3;

        int ZIP_CODE = 4;

        int PASSWORD = 5;

        int CONFIRM_PASSWORD = 6;

        int TERMS = 7;

        int YEAR_OF_BIRTH = 8;

        int COUNTRY = 9;
    }

    private final List<ValidationError> mErrors;

    public ValidationException(List<ValidationError> errors) {
        mErrors = errors;
    }

    public List<ValidationError> getErrors() {
        return mErrors;
    }

}

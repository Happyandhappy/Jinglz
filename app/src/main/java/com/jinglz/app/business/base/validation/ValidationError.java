package com.jinglz.app.business.base.validation;

/**
 * A class to handle Validation exceptions
 */
public class ValidationError {

    @ValidationException.Field
    private int mField;
    private String mMessage;

    /**
     * @param message String contains error message...
     */
    public ValidationError(int field, String message) {
        mMessage = message;
        mField = field;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getField() {
        return mField;
    }

    public void setField(int field) {
        mField = field;
    }
}

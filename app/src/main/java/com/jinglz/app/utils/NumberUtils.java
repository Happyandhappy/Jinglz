package com.jinglz.app.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class NumberUtils {

    private static final NumberFormat df = NumberFormat.getNumberInstance(Locale.US);

    /**
     * Constructs sole NumberUtils constructor.
     */
    private NumberUtils() {
    }

    /**
     * This method is used to format specified object in a string.
     *
     * @param value object to format
     * @return formatted string
     */
    public static String toPrettyNumber(@Nullable Object value) {
        return value != null ? df.format(value) : "0";
    }

    /**
     * This method is used to parse string value to integer. it can raise ParseException.
     *
     * @param value String value to parse
     * @return A number parsed from the string
     */
    public static int fromPrettyNumber(@NonNull String value) {
        try {
            return df.parse(value).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

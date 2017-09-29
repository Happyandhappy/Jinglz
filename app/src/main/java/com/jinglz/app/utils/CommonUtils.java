package com.jinglz.app.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 5/9/2017.
 */

public class CommonUtils {
    /**
     * Common method for getting country name of user's current location
     * @param context Context of class where the method is used
     * @param latitude Double value contains latitude of user's current location
     * @param longitude Double value contains longitude of user's current location
     * @return String value contains country name
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        String s = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                s = addresses.get(0).getCountryName();
                return s;
            }
        } catch (IOException ignored) {

        }
        return s;
    }

}

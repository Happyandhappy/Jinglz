package com.jinglz.app.data.repositories;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jinglz.app.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class LocationRepository {

    private final Context mContext;
    private final LocationManager mLocationManager;

   /**
     * Constructs new instance of LocationRepository with specified context.
     * This class is basically used to keep track of user location, that can be retrieved from
     * LocationManager, sim and geoCoder.
     *
     * @param context to work with application-specific data
     */
    @Inject
    public LocationRepository(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * This method is used to retrieve user country by using {@link #getCountryBySim()}
     * and {@link #getLocationByProvider}. it will return the name of the country.
     *
     * @return String containing name of the country.
     */
    public Single<String> getUserCountry() {
        return getCountryBySim()
                .flatMap(country -> TextUtils.isEmpty(country)
                        ? getLocationByProvider()
                        : Single.just(country));
    }

    /**
     * This method is used to check if the given country is US. it uses {@link #getCountryBySim()}
     * and {@link #getLocationByProvider}. and return true {@code countryCode} is not empty and is US.
     *
     * @return true if country is US, false otherwise.
     */
    public boolean isUsLocation() {
        final String countryCode = getCountryBySim()
                .flatMap(country -> TextUtils.isEmpty(country)
                        ? getLocationByProvider()
                        : Single.just(country))
                .toBlocking().value();

        return !TextUtils.isEmpty(countryCode) && countryCode.equalsIgnoreCase(mContext.getString(R.string.us));
    }

    /**
     * This method is used to retrieve country from {@link TelephonyManager}
     *
     * @return String containing name of the country.
     */
    private Single<String> getCountryBySim() {
        return Single.fromCallable(() -> {
            try {
                final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) {
                    return simCountry.toLowerCase(Locale.US);
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                    final String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) {
                        return networkCountry.toLowerCase(Locale.US);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * This method is used to fetch location from the {@code mLocationManager} by retrieving
     * getLastKnownLocation and then calls {@link #getCountryByLocation(Location)} to retrieve
     * location with the help of Geocoder.
     *
     * @return String containing country name
     */
    @SuppressWarnings("MissingPermission")
    private Single<String> getLocationByProvider() {
        return Single.create(subscriber -> {
            try {
                final Criteria criteria = new Criteria();
                final String provider = mLocationManager.getBestProvider(criteria, true);

                final Location location = mLocationManager.getLastKnownLocation(provider);

                if (location != null) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onSuccess(location);
                    }
                } else {
                    mLocationManager.requestSingleUpdate(provider, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onSuccess(location);
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onSuccess(null);
                            }
                        }
                    }, Looper.getMainLooper());
                }
            } catch (Exception ex) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(ex);
                }
            }
        })
                .map(o -> (Location) o)
                .map(this::getCountryByLocation);
    }

    /**
     * method with specified location to fetch country from the given location.
     * It will return String variable containing country name, with the help of
     * {@code Geocoder} class.
     *
     * @param location Location that will be used by {@code geo}
     * @return String variable containing country
     */
    private String getCountryByLocation(Location location) {
        if (location == null) {
            return null;
        }
        try {
            final Geocoder geo = new Geocoder(mContext, Locale.US);
            final List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getCountryCode().toLowerCase(Locale.US);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

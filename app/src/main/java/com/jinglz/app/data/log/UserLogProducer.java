package com.jinglz.app.data.log;

import android.content.Context;

import com.jinglz.app.R;
import com.jinglz.app.data.network.models.log.UserLogRequest;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.data.repositories.DeviceRepository;
import com.jinglz.app.data.repositories.LocationRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserLogProducer {

    private final Context mContext;
    private final LocationRepository mLocationRepository;
    private final DeviceRepository mDeviceRepository;

    /**
     * Constructs new SignInLocalValidation with the specified context and locationRepository
     *
     * @param context for application-specific data
     * @param locationRepository keeps track of location information
     * @param deviceRepository keeps track of device information
     */
    @Inject
    public UserLogProducer(Context context, LocationRepository locationRepository, DeviceRepository deviceRepository) {
        mContext = context;
        mLocationRepository = locationRepository;
        mDeviceRepository = deviceRepository;
    }

    /**
     * method with specified userResponse, that is used to create user log.
     * for creating log, {@link UserLogRequest#create(UserResponse, boolean, boolean, boolean, String)}
     * method is called.
     * <p>Note, for creating user log it needs detail regarding permissions as well.
     * {@link DeviceRepository#isPushEnabled()} for checking notification alert enabled,
     * {@link DeviceRepository#isCameraPermissionGranted()} for checking camera permission enabled,
     * {@link DeviceRepository#isLocationPermissionGranted()} for checking location permission enabled,
     * {@link LocationRepository#isUsLocation()} for checking location is {@code R.string.us} or
     * {@code R.string.non_us}</p>
     *
     * @param userResponse {@link UserLogRequest} that contains user information
     * @return UserLogRequest object
     */
    public UserLogRequest produce(UserResponse userResponse) {
        return UserLogRequest.create(userResponse,
                mDeviceRepository.isPushEnabled(),
                mDeviceRepository.isCameraPermissionGranted(),
                mDeviceRepository.isLocationPermissionGranted(),
                mContext.getString(mLocationRepository.isUsLocation() ? R.string.us : R.string.non_us));
    }
}

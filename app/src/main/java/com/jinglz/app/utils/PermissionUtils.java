package com.jinglz.app.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public final class PermissionUtils {

    /**
     * Constructs an empty constructor for PermissionUtils
     */
    private PermissionUtils() {

    }

    /**
     * This method is used to determine whether the calling process have been
     * granted a particular permission. This method is with specified context and
     * permission name.
     *
     * @param context to be used for application-specific resources.
     * @param permission String containing the name of the permission being checked.
     * @return boolean value, Returns true if permission granted
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        final int permissionResource = context.checkCallingOrSelfPermission(permission);
        return (permissionResource == PackageManager.PERMISSION_GRANTED);
    }
}

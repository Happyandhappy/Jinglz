package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ChangePasswordModel {

    public abstract String oldPassword();

    public abstract String newPassword();

    public abstract String confPassword();

    /**
     * method with specified oldPassword, newPassword and confPassword to construct new
     * AutoValue_ChangePasswordModel.
     *
     * @param oldPassword string containing old password
     * @param newPassword string containing new password
     * @param confPassword string containing confirm password
     * @return ChangePasswordModel object
     */
    public static ChangePasswordModel create(String oldPassword, String newPassword, String confPassword) {
        return new AutoValue_ChangePasswordModel(oldPassword, newPassword, confPassword);
    }

}

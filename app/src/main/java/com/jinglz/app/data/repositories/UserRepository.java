package com.jinglz.app.data.repositories;

import com.jinglz.app.data.local.PreferencesHelper;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.ChangePasswordRequest;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.user.SignUpRequest;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class UserRepository {

    private final Api mApi;
    private final PreferencesHelper mPreferences;


    /**
     * Constructs a new UserRepository with a specified api and preferences.
     * this is basically used to keep track of user.
     *
     * @param api Api is used for signUp, update user details and change password.
     * @param preferences to save details in sharedPreference.
     */
    @Inject
    public UserRepository(Api api, PreferencesHelper preferences) {
        mApi = api;
        mPreferences = preferences;
    }

    /**
     * method with specified signUpRequest. this is used to hit signUp api, using
     * {@link Api#signUp(SignUpRequest)} by sending {@code signUpRequest}.
     * if signUp successfully, it will save data in sharedPreference by calling
     * {@link PreferencesHelper#setUserData(UserResponse)}.
     *
     * @param signUpRequest SignUpRequest containing client id, client secret and user detail.
     * @return UserResponse instance
     */
    public Single<UserResponse> createUser(SignUpRequest signUpRequest) {
        return mApi.signUp(signUpRequest)
                .map(DataResponse::data)
                .doOnSuccess(mPreferences::setUserData);
    }

    /**
     * method with specified editProfileModel. this is used to update user detail, using
     * {@link Api#updateUser(EditProfileModel)} by sending {@code editProfileModel}.
     * if updated successfully, it will save data in sharedPreference by calling
     * {@link PreferencesHelper#setUserData(UserResponse)}.
     *
     * @param editProfileModel instance of {@link EditProfileModel}
     * @return UserResponse instance
     */
    public Single<UserResponse> updateUser(EditProfileModel editProfileModel) {
        return mApi.updateUser(editProfileModel)
                .map(DataResponse::data)
                .doOnSuccess(mPreferences::setUserData);
    }

    /**
     * method with specified changePasswordRequest. this is used to change user password, using
     * {@link Api#changePassword(ChangePasswordRequest)} by sending {@code changePasswordRequest}.
     * if password changed successfully, it will return {@link UserResponse}.
     *
     * @param changePasswordRequest instance of {@link ChangePasswordRequest}
     * @return UserResponse instance
     */
    public Single<UserResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        return mApi.changePassword(changePasswordRequest)
                .map(DataResponse::data);
    }
}

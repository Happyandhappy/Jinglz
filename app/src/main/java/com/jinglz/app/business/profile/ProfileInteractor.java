package com.jinglz.app.business.profile;

import android.content.Context;

import com.jinglz.app.R;
import com.jinglz.app.business.base.exception.NetworkException;
import com.jinglz.app.business.base.validation.PasswordValidation;
import com.jinglz.app.business.base.validation.PasswordValidationError;
import com.jinglz.app.business.base.validation.PasswordValidationException;
import com.jinglz.app.business.profile.validation.ProfileLocalValidation;
import com.jinglz.app.data.log.UserLogProducer;
import com.jinglz.app.data.network.models.ChangePasswordModel;
import com.jinglz.app.data.network.models.ChangePasswordRequest;
import com.jinglz.app.data.network.models.UploadFileResponse;
import com.jinglz.app.data.network.models.log.Loggable;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.data.panel.CrashlyticsManager;
import com.jinglz.app.data.panel.MixpanelManager;
import com.jinglz.app.data.repositories.FilesRepository;
import com.jinglz.app.data.repositories.SessionRepository;
import com.jinglz.app.data.repositories.UserRepository;
import com.jinglz.app.injection.session.PerSession;
import com.jinglz.app.ui.feed.models.ShortUserData;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Collections;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class ProfileInteractor {

    private final MixpanelManager mMixpanelManager;
    private final CrashlyticsManager mCrashlyticsManager;
    private final UserRepository mUserRepository;
    private final UserLogProducer mUserLogProducer;
    private final SessionRepository mSessionRepository;
    private final FilesRepository mFilesRepository;
    private final ProfileLocalValidation mProfileLocalValidation;
    private final PasswordValidation mPasswordValidation;
    private final Context mContext;

    /**
     * constructs new ProfileInteractor mixpanelManager, crashlyticsManager, userRepository,
     * userLogProducer, sessionRepository, filesRepository, profileLocalValidation,
     * passwordValidation and context
     * @param mixpanelManager that produce user response
     * @param crashlyticsManager tracks user response
     * @param userRepository for updating user detail and changing password
     * @param userLogProducer contains location and device details
     * @param sessionRepository keep track of user sessions
     * @param filesRepository keep tracks of image files
     * @param profileLocalValidation for handling profile validations
     * @param passwordValidation for password validation
     * @param context for application specific resources
     */
    @Inject
    public ProfileInteractor(
            MixpanelManager mixpanelManager,
            CrashlyticsManager crashlyticsManager,
            UserRepository userRepository,
            UserLogProducer userLogProducer,
            SessionRepository sessionRepository,
            FilesRepository filesRepository,
            ProfileLocalValidation profileLocalValidation,
            PasswordValidation passwordValidation, Context context) {
        mMixpanelManager = mixpanelManager;
        mCrashlyticsManager = crashlyticsManager;
        mUserRepository = userRepository;
        mUserLogProducer = userLogProducer;
        mSessionRepository = sessionRepository;
        mFilesRepository = filesRepository;
        mProfileLocalValidation = profileLocalValidation;
        mPasswordValidation = passwordValidation;
        mContext = context;
    }

    /**
     * method calls {@link SessionRepository#getCurrentSession()} and return user response
     *
     * @return Observable object of UserResponse
     */
    public Observable<UserResponse> getUserData() {
        return mSessionRepository.getCurrentSession();
    }

    /**
     * method calls {@link SessionRepository#getCachedSession()} and return user response
     *
     * @return Observable object of UserResponse
     */
    public Single<UserResponse> getUserDataCached() {
        return mSessionRepository.getCachedSession();
    }

    /**
     * method is used to edit user profile information.
     * constructs new EditProfileModel with id, gender, yearOfBirth, firstName, lastName,
     * zipCode, phone and image
     *
     * @return Observable object of EditProfileModel
     */
    public Observable<EditProfileModel> getProfileUserData() {
        return mSessionRepository.getCurrentSession()
                .subscribeOn(Schedulers.io())
                .map(userResponse -> new EditProfileModel(userResponse.id(),
                        userResponse.gender(),
                        userResponse.yearOfBirth(),
                        userResponse.firstName(),
                        userResponse.lastName(),
                        userResponse.zipCode(),
                        userResponse.phone(),
                        userResponse.image()));
    }

    /**
     * method with specified model to update user details after validating.
     * use {@link ProfileLocalValidation#validate(EditProfileModel)} for validation process.
     * and {@link UserRepository#updateUser(EditProfileModel)} for updating.
     *
     * @param model that needs to be updated
     * @return Single object of UserResponse
     */
    public Single<UserResponse> updateUser(EditProfileModel model) {
        return Single.just(model)
                .subscribeOn(Schedulers.io())
                .flatMap(mProfileLocalValidation::validate)
                .flatMap(mUserRepository::updateUser);
    }

    /**
     * method with specified id and file for uploading image file by calling
     * {@link FilesRepository#uploadImage(String, File)}
     *
     * @param id String id that will required to create key for uploading file
     * @param file file to be uploaded
     * @return
     */
    public UploadFileResponse uploadFile(String id, File file) {
        return mFilesRepository.uploadImage(id, file);
    }

    /**
     * This method is used to retrieve subscribed user data. first it will fetch current session
     * by {@link SessionRepository#getCurrentSession()} after that produce user response by
     * calling {@link MixpanelManager#trackUser(String, Loggable)} and tracks user by using
     * {@link CrashlyticsManager#trackUser(UserResponse)}.
     * creates {@code ShortUserData} and return Observable object of ShortUserData
     *
     * @return Observable object of ShortUserData
     */
    public Observable<ShortUserData> getShortUserData() {
        return mSessionRepository.getCurrentSession()
                .doOnNext(userResponse -> mMixpanelManager.trackUser(userResponse.id(), mUserLogProducer.produce(userResponse)))
                .doOnNext(mCrashlyticsManager::trackUser)
                .subscribeOn(Schedulers.io())
                .filter(response -> response != null)
                .map(response -> ShortUserData.create(response.email(),
                        response.firstName(),
                        response.lastName(),
                        response.image()));
    }

    /**
     * method is used to close the session by {@link SessionRepository#logout()}
     * and release the user from crashlytics manager by calling {@link CrashlyticsManager#releaseUser()}
     */
    public void cleanUserData() {
        mSessionRepository.logout();
        mCrashlyticsManager.releaseUser();
    }

    /**
     * method that is used to change the password with specified model.
     * checks validation first then request for changing password {@link UserRepository#changePassword(ChangePasswordRequest)}
     *
     *
     * @param model ChangePasswordModel object that contains old password, new password and confirmed password.
     * @return Completable object
     */
    public Completable changePassword(ChangePasswordModel model) {
        return Single.just(model)
                .subscribeOn(Schedulers.io())
                .flatMap(mPasswordValidation::validate)
                .map(model1 -> ChangePasswordRequest.create(model1.oldPassword(), model1.newPassword()))
                .flatMap(mUserRepository::changePassword)
                .onErrorResumeNext(this::handleChangePasswordHttpException)
                .toCompletable();
    }

    /**
     * checks whether session exist by calling {@link SessionRepository#isSessionExist()}
     *
     * @return Observable object of boolean
     */
    public Observable<Boolean> isSessionExist() {
        return mSessionRepository.isSessionExist()
                .subscribeOn(Schedulers.io());
    }

    /**
     * this method is used to handle different network exceptions. Throws new Single object if exception
     * occurs. it checks for the {@code HttpException} and {@code UnknownHostException}
     *
     * @param throwable object to be thrown for testing Exceptions
     * @return Single object
     */
    private Single handleChangePasswordHttpException(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            final PasswordValidationError error = PasswordValidationError.create(
                    PasswordValidationException.ResetPasswordField.NONE,
                    mContext.getString(R.string.error_change_password));
            return Single.error(new PasswordValidationException(Collections.singletonList(error)));
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new NetworkException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }
}

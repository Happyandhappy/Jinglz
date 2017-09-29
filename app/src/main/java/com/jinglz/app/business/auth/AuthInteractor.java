package com.jinglz.app.business.auth;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.jinglz.app.BuildConfig;
import com.jinglz.app.Constants;
import com.jinglz.app.R;
import com.jinglz.app.business.auth.validation.ForgotPasswordValidation;
import com.jinglz.app.business.auth.validation.SignInLocalValidation;
import com.jinglz.app.business.auth.validation.SignUpLocalValidation;
import com.jinglz.app.business.base.validation.PasswordValidation;
import com.jinglz.app.data.log.UserLogProducer;
import com.jinglz.app.data.network.models.ChangePasswordModel;
import com.jinglz.app.data.network.models.ForgotPasswordRequest;
import com.jinglz.app.data.network.models.ForgotPasswordResponse;
import com.jinglz.app.data.network.models.ReferralData;
import com.jinglz.app.data.network.models.ResetPasswordRequest;
import com.jinglz.app.data.network.models.UploadFileResponse;
import com.jinglz.app.data.network.models.facebook.FacebookUserData;
import com.jinglz.app.data.network.models.google.GoogleSignUpModel;
import com.jinglz.app.data.network.models.user.AlreadyRegisterResponse;
import com.jinglz.app.data.network.models.user.SignInRequest;
import com.jinglz.app.data.network.models.user.SignInSocialRequest;
import com.jinglz.app.data.network.models.user.SignUpRequest;
import com.jinglz.app.data.network.models.user.SignUpSocialRequest;
import com.jinglz.app.data.network.models.user.SignUpSocialUser;
import com.jinglz.app.data.network.models.user.SignUpUser;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.data.panel.MixpanelManager;
import com.jinglz.app.data.repositories.AuthRepository;
import com.jinglz.app.data.repositories.DeviceRepository;
import com.jinglz.app.data.repositories.FilesRepository;
import com.jinglz.app.data.repositories.UserRepository;
import com.jinglz.app.ui.resetpassword.models.ResetPasswordModel;
import com.jinglz.app.ui.signup.models.FacebookSignUpModel;
import com.jinglz.app.ui.signup.models.SignUpModel;
import com.jinglz.app.ui.signup.models.VerifyPhoneData;
import com.jinglz.app.ui.start.signin.models.SignInManualModel;
import com.mlsdev.rximagepicker.Sources;

import org.json.JSONObject;

import java.io.File;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@Singleton
public class AuthInteractor {

    private static final String TAG = "AuthInteractor";
    private static final String GRANT_TYPE_SOCIAL = "client_credentials";

    private static final String PROVIDER_GOOLE = "google";
    private static final String PROVIDER_FACEBOOK = "facebook";

    private final DeviceRepository mDeviceRepository;
    private final MixpanelManager mMixpanelManager;
    private final AuthRepository mAuthRepository;
    private final FilesRepository mFilesRepository;
    private final UserRepository mUserRepository;
    private final UserLogProducer mUserLogProducer;
    private final SignInLocalValidation mSignInLocalValidation;
    private final SignUpLocalValidation mSignUpLocalValidation;
    private final ForgotPasswordValidation mForgotPasswordValidation;
    private final PasswordValidation mPasswordValidation;
    private final Context mContext;
    private final Gson mGson;

    /**
     * Constructs new AuthInteractor with specified deviceRepository, mixpanelManager, authRepository,
     * filesRepository, userRepository, userLogProducer, signInLocalValidation, signUpLocalValidation,
     * forgotPasswordValidation, passwordValidation, context and gson
     *
     * @param deviceRepository that holds unique device token
     * @param mixpanelManager that holds user detail
     * @param authRepository keeps track of authentication
     * @param filesRepository that keeps track of files
     * @param userRepository for creating user
     * @param userLogProducer used for producing user response
     * @param signInLocalValidation performs sign in validation for local credentials
     * @param signUpLocalValidation performs sign up validation for local credentials
     * @param forgotPasswordValidation performs forgot password validation for local credentials
     * @param passwordValidation performs password validation
     * @param context  for application specific resources
     * @param gson  for json parsing using Gson
     */
    @Inject
    public AuthInteractor(DeviceRepository deviceRepository,
            MixpanelManager mixpanelManager,
            AuthRepository authRepository,
            FilesRepository filesRepository,
            UserRepository userRepository,
            UserLogProducer userLogProducer,
            SignInLocalValidation signInLocalValidation,
            SignUpLocalValidation signUpLocalValidation,
            ForgotPasswordValidation forgotPasswordValidation,
            PasswordValidation passwordValidation, Context context, Gson gson) {
        mDeviceRepository = deviceRepository;
        mMixpanelManager = mixpanelManager;
        mAuthRepository = authRepository;
        mFilesRepository = filesRepository;
        mUserRepository = userRepository;
        mUserLogProducer = userLogProducer;
        mSignInLocalValidation = signInLocalValidation;
        mSignUpLocalValidation = signUpLocalValidation;
        mForgotPasswordValidation = forgotPasswordValidation;
        mPasswordValidation = passwordValidation;
        mContext = context;
        mGson = gson;
    }

    /**
     * method to check the authentication token. return true if token is null.
     *
     *
     * @return {@code Single.just(need)}
     */
    public Single<Boolean> needToSignIn() {
        final boolean need = mAuthRepository.getAuthToken() == null;
        return Single.just(need);
    }

    /**
     * method with specified signInModel. performs manual sign in process after testing validations using
     * {@link SignInLocalValidation#validateManualSignIn(SignInManualModel)}.
     * produce user response if success, error otherwise.
     *
     * @param signInModel hold credentials for signIn
     * @return return {@code Completable}
     */
    public Completable signInManual(SignInManualModel signInModel) {
        return Single.just(signInModel)
                .subscribeOn(Schedulers.io())
                .flatMap(mSignInLocalValidation::validateManualSignIn)
                .map(this::map)
                .flatMap(mAuthRepository::sendSignInManualRequest)
                .onErrorResumeNext(this::handleSignInHttpException)
                .doOnSuccess(userResponse ->
                        mMixpanelManager.trackUser(userResponse.id(), mUserLogProducer.produce(userResponse)))
                .toCompletable();
    }

    /**
     * method with specified token. performs facebook sign in process after testing validations using
     * {@link SignInLocalValidation#validateSocialSignIn(String)}.
     * produce user response if success, error otherwise.
     *
     * @param token String variable to be tested.
     * @return produce user response if success, error message otherwise
     */
    public Completable signInFacebook(String token) {
        return Single.just(token)
                .subscribeOn(Schedulers.io())
                .flatMap(mSignInLocalValidation::validateSocialSignIn)
                .map(this::map)
                .flatMap(signInSocialRequest ->
                        mAuthRepository.sendSocialSignInRequest(signInSocialRequest, PROVIDER_FACEBOOK))
                .onErrorResumeNext(this::handleSignInHttpException)
                .doOnSuccess(userResponse ->
                        mMixpanelManager.trackUser(userResponse.id(), mUserLogProducer.produce(userResponse)))
                .toCompletable();
    }

    /**
     * method with specified signInResult. performs Google sign in process after testing validations using
     * {@link SignInLocalValidation#validateSocialSignIn(String)}.
     * produce user response if success, error otherwise.
     *
     * @param signInResult
     * @return produce user response if success, error message otherwise
     */
    public Completable signInGoogle(GoogleSignInResult signInResult) {
        //noinspection ConstantConditions
        return getGoogleAuthToken(signInResult.getSignInAccount().getEmail())
                .subscribeOn(Schedulers.io())
                .flatMap(mSignInLocalValidation::validateSocialSignIn)
                .map(this::map)
                .flatMap(signInSocialRequest ->
                        mAuthRepository.sendSocialSignInRequest(signInSocialRequest, PROVIDER_GOOLE))
                .onErrorResumeNext(this::handleSignInHttpException)
                .doOnSuccess(userResponse ->
                        mMixpanelManager.trackUser(userResponse.id(), mUserLogProducer.produce(userResponse)))
                .toCompletable();
    }

    /**
     * method with specified url. download file from url and returns file if success,
     * null otherwise.
     *
     * @param url String variable contains url
     * @return null if url is null,
     *         {@code mFilesRepository.downloadFile(url).subscribeOn(Schedulers.io()).toObservable()} otherwise
     */
    public Observable<File> downloadSocialAvatar(String url) {
        if (url == null) {
            return Observable.just(null);
        }
        return mFilesRepository.downloadFile(url)
                .subscribeOn(Schedulers.io())
                .toObservable();
    }

    /**
     * method with specified facebookId. checks if facebook is already registered.
     * using {@link AuthRepository#checkFacebookAlreadyRegister(String)}
     *
     * @param facebookId String variable that contains facebookId
     * @return return error message if {@code alreadyRegistered}, true otherwise.
     */
    public Observable<Boolean> checkIsFacebookAlreadyRegister(String facebookId) {
        return mAuthRepository.checkFacebookAlreadyRegister(facebookId)
                .subscribeOn(Schedulers.io())
                .map(AlreadyRegisterResponse::data)
                .flatMap(alreadyRegistered -> !alreadyRegistered ? Single.just(true) : Single.error(new AuthException(
                        mContext.getString(R.string.error_facebook_user_already_exist))))
                .toObservable();
    }

    /**
     * method with specified model. performs manual sign up process after testing validations using
     * {@link SignInLocalValidation#validateManualSignIn(SignInManualModel)}.
     * produce user response if success, error otherwise.
     *
     *
     * @param model contains user detail
     * @return produce user response if success, error message otherwise
     */
    public Single<VerifyPhoneData> sendSignUpRequest(SignUpModel model) {
        return Single.just(model)
                .subscribeOn(Schedulers.io())
                .flatMap(mSignUpLocalValidation::validate)
                .map(facebookSignUpModel -> SignUpUser.create(model, mDeviceRepository.getDeviceToken(),
                        Constants.PLATFORM,
                        mDeviceRepository.getUniqueDeviceId()))
                .map(socialUser -> SignUpRequest.create(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, socialUser))
                .flatMap(mUserRepository::createUser)
                .onErrorResumeNext(this::handleSignUpHttpExceptions)
                .doOnSuccess(userResponse ->
                        mMixpanelManager.trackUser(userResponse.id(), mUserLogProducer.produce(userResponse)))
                .map(signInResponse -> VerifyPhoneData.create(model.getPhoneNumber(), model.getEmail()));
    }

    /**
     * method for signUp with specified access token and model of {@link FacebookSignUpModel} type.
     * app will use access token and user credentials for signUp process. It uses
     * {@link AuthRepository#sendSocialSignUpRequest(SignUpSocialRequest, String)}.
     * Produce {@code userResponse} if succeed, error message otherwise.
     *
     * @param accessToken unique access token
     * @param model {@link FacebookSignUpModel} variable that contains user details
     * @return produce {@code userResponse} if success, error message otherwise
     */
    public Completable sendSignUpRequest(String accessToken, FacebookSignUpModel model) {
        return Single.just(model)
                .subscribeOn(Schedulers.io())
                .flatMap(mSignUpLocalValidation::validate)
                .map(facebookSignUpModel -> SignUpSocialUser.create(model, mDeviceRepository.getDeviceToken(),
                        Constants.PLATFORM,
                        mDeviceRepository.getUniqueDeviceId()))
                .map(socialUser -> SignUpSocialRequest.create(BuildConfig.CLIENT_ID,
                        BuildConfig.CLIENT_SECRET,
                        accessToken, socialUser))
                .flatMap(signInSocialRequest ->
                        mAuthRepository.sendSocialSignUpRequest(signInSocialRequest, PROVIDER_FACEBOOK))
                .onErrorResumeNext(this::handleSignUpHttpExceptions)
                .doOnSuccess(userResponse ->
                        mMixpanelManager.trackUser(userResponse.id(), mUserLogProducer.produce(userResponse)))
                .toCompletable();
    }

    /**
     * method for signUp with specified access token and model of {@link GoogleSignUpModel} type.
     * app will use access token and user credentials for signUp process. It uses
     * {@link AuthRepository#sendSocialSignUpRequest(SignUpSocialRequest, String)}.
     * Produce {@code userResponse} if succeed, error message otherwise.
     *
     *
     * @param accessToken unique access token
     * @param model {@link GoogleSignUpModel} that contains user details
     * @return produce {@code userResponse} if success, error message otherwise
     */
    public Completable sendSignUpRequest(String accessToken, GoogleSignUpModel model) {
        return Single.just(model)
                .subscribeOn(Schedulers.io())
                .flatMap(mSignUpLocalValidation::validate)
                .map(facebookSignUpModel -> SignUpSocialUser.create(model, mDeviceRepository.getDeviceToken(),
                        Constants.PLATFORM,
                        mDeviceRepository.getUniqueDeviceId()))
                .map(socialUser -> SignUpSocialRequest.create(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
                        accessToken, socialUser))
                .flatMap(signInSocialRequest ->
                        mAuthRepository.sendSocialSignUpRequest(signInSocialRequest, PROVIDER_GOOLE))
                .onErrorResumeNext(this::handleSignUpHttpExceptions)
                .doOnSuccess(userResponse -> mMixpanelManager.trackUser(userResponse.id(),
                        mUserLogProducer.produce(userResponse)))
                .toCompletable();
    }

    /**
     * method with specified token. for requesting {@code facebookUserData}.
     * it uses {@code token} for requesting  {@link AuthRepository#requestFacebookUserData(AccessToken)},
     * perform json parsing using Gson. returns {@code facebookUserData}.
     *
     * @param token unique user token for requesting facebook user data.
     * @return {@code facebookUserData}
     */
    public Observable<FacebookUserData> requestFacebookUserData(AccessToken token) {
        return Observable.just(token)
                .flatMap(mAuthRepository::requestFacebookUserData)
                .subscribeOn(Schedulers.io())
                .map(GraphResponse::getJSONObject)
                .map(JSONObject::toString)
                .map(json -> new Gson().fromJson(json, FacebookUserData.class))
                .map(facebookUserData -> {
                    final String birthday = facebookUserData.getBirthday();
                    if (!TextUtils.isEmpty(birthday) && birthday.split("/").length != 2) {
                        final String[] birthdayParts = birthday.split("/");
                        facebookUserData.setBirthday(birthdayParts.length == 3 ? birthdayParts[2] : birthdayParts[0]);
                    }
                    return facebookUserData;
                });
    }

    /**
     * method with specified code and email for the confirmation of code generated to reset password.
     * it use {@link AuthRepository#validateCode(String, String)} for validation. returns true if code
     * is valid, {@link AuthException} otherwise.
     *
     * @param code String variable that contains code to reset password
     * @param email String variable that contains user email
     * @return returns true if code is valid, {@link AuthException} otherwise
     */
    public Completable confirmPasswordResetCode(String code, String email) {
        return mAuthRepository.validateCode(code, email)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(this::handleConfirmCodePasswordHttpException)
                .flatMap(status -> !status ?
                        Single.error(new AuthException(mContext.getString(R.string.error_incorrect_confirm_code)))
                        : Single.just(true))
                .toCompletable();
    }

    /**
     * method with specified email, to sending request using {@link AuthRepository#sendForgotPasswordRequest(ForgotPasswordRequest)}
     * Note, it performs validation process first using {@link ForgotPasswordValidation#validate(String)},throws
     * exception {@link AuthInteractor#handleForgotPasswordHttpException(Throwable)}
     *
     * @param email String variable contains user email
     * @return {@code Single<String>}
     */
    public Single<String> forgotPassword(String email) {
        return Single.just(email)
                .subscribeOn(Schedulers.io())
                .flatMap(mForgotPasswordValidation::validate)
                .map(ForgotPasswordRequest::create)
                .flatMap(mAuthRepository::sendForgotPasswordRequest)
                .onErrorResumeNext(this::handleForgotPasswordHttpException)
                .map(ForgotPasswordResponse::email);
    }

    /**
     * method with specified model of {@link ResetPasswordModel} type.performs validation
     * using {@link PasswordValidation#validate(ResetPasswordModel)}. send request
     * {@link AuthRepository#resetPassword(ResetPasswordRequest)} if valid.
     * return true if {@code status} true, {@link AuthException} otherwise.
     *
     * @param model of type {@link ResetPasswordModel}
     * @return Completable value for true if password reset, {@link AuthException} otherwise
     */
    public Completable resetPassword(ResetPasswordModel model) {
        return Single.just(model)
                .subscribeOn(Schedulers.io())
                .flatMap(mPasswordValidation::validate)
                .map(this::map)
                .flatMap(mAuthRepository::resetPassword)
                .onErrorResumeNext(this::handleResetPasswordHttpException)
                .flatMap(status -> !status ?
                        Single.error(new AuthException(mContext.getString(R.string.error_reset_password)))
                        : Single.just(true))
                .toCompletable();
    }

    /**
     * method with specified source to retrieve image using {@link FilesRepository#getImageFrom(Sources)}.
     * return file.
     *
     * @param source the source of enum type having two values CAMERA, GALLERY
     * @return Image file
     */
    public Single<File> getImageFrom(Sources source) {
        return mFilesRepository.getImageFrom(source);
    }

    /**
     * method with specified file. to upload Image using {@link FilesRepository#uploadImage(File)}
     * returns {@link UploadFileResponse}
     *
     * @param file File to be uploaded.
     * @return UploadFileResponse object.
     */
    public UploadFileResponse uploadImage(File file) {
        return mFilesRepository.uploadImage(file);
    }

    /**
     * method with specified code and phone. performs verification using {@code isPhoneVerified}
     * returns true if verified, {@link AuthException} otherwise.
     *
     * @param code String variable contains verification code.
     * @param phone String variable contains phone number.
     * @return Completable value for true if verified,
     *         {@code new AuthException(mContext.getString(R.string.error_incorrect_confirm_code_phone)}
     *         otherwise
     */
    public Completable verifyPhone(String code, String phone) {
        return mAuthRepository.verifyPhone(code, phone)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(this::handleConfirmCodePhoneHttpException)
                .map(UserResponse::isPhoneVerified)
                .flatMap(verified -> !verified ?
                        Single.error(new AuthException(mContext.getString(R.string.error_incorrect_confirm_code_phone)))
                        : Single.just(true))
                .toCompletable();
    }

    /**
     * This method is used to request verification code.
     * It uses String variable that contains user email. It uses
     * {@link AuthRepository#requestPhoneVerificationCode(String)} method that request api to
     * requesting verification.
     *
     *
     * @param email String variable that contains user email
     * @return {@code mAuthRepository.requestPhoneVerificationCode(email).subscribeOn(Schedulers.io()).toCompletable()}
     */
    public Completable requestPhoneVerificationCode(String email) {
        return mAuthRepository.requestPhoneVerificationCode(email)
                .subscribeOn(Schedulers.io())
                .toCompletable();
    }

    /**
     * This method is used to retrieve referral data. It takes referringParams as an input parameter,
     * checks if already signed in using {@link AuthInteractor#needToSignIn()}. Returns null if
     * {@code needToSignIn} false, otherwise parse JSONObject using Gson and return {@code referralData}
     *
     *
     * @param referringParams JSONObject that needs parsing to retrieve referralData
     * @return referralData if {@code needToSignIn} true, null otherwise
     */
    public Single<ReferralData> getReferralData(JSONObject referringParams) {
        return needToSignIn()
                .flatMap(needToSignIn -> {
                    if (needToSignIn) {
                        final ReferralData referralData = mGson.fromJson(referringParams.toString(), ReferralData.class);
                        return Single.just(referralData);
                    } else {
                        return Single.just(null);
                    }
                });
    }

    /**
     * method with account information to retrieve token using {@link AuthRepository#getGoogleToken(String)}
     * return string token.<p> Note {@link AuthRepository#getGoogleToken(String)} may String error message
     * if encounter IOException and GoogleAuthException.
     * </p>
     *
     * @param account variable that contains account information including its name and type
     * @return String token if succeed, error otherwise
     */
    public Single<String> getGoogleAuthToken(String account) {
        return Single.just(account)
                .subscribeOn(Schedulers.io())
                .flatMap(mAuthRepository::getGoogleToken);
    }

    /**
     *
     * @param throwable
     * @return
     */
    private Single handleSignInHttpException(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_sign_in_incorrect)));
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }

    /**
     * method with the specified throwable. checks for HttpException, return
     * {@code new AuthException(mContext.getString(R.string.error_user_not_found))} if throwable is instance
     * of {@link HttpException} and {@code new AuthException(mContext.getString(R.string.error_no_internet)}
     * if throwable is instance of {@link UnknownHostException}, {@code throwable} otherwise.
     *
     * @param  throwable the throwable (which is used for checking Exception)
     * @return Single object contains error message.
     *
     */
    private Single handleForgotPasswordHttpException(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_user_not_found)));
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }

    /**
     * method with specified throwable. handles different exceptions raised during signUp process.
     * returns corresponding error message for different Exception like HttpException, UnknownHostException,
     * {@code throwable} otherwise.
     *
     *
     * @param throwable the throwable (which is used for checking Exception)
     * @return Single object contains error message.
     */
    private Single handleSignUpHttpExceptions(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            final int code = ((HttpException) throwable).code();
            if (code / 100 == 4) {
                return Single.error(
                        new AuthException(mContext.getString(R.string.error_account_for_this_device_already_exist)));
            } else {
                return Single.error(new AuthException(mContext.getString(R.string.error_server_error)));
            }
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }

    /**
     * method with specified throwable. handles different exceptions raised during confirm password process.
     * returns corresponding error message for different Exception like HttpException, UnknownHostException,
     * {@code throwable} otherwise.
     *
     *
     * @param throwable the throwable (which is used for checking Exception)
     * @return Single object contains error message.
     */
    private Single handleConfirmCodePasswordHttpException(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_incorrect_confirm_code)));
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }

    /**
     * method with specified throwable. handles different exceptions raised during reset password process.
     * returns corresponding error message for different Exception like HttpException, UnknownHostException,
     * {@code throwable} otherwise.
     *
     *
     * @param throwable the throwable (which is used for checking Exception)
     * @return Single object contains error message.
     */
    private Single handleResetPasswordHttpException(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_reset_password)));
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }

    /**
     * method with specified throwable. handles different exceptions raised during confirmingphone code.
     * returns corresponding error message for different Exception like HttpException, UnknownHostException,
     * {@code throwable} otherwise.
     *
     *
     * @param throwable the throwable (which is used for checking Exception)
     * @return Single object contains error message.
     */
    private Single handleConfirmCodePhoneHttpException(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof HttpException) {
            final int code = ((HttpException) throwable).code();
            if (code / 100 == 4) {
                return Single.error(new AuthException(mContext.getString(R.string.error_incorrect_confirm_code_phone)));
            } else {
                return Single.error(new AuthException(mContext.getString(R.string.error_server_error)));
            }
        } else if (throwable instanceof UnknownHostException) {
            return Single.error(new AuthException(mContext.getString(R.string.error_no_internet)));
        }
        return Single.error(throwable);
    }

    /**
     * method that use model for signIn process.it makes new request by passing
     * {@code BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,model.email(), model.password(),
     * mDeviceRepository.getDeviceToken(), Constants.PLATFORM} and return SignInRequest object.
     *
     *
     * @param model SignInManualModel that contains user email and password needed for SignIn process
     * @return  SignInRequest object
     */
    private SignInRequest map(SignInManualModel model) {
        return SignInRequest.create(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
                model.email(), model.password(), mDeviceRepository.getDeviceToken(),
                Constants.PLATFORM);
    }

    /**
     * method that use model for signIn process.it makes new request by passing
     * {@code BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,facebookToken,
     * mDeviceRepository.getDeviceToken(), Constants.PLATFORM} and return SignInSocialRequest object.
     *
     *
     * @param facebookToken String variable that contains unique token for SignIn process
     * @return  SignInSocialRequest object
     */
    private SignInSocialRequest map(String facebookToken) {
        return SignInSocialRequest.create(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
                facebookToken, mDeviceRepository.getDeviceToken(),
                Constants.PLATFORM);
    }

    /**
     * method that use model to reset password.it makes new request by passing
     * {@code model.code(), model.email(), model.newPassword()} and return ResetPasswordRequest object.
     *
     *
     * @param model ResetPasswordModel that contains email, password and code for re-setting password
     * @return  ResetPasswordRequest object
     */
    private ResetPasswordRequest map(ResetPasswordModel model) {
        return ResetPasswordRequest.create(model.code(), model.email(), model.newPassword());
    }

}

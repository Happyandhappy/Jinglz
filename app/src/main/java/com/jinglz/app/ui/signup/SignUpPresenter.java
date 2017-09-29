package com.jinglz.app.ui.signup;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.arellomobile.mvp.InjectViewState;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.jinglz.app.App;
import com.jinglz.app.R;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.business.base.validation.ValidationError;
import com.jinglz.app.business.base.validation.ValidationException;
import com.jinglz.app.data.network.models.UploadFileResponse;
import com.jinglz.app.data.network.models.google.GoogleSignUpModel;
import com.jinglz.app.data.network.models.google.GoogleUserData;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.signup.models.CheckFacebookComposition;
import com.jinglz.app.ui.signup.models.FacebookSignUpModel;
import com.jinglz.app.ui.signup.models.SignUpModel;
import com.mlsdev.rximagepicker.Sources;

import java.io.File;
import java.lang.annotation.Retention;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@InjectViewState
public class SignUpPresenter extends BasePresenter<SignUpView> {

    private static final String TAG = "SignUpPresenter";
    public static final int MANUAL = 0;
    public static final int FACEBOOK = 1;
    public static final int GOOGLE = 2;

    @Inject AuthInteractor mAuthInteractor;
    @Inject Gson mGson;
    @Inject AnalyticsFacade mAnalyticsFacade;

    @SignUpType
    private int mSignUpType;

    private File mPhoto;
    private String mFacebookId;
    private String mFacebookAccessToken;
    private String mGoogleToken;

    /**
     * Constructs new SignUpPresenter
     */
    public SignUpPresenter() {
        App.get().getComponent().inject(this);
    }

    /**
     * method to set referral code from the specified String {@code code.}
     * @param code String variable contains referral code.
     */
    public void obtainParams(String code) {
        if (code != null) {
            getViewState().setReferralCode(code);
        }
    }

    /**
     * method with specified token to request for facebook user data.
     * retrieve user id, image and personal information. it calls
     * {@code requestFacebookUserData(token)}.
     *
     * @param token {@link AccessToken} object
     */
    public void setFacebookToken(AccessToken token) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        mFacebookAccessToken = token.getToken();
        final Subscription subscription = mAuthInteractor.requestFacebookUserData(token)
                .flatMap(data -> mAuthInteractor.checkIsFacebookAlreadyRegister(String.valueOf(data.getId())),
                         CheckFacebookComposition::create)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(data -> getViewState().fillViewFormFacebook(data.facebookUserData()))
                .doOnNext(data -> mSignUpType = FACEBOOK)
                .doOnNext(data -> mFacebookId = String.valueOf(data.facebookUserData().getId()))
                .observeOn(Schedulers.io())
                .flatMap(data -> mAuthInteractor.downloadSocialAvatar(data.facebookUserData().getAvatar()))
                .doOnNext(file -> mPhoto = file)
                .toCompletable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().hideProgress(), this::handleError);
        addSubscription(subscription);
    }

    /**
     * method with specified error to shoe error message.
     *
     * @param error {@link FacebookException} object to retrieve message from.
     */
    public void onSignUpFacebookError(FacebookException error) {
        getViewState().showError(error.getMessage());
    }

    /**
     * Method with specified model value to register user. if {@see mPhoto} is null it will call
     * {@link #uploadFile(SignUpModel, File)}, {@link #createUser(SignUpModel)} otherwise.
     *
     * @param model {@link SignUpModel} contains user details.
     */
    public void registerClick(SignUpModel model) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        getViewState().cleanErrors();
        if (mPhoto != null) {
            uploadFile(model, mPhoto);
        } else {
            createUser(model);
        }
    }

    /**
     * Method with specified sources to fetch user Image by calling {@link AuthInteractor#getImageFrom(Sources)}.
     * if done successfully file will b saved in {@see mPhoto}
     * @param sources
     */
    public void requestImage(Sources sources) {
        final Subscription subscription = mAuthInteractor.getImageFrom(sources).subscribe(file -> {
            mPhoto = file;
            getViewState().setAvatar(Uri.fromFile(file));
        }, this::handleError);
        addSubscription(subscription);
    }

    public void onLocationPermission(Context context, boolean granted) {
        if (!granted) {
            getViewState().showError(context.getString(R.string.error_locations_permission_rejected));
        }
    }

    public void onCodeConfirmed() {
        getViewState().startHome();
    }

    public void setGoogleAuthResult(GoogleSignInResult result) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        final GoogleSignInAccount account = result.getSignInAccount();
        if (account == null) {
            return;
        }
        final Subscription subscription = mAuthInteractor.getGoogleAuthToken(account.getEmail())
                .doOnSuccess(token -> mGoogleToken = token)
                .map(token -> GoogleUserData.create(account.getGivenName(),
                                                    account.getFamilyName(),
                                                    account.getPhotoUrl() != null
                                                            ? account.getPhotoUrl().toString()
                                                            : null,
                                                    token,
                                                    account.getEmail()))
                .toObservable()
                .flatMap(googleUserData -> mAuthInteractor.downloadSocialAvatar(googleUserData.avatar()),
                         (googleUserData, file) -> {
                             mPhoto = file;
                             return googleUserData;
                         })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    mSignUpType = GOOGLE;
                    getViewState().hideProgress();
                    getViewState().fillViewFormGoogle(data);
                }, this::handleError);
        addSubscription(subscription);
    }

    private void createUser(SignUpModel model) {
        switch (mSignUpType) {
            case FACEBOOK:
                createUserFacebook(model);
                break;
            case GOOGLE:
                createUserGoogle(model);
                break;
            case MANUAL:
            default:
                createUserManual(model);
                break;
        }
    }

    private void createUserManual(SignUpModel model) {
        mAuthInteractor.sendSignUpRequest(model)
                .doOnSuccess(ignored -> mAnalyticsFacade.trackEvent(Event.REGISTER_WITH_PASSWORD))
                .compose(bindToLifecycle().forSingle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    getViewState().hideProgress();
                    getViewState().onSignUpSuccess(data.phone(), data.email());
                }, this::handleError);
    }

    private void createUserFacebook(SignUpModel model) {
        mAuthInteractor.sendSignUpRequest(mFacebookAccessToken, FacebookSignUpModel.create(mFacebookId, model))
                .doOnCompleted(() -> mAnalyticsFacade.trackEvent(Event.REGISTER_WITH_FACEBOOK))
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getViewState().hideProgress();
                    getViewState().startHome();
                }, this::handleError);
    }

    private void createUserGoogle(SignUpModel model) {
        mAuthInteractor.sendSignUpRequest(mGoogleToken, GoogleSignUpModel.create(model))
                .doOnCompleted(() -> mAnalyticsFacade.trackEvent(Event.REGISTER_WITH_FACEBOOK))
                .compose(bindToLifecycle().forCompletable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getViewState().hideProgress();
                    getViewState().startHome();
                }, this::handleError);
    }

    private void uploadFile(SignUpModel model, File file) {
        final UploadFileResponse response = mAuthInteractor.uploadImage(file);
        response.getObserver().setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                switch (state) {
                    case COMPLETED:
                        model.setImage(response.getId());
                        createUser(model);
                        break;
                    case CANCELED:
                    case FAILED:
                        getViewState().hideProgress();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float progress = ((float) bytesCurrent) / bytesTotal * 100f;
            }

            @Override
            public void onError(int id, Exception ex) {
                getViewState().hideProgress();
                Log.e(TAG, "onError: ", ex);
            }
        });
    }

    /**
     *
     * @param throwable
     */
    private void handleError(Throwable throwable) {
        Log.e(TAG, "handleError: ", throwable);
        getViewState().hideProgress();
        if (throwable instanceof ValidationException) {
            final List<ValidationError> errors
                    = ((ValidationException) throwable).getErrors();
            final SignUpView view = getViewState();
            for (ValidationError error : errors) {
                switch (error.getField()) {
                    case ValidationException.Field.NONE:
                        view.showError(error.getMessage());
                        break;
                    case ValidationException.Field.FIRST_NAME:
                        view.setFirstNameError(error.getMessage());
                        break;
                    case ValidationException.Field.LAST_NAME:
                        view.setLastNameError(error.getMessage());
                        break;
                    case ValidationException.Field.EMAIL:
                        view.setEmailError(error.getMessage());
                        break;
                    case ValidationException.Field.PHONE_NUMBER:
                        view.setPhoneNumberError(error.getMessage());
                        break;
                    case ValidationException.Field.PASSWORD:
                        view.setPasswordError(error.getMessage());
                        break;
                    case ValidationException.Field.CONFIRM_PASSWORD:
                        view.setConfirmPasswordError(error.getMessage());
                        break;
                    case ValidationException.Field.ZIP_CODE:
                        view.setZipCodeError(error.getMessage());
                        break;
                    case ValidationException.Field.YEAR_OF_BIRTH:
                        view.setYearOfBirthError(error.getMessage());
                        break;
                    case ValidationException.Field.TERMS:
                        view.setTermsError(error.getMessage());
                        break;
                    case ValidationException.Field.COUNTRY:
                        view.showError(error.getMessage());
                        break;
                }
            }
        }
        if (throwable instanceof AuthException) {
            getViewState().showError(throwable.getMessage());
        }
    }

    @Retention(SOURCE)
    @IntDef({MANUAL, FACEBOOK, GOOGLE})
    public @interface SignUpType {

    }
}

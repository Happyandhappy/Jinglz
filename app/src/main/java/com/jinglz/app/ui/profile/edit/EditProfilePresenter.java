package com.jinglz.app.ui.profile.edit;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.jinglz.app.App;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.business.base.validation.ValidationError;
import com.jinglz.app.business.base.validation.ValidationException;
import com.jinglz.app.business.profile.ProfileInteractor;
import com.jinglz.app.data.local.event.UserDataChangedEvent;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.UploadFileResponse;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;
import com.mlsdev.rximagepicker.Sources;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class EditProfilePresenter extends BasePresenter<EditProfileView> {

    private static final String TAG = "EditProfilePresenter";
    @Inject AuthInteractor mAuthInteractor;
    @Inject ProfileInteractor mProfileInteractor;
    @Inject ImageLoader mImageLoader;

    private File mPhoto;
    private EditProfileModel mOldData;

    public EditProfilePresenter() {
        App.get().getSessionComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadUserData();
    }

    /**
     * This method is used to get image from {@code source}. it will set Avatar to
     * view if succeed, log error otherwise.
     * @param source
     */
    public void requestImage(Sources source) {
        final Subscription subscription = mAuthInteractor.getImageFrom(source).subscribe(file -> {
            mPhoto = file;
            getViewState().setAvatar(Uri.fromFile(file));
        }, throwable -> {
            Log.e(TAG, "requestImage: ", throwable);
        });
        addSubscription(subscription);
    }

    public void saveChanges(EditProfileModel model) {
        getViewState().showProgress();
        getViewState().hideKeyboard();
        getViewState().cleanErrors();
        if (mPhoto != null) {
            uploadFile(model, mPhoto);
        } else {
            updateUser(model);
        }
    }

    private void updateUser(EditProfileModel model) {
        final Subscription subscription = mProfileInteractor.updateUser(model)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::handleError);
        addSubscription(subscription);
    }

    private void onSuccess(UserResponse data) {
        if (!TextUtils.isEmpty(data.image())) {
            mImageLoader.invalidateCache();
        }
        getViewState().hideProgress();
        getViewState().onSaveSuccess();
        RxBus.get().post(new UserDataChangedEvent());
    }

    private void uploadFile(EditProfileModel model, File file) {
        final UploadFileResponse response = mProfileInteractor.uploadFile(mOldData.getId(), file);
        response.getObserver().setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                switch (state) {
                    case COMPLETED:
                        model.setImage(response.getId());
                        updateUser(model);
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

    private void loadUserData() {
        mProfileInteractor.getProfileUserData()
                .doOnNext(model -> mOldData = model)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    getViewState().fillUserData(data);
                }, throwable -> {
                    Log.e(TAG, "loadUserData: ", throwable);
                });
    }

    private void handleError(Throwable throwable) {
        getViewState().hideProgress();
        if (throwable instanceof ValidationException) {
            final List<ValidationError> errors
                    = ((ValidationException) throwable).getErrors();
            final EditProfileView view = getViewState();
            for (ValidationError error : errors) {
                switch (error.getField()) {
                    case ValidationException.Field.FIRST_NAME:
                        view.setFirstNameError(error.getMessage());
                        break;
                    case ValidationException.Field.LAST_NAME:
                        view.setLastNameError(error.getMessage());
                        break;
                    case ValidationException.Field.PHONE_NUMBER:
                        view.setPhoneNumberError(error.getMessage());
                        break;
                    case ValidationException.Field.ZIP_CODE:
                        view.setZipCodeError(error.getMessage());
                        break;
                    case ValidationException.Field.YEAR_OF_BIRTH:
                        view.setYearOfBirthError(error.getMessage());
                        break;
                }
            }
        }
    }
}

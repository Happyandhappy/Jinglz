package com.jinglz.app.ui.profile.edit;

import android.net.Uri;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jinglz.app.ui.base.BaseView;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;

/**
 * This interface is used to handle edit profile errors and
 * used to set user data and user image.
 *
 */
@StateStrategyType(value = SingleStateStrategy.class)
public interface EditProfileView extends BaseView {

    void fillUserData(EditProfileModel model);

    void setAvatar(Uri uri);

    void cleanErrors();

    void onSaveSuccess();

    void setFirstNameError(String message);

    void setLastNameError(String message);

    void setPhoneNumberError(String message);

    void setZipCodeError(String message);

    void setYearOfBirthError(String message);
}

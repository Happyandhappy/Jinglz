package com.jinglz.app.ui.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.jinglz.app.App;
import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.base.widget.CustomTextInputLayout;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;
import com.jinglz.app.ui.signup.models.Gender;
import com.jinglz.app.utils.DialogFactory;
import com.mlsdev.rximagepicker.Sources;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProfileActivity extends BaseActivity implements EditProfileView {

    @Inject
    ImageLoader mImageLoader;

    @InjectPresenter
    EditProfilePresenter mPresenter;

    @BindView(R.id.gender_radio_group) RadioGroup mGenderRadioGroup;
    @BindView(R.id.image_avatar) ImageView mImageAvatar;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.first_name_layout) CustomTextInputLayout mFirstNameLayout;
    @BindView(R.id.last_name_layout) CustomTextInputLayout mLastNameLayout;
    @BindView(R.id.phone_number_layout) CustomTextInputLayout mPhoneNumberLayout;
    @BindView(R.id.year_of_birth_layout) CustomTextInputLayout mYearOfBirthLayout;
    @BindView(R.id.toggle_male) ToggleButton mToggleMale;
    @BindView(R.id.toggle_female) ToggleButton mToggleFemale;
    @BindView(R.id.zip_code_layout) CustomTextInputLayout mZipCodeLayout;

    private BottomSheetMenuDialog mSheetDialog;

    private final RadioGroup.OnCheckedChangeListener mToggleListener = (radioGroup, i) -> {
        for (int j = 0; j < radioGroup.getChildCount(); j++) {
            final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
            view.setChecked(view.getId() == i);
        }
    };

    public static Intent getIntent(Context context) {
        return new Intent(context, EditProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        App.get().getComponent().inject(this);
        bind();
        setSupportActionBar(mToolbar);
        mGenderRadioGroup.setOnCheckedChangeListener(mToggleListener);
    }

    /**
     * Overridden method to load image from specified uri. it will be set in circular shape by
     * calling {@link ImageLoader#displayCircularImage(Uri, ImageView, int)}
     * @param uri to be loaded
     */
    @Override
    public void setAvatar(Uri uri) {
        mImageLoader.displayCircularImage(uri, mImageAvatar, getResources().getDimensionPixelSize(R.dimen.avatarSize));
    }

    /**
     * Overridden method to remove all the error text.
     */
    @Override
    public void cleanErrors() {
        mFirstNameLayout.setError(null);
        mLastNameLayout.setError(null);
        mPhoneNumberLayout.setError(null);
        mZipCodeLayout.setError(null);
        mYearOfBirthLayout.setError(null);
    }

    @Override
    public void onSaveSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * @param model
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void fillUserData(EditProfileModel model) {
        mFirstNameLayout.getEditText().setText(model.getFirstName());
        mLastNameLayout.getEditText().setText(model.getLastName());
        mPhoneNumberLayout.getEditText().setText(model.getPhone());
        mYearOfBirthLayout.getEditText().setText(String.valueOf(model.getYearOfBirth()));
        final Gender gender = Gender.getGenderFromValue(model.getGender());
        if (gender != null) {
            switch (gender) {
                case FEMALE:
                    mToggleFemale.setChecked(true);
                    break;
                case MALE:
                    mToggleMale.setChecked(true);
                    break;
            }
        }
        mZipCodeLayout.getEditText().setText(model.getZipCode());
        mImageLoader.displayCircularImage(model.getImage(), mImageAvatar, R.dimen.avatarSize, R.drawable.avatar_placeholder);
    }

    @OnClick({R.id.toggle_male, R.id.toggle_female})
    void toggleGender(ToggleButton button) {
        final int currentId = mGenderRadioGroup.getCheckedRadioButtonId();
        if (currentId == button.getId()) {
            return;
        }
        mGenderRadioGroup.check(button.getId());
    }

    @OnClick(R.id.text_edit_avatar)
    void editAvatarClick() {
        mSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog).setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_avatar_pick)
                .setItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_camera:
                            mPresenter.requestImage(Sources.CAMERA);
                            break;
                        case R.id.menu_library:
                            mPresenter.requestImage(Sources.GALLERY);
                            break;
                    }
                })
                .createDialog();
        mSheetDialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick(R.id.input_year_of_birth)
    void openYearPicker() {
        DialogFactory.createYearPickerDialog(this, year ->
                mYearOfBirthLayout.getEditText().setText(String.valueOf(year))).show();
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick(R.id.button_save)
    void onSaveClick() {
        final EditProfileModel model = new EditProfileModel();
        model.setFirstName(String.valueOf(mFirstNameLayout.getEditText().getText()));
        model.setLastName(String.valueOf(mLastNameLayout.getEditText().getText()));
        model.setPhone(String.valueOf(mPhoneNumberLayout.getEditText().getText()));
        model.setZipCode(String.valueOf(mZipCodeLayout.getEditText().getText()));
        Integer year = null;
        try {
            year = Integer.parseInt(String.valueOf(mYearOfBirthLayout.getEditText().getText()));
        } catch (NumberFormatException e) {
            //do nothing
        }
        model.setYearOfBirth(year);
        switch (mGenderRadioGroup.getCheckedRadioButtonId()) {
            case R.id.toggle_male:
                model.setGender(Gender.MALE.getGender());
                break;
            case R.id.toggle_female:
                model.setGender(Gender.FEMALE.getGender());
                break;
        }
        mPresenter.saveChanges(model);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFirstNameError(String error) {
        mFirstNameLayout.setError(error);
    }

    @Override
    public void setLastNameError(String error) {
        mLastNameLayout.setError(error);
    }

    @Override
    public void setPhoneNumberError(String error) {
        mPhoneNumberLayout.setError(error);
    }

    @Override
    public void setZipCodeError(String error) {
        mZipCodeLayout.setError(error);
    }

    @Override
    public void setYearOfBirthError(String error) {
        mYearOfBirthLayout.setError(error);
    }
}

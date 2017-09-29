package com.jinglz.app.ui.signup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jinglz.app.App;
import com.jinglz.app.R;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.facebook.FacebookGender;
import com.jinglz.app.data.network.models.facebook.FacebookUserData;
import com.jinglz.app.data.network.models.google.GoogleUserData;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.base.NoUnderlineClickableSpan;
import com.jinglz.app.ui.base.widget.CustomTextInputLayout;
import com.jinglz.app.ui.confirmcode.phone.ConfirmCodePhoneActivity;
import com.jinglz.app.ui.legalnotice.LegalNoticeActivity;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.signup.models.Gender;
import com.jinglz.app.ui.signup.models.SignUpModel;
import com.jinglz.app.ui.start.StartActivity;
import com.jinglz.app.utils.DialogFactory;
import com.jinglz.app.utils.ViewUtil;
import com.mlsdev.rximagepicker.Sources;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class SignUpActivity extends BaseActivity implements SignUpView, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignUpActivity";
    private static final String EXTRA_REFERRAL_CODE = "referralCodeExtra";
    private static final int PHONE_VALIDATION = 123;
    private static final int GOOGLE_AUTH = 124;

    @InjectPresenter
    SignUpPresenter mPresenter;

    @Inject
    ImageLoader mImageLoader;

    @BindView(R.id.first_name_layout) CustomTextInputLayout mFirstNameLayout;
    @BindView(R.id.last_name_layout) CustomTextInputLayout mLastNameLayout;
    @BindView(R.id.email_layout) CustomTextInputLayout mEmailLayout;
    @BindView(R.id.password_layout) CustomTextInputLayout mPasswordLayout;
    @BindView(R.id.confirm_password_layout) CustomTextInputLayout mConfirmPasswordLayout;
    @BindView(R.id.phone_number_layout) CustomTextInputLayout mPhoneNumberLayout;
    @BindView(R.id.zip_code_layout) CustomTextInputLayout mZipCodeLayout;
    @BindView(R.id.year_of_birth_layout) CustomTextInputLayout mYearOfBirthLayout;
    @BindView(R.id.referrer_code_layout) CustomTextInputLayout mReferrerCodeLayout;
    @BindView(R.id.check_terms_and_conditions) CheckBox mCheckTermsAndConditions;
    @BindView(R.id.image_avatar) ImageView mImageAvatar;
    @BindView(R.id.gender_radio_group) RadioGroup mGenderRadioGroup;
    @BindView(R.id.text_sign_up) TextView mTextSignUp;
    @BindView(R.id.container) LinearLayout mContainer;
    @BindView(R.id.text_terms_and_conditions) TextView mTextTermsAndConditions;

    private BottomSheetMenuDialog mSheetDialog;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;

    private final RadioGroup.OnCheckedChangeListener mToggleListener = (radioGroup, i) -> {
        for (int j = 0; j < radioGroup.getChildCount(); j++) {
            final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
            view.setChecked(view.getId() == i);
        }
    };


    public static Intent getIntent(Context context) {
        return getIntent(context, null);
    }

    /**
     * Return intent to perform operation. method with specified context and code to
     * start {@link SignUpActivity}
     * @param context To handle application specific content.
     * @param code String variable contains referral code.
     * @return intent
     */
    public static Intent getIntent(Context context, String code) {
        final Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_REFERRAL_CODE, code);
        return intent;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        App.get().getComponent().inject(this);
        bind();
        final TransformationMethod method = new PasswordTransformationMethod();
        mPasswordLayout.getEditText().setTransformationMethod(method);
        mConfirmPasswordLayout.getEditText().setTransformationMethod(method);
        mGenderRadioGroup.setOnCheckedChangeListener(mToggleListener);
        mPresenter.obtainParams(getIntent().getStringExtra(EXTRA_REFERRAL_CODE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int topPadding = ViewUtil.getStatusBarHeight(this);
            mContainer.setPadding(0, topPadding, 0, 0);
        }
        initSignUpText();
        initTermsText();
        initFacebook();
        initGoogle();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    /**
     * Initialize google and {@see mGoogleApiClient} for google plus
     * integration this build new {@link GoogleSignInOptions} and request for email and profile
     * information.
     */
    private void initGoogle() {
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * call this method to set text in {@see mTextSignUp}
     */
    private void initSignUpText() {
        final String full = getString(R.string.text_or_sign_up);
        final String part = getString(R.string.text_or_sign_up_black_part);
        final SpannableString spannableString = new SpannableString(full);
        final CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(getAssets(), FontConfig.black));
        final int start = full.indexOf(part);
        spannableString.setSpan(typefaceSpan, start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextSignUp.setText(spannableString);
    }

    private void initTermsText() {
        mTextTermsAndConditions.setMovementMethod(new LinkMovementMethod());
        final String full = getString(R.string.text_terms_accept);
        final String part = getString(R.string.text_terms_accept_highlight_part);
        final SpannableString spannableString = new SpannableString(full);
        final CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(getAssets(), FontConfig.black));
        final ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.accent));
        final int start = full.indexOf(part);
        spannableString.setSpan(typefaceSpan, start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new NoUnderlineClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(LegalNoticeActivity.getIntent(SignUpActivity.this));
            }
        }, start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextTermsAndConditions.setText(spannableString);
    }

    /**
     * Initialize facebook integration. on success it will ask for location permissions and
     * on acceptance of permissions set facebook token by calling {@link SignUpPresenter#setFacebookToken(AccessToken)}.
     * on Error it will call {@link SignUpPresenter#onSignUpFacebookError(FacebookException)}.
     */
    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getRxPermissions().request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .doOnNext(accept -> mPresenter.onLocationPermission(SignUpActivity.this, accept))
                        .filter(granted -> granted)
                        .doOnCompleted(() -> mPresenter.setFacebookToken(loginResult.getAccessToken()))
                        .subscribe();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                mPresenter.onSignUpFacebookError(error);
            }
        });
    }

    /**
     * Overridden method to handle activity result
     *
     * @param requestCode int value that contains a requested action code phone or google integration code
     * @param data Intent contains result data of phone validation or google signup
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_VALIDATION) {
            mPresenter.onCodeConfirmed();
        }
        if (requestCode == GOOGLE_AUTH) {
            if (resultCode == RESULT_OK) {
                final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                mPresenter.setGoogleAuthResult(result);
            }
        }
    }

    /**
     * when user successfully sign up onSignUpSuccess() is called after verify
     * startHome() is called {@link MainActivity}
     */
    @Override
    public void startHome() {
        startActivity(MainActivity.getIntent(this));
        finish();
    }

    @Override
    public void onSignUpSuccess(String phone, String email) {
        startActivityForResult(ConfirmCodePhoneActivity.getIntent(this, email, phone), PHONE_VALIDATION);
    }

    /**
     * @param error String contains error message of invalid first name
     */
    @Override
    public void setFirstNameError(String error) {
        mFirstNameLayout.setError(error);
    }

    /**
     * @param error String contains error message of invalid last name
     */
    @Override
    public void setLastNameError(String error) {
        mLastNameLayout.setError(error);
    }

    /**
     * @param error String contains error message of invalid email
     */
    @Override
    public void setEmailError(String error) {
        mEmailLayout.setError(error);
    }
    /**
     * @param error String contains error message of invalid phone number
     */
    @Override
    public void setPhoneNumberError(String error) {
        mPhoneNumberLayout.setError(error);
    }

    /**
     * @param error String contains error message of invalid password
     */
    @Override
    public void setPasswordError(String error) {
        mPasswordLayout.setError(error);
    }
    /**
     * @param error String contains error message of invalid confirmpassord
     */
    @Override
    public void setConfirmPasswordError(String error) {
        mConfirmPasswordLayout.setError(error);
    }

    /**
     * @param error String contains error message of invalid zip code
     */
    @Override
    public void setZipCodeError(String error) {
        mZipCodeLayout.setError(error);
    }

    /**
     * @param error String contains error message of invalid birthdate
     */
    @Override
    public void setYearOfBirthError(String error) {
        mYearOfBirthLayout.setError(error);
    }

    /**
     * @param error String contains error message of accepting terms and conditions
     */
    @Override
    public void setTermsError(String error) {
        showError(error);
    }

    /**
     * Clear all the errors
     */
    @Override
    public void cleanErrors() {
        mFirstNameLayout.setError(null);
        mLastNameLayout.setError(null);
        mEmailLayout.setError(null);
        mPasswordLayout.setError(null);
        mConfirmPasswordLayout.setError(null);
        mPhoneNumberLayout.setError(null);
        mZipCodeLayout.setError(null);
        mYearOfBirthLayout.setError(null);
        mReferrerCodeLayout.setError(null);
    }

    @Override
    public void setAvatar(Uri uri) {
        mImageLoader.displayCircularImage(uri, mImageAvatar, getResources().getDimensionPixelSize(R.dimen.avatarSize));
    }

    /**
     * Method with specified data to fill user detail after fetching information
     * from facebook.
     *
     * @param data {@link FacebookUserData} contains facebook data of user to show on views
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void fillViewFormFacebook(FacebookUserData data) {
        mPasswordLayout.setVisibility(View.GONE);
        mConfirmPasswordLayout.setVisibility(View.GONE);
        mFirstNameLayout.getEditText().setText(data.getFirstName());
        mLastNameLayout.getEditText().setText(data.getLastName());
        mEmailLayout.getEditText().setText(data.getEmail());
        mImageLoader.displayCircularImage(data.getAvatar(), mImageAvatar);
        mYearOfBirthLayout.getEditText().setText(data.getBirthday());
        final FacebookGender gender = data.getGenderAsEnum();
        if (gender != null) {
            switch (gender) {
                case FEMALE:
                    mGenderRadioGroup.check(R.id.toggle_female);
                    break;
                case MALE:
                    mGenderRadioGroup.check(R.id.toggle_male);
                    break;
            }
        }
    }

    /**
     * Method with specified data to fill user detail after fetching information
     * from google.
     *
     * @param data {@link GoogleUserData} contains google user data who is
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void fillViewFormGoogle(GoogleUserData data) {
        mPasswordLayout.setVisibility(View.GONE);
        mConfirmPasswordLayout.setVisibility(View.GONE);
        mFirstNameLayout.getEditText().setText(data.firstName());
        mLastNameLayout.getEditText().setText(data.lastName());
        mEmailLayout.getEditText().setText(data.email());
        mImageLoader.displayCircularImage(data.avatar(), mImageAvatar);
    }

    /**
     * method to set referral code.
     *
     * @param code String contains referral code to set in {@see mReferrerCodeLayout}
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void setReferralCode(String code) {
        mReferrerCodeLayout.getEditText().setText(code);
    }

    @OnClick(R.id.text_edit_avatar)
    public void editAvatarClick() {
        mSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_LIST)
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

    @OnClick(R.id.button_register)
    public void onRegisterClick() {
        getRxPermissions().request(Manifest.permission.ACCESS_FINE_LOCATION)
                .doOnNext(accept -> mPresenter.onLocationPermission(SignUpActivity.this, accept))
                .filter(granted -> granted)
                .doOnCompleted(this::register)
                .subscribe();
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick(R.id.input_year_of_birth)
    public void openYearPicker() {
        DialogFactory.createYearPickerDialog(this, year ->
                mYearOfBirthLayout.getEditText().setText(String.valueOf(year))).show();
    }

    /**
     * method to register new user. it will create new {@link SignUpModel} and set
     * values for user.
     */
    @SuppressWarnings("ConstantConditions")
    private void register() {
        final SignUpModel model = new SignUpModel();
        model.setFirstName(String.valueOf(mFirstNameLayout.getEditText().getText()));
        model.setLastName(String.valueOf(mLastNameLayout.getEditText().getText()));
        model.setEmail(String.valueOf(mEmailLayout.getEditText().getText()));
        model.setPhoneNumber(String.valueOf(mPhoneNumberLayout.getEditText().getText()));
        model.setPassword(String.valueOf(mPasswordLayout.getEditText().getText()));
        model.setConfirmPassword(String.valueOf(mConfirmPasswordLayout.getEditText().getText()));
        model.setZipCode(String.valueOf(mZipCodeLayout.getEditText().getText()));
        Integer year = null;
        try {
            year = Integer.parseInt(String.valueOf(mYearOfBirthLayout.getEditText().getText()));
        } catch (NumberFormatException e) {
            //do nothing
        }
        model.setYearOfBirth(year);
        model.setReferrerCode(String.valueOf(mReferrerCodeLayout.getEditText().getText()));
        model.setAcceptTermsAndConditions(mCheckTermsAndConditions.isChecked());
        switch (mGenderRadioGroup.getCheckedRadioButtonId()) {
            case R.id.toggle_male:
                model.setGender(Gender.MALE);
                break;
            case R.id.toggle_female:
                model.setGender(Gender.FEMALE);
                break;
        }
        mPresenter.registerClick(model);
    }

    @Override
    @OnClick(R.id.button_back)
    public void onBackPressed() {
        startActivity(StartActivity.getIntent(this, true));
        finish();
    }

    @OnClick({R.id.toggle_male, R.id.toggle_female})
    void toggleGender(ToggleButton button) {
        final int currentId = mGenderRadioGroup.getCheckedRadioButtonId();
        if (currentId == button.getId()) {
            return;
        }
        mGenderRadioGroup.check(button.getId());
    }

    @OnClick(R.id.button_facebook)
    public void onClick() {
        LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
    }

    @OnClick(R.id.button_google)
    public void onGoogleClick() {
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_AUTH);
    }

    /**
     * @param error String value contains error message while registration
     */
    @Override
    public void showError(String error) {
        Snackbar.make(mContainer, error, Snackbar.LENGTH_SHORT).show();
    }
}

package com.jinglz.app.ui.start.signin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jinglz.app.R;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.forgotpassword.ForgotPasswordActivity;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.signup.SignUpActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class SignInFragment extends BaseFragment implements SignInView, FacebookCallback<LoginResult>, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInFragment";

    private static final int GOOGLE_AUTH = 124;

    @InjectPresenter SignInPresenter mPresenter;

    @BindView(R.id.input_email) EditText mInputEmail;
    @BindView(R.id.input_password) EditText mInputPassword;
    @BindView(R.id.text_sign_in_method) TextView mTextSignInMethod;
    @BindView(R.id.coordinator) CoordinatorLayout mCoordinator;
    @BindView(R.id.button_sign_up) Button mButtonSignUp;

    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;

    /**
     * @return new Instance of SignInFragment
     */
    public static SignInFragment newInstance() {
        final Bundle args = new Bundle();
        final SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Initialize Facebook sdk in app.
         */
        FacebookSdk.sdkInitialize(getContext().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        bind(view);
        initViews(view.getContext());
        initGoogle();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GOOGLE_AUTH) {
                final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                mPresenter.onSignInGoogle(result);
            }
        }
    }

    /**
     * onLocationPermission check to continue login action
     */
    @OnClick(R.id.button_sign_in)
    public void onClickSignIn() {
        getRxPermissions().request(Manifest.permission.ACCESS_FINE_LOCATION)
                .doOnNext(mPresenter::onLocationPermission)
                .filter(granted -> granted)
                .doOnCompleted(() -> mPresenter.onClickSignIn(mInputEmail.getText().toString(), mInputPassword.getText().toString()))
                .subscribe();
    }

    @OnClick(R.id.button_sign_up)
    public void onClickSignUp() {
        startActivity(SignUpActivity.getIntent(getContext()));
    }

    @OnClick(R.id.button_forgot_password)
    public void onClickForgotPassword() {
        startActivity(ForgotPasswordActivity.getIntent(getContext()));
    }

    /**
     * Handle facebook button click event
     */
    @OnClick(R.id.button_facebook)
    public void onClickSignInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    /**
     * Handle google plus button click event
     */
    @OnClick(R.id.button_google)
    public void onClickSignInGoogle() {
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_AUTH);
    }

    /**
     *
     * @param loginResult onSuccess is called when facebook login action will perform successfully
     *                    it will firstly prompt user for location
     *                    and doOnCompleted will return facebook login token
     * {@link #onCancel()} if user cancel facebook login process
     * {@link #onError(FacebookException)} if facebook login contains exception error message
     */
    @Override
    public void onSuccess(LoginResult loginResult) {
        getRxPermissions().request(Manifest.permission.ACCESS_FINE_LOCATION)
                .doOnNext(mPresenter::onLocationPermission)
                .filter(granted -> granted)
                .doOnCompleted(() -> mPresenter.onSignInFacebook(loginResult.getAccessToken().getToken()))
                .subscribe();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
        mPresenter.onSignInFacebookError(error);
    }

    /**
     * {@link com.jinglz.app.ui.feed.FeedFragment} initialize in {@link MainActivity} will be called to redirect user on home page
     */
    @Override
    public void startFeed() {
        startActivity(MainActivity.getIntent(getContext()));
        ActivityCompat.finishAffinity(getActivity());
    }

    /**
     * Login helper dialog to prompt user for invalid inputs
     */
    @Override
    public void showLoginHelperDialog() {
        new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                .setTitle(R.string.text_incorrect_email_or_password)
                .setMessage(R.string.text_we_have_noticed_you_have_trouble_logging_in)
                .setPositiveButton(R.string.text_reset_password,
                        (dialog, which) -> startActivity(ForgotPasswordActivity.getIntent(getContext())))
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }

    /**
     *
     * @param connectionResult contains error result while login with google plus
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    /**
     * Initialize google plus login
     */
    private void initGoogle() {
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * Initialize Views
     * @param context contains the context of class
     */
    private void initViews(Context context) {
        final String signIn = getString(R.string.text_sign_in);
        final String using = getString(R.string.text_using);
        final String textSignIn = getString(R.string.text_sign_in_method);

        final SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(textSignIn);
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), FontConfig.black));
        sBuilder.setSpan(typefaceSpan, textSignIn.indexOf(signIn), textSignIn.indexOf(using), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextSignInMethod.setText(sBuilder, TextView.BufferType.SPANNABLE);

        final String signUp = getString(R.string.text_sign_up).toUpperCase();
        final String now = getString(R.string.text_now);
        final String textSignUp = getString(R.string.action_sign_up);

        sBuilder.clear();
        sBuilder.append(textSignUp);
        typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(context.getAssets(), FontConfig.bold));
        sBuilder.setSpan(typefaceSpan, textSignUp.indexOf(signUp), textSignUp.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mButtonSignUp.setText(sBuilder, TextView.BufferType.SPANNABLE);
    }
}

package com.jinglz.app.ui.resetpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.base.widget.CustomTextInputLayout;
import com.jinglz.app.ui.confirmcode.password.PasswordChangedActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseActivity implements ResetPasswordView {

    private static final String EXTRA_CODE = "EXTRA_CODE";
    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    @InjectPresenter ResetPasswordPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.input_password) TextInputEditText mInputPassword;
    @BindView(R.id.new_password_layout) CustomTextInputLayout mNewPasswordLayout;
    @BindView(R.id.input_confirm_password) TextInputEditText mInputConfirmPassword;
    @BindView(R.id.confirm_password_layout) CustomTextInputLayout mConfirmPasswordLayout;
    @BindView(R.id.root_container) CoordinatorLayout mRootContainer;

    public static Intent getIntent(Context context, String code, String email) {
        final Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_CODE, code);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        bind();
        getParams();
        initViews();
    }

    private void getParams() {
        final Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        final String code = extras.getString(EXTRA_CODE);
        final String email = extras.getString(EXTRA_EMAIL);
        mPresenter.init(code, email);
    }

    /**
     * This method is used to find views and initialize
     */
    private void initViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @OnClick(R.id.button_reset_password)
    public void onClickResetPassword() {
        mPresenter.onClickChangePassword(mInputPassword.getText().toString(), mInputConfirmPassword.getText().toString());
    }

    /**
     * @param error String value contains error message
     */
    @Override
    public void showError(String error) {
        Snackbar.make(mRootContainer, error, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * @param error String contains error in new password input.
     */
    @Override
    public void showNewPasswordError(String error) {
        mNewPasswordLayout.setError(error);
    }

    /**
     * @param error String contains error new password not match
     */
    @Override
    public void showConfirmPasswordError(String error) {
        mConfirmPasswordLayout.setError(error);
    }

    /**
     * Clear all errors found in class
     */
    @Override
    public void cleanErrors() {
        mNewPasswordLayout.setError(null);
        mConfirmPasswordLayout.setError(null);
    }

    /**
     * If password successfully changed {@link PasswordChangedActivity} will be launched.
     */
    @Override
    public void passwordChanged() {
        startActivity(PasswordChangedActivity.getIntent(this));
        finish();
    }
}

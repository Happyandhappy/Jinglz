package com.jinglz.app.ui.forgotpassword;

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
import com.jinglz.app.ui.confirmcode.password.ConfirmCodePasswordActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordView {

    @InjectPresenter ForgotPasswordPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.input_email) TextInputEditText mInputEmail;
    @BindView(R.id.root_container) CoordinatorLayout mRootContainer;
    @BindView(R.id.email_layout) CustomTextInputLayout mEmailLayout;

    public static Intent getIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        bind();
        initViews();
    }

    @OnClick(R.id.button_forgot_password)
    public void onClickForgotPassword() {
        mPresenter.onClickRequestPassword(mInputEmail.getText().toString());
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * overridden method to show Snackbar for
     * error encountered.
     *
     * @param error String value contains error message
     */
    @Override
    public void showError(String error) {
        Snackbar.make(mRootContainer, error, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * overridden method to set error in text input field {@see mEmailLayout}.
     *
     * @param error String value contains error message to set
     */

    @Override
    public void showEmailError(String error) {
        mEmailLayout.setError(error);
    }

    /**
     * This method is used to remove data from text input field {@see mEmailLayout}.
     */
    @Override
    public void cleanErrors() {
        mEmailLayout.setError(null);
    }

    /**
     * overridden method to start {@link ConfirmCodePasswordActivity}
     * @param email String variable contains email to be passed
     */
    @Override
    public void startConfirmCode(String email) {
        startActivity(ConfirmCodePasswordActivity.getIntent(this, email));
        finish();
    }
}

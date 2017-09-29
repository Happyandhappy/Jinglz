package com.jinglz.app.ui.changepassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.data.network.models.ChangePasswordModel;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.base.widget.CustomTextInputLayout;
import com.jinglz.app.ui.confirmcode.password.PasswordChangedActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordView {

    @InjectPresenter ChangePasswordPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.old_password_layout) CustomTextInputLayout mOldPasswordLayout;
    @BindView(R.id.new_password_layout) CustomTextInputLayout mNewPasswordLayout;
    @BindView(R.id.confirm_password_layout) CustomTextInputLayout mConfirmPasswordLayout;

    public static Intent getIntent(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        bind();
        setSupportActionBar(mToolbar);
        setTitle(R.string.text_change_password);
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick(R.id.button_reset_password)
    public void onClick() {
        final String oldPass = String.valueOf(mOldPasswordLayout.getEditText().getText());
        final String newPass = String.valueOf(mNewPasswordLayout.getEditText().getText());
        final String confPass = String.valueOf(mConfirmPasswordLayout.getEditText().getText());
        mPresenter.changePassword(ChangePasswordModel.create(oldPass, newPass, confPass));
    }

    /**
     * calls {@link PasswordChangedActivity} activity on password changed.
     */
    @Override
    public void onPasswordChanged() {
        startActivity(PasswordChangedActivity.getIntent(this));
        finish();
    }

    /**
     * overridden method to show error for old password field
     */
    @Override
    public void showOldPasswordError(String message) {
        mOldPasswordLayout.setError(message);
    }

    /**
     * overridden method to show error for new password field
     */
    @Override
    public void showNewPasswordError(String message) {
        mNewPasswordLayout.setError(message);
    }

    /**
     * overridden method to show error for confirm password field
     */
    @Override
    public void showConfirmPasswordError(String message) {
        mConfirmPasswordLayout.setError(message);
    }

    /**
     * overridden method to remove all the errors
     */
    @Override
    public void cleanErrors() {
        mOldPasswordLayout.setError(null);
        mNewPasswordLayout.setError(null);
        mConfirmPasswordLayout.setError(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

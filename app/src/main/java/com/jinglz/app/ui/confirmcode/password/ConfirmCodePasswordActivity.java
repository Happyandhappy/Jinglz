package com.jinglz.app.ui.confirmcode.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.resetpassword.ResetPasswordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmCodePasswordActivity extends BaseActivity implements ConfirmCodePasswordView {

    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    @InjectPresenter ConfirmCodePasswordPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.root_container) CoordinatorLayout mRootContainer;
    @BindView(R.id.text_description) TextView mTextDescription;
    @BindView(R.id.text_code) PinEntryEditText mTextCode;

    public static Intent getIntent(Context context, String email) {
        final Intent intent = new Intent(context, ConfirmCodePasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code_password);
        ButterKnife.bind(this);
        bind();
        getParams();
        initViews();
    }

    @OnClick(R.id.button_continue)
    public void onClick() {
        mPresenter.validateCode(mTextCode.getText().toString());
    }

    /**
     * @param error String value contains error message
     */
    @Override
    public void showError(String error) {
        Snackbar.make(mRootContainer, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setEmail(String email) {
        mTextDescription.setText(getString(R.string.text_confirm_code_password, email));
    }

    /**
     * Start new Activity {@link ResetPasswordActivity} to confirm code entered by user
     * @param code String contains code input by user.
     * @param email  String contains user email imput
     */
    @Override
    public void startResetPassword(String code, String email) {
        startActivity(ResetPasswordActivity.getIntent(this, code, email));
        finish();
    }

    /**
     * used to initialize views.
     */
    private void initViews() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * method is used to get intent parameters.
     */
    private void getParams() {
        mPresenter.init(getIntent().getExtras().getString(EXTRA_EMAIL));
    }
}

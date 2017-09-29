package com.jinglz.app.ui.confirmcode.phone;

import android.app.Activity;
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

import butterknife.BindView;
import butterknife.OnClick;

public class ConfirmCodePhoneActivity extends BaseActivity implements ConfirmCodePhoneView {

    private static final String EXTRA_PHONE = "EXTRA_PHONE";
    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    @InjectPresenter ConfirmCodePhonePresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.root_container) CoordinatorLayout mRootContainer;
    @BindView(R.id.text_description) TextView mTextDescription;
    @BindView(R.id.text_code) PinEntryEditText mTextCode;

    public static Intent getIntent(Context context, String email, String phone) {
        final Intent intent = new Intent(context, ConfirmCodePhoneActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PHONE, phone);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code_phone);
        bind();
        getParams();
        initViews();
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClick() {
        mPresenter.validateCode(mTextCode.getText().toString());
    }

    @OnClick(R.id.button_send)
    public void onSendClickClick() {
        mPresenter.sendVerifyCodeRequest();
    }

    @OnClick(R.id.button_skip)
    public void onSkipClickClick() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /**
     * used to show error using snackbar.
     * @param error String value contains error message
     */
    @Override
    public void showError(String error) {
        Snackbar.make(mRootContainer, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPhone(String phone) {
        mTextDescription.setText(getString(R.string.text_confirm_code_phone, phone));
    }

    @Override
    public void onSuccess() {
        setResult(Activity.RESULT_OK);
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
        final Intent intent = getIntent();
        mPresenter.init(intent.getStringExtra(EXTRA_EMAIL), intent.getStringExtra(EXTRA_PHONE));
    }
}

package com.jinglz.app.ui.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class ServicesActivity extends BaseActivity implements ServicesView {

    private static final String TAG = "ServicesActivity";

    private static final int REQUEST_CODE_PROFILE_SHARING_LINK = 124;

    @InjectPresenter ServicesPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.switch_paypal) SwitchCompat mSwitchPaypal;

    public static Intent getIntent(Context context) {
        return new Intent(context, ServicesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        bind();
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.text_services));
    }

    @Override
    public void setSwitchChecked(boolean checked) {
        Log.d(TAG, "setSwitchChecked: " + checked);
        mSwitchPaypal.setChecked(checked);
    }

    @OnCheckedChanged(R.id.switch_paypal)
    void onSwitchChecked(boolean checked) {
        mPresenter.onCheckedChange(checked);
    }

    /**
     * Overridden method with specified configuration and oAuthScopes. used to start intent for
     * {@link PayPalProfileSharingActivity}.
     *
     * @param configuration {@link PayPalConfiguration} to pass as intent parameter
     * @param oAuthScopes {@link PayPalOAuthScopes} to pass as intent parameter.
     */
    @Override
    public void startLinkPayPalAuthorization(PayPalConfiguration configuration, PayPalOAuthScopes oAuthScopes) {
        final Intent intent = new Intent(this, PayPalProfileSharingActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, oAuthScopes);
        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING_LINK);
    }

    @Override
    public void startPayPalService(PayPalConfiguration configuration) {
        final Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);
    }

    @Override
    public void stopPayPalService() {
        stopService(new Intent(this, PayPalService.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final PayPalAuthorization auth = data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
            if (auth != null) {
                final String authorization_code = auth.getAuthorizationCode();
                Log.d(TAG, "onActivityResult: " + authorization_code);
                mPresenter.linkPayPalAccount(authorization_code);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            mPresenter.onLinkCanceled();
            Log.i(TAG, "The user canceled.");
        } else if (resultCode == PayPalProfileSharingActivity.RESULT_EXTRAS_INVALID) {
            mPresenter.onLinkCanceled();
            Log.i(TAG, "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. " + "Please see the docs.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

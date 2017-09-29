package com.jinglz.app.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.jinglz.app.R;
import com.jinglz.app.utils.ViewUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    private ProgressDialog mProgressDialog;
    protected RxPermissions mRxPermissions;

    /**
     * Used to start a progress dialog
     */
    public void showLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
            return;
        }

        mProgressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * Used to hide a progress dialog
     */
    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public RxPermissions getRxPermissions() {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        return mRxPermissions;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    /**
     * Call instead of ButterKnife.bind(activity)
     */
    protected void bind() {
        ButterKnife.bind(this);
    }

    /**
     * Call this to show toast of error message.
     *
     * @param error String value contains error message
     */
    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * call this to show progress dialog
     */
    @Override
    public void showProgress() {
        showLoadingDialog();
    }

    /**
     * call this to hide progress dialog
     */
    @Override
    public void hideProgress() {
        hideLoadingDialog();
    }

    /**
     * call this to hide keyboard
     */
    @Override
    public void hideKeyboard() {
        ViewUtil.hideKeyboard(this);
    }

}

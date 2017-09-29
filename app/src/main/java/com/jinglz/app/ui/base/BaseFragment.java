package com.jinglz.app.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends MvpAppCompatFragment implements BaseView {

    private Unbinder mUnbinder;

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    /**
     * used to Return BaseActivity.
     *
     * @return  BaseActivity
     * @throws RuntimeException if BaseActivity is null.
     */
    @NonNull
    public BaseActivity getBaseActivity() {
        final FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        throw new RuntimeException("BaseActivity is null");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mUnbinder == null) {
            throw new RuntimeException("Use this.bind(view) instead of ButterKnife.bind(fragment, view)");
        }
    }

    /**
     * Call after inflate view
     *
     * @param view view
     */
    protected void bind(@NonNull View view) {
        mUnbinder = ButterKnife.bind(this, view);
    }

    /**
     * this method is used to show progress dialog by calling {@link BaseActivity#showLoadingDialog()}
     */
    protected void showLoadingDialog() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showLoadingDialog();
    }
    /**
     * this method is used to hide progress dialog by calling {@link BaseActivity#hideLoadingDialog()}
     */
    protected void hideLoadingDialog() {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideLoadingDialog();
    }

    /**
     * this method is used to retrieve permissions by calling {@link BaseActivity#getRxPermissions()}
     */
    protected RxPermissions getRxPermissions() {
        final BaseActivity baseActivity = getBaseActivity();
        return baseActivity.getRxPermissions();
    }

    /**
     * this method is used to show toast with specified error by calling {@link BaseActivity#showError(String)}
     */
    @Override
    public void showError(String error) {
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.showError(error);
    }

    /**
     * call this to show Progress dialog
     */
    @Override
    public void showProgress() {
        showLoadingDialog();
    }

    /**
     * call this to hide Progress dialog
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
        final BaseActivity baseActivity = getBaseActivity();
        baseActivity.hideKeyboard();
    }

}

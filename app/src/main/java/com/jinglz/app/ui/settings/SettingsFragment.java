package com.jinglz.app.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.changepassword.ChangePasswordActivity;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.profile.edit.EditProfileActivity;
import com.jinglz.app.ui.services.ServicesActivity;
import com.jinglz.app.ui.start.StartActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment implements SettingsView, NavigationFragment {

    @InjectPresenter SettingsPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    /**
     * Returns new instance of SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
        bind(root);
        initToolbar();
        return root;
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_settings;
    }

    @Override
    public void openEditProfile() {
        startActivity(EditProfileActivity.getIntent(getContext()));
    }

    @Override
    public void openChangePassword() {
        startActivity(ChangePasswordActivity.getIntent(getContext()));
    }

    @Override
    public void openServices() {
        startActivity(ServicesActivity.getIntent(getContext()));
    }

    @Override
    public void onLogoutSuccess() {
        getActivity().finishAffinity();
        startActivity(StartActivity.getIntent(getContext(), true));
    }

    @OnClick({R.id.text_edit_profile, R.id.text_change_password, R.id.text_services, R.id.text_log_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_edit_profile:
                mPresenter.onEditProfileClick();
                break;
            case R.id.text_change_password:
                mPresenter.onChangePasswordClick();
                break;
            case R.id.text_services:
                mPresenter.onServicesClick();
                break;
            case R.id.text_log_out:
                mPresenter.onLogoutClick();
                break;
        }
    }

    /**
     * Initialize the tool bar
     */
    private void initToolbar() {
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, R.string.menu_settings);
    }
}

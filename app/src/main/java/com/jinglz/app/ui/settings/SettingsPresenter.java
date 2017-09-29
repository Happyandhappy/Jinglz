package com.jinglz.app.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.profile.ProfileInteractor;
import com.jinglz.app.data.panel.CrashlyticsManager;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    @Inject ProfileInteractor mProfileInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    /**
     * Constructor for settings Presenter
     */
    public SettingsPresenter() {
        App.get().getSessionComponent().inject(this);
    }

    /**
     * This method is used to perform edit profile click.
     */
    public void onEditProfileClick() {
        getViewState().openEditProfile();
    }

    /**
     * This method is used to perform change password click.
     */
    public void onChangePasswordClick() {
        getViewState().openChangePassword();
    }

    /**
     * This method is used to perform service click
     *
     */
    public void onServicesClick() {
        getViewState().openServices();
    }

    /**
     * When logout click is performed
     * {@link #onLogoutClick()} will be call
     */

    public void onLogoutClick() {
        mProfileInteractor.cleanUserData();
        mAnalyticsFacade.trackEvent(Event.LOGOUT);
        getViewState().onLogoutSuccess();
    }
}

package com.jinglz.app.ui.splash;

import com.arellomobile.mvp.InjectViewState;
import com.jinglz.app.App;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.business.update.MarketUpdateInteractor;
import com.jinglz.app.data.network.models.ReferralData;
import com.jinglz.app.data.network.models.update.UpdateModel;
import com.jinglz.app.ui.base.BasePresenter;
import com.jinglz.app.ui.splash.models.SplashModel;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject AuthInteractor mAuthInteractor;
    @Inject MarketUpdateInteractor mMarketUpdateInteractor;

    /**
     * Constructor for SplashPresenter
     */
    public SplashPresenter() {
        App.get().getComponent().inject(this);
    }

    /**
     *
     * @param referringParams
     */
    public void referralFlow(JSONObject referringParams) {
        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .toSingle()
                .flatMap(v -> Single.zip(mAuthInteractor.getReferralData(referringParams), mMarketUpdateInteractor.checkForUpdate(),
                        SplashModel::new))
                .compose(bindToLifecycle().forSingle())
                .subscribe(splashModel -> {
                    final ReferralData data = splashModel.getReferralData();
                    final UpdateModel updateModel = splashModel.getMarketUpdate();

                    if (updateModel != null && updateModel.isInNeedToUpdate()) {
                        getViewState().openNewVersionDialog(updateModel.getUpdate());
                    } else if (data != null) {
                        if (data.clickedBranchLink()) {
                            getViewState().openSignUp(data.referralCode());
                        } else {
                            getViewState().openOnboarding();
                        }
                    } else {
                        getViewState().openHome();
                    }
                }, Throwable::printStackTrace);
    }

    public void baseFlow() {
        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .toSingle()
                .flatMap(time -> Single.zip(mAuthInteractor.needToSignIn(),
                        mMarketUpdateInteractor.checkForUpdate(),
                        SplashModel::new))
                .compose(bindToLifecycle().forSingle())
                .subscribe(splashModel -> {
                    final UpdateModel updateModel = splashModel.getMarketUpdate();

                    if (updateModel != null && updateModel.isInNeedToUpdate()) {
                        getViewState().openNewVersionDialog(updateModel.getUpdate());
                    } else if (splashModel.isAuthorized()) {
                        getViewState().openHome();
                    } else {
                        getViewState().openOnboarding();
                    }
                }, Throwable::printStackTrace);
    }
}

package com.jinglz.app.ui.splash.models;

import android.support.annotation.Nullable;

import com.jinglz.app.data.network.models.ReferralData;
import com.jinglz.app.data.network.models.update.UpdateModel;

public class SplashModel {

    private boolean isAuthorized;
    private ReferralData referralData;

    @Nullable
    private UpdateModel marketUpdate;

    public SplashModel(boolean isAuthorized, @Nullable UpdateModel marketUpdate) {
        this.isAuthorized = isAuthorized;
        this.marketUpdate = marketUpdate;
    }

    public SplashModel(ReferralData referralData, @Nullable UpdateModel marketUpdate) {
        this.referralData = referralData;
        this.marketUpdate = marketUpdate;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public ReferralData getReferralData() {
        return referralData;
    }

    @Nullable
    public UpdateModel getMarketUpdate() {
        return marketUpdate;
    }
}

package com.jinglz.app.data.network.models;

import com.google.auto.value.AutoValue;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.data.network.models.user.UserResponse;

@AutoValue
public abstract class RedeemValidationModel {

    public abstract CurrentCoinsResponse coinResponse();

    public abstract int coinsSum();

    public abstract UserResponse userData();

    /**
     * returns new RedeemValidationModel object by constructing
     * new AutoValue_RedeemValidationModel with specified coinResponse, coinsSum and userData.
     *
     * @param coinResponse it holds coins information {@link CurrentCoinsResponse}
     * @param coinsSum total count of coins available to redeem
     * @param userData it holds user information {@link UserResponse}
     * @return Returns a new instance of RedeemValidationModel class
     */
    public static RedeemValidationModel create(CurrentCoinsResponse coinResponse, int coinsSum, UserResponse userData) {
        return new AutoValue_RedeemValidationModel(coinResponse, coinsSum, userData);
    }

}


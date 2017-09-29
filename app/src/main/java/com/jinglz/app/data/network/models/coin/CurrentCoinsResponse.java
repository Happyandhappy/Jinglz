package com.jinglz.app.data.network.models.coin;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class CurrentCoinsResponse {

    @SerializedName("currentCoins")
    public abstract int currentCoins();

    @SerializedName("ratio")
    public abstract double ratio();

    @SerializedName("minimumToRedeem")
    public abstract int minimumToRedeem();

    @SerializedName("messageMinimumRedeem")
    public abstract String messageMinimumRedeem();

    /**
     * static method with specified int currentCoins, ratio, minimumToRedeem and messageMinimumRedeem, to
     * construct new AutoValue_CurrentCoinsResponse
     *
     * @param currentCoins provides number of coins
     * @param ratio for redeem coins with given ratio
     * @param minimumToRedeem minimum number to redeem coins
     * @param messageMinimumRedeem string message to display
     * @return CurrentCoinsResponse object
     */
    public static CurrentCoinsResponse create(int currentCoins,
                                              double ratio,
                                              int minimumToRedeem,
                                              String messageMinimumRedeem) {
        return new AutoValue_CurrentCoinsResponse(currentCoins, ratio, minimumToRedeem, messageMinimumRedeem);
    }

    /**
     * static method with specified gson, for constructing new AutoValue_CurrentCoinsResponse.GsonTypeAdapter.
     *
     * @param gson value to pass for {@link AutoValue_CurrentCoinsResponse.GsonTypeAdapter}
     * @return TypeAdapter object of CurrentCoinsResponse type
     */
    public static TypeAdapter<CurrentCoinsResponse> typeAdapter(Gson gson) {
        return new AutoValue_CurrentCoinsResponse.GsonTypeAdapter(gson);
    }
}

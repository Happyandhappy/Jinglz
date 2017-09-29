package com.jinglz.app.data.repositories;

import com.jinglz.app.data.local.PreferencesHelper;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.coin.CoinsHistoryResponse;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.injection.session.PerSession;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@PerSession
public class CoinsRepository {

    private final Api mApi;
    private final PreferencesHelper mPreferences;

    /**
     * constructs new CoinsRepository with specified api and preferences.
     * {@code preferences} will be used to save coin history in sharedPreference.
     *
     * @param api Api instance
     * @param preferences sharedPreference
     */
    @Inject
    public CoinsRepository(Api api, PreferencesHelper preferences) {
        mApi = api;
        mPreferences = preferences;
    }

    /**
     * This method is used to fetch coins history according to specified time zone {@code gmt}.
     *
     * @param gmt variable containing timeZone
     * @return new instance of CoinsHistoryResponse
     */
    public Observable<CoinsHistoryResponse> getCoinsHistory(int gmt) {
        return Single.just(mPreferences.getCoinsHistory())
                .flatMapObservable(coinsHistoryResponse -> {
                    if (coinsHistoryResponse == null) {
                        return mApi.coinsHistory(gmt)
                                .map(DataResponse::data)
                                .doOnSuccess(mPreferences::setCoinsHistory)
                                .toObservable();
                    } else {
                        return Single.merge(Single.just(coinsHistoryResponse), mApi.coinsHistory(gmt)
                                .map(DataResponse::data)
                                .doOnSuccess(mPreferences::setCoinsHistory));
                    }
                })
                .distinctUntilChanged();
    }

    /**
     * This method is used to retrieve coins detail. it uses {@link Api#getCurrentCoins()}
     * to retrieve response and save in {@code mPreferences} using {@link PreferencesHelper#setCoins(CurrentCoinsResponse)}
     *
     * @return new Instance of CurrentCoinsResponse.
     */

    public Observable<CurrentCoinsResponse> getCoins() {
        return Single.just(mPreferences.getCoins())
                .flatMapObservable(currentCoinsResponse -> {
                    if (currentCoinsResponse == null) {
                        return mApi.getCurrentCoins()
                                .doOnSuccess(mPreferences::setCoins)
                                .toObservable();
                    } else {
                        return Single.merge(Single.just(currentCoinsResponse), mApi.getCurrentCoins()
                                .doOnSuccess(mPreferences::setCoins));
                    }
                });
    }

    /**
     * This method is used to retrieve current coins detail. it uses {@link Api#getCurrentCoins()}
     * to retrieve response and save in {@code mPreferences} using {@link PreferencesHelper#setCoins(CurrentCoinsResponse)}
     *
     * @return new Instance of CurrentCoinsResponse.
     */
    public Single<CurrentCoinsResponse> getCoinsUpToDay() {
        return mApi.getCurrentCoins()
                .doOnSuccess(mPreferences::setCoins);
    }
}

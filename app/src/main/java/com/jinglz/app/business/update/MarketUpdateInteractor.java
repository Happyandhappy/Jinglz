package com.jinglz.app.business.update;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.objects.Update;
import com.jinglz.app.data.network.models.update.UpdateModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class MarketUpdateInteractor {

    private final AppUpdaterUtils mAppUpdater;

    /**
     * constructs new MarketUpdateInteractor with specified appUpdaterUtils.
     *
     * @param appUpdaterUtils used to update version
     */

    @Inject
    public MarketUpdateInteractor(AppUpdaterUtils appUpdaterUtils) {
        mAppUpdater = appUpdaterUtils;
    }

    /**
     * This method is used to check for the updates. it constructs new AppUpdaterUtils and uses
     * UpdateListener to test if needs update. onSuccess it will constructs new UpdateModel with specified
     * isNeedToUpdate and update, throws error otherwise.
     *
     * @return Single object of UpdateModel
     */
    public Single<UpdateModel> checkForUpdate() {
        return Single.<UpdateModel>fromEmitter(emitter -> {

            final AppUpdaterUtils.UpdateListener listener = new AppUpdaterUtils.UpdateListener() {
                @Override
                public void onSuccess(Update update, Boolean isNeedToUpdate) {
                    emitter.onSuccess(new UpdateModel(isNeedToUpdate, update));
                }

                @Override
                public void onFailed(AppUpdaterError appUpdaterError) {
                    emitter.onError(new Throwable(appUpdaterError.name()));
                }
            };
            emitter.setCancellation(mAppUpdater::stop);

            mAppUpdater.withListener(listener);
            mAppUpdater.start();
        }).onErrorResumeNext(throwable -> Single.just(null));
    }
}

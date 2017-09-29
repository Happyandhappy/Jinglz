package com.jinglz.app.utils;


import android.content.Context;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import rx.Observable;
import rx.schedulers.Schedulers;

public class ConnectivityUtils {

    /**
     * Returns new instance of Connectivity with specified context that is used to
     * observes network connectivity. Information about network state, type and name are contained in
     * observed Connectivity object.
     *
     * @param context Context of the activity or an application
     * @return RxJava Observable with Connectivity class containing information about network state,
     * type and name
     */
    @NonNull
    public static Observable<Connectivity> getConnectivityObservable(@NonNull Context context) {
        return ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .filter(Connectivity.hasState(NetworkInfo.State.CONNECTED));
    }
}

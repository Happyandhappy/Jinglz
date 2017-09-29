package com.jinglz.app.utils.rx;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import rx.Observable;

public class ConnectivityUtils {

    /**
     * To check internet connection while using app
     * @param context Context of class in which hasInternetObservable() is used.
     * @return a boolean value
     *          true if  internet is connected.
     *          false if internet is not connected
     */
    @NonNull
    public static Observable<Boolean> hasInternetObservable(@NonNull Context context) {
        return ReactiveNetwork.observeNetworkConnectivity(context)
                .map(Connectivity.hasState(NetworkInfo.State.CONNECTED));
    }
}

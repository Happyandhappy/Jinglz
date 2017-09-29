package com.jinglz.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ApplicationState implements Application.ActivityLifecycleCallbacks {

    private final ApplicationForegroundListener mApplicationForegroundListener;
    private final List<Activity> mActivityList;

    public ApplicationState(@NonNull ApplicationForegroundListener applicationForegroundListener) {
        mApplicationForegroundListener = applicationForegroundListener;
        mActivityList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mActivityList.isEmpty()) {
            mApplicationForegroundListener.onForeground();
        }
        mActivityList.add(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActivityList.remove(activity);
        if (mActivityList.isEmpty()) {
            mApplicationForegroundListener.onBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public interface ApplicationForegroundListener {

        void onForeground();

        void onBackground();

    }
}

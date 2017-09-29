package com.jinglz.app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.injection.app.AppComponent;
import com.jinglz.app.injection.app.AppModule;
import com.jinglz.app.injection.app.DaggerAppComponent;
import com.jinglz.app.injection.session.SessionComponent;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends MultiDexApplication {

    private static App mApp;

    private AppComponent mComponent;
    private SessionComponent mSessionComponent;
    private AnalyticsFacade mAnalyticsFacade;


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mApp = this;
        mAnalyticsFacade = getComponent().provideAnalyticsFacade();

        Branch.getAutoInstance(this);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        Fabric.with(this, new Crashlytics(), new Answers());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(FontConfig.regular)
                .setFontAttrId(R.attr.fontPath)
                .build());

        registerActivityLifecycleCallbacks(new ApplicationState(mApplicationForegroundListener));
    }

    public static App get() {
        return mApp;
    }

    public AppComponent getComponent() {
        if (mComponent == null) {
            mComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return mComponent;
    }

    public SessionComponent getSessionComponent() {
        if (mSessionComponent == null) {
            mSessionComponent = getComponent().sessionComponentBuilder().build();
        }
        return mSessionComponent;
    }

    public void releaseSessionComponent() {
        mSessionComponent = null;
    }

    private final ApplicationState.ApplicationForegroundListener mApplicationForegroundListener =
            new ApplicationState.ApplicationForegroundListener() {

                @Override
                public void onForeground() {
                    mAnalyticsFacade.trackEvent(Event.OPEN_APPLICATION);
                }

                @Override
                public void onBackground() {
                    mAnalyticsFacade.trackEvent(Event.LEAVE_APPLICATION);
                }
            };
}

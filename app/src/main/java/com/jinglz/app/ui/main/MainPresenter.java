package com.jinglz.app.ui.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.jinglz.app.App;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.feed.FeedInteractor;
import com.jinglz.app.business.profile.ProfileInteractor;
import com.jinglz.app.data.local.event.UserDataChangedEvent;
import com.jinglz.app.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    private static final String TAG = "MainPresenter";
    @Inject ProfileInteractor mProfileInteractor;
    @Inject FeedInteractor mFeedInteractor;
    @Inject AnalyticsFacade mAnalyticsFacade;

    /**
     * Constructs new MainPresenter.
     */
    public MainPresenter() {
        App.get().getSessionComponent().inject(this);
        RxBus.get().register(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadUserData();
        loadContestResults();
        registerSessionListener();
    }

    @Override
    public void onDestroy() {
        RxBus.get().unregister(this);
        super.onDestroy();
    }
    /**
     * Used to handle event when user data changes. it will
     * load user data by calling {@link #loadUserData()}.
     *
     * @param event generated event of UserDataChangedEvent
     */
    @Subscribe
    public void onUserDataChanged(UserDataChangedEvent event) {
        Log.d(TAG, "onUserDataChanged: update user header");
        loadUserData();
    }
    /**
     * This method is used to register session listener. it first checks
     * whether session exists and track event for {@see SESSION_EXPIRED}.
     * Throws exception if Unauthorized.
     */
    private void registerSessionListener() {
        final Subscription subscription = mProfileInteractor.isSessionExist()
                .distinctUntilChanged()
                .filter(exist -> !exist)
                .doOnNext(ignored -> mAnalyticsFacade.trackEvent(Event.SESSION_EXPIRED))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> getViewState().onUnauthorized(),
                        throwable -> Log.e(TAG, "loadUserData: ", throwable));
        addSubscription(subscription);
    }

    /**
     * This method is used to retrieve subscribed user data by calling
     * {@link ProfileInteractor#getShortUserData()}. on success {@code shortUserData}
     * will be saved, throw error otherwise.
     */
    private void loadUserData() {
        final Subscription subscription = mProfileInteractor
                .getShortUserData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shortUserData -> getViewState().setUserData(shortUserData),
                        throwable -> {
                            Log.e(TAG, "loadUserData: ", throwable);
                        });
        addSubscription(subscription);
    }
    /**
     * This method is used to fetch contest result by calling {@link FeedInteractor#getContestResults()}
     * {@see contestResults} will be saved on success, throw error otherwise.
     */
    private void loadContestResults() {
        mFeedInteractor.getContestResults()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contestResults -> {
                    if (contestResults.size() > 0) {
                        getViewState().showEntriesAndWinningsDialog(contestResults);
                    }
                }, throwable -> Log.e(TAG, "loadContestResults: ", throwable));
    }
}

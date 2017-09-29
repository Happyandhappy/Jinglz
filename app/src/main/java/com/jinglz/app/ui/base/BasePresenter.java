package com.jinglz.app.ui.base;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.jinglz.app.utils.rx.lifecycle.PresenterEvent;
import com.jinglz.app.utils.rx.lifecycle.RxLifecyclePresenter;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Simplifies getting view. Assumes that we don't care if there are multiple views
 * attached and get the random one.
 */
public abstract class BasePresenter<T extends BaseView> extends MvpPresenter<T>
        implements LifecycleProvider<PresenterEvent> {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final BehaviorSubject<PresenterEvent> lifecycleSubject = BehaviorSubject.create();

    protected void addSubscription(Subscription subscription) {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        lifecycleSubject.onNext(PresenterEvent.CREATE);
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<PresenterEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <K> LifecycleTransformer<K> bindUntilEvent(@NonNull PresenterEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <K> LifecycleTransformer<K> bindToLifecycle() {
        return RxLifecyclePresenter.bindPresenter(lifecycleSubject);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(PresenterEvent.DESTROY);
        mSubscriptions.clear();
    }
}

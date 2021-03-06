package com.jinglz.app.utils.rx;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;


public class RetryWithDelay
        implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int _maxRetries;
    private final int _retryDelayMillis;
    private int _retryCount;

    /**
     * Constructor of RetryWithDelay
     * @param maxRetries Integer value contains number of maximum  retries to load image
     * @param retryDelayMillis Integer value contains retry delay time
     */

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        _maxRetries = maxRetries;
        _retryDelayMillis = retryDelayMillis;
        _retryCount = 0;
    }

    /**
     * @return new Instance of RetryWithDelay
     */
    public static RetryWithDelay getDefaultInstance() {
        return new RetryWithDelay(5, 250);
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.flatMap(throwable -> {
            if (++_retryCount < _maxRetries) {
                // When this Observable calls onNext, the original
                // Observable will be retried (i.e. re-subscribed).

                return Observable.timer(_retryCount * _retryDelayMillis,
                        TimeUnit.MILLISECONDS);
            }

            // Max retries hit. Just pass the error along.
            return Observable.error(throwable);
        });
    }
}

package com.jinglz.app.ui.feed.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CoinsHistoryModel {

    /**
     * it is used to construct new AutoValue_CoinsHistoryModel with specified today,
     * week, month and current.
     *
     * @param today
     * @param week
     * @param month
     * @param current
     * @return AutoValue_CoinsHistoryModel instance
     */
    public static CoinsHistoryModel create(double today, double week, double month, double current) {
        return new AutoValue_CoinsHistoryModel(today, week, month, current);
    }

    public abstract double today();

    public abstract double week();

    public abstract double month();

    public abstract double current();

}

package com.jinglz.app.data.local.event;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UpdateBalanceEvent {

    public abstract int balance();

    /**
     * create new UpdateBalanceEvent with specified value balance.
     *
     * @param balance number value to update balance event
     * @return {@code new AutoValue_UpdateBalanceEvent(balance)}
     */
    public static UpdateBalanceEvent create(int balance) {
        return new AutoValue_UpdateBalanceEvent(balance);
    }

}

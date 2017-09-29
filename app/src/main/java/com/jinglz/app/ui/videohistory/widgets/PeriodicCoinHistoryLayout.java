package com.jinglz.app.ui.videohistory.widgets;

import android.content.Context;
import android.widget.LinearLayout;

import com.jinglz.app.data.network.models.history.GameStatistic;

import java.util.List;

/**
 * An abstract class for {@link PeriodicCoinHistoryLayout} layout
 * and set Coins list contains {@link GameStatistic}
 */
public abstract class PeriodicCoinHistoryLayout extends LinearLayout {

    public PeriodicCoinHistoryLayout(Context context) {
        super(context);
    }

    public abstract void setCoins(List<GameStatistic> statistics);

}

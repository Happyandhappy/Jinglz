package com.jinglz.app.ui.videohistory.widgets;

import android.content.Context;
import android.widget.LinearLayout;

import com.jinglz.app.R;
import com.jinglz.app.data.network.models.history.GameStatistic;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeekCoinHistoryLayout extends PeriodicCoinHistoryLayout {

    @BindView(R.id.container_days) LinearLayout mContainerDays;
    @BindDimen(R.dimen.normal) int mSpacingNormal;

    public WeekCoinHistoryLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        inflate(getContext(), R.layout.layout_video_history_week, this);
        ButterKnife.bind(this);
    }

    /**
     * Set coins on views
     * @param statistics List that contains {@link GameStatistic}
     */
    @Override
    public void setCoins(List<GameStatistic> statistics) {
        mContainerDays.removeAllViews();
        for (int i = 0; i < statistics.size(); i++) {
            final TextViewHistoryDay textView = new TextViewHistoryDay(getContext());
            final GameStatistic statistic = statistics.get(i);
            textView.setDay(statistic.date(), statistic.total());

            if (i < statistics.size() - 1) {
                textView.setPadding(0, 0, mSpacingNormal, 0);
            }

            mContainerDays.addView(textView);
        }
    }
}

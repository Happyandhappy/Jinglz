package com.jinglz.app.ui.videohistory.widgets;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.jinglz.app.R;
import com.jinglz.app.data.network.models.history.GameStatistic;
import com.jinglz.app.utils.DateUtils;
import com.jinglz.app.utils.NumberUtils;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthCoinHistoryLayout extends PeriodicCoinHistoryLayout {

    @BindView(R.id.container_weeks) LinearLayout mContainerWeeks;
    @BindView(R.id.text_title) TextView mTextTitle;
    @BindString(R.string.month_balance_header_format) String mHeaderFormat;
    @BindDimen(R.dimen.normal) int mSpacingNormal;

    /**
     * Public constructor
     * @param context context of class
     */
    public MonthCoinHistoryLayout(Context context) {
        super(context);
        init();
    }

    /**
     * Initialize view and inflate layout_video_history_month layout
     */
    private void init() {
        if (isInEditMode()) {
            return;
        }
        inflate(getContext(), R.layout.layout_video_history_month, this);
        ButterKnife.bind(this);
    }

    /**
     *
     * @param weeks List of {@link GameStatistic}
     */
    @Override
    public void setCoins(List<GameStatistic> weeks) {
        final int sum = Stream.of(weeks).map(GameStatistic::total).reduce((value1, value2) -> value1 + value2).get();
        mTextTitle.setText(String.format(mHeaderFormat, DateUtils.getCurrentMonthName(),
                                         NumberUtils.toPrettyNumber(sum)).toUpperCase());

        mContainerWeeks.removeAllViews();
        for (int i = 0; i < weeks.size(); i++) {
            final TextViewHistoryWeek textView = new TextViewHistoryWeek(getContext());
            final GameStatistic gameStatistic = weeks.get(i);
            textView.setDay(gameStatistic.date(), gameStatistic.total());
            if (i < weeks.size() - 1) {
                textView.setPadding(0, 0, mSpacingNormal, 0);
            }
            mContainerWeeks.addView(textView);
        }
    }
}

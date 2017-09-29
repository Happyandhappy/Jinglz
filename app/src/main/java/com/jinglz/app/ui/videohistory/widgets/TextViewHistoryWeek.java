package com.jinglz.app.ui.videohistory.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.utils.DateUtils;
import com.jinglz.app.utils.NumberUtils;

import java.util.Date;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Style class of TextView of History Weeks
 */
public class TextViewHistoryWeek extends TextView {

    @BindColor(R.color.textSecondary) int mColorTextSecondary;
    @BindDimen(R.dimen.normal) int mSpacingNormal;
    @BindDimen(R.dimen.very_small) int mSpacingVerySmall;
    @BindString(R.string.week_name_format) String mStringWeekFormat;
    @BindString(R.string.week_balance_format) String mStringWeekBalanceFormat;

    public TextViewHistoryWeek(Context context) {
        super(context);
        init(context);
    }

    public TextViewHistoryWeek(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewHistoryWeek(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewHistoryWeek(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    /**
     * Bind class with butterknife
     * @param context Context of the class
     */
    private void init(Context context) {
        ButterKnife.bind(this);
    }

    public void setDay(Date weekDate, int coins) {
        setTextColor(Color.WHITE);
        setPadding(0, 0, mSpacingNormal, 0);
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.line_black, 0, 0, 0);
        setCompoundDrawablePadding(mSpacingVerySmall);
        final String weekName = String.format(mStringWeekFormat, DateUtils.getWeekOfMonth(weekDate)).toUpperCase();
        final String text = String.format(mStringWeekBalanceFormat, weekName,
                                          NumberUtils.toPrettyNumber(coins)).toUpperCase();

        final Spannable spann = new SpannableString(text);

        final int spannIndex = text.indexOf(weekName);
        spann.setSpan(new ForegroundColorSpan(mColorTextSecondary), spannIndex,
                      spannIndex + weekName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        setText(spann);
    }
}

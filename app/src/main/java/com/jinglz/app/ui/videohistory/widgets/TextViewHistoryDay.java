package com.jinglz.app.ui.videohistory.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.utils.DateUtils;
import com.jinglz.app.utils.NumberUtils;

import java.util.Date;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Style class of TextView of History Days
 */
public class TextViewHistoryDay extends TextView {

    @BindColor(R.color.yellow) int mColorYellow;
    @BindDimen(R.dimen.very_small) int mSpacingVerySmall;
    @BindDimen(R.dimen.textNormal) int mTextNormal;

    public TextViewHistoryDay(Context context) {
        super(context);
        init(context);
    }

    public TextViewHistoryDay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewHistoryDay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewHistoryDay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        ButterKnife.bind(this);
    }

    public void setDay(Date day, int coins) {
        setTextColor(Color.WHITE);
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.line_black, 0, 0, 0);
        setCompoundDrawablePadding(mSpacingVerySmall);
        final String dayName = DateUtils.getDayName(day).toUpperCase();
        final String text = dayName + "\n" + NumberUtils.toPrettyNumber(coins);

        final Spannable spann = new SpannableString(text);

        final int spannIndex = text.indexOf(dayName);
        spann.setSpan(new ForegroundColorSpan(mColorYellow), spannIndex,
                      spannIndex + dayName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        spann.setSpan(new AbsoluteSizeSpan(mTextNormal, false), spannIndex,
                      spannIndex + dayName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        final CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(getContext().getAssets(), FontConfig.bold));
        spann.setSpan(typefaceSpan, spannIndex, dayName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        setText(spann);
    }
}

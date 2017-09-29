package com.jinglz.app.ui.feed.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.utils.NumberUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinHistoryLayout extends LinearLayout {

    @BindView(R.id.text_value) TextView mTextValue;
    @BindView(R.id.text_when) TextView mTextWhen;

    /**
     * constructs new CoinHistoryLayout with specified context.
     *
     * @param context to handle application-specified data
     */
    public CoinHistoryLayout(Context context) {
        super(context);
        init();
    }

    /**
     * constructs new CoinHistoryLayout with specified context and attrs.
     *
     * @param context to handle application-specified data
     * @param attrs set of attribute found with tags in xml
     */
    public CoinHistoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * constructs new CoinHistoryLayout with specified context, attrs and defStyleAttr.
     *
     * @param context to handle application-specified data
     * @param attrs set of attribute found with tags in xml
     * @param defStyleAttr value for style attribute
     */
    public CoinHistoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * constructs new CoinHistoryLayout with specified context, attrs, defStyleAttr and defStyleRes
     *
     * @param context to handle application-specified data
     * @param attrs set of attribute found with tags in xml
     * @param defStyleAttr value for style attribute
     * @param defStyleRes value for style resource
     */
    public CoinHistoryLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.layout_coin_history, this);
        ButterKnife.bind(this);
    }

    public void setValue(double value) {
        mTextValue.setText(NumberUtils.toPrettyNumber(value));
    }

    public void setWhen(String when) {
        mTextWhen.setText(when);
    }
}

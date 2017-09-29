package com.jinglz.app.ui.entrieswinnings;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinsLayout extends TableLayout {

    @BindView(R.id.text_tap_reveal_winnings) TextView mTextTapRevealWinnings;
    @BindView(R.id.table_row_time) TableRow mTableRowTime;
    @BindView(R.id.table_row_coins) TableRow mTableRowCoins;

    private int mLastOpenedCoin;
    private TotalResultListener mTotalResultListener;
    Context mContext;
    private final List<ContestResult> mContestResults = new ArrayList<>();

    /**
     * Construct new CoinsLayout with specified context.
     * @param context used to handle application-specific data.
     */
    public CoinsLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     * Construct new CoinsLayout with specified context and attrs.
     *
     * @param context used to handle application-specific data.
     * @param attrs A collection of attributes
     */
    public CoinsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * This method is used to set contest result to retrieve active views.
     * it calls {@link #initTimeView(List)} and {@link #initActiveCoins(List)} for this purpose.
     *
     * @param results list of {@link ContestResult}
     */
    public void setData(List<ContestResult> results) {
        mContestResults.addAll(results);
        initTimeView(results);
        initActiveCoins(results);
    }

    /**
     * used to initialize {@see mTotalResultListener}
     *
     * @param totalResultListener TotalResultListener instance to be assigned
     */
    public void setTotalResultListener(TotalResultListener totalResultListener) {
        mTotalResultListener = totalResultListener;
    }


    public boolean revealNext() {
        if (mLastOpenedCoin >= mContestResults.size()) {
            return false;
        }
        final FrameLayout view = (FrameLayout) mTableRowCoins.getVirtualChildAt(mLastOpenedCoin);
        final ImageView image = ButterKnife.findById(view, R.id.image_coin);
        final TextView text = ButterKnife.findById(view, R.id.text_coin);
        final ObjectAnimator flip = ObjectAnimator.ofFloat(image, "rotationY", 0f, 90f);
        flip.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                final ObjectAnimator flip = ObjectAnimator.ofFloat(text, "rotationY", 90f, 0f);
                flip.setDuration(200);
                flip.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        flip.setDuration(200);
        flip.start();
        if (mTotalResultListener != null) {
            mTotalResultListener.onResultChange(getTotal(mLastOpenedCoin));
        }
        mLastOpenedCoin++;
        return true;
    }


    private void init() {
        inflate(getContext(), R.layout.view_coins_layout, this);
        ButterKnife.bind(this);
        initTapRevealSpan();
        mLastOpenedCoin = 0;
    }

    private void initTapRevealSpan() {
        final String full = getContext().getString(R.string.text_tap_reveal_winnings);
        final String coin = "%coin%";
        final int idx = full.indexOf(coin);
        final SpannableString result = new SpannableString(full);
        final Drawable coinDrawable = ContextCompat.getDrawable(getContext(), R.drawable.discover_coin);
        final int size = getResources().getDimensionPixelSize(R.dimen.dialog_coin_tiny);
        coinDrawable.setBounds(0, 0, size, size);
        result.setSpan(new ImageSpan(coinDrawable, DynamicDrawableSpan.ALIGN_BOTTOM), idx,
                       idx + coin.length(),
                       Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextTapRevealWinnings.setText(result);
    }

    /**
     * method with specified results,that contains id, amount position and created information.
     * used to initialise time view.
     *
     * @param results
     */
    private void initTimeView(List<ContestResult> results) {
        for (int i = 0; i < mTableRowTime.getVirtualChildCount(); i++) {
            final TextView view = (TextView) mTableRowTime.getVirtualChildAt(i);
            if (i >= results.size()) {
                view.setText(null);
            } else {
                view.setText(DateUtils.toTimeFormat(DateUtils.fromServerFormat(results.get(i).getCreatedAt())));
            }
        }
    }

    /**
     * method with specified results,that contains id, amount position and created information.
     * used to initialise the state of active coins. if {@code i} is greater or equal to the {@code results}
     * size it will set Activation to false, true otherwise.
     *
     * @param results
     */
    private void initActiveCoins(List<ContestResult> results) {
        for (int i = 0; i < mTableRowCoins.getVirtualChildCount(); i++) {
            final FrameLayout view = (FrameLayout) mTableRowCoins.getVirtualChildAt(i);
            final ImageView image = ButterKnife.findById(view, R.id.image_coin);
            final TextView text = ButterKnife.findById(view, R.id.text_coin);
            if (i >= results.size()) {
                image.setActivated(false);
            } else {
                final ContestResult result = results.get(i);
                text.setText(String.format(Locale.getDefault(), "+%d", result.getAmount()));
                image.setActivated(true);
            }
        }
    }

    /**
     * Used to retrieve total amount from {@see mContestResults} for specified itemCount.
     *
     * @param itemCount to set limit
     * @return Returns total count
     * @throws IllegalArgumentException if {@code itemCount} is greater than {@see mContestResults} size
     */
    private int getTotal(int itemCount) {
        if (itemCount >= mContestResults.size()) {
            throw new IllegalArgumentException("Item count more that data size");
        }
        int total = 0;
        for (int i = 0; i <= itemCount; i++) {
            total += mContestResults.get(i).getAmount();
        }
        return total;
    }

    public interface TotalResultListener {

        void onResultChange(int total);
    }
}

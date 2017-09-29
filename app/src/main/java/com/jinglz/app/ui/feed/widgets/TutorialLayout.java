package com.jinglz.app.ui.feed.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.utils.ViewUtil;

import butterknife.BindDimen;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class TutorialLayout extends FrameLayout {

    @BindDimen(R.dimen.view_spacing_normal) int mSpacingNormal;
    @BindDimen(R.dimen.default_stroke_width) int mStrokeWidth;
    @BindDimen(R.dimen.button_height) int mButtonHeight;

    private Bitmap mBitmap;
    private View mLastTutorialView;
    private Paint mPaint;

    private TutorialListener mTutorialListener;

    private int mX;
    private int mY;

    private Button mButtonGotIt;
    private TextView mText;

    /**
     * constructs new TutorialLayout with specified context.
     *
     * @param context to handle application-specified resources
     */
    public TutorialLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    /**
     * constructs new TutorialLayout with specified context and attrs.
     *
     * @param context to handle application-specified resources
     * @param attrs collection of attribute
     */
    public TutorialLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * constructs new TutorialLayout with specified context, attrs and defStyleAttr.
     *
     * @param context to handle application-specified resources
     * @param attrs collection of attribute
     * @param defStyleAttr value to style atrribute
     */
    public TutorialLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TutorialLayout(@NonNull Context context,
                          @Nullable AttributeSet attrs,
                          @AttrRes int defStyleAttr,
                          @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setTutorialListener(TutorialListener tutorialListener) {
        mTutorialListener = tutorialListener;
    }

    /**
     * method with specified view, text and isLast, used to display tutorial.
     * view will be assigned to {@see mLastTutorialView}, if {@code isLast} is true
     * {@code R.string.action_got_it} will be set as text, {@code R.string.action_next} otherwise.
     *
     * @param view to initialize mLastTutorialView
     * @param text to set text for {@code mText}
     * @param isLast to check if tutorial is last
     */
    public void showTutorial(View view, CharSequence text, boolean isLast) {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
            mLastTutorialView.setDrawingCacheEnabled(false);
        }
        final int[] location = new int[2];
        mLastTutorialView = view;
        view.getLocationInWindow(location);
        view.setDrawingCacheEnabled(true);
        mText.setText(text);
        mButtonGotIt.setText(isLast ? R.string.action_got_it : R.string.action_next);
        mBitmap = view.getDrawingCache();
        mX = location[0];
        mY = location[1];
        setVisibility(VISIBLE);
        textTo(!inTop());
        invalidate();
    }

    /**
     * method is called to close the tutorial when completed or to show tutorial section.
     * {@code mBitmap} will recycled if not null.
     */
    public void closeTutorial() {
        setVisibility(GONE);
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
            mLastTutorialView.setDrawingCacheEnabled(false);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
            mLastTutorialView.setDrawingCacheEnabled(false);
            mLastTutorialView = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mX, mY, mPaint);

            final boolean inTop = inTop();

            final int lineX = mX + mBitmap.getWidth() / 2;
            final int lineY = mY + (inTop ? mBitmap.getHeight() + mSpacingNormal : -mSpacingNormal);
            final int lineYEnd = inTop ? mText.getTop() - mSpacingNormal : mText.getBottom() + mSpacingNormal;

            canvas.drawLine(lineX, lineY, lineX, lineYEnd, mPaint);
        }
    }

    /**
     * This method is used to test if image height is less than the {@code parentCenter}.
     * it will return true if {@code parentCenter} is equal to zero or greater than {@code viewCenter}.
     *
     * @return boolean true if {@code viewCenter < parentCenter || parentCenter == 0}
     */
    private boolean inTop() {
        final int viewCenter = mBitmap.getHeight() / 2 + mY;
        final int parentCenter = getHeight() / 2;
        return viewCenter < parentCenter || parentCenter == 0;
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        ButterKnife.bind(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth);
        final int topPadding;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            topPadding = ViewUtil.getStatusBarHeight(getContext());
        } else {
            topPadding = 0;
        }
        setPadding(mSpacingNormal, mSpacingNormal + topPadding, mSpacingNormal, mSpacingNormal);
        setClickable(true);
        setFocusable(true);
        setVisibility(GONE);
        setBackgroundResource(R.color.tutorial_background);
        initButton(context);
        initText(context);
        initCross(context);
    }

    /**
     *
     * @param context
     */
    private void initButton(Context context) {
        mButtonGotIt = new Button(context);
        final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                           (int) getResources().getDimension(R.dimen.button_height));
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        mButtonGotIt.setLayoutParams(layoutParams);
        mButtonGotIt.setBackgroundResource(R.drawable.background_button_blue);

        final TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mButtonGotIt.setForeground(getResources().getDrawable(outValue.resourceId, getContext().getTheme()));
        }

        mButtonGotIt.setTextColor(Color.WHITE);
        mButtonGotIt.setTypeface(TypefaceUtils.load(context.getAssets(), FontConfig.bold));
        addView(mButtonGotIt);

        mButtonGotIt.setOnClickListener(v -> {
            if (mTutorialListener != null) {
                mTutorialListener.next();
            }
        });
    }

    private void initCross(Context context) {
        final ImageView cross = new ImageView(context);
        final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                           ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.TOP | Gravity.END;
        cross.setLayoutParams(layoutParams);
        cross.setImageResource(R.drawable.ic_cross);

        addView(cross);

        cross.setOnClickListener(v -> {
            setVisibility(GONE);
            if (mTutorialListener != null) {
                mTutorialListener.completeAll();
            }
        });
    }

    private void initText(Context context) {
        mText = new TextView(context);
        mText.setTextColor(Color.WHITE);
        mText.setGravity(Gravity.CENTER);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textNormal));
        addView(mText);
    }

    private void textTo(boolean top) {
        final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                       ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER | (top ? Gravity.TOP : Gravity.BOTTOM);
        final int margin = mButtonHeight * 2;
        if (top) {
            layoutParams.setMargins(0, margin, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, margin);
        }
        mText.setLayoutParams(layoutParams);
    }

    public interface TutorialListener {

        void next();

        void completeAll();
    }
}

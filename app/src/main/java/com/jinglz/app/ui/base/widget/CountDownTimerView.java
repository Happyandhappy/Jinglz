package com.jinglz.app.ui.base.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jinglz.app.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CountDownTimerView extends TextView {

    /**
     * Callback interface for calling on finish and for tick specified time
     */
    public interface TimerListener {

        void onTick(long millisUntilFinished);

        void onFinish();
    }

    private long mHours;
    private long mMinutes;
    private long mSeconds;
    private long mMilliSeconds;

    private String mTimeFormat;

    private boolean mDisplayTime = true;

    private TimerListener mListener;
    private CountDownTimer mCountDownTimer;

    /**
     * Provide listener to set for timer.
     *
     * @param listener TimerListener instance
     */
    public void setOnTimerListener(TimerListener listener) {
        mListener = listener;
    }

    /**
     * Constructs new CountDownTimerView with specified context.
     *
     * @param context for accessing application-specific values.
     */
    public CountDownTimerView(Context context) {
        this(context, null);
    }

    /**
     * Constructs new CountDownTimerView with specified context and attrs.
     *
     * @param context for accessing application-specific values
     * @param attrs collection of attributes
     */
    public CountDownTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs new CountDownTimerView with specified context, attrs and defStyleAttr.
     *
     * @param context for accessing application-specific values
     * @param attrs collection of attributes
     * @param defStyleAttr style attribute
     */
    public CountDownTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * used to initialize {@see mDisplayTime}
     *
     * @param displayTime boolean value to be assign
     */
    public void setDisplayTime(boolean displayTime) {
        mDisplayTime = displayTime;
    }

    /**
     * Used to set the time format ro {@see mTimeFormat}.
     *
     * @param timeFormat String value containing time format
     */
    public void setTimeFormat(String timeFormat) {
        mTimeFormat = timeFormat;
    }

    /**
     * Method used to initialize counter. it will set new CountDownTimer with
     * {@see mMilliSeconds} and delay of 1000 sec.
     */
    private void initCounter() {
        mCountDownTimer = new CountDownTimer(mMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                calculateTime(millisUntilFinished);
                if (mListener != null) {
                    mListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                calculateTime(0);
                if (mListener != null) {
                    mListener.onFinish();
                }
            }
        };
    }

    /**
     * This method will start the count down if {@see mCountDownTimer} not equals to null.
     */
    public void startCountDown() {
        if (mMilliSeconds <= 0) {
            mMilliSeconds = 0;
            return;
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    /**
     * This method will stop the count down if {@see mCountDownTimer} not equals to null.
     */
    public void stopCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /**
     * This method is used to set time value after calculating it using {@link #calculateTime(long)}.
     *
     * @param value value to set
     * @param unit Time unit to convert in.
     */
    public void setTime(long value, TimeUnit unit) {
        stopCountDown();
        mMilliSeconds = unit.toMillis(value);
        initCounter();
        calculateTime(mMilliSeconds);
    }

    /**
     * This method is used to calculate time from specified milliSeconds and display by calling
     * {@link #displayText()}.
     *
     * @param milliSeconds
     */
    private void calculateTime(long milliSeconds) {
        mSeconds = (milliSeconds / 1000);
        mMinutes = mSeconds / 60;
        mSeconds = mSeconds % 60;

        mHours = mMinutes / 60;
        mMinutes = mMinutes % 60;

        displayText();
    }

    /**
     * This method is used to set text in {@code String.format(Locale.US, mTimeFormat, mHours, mMinutes, mSeconds)}
     * format.
     */
    private void displayText() {
        if (mDisplayTime) {
            if (mTimeFormat == null) {
                mTimeFormat = getContext().getString(R.string.time_hours);
            }
            setText(String.format(Locale.US, mTimeFormat, mHours, mMinutes, mSeconds));
        }
    }
}

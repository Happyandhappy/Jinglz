package com.jinglz.app.ui.start;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;

import java.lang.annotation.Retention;

import butterknife.BindView;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@FragmentWithArgs
public class OnboardingFragment extends BaseFragment {

    @BindView(R.id.container) RelativeLayout mContainer;
    @BindView(R.id.text_welcome) TextView mTextWelcome;
    @BindView(R.id.text_head) TextView mTextHead;
    @BindView(R.id.image_coin) ImageView mImageCoin;
    @BindView(R.id.text_title) TextView mTextTitle;
    @BindView(R.id.text_description) TextView mTextDescription;

    @Retention(SOURCE)
    @IntDef({PAGE_WATCH, PAGE_PLAY, PAGE_WIN})
    public @interface Page {

    }

    public static final int PAGE_WATCH = 0;
    public static final int PAGE_PLAY = 1;
    public static final int PAGE_WIN = 2;

    @Arg int mPage;

    /**
     * @param page Integer value contains index of pager.
     * @return new Instance of  OnboardingFragment
     */
    public static OnboardingFragment newInstance(@Page int page) {
        return OnboardingFragmentBuilder.newOnboardingFragment(page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        bind(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view.getContext());
        setPageInfo();
    }

    /**
     * Initialize view and change fonts
     * @param context Context of {@link OnboardingFragment}
     */
    private void initViews(Context context) {
        final String welcomeTo = getString(R.string.text_welcome_to);
        final String appName = getString(R.string.app_name).toUpperCase();
        final String title = welcomeTo + appName;

        final SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(welcomeTo).append(appName);
        final CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                TypefaceUtils.load(context.getAssets(), FontConfig.black));
        sBuilder.setSpan(typefaceSpan, title.indexOf(appName), title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextWelcome.setText(sBuilder, TextView.BufferType.SPANNABLE);
    }

    private void setPageInfo() {
        switch (mPage) {
            case PAGE_WATCH:
                setWatchPageInfo();
                break;
            case PAGE_PLAY:
                setPlayPageInfo();
                break;
            case PAGE_WIN:
                setWinPageInfo();
                break;
        }
    }

    /**
     * Show result of winning to user .... and setWin page info on views mTextHead, mImageCoin, mTextTitle, mTextDescription
     */
    private void setWinPageInfo() {
        mTextHead.setText(R.string.win_head);
        mImageCoin.setImageResource(R.drawable.ic_coin_win);
        mTextTitle.setText(R.string.text_win_title);
        mTextDescription.setText(R.string.text_win_description);
    }
    /**
     * Suggestion info for winning will show on views : mTextHead, mImageCoin, mTextTitle, mTextDescription
     */
    private void setPlayPageInfo() {
        mTextHead.setText(R.string.play_head);
        mImageCoin.setImageResource(R.drawable.ic_coin_play);
        mTextTitle.setText(R.string.text_play_title);
        mTextDescription.setText(R.string.text_play_description);
    }
    /**
     * Watch ads info views : mTextHead, mImageCoin, mTextTitle, mTextDescription
     */
    private void setWatchPageInfo() {
        mTextHead.setText(R.string.watch_head);
        mImageCoin.setImageResource(R.drawable.ic_coin_watch);
        mTextTitle.setText(R.string.text_watch_title);
        mTextDescription.setText(R.string.text_watch_description);
    }
}

package com.jinglz.app.ui.start;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.utils.ViewUtil;
import com.rd.PageIndicatorView;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

public class StartActivity extends BaseActivity implements StartView {

    private static final String TAG = "StartActivity";
    private static final String SKIP_ONBOARDING_KEY = "skipBoardingExtra";

    @InjectPresenter StartPresenter mPresenter;

    @BindView(R.id.pager_onboarding) ViewPager mPagerOnboarding;
    @BindView(R.id.navigation_container) FrameLayout mNavigationContainer;
    @BindView(R.id.button_next) Button mButtonNext;
    @BindView(R.id.button_back) Button mButtonBack;
    @BindView(R.id.pager_indicator) PageIndicatorView mPagerIndicator;

    @BindColor(R.color.red) int mSignInPagerIndicatorColor;
    @BindView(R.id.root_container) FrameLayout mRootContainer;

    public static Intent getIntent(Context context) {
        return getIntent(context, false);
    }

    public static Intent getIntent(Context context, boolean skipOnBoarding) {
        final Intent intent = new Intent(context, StartActivity.class);
        intent.putExtra(SKIP_ONBOARDING_KEY, skipOnBoarding);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bind();
        initViews();
        final boolean skipOnBoarding = getIntent().getBooleanExtra(SKIP_ONBOARDING_KEY, true);
        mPresenter.init(OnboardingPagerAdapter.PAGES_COUNT, skipOnBoarding);
    }

    @OnClick({R.id.button_next, R.id.button_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next:
                mPresenter.onNextClick();
                break;
            case R.id.button_back:
                mPresenter.onBackClick();
                break;
        }
    }
    /**
     * @param show boolean value to show/hide next button
     */
    @Override
    public void showNextButton(boolean show) {
        TransitionManager.beginDelayedTransition(mNavigationContainer);
        mButtonNext.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * @param show boolean value to show/hide back button
     */
    @Override
    public void showBackButton(boolean show) {
        TransitionManager.beginDelayedTransition(mNavigationContainer);
        mButtonBack.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setSelectedPage(int page) {
        if (page > mPagerOnboarding.getChildCount()) {
            return;
        }
        mPagerOnboarding.setCurrentItem(page);
    }
    /**
     * @param highlight boolean value to setIndicator highlight on mPagerIndicator
     */
    @Override
    public void highlightIndicator(boolean highlight) {
        mPagerIndicator.setSelectedColor(highlight ? mSignInPagerIndicatorColor : Color.WHITE);
    }

    /**
     * @param highlight boolean value to highlight back button
     */
    @Override
    public void highlightBackButton(boolean highlight) {
        mButtonBack.setTextColor(highlight ? Color.BLACK : Color.WHITE);
        mButtonBack.setCompoundDrawablesWithIntrinsicBounds(highlight ? R.drawable.ic_back_black
                                                                    : R.drawable.ic_back, 0, 0, 0);
    }

    /**
     * Skip Boarding
     */
    @Override
    public void skipOnBoarding() {
        final int position = mPagerOnboarding.getAdapter().getCount() - 1;
        mPagerOnboarding.setCurrentItem(position);
        mPagerOnboarding.post(() -> mPagerIndicator.setSelection(position));
    }

    /**
     * Initialize views,animations and page slider for introduction and contains login/sign up links
     */
    private void initViews() {
        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                                                                   ContextCompat.getColor(this, R.color.green),
                                                                   ContextCompat.getColor(this, R.color.red),
                                                                   ContextCompat.getColor(this, R.color.blue),
                                                                   Color.WHITE);

        colorAnimator.addUpdateListener(animator -> mPagerOnboarding.setBackgroundColor((Integer) animator.getAnimatedValue()));
        colorAnimator.setDuration((OnboardingPagerAdapter.PAGES_COUNT - 1) * 10000000000L);

        final OnboardingPagerAdapter onboardingPagerAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        mPagerOnboarding.setAdapter(onboardingPagerAdapter);
        mPagerOnboarding.setOffscreenPageLimit(onboardingPagerAdapter.getCount());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int topPadding = ViewUtil.getStatusBarHeight(this);
            mNavigationContainer.setPadding(0, topPadding, 0, 0);
        }
        mPagerOnboarding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                colorAnimator.setCurrentPlayTime((long) ((positionOffset + position) * 10000000000L));
            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.onPageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //no-op
            }
        });
    }

    /**
     *
     * @param error String value contains error message
     */
    @Override
    public void showError(String error) {
        Snackbar.make(mRootContainer, error, Snackbar.LENGTH_LONG).show();
    }
}

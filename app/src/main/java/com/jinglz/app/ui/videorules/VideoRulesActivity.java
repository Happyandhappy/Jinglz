package com.jinglz.app.ui.videorules;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;
import com.jinglz.app.ui.showvideo.ShowVideoActivity;
import com.jinglz.app.utils.ViewUtil;
import com.rd.PageIndicatorView;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoRulesActivity extends BaseActivity implements VideoRulesView {

    private static final String EXTRA_PRE_VIDEO_SHOW = "EXTRA_PRE_VIDEO_SHOW";
    private static final String EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID";
    private static final String EXTRA_CONTEST_ID = "EXTRA_CONTEST_ID";

    /**
     * Presenter of VideoRulesActivity
     */
    @InjectPresenter VideoRulesPresenter mPresenter;

    @BindView(R.id.pager_rules) ViewPager mPagerRules;
    @BindView(R.id.pager_indicator) PageIndicatorView mPagerIndicator;
    @BindView(R.id.button_next) Button mButtonNext;
    @BindView(R.id.button_back) Button mButtonBack;
    @BindView(R.id.navigation_container) FrameLayout mNavigationContainer;
    @BindView(R.id.root_container) FrameLayout mRootContainer;


    /**
     *
     * @param context Contain the context of class
     * @param video contains {@link AvailableVideoItemModel} date to send with intent
     * @return Intent actions to perform
     */
    public static Intent getIntentPreVideo(Context context, AvailableVideoItemModel video) {
        final Intent intent = new Intent(context, VideoRulesActivity.class);
        intent.putExtra(EXTRA_PRE_VIDEO_SHOW, true);
        intent.putExtra(EXTRA_VIDEO_ID, video.getId());
        intent.putExtra(EXTRA_CONTEST_ID, video.getContestId());
        return intent;
    }

    /**
     * It will send Intent just for playing rules
     * @param context contains context of class
     * @return Intent to redirect user to {@link VideoRulesActivity}
     */
    public static Intent getIntentJustRules(Context context) {
        final Intent intent = new Intent(context, VideoRulesActivity.class);
        intent.putExtra(EXTRA_PRE_VIDEO_SHOW, false);
        return intent;
    }

    /**
     *
     * @return VideoRulesPresenter to initialize a view for {@link VideoRulesPagerAdapter}
     */
    @ProvidePresenter
    VideoRulesPresenter providePresenter() {
        final Bundle extra = getIntent().getExtras();
        return new VideoRulesPresenter(extra.getBoolean(EXTRA_PRE_VIDEO_SHOW, false),
                                       extra.getString(EXTRA_VIDEO_ID),
                                       extra.getString(EXTRA_CONTEST_ID),
                                       VideoRulesPagerAdapter.PAGES_COUNT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_rules);
        bind();
        initViews();
    }

    /**
     *
     * @param view It contains an integer id
     *             to handle which button action will be perform
     */

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
     *
     * @param show boolean value which will show/hide Next Button when onPageChanged() is called
     */
    @Override
    public void showNextButton(boolean show) {
        TransitionManager.beginDelayedTransition(mNavigationContainer);
        mButtonNext.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     *
     * @param show boolean value which will show/hide Back Button when onPageChanged() is called
     */
    @Override
    public void showBackButton(boolean show) {
        TransitionManager.beginDelayedTransition(mNavigationContainer);
        mButtonBack.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     *
     * @param page Contains an integer value to set Current item of {@link VideoRulesPagerAdapter}
     */
    @Override
    public void setSelectedPage(int page) {
        if (page > mPagerRules.getChildCount()) {
            return;
        }
        mPagerRules.setCurrentItem(page);
    }

    /**
     * It will open video
     * @param videoId String contains value of videoid which will play currently
     * @param contestId String contains value of contest id
     */
    @Override
    public void openVideo(String videoId, String contestId) {
        startActivity(ShowVideoActivity.getIntentWithRules(this, videoId, contestId));
        finish();
    }

    @Override
    public void close() {
        finish();
    }

    /**
     * Initialize views
     * {@link android.support.v4.view.ViewPager.OnPageChangeListener} for handle page items
     */
    private void initViews() {
        final VideoRulesPagerAdapter videoRulesPagerAdapter = new VideoRulesPagerAdapter(getSupportFragmentManager());
        mPagerRules.setAdapter(videoRulesPagerAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int topPadding = ViewUtil.getStatusBarHeight(this);
            mNavigationContainer.setPadding(0, topPadding, 0, 0);
        }
        mPagerRules.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //no-op
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
}

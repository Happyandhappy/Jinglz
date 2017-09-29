package com.jinglz.app.ui.feed;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.App;
import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.base.animations.AnimateCounter;
import com.jinglz.app.ui.feed.adapters.AvailableVideoListener;
import com.jinglz.app.ui.feed.adapters.FeedVideoAdapter;
import com.jinglz.app.ui.feed.adapters.VideoTutorial;
import com.jinglz.app.ui.feed.adapters.WatchedVideoListener;
import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;
import com.jinglz.app.ui.feed.models.TutorialModel;
import com.jinglz.app.ui.feed.models.TutorialSectionModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;
import com.jinglz.app.ui.feed.models.WatchedVideoItemModel;
import com.jinglz.app.ui.feed.widgets.CoinHistoryLayout;
import com.jinglz.app.ui.feed.widgets.TutorialLayout;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.redeem.RedeemFragment;
import com.jinglz.app.ui.showvideo.ShowVideoActivity;
import com.jinglz.app.ui.videorules.VideoRulesActivity;
import com.jinglz.app.ui.winnings.WinningsActivity;
import com.jinglz.app.utils.BrowserUtils;
import com.jinglz.app.utils.NumberUtils;
import com.jinglz.app.utils.ViewUtil;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import org.zakariya.stickyheaders.SectioningAdapter;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedFragment extends BaseFragment implements FeedView, SwipeRefreshLayout.OnRefreshListener, AvailableVideoListener, WatchedVideoListener, NavigationFragment {

    private static final String TAG = "FeedFragment";

    @InjectPresenter FeedPresenter mPresenter;
    @Inject ImageLoader mImageLoader;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.balance_layout) LinearLayout mBalanceLayout;
    @BindView(R.id.balance_details_layout) FrameLayout mBalanceDetailsLayout;
    @BindView(R.id.feed_container) LinearLayout mFeedContainer;
    @BindView(R.id.image_arrow) ImageView mImageArrow;
    @BindView(R.id.text_total_balance) TextView mTextTotalBalance;
    @BindView(R.id.coin_history_container) LinearLayout mCoinHistoryContainer;
    @BindView(R.id.text_my_balance) TextView mTextMyBalance;
    @BindView(R.id.button_redeem_coins) Button mButtonRedeemCoins;
    @BindView(R.id.list_videos) RecyclerView mListVideos;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.layout_no_internet) FrameLayout mLayoutNoInternet;
    @BindView(R.id.tutorials_layout) TutorialLayout mTutorialsLayout;
    @BindView(R.id.root_container) CoordinatorLayout mRootContainer;
    @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;

    private FeedVideoAdapter mVideosAdapter;
    public static  MediaPlayer player;

    /**
     * Returns new Instance of FeedFragment. parameters can be passed via bundle.
     *
     * @return FeedFragment instance
     */
    public static FeedFragment newInstance() {

        final Bundle args = new Bundle();

        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_feed, container, false);
        bind(root);
        App.get().getComponent().inject(this);
        initViews();
        return root;
    }

    @OnClick(R.id.balance_layout)
    public void onClickBalanceLayout() {
        final Transition expandAnimation = new ChangeBounds();
        expandAnimation.setDuration(200);
        TransitionManager.beginDelayedTransition(mFeedContainer, new TransitionSet().addTransition(expandAnimation).addTransition(new Rotate()));
        final ViewGroup.LayoutParams params = mBalanceDetailsLayout.getLayoutParams();
        final boolean collapsed = params.height == 0;
        params.height = collapsed ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        mBalanceDetailsLayout.setLayoutParams(params);
        mImageArrow.setRotation(collapsed ? -90 : 90);
    }

    @Override
    public void showTutorialSection(TutorialSectionModel section) {
        if (showSection(section)) {
            final Iterator<TutorialModel> tutorials = section.tutorials().iterator();
            if (tutorials.hasNext()) {
                showTutorial(tutorials.next(), section.type(), !tutorials.hasNext());
            }
            mTutorialsLayout.setTutorialListener(new TutorialLayout.TutorialListener() {
                @Override
                public void next() {
                    if (tutorials.hasNext()) {
                        showTutorial(tutorials.next(), section.type(), !tutorials.hasNext());
                    } else {
                        mPresenter.nextTutorialSection(section.type(), true);
                    }
                }
                @Override
                public void completeAll() {
                    mPresenter.completeAllTutorials();
                }
            });
        } else {
            mPresenter.nextTutorialSection(section.type(), false);
        }
    }

    @Override
    public void closeTutorial() {
        mTutorialsLayout.closeTutorial();
    }
    /**
     * This method is used to scroll recycler view to the position according to
     * the specified {@code section}
     *
     * @param section TutorialSectionModel type to select position to scroll
     * @return Returns true if {@code scrollPosition} not null and list is scrolled to
     * given position, false otherwise.
     */
    private boolean showSection(TutorialSectionModel section) {
        final int scrollPosition = mVideosAdapter.getSectionPosition(section.type());
        if (scrollPosition != -1) {
            mListVideos.getLayoutManager().scrollToPosition(scrollPosition);
            return true;
        }
        return false;
    }

    /**
     *
     * @param tutorial
     * @param sectionType
     * @param isLast
     */
    @SuppressWarnings("WrongConstant")
    private void showTutorial(TutorialModel tutorial, @VideoSectionModel.SectionType int sectionType, boolean isLast) {
        mListVideos.postDelayed(() -> {
            int viewPosition = 0;
            SectioningAdapter.ViewHolder holder;
            do {
                viewPosition++;
                holder = (SectioningAdapter.ViewHolder) mListVideos
                        .getChildViewHolder(mListVideos.getChildAt(viewPosition));
            } while (holder.getItemViewUserType() != sectionType);

            final View view;
            switch (tutorial.type()) {
                case TutorialModel.TUTORIAL_TYPE_BALANCE:
                    view = mBalanceLayout;
                    break;
                default:
                    view = holder instanceof VideoTutorial ?
                            ((VideoTutorial) holder).getTutorialView(tutorial.type()) : null;
            }
            if (view != null) {
                TransitionManager.beginDelayedTransition(mRootContainer);
                mTutorialsLayout.showTutorial(view, tutorial.text(), isLast);
            }
        }, 100);
    }

    @Override
    public void animateTotalBalance(double coins) {
       /* player = MediaPlayer.create(getActivity(), R.raw.coin_test);
        player.setLooping(true); // Set looping
        player.setVolume(0,0);
        player.start();*/

        final int currentBalance = NumberUtils.fromPrettyNumber(mTextTotalBalance.getText().toString());
        if (currentBalance == (int) coins) {
            return;
        }

        final AnimateCounter animateCounter = new AnimateCounter.Builder(mTextTotalBalance)
                .setCount(currentBalance, (int) coins)
                .setDuration(5000)
                .setInterpolator(new DecelerateInterpolator(1.5f))
                .build();
      /*  animateCounter.setAnimateCounterListener(() ->
                player.stop());*/

        animateCounter.execute();
    }

    @Override
    public void setTotalBalance(double coins) {
        mTextTotalBalance.setText(NumberUtils.toPrettyNumber(coins));
       // animateTotalBalance(coins);

    }

    @Override
    public void setTodayCoins(double coins) {
        final CoinHistoryLayout layout = new CoinHistoryLayout(getContext());
        layout.setValue(coins);
        layout.setWhen(getString(R.string.text_today));

        mCoinHistoryContainer.addView(layout, 0);

        mCoinHistoryContainer.post(() -> {
            final int paddingStart = (mBalanceDetailsLayout.getWidth() - layout.getWidth()) / 2;
            final int paddingEnd = (mBalanceDetailsLayout.getWidth() - mButtonRedeemCoins.getWidth()) / 2;
            mBalanceDetailsLayout.setPadding(paddingStart, 0, paddingEnd, 0);
            final int padding = (int) getResources().getDimension(R.dimen.large);
            layout.setPadding(0, 0, padding, 0);
        });
    }

    @Override
    public void setWeekCoins(double coins) {
        final CoinHistoryLayout layout = new CoinHistoryLayout(getContext());
        layout.setValue(coins);
        layout.setWhen(getString(R.string.text_week));
        final int paddingEnd = (int) getResources().getDimension(R.dimen.large);
        layout.setPadding(0, 0, paddingEnd, 0);
        mCoinHistoryContainer.addView(layout, 1);
    }

    @Override
    public void setMonthCoins(double coins) {
        final CoinHistoryLayout layout = new CoinHistoryLayout(getContext());
        layout.setValue(coins);
        layout.setWhen(getString(R.string.text_month));
        final int paddingEnd = (int) getResources().getDimension(R.dimen.large);
        layout.setPadding(0, 0, paddingEnd, 0);
        mCoinHistoryContainer.addView(layout, 2);
    }

    @Override
    public void clearBalance() {
        if (mCoinHistoryContainer.getChildCount() > 1) {
            mCoinHistoryContainer.removeViews(0, mCoinHistoryContainer.getChildCount() - 1);
        }
    }

    @Override
    public void setVideos(List<VideoSectionModel> sections) {
        mVideosAdapter.setItems(sections);
        mListVideos.post(() -> mPresenter.onFeedLoaded());
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mRefreshLayout.setRefreshing(refreshing);
    }

    @OnClick(R.id.button_redeem_coins)
    public void onClickRedeem() {
        ((MainActivity) getBaseActivity()).showFragment(RedeemFragment.newInstance());
    }

    @Override
    public void onRefresh() {
        FeedFragment.newInstance();
        mRefreshLayout.setRefreshing(false);
       // mPresenter.loadFeed(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showNoInternetError(boolean show) {
        if (show) {
            mVideosAdapter.clear();
        }
        mLayoutNoInternet.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickLearnMore(AvailableVideoItemModel item) {
        BrowserUtils.openLink(getContext(), item.getInfoLink());
    }

    @Override
    public void onClickVideo(AvailableVideoItemModel item) {
        mPresenter.onClickVideo(item);
    }

    @Override
    public void onTimerFinished(AvailableVideoItemModel item) {
        mPresenter.onTimerFinished(item);
    }

    @Override
    public void showVideoRules(AvailableVideoItemModel item) {
        startActivity(VideoRulesActivity.getIntentPreVideo(getContext(), item));
    }

    @Override
    public void openVideoWithRules(AvailableVideoItemModel item) {
        startActivity(ShowVideoActivity.getIntentWithRules(getContext(), item.getId(), item.getContestId()));
    }

    @Override
    public void openVideoWithoutRules(AvailableVideoItemModel item) {
        startActivity(ShowVideoActivity.getIntentWithoutRules(getContext(), item.getId(), item.getContestId()));
    }

    @Override
    public void onClickWinnings(WatchedVideoItemModel item) {
        startActivity(WinningsActivity.getIntent(getContext(), item.getId(), item.getContestId()));
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_home;
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int topPadding = ViewUtil.getStatusBarHeight(getContext());
            mAppBarLayout.setPadding(0, topPadding, 0, 0);
        }
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, null);
        mListVideos.setLayoutManager(new StickyHeaderLayoutManager());
        mVideosAdapter = new FeedVideoAdapter(mImageLoader, this, this);
        mListVideos.setHasFixedSize(true);
        mListVideos.setAdapter(mVideosAdapter);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.accent);
    }
}

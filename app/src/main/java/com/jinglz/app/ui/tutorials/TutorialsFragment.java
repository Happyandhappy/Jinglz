package com.jinglz.app.ui.tutorials;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.feed.FeedFragment;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.videorules.VideoRulesActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class TutorialsFragment extends BaseFragment implements NavigationFragment, TutorialsView {

    @InjectPresenter TutorialsPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    /**
     *
     * @return instance of TutorialsFragment
     */
    public static TutorialsFragment newInstance() {
        final Bundle args = new Bundle();
        final TutorialsFragment fragment = new TutorialsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tutorials, container, false);
        bind(root);
        initToolbar();
        return root;
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_tutorials;
    }

    /**
     * It will redirect user to {@link com.jinglz.app.data.local.event.FeedTutorialsEvent} to show feed tutorials.
     */
    @Override
    public void showFeedTutorials() {
        ((MainActivity) getActivity()).popTo(FeedFragment.class);
    }

    /**
     * Redirect user to {@link VideoRulesActivity} to show video play rules
     */
    @Override
    public void showVideoRules() {
        startActivity(VideoRulesActivity.getIntentJustRules(getContext()));
    }

    /**
     *
     * @param view Contains integer id of a view which is clicked by user
     */
    @OnClick({R.id.text_feed_tutorials, R.id.text_video_rules})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_feed_tutorials:
                mPresenter.onFeedTutorialsClick();
                break;
            case R.id.text_video_rules:
                mPresenter.onVideoRulesClick();
                break;
        }
    }

    /**
     * Initialize toolbar
     */
    private void initToolbar() {
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, R.string.menu_tutorials);
    }
}

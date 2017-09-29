package com.jinglz.app.ui.videohistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.CommonPagerAdapter;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.videohistory.tabs.month.MonthGameHistoryTabFragment;
import com.jinglz.app.ui.videohistory.tabs.today.TodayGameHistoryTabFragment;
import com.jinglz.app.ui.videohistory.tabs.week.WeekGameHistoryTabFragment;

import butterknife.BindView;

public class GameHistoryFragment extends BaseFragment implements NavigationFragment {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;

    public static GameHistoryFragment newInstance() {
        return new GameHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_game_history, container, false);
        bind(root);
        initToolbar();
        initViews();
        return root;
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_game_history;
    }

    /***
     * Initialize views and {@link CommonPagerAdapter}
     */
    private void initViews() {
        final CommonPagerAdapter adapter = new CommonPagerAdapter(getContext(), getChildFragmentManager());
        adapter.add(TodayGameHistoryTabFragment.newInstance(), R.string.text_today);
        adapter.add(WeekGameHistoryTabFragment.newInstance(), R.string.text_this_week);
        adapter.add(MonthGameHistoryTabFragment.newInstance(), R.string.text_this_month);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(adapter.getCount() - 1);
    }

    /**
     * Set Tool bar
     */
    private void initToolbar() {
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, R.string.menu_game_history);
    }
}

package com.jinglz.app.ui.videorules;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *
 */
public class VideoRulesPagerAdapter extends FragmentStatePagerAdapter {

    public static final int PAGES_COUNT = 3;

    /**
     *Constructor for VideoRulesPagerAdapter
     */
    public VideoRulesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     *
     * @param position Integer value contains index of page
     * @return an newInstance of {@link VideoRuleFragment} at position index.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return VideoRuleFragment.newInstance(VideoRuleFragment.PAGE_EYE);
            case 1:
                return VideoRuleFragment.newInstance(VideoRuleFragment.PAGE_VOLUME);
            case 2:
                return VideoRuleFragment.newInstance(VideoRuleFragment.PAGE_VIEW);
            default:
                return null;
        }
    }

    /**
     * @return an Integer value that contains number of pages in adapter
     */
    @Override
    public int getCount() {
        return PAGES_COUNT;
    }
}

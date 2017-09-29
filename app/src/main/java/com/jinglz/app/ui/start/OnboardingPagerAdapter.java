package com.jinglz.app.ui.start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jinglz.app.ui.start.signin.SignInFragment;

public class OnboardingPagerAdapter extends FragmentStatePagerAdapter {

    public static final int PAGES_COUNT = 4;

    public OnboardingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     *
     * @param position int contains page index
     * @return a newInstance of {@link OnboardingFragment}
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OnboardingFragment.newInstance(OnboardingFragment.PAGE_WATCH);
            case 1:
                return OnboardingFragment.newInstance(OnboardingFragment.PAGE_PLAY);
            case 2:
                return OnboardingFragment.newInstance(OnboardingFragment.PAGE_WIN);
            case 3:
                return SignInFragment.newInstance();
            default:
                return null;
        }
    }

    /**
     *
     * @return number of pages to display
     */
    @Override
    public int getCount() {
        return PAGES_COUNT;
    }
}

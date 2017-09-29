package com.jinglz.app.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommonPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragments;
    private final List<String> mTitles;
    private final Context mContext;

    /**
     * Construct new CommonPagerAdapter with specified context and fragmentManager.
     *
     * @param context for accessing application-specific data
     * @param fragmentManager to work with FragmentManager
     */
    public CommonPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context.getApplicationContext();
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    /**
     * method with specified fragment and title to add new fragment and set its corresponding title.
     *
     * @param fragment Fragment instance to be added
     * @param title String variable containing title.
     */
    public void add(@NonNull Fragment fragment, String title) {
        mFragments.add(fragment);
        mTitles.add(title);
        notifyDataSetChanged();
    }

    /**
     * method with specified fragment and title to add new fragment and set its corresponding title.
     *
     * @param fragment Fragment instance to be added
     * @param title integer variable containing title.
     */
    public void add(@NonNull Fragment fragment, @StringRes int title) {
        mFragments.add(fragment);
        mTitles.add(mContext.getString(title));
        notifyDataSetChanged();
    }

    /**
     * Returns fragment at specified position.
     * returns fragment if {@code position} is less than {@see mFragments} size, null otherwise
     *
     * @param position contains number of fragment which should be displayed.
     * @return Fragment object
     */
    @Override
    @Nullable
    public Fragment getItem(int position) {
        return position < mFragments.size() ? mFragments.get(position) : null;
    }

    /**
     * This method is used to return title at specified position
     *
     * @param position contains position to fetch title
     * @return CharSequence
     */
    @Override
    @Nullable
    public CharSequence getPageTitle(int position) {
        return position < mTitles.size() ? mTitles.get(position) : null;
    }

    /**
     * used to retrieve total size of {@see mFragments}
     * @return size
     */
    @Override
    public int getCount() {
        return mFragments.size();
    }
}

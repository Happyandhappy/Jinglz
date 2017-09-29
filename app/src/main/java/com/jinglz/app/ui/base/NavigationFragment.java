package com.jinglz.app.ui.base;

import android.support.annotation.IdRes;

/**
 * interface to handle functions related to drawer fragments
 */
public interface NavigationFragment {

    /**
     * Used to return id of the drawer
     *
     * @return id of the drawer
     */
    @IdRes
    int getDrawerItemId();

}

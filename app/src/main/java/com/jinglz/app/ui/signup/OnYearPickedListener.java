package com.jinglz.app.ui.signup;

/**
 * interface to perform functionality on date picked. class implementing
 * this interface needs to override onPicked method to work on {@code year}s
 */
public interface OnYearPickedListener {

    void onPicked(int year);

}

package com.jinglz.app.ui.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

public class CustomTextInputLayout extends TextInputLayout {

    /**
     * Constructs new CustomTextInputLayout with specified context. this is used to work with
     * custom TextInputLayout.
     *
     * @param context for accessing application-specific data
     */
    public CustomTextInputLayout(Context context) {
        super(context);
    }

    /**
     * Constructs new CustomTextInputLayout with specified context and attrs.
     *
     * @param context for accessing application-specific data
     * @param attrs collection of attributes to be used
     */
    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs new CustomTextInputLayout with specified context,attrs and defStyleAttr.
     *
     * @param context for accessing application-specific data
     * @param attrs collection of attributes to be used
     * @param defStyleAttr style parameter
     */
    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * used to set error if there is an error in CharSequence.
     * @param error
     */
    @Override
    public void setError(@Nullable CharSequence error) {
        setErrorEnabled(error != null);
        super.setError(error);
    }
}

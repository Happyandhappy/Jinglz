package com.jinglz.app.ui.showvideo.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;

import com.jinglz.app.R;

/**
 * Class extending {@link DialogFragment} to set dialog for
 * video layout parameters will be set as MATCH_PARENT on start.
 */
public abstract class BaseVideoDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.TranslucentStatusTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}

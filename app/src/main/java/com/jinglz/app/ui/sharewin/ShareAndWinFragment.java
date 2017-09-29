package com.jinglz.app.ui.sharewin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.utils.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareAndWinFragment extends BaseFragment implements ShareAndWinView, NavigationFragment {

    @InjectPresenter ShareAndWinPresenter mPresenter;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.button_share) ImageButton mButtonShare;
    @BindView(R.id.text_code) TextView mTextCode;
    @BindView(R.id.image_toolbar) ImageView mImageToolbar;
    @BindView(R.id.text_toolbar_title) TextView mTextToolbarTitle;

    /**
     * Constructs new instance of ShareAndWinFragment.
     */
    public static ShareAndWinFragment newInstance() {
        return new ShareAndWinFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_share_and_win, container, false);
        bind(root);
        initToolbar();
        return root;
    }

    /**
     * method with specified code to set code in {@see mTextCode}
     * @param code String value to set.
     */
    @Override
    public void setReferralCode(String code) {
        mTextCode.setText(code);
    }

    /**
     * Method with specified message. This is used to send
     * share request via share intent service. {@code message} can
     * be sent as its parameters containing invite message.
     *
     * @param message String variable contains invite message
     */
    @Override
    public void startShareIntent(String message) {
        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.text_share_via)));
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_share;
    }

    @OnClick(R.id.button_share)
    void onShareClick() {
        mPresenter.shareClick();
    }

    /**
     * Initialize tool bar.
     */
    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int topPadding = ViewUtil.getStatusBarHeight(getContext());
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbar.getLayoutParams();
            params.topMargin = topPadding;
        }
        getBaseActivity().setSupportActionBar(mToolbar);
        final ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        getBaseActivity().setTitle(null);
    }
}
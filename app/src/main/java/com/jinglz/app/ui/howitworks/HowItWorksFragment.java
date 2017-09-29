package com.jinglz.app.ui.howitworks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.legalnotice.LegalNoticeActivity;
import com.jinglz.app.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HowItWorksFragment extends BaseFragment implements HowItWorksView, NavigationFragment {

    @InjectPresenter HowItWorksPresenter mPresenter;

    @BindView(R.id.text_how_it_works) TextView mTextHowItWorks;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.shadow) View mShadow;

    /**
     * Returns new instance of HowItWorksFragment.
     * @return HowItWorksFragment instance
     */
    public static HowItWorksFragment newInstance() {
        return new HowItWorksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_how_it_works, container, false);
        bind(root);
        initToolbar();
        return root;
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_how_it_works;
    }

    /**
     * Overridden method from HowItWorksView to set the specified text in to
     * {@see mTextHowItWorks}
     * @param text
     */
    @Override
    public void setText(CharSequence text) {
        mTextHowItWorks.setText(text);
    }

    @Override
    public void openLegalNotice() {
        startActivity(LegalNoticeActivity.getIntent(getContext()));
    }

    @OnClick(R.id.button_legal_notice)
    public void onClick() {
        mPresenter.onLegalNoticeClick();
    }

    /**
     * This method is used to set the navigation toolbar title.
     */
    private void initToolbar() {
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, R.string.menu_how_it_works);
    }
}

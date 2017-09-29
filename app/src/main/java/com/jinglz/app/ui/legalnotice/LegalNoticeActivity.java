package com.jinglz.app.ui.legalnotice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;

import butterknife.BindView;

public class LegalNoticeActivity extends BaseActivity implements LegalNoticeView {

    @InjectPresenter LegalNoticePresenter mPresenter;
    @BindView(R.id.web_view) WebView mWebView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static Intent getIntent(Context context) {
        return new Intent(context, LegalNoticeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        bind();
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(R.string.action_legal_notice);
    }

    /**
     * Overridden method to load data in {@see mWebView}
     * @param text String value contains url to load in WebView.
     */
    @Override
    public void setText(String text) {
        mWebView.loadData(text, "text/html", "UTF-8");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

package com.jinglz.app.ui.winnings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jinglz.app.App;
import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.showvideo.ShowVideoActivity;
import com.jinglz.app.ui.winnings.adapter.WinningsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class WinningsActivity extends BaseActivity implements WinningsView {

    private static final String EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID";
    private static final String EXTRA_CONTEST_ID = "EXTRA_CONTEST_ID";

    @InjectPresenter WinningsPresenter mPresenter;

    @BindView(R.id.list_users) RecyclerView mListUsers;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    @Inject ImageLoader mImageLoader;

    private WinningsAdapter mAdapter;

    /**
     * Method to send value with intent
     * @param context Context contains context of class
     * @param videoId Sting contains url of video which will play
     * @param contestId String contains contest id of video id
     * @return Intent with cotext, video id and contest id
     */
    public static Intent getIntent(Context context, String videoId, String contestId) {
        final Intent intent = new Intent(context, WinningsActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_CONTEST_ID, contestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winnings);
        App.get().getSessionComponent().inject(this);
        bind();
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(R.string.text_drawing_results);

        mAdapter = new WinningsAdapter(mImageLoader, v -> {
            mPresenter.onFooterClick();
        });
        mListUsers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mListUsers.setAdapter(mAdapter);
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

    @ProvidePresenter
    WinningsPresenter providePresenter() {
        final Bundle extras = getIntent().getExtras();
        return new WinningsPresenter(extras.getString(EXTRA_VIDEO_ID), extras.getString(EXTRA_CONTEST_ID));
    }

    @Override
    public void setRanking(List<ContestLeadershipRecord> records) {
        mAdapter.update(records);
    }

    /**
     * Hide mProgressBar Progress bar
     */
    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * This method for Playing video in next activity {@link ShowVideoActivity}
     * @param videoId String contains video id which will play
     * @param contestId String contains Contest id sepecified with video id
     */
    @Override
    public void openVideo(String videoId, String contestId) {
        startActivity(ShowVideoActivity.getIntentWithoutRules(this, videoId, contestId));
    }
}

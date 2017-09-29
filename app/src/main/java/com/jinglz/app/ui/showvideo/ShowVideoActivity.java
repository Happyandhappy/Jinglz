package com.jinglz.app.ui.showvideo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.showvideo.dialogs.ContestSuccessDialog;
import com.jinglz.app.ui.showvideo.dialogs.EyeContactDialog;
import com.jinglz.app.ui.showvideo.dialogs.LowVolumeDialog;
import com.jinglz.app.ui.showvideo.models.ShowVideoModel;
import com.jinglz.app.utils.DateUtils;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ShowVideoActivity extends BaseActivity implements ShowVideoView {

    private static final String EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID";
    private static final String EXTRA_CONTEST_ID = "EXTRA_CONTEST_ID";
    private static final String EXTRA_WITH_RULES = "EXTRA_WITH_RULES";

    @BindView(R.id.root_container) FrameLayout mRootContainer;
    @BindView(R.id.text_timer) TextView mTextTimer;
    @BindView(R.id.text_low_volume) TextView mTextLowVolume;
    @BindView(R.id.button_back) ImageButton mButtonBack;
    @BindView(R.id.text_eye_contact) TextView mTextEyeContact;
    @BindView(R.id.onOffBgToggle) ToggleButton onOffBgToggle;
    @InjectPresenter ShowVideoPresenter mPresenter;

    private JWPlayerView mPlayerView;
    private LowVolumeDialog mLowVolumeDialog;
    private EyeContactDialog mEyeContactDialog;

    /**
     * call this method to start {@link ShowVideoActivity} with specified parameters passing via
     * intents. {@code EXTRA_WITH_RULES} is passe true for starting intent with rules.
     * @param context for handling application specific data.
     * @param videoId String contains video id to pass via intent
     * @param contestId String contains contest id
     * @return ShowVideoActivity instance
     */
    public static Intent getIntentWithRules(Context context, String videoId, String contestId) {
        final Intent intent = new Intent(context, ShowVideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_CONTEST_ID, contestId);
        intent.putExtra(EXTRA_WITH_RULES, true);
        return intent;
    }

    /**
     * call this method to start {@link ShowVideoActivity} with specified parameters passing via
     * intents. {@code EXTRA_WITH_RULES} is passe false for starting intent without rules.
     *
     * @param context for handling application specific data.
     * @param videoId String contains video id to pass via intent
     * @param contestId String contains contest id
     * @return ShowVideoActivity instance
     */
    public static Intent getIntentWithoutRules(Context context, String videoId, String contestId) {
        final Intent intent = new Intent(context, ShowVideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_CONTEST_ID, contestId);
        intent.putExtra(EXTRA_WITH_RULES, false);
        return intent;
    }

    /**
     * Returns new instance of ShowVideoPresenter.
     *
     * @return ShowVideoPresenter instance
     */
    @ProvidePresenter
    ShowVideoPresenter providePresenter() {
        final Intent intent = getIntent();
        return new ShowVideoPresenter(intent.getStringExtra(EXTRA_VIDEO_ID),
                                      intent.getStringExtra(EXTRA_CONTEST_ID),
                                      intent.getBooleanExtra(EXTRA_WITH_RULES, false));
    }


    /**
     * On configuration change it will ask for camera permissions if not assigned.
     *
     * @param newConfig {@link Configuration} contains configuration details
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPresenter.onCameraReady(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);


        bind();
        initView();
        checkPermissions();
    }

    @OnCheckedChanged(R.id.onOffBgToggle)
    void onOffBackground(){
        boolean isCheckedState = onOffBgToggle.isChecked();
        if (isCheckedState){
            mPlayerView.setBackgroundColor(getResources().getColor(R.color.white));
            mButtonBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_black));
        }else{

            mPlayerView.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            mButtonBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
       }
    }


    @OnClick(R.id.button_back)
    public void onClickBack() {
        finish();
    }

    /**
     * Overridden method with specified video to play given url.
     * video url will be retrieved from {@code video} and added to {@link PlaylistItem}.
     * it will be then loaded by using mPlayerView.
     *
     * @param video {@link ShowVideoModel} object to retrieve video path.
     */
    @Override
    public void showVideo(ShowVideoModel video) {
        if(video.videoUrl() != null) {
            final PlaylistItem playlist = new PlaylistItem(video.videoUrl());
            playlist.setImage(video.imageUrl());

            mPlayerView.load(playlist);
            mPlayerView.play();
        }
        mPlayerView.addOnTimeListener((current, duration) -> {
            mTextTimer.setText(DateUtils.toTimerFormat(duration - current));
            if (current == 0) {
                mPresenter.onVideoReady();
            }
        });
    }

    /**
     * call this method to play video.
     * @param play a boolean value to play/stop video
     */
    @Override
    public void playVideo(boolean play) {
        mPlayerView.play(play);
    }

    /**
     * call this method to show dialog if volume is low before video play. it will create new instance of
     * {@link LowVolumeDialog} if {@code show} is true, dismiss otherwise. show will be true when volume is low
     * while playing video.
     *
     * @param show show alert if true, dismiss otherwise.
     */
    @Override
    public void showFirstLowVolumeAlert(boolean show) {
        if (show) {
            if (mLowVolumeDialog == null) {
                mLowVolumeDialog = LowVolumeDialog.newInstance();
            }
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mLowVolumeDialog.show(transaction, LowVolumeDialog.class.getName());
        } else if (mLowVolumeDialog != null) {
            mLowVolumeDialog.dismiss();
        }
    }
    /**
     * call this method to show dialog if volume is low after video play. visibility of {@see mTextEyeContact}
     * will be hide if {@code show} is true. visibility of {@code mTextLowVolume} will be visible if {@code show}
     * is true, gone otherwise. show will be true when volume is low while playing video.
     *
     * @param show visibility of {@see mTextEyeContact} will be hide if true
     */
    @Override
    public void showLowVolumeAlert(boolean show) {
        if (mLowVolumeDialog != null && mLowVolumeDialog.isVisible() && !show) {
            mLowVolumeDialog.dismiss();
        }
        if (show) {
            mTextEyeContact.setVisibility(View.GONE);
        }
        mTextLowVolume.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * call this method to show alert for eye contact before playing video. it will create new instance of
     * {@link EyeContactDialog} if {@code show} is true, dismiss dialog otherwise.
     *
     * @param show boolean variable if true, display dialog, dismiss otherwise.
     */
    @Override
    public void showFirstEyeContactAlert(boolean show) {
        if (show) {
            if (mEyeContactDialog == null) {
                mEyeContactDialog = EyeContactDialog.newInstance();
            }
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mEyeContactDialog.show(transaction, EyeContactDialog.class.getName());
        } else if (mEyeContactDialog != null) {
            mEyeContactDialog.dismiss();
        }
    }

    /**
     * call this method to show alert for eye contact after playing video. visibility of {@see mTextLowVolume}
     * will be hide if {@code show} is true.  visibility of {@code mTextEyeContact} will be visible if {@code show}
     * is true, gone otherwise. show will be true if there is no eye contact while playing video.
     *
     * @param show visibility of {@see mTextEyeContact} will be hide if true
     */
    @Override
    public void showEyeContactAlert(boolean show) {
        if (mEyeContactDialog != null && mEyeContactDialog.isVisible() && !show) {
            mEyeContactDialog.dismiss();
        }
        if (show) {
            mTextLowVolume.setVisibility(View.GONE);
        }
        mTextEyeContact.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Prompt user to allow permission for using camera of phone
     */
    @Override
    public void showPermissionError() {
        Snackbar.make(mRootContainer, R.string.text_camera_permission_error, Snackbar.LENGTH_SHORT)
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, @DismissEvent int event) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void close() {
        finish();
    }

    /**
     * Overridden method to show {@link ContestSuccessDialog} on success.
     * @param video {@link ShowVideoModel} object .
     */
    @Override
    public void contestSuccess(ShowVideoModel video) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ContestSuccessDialog.newInstance(video)
                .show(transaction, ContestSuccessDialog.class.getName());
    }

    /**
     * Initialize views and {@link JWPlayerSupportFragment}
     */
    private void initView() {
        final JWPlayerSupportFragment fragment  = (JWPlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment);
        if (fragment!=null && mPlayerView!=null){
            fragment.setFullscreenOnDeviceRotate(false);
            mPlayerView = fragment.getPlayer();


        mPlayerView.addTouchables(new ArrayList<>(Collections.singletonList(mButtonBack)));
        mPlayerView.setControls(false);

        mPlayerView.addOnCompleteListener(() -> mPresenter.onVideoCompeted());
        mPlayerView.addOnBufferListener(mPresenter::onBufferChange);

        onOffBgToggle.setVisibility(View.GONE);
        }
    }

    /**
     * Check if application have permission to use camera.
     */
    private void checkPermissions() {
        getRxPermissions().request(Manifest.permission.CAMERA).subscribe(status -> mPresenter.onCameraReady(status), Throwable::printStackTrace);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

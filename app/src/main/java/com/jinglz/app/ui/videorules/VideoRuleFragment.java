package com.jinglz.app.ui.videorules;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hwangjr.rxbus.RxBus;
import com.jinglz.app.R;
import com.jinglz.app.data.local.event.VideoRulesGotItEvent;
import com.jinglz.app.ui.base.BaseFragment;

import java.lang.annotation.Retention;

import butterknife.BindView;
import butterknife.OnClick;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@FragmentWithArgs
public class VideoRuleFragment extends BaseFragment {

    @BindView(R.id.container) LinearLayout mContainer;
    @BindView(R.id.image_icon) ImageView mImageIcon;
    @BindView(R.id.text_description) TextView mTextDescription;
    @BindView(R.id.text_title) TextView mTextTitle;
    @BindView(R.id.button_got_it) Button mButtonGotIt;

    @Retention(SOURCE)
    @IntDef({PAGE_EYE, PAGE_VOLUME, PAGE_VIEW})
    public @interface Page {

    }

    public static final int PAGE_EYE = 0;
    public static final int PAGE_VOLUME = 1;
    public static final int PAGE_VIEW = 2;

    @Arg
    @Page
    int mPage;

    /**
     * @param page which page index will be selected
     * @return a newInstance of VideoRuleFragment(int)
     */
    public static VideoRuleFragment newInstance(@Page int page) {
        return VideoRuleFragmentBuilder.newVideoRuleFragment(page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video_rule, container, false);
        bind(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPageInfo();
    }

    @OnClick(R.id.button_got_it)
    public void onClick() {
        RxBus.get().post(new VideoRulesGotItEvent());
    }

    private void setPageInfo() {
        switch (mPage) {
            case PAGE_EYE:
                setEyePageInfo();
                break;
            case PAGE_VIEW:
                setViewPageInfo();
                break;
            case PAGE_VOLUME:
                setVolumePageInfo();
                break;
        }
    }

    /**
     * It will show Eye Rules of current item of VideoRulesFragment
     */
    private void setEyePageInfo() {
        mImageIcon.setImageResource(R.drawable.ic_eye_contact);
        mTextTitle.setText(getString(R.string.text_video_rule_eye_contact));
        mTextDescription.setText(getString(R.string.text_video_rule_eye_contact_description));
    }

    /**
     * It will show Volume Rules of current item of VideoRulesFragment
     */
    private void setVolumePageInfo() {
        mImageIcon.setImageResource(R.drawable.ic_volume);
        mTextTitle.setText(getString(R.string.text_video_rule_volume));
        mTextDescription.setText(getString(R.string.text_video_rule_volume_description));
    }

    /**
     * It will show watch the entire video Rule of current item of VideoRulesFragment
     */
    private void setViewPageInfo() {
        mImageIcon.setImageResource(R.drawable.ic_alarm);
        mTextTitle.setText(getString(R.string.text_video_rule_view));
        mTextDescription.setText(getString(R.string.text_video_rule_view_description));
        mButtonGotIt.setVisibility(View.VISIBLE);
    }
}

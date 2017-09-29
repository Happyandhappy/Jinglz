package com.jinglz.app.ui.showvideo.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hwangjr.rxbus.RxBus;
import com.jinglz.app.R;
import com.jinglz.app.data.local.event.CancelVideoDialogEvent;
import com.jinglz.app.ui.showvideo.models.ShowVideoModel;
import com.jinglz.app.utils.BrowserUtils;
import com.jinglz.app.utils.DateUtils;
import com.jinglz.app.utils.ViewUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@FragmentWithArgs
public class ContestSuccessDialog extends BaseVideoDialog {

    @BindView(R.id.image_icon) ImageView mImageIcon;
    @BindView(R.id.text_title) TextView mTextTitle;
    @BindView(R.id.text_description) TextView mTextDescription;
    @BindView(R.id.button_learn_more) Button mButtonLearnMore;
    @BindView(R.id.button_close) ImageButton mButtonClose;
    @BindView(R.id.container) FrameLayout mContainer;

    @Arg public Date mContestDate;
    @Arg public String mInfoLink;

    public static ContestSuccessDialog newInstance(ShowVideoModel video) {
        return ContestSuccessDialogBuilder.newContestSuccessDialog(video.contestDate(), video.infoLink());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_contest_success, container, false);
        ButterKnife.bind(this, view);
        initView(view.getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonLearnMore.setVisibility(mInfoLink != null ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        RxBus.get().post(new CancelVideoDialogEvent());
        super.onCancel(dialog);
    }

    /**
     * To handle click events of different widgets.
     * @param view To retrieve id of clickable view.
     */

    @OnClick({R.id.button_learn_more, R.id.button_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_learn_more:
                BrowserUtils.openLink(getContext(), mInfoLink);
                break;
            case R.id.button_close:
                dismiss();
                RxBus.get().post(new CancelVideoDialogEvent());
                break;
        }
    }

    /**
     * Initialize views
     * @param context Context to handle application specific data
     */
    private void initView(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mButtonClose.getLayoutParams();
            final int top = ViewUtil.getStatusBarHeight(context) + layoutParams.topMargin;
            layoutParams.setMargins(0, top, 0, 0);
        }
        mTextDescription.setText(getString(R.string.text_contest_success_description,
                                           DateUtils.toTimeFormat(mContestDate),
                                           DateUtils.getTimeZoneDisplayName()));
    }
}

package com.jinglz.app.ui.showvideo.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hwangjr.rxbus.RxBus;
import com.jinglz.app.R;
import com.jinglz.app.data.local.event.CancelVideoDialogEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LowVolumeDialog extends BaseVideoDialog {

    @BindView(R.id.image_icon) ImageView mImageIcon;

    /**
     * Returns new instance of LowVolumeDialog
     * @return LowVolumeDialog instance
     */
    public static LowVolumeDialog newInstance() {
        return new LowVolumeDialog();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_low_volume, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        RxBus.get().post(new CancelVideoDialogEvent());
    }

}

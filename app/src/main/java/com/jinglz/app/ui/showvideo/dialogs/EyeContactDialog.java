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

public class EyeContactDialog extends BaseVideoDialog {

    @BindView(R.id.image_icon) ImageView mImageIcon;


    /**
     * Return new instance of EyeContactDialog.
     *
     * @return EyeContactDialog instance
     */
    public static EyeContactDialog newInstance() {
        return new EyeContactDialog();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_eye_contact, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * overridden method to cancel video dialog event.
     * @param dialog Dialog to cancel
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        RxBus.get().post(new CancelVideoDialogEvent());
    }
}

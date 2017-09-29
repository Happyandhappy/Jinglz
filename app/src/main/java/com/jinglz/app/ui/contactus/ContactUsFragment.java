package com.jinglz.app.ui.contactus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.feed.FeedFragment;
import com.jinglz.app.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ContactUsFragment extends BaseFragment implements ContactUsView, NavigationFragment {

    private static final String TAG = "ContactUsFragment";

    @BindView(R.id.button_send) Button mButtonSend;
    @BindView(R.id.input_text) EditText mInputText;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @InjectPresenter ContactUsPresenter mPresenter;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_contact_us, container, false);
        bind(root);
        initToolbar();
        return root;
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_contact_us;
    }

    @OnClick(R.id.button_send)
    void onSendClick() {
        mPresenter.onContactUsClick(String.valueOf(mInputText.getText()));
    }

    @OnTextChanged(R.id.input_text)
    void onTextChanged(CharSequence sequence) {
        mPresenter.onTextChanged(sequence);
    }

    @Override
    public void enableSendButton(boolean enable) {
        mButtonSend.setEnabled(enable);
    }

    /**
     * It will Show showSuccessDialog()
     * it uses {@link AlertDialog} to display success dialog.
     */
    @Override
    public void onSendSuccess() {
        showSuccessDialog();
        ((MainActivity) getBaseActivity()).popTo(FeedFragment.class);
    }

    /**
     * This method is used to display success dialog.
     * it use {@link AlertDialog} to display success dialog.
     */
    private void showSuccessDialog() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_message_sent, null, false);
        final ImageButton buttonOk = ButterKnife.findById(view, R.id.button_ok);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Dialog)
                .setView(view);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        buttonOk.setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * used to set toolbar.
     */
    private void initToolbar() {
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, R.string.menu_contact_us);
    }
}

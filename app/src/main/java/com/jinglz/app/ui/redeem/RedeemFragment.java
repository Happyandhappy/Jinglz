package com.jinglz.app.ui.redeem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.R;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.ui.base.BaseFragment;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.base.widget.TextInputNumberEditText;
import com.jinglz.app.ui.confirmcode.phone.ConfirmCodePhoneActivity;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.utils.DialogFactory;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class RedeemFragment extends BaseFragment implements NavigationFragment, RedeemView {

    private static final String TAG = "RedeemFragment";

    private static final int REQUEST_CODE_PROFILE_SHARING_LINK = 124;
    private static final int REQUEST_PHONE_VALIDATION = 125;

    @InjectPresenter RedeemPresenter mPresenter;

    @BindView(R.id.text_total_balance) TextView mTextTotalBalance;
    @BindView(R.id.input_amount) TextInputNumberEditText mInputAmount;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.text_redeemable_sum) TextView mTextRedeemableSum;
    @BindView(R.id.container) LinearLayout mContainer;
    @BindView(R.id.text_redeem_ratio) TextView mTextRedeemRatio;

    /**
     * Returns new instance of RedeemFragment(.
     * @return  RedeemFragment instance.
     */
    public static RedeemFragment newInstance() {
        return new RedeemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_redeem, container, false);
        bind(root);
        initToolbar();
        return root;
    }

    @Override
    public int getDrawerItemId() {
        return R.id.menu_redeem;
    }

    @Override
    public void startPayPalService(PayPalConfiguration configuration) {
        final Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        getContext().startService(intent);
    }

    @Override
    public void stopPayPalService() {
        getContext().stopService(new Intent(getContext(), PayPalService.class));
    }


    @OnClick(R.id.button_redeem)
    void onRedeemClick() {
        mPresenter.onRedeemClick(String.valueOf(mInputAmount.getFormattedText()));
    }

    @Override
    public void startLinkPayPalAuthorization(PayPalConfiguration configuration, PayPalOAuthScopes oAuthScopes) {
        final Intent intent = new Intent(getContext(), PayPalProfileSharingActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, oAuthScopes);
        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING_LINK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PROFILE_SHARING_LINK: {
                    final PayPalAuthorization auth = data
                            .getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                    if (auth != null) {
                        final String authorization_code = auth.getAuthorizationCode();
                        Log.d(TAG, "onActivityResult: " + authorization_code);
                        mPresenter.linkPayPalAccount(String.valueOf(mInputAmount.getFormattedText()), authorization_code);
                    }
                    break;
                }
                case REQUEST_PHONE_VALIDATION: {
                    onRedeemClick();
                    break;
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "The user canceled.");
        } else if (resultCode == PayPalProfileSharingActivity.RESULT_EXTRAS_INVALID) {
            Log.i(TAG,
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. "
                            + "Please see the docs.");
        }
    }

    @Override
    public void setBalance(String balance) {
        mTextTotalBalance.setText(balance);
    }

    @Override
    public void openPhoneVerification(String email, String phone) {
        startActivityForResult(ConfirmCodePhoneActivity.getIntent(getContext(), email, phone),
                REQUEST_PHONE_VALIDATION);
    }

    @Override
    public void setRedeemSum(String sum) {
        TransitionManager.beginDelayedTransition(mContainer);
        if (sum == null) {
            mTextRedeemableSum.setVisibility(View.GONE);
            mTextRedeemableSum.setText(null);
        } else {
            mTextRedeemableSum.setVisibility(View.VISIBLE);
            mTextRedeemableSum.setText(getRedeemSumSpan(sum));
        }
    }

    @Override
    public void setRedeemRatio(String ratio) {
        mTextRedeemRatio.setText(getString(R.string.text_transferred_to_pay_pal, ratio));
    }

    @Override
    public void openShareDialog(String email, int coins) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_redeem_success, null, false);
        final Button buttonShare = ButterKnife.findById(view, R.id.button_share);
        initShareButtonSpan(buttonShare);
        final TextView text = ButterKnife.findById(view, R.id.text);
        text.setText(getString(R.string.text_payment_emailed, email));
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Dialog)
                .setView(view);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        buttonShare.setOnClickListener(v -> mPresenter.onShareClick(coins));
    }

    @Override
    public void openShareChooser(String text) {
        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.text_share_via)));
    }

    @Override
    public void cleanCoinSum() {
        mInputAmount.setText(null);
    }

    @Override
    public void showFillNumberError() {
        DialogFactory.createSimpleOkErrorDialog(getContext(), R.string.dialog_error_title, R.string.error_fill_phone)
                .show();
    }

    @OnTextChanged(R.id.input_amount)
    void onAmountChange() {
        mPresenter.setAmount(mInputAmount.getFormattedText());
    }

    private Spannable getRedeemSumSpan(String sum) {
        final String full = getString(R.string.text_redeemable_for, sum);
        final SpannableString result = new SpannableString(full);
        final int idx = full.indexOf("\n");
        final String part = full.substring(idx);
        result.setSpan(new CalligraphyTypefaceSpan(TypefaceUtils.load(getContext().getAssets(),
                FontConfig.bold)), idx, idx + part.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new RelativeSizeSpan(1.25f), idx, idx + part.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    private void initShareButtonSpan(Button share) {
        final String full = getString(R.string.action_share);
        final String icon = "%icon%";
        final int idx = full.indexOf(icon);
        final SpannableString result = new SpannableString(full);
        result.setSpan(new ImageSpan(getContext(), R.drawable.ic_share_white, DynamicDrawableSpan.ALIGN_BOTTOM), idx,
                idx + icon.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        share.setText(result, TextView.BufferType.SPANNABLE);
    }

    private void initToolbar() {
        ((MainActivity) getBaseActivity()).setNavigationToolbar(mToolbar, R.string.menu_redeem);

    }
}

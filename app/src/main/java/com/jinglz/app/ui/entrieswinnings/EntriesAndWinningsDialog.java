package com.jinglz.app.ui.entrieswinnings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jinglz.app.App;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.R;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.ui.base.MvpDialogFragment;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class EntriesAndWinningsDialog extends MvpDialogFragment implements EntriesAndWinningsView {

    private static final String TAG = "EntriesAndWinningsDialo";
    public static final String EXTRAS_RESULTS = "extraResults";

    private Unbinder mUnbinder;
    private Subscription mSubscription;

    @InjectPresenter EntriesAndWinningsPresenter mPresenter;

    @BindView(R.id.text_total_won) TextView mTextTotalWon;
    @BindView(R.id.button_share) Button mButtonShare;
    @BindView(R.id.coins_layout) CoinsLayout mCoinsLayout;
    @BindView(R.id.animation_container) ImageView mAnimationContainer;
    @BindView(R.id.container) RelativeLayout mContainer;
    @BindDrawable(R.drawable.big_coin) Drawable mCoin;

    private AnimationDrawable mAnimationDrawable;

    public static EntriesAndWinningsDialog newInstance(List<ContestResult> results) {
        final Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRAS_RESULTS, (ArrayList<? extends Parcelable>) results);
        final EntriesAndWinningsDialog fragment = new EntriesAndWinningsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_entries_and_winnings, container, false);
        App.get().getSessionComponent().inject(this);
        mUnbinder = ButterKnife.bind(this, root);
        mAnimationDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.anim_stars);
        mAnimationContainer.setImageDrawable(mAnimationDrawable);
        initShareButtonSpan();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(getContext(), R.color.transparent)));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroyView();
    }

    @OnClick(R.id.button_share)
    void onShareClick() {
        mPresenter.onShareClick();
    }

    @OnClick(R.id.button_continue)
    void continueClick() {
        if (!mCoinsLayout.revealNext()) {
            mAnimationContainer.setVisibility(View.VISIBLE);
            mAnimationDrawable.start();
            new Handler().postDelayed(this::dismiss, getAnimationDuration());
        }
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
    public void initCoinsLayout(List<ContestResult> data) {
        initTotalSpan(0);
        mCoinsLayout.setData(data);
        mCoinsLayout.setOnClickListener(view -> mCoinsLayout.revealNext());
        mCoinsLayout.setTotalResultListener(total -> {
            TransitionManager.beginDelayedTransition(mContainer, new ChangeText());
            initTotalSpan(total);
        });
    }

    @ProvidePresenter
    EntriesAndWinningsPresenter providePresenter(){
        final List<ContestResult> results = getArguments().getParcelableArrayList(EXTRAS_RESULTS);
        return new EntriesAndWinningsPresenter(results);
    }

    /**
     * method is used to retrieve animation duration.
     * @return
     */
    private long getAnimationDuration() {
        return mAnimationDrawable.getNumberOfFrames() * mAnimationDrawable.getDuration(0);
    }

    /**
     * This method is used to initialize total span for specified coins and set in {@see mTextTotalWon}
     *
     * @param coins Returns number of coins
     */
    private void initTotalSpan(int coins) {
        final int color = ContextCompat.getColor(getContext(), coins == 0 ? R.color.dialog_coins_text : R.color.white);
        final String full = getString(R.string.text_total_won, coins);
        final String coinsStr = String.valueOf(coins);
        final SpannableString result = new SpannableString(full.toUpperCase());
        final int idx = full.indexOf(coinsStr);
        result.setSpan(new CalligraphyTypefaceSpan(TypefaceUtils.load(getContext().getAssets(), FontConfig.bold)), idx,
                       idx + coinsStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new RelativeSizeSpan(2.8f), idx, idx + coinsStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextTotalWon.setTextColor(color);
        mTextTotalWon.setText(result);
    }

    /**
     * used to set spanning for shared button
     */
    private void initShareButtonSpan() {
        final String full = getString(R.string.action_share);
        final String icon = "%icon%";
        final int idx = full.indexOf(icon);
        final SpannableString result = new SpannableString(full);
        result.setSpan(new ImageSpan(getContext(), R.drawable.ic_share_white, DynamicDrawableSpan.ALIGN_BOTTOM), idx,
                       idx + icon.length(),
                       Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mButtonShare.setText(result, TextView.BufferType.SPANNABLE);
    }
}

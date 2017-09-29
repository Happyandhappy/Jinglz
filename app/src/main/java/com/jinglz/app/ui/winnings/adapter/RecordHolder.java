package com.jinglz.app.ui.winnings.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinglz.app.Constants;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class RecordHolder extends RecyclerView.ViewHolder {

    private final ImageLoader mImageLoader;
    @BindView(R.id.text_position) TextView mTextPosition;
    @BindView(R.id.image_avatar) ImageView mImageAvatar;
    @BindView(R.id.text_name) TextView mTextName;
    @BindView(R.id.text_coins) TextView mTextCoins;
    @BindColor(R.color.accent) int mColorAccent;
    @BindColor(R.color.textPrimary) int mColorTextPrimary;
    @BindColor(R.color.light_grey) int mColorLightGrey;

    public RecordHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        mImageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
    }
    /**
     * to change background of position 0,1 and others
     * and text colors of position 0,1 and others
     */
    public void bind(ContestLeadershipRecord item, boolean isSingleItem) {
        final Context context = itemView.getContext();

        mTextCoins.setText(getParametrisedText(context, R.string.text_leadership_coins, item.amount()));
        mTextName.setText(String.format(Constants.NAME_FORMAT, item.user().getFirstName(), item.user().getLastName()));
        mTextPosition.setText(getParametrisedText(context, R.string.text_leadership_position, item.position()));
        final int position = getAdapterPosition();
        if (position == 0) {
            itemView.setBackgroundColor(mColorLightGrey);
            final int color = isSingleItem ? mColorAccent : mColorTextPrimary;
            mTextCoins.setTextColor(color);
            mTextName.setTextColor(color);
            mTextPosition.setTextColor(color);
        } else if (position == 1) {
            itemView.setBackground(null);
            mTextCoins.setTextColor(mColorAccent);
            mTextName.setTextColor(mColorAccent);
            mTextPosition.setTextColor(mColorAccent);
        } else {
            itemView.setBackground(null);
            mTextCoins.setTextColor(mColorTextPrimary);
            mTextName.setTextColor(mColorTextPrimary);
            mTextPosition.setTextColor(mColorTextPrimary);
        }
        mImageLoader.displayCircularImage(item.user().getImage(), mImageAvatar, R.dimen.list_avatar_size, R.drawable.avatar_placeholder);
    }

    /**
     *
     * @param context Context of class
     * @return SpannableString value
     */
    private CharSequence getParametrisedText(Context context, @StringRes int stringRes, int arg) {
        final String full = context.getResources().getString(stringRes, arg);
        final SpannableString result = new SpannableString(full);
        final String argsStr = String.valueOf(arg);
        final int idx = full.indexOf(argsStr);
        result.setSpan(new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), FontConfig.bold)), idx, idx + argsStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }
}

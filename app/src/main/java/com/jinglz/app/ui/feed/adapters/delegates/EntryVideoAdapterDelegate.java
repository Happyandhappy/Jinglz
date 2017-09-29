package com.jinglz.app.ui.feed.adapters.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinglz.app.R;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.ui.base.AdapterDelegate;
import com.jinglz.app.ui.base.widget.CountDownTimerView;
import com.jinglz.app.ui.feed.adapters.AvailableVideoListener;
import com.jinglz.app.ui.feed.adapters.VideoTutorial;
import com.jinglz.app.ui.feed.models.AvailableVideoItemModel;
import com.jinglz.app.ui.feed.models.TutorialModel;
import com.jinglz.app.ui.feed.models.VideoItemModel;
import com.jinglz.app.ui.feed.models.VideoSectionModel;
import com.jinglz.app.utils.DateUtils;
import com.jinglz.app.utils.NumberUtils;
import com.transitionseverywhere.TransitionManager;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class EntryVideoAdapterDelegate implements AdapterDelegate<List<VideoItemModel>> {

    private final ImageLoader mImageLoader;
    private final int mViewType;
    private final AvailableVideoListener mListener;

    /**
     * Constructs new EntryVideoAdapterDelegate with specified viewType, imageLoader and listener.
     * it is used to initialize {@see mImageLoader}, {@see mViewType} and {@see mListener}.
     *
     * @param viewType it is used to set the type of view to be displayed
     * @param imageLoader  to display Images from different sources
     * @param listener to handle video click events
     */
    public EntryVideoAdapterDelegate(@VideoSectionModel.SectionType int viewType,
                                     @NonNull ImageLoader imageLoader,
                                     @NonNull AvailableVideoListener listener) {
        mImageLoader = imageLoader;
        mViewType = viewType;
        mListener = listener;
    }

    public int getViewType() {
        return mViewType;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isForViewType(@NonNull List<VideoItemModel> items, int position) {
        final VideoItemModel item = items.get(position);
        return item instanceof AvailableVideoItemModel && ((AvailableVideoItemModel) item).getViewed();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new EntryVideoViewHolder(LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.item_feed_video_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull List<VideoItemModel> items,
                                 int position,
                                 @NonNull RecyclerView.ViewHolder holder) {
        final EntryVideoViewHolder vh = (EntryVideoViewHolder) holder;
        final AvailableVideoItemModel item = (AvailableVideoItemModel) items.get(position);
        vh.bind(item);
    }

    class EntryVideoViewHolder extends SectioningAdapter.ItemViewHolder
            implements VideoTutorial {

        @BindView(R.id.image_preview) ImageView imagePreview;
        @BindView(R.id.text_name) TextView textName;
        @BindView(R.id.text_sponsor) TextView textSponsor;
        @BindView(R.id.text_time) CountDownTimerView textTime;
        @BindView(R.id.badge_time) LinearLayout badgeTime;
        @BindView(R.id.text_jack_pot) TextView textJackPot;
        @BindView(R.id.text_jack_pot_title) TextView textJackPotTitle;
        @BindView(R.id.badge_info) FrameLayout badgeInfo;
        @BindView(R.id.text_time_title) TextView textTimeTitle;
        @BindView(R.id.text_viewed) TextView textViewed;
        @BindView(R.id.image_re_watch) ImageView imageReWatch;

        @BindString(R.string.text_time_left) String stringTimeLeft;
        @BindString(R.string.text_draw_time) String stringDrawTime;
        @BindString(R.string.text_jack_pot) String stringJackPot;

        private AvailableVideoItemModel item;

        private Spannable timeLeftSpann;
        private Spannable jackPotSpann;
        private Spannable drawTimeSpann;

        public EntryVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initSpanns(itemView.getContext());
            initListener();
        }

        public void bind(AvailableVideoItemModel item) {
            this.item = item;
            mImageLoader.displayImage(item.getImageUrl(), imagePreview);
            textName.setText(item.getName());
            textSponsor.setText(item.getSponsor());
            textJackPot.setText(NumberUtils.toPrettyNumber(item.getJackPot()));
            textJackPotTitle.setText(jackPotSpann);
            badgeInfo.setVisibility(item.getInfoLink() != null ? View.VISIBLE : View.GONE);

            bindTime(item);
        }

        @Override
        public View getTutorialView(@TutorialModel.TutorialType int type) {
            switch (type) {
                case TutorialModel.TUTORIAL_TYPE_RE_WATCH:
                    return imageReWatch;
                case TutorialModel.TUTORIAL_TYPE_VIEWED_TIMER:
                    if (!item.isShowTimer()) {
                        badgeTime.performClick();
                    }
                    return badgeTime;
                case TutorialModel.TUTORIAL_TYPE_VIEWED:
                    return textViewed;
            }
            return null;
        }

        /**
         * This method is used to attach and detach markup objects/
         * @param context for application-specific resources
         */
        private void initSpanns(Context context) {
            final CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                    TypefaceUtils.load(context.getAssets(), FontConfig.black));

            timeLeftSpann = new SpannableString(stringTimeLeft.toUpperCase());
            timeLeftSpann.setSpan(typefaceSpan, stringTimeLeft.indexOf("\n"),
                                  stringTimeLeft.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            jackPotSpann = new SpannableString(stringJackPot.toUpperCase());
            jackPotSpann.setSpan(typefaceSpan, stringJackPot.indexOf("\n"),
                                 stringJackPot.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            drawTimeSpann = new SpannableString(stringDrawTime.toUpperCase());
            drawTimeSpann.setSpan(typefaceSpan, stringDrawTime.indexOf("\n"),
                                  stringDrawTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        private void initListener() {
            badgeInfo.setOnClickListener(v -> mListener.onClickLearnMore(item));
            badgeTime.setOnClickListener(v -> {
                item.setShowTimer(!item.isShowTimer());
                TransitionManager.beginDelayedTransition((ViewGroup) itemView);
                bindTime(item);
            });
            textTime.setOnTimerListener(new CountDownTimerView.TimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.e("DATEEEE CONVERT", DateUtils.toTimeFormat(item.getContestDate())+" DATE");

                    item.setLifeTime(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    mListener.onTimerFinished(item);
                }
            });
            itemView.setOnClickListener(v -> mListener.onClickVideo(item));
        }

        /**
         * This method is used to set time. it will start count down if
         * {@link AvailableVideoItemModel#isShowTimer()} returns true, set text otherwise.
         *
         * @param item it is used to fetch time information from video details.
         */
        private void bindTime(AvailableVideoItemModel item) {

            Log.e("DATEEEE CONVERT", DateUtils.toTimeFormat(item.getContestDate())+" DATE");

            if (item.isShowTimer()) {
                textTimeTitle.setText(timeLeftSpann);
                textTime.setDisplayTime(true);
                textTime.setTime(item.getLifeTime(), TimeUnit.MILLISECONDS);
                textTime.startCountDown();
            } else {
                textTimeTitle.setText(drawTimeSpann);
                textTime.setDisplayTime(false);
                textTime.setText(DateUtils.toTimeFormat(item.getContestDate()));
            }
        }
    }
}

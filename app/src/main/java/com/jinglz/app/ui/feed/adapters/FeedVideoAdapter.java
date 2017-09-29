package com.jinglz.app.ui.feed.adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.jinglz.app.config.FontConfig;
import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.ui.feed.adapters.delegates.EntryVideoAdapterDelegate;
import com.jinglz.app.ui.feed.adapters.delegates.NewVideoAdapterDelegate;
import com.jinglz.app.ui.feed.adapters.delegates.WatchedVideoAdapterDelegate;
import com.jinglz.app.ui.feed.models.VideoSectionModel;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Feed adapter to set videos in list
 * @NewVideoAdapterDelegate a view for recently uploaded videos
 * @WatchedVideoAdapterDelegate a view for showing videos that user watched already
 * @EntryVideoAdapterDelegate a view for newly arrived video
 */
public class FeedVideoAdapter extends SectioningAdapter {

    private final ArrayList<VideoSectionModel> mSections = new ArrayList<>();
    private final NewVideoAdapterDelegate mNewVideoAdapterDelegate;
    private final WatchedVideoAdapterDelegate mWatchedVideoAdapterDelegate;
    private final EntryVideoAdapterDelegate mEntryVideoAdapterDelegate;

    public FeedVideoAdapter(ImageLoader imageLoader, AvailableVideoListener availableListener, WatchedVideoListener watchedListener) {
        mEntryVideoAdapterDelegate = new EntryVideoAdapterDelegate(VideoSectionModel.VIDEOS_ENTRY, imageLoader, availableListener);
        mNewVideoAdapterDelegate = new NewVideoAdapterDelegate(VideoSectionModel.VIDEOS_NEW, imageLoader, availableListener);
        mWatchedVideoAdapterDelegate = new WatchedVideoAdapterDelegate(VideoSectionModel.VIDEOS_HISTORY,
                                                                       imageLoader, watchedListener);
    }

    public void setItems(List<VideoSectionModel> sections) {
        mSections.clear();
        mSections.addAll(sections);
        notifyAllSectionsDataSetChanged();
    }

    public void clear() {
        mSections.clear();
        notifyAllSectionsDataSetChanged();
    }

    public int getSectionPosition(@VideoSectionModel.SectionType int type) {
        final Optional<Integer> index = Stream.range(0, mSections.size())
                .filter(i -> mSections.get(i).type() == type)
                .findFirst();
        if (mSections.size() > 0 && index.isPresent()) {
            return getAdapterPositionForSectionHeader(index.get());
        }
        return -1;
    }

    @Override
    public int getSectionItemUserType(int sectionIndex, int itemIndex) {
        if (mNewVideoAdapterDelegate.isForViewType(mSections.get(sectionIndex).items(), itemIndex)) {
            return mNewVideoAdapterDelegate.getViewType();
        } else if (mWatchedVideoAdapterDelegate.isForViewType(mSections.get(sectionIndex).items(), itemIndex)) {
            return mWatchedVideoAdapterDelegate.getViewType();
        } else if (mEntryVideoAdapterDelegate.isForViewType(mSections.get(sectionIndex).items(), itemIndex)) {
            return mEntryVideoAdapterDelegate.getViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        if (mNewVideoAdapterDelegate.getViewType() == itemUserType) {
            return (ItemViewHolder) mNewVideoAdapterDelegate.onCreateViewHolder(parent);
        } else if (mWatchedVideoAdapterDelegate.getViewType() == itemUserType) {
            return (ItemViewHolder) mWatchedVideoAdapterDelegate.onCreateViewHolder(parent);
        } else if (mEntryVideoAdapterDelegate.getViewType() == itemUserType) {
            return (ItemViewHolder) mEntryVideoAdapterDelegate.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.header_feed_video, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder,
                                     int sectionIndex,
                                     int itemIndex,
                                     int itemUserType) {
        final VideoSectionModel section = mSections.get(sectionIndex);
        if (mNewVideoAdapterDelegate.getViewType() == itemUserType) {
            mNewVideoAdapterDelegate.onBindViewHolder(section.items(), itemIndex, viewHolder);
        } else if (mWatchedVideoAdapterDelegate.getViewType() == itemUserType) {
            mWatchedVideoAdapterDelegate.onBindViewHolder(section.items(), itemIndex, viewHolder);
        } else if (mEntryVideoAdapterDelegate.getViewType() == itemUserType) {
            mEntryVideoAdapterDelegate.onBindViewHolder(section.items(), itemIndex, viewHolder);
        }
    }

    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder,
                                       int sectionIndex,
                                       int headerUserType) {
        final HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        final VideoSectionModel section = mSections.get(sectionIndex);
        holder.bind(section);
    }

    @Override
    public int getNumberOfSections() {
        return mSections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return mSections.get(sectionIndex).items().size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    /**
     * Holder class to handle views of feed listing
     */
    static class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {

        @BindView(R.id.text_title) TextView textTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(VideoSectionModel section) {
            final Context context = itemView.getContext();
            final String title = section.title();
            final String[] words = TextUtils.split(title, " ");

            final String lastWord = words[words.length - 1];

            final SpannableStringBuilder sBuilder = new SpannableStringBuilder(title);
            final CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                    TypefaceUtils.load(context.getAssets(), FontConfig.bold));
            sBuilder.setSpan(typefaceSpan, title.indexOf(lastWord), title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textTitle.setText(sBuilder, TextView.BufferType.SPANNABLE);
        }
    }
}

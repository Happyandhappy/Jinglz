package com.jinglz.app.ui.winnings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinglz.app.R;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to set winning list data
 */
public class WinningsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int FOOTER = 1;
    private final ImageLoader mImageLoader;
    private final List<ContestLeadershipRecord> mData;
    private final View.OnClickListener mFooterClickListener;

    public WinningsAdapter(ImageLoader imageLoader, View.OnClickListener footerClickListener) {
        mImageLoader = imageLoader;
        mFooterClickListener = footerClickListener;
        mData = new ArrayList<>();
    }

    /**
     * Return int id of view of item position
     * @param position Integer value contains item position
     * @return it position == FOOTER footer view will be return else item view will be return
     */
    @Override
    public int getItemViewType(int position) {
        return position >= mData.size() ? FOOTER : ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FOOTER:
                return new FooterHolder(inflater.inflate(R.layout.footer_winning, parent, false), mFooterClickListener);
            case ITEM:
                return new RecordHolder(inflater.inflate(R.layout.list_item_winnings, parent, false), mImageLoader);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = holder.getItemViewType();
        if (viewType == ITEM) {
            final ContestLeadershipRecord record = mData.get(position);
            final RecordHolder vh = (RecordHolder) holder;
            vh.bind(record, mData.size() == 1);
        }
    }

    /**
     * @return Integer value contains size of list
     */
    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    /**
     * Update list data
     */
    public void update(List<ContestLeadershipRecord> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }
}

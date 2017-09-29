package com.jinglz.app.ui.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class UltimateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE_ID = -1;
    private static final int FOOTER_TYPE_ID = -2;
    private boolean mFooterVisibility;
    private boolean mHeaderVisibility;

    /**
     * interface to deal with HeaderViewHolder. used for retrieving and
     * binding {@link HeaderViewHolder}.
     *
     */
    public interface HeaderInterface {

        HeaderViewHolder getHeaderViewHolder(ViewGroup parent);

        void bindHeaderViewHolder(RecyclerView.ViewHolder vh);
    }

    /**
     * interface to deal with FooterViewHolder. used for retrieving and
     * binding {@link FooterViewHolder}.
     *
     */
    public interface FooterInterface {

        FooterViewHolder getFooterViewHolder(ViewGroup parent);

        void bindFooterViewHolder(RecyclerView.ViewHolder vh);
    }

    public UltimateAdapter() {
        setHasStableIds(true);
        // check footer
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE_ID) {
            final HeaderInterface headerInterface = getThisHeader();
            return headerInterface.getHeaderViewHolder(parent);
        } else if (viewType == FOOTER_TYPE_ID) {
            final FooterInterface footerInterface = getThisFooter();
            return footerInterface.getFooterViewHolder(parent);
        } else {
            return getDataViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            // bind header
            getThisHeader().bindHeaderViewHolder(holder);
            ((HeaderViewHolder) holder).hideHeader(!mHeaderVisibility);
        } else if (isFooterPosition(position)) {
            // bind footer
            getThisFooter().bindFooterViewHolder(holder);
            ((FooterViewHolder) holder).hideFooter(!mFooterVisibility);
        } else {
            // bind data
            if (withHeader()) {
                position -= 1;
            }
            bindDataViewHolder(holder, position);
        }
    }

    @Override
    public final int getItemViewType(int absolutePosition) {
        if (absolutePosition == 0 && withHeader()) {
            return HEADER_TYPE_ID;
        } else if (withFooter() && absolutePosition == getFooterPosition()) {
            return FOOTER_TYPE_ID;
        } else {
            final int dataType = getDataViewType(convertAbsolutePositionToData(absolutePosition));
            if (dataType == HEADER_TYPE_ID || dataType == FOOTER_TYPE_ID) {
                throw new IllegalArgumentException(
                        "Data type can't be " + dataType + ", this value is reserved");
            }
            return dataType;
        }
    }

    @Override
    public long getItemId(int position) {
        if (position == 0 && withHeader()) {
            return -111L;
        } else if (withFooter() && position == getFooterPosition()) {
            return -222L;
        } else {
            return getDataId(withHeader() ? position - 1 : position);
        }
    }

    @Override
    public final int getItemCount() {
        int result = 0;
        if (withFooter()) {
            result++;
        }
        if (withHeader()) {
            result++;
        }
        result += getDataSize();
        return result;
    }

    public boolean withFooter() {
        return this instanceof FooterInterface;
    }

    public boolean withHeader() {
        return this instanceof HeaderInterface;
    }

    public abstract int getDataSize();

    public abstract long getDataId(int dataPosition);

    public abstract int getDataViewType(int dataPosition);

    @NonNull
    public abstract RecyclerView.ViewHolder getDataViewHolder(@NonNull ViewGroup parent, int dataViewType);

    public abstract void bindDataViewHolder(@NonNull RecyclerView.ViewHolder vh, int dataPosition);

    public int getDataPosition(int generalPosition) {
        if (withHeader()) {
            return generalPosition - 1;
        }
        return generalPosition;
    }

    public void setFooterVisibility(boolean visible) {
        mFooterVisibility = visible;
        notifyDataSetChanged();
    }

    public void setHeaderVisibility(boolean visible) {
        mHeaderVisibility = visible;
        notifyDataSetChanged();
    }

    public boolean isFooterVisibility() {
        return mFooterVisibility;
    }

    public boolean isHeaderVisibility() {
        return mHeaderVisibility;
    }

    public boolean hasData() {
        return getDataSize() > 0;
    }

    protected int convertAbsolutePositionToData(int absolutePosition) {
        int result = absolutePosition;
        if (withHeader()) {
            result--;
        }
        return result;
    }

    public boolean isHeaderPosition(int position) {
        return getItemViewType(position) == HEADER_TYPE_ID;
    }

    public boolean isFooterPosition(int position) {
        return getItemViewType(position) == FOOTER_TYPE_ID;
    }

    protected int getFooterPosition() {
        int result = 0;
        if (withHeader()) {
            result++;
        }
        result += getDataSize();
        return result;
    }

    private FooterInterface getThisFooter() {
        return (FooterInterface) this;
    }

    private HeaderInterface getThisHeader() {
        return (HeaderInterface) this;
    }

    public abstract static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void hideFooter(boolean hide);
    }

    public abstract static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void hideHeader(boolean hide);
    }

}

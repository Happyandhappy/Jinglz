package com.jinglz.app.utils.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * To handle scrolling of {@link RecyclerView}
 */
public class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private static final int ITEMS_OFFSET_TO_LOAD_DEFAULT = 3;

    private final OnRecyclerViewScrolledToPageListener mCallback;
    private final int mOffset;
    private LinearLayoutManager mLayoutManager;
    private boolean loading;
    private int previousTotal;
    public interface OnRecyclerViewScrolledToPageListener {

        void onRecyclerViewScrolledToEnd();
    }

    public PaginationScrollListener(OnRecyclerViewScrolledToPageListener callback) {
        this.mCallback = callback;
        this.mOffset = ITEMS_OFFSET_TO_LOAD_DEFAULT;
        if (mCallback == null) {
            throw new IllegalStateException("OnRecyclerViewScrolledToPageListener is NULL");
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mLayoutManager == null) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }

        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = mLayoutManager.getItemCount();
        final int firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        final int lastVisibleItem = firstVisibleItem + visibleItemCount;
        if (loading) {
            if (totalItemCount != previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (lastVisibleItem + mOffset) > totalItemCount) {
            // End has been reached

            mCallback.onRecyclerViewScrolledToEnd();

            // Do something

            loading = true;
        }

    }

    public void reset() {
        loading = false;
        previousTotal = -1;
    }
}
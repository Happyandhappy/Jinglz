package com.jinglz.app.utils.recycler;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jinglz.app.ui.base.UltimateAdapter;

/**
 * An ItemDecoration allows the app to add a special drawing and layout offset
 * to specific item views from the adapter's data set.
 *
 * This can be useful for drawing dividers between items, highlights, visual grouping boundaries and more.
 * Set items in Linear layout format.
 */
public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public LinearSpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        final UltimateAdapter adapter = (UltimateAdapter) parent.getAdapter();
        final int position = parent.getChildAdapterPosition(view);

        if (adapter.isHeaderPosition(position) || adapter.isFooterPosition(position)) {
            return;
        }

        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            outRect.top = mSpace;
        } else if (layoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
            outRect.left = mSpace;
        }
    }
}
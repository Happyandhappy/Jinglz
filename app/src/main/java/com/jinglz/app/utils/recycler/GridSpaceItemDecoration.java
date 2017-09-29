package com.jinglz.app.utils.recycler;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jinglz.app.ui.base.UltimateAdapter;

/**
 * An ItemDecoration allows the app to add a special drawing and layout offset
 * to specific item views from the adapter's data set.
 *
 * This can be useful for drawing dividers between items, highlights, visual grouping boundaries and more.
 * Set items in grid layout format.
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public GridSpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        final UltimateAdapter adapter = (UltimateAdapter) parent.getAdapter();
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            final int position = parent.getChildAdapterPosition(view);
            final int spanCount = layoutManager.getSpanCount();
            final int column = (adapter.withHeader() ? position + 1 : position) % spanCount;

            if (adapter.isHeaderPosition(position) || adapter.isFooterPosition(position)) {
                return;
            }

            if (column > 0) {
                outRect.left = mSpace * (spanCount - column) / spanCount;
            }
            if (column < spanCount - 1) {
                outRect.right = mSpace * (column + 1) / spanCount;
            }
            outRect.top = mSpace;
        }
    }
}
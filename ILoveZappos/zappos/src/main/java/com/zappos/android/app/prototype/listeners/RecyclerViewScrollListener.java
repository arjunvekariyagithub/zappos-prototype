package com.zappos.android.app.prototype.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by arjun on 2/8/17.
 *
 * Scroll listener for {@link RecyclerView}.
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    public ScrollPositionListener mScrollPositionListener;
    public RecyclerViewScrollListener(ScrollPositionListener scrollPositionListener) {
        mScrollPositionListener = scrollPositionListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
        if (mScrollPositionListener != null) {
            mScrollPositionListener.onScrolledPosition(layoutManager.findLastVisibleItemPosition());
        }
    }

    public interface ScrollPositionListener {

        /**
         * Listener to notify fragment about current scroll position
         *
         * @param position position for last item in list
         */
        void onScrolledPosition(int position);
    }
}

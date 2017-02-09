package com.zappos.android.app.prototype.listeners;

/**
 * Created by arjun on 2/3/17.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public OnItemClickListener listener;

    private GestureDetector gestureDetector;

    @Nullable
    private View childView;

    private int childViewPosition;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent event) {
        childView = view.findChildViewUnder(event.getX(), event.getY());
        childViewPosition = view.getChildPosition(childView);

        return childView != null && gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent event) {
        // Not needed.
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * A click listener for {@link RecyclerView} items.
     */
    public interface OnItemClickListener {

        /**
         * Called when an item is clicked.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        void onItemClick(View childView, int position);

        /**
         * Called when an item is long pressed.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        void onItemLongPress(View childView, int position);

    }

    /**
     * A simple click listener whose methods can be overridden one by one.
     */
    public static abstract class SimpleOnItemClickListener implements OnItemClickListener {

        /**
         * Called when an item is clicked. The default implementation is a no-op.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        public void onItemClick(View childView, int position) {
            // Do nothing.
        }

        /**
         * Called when an item is long pressed. The default implementation is a no-op.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        public void onItemLongPress(View childView, int position) {
            // Do nothing.
        }

    }

    /**
     * Gesture detector to handle {@link RecyclerView} click events.
     */
    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /**
         * Called when an item is tapped.
         *
         * @param event touch event
         *
         */
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (childView != null) {
                listener.onItemClick(childView, childViewPosition);
            }

            return true;
        }

        /**
         * Called when an item is long pressed.
         *
         * @param event touch event
         *
         */
        @Override
        public void onLongPress(MotionEvent event) {
            if (childView != null) {
                listener.onItemLongPress(childView, childViewPosition);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

    }

}

package com.zappos.android.app.prototype.views;

/**
 * Created by arjun on 2/3/17.
 */

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zappos.android.app.prototype.activity.ProductPageActivity;

/**
 * Created by arjun on 2/5/17.
 * <p>
 * Behaviour class for {@link FloatingActionButton}
 * Responsible for hiding and showing {@link FloatingActionButton} during list scroll.
 */
public class ScrollingFABBehavior extends FloatingActionButton.Behavior {

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (ProductPageActivity.mFABProgressCircle == null) return;

        // hide FAB while scrolling down
//        if (dyConsumed > 0) {
//            ProductPageActivity.mFABProgressCircle.animate().setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    ProductPageActivity.mFABProgressCircle.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            }).scaleX(0.0f).scaleY(0.0f).setInterpolator(new LinearInterpolator()).start();
//        } else if (dyConsumed < 0) { // show FAB while scrolling up
//            ProductPageActivity.mFABProgressCircle.setVisibility(View.VISIBLE);
//            ProductPageActivity.mFABProgressCircle.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new LinearInterpolator()).start();
//        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
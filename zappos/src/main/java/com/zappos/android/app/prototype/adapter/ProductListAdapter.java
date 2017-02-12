package com.zappos.android.app.prototype.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zappos.android.app.prototype.BR;
import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.models.ProductInfo;
import com.zappos.android.app.prototype.utils.Utils;

import java.util.HashSet;
import java.util.List;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Adapter for {@link RecyclerView}. Uses data-binding apis for binding data to
 * {@link RecyclerView} items.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductInfoBindingHolder> {
    private static final String TAG = ProductListAdapter.class.getSimpleName();
    private List<ProductInfo> mProductsList;
    private Context mContext;
    private RelativeLayout mLayoutForColumnWidth;
    private HashSet<Integer> mFavoriteList = new HashSet<Integer>();
    private boolean mIsSimilarProductsView = false;

    public ProductListAdapter(List<ProductInfo> products, Context context,
                              boolean isSimilarProductsView) {
        this.mProductsList = products;
        this.mContext = context;
        this.mIsSimilarProductsView = isSimilarProductsView;
    }

    /**
     * @return appropriate list item layout based on current view mode
     */
    private int getRowLayout() {
        if (mIsSimilarProductsView) {
            return R.layout.row_recommendation;
        }
        if (Utils.isLandscape(mContext) || Utils.isGridView()) {
            return R.layout.row_gridview;
        } else {
            return R.layout.row_listview;
        }
    }

    @Override
    public ProductListAdapter.ProductInfoBindingHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getRowLayout(), parent, false);
        if (Utils.isListView()) {
            View pager = view.findViewById(R.id.view_pager_slide_show);
            if (pager != null) pager.setVisibility(View.GONE);
        }
        mLayoutForColumnWidth = (RelativeLayout) view.findViewById(R.id.root);
        //ImageZoomHelper.setViewZoomable(view.findViewById(R.id.root));
        return new ProductInfoBindingHolder(view);
    }

    /**
     * Called for binding data to particular list item.
     * Uses {@link DataBindingUtil} apis for automatic data binding
     *
     * @param holder   view holder for list item
     * @param position position of list item to which data is to be bound
     */

    @Override
    public void onBindViewHolder(ProductInfoBindingHolder holder, final int position) {
        ProductInfo product = mProductsList.get(position);
        holder.getBinding().setVariable(BR.product, product);
        holder.getBinding().executePendingBindings();
        if (mIsSimilarProductsView) {
            View leftMargin = holder.itemView.findViewById(R.id.margin_layout_left);
            View rightMargin = holder.itemView.findViewById(R.id.margin_layout_right);
            if (position == 0) {
                leftMargin.setVisibility(View.VISIBLE);
                rightMargin.setVisibility(View.GONE);
            } else if (position == mProductsList.size() - 1) {
                rightMargin.setVisibility(View.VISIBLE);
                leftMargin.setVisibility(View.GONE);
            } else {
                rightMargin.setVisibility(View.GONE);
                leftMargin.setVisibility(View.GONE);
            }
        } else {
            handleFavourite(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mProductsList == null ? 0 : mProductsList.size();
    }

    public int getColumnWidth() {
        if (mLayoutForColumnWidth != null) {
            return mLayoutForColumnWidth.getWidth();
        }
        return 0;
    }

    /**
     * Update state of favourite {@link CheckBox} based on prior user selection
     * Uses list item position to keep track of user selection.
     *
     * @param holder   view holder for list item
     * @param position position of list item to which data is to be bound
     */

    public void handleFavourite(ProductInfoBindingHolder holder, final int position) {
        CheckBox cb = (CheckBox) holder.itemView.findViewById(R.id.fav_button);
        cb.setTag(position);
        cb.setChecked(mFavoriteList.contains((Integer) cb.getTag()));

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                Log.d("Zappos", "onCheckedChanged(): " + isChecked);
                if (isChecked) {
                    if (mFavoriteList.contains((Integer) buttonView.getTag())) return;

                    mFavoriteList.add((Integer) buttonView.getTag());
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(1000);
                    animatorSet.setStartDelay(100);
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSet.playTogether(
                            ObjectAnimator.ofFloat(buttonView, "scaleY", 1, 2.0f, 1),
                            ObjectAnimator.ofFloat(buttonView, "scaleX", 1, 2.0f, 1)
                    );
                    animatorSet.start();
                    Utils.showShortToast(mContext, mContext.getResources().
                            getString(R.string.item_saved_to_liked_list));
                } else {
                    if (!mFavoriteList.contains((Integer) buttonView.getTag())) return;
                    mFavoriteList.remove((Integer) buttonView.getTag());
                    Utils.showShortToast(mContext, mContext.getResources().
                            getString(R.string.item_removed_from_liked_list));
                }

            }
        });

    }

    /**
     * View holder class for {@link RecyclerView} items.
     */

    public static class ProductInfoBindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private View itemView;

        public ProductInfoBindingHolder(View v) {
            super(v);
            itemView = v;
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
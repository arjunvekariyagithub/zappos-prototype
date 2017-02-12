package com.zappos.android.app.prototype.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zappos.android.app.prototype.BR;
import com.zappos.android.app.prototype.R;

import java.util.List;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Adapter for {@link RecyclerView}. Uses data-binding apis for binding data to
 * {@link RecyclerView} items.
 */
public class SizeListAdapter extends RecyclerView.Adapter<SizeListAdapter.ProductInfoBindingHolder> {
    private static final String TAG = SizeListAdapter.class.getSimpleName();
    public int mSelectedStylePos = 0;
    private List<String> mSizeList;
    private Context mContext;

    public SizeListAdapter(List<String> products, Context context, int selectedStylePos) {
        this.mSizeList = products;
        this.mContext = context;
        this.mSelectedStylePos = selectedStylePos;
    }

    /**
     * @return appropriate list item layout based on current view mode
     */
    private int getRowLayout() {
        return R.layout.product_size_text;
    }

    @Override
    public SizeListAdapter.ProductInfoBindingHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getRowLayout(), parent, false);
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
        holder.getBinding().setVariable(BR.size, mSizeList.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSizeList == null ? 0 : mSizeList.size();
    }

    public void setSelectedStylePos(int selectedStylePos) {
        mSelectedStylePos = selectedStylePos;
    }


    /**
     * View holder class for {@link RecyclerView} items.
     */

    public static class ProductInfoBindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ProductInfoBindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
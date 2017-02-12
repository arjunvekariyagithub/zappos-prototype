package com.zappos.android.app.prototype.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zappos.android.app.prototype.BR;
import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.activity.ProductPageActivity;
import com.zappos.android.app.prototype.models.ImageInfo;

import java.util.List;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Adapter for {@link RecyclerView}. Uses data-binding apis for binding data to
 * {@link RecyclerView} items.
 */
public class StylesListAdapter extends RecyclerView.Adapter<StylesListAdapter.ProductInfoBindingHolder> {
    private static final String TAG = StylesListAdapter.class.getSimpleName();
    public int mSelectedStylePos = 0;
    private List<ImageInfo> mProductsList;
    private Context mContext;
    private RelativeLayout mLayoutForColumnWidth;

    public StylesListAdapter(List<ImageInfo> products, Context context, int selectedStylePos) {
        this.mProductsList = products;
        this.mContext = context;
        this.mSelectedStylePos = selectedStylePos;
    }

    /**
     * @return appropriate list item layout based on current view mode
     */
    private int getRowLayout() {
        return R.layout.row_styles_list;
    }

    @Override
    public StylesListAdapter.ProductInfoBindingHolder onCreateViewHolder(ViewGroup parent,
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
        ImageInfo style = mProductsList.get(position);
        holder.getBinding().setVariable(BR.styles, style);
        holder.getBinding().executePendingBindings();

        if (ProductPageActivity.selectedStylesRowLayout == null && position == 0) {
            ProductPageActivity.selectedStylesRowLayout = (RelativeLayout)
                    holder.itemView.findViewById(R.id.row);
        }

        if (position == mSelectedStylePos) {
            holder.itemView.findViewById(R.id.row).setSelected(true);
        } else {
            holder.itemView.findViewById(R.id.row).setSelected(false);
        }

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
    }

    @Override
    public int getItemCount() {
        return mProductsList == null ? 0 : mProductsList.size();
    }

    public void setSelectedStylePos(int selectedStylePos) {
        mSelectedStylePos = selectedStylePos;
    }

    public int getColumnWidth() {
        if (mLayoutForColumnWidth != null) {
            return mLayoutForColumnWidth.getWidth();
        }
        return 0;
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
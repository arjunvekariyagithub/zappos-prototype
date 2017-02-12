package com.zappos.android.app.prototype.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.models.ImageInfo;

import java.util.List;

/**
 * Created by arjun on 2/7/17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<ImageInfo> mProductImages;

    public CustomPagerAdapter(Context context, List<ImageInfo> productImages) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProductImages = productImages;
    }

    @Override
    public int getCount() {
        return mProductImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).
                inflate(R.layout.view_pager_item_slideshow, container, false);

        //ViewDataBinding binding = DataBindingUtil.bind(itemView);
//
//        ImageInfo style = mProductImages.get(position);
//        Log.d("Zappos", "ViewPager - instantiateItem(): " + style.getFilename());
//        binding.setVariable(BR.imageInfo, style);
//        binding.executePendingBindings();

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.item_image);

        /**
         * Using Glide to handle image loading.
         */

        Glide.with(imageView.getContext()).load(mProductImages.get(position).getFilename())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontTransform()
                .crossFade()
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

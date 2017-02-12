package com.zappos.android.app.prototype.binding;

import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zappos.android.app.prototype.utils.Constants;
import com.zappos.android.app.prototype.utils.Utils;

/**
 * Created by arjun on 2/3/17.
 * <p>
 * {@link BindingAdapter} for list item data binding.
 */

public class CustomBindingAdapter {

    /**
     * Customized binding method for loading product thumbnails into {@link ImageView}.
     * Using Glide library to handle image loading.
     * <p>
     * Since default '*t-THUMBNAIL.jpg' thumbnail is too small, I have used type 'PAIR' thumbnails
     * with '4x' resolution. I have experimented by retrieving images for different products with
     * url 'http://api.zappos.com/Image?productId=8076430&styleId=2079337' and realized that
     * '*-p-4x.jpg' images are available for almost every product present in zappos database,
     * hence using '*p-4x.jpg' images for thumbnails in this demo app.
     * <p>
     * For some rare products '*p-4x.jpg' may not be available but for demo purpose I am making
     * assumption that it is available for all products.
     *
     * @param imageView image view to load thumbnails into
     * @param url       url for thumbnail of particular product
     * @see Utils#getLargeThumbUrl(String)
     */
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(final ImageView imageView, String url) {
        String largeThumbUrl = Utils.getLargeThumbUrl(url);
        if (largeThumbUrl == null || largeThumbUrl.isEmpty()) {
            imageView.setVisibility(View.GONE);
            return;
        }

        Glide.with(imageView.getContext()).load(largeThumbUrl)
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .crossFade()
                .into(imageView);
    }

    /**
     * Customized binding method to set {@link TextView} visibility based on percentOff value.
     *
     * @param textView   text view for which visibility is to be set
     * @param percentOff discount percentage value
     */
    @BindingAdapter("bind:visibilityOnPercentOff")
    public static void setPriceTextVisibility(TextView textView, String percentOff) {
        if (percentOff.equals(Constants.TEXT_ZERO_PERCENT)) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(percentOff + Constants.TEXT_OFF);
        }
    }

    /**
     * Customized binding method to set {@link TextView} visibility based on percentOff value.
     * Also strikes out original price text if discount is available.
     *
     * @param textView      text view for which visibility is to be set
     * @param originalPrice product original price
     * @param percentOff    discount percentage value
     */

    @BindingAdapter({"bind:originalPrice", "bind:visibilityOnPercentOff"})
    public static void handleOriginalPriceText(TextView textView, String originalPrice, String percentOff) {
        if (percentOff.equals(Constants.TEXT_ZERO_PERCENT)) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            //textView.setText(originalPrice);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Customized binding listener to be called when favourite view state is changed
     *
     * @param view
     */
    public void onFavouriteChanged(View view) {
        CheckBox cb = (CheckBox) view;
        if (cb.isChecked()) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(1000);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(cb, "scaleY", 1, 1.75f, 1),
                    ObjectAnimator.ofFloat(cb, "scaleX", 1, 1.75f, 1)
            );
            animatorSet.start();
        }
    }

    public void onClickSizeText(View textView) {
        Log.d("Zappos", "onClickSizeText() - " + textView.isSelected());
        ((TextView) textView).setSelected(!textView.isSelected());
    }
}

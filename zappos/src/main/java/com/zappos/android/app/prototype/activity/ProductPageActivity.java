package com.zappos.android.app.prototype.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zappos.android.app.prototype.BR;
import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.ZapposApplication;
import com.zappos.android.app.prototype.adapter.CustomPagerAdapter;
import com.zappos.android.app.prototype.adapter.ProductListAdapter;
import com.zappos.android.app.prototype.adapter.SizeListAdapter;
import com.zappos.android.app.prototype.adapter.StylesListAdapter;
import com.zappos.android.app.prototype.data.DataHandler;
import com.zappos.android.app.prototype.listeners.RecyclerItemClickListener;
import com.zappos.android.app.prototype.models.ImageInfo;
import com.zappos.android.app.prototype.models.ProductInfo;
import com.zappos.android.app.prototype.models.SingleProductInfo;
import com.zappos.android.app.prototype.utils.Constants;
import com.zappos.android.app.prototype.utils.Utils;
import com.zappos.android.app.prototype.views.CustomViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductPageActivity extends AppCompatActivity
        implements DataHandler.ProductDetailsListener {

    public static RelativeLayout selectedStylesRowLayout = null;
    public static FABProgressCircle mFABProgressCircle;
    private static int cartItemCount = 0;

    @BindView(R.id.recycler_view_recommend_products)
    public RecyclerView mRecyclerViewRecommendProducts;
    @BindView(R.id.recycler_view_styles)
    public RecyclerView mRecyclerViewStyles;
    @BindView(R.id.recycler_view_size)
    public RecyclerView mRecyclerViewProductSize;

    private TextView mSelectedSizeLayout = null;
    private FloatingActionButton mFAB;
    private View mCartBadgeLayout;
    private TextView mCartBadgeText;
    private ImageView mCartMenuButton;
    private int mSelectedStylePos = 0;
    private int mSelectedSizePos = 0;
    private ArrayList<ImageInfo> mStylePairImageList = new ArrayList<ImageInfo>();
    private Context mContext;
    private CustomPagerAdapter mCustomPagerAdapter;
    private CustomViewPager mViewPager;
    private ProductListAdapter mSimilarProductAdapter;
    private StylesListAdapter mStyleAdapter;
    private RelativeLayout mSimilarProductsLayout;
    private ProgressBar mSimilarProductsProgress;
    private LinearLayout mStylesLayout;
    private List<ImageInfo> mSelectedStyleImages;
    private HashMap<String, List<ImageInfo>> mStylesImages;
    private List<ProductInfo> mSimilarProductsList;
    private String mSelectedStyleId;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private Menu mMenu;
    private ProductInfo mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        DataHandler.getInstance().setProductDetailsListener(this);

        Intent intent = getIntent();

        // check if app is opened from external request i.e. product share link
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            String host = data.getHost(); //"www.zappos.com"
            String pid = data.getQueryParameter("productId");
            String style = data.getQueryParameter("styleId");

            mProduct = new ProductInfo(null, null, pid,
                    null, style, null, null,
                    null, null, null);

            DataHandler.getInstance().requestSingleProduct(mProduct.getProductId());
        } else {
            mProduct = Utils.getCurrentProductInfo();
        }

        initView();
        requestData();
    }

    /**
     * init views and bind data
     */
    private void initView() {
        View root = getLayoutInflater().inflate(R.layout.activity_product_details, null, true);
        setContentView(root);
        ViewDataBinding dataBinding = DataBindingUtil.bind(root);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.home));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFABProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);

        mFABProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isNetworkConnected(mContext)) {
                    Utils.showShortToast(mContext, mContext.getResources().
                            getString(R.string.error_msg_no_internet));
                    return;
                }
                mFABProgressCircle.show();
                mFABProgressCircle.beginFinalAnimation();

                mFABProgressCircle.attachListener(new FABProgressListener() {
                    @Override
                    public void onFABProgressAnimationEnd() {
                        animateCartMenu();
                    }
                });
            }
        });

        dataBinding.setVariable(BR.product, mProduct);
        dataBinding.setVariable(BR.bindingAdapter, ZapposApplication.getCustomBindingAdapter());
        dataBinding.executePendingBindings();

        initRecyclerViewRecommendProducts();
        initSizeView();

        mSimilarProductsLayout = (RelativeLayout) findViewById(R.id.
                product_page_recommendation_layout);
        mStylesLayout = (LinearLayout) findViewById(R.id.
                product_page_style_layout);

        LinearLayout shareItem = (LinearLayout) findViewById(R.id.share_layout);

        shareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProduct();
            }
        });

        mViewPager = (CustomViewPager) findViewById(R.id.view_pager_slide_show);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);
    }

    /**
     * request {@link DataHandler} to fetch required data
     */
    private void requestData() {
        DataHandler.getInstance().requestImages4Product(mProduct.getProductId());
        DataHandler.getInstance().requestSimilarProduct(mProduct.getStyleId());
    }

    /**
     * Called on response to {@link DataHandler#requestImages4Product(String)}
     *
     * @param productImages list of ImageInfo objects for every style of this product.
     */
    @Override
    public void onImageInfoResponse(HashMap<String, List<ImageInfo>> productImages) {
        mStylesImages = productImages;
        mSelectedStyleImages = productImages.get(mProduct.getStyleId());
        if (mSelectedStyleImages == null) return;
        mSelectedStyleId = mProduct.getStyleId();
        startStyleImageSlideShow();
        if (productImages.size() > 1) {
            initStylesView();
        }
    }

    /**
     * Called in response to {@link DataHandler#requestSimilarProduct(String)}
     *
     * @param productInfoList list of {@link ProductInfo} objects
     */

    @Override
    public void onSimilarProductsResponse(List<ProductInfo> productInfoList) {
        mSimilarProductsList = productInfoList;

        if (mSimilarProductsList == null || mSimilarProductsList.size() == 0) {
            hideSimilarProductsView();
        } else {
            setAdapter(mSimilarProductsList);
        }
    }

    /**
     * Called in response to {@link DataHandler#requestSingleProduct(String)}
     *
     * @param productList list of {@link SingleProductInfo} objects
     */


    @Override
    public void onSingleProductResponse(List<SingleProductInfo> productList) {
        if (productList == null || productList.isEmpty()) return;

        SingleProductInfo product = productList.get(0);
        mProduct.setBrandName(product.getBrandName());
        mProduct.setProductName(product.getProductName());

        TextView name = (TextView) findViewById(R.id.product_title);
        TextView brand = (TextView) findViewById(R.id.product_brand);
        name.setText(mProduct.getProductName());
        brand.setText(mProduct.getBrandName());
    }

    private void initRecyclerViewRecommendProducts() {
        mRecyclerViewRecommendProducts = (RecyclerView) findViewById(R.id.recycler_view_recommend_products);
        mRecyclerViewRecommendProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewRecommendProducts.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnItemClickListener()));
    }

    /**
     * initialize colors {@link RecyclerView}
     */


    private void initRecyclerViewStyle() {
        mRecyclerViewStyles = (RecyclerView) findViewById(R.id.recycler_view_styles);
        showRecyclerViewStyles();
        mRecyclerViewStyles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewStyles.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnStylesItemClickListener()));
    }

    /**
     * initialize product size {@link RecyclerView}
     */

    private void initRecyclerViewSize() {
        mRecyclerViewProductSize = (RecyclerView) findViewById(R.id.recycler_view_size);
        mRecyclerViewProductSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewProductSize.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnProductSizeItemClickListener()));
    }

    /**
     * initialize colors view
     */

    private void initStylesView() {
        mStylesLayout.setVisibility(View.VISIBLE);
        initRecyclerViewStyle();
        displayStyles();
    }

    /**
     * initialize product size view
     */

    private void initSizeView() {
        initRecyclerViewSize();
        displaySize();
    }

    private void initTitle() {
        TextView name = (TextView) findViewById(R.id.product_title);
        TextView brand = (TextView) findViewById(R.id.product_brand);
        TextView price = (TextView) findViewById(R.id.product_price);
        TextView priceOriginal = (TextView) findViewById(R.id.product_price_original);
        TextView percentOff = (TextView) findViewById(R.id.product_price_percentage_off);

        name.setText(mProduct.getProductName());
        brand.setText(mProduct.getBrandName());
        price.setText(mProduct.getPrice());

        if (mProduct.getPercentOff().equals(Constants.TEXT_ZERO_PERCENT)) {
            percentOff.setVisibility(View.INVISIBLE);
            priceOriginal.setVisibility(View.INVISIBLE);
        } else {
            percentOff.setVisibility(View.VISIBLE);
            percentOff.setText(mProduct.getPercentOff() + Constants.TEXT_OFF);
            priceOriginal.setText(mProduct.getOriginalPrice());
            priceOriginal.setPaintFlags(priceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceOriginal.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Animate cart menu when item is added to cart
     */

    private void animateCartMenu() {
        cartItemCount += 1;
        if (mCartBadgeText.getVisibility() == View.INVISIBLE ||
                mCartBadgeText.getVisibility() == View.GONE) {
            mCartBadgeText.setText(String.valueOf(cartItemCount));
        }
        mCartBadgeText.setVisibility(View.VISIBLE);
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.setInterpolator(new BounceInterpolator());
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(mCartBadgeText, "scaleY", 1, 1.75f, 1),
                ObjectAnimator.ofFloat(mCartBadgeText, "scaleX", 1, 1.75f, 1)
        );
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCartBadgeText.setText(String.valueOf(cartItemCount));
                Utils.showLongToast(mContext, mContext.getResources().
                        getString(R.string.item_added_to_cart));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimatorSet.start();
    }

    /**
     * make color list adapter. Use type 'PAIR' images for each style(color).
     * Displays current color as first item in color list for user convenience.
     */
    private void displayStyles() {
        mStylePairImageList.clear();
        mSelectedStylePos = 0;
        Iterator it = mStylesImages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String style = (String) pair.getKey();
            List<ImageInfo> styleImageInfo = mStylesImages.get(style);
            for (ImageInfo image : styleImageInfo) {
                if (image.getType().equals(Constants.TYPE_PAIR)) {
                    if (style.equals(mSelectedStyleId)) {
                        mStylePairImageList.add(0, image);
                    } else {
                        mStylePairImageList.add(image);
                    }

                }
            }
        }

        mStyleAdapter = new StylesListAdapter(mStylePairImageList, mContext, mSelectedStylePos);
        mRecyclerViewStyles.setAdapter(mStyleAdapter);
    }

    private void displaySize() {
        mSelectedSizePos = -1;
        ArrayList<String> sizes = new ArrayList<String>();
        sizes.add("S");
        sizes.add("M");
        sizes.add("L");
        sizes.add("XL");
        sizes.add("XXL");
        sizes.add("XXXL");
        SizeListAdapter sizeAdapter = new SizeListAdapter(sizes, mContext, mSelectedStylePos);
        mRecyclerViewProductSize.setAdapter(sizeAdapter);
    }

    /**
     * callback providing error information when data fetch request fails.
     *
     * @param t throwable holding error info
     * @see DataHandler#requestImages4Product(String)
     * @see DataHandler#requestSimilarProduct(String)
     * @see DataHandler#requestSingleProduct(String)
     */

    @Override
    public void onRequestFailure(Throwable t) {

    }


    public void setAdapter(List<ProductInfo> products) {
        showRecyclerViewRecommendProducts();
        mSimilarProductAdapter = new ProductListAdapter(products, mContext, true);
        mRecyclerViewRecommendProducts.setAdapter(mSimilarProductAdapter);
    }

    /**
     * Configure {@link CustomViewPager} to display product images.
     */
    private void startStyleImageSlideShow() {
        if (mViewPager == null) return;
        mCustomPagerAdapter = new CustomPagerAdapter(mContext, mSelectedStyleImages);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_menu, menu);

        mMenu = menu;

        mCartBadgeLayout = menu.findItem(R.id.action_cart).getActionView();
        mCartBadgeText = (TextView) mCartBadgeLayout.findViewById(R.id.badge_textView);
        mCartBadgeText.setVisibility(View.GONE); // initially hidden

        mCartMenuButton = (ImageView) mCartBadgeLayout.findViewById(R.id.badge_icon_button);
        mCartMenuButton.setScaleType(ImageView.ScaleType.CENTER);
        mCartMenuButton.setMinimumWidth(Utils.dpToPx(mContext, 48));
        mCartMenuButton.setMinimumHeight(Utils.dpToPx(mContext, 48));
        mCartMenuButton.setMaxWidth(Utils.dpToPx(mContext, 48));
        mCartMenuButton.setMaxHeight(Utils.dpToPx(mContext, 48));

        mCartMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void hideSimilarProductsView() {
        if (mSimilarProductsLayout != null) {
            mSimilarProductsLayout.setVisibility(View.GONE);
        }
    }

    public void showRecyclerViewRecommendProducts() {
        if (mSimilarProductsProgress != null) {
            mSimilarProductsProgress.setVisibility(View.GONE);
        }

        if (mRecyclerViewRecommendProducts != null) {
            mRecyclerViewRecommendProducts.setVisibility(View.VISIBLE);
        }
    }

    public void showRecyclerViewStyles() {
        if (mRecyclerViewStyles != null) {
            mRecyclerViewStyles.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        switch (itemId) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * Share product url other users.
     * <p>
     * URL form: "http://www.zappos.com/Product?productId=PID&styleId=SID
     * <p>
     * This demo app listens to host: www.zappos.com. Whenever user clicks on zappos product link,
     * it will be open with this demo app instead of Browser.
     */

    public void shareProduct() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));

            String url = "http://www.zappos.com/Product?productId=" + mProduct.getProductId() +
                    "&styleId=" + mSelectedStyleId;

            i.putExtra(Intent.EXTRA_TEXT, url);
            mContext.startActivity(Intent.createChooser(i, "Share via"));
        } catch (Exception e) {
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHandler.getInstance().setProductDetailsListener(null);

        // making static objects null to avoid memory leaks.
        selectedStylesRowLayout = null;
        mFABProgressCircle = null;
    }

    /**
     * Click listener for {@link RecyclerView} items.
     */

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        /**
         * Called when an item is clicked.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        @Override
        public void onItemClick(View childView, int position) {
        }

        /**
         * Called when an item is long pressed.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        @Override
        public void onItemLongPress(View childView, int position) {
            super.onItemLongPress(childView, position);

            if (Utils.isGridView()) return;

            //startProductImageSlideShow(childView, position);
        }
    }

    private class OnProductSizeItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        /**
         * Called when an item is clicked.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        @Override
        public void onItemClick(View childView, int position) {
            if (mSelectedSizeLayout != null) {
                mSelectedSizeLayout.setSelected(false);
            }
            mSelectedSizeLayout = (TextView) childView.findViewById(R.id.size_textView);
            if (mSelectedSizeLayout != null) {
                mSelectedSizeLayout.setSelected(true);
            }
            mSelectedSizePos = position;
        }

        /**
         * Called when an item is long pressed.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        @Override
        public void onItemLongPress(View childView, int position) {
            super.onItemLongPress(childView, position);

            if (Utils.isGridView()) return;

            //startProductImageSlideShow(childView, position);
        }
    }

    private class OnStylesItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        /**
         * Called when an item is clicked.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        @Override
        public void onItemClick(View childView, int position) {
            if (selectedStylesRowLayout != null) {
                selectedStylesRowLayout.setSelected(false);
            }
            selectedStylesRowLayout = (RelativeLayout) childView.findViewById(R.id.row);
            if (selectedStylesRowLayout != null) {
                selectedStylesRowLayout.setSelected(true);
            }

            mSelectedStylePos = position;
            mStyleAdapter.setSelectedStylePos(mSelectedStylePos);
            mSelectedStyleId = mStylePairImageList.get(position).getStyleId();
            mSelectedStyleImages = mStylesImages.get(mSelectedStyleId);
            startStyleImageSlideShow();
        }

        /**
         * Called when an item is long pressed.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        @Override
        public void onItemLongPress(View childView, int position) {
            super.onItemLongPress(childView, position);

            if (Utils.isGridView()) return;

            //startProductImageSlideShow(childView, position);
        }
    }
}

package com.zappos.android.app.prototype.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.activity.HomeActivity;
import com.zappos.android.app.prototype.adapter.CustomPagerAdapter;
import com.zappos.android.app.prototype.adapter.ProductListAdapter;
import com.zappos.android.app.prototype.listeners.RecyclerViewScrollListener;
import com.zappos.android.app.prototype.models.ProductInfo;
import com.zappos.android.app.prototype.data.DataHandler;
import com.zappos.android.app.prototype.utils.Utils;
import com.zappos.android.app.prototype.views.CustomViewPager;
import com.zappos.android.app.prototype.listeners.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;

/**
 * Created by arjun on 2/1/17.
 *
 * {@link Fragment} holding {@link RecyclerView}, error view and loading view.*
 */
public class ProductListFragment extends Fragment implements HomeActivity.FragmentNotifier,
        DataHandler.DataListener, SearchView.OnQueryTextListener, RecyclerViewScrollListener.ScrollPositionListener {
    private static final String TAG = ProductListFragment.class.getSimpleName();

    private static final int MSG_ADD_TO_CART = 100;
    public static int cartItemCount = 0;
    public static int currentSliderPos = -1;
    private int mGlobalLayoutWidth;
    private static String searchTerm = "Adidas";

    private View mRoot = null;


    private ProductListFragment mThisFragment;
    public static boolean isStarting = false;
    private Context mContext;

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public View mCartBadgeLayout;
    public TextView mCartBadgeText;
    public ImageView mCartMenuButton;

    private ProgressBar mProgressBar;
    private LinearLayout mErrorLayout;
    private Button mBtnRetry;
    private TextView mTextError;

    private LinearLayout mListPosLayout;
    private TextView mListPosTextSearchTerm;
    private TextView mListPosTextPosition;
    private TextView mListPosTextItemCount;


    public FABProgressCircle mFABProgressCircle;

    private ProductListAdapter mProductAdapter;
    private CustomPagerAdapter mCustomPagerAdapter;
    private CustomViewPager mViewPager;
    private SearchView search;
    private Menu mMenu;
    List<ProductInfo> mProductInfoList;

    private static SliderLayout slider = null;




    public ProductListFragment() {
        mThisFragment = this;
    }

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isStarting = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setupView(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData4term(searchTerm);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).registerFragmentNotifier(this);
        DataHandler.getInstance().setDataListener(this);

        if (isStarting) {
            isStarting = false;
        } else {
            updateListView();
        }
    }

    /**
     * Initialize and setup views of this fragment
     */
    private View setupView(LayoutInflater inflater) {
        mRoot = inflater.inflate(R.layout.fragment_product_list, null);

        getActivity().getWindow().getDecorView().getViewTreeObserver().
                addOnGlobalLayoutListener(mGlobalLayoutListener);

        mProgressBar = (ProgressBar) mRoot.findViewById(R.id.main_progress);
        mErrorLayout = (LinearLayout) mRoot.findViewById(R.id.error_layout);
        mBtnRetry = (Button) mRoot.findViewById(R.id.error_btn_retry);
        mTextError = (TextView) mRoot.findViewById(R.id.error_txt_cause);

        mListPosLayout = (LinearLayout) mRoot.findViewById(R.id.floating_list_position);
        mListPosTextSearchTerm = (TextView) mRoot.findViewById(R.id.text_search_term);
        mListPosTextPosition = (TextView) mRoot.findViewById(R.id.text_list_position);
        mListPosTextItemCount = (TextView) mRoot.findViewById(R.id.text_total_items);

        mListPosLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRecyclerView != null) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
        });

        mBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData4term(searchTerm);
            }
        });

        if (mRecyclerView == null) {
            initListView(mRoot);
        }

        return mRoot;
    }

    /**
     * initialize {@link RecyclerView}
     *
     * @param root parent of {@link RecyclerView}
     */
    private void initListView(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,
                Utils.getNoOfColumns(mContext, mProductAdapter)));
        mRecyclerView.setOnScrollListener(new RecyclerViewScrollListener(this));
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
//                new OnItemClickListener()));
    }

    /**
     * request {@link DataHandler} to fetch search results for specific search term
     *
     * @param term search term
     */
    public void requestData4term(String term) {
        showLoadingView();
        getActivity().setTitle(term);
        DataHandler.getInstance().requestData4Term(term);
    }

    public void setAdapter(List<ProductInfo> products) {
        showRecyclerView();
        mProductAdapter = new ProductListAdapter(products, mContext);
        mRecyclerView.setAdapter(mProductAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchTerm = s;
        requestData4term(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return true;
    }


    /**
     * callback providing response for {@link #requestData4term(String)}
     *
     * @param productList list of ProductInfo objects.
     */
    @Override
    public void onDataResponse(List<ProductInfo> productList) {
        mProductInfoList = productList;
        if (mProductInfoList == null || mProductInfoList.size() == 0) {
            showErrorView(null);
        } else {
            setAdapter(mProductInfoList);
        }
    }

    /**
     * callback providing error information when {@link #requestData4term(String)} fails.
     *
     * @param t throwable holding error info
     */

    @Override
    public void onRequestFailure(Throwable t) {
        showErrorView(t);
    }

    /**
     * onClick() notifier for {@link FABProgressCircle}.
     *
     * @param fabProgressCircle view object for {@link FABProgressCircle}.
     */
    @Override
    public void notifyOnFABClick(FABProgressCircle fabProgressCircle) {

        if (!Utils.isNetworkConnected(mContext)) {
            Utils.showShortToast(mContext, mContext.getResources().
                    getString(R.string.error_msg_no_internet));
            return;
        }

        mFABProgressCircle = fabProgressCircle;
        mFABProgressCircle.show();
        mFABProgressCircle.beginFinalAnimation();

        mFABProgressCircle.attachListener(new FABProgressListener() {
            @Override
            public void onFABProgressAnimationEnd() {
                animateCartMenu();
            }
        });

//        if (!mHandler.hasMessages(MSG_ADD_TO_CART))
//            mHandler.sendEmptyMessageDelayed(MSG_ADD_TO_CART, 0);
    }

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
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                mCartBadgeText.setText(String.valueOf(cartItemCount));
                Utils.showLongToast(mContext, mContext.getResources().
                        getString(R.string.item_added_to_cart));
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        mAnimatorSet.start();
    }

    /**
     * onBackPressed() notifier from {@link HomeActivity}
     *
     * @return true if search view is iconified false otherwise
     */

    @Override
    public boolean notifyOnBackPressed() {
        if (search != null && !search.isIconified()) {
            search.setQuery("", false);
            search.setIconified(true);
            return false;
        }
        return true;
    }

    /**
     * Called from {@link RecyclerViewScrollListener} on list scroll
     * If position > 10, show floating view to inform user about current scroll position
     *
     * @param position position for last item in list
     */

    @Override
    public void onScrolledPosition(int position) {
        if (position > 10) {
            mListPosTextSearchTerm.setText(searchTerm);
            if (position == mProductInfoList.size() - 1)
                position = mProductInfoList.size();

            mListPosTextPosition.setText(position + "");
            mListPosTextItemCount.setText("/" + mProductInfoList.size());

            if (mListPosLayout.getVisibility() != View.VISIBLE) {
                mListPosLayout.setVisibility(View.VISIBLE);
                mListPosLayout.animate().alpha(1.0f).setDuration(1000).
                        setInterpolator(new LinearInterpolator()).start();
            }
        } else {
            mListPosLayout.animate().alpha(0.0f).setDuration(1000).
                    setInterpolator(new LinearInterpolator()).start();
            mListPosLayout.setVisibility(View.GONE);
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_ADD_TO_CART) {
                mFABProgressCircle.beginFinalAnimation();
            }
        }
    };
    /**
     * Global layout change listener to handle view change during Keyboard show and hide.
     */
    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int width = -1;

            try {
                width = getActivity().getWindow().getDecorView().getWidth();
            } catch (Exception e) {
                // called too early. so, just skip.
            }

            if (width != -1) {
                if (mGlobalLayoutWidth != width) {
                    mGlobalLayoutWidth = width;

                    if (mRecyclerView != null) {
                        updateEmptyViewWidth();
                    }
                }
            }
        }
    };

    private void updateEmptyViewWidth() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);

        if (Utils.isTablet(mContext)) {
            menu.findItem(R.id.action_view_as).setVisible(false);
        } else {
            menu.findItem(R.id.action_view_as).setVisible(true);
        }

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

        init_search_menu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        switch (itemId) {
            case R.id.action_view_as:
                toggleListViewType();
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * Called on device rotation. Update {@link RecyclerView} UI for seamless user experience
     * and make best use of available space.
     *
     * @param newConfig Object representing config info
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateListView();
    }

    private void init_search_menu(Menu menu) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);

            search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setQueryHint(getActivity().getResources().getString(R.string.search_hint));
            search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
            search.setOnQueryTextListener(this);

        }
    }

    /**
     * Update {@link RecyclerView} UI for seamless user experience and make best use of
     * available space. Also maintains on-screen item position during UI update.
     */
    private void updateListView() {
        GridLayoutManager layoutManager = ((GridLayoutManager) mRecyclerView.getLayoutManager());
        int scrollPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (scrollPosition == -1)
            scrollPosition = layoutManager.findFirstVisibleItemPosition();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,
                Utils.getNoOfColumns(mContext, mProductAdapter)));
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mProductAdapter);
    }

    /**
     * @param throwable required for fetching appropriate error
     *
     * @see Utils#fetchErrorMessage(Context, Throwable)
     *
     * @return
     */
    public void showErrorView(Throwable throwable) {
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }

        if (mErrorLayout.getVisibility() == View.GONE) {
            mErrorLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mBtnRetry.setVisibility(throwable == null ? View.GONE : View.VISIBLE);
            mTextError.setText(Utils.fetchErrorMessage(mContext, throwable));
        }
    }

    public void showLoadingView() {
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(View.GONE);
        }

        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void showRecyclerView() {
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(View.GONE);
        }

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * toggle between two view modes, Grid View & Large View.
     */
    private void toggleListViewType() {
        if (Utils.isGridView()) {
            Utils.setCurrentViewType(HomeActivity.LIST_VIEW);
            mMenu.findItem(R.id.action_view_as).setIcon(mContext.getResources().getDrawable(R.drawable.ic_menu_grid));
        } else {
            Utils.setCurrentViewType(HomeActivity.GRID_VIEW);
            mMenu.findItem(R.id.action_view_as).setIcon(mContext.getResources().getDrawable(R.drawable.ic_menu_list));
        }
        updateListView();
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

    private void startProductImageSlideShow(View childView, int position) {
        mViewPager = (CustomViewPager) childView.findViewById(R.id.view_pager_slide_show);
        Log.d("Zappos", "onItemLongPress() - position: " + position);
        if (mViewPager == null) return;

        if (mViewPager.getVisibility() == View.VISIBLE) {
            mViewPager.setVisibility(View.GONE);
            mViewPager = null;
            return;
        }

        mViewPager.setVisibility(View.VISIBLE);
        ArrayList<String> img_urls = new ArrayList<String>();
        String uri = Utils.getLargeThumbUrl(mProductInfoList.
                get(position).getThumbnailImageUrl());
        img_urls.add(uri);
        img_urls.add(uri);
        img_urls.add(uri);
        img_urls.add(uri);
        img_urls.add(uri);

        mCustomPagerAdapter = new CustomPagerAdapter(mContext, img_urls);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((HomeActivity) getActivity()).unRegisterFragmentNotifier();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

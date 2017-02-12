package com.zappos.android.app.prototype.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.fragments.ProductListFragment;
import com.zappos.android.app.prototype.utils.Constants;
import com.zappos.android.app.prototype.utils.Preference;
import com.zappos.android.app.prototype.utils.Utils;

import butterknife.ButterKnife;

/**
 * Created by arjun on 2/1/17.
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GRID_VIEW = 100;
    public static final int LIST_VIEW = 101;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Context mContext;

    private ProductListFragment mProductListFragment;
    private FragmentNotifier mFragmentNotifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_home);
        ButterKnife.bind(this);

        mContext = this;

        if (Constants.API_KEY.isEmpty()) {
            Utils.showLongToast(mContext, mContext.getResources().
                    getString(R.string.key_missing_msg));
            return;
        }

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * initialize toolbar, navigation drawer and content fragment.
     */
    public void initView() {
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProductListFragment = ProductListFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content,
                mProductListFragment).commitAllowingStateLoss();

        setupDrawer(toolbar);

        int viewType = Preference.getInstance(mContext).loadIntKey(Preference.VIEW_TYPE, GRID_VIEW);
        Utils.setCurrentViewType(viewType);
    }

    private void setupDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mFragmentNotifier == null || mFragmentNotifier.notifyOnBackPressed()) {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Send touch events to {@link com.daimajia.slider.library.SliderLayout} to enable
     * swipe gesture for image slide show in Large list view mode. Also send events to
     * framework to keep {@link android.support.v7.widget.RecyclerView} scrolling behaviour intact.
     *
     * @param ev touch events from framework side
     * @return return value from callback to framework
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (Utils.isListView() && mRecyclerView != null && slider != null) {
//            View childView = mRecyclerView.findChildViewUnder(ev.getX(), ev.getY());
//            int childViewPosition = mRecyclerView.getChildPosition(childView);
//            if (currentSliderPos == childViewPosition) {
//                return slider.dispatchTouchEvent(ev);
//            }
//        }
//        return mImageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @param listener lister to notify {@link ProductListFragment} about click events on
     *                 {@link FloatingActionButton}
     */

    public void registerFragmentNotifier(FragmentNotifier listener) {
        mFragmentNotifier = listener;
    }

    public void unRegisterFragmentNotifier() {
        mFragmentNotifier = null;
    }

    public interface FragmentNotifier {

        /**
         * Notify {@link ProductListFragment} when {@link FABProgressCircle} is clicked.
         *
         * @param fabProgressCircle view object for {@link FABProgressCircle}.
         */
        void notifyOnFABClick(FABProgressCircle fabProgressCircle);


        /**
         * Notify {@link ProductListFragment} when {@link #onBackPressed()} is called.
         */

        boolean notifyOnBackPressed();
    }
}

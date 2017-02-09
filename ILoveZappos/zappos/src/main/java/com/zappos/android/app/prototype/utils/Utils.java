package com.zappos.android.app.prototype.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.zappos.android.app.prototype.R;
import com.zappos.android.app.prototype.activity.HomeActivity;
import com.zappos.android.app.prototype.adapter.ProductListAdapter;

import java.util.concurrent.TimeoutException;

/**
 * Created by arjun on 2/1/17.
 *
 * Utility class for performing application level common operations.
 *
 */
public class Utils {

    private static int currentViewType = HomeActivity.GRID_VIEW;

    private static Typeface fontFace;

    public static void setCurrentViewType(int viewType) {
        currentViewType = viewType;
    }

    public static boolean isGridView() {
        return currentViewType == HomeActivity.GRID_VIEW;
    }

    public static boolean isListView() {
        return currentViewType == HomeActivity.LIST_VIEW;
    }

    /**
     * Provides value for number of columns to be used for
     * {@link android.support.v7.widget.RecyclerView}
     *
     * @param context context
     * @param adapter list adapter
     *
     * @return number of columns based on device type and orientation.
     */
    public static int getNoOfColumns(Context context, ProductListAdapter adapter) {
        if (isGridView()) {
            return context.getResources().getInteger(R.integer.grid_view_column_count);
            //return calculateColumns(context, adapter);
        } else {
            return context.getResources().getInteger(R.integer.large_view_column_count);
        }
    }

    /**
     * Calculate number of columns dynamically, considering list item width and screen size.
     *
     * @param context context
     * @param adapter list adapter
     *
     * @return number of columns
     */
    private static int calculateColumns(Context context, ProductListAdapter adapter) {
        int n = 2;
        if (!isLandscape(context)) {
            if (isPhone(context)) return n;
            else return context.getResources().getInteger(
                        R.integer.large_view_column_count);
        }
        if (isTablet(context)) return context.getResources().getInteger(
                    R.integer.large_view_column_count);

        if (isLandscape(context)) n = 3;

        int screenWidth = getScreenWidth(context);
        int screenHeight = getScreenHeight(context);

        int columnWidth = 0;
        double ratio;

        try {
            if (adapter != null) columnWidth = adapter.getColumnWidth();

            if (columnWidth > 0) {
                ratio = ((double) screenWidth / (double) columnWidth);
            } else { // if app launched in landscape
                columnWidth = screenHeight / 2;
                ratio = ((double) screenWidth / (double) columnWidth);
            }
            n = decideColumns(ratio);
        } catch (Exception e) {
            // called too early. so, just skip.
        }
        return n;
    }

    private static int decideColumns(double ratio) {
        // TODO Auto-generated method stub
        if (ratio > 2.5 && ratio < 3.5) {
            return 3;
        } else if (ratio >= 3.5 && ratio < 4.5) {
            return 4;
        } else if (ratio >= 4.5 && ratio < 5.5) {
            return 5;
        } else {
            return 4;
        }
    }

    /**
     * Make url for high resolution product thumbnail. This api simple replace term 't-THUMBNAIL'
     * in image url with 'p-4x'.
     *
     * @param smallThumbUrl url for small thumbnail
     *
     * @return url for large thumbnail
     */
    public static String getLargeThumbUrl(String smallThumbUrl) {
        if (smallThumbUrl.contains(Constants.THUMB_URL_SUFFIX_THUMBNAIL)) {
            return smallThumbUrl.replace(Constants.THUMB_URL_SUFFIX_THUMBNAIL,
                    Constants.THUMB_URL_SUFFIX_4X);
        }
        return null;
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    public static String fetchErrorMessage(Context context, Throwable throwable) {
        String errorMsg = context.getResources().getString(R.string.error_msg_no_data);

        if (!isNetworkConnected(context)) {
            errorMsg = context.getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable != null && throwable instanceof TimeoutException) {
            errorMsg = context.getResources().getString(R.string.error_msg_timeout);
        }
        return errorMsg;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * API to decide device type, based on android OS resource selection functionality
     * @param context
     *
     * @return retrieved device type {@link String} from appropriate values/string.xml
     */
    public static String getDeviceType(Context context) {
        return context.getResources().getString(R.string.device_type);

    }

    public static boolean isTablet(Context context) {

        return getDeviceType(context).equalsIgnoreCase(Constants.DEVICE_TYPE_TABLET_7_INCH)
                || getDeviceType(context).equalsIgnoreCase(Constants.DEVICE_TYPE_TABLET_10_INCH);

    }

    public static boolean isPhone(Context context) {

        if (getDeviceType(context).equalsIgnoreCase(Constants.DEVICE_TYPE_PHONE)) {
            return true;
        }
        return false;

    }

    public static boolean is10InchTablet(Context context) {

        if (getDeviceType(context).equalsIgnoreCase(Constants.DEVICE_TYPE_TABLET_10_INCH)) {
            return true;
        }
        return false;

    }

    public static boolean is7InchTablet(Context context) {

        if (getDeviceType(context).equalsIgnoreCase(Constants.DEVICE_TYPE_TABLET_7_INCH)) {
            return true;
        }
        return false;
    }

    public static int dpToPx(Context context, long dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int pxToDp(Context context, long px) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) px / density);
    }

    public static void loadTypeFace(Context context, String fontName) {
        fontFace = Typeface.createFromAsset(context.getAssets(), fontName);
    }

    public static Typeface getTypeFace() {
        return fontFace;
    }

    public static void showShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortSnack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongSnack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}

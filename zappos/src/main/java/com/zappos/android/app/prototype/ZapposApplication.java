package com.zappos.android.app.prototype;

import android.app.Application;
import android.content.Context;

import com.zappos.android.app.prototype.binding.CustomBindingAdapter;
import com.zappos.android.app.prototype.utils.Utils;

/**
 * Created by arjun on 2/1/17.
 */


public class ZapposApplication extends Application {

    private static ZapposApplication sInstance = null;
    private CustomBindingAdapter mCustomBindingAdapter = null;

    public static ZapposApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static CustomBindingAdapter getCustomBindingAdapter() {
        return sInstance.mCustomBindingAdapter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mCustomBindingAdapter = new CustomBindingAdapter();
        Utils.loadTypeFace(this, "Fenix-Regular.ttf");
    }
}
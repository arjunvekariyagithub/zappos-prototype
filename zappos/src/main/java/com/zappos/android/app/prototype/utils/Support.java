package com.zappos.android.app.prototype.utils;

import android.os.Build;

/**
 * Created by arjun on 2/1/17.
 * <p>
 * Support class to manages device and OS specific functionality
 */
public class Support {

    public static final int API_VERSION = Build.VERSION.SDK_INT;

    public static final boolean IS_FROYO = API_VERSION == 8;
    public static final boolean IS_GINGER_BREAD = API_VERSION == 9 || API_VERSION == 10;
    public static final boolean IS_HONEY_COMB = API_VERSION == 11 || API_VERSION == 12 || API_VERSION == 13;
    public static final boolean IS_ICS = API_VERSION == 14 || API_VERSION == 15;
    public static final boolean IS_JB = API_VERSION == 16 || API_VERSION == 17 || API_VERSION == 18;
    public static final boolean IS_KITKAT = API_VERSION == 19 || API_VERSION == 20;
    public static final boolean IS_LOLIPOP = API_VERSION >= 21;
}

package com.zappos.android.app.prototype.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by arjun on 2/3/17.
 * <p>
 * Manages to load and save the states; view type, etc...
 */
public class Preference {
    public static final String IS_TABLET = "is_tablet";
    public static final String VIEW_TYPE = "view_type";
    private static final String TAG = "Preference";
    private static final String APPPREFS = "ZapposSharedPreferences";
    private static Context mContext;
    private static Preference mUniqueInstance = null;
    private int mode = Context.MODE_PRIVATE;

    private Preference() {
    }

    public static Preference getInstance(Context context) {
        if (mUniqueInstance == null)
            mUniqueInstance = new Preference();

        mContext = context;
        return mUniqueInstance;
    }


    /**
     * save {@link Integer} value
     *
     * @param keyString key to save value for
     * @param key       updated value
     */
    public void saveState(String keyString, int key) {
        try {

            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(keyString);
            editor.putInt(keyString, key);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } catch (OutOfMemoryError oome) {
            Log.e(TAG, "saveState" + oome.toString());
        }
    }

    /**
     * save {@link String} value
     *
     * @param keyString key to save value for
     * @param key       updated value
     */
    public void saveState(String keyString, String key) {
        try {
            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(keyString);
            editor.putString(keyString, key);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } catch (OutOfMemoryError oome) {
            Log.e(TAG, "saveState" + oome.toString());
        }
    }

    /**
     * save {@link Long} value
     *
     * @param keyString key to save value for
     * @param key       updated value
     */
    public void saveState(String keyString, long key) {
        try {

            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(keyString);
            editor.putLong(keyString, key);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } catch (OutOfMemoryError oome) {
            Log.e(TAG, "saveState" + oome.toString());
        }

    }

    /**
     * save {@link Boolean} value
     *
     * @param keyString key to save value for
     * @param key       updated value
     */
    public void saveState(String keyString, boolean key) {
        try {

            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(keyString);
            editor.putBoolean(keyString, key);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } catch (OutOfMemoryError oome) {
            Log.e(TAG, "saveState" + oome.toString());
        }

    }

    /**
     * load value for given key
     *
     * @param keyString key to load value for
     * @return value associated with given key
     */
    public String loadStringKey(String keyString) {
        String key = "";
        try {
            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            key = pref.getString(keyString, "");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return key;
    }

    /**
     * load {@link Integer} value.
     *
     * @param keyString   key to load value for
     * @param returnValue default return value if key does not found
     * @return value associated with given key
     */
    public int loadIntKey(String keyString, int returnValue) {
        int key = 0;
        try {
            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            key = pref.getInt(keyString, returnValue);
        } catch (Exception e) {
            key = returnValue;
            Log.e(TAG, e.toString());
        }
        return key;
    }

    /**
     * load {@link Long} value.
     *
     * @param keyString   key to load value for
     * @param returnValue default return value if key does not found
     * @return value associated with given key
     */
    public long loadLongKey(String keyString, long returnValue) {
        try {
            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            returnValue = pref.getLong(keyString, returnValue);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return returnValue;
    }

    /**
     * load {@link Boolean} value.
     *
     * @param keyString   key to load value for
     * @param returnValue default return value if key does not found
     * @return value associated with given key
     */
    public boolean loadBooleanKey(String keyString, boolean returnValue) {
        try {
            SharedPreferences pref = mContext.getSharedPreferences(APPPREFS, mode);
            returnValue = pref.getBoolean(keyString, returnValue);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return returnValue;
    }

    /**
     * load {@link String} value.
     *
     * @param keyString   key to load value for
     * @param returnValue default return value if key does not found
     * @return value associated with given key
     */
    public String loadSettingsStringKey(String keyString, String returnValue) {
        try {
            SharedPreferences pref = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(mContext);
            returnValue = pref.getString(keyString, returnValue);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return returnValue;
    }
}

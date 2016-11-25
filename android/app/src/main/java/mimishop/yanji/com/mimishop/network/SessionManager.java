package mimishop.yanji.com.mimishop.network;

import android.content.Context;

import java.util.HashMap;

import mimishop.yanji.com.mimishop.Preference;
import mimishop.yanji.com.mimishop.constant.Common;

/**
 * Created by KCJ on 3/24/2015.
 */
public class SessionManager {
    private static SessionManager appInstance;

    public static final String HEADER_APP_KEY   = "app-key";
    public static final String HEADER_DEVICE_ID   = "device-id";
    public static final String HEADER_DEVICE_MODAL   = "device-model";
    public static final String HEADER_OS_TYPE   = "os-type";
    public static final String HEADER_OS_VERSION   = "os-version";
    public static final String HEADER_APP_VERSION   = "app-version";
    public static final String HEADER_APP_MARKET   = "app-market";

    public static SessionManager getInstance() {
        if (appInstance == null) {
            appInstance = new SessionManager();

        }
        return appInstance;
    }

    public  void initHeader(HashMap<String, String> headers) {
        if(Common.mKey != null) {
            headers.put(HEADER_APP_KEY, Common.mKey);
        }

        headers.put(HEADER_DEVICE_ID, Common.mDeviceId);
        headers.put(HEADER_DEVICE_MODAL, Common.mDeviceModel);
        headers.put(HEADER_OS_TYPE, Common.mOsType);
        headers.put(HEADER_OS_VERSION, Common.mOsVersion);
        headers.put(HEADER_APP_VERSION, Common.mAppVersion);
        headers.put(HEADER_APP_MARKET, Common.mMarket);
    }

    public void saveHeader(Context context) {
        Preference pref = Preference.getInstance();
        pref.putSharedPreference(context, HEADER_APP_KEY, Common.mKey);
        pref.putSharedPreference(context, HEADER_DEVICE_ID, Common.mDeviceId);
        pref.putSharedPreference(context, HEADER_DEVICE_MODAL, Common.mDeviceModel);
        pref.putSharedPreference(context, HEADER_OS_TYPE, Common.mOsType);
        pref.putSharedPreference(context, HEADER_OS_VERSION, Common.mOsVersion);
        pref.putSharedPreference(context, HEADER_APP_VERSION, Common.mAppVersion);
        pref.putSharedPreference(context, HEADER_APP_MARKET, Common.mMarket);
    }

    public void restoreHeader(Context context) {
        Preference pref = Preference.getInstance();
        pref.getSharedPreference(context, Common.mKey, HEADER_APP_KEY);
        pref.getSharedPreference(context, Common.mDeviceId, HEADER_DEVICE_ID);
        pref.getSharedPreference(context, Common.mDeviceModel, HEADER_DEVICE_MODAL);
        pref.getSharedPreference(context, Common.mOsType, HEADER_OS_TYPE);
        pref.getSharedPreference(context, Common.mOsVersion, HEADER_OS_VERSION);
        pref.getSharedPreference(context, Common.mAppVersion, HEADER_APP_VERSION);
        pref.getSharedPreference(context, Common.mMarket, HEADER_APP_MARKET);

    }
}

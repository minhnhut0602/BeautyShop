package mimishop.yanji.com.mimishop.constant;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.BuildConfig;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.Location;
import mimishop.yanji.com.mimishop.modal.Tube;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 3/23/2015.
 */
public class Common {

    public static final int MAX_BANNER_CNT = 3;
    public static final String APP_PAGE_URL = "http://www.miggle.co.kr";
    public static String mKey = null;
    public static String mDeviceId = null;
    public static String mDeviceModel = Build.MODEL;
    public static String mOsType = "A";
    public static String mOsVersion = Build.VERSION.RELEASE;
    public static String mAppVersion = "1.0";
    public static String mMarket = "googlestore";
    public static final String CAPTURED_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mimishop/";
    public static int MY_SOCKET_TIMEOUT_MS = 10000; // 10s
    public static String CUR_VER = "1.0";
    public static boolean isDebug = BuildConfig.DEBUG;

    public static ArrayList<Image> mTempList = null;
    public static ArrayList<Location> mArrLocation = new ArrayList<Location>();
    public static ArrayList<Tube> mArrTube = new ArrayList<Tube>();
    public static FreeTalk mTempFreetalk = null;

    public static     double       CAST_BANNER_RATIO = (720.0f/240.0f);  // width/height
    public static     double       HOME_BANNER_RATIO = (720.0f/262.0f);  // width/height
    public static     double       LIFE_BANNER_RATIO = (720.0f/350.0f);  // width/height
    public static     double       LOGOUT_BANNER_RATIO = (720.0f/1127.0f);  // width/height
    public static     double       PUSH_BANNER_RATIO = (320.0f/180.0f);  // width/height

    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "319552300735";// Google API console 에서 얻은 Project ID 값을 설정

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "MimiShop";

    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            "mimishop.yanji.com.mimishop.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}

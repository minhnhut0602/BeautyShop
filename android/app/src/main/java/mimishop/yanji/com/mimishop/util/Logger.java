package mimishop.yanji.com.mimishop.util;

import mimishop.yanji.com.mimishop.constant.Common;

/**
 * Log class
 * <p/>
 * 배포 시 DEBUG = false 로 설정
 *
 * @author Kris
 */
public final class Logger {

    public static final boolean DEBUG = Common.isDebug;

    public static int v(String tag, String msg) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.v(tag, msg, tr);
    }

    public static int e(String tag, String msg) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.e(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.w(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.i(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.d(tag, msg, tr);
    }

    public static int s(String tag, String msg) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.d(tag, msg);
    }

    public static int s(String tag, String msg, Throwable tr) {
        if (!DEBUG) {
            return -1;
        }
        msg = tag + " - " + msg;
        // writeLog(tag, msg);
        return android.util.Log.d(tag, msg, tr);
    }


}
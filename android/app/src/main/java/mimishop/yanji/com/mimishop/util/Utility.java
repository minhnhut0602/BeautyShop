package mimishop.yanji.com.mimishop.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.view.TouchImageView;
import android.os.Vibrator;

/**
 * Created by KCJ on 3/24/2015.
 */
public class Utility {

    public static String TAG = "Utility";

    public static ProgressDialog g_dlgProgress = null;
    private static int g_nProgressCount = 0;
    private static  boolean g_isShowProgress = false;

    private File m_pCapturedFile = null;

    private static Utility appInstance;

    public static Utility getInstance() {
        if (appInstance == null) {
            appInstance = new Utility();

        }
        return appInstance;
    }

    public long getMilliSecond() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        return currentTime;
    }

    public String getRemainTime(long time) {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        long remainTime = currentTime - time*1000;

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        calendar.setTimeInMillis(remainTime);
        return formatter.format(calendar.getTime());
    }

    public String getTime(long secod) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        calendar.setTimeInMillis(secod*1000);
        return formatter.format(calendar.getTime());
    }

    public String getTime1(long millisecond) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        calendar.setTimeInMillis(millisecond);
        return formatter.format(calendar.getTime());
    }

    /*
    HH : 0 -23
    kk : 1 - 24
    KK : 0 -11
    hh : 1 - 12*/
    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        calendar.setTimeInMillis(currentTime);
        return formatter.format(calendar.getTime());
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTimeInMillis(currentTime);
        return formatter.format(calendar.getTime());
    }

    public static String getDateString(String time, String format) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format);
            Date dt1 = format1.parse(time);
            Calendar c = Calendar.getInstance();
            c.setTime(dt1);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(c.getTime());
        }
        catch (Exception e) {
            return  "";
        }
    }

    public String getDayOfWeek(Date date, boolean korean) {
        String[][] week = {{"일요일", "Sun"}, {"월요일", "Mon"}, {"화요일", "Tue"}, {"수요일", "Wen"}, {"목요일", "Thu"}, {"금요일", "Fri"}, {"토요일", "Sat"}};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(korean) {
            return week[cal.get(Calendar.DAY_OF_WEEK)-1][0];
            }
        else {
            return week[cal.get(Calendar.DAY_OF_WEEK) - 1][1];
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(String json, Type type) {
        try {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) parameterizedType.getRawType();
                if (rawType.getName().equals(List.class.getName())) {

                    // if the T is of List type
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    Class<?> actualType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    Method method = actualType.getMethod("create", JSONObject.class);
                    List<Object> list = ArrayList.class.newInstance();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        Object object = method.invoke(null, dataObject);
                        list.add(object);
                    }
                    return (T) list;
                }
            } else {
                Class<?> rawType = (Class<?>) type;
                JSONObject graphObject = new JSONObject(json);
                Method method = rawType.getMethod("create", JSONObject.class);
                Object object = method.invoke(null, graphObject);
                return (T) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getDeviceId(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getDeviceId();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String getImagePathToUri(Activity context, Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgPath;
    }

    public static void startURLActivity(Activity context, String strURL) {

        if (strURL == null) return;

        if (URLUtil.isValidUrl(strURL) == false) return;

        Uri url = Uri.parse(strURL);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(url);
        context.startActivity(i);
    }

    public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getLocation(Context context, final ArrayAdapter<?> adapter,
                            boolean refresh) {
        Log.d("getLocation", " init");
        if (!refresh) {
            if (AppController.getInstance().getLocation() != null) {
                return;
            }
        }
        final LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                AppController.getInstance().setLocation(location);
                if (adapter != null)
                    adapter.notifyDataSetChanged();
                locationManager.removeUpdates(this);
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public static float getDisance(Shop shop, LatLng center) {
        Location cur_location = new Location("jy");
        cur_location.setLatitude(center.latitude);
        cur_location.setLongitude(center.longitude);

        Location location = new Location("jy");

        location.setLatitude(shop.getShopLatitude());
        location.setLongitude(shop.getShopLongtitude());
        float distance = cur_location.distanceTo(location);

        return distance;
    }

    public String getDistance(Shop store) {
        String result = "";

        double latitude = store.getShopLatitude();
        double longitude = store.getShopLongtitude();


        Location cur_location = AppController.getInstance().getLocation();
        if (cur_location == null) return "";
        if (latitude == 0 || longitude == 0) return "";

        Location location = new Location("jy");

        location.setLatitude(latitude);
        location.setLongitude(longitude);

        float distance = cur_location.distanceTo(location);

        if (distance >= 6000) {
            result = "6km 초과";
        } else if (distance >= 1000) {
            result = String.format("%.1f", (distance / 1000)) + "km";
        } else {
            result = (int) distance + "m";
        }

        return result;
    }

    public void doShareText(Activity activity) {
        String message = ResponseMessage.MSG_SHARE;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message + "\n" + ServerAPIPath.APP_PAGE_URL);
        activity.startActivity(Intent.createChooser(intent, ""));
    }

    public void doShare(Activity activity, View view) {
        doShareText(activity);
//        screenCapture(activity, view);
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        if (m_pCapturedFile != null && m_pCapturedFile.exists()) {
//            final Uri uri = Uri.fromFile(m_pCapturedFile);
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//        }
//        activity.startActivity(Intent.createChooser(intent, ""));
    }

    public void screenCapture(Activity activity, View view) {
        m_pCapturedFile = null;

        if (view != null) {
            view.setDrawingCacheEnabled(true);

            saveImageToSdCard(activity, view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
        }
    }

    private void saveImageToSdCard(Activity activity, Bitmap bitmap) {
        String capturedFilePath = Common.CAPTURED_FILE_PATH;

        File dir = new File(capturedFilePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            String capturedFileName = System.currentTimeMillis() + ".jpg";
            m_pCapturedFile = new File(dir, capturedFileName);
            outputStream = new FileOutputStream(m_pCapturedFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream);
            outputStream.flush();

            refreshGallery(activity);
        } catch (Exception e) {
            Logger.e(TAG, "saveImageToSdCard - Exception : " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                Logger.e(TAG, "saveImageToSdCard - Exception : " + e.getMessage());
            }
        }
    }

    private void refreshGallery(Activity activity) {
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                .parse("file://" + Environment.getExternalStorageDirectory())));
    }

    public static Rect getFrameForView(View v) {
        int location[] = new int[2];
        v.getLocationOnScreen(location);
        Rect viewRect = new Rect(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight());
        return viewRect;
    }

    // Show center toast
    public static void showCenterToast(Context ctx, String msg, int duration) {
        Toast toast = Toast.makeText(ctx, msg, duration);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void showCenterToast(Context context, int message_id, int duration) {
        Toast toast = Toast.makeText(context, context.getResources().getString(message_id), duration);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void showWaitingDlg(Activity activity) {
        if(true == g_isShowProgress) {
            g_nProgressCount++;
            return;
        }

        g_dlgProgress = ProgressDialog.show(activity, null, "잠시만 기다려주세요.");
        if (g_dlgProgress != null) {
            g_dlgProgress.setCancelable(false);
            g_dlgProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {

                }
            });

            g_isShowProgress = true;
            g_nProgressCount = 0;
        }
    }

    public static void hideWaitingDlg() {
        if(g_isShowProgress == true && g_dlgProgress != null) {
            if (g_nProgressCount != 0) {
                g_nProgressCount--;
            }
            else {
                g_dlgProgress.dismiss();
                g_dlgProgress = null;
                g_isShowProgress = false;
            }
        }
    }

    public static String getLocation(Activity activity, Location location) {
        String addressString;

        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                sb.append(address.getLocality()).append("\n");
                sb.append(address.getCountryName());
            }

            addressString = sb.toString();
            addressString = addressString.replace("대한민국", "").trim();

            Log.e("Address from lat,long ;", addressString);
        } catch (IOException e) {
            addressString = "서울특별시 강남구 역삼동";
        }

        return addressString;
    }

    public static boolean isShowKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm.isActive();
    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void setDelayPolicy(StringRequest req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                Common.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static Rect getIntersectionRect(Rect rect1, Rect rect2) {

        int x1 = rect1.left;
        int y2 = rect1.top;
        int w1 = rect1.width();
        int h1 = rect1.height();
        int x3 = rect2.left;
        int y4 = rect2.top;
        int w2 = rect2.width();
        int h2 = rect2.height();

        // get the coordinates of other points needed later:
        int x2 = x1 + w1;
        int x4 = x3 + w2;
        int y1 = y2 - h1;
        int y3 = y4 - h2;

        // find intersection:
        int xL = Math.max(x1, x3);
        int xR = Math.min(x2, x4);
        if (xR <= xL)
            return null;
        else {
            int yT = Math.max(y1, y3);
            int yB = Math.min(y2, y4);
            if (yB <= yT)
                return null;
            else {
               Rect retRect = new Rect(xL, yB, xR - xL, yB - yT);
                retRect.right = xL + (xR - xL);
                retRect.bottom = yB + (yB - yT);
                return retRect;
            }
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } //

    /**
     * bitmap 를 jpeg 파일로 저장; 출처 : http://snowbora.com/418
     */
    public static boolean SaveBitmapToFileCache(Bitmap bitmap,
                                                String strFilePath, boolean bPNG) {

        if (bitmap == null) {
            return false;
        }

        boolean bRet;

        bRet = false;

        File fileItem = new File(strFilePath);
        OutputStream out = null;

        try {
            fileItem.getParentFile().mkdirs();
            fileItem.createNewFile();
            out = new FileOutputStream(fileItem);
            if (bPNG) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.flush();
            bRet = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bRet;
    }

    public static void vibrate(Context context, long millisecond){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        v.vibrate(millisecond);
    }
}

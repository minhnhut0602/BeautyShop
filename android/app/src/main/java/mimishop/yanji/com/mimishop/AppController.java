package mimishop.yanji.com.mimishop;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;

import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.LruBitmapCache;
import mimishop.yanji.com.mimishop.util.Utility;

import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by KCJ on 3/23/2015.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static AppController mInstance;

    private Location cur_location = null;

    //서울시 강남구 역삼동
    final double default_lat = 37.4998715;
    final double default_lng = 127.0386833;


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private User mCurrentUser = null;
    private App mApp = null;
    private Shop mMyShop = null;

    public  boolean isUserId = false;

    @Override
    public void onCreate() {
        super.onCreate();

        printHashKey();
        mInstance = this;

        Location location = new Location("hongik");
        location.setLatitude(default_lat);
        location.setLongitude(default_lng);
        cur_location = location;

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "mimishop.yanji.com.mimishop",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("test:", "hash key:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }//VM4rr2sUbOxGgTGdf3agY9L17o8=
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setApp(App app) {
        mApp = app;
    }

    public App getApp() {
        return mApp;
    }

    public void setLocation(Location location) {
        cur_location = location;
    }

    public Location getLocation() {
        return cur_location;
    }

    public boolean isMyShop(int owner_id) {
        if (owner_id == mCurrentUser.getId()) {
            return true;
        }

        return false;
    }

    public void requestRegisterGCMRegID(String reg_id) {

        if (reg_id == null || reg_id.equals("")) {
            Toast.makeText(this, "GCM이 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String gcm_id = reg_id;

        Map<String, String> params = new HashMap<String, String>();
        params.put("reg_id", gcm_id);

        String url = ServerAPIPath.API_POST_REGISTER_GCMREGID;
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object s) {
                        Logger.d("requestRegisterGCMRegID", "onResponse - 성공");
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String volleyError) {
                        Logger.d("requestRegisterGCMRegID", volleyError);
                    }
                }
                , params);
    }

    public void requestBannerClick(int banner_id) {
        String url = ServerAPIPath.API_POST_CLICK_BANNER;

        Map<String, String> params = new HashMap<String, String>();
        params.put("banner_id", String.format("%d", banner_id));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object s) {
                        Logger.d("requestBannerClick", "onResponse - 성공");
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String volleyError) {
                        Logger.d("requestBannerClick", volleyError);
                    }
                }
                , params);
    }

    public static void showLimitUserDlg (Context context) {
        TextView textView = new TextView(context);
        textView.setText("당신의 권한이 제한되었습니다.\n운영자에게 문의해주십시오.\n");
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(R.color.font_color_gray));
        textView.setGravity(Gravity.CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(textView);
        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }

        });

        builder.show();
    }
}

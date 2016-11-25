package mimishop.yanji.com.mimishop.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.ExitDialog;
import mimishop.yanji.com.mimishop.dialog.LoginDialog;
import mimishop.yanji.com.mimishop.fragment.EventFragment;
import mimishop.yanji.com.mimishop.fragment.FreeTalkFragment;
import mimishop.yanji.com.mimishop.fragment.HomeFragment;
import mimishop.yanji.com.mimishop.fragment.MoreShopEventFragment;
import mimishop.yanji.com.mimishop.fragment.MyPageFragment;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CustomImageButton;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static String TAG = "MainActivity";

    private static MainActivity mainActivity;

    public static final int FRAG_KIND_HOME = 0;
    public static final int FRAG_KIND_FREETALK = 1;
    public static final int FRAG_KIND_EVENT = 2;
    public static final int FRAG_KIND_MYPAGE = 3;
    public static int RELOAD_FRAG_IDX = FRAG_KIND_HOME;

    private int m_nCurrentFragIdx = -1;
    private Fragment m_pCurrentFragment = null;

    private CustomImageButton m_ibHome = null;
    private CustomImageButton m_ibTalk = null;
    private CustomImageButton m_ibEvent = null;
    private CustomImageButton m_ibMyPage = null;

    private TextView    m_tvNotification = null;


    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                }
            };

    public static MainActivity getInstance() {
        return mainActivity;
    }

    // Facebook
    private CallbackManager m_callbackManager;
    public  ShareDialog m_shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        SessionManager.getInstance().restoreHeader(MainActivity.this);

        initUI();

        // blue screen
        if(AppController.getInstance().getCurrentUser() == null) {
            Common.mDeviceId = Utility.getInstance().getDeviceId(this);
            requestInit();
        }
        else {
            init();
        }

        initFacebook();
    }

    /**
     * Init facebook
     */
    private void initFacebook() {
        // 페이스북 sdk 초기화
        m_callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(m_callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        m_shareDialog = new ShareDialog(this);
        m_shareDialog.registerCallback(m_callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                String postId = result.getPostId();
                if (postId != null && !postId.isEmpty()) {
//                    Toast.makeText(getActivity(), R.string.share_success, Toast.LENGTH_SHORT).show();
                    Log.d("aa", "onSuccess");
                }
            }

            @Override
            public void onCancel() {
//                Toast.makeText(m_app, R.string.share_fail, Toast.LENGTH_SHORT).show();
                Log.d("aa", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
//                Toast.makeText(getActivity(), R.string.share_fail, Toast.LENGTH_SHORT).show();
                Log.d("aa", "onError");
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        m_callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initCommonData() {
       TubeSearchActivity.requestTubeList();
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if(AppController.getInstance().getCurrentUser() == null) {
            Common.mDeviceId = Utility.getInstance().getDeviceId(this);
            requestInit();
        }
        else {
            init();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mHandleMessageReceiver);
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }

    private void initUI() {
        // init ui
        m_ibHome = (CustomImageButton)findViewById(R.id.btn_main_home);
        m_ibHome.setOnClickListener(this);
        m_ibTalk = (CustomImageButton)findViewById(R.id.btn_main_talk);
        m_ibTalk.setOnClickListener(this);
        m_ibEvent = (CustomImageButton)findViewById(R.id.btn_main_event);
        m_ibEvent.setOnClickListener(this);
        m_ibMyPage = (CustomImageButton)findViewById(R.id.btn_main_mypage);
        m_ibMyPage.setOnClickListener(this);

        m_tvNotification = (TextView)findViewById(R.id.tv_notification);
        m_tvNotification.setVisibility(View.GONE);
    }

    private void init(){

        initGCM();

        if(RELOAD_FRAG_IDX != FRAG_KIND_HOME) {
            m_nCurrentFragIdx = RELOAD_FRAG_IDX;
            RELOAD_FRAG_IDX = FRAG_KIND_HOME;
        }

        if(m_nCurrentFragIdx != -1 && m_nCurrentFragIdx != FRAG_KIND_HOME) {
            replaceNewFragment(m_nCurrentFragIdx);
        }
        else {
            replaceNewFragment(FRAG_KIND_HOME);
        }

        showNotification();

        checkGps();

        initCommonData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("frag_idx", m_nCurrentFragIdx);
        SessionManager.getInstance().saveHeader(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        m_nCurrentFragIdx = savedInstanceState.getInt("frag_idx", RELOAD_FRAG_IDX);
        SessionManager.getInstance().restoreHeader(this);
    }

    private void initGCM() {

        if (!checkPlayServices(this)) {
            Log.i("test", "No valid Google Play Services APK found.");
            Toast.makeText(this, "No valid Google Play Services APK found.", Toast.LENGTH_SHORT).show();
            return;
        }
        registerReceiver(mHandleMessageReceiver,
                new IntentFilter(Common.DISPLAY_MESSAGE_ACTION));


        GCMRegistrar.checkDevice(this);

        GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);

        if (regId.equals("")) {
            GCMRegistrar.register(this, Common.SENDER_ID);
        } else {

            AppController.getInstance().requestRegisterGCMRegID(regId);
            Logger.v(TAG, "Already registered");

        }
    }

    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context,
                //        9000).show();
            } else {
                Log.i("test", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /*
      ui functions
     */
    public void replaceNewFragment(int kind) {
        replaceNewFragment(kind, null);
    }

    public void replaceNewFragment(int kind, Bundle bundle) {

        if(kind == m_nCurrentFragIdx) return;

        Fragment frag = getFragment(kind);

        if(frag == null) return;

        m_nCurrentFragIdx = kind;

        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.container, frag,  "MY_FRAGMENT");
        transaction.commit();

        m_pCurrentFragment = frag;

        setSelectedMenuButton(kind);
    }

    private Fragment getFragment(int kind){

        Fragment newFrag = null;

        switch (kind) {
            case FRAG_KIND_HOME:
                newFrag = new HomeFragment();
                break;
            case FRAG_KIND_FREETALK:
                newFrag = new FreeTalkFragment();
                break;
//            case FRAG_KIND_EVENT:
//                newFrag = new EventFragment();
//                break;
            case FRAG_KIND_EVENT:
                newFrag = new MoreShopEventFragment();
                break;
            case FRAG_KIND_MYPAGE:
                if(AppController.getInstance().isUserId == true) {
                    showLoginDialog();
                } else newFrag = new MyPageFragment();
                break;
            default:
                break;
        }

        return newFrag;
    }

    public Fragment getVisibleFragment(){
        return m_pCurrentFragment;
    }


    private void setSelectedMenuButton(int kind){

        Logger.d("MainActivity", "setSelectedMenuButton::::::::"+kind);
        m_ibHome.setSelected(false);
        m_ibEvent.setSelected(false);
        m_ibTalk.setSelected(false);
        m_ibMyPage.setSelected(false);

        switch (kind) {
            case FRAG_KIND_HOME:
                m_ibHome.setSelected(true);
                break;
            case FRAG_KIND_FREETALK:
                m_ibTalk.setSelected(true);
                break;
            case FRAG_KIND_EVENT:
                m_ibEvent.setSelected(true);
                break;
            case FRAG_KIND_MYPAGE:
                m_ibMyPage.setSelected(true);
                break;
            default:
                break;
        }
    }

    /*
        action Events
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_main_home:
                replaceNewFragment(FRAG_KIND_HOME);
                break;
            case R.id.btn_main_talk:
                replaceNewFragment(FRAG_KIND_FREETALK);
                break;
//            case R.id.btn_main_event:
//                replaceNewFragment(FRAG_KIND_EVENT);
//                break;
            case R.id.btn_main_event:
                replaceNewFragment(FRAG_KIND_EVENT);
                break;
            case R.id.btn_main_mypage:
                replaceNewFragment(FRAG_KIND_MYPAGE);
                break;
        }
    }

    private void showExitDialog() {
        App app = AppController.getInstance().getApp();
        ExitDialog dlg = new ExitDialog(this);
        dlg.show();
    }

    private void showLoginDialog() {
        LoginDialog dlg = new LoginDialog(this);
        dlg.show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showNotification() {
        User user = AppController.getInstance().getCurrentUser();

        if(user == null) {
            return;
        }

        if(user.isHasNewMessage() == true) {
           m_tvNotification.setVisibility(View.VISIBLE);
        }
        else {
            m_tvNotification.setVisibility(View.GONE);
        }
    }

    public void requestUpdateAccount() {

        String url = ServerAPIPath.API_POST_UPDATE_ACCOUNT;

        Map<String, String> params = new HashMap<String, String>();

        User user = AppController.getInstance().getCurrentUser();

        if(user.isUserNewMessage() == true) {
            params.put("userNewMessage", "Y");
        }
        else {
            params.put("userNewMessage", "N");
        }

        if(user.isUserNewEvent() == true) {
            params.put("userNewEvent", "Y");
        }
        else {
            params.put("userNewEvent", "N");
        }

        if(user.isUserNewInform() == true) {
            params.put("userNewInform", "Y");
        }
        else {
            params.put("userNewInform", "N");
        }

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        if(o == null) {
                            Toast.makeText(MainActivity.this, ResponseMessage.ERR_CREATE_ACCOUNT, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        showNotification();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    public void requestInit(){
        String url = ServerAPIPath.API_INIT;
        ServerAPICall.getInstance().callGET(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {
                        responseInit(s);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String err) {
                        Toast.makeText(MainActivity.this, err, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void responseInit(Object s) {
        if(s == null) {
            Toast.makeText(MainActivity.this, ResponseMessage.ERR_NO_RESULT, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            JSONObject object = (JSONObject) s;
            User user = new User(object);
            App app = new App(object);

            AppController.getInstance().setCurrentUser(user);
            AppController.getInstance().setApp(app);

            Common.mKey = user.getUserKey();

            SessionManager.getInstance().saveHeader(this);

            AppController.getInstance().isUserId = user.isUserID();

            init();
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private LocationManager mLocationManager;

    private void showGoogleMapAgreeAlertDialog() {
        TextView textView = new TextView(this);
        textView.setText("\n위치정보 활용동의\n\n[미미샵]에서 현재위치 정보를\n사용하고자 합니다.\n\n위치정보를 켜지 않으면 마지막으로\n저장된 위치가 현위치로 설정됩니다.\n");
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.font_color_gray));
        textView.setGravity(Gravity.CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(textView);
        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(intent);
                dialog.dismiss();
            }

        });
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }

        });
        builder.show();
    }

    private void checkGps() {
        /*
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) {
            showGoogleMapAgreeAlertDialog();
        }

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
            showGoogleMapAgreeAlertDialog();
        }
        */

        // LBS
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Logger.d(TAG, "checkGps - location : " + location);

        double latitude = 0.0;
        double longitude = 0.0;

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Logger.d(TAG, "checkGps - latitude : " + latitude + ", longitude : " + longitude);

           AppController.getInstance().setLocation(location);
        }

        getLocation();
    }


    private void getLocation() {
        try {
            Location location = null;

            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting network status
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // getting GPS status
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                if(false) {
                    showGoogleMapAgreeAlertDialog();
                }
                else {
                    Toast.makeText(this, ResponseMessage.ERR_NO_GPS, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if (isNetworkEnabled) {
                    Logger.d(TAG, "getLocation - Network Enabled");

                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

                    if (mLocationManager != null) {
                        location = mLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            AppController.getInstance().setLocation(location);

                            return;
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    Logger.d(TAG, "getLocation - GPS Enabled");

                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

                    if (mLocationManager != null) {
                        location = mLocationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            double latitude = 0.0;
                            double longitude = 0.0;

                            AppController.getInstance().setLocation(location);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(TAG, "getLocation - Exception : " + e.getMessage());

        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Logger.d(TAG, "onLocationChanged - latitude : " + latitude + ", longitude : " + longitude);

            AppController.getInstance().setLocation(location);
        }
    };

}

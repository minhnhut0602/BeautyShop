package mimishop.yanji.com.mimishop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class IntroActivity extends Activity {

    public static final String TAG = "IntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Common.mDeviceId = Utility.getInstance().getDeviceId(this);

        if(true) {  // kcj test
            Utility.getInstance().getLocation(this, null, true);
        }
        requestInit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_intro, menu);
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

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void requestInit(){
        String url = ServerAPIPath.API_INIT;
            ServerAPICall.getInstance().callGET(url,
                    new onReponseListener() {
                        @Override
                        public void onResponse(Object s) {
                            responseInit(s);
                        }
                    },
                    new onErrorListener() {
                        @Override
                        public void onErrorResponse(String err) {
                            Toast.makeText(IntroActivity.this, err, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
    }

    private void responseInit(Object s) {
       if(s == null) {
            Toast.makeText(IntroActivity.this, ResponseMessage.ERR_NO_RESULT, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            JSONObject object = (JSONObject) s;
            final User user = new User(object);
            App app = new App(object);

            AppController.getInstance().setCurrentUser(user);
            AppController.getInstance().setApp(app);

            Common.mKey = user.getUserKey();
            SessionManager.getInstance().saveHeader(IntroActivity.this);

            int hairCnt = object.getInt("hairShopCnt");
            int nailCnt = object.getInt("nailShopCnt");

            String text = String.format("헤어샵 %d점포 등록      네일샵 %d점포등록", hairCnt, nailCnt);

            ((TextView)findViewById(R.id.tv_info)).setText(text);

            JSONObject setting = object.getJSONObject("setting");
            double rationMainBanner = setting.getDouble("settingMainBannerRatio");
            Common.HOME_BANNER_RATIO = rationMainBanner;
            rationMainBanner = setting.getDouble("settingCastBannerRatio");
            Common.CAST_BANNER_RATIO = rationMainBanner;
            rationMainBanner = setting.getDouble("settingEventBannerRatio");
            Common.LIFE_BANNER_RATIO = rationMainBanner;
            rationMainBanner = setting.getDouble("settingPushBannerRatio");
            Common.PUSH_BANNER_RATIO= rationMainBanner;
            rationMainBanner = setting.getDouble("settingLogoutBannerRatio");
            Common.LOGOUT_BANNER_RATIO = rationMainBanner;

            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    AppController.getInstance().isUserId = user.isUserID();
                    access();
                }
            }, 2000);
        }
        catch (Exception e) {
            Toast.makeText(IntroActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void access(){
        String url = ServerAPIPath.API_ACCESS;
        ServerAPICall.getInstance().callGET(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {
                        startMainActivity();
//                    if (user.isUserID() == false) {
//                        startMainActivity();
//                    } else {
//                        startLoginActivity();
//                    }
                        finish();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String err) {
                        Toast.makeText(IntroActivity.this, err, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}

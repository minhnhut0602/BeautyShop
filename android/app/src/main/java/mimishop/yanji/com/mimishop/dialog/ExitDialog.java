package mimishop.yanji.com.mimishop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.activity.event.EventDetailActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.modal.Banner;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class ExitDialog extends Dialog implements View.OnClickListener {

    public final static String TAG = "ExitDialog";

    private Context mContext;
    private NetworkImageView mIvAdImage;

    private Button mBtnOK;
    private Button mBtnClose;

    private Banner mExitAds = null;

    public ExitDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_exit);

        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mIvAdImage = (NetworkImageView) findViewById(R.id.iv_ad_image);

        final  View imageView = findViewById(R.id.rl_image);
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = imageView.getMeasuredWidth();
                int height = imageView.getMeasuredHeight();
                double ratio = Common.LOGOUT_BANNER_RATIO;
                width = View.MeasureSpec.getSize(width);
                height = (int) (width / ratio);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.height = height;
                imageView.setLayoutParams(params);
            }
        });

        mIvAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Banner banner = mExitAds;
                if(banner.getBannerShowMode() == Banner.BANNER_DETAIL_MODE) {
                    Intent intent = new Intent((Activity) mContext, EventDetailActivity.class);
                    intent.putExtra("banner", banner);
                    ((Activity) mContext).startActivity(intent);
                }
                else {
                    if (banner.getBannerAds().getAdsURL() != null) {
                        Utility.startURLActivity((Activity) mContext, banner.getBannerAds().getAdsURL());
                    }
                }

                AppController.getInstance().requestBannerClick(banner.getId());
            }
        });

        mBtnOK = (Button) findViewById(R.id.btn_ok);
        mBtnOK.setOnClickListener(this);

        mBtnClose = (Button) findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(this);

        requestFinishAds();

        if(false) {
            initKeyboardTouch();
        }
    }

    private  void initKeyboardTouch() {
        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        view = findViewById(R.id.content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                close();
                break;
            case R.id.btn_close:

                onDestroy();
                break;
        }
    }

    public void close()
    {
        Utility.showWaitingDlg((Activity)mContext);
        String url = ServerAPIPath.API_FINISH;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(AppController.getInstance().getCurrentUser().getId()));

        Log.e(TAG, "id:" + String.valueOf(AppController.getInstance().getCurrentUser().getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Utility.hideWaitingDlg();
                        onDestroy();
                        ((MainActivity)mContext).finish();
                        System.exit(0);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                        onDestroy();
                        ((MainActivity)mContext).finish();
                        System.exit(0);

                    }
                }, params);
    }


    private void onDestroy() {
        dismiss();
    }

    private void showAds() {
        if(mExitAds  == null) {
            return;
        }

        Advertise ads = mExitAds.getBannerAds();
        if(ads.getAdsImgURL() != null) {
            mIvAdImage.setImageUrl(ads.getAdsImgURL(), AppController.getInstance().getImageLoader());
        }

        mIvAdImage.setDefaultImageResId(R.drawable.exit);
    }

    private void requestFinishAds() {
        String url = ServerAPIPath.API_GET_EXIT_ADS;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject dataArray = (JSONObject) o;
                            if (dataArray != null) {
                                mExitAds = new Banner(dataArray);
                            }

                            showAds();
                        }
                        catch (Exception e) {
                            Toast.makeText(mContext, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

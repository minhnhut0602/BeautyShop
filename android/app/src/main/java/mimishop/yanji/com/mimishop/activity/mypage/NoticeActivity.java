package mimishop.yanji.com.mimishop.activity.mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.User;


@SuppressLint("SetJavaScriptEnabled")
public class NoticeActivity extends Activity {

    private WebView mWebView;
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("공지사항");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_notice);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                mPbLoading.setVisibility(View.GONE);
                mWebView.setBackgroundColor(0x00000000);
                if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                mPbLoading.setVisibility(View.GONE);
            }
        });

        mWebView.setBackgroundColor(0x00000000);
        if (Build.VERSION.SDK_INT >= 11) mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        User currentUser = AppController.getInstance().getCurrentUser();
        if (currentUser.getUserPrivacyURL() != null && !currentUser.getUserPrivacyURL().equals("")) {
            mWebView.loadUrl(currentUser.getUserPrivacyURL());
        }

        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        initMenu();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_notice));
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_back);

        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        mWebView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();

        mWebView.resumeTimers();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
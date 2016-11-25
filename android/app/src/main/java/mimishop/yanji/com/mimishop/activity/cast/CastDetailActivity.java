package mimishop.yanji.com.mimishop.activity.cast;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.CastDetailViewPagerAdapter;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.WriteCastCommentDialog;
import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.modal.CastComment;
import mimishop.yanji.com.mimishop.modal.CastDetail;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.ExtendedViewPager;

public class CastDetailActivity extends Activity  implements  ViewPager.OnPageChangeListener{

    public static final String TAG = "CastDetailActivity";
    private static CastDetailActivity mInstace = null;

    private ExtendedViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private CastDetailViewPagerAdapter mCastDetailViewPagerAdapter;
    private ArrayList<CastDetail> mCastDetailList = new ArrayList<CastDetail>();

    private int mCastID = 0;
    private Cast m_clsCast = null;
    private CastComment m_clsCastComment = null;

    public static CastDetailActivity getInstance() {
        return mInstace;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);
        mCastID = getIntent().getIntExtra("cast_id", -1);

        mInstace = this;
        init();
    }

    public void finishActivity() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_cast_detail, menu);
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

    private void init() {
        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);
        mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

        mCastDetailViewPagerAdapter = new CastDetailViewPagerAdapter(this, R.layout.view_cast_detail_main, mCastDetailList);
        mViewPager.setAdapter(mCastDetailViewPagerAdapter);
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mIndicator.setViewPager(mViewPager);

        requestGetCast();
    }

    public Cast getCast() {
        return m_clsCast;
    }

    public void prevPage() {
        int currentItem = mViewPager.getCurrentItem();

        if(currentItem > 0) {
            currentItem--;
        }

        mViewPager.setCurrentItem(currentItem);
    }

    public void gotoFirstPage() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshUI();
    }

    public void startWriteCastCommentDialog() {
        WriteCastCommentDialog dlg = new WriteCastCommentDialog(this, mCastID);

        if(m_clsCastComment != null) {
            dlg.setCastComment(m_clsCastComment);
        }

        dlg.show();
    }

    public void refreshUI() {
        mCastDetailViewPagerAdapter.refreshUI(mViewPager.getCurrentItem());
    }

    public void likeCast() {
        String url = ServerAPIPath.API_POST_LIKE_CAST;
        Map<String, String> params = new HashMap<String, String>();

        params.put("cast_id", String.format("%d", mCastID));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject object = (JSONObject)o;
                            int cnt = object.getInt("cnt");
                            boolean duplicate = object.getBoolean("isDuplicate");
                            if(cnt != m_clsCast.getCastHeartCnt()) {
                                m_clsCast.setCastHeartCnt(cnt);
                                refreshUI();
                            }

                            if(duplicate == true) {
                                Toast.makeText(CastDetailActivity.this, ResponseMessage.SUCCESS_DUPLICATE_LIKE, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(CastDetailActivity.this, ResponseMessage.ERR_LIKE_DISABLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastDetailActivity.this, ResponseMessage.ERR_NO_NETWORK, Toast.LENGTH_SHORT).show();
                    }
                },
                params);

    }

    public void shareCast() {
        String url = ServerAPIPath.API_POST_SHARE_CAST;
        Map<String, String> params = new HashMap<String, String>();

        params.put("cast_id", String.format("%d", mCastID));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject object = (JSONObject)o;
                            int cnt = object.getInt("cnt");
                            boolean duplicate = object.getBoolean("isDuplicate");

                            if(cnt != m_clsCast.getCastShareCnt()) {
                                m_clsCast.setCastShareCnt(cnt);
                                refreshUI();
                            }

                            if(duplicate == true) {
                                Toast.makeText(CastDetailActivity.this, ResponseMessage.SUCCESS_DUPLICATE_SHARE, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(CastDetailActivity.this, ResponseMessage.ERR_LIKE_DISABLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastDetailActivity.this, ResponseMessage.ERR_NO_NETWORK, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestCastDetailList(){

        String url = ServerAPIPath.API_GET_CAST_DETAIL_LIST;

        if(mCastID == 0) {
            Toast.makeText(this, "캐스트가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        url+="?cast_id="+mCastID;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseCastDetailList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseCastDetailList(Object o){

        try {

            mCastDetailList.clear();
            JSONArray dataArray = (JSONArray)o;

            mCastDetailList.add(new CastDetail());

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                CastDetail category = new CastDetail(jsonObject);

                mCastDetailList.add(category);
            }

            mCastDetailList.add(new CastDetail());

            mCastDetailViewPagerAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Logger.e(TAG, "responseCastDetailList - JSONException : " + e.getMessage());
            Toast.makeText(CastDetailActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestGetCast(){

        String url = ServerAPIPath.API_GET_CAST;

        if(mCastID == 0) {
            Toast.makeText(this, "캐스트가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        url+="?cast_id="+mCastID;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject jsonObject = (JSONObject)o;
                            m_clsCast = new Cast(jsonObject);
                            requestCastDetailList();
                        }
                        catch (Exception e) {
                            Logger.e(TAG, "responseGetCast - JSONException : " + e.getMessage());
                            Toast.makeText(CastDetailActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}

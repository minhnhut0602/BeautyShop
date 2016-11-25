package mimishop.yanji.com.mimishop.activity.agreement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.AgreeFragmentViewPagerAdapter;
import mimishop.yanji.com.mimishop.adapter.EventFragmentViewPagerAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Life;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 3/23/2015.
 */
public class AgreementActivity extends Activity implements  ViewPager.OnPageChangeListener{

    public static final String TAG = "AgreementActivity";

    List<String> m_lstContent = new ArrayList<String>();

    private ViewPager mViewPager = null;
    private AgreeFragmentViewPagerAdapter  mAgreePagerAdapter = null;
    private PagerSlidingTabStrip mIndicator = null;

    private int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        type = getIntent().getIntExtra("type", 0);

        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mIndicator = (PagerSlidingTabStrip)findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);
        initMenu();
        init();
    }


    private void init(){
        requestAgreement();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setGravity(Gravity.LEFT);
        if(type == 0) {
            title.setText("이용약관");
            mIndicator.setVisibility(View.VISIBLE);
        } else {
            title.setText("개인정보 수집 및 이용");
            mIndicator.setVisibility(View.GONE);
        }

        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

    }

    private void requestAgreement(){

        String url = ServerAPIPath.API_GET_AGREEMENT;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseAgreement(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(AgreementActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void responseAgreement(Object json){

        try {
            m_lstContent.clear();
            JSONArray dataArray = (JSONArray)json;
            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                m_lstContent.add(jsonObject.getString("content"));
            }
            mAgreePagerAdapter = new AgreeFragmentViewPagerAdapter(this, R.layout.item_agreement, m_lstContent,type);
            mViewPager.setAdapter(mAgreePagerAdapter);
            mIndicator.setViewPager(mViewPager);
        } catch (Exception e) {
            Logger.e(TAG, "responseAgreement - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

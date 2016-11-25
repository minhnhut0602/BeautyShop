package mimishop.yanji.com.mimishop.activity.event;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.R;
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
public class EventLifeActivity extends Activity implements  ViewPager.OnPageChangeListener {

    public static final String TAG = "EventFragment";

    private static EventLifeActivity frag = null;
    private ArrayList<Category> m_lstCategory = new ArrayList<Category>();

    private ViewPager mViewPager = null;
    private EventFragmentViewPagerAdapter  mEventPagerAdapter = null;
    private PagerSlidingTabStrip mIndicator = null;
    private View m_vMain = null;

    private int m_nSelectedCategoryIDX = 0;

    public static EventLifeActivity getInstance() {
        return  frag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event);
        frag = this;

        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mIndicator = (PagerSlidingTabStrip)findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);
        m_vMain = findViewById(R.id.content);
        init();
        initMenu();
    }


    private void init(){
        requestCategoryList();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("생활정보");

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

    public void gotoEventDetail( Life event){

        String url = "";
        if (event.getLifeURL() != null) {
            url = event.getLifeURL() ;
        } else {
            url = Common.APP_PAGE_URL;
        }
        Utility.startURLActivity(this, url);

        event.setEventClickCnt(event.getEventClickCnt() + 1);
        event.setEventClickCnt(event.getEventClickCnt());

        requestIncreaseEventClick(event);
    }

    public int getCategoryIDX(int position) {
       return m_lstCategory.get(position).getId();
    }

    public void showContent() {
        m_vMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        m_nSelectedCategoryIDX = position;
    }

    private void requestCategoryList(){

        String url = ServerAPIPath.API_GET_CATEGORY_LIST;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseCategoryList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(EventLifeActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void responseCategoryList(Object json){

        try {

            m_lstCategory.clear();
            JSONArray dataArray = (JSONArray)json;

            Category category = new Category();
            category.setName("전체");
            category.setId(0);
            m_lstCategory.add(category);

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                category = new Category(jsonObject);

                if(category.isEventCategory() == true) {
                    m_lstCategory.add(category);
                }
            }

            mEventPagerAdapter = new EventFragmentViewPagerAdapter(this, R.layout.item_row_event_fragment, m_lstCategory);
            mViewPager.setAdapter(mEventPagerAdapter);
            mIndicator.setViewPager(mViewPager);
            m_nSelectedCategoryIDX = 0;

        } catch (Exception e) {
            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
        showContent();
    }

    public void requestIncreaseEventClick(Life event) {
        String url = ServerAPIPath.API_POST_LIFE_CLICK;

        Map<String, String> params = new HashMap<String, String>();
        params.put("life_id", String.format("%d", event.getId()));
        params.put("click_cnt", String.format("%d", event.getEventClickCnt()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {

                    }
                }, params
        );
    }


}

package mimishop.yanji.com.mimishop.activity.cast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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
import mimishop.yanji.com.mimishop.activity.event.EventDetailActivity;
import mimishop.yanji.com.mimishop.adapter.CastAdapter;
import mimishop.yanji.com.mimishop.adapter.CastPremiumViewPagerAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Banner;
import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.ExpandableHeightGridView;

public class CastActivity extends Activity implements  ViewPager.OnPageChangeListener, AbsListView.OnScrollListener  {

    public static final String TAG = "CastActivity";

    private final static int PREMIUM_MAX_CNT = 5;

    private ExpandableHeightGridView m_gvCast = null;
    private CastAdapter m_adpCast = null;
    private ArrayList<Cast> m_lstCast = new ArrayList<Cast>();

    private ViewPager mViewPager;
    private CastPremiumViewPagerAdapter mCastPremiumViewPagerAdapter;
    private ArrayList<Banner> mCastBannerList = new ArrayList<Banner>();

    private TabPageIndicator mIndicator;

    private ImageView mIvFirstDot;
    private ImageView mIvSecondDot;
    private ImageView mIvThirdDot;
    private ImageView mIvFourthDot;
    private ImageView mIvFifthDot;

    private DrawerLayout m_dlDrawerLayout = null;
    private View         m_icSliderMenu = null;

    // slidermenu
    private View m_icHome = null;
    private View m_icBest = null;
    private ListView m_lvCategory = null;
    private ArrayList<Category> m_arrCategory = new ArrayList<Category>();
    private int m_nCategoryIdx = -1;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = firstVisibleItem + visibleItemCount;

        if (lastItem == totalItemCount) {
            if (m_nPreLastNum != lastItem) {
                m_nPreLastNum = lastItem;
                addItems(totalItemCount);
            }
        }
    }

    private void addItems(int totalItemCount) {
        int pageNum = (totalItemCount / m_nPageItemCnt) + 1;
        requestCastList(pageNum, false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_cast, menu);
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

    private void init(){
        initMenu();

        initMain();

        initSliderMenu();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_cast));

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

        ImageButton menuBtn = (ImageButton)view.findViewById(R.id.btn_menu);

        if(menuBtn != null) {
            menuBtn.setVisibility(View.VISIBLE);

            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m_dlDrawerLayout.openDrawer(m_icSliderMenu);
                }
            });
        }
    }

    private void initSliderMenu() {
        m_dlDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        m_icSliderMenu = findViewById(R.id.ic_slide_menu);

        m_icHome = m_icSliderMenu.findViewById(R.id.ic_home);
        m_icBest = m_icSliderMenu.findViewById(R.id.ic_best);
        m_lvCategory = (ListView)m_icSliderMenu.findViewById(R.id.lv_category);

        ImageView icon = (ImageView)m_icHome.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.ic_home_selector);

        m_icHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dlDrawerLayout.closeDrawer(m_icSliderMenu);
                m_nCategoryIdx = -1;
                requestCastList(1, true);
            }
        });

        icon = (ImageView)m_icBest.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.ic_best_selector);
        TextView title = (TextView)m_icBest.findViewById(R.id.tv_title);
        title.setText("베스트");

        m_icBest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dlDrawerLayout.closeDrawer(m_icSliderMenu);
                startCastBestActivity();
            }
        });

        m_lvCategory.setAdapter(new ArrayAdapter<Category>(this, R.layout.item_row_cast_slider, m_arrCategory) {
            class ViewHolder {
                private ImageView iv_icon;
                private TextView tv_name;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Category shop = m_arrCategory.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_cast_slider, null);
                    holder = new ViewHolder();
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.tv_name.setText(shop.getName());
                holder.tv_name.setTextSize(12);
                holder.tv_name.setTextColor(getResources().getColorStateList(R.color.text_cast_item_selector));

                if (shop.getName().equals("네일")) {
                    holder.iv_icon.setImageResource(R.drawable.ic_nail_selector);
                    holder.iv_icon.setVisibility(View.VISIBLE);
                } else if (shop.getName().equals("헤어")) {
                    holder.iv_icon.setImageResource(R.drawable.ic_hair_selector);
                    holder.iv_icon.setVisibility(View.VISIBLE);
                }
                else if (shop.getName().equals("메이크업")){
                    holder.iv_icon.setImageResource(R.drawable.ic_makeup_selector);
                    holder.iv_icon.setVisibility(View.VISIBLE);
                }
                else if (shop.getName().equals("다이어트")){
                    holder.iv_icon.setImageResource(R.drawable.ic_diet_selector);
                    holder.iv_icon.setVisibility(View.VISIBLE);
                }
                else if (shop.getName().equals("패션")){
                    holder.iv_icon.setImageResource(R.drawable.ic_fasion_selector);
                    holder.iv_icon.setVisibility(View.VISIBLE);
                }
                else {
                    holder.iv_icon.setVisibility(View.GONE);
                }

                return convertView;
            }
        });

        m_lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                m_dlDrawerLayout.closeDrawer(m_icSliderMenu);
                m_nCategoryIdx = i;
                requestCastList(1, true);
            }
        });

        requestCategoryList();
    }

    private void initMain() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);
        mCastPremiumViewPagerAdapter = new CastPremiumViewPagerAdapter(this, R.layout.item_row_cast_premier, mCastBannerList);
        mViewPager.setAdapter(mCastPremiumViewPagerAdapter);
        mCastPremiumViewPagerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CastPremiumViewPagerAdapter.ViewHolder holder = (CastPremiumViewPagerAdapter.ViewHolder) view.getTag();
                Banner banner = holder.banner;
                if(banner.getBannerShowMode() == Banner.BANNER_DETAIL_MODE) {
                    Intent intent = new Intent(CastActivity.this, EventDetailActivity.class);
                    intent.putExtra("banner", banner);
                    startActivity(intent);
                }
                else {
                    if (banner.getBannerAds().getAdsURL() != null) {
                        Utility.startURLActivity(CastActivity.this, banner.getBannerAds().getAdsURL());
                    }
                }

                AppController.getInstance().requestBannerClick(banner.getId());
            }
        });

        final RelativeLayout rlCastBanner = (RelativeLayout)findViewById(R.id.rl_cast_banner);
        ViewTreeObserver vto = rlCastBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlCastBanner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  = rlCastBanner.getMeasuredWidth();
                int height = rlCastBanner.getMeasuredHeight();
                width = View.MeasureSpec.getSize(width);
                height = (int)(width/ Common.CAST_BANNER_RATIO);

                rlCastBanner.getLayoutParams().height = height;
            }
        });

        mIndicator.setViewPager(mViewPager);

        mIvFirstDot = (ImageView) findViewById(R.id.iv_first_dot);
        mIvSecondDot = (ImageView) findViewById(R.id.iv_second_dot);
        mIvThirdDot = (ImageView)  findViewById(R.id.iv_third_dot);
        mIvFourthDot = (ImageView) findViewById(R.id.iv_fourth_dot);
        mIvFifthDot = (ImageView)  findViewById(R.id.iv_fifth_dot);

        m_gvCast = (ExpandableHeightGridView) findViewById(R.id.myId);
        m_gvCast.setExpanded(true);
        m_gvCast.setOnScrollListener(this);

        m_adpCast = new CastAdapter(this, R.layout.item_row_cast, m_lstCast);
        m_gvCast.setAdapter(m_adpCast);

        m_gvCast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startCastDetailActivity(m_lstCast.get(i));
            }
        });

        requestCastList(1, true);
        requestBannerList();
    }

    private void initDots() {
        mIvFirstDot.setBackgroundResource(R.drawable.ic_indicator);
        mIvSecondDot.setBackgroundResource(R.drawable.ic_indicator);
        mIvThirdDot.setBackgroundResource(R.drawable.ic_indicator);
        mIvFourthDot.setBackgroundResource(R.drawable.ic_indicator);
        mIvFifthDot.setBackgroundResource(R.drawable.ic_indicator);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {

        initDots();

        switch (position) {
            case 0:
                mIvFirstDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                break;
            case 1:
                mIvSecondDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                break;
            case 2:
                mIvThirdDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                break;
            case 3:
                mIvFourthDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                break;
            case 4:
                mIvFifthDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                break;
        }
    }


    private void startCastDetailActivity(Cast cast){
        Intent intent = new Intent(this, CastDetailActivity.class);

        intent.putExtra("cast_id",cast.getId());
        startActivity(intent);
    }

    private void startCastBestActivity() {
        Intent intent = new Intent(this, CastBestActivity.class);
        startActivity(intent);
    }

    private void showPremier() {
        mCastPremiumViewPagerAdapter.notifyDataSetChanged();

        int count = mCastBannerList.size();

        if(mCastBannerList.size() == 0) {
            return;
        }

        if(count >= 1) {
            mIvFirstDot.setVisibility(View.VISIBLE);
        }
        else {
            mIvFirstDot.setVisibility(View.GONE);
        }

        if(count >= 3) {
            mIvThirdDot.setVisibility(View.VISIBLE);
        }
        else {
            mIvThirdDot.setVisibility(View.GONE);
        }

        if(count >= 2) {
            mIvSecondDot.setVisibility(View.VISIBLE);
        }
        else {
            mIvSecondDot.setVisibility(View.GONE);
        }

        if(count >= 4) {
            mIvFourthDot.setVisibility(View.VISIBLE);
        }
        else {
            mIvFourthDot.setVisibility(View.GONE);
        }

        if(count >= 5) {
            mIvFifthDot.setVisibility(View.VISIBLE);
        }
        else {
            mIvFifthDot.setVisibility(View.GONE);
        }
    }

    private void requestBannerList() {
        String url = ServerAPIPath.API_GET_CAST_BANNER_LIST;
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseBannerList(o);
                        showPremier();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    private void responseBannerList(Object json) {
        try {
            mCastBannerList.clear();

            JSONArray dataArray = (JSONArray)json;

            int cnt = dataArray.length();
            for(int i = 0; i < PREMIUM_MAX_CNT; i++) {
                if(cnt > i) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    Banner advertise = new Banner(jsonObject);

                    mCastBannerList.add(advertise);
                }
                else {
                    break;
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseBannerList - JSONException : " + e.getMessage());
        }
    }

    private void requestCastList(int pageNum, boolean isChangeCategory){

        if (m_nPrePageNum == pageNum && isChangeCategory == false) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_gvCast.setSelection(0);
            m_lstCast.clear();
        }

        String url = ServerAPIPath.API_GET_CAST_LIST;

        boolean isChange = false;
        if(m_nCategoryIdx != -1) {
            url += "?category_id="+m_arrCategory.get(m_nCategoryIdx).getId();
            isChange = true;
        }

        if(pageNum >= 1) {
            if(isChange == false) {
                url += "?page_num="+pageNum;
            }
            else {
                url += "&page_num="+pageNum;
            }
        }

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseCastList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(CastActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseCastList(Object o){

        try {

            JSONObject jsonObject = (JSONObject)o;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
            JSONArray dataArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                Cast category = new Cast(dataObject);

                m_lstCast.add(category);
            }
            m_adpCast.notifyDataSetChanged();

        } catch (Exception e) {
            Logger.e(TAG, "responseCastList - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }


    private void requestCategoryList() {

        String url = ServerAPIPath.API_GET_CATEGORY_LIST;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            m_arrCategory.clear();

                            JSONArray dataArray = (JSONArray) o;

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                Category category = new Category(jsonObject);

                                if (category.isCastCategory() == true) {
                                    m_arrCategory.add(category);
                                }
                            }

                            ((ArrayAdapter<Category>) m_lvCategory.getAdapter()).notifyDataSetChanged();

                        } catch (Exception e) {
                            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
                            Toast.makeText(CastActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

package mimishop.yanji.com.mimishop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.agreement.AgreementActivity;
import mimishop.yanji.com.mimishop.activity.cast.CastActivity;
import mimishop.yanji.com.mimishop.activity.ShopSearchActivity;
import mimishop.yanji.com.mimishop.activity.event.EventDetailActivity;
import mimishop.yanji.com.mimishop.activity.event.EventLifeActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.modal.Banner;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 3/23/2015.
 */
public class HomeFragment extends Fragment {

    public static String TAG = "HomeFragment";

    public static final int REQ_BEST_FREETALK = 100;
    public static final int REQ_BEST_CAST = 101;

    private View m_pMainView;

    private ViewPager mBannerViewPager;
    private ArrayList<Banner> mBannerList = new ArrayList<Banner>();
    private TabPageIndicator mIndicator;

    private ImageView mIvFirstDot;
    private ImageView mIvSecondDot;
    private ImageView mIvThirdDot;


    private ScrollView       m_svMain = null;

    private boolean []m_isLoadingFlag = new boolean[5];
    private boolean   m_isFirstLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        m_pMainView = view;

        init();

        return view;
    }

    private void init(){

        m_svMain = (ScrollView)m_pMainView.findViewById(R.id.content);

        initMenu();

        initButtons();

        requestBannerList();

    }

    private void initMenu() {
        ImageButton btn = (ImageButton)m_pMainView.findViewById(R.id.ib_share);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility utility = Utility.getInstance();
                utility.doShare(getActivity(), m_pMainView);
            }
        });

        btn = (ImageButton)m_pMainView.findViewById(R.id.ib_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(0); // all
            }
        });
    }


    private  void showMainView () {
        boolean isVisible = false;
        for(int i = 0; i < m_isLoadingFlag.length; i++) {

            if(m_isLoadingFlag[i] == false) {
                isVisible = false;
                break;
            }
            else {
                isVisible = true;
            }
        }

        if(isVisible == true && m_isFirstLoading == true) {
            m_svMain.smoothScrollTo(0,0);
            m_svMain.setVisibility(View.VISIBLE);
            m_isFirstLoading = false;
        }
    }

    private void initDots() {
        mIvFirstDot.setBackgroundResource(R.drawable.ic_indicator);
        mIvSecondDot.setBackgroundResource(R.drawable.ic_indicator);
        mIvThirdDot.setBackgroundResource(R.drawable.ic_indicator);
    }

    private void initBanner() {

        mIvFirstDot = (ImageView) m_pMainView.findViewById(R.id.iv_first_dot);
        mIvSecondDot = (ImageView) m_pMainView.findViewById(R.id.iv_second_dot);
        mIvThirdDot = (ImageView) m_pMainView.findViewById(R.id.iv_third_dot);
        mBannerViewPager = (ViewPager) m_pMainView.findViewById(R.id.view_pager);

        if(mBannerList.size() == 1) {
            mIvSecondDot.setVisibility(View.GONE);
            mIvThirdDot.setVisibility(View.GONE);
        }
        else if(mBannerList.size() == 2) {
            mIvThirdDot.setVisibility(View.GONE);
        }

        mIndicator = (TabPageIndicator) m_pMainView.findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                initDots();

                switch (i) {
                    case 0:
                        mIvFirstDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                        break;
                    case 1:
                        mIvSecondDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                        break;
                    case 2:
                        mIvThirdDot.setBackgroundResource(R.drawable.ic_indicator_activate);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        final RelativeLayout rlHomeBanner = (RelativeLayout)m_pMainView.findViewById(R.id.rl_home_banner);
        ViewTreeObserver vto = rlHomeBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlHomeBanner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = rlHomeBanner.getMeasuredWidth();
                int height = rlHomeBanner.getMeasuredHeight();
                width = View.MeasureSpec.getSize(width);
                height = (int) (width / Common.HOME_BANNER_RATIO);

                rlHomeBanner.getLayoutParams().height = height;
            }
        });

        mBannerViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mBannerList.size();
            }

            @Override
            public Object instantiateItem(final ViewGroup container, int position) {
                final Banner banner = mBannerList.get(position);

                View convertView = null;
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_row_banner, null);

                NetworkImageView ivImage = (NetworkImageView) convertView.findViewById(R.id.iv_banner);

                final Advertise ads = banner.getBannerAds();

                if (ads.getAdsImgURL() != null) {
                    ivImage.setImageUrl(ads.getAdsImgURL(), AppController.getInstance().getImageLoader());
                } else {
                    ivImage.setDefaultImageResId(R.drawable.btn_transparent_normal);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (banner.getBannerShowMode() == Banner.BANNER_DETAIL_MODE) {
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            intent.putExtra("banner", banner);
                            startActivity(intent);
                        } else {
                            if (ads.getAdsURL() != null) {
                                Utility.startURLActivity(getActivity(), ads.getAdsURL());
                            }
                        }

                        AppController.getInstance().requestBannerClick(banner.getId());
                    }
                });

                ((ViewPager) container).addView(convertView, 0);
                final View rlContent = convertView;
                ViewTreeObserver vto = rlContent.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rlContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int width = rlContent.getMeasuredWidth();
                        int height = rlContent.getMeasuredHeight();

                        height = (int) ((float) width / Common.HOME_BANNER_RATIO);

                        rlContent.getLayoutParams().height = height;
                    }
                });
                return convertView;
            }

            @Override
            public void destroyItem(View pager, int position, Object view) {
                ((ViewPager) pager).removeView((View) view);
            }

            @Override
            public boolean isViewFromObject(View pager, Object obj) {
                return pager == obj;
            }

            @Override
            public void restoreState(Parcelable arg0, ClassLoader arg1) {
            }

            @Override
            public Parcelable saveState() {
                return null;
            }

            @Override
            public void startUpdate(View arg0) {
            }

            @Override
            public void finishUpdate(View arg0) {
            }
        });

        mIndicator.setViewPager(mBannerViewPager);
    }

    private void initButtons() {
        ((Button)m_pMainView.findViewById(R.id.btn_find_hair)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(2); // hair
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_nail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(1); // nail
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_eyelashes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(3); // ???
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_waksing)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(4); // ??
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_skin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(5); // ??
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_massage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(6); // ???
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_mimi_cast)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCastActivity();      //?????
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_life)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEventLifActivity();      //?????
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_banyonggu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(7); // ???
            }
        });

        ((Button)m_pMainView.findViewById(R.id.btn_find_tatu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopMapActivity(8); // ???
            }
        });

    }

    private void requestBannerList() {
        String url = ServerAPIPath.API_GET_BANNER_LIST;
        Utility.showWaitingDlg(getActivity());
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseBannerList(o);
                        m_isLoadingFlag[0] = true;
                        showMainView();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    private void responseBannerList(Object json) {
        try {
            mBannerList.clear();

            JSONArray dataArray = (JSONArray)json;

            int cnt = dataArray.length();
            for(int i = 0; i < Common.MAX_BANNER_CNT; i++) {
                if(cnt > i) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    Banner advertise = new Banner(jsonObject);

                    mBannerList.add(advertise);
                }
                else {
                    break;
                }
            }

            initBanner();

        } catch (Exception e) {
            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseBannerList - JSONException : " + e.getMessage());
        }
    }

    private void startShopMapActivity(int category){
        Intent intent = new Intent(getActivity(), ShopSearchActivity.class);

        intent.putExtra("category_id", category);
        startActivity(intent);
    }

    private void startCastActivity() {
        Intent intent = new Intent(getActivity(), CastActivity.class);

        startActivity(intent);

    }

    private void startEventLifActivity() {
        Intent intent = new Intent(getActivity(), EventLifeActivity.class);

        startActivity(intent);

    }


}

package mimishop.yanji.com.mimishop.activity.shopinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.devsmart.android.ui.HorizontalListView;
//import com.facebook.android.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.ImageAdapter;
import mimishop.yanji.com.mimishop.adapter.MyPriceAdapter;
import mimishop.yanji.com.mimishop.adapter.ProductSectionizer;
import mimishop.yanji.com.mimishop.adapter.SimpleSectionAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.InputWrongInfoDialog;
import mimishop.yanji.com.mimishop.dialog.LoginDialog;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.Life;
import mimishop.yanji.com.mimishop.modal.Product;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.StarRatingBar;

public class ShopDetailActivity extends Activity implements AbsListView.OnScrollListener {

    public static final String TAG = "ShopDetailActivity";
    public  static final int   SHOP_STATUS_CLOSE = 0;
    public  static final int   SHOP_STATUS_WRONG_NAME = 1;
    public  static final int   SHOP_STATUS_WRONG_ADDRESS = 2;
    public  static final int   SHOP_STATUS_WRONG_PHONENUMBER = 3;
    public  static final int   SHOP_STATUS_WRONG_OPEN_TIME = 4;
    public  static final int   SHOP_STATUS_WRONG_DISCOUNT_INFO = 5;

    private int m_nShopID = 0;
    private Shop m_clsShop = null;
    private ArrayList<Event> m_arrEvent = new ArrayList<Event>();

    private static ShopDetailActivity m_pInstance = null;

    private View m_vgMain = null;

    private NetworkImageView m_ivShop = null;
    private TextView  m_tvImgCnt = null;
    private HorizontalListView m_hlvShopImage = null;
    private ImageAdapter m_imgAdapter = null;

    private MyPriceAdapter m_adpPrice= null;
    private ArrayList<Product> m_lstPrices= new ArrayList<Product>();
    private SimpleSectionAdapter<Product> m_adpSectionPricesAdapter = null;

    private ListView m_lvPrice = null;

    private Image m_pSelectedImage = null;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        m_nShopID = getIntent().getIntExtra("shop_id", -1);

        m_pInstance = this;

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_shop_detail, menu);
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

    public static ShopDetailActivity getInstance() {
        return m_pInstance;
    }

    public Shop getShop() {
        return m_clsShop;
    }

    private void init() {

        m_vgMain = findViewById(R.id.lv_price);
        // 1. Create a Sectionizer
        m_lvPrice = (ListView)findViewById(R.id.lv_price);

        // add Header, Footer
        View header = getLayoutInflater().inflate(R.layout.view_shop_detail_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.view_shop_detail_footer, null, false);

        m_lvPrice.addHeaderView(header);
        m_lvPrice.addFooterView(footer);

        m_adpPrice = new MyPriceAdapter(this, R.layout.item_row_my_price, m_lstPrices);
        m_adpPrice.setShowProductName(true);
        m_adpPrice.setHeaderId(R.layout.item_row_my_price_header);
        m_lvPrice.setAdapter(m_adpPrice);
        m_lvPrice.setOnScrollListener(this);

        if(false) {
            // 2. Wrap your existing adapter with the SimpleSectionAdapter
            m_adpSectionPricesAdapter = new SimpleSectionAdapter<Product>(this,
                    m_adpPrice, R.layout.item_row_my_price, R.id.tv_product_name, new ProductSectionizer());

            // 3. Set the SimpleSectionAdapter to your ListView
            m_lvPrice.setAdapter(m_adpSectionPricesAdapter);
        }

        ImageButton btnUserAfter = (ImageButton)findViewById(R.id.btn_user_after);
        btnUserAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUseAfterActivity();
            }
        });

        ImageButton btnQA = (ImageButton)findViewById(R.id.btn_question_answer);
        btnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestionAndAnswerActivity();
            }
        });

        ImageButton btnWrongInfo = (ImageButton)findViewById(R.id.btn_wrong_info);
        btnWrongInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWrongInfoDlg();
            }
        });

        m_ivShop = (NetworkImageView) findViewById(R.id.iv_shop);

        initMenu();

        requestShopEventList();
        requestProductListOfShop(1);
        requestGetShop();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);
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

        TextView title = (TextView)view.findViewById(R.id.tv_title);

        if(m_clsShop != null) {
            title.setText(m_clsShop.getName());
        }
        else {
            title.setText("상점정보 보기");
        }

        ImageButton shareBtn = (ImageButton)view.findViewById(R.id.ib_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility utility = Utility.getInstance();
                utility.doShare(ShopDetailActivity.this, m_vgMain);
            }
        });

        ImageButton btnJim = (ImageButton)findViewById(R.id.ib_like);
        btnJim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestShopJim();
            }
        });
    }

    private void shopShopInformation(){
        initMenu();
        if(m_clsShop != null) {
            showPhotoNav();
            showBaseInfo();
            showDetailInfo();
        }

        showEvent();
        showPriceList();
    }

    private void showPhotoNav() {
        View llphoto = findViewById(R.id.ll_photo);
        if(m_clsShop.getShopSubImageArray().size() == 0) {
            llphoto.setVisibility(View.GONE);
        }
        else {
            if (m_clsShop.getImage() != null) {
                m_pSelectedImage = m_clsShop.getImage();
                m_ivShop.setImageUrl(m_clsShop.getImage().getImgUrl(), AppController.getInstance().getImageLoader());

                m_ivShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShopDetailActivity.this, ImageViewActivity.class);
                        Common.mTempList = new ArrayList<Image>();
                        Common.mTempList.add(m_clsShop.getImage());
                        intent.putExtra("idx", 0);
                        startActivity(intent);
                    }
                });
            }

            m_tvImgCnt = (TextView) findViewById(R.id.tv_img_cnt);

            m_hlvShopImage = (HorizontalListView) findViewById(R.id.hlv_shop_bar);
            m_imgAdapter = new ImageAdapter(this, R.layout.item_row_image, m_clsShop.getShopSubImageArray(), m_hlvShopImage);
            m_imgAdapter.setSelectedItemPostion(0);
            m_hlvShopImage.setAdapter(m_imgAdapter);

            m_hlvShopImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    m_imgAdapter.setSelectedItemPostion(i);
                    m_imgAdapter.notifyDataSetChanged();

                    setSelectPhoto(i);

                    if(m_pSelectedImage != null) {
                        Intent intent = new Intent(ShopDetailActivity.this, ImageViewActivity.class);
                        Common.mTempList = m_clsShop.getShopSubImageArray();
                        intent.putExtra("idx", m_imgAdapter.getSelectedItemPostion());
                        startActivity(intent);
                    }
                }
            });
        }

        View btnCall = (View)findViewById(R.id.btn_call_phone);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL",
                        Uri.parse("tel:" + m_clsShop.getPhoneNumber())));
                requestIncreaseShopCall(m_clsShop);
            }
        });

        View btnShowMap = findViewById(R.id.btn_show_map);
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopDetailActivity.this, MapActivity.class);
                intent.putExtra("shopName", m_clsShop.getName());
                intent.putExtra("shopLongtitude", m_clsShop.getShopLongtitude());
                intent.putExtra("shopLatitude", m_clsShop.getShopLatitude());
                startActivity(intent);
            }
        });

        setSelectPhoto(0);
    }

    private void showBaseInfo() {
        TextView tvShopname = (TextView)findViewById(R.id.tv_shop_name);
        tvShopname.setText(m_clsShop.getName());

        StarRatingBar ratingBar = (StarRatingBar)findViewById(R.id.rating_bar);
        ratingBar.setRating(m_clsShop.getShopRemarkLevel());

        TextView tvShopReviewCount = (TextView) findViewById(R.id.tv_review_cnt);
        tvShopReviewCount.setText(String.format("평가 %d건", m_clsShop.getShopRemarkCnt()));

        TextView tvPhoneNumber = (TextView)findViewById(R.id.tv_phone_number);
        tvPhoneNumber.setText(m_clsShop.getPhoneNumber());

        TextView tvAddress = (TextView)findViewById(R.id.tv_address);
        tvAddress.setText(m_clsShop.getAddress());
    }

    private void showDetailInfo() {
        TextView tvTime = (TextView)findViewById(R.id.tv_time);
        tvTime.setText(m_clsShop.getShopOpenTime());

        TextView tvRest = (TextView)findViewById(R.id.tv_rest);
        tvRest.setText(m_clsShop.getRestDay());

        TextView tvPark = (TextView)findViewById(R.id.tv_park);
        if(m_clsShop.isShopParkable() == true) {
            tvPark.setText("가능");
        }
        else {
            tvPark.setText("불가능");
        }
        TextView tvDiscountInfo= (TextView)findViewById(R.id.tv_description);
        if(m_clsShop.getShopDescriptionString() != null) {
            tvDiscountInfo.setText(Html.fromHtml(m_clsShop.getShopDescriptionString()));
        }

        View llPhoto = findViewById(R.id.ll_price_photo);
        if(m_clsShop.getShopPriceImageArray().size() >= 1) {
            llPhoto.setVisibility(View.VISIBLE);

            HorizontalListView hlvPriceImage = (HorizontalListView) findViewById(R.id.hlv_price_bar);
            ImageAdapter imgAdapter = new ImageAdapter(this, R.layout.item_row_image, m_clsShop.getShopPriceImageArray(), hlvPriceImage);
            hlvPriceImage.setAdapter(imgAdapter);

            hlvPriceImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ShopDetailActivity.this, ImageViewActivity.class);

                    Common.mTempList = m_clsShop.getShopPriceImageArray();
                    intent.putExtra("idx", i);
                    startActivity(intent);
                }
            });
        }
        else {
            llPhoto.setVisibility(View.GONE);
        }

        View llRoad = findViewById(R.id.ll_road);
        if(m_clsShop.getShopRoad() != null && m_clsShop.getShopRoad().equals("") == false) {
            llRoad.setVisibility(View.VISIBLE);

            tvDiscountInfo= (TextView)findViewById(R.id.tv_road);
            tvDiscountInfo.setText(Html.fromHtml(m_clsShop.getShopRoad()));
        }
        else {
            llRoad.setVisibility(View.GONE);
        }
    }


    private void showEvent() {
        View eventView = findViewById(R.id.ll_event);
        if(m_arrEvent.size() == 0) {
            eventView.setVisibility(View.GONE);
            return;
        }

        eventView.setVisibility(View.VISIBLE);

        View icMain = findViewById(R.id.ic_event);
        View eventMain = icMain.findViewById(R.id.rl_main);
        eventMain.setBackgroundColor(getResources().getColor(R.color.transparent));

        final Event event = m_arrEvent.get(0);

        NetworkImageView iv_event = (NetworkImageView) icMain.findViewById(R.id.iv_event);
        NetworkImageView iv_banner = (NetworkImageView) icMain.findViewById(R.id.iv_banner);
        TextView tv_explain = (TextView) icMain.findViewById(R.id.tv_explain);
        TextView tv_money = (TextView) icMain.findViewById(R.id.tv_money);
        TextView tv_summary = (TextView) icMain.findViewById(R.id.tv_summary);
        TextView tv_location = (TextView) icMain.findViewById(R.id.tv_location);
        TextView tv_shop_name = (TextView) icMain.findViewById(R.id.tv_shop_name);
        TextView tv_click_cnt = (TextView) icMain.findViewById(R.id.tv_click_cnt);

        iv_banner.setVisibility(View.GONE);

        if(event.getEventImage() != null) {
            eventMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShopDetailActivity.this, ImageViewActivity.class);
                    Common.mTempList = new ArrayList<Image>();
                    Common.mTempList.add(event.getEventImage());
                    intent.putExtra("idx", 0);
                    startActivity(intent);
                }
            });
            iv_event.setImageUrl(event.getEventImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }

        if (tv_shop_name != null) {
            tv_shop_name.setText(event.getShopName());
        }

        if(tv_location != null) {
           tv_location.setText(event.getSubLocation());
        }

        if(tv_summary != null) {
           tv_summary.setText(event.getEventSummary());
        }

        if(tv_money != null) {
          tv_money.setText(String.format("%d 원", event.getPrice()));
        }

        if(tv_explain != null) {
           tv_explain.setText(event.getEventExplain());
        }

        if(tv_click_cnt  != null) {
            tv_click_cnt.setText(String.format("%d", event.getEventClickCnt()));
        }
    }

    private void showPriceList() {

        View llPrice = findViewById(R.id.ll_price);
        View fl_divider_1 = findViewById(R.id.fl_divider_1);
        View fl_divider_2 = findViewById(R.id.fl_divider_2);

       if(m_lstPrices.size() == 0) {
           llPrice.setVisibility(View.GONE);
           if(m_arrEvent.size() == 0) {
               fl_divider_1.setVisibility(View.GONE);
           }
           else {
               fl_divider_2.setVisibility(View.GONE);
           }
       }
        else {
           llPrice.setVisibility(View.VISIBLE);
           fl_divider_1.setVisibility(View.VISIBLE);
           fl_divider_2.setVisibility(View.VISIBLE);
       }
    }

    private void setSelectPhoto(int idx) {
        ArrayList<Image> imageArrayList = m_clsShop.getShopSubImageArray();
        if(imageArrayList.size() > idx) {
            if (imageArrayList.get(idx) != null) {
                m_pSelectedImage = imageArrayList.get(idx);
            }
        }

        if(imageArrayList.size() != 0) {
            if (m_tvImgCnt != null) {
                String str = String.format("%d/%d", (idx + 1), imageArrayList.size());
                m_tvImgCnt.setText(str);
            }
        }
        else {
            if (m_tvImgCnt != null) {
                m_tvImgCnt.setText("0/0");
            }
        }
    }

    private void showLoginDialog() {
        LoginDialog dlg = new LoginDialog(this);
        dlg.show();
    }

    private void startWrongInfoDlg() {
        if(AppController.getInstance().isUserId == true) {
            showLoginDialog();
            return;
        }

        if(AppController.getInstance().isMyShop(m_clsShop.getShopOwnerID()) == true) {
            Toast.makeText(this, "당신의 숍입니다. 마이페이지에서 관리해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        InputWrongInfoDialog dlg = new InputWrongInfoDialog(ShopDetailActivity.this, m_clsShop.getId());
        dlg.show();
    }

    private void startUseAfterActivity(){
        if(AppController.getInstance().isUserId == true) {
            showLoginDialog();
            return;
        }

        if(AppController.getInstance().isMyShop(m_clsShop.getShopOwnerID()) == true) {
            Toast.makeText(this, "당신의 숍입니다.  마이페이지에서 관리해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ShowUserAfterActivity.class);
        intent.putExtra("shop_id", m_clsShop.getId());
        startActivity(intent);
    }

    private void startQuestionAndAnswerActivity(){
        if(AppController.getInstance().isMyShop(m_clsShop.getShopOwnerID()) == true) {
            Utility.showCenterToast(this, "당신의 숍입니다.  마이페이지에서 관리해주세요.", Toast.LENGTH_SHORT);
            return;
        }

        if(m_clsShop.isShopQuestionable() == false) {
            Utility.showCenterToast(this, "Q&A준비중인 상점입니다.전화로 문의해 주세요.", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(this, ShowQuestionAndAnswer.class);
        intent.putExtra("shop_id", m_clsShop.getId());
        startActivity(intent);
    }

    private void requestGetShop(){

        String url = ServerAPIPath.API_GET_SHOP;

        if(m_nShopID <= 0) {
            Toast.makeText(this, "숍이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        url += "?shop_id="+m_nShopID;
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Utility.hideWaitingDlg();
                        try {
                            JSONObject dataObject = (JSONObject)o;

                            m_clsShop = new Shop(dataObject);

                            shopShopInformation();
                            requestShopEventList();

                            requestIncreaseShopClick(m_clsShop);

                        } catch (Exception e) {
                            Logger.e(TAG, "responseGetShop - JSONException : " + e.getMessage());
                            Toast.makeText(ShopDetailActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(ShopDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void requestIncreaseShopClick(Shop event) {

        String url = ServerAPIPath.API_POST_SHOP_CLICK;

        Map<String, String> params = new HashMap<String, String>();
        params.put("shop_id", String.format("%d", event.getId()));

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

    public void requestIncreaseShopCall(Shop event) {
        String url = ServerAPIPath.API_POST_SHOP_CALL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("shop_id", String.format("%d", event.getId()));

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

    private void requestShopJim() {
        String url = ServerAPIPath.API_POST_SHOP_JIM;

        Map<String, String> params = new HashMap<String, String>();

        if(m_clsShop != null) {
            params.put("shop_id", String.format("%d", m_clsShop.getId()));
        }
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(ShopDetailActivity.this, ResponseMessage.SUCCESS_JIM, Toast.LENGTH_SHORT).show();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(ShopDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestShopEventList()  {

        String url = ServerAPIPath.API_GET_SHOP_EVENT_LIST;
        int shop_id = m_nShopID;
        url+="?shop_id="+shop_id;
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            Utility.hideWaitingDlg();
                            m_arrEvent.clear();
                            JSONObject jsonObject = (JSONObject)o;
                            JSONArray dataArray = jsonObject.getJSONArray("list");


                            for(int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Event event = new Event(dataObject);

                                m_arrEvent.add(event);
                            }

                        } catch (Exception e) {
                            Toast.makeText(ShopDetailActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                            Logger.e(TAG, "responseShopEventList - JSONException : " + e.getMessage());
                        }
                        shopShopInformation();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(ShopDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

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
        requestProductListOfShop(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestProductListOfShop(1);
    }

    private void requestProductListOfShop(int pageNum){
        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvPrice.setSelection(0);
            m_lstPrices.clear();
            m_lstPrices.add(new Product());
        }


        String url = ServerAPIPath.API_GET_PRODUCT_LIST;
        int shop_id = m_nShopID;
        url+="?shop_id="+shop_id;
        url+="&page_num="+pageNum;

        Utility.showWaitingDlg(this);

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Utility.hideWaitingDlg();
                        try {
                            JSONObject jsonObject = (JSONObject)o;
                            int total = jsonObject.getInt("total");
                            int page_num = jsonObject.getInt("page");
                            m_nPageItemCnt = jsonObject.getInt("rows_per_page");

                            JSONArray dataArray = jsonObject.getJSONArray("list");

                            if(dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    Product product = new Product(dataObject);

                                    m_lstPrices.add(product);
                                }

                                m_adpPrice.notifyDataSetChanged();
                            }
                            else {
                                if(page_num == 1) {
                                    m_lstPrices.clear();
                                }
                            }

                            shopShopInformation();

                        } catch (Exception e) {
                            Logger.e(TAG, "responseProductListOfShop - JSONException : " + e.getMessage());
                            Toast.makeText(ShopDetailActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(ShopDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

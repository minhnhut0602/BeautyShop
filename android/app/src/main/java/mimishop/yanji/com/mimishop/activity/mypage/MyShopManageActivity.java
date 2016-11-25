package mimishop.yanji.com.mimishop.activity.mypage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.astuetz.PagerSlidingTabStrip;
import com.devsmart.android.ui.HorizontalListView;
//import com.facebook.android.Util;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.activity.shopinfo.ShopDetailActivity;
import mimishop.yanji.com.mimishop.adapter.MyPriceAdapter;
import mimishop.yanji.com.mimishop.adapter.QuestionAnswerAdapter;
import mimishop.yanji.com.mimishop.adapter.ShopImageAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.AddProductDialog;
import mimishop.yanji.com.mimishop.dialog.CameraAndPhotoDialog;
import mimishop.yanji.com.mimishop.dialog.FindShopPasswordDialog;
import mimishop.yanji.com.mimishop.dialog.WriteShopAnswerDialog;
import mimishop.yanji.com.mimishop.fragment.MyPageFragment;
import mimishop.yanji.com.mimishop.modal.Answer;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.Product;
import mimishop.yanji.com.mimishop.modal.Question;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.SelectPhotoManager;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CustomEditText;

public class MyShopManageActivity extends Activity  implements  ViewPager.OnPageChangeListener, View.OnClickListener, AbsListView.OnScrollListener{

    public static final String TAG = "MyShopManageActivity";

    private static final int  MAX_IMAGE_CNT  = 10;
    public static final  int  REQ_CODE_SELECT_SHOP_SUB_IMAGE = 0; // 0 ~ 9
    public static final  int  REQ_CODE_SELECT_SHOP_MAIN_IMAGE = 10;
    public static final  int  REQ_CODE_SELECT_PRICE_IMAGE = 11; // 11 ~ 20
    public static final  int  REQ_CODE_SELECT_SHOP_EVENT_IMAGE = 21;
    private static final int  UPLOAD_FILE_CNT = 22;

    private static final String []ARR_MENU = {"상점설정", "상점문의", "가격정보", "상점배너"};
    private static final int    []ARR_VIEW = {R.layout.view_shop_setting, R.layout.view_shop_question_answer, R.layout.view_shop_price_setting, R.layout.view_shop_event_setting};
    private static final String SHOP_MAIN_IMAGE = "shop.jpg";
    private static final String SHOP_MAIN_SUB_IMAGE = "shopXX.jpg";
    private static final String EVENT_IMAGE = "event.jpg";
    private static final String PRICE_IMAGE = "priceXX.jpg";

    private static MyShopManageActivity m_pInstance = null;

    private Shop m_pShop = null;

    private View m_vLogin;
    private View m_vManage;

    private View m_vButtons;

    // slide menu
    private DrawerLayout m_dlDrawerLayout = null;
    private View         m_icSliderMenu = null;

    // slidermenu
    private View m_icLogOut = null;
    private ListView m_lvCategory = null;
    private ArrayList<Category> m_arrCategory = new ArrayList<Category>();
    private int m_nCategoryIdx = -1;

    // view login
    private CheckBox m_cbAutoLogin = null;
    private EditText m_etLoginShopID = null;
    private EditText m_etLoginPW = null;

    // view main
    private ViewPager mViewPager = null;
    private ShopManageViewPagerAdapter mPagerAdapter = null;
    private PagerSlidingTabStrip mIndicator = null;

    // view setting
    private NetworkImageView   m_nivSelectedImg = null;
    private ImageView          m_ivSelectedImg = null;
    private HorizontalListView m_hlvShopBar = null;
    private ArrayList<Image>   m_arrShopImage = new ArrayList<Image>();
    private ShopImageAdapter   m_adpImageAdapter = null;
    private CustomEditText     m_etShopName = null;
    private CustomEditText     m_etShopPhoneNumber = null;
    private CustomEditText     m_etShopAddress = null;
    private CustomEditText     m_etShopOpenTime = null;
    private CustomEditText     m_etShopRestTime = null;
    private EditText           m_etShopRoad = null;
    private EditText           m_etShopDescription = null;
    private CheckBox           m_cbShopParkEnable = null;
    private CheckBox           m_cbShopParkDisable = null;

    private boolean            []m_isRegister = new boolean[5];

    // view question
    private ExpandableListView m_lvQuestionAnswer = null;
    private QuestionAnswerAdapter m_adtQuestionAnswerAdapter = null;
    private ArrayList<Question> m_arrGroupList = new ArrayList<Question>();
    private ArrayList<ArrayList<Answer>> m_arrChildList = new ArrayList<ArrayList<Answer>>();
    private RadioGroup  m_rgQuestionable = null;

    // view price
    private ListView m_lvProduct = null;
    private MyPriceAdapter m_adpPrice= null;
    private ArrayList<Product> m_lstPrices = new ArrayList<Product>();
    private HorizontalListView m_hlvPriceImageBar = null;
    private ArrayList<Image>   m_arrPriceImage = new ArrayList<Image>();
    private ShopImageAdapter   m_adpPriceImageAdapter = null;

    // view event
    private ImageView mIvPhoto;
    private NetworkImageView mIvNetPhoto;
    private CustomEditText   m_etEventUserName;
    private CustomEditText   m_etEventTitle;
    private EditText         m_etEventContent;
    private ArrayList<Event> m_arrShopEventList = new ArrayList<Event>();

    private CameraAndPhotoDialog m_dlgCameraAndPhoto = null;

    private boolean m_isSetCameraBitmap = true;
    private Image   m_pCameraBitmap = null;
    private int     m_nActivityReqCode = 0;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    private int m_nActivityPageNum = 0;

    boolean m_isLoginned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop_manage);

        m_pInstance = this;

        init();

        // blue screen
        if(AppController.getInstance().getCurrentUser() == null) {
            Common.mDeviceId = Utility.getInstance().getDeviceId(this);
            requestInit();
        }
        else {
            requestMyShop();
        }
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
                        Toast.makeText(MyShopManageActivity.this, err, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void responseInit(Object s) {
        if(s == null) {
            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_NO_RESULT, Toast.LENGTH_SHORT).show();
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

            requestMyShop();
        }
        catch (Exception e) {
            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public static MyShopManageActivity getInstance(){return m_pInstance;}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_my_shop_manage, menu);
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
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        m_nActivityPageNum = position;
    }

    @Override
    public void onClick(View view) {

        for(int i = 0; i < m_isRegister.length; i++) {
            m_isRegister[i] = false;
        }

        Utility.hideSoftKeyboard(this);

        switch (view.getId()) {
            case R.id.btn_register_shop_image:
                m_isRegister[0] = true;
                uploadShopImages();
                break;
            case R.id.btn_modify_shop_base_info:
            case R.id.btn_register_shop_base_info:
                m_isRegister[1] = true;
                if(m_etShopAddress.getText() != null && m_etShopAddress.getText().toString().equals("") != true) {
                    requestGetLatLngFromAddress(m_etShopAddress.getText().toString());
                }
                else {
                    requestModifyShop();
                }
                break;
            case R.id.btn_modify_shop_detail:
            case R.id.btn_register_shop_detail:
                m_isRegister[2] = true;
                requestModifyShop();
                break;
            case R.id.btn_modify_shop_road:
            case R.id.btn_register_shop_road:
                m_isRegister[3] = true;
                requestModifyShop();
            break;
            case R.id.btn_modify_price_image:
            case R.id.btn_register_price_image:
                m_isRegister[4] = true;
                uploadShopPriceImages();
                break;
            case R.id.btn_register_shop_event_info:
            case R.id.btn_modify_shop_event_info:
                Event event = getMyShopEvent();
                if(event != null && event.getEventImage() != null && event.getEventImage().getFile() != null) {
                    uploadShopEventImage();
                }
                else {
                    requestRegisterEvent();
                }
               break;
        }
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_my_shop_manage));

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

        m_vButtons = view.findViewById(R.id.ll_buttons);
        m_vButtons.setVisibility(View.GONE);

        ImageButton btnPreview = (ImageButton)view.findViewById(R.id.ib_preview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShopDetailActivity(m_pShop);
            }
        });

        ImageButton btnShowSlid= (ImageButton)view.findViewById(R.id.ib_menu);
        btnShowSlid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_dlDrawerLayout.isDrawerOpen(m_icSliderMenu) == true) {
                    m_dlDrawerLayout.closeDrawer(m_icSliderMenu);
                }
                else {
                    m_dlDrawerLayout.openDrawer(m_icSliderMenu);
                }
            }
        });
    }

    private void startShopDetailActivity(Shop shop){

        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra("shop_id", shop.getId());
        startActivity(intent);
    }

    private void initKeyboardTouch() {
        View view = findViewById(R.id.rl_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(MyShopManageActivity.this);
            }
        });
    }

    private void initSliderMenu() {
        m_dlDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        m_icSliderMenu = findViewById(R.id.ic_slide_menu);

        m_icLogOut = m_icSliderMenu.findViewById(R.id.ll_log_out);
        m_lvCategory = (ListView)m_icSliderMenu.findViewById(R.id.lv_category);

        m_icLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dlDrawerLayout.closeDrawer(m_icSliderMenu);
                requestLogoutShop();
            }
        });
        Category category = new Category();
        category.setName("내 상점 목록");
        m_arrCategory.add(category);
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

                if (position == 0) {
                    holder.iv_icon.setImageResource(R.drawable.ic_my_shop_selector);
                    holder.iv_icon.setVisibility(View.VISIBLE);
                }

                return convertView;
            }
        });

        m_lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                m_dlDrawerLayout.closeDrawer(m_icSliderMenu);
            }
        });
    }

    private void init() {

        initMenu();

        initSliderMenu();

        m_vLogin = findViewById(R.id.ic_shop_manage_logo);
        m_vLogin.setVisibility(View.GONE);
        m_vManage = findViewById(R.id.ll_main);
        m_vManage.setVisibility(View.GONE);

        m_dlgCameraAndPhoto = new CameraAndPhotoDialog(this);

        for(int i = 0; i< MAX_IMAGE_CNT; i++) {
            m_arrPriceImage.add(new Image());
        }

        for(int i = 0; i< MAX_IMAGE_CNT; i++) {
            m_arrShopImage.add(new Image());
        }

        initMain();
        initLogin();

        initKeyboardTouch();
    }

    private void showPage(){

        if(m_pShop == null) {
            return;
        }

        if(m_isLoginned == true || m_pShop.isShopAutoLogin() == true) {
            showMainView();
        }
        else {
            showLoginView();
        }
    }

    private void showMainView() {
        m_vLogin.setVisibility(View.GONE);
        m_vManage.setVisibility(View.VISIBLE);
        m_vButtons.setVisibility(View.VISIBLE);

        showShopSetting();
        showShopQuestionAndAnswer();
        showShopPriceSetting();
        showShopEventSetting();
    }

    private void showLoginView() {
        m_vLogin.setVisibility(View.VISIBLE);
        m_vManage.setVisibility(View.GONE);
        m_vButtons.setVisibility(View.GONE);
        m_etLoginShopID.setText(m_pShop.getShopID());
    }

    private void initLogin() {
        m_cbAutoLogin = (CheckBox)findViewById(R.id.cb_autologin);

        m_etLoginShopID = (EditText)findViewById(R.id.et_logo_shop_id);
        m_etLoginPW = (EditText)findViewById(R.id.et_logo_shop_pw);

        ImageView imageView = (ImageView)findViewById(R.id.iv_name_exit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_etLoginShopID.setText("");
            }
        });
        imageView = (ImageView)findViewById(R.id.iv_pw_exit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_etLoginPW.setText("");
            }
        });

        Button login = (Button)findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(MyShopManageActivity.this);
                requestLoginShop();
            }
        });

        TextView btnFindPasswd = (TextView)findViewById(R.id.tv_find_shop_password);
        btnFindPasswd.setPaintFlags(btnFindPasswd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnFindPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindShopPasswordDialog dlg = new FindShopPasswordDialog(MyShopManageActivity.this, m_pShop.getId());
                dlg.show();
            }
        });
    }


    private void initMain() {
        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        mIndicator = (PagerSlidingTabStrip)findViewById(R.id.indicator);
        mIndicator.setOnPageChangeListener(this);

        mPagerAdapter = new ShopManageViewPagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mViewPager);
    }


    private void initShopSetting() {
        // imagelist
        m_nivSelectedImg = (NetworkImageView)findViewById(R.id.niv_selected_img);
        m_ivSelectedImg = (ImageView)findViewById(R.id.iv_selected_img);
        m_nivSelectedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCameraAndPhotoDlg(SHOP_MAIN_IMAGE);
            }
        });
        m_ivSelectedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraAndPhotoDlg(SHOP_MAIN_IMAGE);
            }
        });

        m_hlvShopBar = (HorizontalListView)findViewById(R.id.hlv_shop_bar);

        m_arrShopImage.clear();

        for(int i = 0; i< MAX_IMAGE_CNT; i++) {
            m_arrShopImage.add(new Image());
        }

        m_adpImageAdapter = new ShopImageAdapter(this, R.layout.item_row_shop_image, m_arrShopImage);
        m_hlvShopBar.setAdapter(m_adpImageAdapter);

        m_hlvShopBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v instanceof ViewGroup) {
                    ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        m_hlvShopBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                boolean isJustRemove = false;
                int size = m_arrShopImage.size();
                for (int j = 0; j < size; j++) {
                    Image image = m_arrShopImage.get(j);
                    if(image.isJustRemove() == true) {
                        image.setJustRemove(false);
                        isJustRemove = true;
                    }
                }

                if(isJustRemove == true) {
                    return;
                }

                Image image = m_arrShopImage.get(i);
                if (image.getId() <= 0 && image.getBitmap() == null) {
                    String imageName = SHOP_MAIN_SUB_IMAGE;
                    imageName = imageName.replace("XX", String.format("%d",i));
                    showCameraAndPhotoDlg(imageName);
                }
            }
        });

        m_etShopName = (CustomEditText)findViewById(R.id.et_shop_name);
        m_etShopPhoneNumber = (CustomEditText)findViewById(R.id.et_shop_phonenumber);
        m_etShopAddress = (CustomEditText)findViewById(R.id.et_shop_address);
        m_etShopOpenTime = (CustomEditText)findViewById(R.id.et_shop_open_time);
        m_etShopRestTime = (CustomEditText)findViewById(R.id.et_shop_rest_time);
        m_cbShopParkEnable = (CheckBox)findViewById(R.id.cb_shop_park_enable);
        m_cbShopParkDisable = (CheckBox)findViewById(R.id.cb_shop_park_disable);
        m_cbShopParkEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_cbShopParkDisable.setChecked(false);
                m_cbShopParkEnable.setChecked(true);
            }
        });
        m_cbShopParkDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_cbShopParkEnable.setChecked(false);
                m_cbShopParkDisable.setChecked(true);
            }
        });
        m_cbShopParkEnable.setChecked(false);
        m_cbShopParkDisable.setChecked(true);

        m_etShopDescription= (EditText)findViewById(R.id.et_shop_description);
        m_etShopRoad = (EditText)findViewById(R.id.et_shop_road);

        Button btnRegister = (Button)findViewById(R.id.btn_register_shop_image);
        btnRegister.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_modify_shop_base_info);
        btnRegister.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_register_shop_base_info);
        btnRegister.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_register_shop_detail);
        btnRegister.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_modify_shop_detail);
        btnRegister.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_register_shop_road);
        btnRegister.setOnClickListener(this);
        btnRegister = (Button)findViewById(R.id.btn_modify_shop_road);
        btnRegister.setOnClickListener(this);

        showShopSetting();

        View content = findViewById(R.id.ll_content);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(MyShopManageActivity.this);
            }
        });
    }

    private void showShopSetting() {
        Shop myShop = m_pShop;

        if(myShop == null) {
            return;
        }

        if(m_nivSelectedImg == null) {
            return;
        }

        ArrayList<Image> arrayList = myShop.getShopSubImageArray();
        m_arrShopImage.clear();
        for(int i = 0; i < MAX_IMAGE_CNT; i++) {
            Image image = new Image();
            if(i < arrayList.size()) {
                image = arrayList.get(i);
                image.setDeleted(false);
            }
            m_arrShopImage.add(image);
        }

        if(m_isSetCameraBitmap == false && m_pCameraBitmap != null) {
            if(m_nActivityReqCode == REQ_CODE_SELECT_SHOP_MAIN_IMAGE) {
                m_pShop.setShopMainImg(m_pCameraBitmap);
            }
            else if(m_nActivityReqCode >= REQ_CODE_SELECT_SHOP_SUB_IMAGE && m_nActivityReqCode < REQ_CODE_SELECT_SHOP_MAIN_IMAGE){
                m_arrShopImage.set(m_nActivityReqCode, m_pCameraBitmap);
            }

            m_isSetCameraBitmap = true;
            m_pCameraBitmap = null;
        }


        if(myShop.getImage() != null) {
            m_nivSelectedImg.setImageUrl(myShop.getImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }

        m_etShopName.setText(myShop.getName());

        if(myShop.getPhoneNumber() != null) {
            m_etShopPhoneNumber.setText(myShop.getPhoneNumber());
        }

        if(myShop.getAddress() != null) {
            m_etShopAddress.setText(myShop.getAddress());
        }

        if(myShop.getShopOpenTime() != null) {
            m_etShopOpenTime.setText(myShop.getShopOpenTime());
        }

        if(myShop.getRestDay() != null) {
            m_etShopRestTime.setText(myShop.getRestDay());
        }

        if(myShop.getShopRoad() != null) {
            m_etShopRoad.setText(myShop.getShopRoad());
        }

        if(myShop.getShopDescriptionString() != null) {
            m_etShopDescription.setText(Html.fromHtml(myShop.getShopDescriptionString()));
        }

        if(myShop.isShopParkable() == true) {
            m_cbShopParkEnable.setChecked(true);
        }
        else {
            m_cbShopParkEnable.setChecked(false);
        }
    }

    private void showCameraAndPhotoDlg(String capturePath) {
        m_dlgCameraAndPhoto = new CameraAndPhotoDialog(this);
        m_dlgCameraAndPhoto.setTitle("대표이미지 등록");
        m_dlgCameraAndPhoto.setExplain("대표이미지를 등록해주세요.");
        m_dlgCameraAndPhoto.setCaptureFileName(capturePath);
        m_dlgCameraAndPhoto.show();
    }

    private void initShopQuestion() {
        m_lvQuestionAnswer = (ExpandableListView)findViewById(R.id.lv_questionanswer);
        m_adtQuestionAnswerAdapter = new QuestionAnswerAdapter(this, m_arrGroupList, m_arrChildList,
                R.layout.item_group_question_answer, R.layout.item_row_question_answer);
        m_adtQuestionAnswerAdapter.setEnableWriteAnswer();
        m_lvQuestionAnswer.setAdapter(m_adtQuestionAnswerAdapter);

        // 그룹 클릭 했을 경우 이벤트
        m_lvQuestionAnswer.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        m_lvQuestionAnswer.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });

        // 그룹이 닫힐 경우 이벤트
        m_lvQuestionAnswer.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // 그룹이 열릴 경우 이벤트
        m_lvQuestionAnswer.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        m_rgQuestionable = (RadioGroup)findViewById(R.id.rg_shop_status);
        m_rgQuestionable.setEnabled(false);

        m_rgQuestionable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) m_rgQuestionable.getChildAt(0);
                m_pShop.setShopQuestionable(radioButton.isChecked());

                showShopQuestionAndAnswer();
            }
        });

        m_lvQuestionAnswer.setOnScrollListener(this);

        showShopQuestionAndAnswer();
        resetQuestionPage();
    }

    private void showShopQuestionAndAnswer() {
        Shop myShop = m_pShop;

        if(myShop == null) {
            return;
        }

        if(m_lvQuestionAnswer == null) {
            return;
        }

        if(myShop.isShopQuestionable() == false) {
            m_lvQuestionAnswer.setVisibility(View.GONE);

            RadioButton radioButton = (RadioButton)m_rgQuestionable.getChildAt(1);
            radioButton.setChecked(true);
        }
        else {
            m_lvQuestionAnswer.setVisibility(View.VISIBLE);
            RadioButton radioButton = (RadioButton)m_rgQuestionable.getChildAt(0);
            radioButton.setChecked(true);
        }
    }

    public  void createComment(final Question question){
        if(false) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View layout = inflater.inflate(R.layout.dlg_write_answer, (ViewGroup) findViewById(R.id.root));
            AlertDialog.Builder builder = new AlertDialog.Builder(MyShopManageActivity.this);
            builder.setTitle("댓글 달기");
            builder.setView(layout);

            final AlertDialog fileCreateDlg = builder.create();

            Button btnOK = (Button) layout.findViewById(R.id.btn_answer);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = (EditText) layout.findViewById(R.id.et_comment);
                    requestWriteAnswer(question, null, editText.getText().toString());
                    fileCreateDlg.dismiss();
                }
            });

            fileCreateDlg.show();
        }
        else {
            final WriteShopAnswerDialog dialog = new WriteShopAnswerDialog(this, true, question, null);
            dialog.show();
        }
    }

    public void removeAnswer(Answer answer){
        requestRemoveAnswer(answer);
    }

    public void modifyAnswer(final  Question question, final Answer answer){
        final WriteShopAnswerDialog dialog = new WriteShopAnswerDialog(this, false, question, answer);
        dialog.show();
    }

    private Dialog createWriteCommentDialog(boolean create) {
        final Dialog dialog = new Dialog(MyShopManageActivity.this);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        dialog.getWindow().setAttributes(lpWindow);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_write_answer);

        ImageButton btnExit = (ImageButton)dialog.findViewById(R.id.ib_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView tvTitle = (TextView)dialog.findViewById(R.id.tv_title);
        if(create == true) {
            tvTitle.setText("댓글 달기");
        }
        else {
            tvTitle.setText("댓글 변경");
        }
        return dialog;
    }


    public void addPrice(Product product) {
        m_lstPrices.add(product);
        m_adpPrice.notifyDataSetChanged();
        requestRegisterProduct(product);
    }

    public void modifyProduct(Product product) {
        requestRegisterProduct(product);
    }

    private void initShopPriceSetting() {

        m_lvProduct = (ListView)findViewById(R.id.lv_shop_product);

        // Header, Footer 생성 및 등록
        View header = getLayoutInflater().inflate(R.layout.view_shop_price_setting_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.view_shop_price_setting_footer, null, false);

        m_lvProduct.addHeaderView(header);
        m_lvProduct.addFooterView(footer);

        // adapter
        m_adpPrice = new MyPriceAdapter(this, R.layout.item_row_my_price, m_lstPrices);
        m_lvProduct.setAdapter(m_adpPrice);
        m_adpPrice.setShowProductName(true);

        m_lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddProductDialog dialog = new AddProductDialog(MyShopManageActivity.this, m_lstPrices.get(i - 1));
                dialog.show();
            }
        });

        m_lvProduct.setOnScrollListener(this);

        Button btnSaveInfo = (Button)findViewById(R.id.btn_modify_price_image);
        btnSaveInfo.setOnClickListener(this);
        btnSaveInfo = (Button)findViewById(R.id.btn_register_price_image);
        btnSaveInfo.setOnClickListener(this);

        Button btnAddProduct = (Button)findViewById(R.id.btn_add_product);
        btnAddProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddProductDialog dialog = new AddProductDialog(MyShopManageActivity.this);
                dialog.show();
            }
        });

        m_arrPriceImage.clear();

        for(int i = 0; i< 10; i++) {
            m_arrPriceImage.add(new Image());
        }

        m_hlvPriceImageBar = (HorizontalListView)findViewById(R.id.hlv_product_image_bar);
        m_adpPriceImageAdapter = new ShopImageAdapter(this, R.layout.item_row_shop_image, m_arrPriceImage);
        m_hlvPriceImageBar.setAdapter(m_adpPriceImageAdapter);

        m_hlvPriceImageBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v instanceof ViewGroup) {
                    ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        m_hlvPriceImageBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                boolean isJustRemove = false;
                int size = m_arrPriceImage.size();
                for (int j = 0; j < size; j++) {
                    Image image = m_arrPriceImage.get(j);
                    if(image.isJustRemove() == true) {
                        image.setJustRemove(false);
                        isJustRemove = true;
                    }
                }

                if(isJustRemove == true) {
                    return;
                }

                Image image = m_arrPriceImage.get(i);
                if (image.getId() <= 0 && image.getBitmap() == null) {
                    String imageName = PRICE_IMAGE;
                    imageName = imageName.replace("XX", String.format("%d", i));
                    showCameraAndPhotoDlg(imageName);
                }
                if( image.isJustRemove()  == true) {
                    image.setJustRemove(false);
                }
            }
        });

        showShopPriceSetting();
        resetPricePage();
    }

    private void showShopPriceSetting() {
        Shop myShop = m_pShop;

        if(myShop == null) return;
        if(m_lvProduct == null) return;

        ArrayList<Image> arrayList = myShop.getShopPriceImageArray();
        m_arrPriceImage.clear();
        for(int i = 0; i < MAX_IMAGE_CNT; i++) {
            Image image = new Image();
            if(i < arrayList.size()) {
                image = arrayList.get(i);
            }
            m_arrPriceImage.add(image);
        }

        if(m_isSetCameraBitmap == false && m_pCameraBitmap != null) {
            if(m_nActivityReqCode >= REQ_CODE_SELECT_PRICE_IMAGE && m_nActivityReqCode < REQ_CODE_SELECT_SHOP_EVENT_IMAGE) {
                int idx = (m_nActivityReqCode - REQ_CODE_SELECT_PRICE_IMAGE);
               m_arrPriceImage.set(idx, m_pCameraBitmap);
            }

            m_isSetCameraBitmap = true;
            m_pCameraBitmap = null;
        }
    }

    private View.OnClickListener m_onSelectEventImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            m_dlgCameraAndPhoto = new CameraAndPhotoDialog(MyShopManageActivity.this);
            m_dlgCameraAndPhoto.setCaptureFileName(EVENT_IMAGE);
            SelectPhotoManager manager = new SelectPhotoManager(MyShopManageActivity.this);
            manager.startGallaryActivity(MyShopManageActivity.this, MyShopManageActivity.REQ_CODE_SELECT_SHOP_EVENT_IMAGE);
        }
    };

    private void initShopEventSetting() {
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo);
        mIvNetPhoto = (NetworkImageView) findViewById(R.id.iv_net_photo);

        mIvNetPhoto.setOnClickListener(m_onSelectEventImageListener);
        mIvPhoto.setOnClickListener(m_onSelectEventImageListener);

        Button btnOK = (Button) findViewById(R.id.btn_register_shop_event_info);
        btnOK.setOnClickListener(this);
        btnOK = (Button) findViewById(R.id.btn_modify_shop_event_info);
        btnOK.setOnClickListener(this);

        m_etEventUserName = (CustomEditText) findViewById(R.id.et_user_name);
        m_etEventTitle = (CustomEditText) findViewById(R.id.et_summary);
        m_etEventContent = (EditText) findViewById(R.id.et_explain);

        showShopEventSetting();
        requestShopEventList();

        View content = findViewById(R.id.ll_content);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(MyShopManageActivity.this);
            }
        });
    }

    private Event getMyShopEvent() {
        if(m_arrShopEventList.size() == 0) {
            return null;
        }

        return m_arrShopEventList.get(0);
    }

    private void showShopEventSetting() {
        if(m_arrShopEventList.size() == 0) {
            return;
        }

        if(mIvPhoto == null) {
            return;
        }

        Event event = m_arrShopEventList.get(0);

        if(m_isSetCameraBitmap == false && m_pCameraBitmap != null) {
            if(m_nActivityReqCode == REQ_CODE_SELECT_SHOP_EVENT_IMAGE) {
                event.setEventImage(m_pCameraBitmap);
            }

            m_isSetCameraBitmap = true;
            m_pCameraBitmap = null;
        }

        if(event.getEventImage() != null) {
            mIvNetPhoto.setImageUrl(event.getEventImage().getImgUrl(), AppController.getInstance().getImageLoader());
            mIvNetPhoto.setVisibility(View.VISIBLE);
            mIvPhoto.setVisibility(View.GONE);
        }
        else {
            mIvNetPhoto.setVisibility(View.GONE);
            mIvPhoto.setVisibility(View.VISIBLE);
        }

        m_etEventTitle.setText(event.getEventSummary());
        m_etEventContent.setText(event.getEventExplain());

        User user = AppController.getInstance().getCurrentUser();
        m_etEventUserName.setText(user.getUserID());
    }

    private int getUploadRequestCode() {

        String name = m_dlgCameraAndPhoto.getCaptureFileName();

        int reqCode = REQ_CODE_SELECT_SHOP_MAIN_IMAGE;
        if(name.contains("shop") == true && name.equals(SHOP_MAIN_IMAGE) == false) {
            int idx = name.indexOf(".jpg");
            reqCode = Integer.parseInt(name.substring(4, idx));
        }
        else if(name.equals(EVENT_IMAGE) == true) {
            reqCode = REQ_CODE_SELECT_SHOP_EVENT_IMAGE;
        }
        else if(name.contains("price") == true){
            int idx = name.indexOf(".jpg");
            reqCode = Integer.parseInt(name.substring(5, idx));
            reqCode = reqCode + REQ_CODE_SELECT_PRICE_IMAGE;
        }

        return reqCode;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SessionManager.getInstance().saveHeader(this);
        if(m_dlgCameraAndPhoto.getCapturePath() != null) {
            outState.putString("capture_path", m_dlgCameraAndPhoto.getCapturePath());
        }

        if(m_dlgCameraAndPhoto.getCaptureFileName() != null) {
            outState.putString("capture_name", m_dlgCameraAndPhoto.getCaptureFileName());
        }

        outState.putInt("cur_tap_idx", mViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SessionManager.getInstance().restoreHeader(this);
        String str = savedInstanceState.getString("capture_path");
        if(str != null) {
            m_dlgCameraAndPhoto.setCapturePath(str);
        }

        str = savedInstanceState.getString("capture_name");
        if(str != null) {
            m_dlgCameraAndPhoto.setCaptureFileName(str);
        }

        int idx = savedInstanceState.getInt("cur_tap_idx");
        mViewPager.setCurrentItem(idx);

        MainActivity.RELOAD_FRAG_IDX = MainActivity.FRAG_KIND_MYPAGE;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK)
        {
            Bitmap bitmap = null;
            String path = null;
            if(requestCode == CameraAndPhotoDialog.REQ_CODE_GALLARY_IMAGE || requestCode == REQ_CODE_SELECT_SHOP_EVENT_IMAGE) {
                try {
                    bitmap = m_dlgCameraAndPhoto.getPhotoFromUri(data.getData());
                    path = m_dlgCameraAndPhoto.getGallaryPath();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == CameraAndPhotoDialog.REQ_CODE_CAPTURE_IMAGE) {

                try {
                    path = m_dlgCameraAndPhoto.getCapturePath();
                    bitmap = m_dlgCameraAndPhoto.getPhotoFromPath(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int reqCode = getUploadRequestCode();
            if(bitmap != null)  {
                m_nActivityReqCode = reqCode;
                if(reqCode == REQ_CODE_SELECT_SHOP_EVENT_IMAGE) {
                    File file = new File(path);

                    Event event = getMyShopEvent();
                    if(event == null) {
                        event = new Event();
                        m_arrShopEventList.add(event);
                    }
                    Image image = event.getEventImage();
                    if(image == null) {
                        image = new Image();
                    }

                    image.setFile(file);
                    event.setEventImage(image);


                    if(mIvPhoto != null) {
                        mIvPhoto.setImageBitmap(bitmap);
                        mIvPhoto.setVisibility(View.VISIBLE);
                        mIvNetPhoto.setVisibility(View.GONE);
                    }
                    else {
                        m_isSetCameraBitmap = false;
                        m_pCameraBitmap = image;
                    }
                }
                else if (reqCode == REQ_CODE_SELECT_SHOP_MAIN_IMAGE) {
                    File file = new File(path);

                    if(m_pShop == null) {
                        m_pShop = new Shop();
                    }

                    Image image = m_pShop.getShopMainImg();
                    if(image == null) {
                        image = new Image();
                    }

                    image.setFile(file);
                    m_pShop.setShopMainImg(image);

                    if(m_ivSelectedImg != null) {
                        m_ivSelectedImg.setImageBitmap(bitmap);
                        m_ivSelectedImg.setVisibility(View.VISIBLE);
                        m_nivSelectedImg.setVisibility(View.GONE);
                    }
                    else {
                        m_isSetCameraBitmap = false;
                        m_pCameraBitmap = image;
                    }
                }
                else if(reqCode >= REQ_CODE_SELECT_SHOP_SUB_IMAGE && reqCode < REQ_CODE_SELECT_SHOP_MAIN_IMAGE) {
                    m_arrShopImage.get(reqCode).setFile(new File(path));
                    m_arrShopImage.get(reqCode).setBitmap(bitmap);

                    if(m_adpImageAdapter != null) {
                        m_adpImageAdapter.notifyDataSetChanged();
                    }
                    else {
                        m_isSetCameraBitmap = false;
                        m_pCameraBitmap = m_arrShopImage.get(reqCode);
                    }
                }
                else if(reqCode >= REQ_CODE_SELECT_PRICE_IMAGE && reqCode < REQ_CODE_SELECT_SHOP_EVENT_IMAGE) {
                    int idx = (reqCode - REQ_CODE_SELECT_PRICE_IMAGE);
                    m_arrPriceImage.get(idx).setFile(new File(path));
                    m_arrPriceImage.get(idx).setBitmap(bitmap);

                    if(m_adpImageAdapter  != null) {
                        m_adpPriceImageAdapter.notifyDataSetChanged();
                    }
                    else {
                        m_isSetCameraBitmap = false;
                        m_pCameraBitmap = m_arrPriceImage.get(reqCode);
                    }
                }
            }
        }
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

        if(m_nActivityPageNum == 1) {
            requestQuestionAndAnswer(pageNum);
        }
        else  if(m_nActivityPageNum== 2) {
            requestProductListOfShop(pageNum);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetQuestionPage() {
        m_nPrePageNum = -1;
        requestQuestionAndAnswer(1);
    }

    private void resetPricePage() {
        m_nPrePageNum = -1;
        requestProductListOfShop(1);
    }

    private void requestQuestionAndAnswer(int pageNum){

        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvQuestionAnswer.setSelection(0);
            m_arrGroupList.clear();
            m_arrChildList.clear();
        }

        String url = ServerAPIPath.API_GET_SHOP_QUESTION_LIST;
        int shop_id = m_pShop.getId();
        url+="&shopcommentShopID="+shop_id;
        url+="&page_num="+pageNum;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseQuestionList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void responseQuestionList(Object json){

        try {
            JSONObject jsonObject = (JSONObject)json;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
            JSONArray dataArray = jsonObject.getJSONArray("list");

            for(int i = 0; i < dataArray.length(); i++) {
                Question question = new Question(dataArray.getJSONObject(i));

                m_arrGroupList.add(question);

                m_arrChildList.add(new ArrayList<Answer>());
            }
            requestAnswerList();

        } catch (Exception e) {
            Logger.e(TAG, "responseQuestionList - JSONException : " + e.getMessage());
            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestAnswerList(){

        String url = ServerAPIPath.API_GET_SHOP_ANSWER_LIST_WITH_SHOP_ID;
        int shop_id = m_pShop.getId();
        url+="?shop_id="+shop_id;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseAnswerList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseAnswerList(Object json){

        try {
            JSONArray dataArray = (JSONArray)json;
            ArrayList<Answer>  answerList = new ArrayList<Answer>();
            for(int i = 0; i < dataArray.length(); i++) {
                Answer answer = new Answer(dataArray.getJSONObject(i));
                answerList.add(answer);
            }

            for(int i = 0; i < m_arrGroupList.size(); i++) {
                Question question = m_arrGroupList.get(i);
                ArrayList<Answer> answers = m_arrChildList.get(i);
                for(int j = 0; j < answerList.size();j++){
                    if(question.getId() == answerList.get(j).getShopCommentID()){
                        answers.add(answerList.get(j));
                    }
                }
            }

            m_adtQuestionAnswerAdapter.notifyDataSetChanged();

            for(int i = 0; i < m_arrGroupList.size(); i++) {
                m_lvQuestionAnswer.expandGroup(i);
            }

        } catch (Exception e) {
            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseAnswerList - JSONException : " + e.getMessage());
        }
    }

    private void requestModifyShop() {

        String url = ServerAPIPath.API_POST_MODIFY_MY_SHOP;


        Map<String, String> params = new HashMap<String, String>();

        int shop_id = m_pShop.getId();

        params.put("shop_id", String.format("%d", shop_id));

        if(m_isRegister[0] == true) {
            JSONArray array = new JSONArray();
            ArrayList<Image> imageArrayList = m_pShop.getShopSubImageArray();

            for (int i = 0; i < m_arrShopImage.size(); i++) {
                if (m_arrShopImage.get(i).getId() > 0) {
                    array.put(m_arrShopImage.get(i).getId());
                } else {
                    if (imageArrayList.size() > i) {
                        Image image = imageArrayList.get(i);
                        if (image != null && image.isDeleted() == false) {
                            array.put(image.getId());
                        }
                    }
                }
            }

            if(imageArrayList.size() > m_arrShopImage.size()) {
                for(int i = m_arrShopImage.size(); i < imageArrayList.size(); i++) {
                    Image image = imageArrayList.get(i);
                    array.put(image.getId());
                }
            }

            params.put("shopImgIDArray", array.toString());

            if(m_pShop.getShopMainImg() != null) {
                Image image = m_pShop.getShopMainImg();
                if(image != null && image.getId() > 0) {
                    params.put("shopImgID", String.format("%d", image.getId()));
                }
            }
        }

        if(m_isRegister[1] == true) {
            if(m_etShopName.getText()  != null) {
                params.put("shopName", m_etShopName.getText().toString());
            }

            if(m_etShopName.getText()  != null) {
                params.put("shopName", m_etShopName.getText().toString());
            }

            if(m_etShopPhoneNumber.getText()  != null) {
                params.put("shopPhonenumber", m_etShopPhoneNumber.getText().toString());
            }

            if(m_etShopAddress.getText()  != null) {
                params.put("shopAddress", m_etShopAddress.getText().toString());
                params.put("shopLongtitude", String.format("%f",m_pShop.getShopLongtitude()));
                params.put("shopLatitude", String.format("%f",m_pShop.getShopLatitude()));
            }

            if(m_etShopOpenTime.getText() != null) {
                params.put("shopOpenTimeString", m_etShopOpenTime.getText().toString());
            }

            if(m_etShopRestTime.getText() != null) {
                params.put("shopRestTimeString", m_etShopRestTime.getText().toString());
            }

            if (m_cbShopParkEnable.isChecked() == true) {
                params.put("shopParkable", "Y");
            } else {
                params.put("shopParkable", "N");
            }
        }

        if(m_isRegister[2] == true) {
            if(m_etShopDescription.getText() != null) {
                params.put("shopDescription", m_etShopDescription.getText().toString());
            }
        }

        if(m_isRegister[3] == true) {
            if(m_etShopRoad.getText() != null) {
                params.put("shopRoad", m_etShopRoad.getText().toString());
            }
        }

        if(m_isRegister[4] == true) {
            JSONArray array = new JSONArray();
            ArrayList<Image> imageArrayList = m_pShop.getShopPriceImageArray();

            for (int i = 0; i < m_arrPriceImage.size(); i++) {
                if (m_arrPriceImage.get(i).getId() > 0) {
                    array.put(m_arrPriceImage.get(i).getId());
                } else {
                    if (imageArrayList.size() > i) {
                        Image image = imageArrayList.get(i);
                        if (image != null && image.isDeleted() == false) {
                            array.put(image.getId());
                        }
                    }
                }
            }

            if (imageArrayList.size() > m_arrPriceImage.size()) {
                for (int i = m_arrPriceImage.size(); i < imageArrayList.size(); i++) {
                    Image image = imageArrayList.get(i);
                    array.put(image.getId());
                }
            }

            params.put("shopPriceImgIDArrayString", array.toString());
        }

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(MyShopManageActivity.this, ResponseMessage.SUCCESS_REGISTER, Toast.LENGTH_SHORT).show();
                        requestMyShop();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_REGISTER_SHOP, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                },
                params);
    }

    private void requestRegisterProduct(final  Product product) {

            String url = ServerAPIPath.API_POST_REGISTER_MY_PRODUCT;
            Map<String, String> params = new HashMap<String, String>();

            if(product.isNewProduct() == false && product.getId() > 0) {
                params.put("product_id", String.format("%d", product.getId()));
            }

            int shop_id = m_pShop.getId();

            params.put("shop_id", String.format("%d", shop_id));
            params.put("productName", product.getName());
            params.put("productPrice",product.getPrice());
            params.put("productEventPrice", product.getEventPrice());

            Utility.showWaitingDlg(this);
            ServerAPICall.getInstance().callPOST(url,
                    new onReponseListener() {
                        @Override
                        public void onResponse(Object o) {
                            Toast.makeText(MyShopManageActivity.this, ResponseMessage.SUCCESS_REGISTER, Toast.LENGTH_SHORT).show();

                            resetPricePage();
                            Utility.hideWaitingDlg();
                        }
                    },
                    new onErrorListener() {
                        @Override
                        public void onErrorResponse(String s) {
                            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_REGISTER_FAIL, Toast.LENGTH_SHORT).show();
                            Utility.hideWaitingDlg();
                        }
                    },
                    params);
    }


    private void requestProductListOfShop(int pageNum){

        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvProduct.setSelection(0);
            m_lstPrices.clear();
        }


        String url = ServerAPIPath.API_GET_PRODUCT_LIST;
        int shop_id = m_pShop.getId();
        url+="?shop_id="+shop_id;
        url+="&page_num="+pageNum;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            JSONObject jsonObject = (JSONObject)o;
                            int total = jsonObject.getInt("total");
                            int page_num = jsonObject.getInt("page");
                            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
                            JSONArray dataArray = jsonObject.getJSONArray("list");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Product product = new Product(dataObject);

                                m_lstPrices.add(product);
                            }
                        } catch (Exception e) {
                            Logger.e(TAG, "responseProductListOfShop - JSONException : " + e.getMessage());
                            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                        m_adpPrice.notifyDataSetChanged();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void requestWriteAnswer(Question question, Answer answer ,String content) {

        final  Question shopQuestion = question;
        final  Answer shopAnswer= answer;
        final  String shopAnswerContent= content;

        if(shopAnswerContent == null || shopAnswerContent.equals("") == true) {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_SHOP_ANSWER;
        Map<String, String> params = new HashMap<String, String>();

        if(shopAnswer != null) {
            params.put("answer_id", String.format("%d",shopAnswer.getId()));
        }
        params.put("shopanswerShopCommentID", String.format("%d", shopQuestion.getId()));
        params.put("shopanswerContent", shopAnswerContent);

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        resetQuestionPage();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestRemoveAnswer(Answer answer) {

        final  Answer shopAnswer= answer;

        if(shopAnswer == null) {
            return;
        }

        String url = ServerAPIPath.API_POST_REMOVE_SHOP_ANSWER;

        Map<String, String> params = new HashMap<String, String>();

        params.put("answer_id", String.format("%d",shopAnswer.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        resetQuestionPage();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestAutoLogin() {
        String url = ServerAPIPath.API_POST_IS_SHOP_AUTO_LOGIN;
        Map<String, String> params = new HashMap<String, String>();

        if(m_pShop != null) {
            params.put("shop_id", String.format("%d", m_pShop.getId()));
        }

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        String data = (String)o;
                        if(data.equals("TRUE") == true){
                            // autologin
                        }
                        else {
                            // no autologin
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestLoginShop() {

        if(m_etLoginShopID.getText().toString().length() == 0) {
            Toast.makeText(this, "숍아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_etLoginPW.getText().toString().length() == 0) {
            Toast.makeText(this, "숍PW를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_SHOP_LOGIN;

        Map<String, String> params = new HashMap<String, String>();

        params.put("shopID", m_etLoginShopID.getText().toString());
        params.put("shopPW", m_etLoginPW.getText().toString());

        if(m_cbAutoLogin.isChecked() == false) {
            params.put("autoLogin", String.format("%d", 0));
        }
        else {
            params.put("autoLogin", String.format("%d", 1));
        }

        Utility.showWaitingDlg(this);

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        String data = (String)o;
                        if(data.equals("TRUE") == true){
                            m_isLoginned = true;
                            showMainView();
                        }
                        else {
                            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_LOGIN, Toast.LENGTH_SHORT).show();
                        }

                        m_etLoginPW.setText("");
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                },
                params);
    }

    private void requestShopEventList()  {

        String url = ServerAPIPath.API_GET_SHOP_EVENT_LIST;
        int shop_id = m_pShop.getId();
        url+="?shop_id="+shop_id;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseShopEventList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseShopEventList(Object json){

        try {
            m_arrShopEventList.clear();
            JSONObject jsonObject = (JSONObject)json;
            JSONArray dataArray = jsonObject.getJSONArray("list");

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                Event event = new Event(dataObject);

                m_arrShopEventList.add(event);
            }

            showShopEventSetting();

        } catch (Exception e) {
            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseShopEventList - JSONException : " + e.getMessage());
        }
    }

    private void requestRegisterEvent() {

        android.text.Editable text =  ((EditText)findViewById(R.id.et_explain)).getText();
        if(text == null || text.toString().equals("") == true) {
            Toast.makeText(this, "이벤트내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        Event event = getMyShopEvent();
        if(event != null && event.getId() > 0) {
            params.put("event_id", String.format("%d", event.getId()));
        }
        int shop_id = m_pShop.getId();
        params.put("eventShopID", String.format("%d", shop_id));

        params.put("eventContent", m_etEventContent.getText().toString());
        params.put("eventSummary", m_etEventTitle.getText().toString());

        if(m_pShop.getShopCategory() != null) {
            params.put("eventCategoryID", String.format("%d", m_pShop.getShopCategory().getId()));
        }

        if(event != null && event.getEventImage() != null && event.getEventImage().getId() > 0) {
            params.put("eventImgID", String.format("%d", event.getEventImage().getId()));
        }
        String url = ServerAPIPath.API_POST_REGISTER_EVENT;
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        requestShopEventList();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_REGISTER_EVENT, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                },
                params);
    }

    private void requestGetLatLngFromAddress(String address) {
        String url = ServerAPIPath.API_GET_LATLNG;

        String utf8Location = null;
        try {
            utf8Location = URLEncoder.encode(address, "UTF-8");
            url += utf8Location;
        }
        catch (Exception e){

        }
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseGetLatLngFromAddress(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseGetLatLngFromAddress(Object o) {
        try {
            String json = (String)o;
            JSONObject response = new JSONObject(json);
            Logger.d(TAG, "responseGetLatLngFromAddress - response : " + response);

            String code = response.getString("status");

            Logger.d(TAG, "responseGetLatLngFromAddress - code : " + code);
            if (!code.equals("OK")) {
                Toast.makeText(this, ResponseMessage.ERR_INVALIDE_ADDRESS, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray jsonArray = response.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry").getJSONObject("location");
            double tempLat = jsonObject1.getDouble("lat");
            double tempLng = jsonObject1.getDouble("lng");

            m_pShop.setShopLatitude(tempLat);
            m_pShop.setShopLongtitude(tempLng);

            requestModifyShop();

        } catch (Exception e) {
            Logger.e(TAG, "responseGetLatLngFromAddress - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALIDE_ADDRESS, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLogoutShop() {
        String url = ServerAPIPath.API_POST_LOGOUT_SHOP;

        Map<String, String> params = new HashMap<String, String>();

        params.put("shop_id", String.format("%d",m_pShop.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        m_isLoginned = false;
                        showLoginView();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    public void requestMyShop() {
        String url = ServerAPIPath.API_GET_MY_SHOP;
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            if(o == null) {
                                return;
                            }

                            JSONObject dataObject = (JSONObject) o;

                            m_pShop = new Shop(dataObject);

                            showPage();
                        } catch (Exception e) {
                            Toast.makeText(MyShopManageActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                        Utility.hideWaitingDlg();

                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyShopManageActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    private void uploadShopImages(){
        int startIdx = REQ_CODE_SELECT_SHOP_SUB_IMAGE;
        int endIdx = REQ_CODE_SELECT_SHOP_MAIN_IMAGE;

        boolean isAllLoad = true;
        for(int i = startIdx; i < endIdx; i++){
            Image image = m_arrShopImage.get(i);
            if (image.getFile() != null) {
                uploadShopImageFile(image.getFile(), i);
                isAllLoad = false;
                break;
            }
        }

        Image image = m_pShop.getShopMainImg();
        if(isAllLoad == true && image.getFile() != null) {
            uploadShopImageFile(image.getFile(), REQ_CODE_SELECT_SHOP_MAIN_IMAGE);
            isAllLoad = false;
        }

        if(isAllLoad == true) {
            requestModifyShop();
        }
    }

    private void uploadShopEventImage() {
        Event event = getMyShopEvent();
        if (event.getEventImage() != null) {
            Image image = event.getEventImage();
            if(image.getFile() != null) {
                uploadShopImageFile(image.getFile(), REQ_CODE_SELECT_SHOP_EVENT_IMAGE);
            }
        }
    }

    private void uploadShopPriceImages(){
        int startIdx = REQ_CODE_SELECT_PRICE_IMAGE;
        int endIdx = REQ_CODE_SELECT_SHOP_EVENT_IMAGE;

        boolean isAllLoad = true;

        for(int i = startIdx; i < endIdx; i++) {
            int idx = i - startIdx;
            Image image = m_arrPriceImage.get(idx);
            if (image.getFile() != null) {
                uploadShopImageFile(image.getFile(), i);
                isAllLoad = false;
                break;
            }
        }

        if(isAllLoad == true) {
            requestModifyShop();
        }
    }

    private void uploadShopImageFile(File file, final int req) {
        SystemClock.sleep(1000);
        String url = ServerAPIPath.API_POST_UPLOAD_SHOP_IMAGE;

        if(REQ_CODE_SELECT_SHOP_EVENT_IMAGE == req) {
            url = ServerAPIPath.API_POST_UPLOAD_EVENT_IMAGE;
        }

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().uploadFile(url, "img_filename", file,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            Logger.d(TAG, "uploadFile - response : " + response);

                            String code = response.getString("result_code");
                            String msg = response.getString("result_msg");

                            Logger.d(TAG, "uploadFile - code : " + code);
                            Logger.d(TAG, "uploadFile - msg : " + msg);
                            if (!code.equals("0")) {
                                Toast.makeText(MyShopManageActivity.this, msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject dataObject = response.getJSONObject("result_data");
                            int id = dataObject.getInt("id");

                            if(id  > 1){

                                if(req == REQ_CODE_SELECT_SHOP_EVENT_IMAGE) {

                                    Event event = getMyShopEvent();
                                    Image image = event.getEventImage();
                                    image.setFile(null);
                                    image.setId(id);

                                    requestRegisterEvent();
                                }
                                else if(req >= REQ_CODE_SELECT_SHOP_SUB_IMAGE && req <= REQ_CODE_SELECT_SHOP_MAIN_IMAGE ) {

                                    Image image = null;
                                    if(req == REQ_CODE_SELECT_SHOP_MAIN_IMAGE) {
                                        image = m_pShop.getShopMainImg();
                                    }
                                    else {
                                        image = m_arrShopImage.get(req);
                                    }
                                    image.setFile(null);
                                    image.setId(id);
                                    uploadShopImages();
                                }
                                else if(req >= REQ_CODE_SELECT_PRICE_IMAGE && req < REQ_CODE_SELECT_SHOP_EVENT_IMAGE ) {
                                    int idx = req - REQ_CODE_SELECT_PRICE_IMAGE;
                                    Image image = m_arrPriceImage.get(idx);
                                    image.setFile(null);
                                    image.setId(id);

                                    uploadShopPriceImages();
                                }
                            }
                            else {
                                throw new JSONException("실패");
                            }


                        } catch (JSONException e) {
                            Logger.e(TAG, "uploadFile - JSONException : " + e.getMessage());
                            Toast.makeText(MyShopManageActivity.this, "이미지업로드 실패.", Toast.LENGTH_SHORT).show();
                        }
                        Utility.hideWaitingDlg();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        Logger.d(TAG, "uploadFile - VolleyError : ");

                        Toast.makeText(MyShopManageActivity.this, "이미지업로드 실패.", Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    public class ShopManageViewPagerAdapter extends PagerAdapter {

        public static final String TAG = "ShopManageViewPagerAdapter";
        private Context mContext;

        private View.OnClickListener mOnClickListener;

        public ShopManageViewPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return ARR_MENU.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ARR_MENU[position];
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {

            View convertView = null;
            convertView = LayoutInflater.from(mContext).inflate(ARR_VIEW[position], null);
            ((ViewPager) container).addView(convertView, 0);

            switch (position) {
                case 0:
                    initShopSetting();
                    break;
                case 1:
                    initShopQuestion();
                    break;
                case 2:
                    initShopPriceSetting();
                    break;
                case 3:
                    initShopEventSetting();
                    break;
            }

            return convertView;
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
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }
}

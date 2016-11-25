package mimishop.yanji.com.mimishop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devsmart.android.ui.HorizontalListView;
//import com.facebook.android.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.shopinfo.ShopDetailActivity;
import mimishop.yanji.com.mimishop.adapter.CategoryAdapter;
import mimishop.yanji.com.mimishop.adapter.MyShopAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.CameraAndPhotoDialog;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.modal.Tube;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 3/25/2015.
 */
public class ShopSearchActivity extends FragmentActivity {

    public static final String TAG = "ShopSearchActivity";
    public static final int LIMIT_SEQUE = 99999999;

    private int []ARR_MAKER_CLOSE = {
            R.drawable.ic_marker_close01, R.drawable.ic_marker_close02, R.drawable.ic_marker_close03, R.drawable.ic_marker_close04, R.drawable.ic_marker_close05, R.drawable.ic_marker_close06, R.drawable.ic_marker_close07, R.drawable.ic_marker_close08, R.drawable.ic_marker_close09, R.drawable.ic_marker_close10,
            R.drawable.ic_marker_close11, R.drawable.ic_marker_close12, R.drawable.ic_marker_close13, R.drawable.ic_marker_close14, R.drawable.ic_marker_close15, R.drawable.ic_marker_close16, R.drawable.ic_marker_close17, R.drawable.ic_marker_close18, R.drawable.ic_marker_close19, R.drawable.ic_marker_close20,
            R.drawable.ic_marker_close21, R.drawable.ic_marker_close22, R.drawable.ic_marker_close23, R.drawable.ic_marker_close24, R.drawable.ic_marker_close25, R.drawable.ic_marker_close26, R.drawable.ic_marker_close27, R.drawable.ic_marker_close28, R.drawable.ic_marker_close29, R.drawable.ic_marker_close30,
            R.drawable.ic_marker_close31, R.drawable.ic_marker_close32, R.drawable.ic_marker_close33, R.drawable.ic_marker_close34, R.drawable.ic_marker_close35, R.drawable.ic_marker_close36, R.drawable.ic_marker_close37, R.drawable.ic_marker_close38, R.drawable.ic_marker_close39, R.drawable.ic_marker_close40,
            R.drawable.ic_marker_close41, R.drawable.ic_marker_close42, R.drawable.ic_marker_close43, R.drawable.ic_marker_close44, R.drawable.ic_marker_close45, R.drawable.ic_marker_close46, R.drawable.ic_marker_close47, R.drawable.ic_marker_close48, R.drawable.ic_marker_close49, R.drawable.ic_marker_close50
    };
    private int []ARR_MAKER_OPEN = {
            R.drawable.ic_marker_open01, R.drawable.ic_marker_open02, R.drawable.ic_marker_open03, R.drawable.ic_marker_open04, R.drawable.ic_marker_open05, R.drawable.ic_marker_open06, R.drawable.ic_marker_open07, R.drawable.ic_marker_open08, R.drawable.ic_marker_open09, R.drawable.ic_marker_open10,
            R.drawable.ic_marker_open11, R.drawable.ic_marker_open12, R.drawable.ic_marker_open13, R.drawable.ic_marker_open14, R.drawable.ic_marker_open15, R.drawable.ic_marker_open16, R.drawable.ic_marker_open17, R.drawable.ic_marker_open18, R.drawable.ic_marker_open19, R.drawable.ic_marker_open20,
            R.drawable.ic_marker_open21, R.drawable.ic_marker_open22, R.drawable.ic_marker_open23, R.drawable.ic_marker_open24, R.drawable.ic_marker_open25, R.drawable.ic_marker_open26, R.drawable.ic_marker_open27, R.drawable.ic_marker_open28, R.drawable.ic_marker_open29, R.drawable.ic_marker_open30,
            R.drawable.ic_marker_open31, R.drawable.ic_marker_open32, R.drawable.ic_marker_open33, R.drawable.ic_marker_open34, R.drawable.ic_marker_open35, R.drawable.ic_marker_open36, R.drawable.ic_marker_open37, R.drawable.ic_marker_open38, R.drawable.ic_marker_open39, R.drawable.ic_marker_open40,
            R.drawable.ic_marker_open41, R.drawable.ic_marker_open42, R.drawable.ic_marker_open43, R.drawable.ic_marker_open44, R.drawable.ic_marker_open45, R.drawable.ic_marker_open46, R.drawable.ic_marker_open47, R.drawable.ic_marker_open48, R.drawable.ic_marker_open49, R.drawable.ic_marker_open50
    };

    private int []ARR_MAKER_NAIL_OPEN = {
            R.drawable.green_01, R.drawable.green_02, R.drawable.green_03, R.drawable.green_04, R.drawable.green_05, R.drawable.green_06, R.drawable.green_07, R.drawable.green_08, R.drawable.green_09, R.drawable.green_10,
            R.drawable.green_11, R.drawable.green_12, R.drawable.green_13, R.drawable.green_14, R.drawable.green_15, R.drawable.green_16, R.drawable.green_17, R.drawable.green_18, R.drawable.green_19, R.drawable.green_20,
            R.drawable.green_21, R.drawable.green_22, R.drawable.green_23, R.drawable.green_24, R.drawable.green_25, R.drawable.green_26, R.drawable.green_27, R.drawable.green_28, R.drawable.green_29, R.drawable.green_30,
            R.drawable.green_31, R.drawable.green_32, R.drawable.green_33, R.drawable.green_34, R.drawable.green_35, R.drawable.green_36, R.drawable.green_37, R.drawable.green_38, R.drawable.green_39, R.drawable.green_40,
            R.drawable.green_41, R.drawable.green_42, R.drawable.green_43, R.drawable.green_44, R.drawable.green_45, R.drawable.green_46, R.drawable.green_47, R.drawable.green_48, R.drawable.green_49, R.drawable.green_50
    };

    private static final int   REQ_SELECT_TUBE = 0;
    private static final int   REQ_SELECT_LOCATION = 1;
    private static ShopSearchActivity mInstance = null;

    private View m_rlShopBar = null;
    private HorizontalListView m_hlvShopList = null;
    private MyShopAdapter m_adpMyShop = null;
    private ArrayList<Shop> m_lstShop = new ArrayList<Shop>();
    private ArrayList<Shop> m_lstTempShop = new ArrayList<Shop>();

    private GoogleMap m_vGoogleMap;

    private Spinner m_spCategory = null;
    private CategoryAdapter m_adpCategory = null;
    private int     m_nBeforeCategoryIdx = 0;
    private int     m_nCategoryIdx = 0;
    private ArrayList<Category> m_lstCategory = new ArrayList<Category>();

    private EditText m_etShopName = null;
    private EditText m_etLocation = null;

    private  boolean m_isSearchPostition = true;
    private  boolean m_isSearchAllLocation = true;

    private boolean showRealTimeShop = false;
    private boolean pressedRealTimeShop = false;

    private int defaultZoom = 15;
    private int locationZoom = 13;
    private int defaultDistance = 800;
    private int shop_count = 50;

    private Tube m_pCurrentTube = null;
    private mimishop.yanji.com.mimishop.modal.Location m_pCurrentLocation = null;

    private boolean m_isFirstStart = true;
    private int     m_nFirstCategoryIdentifier = 0;  // 0:전체 1:네일 2:헤어
    public static ShopSearchActivity getInstance() {
        return  mInstance;
    }

    private String m_strDefaultLocation = "지역";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map_activity);

        m_nFirstCategoryIdentifier = getIntent().getIntExtra("category_id", 0);
        m_nCategoryIdx = m_nFirstCategoryIdentifier;

        mInstance = this;
        defaultDistance = 1500;

        init();
    }


    private void init(){
        // btn_show_list
        m_hlvShopList = (HorizontalListView)findViewById(R.id.hlv_shop_bar);
        m_rlShopBar = findViewById(R.id.rl_shop_bar);
        final Button btnShowList = (Button)findViewById(R.id.btn_show_list);
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_hlvShopList.getVisibility() == View.VISIBLE) {
                    m_hlvShopList.setVisibility(View.GONE);
                    m_rlShopBar.setVisibility(View.GONE);
                    btnShowList.setBackground(getResources().getDrawable(R.drawable.btn_show_list_1_selector));
                }
                else {
                    m_hlvShopList.setVisibility(View.VISIBLE);
                    m_rlShopBar.setVisibility(View.VISIBLE);
                    btnShowList.setBackground(getResources().getDrawable(R.drawable.btn_show_list_selector));
                }
            }
        });

        m_adpMyShop = new MyShopAdapter(this, R.layout.item_row_my_shop, m_lstShop);
        m_hlvShopList.setAdapter(m_adpMyShop);

        m_hlvShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startShopDetailActivity(m_lstShop.get(i));
            }
        });

        m_etShopName = (EditText) findViewById(R.id.et_search_shop_text);
        m_etShopName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchByShopName();

                    return true;
                }
                return false;
            }
        });

        ImageButton searchButton = (ImageButton)findViewById(R.id.ib_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByShopName();
            }
        });

        initLocation();

        initMap();

        initCategory();

        initMenu();

        initKeyboardTouch();

        Button searchTube = (Button)findViewById(R.id.btn_search_tube);
        searchTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopSearchActivity.this, TubeSearchActivity.class);
                startActivityForResult(intent, REQ_SELECT_TUBE);
            }
        });
    }

    private void initKeyboardTouch() {
        View view = findViewById(R.id.menu_main);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(ShopSearchActivity.this);
            }
        });

        if(m_vGoogleMap!=null) {
            m_vGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Utility.hideSoftKeyboard(ShopSearchActivity.this);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode == REQ_SELECT_TUBE) {
                searchByTube();
            }
            else if(requestCode == REQ_SELECT_LOCATION) {
                if(LocationSearchActivity.SelectedLocation != null) {
                    setCurrentLocation(LocationSearchActivity.SelectedLocation);
                    searchByLocationPosition();
                }
            }
        }
    }

    public void setTube(Tube tube) {
        m_pCurrentTube = tube;
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

        ImageButton backSearch = (ImageButton)view.findViewById(R.id.btn_search);
        backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               searchByMap();
            }
        });

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_shop_search));
    }

    private void initLocation() {
        m_etLocation = (EditText)findViewById(R.id.et_location);
        m_etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationSearchActivity();
            }
        });
        m_etLocation.setFocusable(false);
        m_etLocation.setFocusableInTouchMode(false);
        m_etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchByLocationText();
                    return true;
                }

                return false;
            }
        });

        View view = findViewById(R.id.rl_location_search);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationSearchActivity();
            }
        });

        ImageButton button = (ImageButton)findViewById(R.id.ib_change_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationSearchActivity();
            }
        });

        Utility.showWaitingDlg(ShopSearchActivity.this);

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                String address = Utility.getLocation(ShopSearchActivity.this, AppController.getInstance().getLocation());
                m_etLocation.setText(address);

                Utility.hideWaitingDlg();
            }
        }, 100);
    }

    private void startLocationSearchActivity() {
        Intent intent = new Intent(ShopSearchActivity.this, LocationSearchActivity.class);
        startActivityForResult(intent, REQ_SELECT_LOCATION);
    }

    public  void setCurrentLocation( mimishop.yanji.com.mimishop.modal.Location location) {
        m_etLocation.setText(location.getRealLocationName());

        m_pCurrentLocation = location;
    }

    private void initCategory(){
        m_spCategory = (Spinner)findViewById(R.id.sp_category);

        m_spCategory.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                m_nCategoryIdx = i;

                searchByCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        m_adpCategory = new CategoryAdapter(this, R.layout.item_row_category, m_lstCategory);

        m_spCategory.setAdapter(m_adpCategory);

        ImageButton ibChangeCategory = (ImageButton)findViewById(R.id.ib_change_category);
        ibChangeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_spCategory.performClick();
            }
        });

        requestCategoryList();
    }


    private void initMap() {
        m_vGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        Location location = AppController.getInstance().getLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(m_vGoogleMap!=null) {
            m_vGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));

            m_vGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    try {
                        int idx = Integer.parseInt(marker.getSnippet());
                        Shop shop = m_lstShop.get((idx - 1));
                        startShopDetailActivity(shop);
                    } catch (Exception e) {

                    }
                    return false;
                }
            });
        }
        Button btnSearch = (Button)findViewById(R.id.btn_search_location);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_vGoogleMap != null) {
                    LatLng center = m_vGoogleMap.getCameraPosition().target;
                    m_etLocation.setText(m_strDefaultLocation);

                    searchShopWithLocation(center.latitude, center.longitude, getDistance());
                }
            }
        });

        Button btnGoto = (Button)findViewById(R.id.btn_find_current_location);
        btnGoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Location location = AppController.getInstance().getLocation();
                gotoMoveCamera(location.getLatitude(), location.getLongitude(), defaultZoom);

                m_etLocation.setText(m_strDefaultLocation);
                searchShopWithLocation(location.getLatitude(), location.getLongitude(), getDistance());
            }
        });

        final ImageButton btnRealTimeShop = (ImageButton)findViewById(R.id.ib_show_real_time_shop);
        btnRealTimeShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showRealTimeShop == false) {
                    showRealTimeShop = true;
                    pressedRealTimeShop = true;
                    btnRealTimeShop.setSelected(true);

                    searchRealTimeShop();
                } else {
                    showRealTimeShop = false;
                    btnRealTimeShop.setSelected(false);
                }

                showMapIcons();
            }
        });

        btnRealTimeShop.setSelected(false);
    }

    public void startShopDetailActivity(Shop shop){

        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra("shop_id", shop.getId());
        startActivity(intent);
    }

    private void gotoShop(Shop shop) {
        if(shop == null) return;
        if(m_vGoogleMap == null) return;
        LatLng latLng = new LatLng(shop.getShopLatitude(), shop.getShopLongtitude());
        m_vGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
    }

    private void showMapIcons() {
        if(m_vGoogleMap == null) return;
        m_vGoogleMap.clear();
        Location currentLocation = AppController.getInstance().getLocation();

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(latLng).title("Me");
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location));
        m_vGoogleMap.addMarker(marker);

        for(int i = 0; i < m_lstShop.size(); i++){

            if(i >= ARR_MAKER_OPEN.length) {
                break;
            }

            Shop shop = m_lstShop.get(i);

            latLng = new LatLng(shop.getShopLatitude(), shop.getShopLongtitude());
            marker = new MarkerOptions().position(latLng).title(shop.getName());
            marker.snippet(String.format("%d", (i + 1)));

            if(shop.isShopEmoticonChecked() == true) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_heart2));
            }
            else if(shop.isShopEventing() == true) {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_heart2));
            }
            else if(shop.isShopEnded() == false){
                if(shop.getShopCategory().getName().equals("헤어") == true) {
                    marker.icon(BitmapDescriptorFactory.fromResource(ARR_MAKER_OPEN[i]));
                }
                else {
                    marker.icon(BitmapDescriptorFactory.fromResource(ARR_MAKER_NAIL_OPEN[i]));
                }
            }
            else  {
                marker.icon(BitmapDescriptorFactory.fromResource(ARR_MAKER_CLOSE[i]));
            }

            m_vGoogleMap.addMarker(marker);
        }

        if(m_pCurrentTube != null) {
            latLng = new LatLng(m_pCurrentTube.getTubeLatitude(), m_pCurrentTube.getTubeLongtitude());
            marker = new MarkerOptions().position(latLng).title(m_pCurrentTube.getTubeName());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_tube));
            m_vGoogleMap.addMarker(marker);
        }
    }

    private int getDistance() {
        if(m_vGoogleMap == null) return 0;
        LatLngBounds curScreen = m_vGoogleMap.getProjection()
                .getVisibleRegion().latLngBounds;
        LatLng east = curScreen.northeast;
        LatLng center = m_vGoogleMap.getCameraPosition().target;

        Location cur_location = new Location("jy");
        cur_location.setLatitude(center.latitude);
        cur_location.setLongitude(center.longitude);

        if(cur_location == null) return defaultDistance;
        if(east == null) return defaultDistance;

        Location location = new Location("jy");

        location.setLatitude(east.latitude);
        location.setLongitude(east.longitude);

        float distance = cur_location.distanceTo(location);

        return (int)distance;
    }

    private void gotoMoveCamera(double lat, double lng, float zoom) {
        if(m_vGoogleMap == null) return;
        LatLng latLng = new LatLng(lat, lng);
        m_vGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void searchByShopName() {

        m_nCategoryIdx = m_nFirstCategoryIdentifier;

        requestShopList(0, 0, 0, true, shop_count);

        m_spCategory.setSelection(m_nCategoryIdx);
    }

    private void searchByTube() {
        if(m_pCurrentTube == null) {
            return;
        }

        m_etShopName.setText("");
        m_etLocation.setText(m_strDefaultLocation);
        m_nCategoryIdx = m_nFirstCategoryIdentifier;

        Tube tube = m_pCurrentTube;
        gotoMoveCamera(tube.getTubeLatitude(), tube.getTubeLongtitude(), defaultZoom);
        searchShopWithLocation(tube.getTubeLatitude(), tube.getTubeLongtitude(), getDistance());

        m_spCategory.setSelection(m_nCategoryIdx);
    }


    private void searchByCategory() {
        if(m_nCategoryIdx == m_nBeforeCategoryIdx) {
            return;
        }

        searchByMap();
    }

    private void searchByLocationPosition() {

        if(m_pCurrentLocation == null) {
            return;
        }

        m_etShopName.setText("");
        m_nCategoryIdx = m_nFirstCategoryIdentifier;

        gotoMoveCamera(m_pCurrentLocation.getLocationLatitude(), m_pCurrentLocation.getLocationLongtitude(), locationZoom);
        searchShopWithLocation(m_pCurrentLocation.getLocationLatitude(),  m_pCurrentLocation.getLocationLongtitude(), getDistance());

        m_spCategory.setSelection(m_nCategoryIdx);
    }

    private void searchByLocationText() {

        m_isSearchPostition = false;
        m_nCategoryIdx = m_nFirstCategoryIdentifier;

        requestGetLatLngFromAddress(m_etLocation.getText().toString());
        requestShopList(0, 0, 0, false, shop_count);

        m_spCategory.setSelection(m_nCategoryIdx);
        m_isSearchPostition = true;
    }

    private void searchByMap() {
        if(m_vGoogleMap == null) return;
        LatLng center = m_vGoogleMap.getCameraPosition().target;
        searchShopWithLocation(center.latitude, center.longitude, getDistance());
    }

    private void searchShopWithLocation(double lat, double longti, double dist) {
        m_isSearchPostition = true;
        requestShopList(lat, longti, dist, false, shop_count);
    }

    private void searchRealTimeShop() {
        if(m_vGoogleMap == null) return;
        LatLng center = m_vGoogleMap.getCameraPosition().target;
        m_isSearchPostition = true;
        requestShopList(center.latitude, center.longitude, 2000, false, 200);
    }


    private void requestShopList(double lat, double longti, double dist, boolean isAllLocation, int cnt) {
        m_isSearchAllLocation = isAllLocation;

        Utility.hideSoftKeyboard(this);

        String url = ServerAPIPath.API_GET_SHOP_LIST;

        EditText etSearchText = (EditText) findViewById(R.id.et_search_shop_text);
        String params = "";
        boolean isChange = false;
        if(etSearchText.getText() != null && etSearchText.getText().toString().equals("") != true) {
            String utf8Name = etSearchText.getText().toString().trim();

            try {
                utf8Name = URLEncoder.encode(utf8Name, "UTF-8");
            }
            catch (Exception e){

            }
            params = "?shopName="+utf8Name;
            isChange = true;
        }

        String text = m_etLocation.getText().toString();
        if(m_isSearchPostition == false && text.equals(m_strDefaultLocation) == false && text.equals("") == false){
            String utf8Location = LocationSearchActivity.getFilteredLocationText(text);

            try {
                utf8Location = URLEncoder.encode(utf8Location, "UTF-8");
            }
            catch (Exception e){

            }
            if(isChange == true) {
                params += "&location="+utf8Location;
            }
            else {
                params = "?location="+utf8Location;
                isChange = true;
            }
        }
        else {
            if(isAllLocation == false) {
                if (isChange == true) {
                    params += "&cur_lat=" + lat + "&cur_long=" + longti + "&radius=" + dist;
                } else {
                    params = "?cur_lat=" + lat + "&cur_long=" + longti + "&radius=" + dist;
                    isChange = true;
                }
            }
        }

        if(m_nCategoryIdx > 0) {
            int category_id = m_lstCategory.get(m_nCategoryIdx).getId();
            if(isChange == true) {
                params += "&category_id="+category_id;
            }
            else {
                params = "?category_id="+category_id;
                isChange = true;
            }
        }

        if(cnt != 0) {
            if(isChange == true) {
                params += "&cnt="+cnt;
            }
            else {
                params = "?cnt="+cnt;
                isChange = true;
            }
        }

        m_nBeforeCategoryIdx = m_nCategoryIdx;

        url += params;
        Log.e("test",url);
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseShopList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShopSearchActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    private void responseShopList(Object o) {
        try {
            m_lstShop.clear();
            m_lstTempShop.clear();

            JSONArray dataArray = (JSONArray)o;

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                Shop shop = new Shop(jsonObject);

                m_lstShop.add(shop);
                m_lstTempShop.add(shop);
            }
        } catch (Exception e) {
            Logger.e(TAG, "responseShopList - JSONException : " + e.getMessage());
            Toast.makeText(ShopSearchActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }

        showShopBar();
    }

    private void showShopBar() {
        if(m_vGoogleMap == null) return;

        if(m_lstShop.size() == 0) {
            m_hlvShopList.setVisibility(View.GONE);
            m_rlShopBar.setVisibility(View.GONE);
        }
        else {
            m_hlvShopList.setVisibility(View.VISIBLE);
            m_rlShopBar.setVisibility(View.VISIBLE);
        }


        ArrayList<Shop> arrShop = new ArrayList<Shop>();

        // first: shopSequence
        // second: image and price
        // third: other
        for(int i = 0; i < (m_lstShop.size() - 1); i++) {
            Shop shop1 = m_lstShop.get(i);
            for(int j = (i+1); j < m_lstShop.size(); j++) {
                Shop shop2 = m_lstShop.get(j);

                if(shop2.getShopSequence() <shop1.getShopSequence()) {
                    m_lstShop.set(j, shop1);
                    m_lstShop.set(i, shop2);
                }
            }
        }

        for(int i = 0; i < m_lstShop.size();i++) {
            Shop shop = m_lstShop.get(i);
            shop.isAdded = false;

            if(shop.getShopSequence() != LIMIT_SEQUE) {
                shop.isAdded = true;
                arrShop.add(shop);
            }
        }

        // image and price
        for(int i = 0; i < m_lstShop.size(); i++) {
            Shop shop = m_lstShop.get(i);

            if(shop.isAdded == false  && shop.getShopSubImageArray().size() != 0 && shop.getShopPriceImageArray().size() != 0) {
                arrShop.add(shop);
                shop.isAdded = true;
            }
        }

        // image
        for(int i = 0; i < m_lstShop.size(); i++) {
            Shop shop = m_lstShop.get(i);

            if(shop.isAdded == false  && shop.getShopSubImageArray().size() != 0) {
                arrShop.add(shop);
                shop.isAdded = true;
            }
        }

        // price
        for(int i = 0; i < m_lstShop.size(); i++) {
            Shop shop = m_lstShop.get(i);

            if(shop.isAdded == false  && shop.getShopPriceImageArray().size() != 0) {
                arrShop.add(shop);
                shop.isAdded = true;
            }
        }

        // other
        for(int i = 0; i < m_lstShop.size(); i++) {
            Shop shop = m_lstShop.get(i);

            if(shop.isAdded == false) {
                arrShop.add(shop);
                shop.isAdded = true;
            }
        }


        if(arrShop.size() > shop_count) {
            m_lstShop = new ArrayList<Shop>();
            for (int i = 0; i < shop_count; i++) {
                Shop shop = arrShop.get(i);
                m_lstShop.add(shop);
            }
        }
        else {
            m_lstShop = arrShop;
        }

        m_adpMyShop = new MyShopAdapter(this, R.layout.item_row_my_shop, m_lstShop);
        m_hlvShopList.setAdapter(m_adpMyShop);

        m_adpMyShop.notifyDataSetChanged();

        showMapIcons();

        if(pressedRealTimeShop == true) {
            LatLng center = m_vGoogleMap.getCameraPosition().target;

            int size = m_lstShop.size();
            int idx = -1;
            float minDistance = 9999999;
            for(int i = 0; i < size; i++) {
                Shop shop = m_lstShop.get(i);
                float dist = Utility.getDisance(shop, center);
                if(dist < minDistance && shop.isShopEnded() == false) {
                    minDistance = dist;
                    idx = i;
                }
            }

            if(idx != -1) {
                Shop shop = m_lstShop.get(idx);
                gotoMoveCamera(shop.getShopLatitude(), shop.getShopLongtitude(), defaultZoom);
            }

            pressedRealTimeShop = false;
        }

        if(m_isSearchAllLocation == true && m_lstShop.size() > 0) {
            Shop shop = m_lstShop.get(0);
            gotoMoveCamera(shop.getShopLatitude(), shop.getShopLongtitude(), defaultZoom);
        }
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
                        Toast.makeText(ShopSearchActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseCategoryList(Object json){
        try {
            JSONArray dataArray = (JSONArray)json;

            Category category = new Category();
            category.setName("전체");
            category.setId(0);
            m_lstCategory.add(category);

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                category = new Category(jsonObject);

                if(category.isShopCategory() == true) {
                    m_lstCategory.add(category);
                }
            }

            m_adpCategory.notifyDataSetChanged();

            if(m_isFirstStart == true && m_nFirstCategoryIdentifier != 0) {

                int size = m_lstCategory.size();

                String categoryName = "헤어";

                if(m_nFirstCategoryIdentifier == 1) {
                    categoryName = "네일";
                } else  if(m_nFirstCategoryIdentifier == 2) {
                    categoryName = "헤어";
                } else  if(m_nFirstCategoryIdentifier == 3) {
                    categoryName = "속눈썹연장";
                } else  if(m_nFirstCategoryIdentifier == 4) {
                    categoryName = "왁싱";
                } else  if(m_nFirstCategoryIdentifier == 5) {
                    categoryName = "피부";
                } else  if(m_nFirstCategoryIdentifier == 6) {
                    categoryName = "마사지";
                } else  if(m_nFirstCategoryIdentifier == 7) {
                    categoryName = "반영구화장";
                } else  if(m_nFirstCategoryIdentifier == 8) {
                    categoryName = "타투";
                }




                for(int i = 0; i < size; i++) {
                    Category categor1 = m_lstCategory.get(i);
                    if(categor1.getName().contains(categoryName) == true) {
                        m_spCategory.setSelection(i);
                        m_nCategoryIdx = i;
                        break;
                    }
                }
                m_isFirstStart = false;

                Location location = AppController.getInstance().getLocation();
                searchShopWithLocation(location.getLatitude(), location.getLongitude(), defaultDistance);
            }

        } catch (Exception e) {
            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestGetLatLngFromAddress(String address) {
        String url = ServerAPIPath.API_GET_LATLNG;

        String utf8Location = null;
        try {
            utf8Location = URLEncoder.encode(address.trim(), "UTF-8");
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
                        Toast.makeText(ShopSearchActivity.this, s, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray jsonArray = response.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry").getJSONObject("location");
            double tempLat = jsonObject1.getDouble("lat");
            double tempLng = jsonObject1.getDouble("lng");

            gotoMoveCamera(tempLat, tempLng, defaultZoom);

        } catch (Exception e) {
            Logger.e(TAG, "responseGetLatLngFromAddress - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }
}


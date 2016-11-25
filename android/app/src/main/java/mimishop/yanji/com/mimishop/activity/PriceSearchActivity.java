package mimishop.yanji.com.mimishop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.shopinfo.ShopDetailActivity;
import mimishop.yanji.com.mimishop.adapter.SearchPriceAdapter;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.Location;
import mimishop.yanji.com.mimishop.modal.Product;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.ComboBox;

public class PriceSearchActivity extends Activity implements AbsListView.OnScrollListener {

    public static final String TAG = "PriceSearchActivity";

    private static final int   REQ_SELECT_LOCATION = 1;

    private Spinner  m_spPriceSort = null;
    private EditText m_etSearchName = null;
    private EditText m_etLocation = null;
    private ListView m_lvPrice = null;

    private View      m_llMain = null;
    private ImageView m_ivEmpty = null;

    private SearchPriceAdapter m_aptPrice = null;

    private ArrayList<String> m_lstPriceSort = new ArrayList<String>();
    private ArrayList<Product> m_lstPrice = new ArrayList<Product>();

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    private mimishop.yanji.com.mimishop.modal.Location m_pCurrentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_search);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_price_search, menu);
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

        initLocationList();

        initSearchBar();

        initPriceList();

        m_ivEmpty = (ImageView)findViewById(R.id.iv_empty);
        m_llMain = findViewById(R.id.ll_main);

        initKeyboradTouch();
    }

    private void initKeyboradTouch() {
        View view = findViewById(R.id.rl_content);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utility.hideSoftKeyboard(PriceSearchActivity.this);
                return false;
            }
        });

        m_lvPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utility.hideSoftKeyboard(PriceSearchActivity.this);
                int idx = i - 1;
                if (idx >= 0) {
                    startShopDetailActivity(m_lstPrice.get(idx));
                }
            }
        });
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("내 주변 가격검색");

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

    private void initLocationList() {
        m_etLocation = (EditText) findViewById(R.id.et_location);
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
                    searchProductList();
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
    }

    private void startLocationSearchActivity() {
        Intent intent = new Intent(PriceSearchActivity.this, LocationSearchActivity.class);
        startActivityForResult(intent, REQ_SELECT_LOCATION);
    }

    private void startShopDetailActivity(Product shop){

        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra("shop_id", shop.getProductShopID());
        startActivity(intent);
    }

    public  void setCurrentLocation( mimishop.yanji.com.mimishop.modal.Location location) {
        m_etLocation.setText(location.getRealLocationName());

        m_pCurrentLocation = location;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK)
        {
             if(requestCode == REQ_SELECT_LOCATION) {
                if(LocationSearchActivity.SelectedLocation != null) {
                    setCurrentLocation(LocationSearchActivity.SelectedLocation);
                    searchProductList();
                }
            }
        }
    }

    private void initSearchBar() {
        ImageButton button = (ImageButton)findViewById(R.id.ib_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProductList();
            }
        });

        ImageButton ibChangeCategory = (ImageButton)findViewById(R.id.ib_change_category);
        ibChangeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_spPriceSort.performClick();
            }
        });

        m_etSearchName = (EditText)findViewById(R.id.et_search_product_text);
        m_etSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchProductList();
                    return true;
                }

                return false;
            }
        });
    }


    private void initPriceList() {
        m_lvPrice = (ListView)findViewById(R.id.lv_sort_product);
        m_aptPrice = new SearchPriceAdapter(this, R.layout.item_row_search_price, m_lstPrice);
        View header = getLayoutInflater().inflate(R.layout.item_row_search_price_header, null, false);
        m_lvPrice.addHeaderView(header);
        m_aptPrice.setShowRating(true);
        m_lvPrice.setAdapter(m_aptPrice);

        m_lvPrice.setOnScrollListener(this);

        m_lstPriceSort.add("별표순");
        m_lstPriceSort.add("가격(낮은순)");
        m_lstPriceSort.add("가격(높은순)");

        m_spPriceSort = (Spinner)findViewById(R.id.sp_price_sort);
        //set the ArrayAdapter to the spinner
        m_spPriceSort.setAdapter(new ArrayAdapter<String>(this, R.layout.item_row_spinner_star, m_lstPriceSort) {
            @Override
            public int getCount() {
                return m_lstPriceSort.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if(v == null)
                {
                    LayoutInflater vi = (LayoutInflater) PriceSearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate( R.layout.item_row_spinner_star, null);
                }

                v.setBackgroundColor(getContext().getResources().getColor(R.color.white));

                String rm = getItem( position);
                TextView cpText = (TextView) v.findViewById( R.id.tv_name);
                cpText.setText( rm);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if(v == null)
                {
                    LayoutInflater vi = (LayoutInflater) PriceSearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate( R.layout.item_row_spinner_star, null);
                }

                String rm = getItem( position);
                TextView cpText = (TextView) v.findViewById( R.id.tv_name);
                cpText.setTextColor(PriceSearchActivity.this.getResources().getColorStateList(R.color.text_cast_item_selector));
                cpText.setText( rm);
                v.setBackground(PriceSearchActivity.this.getResources().getDrawable(R.drawable.bg_list_category_item_selector));
                return v;
            }
        });

        m_spPriceSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshListView(m_spPriceSort.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void refreshListView(int postion) {
        int i = postion;

        if(i == 0) { // star order
            sortProductListByStar();
        }
        else {

            if(i == 1) { // low price
                sortProductList(true);
            }
            else if(i == 2) { // high price
                sortProductList(false);
            }
        }

        m_aptPrice.notifyDataSetChanged();
    }

    private void refreshUI() {
        if(m_lstPrice.size() == 0) {
            m_llMain.setVisibility(View.GONE);
            m_ivEmpty.setVisibility(View.VISIBLE);
        }
        else  {
            m_llMain.setVisibility(View.VISIBLE);
            m_ivEmpty.setVisibility(View.GONE);

            refreshListView(m_spPriceSort.getSelectedItemPosition());
        }

        m_aptPrice.notifyDataSetChanged();
        m_lvPrice.setItemChecked(-1, true);
    }

    private void sortProductListByStar() {
        if(m_lstPrice.size() == 0) {
            return;
        }
        for(int i = 0; i < (m_lstPrice.size()-1); i++) {
            for(int j = i + 1; j < m_lstPrice.size(); j++) {
                Product product1 = m_lstPrice.get(i);
                Product product2 = m_lstPrice.get(j);

                if (product1.getShopLevel() < product2.getShopLevel()) {
                        m_lstPrice.set(i, product2);
                        m_lstPrice.set(j, product1);
                }
            }
        }
    }


    private void sortProductList(boolean isPriceOrder){
        if(m_lstPrice.size() == 0) {
            return;
        }

        for(int i = 0; i < (m_lstPrice.size()-1); i++) {
            for(int j = i + 1; j < m_lstPrice.size(); j++) {
                Product product1 = m_lstPrice.get(i);
                Product product2 = m_lstPrice.get(j);

                if(isPriceOrder == true) {
                    if (product1.getProductPrice() > product2.getProductPrice()) { // low price
                        m_lstPrice.set(i, product2);
                        m_lstPrice.set(j, product1);
                    }
                }
                else  if(isPriceOrder == false) { // high price
                    if (product1.getProductPrice() < product2.getProductPrice()) {
                        m_lstPrice.set(i, product2);
                        m_lstPrice.set(j, product1);
                    }
                }
            }
        }
    }

    private void searchProductList() {
        Utility.hideSoftKeyboard(this);
        m_nPrePageNum = -1;
        requestSearchProductList(1);
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
        requestSearchProductList(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestSearchProductList(1);
    }


    private void requestSearchProductList(int pageNum){

        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvPrice.setSelection(0);
            m_lstPrice.clear();
        }

        String url = ServerAPIPath.API_GET_SEARCH_PRODUCT_NAME;
        String utf8Name = m_etSearchName.getText().toString();

        if(utf8Name.equals("") == true) {
            Toast.makeText(this, "검색문자렬을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            utf8Name = utf8Name.trim();
            utf8Name = URLEncoder.encode(utf8Name, "UTF-8");
        }
        catch (Exception e) {

        }

        url+="?product_name="+utf8Name;
        url+="&page_num="+pageNum;

        url+="&order_type="+m_spPriceSort.getSelectedItemPosition();

        String locationName = m_etLocation.getText().toString();
        if(locationName.equals("") == false && locationName.equals("전체") == false) {

            String utf8Location = locationName;

            try {
                utf8Location = URLEncoder.encode(utf8Location, "UTF-8");
            }
            catch (Exception e){

            }

            url += "&location_name="+utf8Location;
        }

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseSearchProductList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(PriceSearchActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    private void responseSearchProductList(Object json){
        if(json == null) {
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonObject = (JSONObject)json;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
            JSONArray dataArray = jsonObject.getJSONArray("list");

            if(dataArray.length() > 0) {
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    Product product = new Product(dataObject);

                    m_lstPrice.add(product);
                }
            }

            m_aptPrice.notifyDataSetChanged();

            TextView textView = (TextView)findViewById(R.id.tv_result_title);
            if(m_lstPrice.size() > 0) {
                textView.setText("\""+m_etSearchName.getText().toString()+"\""+" 검색결과");
            }
            else {
                textView.setText("검색결과: 없음");
            }
        }
        catch (Exception e) {
            Logger.e(TAG, "responseSearchProductList - JSONException : " + e.getMessage());
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }

        refreshUI();
    }
}


package mimishop.yanji.com.mimishop.activity.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.LocationSearchActivity;
import mimishop.yanji.com.mimishop.activity.shopinfo.ShopDetailActivity;
import mimishop.yanji.com.mimishop.adapter.EventAdapter;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.ComboBox;

public class EventActivity extends Activity  implements AbsListView.OnScrollListener{

    public static  final String TAG = "EventActivity";
    private static final int   REQ_SELECT_LOCATION = 1;

    private EventAdapter m_adpEvent = null;
    private ArrayList<Event> m_lstEvents = new ArrayList<Event>();
    private ListView m_lvEvents = null;

    private ComboBox m_spCategory = null;
    private EditText m_etLocation = null;
    private ArrayList<Category> m_lstCategory = new ArrayList<Category>();

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    boolean isFirstLoading = true;

    private mimishop.yanji.com.mimishop.modal.Location m_pCurrentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_event, menu);
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
        m_adpEvent = new EventAdapter(this, R.layout.item_row_event, m_lstEvents);
        m_lvEvents = (ListView) findViewById(R.id.lv_sort_product);

        m_lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utility.hideSoftKeyboard(EventActivity.this);
                requestIncreaseEventClick(m_lstEvents.get(i));
                startEventDetail(m_lstEvents.get(i));
                }
        });

        m_lvEvents.setAdapter(m_adpEvent);
        m_lvEvents.setOnScrollListener(this);

        initLocationList();
        initCategory();

        m_nPreLastNum = 0;
        m_nPrePageNum = -1;

        requestEventList(1, true);

        initKeyboradTouch();
    }

    private void initKeyboradTouch() {
        View view = findViewById(R.id.rl_content);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utility.hideSoftKeyboard(EventActivity.this);
                return false;
            }
        });
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
        if(true) {
            m_etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        requestEventList(1, true);
                        return true;
                    }

                    return false;
                }
            });
        }


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
        Intent intent = new Intent(EventActivity.this, LocationSearchActivity.class);
        startActivityForResult(intent, REQ_SELECT_LOCATION);
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
                    requestEventList(1, true);
                }
            }
        }
    }

    private void initCategory(){
        m_spCategory = (ComboBox)findViewById(R.id.sp_category);

        //set the ArrayAdapter to the spinner
        m_spCategory.setAdapter(new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, m_lstCategory) {

            class ViewHolder {
                TextView tv_text;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
                    holder = new ViewHolder();
                    holder.tv_text = (TextView) convertView.findViewById(android.R.id.text1);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Category category = m_lstCategory.get(position);
                holder.tv_text.setTextSize(13);
                holder.tv_text.setTextColor(getContext().getResources().getColor(R.color.text_gray));
                holder.tv_text.setText(category.getName());

                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // notifyDataSetChanged();
                View v = convertView;
                if(v == null)
                {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate( android.R.layout.simple_spinner_dropdown_item, null);
                }

                Category rm = getItem( position);
                String boardTitle = rm.getName();

                TextView cpText = (TextView) v.findViewById( android.R.id.text1);
                cpText.setTextSize(13);
                cpText.setTextColor(getContext().getResources().getColor(R.color.text_gray));
                cpText.setText( boardTitle);

                DisplayMetrics metrics = parent.getResources().getDisplayMetrics();
                float dp = 30f;
                float fpixels = metrics.density * dp;
                int pixels = (int) (fpixels + 0.5f);

                cpText.setHeight(pixels);

                return v;
            }
        });

        m_spCategory.setOnItemClickListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(isFirstLoading == true) {
                    isFirstLoading = false;
                }
                else {
                    requestEventList(1, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        m_spCategory.setSelectedIndex(0);

        requestCategoryList();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("이벤트 모아보기");

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

    private void startEventDetail(Event event) {
        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra("shop_id", event.getEventShopID());
        startActivity(intent);
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
        requestEventList(pageNum, false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void requestCategoryList(){

        String url = ServerAPIPath.API_GET_CATEGORY_LIST;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            m_lstCategory.clear();

                            JSONArray dataArray = (JSONArray) o;

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

                            SpinnerAdapter adapter = m_spCategory.getAdapter();

                            ((ArrayAdapter<String>)adapter).notifyDataSetChanged();

                        } catch (Exception e) {
                            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
                            Toast.makeText(EventActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(EventActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestEventList(int pageNum, boolean isChangeParam)  {
        if (m_nPrePageNum == pageNum && isChangeParam == false) {
            return;
        }
        
        if(pageNum == 1) {
            m_lvEvents.setSelection(0);
        	m_lstEvents.clear();
        }

        m_nPrePageNum = pageNum;

        String url = ServerAPIPath.API_GET_SHOP_EVENT_LIST;
        String params = "";
        boolean isChange = false;

        String text = m_etLocation.getText().toString();
        if(text.equals("전체") == false && text.equals("") == false){

            String utf8Location = text;

            try {
                utf8Location = URLEncoder.encode(utf8Location, "UTF-8");
            }
            catch (Exception e){

            }

            if(isChange == true) {
                params += "&location="+ utf8Location;
            }
            else {
                params = "?location="+utf8Location;
                isChange = true;
            }
        }
        if(m_spCategory.getSelectedIndex() > 0) {
            if(isChange == true) {
                params += "&category_id="+m_lstCategory.get(m_spCategory.getSelectedIndex()).getId();
            }
            else {
                params = "?category_id="+m_lstCategory.get(m_spCategory.getSelectedIndex()).getId();
                isChange = true;
            }
        }

        if(pageNum >= 1) {
            if(isChange == false) {
                params += "?page_num="+pageNum;
            }
            else {
                params += "&page_num="+pageNum;
            }
        }

        url += params;

        Utility.showWaitingDlg(this);
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

                            for(int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Event event = new Event(dataObject);

                                m_lstEvents.add(event);
                            }

                        } catch (Exception e) {
                            Logger.e(TAG, "responseEventList - JSONException : " + e.getMessage());
                            Toast.makeText(EventActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                        m_adpEvent.notifyDataSetChanged();

                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(EventActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void requestIncreaseEventClick(final Event event) {
        String url = ServerAPIPath.API_POST_EVENT_CLICK;
        Map<String, String> params = new HashMap<String, String>();
        params.put("event_id", String.format("%d", event.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject object = (JSONObject) o;
                            int cnt = object.getInt("eventClickCnt");
                            event.setEventClickCnt(cnt);

                            m_adpEvent.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(EventActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(EventActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }, params);
    }
}

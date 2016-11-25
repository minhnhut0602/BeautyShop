package mimishop.yanji.com.mimishop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Location;
import mimishop.yanji.com.mimishop.modal.Tube;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class LocationSearchActivity extends Activity {

    public final static String TAG = "LocationSearchActivity";

    public static Location SelectedLocation = null;
    private EditText m_etSearchName;
    private ListView m_lvLocation;
    private ImageView m_ivEmpty;

    private ArrayList<Location> m_arrLocation = new ArrayList<Location>();

    private static  LocationSearchActivity mInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        mInstance = this;
        init();

        SelectedLocation = null;
    }

    public static LocationSearchActivity  getInstance() {
        return mInstance;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_tube_search, menu);
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
    protected void onDestroy() {
        mInstance = null;
        super.onDestroy();
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

                requestLocationList(m_etSearchName.getText().toString());
            }
        });

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_location_search));
    }

    private void init() {
        initMenu();

        m_etSearchName = (EditText)findViewById(R.id.et_search_text);

        m_etSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utility.hideSoftKeyboard(LocationSearchActivity.this);
                    requestLocationList(m_etSearchName.getText().toString());
                    return true;
                }
                return false;
            }
        });

        m_etSearchName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
                requestLocationList(m_etSearchName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        m_lvLocation = (ListView)findViewById(R.id.lv_location);

        m_lvLocation.setAdapter(new ArrayAdapter<Location>(this, R.layout.item_row_location_1, m_arrLocation){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Location envelop = m_arrLocation.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate( R.layout.item_row_location_1, null);
                }

                mimishop.yanji.com.mimishop.modal.Location rm = getItem( position);
                String boardTitle = rm.getLocationName();

                TextView cpText = (TextView) convertView.findViewById(R.id.tv_location);

                cpText.setText( boardTitle);
                return convertView;
            }
        });

        m_lvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Location tube = m_arrLocation.get(i);
                finishWithResult(tube);
            }
        });

        ImageButton button = (ImageButton)findViewById(R.id.ib_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationList(m_etSearchName.getText().toString());
            }
        });

        button = (ImageButton)findViewById(R.id.ib_set_current_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showWaitingDlg(LocationSearchActivity.this);

                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        String address = Utility.getLocation(LocationSearchActivity.this, AppController.getInstance().getLocation());
                        m_etSearchName.setText(address);

                        Utility.hideWaitingDlg();
                    }
                }, 100);
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                TextView label = (TextView)findViewById(R.id.tv_set_current_location);

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    label.setPressed(true);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    label.setPressed(false);
                }

                return false;
            }
        });

        m_ivEmpty = (ImageView)findViewById(R.id.iv_empty);
    }

    private void search() {

        m_arrLocation.clear();

        int size = Common.mArrLocation.size();

        for(int i = 0; i < size; i++) {
            m_arrLocation.add(Common.mArrLocation.get(i));
        }

        if(m_arrLocation.size() > 0) {
            m_lvLocation.setVisibility(View.VISIBLE);
            m_ivEmpty.setVisibility(View.GONE);
            ((ArrayAdapter<Location>) m_lvLocation.getAdapter()).notifyDataSetChanged();
        }
        else {
            m_lvLocation.setVisibility(View.GONE);
            m_ivEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void finishWithResult(Location result) {

        Utility.hideSoftKeyboard(this);

        Intent resultIntent = new Intent();
        SelectedLocation = result;
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public static String getFilteredLocationText(String searchText) {
        String result = searchText.trim();
        String [][]filterPattern =
                {
                        {"서울", "서울시", "서울특별시"},
                        {"부산", "부산시", "부산광역시"},
                        {"광주", "광주시", "광주광역시"},
                        {"인천", "인천시", "인천광역시"},
                        {"울산", "울산시", "울산광역시"},
                        {"세종", "세종시", "세종특별자치시"},
                        {"대구", "대구시", "대구광역시"},
                        {"제주", "제주도", "제주특별자치도"}
                };

        String []arrSearch = searchText.split(" ");

        String firstString = arrSearch[0];
        for(int i = 0; i < filterPattern.length; i++) {
            if(firstString.equals(filterPattern[i][0]) == true || firstString.equals(filterPattern[i][1]) == true) {
                result = searchText.replace(firstString, filterPattern[i][2]);
                break;
            }
        }
        return result;
    }

    private void requestLocationList(String searhText){

        String url = ServerAPIPath.API_GET_LOCATION_LIST;
//        searhText = "75 Zhu Jiang Jie, Huanggu Qu, Shenyang Shi, Liaoning Sheng, China, 110000";
        if(searhText != null && searhText.equals("") == false) {
            String utf8Name = getFilteredLocationText(searhText);
            try {
                utf8Name = utf8Name.trim();
                utf8Name = URLEncoder.encode(utf8Name, "UTF-8");
            }
            catch (Exception e) {

            }
            url = url + "?search_word="+utf8Name;
        }
//        url = "http://www.miggle.co.kr:8080/api/getLocationList?search_word=75%20Zhu%20Jiang%20Jie%2C%20Huanggu%20Qu%2C%20Shenyang%20Shi%2C%20Liaoning%20Sheng%2C%20China%2C%20110000";
        ServerAPICall.getInstance().callGET(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {
                        responseLocationList(s);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String err) {
                        Toast.makeText(LocationSearchActivity.this, err, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseLocationList(Object json) {
        if(json == null) {
            Toast.makeText(LocationSearchActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ArrayList<Location> arrayList = Common.mArrLocation;
            arrayList.clear();

            JSONArray dataArray = (JSONArray)json;
            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                mimishop.yanji.com.mimishop.modal.Location location = new mimishop.yanji.com.mimishop.modal.Location(dataObject);

                arrayList.add(location);
            }

            search();
        }
        catch (Exception e) {
            Toast.makeText(this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseLocationList - JSONException : " + e.getMessage());
        }
    }
}

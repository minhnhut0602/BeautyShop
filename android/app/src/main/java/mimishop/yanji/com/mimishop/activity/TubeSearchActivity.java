package mimishop.yanji.com.mimishop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Envelop;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.modal.Tube;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class TubeSearchActivity extends Activity {

    public final static String TAG = "TubeSearchActivity";

    private EditText m_etSearchName;
    private ListView m_lvTube;

    private ArrayList<Tube> m_arrTube = new ArrayList<Tube>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tube_search);

        init();
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

    private void initMenu() {
        View view = findViewById(R.id.menu_main);
        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	Utility.hideSoftKeyboard(TubeSearchActivity.this);
                    finish();
                }
            });
        }

        ImageButton backSearch = (ImageButton)view.findViewById(R.id.btn_search);
        backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search();
            }
        });

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_tube_search));
    }

    private void init() {

        initMenu();

        m_etSearchName = (EditText)findViewById(R.id.et_search_text);

        m_etSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utility.hideSoftKeyboard(TubeSearchActivity.this);
                    search();
                    return true;
                }
                return false;
            }
        });

        m_etSearchName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
                search();
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

        m_lvTube = (ListView)findViewById(R.id.lv_tube);

        m_lvTube.setAdapter(new ArrayAdapter<Tube>(this, R.layout.item_row_tube, m_arrTube){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Tube envelop = m_arrTube.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate( R.layout.item_row_tube, null);
                }

                TextView tvContent = (TextView)convertView.findViewById(R.id.tv_name);
                tvContent.setText(envelop.getTubeName());

                LinearLayout llSub = (LinearLayout)convertView.findViewById(R.id.ll_sub);
                ArrayList<Tube> subList = envelop.getSubTubList();

                int size = subList.size();

                llSub.removeAllViews();

                for(int i = 0; i < size; i++) {

                    Tube sub1 = subList.get(i);

                    TextView textView  = new TextView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(5,0,0,0);
                    textView.setLayoutParams(params);
                    textView.setLines(1);
                    textView.setEllipsize(TextUtils.TruncateAt.END);

                    textView.setText(sub1.getTubeSubName());
                    textView.setTextColor(getContext().getResources().getColor(R.color.white));
                    textView.setPadding(15,3,15,3);

                    String []subName = {"1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선", "신분당선", "분당선"};
                    int []subColor = {R.drawable.layout_bg_tube_1_round, R.drawable.layout_bg_tube_2_round, R.drawable.layout_bg_tube_3_round, R.drawable.layout_bg_tube_4_round, R.drawable.layout_bg_tube_5_round
                                     , R.drawable.layout_bg_tube_6_round, R.drawable.layout_bg_tube_7_round, R.drawable.layout_bg_tube_8_round, R.drawable.layout_bg_tube_9_round, R.drawable.layout_bg_tube_10_round, R.drawable.layout_bg_tube_11_round};

                    boolean isChange = false;
                    for(int j = 0; j < subName.length; j++) {
                        if(sub1.getTubeSubName().contains(subName[j]) == true) {
                            textView.setBackground(getContext().getResources().getDrawable(subColor[j]));
                            isChange = true;
                            break;
                        }
                    }

                    if(isChange == false) {
                        textView.setBackground(getContext().getResources().getDrawable(subColor[10]));
                    }

                    llSub.addView(textView);
                }

                return convertView;
            }
        });

        m_lvTube.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tube tube = m_arrTube.get(i);
                finishWithResult(tube);
            }
        });

        ImageButton button = (ImageButton)findViewById(R.id.ib_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    private void search() {

        m_arrTube.clear();

        String text = m_etSearchName.getText().toString().trim();

        ArrayList<Tube> arrTube = Common.mArrTube;
        int size = arrTube.size();
        for(int i = 0; i < size; i++) {
            Tube tube = arrTube.get(i);

            if(tube.getTubeName().contains(text) == true) {
                m_arrTube.add(tube);
            }
        }

        ((ArrayAdapter<Tube>)m_lvTube.getAdapter()).notifyDataSetChanged();
    }

    private void finishWithResult(Tube result) {

        Utility.hideSoftKeyboard(this);

        Intent resultIntent = new Intent();
        ShopSearchActivity activity = ShopSearchActivity.getInstance();
        if(activity != null) {
            activity.setTube(result);
        }
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public static void requestTubeList() {
        String url = ServerAPIPath.API_GET_TUBE_LIST;

        if(false) {
            /*if (m_etSearchName.getText() != null && m_etSearchName.getText().toString().equals("") != true) {
                String utf8Name = m_etSearchName.getText().toString();

                try {
                    utf8Name = URLEncoder.encode(utf8Name, "UTF-8");
                } catch (Exception e) {

                }
                url += "?tubeName=" + utf8Name;
            }*/
        }
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            ArrayList<Tube> arrTube = Common.mArrTube;
                            arrTube.clear();

                            JSONArray dataArray = (JSONArray)o;

                            for(int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                Tube shop = new Tube(jsonObject);

                                arrTube.add(shop);
                            }

                            sortTubeList(arrTube);

                        } catch (Exception e) {
                            Logger.e(TAG, "responseTubeList - JSONException : " + e.getMessage());
                            Toast.makeText(MainActivity.getInstance(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MainActivity.getInstance(), s, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private static void sortTubeList(ArrayList<Tube> arrTube) {

        int size = arrTube.size();

        ArrayList<Tube> newTubeList = new ArrayList<Tube>();

        for(int i = 0; i < size; i++) {

            Tube tube = arrTube.get(i);

            if(tube.isFlag == true) {
                continue;
            }

            Tube newTube = new Tube();
            newTube.setTubeName(tube.getTubeName());
            newTube.setTubeLatitude(tube.getTubeLatitude());
            newTube.setTubeLongtitude(tube.getTubeLongtitude());
            newTube.addSubTube(tube);
            tube.isFlag = true;
            for(int j = 0; j < size;j++) {
                Tube tube1 = arrTube.get(j);

                if(tube1.isFlag == true) {
                    continue;
                }

                if(tube.getTubeLatitude() == tube1.getTubeLatitude() || tube.getTubeLongtitude() == tube1.getTubeLongtitude()){
                    newTube.addSubTube(tube1);
                    tube1.isFlag = true;
                }
            }

            newTubeList.add(newTube);
        }

        Common.mArrTube = newTubeList;
    }
}

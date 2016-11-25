package mimishop.yanji.com.mimishop.activity.mypage;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.Envelop;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class MyEnvelopeActivity extends Activity implements AbsListView.OnScrollListener{

    public final String TAG = "MyEnvelopeActivity";

    private CheckBox m_cbDelete;
    private Button   m_btnDelete;
    private ListView m_lstEnvelop;

    private ArrayList<Envelop> m_arrEnvelop = new ArrayList<Envelop>();

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_envelope);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_my_envelope, menu);
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

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_my_envelope));
        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doReaded();
                    finish();
                }
            });
        }
    }

    private void init() {
        initMenu();

        m_cbDelete = (CheckBox)findViewById(R.id.cb_delete);

        m_cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                allCheck(b);
            }
        });

        m_btnDelete = (Button) findViewById(R.id.btn_delete);

        m_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReadedAndDelete();
            }
        });

        m_lstEnvelop = (ListView) findViewById(R.id.lv_envelop);

        m_lstEnvelop.setAdapter(new ArrayAdapter<Envelop>(this, R.layout.item_row_envelop, m_arrEnvelop){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Envelop envelop = m_arrEnvelop.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate( R.layout.item_row_envelop, null);
                }

                TextView tvContent = (TextView)convertView.findViewById(R.id.tv_content);
                TextView tvTime = (TextView)convertView.findViewById(R.id.tv_time);
                CheckBox cbDelete = (CheckBox)convertView.findViewById(R.id.cb_delete);
                ImageView imgEnvelop = (ImageView)convertView.findViewById(R.id.iv_envelop);

                View root = convertView.findViewById(R.id.root);

                tvContent.setText(envelop.getEnvelopContent());
                tvTime.setText(Utility.getInstance().getTime(envelop.getEnvelopPostTime()));

                if(envelop.isDeleted() == true) {
                    cbDelete.setChecked(true);
                }
                else {
                    cbDelete.setChecked(false);
                }

                cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        envelop.setDeleted(b);
                    }
                });

                if(envelop.isEnvelopReaded() == true) {
                    imgEnvelop.setSelected(false);
                    root.setSelected(false);
                    root.setBackground(getResources().getDrawable(R.drawable.layout_bg_envelop_selected));
                }
                else {
                    imgEnvelop.setSelected(true);
                    root.setSelected(true);
                    root.setBackground(getResources().getDrawable(R.drawable.layout_bg_envelop));
                }

                return convertView;
            }
        });

        m_lstEnvelop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Envelop envelop = m_arrEnvelop.get(i);
                boolean isChecked = envelop.isDeleted();
                if (isChecked == true) {
                    envelop.setDeleted(false);
                } else {
                    envelop.setDeleted(true);
                }
                showEnvelop();
            }
        });

        m_lstEnvelop.setOnScrollListener(this);

        requestEnvelopList(1);
    }

    private  void allCheck(boolean isChecked) {
        int size = m_arrEnvelop.size();

        for(int i = 0; i < size; i++) {
            m_arrEnvelop.get(i).setDeleted(isChecked);
        }

        ((ArrayAdapter<Envelop>)m_lstEnvelop.getAdapter()).notifyDataSetChanged();
    }

    private void doReadedAndDelete() {

        int size = m_arrEnvelop.size();
        ArrayList<Envelop> arrayList = new ArrayList<Envelop>();
        for(int i = 0; i < size; i++) {
           Envelop envelop = m_arrEnvelop.get(i);

           envelop.setEnvelopReaded(true);
           if(envelop.isChanged() == true) {
               arrayList.add(envelop);
           }
        }

        requestModifyEnvelopList(true, arrayList);
    }

    private void doReaded() {
        int size = m_arrEnvelop.size();
        ArrayList<Envelop> arrayList = new ArrayList<Envelop>();
        for(int i = 0; i < size; i++) {
            Envelop envelop = m_arrEnvelop.get(i);

            if(envelop.isEnvelopReaded() == false) {
                envelop.setEnvelopReaded(true);
            }

            if(envelop.isChanged() == true) {
                arrayList.add(envelop);
            }
        }

        requestModifyEnvelopList(false, arrayList);
    }

    private void showEnvelop() {
        ((ArrayAdapter<Envelop>)m_lstEnvelop.getAdapter()).notifyDataSetChanged();
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
        requestEnvelopList(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestEnvelopList(1);
    }

    private void requestEnvelopList(int pageNum) {
        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lstEnvelop.setSelection(0);
            m_arrEnvelop.clear();
        }

        String url = ServerAPIPath.API_GET_MY_ENVELOP_LIST;
        url += "?page_num="+pageNum;

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseEnvelopList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(MyEnvelopeActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseEnvelopList(Object json){

        try {
            JSONObject jsonObject = (JSONObject)json;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            m_nPageItemCnt = jsonObject.getInt("rows_per_page");

            JSONArray dataArray = jsonObject.getJSONArray("list");

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                Envelop freeTalk = new Envelop(dataObject);

                m_arrEnvelop.add(freeTalk);
            }

        } catch (JSONException e) {
            Logger.e(TAG, "responseEnvelopList - JSONException : " + e.getMessage());
            Toast.makeText(MyEnvelopeActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }

        showEnvelop();
    }

    public void requestModifyEnvelopList(final boolean bNeedRefresh, final  ArrayList<Envelop> arrayList) {

        if(arrayList == null || arrayList.size() == 0) {
            return;
        }

        final  ArrayList<Envelop> arrEnvelop = arrayList;
        String url = ServerAPIPath.API_POST_MODIFY_GCM_LIST;
        Map<String, String> params = new HashMap<String, String>();

        try {
            JSONArray array = new JSONArray();

            int size = arrEnvelop.size();

            for(int i = 0 ; i < size; i++) {
                Envelop curEnvelop = arrEnvelop.get(i);
                JSONObject obj = new JSONObject();
                obj.put("gcm_id", String.format("%d", curEnvelop.getId()));

                if(curEnvelop.isEnvelopReaded() == false) {
                    obj.put("gcmlogReaded", "0");
                }
                else {
                    obj.put("gcmlogReaded", "1");
                }

                if(curEnvelop.isDeleted() == false) {
                    obj.put("gcmlogStatus", "0");
                }
                else {
                    obj.put("gcmlogStatus", "-1");
                }

                array.put(obj);
            }

            params.put("gcm_array", array.toString());
        }
        catch (Exception e) {
            Toast.makeText(MyEnvelopeActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        if(bNeedRefresh == true) {
                            resetPage();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyEnvelopeActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }
}

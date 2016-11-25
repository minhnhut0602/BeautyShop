package mimishop.yanji.com.mimishop.activity.shopinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.ShopCommentAdapter;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.Question;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.StarRatingBar;

public class ShowUserAfterActivity extends Activity implements AbsListView.OnScrollListener {

    public static String TAG = "ShowUserAfterActivity";
    private static ShowUserAfterActivity mInstance = null;

    private StarRatingBar m_vRatingBar = null;
    private ListView      m_lvComments = null;
    private EditText      m_etModify = null;
    private ShopCommentAdapter m_adpComment = null;

    private ArrayList<Question> m_lstComments = new ArrayList<Question>();
    private Question m_pMyComment = null;

    private  int m_nShopID;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    public static ShowUserAfterActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_after);
        m_nShopID = getIntent().getIntExtra("shop_id", 0);

        if(m_nShopID == 0) {
            Toast.makeText(this, "상점이 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        mInstance = this;

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_show_user_after, menu);
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

    private void init() {

        initMenu();

        m_vRatingBar = (StarRatingBar)findViewById(R.id.rating_bar);
        Button btnAdd = (Button)findViewById(R.id.btn_select_star);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int level = m_vRatingBar.getRating();
                level++;
                if(level > m_vRatingBar.getMaxRating()) {
                    level = 0;
                }

                m_vRatingBar.setRating(level);
            }
        });

        m_etModify = (EditText)findViewById(R.id.et_modify);
        Utility.showSoftKeyboard(ShowUserAfterActivity.this, m_etModify);
        Button btnModify = (Button)findViewById(R.id.btn_modify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(ShowUserAfterActivity.this);
                requestWriteUserRemark();
            }
        });

        m_lvComments = (ListView)findViewById(R.id.lv_comments);
        m_adpComment = new ShopCommentAdapter(this, R.layout.item_row_shop_user_remark_comment, m_lstComments);
        m_lvComments.setAdapter(m_adpComment);
        m_lvComments.setVisibility(View.GONE);
        m_lvComments.setOnScrollListener(this);

        initKeyboardTouch();

        requestUserRemarkList(1);

        Utility.showSoftKeyboard(this, m_etModify);
    }

    private void initKeyboardTouch() {
        View view = findViewById(R.id.ll_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(ShowUserAfterActivity.this);
            }
        });

        m_lvComments.setSelector(R.drawable.btn_transparent_normal);
        m_lvComments.setItemsCanFocus(false);

        m_lvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utility.hideSoftKeyboard(ShowUserAfterActivity.this);
            }
        });
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);
        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.hideSoftKeyboard(ShowUserAfterActivity.this);
                    finish();
                }
            });
        }

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("이용 후기");
    }


    private void refreshUI() {
        m_adpComment.notifyDataSetChanged();
        if(m_lstComments.size() > 0) {
            m_lvComments.setVisibility(View.VISIBLE);
        }
        else {
            m_lvComments.setVisibility(View.GONE);
        }

        if(m_pMyComment != null) {
            m_vRatingBar.setRating(m_pMyComment.getShopcommentShopLevel());
            m_etModify.setText(m_pMyComment.getContent());
        }
    }

    private void requestWriteUserRemark() {

        String url = ServerAPIPath.API_POST_WRITE_SHOP_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        int shop_id = m_nShopID;
        if(m_pMyComment != null) {
            params.put("comment_id", String.format("%d",m_pMyComment.getId()));
        }

        params.put("shopcommentShopID", String.format("%d", shop_id));
        params.put("shopcommentType", "UR");
        params.put("shopcommentContent", m_etModify.getText().toString());
        params.put("shopcommentShopLevel", String.format("%d", m_vRatingBar.getRating()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(ShowUserAfterActivity.this, ResponseMessage.SUCCESS_REGISTER, Toast.LENGTH_SHORT).show();

                        resetPage();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowUserAfterActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    public void requestRemoveUserRemark(Question question) {

        final  Question shopAnswer= question;

        if(shopAnswer == null) {
            return;
        }

        String url = ServerAPIPath.API_POST_REMOVE_SHOP_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("comment_id", String.format("%d", shopAnswer.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        m_etModify.setText("");
                        m_vRatingBar.setRating(0);
                        m_pMyComment = null;

                        resetPage();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowUserAfterActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    public  void requestionInformUR(Question question) {
        Toast.makeText(this, "미구현입니다.", Toast.LENGTH_SHORT).show();
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
        requestUserRemarkList(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestUserRemarkList(1);
    }


    private void requestUserRemarkList(int pageNum){
        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvComments.setSelection(0);
            m_lstComments.clear();
        }

        String url = ServerAPIPath.API_GET_SHOP_USERREMARK_LIST;
        int shop_id = m_nShopID;
        url+="&shopcommentShopID="+shop_id;
        url+="&page_num="+pageNum;

        Utility.showWaitingDlg(this);

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseUserRemarkList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowUserAfterActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    private void responseUserRemarkList(Object json){

        try {

            JSONObject jsonObject = (JSONObject)json;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
            JSONArray dataArray = jsonObject.getJSONArray("list");

            int shop_id = m_nShopID;
            int user_id = AppController.getInstance().getCurrentUser().getId();

            for(int i = 0; i < dataArray.length(); i++) {
                Question question = new Question(dataArray.getJSONObject(i));

                m_lstComments.add(question);

                if(shop_id == question.getShopcommentShopID() && user_id == question.getShopcommentPostManID()) {
                    m_pMyComment = question;
                }
            }

        } catch (Exception e) {
            Toast.makeText(ShowUserAfterActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseUserRemarkList - JSONException : " + e.getMessage());

        }

        refreshUI();
    }

}

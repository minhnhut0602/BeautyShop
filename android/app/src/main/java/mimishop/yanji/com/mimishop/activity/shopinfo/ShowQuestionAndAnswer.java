package mimishop.yanji.com.mimishop.activity.shopinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
import mimishop.yanji.com.mimishop.adapter.QuestionAnswerAdapter;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Answer;
import mimishop.yanji.com.mimishop.modal.Question;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class ShowQuestionAndAnswer extends Activity implements AbsListView.OnScrollListener {

    public static final String TAG = "ShowQuestionAndAnswer";

    private ExpandableListView  m_lvQuestionAnswer = null;
    private QuestionAnswerAdapter m_adtAdapter = null;
    private ArrayList<Question> m_arrGroupList = new ArrayList<Question>();
    private ArrayList<ArrayList<Answer>> m_arrChildList = new ArrayList<ArrayList<Answer>>();
    private static ShowQuestionAndAnswer m_pInstance = null;
    private EditText m_etQuestion = null;

    private int m_nShopID = 0;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question_and_answer);

        m_pInstance = this;

        m_nShopID = getIntent().getIntExtra("shop_id", 0);

        if(m_nShopID == 0) {
            Toast.makeText(this, "상점이 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_show_question_and_answer, menu);
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

    public static ShowQuestionAndAnswer getInstance() {
        return m_pInstance;
    }

    private void init(){
        initMenu();

        m_lvQuestionAnswer = (ExpandableListView)findViewById(R.id.lv_questionanswer);
        m_adtAdapter = new QuestionAnswerAdapter(this, m_arrGroupList, m_arrChildList,
                            R.layout.item_group_question_answer, R.layout.item_row_question_answer);
        m_lvQuestionAnswer.setAdapter(m_adtAdapter);
        m_lvQuestionAnswer.setOnScrollListener(this);

        // 그룹 클릭 했을 경우 이벤트
        m_lvQuestionAnswer.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                Utility.hideSoftKeyboard(ShowQuestionAndAnswer.this);
                return false;
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        m_lvQuestionAnswer.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Utility.hideSoftKeyboard(ShowQuestionAndAnswer.this);
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
        m_etQuestion = (EditText)findViewById(R.id.et_question);

        Button button = (Button)findViewById(R.id.btn_write_question);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(ShowQuestionAndAnswer.this);
                createQuestion();
            }
        });

        initKeyboardTouch();
        requestQuestionAndAnswer(1);

        Utility.showSoftKeyboard(this, m_etQuestion);
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);
        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.hideSoftKeyboard(ShowQuestionAndAnswer.this);
                    finish();
                }
            });
        }

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_question_answer));
    }

    private void initKeyboardTouch() {
        View view = findViewById(R.id.ll_main);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(ShowQuestionAndAnswer.this);
            }
        });
    }

    public void createQuestion() {
        requestWriteQuestion(null);
    }

    public void modifyQuestion(Question question) {
        requestWriteQuestion(question);
    }

    public void removeQuestion(Question question) {
        requestRemoveQuestion(question);
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
        requestQuestionAndAnswer(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestQuestionAndAnswer(1);
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
        int shop_id = m_nShopID;
        url+="&shopcommentShopID="+shop_id;
        url+="&page_num="+pageNum;

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseQuestionList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowQuestionAndAnswer.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
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

            m_adtAdapter.notifyDataSetChanged();

            if(m_arrGroupList.size() == 0) {
                m_lvQuestionAnswer.setVisibility(View.GONE);
            }
            else {
                m_lvQuestionAnswer.setVisibility(View.VISIBLE);
            }
            requestAnswerList();


            for(int i = 0; i < m_arrGroupList.size(); i++) {
                m_lvQuestionAnswer.expandGroup(i);
            }

        } catch (Exception e) {
            Toast.makeText(ShowQuestionAndAnswer.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseQuestionList - JSONException : " + e.getMessage());
        }
    }

    private void requestAnswerList(){

        String url = ServerAPIPath.API_GET_SHOP_ANSWER_LIST_WITH_SHOP_ID;
        int shop_id = ShopDetailActivity.getInstance().getShop().getId();
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
                        m_adtAdapter.notifyDataSetChanged();
                        Toast.makeText(ShowQuestionAndAnswer.this, s, Toast.LENGTH_SHORT).show();
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

            m_adtAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Logger.e(TAG, "responseAnswerList - JSONException : " + e.getMessage());
            Toast.makeText(ShowQuestionAndAnswer.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }


    private void requestWriteQuestion(final Question question) {
        if(m_etQuestion.getText().toString().equals("") == true) {
            Toast.makeText(this, "질문내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            Utility.showSoftKeyboard(this, m_etQuestion);
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_SHOP_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        int shop_id = ShopDetailActivity.getInstance().getShop().getId();
        if(question != null) {
            params.put("comment_id", String.format("%d",question.getId()));
        }

        params.put("shopcommentShopID", String.format("%d", shop_id));
        params.put("shopcommentType", "QA");
        params.put("shopcommentContent", m_etQuestion.getText().toString());

        Utility.hideSoftKeyboard(this);

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(ShowQuestionAndAnswer.this, ResponseMessage.SUCCESS_REGISTER, Toast.LENGTH_SHORT).show();

                        resetPage();
                        m_etQuestion.setText("");
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowQuestionAndAnswer.this, s, Toast.LENGTH_SHORT).show();
                        m_etQuestion.setText("");
                    }
                },
                params);

    }

    private void requestRemoveQuestion(Question question) {

        Utility.hideSoftKeyboard(this);

        final  Question shopAnswer= question;

        if(shopAnswer == null) {
            return;
        }

        String url = ServerAPIPath.API_POST_REMOVE_SHOP_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("comment_id", String.format("%d",shopAnswer.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        resetPage();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowQuestionAndAnswer.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }
}

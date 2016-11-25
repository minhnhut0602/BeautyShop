package mimishop.yanji.com.mimishop.activity.mypage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.freetalk.WriteCommentActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.FreetalkCommentTAG;
import mimishop.yanji.com.mimishop.modal.FreetalkRelation;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CustomTabButton;

public class ShowMyNewsActivity extends Activity  implements AbsListView.OnScrollListener   {

    public static final String TAG = "ShowMyNewsActivity";
    public  static  final int REQ_WRITE_COMMENT_FREETALK = 1;

    private ListView m_lvNews;
    private CustomTabButton m_btnTab1;
    private CustomTabButton m_btnTab2;
    private CustomTabButton m_btnTab3;

    private int      m_nSelectedTabIdx = 0;

    private ArrayList<FreeTalk> m_arrFreetalk = new ArrayList<FreeTalk>();
    private ArrayList<Comment>  m_arrComment = new ArrayList<Comment>();
    private ArrayList<FreetalkCommentTAG>  m_arrRecentNews = new ArrayList<FreetalkCommentTAG>();

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_news);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_show_my_news, menu);
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
        initTab();

        m_lvNews = (ListView)findViewById(R.id.lv_news);
        m_lvNews.setOnScrollListener(this);

        m_nSelectedTabIdx = 0;

        //request
        selectTab();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText("내 소식 보기");
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_back);

        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void initTab() {
        m_btnTab1 = (CustomTabButton)findViewById(R.id.btn_show_my_writing);
        m_btnTab2 = (CustomTabButton)findViewById(R.id.btn_show_my_comment);
        m_btnTab3 = (CustomTabButton)findViewById(R.id.btn_show_my_recent_news);

        m_btnTab1.setOnClickEvent(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_nSelectedTabIdx = 0;
                selectTab();
            }
        });

        m_btnTab2.setOnClickEvent(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_nSelectedTabIdx = 1;
                selectTab();
            }
        });

        m_btnTab3.setOnClickEvent(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_nSelectedTabIdx = 2;
                selectTab();
            }
        });
    }

    private void selectTab() {
        m_btnTab1.refreshSelect(false);
        m_btnTab2.refreshSelect(false);
        m_btnTab3.refreshSelect(false);

        switch (m_nSelectedTabIdx) {
            case 0:
                m_btnTab1.refreshSelect(true);
            break;
            case 1:
                m_btnTab2.refreshSelect(true);
                break;
            case 2:
                m_btnTab3.refreshSelect(true);
                break;
        }

        resetPage();
    }

    private void refreshUI() {
        switch (m_nSelectedTabIdx) {
            case 0:
                showFreetalk();
               break;
            case 1:
                showFreetalkComment();
                break;
            case 2:
                showRecentInform();
                break;
        }
    }

    private void showFreetalk() {
        m_lvNews.setAdapter(new ArrayAdapter<FreeTalk>(this, R.layout.item_row_my_writing, m_arrFreetalk) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final FreeTalk freeTalk = m_arrFreetalk.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_my_writing, null);
                }

                TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                TextView tvMainCategory = (TextView) convertView.findViewById(R.id.tv_main_category);
                TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);

                String content = freeTalk.getContent() + "[" + freeTalk.getCommentCount() + "]";
                Spannable wordtoSpan = new SpannableString(content);
                wordtoSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.main_background_color)), freeTalk.getContent().length(), content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvContent.setText(wordtoSpan);
                tvMainCategory.setText("자유톡");
                tvCategory.setText(freeTalk.getCategory().getName());

                ImageButton btnButton = (ImageButton) convertView.findViewById(R.id.ib_delete);
                btnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestRemoveFreetalk(freeTalk);
                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startWritingFreetalkComment(freeTalk.getId());
                    }
                });

                return convertView;
            }
        });
    }

    private void showFreetalkComment() {
        m_lvNews.setAdapter(new ArrayAdapter<Comment>(this, R.layout.item_row_my_freetalk_comment,  m_arrComment){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Comment freeTalk = m_arrComment.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate( R.layout.item_row_my_freetalk_comment, null);
                }

                TextView tvContent = (TextView)convertView.findViewById(R.id.tv_content);
                TextView tvMainCategory = (TextView)convertView.findViewById(R.id.tv_main_category);
                TextView tvCategory = (TextView)convertView.findViewById(R.id.tv_category);
                final TextView tvUserName = (TextView)convertView.findViewById(R.id.tv_user_name);
                final TextView tvFreeTalkContent = (TextView)convertView.findViewById(R.id.tv_freetalk_content);

                tvMainCategory.setText("자유톡");
                tvCategory.setText(freeTalk.getCommentCategoryName());
                tvContent.setText(freeTalk.getContent());
                tvUserName.setText("by "+freeTalk.getName());
                tvFreeTalkContent.setText("");

                final View rootView = convertView;
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // Ensure you call it only once :
                        rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        // Here you can get the size :)
                        View view = rootView.findViewById(R.id.ll_content);
                        View view1 = rootView.findViewById(R.id.ll_freetalk);
                        View view2 = rootView.findViewById(R.id.ll_user_name);
                        Rect rect1 = Utility.getFrameForView(view);
                        Rect rect2 = Utility.getFrameForView(view1);
                        Rect rect3 = Utility.getFrameForView(view2);

                        int width = rect1.width() - rect2.width() - rect3.width();

                        if (width < (rect1.width() - rect2.width()) / 2) {
                            width = (rect1.width() - rect2.width()) / 2;
                            tvUserName.setMaxWidth(width);
                        }

                        tvFreeTalkContent.setMaxWidth(width);
                        tvFreeTalkContent.setText(freeTalk.getCommentFreetalkContent());
                    }
                });

                return convertView;
            }
        });

        m_lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startWritingFreetalkComment(m_arrComment.get(i).getCommentFreetalkID());
            }
        });
    }

    private void showRecentInform() {
        m_lvNews.setAdapter(new ArrayAdapter<FreetalkCommentTAG>(this, R.layout.item_row_my_recent_news, m_arrRecentNews) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final FreetalkCommentTAG freeTalk = m_arrRecentNews.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_my_recent_news, null);
                }

                TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                TextView tvFreetalkContent = (TextView) convertView.findViewById(R.id.tv_freetalk_content);
                TextView tvMainCategory = (TextView) convertView.findViewById(R.id.tv_main_category);
                TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
                TextView tvPostTime = (TextView) convertView.findViewById(R.id.tv_post_time);

                String content = "새소식이 들어왔습니다.";


                if(freeTalk.getFreetalCommentTAGUserID() != null) {
                    User user = AppController.getInstance().getCurrentUser();
                    content = freeTalk.getFreetalCommentTAGUserID() + "님이 " + user.getUserID() + "님을 언급했습니다.";

                    Spannable wordtoSpan = new SpannableString(content);

                    wordtoSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.main_background_color)),
                            0, freeTalk.getFreetalCommentTAGUserID().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    int idx = content.indexOf(user.getUserID());

                    wordtoSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.main_background_color)),
                            idx, idx + user.getUserID().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvContent.setText(wordtoSpan);
                }


                tvFreetalkContent.setText(freeTalk.getFreetalkcommentContent());
                tvMainCategory.setText("자유톡");
                tvCategory.setText(freeTalk.getFreetalkCateogryName());
                tvPostTime.setText(Utility.getInstance().getTime(freeTalk.getFreetalkCommentTAGPostTime()));

                return convertView;
            }
        });

        m_lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FreetalkCommentTAG freeTalk = m_arrRecentNews.get(i);
                startWritingFreetalkComment(freeTalk.getFreetalkcommentFreetalkID());
            }
        });
    }

    private void startWritingFreetalkComment(int freetalkID) {
        Intent intent = new Intent(this, WriteCommentActivity.class);
        intent.putExtra("freetalk_id", freetalkID);
        intent.putExtra("hide_keyboard", true);
        startActivityForResult(intent, REQ_WRITE_COMMENT_FREETALK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode == REQ_WRITE_COMMENT_FREETALK && m_nSelectedTabIdx == 1){
                resetPage();
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

        switch (m_nSelectedTabIdx) {
            case 0:
                requestFreeTalkList(pageNum);
                break;
            case 1:
                requestFreeTalkCommentList(pageNum);
                break;
            case 2:
                requestRecentInformList(pageNum);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        switch (m_nSelectedTabIdx) {
            case 0:
                requestFreeTalkList(1);
                break;
            case 1:
                requestFreeTalkCommentList(1);
                break;
            case 2:
                requestRecentInformList(1);
                break;
        }
    }

    private void requestFreeTalkList(int pageNum) {
        String url = ServerAPIPath.API_GET_MY_FREETALK_LIST;

        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvNews.setSelection(0);
            m_arrFreetalk.clear();
        }

        url += "?page_num="+pageNum;

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
                                FreeTalk freeTalk = new FreeTalk(dataObject);

                                m_arrFreetalk.add(freeTalk);
                            }


                        } catch (Exception e) {
                            Logger.e(TAG, "responseFreeTalkCommenList - JSONException : " + e.getMessage());
                            Toast.makeText(ShowMyNewsActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                        refreshUI();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowMyNewsActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    private void requestRemoveFreetalk(final FreeTalk freeTalk) {

        String url = ServerAPIPath.API_POST_REMOVE_FREETALK;
        Map<String, String> params = new HashMap<String, String>();

        params.put("freetalk_id", String.format("%d", freeTalk.getId()));
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
                        Toast.makeText(ShowMyNewsActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestFreeTalkCommentList(int pageNum) {
        String url = ServerAPIPath.API_GET_MY_FREETALK_COMMENT_LIST;

        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvNews.setSelection(0);
            m_arrComment.clear();
        }

        url += "?page_num="+pageNum;

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

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Comment freeTalk = new Comment(dataObject);

                                m_arrComment.add(freeTalk);
                            }

                        } catch (Exception e) {
                            Logger.e(TAG, "responseFreeTalkCommenList - JSONException : " + e.getMessage());
                            Toast.makeText(ShowMyNewsActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                        refreshUI();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowMyNewsActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }


    private void requestRecentInformList(int pageNum) {
        String url = ServerAPIPath.API_GET_RECENT_NEWS_LIST;

        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvNews.setSelection(0);
            m_arrRecentNews.clear();
        }

        url += "?page_num="+pageNum;


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
                                FreetalkCommentTAG freeTalk = new FreetalkCommentTAG(dataObject);

                                m_arrRecentNews.add(freeTalk);
                            }

                        } catch (Exception e) {
                            Logger.e(TAG, "responseFreeTalkCommenList - JSONException : " + e.getMessage());
                            Toast.makeText(ShowMyNewsActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                        refreshUI();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(ShowMyNewsActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }
}

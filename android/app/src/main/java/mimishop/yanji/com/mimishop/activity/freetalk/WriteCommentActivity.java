package mimishop.yanji.com.mimishop.activity.freetalk;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.CommentAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.SelectRemoveDialog;
import mimishop.yanji.com.mimishop.dialog.SelectTagAndDeclareDialog;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;
import mimishop.yanji.com.mimishop.view.ImageArrayLayout;

public class WriteCommentActivity extends Activity  implements AbsListView.OnScrollListener {

    public static final String TAG = "WriteCommentActivity";

    private static WriteCommentActivity mInstance = null;

    private int m_nFreetalkID = 0;
    private FreeTalk m_pFreetalk = null;

    private ImageArrayLayout m_ivFreetalk = null;
    private TextView m_tvFreetalkContent = null;
    private CircledNetworkImageView m_civUserImage = null;
    private TextView m_tvFreetalkUserName = null;
    private TextView m_tvFreetalkTime = null;
    private TextView m_tvFreetalkCategory = null;
    private TextView m_tvFreetalkLikeCnt = null;
    private TextView m_tvFreetalkCommentCnt = null;
    private TextView m_tvFreetalkDeclareCnt = null;

    private EditText m_etComment = null;

    private ListView m_lvFreetalkComment = null;
    private CommentAdapter m_adpComment = null;

    private ArrayList<Comment> m_lstComments = new ArrayList<Comment>();
    private ArrayList<Comment> m_lstTagComment = new ArrayList<Comment>();

    private View m_icFreetalk = null;
    private boolean m_isChanged = false;
    private boolean m_isHideKeyboardWhenFirst = false;
    private boolean m_isAfterWriteComment = false;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    public static WriteCommentActivity getInstance() {
        return  mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        m_nFreetalkID = getIntent().getIntExtra("freetalk_id", 0);
        m_isHideKeyboardWhenFirst = getIntent().getBooleanExtra("hide_keyboard", false);
        if(m_nFreetalkID == 0) {
            finish();
            return;
        }

        mInstance = this;

        initMenu();

        m_icFreetalk = getLayoutInflater().inflate(R.layout.item_row_freetalk, null, false);
        m_icFreetalk.setPadding(0, 0, 0, 20); // space between header and cell.

        initFreetalk();

        m_lvFreetalkComment = (ListView)findViewById(R.id.lv_freetalkcomment);
        m_lvFreetalkComment.addHeaderView(m_icFreetalk);
        View footer = getLayoutInflater().inflate(R.layout.view_write_freetalk_comment_footer, null, false);
        m_lvFreetalkComment.addFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(WriteCommentActivity.this);
            }
        });

        m_adpComment = new CommentAdapter(this, R.layout.item_row_comment, m_lstComments);
        m_lvFreetalkComment.setAdapter(m_adpComment);
        m_lvFreetalkComment.setOnScrollListener(this);

        m_etComment = (EditText)findViewById(R.id.et_content);
        ImageButton btnWriteComment = (ImageButton)findViewById(R.id.ib_write_comment);
        btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(WriteCommentActivity.this);
                requestWritingFreeTalkComment();
            }
        });

        if(m_isHideKeyboardWhenFirst == false) {
            m_etComment.requestFocus();
        }

        initKeyboardTouch();

        requestFreeTalkCommentList(1);
    }

    private void initKeyboardTouch() {
        View view = findViewById(R.id.ll_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(WriteCommentActivity.this);
            }
        });

        final View rootView = findViewById(R.id.root);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(WriteCommentActivity.this);
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    m_lvFreetalkComment.setSelection(1);
                } else {
                    //m_isShowKeyboard = false;
                }
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

                    finishActivity();
                    finish();
                }
            });
        }

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("댓글 달기");
    }

    private void finishActivity() {
        Utility.hideSoftKeyboard(WriteCommentActivity.this);

        if(m_isChanged == true) {
            setResult(RESULT_OK);
            Common.mTempFreetalk = m_pFreetalk;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initFreetalk() {
        m_icFreetalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(WriteCommentActivity.this);
                SelectTagAndDeclareDialog dlg = new SelectTagAndDeclareDialog(WriteCommentActivity.this, m_pFreetalk);
                dlg.setDisableTAG();
                dlg.show();
            }
        });

        m_ivFreetalk = (ImageArrayLayout)m_icFreetalk.findViewById(R.id.iv_image_array);
        m_tvFreetalkContent = (TextView)m_icFreetalk.findViewById(R.id.tv_talk_content);
        m_civUserImage = (CircledNetworkImageView) m_icFreetalk.findViewById(R.id.iv_level);
        m_tvFreetalkUserName = (TextView) m_icFreetalk.findViewById(R.id.tv_nickname);
        m_tvFreetalkTime = (TextView) m_icFreetalk.findViewById(R.id.tv_login_time);
        m_tvFreetalkCategory = (TextView) m_icFreetalk.findViewById(R.id.tv_category);
        m_tvFreetalkLikeCnt = (TextView)m_icFreetalk.findViewById(R.id.tv_like_count);
        m_tvFreetalkCommentCnt = (TextView)m_icFreetalk.findViewById(R.id.tv_comment_count);
        m_tvFreetalkDeclareCnt = (TextView)m_icFreetalk.findViewById(R.id.tv_declare_count);

        m_icFreetalk.findViewById(R.id.btn_give_heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLikeFreeTalk(m_pFreetalk);
            }
        });
        m_icFreetalk.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShare(m_pFreetalk);
            }
        });

        m_ivFreetalk.setVisibility(View.GONE);

        requestGetFreetalk();
    }

    private void showFreetalk() {

        if(m_pFreetalk == null) {
            return;
        }

        m_tvFreetalkContent.setText(m_pFreetalk.getContent());
        m_tvFreetalkUserName.setText(m_pFreetalk.getUserNickName());
        m_tvFreetalkTime.setText(Utility.getInstance().getTime1(m_pFreetalk.getCrtPostTime()));

        if (m_pFreetalk.getLevelImageURL() != null) {
            m_civUserImage.setImageUrl(m_pFreetalk.getLevelImageURL(), AppController.getInstance().getImageLoader());
        }
        else {
            m_civUserImage.setDefaultImageResId(R.drawable.bg_person_default);
        }

        m_tvFreetalkCategory.setText(m_pFreetalk.getCategory().getName());
        m_tvFreetalkLikeCnt.setText(String.format("%d", m_pFreetalk.getLikeCount()));
        m_tvFreetalkCommentCnt.setText(String.format("%d", m_pFreetalk.getCommentCount()));
        m_tvFreetalkDeclareCnt.setText(String.format("%d", m_pFreetalk.getDeclareCount()));

        if (m_pFreetalk.getFreetalkImgArray() != null) {
            m_ivFreetalk.setVisibility(View.VISIBLE);
            m_ivFreetalk.setImageArray(m_pFreetalk.getFreetalkImgArray());
        }
        else {
            m_ivFreetalk.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_write_comment, menu);
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

    public void showDeclareAndTagDialog(Comment comment) {
        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(this);
            return;
        }

        Utility.hideSoftKeyboard(WriteCommentActivity.this);
        SelectTagAndDeclareDialog dlg = new SelectTagAndDeclareDialog(WriteCommentActivity.this, comment);
        dlg.show();
    }

    public void showRemoveDialog(Comment comment) {

        Utility.hideSoftKeyboard(WriteCommentActivity.this);

        SelectRemoveDialog dlg = new SelectRemoveDialog(this, comment);
        dlg.show();
    }


    public void tagComment(final Comment comment) {
        m_lstTagComment.add(comment);

        String content = " @" + comment.getName();

        Spannable wordtoSpan = new SpannableString(content);

        wordtoSpan.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.main_background_color)),
                0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        m_etComment.append(wordtoSpan);
    }

    public  void removeComment(final Comment comment){

        if(comment == null) {
            return;
        }

        String url = ServerAPIPath.API_POST_REMOVE_FREETALK_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("freetalk_comment_id", String.format("%d", comment.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        m_isChanged = true;
                        m_lstComments.remove(comment);
                        m_adpComment.notifyDataSetChanged();
                        requestGetFreetalk();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(WriteCommentActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
                , params
        );
    }



    private void requestGetFreetalk()  {
        String url = ServerAPIPath.API_GET_FREETALK;

        url += "?freetalk_id="+m_nFreetalkID;
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            JSONObject data = (JSONObject) o;

                            m_pFreetalk = new FreeTalk(data);

                            showFreetalk();
                        } catch (Exception e) {
                            Logger.e(TAG, "responseFreeTalkCommentList - JSONException : " + e.getMessage());
                            Toast.makeText(WriteCommentActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(WriteCommentActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
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
        requestFreeTalkCommentList(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestFreeTalkCommentList(1);
    }

    private void requestFreeTalkCommentList(int pageNum)  {
        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvFreetalkComment.setSelection(0);
            m_lstComments.clear();
        }

        String url = ServerAPIPath.API_GET_FREETALK_COMMENT_LIST;

        url = String.format("%s?page_num=%d", url, pageNum);
        url += "&freetalk_id="+m_nFreetalkID;

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            JSONObject jsonObject = (JSONObject) o;
                            int total = jsonObject.getInt("total");
                            int page_num = jsonObject.getInt("page");
                            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
                            JSONArray dataArray = jsonObject.getJSONArray("list");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Comment comment = new Comment(dataObject);

                                m_lstComments.add(comment);
                            }

                            m_adpComment.notifyDataSetChanged();

                            if (m_isAfterWriteComment == true) {
                                m_lvFreetalkComment.setSelection(1);
                                m_isAfterWriteComment = false;
                            }
                        } catch (Exception e) {
                            Logger.e(TAG, "responseFreeTalkCommentList - JSONException : " + e.getMessage());
                            Toast.makeText(WriteCommentActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }


                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(WriteCommentActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    private void requestWritingFreeTalkComment() {

        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(this);
            return;
        }

        if(m_etComment.getText() == null || m_etComment.getText().toString().equals("")) {
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_FREETALKCOMMENT;

        Map<String, String> params = new HashMap<String, String>();
        params.put("freetalk_comment_content", m_etComment.getText().toString());
        params.put("freetalk_id", String.format("%d", m_pFreetalk.getId()));

        ArrayList<Comment> tagList = new ArrayList<Comment>();
        try {
            //freetalk_comment_red
            //freetalk_comment_tag_list
            Editable bodyText = (Editable) m_etComment.getEditableText();
            ForegroundColorSpan[] styleSpans = bodyText.getSpans(0, bodyText.length(), ForegroundColorSpan.class);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < styleSpans.length; i++) {
                ForegroundColorSpan style = styleSpans[i];
                int start = bodyText.getSpanStart(style);
                int end = bodyText.getSpanEnd(style);

                JSONObject object = new JSONObject();
                object.put("start", start);
                object.put("end", end);

                int size = m_lstTagComment.size();
                for(int j = 0; j < size; j++){
                    String content = " @" + m_lstTagComment.get(j).getName();
                    String styleContent = m_etComment.getText().toString().substring(start, end);
                    if(content.compareTo(styleContent) == 0) {
                        tagList.add(m_lstTagComment.get(j));
                        break;
                    }
                }

                jsonArray.put(object);
            }

            if(jsonArray.length() > 0) {
                params.put("freetalk_comment_red", jsonArray.toString());
            }

            int size = tagList.size();
            jsonArray = new JSONArray();
            for(int i = 0; i < size; i++) {
                Comment comment = tagList.get(i);
                jsonArray.put(comment.getUserID());
            }

            if(jsonArray.length() > 0) {
                params.put("freetalk_comment_tag_list", jsonArray.toString());
            }
        }
        catch (JSONException e) {

        }
        m_lstTagComment.clear();

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object s) {

                        m_isAfterWriteComment = true;

                        resetPage();
                        requestGetFreetalk();
                        m_isChanged = true;
                        m_etComment.setText("");

                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String volleyError) {
                        m_etComment.setText("");
                        Toast.makeText(WriteCommentActivity.this, ResponseMessage.ERR_NO_NETWORK, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
                , params);
    }

    public void requestLikeFreetalkComment(final Comment freeTalk){
        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(this);
            return;
        }

        m_lvFreetalkComment.clearFocus();

        String url = ServerAPIPath.API_POST_LIKE_FREETALK_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("freetalk_comment_id", String.format("%d", freeTalk.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            try {

                                JSONObject object = (JSONObject)o;
                                int cnt = object.getInt("cnt");
                                boolean duplicate = object.getBoolean("isDuplicate");
                                if(cnt != freeTalk.getCommentHeartCnt()) {
                                    freeTalk.setCommentHeartCnt(cnt);
                                }

                                if(duplicate == true) {
                                    Toast.makeText(WriteCommentActivity.this, ResponseMessage.SUCCESS_DUPLICATE_LIKE, Toast.LENGTH_SHORT).show();
                                }

                                m_adpComment.notifyDataSetChanged();
                            }
                            catch (Exception e) {
                                Toast.makeText(WriteCommentActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                            }

                            m_isChanged = true;
                        }
                        catch (Exception e) {
                            Toast.makeText(WriteCommentActivity.this, ResponseMessage.ERR_LIKE_DISABLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(WriteCommentActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestLikeFreeTalk(final FreeTalk freeTalk){

        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(this);
            return;
        }

        String url = ServerAPIPath.API_POST_LIKE_FREETALK;

        Map<String, String> params = new HashMap<String, String>();

        params.put("freetalk_id", String.format("%d", freeTalk.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            JSONObject object = (JSONObject)o;
                            int cnt = object.getInt("cnt");
                            boolean duplicate = object.getBoolean("isDuplicate");
                            if(cnt != freeTalk.getLikeCount()) {
                                freeTalk.setLikeCount(cnt);
                            }

                            if(duplicate == true) {
                                Toast.makeText(WriteCommentActivity.this, ResponseMessage.SUCCESS_DUPLICATE_LIKE, Toast.LENGTH_SHORT).show();
                            }

                            m_adpComment.notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            Toast.makeText(WriteCommentActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(WriteCommentActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }, params
        );
    }

    public void doShare(FreeTalk freeTalk) {
        Utility utility = Utility.getInstance();

        utility.doShare(this, m_icFreetalk);
    }
}

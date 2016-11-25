package mimishop.yanji.com.mimishop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import mimishop.yanji.com.mimishop.activity.cast.CastDetailActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.CastComment;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;

/**
 * Created by KimCholJu on 5/17/2015.
 */
public class WriteCastCommentDialog extends Dialog implements DialogInterface.OnKeyListener,AbsListView.OnScrollListener {

    public static final String TAG = "WriteCastCommentDialog";

    private Context mContext;
    private int mCastIdx;
    private CastComment m_pCastComment;

    private  Button m_btnSearch1; //인기순
    private  Button m_btnSearch2; //최신수

    private EditText m_etText;
    private Button m_btnWriteComment;

    private ListView m_lvComment;
    private ArrayList<CastComment> m_arrCastComment = new ArrayList<CastComment>();

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;
    private int m_nPreOrderType = 0;

    boolean bFirst = true;

    public WriteCastCommentDialog(Context context, int cast_idx) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        mCastIdx = cast_idx;
    }

    public void setCastComment(CastComment comment) {
        m_pCastComment = comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_write_cast_comment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Button btnClose = (Button)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dismissDlg();
            }
        });

        m_btnSearch1 = (Button)findViewById(R.id.btn_search_1);
        m_btnSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_btnSearch1.setSelected(true);
                m_btnSearch2.setSelected(false);
                requestGetCastCommentList(1, 1);
            }
        });

        m_btnSearch2 = (Button)findViewById(R.id.btn_search_2);
        m_btnSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_btnSearch1.setSelected(false);
                m_btnSearch2.setSelected(true);
                requestGetCastCommentList(1, 2);
            }
        });


        m_etText = (EditText)findViewById(R.id.et_comment);
        if(m_pCastComment != null) {
            m_etText.setText(m_pCastComment.getContent());
        }
//
//        m_etText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(bFirst) {bFirst=false;return;}
//                if (AppController.getInstance().isUserId == true) {
//                    Utility.hideSoftKeyboard((Activity)mContext);
//                    LoginDialog dlg = new LoginDialog(mContext);
//                    dlg.show();
//                }
//            }
//        });
        m_etText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().isUserId == true) {
                    Utility.hideSoftKeyboard((Activity)mContext);
                    LoginDialog dlg = new LoginDialog(mContext);
                    dlg.show();
                }
            }
        });

        m_btnWriteComment = (Button)findViewById(R.id.btn_write_comment);
        m_btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                requestWriteCastComment();
            }
        });

        initCommentList();

        m_btnSearch1.setSelected(true);
        m_lvComment.setOnScrollListener(this);

        requestGetCastCommentList(1, 1);

        initKeyboardTouch();
        this.setOnKeyListener(this);
    }

    private void dismissDlg() {
        if(mContext.getClass() == CastDetailActivity.class) {
            CastDetailActivity.getInstance().getCast().setCastCommentCnt(m_arrCastComment.size());
            CastDetailActivity.getInstance().refreshUI();
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismissDlg();
        }

        return false;
    }


    private void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (focus != null) {
            im.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    private void initKeyboardTouch() {
        m_lvComment.setSelector(R.drawable.btn_transparent_normal);
        m_lvComment.setItemsCanFocus(false);

        m_lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
            }
        });

        View view = findViewById(R.id.rl_top);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }

    private void initCommentList() {
        m_lvComment = (ListView)findViewById(R.id.lv_cast_comment);
        m_lvComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
        m_lvComment.setAdapter(new ArrayAdapter<CastComment>(getContext(), R.layout.item_row_cast_comment, m_arrCastComment){

            class ViewHolder {
                private CircledNetworkImageView civ_user;
                private TextView tv_comment;
                private TextView tv_user_name;
                private TextView tv_time;
                private TextView tv_like_count;
                private Button   btn_like;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final CastComment best = getItem(position);
                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_cast_comment, null);

                    holder = new ViewHolder();
                    holder.civ_user = (CircledNetworkImageView) convertView.findViewById(R.id.civ_user);
                    holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                    holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_date);
                    holder.tv_like_count = (TextView) convertView.findViewById(R.id.tv_like_count);
                    holder.btn_like = (Button)convertView.findViewById(R.id.btn_like);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if(best.getUserImage() != null) {
                    holder.civ_user.setImageUrl(best.getUserImage().getImgUrl(), AppController.getInstance().getImageLoader());
                }
                else {
                    holder.civ_user.setDefaultImageResId(R.drawable.bg_profile_default_img);
                }

                holder.tv_user_name.setText(best.getName());
                holder.tv_comment.setText(best.getContent());
                holder.tv_time.setText(Utility.getInstance().getTime1(best.getTime()));
                holder.tv_like_count.setText(String.format("%d",best.getCastcommentHeartCnt()));

                holder.btn_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestLikeCastComment(best);
                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard();
                    }
                });

                return convertView;
            }
        });

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
        requestGetCastCommentList(pageNum, m_nPreOrderType);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestGetCastCommentList(1, 2);
    }

    private void requestGetCastCommentList(int pageNum, int type) {

        if (m_nPrePageNum == pageNum && m_nPreOrderType == type) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvComment.setSelection(0);
            m_arrCastComment.clear();
        }

        String url = ServerAPIPath.API_GET_CAST_COMMENT_LIST;

        url += "?page_num="+pageNum;
        url += "&type="+type+"&cast_id="+mCastIdx;

        Utility.showWaitingDlg((Activity)mContext);

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseGetCastCommentList(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    private void responseGetCastCommentList(Object json) {
        try {
            JSONObject jsonObject = (JSONObject)json;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            m_nPageItemCnt = jsonObject.getInt("rows_per_page");
            JSONArray dataArray = jsonObject.getJSONArray("list");

            int cnt = dataArray.length();
            for(int i = 0; i < cnt; i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                CastComment advertise = new CastComment(dataObject);

                m_arrCastComment.add(advertise);
            }

            ((ArrayAdapter<CastComment>)m_lvComment.getAdapter()).notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(getContext(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseGetCastCommentList - JSONException : " + e.getMessage());
        }
    }

    private void requestWriteCastComment() {
        if(m_etText.getText() == null || m_etText.getText().toString().equals("")) {
            Toast.makeText(getContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_CAST_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        if(m_pCastComment != null ) {
            params.put("cast_id", String.format("%d", m_pCastComment.getId()));
        }

        params.put("castcommentCastID", String.format("%d", mCastIdx));
        params.put("castcommentContent", m_etText.getText().toString());

        m_etText.setText("");

        Utility.showWaitingDlg((Activity)mContext);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        m_btnSearch1.setSelected(false);
                        m_btnSearch2.setSelected(true);
                        resetPage();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }, params
        );
    }

    public void requestLikeCastComment(final CastComment comment) {
        String url = ServerAPIPath.API_POST_LIKE_CAST_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("cast_comment_id", String.format("%d", comment.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            JSONObject object = (JSONObject)o;
                            int cnt = object.getInt("cnt");
                            boolean duplicate = object.getBoolean("isDuplicate");
                            if(cnt != comment.getCastcommentHeartCnt()) {
                                comment.setCastcommentHeartCnt(cnt);
                            }

                            if(duplicate == true) {
                                Toast.makeText(mContext, ResponseMessage.SUCCESS_DUPLICATE_LIKE, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(getContext(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                        ((ArrayAdapter<CastComment>) m_lvComment.getAdapter()).notifyDataSetChanged();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                }
                ,params
        );
    }
}

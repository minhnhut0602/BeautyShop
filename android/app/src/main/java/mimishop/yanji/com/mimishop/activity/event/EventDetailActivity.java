package mimishop.yanji.com.mimishop.activity.event;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
//import com.facebook.android.Util;
import com.google.android.gms.maps.model.Circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.LoginDialog;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Banner;
import mimishop.yanji.com.mimishop.modal.BannerComment;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;

public class EventDetailActivity extends Activity implements AbsListView.OnScrollListener {

    public static final String TAG = "EventDetailActivity";

    Banner m_pBanner = null;

    ListView m_lvComment = null;
    NetworkImageView m_nivBanner = null;
    View  m_llDate = null;
    View  m_llContent = null;
    View  m_llTitle = null;
    TextView m_tvBannerName = null;
    TextView m_tvBannerDate = null;
    TextView m_tvBannerContent = null;
    EditText m_etComment = null;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;

    ArrayList<BannerComment> m_arrBannerComment = new ArrayList<>();
    ArrayAdapter<BannerComment> m_adpComment = null;
    private View m_vHeader = null;

    boolean m_bNeedScrollFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        m_pBanner = (Banner)getIntent().getSerializableExtra("banner");

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_event_detail, menu);
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

        m_lvComment = (ListView)findViewById(R.id.lv_comment);

        View header = getLayoutInflater().inflate(R.layout.view_banner_comment_header, null, false);
        m_nivBanner = (NetworkImageView)header.findViewById(R.id.iv_banner);
        m_tvBannerName = (TextView)header.findViewById(R.id.tv_event_title);
        m_tvBannerContent = (TextView)header.findViewById(R.id.tv_event_content);
        m_tvBannerDate = (TextView)header.findViewById(R.id.tv_event_date);
        m_llDate = header.findViewById(R.id.ll_date);
        m_llTitle = header.findViewById(R.id.ll_title);
        m_llContent = header.findViewById(R.id.ll_content);
        m_vHeader = header;

        m_lvComment.addHeaderView(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(EventDetailActivity.this);
            }
        });
        m_adpComment = new ArrayAdapter<BannerComment>(this, R.layout.item_row_banner_comment, m_arrBannerComment) {
            class ViewHolder {
                private CircledNetworkImageView civ_user;
                private TextView tv_user_name;
                private TextView tv_comment;
                private TextView tv_date;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final BannerComment comment = m_arrBannerComment.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_banner_comment, null);
                    holder = new ViewHolder();
                    holder.civ_user = (CircledNetworkImageView) convertView.findViewById(R.id.civ_user);
                    holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                    holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                    holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if (comment.m_strPostManPhotoURL != null) {
                    holder.civ_user.setImageUrl(comment.m_strPostManPhotoURL, AppController.getInstance().getImageLoader());
                } else {
                    holder.civ_user.setImageUrl(null, AppController.getInstance().getImageLoader());
                    holder.civ_user.setDefaultImageResId(R.drawable.bg_person_default);
                }
                holder.tv_user_name.setText(comment.m_strPostManID);
                holder.tv_comment.setText(comment.m_strContent);
                holder.tv_date.setText(Utility.getInstance().getTime1(comment.m_lPostTime));

                return convertView;
            }
        };
        m_lvComment.setAdapter(m_adpComment);
        m_lvComment.setOnScrollListener(this);
        m_lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utility.hideSoftKeyboard(EventDetailActivity.this);
            }
        });

        m_etComment = (EditText)findViewById(R.id.et_content);

        m_etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (AppController.getInstance().isUserId == true) {
                    Utility.hideSoftKeyboard(EventDetailActivity.this);
                    LoginDialog dlg = new LoginDialog(EventDetailActivity.this);
                    dlg.show();
                }
            }
        });
        m_etComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().isUserId == true) {
                    Utility.hideSoftKeyboard(EventDetailActivity.this);
                    LoginDialog dlg = new LoginDialog(EventDetailActivity.this);
                    dlg.show();
                }
            }
        });

        ImageButton btnWriteComment = (ImageButton)findViewById(R.id.ib_write_comment);
        btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(EventDetailActivity.this);
                requestWritingBannerComment();
            }
        });

        initKeyboardTouch();

        showBanner();

        requestGetCommentList(1);
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_event_detail));

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

    private void initKeyboardTouch() {
        m_vHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(EventDetailActivity.this);
            }
        });

        final View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(EventDetailActivity.this);
            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = view.getRootView().getHeight() - view.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    m_lvComment.setSelection(1);
                } else {
                    //m_isShowKeyboard = false;
                }
            }
        });

    }

    private void showBanner() {
        if(m_pBanner == null) {
            return;
        }

        if(m_pBanner.getBannerImg() != null) {
            m_nivBanner.setVisibility(View.VISIBLE);
            m_nivBanner.setImageUrl(m_pBanner.getBannerImg(), AppController.getInstance().getImageLoader());
        }
        else {
            m_nivBanner.setVisibility(View.GONE);
        }

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        String imagUrl = m_pBanner.getBannerImg();
        ImageLoader.ImageContainer newContainer = imageLoader.get(imagUrl, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                // If this was an immediate response that was delivered inside of a layout
                // pass do not set the image immediately as it will trigger a requestLayout
                // inside of a layout. Instead, defer setting the image by posting back to
                // the main thread.
                if (isImmediate) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onResponse(response, false);
                        }
                    });
                    return;
                }

                if (response.getBitmap() != null) {
                    Bitmap bitmap = response.getBitmap();
                    final int bmpWidth = bitmap.getWidth();
                    final int bmpHeight = bitmap.getHeight();

                    final ImageView imageView = m_nivBanner;


                    ViewTreeObserver vto = imageView.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            int width = imageView.getMeasuredWidth();
                            int height = imageView.getMeasuredHeight();
                            float ratio = (float) bmpHeight / bmpWidth;
                            width = View.MeasureSpec.getSize(width);
                            height = (int) (width * ratio);

                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
                            params.height = height;
                            imageView.setLayoutParams(params);
                        }
                    });
                }
            }
        });

        if(m_pBanner.isShowDate() == true) {
            m_llDate.setVisibility(View.VISIBLE);
            String strStartDate = Utility.getInstance().getDateString(m_pBanner.getBannerStartTime(), "yyyy-MM-dd HH:mm:SS");
            String strEndDate = Utility.getInstance().getDateString(m_pBanner.getBannerEndTime(), "yyyy-MM-dd HH:mm:SS");

            m_tvBannerDate.setText(String.format("%s ~ %s", strStartDate, strEndDate));
        }
        else  {
            m_llDate.setVisibility(View.GONE);
        }

        if(m_pBanner.isShowTitle() == true) {
            m_llTitle.setVisibility(View.VISIBLE);
            m_tvBannerName.setText(m_pBanner.getBannerTitle());
        }
        else  {
            m_llTitle.setVisibility(View.GONE);
        }

        if(m_pBanner.isShowContent() == true) {
            m_llContent.setVisibility(View.VISIBLE);
            m_tvBannerContent.setText(m_pBanner.getBannerContent());
        }
        else  {
            m_llContent.setVisibility(View.GONE);
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
        requestGetCommentList(pageNum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void resetPage() {
        m_nPrePageNum = -1;
        requestGetCommentList(1);
    }

    private  void requestGetCommentList(int pageNum) {
        if (m_nPrePageNum == pageNum) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvComment.setSelection(0);
            m_arrBannerComment.clear();
        }

        String url = ServerAPIPath.API_GET_BANNER_COMMENT_LIST;

        url = String.format("%s?page_num=%d", url, pageNum);
        url = String.format("%s&banner_id=%d", url, m_pBanner.getId());

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
                                BannerComment comment = new BannerComment(dataObject);

                                m_arrBannerComment.add(comment);
                            }

                            m_adpComment.notifyDataSetChanged();

                            if(page_num == 1 && m_bNeedScrollFirst == true) {
                                m_lvComment.setSelection(1);
                                m_bNeedScrollFirst = false;
                            }

                        } catch (Exception e) {
                            Logger.e(TAG, "requestGetCommentList - JSONException : " + e.getMessage());
                            Toast.makeText(EventDetailActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(EventDetailActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void requestWritingBannerComment() {
        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(this);
            return;
        }

        if(m_etComment.getText().length() == 0) {
            Toast.makeText(EventDetailActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_BANNER_COMMENT;

        Map<String, String> params = new HashMap<>();
        params.put("banner_id", String.format("%d", m_pBanner.getId()));
        params.put("content", m_etComment.getText().toString());

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {

                        Utility.hideWaitingDlg();

                        try {
                            JSONObject jsonObject = (JSONObject)s;
                            int id = jsonObject.getInt("id");
                            if(id > 0) {
                                m_bNeedScrollFirst = true;
                                resetPage();
                            }
                        }
                        catch (JSONException e) {

                        }
                        m_etComment.setText("");
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String volleyError) {
                        m_etComment.setText("");
                        Toast.makeText(EventDetailActivity.this, ResponseMessage.ERR_NO_NETWORK, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
                , params);
    }

}

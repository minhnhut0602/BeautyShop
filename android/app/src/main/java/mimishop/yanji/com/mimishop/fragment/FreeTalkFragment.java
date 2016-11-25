package mimishop.yanji.com.mimishop.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devsmart.android.ui.HorizontalListView;
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
import mimishop.yanji.com.mimishop.activity.freetalk.WriteCommentActivity;
import mimishop.yanji.com.mimishop.activity.freetalk.WritingActivity;
import mimishop.yanji.com.mimishop.adapter.FreeTalkAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.CastComment;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.Location;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.ComboBox;

/**
 * Created by KCJ on 3/23/2015.
 */
public class FreeTalkFragment extends Fragment implements AdapterView.OnItemClickListener , AbsListView.OnScrollListener{

    public static final String TAG = "FreeTalkFragment";

    public static final int REQ_WRITE_FREETALK = 100;
    public static final int REQ_WRITE_COMMENT_FREETALK = 101;

    private ListView m_lvFreeTalk = null;
    private ComboBox m_spCategory = null;
    private EditText m_etSearchWord = null;

    private FreeTalkAdapter m_adpFreeTalk = null;
    private ArrayList<FreeTalk> m_lstFreeTalk = new ArrayList<FreeTalk>();
    private ArrayList<Category> m_lstCategory = new ArrayList<Category>();

    private View vgMain = null;
    private View m_vMain = null;

    private String mSearchWord = "";
    private boolean mIsFirstLoading = true;

    private int m_nPageItemCnt = 10;
    private int m_nPreLastNum = 0;
    private int m_nPrePageNum = -1;


    private FreeTalk m_pSelectedFreetalk = null;


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
        requestFreeTalkList(pageNum, false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_freetalk, container, false);

        vgMain = view;

        if(savedInstanceState != null) {
            mSearchWord = savedInstanceState.getString("searchWord");
        }

        m_vMain = view.findViewById(R.id.content);

        m_adpFreeTalk = new FreeTalkAdapter(getActivity(), R.layout.item_row_freetalk, m_lstFreeTalk);
        m_lvFreeTalk = (ListView) view.findViewById(R.id.lv_freetalk);
        m_lvFreeTalk.setAdapter(m_adpFreeTalk);
        m_lvFreeTalk.setSelector(R.drawable.btn_transparent_normal);
        m_lvFreeTalk.setItemsCanFocus(false);
        m_lvFreeTalk.setOnItemClickListener(this);
        m_lvFreeTalk.setOnScrollListener(this);

        m_nPreLastNum = 0;
        m_nPrePageNum = -1;

        m_spCategory = (ComboBox)view.findViewById(R.id.sp_category);

        //set the ArrayAdapter to the spinner
        m_spCategory.setAdapter(new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, m_lstCategory) {

            class ViewHolder {
                TextView tv_text;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

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

        m_spCategory.setOnItemClickListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                         Utility.hideSoftKeyboard(getActivity());

                        if(mIsFirstLoading == false) {
                            requestFreeTalkList(1, true);
                        }
                        else {
                            mIsFirstLoading = false;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
        m_spCategory.setSelectedIndex(0);

        m_etSearchWord = (EditText)view.findViewById(R.id.et_search_text);
        m_etSearchWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Utility.hideSoftKeyboard(getActivity());
                        requestFreeTalkList(1, true);
                        break;
                }
                return true;
            }
        });

        if(mSearchWord != null) {
            m_etSearchWord.setText(mSearchWord);
        }

        Button button = (Button)view.findViewById(R.id.btn_write_freetalk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWritingActivity();
            }
        });

        init();

        initMenu(view);
        initKeyboardTouch(view);
        return view;
    }

    private void initMenu(View mainView) {
        View view = mainView.findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("자유톡");
    }

    private void init(){
        requestFreeTalkList(1, true);
        requestCategoryList();
    }

    private void initKeyboardTouch(View mainView) {
        View root = mainView.findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(getActivity());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Utility.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode == REQ_WRITE_FREETALK) {
                requestFreeTalkList(1, true);
            }
            else {
                FreeTalk currentFreetalk = m_pSelectedFreetalk;
                if(currentFreetalk != null) {
                    currentFreetalk.setCommentCount(Common.mTempFreetalk.getCommentCount());
                }

                m_adpFreeTalk.notifyDataSetChanged();
            }
        }
    }

    public void doLikeFreeTalk(FreeTalk freeTalk) {
        requestLikeFreeTalk(freeTalk);
    }

    public  void startWritingCommentActivity(FreeTalk freeTalk, boolean hideKeyboard) {
        Intent intent = new Intent(getActivity(), WriteCommentActivity.class);

        intent.putExtra("freetalk_id", freeTalk.getId());
        intent.putExtra("hide_keyboard", hideKeyboard);
        m_pSelectedFreetalk = freeTalk;

        startActivityForResult(intent, REQ_WRITE_COMMENT_FREETALK);
    }

    public void startWritingActivity() {

        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(getActivity());
            return;
        }

        Intent intent = new Intent(getActivity(), WritingActivity.class);

        startActivityForResult(intent, REQ_WRITE_FREETALK);
    }

    public void doShare(FreeTalk freeTalk) {
        Utility utility = Utility.getInstance();

        utility.doShare(getActivity(), vgMain);
    }

    private void requestFreeTalkList(int pageNum, boolean isChangeParams) {

        if (m_nPrePageNum == pageNum && isChangeParams == false) {
            return;
        }

        m_nPrePageNum = pageNum;

        if(pageNum == 1) {
            m_lvFreeTalk.setSelection(0);
            m_lstFreeTalk.clear();
        }

        String url = ServerAPIPath.API_GET_FREETALK_LIST;
        boolean isChange = false;
        if(m_spCategory.getSelectedIndex() != 0) {
            url += "?category_id=" + m_lstCategory.get(m_spCategory.getSelectedIndex()).getId();
            isChange = true;
        }

        if(m_etSearchWord.getText().equals("") == false) {

            String utf8Location = m_etSearchWord.getText().toString();

            try {
                utf8Location = URLEncoder.encode(utf8Location, "UTF-8");
            }
            catch (Exception e){

            }
            if(m_spCategory.getSelectedIndex() != 0) {
                url += "&search_word="+utf8Location;
            }
            else {
                url += "?search_word="+utf8Location;
            }

            isChange = true;
        }

        if(pageNum >= 1) {
            if(isChange == false) {
                url += "?page_num="+pageNum;
            }
            else {
                url += "&page_num="+pageNum;
            }
        }

        Utility.showWaitingDlg(getActivity());
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseFreeTalkList(o);
                        m_vMain.setVisibility(View.VISIBLE);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Utility.hideWaitingDlg();
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void responseFreeTalkList(Object json){

        try {

            JSONObject jsonObject = (JSONObject)json;

            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");

            m_nPageItemCnt = jsonObject.getInt("rows_per_page");

            JSONArray dataArray = jsonObject.getJSONArray("list");

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                FreeTalk freeTalk = new FreeTalk(dataObject);

                m_lstFreeTalk.add(freeTalk);
            }

            m_adpFreeTalk.notifyDataSetChanged();

        } catch (Exception e) {
            Logger.e(TAG, "responsePremierShopList - JSONException : " + e.getMessage());
            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCategoryList(){

        String url = ServerAPIPath.API_GET_CATEGORY_LIST;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseCategoryList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void responseCategoryList(Object json){

        try {

            m_lstCategory.clear();

            JSONArray dataArray = (JSONArray)json;

            Category category = new Category();
            category.setName("전체");
            category.setId(0);
            m_lstCategory.add(category);

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                category = new Category(jsonObject);

                if(category.isTalkCategory() == true) {
                    m_lstCategory.add(category);
                }
            }

            SpinnerAdapter adapter = m_spCategory.getAdapter();

            ((ArrayAdapter<Category>)adapter).notifyDataSetChanged();

        } catch (Exception e) {
            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLikeFreeTalk(final FreeTalk freeTalk){

        User user = AppController.getInstance().getCurrentUser();
        if(user.isLimitUser() == true) {
            AppController.showLimitUserDlg(getActivity());
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
                                Toast.makeText(getActivity(), ResponseMessage.SUCCESS_DUPLICATE_LIKE, Toast.LENGTH_SHORT).show();
                            }

                            m_adpFreeTalk.notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                }, params
        );
    }
}

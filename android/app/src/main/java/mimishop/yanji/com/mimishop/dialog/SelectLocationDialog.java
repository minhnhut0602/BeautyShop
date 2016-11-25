package mimishop.yanji.com.mimishop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
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
import mimishop.yanji.com.mimishop.activity.LoginActivity;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.modal.Location;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 5/5/2015.
 */
public class SelectLocationDialog extends Dialog implements View.OnClickListener {

    public static final String TAG = "SelectLocationDialog";

    private int mExtraCount = 2;

    private Context mContext;

    private ListView m_lvLocation1;
    private ListView m_lvLocation2;

    private String []m_arrLocation1 = {"서울",	"부산",	"대구",	"인천",	"광주",	"대전",	"울산",	"경기도",	"강원도",
                                       "충청북도",	"충청남도",	"전라북도",	"전라남도",	"경상북도",	"경상남도",	"제주도",	"세종시"};

    private String [][]m_arrLocation2 = {
            {    "강남구", //서울
                    "강동구",
                    "강북구",
                    "강서구",
                    "관악구",
                    "광진구",
                    "구로구",
                    "금천구",
                    "노원구",
                    "도봉구",
                    "동대문구",
                    "동작구",
                    "마포구",
                    "서대문구",
                    "서초구",
                    "성동구",
                    "성북구",
                    "송파구",
                    "양천구",
                    "영등포구",
                    "용산구",
                    "은평구",
                    "종로구",
                    "중구",
                    "중랑구"
            },
            {
                    "강서구",  // 부산
                    "금정구",
                    "기장군",
                    "남구",
                    "동구",
                    "동래구",
                    "부산진구",
                    "북구",
                    "사상구",
                    "사하구",
                    "서구",
                    "수영구",
                    "연제구",
                    "영도구",
                    "중구",
                    "해운대구"
            },
            {
                    "남구",       // 대구
                    "달서구",
                    "달성군",
                    "동구",
                    "북구",
                    "서구",
                    "수성구",
                    "중구"
            },
            {
                    "강화군",      // 인천
                    "계양구",
                    "남구",
                    "남동구",
                    "동구",
                    "부평구",
                    "서구",
                    "연수구",
                    "옹진군",
                    "중구"
            },
            {
                    "광산구",    // 광주
                    "남구",
                    "동구",
                    "북구",
                    "서구"

            },
            {
                   "대덕구",      // 대전
                    "동구",
                    "서구",
                    "유성구",
                    "중구"
            },
            {
                    "남구",           //울산
                    "동구",
                    "북구",
                    "울주군",
                    "중구"

            },
            {
                    "수원시",           //경기도
                    "성남시",
                    "의정부시",
                    "안양시",
                    "부천시",
                    "광명시",
                    "평택시",
                    "동두천시",
                    "안산시",
                    "고양시",
                    "과천시",
                    "구리시",
                    "남양주시",
                    "오산시",
                    "시흥시",
                    "군포시",
                    "의왕시",
                    "하남시",
                    "용인시",
                    "파주시",
                    "이천시",
                    "안성시",
                    "김포시",
                    "화성시",
                    "광주시",
                    "양주시",
                    "포천시",
                    "여주시",
                    "연천군",
                    "가평군",
                    "양평군"
            },
            {
                    "원주시",            //강원도
                    "춘천시",
                    "강릉시",
                    "동해시",
                    "속초시",
                    "삼척시",
                    "태백시",
                    "홍천군",
                    "철원군",
                    "횡성군",
                    "평창군",
                    "정선군",
                    "영월군",
                    "인제군",
                    "고성군",
                    "양양군",
                    "화천군",
                    "양구군"
            },
            {
                    "청주시",              // 충청북도
                    "충주시",
                    "제천시",
                    "보은군",
                    "옥천군",
                    "영동군",
                    "증평군",
                    "진천군",
                    "괴산군",
                    "음성군",
                    "단양군"
            },
            {
                    "천안시",              //충청남도
                    "공주시",
                    "보령시",
                    "아산시",
                    "서산시",
                    "논산시",
                    "계룡시",
                    "당진시",
                    "금산군",
                    "부여군",
                    "서천군",
                    "청양군",
                    "홍성군",
                    "예산군",
                    "태안군"
            },
            {
                    "전주시",              //전라북도
                    "익산시",
                    "군산시",
                    "정읍시",
                    "김제시",
                    "남원시",
                    "완주군",
                    "고창군",
                    "부안군",
                    "임실군",
                    "순창군",
                    "진안군",
                    "무주군",
                    "장수군"
            },
            {
                    "목포시",              //전라남도
                    "여수시",
                    "순천시",
                    "나주시",
                    "광양시",
                    "담양군",
                    "곡성군",
                    "구례군",
                    "고흥군",
                    "보성군",
                    "화순군",
                    "장흥군",
                    "강진군",
                    "해남군",
                    "영암군",
                    "무안군",
                    "함평군",
                    "영광군",
                    "장성군",
                    "완도군",
                    "진도군",
                    "신안군"
            },
            {
                    "포항시",        //경상북도
                    "경주시",
                    "김천시",
                    "안동시",
                    "구미시",
                    "영주시",
                    "영천시",
                    "상주시",
                    "문경시",
                    "경산시",
                    "군위군",
                    "의성군",
                    "청송군",
                    "영양군",
                    "영덕군",
                    "청도군",
                    "고령군",
                    "성주군",
                    "칠곡군",
                    "예천군",
                    "봉화군",
                    "울진군",
                    "울릉군"

            },
            {
                    "창원시",       //경상남도
                    "김해시",
                    "진주시",
                    "양산시",
                    "거제시",
                    "통영시",
                    "사천시",
                    "밀양시",
                    "함안군",
                    "거창군",
                    "창녕군",
                    "고성군",
                    "하동군",
                    "합천군",
                    "남해군",
                    "함양군",
                    "산청군",
                    "의령군"

            },
            {
                    "제주시",      //제주도
                    "서귀포시"
            },
            {
                    "조치원읍", // 세종시
                    "연기면",
                    "연동면",
                    "부강면",
                    "금남면",
                    "장군면",
                    "연서면",
                    "전의면",
                    "전동면",
                    "소정면",
                    "한솔동",
                    "도담동",
                    "아름동"

            },
    };


    private ArrayList<String> m_lstLocation1 = new ArrayList<String>();
    private ArrayList<ArrayList<String>> m_lstLocation2 = new ArrayList<ArrayList<String>>();
    private ArrayList<String> m_lstSubLocation1 = new ArrayList<String>();
    private ArrayList<String> m_lstTempSubLocation1 = new ArrayList<String>();

    public SelectLocationDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;

        initData();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_select_location);

        Button btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get exact select position.
                View view1 = getViewByPosition(m_lvLocation2.getFirstVisiblePosition() + 2, m_lvLocation2);
                Rect rect1 = Utility.getFrameForView(view1);
                View view2 = getViewByPosition(m_lvLocation2.getFirstVisiblePosition() + 3, m_lvLocation2);
                Rect rect2 = Utility.getFrameForView(view2);

                ImageView ivSelected = (ImageView)findViewById(R.id.iv_select);
                Rect rect = Utility.getFrameForView(ivSelected);

                Rect interSect1 = Utility.getIntersectionRect(rect, rect1);
                Rect interSect2 = Utility.getIntersectionRect(rect, rect2);

                int selecteIdx = m_lvLocation2.getFirstVisiblePosition();

                if(interSect1 == null) {
                    selecteIdx = selecteIdx +1;
                }
                else if(interSect2 != null){

                    int square1 = interSect1.width()*interSect1.height();
                    int square2 = interSect2.width()*interSect2.height();

                    if(square1 < square2) {
                        selecteIdx = selecteIdx + 1;
                    }
                }

                String location1 = m_arrLocation1[m_lvLocation1.getFirstVisiblePosition()];
                String location2 = m_arrLocation2[m_lvLocation1.getFirstVisiblePosition()][selecteIdx];

                if(mContext.getClass() == LoginActivity.class) {
                    ((LoginActivity)mContext).setLocation(location1 + " " + location2);
                }

                dismiss();
            }
        });
        btnOK = (Button) findViewById(R.id.btn_cancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        m_lvLocation1 = (ListView)findViewById(R.id.lv_location_1);
        m_lvLocation2 = (ListView)findViewById(R.id.lv_location_2);

        m_lvLocation1.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_row_location_datapicker, m_lstLocation1){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = convertView;
                if(v == null)
                {
                    LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(  R.layout.item_row_location_datapicker, null);
                }

                String location = m_lstLocation1.get(position);
                TextView cpText = (TextView) v.findViewById( R.id.tv_location);
                cpText.setText( location);
                return v;
            }
        });

        m_lstSubLocation1 = m_lstLocation2.get(mExtraCount);
        m_lvLocation2.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_row_location_datapicker, m_lstSubLocation1){

            @Override
            public int getCount() {
                return m_lstSubLocation1.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = convertView;
                if(v == null)
                {
                    LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(  R.layout.item_row_location_datapicker, null);
                }

                String location = m_lstSubLocation1.get(position);
                TextView cpText = (TextView) v.findViewById( R.id.tv_location);
                cpText.setText( location);
                return v;
            }
        });

        m_lvLocation1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    m_lstSubLocation1 = m_lstTempSubLocation1;
                    ((ArrayAdapter<String>)m_lvLocation2.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibelItemCount, int totalItemCount) {
                m_lstTempSubLocation1 = m_lstLocation2.get(firstVisibleItem + mExtraCount);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_exit:
                onDestroy();
                break;
        }
    }

    private void onDestroy() {
        dismiss();
    }

    private void initData() {
        int size = m_arrLocation1.length;

        for(int i = 0; i < mExtraCount; i++) {
            m_lstLocation1.add("");
        }
        for(int i = 0; i < size; i++) {
            m_lstLocation1.add(m_arrLocation1[i]);
        }
        for(int i = 0; i < mExtraCount; i++) {
            m_lstLocation1.add("");
        }

        for(int i = 0; i < mExtraCount; i++) {
            m_lstLocation2.add(new ArrayList<String>());
        }
        for(int i = 0; i < size; i++) {
            int arrSize = m_arrLocation2[i].length;
            ArrayList<String> arrayList = new ArrayList<String>();
            for(int k = 0; k < mExtraCount; k++) {
                arrayList.add("");
            }
            for(int j = 0; j < arrSize; j++) {
                arrayList.add(m_arrLocation2[i][j]);
            }
            for(int k = 0; k < mExtraCount; k++) {
                arrayList.add("");
            }
            m_lstLocation2.add(arrayList);
        }
        for(int i = 0; i < mExtraCount; i++) {
            m_lstLocation2.add(new ArrayList<String>());
        }
    }
}

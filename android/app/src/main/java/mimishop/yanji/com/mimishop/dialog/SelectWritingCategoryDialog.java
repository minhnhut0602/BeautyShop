package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import mimishop.yanji.com.mimishop.activity.freetalk.WritingActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;

/**
 * Created by KCJ on 5/5/2015.
 */
public class SelectWritingCategoryDialog extends Dialog {

    public final String TAG = "SelectWritingCategoryDialog";

    private ArrayList<Category> m_arrCategory = new ArrayList<Category>();
    private ListView    m_lvCategory = null;
    private Context     m_pContext = null;
    private Rect        m_rtPopup = null;
    private ImageView   m_ivTriangle = null;

    public SelectWritingCategoryDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        m_pContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_select_writing_category);

        m_lvCategory = (ListView) findViewById(R.id.lv_category);
        m_lvCategory.setAdapter(new ArrayAdapter<Category>(getContext(), R.layout.item_row_writing_category){

            @Override
            public int getCount() {
                return m_arrCategory.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = convertView;
                if(v == null)
                {
                    LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(  R.layout.item_row_writing_category, null);
                }

                Category rm = m_arrCategory.get(position);
                String boardTitle = rm.getName();
                TextView cpText = (TextView) v.findViewById( R.id.tv_mark);
                cpText.setText( boardTitle);
                return v;
            }
        });

        m_lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (m_pContext.getClass() == WritingActivity.class) {
                    ((WritingActivity) m_pContext).setCategory(m_arrCategory.get(i));
                    dismiss();
                }
            }
        });

        m_ivTriangle = (ImageView)findViewById(R.id.iv_triangle);
        if(m_rtPopup != null) {

            m_ivTriangle.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener(){
                        public boolean onPreDraw() {
                            int finalHeight = m_ivTriangle.getMeasuredHeight();
                            int finalWidth = m_ivTriangle.getMeasuredWidth();

                            // Do your work here
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)m_ivTriangle.getLayoutParams();
                            params.leftMargin = m_rtPopup.centerX() - m_ivTriangle.getWidth()/2;
                            params.topMargin =m_rtPopup.bottom - 25; // status bar height

                            m_ivTriangle.setLayoutParams(params);

                            return true;
                        }
                    });
        }

        requestCategoryList();
    }

    public void setPopupRect(Rect rect) {
        m_rtPopup = rect;
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
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void responseCategoryList(Object json){

        try {

            m_arrCategory.clear();
            JSONArray dataArray = (JSONArray)json;

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                Category category = new Category(jsonObject);

                if(category.isTalkCategory() == true) {
                    m_arrCategory.add(category);
                }
            }

            ArrayAdapter<Category> adapter = (ArrayAdapter<Category>)m_lvCategory.getAdapter();

            ((ArrayAdapter<Category>)adapter).notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(getContext(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
        }
    }
}

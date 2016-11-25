package mimishop.yanji.com.mimishop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Question;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CustomEditText;

/**
 * Created by KCJ on 4/28/2015.
 */
public class InputWrongInfoDialog  extends Dialog implements View.OnClickListener {

    public static final String TAG = "InputWrongInfoDialog";

    private Context mContext;
    private ImageButton mBtnClose;

    private Question m_clsWrongInformation = null;
    private int mShopID = 0;

    private boolean m_isShowKeyboard = true;

    public InputWrongInfoDialog(Context context, int shop_id) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        mShopID = shop_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_input_wrong_info);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mBtnClose = (ImageButton) findViewById(R.id.ib_exit);
        mBtnClose.setOnClickListener(this);

        Button btnModify = (Button) findViewById(R.id.btn_modify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                requestWriteShopWrongInfo();
            }
        });

        requestWrongInformation();

        initKeyboardTouch();
    }

    private  void initKeyboardTouch() {
        View view = findViewById(R.id.root);
        final View activityRootView = view;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    m_isShowKeyboard = true;
                }
                else {
                    m_isShowKeyboard = false;
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_isShowKeyboard == true) {
                    hideKeyboard();
                }
                else if(false) {
                    dismiss();
                }
            }
        });

        view = findViewById(R.id.content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (focus != null) {
            im.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
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


    private void showWrongInforamtion() {
        if(m_clsWrongInformation == null) {
            return;
        }

        RadioGroup rgWrongInfo = (RadioGroup)findViewById(R.id.rg_shop_status);
        RadioButton rbButton = (RadioButton)rgWrongInfo.getChildAt(m_clsWrongInformation.getShopcommentShopStatus());
        if(rbButton != null) {
            rbButton.setChecked(true);
        }

        CustomEditText etWrongInfo = (CustomEditText)findViewById(R.id.cet_wrong_info);
        etWrongInfo.setText(m_clsWrongInformation.getContent());
    }

    private void requestWrongInformation(){


        String url = ServerAPIPath.API_GET_SHOP_WRONGINFORMATION_LIST;

        int shop_id = mShopID;
        url+="&shopcommentShopID="+shop_id;
        int user_id = AppController.getInstance().getCurrentUser().getId();
        url+="&shopcommentUserID="+user_id;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject jsonObject = (JSONObject)o;

                            JSONArray dataArray = jsonObject.getJSONArray("list");
                            if (dataArray.length() > 0) {
                                m_clsWrongInformation = new Question(dataArray.getJSONObject(0));
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(getContext(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                        showWrongInforamtion();
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

    private void requestWriteShopWrongInfo() {
        RadioGroup rgWrongInfo = (RadioGroup)findViewById(R.id.rg_shop_status);

        if(rgWrongInfo.getCheckedRadioButtonId() == 0) {
            Toast.makeText(getContext(), "정보를 입력해주십시오.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_SHOP_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        if(m_clsWrongInformation != null) {
            params.put("comment_id", String.format("%d",m_clsWrongInformation.getId()));
        }
        params.put("shopcommentShopID", String.format("%d", mShopID));
        params.put("shopcommentType", "WI");

        rgWrongInfo = (RadioGroup)findViewById(R.id.rg_shop_status);
        View radioButton = rgWrongInfo.findViewById(rgWrongInfo.getCheckedRadioButtonId());
        int idx = rgWrongInfo.indexOfChild(radioButton);

        params.put("shopcommentShopStatus", String.format("%d", idx));

        CustomEditText etContent = (CustomEditText)findViewById(R.id.cet_wrong_info);
        params.put("shopcommentContent", etContent.getText().toString());

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(getContext(), ResponseMessage.SUCCESS_REQUEST, Toast.LENGTH_SHORT).show();
                        dismiss();
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
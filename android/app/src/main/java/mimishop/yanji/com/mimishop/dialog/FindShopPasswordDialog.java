package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CustomEditText;

/**
 * Created by KimCholJu on 5/22/2015.
 */
public class FindShopPasswordDialog extends Dialog {

    public static final String TAG = "FindShopPasswordDialog";

    private Context mContext;
    private CustomEditText m_etPhonenumber;
    private CustomEditText m_etShopName;

    private int  m_nShopID;

    public FindShopPasswordDialog(Context context, int shopID) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        m_nShopID = shopID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.dlg_find_shop_passwd);
        m_etPhonenumber = (CustomEditText)findViewById(R.id.et_phone_number);
        m_etShopName = (CustomEditText)findViewById(R.id.et_shop_name);

        Button btnOK = (Button)findViewById(R.id.btn_transfer);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(m_etPhonenumber.getText().toString().equals("") == true) {
                    Toast.makeText(mContext, ResponseMessage.ERR_NO_PHONENUMBER, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(m_etPhonenumber.getText().toString().equals("") == true) {
                    Toast.makeText(mContext, ResponseMessage.ERR_NO_SHOP_NAME, Toast.LENGTH_SHORT).show();
                    return;
                }

                requestShopPassword();

                hideKeyboard();
                dismiss();
            }
        });

        ImageButton btnExit = (ImageButton)findViewById(R.id.ib_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                dismiss();
            }
        });

        if(true) {
            initKeyboardTouch();
        }
    }


    private void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (focus != null) {
            im.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    private  void initKeyboardTouch() {

        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
        view = findViewById(R.id.content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }

    private void requestShopPassword() {
        String url = ServerAPIPath.API_POST_FIND_SHOP_INFO;

        Map<String, String> params = new HashMap<String, String>();

        params.put("shop_name", m_etShopName.getText());
        params.put("phone_number", m_etPhonenumber.getText());
        params.put("shop_id", String.format("%d", m_nShopID));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(getContext(), ResponseMessage.SUCCESS_TRANSFER, Toast.LENGTH_SHORT).show();
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

package mimishop.yanji.com.mimishop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
import mimishop.yanji.com.mimishop.activity.cast.CastDetailActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KimCholJu on 5/14/2015.
 */
public class ConfirmDeclareDialog extends Dialog implements View.OnClickListener {

    public final static String TAG = "ConfirmDeclareDialog";

    private Context mContext;
    private FreeTalk freetalk;
    private Comment  comment;

    public ConfirmDeclareDialog(Context context, FreeTalk freetalk) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        this.freetalk = freetalk;
    }

    public ConfirmDeclareDialog(Context context, Comment comment) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        this.comment = comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_confirm_declare);

        final Button btnOK = (Button)findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(freetalk != null) {
                    requestDoDeclareFreetalk();
                }
                else if(comment  != null) {
                    requestDoDeclareComment();
                }
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if(false) {
            initKeyboardTouch();
        }
    }

    private  void initKeyboardTouch() {
        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view = findViewById(R.id.content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onClick(View view) {

    }

    private void onDestroy() {
        dismiss();
    }

    public void requestDoDeclareFreetalk(){

        String url = ServerAPIPath.API_POST_DO_DECLARE_FREETALK;

        Map<String, String> params = new HashMap<String, String>();

        params.put("freetalk_id", String.format("%d", freetalk.getId()));
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(mContext, ResponseMessage.SUCCESS_DECLARE, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                params);
    }

    public void requestDoDeclareComment(){

        String url = ServerAPIPath.API_POST_DO_DECLARE_FREETALK_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("freetalk_comment_id", String.format("%d", comment.getId()));
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(mContext, ResponseMessage.SUCCESS_DECLARE, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                },
                params);
    }
}
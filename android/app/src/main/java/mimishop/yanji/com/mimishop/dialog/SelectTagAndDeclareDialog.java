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
import mimishop.yanji.com.mimishop.activity.freetalk.WriteCommentActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
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

/**
 * Created by KimCholJu on 5/14/2015.
 */
public class SelectTagAndDeclareDialog extends Dialog implements View.OnClickListener {

    public final static String TAG= "SelectTagAndDeclareDialog";

    private Context mContext;

    private FreeTalk freetalk = null;
    private Comment  comment = null;
    private boolean disableTAG = false;

    public SelectTagAndDeclareDialog(Context context, FreeTalk freetalk) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        this.freetalk = freetalk;
    }

    public SelectTagAndDeclareDialog(Context context, Comment comment) {
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

        setContentView(R.layout.dlg_select_tag_declare);

        View icTAG = findViewById(R.id.ll_tag);

        icTAG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteCommentActivity.getInstance().tagComment(comment);
                dismiss();
            }
        });

        View icDeclare = findViewById(R.id.ll_declare);
        icDeclare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDeclareDialog dlg = null;
                if(freetalk != null) {
                   dlg =  new ConfirmDeclareDialog(getContext(), freetalk);

                    User me = AppController.getInstance().getCurrentUser();
                    if(me.getId() == freetalk.getPostManID()) {
                        Toast.makeText(getContext(), ResponseMessage.ERR_DUPLICATE_WRITING, Toast.LENGTH_SHORT).show();
                        dismiss();
                        return;
                    }

                }
                else {
                    dlg =  new ConfirmDeclareDialog(getContext(), comment);
                }
                dlg.show();
                dismiss();
            }
        });

        View icExit = findViewById(R.id.ll_exit);
        icExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if(disableTAG == true) {
            icTAG.setVisibility(View.GONE);
            icDeclare.setBackground(mContext.getResources().getDrawable(R.drawable.layout_dlg_top_bg_round_white));
            View view = findViewById(R.id.fl_border_1);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void setDisableTAG() {
        disableTAG = true;
    }


    private void onDestroy() {
        dismiss();
    }
}
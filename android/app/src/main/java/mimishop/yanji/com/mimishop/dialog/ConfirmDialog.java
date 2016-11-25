package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;

/**
 * Created by KCJ on 10/14/2015.
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {

    public final static String TAG = "ConfirmDialog";

    private Context mContext;
    private String m_strMessage;
    private View.OnClickListener m_vOnClickListner;

    public ConfirmDialog(Context context, String message, View.OnClickListener onClickListner) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        m_strMessage = message;
        m_vOnClickListner = onClickListner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_confirm);


        if (false) {
            initKeyboardTouch();
        }

        TextView tvMessage = (TextView)findViewById(R.id.tv_message);
        tvMessage.setText(m_strMessage);

        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    private void initKeyboardTouch() {
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
        dismiss();

        if(m_vOnClickListner != null) {
            m_vOnClickListner.onClick(view);
        }
    }

    private void onDestroy() {
        dismiss();
    }
}
package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.freetalk.WriteCommentActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;

/**
 * Created by KimCholJu on 6/6/2015.
 */
public class SelectRemoveDialog extends Dialog implements View.OnClickListener {

    public final static String TAG= "SelectRemoveDialog";

    private Context mContext;

    private Comment comment = null;

    public SelectRemoveDialog(Context context, Comment comment) {
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

        setContentView(R.layout.dlg_select_remove);

        View icTAG = findViewById(R.id.ll_remove);

        icTAG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WriteCommentActivity.getInstance().removeComment(comment);
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
    }

    @Override
    public void onClick(View view) {

    }

    private void onDestroy() {
        dismiss();
    }
}
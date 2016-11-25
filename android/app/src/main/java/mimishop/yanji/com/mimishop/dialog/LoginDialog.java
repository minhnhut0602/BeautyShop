package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.LoginActivity;
import mimishop.yanji.com.mimishop.activity.MainActivity;

/**
 * Created by John on 1/19/2016.
 */
public class LoginDialog extends Dialog implements View.OnClickListener{
    public final static String TAG = "LoginDialog";

    private Context mContext;

    public LoginDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_login);

        Button mBtnOK = (Button) findViewById(R.id.btn_ok);
        mBtnOK.setOnClickListener(this);
        Button mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        Button mBtnClose = (Button) findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(this);
    }
    private void onDestroy() {
        dismiss();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                startLoginActivity();
                onDestroy();
                break;
            case R.id.btn_cancel:
                onDestroy();
                break;
            case R.id.btn_close:
                onDestroy();
                break;
        }
    }
}

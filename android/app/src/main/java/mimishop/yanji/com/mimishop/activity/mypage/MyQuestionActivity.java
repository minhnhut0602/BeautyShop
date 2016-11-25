package mimishop.yanji.com.mimishop.activity.mypage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.ReceiveQuestionDialog;
import mimishop.yanji.com.mimishop.fragment.MyPageFragment;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class MyQuestionActivity extends Activity {

    public static final String TAG = "MyQuestionActivity";

    private Button            m_btnSendQuestion = null;
    private EditText          m_etQuestion = null;
    private  ReceiveQuestionDialog m_dlgReciveQuestion = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        m_etQuestion = (EditText)findViewById(R.id.et_question);
        m_btnSendQuestion = (Button)findViewById(R.id.btn_send);
        m_btnSendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(MyQuestionActivity.this);
                requestWriteAdminQuestion();
            }
        });

        if(false) {
            User user = MyPageFragment.getInstance().getMe();
            if (user.getUserQuestion() != null) {
                m_etQuestion.setText(user.getUserQuestion());
            }
        }

        initMenu();
        initKeyboardTouch();

        m_dlgReciveQuestion = new ReceiveQuestionDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_my_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_my_question));
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_back);

        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.hideSoftKeyboard(MyQuestionActivity.this);
                    finish();
                }
            });
        }
    }

    private void initKeyboardTouch(){
        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(MyQuestionActivity.this);
            }
        });
    }

    private void requestWriteAdminQuestion() {
        String url = ServerAPIPath.API_POST_WRITE_ADMIN_QUESTION;
        Map<String, String> params = new HashMap<String, String>();
        params.put("content", m_etQuestion.getText().toString());
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        m_dlgReciveQuestion.show();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MyQuestionActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }
}

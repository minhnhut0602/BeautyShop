package mimishop.yanji.com.mimishop.activity.mypage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.Preference;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.LoginActivity;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.dialog.ConfirmDialog;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Utility;

public class MySettingActivity extends Activity {

    private CheckBox m_cbEnvelop;
    private CheckBox m_cbComment;
    private CheckBox m_cbTag;
    private TextView m_tvCurVer;
    private TextView m_tvMaxVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_my_setting, menu);
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

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_my_setting));
        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void init() {
        initMenu();

        m_cbEnvelop = (CheckBox) findViewById(R.id.cb_envelop);
        m_cbComment = (CheckBox) findViewById(R.id.cb_comment);
        m_cbTag = (CheckBox) findViewById(R.id.cb_tag);
        m_tvCurVer = (TextView) findViewById(R.id.tv_current_version);
        m_tvMaxVer = (TextView) findViewById(R.id.tv_max_version);

        m_cbEnvelop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preference pref = Preference.getInstance();
                pref.putSharedPreference(MySettingActivity.this, Preference.KEY_PUSH_ENVELOP, b);
            }
        });

        m_cbComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preference pref = Preference.getInstance();
                pref.putSharedPreference(MySettingActivity.this, Preference.KEY_PUSH_COMMENT, b);
            }
        });

        m_cbTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preference pref = Preference.getInstance();
                pref.putSharedPreference(MySettingActivity.this, Preference.KEY_PUSH_TAG, b);
            }
        });

        findViewById(R.id.rl_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfirmDialog(MySettingActivity.this, "정말 탈퇴 하시겠습니까?",
                        new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                if(view.getId() == R.id.btn_ok) {
                                    requestLogOut();
                                }
                            }
                        }).show();
            }
        });

        refreshUI();
    }

    private void refreshUI() {
        Preference pref = Preference.getInstance();
        AppController controller = AppController.getInstance();
        m_cbEnvelop.setChecked(pref.getSharedPreference(controller, Preference.KEY_PUSH_ENVELOP, true));
        m_cbComment.setChecked(pref.getSharedPreference(controller, Preference.KEY_PUSH_COMMENT, true));
        m_cbTag.setChecked(pref.getSharedPreference(controller, Preference.KEY_PUSH_TAG, true));

        m_tvCurVer.setText(Common.CUR_VER + "v");
        m_tvMaxVer.setText(Common.mAppVersion + "v");
    }

    private void requestLogOut() {
        String url = ServerAPIPath.API_POST_LOG_OUT;
        Map<String, String> params = new HashMap<String, String>();

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Utility.hideWaitingDlg();
                        //Intent intent = new Intent(MySettingActivity.this, LoginActivity.class);
                        AppController.getInstance().setCurrentUser(null);
                        Intent intent = new Intent(MySettingActivity.this, MainActivity.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(MySettingActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }, params
        );
    }
}

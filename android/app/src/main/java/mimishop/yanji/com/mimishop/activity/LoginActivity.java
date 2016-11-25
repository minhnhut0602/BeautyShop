package mimishop.yanji.com.mimishop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import mimishop.yanji.com.mimishop.activity.agreement.AgreementActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.SelectEmailDialog;
import mimishop.yanji.com.mimishop.dialog.SelectLocationDialog;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Location;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CustomEditText;

public class LoginActivity extends Activity {

    public static String TAG = "LoginActivity";

//    private WebView m_wvUserUse;

    private CustomEditText m_etUserID;
    private EditText m_etEmail;
    private CustomEditText m_etAge;
    private EditText m_etBirthday;
    private View           m_rlLocation;
    private TextView       m_tvLocation;
    private TextView       m_tvEmailSelect;
    //tv_email_select
//    private EditText m_etRecommender;
    private CheckBox m_cbMan;
    private CheckBox m_cbWoman;
    private CheckBox m_cbNoSexSelect;
    private CheckBox m_cbAgreePrivay1,m_cbAgreePrivay2,m_cbAgreePrivayAll;
    private Button   m_btnDuplicateID;
    private boolean  m_isDuplicateID = true;

    private int m_nSexSelect = 0;

    private Location m_pSelectedLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((TextView)findViewById(R.id.tv_title)).setText("회원가입하기");
        ((TextView)findViewById(R.id.tv_title)).setGravity(Gravity.LEFT);
        TextView tv_next = (TextView)findViewById(R.id.tv_menu);
        tv_next.setText("가입");
        tv_next.setVisibility(View.VISIBLE);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCreateAccount();
            }
        });
        ImageButton backBtn = (ImageButton)findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }


//        m_wvUserUse = (WebView) findViewById(R.id.wv_privacy_1);
//        m_wvUserUse.getSettings().setJavaScriptEnabled(true);
//        m_wvUserUse.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        m_wvUserUse.setWebChromeClient(new WebChromeClient());
//        m_wvUserUse.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//
//            }
//        });

        m_etUserID = (CustomEditText)findViewById(R.id.et_user_id);
        m_etUserID.setOnFocusEvent(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                m_etUserID.refreshSelect(hasFocus);
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    requestDuplicateNickName();
                }
            }
        });

        View m_rlEmailSelect = findViewById(R.id.rl_email_select);
        m_tvEmailSelect = (TextView)findViewById(R.id.tv_email_select);

        m_rlEmailSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectEmailDialog dlg = new SelectEmailDialog(LoginActivity.this);
                dlg.show();
            }
        });

        m_cbMan = (CheckBox)findViewById(R.id.cb_man);
        m_cbWoman = (CheckBox)findViewById(R.id.cb_woman);
        m_cbNoSexSelect = (CheckBox)findViewById(R.id.cb_no_sex_select);

        m_cbWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickManWoman(0);
            }
        });


        m_cbMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickManWoman(1);
            }
        });


        m_cbNoSexSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickManWoman(2);
            }
        });

        clickManWoman(0);

        m_etAge = (CustomEditText)findViewById(R.id.et_user_age);

        m_rlLocation = findViewById(R.id.rl_location);
        m_tvLocation = (TextView)findViewById(R.id.tv_location);

        m_rlLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLocationDialog dlg = new SelectLocationDialog(LoginActivity.this);
                dlg.show();
            }
        });

//        m_etRecommender = (CustomEditText)findViewById(R.id.et_recommender);
        m_etEmail = (EditText)findViewById(R.id.et_email);

//        App app = AppController.getInstance().getApp();
//        m_wvUserUse.loadUrl(app.getAppUserUseURL());

        m_cbAgreePrivay1 = (CheckBox) findViewById(R.id.cb_agree_privacy1);
        m_cbAgreePrivay2 = (CheckBox) findViewById(R.id.cb_agree_privacy2);
        m_cbAgreePrivayAll = (CheckBox) findViewById(R.id.cb_agree_privacy_all);

        m_cbAgreePrivayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = m_cbAgreePrivayAll.isChecked();
                m_cbAgreePrivay1.setChecked(checked);
                m_cbAgreePrivay2.setChecked(checked);
                m_cbAgreePrivayAll.setChecked(checked);
            }
        });

        m_cbAgreePrivay1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false) {
                    m_cbAgreePrivayAll.setChecked(false);
                } else {
                    if(m_cbAgreePrivay2.isChecked()) m_cbAgreePrivayAll.setChecked(true);
                }
            }
        });

        m_cbAgreePrivay2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false) {
                    m_cbAgreePrivayAll.setChecked(false);
                } else {
                    if(m_cbAgreePrivay1.isChecked()) m_cbAgreePrivayAll.setChecked(true);
                }
            }
        });

        m_btnDuplicateID = (Button)findViewById(R.id.btn_duplicate_id);
        m_btnDuplicateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDuplicateNickName();
            }
        });

        TextView tvAgreePrivacy1 = (TextView)findViewById(R.id.tvAgreePrivacy1);
        TextView tvAgreePrivacy2 = (TextView)findViewById(R.id.tvAgreePrivacy2);
        tvAgreePrivacy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });

        tvAgreePrivacy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        initKeyboradTouch();
    }

    public void emailSelected(String  email)
    {
        m_tvEmailSelect.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private void initKeyboradTouch() {
        View root = findViewById(R.id.root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utility.getInstance().hideSoftKeyboard(LoginActivity.this);
                return false;
            }
        });

        root = findViewById(R.id.ll_content);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utility.getInstance().hideSoftKeyboard(LoginActivity.this);
                return false;
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void clickManWoman(int select){
        m_nSexSelect = select;

        if(m_nSexSelect == 0) {
            m_cbNoSexSelect.setChecked(false);
            m_cbMan.setChecked(false);
            m_cbWoman.setChecked(true);
        } else if(m_nSexSelect == 1) {
            m_cbMan.setChecked(true);
            m_cbWoman.setChecked(false);
            m_cbNoSexSelect.setChecked(false);
        } else {
            m_cbMan.setChecked(false);
            m_cbWoman.setChecked(false);
            m_cbNoSexSelect.setChecked(true);
        }
    }

    public void setLocation(Location location) {
        m_pSelectedLocation = location;
        m_tvLocation.setText(location.getLocationName());
    }

    public void setLocation(String location) {
        m_tvLocation.setText(location);
    }

    private void requestCreateAccount() {

//        if(m_cbAgreePrivay1.isChecked() == false || m_cbAgreePrivay2.isChecked() == false) {
//            Toast.makeText(this, "이용약관에 동의하여주십시오.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(m_etUserID.getText() == null || m_etUserID.getText().toString().equals("")) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        
//        if(m_etEmail.getText() == null || m_etEmail.getText().toString().equals("")) {
//            Toast.makeText(this, "이메일주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(m_tvEmailSelect.getText() == null || m_tvEmailSelect.getText().toString().equals("")) {
//            Toast.makeText(this, "이메일주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        if(m_etEmail.getText() != null && Utility.isEmailValid(m_etEmail.getText().toString()+"@"+m_tvEmailSelect.getText().toString()) == false) {
//            Toast.makeText(this, "이메일주소가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(m_etAge.getText() == null || m_etAge.getText().toString().equals("")) {
//            Toast.makeText(this, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(m_isDuplicateID == true) {
//            Toast.makeText(this, "회원아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(m_tvLocation.getText() == null || m_tvLocation.getText().toString().equals("")) {
//            Toast.makeText(this, "지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        try {
//            int age =  Integer.parseInt(m_etAge.getText());
//        }
//        catch (Exception e) {
//            Toast.makeText(this, "나이를 올바르게 입력해 주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Utility.showWaitingDlg(this);
        String url = ServerAPIPath.API_POST_CREATE_ACCOUNT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("type", "C");

        params.put("userID", m_etUserID.getText().toString());

        if(m_etEmail.getText() != null && m_etEmail.getText().toString().equals("") == false) {
            params.put("userEmail",  m_etEmail.getText().toString());
        }

        if(m_nSexSelect == 0) {
            params.put("userSex", "W");

        } else if(m_nSexSelect == 1) {
            params.put("userSex", "M");
        } else {
            params.put("userSex", "X");
        }

        params.put("userAge", m_etAge.getText().toString());

        if(m_pSelectedLocation != null) {
            params.put("userLocationID", String.format("%d", m_pSelectedLocation.getId()));
        }

        params.put("userLocationName", m_tvLocation.getText().toString());

//        if(m_etRecommender.getText() != null && m_etRecommender.getText().toString().equals("") == false) {
//            params.put("userRecommenderID",  m_etRecommender.getText().toString());
//        }

        ServerAPICall.getInstance().callPOST(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {

                        if(s == null) {
                            Toast.makeText(LoginActivity.this, ResponseMessage.ERR_CREATE_ACCOUNT, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject dataObject = (JSONObject)s;
                        User user = new User(dataObject);
                        App app  = new App(dataObject);

                        AppController.getInstance().setCurrentUser(user);
                        AppController.getInstance().setApp(app);

                        Common.mKey = user.getUserKey();

                        AppController.getInstance().isUserId = user.isUserID();

                        SessionManager.getInstance().saveHeader(LoginActivity.this);
                        Toast.makeText(LoginActivity.this, ResponseMessage.SUCCESS_LOGIN, Toast.LENGTH_SHORT).show();
                        startMainActivity();
                        finish();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String err) {
                        Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                },
                params);
    }

    private void requestDuplicateNickName() {

        if(m_etUserID.getText() == null || m_etUserID.getText().equals("")) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_DUPLICATE_USERID;
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "C");
        params.put("userID", m_etUserID.getText().toString());
        ServerAPICall.getInstance().callPOST(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {

                        if(s == null) {
                            Toast.makeText(LoginActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String result = (String) s;
                        if (result.equals("TRUE")) {
                            m_isDuplicateID = true;
                            m_btnDuplicateID.setText("중복");
                        } else {
                            m_isDuplicateID = false;
                            m_btnDuplicateID.setText("가능");
                        }
                    }
                }
                ,
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
                , params);
    }
}

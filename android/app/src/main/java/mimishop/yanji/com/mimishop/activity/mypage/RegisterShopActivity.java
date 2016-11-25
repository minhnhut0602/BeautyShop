package mimishop.yanji.com.mimishop.activity.mypage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.Preference;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.LocationSearchActivity;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.adapter.MyPriceAdapter;
import mimishop.yanji.com.mimishop.adapter.SimpleSectionAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.CameraAndPhotoDialog;
import mimishop.yanji.com.mimishop.fragment.MyPageFragment;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Product;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.ComboBox;
import mimishop.yanji.com.mimishop.view.CustomEditText;

public class RegisterShopActivity extends Activity {

    public static final String TAG = "RegisterShopAndProduct";
    private static final int UPLOAD_FILE_CNT = 1;

    private View m_icRegisterStore;
    private View m_icRegisterProduct;
    private Button m_btnRegisterStore;
    private Button m_btnRegisterProduct;

    private CustomEditText m_etStoreID;
    private CustomEditText m_etStoreName;
    private CustomEditText m_etStorePW;
    private CustomEditText m_etStorePWConfirm;
    private CustomEditText m_etStoreAddress;
    private CustomEditText m_etStorePhoneNumber;
    private CustomEditText m_etStoreOpenTime;
    private CustomEditText m_etStoreRestTime;
    private CustomEditText m_etStoreStuffName;
    private CustomEditText m_etStoreStuffPhoneNumber;

    private CheckBox       m_cbParkEnable;
    private CheckBox       m_cbParkDisable;

    private ComboBox m_spCategory = null;
    private ArrayList<Category> m_lstCategory = new ArrayList<Category>();
    private ArrayList<String> m_lstCategoryName = new ArrayList<String>();

    // view info
    private MyPriceAdapter m_adpPrice= null;
    private ArrayList<Product> m_lstPrices= new ArrayList<Product>();
    private SimpleSectionAdapter<Product> m_adpSectionPricesAdapter = null;

    private CameraAndPhotoDialog m_dlgCapPhoDlg = null;

    private ListView m_lvPrice = null;

    private boolean m_isDuplicateStoreID = true;

    private boolean m_isRegistered = false;
    private boolean m_isCheckedParkable = false;

    private double  m_fShopLat = 0.0f;
    private double  m_fShopLng = 0.0f;

    private boolean isUploadAll = false;
    private File []arrUploadFile = new File[UPLOAD_FILE_CNT];
    boolean []isUploadFlag = new boolean[UPLOAD_FILE_CNT];
    int []arrUploadFileID = new int[UPLOAD_FILE_CNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop_and_product);

        m_icRegisterStore = findViewById(R.id.ic_register_store);

        // blue screen
        if(AppController.getInstance().getCurrentUser() == null) {
            Common.mDeviceId = Utility.getInstance().getDeviceId(this);
            requestInit();
        }

        init();
    }

    private void init() {
        initRegisterShop();

        initMenu();

        initKeyboardTouch();

        refreshUI();
    }

    public void requestInit(){
        String url = ServerAPIPath.API_INIT;
        ServerAPICall.getInstance().callGET(url,
                new  onReponseListener() {
                    @Override
                    public void onResponse(Object s) {
                        responseInit(s);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String err) {
                        Toast.makeText(RegisterShopActivity.this, err, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void responseInit(Object s) {
        if(s == null) {
            Toast.makeText(RegisterShopActivity.this, ResponseMessage.ERR_NO_RESULT, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            JSONObject object = (JSONObject) s;
            User user = new User(object);
            App app = new App(object);

            AppController.getInstance().setCurrentUser(user);
            AppController.getInstance().setApp(app);

            Common.mKey = user.getUserKey();
        }
        catch (Exception e) {
            Toast.makeText(RegisterShopActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText("내 상점 등록");
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_back);

        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void initKeyboardTouch() {
        View view = findViewById(R.id.ll_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(RegisterShopActivity.this);
            }
        });

        view = findViewById(R.id.menu_main);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(RegisterShopActivity.this);
            }
        });
    }

    private void initRegisterShop() {
        m_dlgCapPhoDlg = new CameraAndPhotoDialog(this);

        m_etStoreID = (CustomEditText) findViewById(R.id.et_store_id);
        m_etStoreName = (CustomEditText) findViewById(R.id.et_store_name);
        m_etStorePW = (CustomEditText) findViewById(R.id.et_store_pw);
        m_etStorePWConfirm = (CustomEditText) findViewById(R.id.et_store_confirm_pw);
        m_etStoreAddress = (CustomEditText) findViewById(R.id.et_store_address);
        m_etStorePhoneNumber = (CustomEditText) findViewById(R.id.et_store_phone_number);
        m_etStoreOpenTime = (CustomEditText) findViewById(R.id.et_store_open_time);
        m_etStoreRestTime = (CustomEditText) findViewById(R.id.et_store_rest_time);
        m_etStoreStuffName = (CustomEditText) findViewById(R.id.et_manager_name);
        m_etStoreStuffPhoneNumber = (CustomEditText) findViewById(R.id.et_manager_phone_number);

        m_etStoreID.setOnFocusEvent(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    requestDuplicateStoreID();
                }

                m_etStoreID.refreshSelect(hasFocus);
            }
        });

        TextView textView = (TextView)findViewById(R.id.tv_duplicate_id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDuplicateStoreID();
            }
        });

        View btnRegisterManagerIdenty = findViewById(R.id.btn_store_register_identification);
        btnRegisterManagerIdenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterIdentyDlg();
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btn_register_store);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utility.hideSoftKeyboard(RegisterShopActivity.this);

                if (m_isRegistered == true) {
                    Toast.makeText(RegisterShopActivity.this, "상점이 이미 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(m_etStoreAddress.getText() != null && m_etStoreAddress.getText().toString().equals("") == false) {
                    requestGetLatLngFromAddress(m_etStoreAddress.getText().toString());
                }
                else {
                    requestRegisterShop();
                }
            }
        });

        m_cbParkDisable = (CheckBox)findViewById(R.id.cb_shop_park_disable);
        m_cbParkEnable = (CheckBox)findViewById(R.id.cb_shop_park_enable);
        m_cbParkDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_isCheckedParkable= false;
                showParkable();
            }
        });
        m_cbParkEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_isCheckedParkable= true;
                showParkable();
            }
        });

        m_isCheckedParkable = false;
        showParkable();


        if (false) {
            m_spCategory = (ComboBox) findViewById(R.id.sp_category);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, m_lstCategoryName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            m_spCategory.setAdapter(dataAdapter);

            requestCategoryList();
        }
    }

    private void showParkable() {
        if(m_isCheckedParkable == true) {
            m_cbParkDisable.setChecked(false);
            m_cbParkEnable.setChecked(true);
        }
        else {
            m_cbParkDisable.setChecked(true);
            m_cbParkEnable.setChecked(false);
        }
    }

    private void showRegisterIdentyDlg() {
        m_dlgCapPhoDlg = new CameraAndPhotoDialog(this);
        m_dlgCapPhoDlg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_register_store_and_product, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("m_etStoreID", m_etStoreID.getText().toString());
        outState.putString("m_etStoreName", m_etStoreName.getText().toString());
        outState.putString("m_etStorePW", m_etStorePW.getText().toString());
        outState.putString("m_etStorePWConfirm", m_etStorePWConfirm.getText().toString());
        outState.putString("m_etStoreAddress", m_etStoreAddress.getText().toString());
        outState.putString("m_etStorePhoneNumber", m_etStorePhoneNumber.getText().toString());
        outState.putString("m_etStoreOpenTime", m_etStoreOpenTime.getText().toString());
        outState.putString("m_etStoreRestTime", m_etStoreRestTime.getText().toString());
        outState.putString("m_etStoreStuffName", m_etStoreStuffName.getText().toString());
        outState.putString("m_etStoreStuffPhoneNumber", m_etStoreStuffPhoneNumber.getText().toString());
        outState.putBoolean("m_isCheckedParkable", m_isCheckedParkable);
        SessionManager.getInstance().saveHeader(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String str = savedInstanceState.getString("m_etStoreName");
        m_etStoreName.setText(str);
        str = savedInstanceState.getString("m_etStoreID");
        m_etStoreID.setText(str);
        str = savedInstanceState.getString("m_etStorePW");
        m_etStorePW.setText(str);
        str = savedInstanceState.getString("m_etStorePWConfirm");
        m_etStorePWConfirm.setText(str);
        str = savedInstanceState.getString("m_etStoreAddress");
        m_etStoreAddress.setText(str);
        str = savedInstanceState.getString("m_etStorePhoneNumber");
        m_etStorePhoneNumber.setText(str);
        str = savedInstanceState.getString("m_etStoreOpenTime");
        m_etStoreOpenTime.setText(str);
        str = savedInstanceState.getString("m_etStoreRestTime");
        m_etStoreRestTime.setText(str);
        str = savedInstanceState.getString("m_etStoreStuffName");
        m_etStoreStuffName.setText(str);
        str = savedInstanceState.getString("m_etStoreStuffPhoneNumber");
        m_etStoreStuffPhoneNumber.setText(str);

        m_isCheckedParkable = savedInstanceState.getBoolean("m_isCheckedParkable");
        showParkable();
        SessionManager.getInstance().restoreHeader(this);

        MainActivity.RELOAD_FRAG_IDX = MainActivity.FRAG_KIND_MYPAGE;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==Activity.RESULT_OK) {
            if (requestCode == CameraAndPhotoDialog.REQ_CODE_GALLARY_IMAGE || requestCode == CameraAndPhotoDialog.REQ_CODE_CAPTURE_IMAGE) {
                isUploadAll = false;
                try {
                    String path = "";
                    Bitmap bitmap = null;
                    if (requestCode == CameraAndPhotoDialog.REQ_CODE_GALLARY_IMAGE) {
                        bitmap = m_dlgCapPhoDlg.getPhotoFromUri(data.getData());
                        path = m_dlgCapPhoDlg.getGallaryPath();
                    } else {
                        path = m_dlgCapPhoDlg.getCapturePath();
                        bitmap = m_dlgCapPhoDlg.getPhotoFromPath(path);
                    }

                    arrUploadFile[0] = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshUI() {

        if(false) {
            if (m_btnRegisterStore.isSelected() == true) {
                m_icRegisterStore.setVisibility(View.VISIBLE);
                m_icRegisterProduct.setVisibility(View.GONE);
            } else {
                m_icRegisterStore.setVisibility(View.GONE);
                m_icRegisterProduct.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestDuplicateStoreID() {

        if(m_etStoreID.getText() == null || m_etStoreID.getText().toString().equals("")) {
            Toast.makeText(this, "상점아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_DUPLICATE_SHOP_ID;
        Map<String, String> params = new HashMap<String, String>();

        params.put("shopID", m_etStoreID.getText().toString());

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            String result = (String) o;
                            TextView textView = (TextView) findViewById(R.id.tv_duplicate_id);
                            if (result.equals("TRUE")) {
                                textView.setTextColor(getResources().getColor(R.color.red));
                                textView.setText("중복");
                                m_isDuplicateStoreID = true;
                            } else {
                                textView.setText("가능");
                                m_isDuplicateStoreID = false;
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(RegisterShopActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(RegisterShopActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestRegisterShop() {

        if(m_etStoreName.getText() == null || m_etStoreName.getText().toString().equals("")) {
            Toast.makeText(this, "상점이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_etStoreAddress.getText() == null || m_etStoreAddress.getText().toString().equals("")) {
            Toast.makeText(this, "상점주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_etStoreID.getText() == null || m_etStoreID.getText().toString().equals("")) {
            Toast.makeText(this, "상점아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_etStorePW.getText() == null) {
            Toast.makeText(this, "PW를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_etStorePWConfirm.getText() == null) {
            Toast.makeText(this, "PW확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_etStorePW.getText().toString().equals(m_etStorePWConfirm.getText().toString()) == false) {
            Toast.makeText(this, "PW를 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(m_isDuplicateStoreID == true) {
            Toast.makeText(this, "상점아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadAllShopImages();

        if(isUploadAll == false)
        {
            return;
        }


        String url = ServerAPIPath.API_POST_REGISTER_MY_SHOP;
        Map<String, String> params = new HashMap<String, String>();

        params.put("shopID", m_etStoreID.getText().toString());
        params.put("shopPW", m_etStorePW.getText().toString());
        params.put("shopName", m_etStoreName.getText().toString().trim());

        if(m_fShopLng != 0) {
            params.put("shopLongtitude", String.format("%f", m_fShopLng));
        }

        if(m_fShopLat != 0) {
            params.put("shopLatitude", String.format("%f", m_fShopLat));
        }

        String address = m_etStoreAddress.getText().toString();
        address = address.trim();
        address = LocationSearchActivity.getFilteredLocationText(address);
        params.put("shopAddress", address);
        params.put("shopPhonenumber",m_etStorePhoneNumber.getText().toString());

        String startTime = m_etStoreOpenTime.getText().toString();
        String endTime = m_etStoreRestTime.getText().toString();
        params.put("shopOpenTimeString", startTime);
        params.put("shopRestTimeString", endTime);

        if(false) {
            params.put("shopCategoryID", String.format("%d", m_lstCategory.get(m_spCategory.getSelectedIndex()).getId()));
        }

        if(m_isCheckedParkable == false) {
            params.put("shopParkable", "N");
        }
        else {
            params.put("shopParkable", "Y");
        }

        if(arrUploadFileID[0] != 0) {
            params.put("shopManagerIdentyImgID", String.format("%d", arrUploadFileID[0]));
        }

        params.put("shopPostManName", m_etStoreStuffName.getText().toString());
        params.put("shopStuffPhonenumber", m_etStoreStuffName.getText().toString());

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(RegisterShopActivity.this, ResponseMessage.SUCCESS_REGISTER, Toast.LENGTH_SHORT).show();
                        m_isRegistered = true;
                        setResult(Activity.RESULT_OK);
                        finish();
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(RegisterShopActivity.this, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                },
                params);
    }

    private void requestCategoryList(){

        String url = ServerAPIPath.API_GET_CATEGORY_LIST;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseCategoryList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(RegisterShopActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseCategoryList(Object json){

        try {
            m_lstCategory.clear();

            JSONArray dataArray = (JSONArray)json;

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                Category category = new Category(jsonObject);

                m_lstCategory.add(category);
            }

            for(int i = 0; i < m_lstCategory.size(); i++){
                m_lstCategoryName.add(m_lstCategory.get(i).getName());
            }

            SpinnerAdapter adapter = m_spCategory.getAdapter();

            ((ArrayAdapter<String>)adapter).notifyDataSetChanged();

        } catch (JSONException e) {
            Toast.makeText(RegisterShopActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
        }
    }

    private void requestGetLatLngFromAddress(String address) {

        if(address == null || address.equals("") == true) {
            Toast.makeText(RegisterShopActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_GET_LATLNG;

        String utf8Location = null;
        try {
            utf8Location = URLEncoder.encode(address, "UTF-8");
            url += utf8Location;
        }
        catch (Exception e){

        }

        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseGetLatLngFromAddress(o);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(RegisterShopActivity.this, ResponseMessage.ERR_INVALIDE_ADDRESS, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    private void responseGetLatLngFromAddress(Object o) {
        try {
            String json = (String)o;
            JSONObject response = new JSONObject(json);
            Logger.d(TAG, "responseGetLatLngFromAddress - response : " + response);

            String code = response.getString("status");

            Logger.d(TAG, "responseGetLatLngFromAddress - code : " + code);
            if (!code.equals("OK")) {
                Toast.makeText(this, ResponseMessage.ERR_INVALIDE_ADDRESS, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray jsonArray = response.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry").getJSONObject("location");
            double tempLat = jsonObject1.getDouble("lat");
            double tempLng = jsonObject1.getDouble("lng");

            m_fShopLat = tempLat;
            m_fShopLng = tempLng;

            requestRegisterShop();

        } catch (JSONException e) {
            Logger.e(TAG, "responseGetLatLngFromAddress - JSONException : " + e.getMessage());
            Toast.makeText(RegisterShopActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAllShopImages(){
        if(isUploadAll == true) {
            return;
        }

        for(int i = 0; i < isUploadFlag.length; i++) {
            isUploadFlag[i] = false;
            arrUploadFileID[i] = 0;
            if (arrUploadFile[i] == null) {
                isUploadFlag[i] = true;
            }
        }

        checkUploadFlag();
    }

    private void checkUploadFlag(){

        for(int i = 0; i < isUploadFlag.length; i++) {
            if(arrUploadFile[i] != null) {
                uploadShopImageFile(arrUploadFile[i], i);
                break;
            }
        }

        boolean isUploadAll = true;
        for(int i = 0; i < isUploadFlag.length; i++) {
            if(isUploadFlag[i] == false) {
                isUploadAll = false;
                break;
            }
        }
        this.isUploadAll = isUploadAll;
    }

    private void uploadShopImageFile(File file, final int req) {
        SystemClock.sleep(1000);
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().uploadFile(ServerAPIPath.API_POST_UPLOAD_SHOP_IMAGE, "img_filename", file,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            Logger.d(TAG, "uploadFile - response : " + response);

                            String code = response.getString("result_code");
                            String msg = response.getString("result_msg");

                            Logger.d(TAG, "uploadFile - code : " + code);
                            Logger.d(TAG, "uploadFile - msg : " + msg);
                            if (!code.equals("0")) {
                                Toast.makeText(RegisterShopActivity.this, msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject dataObject = response.getJSONObject("result_data");
                            arrUploadFileID[req] = dataObject.getInt("id");

                            if(arrUploadFileID[req]  > 1){
                                isUploadFlag[req] = true;
                                arrUploadFile[req] = null;

                                checkUploadFlag();

                                if(isUploadAll == true) {
                                    requestRegisterShop();
                                }
                            }
                            else {
                                throw new JSONException("실패");
                            }

                        } catch (JSONException e) {
                            Logger.e(TAG, "uploadFile - JSONException : " + e.getMessage());
                            Toast.makeText(RegisterShopActivity.this, "이미지업로드 실패.", Toast.LENGTH_SHORT).show();
                        }

                        Utility.hideWaitingDlg();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        Logger.d(TAG, "uploadFile - VolleyError : ");

                        Toast.makeText(RegisterShopActivity.this, "이미지업로드 실패.", Toast.LENGTH_SHORT).show();

                        Utility.hideWaitingDlg();
                    }
                });
    }


}

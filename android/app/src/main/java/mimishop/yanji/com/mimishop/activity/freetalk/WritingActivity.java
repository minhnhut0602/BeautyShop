package mimishop.yanji.com.mimishop.activity.freetalk;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devsmart.android.ui.HorizontalListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.ImageAdapter;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.CameraAndPhotoDialog;
import mimishop.yanji.com.mimishop.dialog.SelectWritingCategoryDialog;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.SelectPhotoManager;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;
import mimishop.yanji.com.mimishop.view.CustomImageButton;


public class WritingActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "WritingActivity";

    private  static final int REQ_CODE_SELECT_IMAGE=100;
    private  static final int PIC_CROP = 0;
    private static final String      CAPTURE_PATH = "/sdcard/tmp/freetalk/";

    private CircledNetworkImageView m_nivUserPhoto = null;
    private EditText          m_etContent = null;
    private ImageButton       m_ibChangeCategory = null;
    private CustomImageButton m_cibAddPhoto = null;

    private Category          m_pSelectedCategory = null;
    private TextView          m_tvCategory = null;
    private SelectWritingCategoryDialog m_dlgCategory = null;
    private ArrayList<Category> m_arrCategory = new ArrayList<Category>();

    private CameraAndPhotoDialog m_dlgCameraAndPhoto = null;

    private ListView           m_lvImageArray = null;
    private ArrayList<Image>   m_arrFreetakImg = new ArrayList<Image>();

    private boolean isUpload = false;
    private SelectPhotoManager  manager = null;

    private boolean isOpenedKeyboard = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void initContent() {
        m_cibAddPhoto = (CustomImageButton)findViewById(R.id.cib_add_photo);
        m_cibAddPhoto.setOnClickListener(this);
        m_tvCategory = (TextView)findViewById(R.id.tv_category);
        m_ibChangeCategory = (ImageButton)findViewById(R.id.ib_change_category);
        m_ibChangeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rect rect = Utility.getFrameForView(m_tvCategory);
                m_dlgCategory.setPopupRect(rect);
                m_dlgCategory.show();
            }
        });

        m_tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rect rect = Utility.getFrameForView(m_tvCategory);
                m_dlgCategory.setPopupRect(rect);
                m_dlgCategory.show();
            }
        });

        m_etContent = (EditText)findViewById(R.id.et_content);
        m_etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpenedKeyboard == false) {
                    isOpenedKeyboard = true;
                }
            }
        });

        m_etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                Rect rect = Utility.getFrameForView(textView);
                if(rect.height() >= 320) {
                    textView.getLayoutParams().height = 320;
                }
                else {
                    textView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                }

                return false;
            }
        });

        m_nivUserPhoto = (CircledNetworkImageView)findViewById(R.id.iv_user_photo);

        User user = AppController.getInstance().getCurrentUser();

        if(user.getUserImage() != null) {
            m_nivUserPhoto.setImageUrl(user.getUserImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }
        else {
            m_nivUserPhoto.setDefaultImageResId(R.drawable.bg_person_default);
        }
    }

    private void initImageList() {
        m_lvImageArray = (ListView)findViewById(R.id.lv_content);
        m_lvImageArray.setAdapter(new ArrayAdapter<Image>(this, R.layout.item_row_write_freetalk_image, m_arrFreetakImg) {
            class ViewHolder {
                private ImageView iv_image;
                private ImageButton ib_cancel;
            }

            @Override
            public int getCount() {
                return m_arrFreetakImg.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Image image = m_arrFreetakImg.get(position);

                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_write_freetalk_image, null);
                    holder = new ViewHolder();
                    holder.ib_cancel = (ImageButton) convertView.findViewById(R.id.ib_cancel);
                    holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_photo);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.iv_image.setImageBitmap(image.getBitmap());
                holder.ib_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        m_arrFreetakImg.remove(image);

                        if(m_arrFreetakImg.size() == 0) {
                            m_lvImageArray.setVisibility(View.GONE);
                        }
                        else {
                            showImageListView();
                        }
                    }
                });

                convertView.setOnClickListener(m_onClickForKeyboard);

                return convertView;
            }
        });

        m_lvImageArray.setVisibility(View.GONE);
    }

    private void initActions() {
        ImageButton btnCancel = (ImageButton)findViewById(R.id.ib_back);
        btnCancel.setOnClickListener(this);

        if(false) {
            ImageButton btnComplete = (ImageButton) findViewById(R.id.ib_complete);
            btnComplete.setOnClickListener(this);
        }

        Button btnComplete = (Button) findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(this);
    }


    private void init(){

        initContent();
        initImageList();
        initActions();

        m_dlgCategory = new SelectWritingCategoryDialog(this);
        requestCategoryList();

        initKeyboardTouch();
    }

    private View.OnClickListener m_onClickForKeyboard = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpenedKeyboard == false) {
                Utility.showSoftKeyboard(WritingActivity.this, m_etContent);
                isOpenedKeyboard = true;
            } else {
                Utility.hideSoftKeyboard(WritingActivity.this);
                isOpenedKeyboard = false;
            }
        }
    };

    private View rootLayout = null;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            if(heightDiff <= contentViewTop){
                isOpenedKeyboard = false;
            } else {
                isOpenedKeyboard = true;
            }
        }
    };

    private void initKeyboardTouch() {
        final View view1 = findViewById(R.id.content);
        view1.setOnClickListener(m_onClickForKeyboard);
        final View view2 = findViewById(R.id.ll_bottom);
        view2.setOnClickListener(m_onClickForKeyboard);

        rootLayout = findViewById(R.id.root);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
    }


    public void setCategory(Category category) {
        m_pSelectedCategory = category;
        m_tvCategory.setHint("");
        m_tvCategory.setText(category.getName());
    }

    /*
        action Events
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_back:
                finish();
                Utility.hideSoftKeyboard(WritingActivity.this);
                break;
            case R.id.btn_complete:
            case R.id.ib_complete:
                Utility.hideSoftKeyboard(WritingActivity.this);

                if(m_arrFreetakImg.size() > 0) {
                    uploadAllImage();
                }
                else {
                    requestWritingFreeTalk();
                }
                break;
            case R.id.cib_add_photo:
                manager = new SelectPhotoManager(this);
                manager.doPickFromGallery();
                break;
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private void showImageListView() {
        m_lvImageArray.setVisibility(View.VISIBLE);
        ((ArrayAdapter<Image>)m_lvImageArray.getAdapter()).notifyDataSetChanged();

        int contentHeight = 0;
        int size = m_arrFreetakImg.size();
        for(int i = 0;i < size; i++) {
            Image image = m_arrFreetakImg.get(i);
            contentHeight += image.getBitmap().getHeight();
        }
        final View view2 = findViewById(R.id.ll_bottom);
        int lvHeight = (int) (view2.getY() - m_lvImageArray.getY());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)m_lvImageArray.getLayoutParams();
        if(contentHeight > lvHeight) {
            params.addRule(RelativeLayout.ABOVE, R.id.ll_bottom);
        }
        else {
            params.addRule(RelativeLayout.ABOVE, 0);
        }

        m_lvImageArray.setLayoutParams(params);
        m_lvImageArray.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK) {
            if (requestCode == SelectPhotoManager.PICK_FROM_FILE) {

                try {

                    Uri imageUri = data.getData();
                    String name_Str = manager.getCacheFileName();
                    isUpload = false;
                    Bitmap image_bitmap = manager.getCachePhotoFromGallary(imageUri, name_Str);
                    if(image_bitmap != null) {
                        Image image = new Image();
                        File uploadFile = new File(name_Str);
                        image.setFile(uploadFile);
                        image.setBitmap(image_bitmap);
                        m_arrFreetakImg.add(image);

                        showImageListView();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //user is returning from cropping the image
            else if (requestCode == PIC_CROP) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                //retrieve a reference to the ImageView
                ImageView picView = (ImageView) findViewById(R.id.iv_photo);
                //display the returned cropped image
                picView.setImageBitmap(thePic);
            }
        }
    }

    private void showCameraAndPhotoDlg(String filename) {
        m_dlgCameraAndPhoto = new CameraAndPhotoDialog(this);
        m_dlgCameraAndPhoto.setTitle("자유톡이미지 등록");
        m_dlgCameraAndPhoto.setExplain("자유톡이미지를 등록해주세요.");
        m_dlgCameraAndPhoto.setCaptureFileName(filename);
        m_dlgCameraAndPhoto.show();
    }

    private void uploadAllImage() {
        int size = m_arrFreetakImg.size();
        isUpload = true;
        for(int i = 0; i < size;i++) {
            Image image =  m_arrFreetakImg.get(i);
            if(image.getFile() != null) {
                uploadFile(image.getFile(), i);
                isUpload = false;
                return;
            }
        }

        if(isUpload == true) {
            requestWritingFreeTalk();
        }
    }

    private void uploadFile(File file, final int req) {
        Utility.showWaitingDlg(this);
        ServerAPICall.getInstance().uploadFile(ServerAPIPath.API_POST_UPLOAD_FREETALK_IMAGE, "img_filename", file,
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
                                Toast.makeText(WritingActivity.this, msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject dataObject = response.getJSONObject("result_data");
                            int uploadedImageID = dataObject.getInt("id");

                            if(uploadedImageID > 1){
                                Image image = m_arrFreetakImg.get(req);
                                image.setId(uploadedImageID);
                                image.setFile(null);

                                uploadAllImage();
                            }
                            else {
                                throw new JSONException("실패");
                            }

                        } catch (JSONException e) {
                            Logger.e(TAG, "uploadFile - JSONException : " + e.getMessage());
                            Toast.makeText(WritingActivity.this, ResponseMessage.ERR_UPLOAD_IMAGE, Toast.LENGTH_SHORT).show();
                        }
                        Utility.hideWaitingDlg();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        Logger.d(TAG, "uploadFile - VolleyError : ");

                        Toast.makeText(WritingActivity.this, ResponseMessage.ERR_UPLOAD_IMAGE, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }


    private void requestWritingFreeTalk() {

        if(m_etContent.getText() == null || m_etContent.getText().toString().equals("")) {
            Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_WRITE_FREETALK;
        Map<String, String> params = new HashMap<String, String>();

        params.put("content", m_etContent.getText().toString());

        if(m_pSelectedCategory != null) {
            params.put("category_id", String.format("%d", m_pSelectedCategory.getId()));
        }

        if(m_arrFreetakImg.size() > 0) {
            JSONArray array = new JSONArray();

            int size = m_arrFreetakImg.size();

            for(int i = 0 ; i < size; i++) {
                array.put(m_arrFreetakImg.get(i).getId());
            }

            params.put("img_id_array",  array.toString());
        }

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        setResult(RESULT_OK);
                        finish();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        isUpload = false;
                        Toast.makeText(WritingActivity.this, s, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WritingActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void responseCategoryList(Object json){

        try {

            m_arrCategory.clear();
            JSONArray dataArray = (JSONArray)json;

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                Category category = new Category(jsonObject);

                if(category.isTalkCategory() == true) {
                    if(category.getName().compareTo("잡담") == 0) {
                        setCategory(category);
                    }
                    m_arrCategory.add(category);
                }
            }
        } catch (Exception e) {
            Toast.makeText(WritingActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseCategoryList - JSONException : " + e.getMessage());
        }
    }
}

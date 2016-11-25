package mimishop.yanji.com.mimishop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.util.SelectPhotoManager;
import mimishop.yanji.com.mimishop.util.Utility;

public class CameraAndPhotoDialog extends Dialog implements View.OnClickListener {

    public static final int         REQ_CODE_GALLARY_IMAGE = SelectPhotoManager.PICK_FROM_FILE;
    public static final int         REQ_CODE_CAPTURE_IMAGE = SelectPhotoManager.PICK_FROM_CAMERA;


    private Context mContext;
    private android.support.v4.app.Fragment mFrag = null;

    private Button mBtnTakePhoto;
    private Button mBtnShowGallary;
    private ImageButton mBtnClose;
    private TextView mTitle;
    private TextView mExplain;

    private String m_strTitle = null;
    private String m_strExplain = null;
    public static String strCapturePath = null;
    private String m_strCaptureFileName = null;
    private String m_strGallaryFileName = null;

    private boolean m_isFragmentContext = false;
    private SelectPhotoManager manager = null;

    public CameraAndPhotoDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        manager = new SelectPhotoManager((Activity)context);
    }

    public CameraAndPhotoDialog(Context context, android.support.v4.app.Fragment fragment) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        mFrag = fragment;
        manager = new SelectPhotoManager(mFrag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_register_identification);

        mBtnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        mBtnTakePhoto.setOnClickListener(this);
        mBtnShowGallary = (Button) findViewById(R.id.btn_show_gallary);
        mBtnShowGallary.setOnClickListener(this);

        mBtnClose = (ImageButton) findViewById(R.id.ib_exit);
        mBtnClose.setOnClickListener(this);

        mTitle = (TextView)findViewById(R.id.tv_dlg_title);

        if(m_strTitle != null) {
            mTitle.setText(m_strTitle);
        }

        mExplain = (TextView)findViewById(R.id.tv_dlg_explain);

        if(m_strExplain != null) {
            mExplain.setText(m_strExplain);
        }

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
        switch (view.getId()) {
            case R.id.btn_take_photo:
                String fileName = m_strCaptureFileName;

                if (m_strCaptureFileName == null) {
                    fileName =  "temp.jpg";
                }

                if(mFrag == null) {
                    strCapturePath = manager.startCaptureActivity((Activity) mContext, REQ_CODE_CAPTURE_IMAGE, fileName);
                }
                else {
                    strCapturePath = manager.startCaptureActivity(mFrag, REQ_CODE_CAPTURE_IMAGE, fileName);
                }
                onDestroy();
                break;
            case R.id.btn_show_gallary:
                if(mFrag == null) {
                    manager.startGallaryActivity((Activity)mContext, REQ_CODE_GALLARY_IMAGE);
                }
                else {
                    manager.startGallaryActivity(mFrag, REQ_CODE_GALLARY_IMAGE);
                }
                onDestroy();
                break;
            case R.id.ib_exit:
                onDestroy();
                break;
        }
    }

    private void onDestroy() {
        dismiss();
    }

    public void setTitle(String title) {
        m_strTitle = title;
    }

    public void setExplain(String explain) {
        m_strExplain = explain;
    }

    public void setCaptureFileName(String path) {
        m_strCaptureFileName = path;
    }

    public String getCaptureFileName() {
        return m_strCaptureFileName;
    }

    public String getCapturePath() {
        return strCapturePath;
    }
    public void setCapturePath(String path) {
        strCapturePath = path;
    }

    public String getGallaryPath() {
        return m_strGallaryFileName;
    }

    public Bitmap getPhotoFromUri(Uri uri) {
        m_strGallaryFileName = manager.getCacheFileName();
        return manager.getCachePhotoFromGallary(uri, m_strGallaryFileName);
    }

    public Bitmap getPhotoFromPath(String path) {
        return manager.getPhotoFromCamera(path);
    }
}

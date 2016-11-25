package mimishop.yanji.com.mimishop.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.APIErrorResult;
import com.kakao.KakaoStoryHttpResponseHandler;
import com.kakao.KakaoStoryLinkInfo;
import com.kakao.KakaoStoryService;
import com.kakao.MyStoryInfo;
import com.kakao.NoteKakaoStoryPostParamBuilder;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.exception.KakaoException;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.activity.mypage.MyEnvelopeActivity;
import mimishop.yanji.com.mimishop.activity.mypage.MyJimActivity;
import mimishop.yanji.com.mimishop.activity.mypage.MyQuestionActivity;
import mimishop.yanji.com.mimishop.activity.mypage.MySettingActivity;
import mimishop.yanji.com.mimishop.activity.mypage.MyShopManageActivity;
import mimishop.yanji.com.mimishop.activity.mypage.NoticeActivity;
import mimishop.yanji.com.mimishop.activity.mypage.RegisterShopActivity;
import mimishop.yanji.com.mimishop.activity.mypage.ShowMyNewsActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.dialog.CameraAndPhotoDialog;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;
import mimishop.yanji.com.mimishop.view.CustomImageButton;

import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

import mimishop.yanji.com.mimishop.util.StoryLink;


//import com.facebook.widget.FacebookDialog;

/**
 * Created by KCJ on 3/23/2015.
 */
public class MyPageFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int REQ_CODE_REGISTER_SHOP = 0;

    public static final String TAG = "MyPageFragment";

    private View              m_vgMain = null;
    private CircledNetworkImageView m_ivUserImage = null;
    private TextView          m_tvNickName = null;
    private TextView          m_tvLevel = null;
    private TextView          m_tvPoint = null;
    private Button            m_btnUserPrivacy = null;

    private User              m_clsUserInfo = null;
    private Shop              m_clsMyShop = null;

    private File              m_pUserPhotoFile = null;
    private String            m_strCapturePath = "user.jpg";
    private int               m_nUploadeUserPhotoID = 0;
    private boolean           m_isUploaded = false;
    private View m_vMain = null;
    private static MyPageFragment    m_pFrag = null;

    private TextView m_tvNewInform;
    private TextView m_tvNewMessage;
    private TextView m_tvNewEvent;

    private static CameraAndPhotoDialog m_dlgCameraAndPhoto = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        m_vgMain = view;
        m_pFrag = this;

        m_vMain = view.findViewById(R.id.content);
        m_dlgCameraAndPhoto = new CameraAndPhotoDialog(getActivity(), this);
        m_ivUserImage = (CircledNetworkImageView) view.findViewById(R.id.niv_user);
        m_ivUserImage.setDefaultImageResId(R.drawable.bg_person_default);
        m_ivUserImage.setOnClickListener(this);

        m_tvNickName = (TextView) view.findViewById(R.id.tv_nickname);
        m_tvLevel = (TextView) view.findViewById(R.id.tv_level);
        m_tvPoint = (TextView) view.findViewById(R.id.tv_point);

        m_tvNewInform = (TextView) view.findViewById(R.id.tv_notification_inform);
        m_tvNewMessage = (TextView) view.findViewById(R.id.tv_notification_message);
        m_tvNewEvent = (TextView) view.findViewById(R.id.tv_notification_event);

        ImageButton btnFacebook = (ImageButton) view.findViewById(R.id.ib_facebook);
        btnFacebook.setOnClickListener(this);
        ImageButton btnKakao = (ImageButton) view.findViewById(R.id.ib_kakao);
        btnKakao.setOnClickListener(this);
        ImageButton btnKakaoStory = (ImageButton) view.findViewById(R.id.ib_kakaostory);
        btnKakaoStory.setOnClickListener(this);
        ImageButton btnTakePhoto = (ImageButton) view.findViewById(R.id.ib_take_photo);
        btnTakePhoto.setOnClickListener(this);

        CustomImageButton btnMyJim = (CustomImageButton) view.findViewById(R.id.btn_my_jim);
        btnMyJim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startMyJimActivity();
            }
        });

        CustomImageButton btnWriting = (CustomImageButton) view.findViewById(R.id.btn_see_my_news);
        btnWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShowMyNewsActivity();
            }
        });

        CustomImageButton btnComment = (CustomImageButton) view.findViewById(R.id.btn_setting);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMySettingActivity();
            }
        });

        CustomImageButton btnPrivacy = (CustomImageButton)view.findViewById(R.id.btn_my_privacy);
        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_clsUserInfo.getUserPrivacyURL() == null || URLUtil.isValidUrl(m_clsUserInfo.getUserPrivacyURL()) == false) {
                    Toast.makeText(getActivity(), "등록된 공지사항이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                startNoticeActivity();
            }
        });

        CustomImageButton btnQuestion = (CustomImageButton) view.findViewById(R.id.btn_question_to_admin);
        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyQuestionActivity();
            }
        });

        CustomImageButton btnWebtun = (CustomImageButton) view.findViewById(R.id.btn_show_evelop);
        btnWebtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyEnvelopActivity();
                return;
            }
        });

        CustomImageButton btnRegisterStore  = (CustomImageButton)view.findViewById(R.id.btn_register_shop);
        btnRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true) {
                    Toast.makeText(getActivity(),"준비중입니다. 제휴문의를 이용해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(m_clsMyShop == null) {
                    startRegisterStoreAndProductActivity();
                }
                else {
                    if(m_clsMyShop.getShopStatus() == Shop.SHOP_WAIT) {
                        Toast.makeText(getActivity(), "대기중입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "이미 상점을 등록하셨습니다. 상점관리를 해주십시오.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        CustomImageButton btnManageShop = (CustomImageButton)view.findViewById(R.id.btn_manage_shop);
        btnManageShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true) {
                    Toast.makeText(getActivity(),"준비중입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(m_clsMyShop == null) {
                    Toast.makeText(getActivity(), "상점을 등록하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(m_clsMyShop.getShopStatus() == Shop.SHOP_WAIT) {
                    Toast.makeText(getActivity(), "대기중입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                startMyShopManage();
            }
        });

        initMenu(view);

        // blue screen
        if(AppController.getInstance().getCurrentUser() == null) {
            requestInit();
        }
        else {
            init();
        }

        return view;
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

                    }
                });
    }

    private void responseInit(Object s) {
        if (s == null) {

            return;
        }

        try {
            JSONObject object = (JSONObject) s;
            User user = new User(object);
            App app = new App(object);

            AppController.getInstance().setCurrentUser(user);
            AppController.getInstance().setApp(app);

            Common.mKey = user.getUserKey();

            init();

        } catch (Exception e) {

        }
    }

    private void init(){
        requestUserInfo();
        requestMyShop();
    }

    private void initMenu(View mainView) {
        View view = mainView.findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText("마이페이지");
    }

    public static MyPageFragment getInstance() {
        return m_pFrag;
    }

    public User getMe() {return m_clsUserInfo;}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_facebook:
                shareFacebook();
                break;
            case R.id.ib_kakao:
                shareKakao();
                break;
            case R.id.ib_kakaostory:
                shareKakaoStory();
                break;
            case R.id.niv_user:
            case R.id.ib_take_photo:
                showCameraAndPhotoDlg(m_strCapturePath);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == CameraAndPhotoDialog.REQ_CODE_GALLARY_IMAGE || requestCode == CameraAndPhotoDialog.REQ_CODE_CAPTURE_IMAGE ) {

                Uri u = null;
                try {
                    String path = "";
                    Bitmap bitmap = null;
                    if(requestCode == CameraAndPhotoDialog.REQ_CODE_GALLARY_IMAGE) {
                        bitmap = m_dlgCameraAndPhoto.getPhotoFromUri(intent.getData());
                        path = m_dlgCameraAndPhoto.getGallaryPath();
                    }
                    else {
                        path = m_dlgCameraAndPhoto.getCapturePath();
                        bitmap = m_dlgCameraAndPhoto.getPhotoFromPath(path);
                    }

                    m_pUserPhotoFile = new File(path);
                    uploadUserImageFile(m_pUserPhotoFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == REQ_CODE_REGISTER_SHOP) {
                requestMyShop();
            }
        }
    }

    private void showCameraAndPhotoDlg(String filename) {
        m_dlgCameraAndPhoto = new CameraAndPhotoDialog(getActivity(), this);
        if(m_clsUserInfo.getUserImage() == null) {
            m_dlgCameraAndPhoto.setTitle("유저사진 등록");
            m_dlgCameraAndPhoto.setExplain("유저사진을 등록해주세요.");
        }
        else {
            m_dlgCameraAndPhoto.setTitle("유저사진 변경");
            m_dlgCameraAndPhoto.setExplain("유저사진을 변경해주세요.");
        }
        m_dlgCameraAndPhoto.setCaptureFileName(filename);
        m_dlgCameraAndPhoto.show();
    }

//    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
//        @Override
//        public void onError(FacebookDialog.PendingCall pendingCall,
//                            Exception error, Bundle data) {
//            Toast.makeText(getActivity().getApplicationContext(), "친구초대에 실패하였습니다.",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onComplete(FacebookDialog.PendingCall pendingCall,
//                               Bundle data) {
//            Toast.makeText(getActivity().getApplicationContext(), "친구초대 요청을 보냈습니다.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    };


    private void shareFacebook(){
//        String message = ResponseMessage.MSG_SHARE;
//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentTitle("앱으로 연결")
//                    .setContentDescription(message)
//                    .setContentUrl(Uri.parse(ServerAPIPath.APP_PAGE_URL))
//                            //.setContentUrl(Uri.parse("https://fb.me/1625189447735586"))
//                    .build();
//            ((MainActivity)getActivity()). m_shareDialog.show(linkContent);
//        }

        try {
            String message = ResponseMessage.MSG_SHARE;
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message+"\n"+ServerAPIPath.APP_PAGE_URL);
            PackageManager pm = getActivity().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

            boolean isExist = false;
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    startActivity(shareIntent);
                    isExist = true;
                    inviteFriend("f");
                    break;
                }
            }

            if(isExist == false) {
                Toast.makeText(getActivity(), "Facebook앱을 설치하세요.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void shareKakao() {
        try {
            String message = ResponseMessage.MSG_SHARE;

            KakaoLink kakaoLink = KakaoLink.getKakaoLink(getActivity());
            KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink
                    .createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText(message);
            kakaoTalkLinkMessageBuilder.addAppButton("앱으로 연결");
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), getActivity());
            inviteFriend("k");
        } catch (KakaoParameterException e) {
            Toast.makeText(getActivity(), e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void shareKakaoStory() {

        String message = ResponseMessage.MSG_SHARE;
        Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
        urlInfoAndroid.put("title", getResources().getString(R.string.app_name));
        urlInfoAndroid.put("desc", message);
        urlInfoAndroid.put("type", "advertise");
        urlInfoAndroid.put("installurl", "market://details?id=mimishop.yanji.com.mimishop");
        urlInfoAndroid.put("executeurl", "mimishop://starActivity");

       // Recommended: Use application context for parameter.
        StoryLink  storyLink = StoryLink.getLink(getActivity().getApplicationContext());

        // check, intent is available.
        if(!storyLink.isAvailableIntent()) {
            Toast.makeText(getActivity(), "KakaoStory앱을 설치하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * @param activity
         * @param url
         * @param message
         * @param appId
         * @param appVer
         * @param appName
         * @param encoding
         */
        try {
            storyLink.openKakaoLink(getActivity(),
                    message +"\n"+ServerAPIPath.APP_PAGE_URL,
                    getActivity().getPackageName(),
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName,
                    "앱으로 연결",
                    "UTF-8",
                    urlInfoAndroid);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getLocalizedMessage());
            e.printStackTrace();

        }


//        try {
//            String message = ResponseMessage.MSG_SHARE;
//            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, message);
//            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
//            PackageManager pm = getActivity().getPackageManager();
//            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
//
//            boolean isExist = false;
//            for (final ResolveInfo app : activityList) {
//                if ((app.activityInfo.name).contains("KakaoStory")) {
//                    final ActivityInfo activity = app.activityInfo;
//                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
//                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    shareIntent.setComponent(name);
//                    startActivity(shareIntent);
//                    isExist = true;
//                    inviteFriend("k");
//                    break;
//                }
//            }
//
//            if(isExist == false) {
//                Toast.makeText(getActivity(), "KakaoStory앱을 설치하세요.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//        catch (Exception e) {
//            Toast.makeText(getActivity(), e.toString(),
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    private void refreshUI() {
        if(m_clsUserInfo == null) {
            return;
        }

        if(m_clsUserInfo.getUserImage() != null) {
            m_ivUserImage.setImageUrl(m_clsUserInfo.getUserImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }
        else {
            m_ivUserImage.setImageResource(R.drawable.bg_person_default);
        }

        m_tvNickName.setText(m_clsUserInfo.getUserID());
        m_tvLevel.setText(String.format("%d", m_clsUserInfo.getLevel()));
        m_tvPoint.setText(String.format("%d", m_clsUserInfo.getPoint()));

        if(m_clsUserInfo.isUserNewMessage() == true) {
            m_tvNewMessage.setVisibility(View.VISIBLE);
        }
        else {
            m_tvNewMessage.setVisibility(View.GONE);
        }
        if(m_clsUserInfo.isUserNewEvent() == true) {
            m_tvNewEvent.setVisibility(View.VISIBLE);
        }
        else {
            m_tvNewEvent.setVisibility(View.GONE);
        }
        if(m_clsUserInfo.isUserNewInform() == true) {
            m_tvNewInform.setVisibility(View.VISIBLE);
        }
        else {
            m_tvNewInform.setVisibility(View.GONE);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void startRegisterStoreAndProductActivity() {
        Intent intent = new Intent(getActivity(), RegisterShopActivity.class);

        startActivityForResult(intent, REQ_CODE_REGISTER_SHOP);
    }

    private void startMyJimActivity() {
        if(m_clsUserInfo.getMyShops() == null || m_clsUserInfo.getMyShops().size() == 0) {
            Toast.makeText(getActivity(), "내찜이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), MyJimActivity.class);
        startActivity(intent);
    }

    private void startShowMyNewsActivity() {
        if(true) {
            Toast.makeText(getActivity(),"준비중입니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(m_clsUserInfo.isUserNewInform() == true) {
            AppController.getInstance().getCurrentUser().setUserNewInform(false);
            MainActivity.getInstance().requestUpdateAccount();
            m_clsUserInfo.setUserNewInform(false);
            refreshUI();
        }

        Intent intent = new Intent(getActivity(), ShowMyNewsActivity.class);

        startActivity(intent);
    }


    private void startMyShopManage(){
        Intent intent = new Intent(getActivity(), MyShopManageActivity.class);

        startActivity(intent);
    }

    private void startMyQuestionActivity() {
        Intent intent = new Intent(getActivity(), MyQuestionActivity.class);

        startActivity(intent);
    }

    private void startNoticeActivity() {
        if(m_clsUserInfo.isUserNewEvent() == true) {
            AppController.getInstance().getCurrentUser().setUserNewEvent(false);
            MainActivity.getInstance().requestUpdateAccount();
            m_clsUserInfo.setUserNewEvent(false);
            refreshUI();
        }

        Intent intent = new Intent(getActivity(), NoticeActivity.class);

        startActivity(intent);
    }

    private void startMyEnvelopActivity() {
        if(m_clsUserInfo.isUserNewMessage() == true) {
            AppController.getInstance().getCurrentUser().setUserNewMessage(false);
            MainActivity.getInstance().requestUpdateAccount();
            m_clsUserInfo.setUserNewMessage(false);
            refreshUI();
        }

        Intent intent = new Intent(getActivity(), MyEnvelopeActivity.class);

        startActivity(intent);
    }

    private void startMySettingActivity() {
        Intent intent = new Intent(getActivity(), MySettingActivity.class);

        startActivity(intent);
    }

    private void requestUserInfo() {
        Utility.showWaitingDlg(getActivity());
        String url = ServerAPIPath.API_GET_ME;
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject dataObject = (JSONObject)o;
                            User user = new User(dataObject);
                            AppController.getInstance().setCurrentUser(user);
                            m_vMain.setVisibility(View.VISIBLE);
                            m_clsUserInfo = user;

                            refreshUI();
                        }
                        catch (Exception e) {
                            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }

                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    public void requestMyShop() {
        String url = ServerAPIPath.API_GET_MY_SHOP;
        Utility.showWaitingDlg(getActivity());
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            if(o == null) {
                            	 Utility.hideWaitingDlg();
                                return;
                            }

                            JSONObject dataObject = (JSONObject) o;
                            m_clsMyShop = new Shop(dataObject);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                }
        );
    }

    // kind: 'k':kakao 'f':facebook
    public void inviteFriend(String kind) {
        String url = ServerAPIPath.API_POST_INVITE_FRIEND;
        Map<String, String> params = new HashMap<String, String>();

        params.put("kind", kind);

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), ResponseMessage.ERR_LIKE_DISABLE, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), ResponseMessage.ERR_NO_NETWORK, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }

    private void requestCreateAccount() {

        if(m_nUploadeUserPhotoID <= 0) {
            Toast.makeText(getActivity(), "사용자사진을 캡쳐해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ServerAPIPath.API_POST_UPDATE_ACCOUNT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("userImgID", String.format("%d", m_nUploadeUserPhotoID));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject dataObject = (JSONObject) o;
                            User user = new User(dataObject);

                            AppController.getInstance().setCurrentUser(user);

                            m_clsUserInfo = user;
                            refreshUI();

                            Toast.makeText(getActivity(), ResponseMessage.SUCCESS_REGISTER_PHOTO, Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e ) {
                            Toast.makeText(getActivity(), ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                }
                ,params
        );
    }

    private void uploadUserImageFile(File file) {
        String url = ServerAPIPath.API_POST_UPLOAD_USER_IMAGE;
        Utility.showWaitingDlg(getActivity());
        ServerAPICall.getInstance().uploadFile(url, "img_filename", file,
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
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject dataObject = response.getJSONObject("result_data");
                            m_nUploadeUserPhotoID = dataObject.getInt("id");

                            if(m_nUploadeUserPhotoID  > 1){
                                m_isUploaded = true;
                                m_pUserPhotoFile= null;
                                requestCreateAccount();
                            }
                            else {
                                throw new JSONException("실패");
                            }


                        } catch (JSONException e) {
                            Logger.e(TAG, "uploadFile - JSONException : " + e.getMessage());
                            Toast.makeText(getActivity(), "이미지업로드 실패.", Toast.LENGTH_SHORT).show();
                        }
                        Utility.hideWaitingDlg();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        Logger.e(TAG, "uploadFile - VolleyError : "+responseString);

                        Toast.makeText(getActivity(), "이미지업로드 실패.", Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }
}

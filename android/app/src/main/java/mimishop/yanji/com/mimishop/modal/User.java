package mimishop.yanji.com.mimishop.modal;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.util.Logger;

/**
 * Created by KCJ on 3/25/2015.
 */
public class User extends BaseModal {

    public static String TAG = "User";
    public static final int  STATUS_DELETE = -1;
    public static final int  STATUS_NORMAL = 0;
    public static final int  STATUS_LIMIT = 1;
    public static final int  STATUS_STOP = 2;
    public static final int  STATUS_LIMIT_STOP = 3;

    private Image userImage;
    private int userLevel;
    private int userPoint;
    private String userID;
    private String userNickname;
    private String userKey;
    private String userEmail;
    private String userSessionStatus;
    private boolean userNewEvent;
    private boolean userNewInform;
    private boolean userNewMessage;
    private String userDeviceID;
    private String userDeviceModel;
    private String userOsType;
    private String userOsVersion;
    private String userAppVersion;
    private String userQuestion;

    private ArrayList<Shop> userJimList;
    private String userWebtun1;
    private String userWebtun2;
    private String userPrivacyURL;
    private long   userPrivacyTime;
    private int     userStatus;

    public User() {

        super();
    }

    public User(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static User create(JSONObject jsonObject){
        return new User(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {

            try {
                userKey = jsonObject.getString("userKey");
                userDeviceID = jsonObject.getString("userDeviceID");
                userDeviceModel = jsonObject.getString("userDeviceModel");
                userOsType = jsonObject.getString("userOsType");
                userOsVersion = jsonObject.getString("userOsVersion");
                userAppVersion = jsonObject.getString("userAppVersion");
                userEmail = jsonObject.getString("userEmail");
                userSessionStatus = jsonObject.getString("userSessionStatus");

                String newMessage = jsonObject.getString("userNewMessage");
                if(newMessage.equals("N") == true) {
                    userNewMessage = false;
                }
                else {
                    userNewMessage = true;
                }

                newMessage = jsonObject.getString("userNewEvent");
                if(newMessage.equals("N") == true) {
                    userNewEvent = false;
                }
                else {
                    userNewEvent = true;
                }

                newMessage = jsonObject.getString("userNewInform");
                if(newMessage.equals("N") == true) {
                    userNewInform = false;
                }
                else {
                    userNewInform = true;
                }

            }catch (JSONException e) {

            }

            userID = jsonObject.getString("userID");
            userLevel = jsonObject.getInt("userLevel");
            String userImgURL = jsonObject.getString("userImgURL");
            if(userImgURL != null && userImgURL.equals("null") == false) {
                userImage = new Image();
                userImage.setImgUrl(userImgURL);
                userImage.setId(jsonObject.getInt("userImgID"));
            }

            userPoint = jsonObject.getInt("userPoint");

            userPrivacyURL = jsonObject.getJSONObject("setting").getString("settingAppNoticeURL");
            userStatus = jsonObject.getInt("userStatus");

            try {
                userQuestion = jsonObject.getString("userQuestion");
                if (userQuestion.equals("null") == true) {
                    userQuestion = "";
                }
            }catch(Exception e){
                Logger.e(TAG, "initWithJSON - userQuestion JSONException : " + e.getMessage());
            }

            try {
                JSONArray jsonArray = jsonObject.getJSONArray("userJimList");
                if (jsonArray != null && jsonArray.length() > 0) {

                    userJimList = new ArrayList<Shop>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Shop shop = new Shop(jsonArray.getJSONObject(i));
                        userJimList.add(shop);
                    }
                } else {

                    if (userJimList != null) {
                        userJimList.clear();
                    }

                    userJimList = null;
                }
            }catch(Exception e)
            {
                Logger.e(TAG, "initWithJSON - userJimList JSONException : " + e.getMessage());
            }
            userNickname = jsonObject.getString("userNickname");

        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public void setJsonObject(JSONObject jsonObject) {
        initWithJSON(jsonObject);
    }

    public Image getUserImage(){
        return userImage;
    }

    public void setUserImage(Image image) {
        userImage = image;
    }

    public String getNickName() {
        return userNickname;
    }

    public boolean isUserID() {

        boolean ret = false;

        if(userID == null) {
            ret = true;
        }
        else  if(userID.equals("null") == true) {
            ret = true;
        }
        else  if(userID.equals("") == true) {
            ret = true;
        }

        return ret;
    }

    public void setNickName(String nickName) {
        this.userNickname = nickName;
    }

    public String getUserKey() {return  userKey;}

    public int getLevel() {
        return userLevel;
    }

    public void setLevel(int level) {
        this.userLevel = level;
    }

    public int getPoint() {
        return userPoint;
    }

    public void setPoint(int point) {
        this.userPoint = point;
    }

    public ArrayList<Shop> getMyShops() {
        return userJimList;
    }

    public void setMyShops(ArrayList<Shop> shops) {
        userJimList = shops;
    }

    public String getUserWebtun1(){
        return userWebtun1;
    }

    public void setUserWebtun1(String userWebtun1) {
        this.userWebtun1 = userWebtun1;
    }

    public String getUserWebtun2(){
        return userWebtun2;
    }

    public void setUserWebtun2(String userWebtun2) {
        this.userWebtun2 = userWebtun2;
    }

    public String getUserPrivacyURL() {
        return userPrivacyURL;
    }

    public void setUserPrivacyURL(String url) {
        this.userPrivacyURL = url;
    }

    public long getUserPrivacyTime() {
        return userPrivacyTime;
    }

    public void setUserPrivacyTime(long time) {
        this.userPrivacyTime = time;
    }

    public String getUserQuestion() {
        return userQuestion;
    }

    public void  setUserQuestion(String question) {
        this.userQuestion = question;
    }

    public  String getUserID() {
        return userID;
    }

    public boolean isAutoLoginPossible(Shop shop) {
        // todo
        return false;
    }

    public boolean isHasNewMessage() {
        if(userNewMessage == true || userNewInform == true || userNewEvent == true) {
            return true;
        }
        return false;
    }

    public boolean isUserNewMessage() {
        return userNewMessage;
    }

    public boolean isUserNewEvent() {
        return userNewEvent;
    }

    public boolean isUserNewInform() {
        return userNewInform;
    }

    public void setUserNewMessage(boolean isNewMessage) {
        userNewMessage = isNewMessage;
    }

    public void setUserNewEvent(boolean isNewMessage) {
        userNewEvent = isNewMessage;
    }

    public void setUserNewInform(boolean isNewMessage) {
        userNewInform = isNewMessage;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public boolean isLimitUser() {
        if(userStatus == STATUS_LIMIT || userStatus == STATUS_LIMIT_STOP) {
            return true;
        }

        return false;
    }


}

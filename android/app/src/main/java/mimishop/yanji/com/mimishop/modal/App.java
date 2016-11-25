package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

import mimishop.yanji.com.mimishop.util.Logger;

/**
 * Created by KCJ on 4/6/2015.
 */
public class App extends BaseModal  {

    public static String TAG = "App";

    private String   appMarketName;
    private String   appNoticeType;
    private String   appUserPiURL;
    private String   appUserUseURL;
    private Image    appAdImg;

    public App() {
        super();
    }

    public App(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static Answer create(JSONObject jsonObject){
        return new Answer(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {

            appMarketName = jsonObject.getString("appMarketName");
            appNoticeType = jsonObject.getString("appNoticeType");
            JSONObject setting = jsonObject.getJSONObject("setting");
            appUserPiURL = setting.getString("settingUserPiURL");
            appUserUseURL = setting.getString("settingUserUseURL");

            String appAdImgURL = jsonObject.getString("appAdImgURL");
            if(appAdImgURL != null && appAdImgURL.equals("null") == false) {
                appAdImg = new Image();
                appAdImg.setImgUrl(appAdImgURL);
                appAdImg.setId(jsonObject.getInt("appAdImgID"));
            }

        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public  String getAppMarketName() {
        return appMarketName;
    }

    public void setAppMarketName(String adURL){
        appMarketName = adURL;
    }

    public  String getAppNoticeType() {
        return appNoticeType;
    }

    public void setAppNoticeType(String adURL){
        appNoticeType = adURL;
    }

    public  String getAppUserPiURL() {
        return appUserPiURL;
    }

    public void setAppUserPiURL(String adURL){
        appUserPiURL = adURL;
    }

    public  String getAppUserUseURL() {
        return appUserUseURL;
    }

    public void setAppUserUseURL(String adURL){
        appUserUseURL = adURL;
    }

    public Image getAppAdImg() {
        return appAdImg;
    }

    public void setAppAdImg(Image appAdImg) {
        this.appAdImg = appAdImg;
    }
}

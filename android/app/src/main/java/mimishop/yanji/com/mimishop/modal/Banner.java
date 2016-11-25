package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 10/1/2015.
 */
public class Banner extends BaseModal {

    public static final int BANNER_URL_MODE = 0;
    public static final int BANNER_DETAIL_MODE = 1;

    private String bannerTitle;
    private String bannerStartTime;
    private String bannerEndTime;
    private String bannerContent;
    private int    bannerType;
    private int    bannerDateShow;
    private int    bannerTitleShow;
    private int    bannerContentShow;
    private int    bannerShowMode;
    private Advertise bannerAds;
    private String bannerBackImgURL;
    private int    bannerStatus;

    public Banner() {
        super();
    }

    public Banner(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            bannerTitle = jsonObject.getString("bannerTitle");
            bannerStartTime = jsonObject.getString("bannerStartDate");
            bannerEndTime = jsonObject.getString("bannerEndDate");
            bannerContent = jsonObject.getString("bannerContent");
            bannerType = jsonObject.getInt("bannerType");
            bannerShowMode = jsonObject.getInt("bannerShowMode");
            bannerStatus = jsonObject.getInt("bannerStatus");
            bannerDateShow = jsonObject.getInt("bannerDateShow");
            bannerTitleShow = jsonObject.getInt("bannerTitleShow");
            bannerContentShow = jsonObject.getInt("bannerContentShow");
            bannerAds = new Advertise(jsonObject.getJSONObject("bannerAds"));
            bannerBackImgURL = jsonObject.getString("bannerBackImgURL");
            if(bannerBackImgURL == null || bannerBackImgURL.equals("null") == true) {
                bannerBackImgURL = null;
            }
        } catch (JSONException e) {

        }
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public String getBannerContent() {
        return bannerContent;
    }

    public String getBannerStartTime() {
        return bannerStartTime;
    }

    public String getBannerEndTime() {
        return bannerEndTime;
    }

    public Advertise getBannerAds() {
        return bannerAds;
    }

    public int getBannerShowMode() {
        return bannerShowMode;
    }

    public boolean isShowDate() {
        if(bannerDateShow == 0) {
            return false;
        }
        return true;
    }

    public boolean isShowTitle() {
        if(bannerTitleShow == 0) {
            return false;
        }
        return true;
    }


    public boolean isShowContent() {
        if(bannerContentShow == 0) {
            return false;
        }
        return true;
    }

    public String getBannerImg() {
        if(bannerBackImgURL == null || bannerBackImgURL.isEmpty() == true) {
            return bannerAds.getAdsImgURL();
        }

        return bannerBackImgURL;
    }
}

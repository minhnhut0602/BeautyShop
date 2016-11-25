package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KCJ on 3/27/2015.
 */
public class Cast extends BaseModal {
    private String castTitle;
    private Image castCoveredImg;

    private int castHeartCnt;
    private int castCommentCnt;
    private int castShareCnt;

    private Image castBannerImg;

    private String castPostManNickName;
    private String castPostManImgURL;

    private long   castPostTime;
    private String   castPostTimeString;

    public Cast() {
        super();
    }

    public Cast(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {

        super.initWithJSON(jsonObject);

        try {
            castTitle = jsonObject.getString("castTitle");
            Object appAdImgURL = jsonObject.get("castCoveredImgURL");
            if(appAdImgURL != null && appAdImgURL.toString().equals("null") == false) {
                String url = (String)appAdImgURL;
                castCoveredImg = new Image();
                castCoveredImg.setImgUrl(url);
                castCoveredImg.setId(jsonObject.getInt("castCoveredImgID"));
            }
            else {
                castCoveredImg = null;
            }

            castHeartCnt = jsonObject.getInt("castHeartCnt");
            castShareCnt = jsonObject.getInt("castShareCnt");
            castCommentCnt = jsonObject.getInt("castCommentCnt");
            castPostTime = jsonObject.getLong("castPostTime");
            castPostTimeString = jsonObject.getString("castPostTimeString");

            Object postManImgURL = jsonObject.get("castPostManImgURL");
            if(postManImgURL != null && postManImgURL.toString().equals("null") == false) {
                castPostManImgURL = jsonObject.getString("castPostManImgURL");
            }
            else {
                castPostManImgURL = null;
            }

            castPostManNickName = jsonObject.getString("castPostManNickName");

        } catch (JSONException e) {
            castPostManNickName = null;
        }
    }

    public static Cast create(JSONObject jsonObject){
        return new Cast(jsonObject);
    }

    public String getTitle() {
        return castTitle;
    }

    public void setTitle(String title) {
        this.castTitle = title;
    }

    public Image getCoverImage() {
        return castCoveredImg;
    }

    public void setCoverImage(Image coverImage) {
        this.castCoveredImg = coverImage;
    }

    public int getCastHeartCnt() {
        return castHeartCnt;
    }

    public int getCastCommentCnt() {
        return castCommentCnt;
    }

    public Image getCastBannerImg() {
        return castBannerImg;
    }

    public int getCastShareCnt() {
        return castShareCnt;
    }

    public void setCastShareCnt(int cnt) {
        castShareCnt = cnt;
    }
    public void setCastHeartCnt(int cnt) {
        castHeartCnt = cnt;
    }

    public void setCastCommentCnt(int cnt) {
        castCommentCnt = cnt;
    }

    public String getCastPostManID() {
        return castPostManNickName;
    }

    public String getCastPostManImgURL() {
        return castPostManImgURL;
    }

    public long getCastPostTime() {
        return castPostTime;
    }
    public String getCastPostTimeString() {
        return castPostTimeString;
    }
}


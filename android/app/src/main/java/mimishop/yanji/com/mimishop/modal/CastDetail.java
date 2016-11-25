package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KCJ on 3/28/2015.
 */
public class CastDetail extends BaseModal {
    private String castdetailContent;
    private Image castdetailImg;
    private int castdetailCastID;
    private int castdetailIdx;
    private String castdetailPublish;

    public CastDetail() {
        super();
    }

    public CastDetail(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            castdetailContent = jsonObject.getString("castdetailContent");
            castdetailPublish = jsonObject.getString("castdetailPublish");
            String appAdImgURL = jsonObject.getString("castdetailImgURL");
            if (appAdImgURL != null && appAdImgURL.equals("null") == false) {
                castdetailImg = new Image();
                castdetailImg.setImgUrl(appAdImgURL);
                castdetailImg.setId(jsonObject.getInt("castdetailImgID"));
            }
            castdetailCastID = jsonObject.getInt("castdetailCastID");
            castdetailIdx = jsonObject.getInt("castdetailIdx");
        }
        catch (JSONException e) {

        }
    }

    public static CastDetail create(JSONObject jsonObject){
        return new CastDetail(jsonObject);
    }

    public String getTitle() {
        return castdetailContent;
    }

    public void setTitle(String title) {
        this.castdetailContent = title;
    }

    public Image getCoverImage() {
        return castdetailImg;
    }

    public void setCoverImage(Image coverImage) {
        this.castdetailImg = coverImage;
    }

    public int getCastdetailCastID() {
        return castdetailCastID;
    }

    public int getCastdetailIdx() {
        return castdetailIdx;
    }
    public String getPublish() {
        return castdetailPublish;
    }
}

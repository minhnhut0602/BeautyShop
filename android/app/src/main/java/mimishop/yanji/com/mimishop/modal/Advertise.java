package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimCholJu on 5/14/2015.
 */
public class Advertise extends BaseModal {

    private String adsImgURL;
    private String adsURL;

    public Advertise() {
        super();
    }

    public Advertise(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            adsImgURL = jsonObject.getString("adsImgURL");
            adsURL = jsonObject.getString("adsURL");
        } catch (JSONException e) {

        }
    }

    public String getAdsImgURL() {
        return adsImgURL;
    }

    public String getAdsURL() {
        return adsURL;
    }
}
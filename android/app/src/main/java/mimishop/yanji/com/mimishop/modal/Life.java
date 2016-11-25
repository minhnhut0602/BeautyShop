package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 8/7/2015.
 */
public class Life extends BaseModal {

    // click count
    private int    eventClickCnt;
    private String lifeSubject;
    private String lifeExplain;
    private String lifeImgURL;
    private String lifeURL;

    public Life() {
        super();
    }

    public Life(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            eventClickCnt = jsonObject.getInt("lifeClickCnt");

            lifeSubject = jsonObject.getString("lifeSubject");
            lifeExplain = jsonObject.getString("lifeExplain");

            lifeImgURL = jsonObject.getString("adsImgURL");
            lifeURL = jsonObject.getString("adsURL");
        }
        catch (JSONException e) {

        }
    }

    public String getLifeImgURL(){
        return  lifeImgURL;
    }

    public String getLifeURL() {
        return  lifeURL;
    }

    public int getEventClickCnt() {
        return eventClickCnt;
    }

    public void setEventClickCnt(int cnt) {
        eventClickCnt = cnt;
    }

    public String getSubject() {
        return lifeSubject;
    }

    public String getExplain() {
        return lifeExplain;
    }
}

package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 10/1/2015.
 */
public class BannerComment extends BaseModal {

    public int m_nPostManID;
    public int m_nBannerID;
    public String m_strContent;
    public long m_lPostTime;
    public int  m_nStatus;
    public String m_strPostManID;
    public String m_strPostManPhotoURL;

    public BannerComment() {
        super();
    }

    public BannerComment(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            m_nPostManID = jsonObject.getInt("bannercommentPostManID");
            m_nBannerID = jsonObject.getInt("bannercommentBannerID");
            m_strContent = jsonObject.getString("bannercommentContent");
            m_lPostTime = jsonObject.getLong("bannercommentPostTime");
            m_nStatus = jsonObject.getInt("bannercommentStatus");
            m_strPostManID = jsonObject.getString("userID");
            m_strPostManPhotoURL = jsonObject.getString("userImgURL");
            if(m_strPostManPhotoURL != null && m_strPostManPhotoURL.equals("null") == true) {
                m_strPostManPhotoURL = null;
            }
        } catch (JSONException e) {

        }
    }
}
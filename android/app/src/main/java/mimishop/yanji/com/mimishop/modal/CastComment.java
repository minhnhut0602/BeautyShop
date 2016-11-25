package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

import mimishop.yanji.com.mimishop.util.Logger;

/**
 * Created by KimCholJu on 5/17/2015.
 */
public class CastComment extends BaseModal {

    public static final String TAG = "Comment";

    private User castcommentPostMan;
    private long castcommentPostTime;
    private String castcommentContent;
    private int castcommentHeartCnt;

    public CastComment() {
        super();
    }

    public CastComment(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Comment create(JSONObject jsonObject){
        return new Comment(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            castcommentPostTime = jsonObject.getLong("castcommentPostTime");
            castcommentContent = jsonObject.getString("castcommentContent");
            castcommentHeartCnt = jsonObject.getInt("castcommentHeartCnt");
            castcommentPostMan = new User(jsonObject);

        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public Image getUserImage() {
        return castcommentPostMan.getUserImage();
    }

    public String getName() {
        return castcommentPostMan.getUserID();
    }

    public long getTime() {
        return castcommentPostTime;
    }

    public void setTime(long time) {
        this.castcommentPostTime = time;
    }

    public String getContent() {
        return castcommentContent;
    }

    public void setContent(String content) {
        this.castcommentContent = content;
    }

    public int getCastcommentHeartCnt() {
        return castcommentHeartCnt;
    }
    public void setCastcommentHeartCnt(int cnt) {
        castcommentHeartCnt = cnt;
    }
}

package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimCholJu on 6/17/2015.
 */
public class FreetalkCommentTAG extends BaseModal  {

    public static String TAG = "FreetalkCommentTAG";

    private String freetalkcommentContent;
    private int freetalkcommenttagTAGUserID;
    private String tagPostManUserID;
    private long freetalkcommenttagPostTime;
    private int freetalkcommenttagStatus;
    private String freetalkcommentCateogryName;
    private int freetalkcommentFreetalkID;

    public FreetalkCommentTAG() {
        super();
    }

    public FreetalkCommentTAG(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static Location create(JSONObject jsonObject) {
        return new Location(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            freetalkcommentContent = jsonObject.getString("freetalkcommentContent");
            freetalkcommenttagTAGUserID = jsonObject.getInt("freetalkcommenttagTAGUserID");
            freetalkcommenttagPostTime = jsonObject.getLong("freetalkcommenttagPostTime");
            freetalkcommenttagStatus = jsonObject.getInt("freetalkcommenttagStatus");
            tagPostManUserID = jsonObject.getString("tagPostManUserID");
            freetalkcommentCateogryName = jsonObject.getString("freetalkCategoryName");
            freetalkcommentFreetalkID = jsonObject.getInt("freetalkcommentFreetalkID");
        } catch (JSONException e) {

        }
    }

    public String getFreetalkcommentContent() {
        return  freetalkcommentContent;
    }

    public String getFreetalCommentTAGUserID() {
        return tagPostManUserID;
    }

    public long getFreetalkCommentTAGPostTime() {
        return freetalkcommenttagPostTime;
    }

    public String getFreetalkCateogryName() {
        return freetalkcommentCateogryName;
    }

    public int getFreetalkcommentFreetalkID() {
        return freetalkcommentFreetalkID;
    }

}

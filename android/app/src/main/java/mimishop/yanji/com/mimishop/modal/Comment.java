package mimishop.yanji.com.mimishop.modal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.util.Logger;

/**
 * Created by KCJ on 3/26/2015.
 */
public class Comment extends BaseModal {

    public static final String TAG = "Comment";

    private long commentPostTime;
    private String commentContent;
    private int   commentHeartCnt;
    private String commentCategoryName;
    private String commentFreetalkContent;
    private int commentFreetalkID;
    private int commentPostManID;
    private ArrayList<RedList> redLists;
    private String commentPostManName;
    private String commentPostManImgURL;

    public class RedList {
        public int start;
        public int end;
    }

    public Comment() {
        super();
    }

    public Comment(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Comment create(JSONObject jsonObject){
        return new Comment(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            commentPostTime = jsonObject.getLong("freetalkcommentPostTime");
            commentContent = jsonObject.getString("freetalkcommentContent");
            commentHeartCnt = jsonObject.getInt("freetalkcommentHeartCnt");
            commentPostManName = jsonObject.getString("userID");
            commentCategoryName = jsonObject.getString("freetalkCategoryName");
            commentFreetalkContent = jsonObject.getString("freetalkContent");
            commentFreetalkID = jsonObject.getInt("freetalkcommentFreetalkID");
            commentPostManID = jsonObject.getInt("freetalkcommentPostManID");
            commentPostManImgURL = jsonObject.getString("userImgURL");
            if(commentPostManImgURL != null && commentPostManImgURL.equals("null") == true) {
                commentPostManImgURL = null;
            }

            String jsonObjectString = jsonObject.getString("freetalkcommentRedList");
            JSONArray array = new JSONArray(jsonObjectString);
            redLists = new ArrayList<RedList>();
            int size = array.length();
            for(int i = 0; i < size;i++) {
                JSONObject object = array.getJSONObject(i);
                RedList list = new RedList();
                list.start = object.getInt("start");
                list.end = object.getInt("end");
                redLists.add(list);
            }

        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public String getUserImageURL() {
        return commentPostManImgURL;
    }

    public String getName() {
        return commentPostManName;
    }
    public int getUserID() {
        return commentPostManID;
    }

    public long getTime() {
        return commentPostTime;
    }

    public void setTime(long time) {
        this.commentPostTime = time;
    }

    public String getContent() {
        return commentContent;
    }

    public void setContent(String content) {
        this.commentContent = content;
    }

    public String getCommentFreetalkContent() {
        return commentFreetalkContent;
    }

    public String getCommentCategoryName() {
        return commentCategoryName;
    }

    public int getCommentHeartCnt() {
        return commentHeartCnt;
    }

    public void setCommentHeartCnt(int cnt) {
        commentHeartCnt = cnt;
    }

    public int getCommentFreetalkID() {
        return commentFreetalkID;
    }

    public ArrayList<RedList> getRedLists() {
        return redLists;
    }
}

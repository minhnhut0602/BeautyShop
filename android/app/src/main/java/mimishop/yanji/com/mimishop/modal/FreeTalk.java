package mimishop.yanji.com.mimishop.modal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.util.Logger;

/**
 * Created by KCJ on 3/24/2015.
 */
public class FreeTalk extends BaseModal{

    public static final String TAG = "FreeTalk";

    private long freetalkPostTime;
    private String freetalkContent;
    private Category freetalkCategory;
    private int freetalkHeartCnt;
    private int freetalkCommentCnt;
    private int freetalkReviewCnt;
    private int freetalkDeclareCnt;
    private ArrayList<Image> freetalkImgArray;

    private int    postManID;
    private String postManUserID;
    private String postManUserImgURL;
    private int    postManLevel;

    public FreeTalk() {

        super();
    }

    public FreeTalk(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static FreeTalk create(JSONObject jsonObject){
        return new FreeTalk(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            freetalkImgArray = new ArrayList<Image>();

            freetalkPostTime = jsonObject.getLong("freetalkPostTime");
            freetalkContent = jsonObject.getString("freetalkContent");

            String freetalkCategoryName = jsonObject.getString("freetalkCategoryName");
            freetalkCategory = new Category();
            freetalkCategory.setName(freetalkCategoryName);
            freetalkCategory.setId(jsonObject.getInt("freetalkCategoryID"));

            freetalkHeartCnt = jsonObject.getInt("freetalkHeartCnt");
            freetalkCommentCnt = jsonObject.getInt("freetalkCommentCnt");

            try {
                JSONArray shopImgArray = jsonObject.getJSONArray("freetalkImgArray");
                if (shopImgArray != null) {
                    for (int i = 0; i < shopImgArray.length(); i++) {
                        JSONObject dataObject = shopImgArray.getJSONObject(i);
                        Image image = new Image(dataObject);
                        freetalkImgArray.add(image);
                    }
                }
            }
            catch (Exception e) {

            }
            postManID = jsonObject.getInt("freetalkPostManID");
            postManUserID = jsonObject.getString("userID");
            postManUserImgURL = jsonObject.getString("userImgURL");
            postManLevel = jsonObject.getInt("userLevel");

            if(postManUserImgURL != null && postManUserImgURL.equals("null") == true) {
                postManUserImgURL = null;
            }

            freetalkDeclareCnt = jsonObject.getInt("freetalkDeclareCnt");
            freetalkReviewCnt = jsonObject.getInt("freetalkReviewCnt");
        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public String getUserNickName(){
        return postManUserID;
    }

    public String getContent(){
        return freetalkContent;
    }

    public void setContent(String comment) {
        this.freetalkContent = comment;
    }

    public long getCrtPostTime(){
        return freetalkPostTime;
    }

    public void setCrtPostTime(long crtPostTime){
        this.freetalkPostTime = crtPostTime;
    }

    public String getLevelImageURL(){
        return postManUserImgURL;
    }

    public Category getCategory(){
        return freetalkCategory;
    }

    public void setCategory(Category category){
        this.freetalkCategory = category;
    }

    public int getLevel() {
        return postManLevel;
    }

    public int getLikeCount() {
        return freetalkHeartCnt;
    }

    public void setLikeCount(int likeCount) {
        this.freetalkHeartCnt = likeCount;
    }

    public int getCommentCount() {
        return freetalkCommentCnt;
    }

    public int getDeclareCount() {
        return freetalkDeclareCnt;
    }

    public void setCommentCount(int commentCount) {
        this.freetalkCommentCnt = commentCount;
    }

    public int getVisitorCount() {
        return freetalkReviewCnt;
    }

    public void setVisitorCount(int visitorCount) {
        this.freetalkReviewCnt = visitorCount;
    }

    public ArrayList<Image> getFreetalkImgArray() {
        return freetalkImgArray;
    }

    public int getPostManID() {
        return postManID;
    }
}

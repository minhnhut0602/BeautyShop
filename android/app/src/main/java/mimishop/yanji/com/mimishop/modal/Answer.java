package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 3/26/2015.
 */
public class Answer extends BaseModal  {

    private int shopanswerPostManID;
    private String userNickname;
    private long shopanswerPostTime;
    private String shopanswerContent;
    private int shopanswerShopCommentID;

    public String getName() {
        return userNickname;
    }

    public void setName(String name){
        this.userNickname = name;
    }

    public Answer() {
        super();
    }

    public Answer(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static Answer create(JSONObject jsonObject){
        return new Answer(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);


        try {
            userNickname = jsonObject.getString("userNickname");
            shopanswerContent = jsonObject.getString("shopanswerContent");
            shopanswerPostManID = jsonObject.getInt("shopanswerPostManID");
            shopanswerPostTime = jsonObject.getLong("shopanswerPostTime");
            shopanswerShopCommentID = jsonObject.getInt("shopanswerShopCommentID");
        } catch (JSONException e) {

        }
    }

    public long getTime() {
        return shopanswerPostTime;
    }

    public void setTime(long time) {
        this.shopanswerPostTime = time;
    }

    public String getContent() {
        return shopanswerContent;
    }

    public void setContent(String content) {
        this.shopanswerContent = content;
    }

    public int getShopCommentID() {
        return  shopanswerShopCommentID;
    }
}

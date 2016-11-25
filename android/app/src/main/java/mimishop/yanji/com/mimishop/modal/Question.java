package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 3/26/2015.
 */
public class Question extends BaseModal {
    private int shopcommentPostManID;
    private String userNickname;
    private long shopcommentPostTime;
    private String shopcommentContent;
    private int shopcommentShopID;
    private int shopcommentShopStatus;
    private int shopcommentShopLevel;

    public Question() {
        super();
    }

    public Question(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Question create(JSONObject jsonObject){
        return new Question(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {

        super.initWithJSON(jsonObject);
        try {
            userNickname = jsonObject.getString("userNickname");
            shopcommentPostManID = jsonObject.getInt("shopcommentPostManID");
            shopcommentContent = jsonObject.getString("shopcommentContent");
            shopcommentShopID = jsonObject.getInt("shopcommentShopID");
            shopcommentPostTime = jsonObject.getLong("shopcommentPostTime");
            shopcommentShopStatus = jsonObject.getInt("shopcommentShopStatus");
            shopcommentShopLevel = jsonObject.getInt("shopcommentShopLevel");
        } catch (JSONException e) {

        }
    }

    public String getName() {
        return userNickname;
    }

    public void setName(String name){
        this.userNickname = userNickname;
    }

    public long getTime() {
        return shopcommentPostTime;
    }

    public void setTime(long time) {
        this.shopcommentPostTime = time;
    }

    public String getContent() {
        return shopcommentContent;
    }

    public void setContent(String content) {
        this.shopcommentContent = content;
    }

    public int getShopcommentShopStatus() {
        return this.shopcommentShopStatus;
    }

    public int getShopcommentShopLevel() {
        return shopcommentShopLevel;
    }

    public int getShopcommentShopID() {
        return shopcommentShopID;
    }

    public int getShopcommentPostManID() {
        return shopcommentPostManID;
    }
}


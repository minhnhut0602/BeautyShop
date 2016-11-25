package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimCholJu on 5/10/2015.
 */
public class FreetalkRelation extends BaseModal  {

    private String freetalkContent;
    private int freetalkrelationFreetalkID;
    private int freetalkrelationPostManID;
    private int freetalkrelationTAG;
    private int freetalkrelationDeclare;
    private long freetalkrelationPostTime;
    private int freetalkrelationStatus;
    private String freetalkrelationUserID;
    private String freetalkCateogryName;

    public FreetalkRelation() {
        super();
    }

    public FreetalkRelation(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static Location create(JSONObject jsonObject) {
        return new Location(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            freetalkContent = jsonObject.getString("freetalkContent");
            freetalkrelationFreetalkID = jsonObject.getInt("freetalkrelationFreetalkID");
            freetalkrelationPostManID = jsonObject.getInt("freetalkrelationPostManID");
            freetalkrelationTAG = jsonObject.getInt("freetalkrelationTAG");
            freetalkrelationDeclare = jsonObject.getInt("freetalkrelationDeclare");
            freetalkrelationPostTime = jsonObject.getLong("freetalkrelationPostTime");
            freetalkrelationStatus = jsonObject.getInt("freetalkrelationStatus");
            freetalkrelationUserID = jsonObject.getString("freetalkrelationUserID");
            freetalkCateogryName = jsonObject.getString("freetalkCateogryName");
        } catch (JSONException e) {

        }
    }

    public String getFreetalkContent() {
        return  freetalkContent;
    }

    public String getFreetalkrelationUserID() {
        return freetalkrelationUserID;
    }

    public long getFreetalkrelationPostTime() {
        return freetalkrelationPostTime;
    }

    public String getFreetalkCateogryName() {
        return freetalkCateogryName;
    }

    public int getFreetalkrelationTAG() {
        return freetalkrelationTAG;
    }

    public int getFreetalkrelationDeclare() {
        return freetalkrelationDeclare;
    }
}

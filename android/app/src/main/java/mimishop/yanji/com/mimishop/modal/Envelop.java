package mimishop.yanji.com.mimishop.modal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimCholJu on 5/12/2015.
 */
public class Envelop extends BaseModal {

    private String envelopContent;
    private long envelopPostTime;
    private boolean envelopReaded;
    private boolean originEnvelopReaded;
    private boolean isDeleted = false;
    private boolean isChange = false;

    public Envelop() {
        super();
    }

    public Envelop(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);
        try {
            envelopContent = jsonObject.getString("gcmlogContent");
            envelopPostTime = jsonObject.getLong("gcmlogPostTime");

            String readed = jsonObject.getString("gcmlogReaded");
            if(readed.equals("0")) {
                envelopReaded = false;
            }
            else {
                envelopReaded = true;
            }

            originEnvelopReaded = envelopReaded;
        }
        catch (JSONException e) {

        }
    }

    public String getEnvelopContent() {
        return envelopContent;
    }

    public long getEnvelopPostTime() {
        return envelopPostTime;
    }

    public boolean isEnvelopReaded() {
        return envelopReaded;
    }

    public void setEnvelopReaded(boolean isReaded) {

        if(originEnvelopReaded != isReaded) {
            isChange = true;
        }

        envelopReaded = isReaded;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {

        if(isDeleted == true) {
            isChange = true;
        }
        else {
            isChange = false;
        }

        this.isDeleted = isDeleted;
    }

    public boolean isChanged() {
        return  isChange;
    }
}

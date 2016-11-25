package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by KCJ on 3/24/2015.
 */
public abstract class BaseModal   implements Serializable {

    private int id;

    public BaseModal() {
        init();
    }

    public BaseModal(JSONObject jsonObject) {
        initWithJSON(jsonObject);
    }

    protected   void init() {

    }

    protected void initWithJSON(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
        } catch (JSONException e) {

        }
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

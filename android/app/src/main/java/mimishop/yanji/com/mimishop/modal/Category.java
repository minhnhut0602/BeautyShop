package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 3/24/2015.
 */
public class Category extends BaseModal {
    private String categoryName;

    private boolean isShopCategory;
    private boolean isTalkCategory;
    private boolean isCastCategory;
    private boolean isEventCategory;

    public Category() {

        super();
    }

    public Category(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Category create(JSONObject jsonObject){
        return new Category(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            categoryName = jsonObject.getString("categoryName");

            int val = jsonObject.getInt("categoryShop");
            if(val == 1) {
                isShopCategory = true;
            }
            else {
                isShopCategory = false;
            }

            val = jsonObject.getInt("categoryFreetalk");
            if(val == 1) {
                isTalkCategory = true;
            }
            else {
                isTalkCategory = false;
            }

            val = jsonObject.getInt("categoryCast");
            if(val == 1) {
                isCastCategory = true;
            }
            else {
                isCastCategory = false;
            }

            val = jsonObject.getInt("categoryEvent");
            if(val == 1) {
                isEventCategory = true;
            }
            else {
                isEventCategory = false;
            }

        } catch (JSONException e) {

        }
    }


    public String getName() {
        return categoryName;
    }

    public void setName(String name){
        this.categoryName = name;
    }

    public boolean isShopCategory() {
        return isShopCategory;
    }

    public boolean isTalkCategory() {
        return isTalkCategory;
    }

    public boolean isCastCategory() {
        return isCastCategory;
    }

    public boolean isEventCategory() {
        return isEventCategory;
    }
}

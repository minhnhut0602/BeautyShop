package mimishop.yanji.com.mimishop.modal;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

public class Image  extends BaseModal implements Serializable {

    private String imgUrl;
    private Bitmap bitmap;
    private File   file;
    private boolean deleted = false;
    private boolean isJustRemove = false;

    public Image() {

        super();

        imgUrl = null;
        bitmap = null;
    }

    public Image(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static Image create(JSONObject jsonObject){
        return new Image(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);
        try {
            imgUrl = jsonObject.getString("imageURL");

        } catch (JSONException e) {

        }
    }


    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean isDelete) {
        deleted = isDelete;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public boolean isJustRemove() {
        return isJustRemove;
    }

    public void setJustRemove(boolean isJustRemove) {
        this.isJustRemove = isJustRemove;
    }
}

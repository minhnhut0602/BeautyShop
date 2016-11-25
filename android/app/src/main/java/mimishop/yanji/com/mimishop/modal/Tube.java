package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KimCholJu on 5/12/2015.
 */
public class Tube extends BaseModal {

    private String tubeName;
    private String tubeSubName;
    private double tubeLatitude;
    private double tubeLongtitude;

    private ArrayList<Tube> arrSubTubList = new ArrayList<Tube>();

    public boolean isFlag = false;

    public Tube() {
        super();
    }

    public Tube(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            tubeName = jsonObject.getString("tubeName");
            tubeSubName = jsonObject.getString("tubeSubName");
            tubeLatitude = jsonObject.getDouble("tubeLatitude");
            tubeLongtitude = jsonObject.getDouble("tubeLongtitude");
        } catch (JSONException e) {

        }
    }

    public String getTubeName() {
        return tubeName;
    }
    public void setTubeName(String name) {
        tubeName = name;
    }

    public String getTubeSubName() {
        return tubeSubName;
    }

    public void addSubTube(Tube tube) {
        arrSubTubList.add(tube);
    }

    public ArrayList<Tube> getSubTubList() {
        return arrSubTubList;
    }

    public double getTubeLatitude() {
        return tubeLatitude;
    }

    public void setTubeLatitude(double lat) {
        tubeLatitude = lat;
    }

    public double getTubeLongtitude() {
        return tubeLongtitude;
    }

    public void setTubeLongtitude(double lng) {
        tubeLongtitude = lng;
    }
}
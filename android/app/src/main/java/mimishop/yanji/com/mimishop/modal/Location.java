package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 4/6/2015.
 */
public class Location extends BaseModal {

    public static String TAG = "App";

    private String locationName1;
    private String locationName2;
    private String locationName3;
    private String locationName4;
    private double locationLongtitude;
    private double locationLatitude;

    public Location() {
        super();
    }

    public Location(JSONObject jsonObject) {
        super(jsonObject);

    }

    public static Location create(JSONObject jsonObject) {
        return new Location(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            locationName1 = jsonObject.getString("locationName1");
            locationName2 = jsonObject.getString("locationName2");
            locationName3 = jsonObject.getString("locationName3");
            locationName4 = jsonObject.getString("locationName4");
            locationLongtitude = jsonObject.getDouble("locationLongtitude");
            locationLatitude = jsonObject.getDouble("locationLatitude");
        } catch (JSONException e) {

        }
    }

    public double getLocationLongtitude() {
        return locationLongtitude;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public String getLocationName1() {
        return locationName1;
    }

    public String getLocationName2() {
        return locationName2;
    }

    public String getLocationName3() {
        return locationName3;
    }

    public String getLocationName4() {
        return locationName4;
    }

    public String getLocationName() {
        String locationName = locationName1;

        if(locationName2 != null && locationName2.equals("") == false) {
            locationName = locationName + " " + locationName2;
        }

        if(locationName3 != null && locationName3.equals("") == false) {
            locationName = locationName + " " + locationName3;
        }

        if(locationName4 != null && locationName4.equals("") == false) {
            locationName = locationName + " " + locationName4;
        }

        return locationName;
    }

    public String getRealLocationName() {
        String locationName = locationName1;

        if(locationName2 != null && locationName2.equals("") == false) {
            locationName = locationName + " " + locationName2;
        }

        if(locationName3 != null && locationName3.equals("") == false) {
            locationName = locationName + " " + locationName3;
        }

        return locationName;
    }

    public  void setLocationName1(String name) {
        locationName1 = name;
    }
}
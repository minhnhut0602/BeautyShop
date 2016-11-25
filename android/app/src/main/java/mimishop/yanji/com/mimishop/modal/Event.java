package mimishop.yanji.com.mimishop.modal;

import android.util.Log;

//import com.facebook.android.Util;

import org.json.JSONException;
import org.json.JSONObject;

import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 3/24/2015.
 */
public class Event extends BaseModal {

    public static final String TAG = "Event";

    // life
    private  Image eventImg;
    private String eventSummary;
    private String eventContent;
    private int    eventMoney;
    private String eventAddress;

    // shop event
    private int    eventShopID;
    private String eventShopName;

    // click count
    private int    eventClickCnt;
    private String eventStart;
    private String eventEnd;

    private int eventEmoticon;
    public Event() {
        super();
    }

    public Event(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Event create(JSONObject jsonObject){
        return new Event(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);
        Log.e("Event",jsonObject.toString());
        try {
            try {
                eventContent = jsonObject.getString("eventContent");
                eventSummary = jsonObject.getString("eventSummary");
                eventClickCnt = jsonObject.getInt("eventClickCnt");
                eventAddress = jsonObject.getString("eventAddress");
                eventShopID = jsonObject.getInt("eventShopID");
                eventShopName = jsonObject.getString("eventShopName");
                eventMoney = jsonObject.getInt("eventMoney");
                eventStart = jsonObject.getString("eventStart");
                eventEnd = jsonObject.getString("eventEnd");
                eventEmoticon = jsonObject.getInt("eventEmoticon");
            }catch (Exception e) {

            }

            String eventImgURL = jsonObject.getString("eventImgURL");
            if(eventImgURL != null && eventImgURL.equals("null") == false) {
                eventImg = new Image();
                eventImg.setImgUrl(eventImgURL);
                eventImg.setId(jsonObject.getInt("eventImgID"));
            }

        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public boolean isEventEmoticonChecked()
    {
        if(eventEmoticon == 1) return true;
        return false;
    }


    public Image getEventImage(){
        return  eventImg;
    }

    public void setEventImage(Image image){
        eventImg = image;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public  void setEventSummary(String summary) {
        eventSummary = summary;
    }

    public String getEventExplain() {
        return eventContent;
    }

    public  void setEventExplain(String summary) {
        eventContent = summary;
    }

    public String getLocation() {
        return eventAddress;
    }

    public String getSubLocation() {
        String []arrLocation = eventAddress.split(" ");
        String strLocation = arrLocation[0];
        if(arrLocation.length >= 2) {
            strLocation = strLocation + " " + arrLocation[1];
        }
        return strLocation;
    }

    public  void setLocation(String summary) {
        eventAddress = summary;
    }

    public  int getPrice() {
        return eventMoney;
    }

    public void setPrice(int price) {
        this.eventMoney = price;
    }

    public String getShopName() {return  eventShopName;}

    public int getEventShopID() {
        return eventShopID;
    }
    public int getEventClickCnt() {
        return eventClickCnt;
    }

    public void setEventClickCnt(int cnt) {
        eventClickCnt = cnt;
    }

    public  boolean isEventing() {
        String curDate = Utility.getInstance().getCurrentDate();

        if(curDate.compareToIgnoreCase(eventStart) >= 0 && curDate.compareToIgnoreCase(eventEnd) <= 0) {
            return true;
        }
        return  false;
    }
}

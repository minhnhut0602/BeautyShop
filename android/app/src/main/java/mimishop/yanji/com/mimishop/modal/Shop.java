package mimishop.yanji.com.mimishop.modal;

import android.util.Log;

//import com.facebook.android.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class Shop  extends BaseModal {

    public static  final String TAG = "Shop";

    public static final int SHOP_WAIT = 1;
    public static final int SHOP_OK = 2;

    private Image    shopImg;
    private String   shopName;
    private String   shopAddress;
    private Category shopCategory;
    private String      shopID;
    private int      shopLevel;
    private int      shopRemarkCnt;
    private int      shopRemarkLevel;

    private int      shopOwnerID;
    private String   shopRestTime;
    private String   shopOpenTime;
    private String   shopPhonenumber;
    private String   shopPostManName;
    private String   shopStuffPhoneNumber;
    private boolean   shopQuestionable;
    private String   shopDiscountInfo;
    private int      shopBannerDays;
    private boolean   shopParkable;
    private Event    shopEvent;
    private boolean  shopEventable;
    private int      shopCallCnt;
    private int      shopClickCnt;
    private Image    shopManagerIdentyImg;
    private double    shopLongtitude;
    private double    shopLatitude;
    private String shopDescriptionString;
    private boolean  shopAutoLogin;
    private String    shopRoad;

    private ArrayList<Image> shopSubImageArray;
    private ArrayList<Image> shopShopPriceImageArray;

    private int      shopStatus;
    private int      shopSequence;


    public boolean isAdded = false;

    public Shop() {

        super();
    }


    public Shop(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Shop create(JSONObject jsonObject){
        return new Shop(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);
        Log.e("Shop", jsonObject.toString());
        try {
            shopSubImageArray = new ArrayList<Image>();
            shopShopPriceImageArray = new ArrayList<Image>();
            try {
                shopID = jsonObject.getString("shopID");
            }
            catch (Exception e) {
                shopID = "";
            }

            shopName = jsonObject.getString("shopName");
            shopAddress = jsonObject.getString("shopAddress");
            shopStatus = jsonObject.getInt("shopStatus");
            shopSequence = jsonObject.getInt("shopConsequence");

            Object shopImgURL = jsonObject.get("shopImgURL");
            if(shopImgURL != null && shopImgURL.toString().equals("null") == false) {
                shopImg = new Image();
                shopImg.setImgUrl((String)shopImgURL);
                shopImg.setId(jsonObject.getInt("shopImgID"));
            }

            shopOwnerID = jsonObject.getInt("shopOwnerID");
            shopPhonenumber = jsonObject.getString("shopPhonenumber");
            if(shopPhonenumber.toString().equals("null") || shopPhonenumber.toString().equals("")) {
                shopPhonenumber="";
            }
            shopRestTime = jsonObject.getString("shopRestTimeString");
            shopOpenTime = jsonObject.getString("shopOpenTimeString");

            if(shopRestTime.toString().equals("null") || shopRestTime.toString().equals("")) {
                shopRestTime = "일반";
            }

            if(shopOpenTime.toString().equals("null") || shopOpenTime.toString().equals("")) {
                shopOpenTime ="일반";
            }

            if(jsonObject.getString("shopParkable").equals("Y") == true) {
                shopParkable = true;
            }
            else {
                shopParkable = false;
            }

            int shopAutoLogin = jsonObject.getInt("shopAutoLogin");
            if(shopAutoLogin == 0) {
                this.shopAutoLogin = false;
            }
            else {
                this.shopAutoLogin = true;
            }

            Object obj = jsonObject.get("shopRoad");
            if(obj != null && obj.toString().equals("null") == false) {
                shopRoad = obj.toString();
            }
            else {
                shopRoad = null;
            }

            obj = jsonObject.getString("shopDescriptionString");
            if(obj != null && obj.toString().equals("null") == false) {
                shopDescriptionString = obj.toString();
            }
            else {
                shopDescriptionString = "없음";
            }

            shopLongtitude = jsonObject.getDouble("shopLongtitude");
            shopLatitude = jsonObject.getDouble("shopLatitude");

            Object shopImagArrayObj = jsonObject.get("shopImgArray");
            if(shopImagArrayObj != null && shopImagArrayObj.toString().equals("null") == false) {
                JSONArray shopImgArray = jsonObject.getJSONArray("shopImgArray");
                for (int i = 0; i < shopImgArray.length(); i++) {
                    JSONObject dataObject = shopImgArray.getJSONObject(i);
                    Image image = new Image(dataObject);
                    shopSubImageArray.add(image);
                }
            }

            shopImagArrayObj = jsonObject.get("shopPriceImgArray");
            if(shopImagArrayObj != null && shopImagArrayObj.toString().equals("null") == false) {
                JSONArray shopImgArray = jsonObject.getJSONArray("shopPriceImgArray");
                for (int i = 0; i < shopImgArray.length(); i++) {
                    JSONObject dataObject = shopImgArray.getJSONObject(i);
                    Image image = new Image(dataObject);
                    shopShopPriceImageArray.add(image);
                }
            }

            try {
                shopRemarkCnt = jsonObject.getInt("shopReviewCnt");
                shopRemarkLevel = jsonObject.getInt("shopRemarkLevel");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if(jsonObject.getString("shopQuestionable").equals("Y") == true) {
                shopQuestionable = true;
            }
            else {
                shopQuestionable = false;
            }

            try {
                String shopCategoryName = jsonObject.getString("shopCategoryName");
                shopCategory = new Category();
                shopCategory.setName(shopCategoryName);
                shopCategory.setId(jsonObject.getInt("shopCategoryID"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if(jsonObject.getString("shopEventable").equals("Y") == true) {
                shopEventable = true;
            }
            else {
                shopEventable = false;
            }

            try {
                shopEvent = new Event(jsonObject.getJSONObject("shopEvent"));
            }
            catch (Exception e) {
                e.printStackTrace();
                shopEvent = null;
            }

            shopBannerDays = jsonObject.getInt("shopBannerDays");
            shopCallCnt= jsonObject.getInt("shopCallCnt");
            shopClickCnt= jsonObject.getInt("shopClickCnt");
            shopDiscountInfo = jsonObject.getString("shopDiscountInfo");
        } catch (JSONException e) {
            Logger.e(TAG, "initWithJSON - JSONException : " + e.getMessage());
        }
    }

    public Image getImage() {

        if(shopImg != null) {
            return shopImg;
        }
        if(shopSubImageArray != null && shopSubImageArray.size() > 0) {
            return shopSubImageArray.get(0);
        }

        return  null;
    }

    public Image getShopMainImg() {
        return  shopImg;
    }

    public void setShopMainImg(Image image) {
        shopImg = image;
    }

    public String getName() {
        return shopName;
    }

    public void setName(String name){
        this.shopName = name;
    }

    public String getAddress() {
        return shopAddress;
    }

    public void setAddress(String address) {
        this.shopAddress = address;
    }

    public String getRestDay() {
        return shopRestTime;
    }

    public void setRestDay(String restDay) {
        this.shopRestTime = restDay;
    }

    public int getShopStatus(){
        return shopStatus;
    }

    public ArrayList<Image> getShopSubImageArray() {return shopSubImageArray;}
    public ArrayList<Image> getShopPriceImageArray() {return shopShopPriceImageArray;}

    public int getShopBannerDays(){
        return shopBannerDays;
    }

    public int getShopClickCnt() {
        return shopClickCnt;
    }

    public int getShopCallCnt() {
        return shopCallCnt;
    }

    public boolean isShopEventable() {
        return  shopEventable;
    }

    public boolean isShopEventing() {
        if(shopEventable == false) {
            return  false;
        }

        if(shopEvent != null && shopEvent.isEventing() == true) {
            return true;
        }

        return false;
    }

    public boolean isShopEmoticonChecked() {
        if(shopEvent != null && shopEvent.isEventEmoticonChecked()) return true;
        return false;
    }

    public boolean isShopParkable() {
        return  shopParkable;
    }

    public double getShopLongtitude() {
        return  shopLongtitude;
    }

    public double getShopLatitude() {
        return  shopLatitude;
    }

    public void setShopQuestionable(boolean isQuestionable) {
        shopQuestionable = isQuestionable;
    }

    public  String getPhoneNumber() {
        return shopPhonenumber;
    }

    private  boolean compareTime(String time, String time1, String time2) {

        if (time1.compareTo(time2) > 0) {
            int hour_sys = Integer.parseInt(time2.split(":")[0]);
            int min_sys = Integer.parseInt(time2.split(":")[1]);

            hour_sys += 24;

            String result = String.format("%02d:%02d", hour_sys, min_sys);
            time2 = result;
        }

        if (time.compareTo(time1) > 0 && time.compareTo(time2) < 0) {
           return true;
        }

        return false;
    }

    public boolean isShopEnded() {
        //  10:00~22:00
        // 매일 10:00~20:30 월요일 09:00~18:00
        // 매일, 월요일~일요일, 평일, 주말
        // exception: 10:00~08:00

        Utility utility = Utility.getInstance();

        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();

        // 현재 시간을 저장 한다.
        Date date = new Date(now);

        // 시간
        String currentTime = utility.getCurrentTime();
        String currentDay = utility.getDayOfWeek(date, true);

        boolean isEnded = true;
        String[] tokens = shopOpenTime.trim().split(" ");

        try {
            if (tokens.length == 1) {
                String[] arrTime = tokens[0].split("~");
                isEnded = (compareTime(currentTime, arrTime[0], arrTime[1]) == true ? false : true);
            }
            else {

                for(int i = 0; i < tokens.length; i+=2) {
                    String day = tokens[i];
                    String []arrTime = tokens[i + 1].split("~");
                    boolean isTimeEnded = (compareTime(currentTime, arrTime[0], arrTime[1]) == true ? false : true);
                    if(day.equals(currentDay) == true) {
                        isEnded = isTimeEnded;
                        break;
                    }
                    else if(day.equals("주말") == true && (currentDay.equals("토요일") == true || currentDay.equals("일요일")==true)){
                        isEnded = isTimeEnded;
                        break;
                    }
                    else if(day.equals("평일") == true || day.equals("매일")) { // 평일, 매일
                        isEnded = isTimeEnded;
                    }
                }
            }

        }
        catch (Exception e) {
            isEnded = true;
        }

        return  isEnded;
    }

    public String getShopDiscountInfo(){
        return  shopDiscountInfo;
    }

    public void setShopLatitude(double shopLatitude) {
       this.shopLatitude = shopLatitude;
    }

    public void setShopLongtitude(double shopLongtitude) {
        this.shopLongtitude = shopLongtitude;
    }

    public String getShopOpenTime() {
        return shopOpenTime;
    }

    public String getShopDescriptionString() {
        return shopDescriptionString;
    }

    public int getShopRemarkCnt() {
        return shopRemarkCnt;
    }

    public int getShopRemarkLevel() {
        return  shopRemarkLevel;
    }

    public int getShopOwnerID() {
        return  shopOwnerID;
    }

    public String getShopID() {
        return shopID;
    }

    public boolean isShopAutoLogin() {
        return shopAutoLogin;
    }

    public String getShopRoad() {
        return shopRoad;
    }

    public Category getShopCategory() {
        return shopCategory;
    }

    public int getShopSequence() {
        return shopSequence;
    }

    public boolean isShopQuestionable() {
        return  shopQuestionable;
    }
}

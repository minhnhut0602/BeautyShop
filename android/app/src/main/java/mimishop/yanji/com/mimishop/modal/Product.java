package mimishop.yanji.com.mimishop.modal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 3/26/2015.
 */
public class Product extends BaseModal {

    private String         productPrice;
    private String         productEventPrice;
    private String      productName;
    private int         productShopID;
    private int         productIdx = 0;
    private  boolean    isNewProduct = false;
    private int         shopLevel;
    private int         shopReviewCount;
    private String      shopName;
    private String      shopAddress;

    private int         realPrice;
    public Product() {

        super();
    }

    public Product(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Product create(JSONObject jsonObject){
        return new Product(jsonObject);
    }

    protected void initWithJSON(JSONObject jsonObject) {
        super.initWithJSON(jsonObject);

        try {
            productName = jsonObject.getString("productName");
            productShopID = jsonObject.getInt("productShopID");
            try {
                productEventPrice = jsonObject.getString("productEventPrice");
            }
            catch (Exception e) {
                productEventPrice = "0원";
            }
            productPrice = jsonObject.getString("productPrice");
            realPrice = getRealPrice();

            shopLevel = jsonObject.getInt("shopRemarkLevel");
            shopName = jsonObject.getString("shopName");
            shopReviewCount = jsonObject.getInt("shopReviewCnt");
            shopAddress = jsonObject.getString("shopAddress");

        } catch (JSONException e) {

        }
    }

    private int getRealPrice() {
        int price = 0;

        // 15,000원, 15,500~20,000원
        String strPrice = productPrice.replace("원","");
        strPrice = strPrice.replace(",","");
        try {
            String []arrPrice = strPrice.split("~");
            int size = arrPrice.length;

            if(size == 1) {
                price = Integer.parseInt(arrPrice[0]);
            }
            else if(size == 2){
                price = (Integer.parseInt(arrPrice[0]) + Integer.parseInt(arrPrice[1]))/2;
            }
        }
        catch (Exception e) {
            price = -1;
        }

        return price;
    }


    public String getName() {
        return productName;
    }

    public void setName(String name) {

        this.productName = name;
    }

    public String getEventPrice() {
        return productEventPrice;
    }

    public void setEventPrice(String price) {
        this.productEventPrice = price;
    }

    public String getPrice() {
        return productPrice;
    }

    public void setPrice(String price) {
        this.productPrice = price;
    }

    public  int getIndex() {return productIdx;}
    public  void  setIndex(int idx) {productIdx = idx;}

    public boolean isNewProduct(){
        return isNewProduct;
    }

    public void setNewProduct() {
        isNewProduct = true;
    }

    public String getShopName() {
        return  shopName;
    }

    public int  getShopLevel() {
        return  shopLevel;
    }
    public int getShopReviewCount() {
        return shopReviewCount;
    }
    public String getShopAddress() {
        return shopAddress;
    }

    public int getProductPrice() {
        return realPrice;
    }

    public int getProductShopID() {
        return productShopID;
    }
}

package mimishop.yanji.com.mimishop.network;

/**
 * Created by KCJ on 3/24/2015.
 */
public class ServerAPIPath {
    public static final String APP_PAGE_URL =  "https://play.google.com/store/apps/details?id=mimishop.yanji.com.mimishop";//"market://details?id=mimishop.yanji.com.mimishop";//"http://www.mimishop.co.kr";
    public static final String API_BASE_URL = "http://www.miggle.co.kr/api/";//"http://192.168.0.11:8080/index.php/api/"

    public static final String API_INIT = API_BASE_URL + "init";
    public static final String API_ACCESS = API_BASE_URL + "access";
    public static final String API_FINISH = API_BASE_URL + "finish";
    public static final String API_GET_AGREEMENT = API_BASE_URL + "getAgreement";
    public static final String API_GET_FREETALK_LIST = API_BASE_URL + "getFreeTalkList";
    public static final String API_GET_FREETALK = API_BASE_URL + "getFreetalk";
    public static final String API_GET_FREETALK_COMMENT_LIST = API_BASE_URL + "getFreeTalkCommentList";
    public static final String API_GET_LOCATION_LIST = API_BASE_URL + "getLocationList";
    public static final String API_GET_CATEGORY_LIST = API_BASE_URL + "getCategoryList";
    public static final String API_GET_LIFE_EVNET_LIST = API_BASE_URL + "getLifeEventList";
    public static final String API_GET_LIFE_BANNER = API_BASE_URL + "getLifeBanner";
    public static final String API_GET_ME = API_BASE_URL + "getMe";
    public static final String API_GET_MY_SHOP = API_BASE_URL + "getMyShop";
    public static final String API_GET_MY_FREETALK_LIST = API_BASE_URL + "getMyFreeTalkList";
    public static final String API_GET_MY_FREETALK_COMMENT_LIST = API_BASE_URL + "getMyFreeTalkCommentList";
    public static final String API_GET_PRODUCT_LIST = API_BASE_URL + "getProductListOfShop";
    public static final String API_GET_SHOP_EVENT_LIST = API_BASE_URL + "getEventList";
    public static final String API_GET_SHOP_LIST = API_BASE_URL + "searchShopList";
    public static final String API_GET_SHOP = API_BASE_URL + "getShop";
    public static final String API_GET_SEARCH_PRODUCT_NAME = API_BASE_URL + "searchProductList";
    public static final String API_GET_CAST_LIST = API_BASE_URL + "getCastList";
    public static final String API_GET_CAST_DETAIL_LIST = API_BASE_URL + "getCastDetailList";
    public static final String API_GET_LATLNG = "http://maps.google.com/maps/api/geocode/json?ka&sensor=false&address=";
    public static final String API_GET_RECENT_NEWS_LIST = API_BASE_URL + "getRecentNewsList";
    public static final String API_GET_MY_ENVELOP_LIST = API_BASE_URL + "getEnvelopList";
    public static final String API_GET_TUBE_LIST = API_BASE_URL + "getTubeList";
    public static final String API_GET_BANNER_LIST = API_BASE_URL + "getBannerAdsList";
    public static final String API_GET_CAST_BANNER_LIST = API_BASE_URL + "getCastAdsList";
    public static final String API_GET_EXIT_ADS = API_BASE_URL + "getExitAds";
    public static final String API_GET_BEST_CAST = API_BASE_URL + "getBestCast";
    public static final String API_GET_BEST_LIFE_EVENT = API_BASE_URL + "getBestLifeEvent";
    public static final String API_GET_BEST_SHOP_EVENT = API_BASE_URL + "getBestShopEvent";
    public static final String API_GET_BEST_FREETALK = API_BASE_URL + "getBestFreetalk";
    public static final String API_GET_CAST = API_BASE_URL + "getCast";
    public static final String API_GET_CAST_COMMENT_LIST = API_BASE_URL + "getCastCommentList";
    public static final String API_GET_BEST_CAST_COMMENT_LIST = API_BASE_URL + "getBestCastCommentList";
    public static final String API_GET_BANNER_COMMENT_LIST = API_BASE_URL + "getBannerCommentList";

    /* WI:wrong information UR: User Remark QA:Question of Answer */
    public static final String API_GET_SHOP_COMMENT_LIST = API_BASE_URL + "getShopCommentList";
    public static final String API_GET_SHOP_QUESTION_LIST = API_GET_SHOP_COMMENT_LIST + "?shopcommentType=QA";
    public static final String API_GET_SHOP_USERREMARK_LIST = API_GET_SHOP_COMMENT_LIST + "?shopcommentType=UR";
    public static final String API_GET_SHOP_WRONGINFORMATION_LIST = API_GET_SHOP_COMMENT_LIST + "?shopcommentType=WI";
    public static final String API_GET_SHOP_ANSWER_LIST_WITH_SHOP_ID = API_BASE_URL + "getShopAnswerListWithShopID";

    public static final String API_POST_CREATE_ACCOUNT = API_BASE_URL + "registerUser";
    public static final String API_POST_UPDATE_ACCOUNT = API_BASE_URL + "registerUser";
    public static final String API_POST_WRITE_FREETALK = API_BASE_URL + "writeFreetalk";
    public static final String API_POST_WRITE_FREETALKCOMMENT = API_BASE_URL + "writeFreetalkComment";
    public static final String API_POST_WRITE_ADMIN_QUESTION = API_BASE_URL + "writeMyQuestion";
    public static final String API_POST_DUPLICATE_USERID = API_BASE_URL + "isDuplicationUserID";
    public static final String API_POST_DUPLICATE_SHOP_ID = API_BASE_URL + "isDuplicationShopID";
    public static final String API_POST_LIKE_FREETALK = API_BASE_URL + "likeFreeTalk";
    public static final String API_POST_LIKE_FREETALK_COMMENT = API_BASE_URL + "likeFreetalkComment";
    public static final String API_POST_UPLOAD_FREETALK_IMAGE = API_BASE_URL + "uploadFreeTalkImage";
    public static final String API_POST_UPLOAD_SHOP_IMAGE = API_BASE_URL + "uploadShopImage";
    public static final String API_POST_UPLOAD_EVENT_IMAGE = API_BASE_URL + "uploadEventImage";
    public static final String API_POST_UPLOAD_USER_IMAGE = API_BASE_URL + "uploadUserImage";
    public static final String API_POST_REGISTER_MY_SHOP = API_BASE_URL + "registerMyShop";
    public static final String API_POST_MODIFY_MY_SHOP = API_BASE_URL + "registerMyShop";
    public static final String API_POST_REGISTER_MY_PRODUCT = API_BASE_URL + "registerMyProduct";
    public static final String API_POST_WRITE_SHOP_ANSWER = API_BASE_URL + "writeShopAnswer";
    public static final String API_POST_REMOVE_SHOP_ANSWER = API_BASE_URL + "removeShopAnswer";
    public static final String API_POST_REGISTER_EVENT = API_BASE_URL + "registerEvent";
    public static final String API_POST_SHOP_JIM = API_BASE_URL + "doMyJim";
    public static final String API_POST_WRITE_SHOP_COMMENT = API_BASE_URL + "writeShopComment";
    public static final String API_POST_REMOVE_SHOP_COMMENT = API_BASE_URL + "removeShopComment";
    public static final String API_POST_IS_SHOP_AUTO_LOGIN= API_BASE_URL + "isShopAutoLogin";
    public static final String API_POST_SHOP_LOGIN= API_BASE_URL + "loginShop";
    public static final String API_POST_REMOVE_FREETALK= API_BASE_URL + "removeFreetalk";
    public static final String API_POST_REGISTER_GCMREGID = API_BASE_URL + "registerGCMRegID";
    public static final String API_POST_MODIFY_GCM = API_BASE_URL + "modifyGCM";
    public static final String API_POST_MODIFY_GCM_LIST = API_BASE_URL + "modifyGCMList";
    public static final String API_POST_DO_TAG_FREETALK_COMMENT = API_BASE_URL + "doTAGFreetalkComment";
    public static final String API_POST_DO_DECLARE_FREETALK = API_BASE_URL + "doDeclareFreetalk";
    public static final String API_POST_DO_DECLARE_FREETALK_COMMENT = API_BASE_URL + "doDeclareFreetalkComment";
    public static final String API_POST_LIKE_CAST = API_BASE_URL + "likeCast";
    public static final String API_POST_SHARE_CAST = API_BASE_URL + "shareCast";
    public static final String API_POST_WRITE_CAST_COMMENT = API_BASE_URL + "writeCastComment";
    public static final String API_POST_FIND_SHOP_INFO = API_BASE_URL + "findShopInfo";
    public static final String API_POST_LOGOUT_SHOP= API_BASE_URL + "logoutShop";
    public static final String API_POST_LIKE_CAST_COMMENT = API_BASE_URL + "likeCastComment";
    public static final String API_POST_REMOVE_FREETALK_COMMENT = API_BASE_URL + "removeFreetalkComment";
    public static final String API_POST_INVITE_FRIEND = API_BASE_URL + "inviteFriend";
    public static final String API_POST_CLICK_BANNER = API_BASE_URL + "clickBanner";
    public static final String API_POST_WRITE_BANNER_COMMENT = API_BASE_URL + "writeBannerComment";
    public static final String API_POST_REMOVE_BANNER_COMMENT = API_BASE_URL + "removeBannerComment";
    public static final String API_POST_EVENT_CLICK = API_BASE_URL+"clickEvent";
    public static final String API_POST_LIFE_CLICK = API_BASE_URL+"clickLife";
    public static final String API_POST_SHOP_CLICK = API_BASE_URL+"clickShop";
    public static final String API_POST_SHOP_CALL = API_BASE_URL+"callShop";
    public static final String API_POST_LOG_OUT = API_BASE_URL+"logOut";
}

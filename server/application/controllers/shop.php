<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Shop extends CI_Controller {

    /**
     * Index Page for this controller.
     *
     * Maps to the following URL
     * 		http://example.com/index.php/welcome
     *	- or -
     * 		http://example.com/index.php/welcome/index
     *	- or -
     * Since this controller is set as the default controller in
     * config/routes.php, it's displayed at http://example.com/
     *
     * So any other public methods not prefixed with an underscore will
     * map to /index.php/welcome/<method_name>
     * @see http://codeigniter.com/user_guide/general/urls.html
     */

    function __construct() {
        // Call the Model constructor
        parent::__construct ();

        if(true) { // testing
            error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
        }

        $this->load->database ();
        $this->load->model ( 'shop_model' );
        $this->load->model ( 'gcm_model' );
        $this->load->model ( 'admin_model' );
        $this->load->model('image_model');
        $this->load->model ( 'member_model' );
        $this->load->library('image_lib');
        $this->load->library('util');
        $this->load->helper ( 'url' );
        date_default_timezone_set('Asia/Seoul');
    }

    function index() {
        //
        // data for view
        //

        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $shop_name = $this->input->get('shop_name');
        $shop_phonenumber = $this->input->get('shop_phonenumber');
        $shop_eventable = $this->input->get('shop_eventable');
        $shop_address = $this->input->get('shop_address');
        $shop_callcnt_condition = $this->input->get('shop_callcnt_condition');
        $shop_clickcnt_condition = $this->input->get('shop_clickcnt_condition');
        $shop_status = $this->input->get('shop_status');
        $shop_visibility = $this->input->get('shop_visibility');
        $shop_category_id = $this->input->get('shop_category_id');

        $data['arrCategory'] = $this->shop_model->getCategoryList();
        $arrShop = $this->shop_model->getShopList($pageNum, $shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition,
            $shop_clickcnt_condition, $shop_status, $shop_address, $shop_visibility, $shop_category_id);
        $data['totalShopCnt'] = $this->shop_model->getShopCount($shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition,
                    $shop_clickcnt_condition, $shop_status, $shop_address, $shop_visibility, $shop_category_id);
        $data['totalPageCnt'] = ((int)($this->shop_model->getShopCount($shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition,
                    $shop_clickcnt_condition, $shop_status, $shop_address, $shop_visibility, $shop_category_id) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;
        $data['arrShop'] = $arrShop;

        if($shop_name != null) {
            $data['shopName'] = $shop_name;
        }

        if($shop_address != null) {
            $data['shopAddress'] = $shop_address;
        }

        if($shop_phonenumber != null) {
            $data['shopPhoneNumber'] = $shop_phonenumber;
        }

        if($shop_eventable != null) {
            $data['shopEventable'] = $shop_eventable;
        }

        if($shop_callcnt_condition != null) {
            $data['shopCallCntCondition'] = $shop_callcnt_condition;
        }
        if($shop_clickcnt_condition != null) {
            $data['shopClickCntCondition'] = $shop_clickcnt_condition;
        }
        if($shop_status != null) {
            $data['shopStatus'] = $shop_status;
        }

        if($shop_visibility != null) {
            $data['shopVisibility'] = $shop_visibility;
        }

        if($shop_category_id != null) {
            $data['selectedCateogryID'] = $shop_category_id;
        }

        if(false) {
            echo json_encode($arrShop);
        }

        //load the view
        $data['main_content'] = 'admin/shop/shop';
        $data['tab_id'] = ADMIN_TAB_ID_SHOP;
        $this->load->view('includes/template', $data);
    }

    function  shop_consequence() {

        $address = $this->input->get("address");

        //load the view
        $data['arrShop'] = $this->shop_model->getShopConArray($address);
        $data['currentPageNum'] = 1;

        if($address != null) {
            $data['address'] = $address;
        }

        $data['main_content'] = 'admin/shop/shop_consequence';
        $data['tab_id'] = ADMIN_TAB_ID_SHOPSEQUENCE;
        $this->load->view('includes/template', $data);
    }

    function  update_shop() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }

        //load the view
        $data['shop_id'] = $shop_id;
        $result = $this->shop_model->getShop($shop_id);
        $shopIDArrayString =  $result['shopImgIDArrayString'];
        $result['shopImgArray'] = $this->getImageList($shopIDArrayString);
        $result['shopPriceImgArray'] = $this->getImageList($result ['shopPriceImgIDArrayString']);
        $result['shopQuestionCnt'] = $this->shop_model->getShopQuestionCount($shop_id);
        $result = $this->getShopReviewResult($result, $shop_id);

        // event
        $arrEvent = $this->shop_model->getEventList($shop_id);
        if(count($arrEvent) > 0) {
            $result['shopEvent'] = $arrEvent[0];
        }
        else {
            $result['shopEvent'] = null;
        }
        $data['shopInfo'] = $result;

        $this->load->view('admin/shop/update_shop', $data);
    }

    function  shop_show() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }

        //load the view
        $data['shop_id'] = $shop_id;
        $result = $this->shop_model->getShop($shop_id);
        $shopIDArrayString =  $result['shopImgIDArrayString'];
        $result['shopImgArray'] = $this->getImageList($shopIDArrayString);
        $result['shopPriceImgArray'] = $this->getImageList($result ['shopPriceImgIDArrayString']);
        $result['shopQuestionCnt'] = $this->shop_model->getShopQuestionCount($shop_id);
        $result['shopProductCnt'] = $this->shop_model->getProductCount($shop_id);
        $result = $this->getShopReviewResult($result, $shop_id);

        // event
        $arrEvent = $this->shop_model->getEventList($shop_id);
        if(count($arrEvent) > 0) {
            $result['shopEvent'] = $arrEvent[0];
        }
        else {
            $result['shopEvent'] = null;
        }
        $data['shopInfo'] = $result;

        $this->load->view('admin/shop/shop_show', $data);
    }

    function  show_price_image_array() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }
        $result = $this->shop_model->getShop($shop_id);

        $data = Array();
        $data['arrImage'] =  $this->getImageList($result ['shopPriceImgIDArrayString']);

        $this->load->view('admin/show_image_array', $data);
    }

    function  update_shop_price_image_array() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }
        $result = $this->shop_model->getShop($shop_id);

        $data = Array();
        $data['arrImage'] =  $this->getImageList($result ['shopPriceImgIDArrayString']);
        $data['shopID'] = $shop_id;
        $data['isShopImage'] = 0;

        $this->load->view('admin/shop/update_shop_image', $data);
    }

    function  update_shop_image_array() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }
        $result = $this->shop_model->getShop($shop_id);

        $data = Array();
        $data['arrImage'] =  $this->getImageList($result ['shopImgIDArrayString']);
        $data['shopID'] = $shop_id;
        $data['isShopImage'] = 1;

        $this->load->view('admin/shop/update_shop_image', $data);
    }

    function  show_manager_identi_image() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }
        $result = $this->shop_model->getShop($shop_id);

        $data = Array();
        $arrImage = Array();
        if($result['shopIdentyImgURL'] != null) {
            $arrImage[0]['imageURL'] = $result['shopIdentyImgURL'];
        }
        $data['arrImage'] = $arrImage;

        $this->load->view('admin/show_image_array', $data);
    }

    function  show_event_banner_image() {
        $shop_id = $this->input->get('event_id');

        if($shop_id == null) {
            return;
        }
        $result = $this->shop_model->getEvent($shop_id);

        $data = Array();
        $arrImage = Array();
        if($result['eventImgURL'] != null) {
            $arrImage[0]['imageURL'] = $result['eventImgURL'];
        }
        $data['arrImage'] = $arrImage;

        $this->load->view('admin/show_image_array', $data);
    }

    function show_shop_image_array() {
        $shop_id = $this->input->get('shop_id');

        if($shop_id == null) {
            return;
        }
        $result = $this->shop_model->getShop($shop_id);

        $data = Array();
        $data['arrImage'] =  $this->getImageList($result ['shopImgIDArrayString']);

        $this->load->view('admin/show_image_array', $data);
    }

    function add_shop() {
        $data = Array();
        $this->load->view('admin/shop/add_shop', $data);
    }

    function  shop_password_question_list() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['arrQuestion'] = $this->admin_model->getQuestionList(ADMIN_TYPE_ADMIN_QUESTION_SHOP_PASSWORD, ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->admin_model->getQuestionCount(ADMIN_TYPE_ADMIN_QUESTION_SHOP_PASSWORD) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;
        $this->load->view('admin/shop/shop_password_question_list', $data);
    }


    private function doRespond($p_result_code, $p_result_msg, $p_result) {
        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    private function getImageList($arr_img_id) {
        if($arr_img_id == null) {
            return null;
        }

        $shopIDArrayString = $arr_img_id;

        if ($shopIDArrayString != null) {
            $arrImgID = json_decode ( $shopIDArrayString );

            $cnt = count ( $arrImgID );

            $result = Array ();

            for($i = 0; $i < $cnt; $i ++) {

                $imgInfo = $this->image_model->getImageByID ( $arrImgID [$i] );
                array_push ( $result, $imgInfo );
            }
        }
        return $result;
    }

    private function getShopReviewResult($result, $shop_id) {
        $review = $this->shop_model->getShopLevel($shop_id);
        $result['shopReviewCnt'] = $review['shopReviewCnt'];

        if($review['shopRemarkLevel'] == null) {
            $result['shopRemarkLevel'] = 0;
        }
        else {
            $result['shopRemarkLevel'] = $review['shopRemarkLevel'];
        }

        return $result;
    }

    function  register_image() {
        $arr_img_id = $this->input->post('arr_img_id');
        $shop_id = $this->input->post('shop_id');

        if ($shop_id == null || $arr_img_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->shop_model->updateShopImage($shop_id, $arr_img_id);
        $this->doRespond(0, "success", null );
    }

    function  create_shop() {
        $shop_category_id = $this->input->post('shop_category_id');
        $shop_staus = $this->input->post('shop_status');
        $shop_eventable = $this->input->post('shop_eventable');
        $shop_remark = $this->input->post('shop_remark');
        $shop_live_date= $this->input->post('shop_live_date');
        $shop_name = $this->input->post('shop_name');

        $shop = $this->shop_model->createShop($shop_name, $shop_category_id, $shop_staus, $shop_eventable, $shop_live_date);

        if($shop == null) {
            $this->doRespond(-1, "false", null );
            return;
        }

        if($shop_remark != null) {
            $this->shop_model->setRemark($shop['id'], $shop_remark);
        }

        $this->doRespond(0, "success", null );
    }

    function  update_shop_info()
    {
        $shop_id = $this->input->post('shop_id');
        $shop_category_id = $this->input->post('shop_category_id');
        $shop_staus = $this->input->post('shop_status');
        $shop_questionable = $this->input->post('shop_questionable');
        $shop_eventable = $this->input->post('shop_eventable');
        $shop_live_date = $this->input->post('shop_live_date');
        
        $shop_img_image_array = $this->input->post('shop_img_array_string');
        $is_shop_image = $this->input->post('is_shop_image');
        
        $shop_address = $this->input->post('shop_address');
        $shop_phonenumber = $this->input->post('shop_phonenumber');
        $shop_manageridenty_imgid = $this->input->post('shop_manageridenty_imgid');
        $shop_visibility = $this->input->post("shop_visibility");
        $shop_name = $this->input->post('shop_name');
        
        $shop_road = $this->input->post('shop_road');
        $shop_opentime = $this->input->post('shop_opentime');
        $shop_resttime = $this->input->post('shop_resttime');
        $shop_parkable = $this->input->post('shop_parkable');
        $shop_description = $this->input->post('shop_description');
        
        if ($shop_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        if ($shop_live_date > LIMIT_SHOP_LIVEDATE) {
            $shop_live_date = LIMIT_SHOP_LIVEDATE;
        }

        $price_image_array = null;
        if ($is_shop_image != null) {
            if ($is_shop_image == 0) {
                $price_image_array = $shop_img_image_array;
            }
        }

        if (true) {
            $result = $this->shop_model->updateShopInfo($shop_id, $shop_category_id, $shop_staus, $shop_questionable, $shop_eventable, $shop_live_date, $shop_address,
                $shop_phonenumber, $shop_img_image_array, $price_image_array, $shop_manageridenty_imgid, $shop_visibility, $shop_name, 
                $shop_road,$shop_opentime,$shop_resttime,$shop_parkable,$shop_description);
        }

        if(strcmp($result, MSG_SUCCESS) == 0) {
            $this->doRespond(0, MSG_SUCCESS, null);
        }
        else {
            $this->doRespond(-1, MSG_FAIL, null);
        }
    }

    function  register_event () {

        $event_id = $this->input->post('event_id');
        $event_shop_id = $this->input->post('event_shop_id');
        $event_summary = $this->input->post('event_summary');
        $event_content = $this->input->post('event_content');
        $event_start = $this->input->post('event_start');
        $event_end = $this->input->post('event_end');
        $event_img_id = $this->input->post('event_img_id');
		$event_emoticon_checked = $this->input->post('event_emoticon_checked');

        $data = Array();
        $data["eventShopID"] = $event_shop_id;
        $data["eventContent"] = $event_content;

        if($event_summary != null) {
            $data["eventSummary"] = $event_summary;
        }

        if($event_start != null) {
            $data["eventStart"] = $event_start;
        }
        else {
            $data["eventStart"]  = $this->util->getDate();
        }

        if($event_end != null) {
            $data["eventEnd"] = $event_end;
        }
        else {
            $data["eventStart"]  = $this->util->getDate();
        }

        if($event_img_id != null && $event_img_id != 0 && strcmp($event_img_id, "0") != 0) {
            $data['eventImgID'] = $event_img_id;
        }
		
		$data['eventEmoticon'] = $event_emoticon_checked;
		
        if ($event_id != NULL) {
            $data['id'] = $event_id;
            $this->shop_model->updateEvent ( $data, MODE_UPDATE );
        } else {
            $this->shop_model->updateEvent ( $data, MODE_ADD);
        }

        $this->doRespond(0, "success", null );
    }

    function add_shop_consequence() {

        $shop_id = $this->input->post('id');
        $shop_consequence = $this->input->post('consequence');

        if($shop_id == null || $shop_consequence == null) {
            $this->doRespond(-1, MSG_FAIL, null );
            return;
        }

        $arrShop = $this->shop_model->getShopConArray();

        $cnt = count($arrShop);
        if($shop_consequence > $cnt) {
            $this->shop_model->updateConsequence($shop_id, $cnt + 1);
        }
        else {
            $this->shop_model->updateConsequence($arrShop[$shop_consequence-1]['id'], LIMIT_CONSEQUE);
            $this->shop_model->updateConsequence($shop_id, $shop_consequence);
        }

        $this->doRespond(0, MSG_SUCCESS, null );
    }

    function remove_shop_consequence() {
        $arr_id = $this->input->post('arr_id');
        $arr_id = json_decode($arr_id, true);

        if($arr_id == null && count($arr_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null );
            return;
        }

        $cnt = count($arr_id);
        for($i = 0; $i < $cnt; $i++) {
            $shop_id = $arr_id[$i];

            $arrShop = $this->shop_model->getShopConArray();
            $shop_cnt = count($arrShop);
            $index = 1;
            for($j = 0; $j < $shop_cnt; $j++) {
                if($arrShop[$j]['id']  != $shop_id) {
                    $this->shop_model->updateConsequence($arrShop[$j]['id'], $index);
                    $index++;
                }
                else {
                    $this->shop_model->updateConsequence($arrShop[$j]['id'], LIMIT_CONSEQUE);
                }
            }

        }

        $this->doRespond(0, MSG_SUCCESS, null );
    }

    function  shop_review_list() {
        $pageNum = $this->input->get('page_num');
        $shop_id = $this->input->get('shop_id');
        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['shopID'] = $shop_id;
        $data['arrShopComment'] = $this->shop_model->getShopCommentListWithShop($shop_id, TYPE_SHOPCOMMENT_USERREMARK, ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->shop_model->getShopCommentCountWithShop($shop_id, TYPE_SHOPCOMMENT_USERREMARK) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/shop/shop_review_list', $data);
    }

    function update_shop_comment() {
        $id = $this->input->post("comment_id");
        $status = $this->input->post("comment_status");
        if($id == null || $status == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->admin_model->updateShopComment($id, $status);

        $type = $this->input->post("comment_type");
        if($type != null && strcmp($type, TYPE_SHOPCOMMENT_QUESTIONANSWER) == 0) {
            $this->shop_model->updateShopAnswerWithShopCommentID($id);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  shop_question_list() {
        $pageNum = $this->input->get('page_num');
        $shop_id = $this->input->get('shop_id');
        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['shopID'] = $shop_id;
        $data['arrShopComment'] = $this->shop_model->getShopCommentList($shop_id, TYPE_SHOPCOMMENT_QUESTIONANSWER, ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->shop_model->getShopCommentCount($shop_id, TYPE_SHOPCOMMENT_QUESTIONANSWER) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/shop/shop_question_list', $data);
    }

    function  shop_wrong_info_list() {
        $pageNum = $this->input->get('page_num');
        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['arrShopComment'] = $this->shop_model->getShopCommentList1(TYPE_SHOPCOMMENT_WRONGINFO, ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->shop_model->getShopCommentCount1(TYPE_SHOPCOMMENT_WRONGINFO) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/shop/shop_wrong_info_list', $data);
    }

    function  shop_product_list() {
        $pageNum = $this->input->get('page_num');
        $shop_id = $this->input->get('shop_id');
        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['shopID'] = $shop_id;
        $data['arrProduct'] = $this->shop_model->getProductList($shop_id,ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->shop_model->getProductCount($shop_id) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/shop/shop_product_list', $data);
    }

    function  update_product() {
        $id = $this->input->post("product_id");
        $status = $this->input->post("product_status");
        $price = $this->input->post("product_price");
        $event_price = $this->input->post("product_event_price");
        $name = $this->input->post("product_name");
        if($id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $data = Array();
        if($status != null) {
            $data['productStatus'] = $status;
        }
        if($price != null) {
            $data['productPrice'] = $price;
        }
        if($event_price != null) {
            $data['productEventPrice'] = $event_price;
        }
        if($name != null) {
            $data['productName'] = $name;
        }

        $data['id'] = $id;
        $this->shop_model->updateProduct ( $data, MODE_UPDATE );

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  add_product() {
        $price = $this->input->post("product_price");
        $event_price = $this->input->post("product_event_price");
        $name = $this->input->post("product_name");
        $shop_id = $this->input->post("product_shop_id");
        if($name== null || $event_price == null || $shop_id== null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $data = Array();
        if($price != null) {
            $data['productPrice'] = $price;
        }
        if($event_price != null) {
            $data['productEventPrice'] = $event_price;
        }
        if($name != null) {
            $data['productName'] = $name;
        }

        $data['productShopID'] = $shop_id;

        $this->shop_model->updateProduct($data, MODE_ADD);
        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  update_shop_product() {
        $product_id = $this->input->get('product_id');
        if($product_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $product = $this->shop_model->getProduct($product_id);
        if($product == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $data = Array();
        $data['product'] = $product;
        $this->load->view('admin/shop/update_shop_product', $data);
    }

    function  request_shop_list() {

        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $shopName = $this->input->get('shop_name');
        $shopIdenti = $this->input->get('shop_identi');

        $data = Array();

        $arrShop = $this->shop_model->getRequestShopList($pageNum, $shopName, $shopIdenti);
        $data['totalPageCnt'] = ((int)($this->shop_model->getRequestShopCount($shopName, $shopIdenti) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;
        $data['arrShop'] = $arrShop;

        if($shopName != null) {
            $data['shopName'] = $shopName;
        }

        if($shopIdenti != null) {
            $data['shopIdenti'] = $shopIdenti;
        }

        //load the view
        $data['main_content'] = 'admin/shop/request_shop_list';
        $data['tab_id'] = ADMIN_TAB_ID_SHOP;
        $this->load->view('includes/template', $data);
    }

    function  remove_shop_arr() {
        $arr_shop_id = $this->input->post('arr_shop_id');
        $arr_shop_id = json_decode($arr_shop_id, true);
        if($arr_shop_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }
        
        $cnt = count($arr_shop_id);
        for($i = 0; $i < $cnt; $i++) {
        	$shop_id = $arr_shop_id[$i];
        	$this->shop_model->removeShop($shop_id);
        }
        $this->doRespond(0, MSG_SUCCESS, null);
    }


    function  remove_shop() {
        $shop_id = $this->input->post('shop_id');
        if($shop_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->shop_model->removeShop($shop_id);
        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function change_password() {
        $shop_id = $this->input->post('shop_id');
        $password = $this->input->post('password');

        if($shop_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->shop_model->updateShopPassword($shop_id, $password);
        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function use_remark() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $orderType = $this->input->get('order_type');
        $shopName = $this->input->get('shop_name');
        $postMan = $this->input->get('post_man');

        if($orderType == 0) {
            $orderType = null;
        }

        $data = Array();

        $data['arrShopComment'] = $this->shop_model->getShopUserRemarkList($pageNum, $shopName, $postMan, $orderType);
        $data['totalPageCnt'] = ((int)($this->shop_model->getShopUserRemarkCount($shopName, $postMan) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        if($shopName != null) {
            $data['shopName'] = $shopName;
        }

        if($orderType != null) {
            $data['orderType'] = $orderType;
        }

        if($postMan != null) {
            $data['postMan'] = $postMan;
        }

        //load the view
        $data['main_content'] = 'admin/shop/shop_use_remark';
        $data['tab_id'] = ADMIN_TAB_ID_MONITOR;
        $this->load->view('includes/template', $data);
    }

    function  write_use_remark() {
        $this->load->view('admin/shop/write_use_remark', null);
    }

    function  find_shop() {

        $pageNum = $this->input->get('page_num');
        if($pageNum == null) {
            $pageNum = 1;
        }

        $shopName = $this->input->get('shop_name');
        $shopStatus = $this->input->get('shop_status');
        $orderType = $this->input->get('order_type');

        $data = Array();
        $data['shopName'] = $shopName;
        $data['shopStatus'] = $shopStatus;
        $data['shopOrderType'] = $orderType;

        $data['arrShop'] = $this->shop_model->getUserRemarkShopList($pageNum, $shopName, $shopStatus,  $orderType);
        $data['totalPageCnt'] = ((int)($this->shop_model->getUserRemarkShopCount($shopName, $shopStatus) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/shop/find_shop', $data);
    }

    function   remove_user_remark_array() {
        $arr_comment = $this->input->post("arr_comment");

        $arr_comment = json_decode($arr_comment, true);

        if($arr_comment == null || count($arr_comment) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_comment);
        for($i = 0; $i < $cnt; $i++) {

            $id = $arr_comment[$i];
            $this->shop_model->removeShopComment($id);
            $this->admin_model->updateShopLevel($id);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  remove_shop_comment() {
        $comment_id_array = $this->input->post("comment_id_array");

        $comment_id_array = json_decode($comment_id_array, true);

        if($comment_id_array == null || count($comment_id_array) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($comment_id_array);
        for($i = 0; $i < $cnt; $i++) {
            $life_id = $comment_id_array[$i];
            $this->shop_model->removeShopComment($life_id);
        }

        $this->doRespond(0, "success", null);
    }

    function  manage_shop_comment() {
        $comment_id_array = $this->input->post("comment_id_array");

        $comment_id_array = json_decode($comment_id_array, true);

        if($comment_id_array == null || count($comment_id_array) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($comment_id_array);
        for($i = 0; $i < $cnt; $i++) {
            $life_id = $comment_id_array[$i];
            $this->shop_model->manageShopComment($life_id);
        }

        $this->doRespond(0, "success", null);
    }

    function get_shop_array() {
        $arr_shop = $this->input->get('arr_shop');
        $arr_shop = json_decode($arr_shop, true);

        if($arr_shop == null || count(arr_shop) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $data = Array();
        for($i = 0; $i < count($arr_shop); $i++) {
            $data[$i] = $this->shop_model->getShop($arr_shop[$i]);
        }

        $this->doRespond(0, "success", $data);
    }

    function write_user_remark() {
        $arr_shop = $this->input->post('arr_shop');
        $arr_shop = json_decode($arr_shop, true);

        $user_id = $this->input->post('user_id');
        $mark = $this->input->post('mark');
        $content = $this->input->post('content');

        if($arr_shop == null || count(arr_shop) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $post_man_id = ADMIN_ID;
        if($user_id != null) {
            $user_id = $this->member_model->getIDbyUserID($user_id);
            if($user_id == null) {
                $this->doRespond(-1, "지정한 유저가 존재하지 않습니다.", null);
                return;
            }

            $post_man_id = $user_id;
        }

        for($i = 0; $i < count($arr_shop); $i++) {
            $data = Array (
                "shopcommentPostManID" => $post_man_id,
                "shopcommentShopID" => $arr_shop[$i],
                "shopcommentContent" => $content,
                "shopcommentShopLevel"=>$mark,
                "shopcommentType" => TYPE_SHOPCOMMENT_USERREMARK,
                "shopcommentPostTime" => $this->util->getTime()
            );

            $this->shop_model->createShopComment($data);
            $this->shop_model->updateShopLevel($arr_shop[$i]);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
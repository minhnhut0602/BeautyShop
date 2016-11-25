<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Freetalk extends CI_Controller {

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

        $this->load->model ( 'freetalk_model' );
        $this->load->model ( 'cast_model' );
        $this->load->model ( 'shop_model' );
        $this->load->model ( 'banner_model' );
        $this->load->model ( 'admin_model' );
        $this->load->model ('image_model');
        $this->load->model ('member_model');

        $this->load->library('image_lib');

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

        $category_id = $this->input->get('category_id');
        $search_word = $this->input->get('search_word');

        $data = Array();
        $data['arrCategory'] = $this->freetalk_model->getCategoryList();
        $data['arrFreetalk'] = $this->freetalk_model->getFreetalkList($pageNum, $category_id, $search_word);
        $data['totalPageCnt'] = ((int)($this->freetalk_model->getFreetalkListCount($category_id, $search_word) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;
        $data['selectedCateogryID'] = $category_id;

        //load the view
        $data['main_content'] = 'admin/freetalk/freetalk';
        $data['tab_id'] = ADMIN_TAB_ID_TALK;
        $this->load->view('includes/template', $data);
    }


    function add_modify_freetalk() {
        $freetalk_id = $this->input->get("freetalk_id");
        $horizental = $this->input->get("horizental");

        $data = Array();
        $data['arrCategory'] = $this->freetalk_model->getCategoryList();
        if($freetalk_id != null) {
            $data['freetalk'] = $this->freetalk_model->getFreetalk($freetalk_id);
            if( $data['freetalk'] != null) {
                $data['freetalk']['arrFreetalkImg'] = $this->getImageList( $data['freetalk']['freetalkImgIDArrayString']);
            }
        }
        else {
            $data['freetalk'] = null;
        }

        if($horizental != null) {
            $data['isHorizental'] = true;
        }

        //load the view
        $this->load->view('admin/freetalk/add_modify_freetalk', $data);
    }

    function manage_freetalk() {
        //
        // data for view
        //

        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $tab_idx = $this->input->get('tab_idx');
        if($tab_idx == null) {
            $tab_idx = 0;
        }

        $category_id = $this->input->get('category_id');
        $is_best = $this->input->get('is_best');
        $start_date = $this->input->get('start_date');
        $end_date = $this->input->get('end_date');
        $cast_post_man = $this->input->get('cast_post_man');
        $order_type = $this->input->get('order_type');

        $data = Array();
        $data['arrCategory'] = $this->freetalk_model->getCategoryList();
        $data['arrFreetalk'] = $this->freetalk_model->getFreetalkListWithCond($pageNum, $category_id, $is_best, $start_date, $end_date, $cast_post_man, $order_type);
        $data['arrBestFreetalk'] = $this->freetalk_model->getBestFreetalkList(ADMIN_CNT_BEST_FREETALK, $category_id, $start_date, $end_date, $cast_post_man, $order_type);
        $data['totalPageCnt'] = ((int)($this->freetalk_model->getFreetalkListCountWithCond($category_id, $is_best, $start_date, $end_date, $cast_post_man) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        if($category_id != null) {
            $data['selectedCateogryID'] = $category_id;
        }
        if($cast_post_man != null) {
            $data['postMan'] = $cast_post_man;
        }
        if($is_best != null) {
            $data['isBest'] = $is_best;
        }
        if($start_date != null) {
            $data['startDate'] = $start_date;
        }
        if($end_date != null) {
            $data['endDate'] = $end_date;
        }
        if($order_type != null) {
            $data['orderType'] = $order_type;
        }

        //load the view
        $data['main_content'] = 'admin/freetalk/manage_freetalk';
        $data['tab_id'] = ADMIN_TAB_ID_TALK;
        $data['sub_tab_id'] = $tab_idx;
        $this->load->view('includes/template', $data);
    }

    function manage_comment() {
//
        // data for view
        //

        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $type = $this->input->get('type');

        if($type == null) {
            $type = 0;
        }

        $start_date = $this->input->get('start_date');
        $end_date = $this->input->get('end_date');
        $post_man = $this->input->get('post_man');
        $order_type = $this->input->get('order_type');

        $data = Array();

        if($type == 0){ // 전체
            $data['arrComment'] = $this->freetalk_model->getAllCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type);
            $data['totalPageCnt'] = $this->freetalk_model->getFreetalkCommentCountWithCond($start_date, $end_date, $post_man);
            $data['totalPageCnt'] = $data['totalPageCnt'] + $this->cast_model->getCastCommentCountWithCond($start_date, $end_date, $post_man);
            $data['totalPageCnt'] = $data['totalPageCnt'] + $this->banner_model->getBannerCommentCountWithCond($start_date, $end_date, $post_man);
            $data['totalPageCnt'] = $data['totalPageCnt'] + $this->freetalk_model->getDeclareCommentCountWithCond($start_date, $end_date, $post_man);
            $data['totalPageCnt'] = ((int)($data['totalPageCnt'] / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;

        }
        else if($type == 1) { // 자유톡
            $data['arrComment'] = $this->freetalk_model->getFreetalkCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type);
            $data['totalPageCnt'] = ((int)($this->freetalk_model->getFreetalkCommentCountWithCond($start_date, $end_date, $post_man) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        }
        else if($type == 2){ // 캐스트
            $data['arrComment'] = $this->cast_model->getCastCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type);
            $data['totalPageCnt'] = ((int)($this->cast_model->getCastCommentCountWithCond($start_date, $end_date, $post_man) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        }
        else if($type == 3){ // 상점
            $data['arrComment'] = $this->banner_model->getBannerCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type);
            $data['totalPageCnt'] = ((int)($this->banner_model->getBannerCommentCountWithCond($start_date, $end_date, $post_man) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        }
        else if($type == 4){ // 신고
            $data['arrComment'] = $this->freetalk_model->getDeclareCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type);
            $data['totalPageCnt'] = ((int)($this->freetalk_model->getDeclareCommentCountWithCond($start_date, $end_date, $post_man) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        }

        $data['currentPageNum'] = $pageNum;

        if($type != null) {
            $data['type'] = $type;
        }
        if($post_man != null) {
            $data['postMan'] = $post_man;
        }
        if($start_date != null) {
            $data['startDate'] = $start_date;
        }
        if($end_date != null) {
            $data['endDate'] = $end_date;
        }
        if($order_type != null) {
            $data['orderType'] = $order_type;
        }

        //load the view
        $data['main_content'] = 'admin/freetalk/manage_comment';
        $data['tab_id'] = ADMIN_TAB_ID_TALK;

        //echo json_encode($data['arrComment']);
        $this->load->view('includes/template', $data);
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

    function remove_freetalk_array() {
        $arr_freetalk_id = $this->input->post("arr_freetalk_id");

        $arr_freetalk_id = json_decode($arr_freetalk_id, true);

        if($arr_freetalk_id == null || count($arr_freetalk_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_freetalk_id);
        for($i = 0; $i < $cnt; $i++) {
            $freetalk_id = $arr_freetalk_id[$i];
            $this->freetalk_model->removeFreetalk($freetalk_id);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function remove_freetalk() {
        $cast_id = $this->input->post("freetalk_id");
        if($cast_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->freetalk_model->removeFreetalk($cast_id);

        $this->doRespond(0, "success", null);
    }

    function visible_freetalk() {
        $cast_id = $this->input->post("freetalk_id");
        if($cast_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $visible = $this->input->post("visible");
        if($visible == null) {
            $visible = STATUS_HIDE;
        }
        else {
            if($visible == true) {
                $visible = STATUS_LIFE;
            }
            else {
                $visible = STATUS_HIDE;
            }
        }

        $this->freetalk_model->setFreetalkStatus($cast_id, $visible);

        $this->doRespond(0, "success", null);
    }

    function add_category() {
        $category_name = $this->input->post("category_name");

        if($category_name == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->freetalk_model->addCategory($category_name);

        $this->doRespond(0, "success", null);
    }

    function  write_freetalk() {
        $freetalk_id = $this->input->post("freetalk_id");
        $freetalk_content = $this->input->post("freetalk_content");
        $freetalk_img_array = $this->input->post("freetlk_img_array");
        $freetalk_category_id = $this->input->post("freetalk_category_id");
        $freetalk_user_id = $this->input->post("freetalk_user_id");

        if ($freetalk_content == null || $freetalk_category_id < 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $post_man_id = ADMIN_ID;
        if($freetalk_user_id != null) {
            $user_id = $this->member_model->getIDbyUserID($freetalk_user_id);
            if($user_id == null) {
                $this->doRespond(-1, "지정한 유저가 존재하지 않습니다.", null);
                return;
            }

            $post_man_id = $user_id;
        }

        if($freetalk_id == null) {
            $this->freetalk_model->createFreetalk($freetalk_img_array, $freetalk_content, $freetalk_category_id, $post_man_id);
        }
        else {
            $this->freetalk_model->modifyFreetalk($freetalk_id, $freetalk_img_array,$freetalk_content, $freetalk_category_id, $post_man_id);
        }
        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  add_like_count() {
        $arr_freetalk_id = $this->input->post("arr_freetalk_id");

        $arr_freetalk_id = json_decode($arr_freetalk_id, true);

        if($arr_freetalk_id == null || count($arr_freetalk_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_freetalk_id);
        for($i = 0; $i < $cnt; $i++) {
            $freetalk_id = $arr_freetalk_id[$i];
            $add_like_cnt = rand(1, 10);
            $this->freetalk_model->addLikeCount($freetalk_id, $add_like_cnt);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function set_best_freetalk() {
        $arr_freetalk_id = $this->input->post("arr_freetalk_id");
        $is_set_best = $this->input->post("is_set_best");

        $arr_freetalk_id = json_decode($arr_freetalk_id, true);

        if($arr_freetalk_id == null || count($arr_freetalk_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        if($is_set_best == 0) {
            $is_set_best = true;

            $best_cnt = $this->freetalk_model->getBestFreetalkCount();

            if($best_cnt >= ADMIN_CNT_BEST_FREETALK || ($best_cnt+ count($arr_freetalk_id)) > ADMIN_CNT_BEST_FREETALK) {
                $this->doRespond(-1, MSG_FAIL_SET_BEST, null);
                return;
            }
        }
        else {
            $is_set_best = false;
        }

        $cnt = count($arr_freetalk_id);
        $ret = MSG_SUCCESS;
        for($i = 0; $i < $cnt; $i++) {
            $freetalk_id = $arr_freetalk_id[$i];
            $result = $this->freetalk_model->setBest($freetalk_id, $is_set_best);

            if($result == false) {
                $ret = MSG_FAIL;
            }
            else if($result != true) {
                $ret = $result;
            }
        }

        if($ret != MSG_SUCCESS) {
            $this->doRespond(-1, $ret, null);
        }
        else {
            $this->doRespond(0, MSG_SUCCESS, null);
        }
    }

    function  remove_comment_array() {

        $arr_comment = $this->input->post("arr_comment");

        $arr_comment = json_decode($arr_comment, true);

        if($arr_comment == null || count($arr_comment) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_comment);
        $arr_type = $this->input->post("arr_type");
        $arr_type = json_decode($arr_type, true);
        for($i = 0; $i < $cnt; $i++) {
            $type = $arr_type[$i];
            $id = $arr_comment[$i];
            if($type == 1 || $type == 4) {
                $this->freetalk_model->removeFreetalkComment($id);
            }
            else if($type == 2) {
                $this->cast_model->removeCastComment($id);
            }
            else if($type == 3) {
                $this->banner_model->removeBannerComment($id);
            }
        }

        $this->doRespond(0, MSG_SUCCESS, null);

    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
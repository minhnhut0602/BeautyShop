<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Cast extends CI_Controller {

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
        $this->load->model ( 'cast_model' );
        $this->load->model ( 'admin_model' );
        $this->load->model ('image_model');
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

        $tab_idx = $this->input->get('tab_idx');
        if($tab_idx == null) {
            $tab_idx = 0;
        }

        $search_word = $this->input->get('search_word');

        $category_id = $this->input->get('category_id');
        $order_type = $this->input->get('order_type');

        $data = Array();
        $data['arrCategory'] = $this->cast_model->getCategoryList();
        $data['arrCast'] = $this->cast_model->getCastListWithSearchWord($pageNum, ADMIN_MAX_PAGE_ITEM_CNT, $category_id, $search_word, $order_type);
        $data['arrBestCast'] = $this->cast_model->getBestCastList(ADMIN_CNT_BEST_CAST, $category_id, $search_word, $order_type);
        $data['totalCnt'] = $this->cast_model->getCastCount($category_id, $search_word);
        $data['totalPageCnt'] = ((int)( $data['totalCnt'] / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        if($category_id != null) {
            $data['selectedCateogryID'] = $category_id;
        }
        if($search_word != null) {
            $data['searchWord'] = $search_word;
        }
        if($order_type != null) {
            $data['orderType'] = $order_type;
        }

        //load the view
        $data['main_content'] = 'admin/cast/cast';
        $data['tab_id'] = ADMIN_TAB_ID_CAST;
        $data['sub_tab_id'] = $tab_idx;
        $this->load->view('includes/template', $data);
    }

    function add_modify_cast() {
        $cast_id = $this->input->get("cast_id");
        $data = Array();
        $data['arrCategory'] = $this->cast_model->getCategoryList();
        if($cast_id != null) {
            $data['cast'] = $this->cast_model->getCast($cast_id);
            $data['cast']['arrCastDetail'] = $this->cast_model->getCastDetailListByCastID($cast_id);
        }
        else {
            $data['cast'] = null;
        }

        //load the view
        $this->load->view('admin/cast/add_modify_cast', $data);
    }

    private function doRespond($p_result_code, $p_result_msg, $p_result) {
        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    function remove_cast() {
        $arr_cast_id = $this->input->post("arr_cast_id");

        $arr_cast_id = json_decode($arr_cast_id, true);

        if($arr_cast_id == null || count($arr_cast_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_cast_id);
        for($i = 0; $i < $cnt; $i++) {
            $cast_id = $arr_cast_id[$i];
            $this->cast_model->removeCast($cast_id);
        }

        $this->doRespond(0, "success", null);
    }

    function  write_cast() {
        $cast_title = $this->input->post("cast_title");
        if($cast_title == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_detail = $this->input->post("cast_detail");
        if($cast_detail == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_detail = json_decode($cast_detail, true);
        if(count($cast_detail) == 0) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_id = $this->input->post("cast_id");
        $cast_category_id = $this->input->post("cast_category_id");
        if($cast_id == null) {
            $cast = $this->cast_model->createCast($cast_title, $cast_category_id, $cast_detail[0]['castdetailImgID']);
        }
        else {
            $this->cast_model->modifyCast($cast_id, $cast_category_id,  $cast_title, $cast_detail[0]['castdetailImgID']);
            $cast = Array(
                'id' => $cast_id
            );
        }

        if($cast_detail != null) {
            $cnt = count($cast_detail);
            for ($i = 0; $i < $cnt; $i++) {
                $data = $cast_detail[$i];
                if ($data['id'] == null) {
                    $this->cast_model->createCastDetail($data['castdetailPublish'], $data['castdetailContent'], $data['castdetailImgID'], $cast['id'], ($i));
                } else {
                    $this->cast_model->modifyCastDetail($data['id'], $data['castdetailPublish'], $data['castdetailContent'], $data['castdetailImgID'], $cast['id'], ($i));
                }
            }
        }

        $this->doRespond(0, "success", $cast);
    }

    function  write_cast_detail() {
        $cast_detail_img_id = $this->input->post("cast_detail_img_id");
        if($cast_detail_img_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_detail_content = $this->input->post("cast_detail_content");
        if($cast_detail_content == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_detail_index = $this->input->post("cast_detail_index");
        if($cast_detail_index == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_id = $this->input->post("cast_id");
        if($cast_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $cast_detail_id = $this->input->post("cast_detail_id");
        $cast_detail = Array(
            "id" => $cast_detail_id
            );
        if($cast_detail_id == null) {
            $cast_detail = $this->cast_model->createCastDetail($cast_detail_content, $cast_detail_img_id, $cast_id, $cast_detail_index);
        }
        else {
            $this->cast_model->modifyCastDetail($cast_detail_id, $cast_detail_content, $cast_detail_img_id, $cast_id, $cast_detail_index);
        }

        $this->doRespond(0, "success", $cast_detail );
    }

    function remove_cast_detail() {
        $cast_detail_id = $this->input->post("cast_detail_id");
        if($cast_detail_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }
        $cast_detail = $this->cast_model->getCastDetail($cast_detail_id);
        $this->cast_model->removeCastDetail($cast_detail_id);

        $cast_id = $cast_detail['castdetailCastID'];
        $cast_detal_index = $cast_detail['castdetailIdx'];

        $cast_detail_array = $this->cast_model->getCastDetailListByCastID($cast_id);
        $cnt = count($cast_detail_array);

        for ($i = 0; $i < $cnt; $i++) {
            $data = $cast_detail_array[$i];

            $this->cast_model->modifyCastDetail($data['id'], $data['castdetailContent'], $data['castdetailImgID'], $cast_id, ($i));
        }

        $data=Array();
        $data['index'] = $cast_detal_index;
        $this->doRespond(0, "success", $data);
    }

    function add_category() {
        $category_name = $this->input->post("category_name");

        if($category_name == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->cast_model->addCategory($category_name);

        $this->doRespond(0, "success", null);
    }

    function  add_like_count() {
        $arr_cast_id = $this->input->post("arr_cast_id");

        $arr_cast_id = json_decode($arr_cast_id, true);

        if($arr_cast_id == null || count($arr_cast_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_cast_id);
        for($i = 0; $i < $cnt; $i++) {
            $cast_id = $arr_cast_id[$i];
            $add_like_cnt = rand(1, 10);
            $this->cast_model->addLikeCount($cast_id, $add_like_cnt);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function set_best_cast() {
        $arr_cast_id = $this->input->post("arr_cast_id");
        $is_set_best = $this->input->post("is_set_best");

        $arr_cast_id = json_decode($arr_cast_id, true);

        if($arr_cast_id == null || count($arr_cast_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        if($is_set_best == 0) {
            $is_set_best = true;
            $best_cnt = $this->cast_model->getBestCastCount();

            if($best_cnt >= ADMIN_CNT_BEST_CAST || ($best_cnt+ count($arr_cast_id)) > ADMIN_CNT_BEST_CAST) {
                $this->doRespond(-1, MSG_FAIL_SET_BEST, null);
                return;
            }
        }
        else {
            $is_set_best = false;
        }

        $cnt = count($arr_cast_id);
        $ret = MSG_SUCCESS;
        for($i = 0; $i < $cnt; $i++) {
            $cast_id = $arr_cast_id[$i];
            $result = $this->cast_model->setBest($cast_id, $is_set_best);

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

    function  move_up() {
        $life_id = $this->input->post('cast_id');
        $is_most = $this->input->post('is_most');
        if($life_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }
        $life = $this->cast_model->getCast($life_id);
        $this->cast_model->move_up($life,$is_most);

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  move_down() {
        $life_id = $this->input->post('cast_id');
        $is_most = $this->input->post('is_most');
        if($life_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $life = $this->cast_model->getCast($life_id);
        $this->cast_model->move_down($life,$is_most);

        $this->doRespond(0, MSG_SUCCESS, null);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
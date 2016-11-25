<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Lifeinfo extends CI_Controller {

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
        $this->load->model ( 'lifeinfo_model' );
        $this->load->model ( 'admin_model' );
        $this->load->model ('image_model');
        $this->load->model ( 'banner_model' );
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
        $data['arrCategory'] = $this->lifeinfo_model->getCategoryList();
        $data['arrLifeInfo'] = $this->lifeinfo_model->getLifeInfoList($category_id, $pageNum, $search_word);
        $data['totalPageCnt'] = ((int)($this->lifeinfo_model->getLifeInfoListCount($search_word) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;
        $data['selectedCateogryID'] = $category_id;

        //load the view
        $data['main_content'] = 'admin/lifeinfo/lifeinfo';
        $data['tab_id'] = ADMIN_TAB_ID_LIFE;
        $this->load->view('includes/template', $data);
    }


    function add_modify_lifeinfo() {
        $freetalk_id = $this->input->get("lifeinfo_id");
        $data = Array();
        $data['arrCategory'] = $this->lifeinfo_model->getCategoryList();
        if($freetalk_id != null) {
            $data['lifeinfo'] = $this->lifeinfo_model->getLife($freetalk_id);
            if( $data['lifeinfo'] != null) {
                if($data['lifeinfo']['lifeAdsID'] != null) {
                    $data['lifeinfo']['lifeAds'] = $this->banner_model->getAds($data['lifeinfo']['lifeAdsID']);
                }
            }
        }
        else {
            $data['freetalk'] = null;
        }

        //load the view
        $this->load->view('admin/lifeinfo/add_modify_lifeinfo', $data);
    }

    function   search_life_click() {
        $life_id = $this->input->get('lifeinfo_id');
        $start_date  = $this->input->get('start_date');
        $end_date  = $this->input->get('end_date');

        if($life_id == null || $start_date == null || $end_date == null) {
            return;
        }

        $data['arrLifeClickInfo'] = $this->lifeinfo_model->getClickInfo($life_id, $start_date, $end_date);

        //load the view
        $this->load->view('admin/lifeinfo/click_search_result', $data);
    }


    private function doRespond($p_result_code, $p_result_msg, $p_result) {
        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    function remove_lifeinfo_array() {
        $arr_life_id = $this->input->post("lifeinfo_id_array");

        $arr_life_id = json_decode($arr_life_id, true);

        if($arr_life_id == null || count($arr_life_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_life_id);
        for($i = 0; $i < $cnt; $i++) {
            $life_id = $arr_life_id[$i];
            $this->lifeinfo_model->removeLifeInfo($life_id);
        }

        $this->doRespond(0, "success", null);
    }

    function remove_lifeinfo() {
        $cast_id = $this->input->post("lifeinfo_id");
        if($cast_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->lifeinfo_model->removeLifeInfo($cast_id);

        $this->doRespond(0, "success", null);
    }

    function add_category() {
        $category_name = $this->input->post("category_name");

        if($category_name == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->lifeinfo_model->addCategory($category_name);

        $this->doRespond(0, "success", null);
    }


    function  write_lifeinfo() {
        $adsTitle = $this->input->post("adsTitle");
        $adsBannerURL = $this->input->post("adsBannerURL");
        $adsImgID = $this->input->post("adsImgID");
        $categoryID = $this->input->post("categoryID");
        $lifeinfoID = $this->input->post("lifeinfoID");
        $lifeSubject = $this->input->post("lifeSubject");
        $lifeExplain = $this->input->post("lifeExplain");

        if ($adsImgID == null || $categoryID < 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $ads = $this->lifeinfo_model->createAds($adsImgID, $adsBannerURL);

        if($ads == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        if($lifeinfoID == null) {
            $this->lifeinfo_model->createLifeInfo($ads['id'], $adsTitle, $categoryID,$lifeSubject, $lifeExplain);
        }
        else {
            $this->lifeinfo_model->modifyLifeInfo($lifeinfoID, $ads['id'], $adsTitle, $categoryID, $lifeSubject, $lifeExplain);
        }
        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  set_best_life() {
        $life_id = $this->input->post('life_id');
        if($life_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->lifeinfo_model->set_best($life_id);

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  move_up() {
        $life_id = $this->input->post('life_id');
        if($life_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }
        $life = $this->lifeinfo_model->getLife($life_id);
        $this->lifeinfo_model->move_up($life);

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  move_down() {
        $life_id = $this->input->post('life_id');
        if($life_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $life = $this->lifeinfo_model->getLife($life_id);
        $this->lifeinfo_model->move_down($life);

        $this->doRespond(0, MSG_SUCCESS, null);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
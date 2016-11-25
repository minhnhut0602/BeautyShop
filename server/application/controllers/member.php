<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Member extends CI_Controller {

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
        $this->load->model ( 'member_model' );
        $this->load->model ( 'admin_model' );
        $this->load->model ( 'gcm_model' );
        $this->load->library('image_lib');
        $this->load->helper ( 'url' );
        date_default_timezone_set('Asia/Seoul');
    }

    /*****************
     *  web page
     *
     **************************************************************************/
    function index() {
        //
        // data for view
        //

        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $userID = $this->input->get('userID');
        $userSex = $this->input->get('userSex');
        $userLocation = $this->input->get('userLocation');
        $userStatus = $this->input->get('userStatus');
        $userLevel = $this->input->get('userLevel');
        $userRecommender = $this->input->get('userRecommender');

        $arrUser = Array();
        $arrUser = $this->member_model->getUserList($pageNum, $userID, $userSex, $userLocation, $userStatus, $userLevel, $userRecommender);
        $data['arrUser'] = $arrUser;
        $data['arrLocation'] = $this->gcm_model->getLocationList();
        $data['totalPageCnt'] = ((int)($this->member_model->getUserCount($userID, $userSex, $userLocation, $userStatus, $userLevel, $userRecommender) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        if($userID != null) {
            $data['userID'] = $userID;
        }

        if($userSex != null) {
            $data['userSex'] = $userSex;
        }

        if($userLocation != null) {
            $data['userLocation'] = $userLocation;
        }

        if($userStatus != null) {
            $data['userStatus'] = $userStatus;
        }

        if($userLevel != null) {
            $data['userLevel'] = $userLevel;
        }

        if($userRecommender != null) {
            $data['userRecommender'] = $userRecommender;
        }
        $data['tab_id'] = ADMIN_TAB_ID_MEMEBER;

        //load the view
        $data['main_content'] = 'admin/member/member';
        $this->load->view('includes/template', $data);
    }

    function  show_member() {
        $user_id = $this->input->get('user_id');

        if($user_id == null) {
            return;
        }

        //load the view
        $data['user_id'] = $user_id;
        $data['userInfo'] = $this->admin_model->getUser($user_id);
        $data['userInfo']['userRecommenderCnt'] = $this->member_model->getUserRecommenderCount($user_id);
        $data['is_update'] = 0;
        $this->load->view('admin/member/update_member', $data);
    }

    function  update_member() {
        $user_id = $this->input->get('user_id');

        if($user_id == null) {
            return;
        }

        //load the view
        $data['user_id'] = $user_id;
        $data['userInfo'] = $this->admin_model->getUser($user_id);
        $data['userInfo']['userRecommenderCnt'] = $this->member_model->getUserRecommenderCount($user_id);
        $data['is_update'] = 1;
        $this->load->view('admin/member/update_member', $data);
    }

    function  shop_member_list() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $arrUser = $this->member_model->getShopUserList($pageNum);
        $data['arrUser'] = $arrUser;
        $data['totalPageCnt'] = ((int)($this->member_model->getShopUserCount() / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        //load the view
        $data['main_content'] = 'admin/member/shop_member';
        $data['tab_id'] = ADMIN_TAB_ID_MEMEBER;
        
        $this->load->view('includes/template', $data);
    }

    function show_shop_member() {
        $user_id = $this->input->get('user_id');

        if($user_id == null) {
            return;
        }

        $data['user_id'] = $user_id;
        $data['userInfo'] = $this->member_model->getShopUser($user_id);

        //load the view
        $this->load->view('admin/member/show_shop_member', $data);
    }

    function  update_shop_member() {
        $user_id = $this->input->get('user_id');

        if($user_id == null) {
            return;
        }

        //load the view
        $data['user_id'] = $user_id;
        $data['userInfo'] = $this->admin_model->getUser($user_id);
        $data['userInfo']['userRecommenderCnt'] = $this->member_model->getUserRecommenderCount($user_id);
        $data['is_update'] = 1;
        $this->load->view('admin/member/update_shop_member', $data);
    }


    /**************************************************************************
     *  response functions
     *
     **************************************************************************/
    private function doRespond($p_result_code, $p_result_msg, $p_result) {
        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    function  save_level() {
        $user_id = $this->input->post('user_id');
        $user_level = $this->input->post('user_level');
        if($user_id == null || $user_level == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->member_model->updateLevel($user_id, $user_level);

        $this->doRespond(0, "success", null);
    }

    function  update_user() {
        $user_id = $this->input->post('user_id');
        $user_point = $this->input->post('user_point');
        $user_status = $this->input->post('user_status');
        if($user_id == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }
        $this->member_model->updateUserInfo($user_id, $user_point, $user_status);

        $this->doRespond(0, "success", null);
    }

    function  update_status() {
        $user_id = $this->input->post('user_id');
        $user_status = $this->input->post('user_status');
        if($user_id == null || $user_status == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->member_model->updateStatus($user_id, $user_status);

        $this->doRespond(0, "success", null);
    }

    function  remove_user() {
        $user_id = $this->input->post('user_id');
        if($user_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->member_model->removeUser($user_id);

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function process_connection() {
        $shopID = $this->input->post('shop_id');
        if($shopID == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->member_model->processConnectShop($shopID);

        $this->doRespond(0, MSG_SUCCESS, null);
    }

}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
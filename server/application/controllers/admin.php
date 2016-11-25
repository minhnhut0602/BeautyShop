<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Admin extends CI_Controller {

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

        if(ADMIN_DEBUG_MODE) { // testing
            error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
        }

        $this->load->database ();
        $this->load->model ( 'admin_model');
        $this->load->model ( 'member_model');
        $this->load->model ( 'shop_model');
        $this->load->model ( 'setting_model' );
        $this->load->model ( 'banner_model' );
        $this->load->model ( 'lifeinfo_model' );
        $this->load->model('image_model');

        $this->load->library('session');

        date_default_timezone_set('Asia/Seoul');
    }

    //
    // shop view
    //
	public function index()
    {

        $log_out = $this->input->get('log_out');

        if($log_out == 1) {
            $this->member_model->updateRecommendID(ADMIN_ID, 'N');
        }

        $admin_info = $this->admin_model->getAdminUserInfo();
        $logged_in = $this->session->userdata('logged_in');

        if($admin_info['userRecommenderID'] == 'Y' || ($logged_in == TRUE && $log_out != 1)) {
            //
            // data for view
            //

            // shopInfo
            $shopInfo = Array();
            $shopInfo['totalShopCnt'] = $this->admin_model->getTotalShopCount();
            $shopInfo['totalLiveShopCnt'] = $this->admin_model->getLiveShopCount();
            $shopInfo['totalHairShopCnt'] = $this->admin_model->getHairShopCount();
            $shopInfo['totalLiveHairShopCnt'] = $this->admin_model->getLiveHairShopCount();
            $shopInfo['totalNailShopCnt'] = $this->admin_model->getNailShopCount();
            $shopInfo['totalLiveNailShopCnt'] = $this->admin_model->getLiveNailShopCount();
            $shopInfo['totalSoknunShopCnt'] = $this->admin_model->getSoknunShopCount();
            $shopInfo['totalLiveSoknunShopCnt'] = $this->admin_model->getLiveSoknunShopCount();
            $shopInfo['totalWaksingShopCnt'] = $this->admin_model->getWaksingShopCount();
            $shopInfo['totalLiveWaksingShopCnt'] = $this->admin_model->getLiveWaksingShopCount();
            $shopInfo['totalSkinShopCnt'] = $this->admin_model->getSkinShopCount();
            $shopInfo['totalLiveSkinShopCnt'] = $this->admin_model->getLiveSkinShopCount();
            $shopInfo['totalMassageShopCnt'] = $this->admin_model->getMassageShopCount();
            $shopInfo['totalLiveMassageShopCnt'] = $this->admin_model->getLiveMassageShopCount();            
            $shopInfo['totalBanyongguShopCnt'] = $this->admin_model->getBanyongguShopCount();
            $shopInfo['totalLiveBanyongguShopCnt'] = $this->admin_model->getLiveBanyongguShopCount();            
            $shopInfo['totalTatuShopCnt'] = $this->admin_model->getTatuShopCount();
            $shopInfo['totalLiveTatuShopCnt'] = $this->admin_model->getLiveTatuShopCount();            
            
            $shopInfo['todayRequestShopCnt'] = $this->admin_model->getTodayRequestShopCount();
            $shopInfo['totalRequestShopCnt'] = $this->admin_model->getRequestShopCount();
            $data['shopInfo'] = $shopInfo;

            // question Info
            $data['todayQuestionCnt'] = $this->admin_model->getTodayQuestionCount();

            // talk info
            $talkInfo = Array();
            $talkInfo['totalTalkCnt'] = $this->admin_model->getTotalTalkCount();
            $talkInfo['todayTalkCnt'] = $this->admin_model->getTodayTalkCount();
            $data['talkInfo'] = $talkInfo;

            // wrong info
            $data['wrongInfoCnt'] = $this->admin_model->getWrongInfoCount();

            // member Info
            $memberInfo = Array();
            $memberInfo['totalMemberCnt'] = $this->admin_model->getMemberCount();
            $memberInfo['totalTodayMemberCnt'] = $this->admin_model->getTodayMemberCount();
            $memberInfo['totalManMemberCnt'] = $this->admin_model->getManMemberCount();
            $memberInfo['totalWomanMemberCnt'] = $this->admin_model->getWomanMemberCount();
            $memberInfo['total10MemberCnt'] = $this->admin_model->getPeopleCountWithAge(0, 20);
            $memberInfo['total20MemberCnt'] = $this->admin_model->getPeopleCountWithAge(20, 30);
            $memberInfo['total30MemberCnt'] = $this->admin_model->getPeopleCountWithAge(30, 40);
            $memberInfo['total40MemberCnt'] = $this->admin_model->getPeopleCountWithAge(40, 50);
            $memberInfo['total50MemberCnt'] = $this->admin_model->getPeopleCountWithAge(50, 60);
            $memberInfo['totalAccessCnt'] = $this->admin_model->getAccessCount();
            $data['memberInfo'] = $memberInfo;

            // low heart comment
            $data['arrLowHeartComment'] = $this->admin_model->getLowHeartCommmentArray(ADMIN_MAX_PAGE_ITEM_CNT);

            // declare comment
            $data['arrDeclareComment'] = $this->admin_model->getDeclareCommentArray(ADMIN_MAX_PAGE_ITEM_CNT);

            // live event
            $data['arrLiveEvent'] = $this->admin_model->getLiveEventArray();

            // download info
            $download = Array();
            $download['totalDownloadCnt'] = $this->admin_model->getMemberCount();
            $download['todayDownloadCnt'] = $this->admin_model->getTodayMemberCount();
            $download['todayRemoveCnt'] = $this->admin_model->getTodayOutMemberCount();
            $download['totalRemoveCnt'] = $this->admin_model->getTotalOutMemberCount();
            $download['totalLiveCnt'] = $download['totalDownloadCnt'] - $download['totalRemoveCnt'];

            $data['downloadInfo'] = $download;
            //load the view
            $data['main_content'] = 'admin/admin';
            $this->load->view('includes/template', $data);
        }
        else {

            $data = Array();

            // login
            $this->load->view('admin/login', $data);
        }
    }

    function  low_remark_comment_page() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['arrLowHeartComment'] = $this->admin_model->getLowHeartCommmentArray(ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->admin_model->getLowHeartCommmentListCount() / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/low_remark_comment_page', $data);
    }

    function  declare_comment_page() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['arrDeclareComment'] = $this->admin_model->getDeclareCommentArray(ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->admin_model->getDeclareCommentCount() / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/declare_comment_page', $data);
    }

    function  question_list() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $data['arrQuestion'] = $this->admin_model->getQuestionList(ADMIN_TYPE_ADMIN_QUESTION_SHOP_NORMAL, ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['totalPageCnt'] = ((int)($this->admin_model->getQuestionCount(ADMIN_TYPE_ADMIN_QUESTION_SHOP_NORMAL) / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        $this->load->view('admin/question_list', $data);
    }


    function  set_point_page() {
        $data = Array();
        $data['point'] = $this->setting_model->getPointArray();
        $this->load->view('admin/point_page', $data);
    }

    function  manage_main_page() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();

        $data['arrEvent'] = $this->admin_model->getEventConsequenceArray(ADMIN_MAX_PAGE_ITEM_CNT, $pageNum);
        $data['currentPageNum'] = $pageNum;
        $data['totalPageCnt'] = ((int)($this->admin_model->getEventConsequenceCount() / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;

        $this->load->view('admin/manage_main_page', $data);
    }

	function agreement(){
		$type = $this->input->get('type');
		$data = Array();
		$data =  $this->admin_model->getAgreement($type);
		$this->load->view('admin/agreement', $data[0]);
	}	

	function register_agreement(){
		$type = $this->input->post('type');
		$content = $this->input->post('content');
		$this->admin_model->setAgreement($type,$content);
	}
	
    function manage_banner() {

        $data['arrMainBanner'] = $this->banner_model->getBannerArrayWithType(ADMIN_TYPE_MAIN_BANNER);
        $logOutBanner = $this->banner_model->getBannerArrayWithType(ADMIN_TYPE_LOGOUT_BANNER);
        $data['logoutBanner'] = $logOutBanner[0];
        $data['arrCastBanner'] =  $this->banner_model->getBannerArrayWithType(ADMIN_TYPE_CAST_BANNER);

        $data['main_content'] = 'admin/banner/main_banner';
        $this->load->view('includes/template', $data);
    }

    function  show_modify_banner() {
        $bannerID = $this->input->get('banner_id');

        if($bannerID == null) {
            return;
        }

        $data = Array();
        $data['banner'] = $this->banner_model->getBanner($bannerID);
        $data['banner']['ads'] =  $this->banner_model->getAds($data['banner']['bannerAdsID']);
        $this->load->view('admin/banner/modify_banner', $data);
    }

    function   search_life_click() {
        $banner_id = $this->input->get('banner_id');
        $start_date  = $this->input->get('start_date');
        $end_date  = $this->input->get('end_date');

        if($banner_id == null || $start_date == null || $end_date == null) {
            return;
        }

        $data['arrBannerClickInfo'] = $this->banner_model->getClickInfo($banner_id, $start_date, $end_date);

        //load the view
        $this->load->view('admin/banner/click_search_result', $data);
    }

    //
    // request and response
    //
    private function doRespond($p_result_code, $p_result_msg, $p_result) {
        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    function  login_admin() {
        $id = $this->input->post("id");
        $password = $this->input->post("password");
        $save_login = $this->input->post("save_info");

        if($id == null || $password == null) {
            Util::doRespond($this, -1, MSG_FAIL, null);
            return;
        }

        $admin_info = $this->admin_model->getAdminUserInfo();
        if($admin_info == null) {
            Util::doRespond($this, -1, MSG_FAIL, null);
            return;
        }

        if($admin_info['userNickname'] != $id || $admin_info['userPassword'] != $password) {
            Util::doRespond($this, -1, MSG_FAIL, null);
            return;
        }

        // uset userRecommenderID into save login info for Admin.
        if($save_login == 1) { // true
            $this->member_model->updateRecommendID(ADMIN_ID, 'Y');
        }

        $newdata = array(
            'logged_in' => TRUE
        );

        $this->session->set_userdata($newdata);

        Util::doRespond($this, 0, MSG_SUCCESS, null);
    }

    function update_shop_comment() {

        $id = $this->input->post("comment_id");
        $status = $this->input->post("comment_status");

        if($id == null || $status == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->admin_model->updateShopComment($id, $status);

        Util::doRespond($this, 0, MSG_SUCCESS, null);
    }

    function update_comment() {
        $id = $this->input->post("comment_id");
        $status = $this->input->post("comment_status");
        if($id == null || $status == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->admin_model->updateComment($id, $status);

        Util::doRespond($this, 0, MSG_SUCCESS, null);
    }

    function  set_point() {
        $point_array = $this->input->post("point_array");
        if($point_array == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->setting_model->setPointArray($point_array);

        Util::doRespond($this, 0, MSG_SUCCESS, null);
    }

    function  set_main_banner() {
        $point_array = $this->input->post("banner_array");
        if($point_array == null) {
            Util::doRespond($this, -1, MSG_FAIL, null);
        }

        $this->admin_model->setBannerArray($point_array);

        Util::doRespond($this, 0, MSG_SUCCESS, null);
    }

    function add_event_consequence() {

        $shop_id = $this->input->post('id');
        $shop_consequence = $this->input->post('consequence');

        if($shop_id == null || $shop_consequence == null) {
            Util::doRespond($this, -1, MSG_FAIL, null );
            return;
        }

        $event = $this->shop_model->getEvent($shop_id);
        if($event == null) {
            Util::doRespond($this, -1, MSG_FAIL, null );
            return;
        }

        $cnt =  $this->admin_model->getEventConsequenceCount();
        if($shop_consequence > $cnt) {
            $this->admin_model->updateEventConsequence($shop_id, $cnt + 1);
        }
        else {
            $this->admin_model->removeEventConsequence2($shop_consequence-1);
            $this->admin_model->updateEventConsequence($shop_id, $shop_consequence);
        }

        Util::doRespond($this, 0, MSG_SUCCESS, null );
    }

    function remove_event_consequence() {
        $shop_id = $this->input->post('id');
        if($shop_id == null) {
            Util::doRespond($this, -1, MSG_FAIL, null );
            return;
        }
        $this->admin_model->removeEventConsequence1($shop_id);
        
        Util::doRespond($this, 0, MSG_SUCCESS, null );
    }

    function do_upload($dir_name, $file_name)
    {
        $config['upload_path'] = './images/'.$dir_name.'/';
        $config['allowed_types'] = 'gif|jpg|png';
        $config['max_size']	= '10000KB';
        $config['max_width']  = '5000';
        $config['max_height']  = '5000';
        $config['file_name'] = $dir_name . $this->util->getMilliTime() . "_" . $file_name;
        $config['upload_url']=  base_url () ."/images/".$dir_name."/";

        $this->load->library('upload', $config);

        if ( ! $this->upload->do_upload($file_name))
        {
            $error = array('error' => $this->upload->display_errors());
            echo json_encode($error);
            return null;
        }
        else
        {
            $data = array('upload_data' => $this->upload->data());
            $imageURL = $config ['upload_url'] . $this->upload->file_name;
            return $imageURL;
        }
    }

    function upload_image() {

        $file_name = $this->input->get('file_name');
        $kind = $this->input->get('kind');
        $idx = $this->input->get('index');

        if($file_name == null) {
            Util::doRespond($this, -1, MSG_FAIL, 0 );
            return;
        }

        $imageURL = $this->do_upload ($kind, $file_name);

        if ($imageURL != null) {
            $result_img = $this->image_model->createImage ( $imageURL, ADMIN_ID, $kind);
            $result_img['index'] = $idx;
            Util::doRespond($this, 0, MSG_SUCCESS, $result_img );
        }
        else {
            Util::doRespond($this, -1, MSG_FAIL, 0 );
        }
    }

    function  create_image()
    {
        $imageURL = $this->input->post('img_url');
        $location_type = $this->input->post('location_type');
        $idx = $this->input->post('index');

        if ($imageURL == null) {
            Util::doRespond($this, -1, "fail", null);
            return;
        }

        $result_img = $this->_createImage ( $imageURL, ADMIN_ID, $location_type );
        $result_img['index'] = $idx;
        Util::doRespond($this, 0, "success", $result_img );
    }

    private function _createImage($imageURL, $user_id, $location_type ) {
        if ($imageURL == null) {
            return null;
        }
        $result_img = $this->image_model->createImage ( $imageURL, $user_id, $location_type );
        return $result_img;
    }

    function  register_ads() {
        $ads_id = $this->input->post('ads_id');
        $ads_img_id = $this->input->post('ads_img_id');
        $ads_url = $this->input->post('ads_url');

        if ($ads_img_id == null || $ads_url== null) {
            Util::doRespond($this, -1, "fail", null);
            return;
        }

        if($ads_id == null) {
            $result = $this->admin_model->createAds($ads_img_id, $ads_url);
            $ads_id = $result['id'];
        }
        else {
            $this->admin_model->updateAds($ads_id, $ads_img_id, $ads_url);
        }

        $resultAds = Array();
        $resultAds['index'] = $ads_id;
        Util::doRespond($this, 0, MSG_OK, $resultAds);
    }

    function remove_banner() {
        $arr_banner_id = $this->input->post("arr_banner_id");

        $arr_banner_id = json_decode($arr_banner_id, true);

        if($arr_banner_id == null || count($arr_banner_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_banner_id);
        for($i = 0; $i < $cnt; $i++) {
            $banner_id = $arr_banner_id[$i];
            $this->banner_model->removeBanner($banner_id);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function  write_banner() {

        $id = $this->input->post("banner_id");
        $banner_title = $this->input->post("banner_title");
        $banner_url = $this->input->post("banner_url");
        $banner_img_id = $this->input->post("banner_img_id");
        $banner_show_mode= $this->input->post("banner_show_mode");
        $banner_content = $this->input->post("banner_content");
        $banner_start_date = $this->input->post("banner_start_date");
        $banner_end_date = $this->input->post("banner_end_date");
        $banner_back_img_id = $this->input->post("banner_back_img_id");
        $banner_date_show = $this->input->post("banner_date_show");
        $banner_title_show = $this->input->post("banner_title_show");
        $banner_content_show = $this->input->post("banner_content_show");

        if ($banner_img_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $ads = $this->lifeinfo_model->createAds($banner_img_id, $banner_url);

        if($ads == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        if($id == null) {
            $this->banner_model->createBanner($ads['id'], $banner_title, $banner_show_mode, $banner_content, $banner_start_date, $banner_end_date, $banner_back_img_id, $banner_date_show, $banner_title_show, $banner_content_show);
        }
        else {
            $this->banner_model->modifyBanner($id, $ads['id'], $banner_title, $banner_show_mode, $banner_content, $banner_start_date, $banner_end_date, $banner_back_img_id, $banner_date_show, $banner_title_show, $banner_content_show);
        }
        $this->doRespond(0, MSG_SUCCESS, null);
    }

    function remove_question() {
        $arr_question_id = $this->input->post("arr_question_id");

        $arr_question_id = json_decode($arr_question_id, true);

        if($arr_question_id == null || count($arr_question_id) == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $cnt = count($arr_question_id);
        for($i = 0; $i < $cnt; $i++) {
            $question_id = $arr_question_id[$i];
            $this->admin_model->removeQuestion($question_id);
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
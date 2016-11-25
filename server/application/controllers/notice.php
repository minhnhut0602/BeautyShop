<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Notice extends CI_Controller {

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
        $this->load->model ( 'notice_model' );
        $this->load->model ( 'member_model' );
        $this->load->helper ( 'url' );
        date_default_timezone_set('Asia/Seoul');
    }

    function index()
    {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $data = Array();
        $arrNotice = $this->notice_model->getNoticeList($pageNum);
        $data['arrNotice'] = $arrNotice;
        $data['totalPageCnt'] = ((int)($this->notice_model->getNoticeCount() / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        //load the view
        $data['main_content'] = 'admin/notice/notice';
        $data['tab_id'] = ADMIN_TAB_ID_NOTICE;
        $this->load->view('includes/template', $data);
    }

    function preview()
    {
        $data = Array();
        $arrNotice = $this->notice_model->getNoticeList(null);
        $data['arrNotice'] = $arrNotice;

        //load the view
        $data['main_content'] = 'admin/notice/notice_preview';
        $this->load->view($data['main_content'], $data);
    }

    function privacy_preview()
    {
        //load the view
        $data['main_content'] = 'admin/notice/privacy_preview';
        $this->load->view($data['main_content'], $data);
    }

    function add_modify_notice()
    {
        $notice_id = $this->input->get('id');

        $data = Array();
        if($notice_id != null) {
            $data['notice'] = $this->notice_model->getNotice($notice_id);
        }

        //load the view
        $data['main_content'] = 'admin/notice/add_modify_notice';
        $this->load->view($data['main_content'], $data);
    }

    private function doRespond($p_result_code, $p_result_msg, $p_result) {
        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    function  register_notice() {
        $notice_id = $this->input->post("id");
        $notice_title = $this->input->post("title");
        $notice_content = $this->input->post("content");

        if($notice_title == null || strcmp($notice_title, "") == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        if($notice_content == null || strcmp($notice_content, "") == 0) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        if($notice_id == null) {
            $this->notice_model->createNotice($notice_title, $notice_content);
        }
        else {
            $this->notice_model->modifyNotice($notice_id, $notice_title, $notice_content);
        }

        $this->member_model->setNewEvent(true);

        $this->doRespond(0, MSG_OK, null);
    }

    function remove_notice() {

        $notice_id = $this->input->post('id');

        if($notice_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->notice_model->removeNotice($notice_id);
        $this->doRespond(0, MSG_OK, null);
    }

    function click_notice() {
        $notice_id = $this->input->post('id');

        if($notice_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }
        $data = $this->notice_model->getNotice($notice_id);
        $data['noticeClickCnt'] = $data['noticeClickCnt'] + 1;
        $this->notice_model->modifyNoticeClick($notice_id, $data['noticeClickCnt']);
        $this->doRespond(0, MSG_OK, null);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Push extends CI_Controller {

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
        $this->load->model ( 'gcm_model' );
        $this->load->model ( 'admin_model' );
        $this->load->model ( 'image_model' );
        $this->load->model ( 'api_model' );
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
        $data = Array();
        $data['arrLocation'] = $this->gcm_model->getLocationList();
        $data['exitAds'] = $this->api_model->getAds(1);

        //load the view
        $data['main_content'] = 'admin/push/push';
        $data['tab_id'] = ADMIN_TAB_ID_PUSH;
        $this->load->view('includes/template', $data);
    }

    function  shop_password() {

        $shop_id = $this->input->get("id");

        if($shop_id == null) {
            return;
        }

        $data = Array();

        if($shop_id != null) {
            $shop = $this->gcm_model->getShop($shop_id);

            if($shop != null) {
                $data['shop'] = $shop;
            }
        }

        $this->load->view('admin/push/shop_password', $data);
    }

    function  send_push() {
        $user_id = $this->input->get("user_id");

        if($user_id == null) {
            return;
            return;
        }

        $data = Array();
        $data['userInfo'] = $this->api_model->getUser($user_id);
        $this->load->view('admin/push/send_push', $data);
    }

    function  envelop_list() {
        $pageNum = $this->input->get('page_num');

        if($pageNum == null) {
            $pageNum = 1;
        }

        $arrUser = $this->gcm_model->getEnvelopList($pageNum);
        $data['arrEnvelopList'] = $arrUser;
        $data['totalPageCnt'] = ((int)($this->gcm_model->getEnvelopCount() / ADMIN_MAX_PAGE_ITEM_CNT)) + 1;
        $data['currentPageNum'] = $pageNum;

        //load the view
        $data['main_content'] = 'admin/push/envelop_list';
        $this->load->view('includes/template', $data);
    }

    function show_envelop() {
        $gcm_id = $this->input->get("id");

        if($gcm_id == null) {
            return;
            return;
        }

        $data = Array();
        $data['gcmInfo'] = $this->gcm_model->getGCM($gcm_id);

        $this->load->view('admin/push/show_envelop', $data);
    }

    function update_envelop() {
        $gcm_id = $this->input->get("id");

        if($gcm_id == null) {
            return;
            return;
        }

        $data = Array();
        $data['gcmInfo'] = $this->gcm_model->getGCM($gcm_id);

        $this->load->view('admin/push/update_envelop', $data);
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

    function send_message() {
        $kind_push = $this->input->post("kind_push");
        $kind_member = $this->input->post("kind_member");
        $sex = $this->input->post("sex");
        $location = $this->input->post("location");
        $level = $this->input->post("level");
        $message = $this->input->post("message");
        $img_url = $this->input->post("push_img_url");
        $push_landing_url = $this->input->post("push_landing_url");

        if($message == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $arrUser = $this->gcm_model->getUserList($kind_member, $sex, $location, $level);
        $cnt = count($arrUser);

        $data = Array(
            "message" => $message,
            "img_url" => $img_url,
            "landing_url" => $push_landing_url
        );
        $result= 0;
        for($i = 0; $i < $cnt; $i++) {
            $user = $arrUser[$i];

            if($kind_push == 0) { // send push
                $result_code =  $this->sendPushForLog($user['id'], $data);
            }
            else { // send envelop
                $result_code = $this->sendPushEnvelopForLog($user['id'], $data);
            }

            if($result_code != 0) {
                $result = $result_code;
            }
        }

        $this->doRespond(0, MSG_SUCCESS, null);
    }

    private function sendPushForLog($user_id, $data) {
        $user = $this->api_model->getUser($user_id);
        return $this->sendPushMessageWithRegTokens($user['id'], $user['userOsType'], $user['userGCMRegID'], null, $data);
    }

    private function sendPushEnvelopForLog($user_id, $data) {
        $user = $this->api_model->getUser($user_id);
        return $this->sendPushMessageWithRegTokens($user['id'], $user['userOsType'], $user['userGCMRegID'], GCM_LOG_TYPE3, $data);
    }


    private function sendPushMessageWithRegTokens($user_id, $gcm_type, $reg_id, $type, $data)
    {
        $this->load->model('api_model');
        if ($user_id != null) {
            $resultData = Array();
            if($type == null) {
                $resultData['title'] = '푸시알림';
                $resultData['type'] = '0';
            }
            else if($type == GCM_LOG_TYPE3) {
                if($data['title'] != null) {
                    $resultData['title'] = $data['title'];
                }
                else {
                    $resultData['title'] = '쪽찌알림';
                }
                $resultData['type'] = '3';
            }
            $resultData['message'] = $data['message'];
            $resultData['data'] = $data;

            $w_msg = json_encode($resultData);

            $success = false;

            if($gcm_type == 'A') {			// android
                $this->load->library('gcm');
                $this->gcm->setRecepients(Array($reg_id));
                $this->gcm->setMessage($w_msg);
                $this->gcm->setTtl(false);
                $this->gcm->setGroup(false);
                $success = $this->gcm->send();
            }
            else if($gcm_type == 'I'){ // iphone

                $this->load->library('apn');
                $this->apn->payloadMethod = 'enhance';
                $this->apn->connectToPush();

                $type =  $resultData['type'];
                $this->apn->setData($resultData);

                $message = $resultData['message'];
                $success = $this->apn->sendMessage($reg_id, $message , /*badge*/ 0, /*sound*/ 'default'  );

                $this->apn->disconnectPush();
            }

            if ($success) {
                $push_result_msg = "성공";
                $push_result_code = 1;

                $logData = Array(
                    'gcmlogType'=>$resultData['type'],
                    'gcmlogTitle'=>$resultData['title'],
                    'gcmlogContent'=>$data['message'],
                    'gcmlogUserID'=>$user_id,
                    'gcmlogPostTime' => $this->util->getTime()   // MySQL datetime format
                );
                $this->api_model->createGCMLog($logData);


                $newMessage = Array(
                    'userNewMessage'=>'Y'
                );
                $this->api_model->updateUser($user_id, $newMessage);

            } else {
                $push_result_msg = "푸시메시지를 전송할수 없습니다.";
                $push_result_code = -1;
            }


        } else {
            $push_result_msg = "장치토큰을 얻을수 없습니다.";
            $push_result_code = -1;
        }

        return $push_result_code;
    }

    function  register_exit_banner() {
        $ads_id = $this->input->post('ads_id');
        $ads_img_id = $this->input->post('ads_img_id');
        $ads_url = $this->input->post('ads_url');

        if ($ads_img_id == null || $ads_url== null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $this->gcm_model->updateExitAds($ads_id, $ads_img_id, $ads_url);

        $this->doRespond(0, MSG_OK, null);
    }

    function send_shop_password() {
        $user_id = $this->input->post("shop_owner");
        $message = $this->input->post("shop_password");

        if($user_id == null || $message == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $data = Array(
            'message' => '비번:'.$message
        );

        $this->sendPushEnvelopForLog($user_id, $data);
        $this->doRespond(0, MSG_OK, null);
    }

    function  send_push_message() {
        $user_id = $this->input->post("user_id");
        $message = $this->input->post("msg");
        $title = $this->input->post("title");

        if($user_id == null || $message == null) {
            $this->doRespond(-1, "fail", null);
            return;
        }

        $data = Array(
            'title' => $title,
            'message' => $message
        );

        $this->sendPushEnvelopForLog($user_id, $data);
        $this->doRespond(0, MSG_OK, null);
    }

    function remove_push() {
        $gcm_id = $this->input->post('push_id');
        if($gcm_id == null) {
            $this->doRespond(-1, MSG_FAIL, null);
            return;
        }

        $this->gcm_model->removeGCM($gcm_id);
        $this->doRespond(0, MSG_OK, null);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
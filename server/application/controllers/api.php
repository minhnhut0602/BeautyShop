<?php

if (! defined ( 'BASEPATH' ))
	exit ( 'No direct script access allowed' );
class Api extends CI_Controller
{

    /**
     * Api for App.
     *
     * Index Page for this controller.
     *
     * Maps to the following URL
     * http://example.com/index.php/api
     * - or -
     * http://example.com/index.php/api/index
     * - or -
     * Since this controller is set as the default controller in
     * config/routes.php, it's displayed at http://example.com/
     *
     * So any other public methods not prefixed with an underscore will
     * map to /index.php/welcome/<method_name>
     *
     * @see http://codeigniter.com/user_guide/general/urls.html
     */

    protected $userInfo;

    public function index()
    {
        $this->load->view('welcome_message');
    }

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        if (true) { // testing
            error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
        }

        $this->load->database();
        $this->load->model('api_model');
        $this->load->model('admin_model');
        $this->load->library('image_lib');
        $this->load->library('util');
        $this->load->helper('url');

        if (false) {
            $this->output->enable_profiler(TRUE);
            $sections = array(
                'config' => TRUE,
                'queries' => TRUE
            );

            $this->output->set_profiler_sections($sections);
        }

        date_default_timezone_set('Asia/Seoul');
    }

    private function uploadImage($dir_name, $input_name, $width = 0, $height = 0)
    {
        $w_uploadConfig = array(
            'upload_path' => $_SERVER ["DOCUMENT_ROOT"] . "/images/" . $dir_name . "/",
            'upload_url' => base_url() . "/images/" . $dir_name . "/",
            'allowed_types' => "*",
            'max_width' => 5000,
            'max_height' => 5000,
            'max_height' => 5000,
            'overwrite' => TRUE,
            'max_size' => "10000KB",
            'file_name' => $dir_name . $this->util->getMilliTime() . "_" . $input_name
        );
        $this->load->library('upload');
        $this->upload->initialize($w_uploadConfig);

        if ($this->upload->do_upload($input_name)) {

            $config ['image_library'] = 'gd2';
            $config ['source_image'] = $w_uploadConfig ['upload_path'] . $this->upload->file_name;
            $config ['maintain_ratio'] = TRUE;

            list ($w, $h) = getimagesize($w_uploadConfig ['upload_path'] . $this->upload->file_name);

            $ratio = max($width / $w, $height / $h);
            $h = ceil($h * $ratio);
            $w = ceil($w * $ratio);

            $config ['width'] = $w;
            $config ['height'] = $h;

            $this->image_lib->initialize($config);
            if (!$this->image_lib->resize()) {
                $error = $this->image_lib->display_errors();
                $ret = "";
            } else {
                $ret = $w_uploadConfig ['upload_url'] . $this->upload->file_name;
            }

            return $ret;
        } else {
            $error = "실패하였습니다.\n" . $this->upload->display_errors() . "\n" . $this->upload->upload_path;
            echo $error;
            return "";
        }

        return null;
    }

    private function generate_token($len = 32)
    {

        // Array of potential characters, shuffled.
        $chars = array(
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z',
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9'
        );
        shuffle($chars);

        $num_chars = count($chars) - 1;
        $token = '';

        // Create random token at the specified length.
        for ($i = 0; $i < $len; $i++) {
            $token .= $chars [mt_rand(0, $num_chars)];
        }

        return $token;
    }

    private function isValidHeader()
    {
/*        $header = $this->getHeaders();

        $result = $this->api_model->getUserIDWithHeader($header);

        if ($result == null) {
            return false;
        }

        $this->userInfo = $result;
*/
        return true;
        
    }

    private function isValidHeaderMe()
    {
        $header = $this->getHeaders();

        $result = $this->api_model->getUserIDWithHeader($header);

        if ($result == null) {
            return false;
        }

        $this->userInfo = $result;

        return true;
    }
    
    private function doRespond($p_result_code, $p_result_msg, $p_result)
    {
        $w_result = array();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $this->output->set_content_type('application/json')->set_output(json_encode($w_result));
    }


    private function doRespondSuccess($p_result)
    {
        $this->doRespond(0, "Success", $p_result);
    }

    private function doRespondFail($msg)
    {
        $this->doRespond(-1, $msg, null);
    }

    private function doRespondFailWithResult($p_result)
    {
        $this->doRespond(-1, "Fail", $p_result);
    }

    private function doRespondSuccessWithMsg($msg)
    {
        $this->doRespond(0, $msg, null);
    }

    private function getHeaders()
    {
        $headers = $this->input->request_headers();
        return $headers;
    }

    /**
     * Get init information when started app.
     *
     * @param
     *            header
     *
     * @return init information
     */
    function init()
    {

        $header = $this->getHeaders();

        if ($header == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $initInfo = $this->api_model->getInitInformation($header);

        if ($initInfo == null) {

            $appInfo = $this->api_model->getAppInfo($header [APP_VERSION], $header [OS_TYPE]);

            if ($appInfo != null) {
                $initInfo = $appInfo;
                $initInfo ['userKey'] = $this->generate_token();
                $initInfo ['userMarketName'] = $header[APP_MARKET];
                $initInfo ['userOsType'] = $header [OS_TYPE];
                $initInfo ['userDeviceID'] = $header [DEVICE_ID];
                $initInfo ['userDeviceModel'] = $header [DEVICE_MODEL];
                $initInfo ['userOsVersion'] = $header [OS_VERSION];
                $initInfo ['userAppVersion'] = $header [APP_VERSION];
            }
        }
        else {

            $data ['userOsVersion'] = $header [OS_VERSION];
            $data ['userAppVersion'] = $header [APP_VERSION];
			$data ['userAccessed'] = 1;

            // 헤더부갱신
            $this->api_model->updateUser($initInfo['id'], $data);
            
        }

        $initInfo['setting'] = $this->getSetting();
        $initInfo['hairShopCnt'] = $this->admin_model->getLiveHairShopCount();
        $initInfo['nailShopCnt'] = $this->admin_model->getLiveNailShopCount();

        $this->doRespondSuccess($initInfo);
    }

	function access()
	{
		$this->api_model->updateAccess(1);
		$this->doRespondSuccessWithMsg(MSG_SUCCESS);		
	}
	
	function finish()		
	{
		$this->api_model->updateAccess(-1);
		
		$id = $this->input->post("id");
		$data ['userAccessed'] = 0;
		$this->api_model->updateUser($id, $data);
		$this->doRespondSuccessWithMsg(MSG_SUCCESS);
	}

    private function getBannerArray($type)
    {
        $result = $this->api_model->getBannerArrayWithType($type);
        $cnt = count($result);

        for ($i = 0; $i < $cnt; $i++) {
            $id = $result[$i]['bannerAdsID'];
            $result[$i]['bannerAds'] = $this->api_model->getAds($id);
        }

        return $result;
    }

    /*
     * api of admin for users.
     * */
    function getBannerAdsList()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $resultData = $this->getBannerArray(ADMIN_TYPE_MAIN_BANNER);

        $this->doRespondSuccess($resultData);
    }

    function getExitAds()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $resultData = $this->getBannerArray(ADMIN_TYPE_LOGOUT_BANNER);
        $result = $resultData[0];
        $this->doRespondSuccess($result);
    }

    function getCastAdsList()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $resultData = $this->getBannerArray(ADMIN_TYPE_CAST_BANNER);

        $this->doRespondSuccess($resultData);
    }

    function getBestCast()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $cnt = $this->input->get('cnt');
        if ($cnt == null) {
            $cnt = ADMIN_CNT_BEST_CAST;
        }

        $result = $this->api_model->getBestCastList($cnt);
        $this->doRespondSuccess($result);
    }


    function getBestLifeEvent()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $result = $this->api_model->getBestLifeEventList(ADMIN_CNT_BEST_LIFE);
        if ($result != null && count($result) > 0) {
            $result = $result[0];
            $result['lifeAds'] = $this->api_model->getAds($result['lifeAdsID']);
        }
        else {
            $result = null;
        }

        $this->doRespondSuccess($result);
    }

    function getBestShopEvent()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $result = $this->api_model->getBestShopEventList(ADMIN_CNT_BEST_SHOP);
        if ($result != null && count($result) > 0) {
            $result = $result[0];
        }
        else {
            $result = null;
        }
        $this->doRespondSuccess($result);
    }

    function getBestFreetalk()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $cnt = $this->input->get('cnt');
        if ($cnt == null) {
            $cnt = ADMIN_CNT_BEST_FREETALK;
        }

        $result = $this->api_model->getBestFreetalkList($cnt);

        $this->doRespondSuccess($result);
    }

    /*
     * Freetak apis
     * */

    function getFreeTalkList()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $category_id = $this->input->get("category_id");
        $search_word = $this->input->get("search_word");
        $page_num = $this->input->get("page_num");

        $result = $this->api_model->getFreetalkList($page_num, PAGE_ITEM_CNT_FREETALK, $category_id, $search_word);

        $ret = Array(
            "page" => $page_num,
            "rows_per_page" => PAGE_ITEM_CNT_FREETALK,
            "total" => count($result),
            "list" => $result
        );

        $this->doRespondSuccess($ret);
    }

    function getFreetalk()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk_id = $this->input->get("freetalk_id");
        if ($freetalk_id == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $result = $this->api_model->getFreetalk($freetalk_id);

        if ($result == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $this->doRespondSuccess($result);
    }

    function getMyFreeTalkList()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $user_id = $this->userInfo['id'];
        $page_num = $this->input->get("page_num");

        $result = $this->api_model->getFreetalkListByUserID($page_num, PAGE_ITEM_CNT_FREETALK, $user_id);

        $ret = Array(
            "page" => $page_num,
            "rows_per_page" => PAGE_ITEM_CNT_FREETALK,
            "total" => count($result),
            "list" => $result
        );

        $this->doRespondSuccess($ret);
    }

    function getFreeTalkCommentList()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk_id = $this->input->get("freetalk_id");

        if ($freetalk_id == null || $freetalk_id < 0) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $page_num = $this->input->get("page_num");

        $result = $this->api_model->getFreeTalkCommentList($page_num, PAGE_ITEM_CNT_FREETALK_COMMENT, $freetalk_id, null);

        $ret = Array(
            "page" => $page_num,
            "rows_per_page" => PAGE_ITEM_CNT_FREETALK_COMMENT,
            "total" => count($result),
            "list" => $result
        );

        $this->doRespondSuccess($ret);
    }

    function getMyFreeTalkCommentList()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $user_id = $this->userInfo['id'];

        if ($user_id == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $page_num = $this->input->get("page_num");

        $result = $this->api_model->getFreeTalkCommentList($page_num, PAGE_ITEM_CNT_FREETALK_COMMENT, null, $user_id);

        $ret = Array(
            "page" => $page_num,
            "rows_per_page" => PAGE_ITEM_CNT_FREETALK_COMMENT,
            "total" => count($result),
            "list" => $result
        );

        $this->doRespondSuccess($ret);
    }

    function  likeFreeTalk()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk_id = $this->input->post("freetalk_id");

        if ($freetalk_id == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk = $this->api_model->getFreetalk($freetalk_id);
        $user_id = $this->userInfo['id'];

        if ($freetalk != null && $freetalk['freetalkPostManID'] == $user_id) {
            $this->doRespondFail(MSG_YOUR_WRITING);
            return;
        }

        $freetalk_relation = $this->api_model->getFreetalkRelation($freetalk_id, $user_id);
        $first_cnt = $this->api_model->getFreetalkLikeCount($freetalk_id);
        $heart_cnt = $freetalk['freetalkHeartCnt'] - $first_cnt;
        $data = Array();
        $result = Array();
        $result['isDuplicate'] = false;
        if ($freetalk_relation == null) {
            $data['freetalkrelationFreetalkID'] = $freetalk_id;
            $data['freetalkrelationPostManID'] = $user_id;
            $data['freetalkrelationLike'] = 1;
            $data['freetalkrelationPostTime'] = $this->util->getTime();
            $this->api_model->createFreetalkRelation($data);
            $this->increaseUserPoint(IDX_POINT_LIKE);
        } else {
            if ($freetalk_relation['freetalkrelationLike'] == 0) {
                $data['freetalkrelationLike'] = 1;
                $this->increaseUserPoint(IDX_POINT_LIKE);
            } else {
                // $data['castrelationLike'] = 0;
                $data['freetalkrelationLike'] = 1;
                $result['isDuplicate'] = true;
            }

            $this->api_model->modifyFreetalkRelation($freetalk_relation['id'], $data);
        }

        $castData = Array();
        $castData['freetalkHeartCnt'] = $heart_cnt+$this->api_model->getFreetalkLikeCount($freetalk_id);
        $castData['id'] = $freetalk_id;
        $this->api_model->updateFreetalk( $castData, MODE_UPDATE);
        $result['cnt'] =  $castData['freetalkHeartCnt'];
        $this->doRespondSuccess($result);
    }


    function writeFreetalk()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $user_id = $this->userInfo['id'];
        $freetalk_id = $this->input->post("freetalk_id");
        $content = $this->input->post("content");
        $categoryID = $this->input->post("category_id");

        if ($content == null || $categoryID < 0) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $data = array(
            'freetalkPostManID' => $user_id,
            'freetalkContent' => $content,
            'freetalkCategoryID' => $categoryID,
            'freetalkPostTime' => $this->util->getMilliTime()   // MySQL datetime format
        );

        $image_array = $this->input->post("img_id_array");
        if ($image_array != null) {
            $data['freetalkImgIDArrayString'] = $image_array;
        }

        if ($freetalk_id == null) {
            $ret_id = $this->api_model->updateFreetalk($data, MODE_ADD);
        } else {
            $data['id'] = $freetalk_id;
            $ret_id = $this->api_model->updateFreetalk($data, MODE_UPDATE);
        }

        if ($ret_id == null || $ret_id < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail(MSG_ERR_SQL);
            return;
        }

        if ($freetalk_id == null) { // add
            $this->increaseUserPoint(IDX_POINT_WRITE);
        }

        $ret = Array("id" => $ret_id);
        $this->doRespondSuccessWithMsg($ret);
    }

    function removeFreetalk()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $freetalk_id = $this->input->post("freetalk_id");

        if ($freetalk_id == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $data['id'] = $freetalk_id;
        $this->api_model->removeFreetalk($freetalk_id);

        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }


    function doDeclareFreetalk()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $freetalkID = $this->input->post('freetalk_id');
        $userID = $this->userInfo['id'];

        if ($userID == null || $freetalkID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $result = $this->api_model->getFreetalkRelation($freetalkID, $userID);

        if ($result != null && $result['freetalkrelationDeclare'] == 1) {
            $this->doRespondFail(MSG_DECLARRED);
            return;
        }

        $data = array(
            'freetalkrelationFreetalkID' => $freetalkID,
            'freetalkrelationPostManID' => $userID,
            'freetalkrelationDeclare' => '1',
            'freetalkrelationPostTime' => $this->util->getTime()   // MySQL datetime format
        );

        if ($result == null) {
            $this->api_model->createFreetalkRelation($data);
        } else {
            $this->api_model->modifyFreetalkRelation($result['id'], $data);
        }

        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }

    private function writeTagList($freetalk_comment_id)
    {
        // reg tag
        $tagList = $this->input->post("freetalk_comment_tag_list");
        if ($tagList != null && $freetalk_comment_id != null) {
            $tagList = json_decode($tagList);
            $count = count($tagList);
            for ($i = 0; $i < $count; $i++) {
                $tag_user_id = $tagList[$i];

                $result = $this->api_model->getFreetalkCommentTagWithData($freetalk_comment_id, $tag_user_id);

                $data = Array(
                    'freetalkcommenttagFreetalkCommentID' => $freetalk_comment_id,
                    'freetalkcommenttagTAGUserID' => $tag_user_id,
                    'freetalkcommenttagPostTime' => $this->util->getTime()
                );

                if ($result == null) {
                    $this->api_model->createFreetalkCommentTag($data);
                    $this->sendPushTAG($tag_user_id);
                } else {
                    $this->api_model->modifyFreetalkCommentTag($result['id'], $data);
                }
            }
        }
    }

    function writeFreetalkComment()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $user_id = $this->userInfo['id'];
        $freetalk_id = $this->input->post("freetalk_id");
        $content = $this->input->post("freetalk_comment_content");

        if ($content == null || $freetalk_id == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }
        $data = array(
            'freetalkcommentPostManID' => $user_id,
            'freetalkcommentContent' => $content,
            'freetalkcommentFreetalkID' => $freetalk_id,
            'freetalkcommentPostTime' => $this->util->getMilliTime()   // MySQL datetime format
        );

        $redList = $this->input->post("freetalk_comment_red");
        if ($redList != null) {
            $data['freetalkcommentRedList'] = $redList;
        }

        $freetalk_comment_id = $this->input->post("freetalk_comment_id");

        if ($freetalk_comment_id == null) {
            $ret_id = $this->api_model->updateFreetalkComment($data, MODE_ADD);
        } else {
            $data['id'] = $freetalk_comment_id;
            $ret_id = $this->api_model->updateFreetalkComment($data, MODE_UPDATE);
        }

        if ($ret_id == null || $ret_id < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail(MSG_ERR_SQL);
            return;
        }

        if ($freetalk_comment_id == null) { // add
            $freetalk = $this->api_model->getFreetalk($freetalk_id);
            if ($freetalk != null && $freetalk['freetalkPostManID'] != $user_id) {
                $this->increaseUserPoint(IDX_POINT_COMMENT);
                $this->sendPushComment($freetalk_id);
            }
        }

        $this->writeTagList($ret_id);

        $ret = Array("id" => $ret_id);
        $this->doRespondSuccess($ret);
    }


    function  likeFreetalkComment()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk_comment_id = $this->input->post("freetalk_comment_id");

        if ($freetalk_comment_id == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk = $this->api_model->getFreetalkCommentByID($freetalk_comment_id);
        $user_id = $this->userInfo['id'];

        if ($freetalk != null && $freetalk['freetalkcommentPostManID'] == $user_id) {
            $this->doRespondFail(MSG_YOUR_WRITING);
            return;
        }

        $freetalk_relation = $this->api_model->getFreetalkCommentRelation($freetalk_comment_id, $user_id);
        $data = Array();
        $result = Array();
        $result['isDuplicate'] = false;
        if ($freetalk_relation == null) {
            $data['freetalkcommentrelationFreetalkCommentID'] = $freetalk_comment_id;
            $data['freetalkcommentrelationPostManID'] = $user_id;
            $data['freetalkcommentrelationLike'] = 1;
            $data['freetalkcommentrelationPostTime'] = $this->util->getTime();
            $this->api_model->createFreetalkCommentRelation($data);
            $this->increaseUserPoint(IDX_POINT_LIKE);
        } else {
            if ($freetalk_relation['freetalkcommentrelationLike'] == 0) {
                $data['freetalkcommentrelationLike'] = 1;
                $this->increaseUserPoint(IDX_POINT_LIKE);
            } else {
                // $data['castrelationLike'] = 0;
                $data['freetalkcommentrelationLike'] = 1;
                $result['isDuplicate'] = true;
            }

            $this->api_model->modifyFreetalkCommentRelation($freetalk_relation['id'], $data);
        }

        $result['cnt'] = $this->api_model->getFreetalkCommentLikeCount($freetalk_comment_id);

        $castData = Array();
        $castData['freetalkcommentHeartCnt'] = $result['cnt'];
        $castData['id'] = $freetalk_comment_id;
        $this->api_model->updateFreetalkComment($castData, MODE_UPDATE);

        $this->doRespondSuccess($result);
    }


    function doDeclareFreetalkComment()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $freetalk_comment_id = $this->input->post("freetalk_comment_id");

        if ($freetalk_comment_id == null) {
            $this->doRespondFail(MSG_ERR_INPUT_INVALID);
            return;
        }

        $freetalk = $this->api_model->getFreetalkCommentByID($freetalk_comment_id);
        $user_id = $this->userInfo['id'];

        if ($freetalk != null && $freetalk['freetalkcommentPostManID'] == $user_id) {
            $this->doRespondFail(MSG_YOUR_WRITING);
            return;
        }

        $freetalk_relation = $this->api_model->getFreetalkCommentRelation($freetalk_comment_id, $user_id);
        $data = Array();
        $result = Array();
        $result['isDuplicate'] = false;
        if ($freetalk_relation == null) {
            $data['freetalkcommentrelationFreetalkCommentID'] = $freetalk_comment_id;
            $data['freetalkcommentrelationPostManID'] = $user_id;
            $data['freetalkcommentrelationDeclare'] = 1;
            $data['freetalkcommentrelationPostTime'] = $this->util->getTime();
            $this->api_model->createFreetalkCommentRelation($data);
        } else {
            if ($freetalk_relation['freetalkcommentrelationDeclare'] == 0) {
                $data['freetalkcommentrelationDeclare'] = 1;
            } else {
                $data['freetalkcommentrelationDeclare'] = 1;
                $result['isDuplicate'] = true;
            }

            $this->api_model->modifyFreetalkCommentRelation($freetalk_relation['id'], $data);
        }

        if ($result['isDuplicate'] == true) {
            $this->doRespondFail(MSG_DECLARRED);
            return;
        }

        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }

    function removeFreetalkComment()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $comment_id = $this->input->post("freetalk_comment_id");

        if ($comment_id == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $comment = $this->api_model->getFreetalkCommentByID($comment_id);
        $user_id = $this->userInfo['id'];

        if ($comment != null && $comment['freetalkcommentPostManID'] != $user_id) {
            $this->doRespondFail(MSG_NOT_YOUR_WRITING);
            return;
        }

        $data = Array("id" => $comment_id);
        $ret = $this->api_model->updateFreetalkComment($data, MODE_REMOVE);

        if ($ret == null || $ret < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail(MSG_ERR_SQL);
            return;
        }

        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }


    /*
     * user manager apis
     * */
    function registerUser()
    {

        $type = $this->input->post("type");

        if ($type != 'C') { // non create
            $isValidHeader = $this->isValidHeaderMe();

            if ($isValidHeader == false) {
                $this->doRespondFail(MSG_FAIL);
                return;
            }
        }

        if ($this->userInfo != null) {
            $user_id = $this->userInfo['id'];
        } else {
            $user_id = null;
        }

        $user_real_id = $this->input->post("userID");
        $user_email = $this->input->post("userEmail");
        $user_password = $this->input->post("userPassword");
        $user_sex = $this->input->post("userSex");
        $user_age = $this->input->post("userAge");
        $user_recommenderID = $this->input->post("userRecommenderID");
        $user_locationID = $this->input->post("userLocationID");
        $user_ImgID = $this->input->post("userImgID");

        if ($user_id == null && $user_real_id == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $data = Array();

        if ($user_real_id != null) {
            $data['userID'] = $user_real_id;
        }

        if ($user_email != null) {
            $data['userEmail'] = $user_email;
        }
        if ($user_password != null) {
            $data['userPassword'] = $user_password;
        }

        if ($user_sex != null) {
            $data['userSex'] = $user_sex;
        }

        if ($user_age != null) {
            $data['userAge'] = $user_age;
        }

        if ($user_locationID != null) {
            $data['userLocationID'] = $user_locationID;
        }

        if ($user_recommenderID != null) {
            $data['userRecommenderID'] = $user_recommenderID;
        }

        if ($user_ImgID != null) {
            $data['userImgID'] = $user_ImgID;
        }

        $userNewMessage = $this->input->post("userNewMessage");

        if ($userNewMessage != null) {
            $data['userNewMessage'] = $userNewMessage;
        }

        $userNewEvent = $this->input->post("userNewEvent");

        if ($userNewEvent != null) {
            $data['userNewEvent'] = $userNewEvent;
        }

        $userNewInform = $this->input->post("userNewInform");

        if ($userNewInform != null) {
            $data['userNewInform'] = $userNewInform;
        }

        $userLocationName = $this->input->post("userLocationName");
        if ($userLocationName != null) {
            $data['userLocationName'] = $userLocationName;
        }

        if ($user_id == null) {
            $header = $this->getHeaders();

            $appInfo = $this->api_model->getAppInfo($header [APP_VERSION], $header [OS_TYPE]);

            $data ['userKey'] = $header[APP_KEY];
            $data ['userMarketID'] = $appInfo['appMarketID'];
            $data ['userOsType'] = $header [OS_TYPE];
            $data ['userDeviceID'] = $header [DEVICE_ID];
            $data ['userDeviceModel'] = $header [DEVICE_MODEL];
            $data ['userOsVersion'] = $header [OS_VERSION];
            $data ['userAppVersion'] = $header [APP_VERSION];

            $userInfo = $this->api_model->getInitInformation($header);
            if ($userInfo != null) {
                $user_id = $userInfo['id'];
                $this->api_model->updateUser($user_id, $data);
            } else {
                $data['userRegisterTime'] = $this->util->getTime();
                $this->api_model->createUser($data);
            }

            $userInfo = $this->api_model->getInitInformation($header);
        } else {
            $this->api_model->updateUser($user_id, $data);
            $userInfo = $this->api_model->getUser($user_id);
            $userInfo ['userJimList'] = $this->getJimShopArray($user_id);
        }

        if ($userInfo == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $this->doRespondSuccess($userInfo);
    }

    private function getJimShopArray($user_id)
    {
        $jimList = $this->api_model->getUserJimList($user_id);

        $result = Array();
        if ($jimList != null) {

            $cnt = count($jimList);

            for ($i = 0; $i < $cnt; $i++) {

                $shopInfo = $this->api_model->getShop($jimList [$i]['usershoprelationShopID']);
                if ($shopInfo != null) {
                    $shopInfo['shopImgArray'] = $this->getImageList($shopInfo['shopImgIDArrayString']);
                    array_push($result, $shopInfo);
                }
            }
        }

        return $result;
    }

    private function getSetting()
    {
        $setting = $this->admin_model->getAppSetting();
        return $setting;
    }

    function getMe()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $userID = $this->userInfo['id'];

        $userInfo = $this->api_model->getUser($userID);

        if ($userInfo == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $userInfo ['userJimList'] = $this->getJimShopArray($userID);
        $question = $this->api_model->getLastAdminQuestion($userID);
        if ($question != null) {
            $userInfo['userQuestion'] = $question['questionContent'];
        } else {
            $userInfo['userQuestion'] = "";
        }

        $userInfo['setting'] = $this->getSetting();
        $this->doRespondSuccess($userInfo);
    }

    function isDuplicationUserID()
    {

        $type = $this->input->post("type");

        if ($type != 'C') { //init
            $isValidHeader = $this->isValidHeader();

            if ($isValidHeader == false) {
                $this->doRespondFail(MSG_FAIL);
                return false;
            }
        }

        $user_nickname = $this->input->post("userID");
        $result = $this->api_model->getUserIDWithNickname($user_nickname);

        if ($result == null) {
            $this->doRespondSuccess("FALSE");
            return;
        }

        $this->doRespondSuccess("TRUE");
    }

    private function getLevel($point)
    {

        $level = 0;
        if ($point < USER_LEVEL1_POINT) {
            $level = 0;
        } else if ($point < USER_LEVEL2_POINT) {
            $level = 1;
        } else if ($point < USER_LEVEL3_POINT) {
            $level = 2;
        } else if ($point < USER_LEVEL4_POINT) {
            $level = 3;
        } else if ($point < USER_LEVEL5_POINT) {
            $level = 4;
        } else if ($point >= USER_LEVEL5_POINT) {
            $level = 5;
        }

        return $level;
    }

    private function  getIncreatePoint($kind)
    {
        $appSetting = $this->getSetting();
        $point = json_decode($appSetting['settingPointRule']);
        return $point[$kind];
    }

    private function increaseUserPoint($kind)
    {

        $add_point = $this->getIncreatePoint($kind);

        $user_id = $this->userInfo['id'];
        $point = $this->userInfo['userPoint'];
        $level = $this->userInfo['userLevel'];

        $point = $point + $add_point;
        $level = $this->getLevel($point);

        $data = Array(
            "userLevel" => $level,
            "userPoint" => $point,
        );

        $this->api_model->updateUser($user_id, $data);

        return $data;
    }

    function increasePoint()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $data = increaseUserPoint();

        $this->doRespondSuccess($data);
    }

    function  inviteFriend()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $user_id = $this->userInfo['id'];
        $kind = $this->input->post("kind"); //'k','f'
        $result = $this->api_model->getUser($user_id);
        $data = Array();
        if ($kind == 'k') {
            $data['userInviteFriendKakaoCnt'] = $result['userInviteFriendKakaoCnt'] + 1;
        } else if ($kind == 'f') {
            $data['userInviteFriendFBCnt'] = $result['userInviteFriendFBCnt'] + 1;
        }

        $this->api_model->updateUser($user_id, $data);

        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }

    function writeMyQuestion()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $userID = $this->userInfo['id'];
        $content = $this->input->post("content");
        if ($userID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        if ($content == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $data = Array(
            'questionPostManID' => $userID,
            'questionContent' => $content,
            'questionPostTime' => $this->util->getTime(),  // MySQL datetime format
            'questionType' => ADMIN_TYPE_ADMIN_QUESTION_NORMAL,
            'questionStatus' => 1                     // 0:shopQuestion 1:adminQuestion
        );

        $this->api_model->createQuestion($data);

        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }

    private function  isShopLive($shop)
    {
        $now = $this->util->getDate();
        $start_time = $shop['shopRequestTime'];
        $add_date = ' + ' . $shop['shopLiveDate'] . ' days';
        $live_date = date('Y-m-d', strtotime($start_time . $add_date));
        if ($live_date >= $now) {
            return true;
        }

        return false;
    }

    /*
     * apis related my shop
     * */

    function getMyShop()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $userID = $this->userInfo['id'];
        if ($userID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $result = $this->api_model->getShopListWithOwnerID($userID);

        if ($result != null && count($result) > 0) {
            $result = $result[0];

            if ($this->isShopLive($result) == false) {
                $this->doRespondFail(MSG_LIMIT_LIVE_DATE);
                return;
            }

            $shopIDArrayString = $result ['shopImgIDArrayString'];
            $result['shopImgArray'] = $this->getImageList($shopIDArrayString);
            $result['shopPriceImgArray'] = $this->getImageList($result ['shopPriceImgIDArrayString']);
        } else {
            $result = null;
        }

        $this->doRespondSuccess($result);
    }

    private function getImageList($arr_img_id)
    {
        if ($arr_img_id == null) {
            return null;
        }

        $shopIDArrayString = $arr_img_id;

        if ($shopIDArrayString != null) {
            $arrImgID = json_decode($shopIDArrayString);

            $cnt = count($arrImgID);

            $result = Array();

            for ($i = 0; $i < $cnt; $i++) {

                $imgInfo = $this->api_model->getImage($arrImgID [$i]);
                array_push($result, $imgInfo);
            }
        }
        return $result;
    }

    private function getShopReviewResult($result, $shop_id)
    {
        $review = $this->api_model->getShopLevel($shop_id);
        $result['shopReviewCnt'] = $review['shopReviewCnt'];

        if ($review['shopRemarkLevel'] == null) {
            $result['shopRemarkLevel'] = 0;
        } else {
            $result['shopRemarkLevel'] = $review['shopRemarkLevel'];
        }

        return $result;
    }

    function getShop()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        /*$userID = $this->userInfo['id'];
        if ($userID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }*/

        $shop_id = $this->input->get('shop_id');

        $result = $this->api_model->getShop($shop_id);

        if ($result != null) {

            if ($this->isShopLive($result) == false) {
                $this->doRespondFail(MSG_LIMIT_LIVE_DATE);
                return;
            }

            $result['shopImgArray'] = $this->getImageList($result['shopImgIDArrayString']);
            $result['shopPriceImgArray'] = $this->getImageList($result ['shopPriceImgIDArrayString']);
            $result = $this->getShopReviewResult($result, $shop_id);
        }

        $this->doRespondSuccess($result);
    }


    function isDuplicationShopID()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        }


        $store_id = $this->input->post("shopID");
        $result = $this->api_model->getShopWithShopID($store_id);

        if ($result == null) {
            $this->doRespondSuccess("FALSE");
            return;
        }

        $this->doRespondSuccess("TRUE");
    }

    function isShopAutoLogin()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        }
        $userID = $this->userInfo['id'];
        $shopID = $this->input->post("shop_id");
        if ($shopID == null) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        }

        $result = $this->api_model->getShop($shopID);
        if ($result == null) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        }

        if ($result['shopOwnerID'] != $userID) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        } else {
            if ($result['shopAutoLogin'] == 1) {
                $this->doRespondSuccess("TRUE");
                return true;
            } else {
                $this->doRespondSuccess("FALSE");
                return false;
            }
        }

    }

    function logoutShop()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        }

        $userID = $this->userInfo['id'];
        if ($userID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $shopID = $this->input->post("shop_id");
        $autoLogin = 0;

        if ($shopID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $data = Array(
            'id' => $shopID,
            'shopAutoLogin' => $autoLogin
        );
        $this->api_model->updateShop($data, MODE_UPDATE);
        $this->doRespondSuccessWithMsg(MSG_SUCCESS);
    }

    function loginShop()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return false;
        }

        $userID = $this->userInfo['id'];
        if ($userID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $shopID = $this->input->post("shopID");
        $shopPW = $this->input->post("shopPW");
        $autoLogin = $this->input->post("autoLogin");

        if ($shopID == null || $shopPW == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $result = $this->api_model->getShopListWithOwnerID($userID);

        if ($result == null) {
            $this->doRespondFail("FALSE");
            return;
        }

        if (count($result) > 0) {
            $result = $result[0];
        } else {
            $this->doRespondFail("FALSE");
            return;
        }

        if (strcmp($result['shopID'], $shopID) == 0 && strcmp($result['shopPW'], $shopPW) == 0) {
            if ($autoLogin == true) {
                $data = Array(
                    'id' => $result['id'],
                    'shopAutoLogin' => 1
                );
                $this->api_model->updateShop($data, MODE_UPDATE);
            }

            $this->doRespondSuccess("TRUE");
            return;
        } else {
            $this->doRespondSuccess("FALSE");
            return;
        }
    }


    function registerMyShop()
    {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $userID = $this->userInfo['id'];
        $shop_id = $this->input->post("shop_id");
        if ($userID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $shopDictionary = array(
            'shopOwnerID' => $userID,
        );

        $shopImgID = $this->input->post("shopImgID");

        if ($shopImgID != null) {
            $shopDictionary['shopImgID'] = $shopImgID;
        }

        $shopManagerIdentyImgID = $this->input->post("shopManagerIdentyImgID");
        if ($shopManagerIdentyImgID != null) {
            $shopDictionary['shopManagerIdentyImgID'] = $shopManagerIdentyImgID;
        }

        $shopCategoryID = $this->input->post("shopCategoryID");
        if ($shopCategoryID != null) {
            $shopDictionary['shopCategoryID'] = $shopCategoryID;
        }

        $shopID = $this->input->post("shopID");
        if ($shopID != null) {
            $shopDictionary['shopID'] = $shopID;
        }

        $shopPW = $this->input->post("shopPW");
        if ($shopPW != null) {
            $shopDictionary['shopPW'] = $shopPW;
        }

        $shopName = $this->input->post("shopName");
        if ($shopName != null) {
            $shopDictionary['shopName'] = $shopName;
        }

        $shopAddress = $this->input->post("shopAddress");
        if ($shopAddress != null) {
            $shopDictionary['shopAddress'] = $shopAddress;
        }

        $shopPhonenumber = $this->input->post("shopPhonenumber");
        if ($shopPhonenumber != null) {
            $shopDictionary['shopPhonenumber'] = $shopPhonenumber;
        }


        $shopParkable = $this->input->post("shopParkable");
        if ($shopParkable != null) {
            $shopDictionary['shopParkable'] = $shopParkable;
        }

        $shopQuestionable = $this->input->post("shopQuestionable");
        if ($shopQuestionable != null) {
            $shopDictionary['shopQuestionable'] = $shopQuestionable;
        }

        $shopEventable = $this->input->post("shopEventable");
        if ($shopEventable != null) {
            $shopDictionary['shopEventable'] = $shopEventable;
        }

        $shopPostManName = $this->input->post("shopPostManName");
        if ($shopPostManName != null) {
            $shopDictionary['shopPostManName'] = $shopPostManName;
        }

        $shopStuffPhonenumber = $this->input->post("shopStuffPhonenumber");
        if ($shopStuffPhonenumber != null) {
            $shopDictionary['shopStuffPhonenumber'] = $shopStuffPhonenumber;
        }

        $shopImgIDArrayString = $this->input->post("shopImgIDArray");
        if ($shopImgIDArrayString != null) {
            $shopDictionary['shopImgIDArrayString'] = $shopImgIDArrayString;
        }

        $shopLongtitude = $this->input->post("shopLongtitude");
        if ($shopLongtitude != null) {
            $shopDictionary['shopLongtitude'] = $shopLongtitude;
        }

        $shopLatitude = $this->input->post("shopLatitude");
        if ($shopLatitude != null) {
            $shopDictionary['shopLatitude'] = $shopLatitude;
        }

        $shopRoad = $this->input->post("shopRoad");
        if ($shopRoad != null) {
            $shopDictionary['shopRoad'] = $shopRoad;
        }

        $shopDescription = $this->input->post("shopDescription");
        if ($shopDescription != null) {
            $shopDictionary['shopDescriptionString'] = $shopDescription;
        }

        $shopOpenTimeString = $this->input->post("shopOpenTimeString");
        if ($shopOpenTimeString != null) {
            $shopDictionary['shopOpenTimeString'] = $shopOpenTimeString;
        }

        $shopRestTimeString = $this->input->post("shopRestTimeString");
        if ($shopRestTimeString != null) {
            $shopDictionary['shopRestTimeString'] = $shopRestTimeString;
        }

        $shopPriceImgArrayString = $this->input->post("shopPriceImgIDArrayString");
        if ($shopPriceImgArrayString != null) {
            $shopDictionary['shopPriceImgIDArrayString'] = $shopPriceImgArrayString;
        }

        if ($shop_id == null) {
            $shop = $this->api_model->getShopWithShopNameAndAddress($shopName, $shopAddress);

            $shopDictionary['shopAutoLogin'] = 0;

            if ($shop != null) {
                if ($shop['shopOwnerID'] != 0) {
                    $this->doRespondFail(MSG_REGISTTERED_SHOP);
                    return;
                }
                $this->api_model->updateShop($shopDictionary, MODE_UPDATE);
            } else {
                $shopDictionary['shopRequestTime'] = $this->util->getTime();
                $shopDictionary['shopStatus'] = STATUS_REQUEST;
                $result = $this->api_model->updateShop($shopDictionary, MODE_ADD);
            }
        } else {
            $shopDictionary['id'] = $shop_id;
            $this->api_model->updateShop($shopDictionary, MODE_UPDATE);
        }

        $this->doRespondSuccess($result);
    }

    function registerMyProduct()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $shopID = $this->input->post("shop_id");
        $productID = $this->input->post("product_id");
        $productName = $this->input->post("productName");
        if ($shopID == null || $productName == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $productPrice = $this->input->post("productPrice");
        $productEventPrice = $this->input->post("productEventPrice");

        $priceDictionary = array(
            'productShopID' => $shopID,
            'productName' => $productName
        );

        if ($productPrice != null) {
            $priceDictionary['productPrice'] = $productPrice;
        }

        if ($productEventPrice != null) {
            $priceDictionary['productEventPrice'] = $productEventPrice;
        }

        if ($productID == null) {
            $result = $this->api_model->createProduct($priceDictionary);
        } else {
            $result = $this->api_model->modifyProduct($productID, $priceDictionary);
        }

        $this->doRespondSuccess($result);
    }

    function getProductListOfShop()
    {
        $isValidHeader = $this->isValidHeader();

        if ($isValidHeader == false) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $shopID = $this->input->get("shop_id");
        if ($shopID == null) {
            $this->doRespondFail(MSG_FAIL);
            return;
        }

        $page_num = $this->input->get("page_num");
        $result = $this->api_model->getProductListWithShopID($page_num, PAGE_ITEM_CNT_SHOP_PRODUCT, $shopID);

        $ret = Array(
            "page" => $page_num,
            "rows_per_page" => PAGE_ITEM_CNT_SHOP_COMMENT,
            "total" => count($result),
            "list" => $result
        );

        $this->doRespondSuccess($ret);
    }


	/*
	 * type: WI, UR, QA
	 */
	function getShopCommentList() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$type = $this->input->get ( "shopcommentType" );
		$user_id = $this->input->get ( "shopcommentUserID" );
		$shop_id = $this->input->get ( "shopcommentShopID" );

        $page_num = $this->input->get("page_num");

        $result = $this->api_model->getShopCommentList ($page_num, PAGE_ITEM_CNT_SHOP_COMMENT, $type, $shop_id, $user_id );

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_SHOP_COMMENT,
            "total"=>count($result),
            "list"=>$result
        );
	
		$this->doRespondSuccess ( $ret );
	}
	
	function getShopCommentByID() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$comment_id = $this->input->get ( "comment_id" );
		
		if ($comment_id == null){
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$result = $this->api_model->getShopCommentByID ( $comment_id );
			
		$this->doRespondSuccess ( $result );
	}
	
	function getShopAnswerListWithShopID() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$shop_id = $this->input->get ( "shop_id" );
	
		$result = $this->api_model->getShopAnswerListWithShopID ( $shop_id );
	
		$this->doRespondSuccess ( $result );
	}
	
	function getAgreement(){
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$type = $this->input->get ( "type" );
		$result = $this->admin_model->getAgreement($type);
		
		$this->doRespondSuccess ( $result );
	}
	
	function searchShopList() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			
			return;
		}
	
		$location = $this->input->get ( "location" );
		$category_id = $this->input->get ( "category_id" );
		$shop_name = $this->input->get ( "shopName" );
		$cur_lat = $this->input->get ( "cur_lat" );
		$cur_long = $this->input->get ( "cur_long" );
		$radius = $this->input->get ( "radius" );
		$cnt = $this->input->get ( "cnt" );
		
		$result = $this->api_model->getShopList ( $category_id, $location, $shop_name, $cur_lat, $cur_long, $radius, $cnt );
		
		$resultArray = Array();
		if ($result != null) {
			for($j = 0; $j < count($result); $j++) {
				$data = $result [$j];
				$shopIDArrayString = $data ['shopImgIDArrayString'];
				$data ['shopImgArray'] = $this->getImageList($shopIDArrayString);
				$data['shopPriceImgArray'] = $this->getImageList($data ['shopPriceImgIDArrayString']);
                $event = $this->api_model->getEventList(null, null, null,null, $data['id']);
                if(count($event) > 0) {
                    $data['shopEvent'] = $event[0];
                }
                else {
                    $data['shopEvent'] = null;
                }
				$resultArray[$j] = $data;
			}
		}
	
		$this->doRespondSuccess ( $resultArray );
	}
	
	
	function writeShopAnswer() {
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$userid =  $this->userInfo['id'];
		$answer_id = $this->input->post ( "answer_id" );
		$comment_id = $this->input->post ( "shopanswerShopCommentID" );
		$content =  $this->input->post ( "shopanswerContent" );

		if($content == null || $comment_id == null){
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$comment = $this->api_model->getShopCommentByID($comment_id);
		if($comment == null || $comment['shopcommentPostManID'] == $userid) {
			$this->doRespondFail ( MSG_YOUR_WRITING );
			return;
		}

		$data = Array (
				"shopanswerPostManID" => $userid,
				"shopanswerShopCommentID" => $comment_id,
				"shopanswerContent" => $content,
				"shopanswerPostTime" =>  $this->util->getTime()
		);
	
		if ($answer_id != NULL) {
			$this->api_model->modifyShopAnswer ( $answer_id, $data );
		} else {
			$this->api_model->createShopAnswer ( $data );
			$this->increaseUserPoint(IDX_POINT_TIP);
		}
	
		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	function removeShopAnswer() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$answer_id = $this->input->post ( "answer_id" );
	
		$this->api_model->removeShopAnswer ( $answer_id );
	
		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	private function isMyShop($user_id, $shop_id) {
		$usershop = $this->api_model->getShopListWithOwnerID($user_id);
		if($usershop != null && count($usershop) > 0) {
			$usershop = $usershop [0];
			if($usershop['id'] == $shop_id) {
				return TRUE;
			}
		}
		
		return FALSE;
	}
	
	function writeShopComment() {
		$isValidHeader = $this->isValidHeaderMe ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$userid =  $this->userInfo['id'];
		$comment_id = $this->input->post ( "comment_id" );
		$shopcommentShopID = $this->input->post ( "shopcommentShopID" );
		$shopcommentShopStatus = $this->input->post ( "shopcommentShopStatus" );
		$shopcommentContent = $this->input->post ( "shopcommentContent" );
		$shopcommentShopLevel = $this->input->post ( "shopcommentShopLevel" );
		$shopcommentType = $this->input->post ( "shopcommentType" );
		
		if($shopcommentType == null || $shopcommentShopID == null) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		if($shopcommentType != 'WI' && $shopcommentContent == null) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		if($this->isMyShop($userid, $shopcommentShopID) == true) {
			$this->doRespondFail ( MSG_YOUR_SHOP );
			return;
		}
	
		$data = Array (
				"shopcommentPostManID" => $userid,
				"shopcommentShopID" => $shopcommentShopID,
				"shopcommentContent" => $shopcommentContent,
				"shopcommentType" => $shopcommentType,
				"shopcommentPostTime" => $this->util->getTime()
		);
		
		if($shopcommentShopStatus != null) {
			$data['shopcommentShopStatus'] = $shopcommentShopStatus;
		}
		
		if($shopcommentShopLevel != null) {
                $data['shopcommentShopLevel'] = $shopcommentShopLevel;
		}
	
		if ($comment_id != NULL) {
			$this->api_model->modifyShopComment ( $comment_id, $data );
		} else {
            $comment_id=$this->api_model->createShopComment ( $data );
			
			if($shopcommentType == TYPE_SHOPCOMMENT_USERREMARK) {
				$this->increaseUserPoint(IDX_POINT_REMARK);
			}
		}

        if(strcmp($shopcommentType, TYPE_SHOPCOMMENT_USERREMARK) == 0) {
            $this->admin_model->updateShopLevel($comment_id);
        }
		
		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	function removeShopComment() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$comment_id = $this->input->post ( "comment_id" );
	
		$this->api_model->removeShopComment ( $comment_id );
		
		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	
	function doMyJim() {
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$shopID = $this->input->post ( 'shop_id' );
		$userID = $this->userInfo['id'];
	
		if ($userID == null || $shopID == null) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$result = $this->api_model->getUserShopRelation ( $userID, $shopID );
		$data = array (
				'usershoprelationUserID' => $userID,
				'usershoprelationShopID' => $shopID,
				'usershoprelationJim' => '1'
		);
		
		if ($result == null) {
			$this->api_model->createUserShopRelation($data);
            $this->increaseUserPoint(IDX_POINT_LIKE);
			$this->doRespondSuccess ( $result );
		}
		else {
			$this->doRespondFail ( MSG_DUPLICATE_JIM );
		}
	}
	
	
	function searchProductList() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$product_name = $this->input->get ( "product_name" );
		$location_name = $this->input->get ( "location_name" );
        $order_type = $this->input->get ( "order_type" );
        $page_num = $this->input->get("page_num");
		$result = $this->api_model->getProductList ($page_num , PAGE_ITEM_CNT_SHOP_PRODUCT, $location_name, $product_name, $order_type );
	
		$resultData = Array();
		if($result != 0) {
			$cnt = count ( $result );
			
			for($i = 0; $i < $cnt; $i ++) {
				$product = $result [$i];
				
				$productInfo = $this->getShopReviewResult($product, $product ["productShopID"]);
			
				array_push ( $resultData, $productInfo );
			}
		}

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_SHOP_COMMENT,
            "total"=>count($resultData),
            "list"=>$resultData
        );

		$this->doRespondSuccess ( $ret );
	}
	

    /**
     * apis related with events.
     * */

    function getLifeEventList() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $categoryID = $this->input->get ( "category_id" );
        $page_num = $this->input->get ( "page_num" );

        $result = $this->api_model->getLifeEventList ( $page_num, PAGE_ITEM_CNT_LIFE, $categoryID);

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_LIFE,
            "total"=>count($result),
            "list"=>$result
        );

        $this->doRespondSuccess ( $ret );
    }

    function  getLifeBanner() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $result = $this->api_model->getAds(ADMIN_LIFE_ADS_ID);
        $this->doRespondSuccess ( $result );
    }

	function getEventList() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$categoryID = $this->input->get ( "category_id" );
		$locationName = $this->input->get ( "location" );
		$shopID = $this->input->get ( "shop_id" );
		$page_num = $this->input->get ( "page_num" );
	
		$result = $this->api_model->getEventList ($page_num, PAGE_ITEM_CNT_SHOP_EVENT, $locationName, $categoryID, $shopID);

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_SHOP_EVENT,
            "total"=>count($result),
            "list"=>$result
        );

        $this->doRespondSuccess ( $ret );
	}

    function clickShop() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $shop_id = $this->input->post ( "shop_id" );

        if($shop_id == null) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $count = $this->api_model->addShopClickCount($shop_id, 1);
        $data = Array(
            'id' =>$shop_id,
            'shopClickCnt' => $count
        );
        $this->doRespondSuccess($data);
    }

    function callShop() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $shop_id = $this->input->post ( "shop_id" );

        if($shop_id == null) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $count = $this->api_model->addShopCallCount($shop_id, 1);
        $data = Array(
            'id' =>$shop_id,
            'shopCallCnt' => $count
        );
        $this->doRespondSuccess($data);
    }

    function clickBanner() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $event_id = $this->input->post ( "banner_id" );

        if($event_id == null) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $this->api_model->createBannerClickLog($event_id);

        $this->doRespondSuccess(MSG_SUCCESS);
    }

    function clickLife() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $event_id = $this->input->post ( "life_id" );

        if($event_id == null) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $this->api_model->createLifeClickLog($event_id);

        $this->doRespondSuccess(MSG_SUCCESS);
    }
	
	function clickEvent() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$event_id = $this->input->post ( "event_id" );

		if($event_id == null) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}

		$count = $this->api_model->addEventClickCount ( $event_id, 1);
        $data = Array(
            'id' =>$event_id,
            'eventClickCnt' => $count
        );
        $this->doRespondSuccess($data);
	}
	
	function registerEvent() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$event_id = $this->input->post ( "event_id" );
		$eventShopID = $this->input->post ( "eventShopID" );
		$eventContent = $this->input->post ( "eventContent" );
		$eventSummary = $this->input->post ( "eventSummary" );
		$eventImgID = $this->input->post ( "eventImgID" );
		$eventMoney = $this->input->post ( "eventMoney" );
		$eventBannerID = $this->input->post ( "eventBannerID" );
		
		if($eventShopID == null || $eventContent == null) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$data = Array();
		
		$data["eventShopID"] = $eventShopID;
		$data["eventContent"] = $eventContent;
		
		if($eventSummary != null) {
			$data["eventSummary"] = $eventSummary;
		}

		if($eventImgID != null) {
			$data["eventImgID"] = $eventImgID;
		}
		
		if($eventMoney != null) {
			$data["eventMoney"] = $eventMoney;
		}
		
		if($eventBannerID != null) {
			$data["eventBannerID"] = $eventBannerID;
		}

		if ($event_id != NULL) {
            $data['id'] = $event_id;
			$result = $this->api_model->updateEvent ($data, MODE_UPDATE );
		} else {

            $data['eventStart'] = $this->util->getDate();
            $data['eventEnd'] = $this->util->getDate();
            $result = $this->api_model->updateEvent ( $data, MODE_ADD );
		}
		$ret['id'] = $result;
		$this->doRespondSuccess( $ret );
	}
	
	function removeEvent() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$event_id = $this->input->post ( "event_id" );
	
		$this->api_model->removeEvent ( $event_id );
	
		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	function getCastList() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$category_id = $this->input->get ( "category_id" );
		$page_num = $this->input->get ( "page_num" );
		
		$result = $this->api_model->getCastList ($page_num, PAGE_ITEM_CNT_CAST, $category_id);

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_CAST,
            "total"=>count($result),
            "list"=>$result
        );

        $this->doRespondSuccess ( $ret );
	}
	
	function getCast() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$cast_id = $this->input->get ( "cast_id" );
		
		$result = $this->api_model->getCast($cast_id);
		
		if($result != null) {
			if($result['castPostManID'] != null) {
				$user = $this->api_model->getUser($result['castPostManID']);
				$result['castPostManNickName'] = $user['userID'];
				$result['castPostManImgURL'] = $user['userImgURL'];
			}
		}
		
		$this->doRespondSuccess ( $result );
	}
	
	function  likeCast() {
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}
	
		$cast_id = $this->input->post("cast_id");
	
		if($cast_id == null) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}
	
		$result = $this->api_model->getCast($cast_id);
		$user_id = $this->userInfo['id'];
		
		if($result != null && $result['castPostManID'] == $user_id) {
			$this->doRespondFail ( MSG_YOUR_WRITING );
			return;
		}
		
		$cast_relation = $this->api_model->getCastRelation($cast_id, $user_id);
        $first_cnt = $this->api_model->getCastLikeCount($cast_id);
        $cast_heart_cnt = $result['castHeartCnt']- $first_cnt;

        $data = Array();
        $result = Array();
        $result['isDuplicate'] = false;

        if($cast_relation == null) {
			$data['castrelationCastID'] = $cast_id;
			$data['castrelationPostManID'] = $user_id;
			$data['castrelationLike'] = 1;
			$this->api_model->createCastRelation($data);
            $this->increaseUserPoint(IDX_POINT_LIKE);
		}
		else {
			if($cast_relation['castrelationLike'] == 0) {
				$data['castrelationLike'] = 1;
                $this->increaseUserPoint(IDX_POINT_LIKE);
			}
			else {
				// $data['castrelationLike'] = 0;
				$data['castrelationLike'] = 1;
				$result['isDuplicate'] = true;
			}
			
			$this->api_model->modifyCastRelation($cast_relation['id'], $data);
		}
		$castData = Array();
		$castData['castHeartCnt'] = $cast_heart_cnt + $this->api_model->getCastLikeCount($cast_id);
		$this->api_model->modifyCast($cast_id, $castData);
        $result['cnt'] = $castData['castHeartCnt'] ;
		$this->doRespondSuccess($result);
	}
	
	function shareCast() {
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}
	
		$cast_id = $this->input->post("cast_id");
	
		if($cast_id == null) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}
	
		$user_id = $this->userInfo['id'];
		$cast_relation = $this->api_model->getCastRelation($cast_id, $user_id);
		$data = Array();
		$result = Array();
		$result['isDuplicate'] = false;
		if($cast_relation == null) {
			$data['castrelationCastID'] = $cast_id;
			$data['castrelationPostManID'] = $user_id;
			$data['castrelationShare'] = 1;
			$this->api_model->createCastRelation($data);
		}
		else {
			if($cast_relation['castrelationShare'] == 0) {
				$data['castrelationShare'] = 1;
			}
			else {
				// $data['castrelationShare'] = 0;
				$data['castrelationShare'] = 1;
				$result['isDuplicate'] = true;
			}
			$this->api_model->modifyCastRelation($cast_relation['id'], $data);
		}
		
		$result['cnt'] = $this->api_model->getCastShareCount($cast_id);
		$castData = Array();
		$castData['castShareCnt'] = $result['cnt'];
		$this->api_model->modifyCast($cast_id, $castData);
		$this->doRespondSuccess($result);
	}
	
	function getCastDetailList() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$cast_id = $this->input->get ( "cast_id" );
		
		if ($cast_id == null) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$result = $this->api_model->getCastDetailList ( $cast_id );
		$this->doRespondSuccess ( $result );
	}
	
	function getBestCastCommentList() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $cast_id = $this->input->get ( "cast_id" );

        if ($cast_id == null) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $result = $this->api_model->getCastCommentList (1, ADMIN_CNT_BEST_CAST_COMMENT, $cast_id, null, 1 );
        $this->doRespondSuccess ( $result );
    }

	function getCastCommentList() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$cast_id = $this->input->get ( "cast_id" );
		$type = $this->input->get("type");
		
		if ($cast_id == null) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}

        $page_num = $this->input->get ( "page_num" );

        $result = $this->api_model->getCastCommentList ($page_num, PAGE_ITEM_CNT_CAST_COMMENT, $cast_id, null, $type);

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_CAST_COMMENT,
            "total"=>count($result),
            "list"=>$result
        );

        $this->doRespondSuccess ( $ret );
	}

	
	function  likeCastComment() {
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}
	
		$cast_comment_id = $this->input->post("cast_comment_id");

		if($cast_comment_id == null) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}

		$result = $this->api_model->getCastComment($cast_comment_id);
		$user_id = $this->userInfo['id'];
		
		if($result == null) {
			$this->doRespondFail ( MSG_ERR_INPUT_INVALID );
			return;
		}
	
		if($result != null && $result['castcommentPostManID'] == $user_id) {
			$this->doRespondFail ( MSG_YOUR_WRITING );
			return;
		}

		$cast_comment_relation = $this->api_model->getCastCommentRelation($cast_comment_id, $user_id);
		$data = Array();
		$result = Array();
		$result['isDuplicate'] = false;
		if($cast_comment_relation == null) {
			$data['castcommentrelationCastCommentID'] = $cast_comment_id;
			$data['castcommentrelationPostManID'] = $user_id;
			$data['castcommentrelationLike'] = 1;
			$data['castcommentrelationPostTime'] = $this->util->getTime();
			$this->api_model->createCastCommentRelation($data);

            $this->increaseUserPoint(IDX_POINT_LIKE);
		}
		else {
			if($cast_comment_relation['castcommentrelationLike'] == 0) {
				$data['castcommentrelationLike'] = 1;
                $this->increaseUserPoint(IDX_POINT_LIKE);
			}
			else {
				// $data['castrelationLike'] = 0;
				$data['castcommentrelationLike'] = 1;
				$result['isDuplicate'] = true;
			}
			
			$this->api_model->modifyCastCommentRelation($cast_comment_relation['id'], $data);
		}
		
		$result['cnt'] = $this->api_model->getCastCommentLikeCount($cast_comment_id);
		
		$castData = Array();
		$castData['castcommentHeartCnt'] = $result['cnt'];
        $castData['id'] = $cast_comment_id;
		$this->api_model->updateCastComment($castData, MODE_UPDATE);
		
		$this->doRespondSuccess($result);
	}
	
	
	function writeCastComment() {
		$isValidHeader = $this->isValidHeaderMe();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL);
			return;
		}
		
		$castcomment_id = $this->input->post ( "castcomment_id" );
		$castcommentContent = $this->input->post ( "castcommentContent" );
		$castcommentCastID = $this->input->post ( "castcommentCastID" );
		$userid =  $this->userInfo['id'];
		
		if($castcommentContent == null || $castcommentCastID == null) {
			$this->doRespondFail ( MSG_FAIL);
			return;
		}
		
		$data = Array (
				"castcommentContent" => $castcommentContent,
				"castcommentPostManID" => $userid,
				"castcommentCastID" => $castcommentCastID,
				"castcommentPostTime" => $this->util->getMilliTime()
		);

        if($castcomment_id == null) {
            $ret_id = $this->api_model->updateCastComment($data, MODE_ADD);
        }
        else {
            $data['id'] = $castcomment_id;
            $ret_id = $this->api_model->updateCastComment($data, MODE_UPDATE);
        }

        if($ret_id == null || $ret_id < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail (MSG_ERR_SQL);
            return;
        }

        $ret = Array("id"=>$ret_id);
        $this->doRespondSuccess( $ret );
	}
	
	function removeCastComment() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}
		
		$castcomment_id = $this->input->post ( "castcomment_id" );

        $ret_id = $this->api_model->updateCastComment ( $castcomment_id, MODE_REMOVE );
        if($ret_id == null || $ret_id < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail (MSG_ERR_SQL);
            return;
        }

		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	function registerGCMRegID() {
		$isValidHeader = $this->isValidHeaderMe();
		
		if ($isValidHeader == false) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}
		
		$reg_id = $this->input->post ( "reg_id" );
		
		if($reg_id == null) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}

		$userid =  $this->userInfo['id'];
		$gcmRegId =$this->api_model->getUserGCMRegID( $userid );
			
		$data =  Array(
					'userGCMRegID' => $reg_id
		);
		
		$this->api_model->updateUser ( $userid, $data );
		
//		$result = $this->api_model->getGCMLogWithParams($userid, MSG_WELCOME, 3);	
		if($gcmRegId == null || $gcmRegId['userGCMRegID'] == '') {
			$this->sendPushEnvelopForLog(MSG_WELCOME, $userid);
		}
		$this->doRespondSuccessWithMsg( MSG_SUCCESS );
	}
	
	function getEnvelopList() {
		$isValidHeader = $this->isValidHeaderMe();
		
		if ($isValidHeader == false) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}
		
		$userid =  $this->userInfo['id'];
        $page_num = $this->input->get("page_num");
        $result = $this->api_model->getGCMLogList ($page_num, PAGE_ITEM_CNT_ENVELOP, $userid, GCM_LOG_ENVELOP);

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_ENVELOP,
            "total"=>count($result),
            "list"=>$result
        );

        $this->doRespondSuccess ( $ret );
	}
	
	function modifyGCMList() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}
		
		$arr_gcm =  $this->input->post('gcm_array');
		
		if($arr_gcm == null) {
			$this->doRespondFail (MSG_FAIL);
			return ;
		}
		
		$arrGCM = json_decode($arr_gcm, true);
		$cnt = count($arrGCM);
		
		for($i = 0; $i < $cnt; $i++) {
			$gcmObj = $arrGCM[$i];
			$gcm_id = $gcmObj['gcm_id'];
			$gcmReaded = $gcmObj['gcmlogReaded'];
			$gcmStatus = $gcmObj['gcmlogStatus'];
			
			$data = Array();
			if($gcmReaded != null) {
				$data['gcmlogReaded'] = $gcmReaded;
                if($gcmReaded == 1) {
                    $data['gcmlogReadTime'] = $this->util->getTime();
                }
			}
			
			if($gcmStatus != null) {
				$data['gcmlogStatus'] = $gcmStatus;
			}
			$this->api_model->modifyGCMLog($gcm_id, $data);
		}
		
		$this->doRespondSuccessWithMsg(MSG_SUCCESS);
	}
	
	function modifyGCM() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}
		
		$gcm_id =  $this->input->post('gcm_id');
		
		if($gcm_id == null) {
			$this->doRespondFail (MSG_FAIL);
			return ;
		}
		
		$gcmReaded = $this->input->post('gcmlogReaded');
		
		$data = Array();
		if($gcmReaded != null) {
			$data['gcmlogReaded'] = $gcmReaded;

            if($gcmReaded == 1) {
                $data['gcmlogReadTime'] = $this->util->getTime();
            }
		}
		
		$gcmStatus = $this->input->post('gcmlogStatus');
		if($gcmStatus != null) {
			$data['gcmlogStatus'] = $gcmStatus;
		}
		
		$this->api_model->modifyGCMLog($gcm_id, $data);
		
		$this->doRespondSuccessWithMsg(MSG_SUCCESS);
	}
	
	private function makeGCMData($type) {

		if($type == null) {
			return null;
		}
		
		$resultData = Array();
		
		if($type == GCM_LOG_COMMENT) {
			$resultData['title'] = '댓글알림';
		}
		else if($type == GCM_LOG_TAG) {
			$resultData['title'] = '태그알림';
		}
		else if($type == GCM_LOG_ENVELOP) {
			$resultData['title'] = '쪽지알림';
		}

        $resultData['type'] = (string)$type;

		return $resultData;
	}
	
	private function sendPushComment($freetalk_id) {
		if($this->userInfo == null) {
			return;
		}
		
		$freetalk = $this->api_model->getFreetalk($freetalk_id);
		if($freetalk != null) {
			$user = $this->api_model->getUser($freetalk['freetalkPostManID']);
			$msg = $this->userInfo['userID'].'님이 당신의 글에 댓글을 남겼습니다.';
			$this->sendPushMessageWithRegTokens($user['id'], $user['userOsType'], $user['userGCMRegID'], GCM_LOG_COMMENT, $msg);
		}
	}
	
	private function sendPushTAG($user_id) {
		if($this->userInfo == null) {
			return;
		}
		
		$user = $this->api_model->getUser($user_id);
		if($user != null) {
			$msg = $this->userInfo['userID'].'님이 당신의 글에 태그했습니다.';
			$this->sendPushMessageWithRegTokens($user['id'], $user['userOsType'], $user['userGCMRegID'], GCM_LOG_TAG, $msg);
		}
		
	}
	
	private function sendPushEnvelopForLog($msg, $user_id) {
		$user = $this->api_model->getUser($user_id);
		$this->sendPushMessageWithRegTokens($user['id'], $user['userOsType'], $user['userGCMRegID'], GCM_LOG_ENVELOP, $msg);
	}
	
	
	private function sendPushMessageWithRegTokens($user_id, $gcm_type, $reg_id, $type, $msg)
	{
		$this->load->model('api_model');
		if ($user_id != null) {
			$resultData = $this->makeGCMData($type);
			$resultData['message'] = $msg;
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
						'gcmlogContent'=>$msg,
						'gcmlogUserID'=>$user_id,
                        'gcmlogTitle'=>$resultData['title'],
						'gcmlogPostTime' => $this->util->getTime()   // MySQL datetime format
						);
				$this->api_model->createGCMLog($logData);
				

                if($resultData['type'] == GCM_LOG_TAG) {
                    $newMessage = Array(
                        'userNewInform'=>'Y'
                    );
                }
                else {
                    $newMessage = Array(
                        'userNewMessage'=>'Y'
                    );
                }

				$this->api_model->updateUser($user_id, $newMessage);
				
			} else {
				$push_result_msg = "푸시메시지를 전송할수 없습니다.";
				$push_result_code = -1;
			}
	
	
		} else {
			$push_result_msg = "장치토큰을 얻을수 없습니다.";
			$push_result_code = -1;
		}
		
		$this->doRespondSuccess ( $push_result_msg );
	}
	
	
	function getCategoryList() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$result = $this->api_model->getCategoryList ();
	
		if ($result == null) {
			$this->doRespondFail ( MSG_FAIL );
		} else {
			$this->doRespondSuccess ( $result );
		}
	}
	
	function getLocationList() {
		
		$type = $this->input->get("type");
		
		if($type != 'C') {
			$isValidHeader = $this->isValidHeader ();
		
			if ($isValidHeader == false) {
				$this->doRespondFail ( MSG_FAIL );
				return;
			}
		}		
		
		
		$searhWord = $this->input->get('search_word');
		
		$result = $this->api_model->getLocationList ($searhWord, MAX_SEARCH_CNT);
		$this->doRespondSuccess ( $result );
	}
	
	function  getTubeList() {
		$isValidHeader = $this->isValidHeader ();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$name = $this->input->get('tubeName');
		
		$result = $this->api_model->getTubeList ($name);
		
		if ($result == null) {
			$this->doRespondFail ( MSG_FAIL );
		} else {
			$this->doRespondSuccess ( $result );
		}
	}
	
	
	function getRecentNewsList() {
		$isValidHeader = $this->isValidHeaderMe();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$userid =  $this->userInfo['id'];
        $page_num = $this->input->get("page_num");

        $result = $this->api_model->getFreetalkCommentTAGRecentList ($page_num, PAGE_ITEM_CNT_FREETALK_COMMENT_TAG, $userid);

        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_FREETALK_COMMENT_TAG,
            "total"=>count($result),
            "list"=>$result
        );

        $this->doRespondSuccess ( $ret );
	}
	
	
	function findShopInfo() {
		$isValidHeader = $this->isValidHeaderMe();
		
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
		
		$userid =  $this->userInfo['id'];
		$shopName = $this->input->post('shop_name');
		$phoneNumber = $this->input->post('phone_number');
        $shop_id = $this->input->post('shop_id');

		if ($shopName == null || $phoneNumber == null) {
			$this->doRespondFail (MSG_FAIL);
			return;
		}
		
		$content = $shopName.'의 상점정보(비번포함)을 '.$phoneNumber.'에 연락해주십시오.';
		
		$data = Array(
			'questionPostManID' => $userid,
			'questionContent' => $content ,
            'questionAddress' => $phoneNumber ,
            'questionType' => ADMIN_TYPE_ADMIN_QUESTION_SHOP_PASSWORD ,
            'questionShopID' => $shop_id ,
			'questionPostTime' => $this->util->getTime()   // MySQL datetime format
		);
		
		$this->api_model->createQuestion($data);
		$this->doRespondSuccessWithMsg(MSG_SUCCESS);
	}

    function  getBannerCommentList() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $banner_id = $this->input->get('banner_id');
        if($banner_id == null) {
            $this->doRespondFail ( MSG_ERR_INPUT_INVALID );
            return;
        }

        $page_num = $this->input->get('page_num');

        $result = $this->api_model->getBannerCommentList($page_num, PAGE_ITEM_CNT_BANNER_COMMENT, $banner_id);
        $ret = Array(
            "page"=>$page_num,
            "rows_per_page"=>PAGE_ITEM_CNT_BANNER_COMMENT,
            "total"=>count($result),
            "list"=>$result
        );
        $this->doRespondSuccess ( $ret );
    }

    function writeBannerComment() {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL);
            return;
        }

        $banner_id = $this->input->post ( "banner_id" );
        $comment = $this->input->post ( "content" );
        $user_id =  $this->userInfo['id'];

        if($comment == null || $banner_id == null) {
            $this->doRespondFail ( MSG_ERR_INPUT_INVALID);
            return;
        }

        $data = Array (
            "bannercommentContent" => $comment,
            "bannercommentPostManID" => $user_id,
            "bannercommentBannerID" => $banner_id,
            "bannercommentPostTime" => $this->util->getMilliTime()
        );

        $comment_id = $this->input->post('comment_id');

        if ($comment_id != NULL) {
            $data['id'] = $comment_id;
            $ret_id = $this->api_model->updateBannerComment ( $data, MODE_UPDATE );
        } else {
            $ret_id = $this->api_model->updateBannerComment ( $data, MODE_ADD );
        }

        if($ret_id == null || $ret_id < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail (MSG_ERR_SQL);
            return;
        }

        $ret = Array("id"=>$ret_id);
        $this->doRespondSuccess( $ret );
    }

    function removeBannerComment() {
        $isValidHeader = $this->isValidHeader ();

        if ($isValidHeader == false) {
            $this->doRespondFail (MSG_FAIL);
            return;
        }

        $comment_id = $this->input->post ( "comment_id" );

        if($comment_id == null) {
            $this->doRespondFail ( MSG_ERR_INPUT_INVALID);
            return;
        }

        $data = Array("id" => $comment_id);
        $ret = $this->api_model->updateBannerComment ( $data, MODE_REMOVE );

        if($ret == null || $ret < ADMIN_SQL_VALID_ID_MIN) {
            $this->doRespondFail (MSG_ERR_SQL);
            return;
        }

        $this->doRespondSuccessWithMsg( MSG_SUCCESS );
    }

    private function registerUploadImage($image_url, $user_id, $location_type) {
        $data = Array (
            "imageURL" => $image_url,
            "imageUploadLocation" => $location_type,
            "imageUploadUserID" => $user_id,
            "imageUploadTime" => $this->util->getTime()
        );

        $result_img = $this->api_model->createImage ( $data );
        return $result_img;
    }

	function uploadFreeTalkImage(){
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}

		$imageURL = $this->uploadImage ("freetalk", "img_filename");

		if ($imageURL != null) {
            $userid =  $this->userInfo['id'];
            $result_img = $this->registerUploadImage($imageURL, $userid, IMAGE_LOCATION_FREETALK);
			$this->doRespondSuccess( $result_img );
		}
		else {
			$this->doRespondFail ( MSG_FAIL );
		}
	
	}
	
	function uploadShopImage(){
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$imageURL = $this->uploadImage ("shop", "img_filename");

		if ($imageURL != null) {

            $userid =  $this->userInfo['id'];
            $result_img = $this->registerUploadImage($imageURL, $userid, IMAGE_LOCATION_SHOP);
			
			$this->doRespondSuccess( $result_img );
		}
		else {
			$this->doRespondFail ( MSG_FAIL );
		}
	}
	
	function uploadEventImage(){
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$imageURL = $this->uploadImage ("event", "img_filename");

		if ($imageURL != null) {
            $userid =  $this->userInfo['id'];
            $result_img = $this->registerUploadImage($imageURL, $userid, IMAGE_LOCATION_EVENT);
			$this->doRespondSuccess( $result_img );
		}
		else {
			$this->doRespondFail ( MSG_FAIL );
		}
	}
	
	function uploadUserImage(){
		$isValidHeader = $this->isValidHeaderMe();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$imageURL = $this->uploadImage ("user", "img_filename", 200, 250 );
	
			
		if ($imageURL != null) {
            $userid =  $this->userInfo['id'];
            $result_img = $this->registerUploadImage($imageURL, $userid, IMAGE_LOCATION_USER);
	
			$this->doRespondSuccess( $result_img );
		}
		else {
			$this->doRespondFail ( MSG_FAIL );
		}
	}
	
	function getImage() {
		$isValidHeader = $this->isValidHeader ();
	
		if ($isValidHeader == false) {
			$this->doRespondFail ( MSG_FAIL );
			return;
		}
	
		$image_id = $this->input->get ( "image_id" );
		$result = $this->api_model->getImage ($image_id);
	
		if ($result == null) {
			$this->doRespondFail ( MSG_FAIL );
		} else {
			$this->doRespondSuccess ( $result );
		}
	}

    function logOut() {
        $isValidHeader = $this->isValidHeaderMe();

        if ($isValidHeader == false) {
            $this->doRespondFail ( MSG_FAIL );
            return;
        }

        $userid =  $this->userInfo['id'];
        $this->api_model->removeUser($userid);
        $this->doRespondSuccessWithMsg ( MSG_SUCCESS );
    }
}

/* End of file api.php */
/* Location: ./application/controllers/api.php */

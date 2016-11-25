

<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Admin_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();
        $this->load->library('util');
        $this->load->model('freetalk_model');
        $this->load->model('shop_model');
    }

    /**
     * Shop Information
     */

    function  getTotalShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getHairShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_HAIR_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveHairShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_HAIR_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getNailShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_NAIL_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveNailShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_NAIL_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getSoknunShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_SOKNUN_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveSoknunShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_SOKNUN_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }
    
    function  getWaksingShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_WAKSING_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveWaksingShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_WAKSING_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }
        
    function  getSkinShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_SKIN_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveSkinShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_SKIN_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }
        
    function  getMassageShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_MASSAGE_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveMassageShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_MASSAGE_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }
    
    function  getBanyongguShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_BANYONGGU_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveBanyongguShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_BANYONGGU_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }    

    function  getTatuShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_TATU_CATEGORY)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLiveTatuShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopCategoryID', ID_TATU_CATEGORY)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    } 
                
    function getTodayRequestShopCount() {
        $strTodayCondition = "DATE(shopRequestTime) = CURDATE()";
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where($strTodayCondition)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function getRequestShopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shop')
            ->where('shopStatus', STATUS_LIFE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    /**
     * Question Information
     */

    function  getTodayQuestionCount() {
        $strTodayCondition = "DATE(questionPostTime) = CURDATE()";
        $query =  $this->db->select('count(*) as count')
            ->from('tb_question')
            ->where($strTodayCondition)
            ->where('questionStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }


    /**
     * Talk Information
     */

    function  getTotalTalkCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_freetalk')
            ->where('freetalkStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getTodayTalkCount() {
        $strTodayCondition = "DATE(FROM_UNIXTIME(freetalkPostTime)) = CURDATE()";
        $query =  $this->db->select('count(*) as count')
            ->from('tb_freetalk')
            ->where($strTodayCondition)
            ->where('freetalkStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    /**
     * Wrong Information
     */
    function  getWrongInfoCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shopcomment')
            ->where('shopcommentType', TYPE_SHOPCOMMENT_WRONGINFO)
            ->where('shopcommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }



    /**
     * Member Information
     */
    function  getAccessCount() {
		$query =  $this->db->select('cnt')
                ->from('tb_access')
	            ->get();
		if ($query->num_rows() > 0) {
			$result = $query->row()->cnt;			
		} else {
			$result = 0;
		}
		return $result;
	}
	 
    function  getMemberCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getTodayMemberCount() {
        $strTodayCondition = "DATE(userRegisterTime) = CURDATE()";
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where($strTodayCondition)
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getManMemberCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userSex', USER_SEX_MAN)
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getWomanMemberCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userSex', USER_SEX_WOMAN)
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getPeopleCountWithAge($startAge, $endAge) {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userAge >= '.$startAge.' AND userAge < '.$endAge)
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getTodayOutMemberCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userStatus = '.STATUS_DELETE)
            ->where('DATE(userRegisterTime) = CURDATE()')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getTotalOutMemberCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userStatus = '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLowHeartCommmentListCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shopcomment')
            ->where('shopcommentType','UR')
            ->where('shopcommentShopLevel <= '.LIMIT_LOW_SHOP_LEVEL)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getLowHeartCommmentArray($cnt, $page_num = null) {

        if($page_num != null && $page_num >= 1) {
            $last_id = ($page_num - 1)*$cnt;
            $this->db->limit($cnt, $last_id);
        }

        if($cnt != null) {
            $this->db->limit($cnt);
        }

        $query =  $this->db->select('*, tb_shopcomment.id')
            ->from('tb_shopcomment')
            ->where('shopcommentType','UR')
            ->where('shopcommentShopLevel <= '.LIMIT_LOW_SHOP_LEVEL)
            ->join('tb_user', 'shopcommentPostManID = tb_user.id')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  updateShopComment($id, $status) {
        $this->shop_model->updateShopComment($id, $status);

        $this->updateShopLevel($id);
    }

    function  updateShopLevel($shop_comment_id) {
        $query =  $this->db->select('shopcommentShopID as id')
            ->from('tb_shopcomment')
            ->where('id', $shop_comment_id)
            ->get();

        $shop_id = 0;
        if ($query->num_rows() > 0) {
            $result = $query->row();
            $shop_id = $result->id;

        }

        if($shop_id == 0) {
            return;
        }

        $query =  $this->db->select('AVG(shopcommentShopLevel) as level')
            ->from('tb_shopcomment')
            ->where('shopcommentShopID = '.$shop_id)
            ->where('shopcommentType', TYPE_SHOPCOMMENT_USERREMARK)
            ->where('shopcommentStatus != '.STATUS_DELETE)
            ->get();

        $shopLevel = 0;
        if ($query->num_rows() > 0) {
            $result = $query->row();
            $shopLevel = $result->level;
        }

        if($shopLevel == null) {
            $shopLevel = 0;
        }

        $data = Array(
            "shopLevel" =>$shopLevel
        );
        $this->db->update('tb_shop', $data, "id = ".$shop_id);
    }

    /**
     * Declared Commment Array
     */
    function  getFreeTalkComment($freetalk_comment_id) {
        $selectQuery = '*,
						tb_freetalkcomment.id';

        $this->db->where('tb_freetalkcomment.id', $freetalk_comment_id);

        $query = $this->db->select($selectQuery)
            ->from('tb_freetalkcomment')
            ->join('tb_user', 'tb_freetalkcomment.freetalkcommentPostManID = tb_user.id')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];

        } else {
            $result = null;
        }

        return $result;
    }

    function getUser($user_id) {

        $query = $this->db->select('*')
            ->from('tb_user')
            ->where("id", $user_id)
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function  getDeclareCommentCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_freetalkcommentrelation')
            ->where('freetalkcommentrelationDeclare', STATUS_ON)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getDeclareCommentArray($cnt, $page_num= null) {
        $arrDeclare = $this->getDeclareArray($cnt, $page_num);
        $cntDeclare = count($arrDeclare);

        $arrComment = Array();

        for($i = 0; $i < $cntDeclare; $i++) {

            $declare = $arrDeclare[$i];
            $comment = $this->getFreeTalkComment($declare['freetalkcommentrelationFreetalkCommentID']);
            $user = $this->getUser($declare['freetalkcommentrelationPostManID']);

            if($comment != null && $user != null) {
                $comment['declareUserID'] = $user['userID'];
                array_push($arrComment, $comment);
            }
        }

        return $arrComment;
    }

    function  getDeclareArray($cnt, $page_num) {

        if($page_num != null) {
            $last_id = ($page_num-1)*$cnt;
            $this->db->limit($cnt, $last_id);
        }

        if($cnt != null) {
            $this->db->limit($cnt);
        }

        $query =  $this->db->select('*')
            ->from('tb_freetalkcommentrelation')
            ->where('freetalkcommentrelationDeclare', STATUS_ON)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  updateComment($id, $status) {
        $this->freetalk_model->updateComment($id, $status);
    }

    /**
     * Evnet Array
     */
    function  getLiveEventArray() {

        $this->db->join('tb_shop', 'tb_event.eventShopID = tb_shop.id and shopStatus != '.STATUS_DELETE);

        $query =  $this->db->select('*,
                    tb_event.id as id,
                    DATEDIFF(eventEnd, CURDATE()) as eventRemainDate')
            ->from('tb_event')
            ->where('DATEDIFF(eventEnd,CURDATE()) <= 7 and DATEDIFF(eventEnd,CURDATE()) >= 0')
            ->where('eventShopID IS NOT NULL')
            ->where('eventStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  getEventConsequenceCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_event')
            ->where('eventStatus != '.STATUS_DELETE)
            ->where('eventConsequence !='.LIMIT_CONSEQUE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function getEvent($event_id) {
        $query = $this->db->select('*')
            ->from('tb_event')
            ->where('eventStatus != '.STATUS_DELETE)
            ->where('eventConsequence !='.LIMIT_CONSEQUE)
            ->where('id', $event_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

	function getAgreement($type){
		if( $type == null) {
			$query = $this->db->select('*')
	            ->from('tb_agreement')
	            ->get();			
		} else {
			$query = $this->db->select('*')
	            ->from('tb_agreement')
	            ->where('type',$type)
	            ->get();
	    }
        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            //$result = $result[0];
        } else {
            $result = Array();
        }

        return $result;
	}
	
	function setAgreement($type,$content){
        $updateData = Array(
            'content' => $content,
            'modify_time' => date("Y-m-d H:i:s")
        );
        $this->db->update('tb_agreement', $updateData, "type = ".$type);
	}
	
    function getEventConsequenceArray($cnt, $page_num) {
        if($page_num != null && $cnt != null) {
            $last_id = ($page_num-1)*$cnt;
            $this->db->limit($cnt, $last_id);
        }

        $this->db->order_by("eventConsequence");

        $query = $this->db->select('*')
            ->from('tb_event')
            ->where('eventStatus != '.STATUS_DELETE)
            ->where('eventConsequence !='.LIMIT_CONSEQUE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  updateEventConsequence($shop_id, $consequence) {
        $updateData = Array(
            'eventConsequence' => $consequence
        );
        $this->db->update('tb_event', $updateData, "id = ".$shop_id);
    }

    function removeEventConsequence1($event_id) {
        $index = 1;
        $event = $this->getEvent($event_id);
        $cnt = $this->getEventConsequenceCount();

        for($i = 0; $i < $cnt; $i++) {
            if(($i + 1)  != $event['eventConsequence']) {
                $updateData = Array(
                    'eventConsequence' => $index
                );
                $this->db->update('tb_event', $updateData, "eventConsequence = ".($i + 1));
                $index++;
            }
            else {
                $updateData = Array(
                    'eventConsequence' => LIMIT_CONSEQUE
                );
                $this->db->update('tb_event', $updateData, "id = ".$event_id);
            }
        }
    }

    function removeEventConsequence2($consequence) {
        $updateData = Array(
            'eventConsequence' => LIMIT_CONSEQUE
        );
        $this->db->update('tb_event', $updateData, "eventConsequence = ".$consequence);
    }

    function setBannerArray($json_point_array) {
        $arrAds = json_decode($json_point_array, true);

        // mainBannerArray
        $arrMainBanner = Array();
        for($i = 0; $i < ADMIN_MAIN_ADS_CNT; $i++) {
            $adsID = $arrAds[$i]['id'];
            $adsURL = $arrAds[$i]['adsURL'];
            $adsImgID = $arrAds[$i]['adsImgID'];

            if($adsImgID == null || $adsURL== null || strcmp($adsImgID, "") == 0) {
                continue;
            }
            if($adsID == null) {
                 $result = $this->createAds($adsImgID, $adsURL);
                array_push($arrMainBanner, $result['id']);
            }
            else {
                $this->updateAds($adsID, $adsImgID, $adsURL);
                array_push($arrMainBanner, $adsID);
            }
        }
        if(count($arrMainBanner) == ADMIN_MAIN_ADS_CNT) {
            $str_json = json_encode($arrMainBanner);
            $data = Array(
                "appAdsIDArrayString" => $str_json
            );
            $this->db->update("tb_app", $data);
        }

        // lifBannerAds
        $adsID = $arrAds[3]['id'];
        $adsURL = $arrAds[3]['adsURL'];
        $adsImgID = $arrAds[3]['adsImgID'];

        if ($adsID != null) {
            $this->updateAds($adsID, $adsImgID, $adsURL);
        }

        // castBannerAds
        for($i = 4; $i < (4+ADMIN_CAST_ADS_CNT); $i++) {
            $adsID = $arrAds[$i]['id'];
            $adsImgID = $arrAds[$i]['adsImgID'];

            if($adsImgID == null || strcmp($adsImgID, "") == 0) {
                continue;
            }

            if($adsID != null && strcmp($adsID, "") != 0) {
                $data = Array(
                    "castBannerImgID" =>$adsImgID
                );
                $this->db->update('tb_cast', $data, "id = ".$adsID);
            }
        }
    }

    function  createAds($ads_img_id, $ads_url) {
        $data = Array(
            "adsImgID" => $ads_img_id,
            "adsURL" =>$ads_url
        );

        $this->db->insert("tb_ads", $data);

        $query = $this->db->select('*')
            ->from('tb_ads')
            ->where('adsStatus != '.STATUS_DELETE)
            ->where('adsImgID', $ads_img_id)
            ->where('adsURL', $ads_url)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        }

        return $result;
    }

    function  updateAds($ads_id, $ads_img_id, $ads_url) {
        $data = Array(
            "adsImgID" => $ads_img_id,
            "adsURL" =>$ads_url
        );
        $this->db->update('tb_ads', $data, "id = ".$ads_id);
    }

    function  removeQuestion($question_id) {
        $data = Array(
            "questionStatus" => STATUS_DELETE
        );
        $this->db->update('tb_question', $data, "id = ".$question_id);
    }

    function  removeQuestionByUserID($user_id) {
        $data = Array(
            "questionStatus" => STATUS_DELETE
        );
        $this->db->update('tb_question', $data, "questionPostManID = ".$user_id);
    }

    function  getBannerCastList() {
        $this->db->limit(ADMIN_CAST_ADS_CNT);

        $query =  $this->db->select('*')
            ->from('tb_cast')
            ->where('castStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }


    function  getAdminUserInfo() {
        $query =  $this->db->select('*')
            ->from('tb_user')
            ->where('id = '.ADMIN_ID)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];

        } else {
            $result = null;
        }

        return $result;
    }

    function  getQuestionCount($type) {
        if($type != null) {
            $this->db->where('questionType', $type);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_question')
            ->where('questionStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getQuestionList($type, $cnt, $page_num) {
        if($page_num != null) {
            $last_id = ($page_num-1)*$cnt;
            $this->db->limit($cnt, $last_id);
        }

        if($cnt != null) {
            $this->db->limit($cnt);
        }

        if($type != null) {
            $this->db->where('questionType', $type);
        }

        $query =  $this->db->select('*,
                                    (select userID from tb_user where tb_user.id = tb_question.questionPostManID) as userID,
                                    tb_question.id as id,
                                    (select shopID from tb_shop where tb_shop.id = tb_question.questionShopID) as shopID
                                    ')
            ->from('tb_question')
            ->where('questionStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function getAppSetting() {
        $query =  $this->db->select('*')
            ->from('tb_setting')
            ->where('settingStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        }
        else {
            $result = null;
        }
        return $result;
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
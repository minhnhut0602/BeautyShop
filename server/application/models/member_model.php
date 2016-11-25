
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Member_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        $this->load->library('util');
        $this->load->model('freetalk_model');
        $this->load->model('banner_model');
        $this->load->model('gcm_model');
        $this->load->model('shop_model');
        $this->load->model('cast_model');
    }

    function  getUserCount($userID,$userSex, $userLocation, $userStatus, $userLevel, $userRecommender) {
        if($userID != null) {
            $this->db->where('userID', $userID);
        }

        if($userSex != null) {
            $this->db->where('userSex', $userSex);
        }

        if($userLocation != null) {
            $newLocation = $this->util->getUserFilteredLocation($userLocation);
            $strWhere = "INSTR(userLocationName,'".$newLocation."') > 0";
            $this->db->where($strWhere);
        }

        if($userStatus != null) {
            $this->db->where('userStatus', $userStatus);
        }

        if($userLevel != null) {
            $this->db->where('userLevel', $userLevel);
        }

        if($userRecommender != null) {
            $this->db->where('userRecommender', $userRecommender);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->where('id != '.ADMIN_ID)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getUserList($page_num, $userID, $userSex, $userLocation, $userStatus, $userLevel, $userRecommender) {
        if($page_num != null) {
            $last_id = ($page_num - 1)* ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($userID != null) {
            $this->db->where('userID', $userID);
        }

        if($userSex != null) {
            $this->db->where('userSex', $userSex);
        }

        if($userLocation != null) {
            $newLocation = $this->util->getUserFilteredLocation($userLocation);
            $strWhere = "INSTR(userLocationName,'".$newLocation."') > 0";
            $this->db->where($strWhere);
        }

        if($userStatus != null) {
            $this->db->where('userStatus', $userStatus);
        }

        if($userLevel != null) {
            $this->db->where('userLevel', $userLevel);
        }

        if($userRecommender != null) {
            $this->db->where('userRecommender', $userRecommender);
        }

        $this->db->order_by("userRegisterTime", "desc");

        $query =  $this->db->select('*')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->where('id != '.ADMIN_ID)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  getIDbyUserID($userID) {
        $query =  $this->db->select('id')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->where('userID', $userID)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->id;
        }
        else {
            $result = null;
        }
        return $result;
    }

    function  getPoint($user_level) {
        $user_point = 0;
        switch($user_level) {
            case 1:
                $user_point = USER_LEVEL1_POINT;
                break;
            case 2:
                $user_point = USER_LEVEL2_POINT;
                break;
            case 3:
                $user_point = USER_LEVEL3_POINT;
                break;
            case 4:
                $user_point = USER_LEVEL4_POINT;
                break;
            case 5:
                $user_point = USER_LEVEL5_POINT;
                break;
            default:
                $user_point = USER_LEVEL0_POINT;
                break;
        }

        return $user_point;
    }

    function  updateLevel($user_id, $user_level) {

        $user_point = $this->getPoint($user_level);

        $data = Array(
            'userLevel' => $user_level,
            'userPoint' => $user_point);

        $this->db->update('tb_user', $data, "id = ".$user_id);
    }

    private function getLevel($point) {

        $level = 0;
        if($point < USER_LEVEL1_POINT) {
            $level = 0;
        }
        else if($point < USER_LEVEL2_POINT) {
            $level = 1;
        }
        else if($point < USER_LEVEL3_POINT) {
            $level = 2;
        }
        else if($point < USER_LEVEL4_POINT) {
            $level = 3;
        }
        else if($point < USER_LEVEL5_POINT) {
            $level = 4;
        }
        else if($point >= USER_LEVEL5_POINT) {
            $level = 5;
        }

        return $level;
    }

    function updateRecommendID($user_id, $recommend_id) {
        $newdata = array(
            'userRecommenderID' => $recommend_id
        );
        $this->db->update('tb_user', $newdata, "id = ".$user_id);
    }

    function setNewMessage($user_id, $isNewMessage) {

        if($isNewMessage == true) {
            $newdata = array(
                'userNewMessage' => 'Y'
            );
        }
        else {
            $newdata = array(
                'userNewMessage' => 'N'
            );
        }
        $this->db->update('tb_user', $newdata, "id = ".$user_id);
    }

    function setNewEvent($isNewEvent) {

        if($isNewEvent == true) {
            $newdata = array(
                'userNewEvent' => STATUS_YES
            );
        }
        else {
            $newdata = array(
                'userNewEvent' => STATUS_NO
            );
        }
        $this->db->update('tb_user', $newdata, "userStatus != ".STATUS_DELETE);
    }

    function  updateUserInfo($user_id, $user_point, $user_status) {
        $data = Array('userStatus' => $user_status);
        if($user_point != null) {
            $user_level = $this->getLevel($user_point);
            $data['userLevel'] = $user_level;
            $data['userPoint'] = $user_point;
        }

        $this->db->update('tb_user', $data, "id = ".$user_id);
    }

    function  updateStatus($user_id, $user_status) {
        $data = Array('userStatus' => $user_status);
        $this->db->update('tb_user', $data, "id = ".$user_id);
    }

    function  getUserRecommenderCount($user_id) {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->where('id != '.ADMIN_ID)
            ->where('userRecommenderID in (select userID from tb_user where id = '.$user_id.')')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  removeUser($user_id) {
        $data = Array('userStatus' => STATUS_DELETE);
        $this->db->update('tb_user', $data, "id = ".$user_id);

        $data = Array('usershoprelationStatus' => STATUS_DELETE);
        $this->db->update('tb_usershoprelation', $data, "usershoprelationUserID = ".$user_id);

        $this->gcm_model->removeGCMByUserID($user_id);

        $this->freetalk_model->removeFreetalkCommentTagByUserID($user_id);
        $this->freetalk_model->removeFreetalkCommentByUserID($user_id);
        $this->freetalk_model->removeFreetalkCommentRelationByUserID($user_id);
        $this->freetalk_model->removeFreetalkByUserID($user_id);
        $this->freetalk_model->removeFreetalkRelationByUserID($user_id);

        $this->shop_model->removeShopAnswerByUserID($user_id);
        $this->shop_model->removeShopByUserID($user_id);

        $this->cast_model->removeCastCommentByUserID($user_id);
        $this->cast_model->removeCastCommentRelationByUserID($user_id);
        $this->cast_model->removeCastRelationByUserID($user_id);

        $this->banner_model->removeBannerCommentByUserID($user_id);
    }

    function  getShopUserList($pageNum) {
        if($pageNum != null) {
            $last_id = ($pageNum - 1)* ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        $this->db->order_by("tb_shop.shopRequestTime", "desc");

        $query =  $this->db->select('*,
                        tb_user.id as id
                        ')
            ->from('tb_user')
            ->join('tb_shop', 'tb_user.id = tb_shop.shopOwnerID and shopStatus != '.STATUS_DELETE.' and tb_shop.shopOwnerID != '.ADMIN_ID)
            ->where('userStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        return $result;
    }

    function  getShopUserCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->where('id IN (select shopOwnerID from tb_shop where shopOwnerID != '.ADMIN_ID.' and shopStatus != '.STATUS_DELETE.')')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getShopUser($user_id) {
        $query =  $this->db->select('*,
                        tb_user.id as id,
                        tb_shop.id as shopConnectionID
                        ')
            ->from('tb_user')
            ->join('tb_shop', 'tb_user.id = tb_shop.shopOwnerID and shopStatus != '.STATUS_DELETE.' and tb_shop.shopOwnerID != '.ADMIN_ID)
            ->where('userStatus != '.STATUS_DELETE)
            ->where('tb_user.id = '.$user_id)
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

    function processConnectShop($shop_id) {
        $data = Array(
            'shopConnection' => 'Y'
        );

        $this->db->update('tb_shop', $data, "id = ".$shop_id);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
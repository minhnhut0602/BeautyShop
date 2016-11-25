
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Gcm_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        $this->load->library('util');
    }

    function  getLocationList() {

        $this->db->order_by('locationName1, locationName2');

        $query = $this->db->select('*')
            ->from('tb_location')
            ->where('locationName3', "")
            ->where('locationName4', "")
            ->where('locationStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function createGCMLog($data) {
        $this->db->insert('tb_gcmlog', $data);
    }


    /**
     * $kind_member:{0:all, 1:shop_manager 2:normal}
    */
    function  getUserList($kind_member, $sex, $location, $level) {

        if($location != null) {
            $newLocation = $this->util->getUserFilteredLocation($location);
            $strLocationWhere = "INSTR(userLocationName,'".$newLocation."') > 0";
            $this->db->where($strLocationWhere);
        }

        if($sex != null) {
            $this->db->where('userSex', $sex);
        }

        if($level != null) {
            $this->db->where('userLevel', $level);
        }

        $query = $this->db->select('*')
            ->from('tb_user')
            ->where('userStatus != '.STATUS_DELETE)
            ->where('id != '.ADMIN_ID)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        if(count($result) > 0 ) {
            $newResult = Array();
            $cnt = count($result);
            for($i = 0; $i < $cnt; $i++) {
                $hasShop = $this->hasShop($result[$i]['id']);
                if ($kind_member == 1 && $hasShop == true) {
                    array_push($newResult, $result[$i]);
                } else if ($kind_member == 2 && $hasShop == false) { // normal
                    array_push($newResult, $result[$i]);
                }
                else {
                    array_push($newResult, $result[$i]);
                }
            }
            $result = $newResult;
        }

        return $result;
    }

    function  hasShop($user_id) {
        $query = $this->db->select('*')
                          ->from('tb_shop')
                          ->where('shopOwnerID', $user_id)
                          ->where('shopStatus != '.STATUS_DELETE)
                          ->get();

        if ($query->num_rows() > 0) {
            $result = true;
        } else {
            $result = false;
        }

        return $result;
    }

    function  updateExitAds($ads_id, $img_id, $url) {

        $data = Array(
            'adsImgID' => $img_id,
            'adsURL' => $url
            );

        if($ads_id == null) {
            $ads_id = ADMIN_EXIT_ADS_ID;
        }

        $this->db->update('tb_ads', $data, "id = ".$ads_id);
    }

    function  getShop($shop_id) {
        $query = $this->db->select('*')
            ->from('tb_shop')
            ->where('id', $shop_id)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function  getEnvelopCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_gcmlog')
            ->where('gcmlogStatus != '.STATUS_DELETE)
            ->where('gcmlogType = 3')          //GCM_LOG_TYPE3
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getEnvelopList($page_num) {
        if($page_num != null) {
            $last_id = ($page_num - 1) *ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        $query =  $this->db->select('tb_gcmlog.*,
                                     tb_user.userID as recvUserID
                                    ')
            ->from('tb_gcmlog')
            ->where('gcmlogStatus != '.STATUS_DELETE)
            ->join('tb_user', 'tb_gcmlog.gcmlogUserID = tb_user.id and userStatus != '.STATUS_DELETE)
            ->where('gcmlogType = 3')          //GCM_LOG_TYPE3
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function getGCM($gcm_id) {
        $query =  $this->db->select('tb_gcmlog.*,
                                     tb_user.userID as recvUserID
                                    ')
            ->from('tb_gcmlog')
            ->join('tb_user', 'tb_gcmlog.gcmlogUserID = tb_user.id and userStatus != '.STATUS_DELETE)
            ->where('gcmlogStatus != '.STATUS_DELETE)
            ->where('tb_gcmlog.id', $gcm_id)
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

    function removeGCM($gcm_id) {
        $data = Array(
            "gcmlogStatus" => STATUS_DELETE
        );

        $this->db->update('tb_gcmlog', $data, "id = ".$gcm_id);
    }

    function removeGCMByUserID($user_id) {
        $data = Array(
            "gcmlogStatus" => STATUS_DELETE
        );

        $this->db->update('tb_gcmlog', $data, "gcmlogUserID = ".$user_id);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
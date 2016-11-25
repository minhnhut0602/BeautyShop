
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Lifeinfo_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();
        $this->load->library("util");
    }

    function  getLifeInfoList($category_id, $pageNum, $search_word) {
        return $this->getLifeInfoListBy($category_id, $pageNum, $search_word);
    }

    private function  getLifeInfoListBy($category_id, $page_num, $search_word) {
        if($page_num != null) {
            $last_id = ($page_num - 1) * ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($search_word != null) {
            $this->db->where("INSTR(lifeTitle, '".$search_word."') > 0");
        }

        if($category_id != null) {
            $this->db->where('lifeCategoryID', $category_id);
        }

        $this->db->order_by('lifeConsequence');

        $query =  $this->db->select('*,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_life.lifeCategoryID and categoryStatus != '.STATUS_DELETE.'
                    ) AS lifeCategoryName')
            ->from('tb_life')
            ->where('lifeStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }


    function getLife($event_id) {

        $query =  $this->db->select('*')
            ->from('tb_life')
            ->where('id', $event_id)
            ->where("lifeStatus != ".STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];

        } else {
            $result = null;
        }

        return $result;
    }


    function  getLifeInfoListCount($category_id, $search_word) {
        if($search_word != null) {
            $this->db->where("INSTR(lifeTitle,'".$search_word."') > 0");
        }

        if($category_id != null)  {
            $this->db->where("lifeCategoryID", $category_id);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_life')
            ->where('lifeStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getCategoryList() {
        $query = $this->db->select('*')
            ->from('tb_category')
            ->where('categoryEvent', STATUS_ON)
            ->where('categoryStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }


    function  removeLifeInfo($freetalk_id) {
        if($freetalk_id == null) {
            return;
        }
        $data = Array(
            "lifeStatus" => STATUS_DELETE
        );
        $this->db->update('tb_life', $data, "id = ".$freetalk_id);

        $data = Array(
            "lifeclickStatus" => STATUS_DELETE
        );
        $this->db->update('tb_lifeclick', $data, "lifeclickLifeID = ".$freetalk_id);
    }

    function  addCategory($categoryName) {
        if($categoryName == null) {
            return;
        }

        $query =  $this->db->select('*')
            ->from('tb_category')
            ->where('categoryName', $categoryName)
            ->where('categoryStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        if($result == null) {
            $data = Array(
                "categoryName" => $categoryName,
                "categoryFreetalk" => STATUS_OFF,
                "categoryShop" => STATUS_OFF,
                "categoryEvent" => STATUS_ON,
                "categoryCast" => STATUS_OFF
            );
            $this->db->insert('tb_category', $data);
        }
        else {
            $result['categoryEvent'] = 1;
            $this->db->update('tb_category', $result, "id = ".$result['id']);
        }
    }

    function  getAds($adsImgID, $adsURL) {
        $query =  $this->db->select('*')
            ->from('tb_ads')
            ->where('adsURL', $adsURL)
            ->where('adsImgID', $adsImgID)
            ->where('adsStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function  createAds($adsImgID, $adsURL) {

        $result = $this->getAds($adsImgID, $adsURL);

        if($result == null) {
            $data = Array(
                "adsURL" => $adsURL,
                "adsImgID" => $adsImgID
            );

            $this->db->insert('tb_ads', $data);
            $result = $this->getAds($adsImgID, $adsURL);
        }
        else {
            $result['adsStatus'] = STATUS_LIFE;
            $this->db->update('tb_ads', $result, "id = ".$result['id']);
        }

        return $result;
    }


    function createLifeInfo($adsID, $adsTitle, $categoryID, $lifeSubject, $lifeExplain) {
        $data = array(
            'lifeAdsID' => $adsID ,
            'lifeTitle' => $adsTitle ,
            'lifeCategoryID' => $categoryID,
            'lifeSubject'=>$lifeSubject,
            'lifeExplain'=>$lifeExplain,
            'lifeConsequence'=>($this->getLifeInfoCnt() + 1),
            'lifeRegTime'=>$this->util->getTime()
        );

        $this->db->insert('tb_life', $data);
    }

    function modifyLifeInfo($lifeinfoID, $adsID, $adsTitle, $categoryID, $lifeSubject, $lifeExplain) {
        $data = array(
            'lifeAdsID' => $adsID ,
            'lifeTitle' => $adsTitle ,
            'lifeCategoryID' => $categoryID,
            'lifeSubject'=>$lifeSubject,
            'lifeExplain'=>$lifeExplain
        );

        $this->db->update('tb_life', $data, "id = ".$lifeinfoID);
    }

    function  getClickInfo($life_id, $start_date, $end_date) {
        if($life_id != null)  {
            $this->db->where("lifeclickLifeID", $life_id);
        }

        if($start_date != null && $end_date != null) {
            $this->db->where("date(lifeclickTime) >= ", $start_date);
            $this->db->where("date(lifeclickTime) <= ", $end_date);
        }

        $this->db->group_by("date(lifeclickTime)");

        $query =  $this->db->select('
                                    date(lifeclickTime) as lifeclickTime,
                                    count(*) as lifeclickCnt')
            ->from('tb_lifeclick')
            ->where('lifeclickStatus != '.STATUS_DELETE)
            ->get();
        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  set_best($life_id) {
        $data = Array("lifeBest"=>"Y");
        $this->db->update('tb_life', $data, "id = ".$life_id);
        $data = Array("lifeBest"=>"N");
        $this->db->update('tb_life', $data, "id != ".$life_id);
    }

    function  getLifeInfoCnt() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_life')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  move_up($life) {
        $end = $this->getLifeInfoCnt();

        $consequence = $life['lifeConsequence'];

        $consequence = $consequence - 1;

        if($consequence < 0) {
            $consequence = $end;
        }

        $data1 = Array('lifeConsequence' => $consequence);
        $data2 = Array('lifeConsequence' => $life['lifeConsequence']);

        $this->db->update('tb_life', $data2, "lifeConsequence = ".$consequence);
        $this->db->update('tb_life', $data1, "id = ".$life['id']);
    }

    function  move_down($life) {

        $end = $this->getLifeInfoCnt();

        $consequence = $life['lifeConsequence'];

        $consequence = $consequence + 1;

        if($consequence > $end) {
            $consequence = 1;
        }

        $data1 = Array('lifeConsequence' => $consequence);
        $data2 = Array('lifeConsequence' => $life['lifeConsequence']);

        $this->db->update('tb_life', $data2, "lifeConsequence = ".$consequence);
        $this->db->update('tb_life', $data1, "id = ".$life['id']);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
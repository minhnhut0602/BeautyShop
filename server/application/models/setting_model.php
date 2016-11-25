
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Setting_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();
        $this->load->library("util");
    }

    function getSetting() {
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

    function setPointArray($json_point_array) {
        $data = Array(
            "settingPointRule" => $json_point_array
        );
        $this->db->update("tb_setting", $data);
    }


    function  getPointArray() {
        $query =  $this->db->select('settingPointRule')
            ->from('tb_setting')
            ->where('settingStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = json_decode($result[0]['settingPointRule']);
        }
        else {
            $result = Array();
        }

        return $result;
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
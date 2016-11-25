
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Image_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        $this->load->library('util');
    }

    function getLocalImageList($page_num) {
        return $this->getImageList($page_num, true);
    }

    function  getImageList($page_num, $is_local) {
        $one_screen_cnt = (ADMIN_IMAGE_COL_CNT*ADMIN_IMAGE_ROW_CNT);
        if($page_num != null) {
            $last_id = ($page_num - 1) *$one_screen_cnt;
            $this->db->limit($one_screen_cnt, $last_id);
        }

        if($is_local == true) {
            $strWhere = "INSTR(imageURL, '".$this->util->getBaseURL()."') > 0";
            $this->db->where($strWhere);
        }

        $this->db->order_by("imageUploadTime", "desc");

        $query =  $this->db->select('*,
                                    (
                                        select userID from tb_user where id=tb_image.imageUploadUserID
                                    ) as imageUploadRealUserID')
            ->from('tb_image')
            ->where('imageStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  getLocalImageCount() {
       return $this->getImageCount(true);
    }

    function  getImageCount($is_local) {

        if($is_local == true) {
            $strWhere = "INSTR(imageURL, '".$this->util->getBaseURL ()."') > 0";
            $this->db->where($strWhere);
        }


        $query =  $this->db->select('count(*) as count')
            ->from('tb_image')
            ->where('imageStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }


    function getImageByID($image_id) {
        $query = $this->db->select('*,
                         (
                            select userID from tb_user where id=tb_image.imageUploadUserID
                         ) as imageUploadRealUserID
                        ')->from('tb_image')->where('tb_image.id', $image_id)->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return$result;
    }


    function getImage($img_url){
        $query = $this->db->select('*,
                         (
                            select userID from tb_user where id=tb_image.imageUploadUserID
                         ) as imageUploadRealUserID
                        ')->from('tb_image')->where('tb_image.imageURL', $img_url)->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return$result;
    }

    function  getLocationTypeID ($location_type) {

        if(strcmp($location_type, "shop")) {
            return IMAGE_LOCATION_SHOP;
        }
        else if(strcmp($location_type, "freetalk")) {
            return IMAGE_LOCATION_FREETALK;
        }
        else if(strcmp($location_type, "cast")) {
            return IMAGE_LOCATION_CAST;
        }
        else if(strcmp($location_type, "event")) {
            return IMAGE_LOCATION_EVENT;
        }
        else if(strcmp($location_type, "user")) {
            return IMAGE_LOCATION_USER;
        }
        else if(strcmp($location_type, "app")) {
            return IMAGE_LOCATION_OTHER;
        }

        return IMAGE_LOCATION_OTHER;
    }

    function  createImage($img_url, $user_id, $location_type) {

        $location_type = $this->getLocationTypeID($location_type);

        $result =  $this->getImage($img_url);
        if($result == null) {
            $data = Array(
                'imageURL' => $img_url,
                'imageUploadUserID'=>$user_id,
                'imageUploadLocation'=>$location_type,
                'imageUploadTime'=>$this->util->getTime()
            );
            $this->db->insert('tb_image', $data);
            $result =  $this->getImage($img_url);
        }
        else {
            $data = Array(
                'imageStatus' => STATUS_LIFE
            );

            $this->db->update('tb_image', $data, "id = ".$result['id']);

            $result['imageStatus'] = STATUS_LIFE;
        }

        return $result;
    }

    function removeImage($image_id) {
        $data = Array(
            "imageStatus" => STATUS_DELETE
        );
        $this->db->update('tb_image', $data, "id = ".$image_id);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */

<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Banner_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();
        $this->load->library("util");
    }

    function getBannerArray($arr_id) {
        $cnt = count($arr_id);
        $arrRetData = Array();
        for($i = 0; $i < $cnt; $i++) {
            $banner_id = $arr_id[$i];
            $arrRetData[$i] = $this->getBanner($banner_id);
        }

        return $arrRetData;
    }

    function getBannerArrayWithType($type) {
        $query = $this->db->select('*,
                                    (
										SELECT
										tb_image.imageURL
										FROM
										tb_image
										WHERE
										tb_banner.bannerBackImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
									) AS bannerBackImgURL')
            ->from('tb_banner')
            ->where('bannerType', $type)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  getBanner($banner_id) {
        $query = $this->db->select('*,
                                    (
										SELECT
										tb_image.imageURL
										FROM
										tb_image
										WHERE
										tb_banner.bannerBackImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
									) AS bannerBackImgURL')
            ->from('tb_banner')
            ->where('tb_banner.id', $banner_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function removeBanner($banner_id) {
        $data = Array(
            "bannerStatus" => STATUS_DELETE
        );
        $this->db->update('tb_banner', $data, "id = ".$banner_id);

        $data = Array(
            "bannercommentStatus" => STATUS_DELETE
        );
        $this->db->update('tb_bannercomment', $data, "bannercommentBannerID = ".$banner_id);

        $data = Array(
            "bannerclickStatus" => STATUS_DELETE
        );
        $this->db->update('tb_bannerclick', $data, "bannerclickBannerID = ".$banner_id);
    }

    function  getClickInfo($life_id, $start_date, $end_date) {
        if($life_id != null)  {
            $this->db->where("bannerclickBannerID", $life_id);
        }

        if($start_date != null && $end_date != null) {
            $this->db->where("date(bannerclickTime) >= ", $start_date);
            $this->db->where("date(bannerclickTime) <= ", $end_date);
        }

        $this->db->group_by("date(bannerclickTime)");

        $query =  $this->db->select('
                                    date(bannerclickTime) as bannerclickTime,
                                    count(*) as bannerclickCnt')
            ->from('tb_bannerclick')
            ->where('bannerclickStatus != '.STATUS_DELETE)
            ->get();
        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function createBanner($adsID, $banner_title, $banner_show_mode, $banner_content, $banner_start_date, $banner_end_date, $banner_back_img_id, $banner_date_show, $banner_title_show, $banner_content_show) {
        $data = array(
            'bannerAdsID' => $adsID);

        if($banner_title != null) {
            $data['bannerTitle'] = $banner_title;
        }
        if($banner_show_mode != null) {
            $data['bannerShowMode'] = $banner_show_mode;
        }
        if($banner_content != null) {
            $data['bannerContent'] = $banner_content;
        }
        if($banner_start_date != null) {
            $data['bannerStartDate'] = $banner_start_date;
        }
        if($banner_end_date != null) {
            $data['bannerEndDate'] = $banner_end_date;
        }
        if($banner_back_img_id != null) {
            $data['bannerBackImgID'] = $banner_back_img_id;
        }

        if($banner_date_show != null) {
            $data['bannerDateShow'] = $banner_date_show;
        }

        if($banner_title_show != null) {
            $data['bannerTitleShow'] = $banner_title_show;
        }

        if($banner_content_show != null) {
            $data['bannerContentShow'] = $banner_content_show;
        }

        $this->db->insert('tb_banner', $data);
    }

    function modifyBanner($id, $adsID, $banner_title, $banner_show_mode, $banner_content, $banner_start_date, $banner_end_date, $banner_back_img_id, $banner_date_show, $banner_title_show, $banner_content_show) {

        $data = array(
            'bannerAdsID' => $adsID);

        if($banner_title != null) {
            $data['bannerTitle'] = $banner_title;
        }
        if($banner_show_mode != null) {
            $data['bannerShowMode'] = $banner_show_mode;
        }
        if($banner_content != null) {
            $data['bannerContent'] = $banner_content;
        }
        if($banner_start_date != null) {
            $data['bannerStartDate'] = $banner_start_date;
        }
        if($banner_end_date != null) {
            $data['bannerEndDate'] = $banner_end_date;
        }

        if($banner_back_img_id != null) {
            $data['bannerBackImgID'] = $banner_back_img_id;
        }

        if($banner_date_show != null) {
            $data['bannerDateShow'] = $banner_date_show;
        }

        if($banner_title_show != null) {
            $data['bannerTitleShow'] = $banner_title_show;
        }

        if($banner_content_show != null) {
            $data['bannerContentShow'] = $banner_content_show;
        }

        $data['bannerStatus'] = 0;

        $this->db->update('tb_banner', $data, "id = ".$id);
    }

    function  getAds($ads_id) {
        $query =  $this->db->select('*,
									(
										SELECT
										tb_image.imageURL
										FROM
										tb_image
										WHERE
										tb_ads.adsImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
									) AS adsImgURL')
            ->from('tb_ads')
            ->where('id', $ads_id)
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

    function getBannerCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type) {
        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "bannercommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and bannercommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "bannercommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("bannercommentPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("bannercommentHeartCnt", "desc");
            }
        }

        $query =  $this->db->select('tb_bannercomment.id,
                    (3) as type,
                    (
                       1
                    ) AS categoryName,
                    tb_bannercomment.bannercommentContent as content,
                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_bannercomment.bannercommentPostManID and userStatus != '.STATUS_DELETE.'
                    ) AS postMan,
                    from_unixtime(tb_bannercomment.bannercommentPostTime/1000) as postTime,
                    tb_bannercomment.bannercommentHeartCnt as heartCnt,
                    (0) AS declareCnt,
                    tb_bannercomment.bannercommentStatus as status
                    ')
            ->from('tb_bannercomment')
            ->where('bannercommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        return $result;
    }

    function getBannerCommentCountWithCond($start_date, $end_date, $post_man) {
        if($start_date != null && $end_date != null) {
            $strWhere = "bannercommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and bannercommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "bannercommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_bannercomment')
            ->where('bannercommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  removeBannerComment($id) {
        $data = Array(
            "bannercommentStatus" => STATUS_DELETE
        );
        $this->db->update('tb_bannercomment', $data, "id = ".$id);
    }

    function  removeBannerCommentByUserID($user_id) {
        $data = Array(
            "bannercommentStatus" => STATUS_DELETE
        );
        $this->db->update('tb_bannercomment', $data, "bannercommentPostManID = ".$user_id);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
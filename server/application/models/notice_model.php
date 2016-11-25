
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Notice_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();
        $this->load->library("util");
    }


    function  getNoticeList($page_num) {
        if($page_num != null) {
            $last_id = ($page_num - 1) *ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        $query =  $this->db->select('*')
            ->from('tb_notice')
            ->where('noticeStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  getNoticeCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_notice')
            ->where('noticeStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getNotice($id) {
        $query =  $this->db->select('*')
            ->from('tb_notice')
            ->where('noticeStatus != '.STATUS_DELETE)
            ->where('id', $id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result['0'];
        } else {
            $result = null;
        }

        return $result;
    }

    function    createNotice($notice_title, $notice_content) {
        $data = Array(
            'noticeTitle' => $notice_title,
            'noticeContent' => $notice_content,
            'noticeTime' => $this->util->getTime()
        );

        $this->db->insert("tb_notice", $data);
    }

    function    modifyNotice($notice_id, $notice_title, $notice_content) {
        $data = Array(
            'noticeTitle' => $notice_title,
            'noticeContent' => $notice_content
        );
        $this->db->update('tb_notice', $data, "id = ".$notice_id);
    }

    function    modifyNoticeClick($notice_id, $notice_click) {
        $data = Array(
            'noticeClickCnt' => $notice_click
        );
        $this->db->update('tb_notice', $data, "id = ".$notice_id);
    }

    function  removeNotice($notice_id) {
        $data = Array(
            'noticeStatus' => STATUS_DELETE
        );
        $this->db->update('tb_notice', $data, "id = ".$notice_id);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
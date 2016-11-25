
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Freetalk_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        $this->load->library("util");
    }

    function getFreetalk($id) {
        $result = $this->getFreetalkListBy(1, 1,null, null, null, null, null, null, null, true, $id);
        if(count($result) == 0) {
            return null;
        }

        return $result[0];
    }

    function  getFreetalkList($pageNum, $category_id, $search_word) {
        return $this->getFreetalkListBy($pageNum, ADMIN_MAX_PAGE_ITEM_CNT, $category_id, $search_word, null, null, null, null, null, true, null);
    }

    function  getFreetalkListWithCond($pageNum, $category_id, $is_best, $start_date, $end_date, $cast_post_man, $order_type){
        return $this->getFreetalkListBy($pageNum, ADMIN_MAX_PAGE_ITEM_CNT, $category_id, null, $is_best, $start_date, $end_date, $cast_post_man, $order_type, true, null);
    }

    private function  getFreetalkListBy($page_num, $cnt, $category_id, $search_word, $is_best, $start_date, $end_date, $cast_post_man, $order_type, $is_contatin_hide, $freetalk_id)
    {
        if ($page_num != null) {
            $last_id = ($page_num - 1) * $cnt;
            $this->db->limit($cnt, $last_id);
        }

        if ($freetalk_id != null) {
            $this->db->where('tb_freetalk.id', $freetalk_id);
        }

        if ($search_word != null) {
            $this->db->where("INSTR(freetalkContent, '" . $search_word . "') > 0");
        }

        if ($category_id != null) {
            $this->db->where('freetalkCategoryID', $category_id);
        }

        if ($is_best == true || $is_best == 'true') {
            $this->db->where('freetalkBest', STATUS_YES);
        } else if ($is_best == false || $is_best == 'false') {
            $this->db->where('freetalkBest', STATUS_NO);
        }

        if ($start_date != null && $end_date != null) {
            $strWhere = "freetalkPostTime >= UNIX_TIMESTAMP('" . $start_date . "')*1000 and freetalkPostTime <= UNIX_TIMESTAMP('" . $end_date . "')*1000";
            $this->db->where($strWhere);
        }

        if ($cast_post_man != null) {
            $strWhere = "freetalkPostManID IN (select id from tb_user where INSTR(userID, '" . $cast_post_man . "') > 0)";
            $this->db->where($strWhere);
        }

        if ($order_type != null) {
            if ($order_type == 0) {
                $this->db->order_by("freetalkPostTime");
            } else if ($order_type == 1) {
                $this->db->order_by("freetalkHeartCnt", "desc");
            }
            else if ($order_type == 2) {
                $this->db->order_by("freetalkCommentCnt", "desc");
            } else if ($order_type == 3) {
                $this->db->order_by("freetalkDeclareCnt", "desc");
            }
        } else {
            $this->db->order_by("freetalkPostTime", "desc");
        }

        if ($is_contatin_hide == false) {
            $this->db->where('freetalkStatus != ' . STATUS_HIDE);
        }

        $query = $this->db->select('tb_freetalk.*,
                    from_unixtime(freetalkPostTime/1000) as freetalkRealPostTime,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_freetalk.freetalkCategoryID and categoryStatus != ' . STATUS_DELETE . '
                    ) AS freetalkCategoryName,
                    (
                        SELECT
                            COUNT(*)
                        FROM
                            tb_freetalkcomment
                        WHERE
                            tb_freetalkcomment.freetalkcommentFreetalkID = tb_freetalk.id and freetalkcommentStatus != ' . STATUS_DELETE . '
                    ) AS freetalkCommentCnt,
                    (
                        SELECT
                            COUNT(*)
                        FROM
                            tb_freetalkrelation
                        WHERE
                            tb_freetalkrelation.freetalkrelationFreetalkID = tb_freetalk.id and freetalkrelationDeclare = 1 and freetalkrelationStatus != ' . STATUS_DELETE . '
                    ) AS freetalkDeclareCnt,
                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_freetalk.freetalkPostManID and userStatus != ' . STATUS_DELETE . '
                    ) AS freetalkPostManUserID
                    ')
            ->from('tb_freetalk')
            ->join('tb_user', 'tb_freetalk.freetalkPostManID = tb_user.id and userStatus != ' . STATUS_DELETE . ' and (userStatus = ' . STATUS_LIFE . ' OR userStatus = ' . STATUS_LIMIT_USER . ')')
            ->where('freetalkStatus != ' . STATUS_DELETE)
            ->get();
        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function getBestFreetalkList($best_cnt, $category_id, $start_date, $end_date, $cast_post_man, $order_type) {
        $result = $this->getFreetalkListBy(1, $best_cnt, $category_id, null, true, $start_date, $end_date, $cast_post_man, $order_type, false);

        $cnt = count($result);

        // get order by heart
        if($cnt < $best_cnt) {
            $other_result = $this->getFreetalkListBy(1, $best_cnt, $category_id, null, false, $start_date, $end_date, $cast_post_man, 1, false);
        }

        for($i = $cnt; $i < $best_cnt; $i++) {
            $result[$i] = $other_result[$i - $cnt];
        }

        return $result;
    }

    function  getFreetalkListCount($category_id, $search_word)  {
        return $this->getFreetalkListCountBy($category_id, $search_word, null, null, null, null);
    }

    function  getFreetalkListCountWithCond($category_id, $is_best, $start_date, $end_date, $cast_post_man) {
        return $this->getFreetalkListCountBy($category_id, null, $is_best, $start_date, $end_date, $cast_post_man);
    }


    function  getFreetalkListCountBy($category_id, $search_word, $is_best, $start_date, $end_date, $cast_post_man) {
        if($search_word != null) {
            $this->db->where("INSTR(freetalkContent,'".$search_word."') > 0");
        }

        if($category_id != null)  {
            $this->db->where("freetalkCategoryID", $category_id);
        }

        if($is_best == true || $is_best == 'true') {
            $this->db->where('freetalkBest', STATUS_YES);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "freetalkPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and freetalkPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($cast_post_man != null) {
            $strWhere = "freetalkPostManID IN (select id from tb_user where INSTR(userID, '".$cast_post_man."') > 0)";
            $this->db->where($strWhere);
        }

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

    function getBestFreetalkCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_freetalk')
            ->where('freetalkBest', STATUS_YES)
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

    function  getCategoryList() {
        $query = $this->db->select('*')
                            ->from('tb_category')
                            ->where('categoryFreetalk', STATUS_ON)
                            ->where('categoryStatus != '.STATUS_DELETE)
                            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getFreetalkCommentListBy($freetalk_id) {

        if($freetalk_id == null) {
            return;
        }

        $query =  $this->db->select('*')
            ->from('tb_freetalkcomment')
            ->where('freetalkcommentStatus != '.STATUS_DELETE)
            ->where('freetalkcommentFreetalkID', $freetalk_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getFreetalkRelationListBy($freetalk_id) {
        if($freetalk_id == null) {
            return;
        }

        $query =  $this->db->select('*')
            ->from('tb_freetalkrelation')
            ->where('freetalkrelationStatus != '.STATUS_DELETE)
            ->where('freetalkrelationFreetalkID', $freetalk_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getFreetalkCommentTagListBy($comment_id) {
        if($comment_id == null) {
            return;
        }

        $query =  $this->db->select('*')
            ->from('tb_freetalkcommenttag')
            ->where('freetalkcommenttagStatus != '.STATUS_DELETE)
            ->where('freetalkcommenttagFreetalkCommentID', $comment_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getFreetalkCommentRelationListBy($comment_id) {
        if($comment_id == null) {
            return;
        }

        $query =  $this->db->select('*')
            ->from('tb_freetalkcommentrelation')
            ->where('freetalkcommentrelationStatus != '.STATUS_DELETE)
            ->where('freetalkcommentrelationFreetalkCommentID', $comment_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  removeFreetalk($freetalk_id) {
        if($freetalk_id == null) {
            return;
        }

        $this->setFreetalkStatus($freetalk_id, STATUS_DELETE);
    }

    function  setFreetalkCommentStatus($comment_id, $status) {
        if($comment_id == null) {
            return;
        }

        $arrFreetalkCommentRelation = $this->getFreetalkCommentRelationListBy($comment_id);

        $relationCnt = count($arrFreetalkCommentRelation);
        for($j = 0; $j < $relationCnt; $j++) {
            $this->setFreetalkCommentRelationStatus($arrFreetalkCommentRelation[$j]['id'], $status);
        }

        $arrFreetalkCommentTag = $this->getFreetalkCommentTagListBy($comment_id);
        $tagCnt = count($arrFreetalkCommentTag);
        for($j = 0; $j < $tagCnt; $j++) {
            $this->setFreetalkCommentTagStatus($arrFreetalkCommentTag[$j]['id'], $status);
        }

        $data = Array(
                "freetalkcommentStatus" => $status
        );
        $this->db->update('tb_freetalkcomment', $data, "id = ".$comment_id);
    }

    function setFreetalkRelationStatus($relation_id, $status) {
        if($relation_id == null) {
            return;
        }

        $data = Array(
            "freetalkrelationStatus" => $status
        );
        $this->db->update('tb_freetalkrelation', $data, "id = ".$relation_id);
    }

    function setFreetalkCommentRelationStatus($relation_id, $status) {
        if($relation_id == null) {
            return;
        }

        $data = Array(
            "freetalkcommentrelationStatus" => $status
        );
        $this->db->update('tb_freetalkcommentrelation', $data, "id = ".$relation_id);
    }

    function setFreetalkCommentTagStatus($relation_id, $status) {
        if($relation_id == null) {
            return;
        }

        $data = Array(
            "freetalkcommenttagStatus" => $status
        );
        $this->db->update('tb_freetalkcommenttag', $data, "id = ".$relation_id);
    }

    function  setFreetalkStatus($freetalk_id, $status) {
        if($freetalk_id == null) {
            return;
        }

        $arrFreetalkComment = $this->getFreetalkCommentListBy($freetalk_id);
        $cnt = count($arrFreetalkComment);
        for($i = 0; $i < $cnt; $i++) {
            $comment = $arrFreetalkComment[$i];
            $this->setFreetalkCommentStatus($comment['id'], $status);
        }

        $arrFreetalkRelation = $this->getFreetalkRelationListBy($freetalk_id);
        $cnt = count($arrFreetalkRelation);
        for($j = 0; $j < $cnt; $j++) {
            $this->setFreetalkRelationStatus($arrFreetalkRelation[$j]['id'], $status);
        }

        $data = Array(
            "freetalkStatus" => $status
        );
        $this->db->update('tb_freetalk', $data, "id = ".$freetalk_id);
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
                "categoryFreetalk" => STATUS_ON,
                "categoryShop" => STATUS_OFF,
                "categoryEvent" => STATUS_OFF,
                "categoryCast" => STATUS_OFF
            );
            $this->db->insert('tb_category', $data);
        }
        else {
            $result['categoryFreetalk'] = STATUS_ON;
            $this->db->update('tb_category', $result, "id = ".$result['id']);
        }
    }


    function createFreetalk($image_array, $content, $category_id, $post_man_id) {
        $data = array(
            'freetalkPostManID' => $post_man_id,
            'freetalkContent' => $content ,
            'freetalkCategoryID' => $category_id ,
            'freetalkPostTime' => $this->util->getMilliTime()   // MySQL datetime format
        );

        if($image_array != null) {
            $data['freetalkImgIDArrayString'] = $image_array;
        }

        $this->db->insert('tb_freetalk', $data);
    }

    function modifyFreetalk($freetalk_id, $image_array, $content, $category_id, $post_man_id) {
        $data = array(
            'freetalkPostManID' => $post_man_id,
            'freetalkContent' => $content ,
            'freetalkCategoryID' => $category_id
        );

        if($image_array != null) {
            $data['freetalkImgIDArrayString'] = $image_array;
        }

        $this->db->update('tb_freetalk', $data, "id = ".$freetalk_id);
    }

    function addLikeCount($freetalk_id, $add_like_count) {

        $strQuery = 'Update tb_freetalk SET freetalkHeartCnt = freetalkHeartCnt + '.$add_like_count.' where id = '.$freetalk_id;
        $this->db->query($strQuery);
    }

    function  setBest($freetalk_id,$is_set_best) {

        $query = $this->db->select('*')
            ->from('tb_freetalk')
            ->where('id', $freetalk_id)
            ->where('freetalkStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }


        if($result == null) {
            return false;
        }

        if($result['freetalkBest'] == 'Y') {
            if($is_set_best == true) {
                return MSG_DUPLICATE_BEST_CAST;
            }
            else {
                $data = Array(
                    'freetalkBest' => 'N'
                );
            }
        }
        else {
            $data = Array(
                'freetalkBest' => 'Y'
            );
        }

        $this->db->update('tb_freetalk', $data, "id = ".$freetalk_id);
        return true;
    }

    function  getFreetalkCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type) {
        return $this->getFreetalkCommentListWithBy($pageNum,$start_date, $end_date, $post_man, $order_type, false);
    }

    function  getFreetalkCommentCountWithCond($start_date, $end_date, $post_man) {
        return $this->getFreetalkCommentCountWithBy($start_date, $end_date, $post_man, false);
    }

    function  getDeclareCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type) {
        return $this->getFreetalkCommentListWithBy($pageNum,$start_date, $end_date, $post_man, $order_type, true);
    }

    function  getFreetalkCommentListWithBy($pageNum, $start_date, $end_date, $post_man, $order_type, $is_declare) {
        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "freetalkcommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and freetalkcommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "freetalkcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("freetalkcommentPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("freetalkcommentHeartCnt", "desc");
            }
        }

        $sub_select = '(1) as type';
        if($is_declare == true) {
            $strWhere = "tb_freetalkcomment.id IN (select freetalkcommentrelationFreetalkCommentID from tb_freetalkcommentrelation where freetalkcommentrelationDeclare = 1 and freetalkcommentrelationStatus != " . STATUS_DELETE.")";
            $this->db->where($strWhere);
            $sub_select = '(4) as type';
        }

        $query =  $this->db->select(
                    $sub_select.',tb_freetalkcomment.id,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_freetalk.freetalkCategoryID and categoryStatus != '.STATUS_DELETE.'
                    ) AS categoryName,
                    tb_freetalkcomment.freetalkcommentContent as content,
                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_freetalkcomment.freetalkcommentPostManID and userStatus != '.STATUS_DELETE.'
                    ) AS postMan,
                    from_unixtime(tb_freetalkcomment.freetalkcommentPostTime/1000) as postTime,
                    tb_freetalkcomment.freetalkcommentHeartCnt as heartCnt,
                    (
                       SELECT
                            count(*)
                        FROM
                            tb_freetalkcommentrelation
                        WHERE
                            tb_freetalkcomment.id = tb_freetalkcommentrelation.freetalkcommentrelationFreetalkCommentID and tb_freetalkcommentrelation.freetalkcommentrelationDeclare = 1 and tb_freetalkcommentrelation.freetalkcommentrelationStatus != '.STATUS_DELETE.'
                    ) AS declareCnt,
                    tb_freetalkcomment.freetalkcommentStatus as status
                    ')
            ->from('tb_freetalkcomment')
            ->where('freetalkcommentStatus != '.STATUS_DELETE)
            ->join('tb_freetalk', 'tb_freetalkcomment.freetalkcommentFreetalkID = tb_freetalk.id and freetalkStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        return $result;
    }

    function  getDeclareCommentCountWithCond($start_date, $end_date, $post_man) {
        return $this->getFreetalkCommentCountWithBy($start_date, $end_date, $post_man, true);
    }

    function  getFreetalkCommentCountWithBy($start_date, $end_date, $post_man, $is_declare) {
        if($start_date != null && $end_date != null) {
            $strWhere = "freetalkcommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and freetalkcommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "freetalkcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($is_declare == true) {
            $strWhere = "tb_freetalkcomment.id IN (select freetalkcommentrelationFreetalkCommentID from tb_freetalkcommentrelation where freetalkcommentrelationDeclare = 1 and freetalkcommentrelationStatus != " . STATUS_DELETE.")";
            $this->db->where($strWhere);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_freetalkcomment')
            ->where('freetalkcommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getAllCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type) {
        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "freetalkcommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and freetalkcommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "freetalkcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("freetalkcommentPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("freetalkcommentHeartCnt", "desc");
            }
        }

        $query1 =  $this->db->select('tb_freetalkcomment.id,
                    (1) as type,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_freetalk.freetalkCategoryID and categoryStatus != '.STATUS_DELETE.'
                    ) AS categoryName,
                    tb_freetalkcomment.freetalkcommentContent as content,
                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_freetalkcomment.freetalkcommentPostManID and userStatus != '.STATUS_DELETE.'
                    ) AS postMan,
                    from_unixtime(tb_freetalkcomment.freetalkcommentPostTime/1000) as postTime,
                    tb_freetalkcomment.freetalkcommentHeartCnt as heartCnt,
                    (
                       SELECT
                            count(*)
                        FROM
                            tb_freetalkcommentrelation
                        WHERE
                            tb_freetalkcomment.id = tb_freetalkcommentrelation.freetalkcommentrelationFreetalkCommentID and tb_freetalkcommentrelation.freetalkcommentrelationDeclare = 1 and tb_freetalkcommentrelation.freetalkcommentrelationStatus != '.STATUS_DELETE.'
                    ) AS declareCnt,
                    tb_freetalkcomment.freetalkcommentStatus as status,
                    (0) as shopcommentShopStatus
                    ')
            ->from('tb_freetalkcomment')
            ->where('freetalkcommentStatus != '.STATUS_DELETE)
            ->join('tb_freetalk', 'tb_freetalkcomment.freetalkcommentFreetalkID = tb_freetalk.id and freetalkStatus != '.STATUS_DELETE)
            ->get();
        $query1 = $this->db->last_query();

        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "castcommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and castcommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "castcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("castcommentPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("castcommentHeartCnt", "desc");
            }
        }

        $query2 =  $this->db->select('tb_castcomment.id,
                    (2) as type,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_cast.castCategoryID and categoryStatus != '.STATUS_DELETE.'
                    ) AS categoryName,
                    tb_castcomment.castcommentContent as content,

                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_castcomment.castcommentPostManID and userStatus != '.STATUS_DELETE.'
                    ) AS postMan,
                    from_unixtime(tb_castcomment.castcommentPostTime/1000) as postTime,
                    tb_castcomment.castcommentHeartCnt as heartCnt,
                    (0) AS declareCnt,
                    tb_castcomment.castcommentStatus as status,
                    (0) as shopcommentShopStatus
                    ')
            ->from('tb_castcomment')
            ->where('castcommentStatus != '.STATUS_DELETE)
            ->join('tb_cast', 'tb_castcomment.castcommentCastID = tb_cast.id and castStatus != '.STATUS_DELETE)
            ->get();
        $query2 = $this->db->last_query();

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

        $query3 =  $this->db->select('tb_bannercomment.id,
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
                    tb_bannercomment.bannercommentPostTime as postTime,
                    (0) as heartCnt,
                    (0) AS declareCnt,
                    tb_bannercomment.bannercommentStatus as status,
                    (0) as shopcommentShopStatus
                    ')
            ->from('tb_bannercomment')
            ->where('bannercommentStatus != '.STATUS_DELETE)
            ->get();
        $query3 = $this->db->last_query();

        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "freetalkcommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and freetalkcommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "freetalkcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("freetalkcommentPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("freetalkcommentHeartCnt", "desc");
            }
        }

        $strWhere = "tb_freetalkcomment.id IN (select freetalkcommentrelationFreetalkCommentID from tb_freetalkcommentrelation where freetalkcommentrelationDeclare = 1 and freetalkcommentrelationStatus != " . STATUS_DELETE.")";
        $this->db->where($strWhere);
        $sub_select = '(4) as type';

        $query4 =  $this->db->select(
            $sub_select.',tb_freetalkcomment.id,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_freetalk.freetalkCategoryID and categoryStatus != '.STATUS_DELETE.'
                    ) AS categoryName,
                    tb_freetalkcomment.freetalkcommentContent as content,
                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_freetalkcomment.freetalkcommentPostManID and userStatus != '.STATUS_DELETE.'
                    ) AS postMan,
                    from_unixtime(tb_freetalkcomment.freetalkcommentPostTime/1000) as postTime,
                    tb_freetalkcomment.freetalkcommentHeartCnt as heartCnt,
                    (
                       SELECT
                            count(*)
                        FROM
                            tb_freetalkcommentrelation
                        WHERE
                            tb_freetalkcomment.id = tb_freetalkcommentrelation.freetalkcommentrelationFreetalkCommentID and tb_freetalkcommentrelation.freetalkcommentrelationDeclare = 1 and tb_freetalkcommentrelation.freetalkcommentrelationStatus != '.STATUS_DELETE.'
                    ) AS declareCnt,
                    tb_freetalkcomment.freetalkcommentStatus as status,
                    (0) as shopcommentShopStatus
                    ')
            ->from('tb_freetalkcomment')
            ->where('freetalkcommentStatus != '.STATUS_DELETE)
            ->join('tb_freetalk', 'tb_freetalkcomment.freetalkcommentFreetalkID = tb_freetalk.id and freetalkStatus != '.STATUS_DELETE)
            ->get();
        $query4 = $this->db->last_query();

        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "postTime >= '".$start_date."' and postTime <= '".$end_date."'";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("postTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("heartCnt", "desc");
            }
        }

        $strQuery = "select * from (".$query1." UNION ".$query2." UNION ".$query3." UNION ".$query4.") as unionTable";

        $query = $this->db->query($strQuery);
        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        return $result;
    }

    function removeFreetalkComment($comment_id) {
        $updateData = Array(
            "freetalkcommentStatus" => STATUS_DELETE
        );
        $this->db->update('tb_freetalkcomment', $updateData, "id = ".$comment_id);

        $updateData = Array(
            "freetalkcommenttagStatus" => STATUS_DELETE
        );
        $this->db->update('tb_freetalkcommenttag', $updateData, "freetalkcommenttagFreetalkCommentID = ".$comment_id);

        $updateData = Array(
            "freetalkcommentrelationStatus" => STATUS_DELETE
        );
        $this->db->update('tb_freetalkcommentrelation', $updateData, "freetalkcommentrelationFreetalkCommentID = ".$comment_id);
    }

    function  removeFreetalkByUserID($user_id) {
        // arr freetalk list
        $query =  $this->db->select('id')
            ->from('tb_freetalk')
            ->where('freetalkStatus != '.STATUS_DELETE)
            ->where('freetalkPostManID', $user_id)
            ->get();

        $arrFreetalk = Array();
        if ($query->num_rows() > 0) {
            $arrFreetalk = $query->result_array();
        }

        if(count($arrFreetalk) == 0) {
            return;
        }

        for($i = 0; $i < count($arrFreetalk); $i++) {
            $freetalk_id = $arrFreetalk[$i]['id'];
            $this->removeFreetalk($freetalk_id);
        }
    }

    function removeFreetalkCommentByUserID($user_id) {
        // arr freetalkcomment list
        $query =  $this->db->select('id')
            ->from('tb_freetalkcomment')
            ->where('freetalkcommentStatus != '.STATUS_DELETE)
            ->where('freetalkcommentPostManID', $user_id)
            ->get();

        $arrFreetalkComment = Array();
        if ($query->num_rows() > 0) {
            $arrFreetalkComment = $query->result_array();
        }

        if(count($arrFreetalkComment) == 0) {
            return;
        }

        for($i = 0; $i < count($arrFreetalkComment); $i++) {
            $comment_id = $arrFreetalkComment[$i]['id'];
            $this->removeFreetalkComment($comment_id);
        }
    }

    function  removeFreetalkRelationByUserID($user_id) {
        $data = Array(
            "freetalkrelationStatus" => STATUS_DELETE
        );
        $this->db->update('tb_freetalkrelation', $data, "freetalkrelationPostManID = ".$user_id);
    }

    function  removeFreetalkCommentRelationByUserID($user_id) {
        $data = Array(
            "freetalkcommentrelationStatus" => STATUS_DELETE
        );
        $this->db->update('tb_freetalkcommentrelation', $data, "freetalkcommentrelationPostManID = ".$user_id);
    }

    function  removeFreetalkCommentTagByUserID($user_id) {
        $data = Array(
            "freetalkcommenttagStatus" => STATUS_DELETE
        );
        $this->db->update('tb_freetalkcommenttag', $data, "freetalkcommenttagTAGUserID = ".$user_id);
    }

    function  updateComment($id, $status) {
        $data = Array(
            "freetalkcommentStatus" =>$status
        );
        $this->db->update('tb_freetalkcomment', $data, "id = ".$id);
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
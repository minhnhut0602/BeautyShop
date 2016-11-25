
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Cast_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();
        $this->load->library("util");
    }

    function getCast($cast_id) {

        $this->db->where('id', $cast_id);

        $query =  $this->db->select('*,
                                    (
										SELECT
										count(*)
										FROM
										tb_castcomment
										WHERE
										castcommentCastID = tb_cast.id and castcommentStatus != '.STATUS_DELETE.'
										And castcommentPostManID IN (select id from tb_user where  tb_user.id and userStatus != '.STATUS_DELETE.')
									) AS castCommentCnt')
            ->from('tb_cast')
            ->where('castStatus != '.STATUS_DELETE)
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

    function  getCastList($page_num) {
        return $this->getCastListBy($page_num, ADMIN_MAX_PAGE_ITEM_CNT, null, null, null, null);
    }

    function  getCastListWithSearchWord($page_num, $page_item_cnt, $category_id, $search_word, $order_type) {
        return $this->getCastListBy($page_num, $page_item_cnt, $category_id, $search_word, $order_type, null);
    }

    /*
     * $order_type : //0:등록순, 1:좋아요순 2:댓글순
     * */
    private function  getCastListBy($page_num, $page_item_cnt, $category_id, $search_word, $order_type, $is_best) {

        if($page_num != null) {
            $last_id = ($page_num-1)*$page_item_cnt;
            $this->db->limit($page_item_cnt, $last_id);
        }

        if($search_word != null) {
            $this->db->where("INSTR(castTitle, '".$search_word."') > 0");
        }

        if($category_id != null) {
            $this->db->where('castCategoryID', $category_id);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("castPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("castHeartCnt", "desc");
            }
            else if($order_type == 2) {
                $this->db->order_by("castCommentCnt", "desc");
            }
        }
        else {
            $this->db->order_by("castConsequence", "desc");
        }

        if($is_best == true || $is_best == 'true') {
            $this->db->where('castBest', STATUS_YES);
        }
        else if($is_best == false || $is_best == 'false') {
            $this->db->where('castBest', STATUS_NO);
        }


        $query =  $this->db->select('*,
                                    (
										SELECT
										count(*)
										FROM
										tb_castcomment
										WHERE
										castcommentCastID = tb_cast.id and castcommentStatus != '.STATUS_DELETE.'
										And castcommentPostManID IN (select id from tb_user where  tb_user.id and userStatus != '.STATUS_DELETE.')
									) AS castCommentCnt')
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

    function getBestCastList($best_cnt, $category_id, $search_word, $order_type) {
        $result = $this->getCastListBy(1, $best_cnt, $category_id, $search_word, $order_type, true);

        $cnt = count($result);

        // get order by heart
        if($cnt < $best_cnt) {
            $other_result = $this->getCastListBy(1, $best_cnt, $category_id, $search_word, 1, false);
        }

        for($i = $cnt; $i < $best_cnt; $i++) {
            $result[$i] = $other_result[$i - $cnt];
        }

        return $result;
    }


    function  getCastCount($category_id, $search_word) {
        if($search_word != null) {
            $this->db->where("INSTR(castTitle, '".$search_word."') > 0");
        }

        if($category_id != null) {
            $this->db->where('castCategoryID', $category_id);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_cast')
            ->where('castStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getCastCommentListByCastID($cast_id) {
        $query =  $this->db->select('*')
            ->from('tb_castcomment')
            ->where('castcommentStatus != '.STATUS_DELETE)
            ->where('castcommentCastID', $cast_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getCastCommentListByUserID($user_id) {
        $query =  $this->db->select('id')
            ->from('tb_castcomment')
            ->where('castcommentStatus != '.STATUS_DELETE)
            ->where('castcommentPostManID', $user_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getCastCommentRelationListByCastCommentID($cast_comment_id) {
        $query =  $this->db->select('*')
            ->from('tb_castcommentrelation')
            ->where('castcommentrelationStatus != '.STATUS_DELETE)
            ->where('castcommentrelationCastCommentID', $cast_comment_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getCastDetailListByCastID($cast_id) {
        $this->db->order_by("castdetailIdx");

        $query =  $this->db->select('*,
									(
										SELECT
										tb_image.imageURL
										FROM
										tb_image
										WHERE
										tb_castdetail.castdetailImgId = tb_image.id  and imageStatus != '.STATUS_DELETE.'
									) AS castdetailImgURL,
									')
            ->from('tb_castdetail')
            ->where('castdetailCastID', $cast_id)
            ->where('castdetailStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function getCastDetail($cast_detail_id) {
        $query =  $this->db->select('*')
            ->from('tb_castdetail')
            ->where('castdetailStatus != '.STATUS_DELETE)
            ->where('id', $cast_detail_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function  removeCast($id) {
        if($id == null) {
            return;
        }

        $arrCastComment = $this->getCastCommentListByCastID($id);
        $cnt = count($arrCastComment);
        for($i = 0; $i < $cnt; $i++) {
            $this->removeCastComment($arrCastComment[$i]['id']);
        }

        $arrCastDetail = $this->getCastDetailListByCastID($id);
        $cnt = count($arrCastDetail);
        for($i = 0; $i < $cnt; $i++) {
            $this->removeCastDetail($arrCastDetail[$i]['id']);
        }

        $data = Array(
            "castrelationStatus" => STATUS_DELETE
        );

        $this->db->update('tb_castrelation', $data, "castrelationCastID = ".$id);

        $data = Array(
            "castStatus" => STATUS_DELETE
        );

        $this->db->update('tb_cast', $data, "id = ".$id);
    }

    function  removeCastRelationByUserID($user_id) {
        $data = Array(
            "castrelationStatus" => STATUS_DELETE
        );

        $this->db->update('tb_castrelation', $data, "castrelationPostManID = ".$user_id);
    }

    function  removeCastComment($id) {
        $arrCast = $this->getCastCommentRelationListByCastCommentID($id);
        $cnt = count($arrCast);
        for($i = 0; $i < $cnt; $i++) {
            $this->removeCastCommentRelation($arrCast[$i]['id']);
        }
        $data = Array(
            "castcommentStatus" => STATUS_DELETE
        );
        $this->db->update('tb_castcomment', $data, "id = ".$id);
    }

    function removeCastCommentByUserID($user_id) {
        $arrCastComment = $this->getCastCommentListByUserID($user_id);
        $cnt = count($arrCastComment);
        for($i = 0; $i < $cnt; $i++) {
            $this->removeCastComment($arrCastComment[$i]['id']);
        }
    }

    function  removeCastCommentRelation($id) {
        $data = Array(
          "castcommentrelationStatus" => STATUS_DELETE
        );
        $this->db->update('tb_castcommentrelation', $data, "id = ".$id);
    }

    function removeCastCommentRelationByUserID($user_id) {
        $data = Array(
            "castcommentrelationStatus" => STATUS_DELETE
        );
        $this->db->update('tb_castcommentrelation', $data, "castcommentrelationPostManID = ".$user_id);
    }

    function  removeCastDetail($id) {
        $data = Array(
            "castdetailStatus" => STATUS_DELETE
        );
        $this->db->update('tb_castdetail', $data, "id = ".$id);
    }

    function  createCast($title, $cast_category_id,  $cover_img_id) {
        $time =  $this->util->getTime();
        $data = Array(
            'castTitle' => $title,
            'castPostManID' => 0,
            'castCoveredImgID' => $cover_img_id,
            'castCategoryID' => $cast_category_id,
            'castPostTime' => $time,
            'castConsequence'=>($this->getCastCnt()+1)
        );

        $this->db->insert('tb_cast', $data);

        $query = $this->db->select('*')->from('tb_cast')
            ->where('castPostTime', $time)
            ->where('castTitle', $title)
            ->where('castStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function modifyCast($cast_id, $cast_category_id, $cast_title, $cover_img_id) {
        $updateData = Array(
            'castTitle' => $cast_title,
            'castCoveredImgID' => $cover_img_id,
            'castCategoryID' => $cast_category_id
        );

        $this->db->update('tb_cast', $updateData, "id = ".$cast_id);
    }

    function  createCastDetail($publish, $content, $img_id, $cast_id, $detail_idx) {
        $data = Array(
            'castdetailContent' => $content,
            'castdetailImgID' => $img_id,
            'castdetailCastID' => $cast_id,
            'castdetailIdx' => $detail_idx,
            'castdetailPublish' => $publish
        );

        $this->db->insert('tb_castdetail', $data);

        $query = $this->db->select('*')->from('tb_castdetail')
            ->where('castdetailContent', $content)
            ->where('castdetailImgID', $img_id)
            ->where('castdetailCastID', $cast_id)
            ->where('castdetailIdx', $detail_idx)
            ->where('castdetailStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function modifyCastDetail($id, $publish, $content, $img_id, $cast_id, $detail_idx) {
        $data = Array(
            'castdetailContent' => $content,
            'castdetailImgID' => $img_id,
            'castdetailCastID' => $cast_id,
            'castdetailIdx' => $detail_idx,
            'castdetailPublish' => $publish
        );

        $this->db->update('tb_castdetail', $data, "id = ".$id);
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
                "categoryEvent" => STATUS_OFF,
                "categoryCast" => STATUS_ON
            );
            $this->db->insert('tb_category', $data);
        }
        else {
            $result['categoryCast'] = STATUS_ON;
            $this->db->update('tb_category', $result, "id = ".$result['id']);
        }
    }

    function  getCategoryList() {
        $query = $this->db->select('*')
            ->from('tb_category')
            ->where('categoryCast', STATUS_ON)
            ->where('categoryStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function addLikeCount($cast_id, $add_like_count) {

        $strQuery = 'Update tb_cast SET castHeartCnt = castHeartCnt + '.$add_like_count.' where id = '.$cast_id;
        $this->db->query($strQuery);
    }

    function  setBest($cast_id, $is_set_best) {

        $query = $this->db->select('*')
            ->from('tb_cast')
            ->where('id', $cast_id)
            ->where('castStatus != '.STATUS_DELETE)
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

        if($result['castBest'] == 'Y') {
            if($is_set_best == true) {
                return MSG_DUPLICATE_BEST_CAST;
            }
            else {
                $data = Array(
                    'castBest' => 'N'
                );
            }
        }
        else {
            $data = Array(
                'castBest' => 'Y'
            );
        }

        $this->db->update('tb_cast', $data, "id = ".$cast_id);
        return true;
    }

    function getBestCastCount() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_cast')
            ->where('castBest', STATUS_YES)
            ->where('castStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function getCastCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type) {
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

        $query =  $this->db->select('tb_castcomment.id,
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
                    tb_castcomment.castcommentStatus as status
                    ')
            ->from('tb_castcomment')
            ->where('castcommentStatus != '.STATUS_DELETE)
            ->join('tb_cast', 'tb_castcomment.castcommentCastID = tb_cast.id and castStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        return $result;
    }

    function getCastCommentCountWithCond($start_date, $end_date, $post_man) {
        if($start_date != null && $end_date != null) {
            $strWhere = "castcommentPostTime >= UNIX_TIMESTAMP('".$start_date."')*1000 and castcommentPostTime <= UNIX_TIMESTAMP('".$end_date."')*1000";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "castcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_castcomment')
            ->where('castcommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getCastConsequenceArray() {
 		$this->db->order_by('castConsequence');    	
        $query =  $this->db->select('*')
            ->from('tb_cast')
            ->where('castStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
			$result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  getCastCnt() {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_cast')
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  move_down($life,$is_most) {
        $end = $this->getCastCnt();
                
		/*for($i = 1; $i <= $end; $i++) {
	 	    $j = $i+11;
	 	    $data2 = Array('castConsequence' => $i);			
			$this->db->update('tb_cast', $data2, "id = ".$j);
		}*/
		if($is_most == 1) {
			$data1 = Array('castConsequence' => 1);
			$e = $life['castConsequence'];
	 	    for($i = $e-1; $i >=1; $i--) {
	 	    	$j = $i+1;
	 	    	$data2 = Array('castConsequence' => $j);
	 	    	$this->db->update('tb_cast', $data2, "castConsequence = ".$i);
	        }
	        $this->db->update('tb_cast', $data1, "id = ".$life['id']);	        
		} else {
			$castConsequenceArray = $this->getCastConsequenceArray();
	        $consequence = $life['castConsequence'];
			$cnt = count($castConsequenceArray);
			for($i=0;$i<$cnt;$i++) 
			{
				if($consequence == $castConsequenceArray[$i]['castConsequence']) {
					if($i == 0) $consequence = $castConsequenceArray[$cnt-1]['castConsequence'];
					else $consequence = $castConsequenceArray[$i-1]['castConsequence'];
					break;
				}
			}
			
	        //$consequence = $consequence - 1;

	        //if($consequence < 0) {
	        //    $consequence = $end;
	        //}
	        $data1 = Array('castConsequence' => $consequence);
	        $data2 = Array('castConsequence' => $life['castConsequence']);

	        $this->db->update('tb_cast', $data2, "castConsequence = ".$consequence);
	        $this->db->update('tb_cast', $data1, "id = ".$life['id']);	        
		}
    }

    function  move_up($life,$is_most) {

        $end = $this->getCastCnt();        
		if($is_most == 1) {
			$data1 = Array('castConsequence' => $end);
			$e = $life['castConsequence'];
	 	    for($i = $e+1; $i <= $end; $i++) {
	 	    	$j = $i-1;
	 	    	$data2 = Array('castConsequence' => $j);
	 	    	$this->db->update('tb_cast', $data2, "castConsequence = ".$i);
	        }
	        $this->db->update('tb_cast', $data1, "id = ".$life['id']);
		} else {
			$castConsequenceArray = $this->getCastConsequenceArray();
	        $consequence = $life['castConsequence'];
			$cnt = count($castConsequenceArray);
			for($i=0;$i<$cnt;$i++) 
			{
				if($consequence == $castConsequenceArray[$i]['castConsequence']) {
					if($i == $cnt-1) $consequence = $castConsequenceArray[0]['castConsequence'];
					else $consequence = $castConsequenceArray[$i+1]['castConsequence'];
					break;
				}
			}

			
/*	        $consequence = $life['castConsequence'];

	        $consequence = $consequence + 1;

	        if($consequence > $end) {
	            $consequence = 1;
	        }*/
	        $data1 = Array('castConsequence' => $consequence);
	        $data2 = Array('castConsequence' => $life['castConsequence']);

	        $this->db->update('tb_cast', $data2, "castConsequence = ".$consequence);
	        $this->db->update('tb_cast', $data1, "id = ".$life['id']);	        
		}
    }
}

/* End of file admin_model.php */
/* Location: ./application/models/admin_model.php */
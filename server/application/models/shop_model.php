
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Shop_model extends CI_Model
{

    function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        $this->load->library("util");
    }

    function  getShopCount($shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition, $shop_clickcnt_condition,
                           $shop_status, $shop_address, $shop_visibility, $shop_category_id) {
        if($shop_name != null) {
            $strWhere = "INSTR(tb_shop.shopName,'".$shop_name."') > 0";
            $this->db->where($strWhere);
        }

        if($shop_address != null) {
            $address = $this->util->getShopFilteredLocation($shop_address);
            $strWhere = "INSTR(tb_shop.shopAddress,'".$address."') > 0";
            $this->db->where($strWhere);
        }

        if($shop_phonenumber != null) {
            $strWhere = "INSTR(tb_shop.shopPhonenumber,'".$shop_phonenumber."') > 0";
            $this->db->where($strWhere);
        }

        if($shop_eventable != null) {
            $this->db->where('tb_shop.shopEventable', $shop_eventable);
            $this->db->join('tb_shop', "tb_event.eventShopID = tb_shop.id");
        }

        if($shop_visibility != null) {
            $this->db->where('tb_shop.shopVisibility', $shop_visibility);
        }

        if($shop_category_id != null) {
            $this->db->where('tb_shop.shopCategoryID', $shop_category_id);
        }

        if($shop_callcnt_condition != null) {
            if($shop_callcnt_condition == 0) {
                $this->db->order_by('tb_shop.shopCallCnt', 'desc');
            }
            else {
                $this->db->order_by('tb_shop.shopCallCnt');
            }
        }

        if($shop_clickcnt_condition != null) {
            if($shop_clickcnt_condition == 0) {
                $this->db->order_by('tb_shop.shopClickCnt', 'desc');
            }
            else {
                $this->db->order_by('tb_shop.shopClickCnt');
            }
        }

        if($shop_status != null) {
            $this->db->where('tb_shop.shopStatus', $shop_status);
        }

		if($shop_eventable != null) {	
	        $query =  $this->db->select('count(*) as count')
	            ->from('tb_event')
	            ->where('tb_shop.shopStatus != '.STATUS_DELETE)
	            ->get();
		} else {
	        $query =  $this->db->select('count(*) as count')
	            ->from('tb_shop')
	            ->where('tb_shop.shopStatus != '.STATUS_DELETE)
	            ->get();			
		}
        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getShop($shop_id) {
        $result = $this->getShopListBy(null, null,null, null, null, null,null, null,null, null,$shop_id);
        if(count($result) == 0) {
            return null;
        }

        return $result[0];
    }

    function  getShopList($page_num, $shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition, $shop_clickcnt_condition,
                            $shop_status, $shop_address, $shop_visibility, $shop_category_id) {
        return $this->getShopListBy($page_num, $shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition, $shop_clickcnt_condition,
            $shop_status, $shop_address, $shop_visibility, $shop_category_id, null);
    }

    /**
     * @param $page_num
     * @param $shop_name
     * @param $shop_phonenumber
     * @param $shop_eventable
     * @param $shop_callcnt_condition
     * @param $shop_clickcnt_condition
     * @param $shop_status
     * @param $shop_address
     * @param $shop_visibility
     * @param $shop_category_id
     * @param $shop_id
     * @return array
     */
    function  getShopListBy($page_num, $shop_name, $shop_phonenumber, $shop_eventable, $shop_callcnt_condition, $shop_clickcnt_condition,
                          $shop_status, $shop_address, $shop_visibility, $shop_category_id, $shop_id) {
        if($page_num != null && $page_num >= 1) {
            $last_id = ($page_num - 1) *ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($shop_id != null) {
            $this->db->where('tb_shop.id', $shop_id);
        }

        if($shop_name != null) {
            $strWhere = "INSTR(tb_shop.shopName,'".$shop_name."') > 0";
            $this->db->where($strWhere);
        }

        if($shop_phonenumber != null) {
            $strWhere = "INSTR(tb_shop.shopPhonenumber,'".$shop_phonenumber."') > 0";
            $this->db->where($strWhere);
        }

        if($shop_address != null) {
            $address = $this->util->getShopFilteredLocation($shop_address);
            $strWhere = "INSTR(tb_shop.shopAddress,'".$address."') > 0";
            $this->db->where($strWhere);
        }

        if($shop_visibility != null) {
            $this->db->where('tb_shop.shopVisibility', $shop_visibility);
        }

        if($shop_category_id != null) {
            $this->db->where('tb_shop.shopCategoryID', $shop_category_id);
        }

        if($shop_eventable != null) {
            $this->db->where('tb_shop.shopEventable', $shop_eventable);
            $this->db->join('tb_shop', "tb_event.eventShopID = tb_shop.id");
        }		

        if($shop_callcnt_condition != null) {
            if($shop_callcnt_condition == 0) {
                $this->db->order_by('tb_shop.shopCallCnt', 'desc');
            }
            else {
                $this->db->order_by('tb_shop.shopCallCnt');
            }
        }

        if($shop_clickcnt_condition != null) {
            if($shop_clickcnt_condition == 0) {
                $this->db->order_by('tb_shop.shopClickCnt', 'desc');
            }
            else {
                $this->db->order_by('tb_shop.shopClickCnt');
            }
        }

        if($shop_status != null) {
            $this->db->where('tb_shop.shopStatus', $shop_status);
        }

		if($shop_eventable != null) {
	        $query =  $this->db->select('tb_shop.*,
	                        (
								SELECT
									tb_category.categoryName
								FROM
									tb_category
								WHERE
									tb_shop.shopCategoryID = tb_category.id
							) AS shopCategoryName,
							(
								SELECT
									tb_image.imageURL
								FROM
									tb_image
								WHERE
									tb_shop.shopManagerIdentyImgID = tb_image.id
							) AS shopIdentyImgURL')
	            ->from('tb_event')
	            ->where('tb_shop.shopStatus != '.STATUS_DELETE)
	            ->get();
		} else {
	        $query =  $this->db->select('tb_shop.*,
	                        (
								SELECT
									tb_category.categoryName
								FROM
									tb_category
								WHERE
									tb_shop.shopCategoryID = tb_category.id
							) AS shopCategoryName,
							(
								SELECT
									tb_image.imageURL
								FROM
									tb_image
								WHERE
									tb_shop.shopManagerIdentyImgID = tb_image.id
							) AS shopIdentyImgURL')
	            ->from('tb_shop')
	            ->where('tb_shop.shopStatus != '.STATUS_DELETE)
	            ->get();			
		}
		
        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }
        return $result;
    }

    function  getShopQuestionCount($shop_id) {
        $query =  $this->db->select('count(*) as count')
            ->from('tb_shopcomment')
            ->where('shopcommentType', 'QA')
            ->where('shopcommentShopID', $shop_id)
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

    function  updateShopImage($shop_id, $arr_img_id) {
        $data = Array(
            'shopImgIDArrayString' => json_encode($arr_img_id)
        );

        $this->db->update('tb_shop', $data, "id = ".$shop_id);
    }

    function  updateShopLevel($shop_id) {
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

    function createShop($shop_name, $category_id, $status, $eventable, $live_date) {
        $time =  $this->util->getTime();
        $data = Array(
            'shopName' => $shop_name,
            'shopCategoryID' => $category_id,
            'shopStatus' => $status,
            'shopEventable' => $eventable,
            'shopLiveDate' => $live_date,
            'shopRequestTime' => $time
        );

        $this->db->insert('tb_shop', $data);

        $query = $this->db->select('*')->from('tb_shop')
            ->where('shopRequestTime', $time)
            ->where('shopName', $shop_name)
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

    function getShopByNameAndAddress($shop_name, $shop_address) {
        $query = $this->db->select('*')->from('tb_shop')
            ->where('shopName', $shop_name)
            ->where('shopAddress', $shop_address)
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

    function updateShopInfo($shop_id, $category_id, $status, $questionable, $eventable,
                            $live_date, $shop_address, $shop_phonenumber, $shop_img_image_array, $price_image_array,
                            $shop_manageridenty_imgid, $shop_visibility, $shop_name, 
                            $shop_road,$shop_opentime,$shop_resttime,$shop_parkable,$shop_description) {
        $data = Array();
        if($category_id != null) {
            $data['shopCategoryID'] = $category_id;
        }
        if($status != null) {
            $data['shopStatus'] = $status;
        }
        if($eventable != null) {
            $data['shopEventable'] = $eventable;
        }
        if($live_date != null) {
            $data['shopLiveDate'] = $live_date;
        }
        if($shop_address != null) {
            $data['shopAddress'] = $shop_address;
            $latlng = $this->util->get_lat_long($shop_address);
            $latlng = explode(',', $latlng);
            $data['shopLatitude'] = $latlng[0];
            $data['shopLongtitude'] = $latlng[1];
        }

        if($shop_name != null && $shop_address != null) {
            $shop = $this->getShopByNameAndAddress($shop_name, $shop_address);
            if($shop != null && $shop['id'] != $shop_id) {
                return MSG_REGISTTERED_SHOP;

            }
        }

        if($shop_name != null) {
            $data['shopName'] = $shop_name;
        }

        if($questionable != null) {
            $data['shopQuestionable'] = $questionable;
        }
        if($shop_phonenumber != null) {
            $data['shopPhonenumber'] = $shop_phonenumber;
        }

        if($shop_visibility != null) {
            $data['shopVisibility'] = $shop_visibility;
        }

        if($shop_img_image_array != null) {
            $data['shopImgIDArrayString'] = $shop_img_image_array;
        }
        if($price_image_array != null) {
            $data['shopPriceImgIDArrayString'] = $price_image_array;
        }

        if($shop_manageridenty_imgid != null) {
            $data['shopManagerIdentyImgID'] = $shop_manageridenty_imgid;
        }

        if($shop_road != null) {
            $data['shopRoad'] = $shop_road;
        }

		if($shop_opentime != null) {
            $data['shopOpenTimeString'] = $shop_opentime;
        }
        
		if($shop_resttime != null) {
            $data['shopRestTimeString'] = $shop_resttime;
        }

		if($shop_parkable != null) {
            $data['shopParkable'] = $shop_parkable;
        }

		if($shop_description != null) {
            $data['shopDescriptionString'] = $shop_description;
        }
	
        $this->db->update('tb_shop', $data, "id = ".$shop_id);

        return MSG_SUCCESS;
    }

    function  setRemark($shop_id, $remark){
        $data = Array (
            "shopcommentPostManID" => ADMIN_ID,
            "shopcommentShopID" => $shop_id,
            "shopcommentContent" => '',
            "shopcommentType" => TYPE_SHOPCOMMENT_USERREMARK,
            "shopcommentPostTime" => $this->util->getTime()
        );

        $query = $this->db->select('*')->from('tb_shopcomment')
                    ->where('shopcommentType', TYPE_SHOPCOMMENT_USERREMARK)
                    ->where('shopcommentShopID', $shop_id)
                    ->where('shopcommentStatus != '.STATUS_DELETE)
                    ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        $cnt =  count($result);

        $isExistAdmin = false;

        for($i = 0; $i < $cnt; $i++) {
            $comment = $result[$i];
            if($comment['shopcommentPostManID'] == ADMIN_ID) {
                $isExistAdmin = true;
            }

            $updateData = Array(
                'shopcommentShopLevel' => $remark
            );

            $this->db->update('tb_shopcomment', $updateData, "id = ".$comment['id']);
        }

        if($isExistAdmin == false) {
            $this->db->insert('tb_shopcomment', $data);
        }

        return $result;
    }


    function getShopConArray($address) {

        if($address != null) {
            $strWhere = "INSTR(shopAddress,'".$address."') > 0";
            $this->db->where($strWhere);
        }

        $this->db->order_by("shopConsequence");

        $query = $this->db->select('*')
            ->from('tb_shop')
            ->where('shopStatus != '.STATUS_DELETE)
            ->where('shopConsequence !='.LIMIT_CONSEQUE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  updateConsequence($shop_id, $consequence) {
        $updateData = Array(
            'shopConsequence' => $consequence
        );
        $this->db->update('tb_shop', $updateData, "id = ".$shop_id);
    }


    // shop comment

    function  getShopCommentListWithShop($shop_id, $type, $cnt, $page_num) {
        return $this->getShopCommentListBy($shop_id, $type, $cnt, $page_num);
    }

    function  getShopCommentList1($type, $cnt, $page_num) {
        return $this->getShopCommentListBy(null, $type, $cnt, $page_num);
    }

    function  getShopCommentListBy($shop_id, $type, $cnt, $page_num) {

        if($page_num != null && $page_num >= 1) {
            $star_idx = ($page_num-1)*$cnt;
            $this->db->limit($cnt, $star_idx);
        }
        else if($cnt != null) {
            $this->db->limit($cnt);
        }

        if($type != null) {
            $this->db->where('shopcommentType', $type);
        }

        if($shop_id != null) {
            $this->db->where('shopcommentShopID', $shop_id);
        }

        $this->db->order_by('shopcommentPostTime', 'desc');
        $query =  $this->db->select('*,
                         (
							SELECT
								tb_user.userID
							FROM
								tb_user
							WHERE
								tb_shopcomment.shopcommentPostManID = tb_user.id
						) AS userID,
                        (
							SELECT
								tb_shop.shopName
							FROM
								tb_shop
							WHERE
								tb_shopcomment.shopcommentShopID = tb_shop.id
						) AS shopName')
            ->from('tb_shopcomment')
            ->where('shopcommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  getShopCommentCount1($type) {
        return $this->getShopCommentCountBy1(null, $type);
    }

    function  getShopCommentCountWithShop($shop_id, $type) {
        return $this->getShopCommentCountBy1($shop_id, $type);
    }

    function  getShopCommentCountBy1($shop_id, $type) {
        if($type != null) {
            $this->db->where('shopcommentType', $type);
        }

        if($shop_id != null) {
            $this->db->where('shopcommentShopID', $shop_id);
        }

        $query =  $this->db->select('count(*) as count')
            ->from('tb_shopcomment')
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

    function  getShopCommentCount($shop_id, $type) {
        if($shop_id == null) {
            $function = "getShopCommentCnt('".$type."', null)";
        }
        else {
            $function = "getShopCommentCnt('".$type."',".$shop_id.")";
        }

        $query =  $this->db->query('select '.$function.' as count');

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getShopCommentList($shop_id, $type, $cnt, $page_num = null) {

        if($page_num != null) {
            $sql = "CALL getShopCommentList(?,?,?,?)";
            $parameters = array($type, $page_num, $cnt, $shop_id);
        }
        else {
            $sql = 'select * from tb_shopcomment
                              where shopcommentStatus != '.STATUS_DELETE
                                .' and shopcommentPostManID != '.ADMIN_ID
                                .' and shopcommentShopID = '.$shop_id;

            if($type != null) {
                $sql = $sql." and shopcommentType = '".$type."'";
            }

            if($cnt != null) {
                $this->db->limit($cnt);
            }
        }

        $query =  $this->db->query($sql, $parameters);

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        $query->next_result();
        $query->free_result();

        return $result;
    }

    function  updateShopComment($id, $status) {
        $data = Array(
            "shopcommentStatus" =>$status
        );
        $this->db->update('tb_shopcomment', $data, "id = ".$id);

        $data = Array(
            "shopanswerStatus" =>$status
        );
        $this->db->update('tb_shopanswer', $data, "shopanswerShopCommentID = ".$id);
    }

    function updateShopAnswerWithShopCommentID($id, $status) {
        $data = Array(
            "shopanswerStatus" =>$status
        );
        $this->db->update('tb_shopanswer', $data, "shopanswerShopCommentID = ".$id);
    }

    function removeShopAnswer($answerid) {
        $data = Array(
            "shopanswerStatus" => STATUS_DELETE
        );
        $this->db->update('tb_shopanswer', $data, "id = ".$answerid);
    }

    function  getProductList($shop_id, $cnt, $page_num) {

        if($page_num != null && $page_num >= 1) {
            $star_idx = ($page_num-1)*$cnt;
            $this->db->limit($cnt, $star_idx);
        }
        else if($cnt != null) {
            $this->db->limit($cnt);
        }

        $query =  $this->db->select('*')
                            ->from('tb_product')
                            ->where('productShopID', $shop_id)
                            ->where('productStatus != '.STATUS_DELETE)
                            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  getProductCount($shop_id) {
        $query =  $this->db->select('count(*) as count')
                            ->from('tb_product')
                            ->where('productShopID', $shop_id)
                            ->where('productStatus != '.STATUS_DELETE)
                            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;

        } else {
            $result = 0;
        }

        return $result;
    }

    function  getProduct($id) {
        $query =  $this->db->select('*')
            ->from('tb_product')
            ->where('id', $id)
            ->where('productStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }

    function  getRequestShopList($pageNum, $shopName, $shopIdenti) {

        if($pageNum != null && $pageNum >= 1) {
            $last_idx = ($pageNum - 1) * ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_idx);
        }

        if($shopName != null) {
            $strWhere = "INSTR(shopName,'".$shopName."') > 0";
            $this->db->where($strWhere);
        }

        if($shopIdenti != null) {
            if($shopIdenti == 0) {
                $this->db->where('shopManagerIdentyImgID IS NULL');
            }
            else {
                $this->db->where('shopManagerIdentyImgID IS NOT NULL');
            }
        }

        $query =  $this->db->select('*')
            ->from('tb_shop')
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function  getRequestShopCount($shopName, $shopIdenti) {

        if($shopName != null) {
            $strWhere = "INSTR(shopName,'".$shopName."') > 0";
            $this->db->where($strWhere);
        }

        if($shopIdenti != null) {
            if($shopIdenti == 0) {
                $this->db->where('shopManagerIdentyImgID IS NULL');
            }
            else {
                $this->db->where('shopManagerIdentyImgID IS NOT NULL');
            }
        }

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

    function removeShopEvent($event_id){
        $updateDate = Array(
            "eventStatus"=>STATUS_DELETE
        );
        $this->db->update('tb_event', $updateDate, "id = ".$event_id);
    }

    function removeShopByUserID($user_id) {
        $query =  $this->db->select('id')
            ->from('tb_shop')
            ->where('shopStatus != '.STATUS_DELETE)
            ->wherE('shopOwnerID', $user_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        $cnt = count($result);

        for($i = 0; $i < $cnt; $i++) {
            $this->removeShop($result[$i]['id'], STATUS_DELETE);
        }

        return $result;
    }

    function  removeShop($shop_id) {
        $arrShopComment = $this->getShopCommentList($shop_id, null, null, null);
        for($i = 0; $i < count($arrShopComment); $i++) {
            $this->updateShopComment($arrShopComment[$i]['id'], STATUS_DELETE);
        }

        $data = Array(
            "eventStatus" => STATUS_DELETE
        );

        $this->db->update('tb_event', $data, "eventShopID = ".$shop_id);

        $data = Array(
            "productStatus" => STATUS_DELETE
        );

        $this->db->update('tb_product', $data, "productShopID = ".$shop_id);

        $data = Array(
            "shopStatus" => STATUS_DELETE
        );
        $this->db->update('tb_shop', $data, "id = ".$shop_id);
    }

    function  updateShopPassword($shop_id, $password) {
        $data = Array(
            "shopPW" => $password
        );
        $this->db->update('tb_shop', $data, "id = ".$shop_id);
    }

    function getEvent($event_id) {

        $query =  $this->db->select('*,
                                (
                                        SELECT
                                        tb_image.imageURL
                                        FROM
                                        tb_image
                                        WHERE
                                        tb_event.eventImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
                                ) AS eventImgURL')
            ->from('tb_event')
            ->where('id', $event_id)
            ->where('eventStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];

        } else {
            $result = null;
        }

        return $result;
    }

    function  getShopUserRemarkList($pageNum, $shop_name, $post_man, $order_type) {
        return $this->getShopCommentListWithBy($pageNum, TYPE_SHOPCOMMENT_USERREMARK, $shop_name, null, null, $post_man, $order_type);
    }

    function  getShopCommentListWithCond($pageNum, $start_date, $end_date, $post_man, $order_type) {
        return $this->getShopCommentListWithBy($pageNum, null, null, $start_date, $end_date, $post_man, $order_type);
    }

    function  getShopCommentListWithBy($pageNum, $comment_type, $shop_name, $start_date, $end_date, $post_man, $order_type) {
        if($pageNum != null) {
            $last_id = ($pageNum-1)*ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($comment_type != null) {
            $this->db->where("shopcommentType", $comment_type);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "shopcommentPostTime >= '".$start_date."' and shopcommentPostTime <= '".$end_date."'";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "shopcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($shop_name != null) {
            $strWhere = "shopcommentShopID IN (select id from tb_shop where INSTR(shopname, '".$shop_name."') > 0)";
            $this->db->where($strWhere);
        }

        if($order_type != null) {
            if($order_type == 0) {
                $this->db->order_by("shopcommentcommentPostTime", "desc");
            }
            else if($order_type == 1) {
                $this->db->order_by("shopcommentShopLevel", "desc");
            }
            else if($order_type == 2) {
                $this->db->order_by("shopcommentShopLevel");
            }
        }

        $strWhere = "shopcommentPostManID != ".ADMIN_ID;
        $this->db->where($strWhere);
        $query =  $this->db->select('tb_shopcomment.id,
                    (3) as type,
                    (
                        SELECT
                           tb_category.categoryName
                        FROM
                            tb_category
                        WHERE
                            tb_category.id = tb_shop.shopCategoryID and categoryStatus != '.STATUS_DELETE.'
                    ) AS categoryName,
                    tb_shopcomment.shopcommentContent as content,
                    (
                        SELECT
                            tb_user.userID
                        FROM
                            tb_user
                        WHERE
                            tb_user.id = tb_shopcomment.shopcommentPostManID and userStatus != '.STATUS_DELETE.'
                    ) AS postMan,
                    tb_shopcomment.shopcommentPostTime as postTime,
                    (0) as heartCnt,
                    (0) AS declareCnt,
                    tb_shopcomment.shopcommentStatus as status,
                    shopcommentShopStatus,
                    tb_shop.shopName,
                    tb_shopcomment.shopcommentShopLevel
                    ')
            ->from('tb_shopcomment')
            ->where('shopcommentStatus != '.STATUS_DELETE)
            ->join('tb_shop', 'tb_shopcomment.shopcommentShopID = tb_shop.id and shopcommentStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }
        else {
            $result = Array();
        }

        return $result;
    }

    function  getShopCommentCountWithCond($start_date, $end_date, $post_man) {
        return $this->getShopCommentCountBy(null, null, $start_date, $end_date, $post_man);
    }

    function  getShopUserRemarkCount($shop_name, $post_man)  {
        return $this->getShopCommentCountBy(TYPE_SHOPCOMMENT_USERREMARK, $shop_name, null, null, $post_man);
    }

    function  getShopCommentCountBy($comment_type, $shop_name, $start_date, $end_date, $post_man) {
        if($comment_type != null) {
            $this->db->where("shopcommentType", $comment_type);
        }

        if($start_date != null && $end_date != null) {
            $strWhere = "shopcommentPostTime >= '".$start_date."' and shopcommentPostTime <= '".$end_date."'";
            $this->db->where($strWhere);
        }

        if($post_man != null) {
            $strWhere = "shopcommentPostManID IN (select id from tb_user where INSTR(userID, '".$post_man."') > 0)";
            $this->db->where($strWhere);
        }

        if($shop_name != null) {
            $strWhere = "shopcommentShopID IN (select id from tb_shop where INSTR(shopname, '".$shop_name."') > 0)";
            $this->db->where($strWhere);
        }

        $strWhere = "shopcommentPostManID != ".ADMIN_ID;
        $this->db->where($strWhere);

        $query =  $this->db->select('count(*) as count')
            ->from('tb_shopcomment')
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

    function  manageShopComment($comment_id) {
        $data = Array(
            'shopcommentStatus' => STATUS_MANAGE
        );
        $this->db->update('tb_shopcomment', $data, "id = ".$comment_id);
    }

    function  getCategoryList() {
        $query = $this->db->select('*')
            ->from('tb_category')
            ->where('categoryShop', STATUS_ON)
            ->where('categoryStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }

    function  getUserRemarkShopList($pageNum, $shopName, $shopStatus,  $orderType) {

        if($pageNum != null && $pageNum >= 1) {
            $last_id = ($pageNum - 1) *ADMIN_MAX_PAGE_ITEM_CNT;
            $this->db->limit(ADMIN_MAX_PAGE_ITEM_CNT, $last_id);
        }

        if($shopName != null) {
            $strWhere = "INSTR(shopName,'".$shopName."') > 0";
            $this->db->where($strWhere);
        }

        if($orderType != null) {
            if($orderType == 0) {
                $this->db->order_by('shopLevel', 'desc');
            }
            else {
                $this->db->order_by('shopLevel');
            }
        }

        if($shopStatus != null) {
            $this->db->where('shopStatus', $shopStatus);
        }

        $query =  $this->db->select('*')
            ->from('tb_shop')
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = array();
        }

        return $result;
    }

    function  getUserRemarkShopCount($shopName, $shopStatus) {
        if($shopName != null) {
            $strWhere = "INSTR(shopName,'".$shopName."') > 0";
            $this->db->where($strWhere);
        }
        if($shopStatus != null) {
            $this->db->where('shopStatus', $shopStatus);
        }

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


    function removeShopComment($comment_id) {
        $data = Array(
            "shopcommentStatus" => STATUS_DELETE
        );
        $this->db->update('tb_shopcomment', $data, "id = ".$comment_id);

        $data = Array(
            "shopanswerStatus" => STATUS_DELETE
        );
        $this->db->update('tb_shopanswer', $data, "shopanswerShopCommentID = ".$comment_id);
    }

    function removeShopCommentByUserID($user_id) {
        $query =  $this->db->select('id')
            ->from('tb_shopcomment')
            ->where('shopcommentStatus != '.STATUS_DELETE)
            ->wherE('shopcommentPostManID', $user_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        $cnt = count($result);

        for($i = 0; $i < $cnt; $i++) {
            $this->removeShopComment($result[$i]['id'], STATUS_DELETE);
        }
    }

    function removeShopAnswerByUserID($user_id) {
        $data = Array(
            "shopanswerStatus" => STATUS_DELETE
        );
        $this->db->update('tb_shopanswer', $data, "shopanswerPostManID = ".$user_id);
    }


    function getEventList($shop_id) {
        if($shop_id != null) {
            $this->db->where('tb_event.eventShopID', $shop_id);
        }

        $queryStr = 'tb_event.*,
                tb_event.id,
                (
                        SELECT
                        tb_image.imageURL
                        FROM
                        tb_image
                        WHERE
                        tb_event.eventImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
                ) AS eventImgURL,
                tb_shop.shopName as eventShopName,
                tb_shop.shopAddress as eventAddress
                ';

        $this->db->where('tb_event.eventShopID IS NOT NULL');
        $this->db->join('tb_shop', 'tb_event.eventShopID = tb_shop.id and shopStatus != '.STATUS_DELETE);

        $query =  $this->db->select($queryStr)
            ->from('tb_event')
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

    function updateEvent($data, $mode) {

        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_event', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_event', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }
        else if($mode == MODE_REMOVE) {
            $updateDate = Array(
                "eventStatus"=>STATUS_DELETE
            );
            $this->db->update('tb_event', $updateDate, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

    function updateProduct($data, $mode) {
        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_product', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_product', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }
        else if($mode == MODE_REMOVE) {
            $updateDate = Array(
                "productStatus"=>STATUS_DELETE
            );
            $this->db->update('tb_product', $updateDate, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

    function createShopComment($data) {
        $this->db->insert('tb_shopcomment', $data);
        $insert_id = $this->db->insert_id();
        return $insert_id;
    }

    function getShopLevel($shop_id) {
        $selectQuery = 'COUNT(*) as shopReviewCnt, AVG(shopcommentShopLevel) as shopRemarkLevel';

        $query =  $this->db->select($selectQuery)
            ->from('tb_shopcomment')
            ->where('shopcommentShopID', $shop_id)
            ->where('shopcommentType', TYPE_SHOPCOMMENT_USERREMARK)
            ->where('shopcommentStatus != '.STATUS_DELETE)
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
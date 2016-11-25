<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api_model extends CI_Model {

	function __construct()
    {
        // Call the Model constructor
        parent::__construct();

        $this->load->library("util");
        $this->load->model('member_model');
        $this->load->model('cast_model');
        $this->load->model('gcm_model');
        $this->load->model('banner_model');
    }


    private function getImageList($arr_img_id) {
        if($arr_img_id == null) {
            return null;
        }

        $shopIDArrayString = $arr_img_id;

        if ($shopIDArrayString != null) {
            $arrImgID = json_decode ( $shopIDArrayString );

            $cnt = count ( $arrImgID );

            $result = Array ();

            for($i = 0; $i < $cnt; $i ++) {

                $imgInfo = $this->getImage ( $arrImgID [$i] );
                array_push ( $result, $imgInfo );
            }
        }
        return $result;
    }
	
    /*
     * function related tb_app.
     * */
    function getAppInfo($appVersion, $osType) {
    
    	$query =  $this->db->select('*,
    	                            tb_app.id as id,
    								tb_market.marketName AS appMarketName')
                            ->from('tb_app')
                            ->join('tb_market', 'tb_app.appMarketID = tb_market.id and marketStatus != '.STATUS_DELETE)
                            ->where('appOS', $osType)
                            ->where('appVersion', $appVersion)
                            ->where('appStatus != '.STATUS_DELETE)
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
    
    
    /*
     * function related tb_user.
     * */
	function getInitInformation($header) {
		
		$device_model = $header[DEVICE_MODEL];
		$device_id = $header[DEVICE_ID];
		$os_type = $header[OS_TYPE];
		$os_version = $header[OS_VERSION];
		
		
		$query = $this->db->select('*,
									tb_market.marketName AS appMarketName,
									(
										SELECT
											tb_image.imageURL
										FROM
											tb_image
										WHERE
											tb_user.userImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
									) AS userImgURL,
									tb_user.id as id
									')
                ->from('tb_user')
                ->join('tb_market', 'tb_user.userMarketID = tb_market.id')
                ->join('tb_app', 'tb_user.userAppVersion = tb_app.appVersion And tb_user.userOsType = tb_app.appOS')
                ->where('userDeviceId', $device_id)
                ->where('userDeviceModel', $device_model)
                ->where('userOsType', $os_type)
                ->where('userStatus != '.STATUS_DELETE)
                ->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();		
			$result = $result[0];
		} else {
			$result = null;
		}
		
		return $result;
	}
	
	function getUserIDWithHeader($header) {

		$device_model = $header[DEVICE_MODEL];
		$device_id = $header[DEVICE_ID];
		$os_type = $header[OS_TYPE];
		$os_version = $header[OS_VERSION];
		$app_key = $header[APP_KEY];
		$app_version = $header[APP_VERSION];
		$app_market = $header[APP_MARKET];
		
		$query = $this->db->select('*,
									tb_user.id as id
								   ')
						->from('tb_user')
						->join('tb_market', 'tb_user.userMarketID = tb_market.id')
						->where('userDeviceId', $device_id)
						->where('userDeviceModel', $device_model)
						->where('userOsType', $os_type)
						->where('userOsVersion', $os_version)
						->where('userKey', $app_key)
						->where('userAppVersion', $app_version)
						->where('tb_market.marketName', $app_market)
                        ->where('userStatus != '.STATUS_DELETE)
						->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();		
			$result = $result[0];
				
		} else {
			$result = null;
		}

		return $result;
	}
	
	function getUser($user_id) {
	
		$query = $this->db->select('*,
									(
										SELECT
											tb_image.imageURL
										FROM
											tb_image
										WHERE
											tb_user.userImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
									) AS userImgURL')
										->from('tb_user')
										->where("id", $user_id)
                                        ->where('userStatus != '.STATUS_DELETE)
										->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();		
			$result = $result[0];
		} else {
			$result = null;
		}
	
		return $result;
	}
	
	function getUserIDWithNickname($nickname) {
		$query =  $this->db->select('id')
		                ->from('tb_user')
                        ->where('userID', $nickname)
                        ->where('userStatus != '.STATUS_DELETE)
		                ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->row()->id;
	
		} else {
			$result = 0;
		}
	
		return $result;
	}
	
	
	function createUser($user) {
		$this->db->insert('tb_user', $user);
	}
	
	function updateUser ($user_id, $data) {
		$this->db->update('tb_user', $data, "id = ".$user_id);
	}
	
	function updateAccess ($cnt) {
		$query =  $this->db->select('cnt')
                ->from('tb_access')
	            ->get();
		if ($query->num_rows() > 0) {
			$result = $query->row()->cnt;			
		} else {
			$result = 0;
		}
		$data['cnt'] = $result+$cnt;
		if($data['cnt']<0) $data['cnt'] = 0;
		$this->db->update('tb_access', $data);
	}
	
	/*
	 * function relate tb_shop
	 * @return NULL*/
	
	function getShopWithShopID($shop_id) {
		$query =  $this->db->select('*')
		                    ->from('tb_shop')
		                    ->where('shopID', $shop_id)
                            ->where('shopStatus != '.STATUS_DELETE)
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
	
	function getShopWithShopNameAndAddress($shop_name, $shop_address) {
		$query =  $this->db->select('*')
                            ->from('tb_shop')
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
	
	
	function getShop($shop_id) {
	
		$query = $this->db->select('*,
									(
										SELECT
											tb_image.imageURL
										FROM
											tb_image
										WHERE
											tb_shop.shopImgID = tb_image.id  and imageStatus != '.STATUS_DELETE.'
									) AS shopImgURL,
									shopCategoryID,
									(
										SELECT
											tb_category.categoryName
										FROM
											tb_category
										WHERE
											tb_shop.shopCategoryID = tb_category.id and categoryStatus != '.STATUS_DELETE.'
									) AS shopCategoryName,
									(
										SELECT
											tb_image.imageURL
										FROM
											tb_image
										WHERE
											tb_shop.shopManagerIdentyImgID = tb_image.id  and imageStatus != '.STATUS_DELETE.'
									) AS shopManagerIdentyImgURL
									')
										->from('tb_shop')
										->where("id", $shop_id)
                                        ->where('shopStatus != '.STATUS_DELETE)
										->get();
	
		if ($query->num_rows() > 0) {
			$result =  $query->result_array();
			$result = $result[0];
		} else {
			$result = null;
		}
	
		return $result;
	}
	
	function  getShopListWithOwnerID($user_id) {
		$selectQuery ='tb_shop.*,
						(
							SELECT
								tb_image.imageURL
							FROM
								tb_image
							WHERE
								tb_shop.shopImgID = tb_image.id  and imageStatus != '.STATUS_DELETE.'
						) AS shopImgURL,
						shopCategoryID,
						(
							SELECT
								tb_category.categoryName
							FROM
								tb_category
							WHERE
								tb_shop.shopCategoryID = tb_category.id  and categoryStatus != '.STATUS_DELETE.'
						) AS shopCategoryName,
						(
							SELECT
								tb_image.imageURL
							FROM
								tb_image
							WHERE
								tb_shop.shopManagerIdentyImgID = tb_image.id  and imageStatus != '.STATUS_DELETE.'
						) AS shopManagerIdentyImgURL
						';
		$query =  $this->db->select($selectQuery)
		                        ->from('tb_shop')
                                ->join('tb_user', 'tb_user.id = tb_shop.shopOwnerID and userStatus != '.STATUS_DELETE)
                                ->where('shopOwnerID', $user_id)
                                ->where('shopOwnerID != '.ADMIN_ID)
                                ->where('shopStatus != '.STATUS_DELETE)
                                ->get();

		if ($query->num_rows() > 0) {
			$result = $query->result_array();
		}
		else {
			$result = Array();
		}
	
		return $result;
	}
	
	
	function getShopList($category_id, $location, $shop_name, $cur_lat, $cur_long, $radius, $shop_cnt) {
	
		$strSelect =   'tb_shop.*,
						(
							SELECT
								tb_image.imageURL
							FROM
								tb_image
							WHERE
								tb_shop.shopImgID = tb_image.id  and imageStatus != '.STATUS_DELETE.'
						) AS shopImgURL,
						shopCategoryID,
						(
							SELECT
								tb_category.categoryName
							FROM
								tb_category
							WHERE
								tb_shop.shopCategoryID = tb_category.id  and categoryStatus != '.STATUS_DELETE.'
						) AS shopCategoryName,
						(
							SELECT
								tb_image.imageURL
							FROM
								tb_image
							WHERE
								tb_shop.shopManagerIdentyImgID = tb_image.id  and imageStatus != '.STATUS_DELETE.'
						) AS shopManagerIdentyImgURL
						';
	
	
		if($category_id != null) {
			$this->db->where('shopCategoryID', $category_id);
		}
		
		if($location != null) {
			$arrAddress = explode(" ", $location);
			$cnt = count($arrAddress);
			
			$strLocationWhere = "INSTR(shopAddress,'".$arrAddress[0]."') > 0";
			
			for($i = 1; $i < $cnt; $i++) {
				$strLocationWhere = $strLocationWhere." AND INSTR(shopAddress,'".$arrAddress[$i]."') > 0";
			}
			
			$this->db->where($strLocationWhere);
		}
	
		if($shop_name != null) {
			$strNameWhere = "INSTR(shopName,'".$shop_name."') > 0";
			$this->db->where($strNameWhere);
		}
		
		// $cur_lat, $cur_long, $radius
		if($radius == null) {
			$radius = 0.3; // unit: km 
		}
		else {
			$radius = 0.001 * $radius;
		}
		
		if($cur_lat != null && $cur_long != null) {
			$dist = 'ROUND(6371 * ACOS(COS(RADIANS('.$cur_lat.')) * COS(RADIANS(shopLatitude)) * COS(RADIANS(shopLongtitude) - RADIANS('.$cur_long.')) + SIN(RADIANS('.$cur_lat.')) * SIN(RADIANS(shopLatitude))),2)';
			$whereQuery = $dist.' < '.$radius;
			
			$this->db->where($whereQuery);
			$this->db->order_by($dist);
		}

		if($shop_cnt != null) {
			$this->db->limit($shop_cnt);
		}
		
		$query =  $this->db->select($strSelect)
		                    ->from('tb_shop')
                            ->join('tb_user', 'tb_user.id = tb_shop.shopOwnerID and userStatus != '.STATUS_DELETE)
                            ->where('shopStatus != '.STATUS_DELETE)
                            ->where('shopStatus != '.STATUS_REQUEST)
                            ->where('shopVisibility != '.STATUS_HIDE)
		                    ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
		}
		else {
			$result = Array();
		}
	
		return $result;
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


    /**
     * $data: updated or added data. if update or remove, it has id field.
     *
     * return: $id if not processed, it is invalid id or null.
     **/
    function updateShop($data, $mode) {

        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_shop', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_shop', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

    function addShopCallCount($shop_id, $count) {

        $strQuery = 'Update tb_shop SET shopCallCnt = shopCallCnt + '.$count.' where id = '.$shop_id;
        $this->db->query($strQuery);

        return $this->getShopCallCnt($shop_id);
    }

    private function getShopCallCnt($shop_id) {
        $query =  $this->db->select('shopCallCnt as count')
            ->from('tb_shop')
            ->where('id', $shop_id)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;
        }
        else {
            $result = 0;
        }

        return $result;
    }

    function addShopClickCount($shop_id, $count) {

        $strQuery = 'Update tb_shop SET shopClickCnt = shopClickCnt + '.$count.' where id = '.$shop_id;
        $this->db->query($strQuery);

        return $this->getShopClickCnt($shop_id);
    }

    private function getShopClickCnt($shop_id) {
        $query =  $this->db->select('shopClickCnt as count')
            ->from('tb_shop')
            ->where('id', $shop_id)
            ->where('shopStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;
        }
        else {
            $result = 0;
        }

        return $result;
    }
	
	/**
	 * functions related tb_product
	 * @param unknown $productData*/
	

	function getProductListWithShopID($page_num, $page_item_cnt, $shop_id){
		return $this->getProductListBy($page_num, $page_item_cnt,null, null, $shop_id, null);
	}

    function getProductList($page_num, $page_item_cnt, $location_name, $product_name, $order_type) {
        return $this->getProductListBy($page_num, $page_item_cnt,$location_name, $product_name, null, $order_type);
    }
	
	private function getProductListBy($page_num, $page_item_cnt, $location_name, $product_name, $shop_id, $order_type) {

        if($page_item_cnt != null && $page_item_cnt >= 1 && $page_num != null && $page_num >= 1) {
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

        if($order_type != null) {
            if($order_type == 0) { //별표순

            }
            else if($order_type == 1) { //가격순(낮은순)
                $this->db->order_by("productPrice");
            }
            else if($order_type == 2) { //가격순(높은순)
                $this->db->order_by("productPrice", "desc");
            }
        }
        else {
            $this->db->order_by("productName", "desc");
        }

        if($shop_id != null) {
            $this->db->where('productShopID', $shop_id);
        }
	
		if($product_name != null) {
			$this->db->where('productName', $product_name);
		}
	
		if($location_name != null) {
			$strLocationWhere = "INSTR(tb_shop.shopAddress,'".$location_name."') > 0";
			$this->db->where($strLocationWhere);
		}
	
		$selectQuery = 'tb_product.id, 
						tb_product.productName,
				        tb_product.productPrice, 
				        tb_product.productEventPrice, 
				        tb_product.productShopID,
						tb_shop.id as shopID, 
				        tb_shop.shopName,
						tb_shop.shopAddress,  
				        tb_shop.shopLevel';

		$query =  $this->db->select($selectQuery)
                                    ->from('tb_product')
                                    ->where('productStatus != '.STATUS_DELETE)
                                    ->join('tb_shop', 'tb_product.productShopID = tb_shop.id and productStatus != '.STATUS_DELETE)
                                    ->get();
	
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
	
		} else {
			$result = Array();
		}
	
		return $result;
	}
	
	
	function createProduct($productData) {
		$this->db->insert('tb_product', $productData);
	}
	
	function modifyProduct($productID, $productData) {
	
		$query =  $this->db->select('*')
                            ->from('tb_product')
                            ->where('id', $productID)
                            ->where('productStatus != '.STATUS_DELETE)
                            ->get();
	
		if ($query->num_rows() > 0) {
			$this->db->where('id', $productID);
			$query =  $this->db->update('tb_product', $productData);
			$result = "성공";
		}
		else {
			$result = "실패";
		}
	
		return  $result;
	}
	
	
	function getShopCommentList($page_num, $page_item_cnt, $type, $shop_id, $user_id) {

        if($page_item_cnt != null && $page_item_cnt >= 1 && $page_num != null && $page_num >= 1) {
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

        if($user_id != null) {
            $this->db->where('shopcommentPostManID', $user_id);
        }

        if($type != null) {
            $this->db->where('shopcommentType', $type);
        }

        if($shop_id != null) {
            $this->db->where('shopcommentShopID', $shop_id);
        }

        $this->db->order_by("shopcommentPostTime", "desc");

        $selectQuery = 'tb_shopcomment.id,
						tb_shopcomment.shopcommentPostManID,
						tb_shopcomment.shopcommentShopID,
						tb_shopcomment.shopcommentContent,
						tb_shopcomment.shopcommentShopLevel,
						tb_shopcomment.shopcommentType,
						tb_shopcomment.shopcommentShopStatus,
						tb_shopcomment.shopcommentStatus,
						UNIX_TIMESTAMP(
							tb_shopcomment.shopcommentPostTime
						) AS shopcommentPostTime,
						(tb_user.userID) as userNickname';

		$query =  $this->db->select($selectQuery)
                            ->from('tb_shopcomment')
                            ->join('tb_user', 'tb_user.id = tb_shopcomment.shopcommentPostManID and userStatus != '.STATUS_DELETE)
                            ->where('shopcommentStatus != '.STATUS_DELETE)
                            ->where('shopcommentStatus != '.STATUS_HIDE)
                            ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
		}
		else {
			$result = Array();
		}
	
		return $result;
	}
	
	
	/*
	 * functions related tb_shopcomment
	 * 
	 * WI:wrong infromation
	 * UR:user remark
	 * QA:user question
	 * 
	 * shop status:
	 * SHOP_STATUS_CLOSE = 0
     * SHOP_STATUS_WRONG_NAME = 1
     * SHOP_STATUS_WRONG_ADDRESS = 2
     * SHOP_STATUS_WRONG_PHONENUMBER = 3
     * SHOP_STATUS_WRONG_OPEN_TIME = 4
     * SHOP_STATUS_WRONG_DISCOUNT_INFO = 5
	 * 
	 * @param unknown $comment_id
	 * @return NULL***/
	
	function getShopCommentByID($comment_id) {
	
		$selectQuery = '*,
						UNIX_TIMESTAMP(
							tb_shopcomment.shopcommentPostTime
						) AS shopcommentPostTime,
						(
						SELECT
							userID
						FROM
							tb_user
						WHERE
							tb_shopcomment.shopcommentPostManID = tb_user.id  and userStatus != '.STATUS_DELETE.'
						) as userNickname';
	
		$query =  $this->db->select($selectQuery)
                            ->from('tb_shopcomment')
                            ->where('id', $comment_id)
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
	
	function createShopComment($data) {
		$this->db->insert('tb_shopcomment', $data);
        $insert_id = $this->db->insert_id();
        return $insert_id;
	}
	
	function modifyShopComment($comment_id, $data) {
		$this->db->update('tb_shopcomment', $data, "id = ".$comment_id);
	}
	
	function removeShopComment($comment_id) {
       $this->shop_model->removeShopComment($comment_id);
	}
	
	/*
	 * functions related tb_shopanswer 
	 * **/
	function getShopAnswerListWithShopID($shop_id) {
		$query = $this->db->query('SELECT
										*,
										UNIX_TIMESTAMP(
											tb_shopanswer.shopanswerPostTime
										) AS shopanswerPostTime,
										(
										SELECT
											userID
										FROM
											tb_user
										WHERE
											tb_shopanswer.shopanswerPostManID = tb_user.id  and userStatus != '.STATUS_DELETE.'
										) as userNickname,
										tb_shopanswer.id as id
									FROM
										tb_shopanswer,
										(
											SELECT
												id
											FROM
												tb_shopcomment
											WHERE
												tb_shopcomment.shopcommentShopID = '.$shop_id.' and shopcommentStatus != '.STATUS_DELETE.'
										) comment
									WHERE
										tb_shopanswer.shopanswerShopCommentID = comment.id and shopanswerStatus != '.STATUS_DELETE);
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
	
		} else {
			$result = Array();
		}
	
		return $result;
	}
	
	function createShopAnswer($data) {
		$this->db->insert('tb_shopanswer', $data);
	}
	
	function modifyShopAnswer($answerid, $data) {
		$this->db->update('tb_shopanswer', $data, "id = ".$answerid);
	}
	
	function removeShopAnswer($answerid) {
        $this->shop_model->removeShopAnswer($answerid);
	}

    /**
     * @param $best_cnt      the count of best freetalk to get.
     * if best is not exist with in the items of count, it has them by heartCnt.
     *
     * @return array
     */
	function getBestFreetalkList($best_cnt) {
        //$page_num, $page_item_cnt, $category_id, $search_word, $user_id, $freetalk_id, $byLike, $is_best
        $result = $this->getFreeTalkListBy(1, $best_cnt, null, null, null, null, true, true);

        $cnt = count($result);

        // get order by heart
        if($cnt < $best_cnt) {
            $other_result = $this->getFreeTalkListBy(1, $best_cnt, null, null, null, null, true, false);
            for($i = 0; $i < count($other_result); $i++) {
                $result[$cnt+$i] = $other_result[$i];
            }
        }

        return $result;
	}

    /**
     * @param $freetalk_id   freetalk id must be identified.
     * @return null if not exist
     */
	function getFreetalk($freetalk_id) {
		$result = $this->getFreeTalkListBy(1, 1, null, null, null, $freetalk_id, null, null);
		if($result == null) {
			return  null;
		}
		
		if(count($result) == 0) {
			return  null;
		}
		
		return $result[0];
	}


	function getFreetalkList($page_num, $page_item_cnt, $category_id, $search_word) {
		$result = $this->getFreeTalkListBy($page_num, $page_item_cnt, $category_id, $search_word, null, null, null, null);
		return $result;
	}
	
	function getFreetalkListByUserID($page_num, $page_item_cnt, $user_id) {
		$result = $this->getFreeTalkListBy($page_num, $page_item_cnt, null, null, $user_id, null, null, null);
		return $result;
	}

    /**
     * @param $page_num
     * @param $page_item_cnt
     * @param $category_id
     * @param $search_word
     * @param $user_id
     * @param $freetalk_id
     * @param $byLike
     * @param $is_best
     * @return array
     */
	private function  getFreeTalkListBy($page_num, $page_item_cnt, $category_id, $search_word, $user_id, $freetalk_id, $byLike, $is_best) {

        if($byLike != null && $byLike == true) { // best
			$this->db->order_by("freetalkHeartCnt", "desc");
            $this->db->limit($page_item_cnt);
		}
        else if($page_num != null && $page_num >= 1) { // search
            $this->db->order_by('freetalkPostTime',"desc");
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

        if($user_id != null) { // get me
            $this->db->where('tb_freetalk.freetalkPostManID', $user_id);
        }

        if($freetalk_id != null) { // get freetalk
            $this->db->where('tb_freetalk.id', $freetalk_id);
        }

        if($category_id != null) {
            $this->db->where('tb_freetalk.freetalkCategoryID', $category_id);
        }

        if($search_word != null){
            $this->db->where("INSTR(tb_freetalk.freetalkContent,'".$search_word."') > 0");
        }

        if($is_best != null) {
            if ($is_best == true || $is_best == 'true') {
                $this->db->where('freetalkBest', STATUS_YES);
            } else if ($is_best == false || $is_best == 'false') {
                $this->db->where('freetalkBest', STATUS_NO);
            }
        }

		$selectSQL = 'tb_freetalk.*,
                    tb_user.userID AS userID,
                    tb_user.userLevel AS userLevel,
                    (
                        SELECT
                            tb_image.imageURL
                        FROM
                            tb_image
                        WHERE
                            tb_user.userImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
                    ) AS userImgURL,
                    tb_category.categoryName as freetalkCategoryName,
                    (
                        SELECT
                            COUNT(*)
                        FROM
                            tb_freetalkcomment
                        WHERE
                            tb_freetalkcomment.freetalkcommentFreetalkID = tb_freetalk.id and freetalkcommentStatus != '.STATUS_DELETE.'
                    ) AS freetalkCommentCnt,
                    (
                        SELECT
                            COUNT(*)
                        FROM
                            tb_freetalkrelation
                        WHERE
                            tb_freetalkrelation.freetalkrelationFreetalkID = tb_freetalk.id and freetalkrelationDeclare = 1 and freetalkrelationStatus != ' . STATUS_DELETE . '
                    ) AS freetalkDeclareCnt
                    ';
		
		$query = $this->db->select($selectSQL)
								->from('tb_freetalk')
								->join('tb_category', 'tb_freetalk.freetalkCategoryID = tb_category.id and categoryStatus != '.STATUS_DELETE)
								->join('tb_user', 'tb_freetalk.freetalkPostManID = tb_user.id and userStatus != '.STATUS_DELETE.' and (userStatus = '.STATUS_LIFE.' OR userStatus = '.STATUS_LIMIT_USER.')')
                                ->where('freetalkStatus != '.STATUS_DELETE)
                                ->where('freetalkStatus != '.STATUS_HIDE)
								->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
	
		} else {
			$result = Array();
		}

        $resultArray = Array();
        if($result != null) {
            $cnt = count($result);

            for($i = 0; $i < $cnt; $i++) {
                $freetalk = $result[$i];
                $freetalk['freetalkImgArray'] = $this->getImageList($freetalk['freetalkImgIDArrayString']);

                array_push($resultArray ,$freetalk);
            }
        }
	
		return $resultArray;
	}

    /**
     * $page_num: page number. $page_num >= 1
     * $page_cnt: rows per page. if $page_num is null, it is unlimit count. $page_cnt >= 1
     * $freetalk_id: freetalk id for freetalk comment. $freetalk_id >= 1
     * $user_id: post man id for freetalk comment. $user_id >= 0
     *
     * return: array of freetalk comment
     **/
     function  getFreeTalkCommentList($page_num, $page_item_cnt, $freetalk_id, $user_id) {
        if($page_item_cnt != null && $page_item_cnt >= 1 && $page_num != null && $page_num >= 1) {
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

         if($freetalk_id != null && $freetalk_id >= ADMIN_SQL_VALID_ID_MIN) {
             $this->db->where('tb_freetalkcomment.freetalkcommentFreetalkID', $freetalk_id);
         }

         if($user_id != null) {
             $this->db->where('tb_freetalkcomment.freetalkcommentPostManID', $user_id);
         }

         $this->db->order_by('freetalkcommentPostTime',"desc");

        $selectQuery = 'tb_freetalkcomment.*,
                        tb_user.userID as userID,
                        tb_freetalk.freetalkContent as freetalkContent,
                        (
                            SELECT
                                tb_image.imageURL
                            FROM
                                tb_image
                            WHERE
                                tb_user.userImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
                        ) AS userImgURL,
                        (
                            SELECT
                                tb_category.categoryName
                            From
                                tb_category
                            WHERE
                                tb_category.id = tb_freetalk.freetalkCategoryID and categoryStatus != '.STATUS_DELETE.'
                        ) as freetalkCategoryName';
		
		$query = $this->db->select($selectQuery)
							->from('tb_freetalkcomment')
							->join('tb_user', 'tb_freetalkcomment.freetalkcommentPostManID = tb_user.id and userStatus != '.STATUS_DELETE)
							->join('tb_freetalk', 'tb_freetalkcomment.freetalkcommentFreetalkID = tb_freetalk.id and freetalkStatus != '.STATUS_DELETE)
                            ->where('freetalkcommentStatus != '.STATUS_DELETE)
                            ->where('freetalkcommentStatus != '.STATUS_HIDE)
							->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
	
		} else {
			$result = Array();
		}
	
		return $result;
	}
	
	function getFreetalkCommentByID($freetalk_id) {
		$query = $this->db->select('*')
				 ->from('tb_freetalkcomment')
                 ->where('id', $freetalk_id)
                 ->where('freetalkcommentStatus != '.STATUS_DELETE)
				 ->get();
		if($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		}
		else {
			$result = null;
		}
	
		return $result;
	}
	
	
	function getFreetalkLikeCount($freetalk_id) {
		$query =  $this->db->select('count(*) as count')
                            ->from('tb_freetalkrelation')
                            ->where('freetalkrelationFreetalkID', $freetalk_id)
                            ->where('freetalkrelationLike', STATUS_ON)
                            ->where('freetalkrelationStatus != '.STATUS_DELETE)
                            ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->row();
			$result = $result->count;
	
		} else {
			$result = 0;
		}
	
		return $result;
	}
	
	function getFreetalkCommentLikeCount($freetalk_comment_id) {
		$query =  $this->db->select('count(*) as count')
                            ->from('tb_freetalkcommentrelation')
                            ->where('freetalkcommentrelationFreetalkCommentID', $freetalk_comment_id)
                            ->where('freetalkcommentrelationLike', STATUS_ON)
                            ->where('freetalkcommentrelationStatus != '.STATUS_DELETE)
                            ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->row();
			$result = $result->count;
	
		} else {
			$result = 0;
		}
	
		return $result;
	}
	
	function createFreetalkCommentRelation($data) {
		$this->db->insert('tb_freetalkcommentrelation', $data);
	}
	
	function modifyFreetalkCommentRelation($id, $data) {
		$this->db->update('tb_freetalkcommentrelation', $data, "id = ".$id);
	}


    /**
     * $data: updated or added data. if update or remove, it has id field.
     *
     * return: $id if not processed, it is invalid id or null.
     **/
    function updateFreetalk($data, $mode) {

        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_freetalk', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_REMOVE) {
            $this->removeFreetalk($data['id']);
            $ret_id = $data['id'];
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_freetalk', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

	function removeFreetalk($freetalk_id) {

       $this->freetalk_model->removeFreetalk($freetalk_id);
	}
	
	
	function createFreetalkCommentTag($data) {
	
		$this->db->insert('tb_freetalkcommenttag', $data);
	}
	
	function modifyFreetalkCommentTag($freetalk_id, $data) {
	
		$this->db->update('tb_freetalkcommenttag', $data, "id = ".$freetalk_id);
	}

	
	function getFreetalkCommentTagWithData($post_comment_id, $tag_user_id) {
		$query =  $this->db->select('*')
                            ->from('tb_freetalkcommenttag')
                            ->where('freetalkcommenttagFreetalkCommentID',  $post_comment_id)
                            ->where('freetalkcommenttagTAGUserID', $tag_user_id)
                            ->where('freetalkcommenttagStatus != '.STATUS_DELETE)
                            ->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		
		} else {
			$result = Array();
		}
		
		return $result;
	}
	
	function getFreetalkCommentTagList($post_comment_id) {
		$query =  $this->db->select('*')
            ->from('tb_freetalkcommenttag')
            ->where('freetalkcommenttagFreetalkCommentID',  $post_comment_id)
            ->where('freetalkcommenttagStatus != '.STATUS_DELETE)
		    ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
	
		} else {
			$result = Array();
		}
	
		return $result;
	}

    /**
     * $data: updated or added data. if update or remove, it has id field.
     *
     * return: $id if not processed, it is invalid id or null.
     **/
    function updateFreetalkComment($data, $mode) {

        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_freetalkcomment', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_REMOVE) {
           $this->removeFreetalkComment($data['id']);
           $ret_id = $data['id'];
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_freetalkcomment', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

    private function removeFreetalkComment($comment_id) {
       $this->freetalk_model->removeFreetalkComment($comment_id);
    }

	function getFreetalkCommentRelation($cast_id, $user_id) {
		$query =  $this->db->select('*')
                            ->from('tb_freetalkcommentrelation')
                            ->where('freetalkcommentrelationFreetalkCommentID', $cast_id)
                            ->where('freetalkcommentrelationPostManID', $user_id)
                            ->where('freetalkcommentrelationStatus != '.STATUS_DELETE)
                            ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
	
		} else {
			$result = null;
		}
	
		return $result;
	}
	
	function  getFreetalkRelation($freetalk_id, $user_id) {
		$query = $this->db->select('*')
						  ->from('tb_freetalkrelation')
						  ->where('freetalkrelationFreetalkID', $freetalk_id)
						  ->where('freetalkrelationPostManID', $user_id)
                          ->where('freetalkrelationStatus != '.STATUS_DELETE)
						  ->get();
		if($query->num_rows() > 0) {
		
			$result = $query->result_array();
			$result = $result[0];
		}
		else {
			$result = null;
		}
		
		return $result;
	}
	
	function getFreetalkCommentTAGRecentList($page_num, $page_item_cnt, $user_id) {

        if($page_item_cnt != null && $page_item_cnt >= 1 && $page_num != null && $page_num >= 1) {
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

        $this->db->order_by("tb_freetalkcommenttag.freetalkcommenttagPostTime", "desc");

		$selectStr = 'tb_freetalkcomment.*,
                      tb_freetalkcommenttag.freetalkcommenttagTAGUserID,
                      tb_freetalkcommenttag.freetalkcommenttagStatus,
					  (
						SELECT
							userID
						FROM
							tb_user
						WHERE
							tb_user.id = tb_freetalkcomment.freetalkcommentPostManID and userStatus != '.STATUS_DELETE.'
					  ) AS tagPostManUserID,
					  (
						SELECT
							id as  freetalkcommentFreetalkID
						FROM
							tb_freetalk
						WHERE
							tb_freetalk.id = tb_freetalkcomment.freetalkcommentFreetalkID and tb_freetalkcomment.freetalkcommentStatus != '.STATUS_DELETE.'
					  ) as freetalkcommentFreetalkID,
					   (
						SELECT
							(
                            SELECT
                                tb_category.categoryName
                            From
                                tb_category
                            WHERE
                                tb_category.id = tb_freetalk.freetalkCategoryID and categoryStatus != '.STATUS_DELETE.'
                             ) as freetalkCategoryName
						FROM
							tb_freetalk
						WHERE
							tb_freetalk.id = tb_freetalkcomment.freetalkcommentFreetalkID and tb_freetalkcomment.freetalkcommentStatus != '.STATUS_DELETE.'
					  ) as freetalkCategoryName,
					  UNIX_TIMESTAMP(
							freetalkcommenttagPostTime
				      ) AS freetalkcommenttagPostTime';
	
		$joincondition = 'tb_freetalkcomment.id = tb_freetalkcommenttag.freetalkcommenttagFreetalkCommentID and freetalkcommentStatus != '.STATUS_DELETE;

        $query =  $this->db->select($selectStr)
            ->from('tb_freetalkcommenttag')
            ->join('tb_freetalkcomment', $joincondition, 'inner')
            ->where('tb_freetalkcommenttag.freetalkcommenttagTagUserID', $user_id)
            ->where('freetalkcommenttagStatus != '.STATUS_DELETE)
            ->get();

		if ($query->num_rows() > 0) {
			$result = $query->result_array();
		
		} else {
			$result = Array();
		}
		
		return $result;
	}
	
	function createFreetalkRelation($data) {
	
		$this->db->insert('tb_freetalkrelation', $data);
	}
	
	function modifyFreetalkRelation($freetalk_id, $data) {
	
		$this->db->update('tb_freetalkrelation', $data, "id = ".$freetalk_id);
	}


	/*
	 * functions related tb_event
	 * @param unknown $category_id
	 * @return NULL***/
	
	function getBestLifeEventList($best_cnt) {
        $result = $this->getLifeListBy(1, $best_cnt, null, true, true);

        $cnt = count($result);

        // get order by heart
        if($cnt < $best_cnt) {
            $other_result = $this->getLifeListBy(1, $best_cnt, null, true, null);
        }

        for($i = $cnt; $i < $best_cnt; $i++) {
            $result[$i] = $other_result[$i - $cnt];
        }

        return $result;
	}

    function getLifeEventList($page_num, $page_item_cnt, $categoryID) {
        return $this->getLifeListBy( $page_num, $page_item_cnt, $categoryID, false, null);
    }

    /**
     * @param $page_num
     * @param $page_item_cnt
     * @param $categoryID
     * @param $byClickCnt
     * @param $isBest
     * @return array
     */
    private function  getLifeListBy($page_num, $page_item_cnt, $categoryID, $byClickCnt, $isBest) {
        if($byClickCnt != null && $byClickCnt == true) {
            $this->db->order_by("lifeClickCnt", "desc");
        }
        else {
            $this->db->order_by("lifeConsequence");
        }

        if($page_num != null && $page_num >= 1) {
            $last_id = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_id);
        }

        if($categoryID != null) {
            $this->db->where('tb_life.lifeCategoryID', $categoryID);
        }

        if($isBest != null && $isBest == true) {
            $this->db->where('tb_life.lifeBest', STATUS_YES);
        }

        $queryStr = 'tb_life.*,
                     (
						SELECT
							imageURL
						FROM
							tb_image
						WHERE
							tb_image.id = tb_ads.adsImgID and imageStatus != '.STATUS_DELETE.'
					  ) AS adsImgURL,
					  tb_ads.adsURL as adsURL';

        $query =  $this->db->select($queryStr)
                            ->from('tb_life')
                            ->join('tb_ads', 'tb_life.lifeAdsID = tb_ads.id and adsStatus != '.STATUS_DELETE)
                            ->where("lifeStatus != ".STATUS_DELETE)
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

    function createLife($data) {
        $this->db->insert('tb_life', $data);
    }

    function modifyLife($event_id, $data) {
        $this->db->update('tb_life', $data, "id = ".$event_id);
    }

    function createLifeClickLog($event_id) {
        $logData = Array(
            'lifeclickLifeID' => $event_id,
            'lifeclickTime' => $this->util->getTime()
        );

        $this->db->insert('tb_lifeclick', $logData);
    }

	function getBestShopEventList($cnt) {
		return $this->getEventListBy(1, $cnt, null, null, null, null, true);
	}
	
	function getEventList($page_num, $page_item_cnt, $locationName, $categoryID, $shopID) {
		return $this->getEventListBy($page_num, $page_item_cnt, $locationName, $categoryID, $shopID, null, null);
	}

    /**
     * @param $page_num
     * @param $page_item_cnt
     * @param $locationName
     * @param $categoryID
     * @param $shopID
     * @param $event_id
     * @param $byClickCnt
     * @return array
     */
    private function getEventListBy($page_num, $page_item_cnt, $locationName, $categoryID, $shopID, $event_id, $byClickCnt){
        if($page_item_cnt != null && $page_item_cnt >= 1 && $page_num != null && $page_num >= 1) {
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

		if($byClickCnt != null && $byClickCnt == true) { // like
			$this->db->order_by("eventClickCnt", "desc");
		}

		if($categoryID != null) {
			$this->db->where('tb_shop.shopCategoryID', $categoryID);
		}

        $this->db->where('tb_event.eventShopID IS NOT NULL');
        $this->db->join('tb_shop', "tb_event.eventShopID = tb_shop.id and shopEventable = 'Y' and shopStatus != ".STATUS_DELETE);

        if($locationName != null) {
            $strLocationWhere = "INSTR(tb_shop.shopAddress,'".$locationName."') > 0";
            $this->db->where($strLocationWhere);
        }

        if($shopID != null) {
            $this->db->where('tb_event.eventShopID', $shopID);
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
	
	function getEvent($event_id) {
	
		$result = $this->getEventListBy(1, 1, null, null, null, $event_id, null);
        if(count($result) == 0) {
            return null;
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
            $this->shop_model->removeShopEvent($data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

    function addEventClickCount($event_id, $count) {

        $strQuery = 'Update tb_event SET eventClickCnt = eventClickCnt + '.$count.' where id = '.$event_id;
        $this->db->query($strQuery);

        return $this->getEventClickCnt($event_id);
    }

    private function getEventClickCnt($event_id) {
        $query =  $this->db->select('eventClickCnt as count')
            ->from('tb_event')
            ->where('id', $event_id)
            ->where('eventStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->row();
            $result = $result->count;
        }
        else {
            $result = 0;
        }

        return $result;
    }
	
	/**
	 * functions related tb_cast, tb_castdetail
	 * @return NULL if not exist
     */
	
	function getCast($cast_id) {
        //$page_num, $page_item_cnt, $category_id, $cast_id, $byLike, $is_best
		$result = $this->getCastListBy(1, 1, null, $cast_id, null, null);
		
		if($result == null) {
			return null;
		}
		
		if(count($result) == 0) {
			return  null;
		}
		
		if($result != null) {
			$result = $result[0];
		}
		
		return $result;
	}
	
	function getCastList($page_num, $cnt, $category_id) {
		$result = $this->getCastListBy($page_num, $cnt, $category_id, null, null, null);
	
		return $result;
	}
	
	function getBestCastList($best_cnt) {

        $result = $this->getCastListBy(1, $best_cnt, null, null, true, 1);

        $cnt = count($result);

        // get order by heart
        if($cnt < $best_cnt) {
            $other_result = $this->getCastListBy(1, $best_cnt, null, null, true, 2);
            $other_cnt = count($other_result);
            for($i = 0; $i < $other_cnt; $i++) {
                $idx = $cnt+($i);

                if($idx >= $best_cnt) {
                    break;
                }

                $result[$idx] = $other_result[$i];
            }
        }

		return $result;
	}

    /**
     * @param $page_num
     * @param $page_item_cnt
     * @param $category_id
     * @param $cast_id
     * @param $byLike
     * @param $is_best
     * @return array
     */
	private function getCastListBy($page_num, $page_item_cnt, $category_id, $cast_id, $byLike, $is_best) {

        if($byLike != null && $byLike == true) {
            $this->db->order_by("castHeartCnt", "desc");
            $this->db->limit($page_item_cnt);
        }
		else if($page_num != null && $page_num >= 1) {
			$start_idx = ($page_num - 1)*$page_item_cnt;
			$this->db->limit($page_item_cnt, $start_idx);
            $this->db->order_by("castConsequence","desc");
		}
        else {
            $this->db->order_by("castConsequence","desc");
        }

        if($cast_id != null){
            $this->db->where('tb_cast.id', $cast_id);
        }

		if($category_id != null) {
			$this->db->where("castCategoryID", $category_id);
		}

        if($is_best != null ) {
            if($is_best == 1) {
                $this->db->where('castBest', STATUS_YES);
            }
            else if($is_best == 2) {
                $this->db->where('castBest', STATUS_NO);
            }
        }

		$query =  $this->db->select('tb_cast.*,
									(
										SELECT
										tb_image.imageURL
										FROM
										tb_image
										WHERE
										tb_cast.castCoveredImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
									) AS castCoveredImgURL,
									(
										SELECT
										count(*)
										FROM
										tb_castcomment
										WHERE
										castcommentCastID = tb_cast.id and castcommentStatus != '.STATUS_DELETE.'
										And castcommentPostManID IN (select id from tb_user where userStatus != '.STATUS_DELETE.')
									) AS castCommentCnt,
									(
										SELECT
										tb_user.userID
										FROM
										tb_user
										WHERE
										tb_cast.castPostManID = tb_user.id and userStatus != '.STATUS_DELETE.'
									) AS castPostManNickName,
									castPostTime as castPostTimeString,
									UNIX_TIMESTAMP(
												castPostTime
											) AS castPostTime
									')
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
	
	
	function getCastDetailList($cast_id) {
		
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
	
	function modifyCast($id, $data) {
		$this->db->update('tb_cast', $data, "id = ".$id);
	}

	function getCastCommentLikeCount($cast_id) {
		$query =  $this->db->select('count(*) as count')
                            ->from('tb_castcommentrelation')
                            ->where('castcommentrelationCastCommentID', $cast_id)
                            ->where('castcommentrelationLike', STATUS_ON)
                            ->where('castcommentrelationStatus != '.STATUS_DELETE)
		                    ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->row();
			$result = $result->count;
	
		} else {
			$result = 0;
		}
	
		return $result;
	}
	
	function getCastCommentRelation($cast_id, $user_id) {
		$query =  $this->db->select('*')
                            ->from('tb_castcommentrelation')
                            ->where('castcommentrelationCastCommentID', $cast_id)
                            ->where('castcommentrelationPostManID', $user_id)
                            ->where('castcommentrelationStatus != '.STATUS_DELETE)
		                    ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
	
		} else {
			$result = null;
		}
	
		return $result;
	}
	
	function createCastCommentRelation($data) {
		$this->db->insert('tb_castcommentrelation', $data);
	}
	
	function modifyCastCommentRelation($id, $data) {
		$this->db->update('tb_castcommentrelation', $data, "id = ".$id);
	}

    function getCastComment($cast_comment_id) {
        $result = $this->getCastCommentListBy(1, 1, null, null, null, $cast_comment_id);

        if($result == null) {
            return null;
        }

        if(count($result) == 0) {
            return null;
        }

        if($result != null) {
            $result = $result[0];
        }

        return $result;
    }

    function  getCastCommentList($page_num, $page_item_cnt,$cast_id, $user_id, $type) {
		return $this->getCastCommentListBy($page_num, $page_item_cnt, $cast_id, $user_id, $type, null);
	}

    /**
     * @param $page_num
     * @param $page_item_cnt
     * @param $cast_id
     * @param $user_id
     * @param $order_type 0:heartCnt 1:postTime
     * @param $cast_comment_id
     * @return array
     */
	private function getCastCommentListBy($page_num, $page_item_cnt, $cast_id, $user_id, $order_type, $cast_comment_id) {

        if($page_num != null && $page_num >= 1) {

            $start_idx = ($page_num - 1)*$page_item_cnt;
            $this->db->limit($page_item_cnt, $start_idx);
        }

		if($cast_id != null) {
			$this->db->where('castcommentCastID', $cast_id);
		}
		
		if($user_id != null) {
			$this->db->where('castcommentPostManID', $user_id);
		}
		
		if($order_type != null) {
			if($order_type == 1) {
				$this->db->order_by("castcommentHeartCnt", "desc");
			}
			else if($order_type == 2) {
				$this->db->order_by("castcommentPostTime", "desc");
			}
		}
		
		if($cast_comment_id  != null) {
			$this->db->where('tb_castcomment.id', $cast_comment_id);
		}
		
		$query =  $this->db->select('tb_castcomment.*,
									tb_castcomment.id,
									tb_user.userID,
									(
										SELECT
											tb_image.imageURL
										FROM
											tb_image
										WHERE
											tb_user.userImgID = tb_image.id   and imageStatus != '.STATUS_DELETE.'
									) AS userImgURL, 
									tb_castcomment.castcommentPostTime
									')
						->from('tb_castcomment')
						->join('tb_user', 'tb_castcomment.castcommentPostManID = tb_user.id and userStatus != '.STATUS_DELETE)
                        ->where('castcommentStatus != '.STATUS_DELETE )
						->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
		
		} else {
			$result = Array();
		}
		
		return $result;
	}
	
	function getCastRelation($cast_id, $user_id) {
		$query =  $this->db->select('*')
                                ->from('tb_castrelation')
                                ->where('castrelationCastID', $cast_id)
                                ->where('castrelationPostManID', $user_id)
                                ->where('castrelationStatus != '.STATUS_DELETE )
		                        ->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		
		} else {
			$result = null;
		}
		
		return $result;
	}
	
	function getCastLikeCount($cast_id) {
		$query =  $this->db->select('count(*) as count')
		->from('tb_castrelation')
		->where('castrelationCastID', $cast_id)
		->where('castrelationLike', STATUS_ON)
            ->where('castrelationStatus != '.STATUS_DELETE )
		->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->row();
			$result = $result->count;
		
		} else {
			$result = 0;
		}
		
		return $result;
	}
	
	function getCastShareCount($cast_id) {
		$query =  $this->db->select('count(*) as count')
		->from('tb_castrelation')
		->where('castrelationCastID', $cast_id)
		->where('castrelationShare', STATUS_ON)
            ->where('castrelationStatus != '.STATUS_DELETE )
		->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->row();
			$result = $result->count;
	
		} else {
			$result = 0;
		}
	
		return $result;
	}
	
	function createCastRelation($data) {
		$this->db->insert('tb_castrelation', $data);
	}
	
	function modifyCastRelation($id, $data) {
		$this->db->update('tb_castrelation', $data, "id = ".$id);
	}


    /**
     * $data: updated or added data. if update or remove, it has id field.
     *
     * return: $id if not processed, it is invalid id or null.
     **/
    function updateCastComment($data, $mode) {

        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_castcomment', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_REMOVE) {
            $this->removeCastComment($data['id']);
            $ret_id = $data['id'];
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_castcomment', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

	private function removeCastComment($castcomment_id) {

        $this->cast_model->removeCastComment($castcomment_id);
	}
	
	
	function getUserShopRelation($user_id, $shop_id) {
		$query =  $this->db->select('*')
		->from('tb_usershoprelation')
		->where('usershoprelationUserID', $user_id)
		->where('usershoprelationShopID', $shop_id)
            ->where('usershoprelationStatus != '.STATUS_DELETE )
		->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		
		} else {
			$result = null;
		}
		
		return $result;
	}
	
	function getUserJimList($user_id) {
		$query =  $this->db->select('*')
		->from('tb_usershoprelation')
		->where('usershoprelationUserID', $user_id)
		->where('usershoprelationJim', STATUS_ON)
            ->where('usershoprelationStatus != '.STATUS_DELETE )
		->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
		
		} else {
			$result = Array();
		}
		
		return $result;
	}
	
	function createUserShopRelation($data) {
		$this->db->insert('tb_usershoprelation', $data);
	}
	
	function modifyUserShopRelation($shoprelation_id, $data) {
		$this->db->update('tb_usershoprelation', $data, "id = ".$shoprelation_id);
	}
	
	function getUserGCMRegID($user_id)
	{
		$query =  $this->db->select('*')
				->from('tb_user')
					->where('id', $user_id)
					->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		
		} else {
			$result = null;
		}
		return $result;		
	}
	
	function getGCMLogWithParams($user_id, $msg, $type) {
		$query =  $this->db->select('*')
						->from('tb_gcmlog')
							->where('gcmlogContent', $msg)
							->where('gcmlogUserID', $user_id)
							->where('gcmlogType', $type)
                            ->where('gcmlogStatus != '.STATUS_DELETE )
							->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		
		} else {
			$result = null;
		}
		
		return $result;
	}
	
	function getGCMLogList($page_num, $page_item_cnt, $user_id, $type) {
        if($page_num != null && $page_num >= 1) {

            $start_idx = ($page_num - 1)*$page_item_cnt;
            $this->db->limit($page_item_cnt, $start_idx);
        }


        $this->db->order_by("gcmlogPostTime", "desc");
		
		$query =  $this->db->select('*,
									UNIX_TIMESTAMP(
												tb_gcmlog.gcmlogPostTime
											) AS gcmlogPostTime')
							->from('tb_gcmlog')
							->where('gcmlogUserID', $user_id)
							->where('gcmlogType', $type)
                            ->where('gcmlogStatus != '.STATUS_DELETE )
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
	
	function modifyGCMLog($log_id, $data) {
		$this->db->update('tb_gcmlog', $data, "id = ".$log_id);
	}

	function removeGCMLog($log_id) {
        $this->gcm_model->removeGCM($log_id);
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
	
	/**
	 * functions related tb_category
	 * @return NULL*/
	function getCategoryList() {
	
		$query = $this->db->select('*')
                                    ->from('tb_category')
                                    ->where('categoryStatus != '.STATUS_DELETE)
                                    ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
	
		} else {
			$result = Array();
		}
	
		return $result;
	}
	
	/**
	 * functions related tb_location
	 * @return NULL*/
	
	function getLocationListWithCond($strWhere, $cnt) {
		
		if($strWhere != null) {
			$this->db->where($strWhere);
		}
		
		if($cnt != null) {
			$this->db->limit($cnt);
		}
		
		$this->db->order_by('locationName1, locationName2, locationName3, locationName4');
		
		$query =  $this->db->select('*')
		                ->from('tb_location')
                        ->where('locationStatus != '.STATUS_DELETE)
		                ->get();
			
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
				
		} else {
			$result = Array();
		}
		
		return $result;
	}
	
	function getLocationList($search_word, $cnt) {
		
		$result = null;
		if($search_word != null) {

			$arrTempAddress = explode(" ", $search_word);
			$arrAddress = Array();
			$arrAddress[0] = "";
			$arrAddress[1] = "";
			$arrAddress[2] = "";
			$arrAddress[3] = "";
			
			$count = count($arrTempAddress);
			for($i = 0; $i < $count; $i++) {
				$arrAddress[$i] = $arrTempAddress[$i];
			}
			
			// 1, 2, 3, 4
			$strWhere = "INSTR(locationName1,'".$arrAddress[0]."') > 0"
						." AND INSTR(locationName2,'".$arrAddress[1]."') > 0"
						." AND INSTR(locationName3,'".$arrAddress[2]."') > 0"
						." AND INSTR(locationName4,'".$arrAddress[3]."') > 0";
			$result = $this->getLocationListWithCond($strWhere, $cnt);
			
			//1,2,3
			if($result == null) {
				$strWhere = "INSTR(locationName1,'".$arrAddress[0]."') > 0"
							." AND INSTR(locationName2,'".$arrAddress[1]."') > 0"
							." AND INSTR(locationName3,'".$arrAddress[2]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}
			//1,2
			if($result == null) {
				$strWhere = "INSTR(locationName1,'".$arrAddress[0]."') > 0"
							." AND INSTR(locationName2,'".$arrAddress[1]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}			
			// 1,3,4
			if($result == null) {
				$strWhere = "INSTR(locationName1,'".$arrAddress[0]."') > 0"
							." AND INSTR(locationName3,'".$arrAddress[1]."') > 0"
							." AND INSTR(locationName4,'".$arrAddress[2]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}
			
			// 1,4
			if($result == null) {
				$strWhere = "INSTR(locationName1,'".$arrAddress[0]."') > 0"
						    ." AND INSTR(locationName4,'".$arrAddress[1]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}
			
			// 2,3,4
			if($result == null) {
				$strWhere = "INSTR(locationName2,'".$arrAddress[0]."') > 0"
							." AND INSTR(locationName3,'".$arrAddress[1]."') > 0"
							." AND INSTR(locationName4,'".$arrAddress[2]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}
			
			// 2,4
			if($result == null) {
				$strWhere = "INSTR(locationName2,'".$arrAddress[0]."') > 0"
						." AND INSTR(locationName4,'".$arrAddress[1]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}
			
			// 3,4
			if($result == null) {
				$strWhere = "INSTR(locationName3,'".$arrAddress[0]."') > 0"
						." AND INSTR(locationName4,'".$arrAddress[1]."') > 0";
				$result = $this->getLocationListWithCond($strWhere, $cnt);
			}
			
			// 4
//			if($result == null) {
//				$strWhere = "INSTR(locationName4,'".$arrAddress[0]."') > 0";
//				$result = $this->getLocationListWithCond($strWhere, $cnt);
//			}
			
		}
		else {
			$result = $this->getLocationListWithCond($search_word, $cnt);
		}
		
	
		return $result;
	}
	
	
	function getTubeList($tube_name) {
		
		$this->db->order_by("tubeName");
		
		if($tube_name != null) {
			$strWhere = "INSTR(tb_tube.tubeName,'".$tube_name."') > 0";
			$this->db->where($strWhere);
		}
		
		$query =  $this->db->select('*')
		                    ->from('tb_tube')
                            ->where('tubeStatus != '.STATUS_DELETE)
		                    ->get();
		
		if ($query->num_rows() > 0) {
			$result = $query->result_array();

		} else {
			$result = Array();
		}
		
		return $result;
	}
	
	function createQuestion($data) {
		$this->db->insert('tb_question', $data);
	}

    function getLastAdminQuestion($user_id) {
        $this->db->order_by("questionPostTime", "desc");
        $this->db->limit(1);
        $query =  $this->db->select('*')
            ->from('tb_question')
            ->where('questionStatus != '.STATUS_DELETE)
            ->where('questionPostManID', $user_id)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
            $result = $result[0];
        } else {
            $result = null;
        }

        return $result;
    }
	
	/**
	 * functions related tb_image
	 * @return NULL*/
	function  createImage($data){
	
		$this->db->insert('tb_image', $data);
		$query = $this->db->select('*')->from('tb_image')
                                        ->where('tb_image.imageURL', $data['imageURL'])
                                        ->where('imageStatus != '.STATUS_DELETE)
                                        ->get();
	
		if ($query->num_rows() > 0) {
			$result = $query->result_array();
			$result = $result[0];
		} else {
			$result = null;
		}
	
		return $result;
	}
	
	
	function getImage($image_id) {
		$query =  $this->db->select('*')
		                    ->from('tb_image')
		                    ->where('id', $image_id)
                            ->where('imageStatus != '.STATUS_DELETE)
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
            ->where('bannerStatus != '.STATUS_DELETE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        } else {
            $result = Array();
        }

        return $result;
    }

    function createBannerClickLog($banner_id) {
        $logData = Array(
            'bannerclickBannerID' => $banner_id,
            'bannerclickTime' => $this->util->getTime()
        );

        $this->db->insert('tb_bannerclick', $logData);
    }

    /**
     * $page_num: page number. $page_num >= 1
     * $page_cnt: rows per page. if $page_num is null, it is unlimit count. $page_cnt >= 1
     * $banner_id: banner id for banner comment. $banner_id >= 1
     *
     * return: array of banner comment
    **/
    function  getBannerCommentList($page_num, $page_item_cnt, $banner_id) {
        if($page_item_cnt != null && $page_item_cnt >= 1 && $page_num != null && $page_num >= 1) {
            $last_idx = ($page_num - 1) * $page_item_cnt;
            $this->db->limit($page_item_cnt, $last_idx);
        }

        if($banner_id != null && $banner_id >= ADMIN_SQL_VALID_ID_MIN) {
            $this->db->where('tb_bannercomment.bannercommentBannerID', $banner_id);
        }

        $this->db->order_by("bannercommentPostTime", "desc");

        $selectQuery = 'tb_bannercomment.*,
						tb_user.userID AS userID,
						(
							SELECT
								tb_image.imageURL
							FROM
								tb_image
							WHERE
								tb_user.userImgID = tb_image.id and imageStatus != '.STATUS_DELETE.'
						) AS userImgURL,
						';

        $query = $this->db->select($selectQuery)
            ->from('tb_bannercomment')
            ->join('tb_user', 'tb_bannercomment.bannercommentPostManID = tb_user.id and userStatus != '.STATUS_DELETE)
            ->where('bannercommentStatus != '.STATUS_DELETE)
            ->where('bannercommentStatus != '.STATUS_HIDE)
            ->get();

        if ($query->num_rows() > 0) {
            $result = $query->result_array();

        } else {
            $result = Array();
        }

        return $result;
    }


    /**
     * $data: updated or added data. if update or remove, it has id field.
     *
     * return: $id if not processed, it is invalid id or null.
     **/
    function updateBannerComment($data, $mode) {

        $ret_id = null;

        if($mode == MODE_ADD) {
            $this->db->insert('tb_bannercomment', $data);
            $ret_id = $this->db->insert_id();
        }
        else if($mode == MODE_REMOVE) {
           $this->banner_model->removeBannerComment($data['id']);
            $ret_id = $data['id'];
        }
        else if($mode == MODE_UPDATE) {
            $this->db->update('tb_bannercomment', $data, "id = ".$data['id']);
            $ret_id = $data['id'];
        }

        return $ret_id;
    }

    function removeUser($userID) {
        $this->member_model->removeUser($userID);
    }
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */
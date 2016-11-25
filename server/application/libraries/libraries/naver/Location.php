<?php

include_once('simple_html_dom.php');

class Location
{
        private $key = "cdfb231edbe5d2872ddd2e044c92e13d"; // 사용자가 발급받은 오픈API 키 
        private $searchUrl = "http://openapi.naver.com/search"; // 오픈API 호출URL
        private $target = "local";
        private $sort = "vote";
        private $searchWordUrl = 'http://map.naver.com/search2/localSearchSummay.nhn';
        private $searchlocalUrl = 'http://map.naver.com/search2/local.nhn';
        
        private $servername = "localhost";
        private $username = "mimishop1";
        private $password = "mimishop1004";
        private $dbname = "mimishop1";
        private $conn;
        
        /**
         * API 결과를 받아오기 위하여 오픈API 서버에 Request 를 하고 결과를 XML Object 로 반환하는 메소드
         * @return object
         */
        private function query($query)
        {
                $url = sprintf("%s?query=%s&target=%s&sort=%s&key=%s", $this->searchUrl, urlencode($query), $this->target, $this->sort, $this->key);
                $data =file_get_contents($url);
                $xml = simplexml_load_string($data);
                
                return $xml;
        }
        
        private function writeInfoFromQuery($query) {
        	
        	$realData = $this->getBaseInfo($query);
        	$realData = json_decode($realData, true);
        	
        	$retCnt = 0;
        	if($realData != null) {
        		$retData = $this->writeItemIntoDB($realData);
        		
        		if($retData != null) {   
        			$retCnt = 1;
        			//if(false) { // favorite list
        			if(is_array($retData) == true) { // favorite list
	        			$cnt = count($retData);
	        			
	        			for($i = 0; $i < $cnt;$i++) {
	        				$query1 = $query.' '.$retData[$i];
	        				
	        				$favoriteData = $this->getBaseInfo($query1);
	        				$favoriteData = json_decode($favoriteData, true);
	        				
	        				if($favoriteData != null) {
	        					$retData = $this->writeItemIntoDB($favoriteData);
	        					 
	        					if($retData != null) {
	        						$retCnt = $retCnt + 1;
	        					}	
	        				}
	        			}
        			}
        		}	
        	}
        	
        	return $retCnt;
        }
        
        public function getLocationFromQuery($query) {
        	
        	$data = $this->getLocationInfo($query);
        	$retCnt = $this->writeInfoFromQuery($query);
        	
        	// sub items 
        	$searchData = json_decode($data, true);
        	
        	if(array_key_exists ('result', $searchData) == false) {
        		return 0;
        	}
        	
        	$searchData = $searchData['result'];
        	
        	$result = null;
        	if(array_key_exists ('subRegion', $searchData) == true) {
        		if(array_key_exists ('items', $searchData['subRegion']) == true) {
        			$result = $searchData['subRegion']['items'];
        		}
        	}
        	
        	if($result == null) {
        		return $retCnt;
        	}
        	
        	$cnt = count($result);
        	
        	if($cnt == 1) {
        		return $retCnt;
        	}
        	
        	for($i = 0; $i < $cnt;$i++) {
        		$data = $result[$i];
        		
        		if(strcmp(trim($data['query']), trim($query)) == 0) {
        			continue;
        		}
        		
        		$retCnt += $this->getLocationFromQuery($data['query']);
        	}
        	
        	if(true) {
        		usleep(100);
        	}
        	
        	return $retCnt;
        }
        
        public function writeLocationFromNavor($query) {
        	
        	// Create connection
        	$this->conn = new mysqli($this->servername, $this->username, $this->password, $this->dbname);
        	
        	// Check connection
        	if (mysqli_connect_error()) {
        		exit('Connect Error (' . mysqli_connect_errno() . ') '. mysqli_connect_error());
        	}

            mysqli_query($this->conn, "set session character_set_connection=utf8;");
            mysqli_query($this->conn, "set session character_set_results=utf8;");
            mysqli_query($this->conn, "set session character_set_client=utf8;");
        		
        	set_time_limit(0);
        	$retData = Array();
        	
        	$dataLength = $this->getLocationFromQuery($query);
        	
        	$this->conn->close();
	        
	        $retData['msg'] = 'success';
	        $retData['code'] = 0;
	        $retData['result'] = Array();
	        $retData['result']['length'] = $dataLength;
	        $retData['result']['query'] = $query;
	        
	        echo json_encode($retData);
        }
       
        
        public function getBaseInfo($query) {
        	$data = array('sm' => 'hty',
        			'isFirstSearch' => 'true',
        			'menu' => 'location',
        			'query' => $query
        	);
        	 
        	// use key 'http' even if you send the request to https://...
        	$options = array(
        			'http' => array(
        					'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
        					'method'  => 'POST',
        					'content' => http_build_query($data),
        			),
        	);
        	$context  = stream_context_create($options);
        	$result = file_get_contents($this->searchlocalUrl, false, $context);
        	 
        	return $result;
        }
        
        
        public function getLocationInfo($query) {

        	$data = array('sm' => 'hty', 
        			      'isFirstSearch' => 'true', 
        			      'menu' => 'location', 
        			      'query' => $query
        	);
        	
        	// use key 'http' even if you send the request to https://...
        	$options = array(
        			'http' => array(
        					'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
        					'method'  => 'POST',
        					'content' => http_build_query($data),
        			),
        	);
        	
        	$context  = stream_context_create($options);
        	$result = file_get_contents($this->searchWordUrl, false, $context);
        
        	return $result;
        }
         
		public function writeItemIntoDB($data) {
			
			if(array_key_exists ('result', $data) == false) {
				return null;
			}
			
			$result = $data['result'];
			$isSite = false;
			$isAddress = false;
			
			$temp = null;
			if(array_key_exists ('address', $result) == true) {
				$temp = $result['address'];
				$isAddress = true;
			}
			
			if($temp == null) {
				if(array_key_exists ('site', $result) == true) {
					$temp = $result['site'];
					$isSite = true;
				}
			}
			
			$result = $temp;
			if($result == null) {
				return null;
			}
			
			$result =  $result['list'];
			if(count($result) == 0) {
				return null;
			}
			
			$result = $result[0];
			
			$strAddress = '';
			$longtitude = 0;
			$latitude = 0;
		
			if(array_key_exists ('fullAddress', $result) == true) {
				$strAddress = $result['fullAddress'];
			}
			else if(array_key_exists ('address', $result) == true) {
				$strAddress = $result['address'];
			}
			else {
				$strAddress = null;
			}
			
			if($strAddress == null) {
				return null;
			}
			
			$longtitude = $result['x'];
			$latitude = $result['y'];
			$arrTempAddress = explode(" ", $strAddress);
			$arrAddress = Array();
			$arrAddress[0] = "";
			$arrAddress[1] = "";
			$arrAddress[2] = "";
			$arrAddress[3] = "";
		
			$count = count($arrTempAddress);
			for($i = 0; $i < $count; $i++) {
				$arrAddress[$i] = $arrTempAddress[$i];
			}
			
			if($isSite == true) {
				$arrAddress[3] = $result['name'];
			}
			
		
			$sql = "SELECT id from tb_location 
					where STRCMP(locationName1, '".$arrAddress[0]."') = 0 AND STRCMP(locationName2,'".$arrAddress[1]."') = 0 
					      AND STRCMP(locationName3, '".$arrAddress[2]."') = 0 AND STRCMP(locationName4,'".$arrAddress[3]."') = 0";
			
			$modify = false;
			$locationID = 0;
			if ($resultSQL = $this->conn->query($sql)) {
				$row = $resultSQL->fetch_object();
				
				if($row != null) {
					$modify = true;
					$locationID =  $row->id;
				}
				
				$resultSQL->close();
			}
	
			
			$sql = "INSERT INTO tb_location (locationName1, locationName2, locationName3, locationName4,locationLongtitude, locationLatitude)
					 			VALUES ('"
								.$arrAddress[0]."','"
								.$arrAddress[1]."','"
								.$arrAddress[2]."','"
								.$arrAddress[3]."','"
								.$longtitude."','"
								.$latitude."')";
			
			if($modify == true) {
					$sql = "UPDATE tb_location SET "
									."locationLongtitude='".$longtitude."',"
									."locationLatitude='".$latitude."'"
									." WHERE id='".$locationID."'";
			}
			
			
			if ($this->conn->query($sql) == FALSE) {
				echo ( "Error: " . $sql . "<br>" . $this->conn->error);
                return;
			}
			
			$arrFavorite = null;
			if(array_key_exists ('favoriteList', $result) == true) {
				$arrFavorite =  $result['favoriteList'];
				
				if($arrFavorite != null) {
					return $arrFavorite;
				}
			}
			
			return 1;
		}
   }
?>
<?php
/**
 * Created by PhpStorm.
 * User: KCJ
 * Date: 8/6/2015
 * Time: 4:56 PM
 */

class Util {
    function __construct() {
        $ci =& get_instance();

        if(ADMIN_DEBUG_MODE) { // testing
            error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
        }

        $ci->load->helper ( 'url' );
        date_default_timezone_set('Asia/Seoul');
    }

    public static function doRespond($controller, $p_result_code, $p_result_msg, $p_result) {

        if($controller == null) {
            return;
        }

        $w_result = array ();
        $w_result ['result_code'] = $p_result_code;
        $w_result ['result_msg'] = $p_result_msg;
        $w_result ['result_data'] = $p_result;
        $controller->output->set_content_type ( 'application/json' )->set_output ( json_encode ( $w_result ) );
    }

    function getMilliTime() {
        $milliseconds = round(microtime(true) * 1000);
        return $milliseconds;
    }

    function getTime() {

        $now = new DateTime();

        return  $now->format('Y-m-d H:i:s');
    }

    function getDate() {
        $now = new DateTime();

        return  $now->format('Y-m-d');
    }

    // function to get  the address
    function get_lat_long($address){

        $address = str_replace(" ", "+", $address);

        $json = file_get_contents("http://maps.google.com/maps/api/geocode/json?address=$address&sensor=false&region=Asia/Seoul");
        $json = json_decode($json);

        $lat = $json->{'results'}[0]->{'geometry'}->{'location'}->{'lat'};
        $long = $json->{'results'}[0]->{'geometry'}->{'location'}->{'lng'};
        return $lat.','.$long;
    }

    function getBaseURL() {
        return  $this->getDomainURL();
    }

    function getDomainURL() {
        return "http://www.miggle.co.kr";
    }

    function  getUserFilteredLocation($location) {
        $arrFilterLocation3 = array("서울시", "부산시", "광주시", "인천시",  "울산시",  "세종시", "대구시","제주도");
        $arrFilterLocation2 = array("서울특별시", "부산광역시", "광주광역시", "인천광역시",  "울산광역시",  "세종특별자치시", "대구광역시","제주특별자치도");
        $arrFilterLocation1 = array("서울", "부산", "광주", "인천",  "울산",  "세종시", "대구","제주도");

        $return = explode(" ", $location);

        if($return == 1) {
            $location = str_replace($arrFilterLocation3, $arrFilterLocation1, $location);
        }
        else {
            for($i = 0; $i < count($arrFilterLocation2); $i++) {
                if(strcmp($arrFilterLocation2[$i], $return[0]) == 0 || strcmp($arrFilterLocation3[$i], $return[0]) == 0) {
                    $return[0] = $arrFilterLocation1[$i];
                }
            }
            $location = implode(" ", $return);
        }

        return $location ;
    }

    function  getShopFilteredLocation($location) {
        $arrFilterLocation3 = array("서울시", "부산시", "광주시", "인천시",  "울산시",  "세종시", "대구시","제주도");
        $arrFilterLocation2 = array("서울", "부산", "광주", "인천",  "울산",  "세종시", "대구","제주도");
        $arrFilterLocation1 = array("서울특별시", "부산광역시", "광주광역시", "인천광역시",  "울산광역시",  "세종특별자치시", "대구광역시","제주특별자치도");

        $return = explode(" ", $location);

        if($return == 1) {
            $location = str_replace($arrFilterLocation3, $arrFilterLocation1, $location);
        }
        else {
            for($i = 0; $i < count($arrFilterLocation2); $i++) {
                if(strcmp($arrFilterLocation2[$i], $return[0]) == 0 || strcmp($arrFilterLocation3[$i], $return[0]) == 0) {
                    $return[0] = $arrFilterLocation1[$i];
                }
            }
            $location = implode(" ", $return);
        }

        return $location ;
    }
}

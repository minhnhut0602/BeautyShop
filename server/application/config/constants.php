<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/*
|--------------------------------------------------------------------------
| File and Directory Modes
|--------------------------------------------------------------------------
|
| These prefs are used when checking and setting modes when working
| with the file system.  The defaults are fine on servers with proper
| security, but you may wish (or even need) to change the values in
| certain environments (Apache running a separate process for each
| user, PHP under CGI with Apache suEXEC, etc.).  Octal values should
| always be used to set the mode correctly.
|
*/
define('FILE_READ_MODE', 0644);
define('FILE_WRITE_MODE', 0666);
define('DIR_READ_MODE', 0755);
define('DIR_WRITE_MODE', 0777);

/*
|--------------------------------------------------------------------------
| File Stream Modes
|--------------------------------------------------------------------------
|
| These modes are used when working with fopen()/popen()
|
*/

define('FOPEN_READ',							'rb');
define('FOPEN_READ_WRITE',						'r+b');
define('FOPEN_WRITE_CREATE_DESTRUCTIVE',		'wb'); // truncates existing file data, use with care
define('FOPEN_READ_WRITE_CREATE_DESTRUCTIVE',	'w+b'); // truncates existing file data, use with care
define('FOPEN_WRITE_CREATE',					'ab');
define('FOPEN_READ_WRITE_CREATE',				'a+b');
define('FOPEN_WRITE_CREATE_STRICT',				'xb');
define('FOPEN_READ_WRITE_CREATE_STRICT',		'x+b');

define('APP_HEADER',		'mimishop-api-header');
define('DEVICE_ID',			'Device-id');
define('DEVICE_MODEL',		'Device-model');
define('OS_TYPE',			'Os-type');
define('OS_VERSION',		'Os-version');
define('APP_VERSION',		'App-version');
define('APP_MARKET',		'App-market');
define('APP_KEY',			'App-key');

define('MAX_RETURN_CNT',	10);
define('MAX_PAGE_ITEM_CNT',	10);
define('MAX_SEARCH_CNT',	10);
define('MAX_RETURN_SHOP_CNT',	50);

// for user
define('IDX_POINT_TIP',	     0);
define('IDX_POINT_WRITE',	 1);
define('IDX_POINT_REMARK',	 2);
define('IDX_POINT_QUESTION', 3);
define('IDX_POINT_COMMENT',	 4);
define('IDX_POINT_LIKE',	 5);
define('IDX_POINT_SNS',	     6);

define('USER_LEVEL0_POINT',	0);
define('USER_LEVEL1_POINT',	100);
define('USER_LEVEL2_POINT',	1000);
define('USER_LEVEL3_POINT',	5000);
define('USER_LEVEL4_POINT',	10000);
define('USER_LEVEL5_POINT',	50000);
define('USER_MAX_LEVEL',	5);

define('USER_SEX_MAN',	'M');
define('USER_SEX_WOMAN',	'W');

define('STATUS_LIMIT_USER', 1);
define('STATUS_STOP_USER', 2);
define('STATUS_STOP_LIMIT_USER', 3);

define('GCM_LOG_COMMENT',		1);
define('GCM_LOG_TAG',		2);
define('GCM_LOG_ENVELOP',		3);

define('STATUS_OFF', 0);
define('STATUS_ON', 1);
define('STATUS_YES', 'Y');
define('STATUS_NO', 'N');

// for shop
define('LIMIT_LOW_SHOP_LEVEL', 2);
define('ID_NAIL_CATEGORY', 1);
define('ID_HAIR_CATEGORY', 2);
define('ID_SOKNUN_CATEGORY', 15);
define('ID_WAKSING_CATEGORY', 16);
define('ID_SKIN_CATEGORY', 17);
define('ID_MASSAGE_CATEGORY', 18);
define('ID_BANYONGGU_CATEGORY', 19);
define('ID_TATU_CATEGORY', 20);
define('LIMIT_CONSEQUE', 99999999);
define('MAX_SHOP_IMG_CNT',	20);

define('TYPE_SHOPCOMMENT_WRONGINFO', 'WI');
define('TYPE_SHOPCOMMENT_QUESTIONANSWER', 'QA');
define('TYPE_SHOPCOMMENT_USERREMARK', 'UR');

define('STATUS_LIFE', 0);           // 생존
define('STATUS_DELETE', -1);        // 완전 삭제

define('STATUS_VISIBLE', 4);        // 어플에서 보임
define('STATUS_HIDE', 5);           // 어플에서 숨김

define('STATUS_REQUEST', 1);        //신청중
define('STATUS_MANAGE', 2);         //운영
define('STATUS_NON_MANAGE', 3);     //관리자가 0이면 비운영

define('LIMIT_SHOP_LIVEDATE', 365);

define('IMAGE_LOCATION_SHOP', '0');
define('IMAGE_LOCATION_FREETALK', '1');
define('IMAGE_LOCATION_CAST', '2');
define('IMAGE_LOCATION_EVENT', '3');
define('IMAGE_LOCATION_USER', '4');
define('IMAGE_LOCATION_OTHER', '5');

define('MSG_ERR_SQL',	        	'자료기지처리 실패');
define('MSG_ERR_INPUT_INVALID',		'필수데이터 루락');
define('MSG_FAIL',					'실패');
define('MSG_SUCCESS',				'성공');
define('MSG_NO_DATA',				'자료가 없습니다.');
define('MSG_TAGGED',				'이미 태그하셨습니다.');
define('MSG_DECLARRED',				'이미 신고하셨습니다.');
define('MSG_DUPLICATE_JIM',			'이미 찜한 숍입니다.');
define('MSG_REGISTTERED_SHOP',		'이미 등록한 숍입니다.');
define('MSG_YOUR_SHOP',				'당신의 숍입니다.');
define('MSG_YOUR_WRITING',			'당신의 글입니다.');
define('MSG_NOT_YOUR_WRITING',		'당신의 글이 아닙니다.');
define('MSG_LIMIT_LIVE_DATE',		'운영기간이 지난 숍입니다.');
define('MSG_WELCOME', 				'미미샵에 오신것을 환영합니다.');
define('MSG_DUPLICATE_BEST_CAST',	'이미 베스트 게시물입니다.');
define('MSG_FAIL_SET_BEST',			'베스트게시물의 개수가 넘쳐납니다.');


define('MSG_SHOP_COMMENT_WI_0', 	'상점이 페업했어요.');
define('MSG_SHOP_COMMENT_WI_1', 	'상점이름이 잘못되었어요.');
define('MSG_SHOP_COMMENT_WI_2', 	'주소가 잘못되었어요.');
define('MSG_SHOP_COMMENT_WI_3', 	'전화번호가 잘못되었어요.');
define('MSG_SHOP_COMMENT_WI_4', 	'영업시간이 잘못되었어요.');
define('MSG_SHOP_COMMENT_WI_5', 	'할인정보가 잘못되었어요.');

// for admin
define('PAGE_ITEM_CNT_BANNER_COMMENT',	20);
define('PAGE_ITEM_CNT_FREETALK_COMMENT',	20);
define('PAGE_ITEM_CNT_FREETALK',	10);
define('PAGE_ITEM_CNT_CAST',	20);
define('PAGE_ITEM_CNT_CAST_COMMENT',	10);
define('PAGE_ITEM_CNT_ENVELOP',	10);
define('PAGE_ITEM_CNT_LIFE',	10);
define('PAGE_ITEM_CNT_FREETALK_COMMENT_TAG',	20);
define('PAGE_ITEM_CNT_SHOP_COMMENT',	20);
define('PAGE_ITEM_CNT_SHOP_EVENT',	20);
define('PAGE_ITEM_CNT_SHOP_PRODUCT',	20);

define('ADMIN_SQL_VALID_ID_MIN', 1);
define('ADMIN_ID',	0);
define('ADMIN_MAX_PAGE_ITEM_CNT',	10);
define('ADMIN_IMAGE_ROW_CNT',	3);
define('ADMIN_IMAGE_COL_CNT',	4);
define('ADMIN_EXIT_ADS_ID',	1);
define('ADMIN_MAIN_ADS_START_ID',	2);
define('ADMIN_MAIN_ADS_CNT',	3);
define('ADMIN_LIFE_ADS_ID',	5);
define('ADMIN_CAST_ADS_CNT',	3); // 캐스트 맨 우에서부터 3개를 꼽는다.
define('ADMIN_TYPE_MAIN_NORMAL',	0);
define('ADMIN_TYPE_MAIN_BANNER',	1);
define('ADMIN_TYPE_CAST_BANNER',	2);
define('ADMIN_TYPE_LOGOUT_BANNER',	3);
define('ADMIN_BANNER_SHOW_MODE_URL',	0);
define('ADMIN_BANNER_SHOW_MODE_DETAIL',	1);

define('ADMIN_TYPE_ADMIN_QUESTION_NORMAL',	0);
define('ADMIN_TYPE_ADMIN_QUESTION_SHOP_PASSWORD',	1);

define('ADMIN_DEBUG_MODE',	true);

define('MODE_ADD', 0);
define('MODE_UPDATE', 1);
define('MODE_REMOVE', 2);

// admin tab id
define('ADMIN_TAB_ID_MEMEBER',	0);
define('ADMIN_TAB_ID_SHOP',	1);
define('ADMIN_TAB_ID_CAST',	2);
define('ADMIN_TAB_ID_TALK',	3);
define('ADMIN_TAB_ID_LIFE',	4);
define('ADMIN_TAB_ID_PUSH',	5);
define('ADMIN_TAB_ID_NOTICE',	6);
define('ADMIN_TAB_ID_SHOPSEQUENCE',	7);
define('ADMIN_TAB_ID_NAVOR',	8);
define('ADMIN_TAB_ID_MONITOR',	9);
define('ADMIN_TAB_ID_BANNER',	10);
define('ADMIN_TAB_ID_LOGOUT',	11);

define('ADMIN_CNT_BEST_SHOP',	1);
define('ADMIN_CNT_BEST_LIFE',	2);
define('ADMIN_CNT_BEST_CAST',	2);
define('ADMIN_CNT_BEST_FREETALK',	2);
define('ADMIN_CNT_BEST_CAST_COMMENT',	2);

/* End of file constants.php */
/* Location: ./application/config/constants.php */

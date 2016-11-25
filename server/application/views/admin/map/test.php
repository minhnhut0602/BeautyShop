<?php
	include_once('simple_html_dom.php');
$url = 'http://map.naver.com/local/siteview.nhn?code=31830743';

$result = file_get_html($url);

// get time & rest
$arrTime = Array();
foreach($result->find('dd[class="section_detail_time"]') as $timeHtml) {
	$timeHtml = $timeHtml->find('ul', 0);
	foreach($timeHtml->find('li') as $timeStr) {
		$openTimeDay = $timeStr->find('strong', 0)->innertext;
		$restTime = $timeStr->find('span', 0)->innertext;
	
		$index = strrpos($restTime, " ");
	
		$openTime = substr ($restTime, 0, $index);
		
		$restTime = substr ($restTime, $index+1);
		
		$newData = Array();
		$newData['openTime'] = $openTime;
		$newData['openTimeDay'] = $openTimeDay;
		if($restTime == null) {
			$newData['restTime'] = "";
		}
		else {
			$newData['restTime']= $restTime;
		}
		
		array_push($arrTime, $newData);
	}
}

echo json_encode($arrTime);

// get parkable
$parkable = $result->find('span[class="spm spm_info2"]');
if($parkable != null) {
	$parkable = 'Y';
	echo '주차가능';
}
else {
	$parkable = 'N';
	echo '주차불가능';
}

// get price list
$arrProduct = Array();
foreach($result->find('dd[class="ldi section_detail_pay"]') as $priceHtml) {
	$productHtml = $priceHtml->find('ul', 0);
	foreach($productHtml->find('li') as $productStr) {
		$productName = $productStr->find('strong', 0)->innertext;
		$priceStr = $productStr->find('em', 0)->find('strong', 0)->innertext;

		$priceStr = mb_substr($priceStr, 0, (strlen($priceStr) - 3), 'UTF-8');
		
		$priceStr = str_replace (",", "", $priceStr);
		
		$newData = Array();
		$newData['productName'] = $productName;
		$newData['productPrice'] = (int)$priceStr;
	
		array_push($arrProduct, $newData);
	}
}

echo json_encode($arrProduct);

// get image list
$arrImage = Array();
foreach($result->find('ul[class="sp_thumb_lst"]') as $imageListHtml) {
	foreach($imageListHtml->find('li') as $imageHtml) {
		$imageLinkHtml = $imageHtml->find('a', 0);
		$imageStr = $imageLinkHtml->find('img', 0);
		array_push($arrImage, $imageStr->src);
	}
}

echo json_encode($arrImage);

?>
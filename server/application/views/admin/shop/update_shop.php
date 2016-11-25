<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>상점관리</title>
    <link href="<?php echo base_url(); ?>assets/css/admin/global.css" rel="stylesheet" type="text/css">
    <link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap-datepicker3.css" rel="stylesheet" type="text/css">
    <style type="text/css">

        ::selection{ background-color: #E13300; color: white; }
        ::moz-selection{ background-color: #E13300; color: white; }
        ::webkit-selection{ background-color: #E13300; color: white; }

        body {
            background-color: #fff;
            margin: 40px;
            font: 13px/20px normal Helvetica, Arial, sans-serif;
            color: #4F5155;
        }

        a {
            color: #003399;
            background-color: transparent;
            font-weight: normal;
        }

        h1 {
            color: #444;
            background-color: transparent;
            border-bottom: 1px solid #D0D0D0;
            font-size: 19px;
            font-weight: normal;
            margin: 0 0 14px 0;
            padding: 14px 15px 10px 15px;
        }

        code {
            font-family: Consolas, Monaco, Courier New, Courier, monospace;
            font-size: 12px;
            background-color: #f9f9f9;
            border: 1px solid #D0D0D0;
            color: #002166;
            display: block;
            margin: 14px 0 14px 0;
            padding: 12px 10px 12px 10px;
        }

        #body{
            margin: 0 15px 0 15px;
        }

        p.footer{
            text-align: right;
            font-size: 11px;
            border-top: 1px solid #D0D0D0;
            line-height: 32px;
            padding: 0 10px 0 10px;
            margin: 20px 0 0 0;
        }

        #container{
            margin: 10px;
            border: 1px solid #D0D0D0;
            -webkit-box-shadow: 0 0 8px #D0D0D0;
        }

        #submit_container {
            width: 400px;
            height: 30px;
            text-align:center;
        }
        #btn_transfer {
            width: 70px;
            height: 30px;
        }
    </style>

</head>

<body onunload="onExit()" onload="onLoad()">

<div id="container"  value="<?php echo $shopInfo['id']?>">

    <div class="row">
        <div class="col-md-6">
             <table class="table table-bordered table-scrollable">

                <tbody>
                    <tr>
                        <td class="base-table-header">상점명</td>
                        <td><input id="shopName" type="text" name="shopName" size="12" value='<?php echo $shopInfo['shopName'] ?>'/></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">전화번호</td>
                        <td><input id="shopPhonenumber" type="text" name="shopPhonenumber" size="12" value='<?php echo $shopInfo['shopPhonenumber'] ?>'/></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">주소</td>
                        <td><input id="shopAddress" type="text" name="shopAddress" size="12" value='<?php echo $shopInfo['shopAddress'] ?>' style="width: 98%;margin: 3px;"/></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">카테고리</td>
                        <td>
                            <select class="combobox" id="shopCategory">
                                <?php
                                if(strcmp($shopInfo['shopCategoryName'], '헤어') == 0) {
                                    echo '<option value="2" selected> 헤어샵 </option>';
                                }
                                else {
                                    echo '<option value="2"> 헤어샵 </option>';
                                }
                                if(strcmp($shopInfo['shopCategoryName'], '네일') == 0) {
                                    echo '<option value="1" selected> 네일샵 </option>';
                                }
                                else {
                                    echo '<option value="1"> 네일샵 </option>';
                                }
                                if(strcmp($shopInfo['shopCategoryName'], '속눈썹연장') == 0) {
                                    echo '<option value="15" selected> 속눈썹연장 </option>';
                                }
                                else {
                                    echo '<option value="15"> 속눈썹연장 </option>';
                                }
                                if(strcmp($shopInfo['shopCategoryName'], '왁싱') == 0) {
                                    echo '<option value="16" selected> 왁싱 </option>';
                                }
                                else {
                                    echo '<option value="16"> 왁싱 </option>';
                                }                                
                                if(strcmp($shopInfo['shopCategoryName'], '피부') == 0) {
                                    echo '<option value="17" selected> 피부 </option>';
                                }
                                else {
                                    echo '<option value="17"> 피부 </option>';
                                } 
                                if(strcmp($shopInfo['shopCategoryName'], '마사지') == 0) {
                                    echo '<option value="18" selected> 마사지 </option>';
                                }
                                else {
                                    echo '<option value="18"> 마사지 </option>';
                                }                                                                
                                ?>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="base-table-header">상태</td>
                        <td>
                            <select id="shopStatus" value='<?php echo $shopInfo['shopStatus'] ?>'>
                                <?php
                                if($shopInfo['shopStatus'] == STATUS_MANAGE) {
                                    echo '<option value="'.STATUS_MANAGE.'" selected> 운영 </option>';
                                }
                                else {
                                    echo '<option value="'.STATUS_MANAGE.'"> 운영 </option>';
                                }
                                if($shopInfo['shopStatus'] == STATUS_NON_MANAGE) {
                                    echo '<option value="'.STATUS_NON_MANAGE.'" selected> 비운영 </option>';
                                }
                                else {
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                if($shopInfo['shopStatus'] == STATUS_REQUEST) {
                                    echo '<option value="'.STATUS_REQUEST.'" selected> 신청중 </option>';
                                }
                                else {
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">노출여부</td>
                        <td>
                            <select id="shopVisibility" value='<?php echo $shopInfo['shopVisibility'] ?>'>
                                <?php
                                if($shopInfo['shopVisibility'] == STATUS_VISIBLE) {
                                    echo '<option value="'.STATUS_VISIBLE.'" selected> 노출 </option>';
                                }
                                else {
                                    echo '<option value="'.STATUS_VISIBLE.'"> 노출 </option>';
                                }
                                if($shopInfo['shopVisibility'] == STATUS_HIDE) {
                                    echo '<option value="'.STATUS_HIDE.'" selected> 비노출 </option>';
                                }
                                else {
                                    echo '<option value="'.STATUS_HIDE.'"> 비노출 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">이벤트</td>
                        <td>
                            <select id="shopEventable" value='<?php echo $shopInfo['shopEventable'] ?>'>
                                <?php
                                if($shopInfo['shopEventable'] ==  'Y') {
                                    echo '<option value="Y" selected> 진행 </option>';
                                }
                                else {
                                    echo '<option value="Y"> 진행 </option>';
                                }
                                if($shopInfo['shopEventable'] ==  'N') {
                                    echo '<option value="N" selected> 중지 </option>';
                                }
                                else {
                                    echo '<option value="N"> 중지 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">문의글 운영상태</td>
                        <td>
                            <select id="shopQuestionable" value='<?php echo $shopInfo['shopQuestionable'] ?>'>
                                <?php
                                if($shopInfo['shopQuestionable'] == 'Y') {
                                    echo '<option value="Y" selected> 진행 </option>';
                                }
                                else {
                                    echo '<option value="Y"> 진행 </option>';
                                }
                                if($shopInfo['shopQuestionable'] ==  'N') {
                                    echo '<option value="N" selected> 중지 </option>';
                                }
                                else {
                                    echo '<option value="N"> 중지 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="base-table-header">운영기간</td>
                        <td><input id="shopLiveDate" type="text" name="shopLiveDate" size="12" value='<?php echo $shopInfo['shopLiveDate'] ?>'>최대 365일</input></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">별점(평점)</td>
                        <td><?php echo intval($shopInfo['shopRemarkLevel'])?></td>
                    </tr>
                    <tr>
                        <td class="base-table-header" onclick="shopReview(<?php echo $shopInfo['id'] ?>)" style="cursor: pointer;">리뷰</td>
                        <td><?php echo $shopInfo['shopReviewCnt'] ?></td>
                    </tr>
                    <tr>
                        <td class="base-table-header" onclick="shopQuestion(<?php echo $shopInfo['id'] ?>)" style="cursor: pointer;">문의갯수</td>
                        <td><?php echo $shopInfo['shopQuestionCnt'] ?></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">콜수/총</td>
                        <td><?php echo $shopInfo['shopCallCnt'] ?></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">클릭수/총</td>
                        <td><?php echo $shopInfo['shopClickCnt'] ?></td>
                    </tr>

                    <td class="base-table-header">사업자 등록증</td>
                    <td>
                      <form method="post" enctype="multipart/form-data" id="shopManagerIdentyImgForm">
                            <Label id="shopManagerIdentyImgLabel" style="margin-left: 0px;border-style: solid;border-width: 1px;"><?php echo $shopInfo['shopManagerIdentyImgURL'];?></Label>
                            <input id="shopManagerIdentyImg" name = "shopManagerIdentyImg" type="file" value="<?php echo $shopInfo['shopManagerIdentyImgID'];?>" style="font-size:12pt; color:#000000;margin-top: 3px;"/>
                            <div style="margin-top: 3px;">
                                <input type="submit" value="등록"/>
                                <input type="button" onclick="onRemoveShopManagerIdentyImgClick()" value="삭제"/>
                            </div>
                        </form>
                    </td>
                    	
                    <tr>	
	                    <td class="base-table-header">영업시간</td>	
	                    <td><input id="shopOpenTime" type="text" name="shopOpenTime" size="12" value='<?php echo $shopInfo['shopOpenTimeString'] ?>' style="width: 98%;margin: 3px;"/></td>
                    </tr>
                    
                    <tr>
	                    <td class="base-table-header">휴무</td>	                    	
	                    <td><input id="shopRestTime" type="text" name="shopRestTime" size="12" value='<?php echo $shopInfo['shopRestTimeString'] ?>' style="width: 98%;margin: 3px;"/></td>
					</tr>
						
					<tr>
	                    <td class="base-table-header">주차</td>	
	                    <td>
	                        <select id="shopParkable" value='<?php echo $shopInfo['shopParkable'] ?>'>
	                            <?php
	                            if($shopInfo['shopParkable'] == 'Y') {
	                                echo '<option value="Y" selected> 가능 </option>';
	                            }
	                            else {
	                                echo '<option value="Y"> 가능 </option>';
	                            }
	                            if($shopInfo['shopParkable'] ==  'N') {
	                                echo '<option value="N" selected> 불가능 </option>';
	                            }
	                            else {
	                                echo '<option value="N"> 불가능 </option>';
	                            }
	                            ?>
	                        </select>
	                    </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">상세설명</td>
                        <td id="td_shop_description"> <textarea class="form-control" rows="5" id="ta_shop_description" style="text-align:left;margin-top: 5px;"><?php echo $shopInfo['shopDescriptionString']?></textarea> </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">찾아오시는 길</td>
                        <td id="td_shop_road"> <textarea class="form-control" rows="5" id="ta_shop_road" style="text-align:left;margin-top: 5px;"><?php echo $shopInfo['shopRoad']?></textarea> </td>
                    </tr>
                </tbody>
            </table>
            <div class="container" id="submit_container" style="margin-bottom: 15px;">
                <?php
                echo '<button type="button" class="btn btn-default" onclick="saveInfo('.$shopInfo['id'].
                    ')" id="btn_transfer" style="width:auto;"> <span aria-hidden="true"> 글저장 </span></button>';
                ?>
                <button type="button" class="btn btn-default" style="margin: 5px" onclick="showProductList(<?php echo $shopInfo['id']?>)"> 상품등록 및 삭제 </button>
            </div>
        </div>

        <div class="col-md-6">
            <table  class="table table-bordered table-scrollable">
                <tbody>
                    <tr>
                        <td class="base-table-header">이벤트제목</td>
                        <td><input id="shopEventTitle" type="text" name="shopEventTitle" size="10" value='<?php echo $shopInfo['shopEvent']['eventSummary'] ?>'  style="font-size:12pt; color:#000000; width: 320px"/></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">이벤트내용</td>
                        <td><textarea class="form-control" id="shopEventContent"  rows="5" id="shopEventContent"  style="font-size:10pt; color:#000000; width: 320px;height: 100px"/><?php echo $shopInfo['shopEvent']['eventContent'] ?></textarea></td>
                    </tr>
                    <tr>
                        <td class="base-table-header">이벤트 기간설정</td>
                        <td>
                            <!--<div  id="from-date-container" style="width: 100%;">-->
                                <table>
                                    <tbody>
                                    <tr>            							
                                        <td>
                                        	<div  id="from-date-container" style="width: 100%;">
                                        	<input type="text" type="text" class="form-control" value="<?php echo $shopInfo['shopEvent']['eventStart']?>" id="fromDate"/>
                                        	</div>
                                        </td>
                                        <td><label> ~ </label></td>
                                        <td>
                                        	<div  id="to-date-container" style="width: 100%;">
                                        	<input type="text" type="text" class="form-control"  value="<?php echo $shopInfo['shopEvent']['eventEnd']?>" id="toDate"/>
                                        	</div>
            							</td>            						
            							<td><label></label></td>
            							<?php 
            								if($shopInfo['shopEvent']['eventEmoticon'] == 1) 
            									echo '<td><input type="checkbox" id="checked" name="checked" checked="checked"/></td>';
        									else 
        										echo '<td><input type="checkbox" id="checked" name="checked" /></td>';
            							?>
            							<td><label>  이벤트 이모티콘</label></td>
                                    </tr>
                                    </tbody>
                                </table>
                            <!--</div>            				-->
                        </td>
                    </tr>
                    <tr>
                        <td class="base-table-header">이벤트 배너</td>
                        <td>
                            <form method="post" enctype="multipart/form-data" id="eventBannerUploadForm">
                                <Label id="eventBannerImgLabel" style="margin-left: 0px;border-style: solid;border-width: 1px;"><?php echo $shopInfo['shopEvent']['eventImgURL'];?></Label>
                                <input id="eventBannerImg" name = "eventBannerImg" type="file" value="<?php echo $shopInfo['shopEvent']['eventImgID'];?>" style="font-size:12pt; color:#000000;margin-top: 3px;"/>
                                <div style="margin-top: 3px;">
                                    <input type="submit" value="등록"/>
                                    <input type="button" onclick="onRemoveEventBannerImgClick()" value="삭제"/>
                                </div>
                            </form>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="container" id="event_button_container" value = "<?php echo $shopInfo['shopEvent']['id']?>">
                <button type="button" class="btn btn-default" style="margin: 5px" onclick="registerEvent(<?php echo $shopInfo['id']?>)"> 이벤트등록 </button>
            </div>
            </div>
        <div>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/jquery.form.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script type="text/javascript">

    $('#from-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });
    $('#to-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });

    

    function onExit() {
        try {
            window.opener.HandlePopupResult();
        }
        catch (err) {}

    }

    function onLoad() {
        var i = 0;
        while(i < 20) {
            var formName = '#imgUploadForm'+(i+1);
            $(formName)
                .ajaxForm({
                    url : getBaseURL()+'/admin/upload_image?kind=shop&file_name=shopImg'+(i+1)+'&index='+(i+1), // or whatever
                    dataType : 'json',
                    success : function (response) {
                        var code = response['result_code'];
                        if(code == 0) {
                            var ret_id = response['result_data']['index'];
                            var var_name = "shopImg"+ret_id;
                            document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                            var input_name = '#shopImgLabel'+(ret_id);
                            $(input_name).text(response['result_data']['imageURL']);
                        }
                        else {
                            alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
                        }
                    }
                })
            ;
            i++;
        }

        $("#eventBannerUploadForm").ajaxForm({
                url : getBaseURL()+'/admin/upload_image?kind=event&file_name=eventBannerImg',
                dataType : 'json',
                success : function (response) {
                    var code = response['result_code'];
                    if(code == 0) {
                        var var_name = "eventBannerImg";
                        document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                        var input_name = '#eventBannerImgLabel';
                        $(input_name).text(response['result_data']['imageURL']);
                    }
                    else {
                        alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
                    }
                }
            })
        ;

        $("#shopManagerIdentyImgForm").ajaxForm({
            url : getBaseURL()+'/admin/upload_image?kind=shop&file_name=shopManagerIdentyImg',
            dataType : 'json',
            success : function (response) {
                var code = response['result_code'];
                if(code == 0) {
                    var var_name = "shopManagerIdentyImg";
                    document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                    var input_name = '#shopManagerIdentyImgLabel';
                    $(input_name).text(response['result_data']['imageURL']);
                }
                else {
                    alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
                }
            }
        })
        ;
    }

    function onRemoveEventBannerImgClick() {
        $("#eventBannerImgLabel").text("");

        var var_name = "eventBannerImg";
        document.getElementById(var_name).setAttribute("value", "0");
    }

    function onRemoveShopManagerIdentyImgClick() {
        $("#shopManagerIdentyImgLabel").text("");

        var var_name = "shopManagerIdentyImg";
        document.getElementById(var_name).setAttribute("value", "0");
    }

    function saveInfo(shop_id) {
        var shopName = document.getElementById("shopName").value;
        var shopPhonenumber = document.getElementById("shopPhonenumber").value;
        var shopAddress = document.getElementById("shopAddress").value;
        var shopCategory = document.getElementById("shopCategory").value;
        var shopStatus = document.getElementById("shopStatus").value;
        var shopEventable = document.getElementById("shopEventable").value;
        var shopQuestionable = document.getElementById("shopQuestionable").value;
        var shopLiveDate = parseInt(document.getElementById("shopLiveDate").value);
        var shopManagerIdentyImgID = parseInt(document.getElementById("shopManagerIdentyImg").getAttribute("value"));
        var shopVisibility = document.getElementById("shopVisibility").value;

		var shopOpenTime = document.getElementById("shopOpenTime").value;
		var shopRestTime = document.getElementById("shopRestTime").value;
		var shopParkable = document.getElementById("shopParkable").value;
		var ta_shop_description = document.getElementById("ta_shop_description").value;
		var ta_shop_road = document.getElementById("ta_shop_road").value;
			
        if(isNaN(shopManagerIdentyImgID) == true) {
            shopManagerIdentyImgID = null;
        }

        $.ajax({
            url: "update_shop_info",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                shop_id: shop_id,
                shop_category_id:shopCategory,
                shop_status:shopStatus,
                shop_eventable:shopEventable,
                shop_live_date:shopLiveDate,
                shop_questionable:shopQuestionable,
                shop_address:shopAddress,
                shop_phonenumber:shopPhonenumber,
                shop_visibility:shopVisibility,
                shop_manageridenty_imgid:shopManagerIdentyImgID,
                shop_name:shopName,
                shop_road:ta_shop_road,
                shop_opentime:shopOpenTime,
                shop_resttime:shopRestTime,
                shop_parkable:shopParkable,
                shop_description:ta_shop_description
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    alert("상점정보가 변경되었습니다.");
                }
                else {
                    alert(response['result_msg']);
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function shopReview(id) {
        window.open('shop_review_list?shop_id='+id, '상점리뷰리스트', 'width=600,height=720');
    }

    function shopQuestion(id) {
        window.open('shop_question_list?shop_id='+id, '상점문의리스트', 'width=600,height=720');
    }

    function registerEvent(shop_id) {
        var eventSummary = document.getElementById("shopEventTitle").value;
        var eventContent = document.getElementById("shopEventContent").value;
        var eventStart = document.getElementById("fromDate").value;
        var eventEnd = document.getElementById("toDate").value;
        var imgID = document.getElementById("eventBannerImg").getAttribute("value");
        var emoticonChecked = document.getElementById("checked").checked;        	
        var eventBannerID = imgID;
        if(isUndifined(imgID)) {
            eventBannerID = 0;
        }
		
        var id = document.getElementById("event_button_container").getAttribute("value");

        if(isUndifined(id)) {
            id = null;
        }
        if(emoticonChecked == true) emoticonChecked = 1;
        else emoticonChecked = 0;
        $.ajax({
            url: "register_event",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                event_id: id,
                event_shop_id:shop_id,
                event_summary:eventSummary,
                event_content:eventContent,
                event_start:eventStart,
                event_end:eventEnd,
                event_img_id: eventBannerID,
                event_emoticon_checked:emoticonChecked	
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    alert("이벤트 등록 성공!");
                    location.reload(true);
                }
                else {
                    alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function showProductList(id) {
        window.open('shop_product_list?shop_id='+id, '상점상품리스트', 'width=600,height=720');
    }

</script>


</body>

</html>
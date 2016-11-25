<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>상점보기</title>
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

    <div id="container" value="<?php echo $shopInfo['id']?>">
        <div class="row">
            <div class="col-md-6">
                <table class="table table-bordered table-scrollable">

                    <tbody>

                    <tr>
                        <td class="base-table-header">상점명</td>
                        <td><?php echo $shopInfo['shopName']?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">전화번호</td>
                        <td><?php echo $shopInfo['shopPhonenumber']?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">주소</td>
                        <td><?php echo $shopInfo['shopAddress']?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">카테고리</td>
                        <?php
                        	if($shopInfo['shopCategoryID'] == 1) {                    
                        		echo '<td>네일샵</td>';
                    		} else if($shopInfo['shopCategoryID'] == 2) {
                                echo '<td>헤어샵</td>';
                            }  else if($shopInfo['shopCategoryID'] == 15) {
                                echo '<td>속눈썹연장샵</td>';
                            } else if($shopInfo['shopCategoryID'] == 16) {
                                echo '<td>왁싱샵</td>';
                            } else if($shopInfo['shopCategoryID'] == 17) {
                                echo '<td>피부샵</td>';
                            } else if($shopInfo['shopCategoryID'] == 18) {
                                echo '<td>마사지샵</td>';
                            } else if($shopInfo['shopCategoryID'] == 19) {
                                echo '<td>반영구화장</td>';
                            } else if($shopInfo['shopCategoryID'] == 20) {
                                echo '<td>타투</td>';
                            } 
                        ?>
                    </tr>

                    <tr>
                        <td class="base-table-header">상점상태</td>
                        <?php
                        if($shopInfo['shopStatus'] == STATUS_MANAGE) {
                            echo '<td>  운영 </td>';
                        }
                        else  if($shopInfo['shopStatus'] == STATUS_NON_MANAGE){
                            echo '<td>  비운영 </td>';
                        }
                        else if($shopInfo['shopStatus'] == STATUS_REQUEST) {
                            echo '<td>  신청중 </td>';
                        }
                        ?>
                    </tr>

                    <tr>
                        <td class="base-table-header">노출여부</td>
                        <?php
                        if($shopInfo['shopVisibility'] == STATUS_HIDE) {
                            echo '<td>  비노출 </td>';
                        }
                        else {
                            echo '<td>  노출 </td>';
                        }
                        ?>
                    </tr>

                    <tr>
                        <td class="base-table-header">이벤트</td>
                        <?php
                        if($shopInfo['shopEventable'] == 'Y') {
                            echo '<td>진행</td>';
                        }
                        else {
                            echo '<td>중지</td>';
                        }
                        ?>
                    </tr>

                    <tr>
                        <td class="base-table-header">문의글 운영상태</td>
                        <?php
                        if($shopInfo['shopQuestionable'] == 'Y') {
                            echo '<td>진행</td>';
                        }
                        else {
                            echo '<td>중지</td>';
                        }
                        ?>
                    </tr>

                    <tr>
                        <td class="base-table-header">운영기간</td>
                        <td><?php echo $shopInfo['shopLiveDate']?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">별점(평점)</td>
                        <td><?php echo intval($shopInfo['shopRemarkLevel'])?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">리뷰</td>
                        <td><?php echo intval($shopInfo['shopReviewCnt'])?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">문의개수</td>
                        <td><?php echo intval($shopInfo['shopQuestionCnt'])?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">콜수</td>
                        <td><?php echo intval($shopInfo['shopCallCnt'])?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">클릭수</td>
                        <td><?php echo intval($shopInfo['shopClickCnt'])?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">가격표이미지</td>
                        <td>
                            <?php
                            if(count($shopInfo['shopPriceImgArray']) != 0) {
                                echo '등록';
                            }
                            else {
                                echo '미등록';
                            }
                            ?>
                            <button onclick="showPriceImage()">보기</button> <button onclick="updateShopPriceImage()">수정</button>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">사업자등록증</td>
                        <td>
                            <?php
                            if(count($shopInfo['shopManagerIdentyImgID']) != null) {
                                echo '등록';
                            }
                            else {
                                echo '미등록';
                            }
                            ?>
                            <button onclick="showManagerIdentiImage()">보기</button>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header" id="event_banner_title" value="<?php echo $shopInfo['shopEvent']['id']?>">이벤트배너</td>
                        <td>
                            <?php
                            if(count($shopInfo['shopEvent']['eventImgID']) != null) {
                                echo '등록';
                            }
                            else {
                                echo '미등록';
                            }
                            ?>
                            <button onclick="showEventBannerImage()">보기</button>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">이벤트제목</td>
                        <td><?php echo $shopInfo['shopEvent']['eventSummary'] ?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">이벤트내용</td>
                        <td><?php echo $shopInfo['shopEvent']['eventContent'] ?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">이벤트기간</td>
                        <td><?php echo $shopInfo['shopEvent']['eventStart']." ~ ".$shopInfo['shopEvent']['eventEnd']."   " ?>
                        <label></label>
                    	<?php 
							if($shopInfo['shopEvent']['eventEmoticon'] == 1) 
								echo '<input type="checkbox" id="checked" name="checked" checked="checked" disabled = "disabled"/>';
							else 
								echo '<input type="checkbox" id="checked" name="checked" disabled = "disabled"/>';
						?>
						<label>  이벤트 이모티콘</label>
						</td>
                    </tr>

                    <tr>
                        <td class="base-table-header">영업시간</td>
                        <td><?php echo $shopInfo['shopOpenTimeString']?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">휴무</td>
                        <td><?php echo $shopInfo['shopRestTimeString']?></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">주차</td>
                        <?php
                        if($shopInfo['shopParkable'] == STATUS_YES) {
                            echo '<td>가능</td>';
                        }
                        else {
                            echo '<td>불가능</td>';
                        }
                        ?>
                    </tr>

                    <tr>
                        <td class="base-table-header">상세설명</td>
                        <td id="shopDescription"><button onclick="showDescription()">보기</button></td>
                    </tr>

                    <tr>
                        <td class="base-table-header">가격정보</td>
                        <td>
                            <?php
                            if($shopInfo['shopProductCnt'] != 0) {
                                echo '등록';
                            }
                            else {
                                echo '미등록';
                            }
                            ?>
                            <button onclick="showProduct()">보기</button>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">찾아오시는 길</td>
                        <td>
                            <?php
                            if($shopInfo['shopRoad'] != null && strcmp($shopInfo['shopRoad'], "") != 0) {
                                echo '등록';
                            }
                            else {
                                echo '미등록';
                            }
                            ?>
                            <button onclick="showRoad()">보기</button> <button onclick="modifyRoad()">수정</button>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">이용후기</td>
                        <td><button onclick="showUseAfter()">보기</button></td>
                    </tbody>

                </table>

                <div style="margin: 5px;">
                    <button onclick="updateShop()">수정</button>
                    <button onclick="removeShop()">삭제</button>
                    <button onclick="changePassword()">비밀번호 변경</button>
                </div>


            </div>
            <div class="col-md-6">

                <table class="table table-bordered table-scrollable">
                    <tr>
                        <td class="base-table-header"> 상점이미지</td>
                    </tr>

                    <?php
                    $cnt = count($shopInfo['shopImgArray']);
                    for($i = 0; $i < $cnt;$i++){
                        echo '<tr>';
                        echo '<td>'.$shopInfo['shopImgArray'][$i]['imageURL'].'</td>';
                        echo '</tr>';
                    }
                    ?>

                </table>

                <div class="cb-row">
                    <button onclick="updateShopImage()" style="margin-left: 5px;">수정</button>
                    <button onclick="showShopImage()">보기</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Simple pop-up dialog box, containing a form -->
    <dialog id="changePasswordDialog">
        <form method="dialog">
            <section >
                <table class="table table-bordered" style="margin: 5px;">
                    <tbody>
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>변경아이디 </label></td>
                        <td><?php echo $shopInfo['shopID'];?></td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>새 비밀번호 </label></td>
                        <td>
                            <input id="shopPassword" type="password" style="width: 100%;" value="<?php echo $shopPhoneNumber;?>"/>
                            <label>4문자이상 영문, 숫자, 특수문자료 입력</label>
                        </td>
                    </tr>
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>비밀번호 확인</label></td>
                        <td><input id="shopConfirmPassword" type="password" style="width: 100%;"/></td>
                    </tr>
                    </tbody>
                </table>
            </section>
            <menu>
                <div style="text-align: center;">
                    <button id="ok" type="button">변경하기</button>
                    <button id="cancel" type="reset">취소</button>
                </div>
            </menu>
        </form>
    </dialog>

    <!-- Simple pop-up dialog box, containing a form -->
    <dialog id="dlg_shop_description" style="min-width: 300px;max-width: 640px;">
        <form method="dialog">
            <section >
                <table class="table table-bordered" style="margin: 5px;">
                    <tbody>
                    <tr>
                        <td class="base-table-header">상세설명</td>
                    </tr>

                    <tr>
                        <td id="td_shop_description"> <?php echo $shopInfo['shopDescriptionString']?> </td>
                    </tr>
                    </tbody>
                </table>
            </section>
            <menu>
                <div style="text-align: center;">
                    <button id="dlg_description_ok" type="button">확인</button>
                </div>
            </menu>
        </form>
    </dialog>

    <!-- Simple pop-up dialog box, containing a form -->
    <dialog id="dlg_shop_road_show" style="min-width: 300px;max-width: 640px;">
        <form method="dialog">
            <section >
                <table class="table table-bordered" style="margin: 5px;">
                    <tbody>
                    <tr>
                        <td class="base-table-header">찾아오시는 길</td>
                    </tr>

                    <tr>
                        <td id="td_shop_road"> <?php echo $shopInfo['shopRoad']?> </td>
                    </tr>
                    </tbody>
                </table>
            </section>
            <menu>
                <div style="text-align: center;">
                    <button id="dlg_road_ok" type="button">확인</button>
                </div>
            </menu>
        </form>
    </dialog>

    <!-- Simple pop-up dialog box, containing a form -->
    <dialog id="dlg_shop_road_modify" style="min-width: 300px;max-width: 640px;">
        <form method="dialog">
            <section >
                <table class="table table-bordered" style="margin: 5px;">
                    <tbody>
                    <tr>
                        <td class="base-table-header">찾아오시는 길</td>
                    </tr>

                    <tr>
                        <td id="td_shop_road"> <textarea class="form-control" rows="5" id="ta_shop_road" style="text-align:left;margin-top: 5px;"><?php echo $shopInfo['shopRoad']?></textarea> </td>
                    </tr>
                    </tbody>
                </table>
            </section>
            <menu>
                <div style="text-align: center;">
                    <button id="dlg_road_cancel" type="button">취소</button>
                    <button id="dlg_road_modify" type="button">등록</button>
                </div>
            </menu>
        </form>
    </dialog>

<script type="text/javascript">

    function onExit() {
        try {
            window.opener.HandlePopupResult();
        }
        catch (err) {}

    }

    function onLoad() {

    }

    function getShopID() {
        var idx = document.getElementById('container').getAttribute("value");
        return idx;
    }

    function getEventID() {
        var idx = document.getElementById('event_banner_title').getAttribute("value");
        return idx;
    }

    function showPriceImage() {
        var idx = getShopID();
        window.open('show_price_image_array?shop_id='+idx, '가격표이미지보기', 'width=720,height=720');
    }

    function updateShopPriceImage() {
        var idx = getShopID();
        window.open('update_shop_price_image_array?shop_id='+idx, '상점가격표이미지 변경', 'width=720,height=720');
    }

    function showManagerIdentiImage() {
        var idx = getShopID();
        window.open('show_manager_identi_image?shop_id='+idx, '인증서이미지보기', 'width=720,height=720');
    }

    function showEventBannerImage() {
        var idx = getEventID();
        window.open('show_event_banner_image?event_id='+idx, '이벤트이미지보기', 'width=720,height=720');
    }

    function updateShop() {
        var idx = getShopID();
        var params = [
            'height='+screen.height,
            'width='+screen.width,
            'fullscreen=yes' // only works in IE, but here for completeness
        ].join(',');
        window.open('update_shop?shop_id='+idx, '상점변경', params);
    }

    function removeShop() {
        var txt;
        var r = confirm("정말 삭제하시겠습니까?");
        if (r == true) { // remove
            var idx = getShopID();
            $.ajax({
                url: "remove_shop",
                type: "post", // To protect sensitive data
                data: {
                    ajax:true,
                    shop_id: idx
                },
                success:function(response){
                    var code = response['result_code'];

                    if(code == 0) {
                        alert("삭제되었습니다.");
                        window.close();
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
    }

    function changePassword() {
        var favDialog = document.getElementById('changePasswordDialog');
        var cancelButton = document.getElementById('cancel');
        var okButton = document.getElementById('ok');

        // Form cancel button closes the dialog box
        cancelButton.addEventListener('click', function() {
            favDialog.close();
        });

        okButton.addEventListener('click', function() {
            var shop_id = getShopID();
            var password = document.getElementById('shopPassword').value;
            var confirmPassword = document.getElementById('shopConfirmPassword').value;
            if(password != confirmPassword) {
                alert("암호가 맞지 않습니다.");
                return;
            }

            $.ajax({
                url: getBaseURL()+"/shop/change_password",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    shop_id:shop_id,
                    password:password
                },
                success: function (response) {

                    if(response['result_code'] == 0) {
                        alert("변경되었습니다.");
                        favDialog.close();
                    }
                    else {
                        alert("추가실패!");
                    }
                },
                error: function (jqXHR, msg, erro) {
                    alert("전송 실패! 망연결을 확인해주세요.");
                }
            });
        });

        favDialog.showModal();
    }

    function showShopImage() {
        var idx = getShopID();
        window.open('show_shop_image_array?shop_id='+idx, '상점이미지보기', 'width=720,height=720');
    }

    function updateShopImage() {
        var idx = getShopID();
        window.open('update_shop_image_array?shop_id='+idx, '상점이미지 변경', 'width=720,height=720');
    }

    function showProduct() {
        var idx = getShopID();
        window.open('shop_product_list?shop_id='+idx, '상점상품리스트', 'width=600,height=720');
    }

    function showRoad() {
        var favDialog = document.getElementById('dlg_shop_road_show');
        var okButton = document.getElementById('dlg_road_ok');

        // Form cancel button closes the dialog box
        okButton.addEventListener('click', function() {
            favDialog.close();
        });
        favDialog.showModal();
    }

    function modifyRoad() {
        var favDialog = document.getElementById('dlg_shop_road_modify');
        var cancelButton = document.getElementById('dlg_road_cancel');
        var okButton = document.getElementById('dlg_road_modify');

        // Form cancel button closes the dialog box
        cancelButton.addEventListener('click', function() {
            favDialog.close();
        });

        okButton.addEventListener('click', function() {
            var shop_id = getShopID();
            var shopRoad = document.getElementById('ta_shop_road').value;

            $.ajax({
                url: getBaseURL()+"/shop/update_shop_info",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    shop_id:shop_id,
                    shop_road:shopRoad
                },
                success: function (response) {

                    if(response['result_code'] == 0) {
                        alert("변경되었습니다.");
                        favDialog.close();
                    }
                    else {
                        alert("변경 실패!");
                    }
                },
                error: function (jqXHR, msg, erro) {
                    alert("전송 실패! 망연결을 확인해주세요.");
                }
            });
        });

        favDialog.showModal();
    }

    function showUseAfter() {
        var shop_id = getShopID();
        window.open('shop_review_list?shop_id='+shop_id, '상점리뷰리스트', 'width=600,height=720');
    }

    function showDescription() {
        var favDialog = document.getElementById('dlg_shop_description');
        var okButton = document.getElementById('dlg_description_ok');

        // Form cancel button closes the dialog box
        okButton.addEventListener('click', function() {
            favDialog.close();
        });
        favDialog.showModal();
    }

</script>

    <script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
    <script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
    <script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
</body>

</html>
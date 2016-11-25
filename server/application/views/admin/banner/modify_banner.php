
<div class="row">
    <table>
        <tbody>
        <tr>
            <td><label>이벤트명:</label>   </td>
        </tr>
        <tr>

            <td>
                <?php
                if($banner['bannerStatus'] == STATUS_DELETE) {
                    echo '<input id="banner_title" type="text" name="banner_title" size="10" style="font-size:12pt; color:#000000;width: 100%;"/>';
                }
                else {
                    echo '<input id="banner_title" type="text" name="banner_title" size="10" style="font-size:12pt; color:#000000;width: 100%;" value="'.$banner['bannerTitle'].'"/>';
                }
                ?>

            </td>
        </tr>
        <tr>
            <td><label>배너등록:</label>   </td>
        </tr>

        <tr>

            <td>
                <?php
                    switch($banner['bannerType']) {
                        case ADMIN_TYPE_MAIN_BANNER:
                            echo '(배너크기:720*262)';
                            break;
                        case ADMIN_TYPE_CAST_BANNER:
                            echo '(배너크기:720*240)';
                            break;
                        case ADMIN_TYPE_LOGOUT_BANNER:
                            echo '(배너크기:720*1127)';
                            break;
                        default:
                            echo '(배너크기:임의로)';
                            break;
                    }
                ?>
            </td>
        </tr>

        <tr>
            <td>
                <form method="post" enctype="multipart/form-data" id="bannerUploadForm" value="<?php echo  $banner['ads'];?>">
                    <input id="bannerImg" name = "bannerImg" type="file" value="<?php echo  $banner['ads']['adsImgID'];?>" style="font-size:12pt; color:#000000;">
                    <?php
                    if($banner['bannerStatus'] == STATUS_DELETE) {
                        echo '<Label id="bannerImgLabel"/>';
                    }
                    else {
                        echo '<Label id="bannerImgLabel">'.$banner['ads']['adsImgURL'].'</Label>';
                    }
                    ?>
                    </input>
                    <div class ="btn-group" style="margin-top: 5px;">
                        <input type="submit" value="등록"/>
                    </div>
                </form>
            </td>
        </tr>
        </tbody>
    </table>

    <div class="input-group" style="margin-top: 10px;">
        <label>노출방식:</label>
        <select id="show_mode" onchange="onChangeShowMode()">
            <?php
                if($banner['bannerShowMode'] == ADMIN_BANNER_SHOW_MODE_URL) {
                    echo '<option value="'.ADMIN_BANNER_SHOW_MODE_URL.'"selected>URL선택</option>';
                    echo '<option value="'.ADMIN_BANNER_SHOW_MODE_DETAIL.'">이벤트페이지</option>';
                }
                else {
                    echo '<option value="'.ADMIN_BANNER_SHOW_MODE_URL.'">URL선택</option>';
                    echo '<option value="'.ADMIN_BANNER_SHOW_MODE_DETAIL.'" selected>이벤트페이지</option>';
                }
            ?>

        </select>
    </div>

    <table id="url_table">
        <colgroup>
            <col width="20%">
            <col width="80%">
        </colgroup>

        <tbody>
            <tr>
                <td>배너 URL:   </td>
                <td>
                    <?php
                    if($banner['bannerStatus'] == STATUS_DELETE) {
                        echo '<input id="banner_url" type="text" name="banner_url" size="10" style="font-size:12pt; color:#000000;width: 100%;"/>';
                    }
                    else {
                        echo '<input id="banner_url" type="text" name="banner_url" size="10" style="font-size:12pt; color:#000000;width: 100%;" value="'.$banner['ads']['adsURL'].'"/>';
                    }
                    ?>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <button type="button" class="btn btn-default" onclick="WriteBannerBasicInfo(<?php echo $banner['id'] ?> )" style="margin-top: 5px;">수정 및 등록 </button>
                </td>
            </tr>
        </tbody>
    </table>

    <?php
    if($banner['bannerShowMode'] == ADMIN_BANNER_SHOW_MODE_DETAIL) {
        echo '<table id="detail_table" style="visibility: visible">';
    }
    else {
        echo '<table id="detail_table"  style="visibility: hidden">';
    }
    ?>
        <colgroup>
            <col width="20%">
            <col width="80%">
        </colgroup>

        <tbody>

        <tr>
            <td>이벤트이미지:(720*1230)</td>
            <td>
                <form method="post" enctype="multipart/form-data" id="bannerBackImgUploadForm" value="<?php echo  $banner['bannerBackImgID'];?>"  style="text-align:left;margin-top: 5px;">
                    <input id="bannerBackImg" name = "bannerBackImg" type="file" value="<?php echo  $banner['bannerBackImgID'];?>" style="font-size:12pt; color:#000000;">
                    <?php
                    if($banner['bannerStatus'] == STATUS_DELETE) {
                        echo '<Label id="bannerBackImgLabel"/>';
                    }
                    else {
                        echo '<Label id="bannerBackImgLabel">'.$banner['bannerBackImgURL'].'</Label>';
                    }
                    ?>
                    </input>
                    <div class ="btn-group" style="margin-top: 5px;">
                        <input type="submit" value="등록"/>
                    </div>
                </form>
            </td>
        </tr>

        <tr>
            <td>내용:   </td>
            <td>
                 <textarea class="form-control" rows="5" id="banner_content" style="text-align:left;margin-top: 5px;"><?php if($banner['bannerStatus'] == STATUS_DELETE) { echo ' '; } else { echo $banner['bannerContent']; }?></textarea>
            </td>
        </tr>

        <tr>
            <td>이벤트기간:</td>
            <td>
            <div id="banner-from-date-container" style="width: 100%;margin-top: 5px;">
                <table>
                    <tbody>
                    <tr>
                        <?php
                        if($banner['bannerStatus'] == STATUS_DELETE) {
                            echo '<td><input type="text" type="text" class="form-control" id="fromBannerDate"/></td>';
                        }
                        else {
                            echo '<td><input type="text" type="text" class="form-control" value="'.$banner['bannerStartDate'].'" id="fromBannerDate"/></td>';
                        }
                        ?>

                        <td><label> ~ </label></td>
                        <?php
                        if($banner['bannerStatus'] == STATUS_DELETE) {
                            echo '<td><input type="text" type="text" class="form-control" id="toBannerDate"/></td>';
                        }
                        else {
                            echo '<td><input type="text" type="text" class="form-control" value="'.$banner['bannerEndDate'].'" id="toBannerDate"/></td>';
                        }
                        ?>
                    </tr>
                    </tbody>
                </table>
            </div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <?php
                if($banner['bannerDateShow'] == 1) {  // invisible
                    echo ' <input type="checkbox" id="bannerDateShow" name="bannerDateShow" value="'. $banner['bannerDateShow'] .'" checked> 이벤트기간노출 </input>';
                }
                else {  // visible
                    echo ' <input type="checkbox" id="bannerDateShow" name="bannerDateShow" value="'. $banner['bannerDateShow'] .'">이벤트기간노출</input>';
                }

                if($banner['bannerTitleShow'] == 1) {  // invisible
                    echo ' <input type="checkbox" id="bannerTitleShow" name="bannerTitleShow" value="'. $banner['bannerTitleShow'] .'" checked> 이벤트명노출 </input>';
                }
                else {  // visible
                    echo ' <input type="checkbox" id="bannerTitleShow" name="bannerTitleShow" value="'. $banner['bannerTitleShow'] .'">이벤트명노출</input>';
                }

                if($banner['bannerContentShow'] == 1) {  // invisible
                    echo ' <input type="checkbox" id="bannerContentShow" name="bannerContentShow" value="'. $banner['bannerContentShow'] .'" checked> 이벤트내용노출 </input>';
                }
                else {  // visible
                    echo ' <input type="checkbox" id="bannerContentShow" name="bannerContentShow" value="'. $banner['bannerContentShow'] .'">이벤트내용노출</input>';
                }
                ?>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <button type="button" class="btn btn-default" onclick="WriteBannerDetailInfo(<?php echo $banner['id'] ?> )" style="margin-top: 5px;">수정 및 등록 </button>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<script>
    $('#banner-from-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });
    $('#banner-to-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });

    $("#bannerUploadForm").ajaxForm({
        url : getBaseURL()+'/admin/upload_image?kind=app&file_name=bannerImg',
        dataType : 'json',
        success : function (response) {
            var code = response['result_code'];
            if(code == 0) {
                var var_name = "bannerImg";
                document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                var input_name = '#bannerImgLabel';
                $(input_name).text(response['result_data']['imageURL']);
            }
            else {
                alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
            }
        }
    })
    ;

    $("#bannerBackImgUploadForm").ajaxForm({
        url : getBaseURL()+'/admin/upload_image?kind=app&file_name=bannerBackImg',
        dataType : 'json',
        success : function (response) {
            var code = response['result_code'];
            if(code == 0) {
                var var_name = "bannerBackImg";
                document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                var input_name = '#bannerBackImgLabel';
                $(input_name).text(response['result_data']['imageURL']);
            }
            else {
                alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
            }
        }
    })
    ;

    function onChangeShowMode() {
        var value = document.getElementById("show_mode").value;
        var detail_table = document.getElementById("detail_table");
        if(value == 1) {
            detail_table.setAttribute("style", "visibility:visible;");
        }
        else {
            detail_table.setAttribute("style", "visibility:hidden;");
        }
    }

    function WriteBannerBasicInfo(id) {
        var bannerTitle = $('#banner_title').val();
        var bannerURL = document.getElementById("banner_url").value;
        var bannerImgID = document.getElementById("bannerImg").getAttribute("value");
        var showMode = document.getElementById("show_mode").value;
        var bannerID = id;

        if (isUndifined(bannerID) == true) { // Any scope
            bannerID  = null;
        }

        if(bannerURL == null || bannerImgID == null) {
            alert("배너를 등록해주세요.");
            return;
        }

        $.ajax({
            url: getBaseURL()+"/admin/write_banner",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                banner_id:id,
                banner_title:bannerTitle,
                banner_url:bannerURL,
                banner_img_id:bannerImgID,
                banner_show_mode:showMode
            },
            success:function(response){
                if(response['result_code'] == 0) {
                    location.reload(true);
                }
                else {
                    alert("등록 실패!");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function WriteBannerDetailInfo(id) {

        if(id == null) {
            return;
        }

        var bannerContent = $('#banner_content').val();
        var bannerEndDate = $('#toBannerDate').val();
        var bannerStartDate = $('#fromBannerDate').val();
        var bannerURL = document.getElementById("banner_url").value;
        var bannerImgID = document.getElementById("bannerImg").getAttribute("value");
        var showMode = document.getElementById("show_mode").value;

        if(bannerContent == null || bannerContent == "") {
            alert("내용을 입력해주세요.");
            return;
        }

        if(bannerStartDate == null || bannerStartDate == "") {
            alert("기간을 입력해주세요.");
            return;
        }

        if(bannerEndDate == null || bannerEndDate == "") {
            alert("기간을 입력해주세요.");
            return;
        }

        var bannerBackImgID = document.getElementById("bannerBackImg").getAttribute("value");
        if (isUndifined(bannerBackImgID) == true) { // Any scope
            bannerBackImgID  = null;
        }

        var bannerDateShow = document.getElementById("bannerDateShow").checked;
        if(bannerDateShow == true) {
            bannerDateShow = 1;
        }
        else {
            bannerDateShow = 0;
        }

        var bannerTitleShow = document.getElementById("bannerTitleShow").checked;
        if(bannerTitleShow == true) {
            bannerTitleShow = 1;
        }
        else {
            bannerTitleShow = 0;
        }

        var bannerContentShow = document.getElementById("bannerContentShow").checked;
        if(bannerContentShow == true) {
            bannerContentShow = 1;
        }
        else {
            bannerContentShow = 0;
        }

        $.ajax({
            url: getBaseURL()+"/admin/write_banner",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                banner_id:id,
                banner_url:bannerURL,
                banner_img_id:bannerImgID,
                banner_content:bannerContent,
                banner_start_date:bannerStartDate,
                banner_show_mode:showMode,
                banner_end_date:bannerEndDate,
                banner_back_img_id:bannerBackImgID,
                banner_date_show:bannerDateShow,
                banner_title_show:bannerTitleShow,
                banner_content_show:bannerContentShow
            },
            success:function(response){
                if(response['result_code'] == 0) {
                    location.reload(true);
                }
                else {
                    alert("등록 실패!");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }
</script>
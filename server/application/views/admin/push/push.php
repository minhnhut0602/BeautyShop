
<style type="text/css">
    #submit_container {
        width: 150px;
        height: 30px;
        text-align:center;
    }
</style>

<div class="container" xmlns="http://www.w3.org/1999/html" id="root">
    <div class="row">
        <div class="col-md-6"   id = "panel_push">

            <p/>

            <div class="input-group">
                <select id="push_kind">
                    <option value="0" selected> 푸쉬 보내기 </option>
                    <option value="1"> 쪽지 보내기 </option>
                </select>
            </div>

            <p/>

            <div class="input-group">
                <label>회원:   </label>
                <select id="member_kind">
                    <option value="0" selected> 전체 </option>
                    <option value="1"> 상점주 </option>
                    <option value="2"> 일반회원 </option>
                </select>
            </div>

            <div class="input-group">
                <label>성별:   </label>
                <select id="sex_kind">
                    <option value="0" selected> 전체 </option>
                    <option value="M"> 남 </option>
                    <option value="W"> 녀 </option>
                </select>
            </div>

            <div class="input-group">
                <label>지역:   </label>
                <select id="location">
                    <?php
                        echo '<option value="0" selected>전체</option>';
                        $cnt = count($arrLocation);
                        for($i = 0; $i < $cnt; $i++) {
                            $location = $arrLocation[$i];
                            echo '<option value="'.$location['id'].'">'.$location['locationName1'].' '.$location['locationName2'].'</option>';
                        }
                    ?>
                </select>
            </div>

            <div class="input-group">
                <label>등급:   </label>
                <select id="level">
                    <option value="-1" selected> 전체 </option>
                    <option value="0"> 0 </option>
                    <option value="1"> 1 </option>
                    <option value="2"> 2 </option>
                    <option value="3"> 3 </option>
                    <option value="4"> 4 </option>
                    <option value="5"> 5 </option>
                </select>
            </div>

            <div class="input-group">
                <form method="post" enctype="multipart/form-data" id="pushImgUploadForm" style="margin-left: 20px;">
                    <input id="pushImg" name = "pushImg" type="file" value="0" style="font-size:12pt; color:#000000;width:320px">
                    <Label id="pushImgLabel"></Label>
                        (배너크기:320*180)
                    </input>
                    <input type="submit" value="등록"/>
                </form>
                <input id="pushBannerURL" type="url" size="10" style="font-size:12pt; color:#000000;width:380px">랜딩url</input>
                <textarea class="form-control" rows="5" id="push_message" style="text-align:left;margin-top: 5px;"></textarea>
            </div>

            <p/>

            <div id="submit_container">
                <button onclick="onSendPush()">발송</button>
            </div>
        </div>

        <!--
        <div class="col-md-6" id = "panel_logout_banner">
            <label> 로그아웃배너 등록  (배너크기:720*1127) </label>

            <form method="post" enctype="multipart/form-data" id="exitImgUploadForm" style="margin-left: 20px;">
                <input id="exitImg" name = "exitImg" type="file" value='<?php echo $exitAds['adsImgID']?>' style="font-size:12pt; color:#000000;width:320px">
                <Label id="exitImgLabel"><?php echo $exitAds['adsImgURL']?></Label>
                </input>
                <input type="submit" value="등록"/>
            </form>

            <p/>

            <input id="banner_url" type="url" size="10" style="font-size:12pt; color:#000000;width:380px" value='<?php echo $exitAds['adsURL']?>'/>

            <div id="submit_container">
                <button onclick="onRegExitAds(<?php echo $exitAds['id']?>)">수정 및 등록</button>
            </div>
        </div>
        -->
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/jquery.form.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script>

    $("#pushImgUploadForm").ajaxForm({
        url : getBaseURL()+'/admin/upload_image?kind=app&file_name=pushImg',
        dataType : 'json',
        success : function (response) {
            var code = response['result_code'];
            if(code == 0) {
                var var_name = "pushImg";
                document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                var input_name = '#pushImgLabel';
                $(input_name).text(response['result_data']['imageURL']);
            }
            else {
                alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
            }
        }
    })
    ;

    $("#exitImgUploadForm").ajaxForm({
        url : getBaseURL()+'/admin/upload_image?kind=app&file_name=exitImg',
        dataType : 'json',
        success : function (response) {
            var code = response['result_code'];
            if(code == 0) {
                var var_name = "exitImg";
                document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                var input_name = '#exitImgLabel';
                $(input_name).text(response['result_data']['imageURL']);
            }
            else {
                alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
            }
        }
    })
    ;


    function isUndifined(variable) {
        if (typeof variable == 'undefined' || variable == 'undefined') { // Any scope
            return true;
        }
        return false;
    }


    function onSendPush() {
        var kind_push = $('#push_kind').val();
        var kind_member = $('#member_kind').val();
        var sex = $('#sex_kind').val();
        var location = $('#location option:selected').text();
        var level = $('#level').val();
        var message = $('#push_message').val();
        var push_img_url = $('#pushImgLabel').text();
        var push_landing_url = $('#pushBannerURL').val();

        if(message == null || message == "") {
            alert("푸쉬알림 문구를 입력하세요.");
            return;
        }

        if(location == "전체") {
            location = "";
        }

        if(push_img_url == null || isUndifined(push_img_url)) {
            push_img_url = null;
        }

        if(sex == 0) {
            sex = null;
        }
        if(level == '-1') {
            level = null;
        }

        $.ajax({
            url: "push/send_message",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                kind_push: kind_push,
                kind_member: kind_member,
                sex:sex,
                location:location,
                level:level,
                message:message,
                push_img_url:push_img_url,
                push_landing_url:push_landing_url
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("전송 성공!");
                }
                else {
                    alert("전송 실패!지정한 사용자가 유효한지 확인해주세요.");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function onRegExitAds(id) {
        var banner_img_id = document.getElementById("exitImg").getAttribute("value");
        var banner_url = $("#banner_url").val();

        if(banner_img_id == null || banner_img_id == 0) {
            alert("이미지를 등록해주세요.")
            return;
        }

        if(banner_url == "") {
            alert("랜딩 url를  입력해주세요.")
            return;
        }

        var ads_id = id;
        if (isUndifined(id) == true) { // Any scope
            ads_id  = null;
        }

        $.ajax({
            url: "push/register_exit_banner",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                ads_id:ads_id,
                ads_img_id:banner_img_id,
                ads_url:banner_url
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("등록 성공!");
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

</script>
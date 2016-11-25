<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>미끌</title>
    <link href="<?php echo base_url(); ?>assets/css/admin/global.css" rel="stylesheet" type="text/css">
    <link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap.css" rel="stylesheet" type="text/css">
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

<body onunload="onExit()">

<div id="container">
    <table class="table table-bordered table-scrollable">
        <tbody>
            <tr>
                <td  class="base-table-header"> 아이디 </td>
                <td><?php echo $userInfo['userID'] ?></td>
            </tr>
            <tr>
                <td  class="base-table-header"> 지역 </td>
                <td><?php echo $userInfo['userLocationName'] ?></td>
            </tr>
            <tr>
                <td  class="base-table-header"> 성별 </td>
                <td>
                    <?php
		                if($userInfo['userSex'] == 'M') {
		                    echo '남자';
		                } else if($userInfo['userSex'] == 'W') {
		                	echo '여자';
		                } else {
		                    echo '선택 안함';
		                }
                    ?>
                </td>
            </tr>
            <tr>
                <td  class="base-table-header"> 나이 </td>
                <td><?php echo $userInfo['userAge'] ?></td>
            </tr>
            <tr>
                <td  class="base-table-header"> 등급 </td>
                <td><?php echo $userInfo['userLevel'] ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 이메일 </td>
                <td><?php echo $userInfo['userEmail'] ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 포인트 </td>
                <td><?php echo $userInfo['userPoint'] ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 추천인 </td>
                <td><?php echo $userInfo['userRecommender'] ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 추천인수 </td>
                <td><?php echo $userInfo['userRecommenderCnt'] ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 친구초대 </td>
                <td><?php echo $userInfo['userRecommenderCnt'] ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 추천인 수 </td>
                <td><?php echo '카톡 '.$userInfo['userInviteFriendKakaoCnt'].'회  '.'페이스북 '.$userInfo['userInviteFriendFBCnt'].'회' ?></td>
            </tr>

            <tr>
                <td  class="base-table-header"> 스탭 </td>
                <td>
                    <?php
                        if($is_update == 1) {
                            echo '<select id="userStop">';
                            if ($userInfo['userStatus'] == STATUS_STOP_USER || $userInfo['userStatus'] == STATUS_STOP_LIMIT_USER) {
                                echo '<option value="'.$userInfo['userStatus'].'" selected> 스탭 </option>';
                                echo '<option value="0" > 일반 </option>';
                            } else {
                                echo '<option value="2"> 스탭 </option>';
                                echo '<option value="'.$userInfo['userStatus'].'" selected> 일반 </option>';
                            }
                            echo '</select>';
                        }
                        else {
                            if ($userInfo['userStatus'] == STATUS_STOP_USER || $userInfo['userStatus'] == STATUS_STOP_LIMIT_USER) {
                                echo '스탭';
                            } else {
                                echo '일반';
                            }
                        }
                    ?>
                </td>
            </tr>
            <tr>
                <td  class="base-table-header"> 상태 </td>
                <td>
                    <?php
                    if($is_update == 1) {
                        echo '<select id="userStatus">';
                        if ($userInfo['userStatus'] == STATUS_LIMIT_USER || $userInfo['userStatus'] == STATUS_STOP_LIMIT_USER) {
                            echo '<option value="'.$userInfo['userStatus'].'" selected> 제한 </option>';
                            echo '<option value="0" > 정상 </option>';
                        } else {
                            echo '<option value="1"> 제한 </option>';
                            echo '<option value="'.$userInfo['userStatus'].'" selected> 정상 </option>';
                        }
                        echo '</select>';
                    }
                    else {
                        if ($userInfo['userStatus'] == STATUS_LIMIT_USER || $userInfo['userStatus'] == STATUS_STOP_LIMIT_USER) {
                            echo '제한';
                        } else {
                            echo '정상';
                        }
                    }
                    ?>
                </td>
            </tr>
        </tbody>
    </table>
    <div style="margin: 5px;">
        <?php
            if($is_update == 1) {
                echo '<button onclick="saveInfo('.$userInfo['id']. ')" > 저장</button >';
            }
            else {
                echo '<button onclick = "updateUser('.$userInfo['id']. ')" > 수정</button >';
                echo '<button onclick = "removeUser('.$userInfo['id']. ')" style="margin-left: 5px;" > 삭제</button >';
                echo '<button onclick = "sendPush('.$userInfo['id']. ')"  style="margin-left: 5px;"> 쪽찌보내기</button >';
            }
        ?>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

<script type="text/javascript">

    function saveLevel(id)
    {
        var level = document.getElementById("userLevel").value;

        $.ajax({
            url: "save_level",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                user_id: id,
                user_level: level
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("저장 성공!");
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

    function getStatus() {

        var stop = $('#userStop').val();
        var status = $('#userStatus').val();

        if(stop == 2) {
            if(status == 1) {
                status = 3;
            }
            else {
                status = 2;
            }
        }

        return status;
    }

    function saveInfo(id) {

        var status = getStatus();

        $.ajax({
            url: "update_user",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                user_id: id,
                user_status: status
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("저장 성공!");
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

    function onExit() {
        try {
            window.opener.HandlePopupResult();
        }
        catch (err) {}

    }

    function addLevel() {
        var point = parseInt(document.getElementById("userPoint").innerText);
        point += 100;

        if(point > 50000) {
            point = 50000;
        }

        document.getElementById("userPoint").innerText = point;
    }

    function removeLevel() {
        var point = parseInt(document.getElementById("userPoint").innerText);
        point -= 100;

        if(point <= 0) {
            point = 0;
        }

        document.getElementById("userPoint").innerText = point;
    }

    function sendEmblancePoint() {
        var point = parseInt(document.getElementById("userPoint").innerText);
        point += 1000;

        if(point > 50000) {
            point = 50000;
        }

        document.getElementById("userPoint").innerText = point;
    }

    function setStap() {
        $('#btn_set_stap').val(true);
        $('#btn_set_stap').removeClass("btn btn-default btn-sm").addClass("btn btn-default btn-sm active");
        $('#btn_cancel_stap').removeClass("btn btn-default btn-sm active").addClass("btn btn-default btn-sm");
    }

    function cancelStap() {
        $('#btn_set_stap').val(false);
        $('#btn_set_stap').removeClass("btn btn-default btn-sm active").addClass("btn btn-default btn-sm");
        $('#btn_cancel_stap').removeClass("btn btn-default btn-sm").addClass("btn btn-default btn-sm active");
    }

    function exitUser() {
        var isExitUser = $('#btn_exit_user').val();
        if(isExitUser == -1) {
            $('#btn_exit_user').removeClass("btn btn-default btn-sm active").addClass("btn btn-default btn-sm");
            $('#btn_exit_user').val(0);
        }
        else {
            $('#btn_exit_user').removeClass("btn btn-default btn-sm").addClass("btn btn-default btn-sm active");
            $('#btn_exit_user').val(-1);
        }
    }

    function removeUser(id){
        $.ajax({
            url: "remove_user",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                user_id: id
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("삭제 성공!");
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

    function updateUser(idx) {
        window.open('update_member?user_id='+idx, '유저관리', 'width=480,height=520');
    }

    function sendPush(idx) {
        var url = getBaseURL() + '/push/send_push?user_id=' + idx;
        window.open(url, '쪽찌발송', 'width=480,height=520');
    }

</script>


</body>

</html>
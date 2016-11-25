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
            <td  class="base-table-header"> 상점아이디 </td>
            <td><?php echo $userInfo['shopID'] ?></td>
            <td  class="base-table-header"> 미끌아이디 </td>
            <td><?php echo $userInfo['userID'] ?></td>
        </tr>
        <tr>
            <td  class="base-table-header"> 상점이름 </td>
            <td><?php echo $userInfo['shopName'] ?></td>
        </tr>
        <tr>
            <td  class="base-table-header"> 상점주소 </td>
            <td><?php echo $userInfo['shopAddress'] ?></td>
        </tr>
        <tr>
            <td  class="base-table-header"> 상점전화번호 </td>
            <td><?php echo $userInfo['shopPhonenumber'] ?></td>
        </tr>

        <tr>
            <td  class="base-table-header"> 영업시간 </td>
            <td><?php echo $userInfo['shopOpenTimeString'] ?></td>
        </tr>

        <tr>
            <td  class="base-table-header"> 휴무 </td>
            <td><?php echo $userInfo['shopRestTimeString'] ?></td>
        </tr>

        <tr>
            <td  class="base-table-header"> 주차가능여부 </td>
            <td>
                <?php

                    if($userInfo['shopParkable'] == 'Y') {
                        echo '가능';
                    }
                    else {
                        echo '불가';
                    }
                ?>
            </td>
        </tr>

        <tr>
            <td  class="base-table-header"> 사업자등록증 </td>
            <td>
                <?php
                if($userInfo['shopManagerIdentyImgID'] == null) {
                    echo '없음';
                }
                else {
                    echo '등록됨';
                }
                ?>
            </td>
        </tr>

        <tr>
            <td  class="base-table-header"> 신청자이름 </td>
            <td><?php echo $userInfo['shopPostManName'] ?></td>
        </tr>

        <tr>
            <td  class="base-table-header"> 신청자연락처 </td>
            <td><?php echo $userInfo['shopStuffPhoneNumber'] ?></td>
        </tr>

        <tr>
            <td  class="base-table-header"> 승인상태 </td>
            <td>
                <?php
                    if($userInfo['shopStatus'] == STATUS_MANAGE) {
                        echo '승인';
                    }
                    else {
                        echo '미승인';
                    }
                ?>
            </td>
        </tr>

        </tbody>
    </table>
    <div style="margin: 5px;text-align: center;">
        <button onclick = "updateUser(<?php echo $userInfo['id'] ?>)" > 수정 </button >
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

<script type="text/javascript">

    function updateUser(id) {
        window.open('update_shop_member?user_id='+id, '상점멤버변경', 'width=640,height=640');
    }

    function processConnection(shop_connection_id) {
        $.ajax({
            url: "process_connection",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                shop_id: shop_connection_id
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("연동되었습니다.!");
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

</script>


</body>

</html>
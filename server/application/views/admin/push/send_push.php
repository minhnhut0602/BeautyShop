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

<body>

<div class="row">
    <div>
        <table class="table table-bordered" style="margin: 5px;">
            <tbody>
                <tr>
                    <td class="base-search-table-header"><label>발송자 </label></td>
                    <td><input id="userID" type="text" style="width: 100%;" value="관리자"/></td>
                </tr>

                <tr id="recvUserIdentyID" value="<?php echo $userInfo['id'];?>">
                    <td class="base-search-table-header"><label>수신자 </label></td>
                    <td><input id="recvUserID" type="text" style="width: 100%;" value="<?php echo $userInfo['userID'];?>"/></td>
                </tr>

                <tr>
                    <td class="base-search-table-header"><label>제목 </label></td>
                    <td><input id="title" type="text" style="width: 100%;"/></td>
                </tr>

                <tr>
                    <td class="base-search-table-header"><label>메시지 </label></td>
                    <td>
                        <textarea class="form-control" rows="5" id="msg" style="text-align:left;margin-top: 5px;"></textarea>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="row" style="text-align: center;margin: 5px;">
        <button type="button" class="btn btn-default" onclick="sendPush()"/> 발송</button>
        <button type="button" class="btn btn-default" onclick="cancelPush()"/> 취소</button>
    </div>

</div>

<script>
    function cancelPush() {
        $('#title').val('');
        $('#msg').val('');

        window.close();
    }

    function sendPush() {
        var recevID =  document.getElementById("recvUserIdentyID").getAttribute("value");
        var title = $('#title').val();
        var msg = $('#msg').val();

        if(title == null || title == "") {
            alert("제목을 입력해주세요.");
            return;
        }

        if(msg == null || msg == "") {
            alert("내용을 입력해주세요.");
            return;
        }

        $.ajax({
            url: "send_push_message",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                user_id: recevID,
                title: title,
                msg: msg
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

</script>


</body>
<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
</html>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>캐스트 상세 추가</title>
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

<div id="container">
    <p/>
    <div class="input-group">
        <label>이미지:   </label>
        <form method="post" enctype="multipart/form-data" id="imageUploadForm" style="margin-left: 20px;">
            <input id="castdetailImg" name = "castdetailImg" type="file" value="0" style="font-size:12pt; color:#000000;width:320px">
            <Label id="castdetailImgLabel"/>
            </input>

            <input type="submit" value="등록"/>
        </form>
    </div>

    <p/>

    <div class="input-group">
        <label >내용:   </label>
        <textarea id="content" type="text" name="content" size="10" value='10'  style="font-size:12pt; color:#000000; width: 300px" rows="5">
        </textarea>
    </div>

    <p/>

    <div id="submit_container">
        <?php
        echo '<button type="button" class="btn btn-default" onclick="saveInfo()" id="btn_transfer" style="width: auto;"/> <span aria-hidden="true">등록 및 수정</span></button>';
        ?>
    </div>

    <p/>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script type="text/javascript">

    function onLoad() {
        $("#imageUploadForm").ajaxForm({
            url : 'upload_image?kind=cast&file_name=castdetailImg',
            dataType : 'json',
            success : function (response) {
                var code = response['result_code'];
                if(code == 0) {
                    var var_name = "castdetailImgLabel";
                    document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                    var input_name = '#castdetailImgLabel';
                    $(input_name).text(response['result_data']['imageURL']);
                }
                else {
                    alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
                }
            }
        })
        ;
    }

    function isUndifined(variable) {
        if (typeof variable == 'undefined' || variable == 'undefined') { // Any scope
            return true;
        }
        return false;
    }

    function saveInfo() {
        var img_id = document.getElementById("castdetailImg").value;
        var content = document.getElementById("content").value;
        var id = document.getElementById("container").getAttribute("value");
        if(isUndifined(id) == true) {
            id = null;
        }

        if(title == "") {
            alert("알림제목을 입력해주세요.");
            return;
        }

        if(content == "") {
            alert("알림내용을 입력해주세요.");
            return;
        }

        $.ajax({
            url: "register_notice",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                id:id,
                title:title,
                content:content
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    isCreateShop = true;
                    alert("알림이 등록되었습니다.");
                    location.href ='notice?page_num=1';
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
            if(isCreateShop == true) {
                window.opener.HandlePopupResult();
            }
        }
        catch (err) {}

    }

</script>


</body>

</html>
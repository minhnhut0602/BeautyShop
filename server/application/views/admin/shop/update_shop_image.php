<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>상점가격이미지 변경</title>
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

<div id="container" value="<?php echo $shopID?>">
    <table  class="table table-bordered table-scrollable" value="<?php echo $isShopImage?>" id="table">
        <tbody>
        <tr>
            <td class="base-table-header">
                <?php
                if ($isShopImage == 0) {
                     echo    '상점가격이미지';
                }
                else {
                    echo    '상점이미지';
                }
                ?>
            </td>
        </tr>

        <?php
        $cnt = count($arrImage);
        for($i = 0; $i < MAX_SHOP_IMG_CNT;$i++){
            echo '<tr>';
            echo '<td>';
            echo '<form method="post" enctype="multipart/form-data" id="imgUploadForm'.($i+1).'">';
            if($i <= ($cnt - 1 )) {
                echo '<Label id="shopImgLabel'.($i+1).'" style="margin-left: 0px;border-style: solid;border-width: 1px;">'.$arrImage[$i]['imageURL'].'</Label>';
                echo '<input id="shopImg'.($i+1).'" name = "shopImg'.($i+1).'" type="file" value="'.$arrImage[$i]['id'].'" style="font-size:12pt; color:#000000;margin-top: 3px;"/>';
            }
            else {
                echo '<label id="shopImgLabel'.($i+1).'" style="margin-left: 0px;border-style: solid;border-width: 1px;"> 없음 </label>';
                echo '<input id="shopImg'.($i+1).'" name = "shopImg'.($i+1).'" type="file" value="0" style="font-size:12pt; color:#000000;margin-top: 3px;"/>';
            }

            echo '<div style="margin-top: 3px;">';
            echo '<input type="submit" value="등록"/>';
            echo '<input type="button" onclick="onRemoveImgClick('.($i+1).')" value="삭제" style="margin-left: 3px;"/>';
            echo '</div>';
            echo '</form>';
            echo '</td>';
            echo '</tr>';
        }
        ?>
        </tbody>
    </table>

    <div class="container">
        <button type="button" class="btn btn-default" style="margin: 5px" onclick="saveInfo()"> 저장 </button>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/jquery.form.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script type="text/javascript">

    function onLoad() {
        var i = 0;
        var cnt = <?php echo MAX_SHOP_IMG_CNT?>;
        while (i < cnt) {
            var formName = '#imgUploadForm' + (i + 1);
            $(formName)
                .ajaxForm({
                    url: getBaseURL()+'/admin/upload_image?kind=shop&file_name=shopImg' + (i + 1) + '&index=' + (i + 1), // or whatever
                    dataType: 'json',
                    success: function (response) {
                        var code = response['result_code'];
                        if (code == 0) {
                            var ret_id = response['result_data']['index'];
                            var var_name = "shopImg" + ret_id;
                            document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                            var input_name = '#shopImgLabel' + (ret_id);
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
    }

    function getShopID() {
        var idx = document.getElementById('container').getAttribute("value");
        return idx;
    }

    function onRemoveImgClick(idx) {

        $("#shopImgLabel"+idx).text("");

        var var_name = "shopImg"+idx;
        document.getElementById(var_name).setAttribute("value", "0");
    }

    function getImageArrayString() {
        var arrRealImgID = new Array;
        var i = 0;

        while(i < 20) {
            i++;
            var var_name = "shopImg"+i;
            var img_id = document.getElementById(var_name).getAttribute("value");
            if(img_id != "0") {
                arrRealImgID.push(img_id);
            }
        }

        return JSON.stringify(arrRealImgID);
    }

    function saveInfo() {
        var str_img_array = getImageArrayString();
        var shop_id = getShopID();
        var is_shop_image = document.getElementById("table").getAttribute("value");

        $.ajax({
            url: "update_shop_info",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                shop_id: shop_id,
                is_shop_image: is_shop_image,
                shop_img_array_string:str_img_array
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    alert("상점이미지가 변경되었습니다.");
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
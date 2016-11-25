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

<body onunload="onExit()">

<div id="container" value="<?php echo $shopID?>">
    <table  class="table table-bordered table-scrollable" value="<?php echo $isShopImage?>">
        <tbody>
        <tr>
            <td class="base-table-header">상점가격이미지</td>
        </tr>

        <?php
        $cnt = count($arrImage);
        for($i = 0; $i < MAX_SHOP_IMG_CNT;$i++){
            echo '<tr>';
            echo '<form method="post" enctype="multipart/form-data" id="imgUploadForm'.($i+1).'">';
            if($i <= ($cnt - 1 )) {
                echo '<Label id="shopImgLabel'.($i+1).'" style="margin-left: 0px;border-style: solid;border-width: 1px;">'.$arrImage[$i]['imageURL'].'</Label>';
                echo '<input id="shopImg'.($i+1).'" name = "shopImg'.($i+1).'" type="file" value="'.$arrImage[$i]['id'].'" style="font-size:12pt; color:#000000;margin-top: 3px;"/>';
            }
            else {
                echo '<Label id="shopImgLabel'.($i+1).'" style="margin-left: 0px;border-style: solid;border-width: 1px;"/>';
                echo '<input id="shopImg'.($i+1).'" name = "shopImg'.($i+1).'" type="file" value="0" style="font-size:12pt; color:#000000;margin-top: 3px;"/>';
            }

            echo '<div style="margin-top: 3px;">';
            echo '<input type="submit" value="등록"/>';
            echo '<input type="button" onclick="onRemoveImgClick('.($i+1).')" value="삭제"/>';
            echo '</div>';
            echo '</form>';
            echo '</td>';
            echo '</tr>';
        }
        ?>
</div>


<script type="text/javascript">

    function saveInfo() {
        var productID = document.getElementById("container").getAttribute("value");
        var productName = document.getElementById("productName").value;
        var productPrice = document.getElementById("productPrice").value;
        var productEventPrice = document.getElementById("productEventPrice").value;

        $.ajax({
            url: "update_product",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                product_id:productID,
                product_name:productName,
                product_price:productPrice,
                product_event_price:productEventPrice
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    alert("변경되었습니다.");
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
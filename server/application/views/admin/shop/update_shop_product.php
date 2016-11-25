<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>상품변경</title>
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

<div id="container" value="<?php echo $product['id'] ?>">
    <p/>
    <div class="input-group">
        <label>상품이름:   </label>
        <input id="productName" type="text" name="productName" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $product['productName'] ?>"/>
    </div>

    <div class="input-group">
        <label >가격:   </label>
        <input id="productPrice" type="text" name="productPrice" size="10" value="<?php echo $product['productPrice'] ?>" style="font-size:12pt; color:#000000; width: 100px"/>
    </div>

    <p/>

    <div class="input-group">
        <label >이벤트가격:   </label>
        <input id="productEventPrice" type="text" name="productEventPrice" size="10" value="<?php echo $product['productEventPrice'] ?>"  style="font-size:12pt; color:#000000; width: 100px"/>
    </div>

    <p/>

    <div id="submit_container">
        <?php
        echo '<button type="button" class="btn btn-default" onclick="saveInfo()" id="btn_transfer"/> <span aria-hidden="true">전송</span></button>';
        ?>
    </div>

    <p/>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

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
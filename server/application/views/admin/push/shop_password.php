<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>미끌</title>
    <link href="<?php echo base_url(); ?>assets/css/admin/global.css" rel="stylesheet" type="text/css">
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

<div class="container top">
    <label>상점명 입력: </label>
    <p/>
    <div class="input-group" id="shop_name_container" value = "<?php echo $shop['shopOwnerID']?>">
        <input id="shop_name" type="text" size="10" style="font-size:12pt; color:#000000;width:320px" value="<?php echo $shop['shopID']?>"/>
        <button onclick="onSearchShopName()">검색</button>
    </div>

    <p/>

    <input id="password" type="text" size="10" style="font-size:12pt; color:#000000;width:380px" value="<?php echo $shop['shopPW']?>"/>

    <div id="submit_container">
        <button onclick="onSend()">전송</button>
    </div>
</div>

<script>
    function onSearchShopName() {
        var shop_name = $("#shop_name").val();
        if(shop_name == "") {
            alert("상점이름을 입력해주세요.");
            return;
        }

        location.href = "shop_password?shop_name="+encodeURI(shop_name);
    }

    function onSend() {
        var shop_owner =  document.getElementById("shop_name_container").getAttribute("value");
        var shop_password = $("#password").val();

        if(shop_owner == 0 || shop_owner == null) {
            alert("상점주인이 등록되어 있지 않습니다.");
            return;
        }

        if(shop_password == "") {
            alert("암호가 없습니다.");
            return;
        }

        $.ajax({
            url: "send_shop_password",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                shop_owner: shop_owner,
                shop_password: shop_password
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
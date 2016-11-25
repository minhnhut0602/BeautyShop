<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>상점추가</title>
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

<div id="container">
    <p/>
    <div class="input-group">
        <label>상점명:   </label>
        <input id="shopName" type="text" name="shopName" size="10" style="font-size:12pt; color:#000000; width: 300px"/>

    </div>

    <p/>

    <div class="input-group">
        <label>카테고리:   </label>
        <select class="combobox" id="shopCategory">
            <option value="2" selected> 헤어샵 </option>
            <option value="1"> 네일샵 </option>
        </select>
    </div>

    <p/>

    <div class="input-group">
        <label>상태:   </label>
        <select class="combobox" id="shopStatus">
            <?php
            echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
            echo '<option value="'.STATUS_MANAGE.'"> 운영 </option>';
            echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
            ?>
        </select>
    </div>

    <p/>

    <div class="input-group">
        <label>이벤트:   </label>
        <select id="shopEventable">
            <option value="Y" selected> 진행 </option>
            <option value="N"> 중지 </option>
        </select>
    </div>

    <p/>

    <div class="input-group">
        <label >별점(평점):   </label>
        <input id="shopRemark" type="text" name="shopRemark" size="10" value="0"  style="font-size:12pt; color:#000000; width: 40px"/>
    </div>

    <p/>

    <div class="input-group">
        <label >운영기간:   </label>
        <input id="shopLiveDate" type="text" name="shopLiveDate" size="10" value='10'  style="font-size:12pt; color:#000000; width: 40px"/>
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
    var isCreateShop = false;

    function saveInfo() {
        var shopName = document.getElementById("shopName").value;
        var shopCategory = document.getElementById("shopCategory").value;
        var shopStatus = document.getElementById("shopStatus").value;
        var shopEventable = document.getElementById("shopEventable").value;
        var shopRemark = parseFloat(document.getElementById("shopRemark").value);
        var shopLiveDate = parseInt(document.getElementById("shopLiveDate").value);

        $.ajax({
            url: "create_shop",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                shop_category_id:shopCategory,
                shop_status:shopStatus,
                shop_eventable:shopEventable,
                shop_remark:shopRemark,
                shop_live_date:shopLiveDate,
                shop_name:shopName
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    isCreateShop = true;
                    alert("상정이 등록되었습니다.");
                    location.href ='shop?page_num=1';
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
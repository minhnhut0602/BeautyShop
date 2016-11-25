
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>미미샵</title>
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

<div class="container top">

    <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;">
        <div class="row">
            <div class="col-md-12">
                <table class="table table-bordered" style="padding: 5px;">
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>상점이름 </label></td>
                        <td>
                            <input id="shopName" type="text" style="width: 60%;" value="<?php echo $shopName;?>"/>
                            <button type="button" class="btn btn-default" onclick="search()"/> 찾기</button>
                        </td>
                        <td style="background:#eeebdc;text-align: center;"><label>별점</label></td>
                        <td>
                            <select class="combobox" id="shopOrderType">
                                <?php
                                if($shopOrderType != null) {
                                    echo '<option value="-1"> 전체 </option>';
                                    if($shopOrderType == 0) {
                                        echo '<option value="0" selected> 높은순 </option>';
                                        echo '<option value="1"> 낮은순 </option>';
                                    }
                                    else if($shopOrderType == 1){
                                        echo '<option value="0" > 높은순 </option>';
                                        echo '<option value="1" selected> 낮은순 </option>';
                                    }
                                }
                                else {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="0"> 높은순 </option>';
                                    echo '<option value="1"> 낮은순 </option>';
                                }

                                ?>
                            </select>
                            <label>/</label>
                            <select class="combobox" id="shopStatus">
                                <?php
                                if($shopStatus == null) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'"> 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                else if($shopStatus == STATUS_MANAGE) {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'" selected> 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                else if($shopStatus == STATUS_REQUEST) {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'" selected> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'" > 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                else if($shopStatus == STATUS_NON_MANAGE) {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'"> 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'" selected> 비운영 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="row" style="text-align: center;margin: 5px;">
            <button type="button" class="btn btn-default" onclick="search()"/> 검색</button>
            <button type="button" class="btn btn-default" onclick="searchAllList()"/> 전체목록</button>
        </div>
    </div>


    <div class="row" style="margin-top: 10px;">
        <div id = "panel_comment" value="<?php echo $shopID?>">
            <table class="table table-bordered table-scrollable">
                <colgroup>
                    <col width="5%">
                    <col width="10%">
                    <col width="30%">
                    <col width="30%">
                    <col width="10%">
                    <col width="15%">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"  class="base-table-header" >
                        <input type="checkbox" id="check-all" name="check-all" onchange="onCheckAll(this)"/>
                    </th>
                    <th scope="col" class="base-table-header" >번호</th>
                    <th scope="col" class="base-table-header" >상점이름</th>
                    <th scope="col" class="base-table-header" >평점</th>
                    <th scope="col" class="base-table-header" >상태</th>
                    <th scope="col" class="base-table-header" >선택</th>
                </tr>
                </thead>

                <tbody id="list">
                <?php
                $cnt = count($arrShop);

                if($cnt == 0) {
                    echo '<tr class="__oldlist" style="">';
                    echo '<td colspan="9">없습니다.</td>';
                    echo '</tr>';
                }
                else {
                    foreach ($arrShop as $row) {
                        echo '<tr>';
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                        echo '<td>' . $row['id'] . '</td>';
                        echo '<td>' . $row['shopName'] . '</td>';
                        echo '<td>' . $row['shopLevel'] . '</td>';
                        if($row['shopStatus'] == STATUS_MANAGE) {
                            echo '<td>  운영 </td>';
                        }
                        else  if($row['shopStatus'] == STATUS_NON_MANAGE){
                            echo '<td>  비운영 </td>';
                        }
                        else if($row['shopStatus'] == STATUS_REQUEST) {
                            echo '<td>  신청중 </td>';
                        }

                        echo '<td> <button type="button" class="btn btn-default" onclick="selectShop('. $row['id'].')"/> 선택하기</button></td>';
                        echo '</tr>';
                    }
                }
                ?>
                </tbody>
            </table>
        </div>
    </div>

    <nav class="nav_page_bar" id="nav_page_bar">
        <ul class="pagination" id="page_bar">
            <li>
                <button type="button" class="btn btn-default" aria-label="Previous" onclick="prevPage()"/> <span aria-hidden="true">&laquo;</span></button>
            </li>
            <li>
                <input id="currentPage" type="text" name="currentPage" size="10" value='<?php echo $currentPageNum ?>'  style="font-size:12pt; color:#000000; width: 40px"
                       onkeypress="return searchKeyPress(event);"/>
            </li>
            <li id="totalPage">  <?php echo $totalPageCnt ?> </li>
            <li>
                <button type="button" class="btn btn-default" aria-label="Next" onclick="nextPage()"/> <span aria-hidden="true">&raquo;</span></button>
            </li>
        </ul>
    </nav>

    <button type="button" class="btn btn-default" onclick="selectAll()"/> 일괄선택</button>

</div>

<script>

    var arrSelectedShop = new Array;

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            searchByPage(currentPage);
            return false;
        }
        return true;
    }

    function prevPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        currentPage = currentPage - 1;
        if(currentPage <= 0) {
            currentPage = 1;
        }
        var shop_id = document.getElementById('panel_comment').getAttribute("value");
        searchByPage(currentPage);
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        searchByPage(currentPage);
    }

    function search() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        searchByPage(currentPage);
    }

    function searchByPage(page_num) {
        var shopName = $('#shopName').val();
        var shopStaus = $('#shopStatus').find('option:selected').val();
        var shopOrderType = $('#shopOrderType').find('option:selected').val();

        if(shopStaus == -1) {
            shopStaus = null;
        }

        if(shopOrderType == -1) {
            shopOrderType = null;
        }

        searchBy(page_num, shopName, shopStaus, shopOrderType);
    }

    function searchAllList() {
        $('#shopName').val('');
        $('#shopStaus').val(-1);
        $('#shopOrderType').val(-1);

        searchBy(1, null, null, null);
    }

    function searchBy(page_num, shop_name, shop_status, order_type) {

        if(page_num == null) {
            page_num = 1;
        }

        var url = getBaseURL()+'/shop/find_shop?page_num='+page_num;

        if(shop_name != null && shop_name != "") {
            url = url + '&shop_name='+encodeURI(shop_name);
        }

        if(shop_status != null) {
            url = url + '&shop_status='+shop_status;
        }

        if(order_type != null) {
            url = url + '&order_type='+order_type;
        }

        location.href = url;
    }

    function selectAll() {
        var checkboxes = null;

        checkboxes = document.getElementsByName('checked');

        var n = checkboxes.length;

        arrSelectedShop = [];

        for (var i = (n - 1); i >= 0; i--) {
            if (checkboxes[i].checked == true) {
                arrSelectedShop.push(checkboxes[i].value);
            }
        }

        window.close();
    }

    function onCheckAll(checked) {
        var checkboxes = null;

        checkboxes = document.getElementsByName('checked');

        var n = checkboxes.length;
        for (var i = (n - 1); i >= 0; i--) {
                checkboxes[i].checked = checked.checked;
        }
    }

    function selectShop(id) {
        arrSelectedShop = [];
        arrSelectedShop.push(id);

        window.close();
    }

    function onExit() {
        try {
            window.opener.HandlePopupResult(arrSelectedShop);
        }
        catch (err) {}

    }

</script>


</body>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

</html>
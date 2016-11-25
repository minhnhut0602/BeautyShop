
<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row input {float:right;}
</style>

<div class="container" id="main_content">

    <div class="row">

        <div style="display: flex;justify-content: center;">
            <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;width: 60%;">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered" style="padding:5px;">
                            <tr>
                                <td class="base-search-table-header"><label>별점 </label></td>
                                <td>
                                    <select id="orderType">
                                        <?php
                                        $arrType = Array("전체", "높은순", "낮은순");
                                        for($i = 0; $i < count($arrType); $i++) {
                                            if($orderType == $i) {
                                                echo '<option value="'.$i.'" selected> '.$arrType[$i].' </option>';
                                            }
                                            else {
                                                echo '<option value="'.$i.'"> '.$arrType[$i].' </option>';
                                            }
                                        }
                                        ?>
                                    </select>
                                </td>
                            </tr>

                            <tr>
                                <td class="base-search-table-header"><label>작성자 </label></td>
                                <td>
                                    <input id="userID" type="text" style="width: 100%;" value="<?php echo $postMan;?>"/>
                                </td>
                            </tr>

                            <tr>
                                <td class="base-search-table-header"><label>상점이름 </label></td>
                                <td>
                                    <input id="shopName" type="text" style="width: 100%;" value="<?php echo $shopName;?>"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>

                <div class="row" style="text-align: center;margin: 5px;" style="display: flex;justify-content: right;">
                    <button type="button" class="btn btn-default" onclick="onClickSearch()"/> 검색</button>
                    <button type="button" class="btn btn-default" onclick="onClickSearchAll()"/> 전체목록</button>
                </div>

            </div>

        </div>
    </div>

    <div id="tabs-cast" style="margin-top: 5px;">
        <table class="table table-bordered table-scrollable" id="tb_comment_list">
            <colgroup>
                <col width="5%">
                <col width="15%">
                <col width="10%">
                <col width="40%">
                <col width="10%">
                <col width="15%">
                <col width="5%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col" class="base-table-header" >번호</th>
                <th scope="col" class="base-table-header" >상점이름</th>
                <th scope="col" class="base-table-header" >별점</th>
                <th scope="col" class="base-table-header" >내용</th>
                <th scope="col" class="base-table-header" >작성자</th>
                <th scope="col" class="base-table-header" >작성일</th>
                <th scope="col" class="base-table-header" >선택</th>
            </tr>
            </thead>

            <tbody id="list">

            <?php

            $tempArray = $arrShopComment;
            $cnt = count($tempArray);

            if($cnt == 0) {
                echo '<tr class="no_data" style="">';
                echo '<td colspan="10">없습니다.</td>';
                echo '</tr>';
            }
            else {
                $j = 1;
                foreach ($tempArray as $row) {
                    echo '<tr>';
                    echo '<td>' .( $j + ($currentPageNum - 1) * ADMIN_MAX_PAGE_ITEM_CNT) . '</td>';
                    echo '<td onclick="showShopUserRemark('.$row['id'].')" style="cursor:pointer;">' . $row['shopName'] . '</td>';
                    echo '<td>' . $row['shopcommentShopLevel'] . '</td>';
                    echo '<td>' . $row['content'] . '</td>';
                    echo '<td>' .$row['postMan']. '</td>';
                    echo '<td>' .$row['postTime']. '</td>';
                    if($row['shopcommentStatus'] == STATUS_LIFE) {
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                    }
                    else {
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" checked/>' . '</td>';
                    }
                    echo '</tr>';

                    $j++;
                }
            }
            ?>
            </tbody>
        </table>
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

    <div>
        <button type="button" class="btn btn-default" onclick="AddComment()">글쓰기</button>
        <div style="float: right;">
            <button type="button" class="btn btn-default" onclick="RemoveComment()">삭제</button>
        </div>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/admin.js"></script>

<script type="text/javascript">

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            search(currentPage);
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

        search(currentPage);
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        search(currentPage);
    }

    function getOrderType() {
        var orderType = document.getElementById("orderType").value;
        if(orderType == null || isUndifined(orderType) || isNaN(orderType) == true || orderType == "") {
            return null;
        }

        return parseInt(orderType);
    }


    function onClickSearch() {
        var currentPage = parseInt(document.getElementById('currentPage').value);

        search(currentPage);
    }

    function onClickSearchAll() {
        $('#orderTypa').val(0);
        $('#shopName').val('');
        $('#userID').val('');

        searchWithCondition(1, null, null, null);
    }

    function search(page_num) {
        var shopName = document.getElementById('shopName').value;
        var postMan = document.getElementById('userID').value;
        var orderType = getOrderType();

        searchWithCondition(page_num, shopName, postMan, orderType);
    }

    function searchWithCondition(page_num, shop_name, post_man, order_type) {

        if(page_num == null) {
            page_num = 1;
        }

        var url  = getBaseURL()+'/shop/use_remark?page_num='+page_num;

        if(shop_name != null && shop_name != "") {
            url += '&shop_name='+encodeURI(shop_name);
        }

        if(order_type != null) {
            url += '&order_type='+order_type;
        }


        if(post_man != null && post_man != "") {
            url += '&post_man='+encodeURI(post_man);
        }

        location.href = url;
    }

    function getCheckedIDArray() {
        var checkboxes = null;

        checkboxes = document.getElementsByName('checked');

        var n = checkboxes.length;
        var arrCastID  = new Array;
        for (var i = (n - 1); i >= 0; i--) {
            if (checkboxes[i].checked == true) {
                arrCastID.push(checkboxes[i].value);
            }
        }

        return JSON.stringify(arrCastID);
    }

    function RemoveComment() {

        var strJSONIDArray = getCheckedIDArray();

        var arrID = JSON.parse(strJSONIDArray);

        if(arrID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: "remove_user_remark_array",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    arr_comment:strJSONIDArray
                },
                success: function (response) {

                    if(response['result_code'] == 0) {
                        location.reload(true);
                    }
                    else {
                        alert("삭제실패!");
                    }
                },
                error: function (jqXHR, msg, erro) {
                    alert("전송 실패! 망연결을 확인해주세요.");
                }
            });
        }, function no()
        {
            // do nothing
        });
    }

    function AddComment() {
        $.ajax({
            url: "write_use_remark",
            type: "post", // To protect sensitive data
            data: {
                ajax: true
            },
            success: function (response) {

                $('#main_content').html(response);
            },
            error: function (jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

</script>
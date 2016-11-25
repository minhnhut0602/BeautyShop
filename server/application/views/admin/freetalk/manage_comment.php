
<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row input {float:right;}
</style>

<link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap-datepicker3.css" rel="stylesheet" type="text/css">

<div class="container" id="main_content">

    <div class="row">

        <div style="display: flex;justify-content: center;">
            <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;width: 60%;">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered" style="padding:5px;">
                            <tr>
                                <td class="base-search-table-header"><label>작성일자 </label></td>
                                <td>
                                    <div id="from-date-container" style="width: 100%;">
                                        <table>
                                            <tbody>
                                            <tr>
                                                <td><input type="text" type="text" class="form-control" value="<?php echo $startDate?>" id="fromDate"/></td>
                                                <td><label> ~ </label></td>
                                                <td><input type="text" type="text" class="form-control"  value="<?php echo $endDate?>" id="toDate"/><td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="base-search-table-header"><label>작성자 </label></td>
                                <td>
                                    <input id="userID" type="text" style="width: 100%;" value="<?php echo $postMan;?>"/>
                                </td>
                            </tr>

                            <tr>
                                <td class="base-search-table-header"><label>댓글위치 </label></td>
                                <td>
                                    <select id="commentType">
                                        <?php
                                        $arrType = Array("전체", "자유톡", "캐스트", "이벤트", "신고");
                                        for($i = 0; $i < 5; $i++) {
                                            if($type == $i) {
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
                        </table>
                    </div>
                </div>

                <div class="row" style="text-align: center;margin: 5px;" style="display: flex;justify-content: right;">
                    <button type="button" class="btn btn-default" onclick="onClickSearch()"/> 검색</button>
                    <button type="button" class="btn btn-default" onclick="onClickSearchAll()"/> 전체목록</button>
                </div>

            </div>

        </div>

        <div style="float: right;margin-top: 5px;">
            <div class="col-md-6 btn-group btn-group-sm" role="group" id="order-type" style="width: 300px;" value="<?php echo $orderType?>">
                <?php
                $arrOrderType = Array("등록순", "좋아요순");

                for($i = 0; $i < count($arrOrderType); $i++) {
                    if($orderType != null && $orderType == $i) {
                        echo '<button type="button" class="btn btn-default btn-default-sm active" onclick="onClickOrderType('.$i.')">'.$arrOrderType[$i].'</button>';
                    }
                    else {
                        echo '<button type="button" class="btn btn-default btn-default-sm" onclick="onClickOrderType('.$i.')">'.$arrOrderType[$i].'</button>';
                    }
                }
                ?>
            </div>
        </div>

    </div>

    <div style="text-align: center;">
        <?php
            $arrType = Array("전체", "자유톡", "캐스트", "이벤트", "신고");
            if($type == null) {
                $type = 0;
            }
            echo '<font size="5">'.$arrType[$type].'댓글</font>';
        ?>
    </div>

    <div id="tabs-cast" style="margin-top: 5px;">
        <table class="table table-bordered table-scrollable" id="tb_comment_list">
            <colgroup>
                <col width="5%">
                <col width="10%">
                <col width="10%">
                <col width="25%">
                <col width="10%">
                <col width="15%">
                <col width="10%">
                <col width="10%">
                <col width="5%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col" class="base-table-header" >번호</th>
                <th scope="col" class="base-table-header" >위치</th>
                <th scope="col" class="base-table-header" >카테</th>
                <th scope="col" class="base-table-header" >내용</th>
                <th scope="col" class="base-table-header" >작성자</th>
                <th scope="col" class="base-table-header" >작성일</th>
                <th scope="col" class="base-table-header" >좋아요</th>
                <th scope="col" class="base-table-header" >신고</th>
                <th scope="col" class="base-table-header" >선택</th>
            </tr>
            </thead>

            <tbody id="list">

            <?php

            $tempArray = $arrComment;
            $arrType = Array("전체", "자유톡", "캐스트", "이벤트", "자유톡");
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
                    echo '<td id="type'.$row['id'].'" value="'.$row['type'].'">'.$arrType[$row['type']].'</td>';
                    if($row['type'] == 3) {
                        echo '<td>' . 배너 . '</td>';
                    }
                    else {
                        echo '<td>' . $row['categoryName'] . '</td>';
                    }
                    echo '<td>' . $row['content'] . '</td>';
                    echo '<td>' .$row['postMan']. '</td>';
                    echo '<td>' .$row['postTime']. '</td>';
                    echo '<td>' .$row['heartCnt']. '</td>';
                    echo '<td>' .$row['declareCnt']. '</td>';
                    if($row['status'] == STATUS_LIFE) {
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
        <div style="float: right;">
            <button type="button" class="btn btn-default" onclick="RemoveComment()">삭제</button>
        </div>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.js"></script>

<script type="text/javascript">

    var curTabIndex = 0; // 0:all-cast 1:best-cast

    $('#from-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });
    $('#to-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });

    function onClickTab(idx) {
        curTabIndex = idx;
    }

    jQuery(document).ready(function ($) {
        $('#tabs').tab();
    });

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
        var orderType = document.getElementById("order-type").getAttribute("value");
        if(orderType == null || isUndifined(orderType) || isNaN(orderType) == true || orderType == "") {
            return null;
        }

        return parseInt(orderType);
    }

    function search(page_num) {
        var commentType = document.getElementById('commentType').value;
        var startDate = document.getElementById('fromDate').value;
        var endDate = document.getElementById('toDate').value;
        var postMan = document.getElementById('userID').value;
        var orderType = getOrderType();

        searchWithCondition(page_num, commentType, startDate, endDate, postMan, orderType);
    }

    function onClickOrderType(idx) {
        var orderType =  getOrderType();

        $('#order-type').children().each(function(child_idx, val){
            if(child_idx == idx) {
                $(this).toggleClass("active");
            }
            else {
                if(child_idx == orderType) {
                    $(this).toggleClass("active");
                }
            }
        });

        document.getElementById("order-type").setAttribute("value", idx);

        onClickSearch();
    }

    function onClickSearch() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);
        var orderType =  getOrderType();
        var commentType = document.getElementById('commentType').value;
        var startDate = document.getElementById('fromDate').value;
        var endDate = document.getElementById('toDate').value;
        var postMan = document.getElementById('userID').value;

        searchWithCondition(currentPage, commentType, startDate, endDate, postMan, orderType);
    }

    function onClickSearchAll() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);
        var orderType = getOrderType();

        $('#commentType').val(0);
        $('#fromDate').val('');
        $('#toDate').val('');
        $('#userID').val('');

        searchWithCondition(currentPage, null, null, null, null, orderType);
    }

    function searchWithCondition(page_num, commentType, start_date, end_date, post_man, order_type) {

        if(page_num == null) {
            page_num = 1;
        }

        var url  = getBaseURL()+'/freetalk/manage_comment?page_num='+page_num;

        if(commentType != null) {
            url += '&type='+commentType;
        }

        if(start_date != null && start_date != "") {
            url += '&start_date='+start_date;
        }

        if(end_date != null && end_date != "") {
            url += '&end_date='+end_date;
        }

        if(post_man != null && post_man != "") {
            url += '&post_man='+encodeURI(post_man);
        }

        if(order_type != null) {
            url += '&order_type='+order_type;
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

    function getCommentTypeArray(arrCastArray) {
        var arrCastID  = new Array;
        for (var i = 0; i < arrCastArray.length; i++) {
                var type = document.getElementById('type'+arrCastArray[i]);
                arrCastID.push(parseInt(type.getAttribute("value")));
        }
        return JSON.stringify(arrCastID);
    }

    function RemoveComment() {

        var strJSONCastIDArray = getCheckedIDArray();
        var strJSONTypeArray = getCommentTypeArray(JSON.parse(strJSONCastIDArray));

        var arrCastID = JSON.parse(strJSONCastIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: "remove_comment_array",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    arr_comment:strJSONCastIDArray,
                    arr_type:strJSONTypeArray
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

    function AddLikeCount() {
        var strJSONCastIDArray = getCheckedCastArray();
        $.ajax({
            url: "add_like_count",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                arr_freetalk_id: strJSONCastIDArray
            },
            success:function(response){
                alert("좋아요 늘이기 성공!");
                location.reload();
            },
            error:function(jqXHR, msg, erro) {
                alert("추가 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function SetBest() {
        var strJSONCastIDArray = getCheckedCastArray();
        var arrCastID = JSON.parse(strJSONCastIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택한 게시물을 best 게시물로 선정하시겠습니까?", function yes()
        {
            $.ajax({
                url: "set_best_freetalk",
                type: "post", // To protect sensitive data
                data: {
                    ajax:true,
                    arr_freetalk_id: strJSONCastIDArray
                },
                success:function(response){
                    if(response['result_code'] == 0) {
                        alert("베스트 선정 성공!");
                        location.reload();
                    }
                    else {
                        alert(response['result_msg']);
                    }
                },
                error:function(jqXHR, msg, erro) {
                    alert("추가 실패! 망연결을 확인해주세요.");
                }
            });
        }, function no()
        {
            // do nothing
        });
    }

</script>
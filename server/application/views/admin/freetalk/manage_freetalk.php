
<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row input {float:right;}
</style>

<link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap-datepicker3.css" rel="stylesheet" type="text/css">

<div class="container" id="main_panel">

    <div class="row">

        <div style="display: flex;justify-content: center;">
            <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;width: 60%;">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered" style="padding:5px;">
                            <tr>
                                <td class="base-search-table-header"><label>카테고리 </label></td>
                                <td>
                                    <select id="freetalkBest">
                                        <?php
                                        if($isBest == true) {
                                            echo '<option value="0" > 전체 </option>';
                                            echo '<option value="1" selected> 베스트 </option>';
                                        }
                                        else  {
                                            echo '<option value="0" selected> 전체 </option>';
                                            echo '<option value="1" > 베스트 </option>';
                                        }
                                        ?>
                                    </select>
                                    <select class="combobox" id="freetalkCategory">
                                        <?php
                                        if($selectedCateogryID == null || $selectedCateogryID == 0) {
                                            echo '<option value="0" selected> 전체 </option>';
                                        }
                                        else {
                                            echo '<option value="0"> 전체 </option>';
                                        }

                                        $cnt = count($arrCategory);
                                        for($i = 0; $i < $cnt; $i++) {
                                            if($selectedCateogryID != null && $arrCategory[$i]['id'] == $selectedCateogryID) {
                                                echo '<option value="' . $arrCategory[$i]['id'] . '" selected>' . $arrCategory[$i]['categoryName'] . '</option>';
                                            }
                                            else {
                                                echo '<option value="' . $arrCategory[$i]['id'] . '">' . $arrCategory[$i]['categoryName'] . '</option>';
                                            }
                                        }
                                        ?>
                                    </select>
                                </td>
                            </tr>

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
                $arrOrderType = Array("등록순", "좋아요순", "댓글순", "신고순");

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

    <div id="tabs-cast" style="margin-top: 5px;">
        <?php
        echo '<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">';

        $arrTab = Array("전체 게시물", "Best 게시물");

        for($i = 0; $i < count($arrTab); $i++) {
            if($i == $sub_tab_id) {
                echo '<li class="active" onclick="onClickTab('.$i.')"><a href="#freetalk-panel'.$i.'" data-toggle="tab">'.$arrTab[$i].'</a></li>';
            }
            else {
                echo '<li onclick="onClickTab('.$i.')"><a href="#freetalk-panel'.$i.'" data-toggle="tab">'.$arrTab[$i].'</a></li>';
            }
        }

        echo '</ul>';

        echo '<div id="my-tab-content" class="tab-content">';

        for($i = 0; $i < count($arrTab); $i++) {
            if($i == $sub_tab_id) {
                echo '<div class="tab-pane active" id="freetalk-panel'.$i.'"  style="border-style:solid;border-color: #cccccc;border-width: 1px;">';
            }
            else {
                echo '<div class="tab-pane" id="freetalk-panel'.$i.'"  style="border-style:solid;border-color: #cccccc;border-width: 1px;">';
            }

            echo '<div style="margin-left: 0px;margin-top: 5px;padding-left: 5px;padding-right: 5px;" id="tab-freetalk">';
            echo        '<table class="table table-bordered table-scrollable" id="tb_freetalk_list">
                                            <colgroup>
                                                <col width="5%">
                                                <col width="10%">
                                                <col width="35%">
                                                <col width="10%">
                                                <col width="15%">
                                                <col width="10%">
                                                <col width="5%">
                                                <col width="5%">
                                                <col width="5%">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th scope="col" class="base-table-header" >번호</th>
                                                <th scope="col" class="base-table-header" >카테고리</th>
                                                <th scope="col" class="base-table-header" >내용</th>
                                                <th scope="col" class="base-table-header" >작성자</th>
                                                <th scope="col" class="base-table-header" >작성일</th>
                                                <th scope="col" class="base-table-header" >좋아요</th>
                                                <th scope="col" class="base-table-header" >댓글</th>
                                                <th scope="col" class="base-table-header" >신고</th>
                                                <th scope="col" class="base-table-header" >선택</th>
                                            </tr>
                                            </thead>

                                            <tbody id="list">';

            $tempArray = $arrFreetalk;
            if($i == 1) { // best freetalk
                $tempArray = $arrBestFreetalk;
            }
            else {
                $tempArray = $arrFreetalk;
            }

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
                    echo '<td>' . $row['freetalkCategoryName'] . '</td>';
                    $arrImageID = json_decode($row['freetalkImgIDArrayString'], true);
                    if(count($arrImageID) == 0) {
                        echo '<td  onclick="showFreetalk('.$row['id'].')" style="cursor:pointer;">' .$row['freetalkContent']. '</td>';
                    }
                    else {
                        echo '<td  onclick="showFreetalk('.$row['id'].')" style="cursor:pointer;"> <font color="#0EA2F1" size="3"> & </font>' .$row['freetalkContent']. '</td>';
                    }

                    echo '<td>' .$row['freetalkPostManUserID']. '</td>';
                    echo '<td>' .$row['freetalkRealPostTime']. '</td>';
                    echo '<td>' .$row['freetalkHeartCnt']. '</td>';
                    echo '<td>' .$row['freetalkCommentCnt']. '</td>';
                    echo '<td>' .$row['freetalkDeclareCnt']. '</td>';
                    if($row['freetalkStatus'] == STATUS_LIFE) {
                        echo '<td>' .' <input type="checkbox" id="checked'.$i.'" name="checked'.$i.'" value="'. $row['id'] .'"/></td>';
                    }
                    else {
                        echo '<td>' .' <input type="checkbox" id="checked'.$i.'" name="checked'.$i.'" value="'. $row['id'] .'" checked/>' . '</td>';
                    }
                    echo '</tr>';

                    $j++;
                }
            }

            echo  '</tbody>
                                </table>
                            </div>
                        </div>';
        }

        echo '</div>';
        ?>
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
        <button type="button" class="btn btn-default" onclick="NewFreetalk()">글쓰기</button>

        <div style="float: right;">
            <button type="button" class="btn btn-default" onclick="AddLikeCount()">좋야요 늘리기</button>
            <button type="button" class="btn btn-default" onclick="SetBest()">Best선정/해제</button>
            <button type="button" class="btn btn-default" onclick="RemoveFreetalk()">삭제</button>
        </div>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script type="text/javascript">

    var curTabIndex = 0; // 0:all-cast 1:best-cast  0:등록순 1:좋아요순 2:댓글순

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


    function getOrderType() {
        var orderType = document.getElementById("order-type").getAttribute("value");
        if(orderType == null || isUndifined(orderType) || isNaN(orderType) == true || orderType == "") {
            return null;
        }

        return parseInt(orderType);
    }

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

    function search(page_num) {
        var categoryID = document.getElementById('freetalkCategory').value;
        var isBest = document.getElementById('freetalkBest').value;
        var startDate = document.getElementById('fromDate').value;
        var endDate = document.getElementById('toDate').value;
        var postMan = document.getElementById('userID').value;
        var curOrderType =  getOrderType();

        searchWithCondition(page_num, curTabIndex, categoryID, isBest, startDate, endDate, postMan, curOrderType);
    }

    function onClickOrderType(idx) {
        var curOrderType =  getOrderType();

        $('#order-type').children().each(function(child_idx, val){
            if(child_idx == idx) {
                $(this).toggleClass("active");
            }
            else {
                if(child_idx == curOrderType) {
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
        var tabIdx = curTabIndex;
        var categoryID = document.getElementById('freetalkCategory').value;
        var isBest = document.getElementById('freetalkBest').value;
        var startDate = document.getElementById('fromDate').value;
        var endDate = document.getElementById('toDate').value;
        var postMan = document.getElementById('userID').value;

        if(isBest == 0) {
            isBest = null;
        }

        searchWithCondition(currentPage, tabIdx, categoryID, isBest, startDate, endDate, postMan, orderType);
    }

    function onClickSearchAll() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);
        var orderType =  getOrderType();
        var tabIdx = curTabIndex;

        $('#freetalkCategory').val(0);
        $('#freetalkBest').val(0);
        $('#fromDate').val('');
        $('#toDate').val('');
        $('#userID').val('');

        searchWithCondition(currentPage, tabIdx, null, null, null, null, null, orderType);
    }

    function searchWithCondition(page_num, tab_idx, category_id, is_best, start_date, end_date, cast_post_man, order_type) {

        if(page_num == null) {
            page_num = 1;
        }

        var url  = getBaseURL()+'/freetalk/manage_freetalk?page_num='+page_num;

        if(tab_idx != null) {
            url += '&tab_idx='+tab_idx;
        }

        if(category_id != null && category_id != 0) {
            url += '&category_id='+category_id;
        }


        if(is_best != null && is_best != 0) {
            url += '&is_best='+true;
        }

        if(start_date != null && start_date != "") {
            url += '&start_date='+start_date;
        }

        if(end_date != null && end_date != "") {
            url += '&end_date='+end_date;
        }

        if(cast_post_man != null && cast_post_man != "") {
            url += '&cast_post_man='+encodeURI(cast_post_man);
        }

        if(order_type != null) {
            url += '&order_type='+order_type;
        }

        location.href = url;
    }

    function getCheckedCastArray() {
        var checkboxes = null;

        checkboxes = document.getElementsByName('checked'+curTabIndex);

        var n = checkboxes.length;
        var arrCastID  = new Array;
        for (var i = (n - 1); i >= 0; i--) {
            if (checkboxes[i].checked == true) {
                arrCastID.push(checkboxes[i].value);
            }
        }

        return JSON.stringify(arrCastID);
    }

    function RemoveFreetalk() {

        var strJSONCastIDArray = getCheckedCastArray();

        var arrCastID = JSON.parse(strJSONCastIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: getBaseURL()+"/freetalk/remove_freetalk_array",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    arr_freetalk_id:strJSONCastIDArray
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

        var isSetBest = false;

        if(curTabIndex == 0) {
            isSetBest = true;
        }

        var strMsg = "선택한 게시물을 best 게시물로 선정하시겠습니까?"
        if(isSetBest == false) {
            strMsg = "선택한 게시물을 best게시물에서 해제하시겠습니까?"
        }

        doConfirm(strMsg, function yes()
        {
            $.ajax({
                url: "set_best_freetalk",
                type: "post", // To protect sensitive data
                data: {
                    ajax:true,
                    arr_freetalk_id: strJSONCastIDArray,
                    is_set_best:curTabIndex
                },
                success:function(response){
                    if(response['result_code'] == 0) {
                        if(isSetBest == true) {
                            alert("베스트 선정 성공!");
                        }
                        else {
                            alert("베스트 해제 성공!");
                        }
                    }
                    else {
                        alert(response['result_msg']);
                    }
                    location.reload();
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


    function NewFreetalk() {
        $.ajax({
            url: "add_modify_freetalk?horizental=1",
            type: "get", // To protect sensitive data
            data: {
                ajax:true
            },
            success:function(response){
                $('#main_panel').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }


</script>
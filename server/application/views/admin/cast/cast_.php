<div class="container" xmlns="http://www.w3.org/1999/html" id="root" xmlns="http://www.w3.org/1999/html">
    <div class="row">
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-5">
                    <select class="combobox" id="castCategory">
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
                    <span/>
                    <button type="button" class="btn btn-default btn-sm" onclick="onClickAddCategory()">카테고리추가</button>

                </div>

                <div class="col-md-6 btn-group btn-group-sm" role="group" id="order-type" value="<?php echo $orderType ?>">
                    <?php
                        $arrOrderType = Array("등록순", "좋아요순", "댓글순");

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

            <div class="col-md-12 input-group input-group-sm" style="margin-top: 5px;">
                <input type="text" class="form-control " placeholder="Search" id="search_text" style="width: 83%;" value="<?php echo $searchWord?>">
                <button type="button" class="btn btn-default  btn-sm" onclick="onClickSearch()" style="width: 15%;margin-left: 5px;">검색</button>
                </input>
            </div>

            <div  class="col-md-12" align="right">
                <?php
                if($sub_tab_id == 1) {
                    $totalCnt = ADMIN_CNT_BEST_CAST;
                }
                echo  '<label>total:'.$totalCnt.'</label>';
                ?>

            </div>

            <div id="tabs-cast" style="margin-top: 5px;">
                <?php
                    echo '<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">';

                    $arrTab = Array("전체 캐스트", "Best 캐스트");

                    for($i = 0; $i < count($arrTab); $i++) {
                        if($i == $sub_tab_id) {
                            echo '<li class="active" onclick="onClickTab('.$i.')"><a href="#cast-panel'.$i.'" data-toggle="tab">'.$arrTab[$i].'</a></li>';
                        }
                        else {
                            echo '<li onclick="onClickTab('.$i.')"><a href="#cast-panel'.$i.'" data-toggle="tab">'.$arrTab[$i].'</a></li>';
                        }
                    }

                    echo '</ul>';

                    echo '<div id="my-tab-content" class="tab-content">';

                    for($i = 0; $i < count($arrTab); $i++) {
                        if($i == $sub_tab_id) {
                            echo '<div class="tab-pane active" id="cast-panel'.$i.'"  style="border-style:solid;border-color: #cccccc;border-width: 1px;">';
                        }
                        else {
                            echo '<div class="tab-pane" id="cast-panel'.$i.'"  style="border-style:solid;border-color: #cccccc;border-width: 1px;">';
                        }

                             echo '<div style="margin-left: 0px;margin-top: 5px;padding-left: 5px;padding-right: 5px;" id="tab-cast">';
                             echo        '<table class="table table-bordered table-scrollable" id="tb_cast_list">
                                            <colgroup>
                                                <col width="5%">
                                                <col width="40%">
                                                <col width="20%">
                                                <col width="15%">
                                                <col width="15%">
                                                <col width="5%">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th scope="col" class="base-table-header" >번호</th>
                                                <th scope="col" class="base-table-header" >제목</th>
                                                <th scope="col" class="base-table-header" >등록일</th>
                                                <th scope="col" class="base-table-header" >좋아요</th>
                                                <th scope="col" class="base-table-header" >댓글</th>
                                                <th scope="col" class="base-table-header" >선택</th>
                                            </tr>
                                            </thead>

                                            <tbody id="list">';

                                            $tempArray = $arrCast;
                                            if($i == 1) { // best cast
                                                $tempArray = $arrBestCast;
                                            }
                                            else {
                                                $tempArray = $arrCast;
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
                                                    echo '<td onclick="showCast('.$row['id'].')" style="cursor:pointer;">' . $row['castTitle'] . '</td>';
                                                    echo '<td>' .$row['castPostTime']. '</td>';
                                                    echo '<td>' .$row['castHeartCnt']. '</td>';
                                                    echo '<td>' .$row['castCommentCnt']. '</td>';
                                                    if($row['castStatus'] == STATUS_LIFE) {
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

            <button type="button" class="btn btn-default btn-default-sm" onclick="NewCast()">새로운 캐스트</button>
            <button type="button" class="btn btn-default btn-default-sm" onclick="RemoveCast()">삭제</button>
            <button type="button" class="btn btn-default btn-default-sm" onclick="AddLikeCount()">좋아요 늘리기</button>
            <button type="button" class="btn btn-default btn-default-sm" onclick="SetBest()">BEST선정/해제</button>
            <button type="button" class="btn btn-default" onclick="MoveUp()">위로</button>
            <button type="button" class="btn btn-default" onclick="MoveDown()">아래로</button>
        </div>
        <div class="col-md-6" id = "panel_add_modify_cast">

        </div>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/jquery.form.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script  type="text/javascript">

    var curTabIndex = 0; // 0:all-cast 1:best-cast

    jQuery(document).ready(function ($) {
        $('#tabs').tab();
    });

    function onClickTab(idx) {
        curTabIndex = idx;
    }

    function addCode(script) {
        var JS= document.createElement('script');
        JS.text= script;
        document.body.appendChild(JS);
    }

    function getOrderType() {
        var orderType = document.getElementById("order-type").getAttribute("value");
        if(orderType == null || isUndifined(orderType) || isNaN(orderType) == true || orderType == "") {
            return null;
        }

        return parseInt(orderType);
    }

    function onClickOrderType(idx) {
        var curOrderType = getOrderType();

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
        searchWithCondition(page_num, null, null, null);
    }

    function onClickSearch() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);
        var searchWord = document.getElementById('search_text').value;
        var categoryID = document.getElementById('castCategory').value;
        var orderType = getOrderType();
        var tabIdx = curTabIndex;

        searchWithCondition(currentPage, searchWord, categoryID, orderType, tabIdx);
    }

    function searchWithCondition(page_num, search_word, category_id, order_type, tab_idx) {

        if(page_num == null) {
            page_num = 1;
        }

        var url  = getBaseURL()+'/cast?page_num='+page_num;
        if(search_word != null && search_word != "") {
            url += '&search_word='+encodeURI(search_word);
        }

        if(category_id != null && category_id != 0) {
            url += '&category_id='+category_id;
        }

        if(order_type != null) {
            url += '&order_type='+order_type;
        }

        if(tab_idx != null) {
            url += '&tab_idx='+tab_idx;
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

    function RemoveCast() {

        var strJSONCastIDArray = getCheckedCastArray();

        var arrCastID = JSON.parse(strJSONCastIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: "cast/remove_cast",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    arr_cast_id:strJSONCastIDArray
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

    function NewCast() {
        $.ajax({
            url: "cast/add_modify_cast",
            type: "get", // To protect sensitive data
            data: {
                ajax:true
            },
            success:function(response){
                $('#panel_add_modify_cast').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }


    function showCast(id) {
        $.ajax({
            url: "cast/add_modify_cast",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                cast_id:id
            },
            success:function(response){
                $('#panel_add_modify_cast').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }


    function AddNewPage(id) {
        // Find a <table> element with id="tb_cast_detail":
        var table = document.getElementById("tb_cast_detail");

        var lastIdx = table.rows.length;

        if(lastIdx == 1) {
            var row = table.rows[0];
            if(row.getAttribute("class") == "no_data") {
                table.deleteRow(0);
                lastIdx = 0;
            }
        }
        else {
            var lastCell = table.rows[lastIdx - 1];
            lastIdx = parseInt(lastCell.getAttribute("value"));
        }

        //
        // add image row
        //

        // Create an empty <tr> element and add it to the last position of the table:
        var row = table.insertRow(-1);

        // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);

        var newIdx = lastIdx + 1;

        var castImgVarName = "castdetailImg"+newIdx;
        var url = getBaseURL()+"/admin/upload_image?kind=cast&file_name=castdetailImg"+(newIdx)+"&index="+(newIdx);

        cell1.innerHTML = '<td>' +
            '<form method="post" enctype="multipart/form-data" id="imgUploadForm'+newIdx+'" value="'+newIdx+'">'+
            '<input id= "'+castImgVarName+'" name="'+castImgVarName+'" type="file" size="10" style="font-size:12pt; color:#000000;width:380px"/>' +
            '<label id="castdetailImgLabel'+newIdx+'">'+'이미지를 입력해주세요.</label>'+
            '<input type="submit" value="등록"/></form></td>';

        var script =  "$('#imgUploadForm"+newIdx+"').ajaxForm({"+
            "url : '"+url+"',"+
            "dataType : 'json',"+
            "success : function (response) {"+
            "var code = response['result_code'];"+
            "if(code == 0) {"+
            "var ret_id = response['result_data']['index'];"+
            "var var_name = 'castdetailImg'+ret_id;"+
            "document.getElementById(var_name).setAttribute('value', response['result_data']['id']);"+
            "var input_name = '#castdetailImgLabel'+(ret_id);"+
            "$(input_name).text(response['result_data']['imageURL']);"+
            "}"+
            "else {"+
            "alert('저장 실패!지정한 사용자가 유효한지 확인해주세요.');"+
            "}"+
            "}"+
            "});";
        addCode(script);

        //
        // add content row
        //

        row = table.insertRow(-1);
        // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);

        row.setAttribute("value", newIdx); // cast id

        // Add some text to the new cells:
        var castContentName = "castdetailContent"+newIdx;
        var castPublishName = "castdetailPublish"+newIdx;
        cell1.innerHTML = '<td>' +
                            '<input id= "'+castPublishName+'" type="url" size="10" style="font-size:12pt; color:#000000;width:420px">'+':출처</input>' +
                            '<input id= "'+castContentName+'" type="url" size="10" style="font-size:12pt; color:#000000;width:480px"/>' +
                            '</td>';
        cell2.innerHTML = '<button onclick="onRemoveCastDetail('+newIdx+', true)">삭제</button>';
    }


    function WriteCast(id) {

        var castTitle = $('#castTitle').val();
        var tableCastDetail = document.getElementById("tb_cast_detail");
        var row = tableCastDetail.rows[0];
        var categoryID = $('#write_cast_category').val();

        if(castTitle == null || castTitle == "") {
            alert("캐스트제목을 입력해주세요.");
            return;
        }

        if(row == null || (row != null && row.getAttribute("class") == "no_data")) {
            alert("새 페이지를 추가해주세요.");
            return;
        }

        var cast_id = document.getElementById("cast_detail_list").getAttribute("value");

        if (isUndifined(cast_id) == true) { // Any scope
            cast_id  = null;
        }
        var coverImg = null;
        var castDetail  = new Array;
        var length = tableCastDetail.rows.length;

        var i = 0;
        while(i < length) {

            var idx = tableCastDetail.rows[i+1].getAttribute("value");
            var varName2 = "castdetailImg"+(idx);

            var detailImgID = document.getElementById(varName2).getAttribute("value");
            var detailContent = $('#castdetailContent'+idx).val();
            var detailPublish = $('#castdetailPublish'+idx).val();
            var detail_id = tableCastDetail.rows[i].getAttribute("value");
            if(detailContent != "") {
                if (isUndifined(detailImgID) == true) { // Any scope
                    detailImgID  = null;
                }
                else {
                    if(coverImg == null) {
                        coverImg = detailImgID;
                    }
                }
                if (isUndifined(detail_id) == true) { // Any scope
                    detail_id  = null;
                }

                var jsonArg1 = new Object();
                if(detail_id != null && detail_id != "") {
                    jsonArg1['id'] = detail_id;
                }

                jsonArg1['castdetailContent'] = detailContent;
                jsonArg1['castdetailPublish'] = detailPublish;
                jsonArg1['castdetailImgID'] = detailImgID;

                castDetail.push(jsonArg1);
            }

            i+= 2;
        }

        if(coverImg == null) {
            alert("상세페지의 이미지를 추가해주세요.");
            return;
        }

        var json = JSON.stringify(castDetail);
        $.ajax({
            url: "cast/write_cast",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                cast_title:castTitle,
                cast_detail:json,
                cast_id:cast_id,
                cast_category_id:categoryID
            },
            success:function(response){
                if(response['result_code'] == 0) {
                    location.reload(true);
                }
                else {
                    alert("등록 실패!");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function onRemoveCastDetail(id, isNewCell) {

        var cast_detail_id =  id;
        var table = document.getElementById("tb_cast_detail");
        if(isNewCell == false) {
            $.ajax({
                url: "cast/remove_cast_detail",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    cast_detail_id: cast_detail_id
                },
                success: function (response) {

                    if(response['result_code'] == 0) {
                        var row_id = (parseInt(response['result_data']['index'])+1) * 2 - 1;

                        table.deleteRow(row_id);
                        table.deleteRow(row_id - 1);

                        refresh();

                        alert("삭제성공!");
                    }
                    else {
                        alert("삭제실패!");
                    }
                },
                error: function (jqXHR, msg, erro) {
                    alert("전송 실패! 망연결을 확인해주세요.");
                }
            });
        }
        else {
            var del_idx = parseInt(id)*2 - 1;
            table.deleteRow(del_idx);
            table.deleteRow(del_idx-1);
            refresh();
        }
    }

    function refresh () {
        var table = document.getElementById("tb_cast_detail");
        if(table.rows.length == 0) {
            var row = table.insertRow(0);
            var cell1 = row.insertCell(0);

            row.setAttribute("class", "no_data");
            cell1.innerHTML = '<td colspan="10">없습니다.</td>';
        }
    }

    function onClickAddCategory() {
        var text;
        var favDrink = prompt("카테고리이름을 입력해주세요.", "");
        text = favDrink;

        if(text != null && text != "") {
            $.ajax({
                url: "cast/add_category",
                type: "post", // To protect sensitive data
                data: {
                    ajax:true,
                    category_name: text
                },
                success:function(response){
                    alert("추가 성공!");
                    location.reload();
                },
                error:function(jqXHR, msg, erro) {
                    alert("추가 실패! 망연결을 확인해주세요.");
                }
            });
        }
    }

    function AddLikeCount() {
        var strJSONCastIDArray = getCheckedCastArray();
        $.ajax({
            url: "cast/add_like_count",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                arr_cast_id: strJSONCastIDArray
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
                url: "cast/set_best_cast",
                type: "post", // To protect sensitive data
                data: {
                    ajax:true,
                    arr_cast_id: strJSONCastIDArray,
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

    function MoveUp() {
        var strJSONArray = getCheckedCastArray();
        var arrCastID = JSON.parse(strJSONArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        if(arrCastID.length > 1) {
            alert("1개만 선정 가능합니다.");
            return;
        }

        var id = arrCastID[0];

        $.ajax({
            url: getBaseURL()+"/cast/move_up",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                cast_id: id
            },
            success:function(response){
                location.reload();
            },
            error:function(jqXHR, msg, erro) {
                alert("추가 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function MoveDown() {
        var strJSONArray = getCheckedCastArray();
        var arrCastID = JSON.parse(strJSONArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        if(arrCastID.length > 1) {
            alert("1개만 선정 가능합니다.");
            return;
        }

        var id = arrCastID[0];

        $.ajax({
            url: getBaseURL()+"/cast/move_down",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                cast_id: id
            },
            success:function(response){
                location.reload();
            },
            error:function(jqXHR, msg, erro) {
                alert("추가 실패! 망연결을 확인해주세요.");
            }
        });
    }

</script>


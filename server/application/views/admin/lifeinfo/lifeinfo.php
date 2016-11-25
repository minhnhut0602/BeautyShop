
<link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap-datepicker3.css" rel="stylesheet" type="text/css">

<div class="container" xmlns="http://www.w3.org/1999/html" id="root">
    <div class="row">
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-5">
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
                    <span/>
                    <button onclick="onClickAddCategory()">카테고리추가</button>

                </div>

                <div class="col-md-6 input-group input-group-sm">
                    <input type="text" class="form-control " placeholder="Search" id="search_text" style="width: 83%;" value="<?php echo $searchWord?>">
                    <button type="button" class="btn btn-default  btn-sm" onclick="onClickSearch()" style="width: 15%;margin-left: 5px;">검색</button>
                    </input>
                </div>
            </div>

            <div style="margin-top: 5px;">
                <table class="table table-bordered table-scrollable" id="tb_cast_list">
                    <colgroup>
                        <col width="5%">
                        <col width="15%">
                        <col width="50%">
                        <col width="20%">
                        <col width="10%">
                    </colgroup>

                    <thead>
                    <tr>
                        <th scope="col" class="base-table-header" >번호</th>
                        <th scope="col" class="base-table-header" >카테고리</th>
                        <th scope="col" class="base-table-header" >광고명</th>
                        <th scope="col" class="base-table-header" >등록일</th>
                        <th scope="col" class="base-table-header" >선택</th>
                    </tr>
                    </thead>

                    <tbody id="list">
                    <?php
                    $cnt = count($arrLifeInfo);

                    if($cnt == 0) {
                        echo '<tr class="no_data" style="">';
                        echo '<td colspan="10">없습니다.</td>';
                        echo '</tr>';
                    }
                    else {
                        $i = 1;
                        foreach ($arrLifeInfo as $row) {
                            echo '<tr>';
                            echo '<td>' .( $i + ($currentPageNum - 1) * ADMIN_MAX_PAGE_ITEM_CNT) . '</td>';
                            echo '<td>' . $row['lifeCategoryName'] . '</td>';
                            if($row['lifeBest'] == STATUS_YES) {
                                echo '<td onclick="showLifeInfo(' . $row['id'] . ')"  style="cursor:pointer;"> <font color="125191" size="3"> <b>베스트</b> </font>' . $row['lifeTitle'] . '</td>';
                            }
                            else {
                                echo '<td onclick="showLifeInfo(' . $row['id'] . ')"  style="cursor:pointer;"> ' . $row['lifeTitle'] . '</td>';
                            }
                            echo '<td>' . $row['lifeRegTime'] . '</td>';
                            if($row['lifeStatus'] == 0) {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                            }
                            else {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" checked/>' . '</td>';
                            }
                            echo '</tr>';

                            $i++;
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

            <button type="button" class="btn btn-default" onclick="NewLifeInfo()">생활정보 추가</button>
            <button type="button" class="btn btn-default" onclick="RemoveLifeInfo()">삭제</button>
            <button type="button" class="btn btn-default" onclick="SetBestLife()">베스트선정</button>
            <button type="button" class="btn btn-default" onclick="MoveUp()">위로</button>
            <button type="button" class="btn btn-default" onclick="MoveDown()">아래로</button>

        </div>

        <div class="col-md-6" id = "panel_add_modify_lifeinfo">

        </div>
    </div>
</div>

<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script>

    function getCurrentCategoryID() {
        var id = document.getElementById("freetalkCategory").value;

        if(id == "0") {
            return null;
        }
        return id;
    }

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            var searchWord = document.getElementById('search_text').value;
            var id = getCurrentCategoryID();

            searchFreetalkPage(id, searchWord, currentPage);

            return false;
        }
        return true;
    }

    function prevPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var searchWord = document.getElementById('search_text').value;
        currentPage = currentPage - 1;
        if(currentPage <= 0) {
            currentPage = 1;
        }

        searchFreetalkPage(getCurrentCategoryID(), searchWord, currentPage);
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);
        var searchWord = document.getElementById('search_text').value;

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        searchFreetalkPage(getCurrentCategoryID(), searchWord, currentPage);
    }

    function onClickSearch() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);
        var searchWord = document.getElementById('search_text').value;

        searchFreetalkPage(getCurrentCategoryID(), searchWord, currentPage);
    }

    function searchFreetalkPage(category_id, search_word, page_num) {
        var url  = 'lifeinfo?page_num='+page_num;

        if(search_word != null && search_word != "") {
            url += '&search_word='+encodeURI(search_word);
        }

        if(category_id != null && category_id != "") {
            url += '&category_id='+ category_id;
        }

        location.href = url;
    }

    function showLifeInfo(id) {
        $.ajax({
            url: "lifeinfo/add_modify_lifeinfo",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                lifeinfo_id:id
            },
            success:function(response){
                $('#panel_add_modify_lifeinfo').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function NewLifeInfo() {
        $.ajax({
            url: "lifeinfo/add_modify_lifeinfo",
            type: "get", // To protect sensitive data
            data: {
                ajax:true
            },
            success:function(response){
                $('#panel_add_modify_lifeinfo').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
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

    function RemoveLifeInfo() {
        var strJSONArray = getCheckedIDArray();
        var arrCastID = JSON.parse(strJSONArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: "lifeinfo/remove_lifeinfo_array",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    lifeinfo_id_array:strJSONArray
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

    function onClickAddCategory() {
        var text;
        var favDrink = prompt("카테고리이름을 입력해주세요.", "카테고리이름");
        text = favDrink;

        if(text != null || text != "") {
            $.ajax({
                url: getBaseURL()+"/lifeinfo/add_category",
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



    function WriteLifeInfo(id) {
        var adsTitle = $('#lifeinfo_name').val();
        var adsBannerURL = document.getElementById("ads_url").value;
        var adsImgID = document.getElementById("lifeBannerImg").getAttribute("value");
        var categoryID =  $('#write_lifeinfo_category').val();
        var lifeinfoID = id;

        var lifeSubject = $('#life_subject').val();
        var lifeExplain = $('#life_explain').val();

        if (isUndifined(lifeinfoID) == true) { // Any scope
            lifeinfoID  = null;
        }

        if(categoryID == 0) {
            categoryID = null;
        }

        if(adsBannerURL == null || adsImgID == null) {
            alert("배너를 등록해주세요.");
            return;
        }

        if(lifeSubject == null || lifeSubject == "") {
            alert("제목을 입력해주세요.");
            return;
        }

        if(lifeSubject.length > 8) {
            alert("제목은 8자를 넘지 말아야 합니다.");
            return;
        }

        if(lifeExplain == null || lifeExplain == "") {
            alert("내용을 입력해주세요.");
            return;
        }

        $.ajax({
            url: "lifeinfo/write_lifeinfo",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                adsTitle:adsTitle,
                adsBannerURL:adsBannerURL,
                adsImgID:adsImgID,
                categoryID:categoryID,
                lifeinfoID:lifeinfoID,
                lifeSubject:lifeSubject,
                lifeExplain:lifeExplain
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

    function SetBestLife() {
        var strJSONArray = getCheckedIDArray();
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
            url: getBaseURL()+"/lifeinfo/set_best_life",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                life_id: id
            },
            success:function(response){
                location.reload();
            },
            error:function(jqXHR, msg, erro) {
                alert("추가 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function MoveUp() {
        var strJSONArray = getCheckedIDArray();
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
            url: getBaseURL()+"/lifeinfo/move_up",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                life_id: id
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
        var strJSONArray = getCheckedIDArray();
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
            url: getBaseURL()+"/lifeinfo/move_down",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                life_id: id
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
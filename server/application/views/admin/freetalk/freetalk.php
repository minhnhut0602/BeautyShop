<div class="container" xmlns="http://www.w3.org/1999/html" id="root">
    <div class="row">
        <div class="col-md-6">

            <div class="row">
                <div class="col-md-5">
                    <div class="input-group">
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
                        <col width="90%">
                        <col width="5%">
                    </colgroup>
                    <thead>

                    <tbody id="list">
                    <?php
                    $cnt = count($arrFreetalk);

                    if($cnt == 0) {
                        echo '<tr class="no_data" style="">';
                        echo '<td colspan="10">없습니다.</td>';
                        echo '</tr>';
                    }
                    else {
                        $i = 1;
                        foreach ($arrFreetalk as $row) {
                            echo '<tr>';
                            echo '<td>' .( $i + ($currentPageNum - 1) * ADMIN_MAX_PAGE_ITEM_CNT) . '</td>';
                            echo '<td onclick="showFreetalk('.$row['id'].')" style="cursor:pointer;">' . $row['freetalkContent'] . '</td>';
                            if($row['freetalkStatus'] == 0) {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" onclick="setShowFreetalk(this)"/></td>';
                            }
                            else {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" onclick="setShowFreetalk(this)" checked/>' . '</td>';
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

            <button type="button" class="btn btn-default" onclick="NewFreetalk()">새로운 토크</button>
            <button type="button" class="btn btn-default" onclick="RemoveFreetalk()">삭제</button>
            <button type="button" class="btn btn-default" onclick="HideFreetalk()">비노출</button>

        </div>

        <div class="col-md-6" id = "panel_add_modify_freetalk">

        </div>
    </div>
</div>

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
        var url  = 'freetalk?page_num='+page_num;

        if(search_word != null && search_word != "") {
            url += '&search_word='+encodeURI(search_word);
        }

        if(category_id != null && category_id != "") {
            url += '&category_id='+ category_id;
        }

        location.href = url;
    }

    function showFreetalk(id) {
        $.ajax({
            url: "freetalk/add_modify_freetalk",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                freetalk_id:id
            },
            success:function(response){
                $('#panel_add_modify_freetalk').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function NewFreetalk() {
        $.ajax({
            url: "freetalk/add_modify_freetalk",
            type: "get", // To protect sensitive data
            data: {
                ajax:true
            },
            success:function(response){
                $('#panel_add_modify_freetalk').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function setShowFreetalk(checkbox) {
        if(checkbox.checked == false) {
            $.ajax({
                url: "freetalk/visible_freetalk",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    freetalk_id:checkbox.value,
                    visible:true
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
        }
    }

    function HideFreetalk() {
        checkboxes = document.getElementsByName('checked');
        var n = checkboxes.length;
        for (var i = (n - 1); i >= 0; i--) {
            if(checkboxes[i].checked == true) {
                $.ajax({
                    url: "freetalk/visible_freetalk",
                    type: "post", // To protect sensitive data
                    data: {
                        ajax: true,
                        freetalk_id:checkboxes[i].value,
                        table_cell_id:i,
                        visible:false
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
            }
        }
    }

    function RemoveFreetalk() {
        checkboxes = document.getElementsByName('checked');
        var n = checkboxes.length;
        for (var i = (n - 1); i >= 0; i--) {
            if(checkboxes[i].checked == true) {
                $.ajax({
                    url: "freetalk/remove_freetalk",
                    type: "post", // To protect sensitive data
                    data: {
                        ajax: true,
                        freetalk_id:checkboxes[i].value,
                        table_cell_id:i
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
            }
        }
    }

    function onClickAddCategory() {
        var text;
        var favDrink = prompt("카테고리이름을 입력해주세요.", "카테고리이름");
        text = favDrink;

        if(text != null || text != "") {
            $.ajax({
                url: "freetalk/add_category",
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

</script>
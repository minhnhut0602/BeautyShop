<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row button {float:right;}
</style>

<div class="container" xmlns="http://www.w3.org/1999/html" id="root">

    <div class="cb-row">
        <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/notice/preview">미리보기</a></button>
        <button type="button" class="btn btn-default" style="margin-right: 5px;"><a href="<?php echo base_url(); ?>index.php/notice/privacy_preview">이용약관</a></button>
    </div>

    <div>
        <table class="table table-bordered table-scrollable">
            <colgroup>
                <col width="5%">
                <col width="50%">
                <col width="10%">
                <col width="30%">
                <col width="5%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col">번호</th>
                <th scope="col">제목</th>
                <th scope="col">조회수</th>
                <th scope="col">등록일</th>
                <th scope="col">상태</th>
            </tr>
            </thead>

            <tbody id="list">
            <?php
            $cnt = count($arrNotice);

            if($cnt == 0) {
                echo '<tr class="__oldlist" style="">';
                echo '<td colspan="10">없습니다.</td>';
                echo '</tr>';
            }
            else {
                $i = 1;
                foreach ($arrNotice as $row) {
                    echo '<tr>';
                    echo '<td>' .( $i + ($currentPageNum - 1) * 20) . '</td>';
                    echo '<td onclick="showNotice('.$row['id'].')" style="cursor:pointer;">' . $row['noticeTitle'] . '</td>';
                    echo '<td>' . $row['noticeClickCnt'] . '</td>';
                    echo '<td>' . $row['noticeTime'] . '</td>';
                    if($row['noticeStatus'] == 0) {
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                    }
                    else {
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" checked/>' . '</td>';
                    }
                    $i++;
                }

            }
            ?>
            </tbody>
        </table>
    </div>

    <div class="cb-row">
        <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/notice/add_modify_notice">추가</a></button>
        <button type="button" class="btn btn-default" onclick="onClickRemove()" style="margin-right: 5px;">삭제</button>
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

</div>

<script type="text/javascript">

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            location.href = 'notice?page_num='+currentPage;
            return false;
        }
        return true;
    }

    function showNotice(idx) {
        window.open('notice/add_modify_notice?id='+idx, '수정', 'width=640,height=320');
    }

    function HandlePopupResult(){
        location.reload();
    }

    function checkAll(cb) {

        var checked = cb.checked;

        checkboxes = document.getElementsByName('checked');
        for (var i = 0, n = checkboxes.length; i < n; i++) {
            checkboxes[i].checked = cb.checked;
        }
    }

    function prevPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        currentPage = currentPage - 1;
        if(currentPage <= 0) {
            currentPage = 1;
        }

        location.href = 'notice?page_num='+currentPage;
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        location.href = 'notice?page_num=' + currentPage;
    }

    function onClickRemove() {
        checkboxes = document.getElementsByName('checked');
        var n = checkboxes.length;
        for (var i = (n - 1); i >= 0; i--) {
            if(checkboxes[i].checked == true) {
                $.ajax({
                    url: "notice/remove_notice",
                    type: "post", // To protect sensitive data
                    data: {
                        ajax: true,
                        id:checkboxes[i].value,
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

 </script>
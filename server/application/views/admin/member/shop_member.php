
<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row input {float:right;}
</style>

<div class="container">
    <div style="margin-top: 5px;" >
        <table class="table table-bordered table-scrollable">
            <colgroup>
                <col width="5%">
                <col width="10%">
                <col width="15%">
                <col width="25%">
                <col width="15%">
                <col width="15%">
                <col width="5%">
                <col width="10%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col" class="base-table-header" >번호</th>
                <th scope="col" class="base-table-header" >아이디</th>
                <th scope="col" class="base-table-header" >상점이름</th>
                <th scope="col" class="base-table-header" >상점주소</th>
                <th scope="col" class="base-table-header" >전화번호</th>
                <th scope="col" class="base-table-header" >신청날짜</th>
                <th scope="col" class="base-table-header" >상태</th>
            </tr>
            </thead>

            <tbody id="list">
            <?php
            $cnt = count($arrUser);

            if($cnt == 0) {
                echo '<tr class="__oldlist" style="">';
                echo '<td colspan="10">없습니다.</td>';
                echo '</tr>';
            }
            else {
                $i = 1;
                foreach ($arrUser as $row) {
                    echo '<tr>';
                    echo '<td>' .( $i + ($currentPageNum - 1) * ADMIN_MAX_PAGE_ITEM_CNT) . '</td>';
                    echo '<td onclick="showShopUser('.$row['id'].')" style="cursor:pointer;">' . $row['userID'] . '</td>';
                    echo '<td>' . $row['shopName'] . '</td>';
                    echo '<td>' . $row['shopAddress'] . '</td>';
                    echo '<td>' . $row['shopPhonenumber'] . '</td>';
                    echo '<td>' . $row['shopRequestTime'] . '</td>';
                    if($row['shopStatus'] == STATUS_MANAGE) {
                        echo '<td>승인</td>';
                    }
                    else {
                        echo '<td>미승인</td>';
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
</div>

<script type="text/javascript">

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            searchWithPage(currentPage);
            return false;
        }
        return true;
    }

    function showShopUser(idx) {
        window.open('show_shop_member?user_id='+idx, '상점멤버보기', 'width=640,height=720');
    }

    function HandlePopupResult(){
        location.reload();
    }


    function prevPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        currentPage = currentPage - 1;
        if(currentPage <= 0) {
            currentPage = 1;
        }

        searchWithPage(currentPage);
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        searchWithPage(currentPage);
    }

    function search() {
        searchWithPage(1);
    }

    function searchWithPage(page){
        searchWithCondition(page);
    }

    function searchAllList() {
        searchWithCondition(1);
    }

    function searchWithCondition(pageNum) {

        var url = 'member';
        var isChange = false;

        if(pageNum != null) {
            url = url + '?page_num='+pageNum;
            isChange = true;
        }

        location.href = url;
    }

    function onClickUserLimit(cb) {
        var checked = cb.checked;
        var arrValue = cb.value.split("&");
        var id = parseInt(arrValue[0]);
        var status = parseInt(arrValue[1]);
        updateStaus(id, status, checked);
    }

    function updateStaus(id, status, checked) {

        if(checked == true) {
            if(status == 2) {
                status = 3;
            }
            else {
                status = 1;
            }
        }
        else {
            if(status == 3) {
                status = 2;
            }
            else if(status = 0){
                status = 1;
            }
        }


        $.ajax({
            url: "member/update_status",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                user_id: id,
                user_status: status
            },
            success:function(response){
                var code = response['result_code'];
                if(code == 0) {
                    alert("저장 성공!");
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

</script>
<div class="container" xmlns="http://www.w3.org/1999/html" id="root">

    <div>
        <table class="table table-bordered table-scrollable">
            <colgroup>
                <col width="5%">
                <col width="10%">
                <col width="30%">
                <col width="20%">
                <col width="20%">
                <col width="15%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col" class="base-table-header" >번호</th>
                <th scope="col" class="base-table-header" >수신자ID</th>
                <th scope="col" class="base-table-header" >제목</th>
                <th scope="col" class="base-table-header" >발송일시</th>
                <th scope="col" class="base-table-header" >확인일시</th>
                <th scope="col" class="base-table-header" >수신확인여부</th>
            </tr>
            </thead>

            <tbody id="list">
            <?php
            $cnt = count($arrEnvelopList);

            if($cnt == 0) {
                echo '<tr class="__oldlist" style="">';
                echo '<td colspan="10">없습니다.</td>';
                echo '</tr>';
            }
            else {
                $i = 1;
                foreach ($arrEnvelopList as $row) {
                    echo '<tr>';
                    echo '<td>' .( $i + ($currentPageNum - 1) * 20) . '</td>';
                    echo '<td>' . $row['recvUserID'] . '</td>';
                    echo '<td onclick="showEnvelop('.$row['id'].')" style="cursor:pointer;">' . $row['gcmlogTitle'] . '</td>';
                    echo '<td>' . $row['gcmlogPostTime'] . '</td>';
                    echo '<td>' . $row['gcmlogReadTime'] . '</td>';
                    if($row['gcmlogReaded'] == 1) {
                        echo '<td>확인</td>';
                    }
                    else {
                        echo '<td>미확인</td>';
                    }
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

    function HandlePopupResult(){
        location.reload();
    }

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            location.href = 'envelop_list?page_num='+currentPage;
            return false;
        }
        return true;
    }

    function showEnvelop(idx) {
        window.open( getBaseURL()+'/push/show_envelop?id='+idx, '쪽지보기', 'width=640,height=320');
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

        location.href = 'envelop_list?page_num='+currentPage;
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        location.href = 'envelop_list?page_num=' + currentPage;
    }

</script>
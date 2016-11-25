<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row button {float:right;}
</style>

<div class="container">

    <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;">
        <div class="row">
            <div class="col-md-6">
               <table class="table table-bordered" style="margin: 5px;">
                   <tr>
                    <td style="background:#eeebdc;text-align: center;"><label>상점이름 </label></td>
                    <td><input id="shopName" type="text" style="width: 100%;" value="<?php echo $shopName;?>"/></td>
                   </tr>
               </table>
            </div>

            <div class="col-md-6" style="width: 49%;">
                <table class="table table-bordered" style="margin: 5px;">
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>사업자등록증 </label></td>
                        <td>
                            <select class="combobox" id="shopIdentified">
                                <?php
                                if($shopIdenti != null) {
                                    echo '<option value="-1"> 전체 </option>';
                                    if($shopIdenti == 1) {
                                        echo '<option value="1" selected> 등록 </option>';
                                        echo '<option value="0"> 미등록 </option>';
                                    }
                                    else {
                                        echo '<option value="1" > 등록 </option>';
                                        echo '<option value="0" selected> 미등록 </option>';
                                    }
                                }
                                else {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="1"> 등록 </option>';
                                    echo '<option value="0"> 미등록 </option>';
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

    <div style="margin-top: 10px;">
        <table class="table table-bordered table-scrollable">
            <colgroup>
                <col width="5%">
                <col width="10%">
                <col width="15%">
                <col width="30%">
                <col width="15%">
                <col width="15%">
                <col width="10%">
            </colgroup>
            <thead>
            <tr>
                <th class="base-table-header" scope="col">번호</th>
                <th class="base-table-header"  scope="col">아이디</th>
                <th class="base-table-header" scope="col">상점이름</th>
                <th class="base-table-header" scope="col">주소</th>
                <th class="base-table-header"  scope="col">전화번호</th>
                <th  class="base-table-header" scope="col">신청날짜</th>
                <th class="base-table-header"  scope="col">사업자등록증</th>
            </tr>
            </thead>

            <tbody id="list">
            <?php
            $cnt = count($arrShop);

            if($cnt == 0) {
                echo '<tr class="__oldlist" style="">';
                echo '<td colspan="10">없습니다.</td>';
                echo '</tr>';
            }
            else {
                $i = 1;
                foreach ($arrShop as $row) {
                    echo '<tr>';
                    echo '<td>' .$row['id']. '</td>';
                    echo '<td>' . $row['shopID'] . '</td>';
                    echo '<td onclick="showShop('.$row['id'].')" style="cursor:pointer;">' . $row['shopName'] . '</td>';
                    echo '<td>' . $row['shopAddress'] . '</td>';
                    echo '<td>' . $row['shopPhonenumber'] . '</td>';
                    echo '<td>' . $row['shopRequestTime'] . '</td>';

                    if($row['shopManagerIdentyImgID'] == null) {
                        echo '<td>  미등록 </td>';
                    }
                    else {
                        echo '<td>  등록 </td>';
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

    function showShop(idx) {
        var params = [
            'height='+screen.height,
            'width='+screen.width,
            'fullscreen=yes' // only works in IE, but here for completeness
        ].join(',');
        window.open('shop_show?shop_id='+idx, '상점보기', params);
    }

    function HandlePopupResult(){
        location.reload();
    }

    function search() {
        searchWithPage(1);
    }

    function searchWithPage(page){
        var shopName = $('#shopName').val();
        var shopIdenti = $('#shopIdentified').find('option:selected').val();

        if(shopIdenti == -1) {
            shopIdenti = null;
        }

        searchWithCondition(page, shopName, shopIdenti);
    }

    function searchAllList() {
        $('#shopName').val('');
        $('#shopIdentified').val(-1);

        searchWithCondition(1, null, null);
    }

    function searchWithCondition(pageNum, shopName, shopIdenti) {

        var url = 'request_shop_list';
        var isChange = false;

        if(pageNum != null) {
            url = url + '?page_num='+pageNum;
            isChange = true;
        }

        if(shopName != null && shopName != "") {
            if(isChange == false) {
                url = url + '?shop_name='+encodeURI(shopName);
                isChange = true;
            }
            else {
                url = url + '&shop_name='+encodeURI(shopName);
            }
        }

        if(shopIdenti != null) {
            if(isChange == false) {
                url = url + '?shop_identi='+shopIdenti;
                isChange = true;
            }
            else {
                url = url + '&shop_identi='+shopIdenti;
            }
        }

        location.href = url;
    }

</script>
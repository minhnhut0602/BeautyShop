

<div class="container">

    <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;">
        <div class="row">
            <div class="col-md-6">
                <table class="table table-bordered" style="margin: 5px;">
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>업체명 </label></td>
                        <td><input id="shopName" type="text" style="width: 100%;" value="<?php echo $shopName;?>"/></td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>전화번호 </label></td>
                        <td><input id="shopPhoneNumber" type="text" style="width: 100%;" value="<?php echo $shopPhoneNumber;?>"/></td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>상점주소 </label></td>
                        <td><input id="shopAddress" type="text" style="width: 100%;" value="<?php echo $shopAddress;?>"/></td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>샵분류 </label></td>
                        <td>
                            <select class="combobox" id="shopCategory">
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

                </table>
            </div>

            <div class="col-md-6" style="width: 49%;">
                <table class="table table-bordered" style="margin: 5px;">

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>상점상태 </label></td>
                        <td>
                            <select class="combobox" id="shopStatus">
                                <?php
                                if($shopStatus == null) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'"> 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                else if($shopStatus == STATUS_MANAGE) {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'" selected> 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                else if($shopStatus == STATUS_REQUEST) {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'" selected> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'" > 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'"> 비운영 </option>';
                                }
                                else if($shopStatus == STATUS_NON_MANAGE) {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="'.STATUS_REQUEST.'"> 신청중 </option>';
                                    echo '<option value="'.STATUS_MANAGE.'"> 운영 </option>';
                                    echo '<option value="'.STATUS_NON_MANAGE.'" selected> 비운영 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>


                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>노출여부 </label></td>
                        <td>
                            <select class="combobox" id="shopVisibility">
                                <?php
                                if($shopVisibility == null) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="'.STATUS_VISIBLE.'"> 노출 </option>';
                                    echo '<option value="'.STATUS_HIDE.'"> 비노출 </option>';
                                }
                                else if($shopVisibility == STATUS_VISIBLE) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="'.STATUS_VISIBLE.'" selected> 노출 </option>';
                                    echo '<option value="'.STATUS_HIDE.'"> 비노출 </option>';
                                }
                                else if($shopVisibility == STATUS_HIDE) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="'.STATUS_VISIBLE.'" > 노출 </option>';
                                    echo '<option value="'.STATUS_HIDE.'" selected> 비노출 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>


                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>이벤트상태 </label></td>
                        <td>
                            <select class="combobox" id="shopEventable">
                                <?php
                                if($shopEventable == null) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                    echo '<option value="Y"> 정상 </option>';
                                    echo '<option value="N"> 중지 </option>';
                                }
                                else if($shopEventable == 'N') {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="Y"> 정상 </option>';
                                    echo '<option value="N" selected> 중지 </option>';
                                }
                                else {
                                    echo '<option value="-1"> 전체 </option>';
                                    echo '<option value="Y" selected> 정상 </option>';
                                    echo '<option value="N" > 중지 </option>';
                                }

                                ?>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>클릭수 </label></td>
                        <td>
                            <select class="combobox" id="shopClickCntCondition">
                                <?php
                                if($shopClickCntCondition != null) {
                                    echo '<option value="-1"> 미선택 </option>';
                                    if($shopClickCntCondition == 0) {
                                        echo '<option value="0" selected> 높은순 </option>';
                                        echo '<option value="1"> 낮은순 </option>';
                                    }
                                    else {
                                        echo '<option value="0" > 높은순 </option>';
                                        echo '<option value="1" selected> 낮은순 </option>';
                                    }
                                }
                                else {
                                    echo '<option value="-1" selected> 미선택 </option>';
                                    echo '<option value="0"> 높은순 </option>';
                                    echo '<option value="1"> 낮은순 </option>';
                                }

                                ?>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>콜수 </label></td>
                        <td>
                            <select class="combobox" id="shopCallCntCondition">
                                <?php
                                if($shopCallCntCondition != null) {
                                    echo '<option value="-1"> 미선택 </option>';
                                    if($shopCallCntCondition == 0) {
                                        echo '<option value="0" selected> 높은순 </option>';
                                        echo '<option value="1"> 낮은순 </option>';
                                    }
                                    else {
                                        echo '<option value="0" > 높은순 </option>';
                                        echo '<option value="1" selected> 낮은순 </option>';
                                    }
                                }
                                else {
                                    echo '<option value="-1" selected> 미선택 </option>';
                                    echo '<option value="0"> 높은순 </option>';
                                    echo '<option value="1"> 낮은순 </option>';
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
    	
    <div class="cb-row">
        <button type="button" class="btn btn-default"><a href="<?php echo base_url(); ?>index.php/shop/add_shop">업체추가</a></button>
    </div>
    	
    <div  class="col-md-12" align="left">
	    <?php
		    echo  $totalShopCnt;
	    ?>
	</div>    	
    <div>
        <table class="table table-bordered table-scrollable">
            <colgroup>
                <col width="5%">
                <col width="5%">
                <col width="5%">
                <col width="10%">
                <col width="20%">
                <col width="10%">
                <col width="15%">
                <col width="5%">
                <col width="5%">
                <col width="5%">
                <col width="5%">
                <col width="5%">
            	<col width="5%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col" class="base-table-header">번호</th>
                <th scope="col" class="base-table-header" >샵분류</th>
                <th scope="col" class="base-table-header" >아이디</th>
                <th scope="col" class="base-table-header" >상점이름</th>
                <th scope="col" class="base-table-header" >주소</th>
                <th scope="col" class="base-table-header" >전화번호</th>
                <th scope="col" class="base-table-header" >신청날짜</th>
                <th scope="col" class="base-table-header" >이벤트상태</th>
                <th scope="col" class="base-table-header" >상점상태</th>
                <th scope="col" class="base-table-header" >노출여부</th>
                <th scope="col" class="base-table-header" >콜수</th>
                <th scope="col" class="base-table-header" >클릭수</th>
            	<th scope="col" class="base-table-header" >선택</th>
            </tr>
            </thead>

            <tbody id="list">
            <?php
            $cnt = count($arrShop);

            if($cnt == 0) {
                echo '<tr class="__oldlist" style="">';
                echo '<td colspan="12">없습니다.</td>';
                echo '</tr>';
            }
            else {
                $i = 1;
                foreach ($arrShop as $row) {
                    echo '<tr>';
                    //echo '<td>' .( $i + ($currentPageNum - 1) * ADMIN_MAX_PAGE_ITEM_CNT) . '</td>';
                    echo '<td>' .$row['id'] . '</td>';
                    echo '<td>' .$row['shopCategoryName'] . '</td>';
                    echo '<td>' .$row['shopID'] . '</td>';
                    echo '<td onclick="showShop('.$row['id'].')" style="cursor:pointer;">' . $row['shopName'] . '</td>';
                    echo '<td>' . $row['shopAddress'] . '</td>';
                    echo '<td>' . $row['shopPhonenumber'] . '</td>';
                    echo '<td>' .  date($row['shopRequestTime']) . '</td>';

                    if($row['shopEventable'] == 'Y') {
                        echo '<td>  정상 </td>';
                    }
                    else {
                        echo '<td>  중지 </td>';
                    }

                    if($row['shopStatus'] == STATUS_MANAGE) {
                        echo '<td>  운영 </td>';
                    }
                    else  if($row['shopStatus'] == STATUS_NON_MANAGE){
                        echo '<td>  비운영 </td>';
                    }
                    else if($row['shopStatus'] == STATUS_REQUEST) {
                        echo '<td>  신청중 </td>';
                    }

                    if($row['shopVisibility'] == STATUS_HIDE) {
                        echo '<td>  비노출 </td>';
                    }
                    else {
                        echo '<td>  노출 </td>';
                    }

                    echo '<td>'.$row['shopCallCnt'].'</td>';
                    echo '<td>'.$row['shopClickCnt'].'</td>';
                    
                    echo '<td align="center">' .' <input type="checkbox" id="checked'.'" name="checked'.'" value="'. $row['id'] .'"/></td>';
                    
                    echo '</tr>';
					
                    $i++;
                }

            }
            ?>
            </tbody>
        </table>
    </div>
    <div  class="col-md-12" align="right">
    	<button type="button" class="btn btn-default btn-default-sm" onclick="RemoveShop()">삭제</button>
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

        window.open( getBaseURL()+'/shop/shop_show?shop_id='+idx, '상점보기', params);
    }

    function HandlePopupResult(){
        location.reload();
    }

    function search() {
        searchWithPage(1);
    }

    function searchWithPage(page){
        var shopName = $('#shopName').val();
        var shopPhoneNumber = $('#shopPhoneNumber').val();
        var shopAddress = $('#shopAddress').val();
        var shopCallCntCondition = $('#shopCallCntCondition').find('option:selected').val();

        if(shopCallCntCondition == -1) {
            shopCallCntCondition = null;
        }

        var shopClickCntCondition = $('#shopClickCntCondition').find('option:selected').val();

        if(shopClickCntCondition == -1) {
            shopClickCntCondition = null;
        }

        var shopClickCntCondition = $('#shopClickCntCondition').find('option:selected').val();

        if(shopClickCntCondition == -1) {
            shopClickCntCondition = null;
        }

        var shopStatus = $('#shopStatus').find('option:selected').val();

        if(shopStatus == -1) {
            shopStatus = null;
        }

        var shopEventable = $('#shopEventable').find('option:selected').val();
        if(shopEventable == -1) {
            shopEventable = null;
        }
		
        var shopVisiblility = $('#shopVisibility').find('option:selected').val();
        if(shopVisiblility == -1) {
            shopVisiblility = null;
        }

        var shopCategoryID = $('#shopCategory').find('option:selected').val();
        if(shopCategoryID == 0) {
            shopCategoryID = null;
        }

        searchWithCondition(page, shopName, shopPhoneNumber, shopAddress, shopCategoryID, shopCallCntCondition, shopClickCntCondition, shopStatus, shopEventable, shopVisiblility);
    }

    function searchAllList() {
        $('#shopName').val('');
        $('#shopPhoneNumber').val('');
        $('#shopAddress').val('');
        $('#shopCallCntCondition').val(-1);
        $('#shopClickCntCondition').val(-1);
        $('#shopStatus').val(-1);
        $('#shopEventable').val(-1);
        $('#shopVisiblitiy').val(-1);
        $('#shopCategory').val(0);

        searchWithCondition(1, null, null, null, null, null, null, null, null, null);
    }

    function searchWithCondition(pageNum, shopName, shopPhoneNumber, shopAddress, shopCategoryID,  shopCallCntCondition,shopClickCntCondition, shopStatus, shopEventable ,shopVisibility) {

        var url = getBaseURL()+'/shop';
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

        if(shopPhoneNumber != null && shopPhoneNumber != "") {
            if(isChange == false) {
                url = url + '?shop_phonenumber='+encodeURI(shopPhoneNumber);
                isChange = true;
            }
            else {
                url = url + '&shop_phonenumber='+encodeURI(shopPhoneNumber);
            }
        }

        if(shopAddress != null && shopAddress != "") {
            if(isChange == false) {
                url = url + '?shop_address='+encodeURI(shopAddress);
                isChange = true;
            }
            else {
                url = url + '&shop_address='+encodeURI(shopAddress);
            }
        }

        if(shopCallCntCondition != null) {
            if(isChange == false) {
                url = url + '?shop_callcnt_condition='+shopCallCntCondition;
                isChange = true;
            }
            else {
                url = url + '&shop_callcnt_condition='+shopCallCntCondition;
            }
        }

        if(shopClickCntCondition != null) {
            if(isChange == false) {
                url = url + '?shop_clickcnt_condition='+shopClickCntCondition;
                isChange = true;
            }
            else {
                url = url + '&shop_clickcnt_condition='+shopClickCntCondition;
            }
        }

        if(shopStatus != null) {
            if(isChange == false) {
                url = url + '?shop_status='+shopStatus;
                isChange = true;
            }
            else {
                url = url + '&shop_status='+shopStatus;
            }
        }

        if(shopEventable != null) {
            if(isChange == false) {
                url = url + '?shop_eventable='+shopEventable;
                isChange = true;
            }
            else {
                url = url + '&shop_eventable='+shopEventable;
            }
        }

        if(shopVisibility != null) {
            if(isChange == false) {
                url = url + '?shop_visibility='+shopVisibility;
                isChange = true;
            }
            else {
                url = url + '&shop_visibility='+shopVisibility;
            }
        }

        if(shopCategoryID != null) {
            if(isChange == false) {
                url = url + '?shop_category_id='+shopCategoryID;
                isChange = true;
            }
            else {
                url = url + '&shop_category_id='+shopCategoryID;
            }
        }

        location.href = url;
    }

    function getCheckedShopArray() {
        var checkboxes = null;
        checkboxes = document.getElementsByName('checked');

        var n = checkboxes.length;
        var arrShopID  = new Array;
        for (var i = (n - 1); i >= 0; i--) {
            if (checkboxes[i].checked == true) {
                arrShopID.push(checkboxes[i].value);
            }
        }

        return JSON.stringify(arrShopID);
    }
    
	function RemoveShop() {

        var strJSONShopIDArray = getCheckedShopArray();

        var arrShopID = JSON.parse(strJSONShopIDArray);

        if(arrShopID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {        	
            $.ajax({
                url: "shop/remove_shop_arr",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    arr_shop_id:strJSONShopIDArray
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

</script>
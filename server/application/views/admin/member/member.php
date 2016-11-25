
<style>
    .cb-row {margin: 10px;clear:both;overflow:hidden;}
    .cb-row label {float:right;}
    .cb-row input {float:right;}
</style>

<div class="container">

    <div class="row" style="border-color: #808080;border-style: solid;border-width: 1px;">
        <div class="row">
            <div class="col-md-6">
                <table class="table table-bordered" style="margin: 5px;">
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>아이디 </label></td>
                        <td><input id="userID" type="text" style="width: 100%;" value="<?php echo $userID;?>"/></td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>성별 </label></td>
                        <td>
                            <select id="userSex">
                                <?php
                                $arrName = Array('전체', '남', '녀');
                                $arrValue = Array('0', 'M', 'W');
                                $cnt = count($arrName);
                                for($i = 0; $i < $cnt; $i++) {
                                    if($userSex == $arrValue[$i]) {
                                        echo '<option value="' . $arrValue[$i] . '" selected>' . $arrName[$i] . '</option>';
                                    }
                                    else {
                                        echo '<option value="' . $arrValue[$i] . '">' . $arrName[$i] . '</option>';
                                    }
                                }
                                ?>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>지역 </label></td>
                        <td>
                            <select id="userLocation">
                                <?php
                                if($userLocation == null || $userLocation == "") {
                                    echo '<option value="0" selected>전체</option>';
                                }
                                else  {
                                    echo '<option value="0">전체</option>';
                                }
                                $cnt = count($arrLocation);
                                for($i = 0; $i < $cnt; $i++) {
                                    $location = $arrLocation[$i];
                                    if($userLocation == ($location['locationName1'].' '.$location['locationName2'])) {
                                        echo '<option value="'.$location['id'].'" seleceted>'.$location['locationName1'].' '.$location['locationName2'].'</option>';
                                    }
                                    else {
                                        echo '<option value="' . $location['id'] . '">' . $location['locationName1'] . ' ' . $location['locationName2'] . '</option>';
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
                        <td style="background:#eeebdc;text-align: center;"><label>상태 </label></td>
                        <td>
                            <select class="combobox" id="userStatus">
                                <?php
                                if($userStatus == STATUS_LIFE) {
                                    echo '<option value="'.STATUS_LIFE.'" selected> 정상 </option>';
                                    echo '<option value="'.STATUS_LIMIT_USER.'"> 제한 </option>';
                                    echo '<option value="'.STATUS_STOP_USER.'"> 스텝 </option>';
                                    echo '<option value="'.STATUS_STOP_LIMIT_USER.'"> 제한 및 스텝 </option>';
                                }
                                else if($userStatus == STATUS_LIMIT_USER) {
                                    echo '<option value="'.STATUS_LIFE.'"> 정상 </option>';
                                    echo '<option value="'.STATUS_LIMIT_USER.'" selected> 제한 </option>';
                                    echo '<option value="'.STATUS_STOP_USER.'"> 스텝 </option>';
                                    echo '<option value="'.STATUS_STOP_LIMIT_USER.'"> 제한 및 스텝 </option>';
                                }
                                else if($userStatus == STATUS_STOP_USER) {
                                    echo '<option value="'.STATUS_LIFE.'"> 정상 </option>';
                                    echo '<option value="'.STATUS_LIMIT_USER.'" > 제한 </option>';
                                    echo '<option value="'.STATUS_STOP_USER.'" selected> 스텝 </option>';
                                    echo '<option value="'.STATUS_STOP_LIMIT_USER.'"> 제한 및 스텝 </option>';
                                }
                                else if($userStatus == STATUS_STOP_LIMIT_USER) {
                                    echo '<option value="'.STATUS_LIFE.'"> 정상 </option>';
                                    echo '<option value="'.STATUS_LIMIT_USER.'" > 제한 </option>';
                                    echo '<option value="'.STATUS_STOP_USER.'"> 스텝 </option>';
                                    echo '<option value="'.STATUS_STOP_LIMIT_USER.'" selected> 제한 및 스텝 </option>';
                                }
                                ?>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>등급 </label></td>
                        <td>
                            <select class="combobox" id="userLevel">
                                <?php
                                if($userLevel == null || $userLevel < 0) {
                                    echo '<option value="-1" selected> 전체 </option>';
                                }
                                else {
                                    echo '<option value="-1"> 전체 </option>';
                                }

                                for($i = 0; $i <= USER_MAX_LEVEL; $i++) {
                                    if($userLevel != null && $i == $userLevel) {
                                        echo '<option value="'.$i.'" selected> 레블'.$i.' </option>';
                                    }
                                    else {
                                        echo '<option value="'.$i.'"> 레블'.$i.' </option>';
                                    }
                                }
                                ?>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td style="background:#eeebdc;text-align: center;"><label>추천인 </label></td>
                        <td><input id="userRecommender" type="text" style="width: 100%;" value="<?php echo $userRecommender;?>"/></td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="row" style="text-align: center;margin: 5px;">
            <button type="button" class="btn btn-default" onclick="search()"/> 검색</button>
            <button type="button" class="btn btn-default" onclick="searchAllList()"/> 전체목록</button>
        </div>

    </div>

    <div style="margin-top: 5px;" >
        <table class="table table-bordered table-scrollable">
            <colgroup>
                <col width="5%">
                <col width="10%">
                <col width="5%">
                <col width="20%">
                <col width="15%">
                <col width="15%">
                <col width="5%">
                <col width="20%">
                <col width="5%">
            </colgroup>
            <thead>
            <tr>
                <th scope="col" class="base-table-header" >번호</th>
                <th scope="col" class="base-table-header" >아이디</th>
                <th scope="col" class="base-table-header" >성별</th>
                <th scope="col" class="base-table-header" >지역</th>
                <th scope="col" class="base-table-header" >상태</th>
                <th scope="col" class="base-table-header" >추천인</th>
                <th scope="col" class="base-table-header" >등급</th>
                <th scope="col" class="base-table-header" >가입일</th>
                <th scope="col" class="base-table-header" >제한</th>
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
                    echo '<td onclick="showUser('.$row['id'].')" style="cursor:pointer;">' . $row['userID'] . '</td>';
                    echo '<td>' . $row['userSex'] . '</td>';
                    echo '<td>' . $row['userLocationName'] . '</td>';
                    if($row['userStatus'] == STATUS_LIFE) {
                        echo '<td>정상</td>';
                    }
                    else if($row['userStatus'] == STATUS_LIMIT_USER) {
                        echo '<td>제한</td>';
                    }
                    else if($row['userStatus'] == STATUS_STOP_USER) {
                        echo '<td>스텝</td>';
                    }
                    else if($row['userStatus'] == STATUS_STOP_LIMIT_USER) {
                        echo '<td>스텝 & 제한</td>';
                    }
                    echo '<td>' . $row['userRecommender'] . '</td>';
                    echo '<td>' . $row['userLevel'] . '</td>';
                    echo '<td>' . $row['userRegisterTime'] . '</td>';

                    if($row['userStatus'] == 0) {
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'.$row['id'].'&'.$row['userStatus'].'" onclick="onClickUserLimit(this)"/>' . '</td>';
                    }
                    else {
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'.$row['id'].'&'.$row['userStatus'].'" onclick="onClickUserLimit(this)" checked/>' . '</td>';
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

    function showUser(idx) {
        window.open('member/show_member?user_id='+idx, '유저보기', 'width=480,height=640');
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
        var userID = $('#userID').val();
        var userSex = $('#userSex').find('option:selected').val();
        var userLocation = $('#userLocation').find('option:selected').text();
        var userStatus = $('#userStatus').find('option:selected').val();
        var userLevel = $('#userLevel').find('option:selected').val();
        var userRecommender = $('#userRecommender').val();

        if(userLocation == '전체') {
            userLocation = null;
        }

        if(userLevel == '-1') {
            userLevel = null;
        }

        searchWithCondition(page, userID, userSex, userLocation, userStatus, userLevel, userRecommender);
    }

    function searchAllList() {
        $('#userID').val('');
        $('#userRecommender').val('');
        $('#userSex').val('0');
        $('#userStatus').val(0);
        $('#userLevel').val(0);
        $('#userLocation').val(0);

        searchWithCondition(1, null, null, null, null, null, null);
    }

    function searchWithCondition(pageNum, userID, userSex, userLocation, userStatus, userLevel, userRecommender ) {

        var url = 'member';
        var isChange = false;

        if(pageNum != null) {
            url = url + '?page_num='+pageNum;
            isChange = true;
        }

        if(userID != null && userID != "") {
            if(isChange == false) {
                url = url + '?userID='+encodeURI(userID);
                isChange = true;
            }
            else {
                url = url + '&userID='+encodeURI(userID);
            }
        }

        if(userRecommender != null && userRecommender != "") {
            if(isChange == false) {
                url = url + '?userRecommender='+encodeURI(userRecommender);
                isChange = true;
            }
            else {
                url = url + '&userRecommender='+encodeURI(userRecommender);
            }
        }

        if(userSex != null && userSex != '0') {
            if(isChange == false) {
                url = url + '?userSex='+userSex;
                isChange = true;
            }
            else {
                url = url + '&userSex='+userSex;
            }
        }

        if(userLocation != null) {
            if(isChange == false) {
                url = url + '?userLocation='+encodeURI(userLocation);
                isChange = true;
            }
            else {
                url = url + '&userLocation='+encodeURI(userLocation);
            }
        }

        if(userStatus != null) {
            if(isChange == false) {
                url = url + '?userStatus='+userStatus;
                isChange = true;
            }
            else {
                url = url + '&userStatus='+userStatus;
            }
        }

        if(userLevel != null) {
            if(isChange == false) {
                url = url + '?userLevel='+userLevel;
                isChange = true;
            }
            else {
                url = url + '&userLevel='+userLevel;
            }
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
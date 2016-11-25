<link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap-datepicker3.css" rel="stylesheet" type="text/css">

<div class="container top">

    <div class="row">
        <div class="col-md-6">
            <div style="margin-top: 5px;">
                <table class="table table-bordered table-scrollable" id="tb_cast_list">
                    <colgroup>
                        <col width="5%">
                        <col width="15%">
                        <col width="70%">
                        <col width="10%">
                    </colgroup>

                    <thead>
                    <tr>
                        <th scope="col" class="base-table-header" >번호</th>
                        <th scope="col" class="base-table-header" >위치</th>
                        <th scope="col" class="base-table-header" >이벤트명</th>
                        <th scope="col" class="base-table-header" >선택</th>
                    </tr>
                    </thead>

                    <tbody id="list">
                    <?php
                    $cnt = count($arrMainBanner);

                    if($cnt == 0) {
                        echo '<tr class="no_data" style="">';
                        echo '<td colspan="10">없습니다.</td>';
                        echo '</tr>';
                    }
                    else {
                        $i = 1;
                        foreach ($arrMainBanner as $row) {
                            echo '<tr>';
                            echo '<td>' .$i. '</td>';
                            echo '<td> 메인'.$i. '</td>';
                            if($row['bannerStatus'] == STATUS_DELETE) {
                                echo '<td onclick="showBannerInfo('.$row['id'].')"  style="cursor:pointer;"> </td>';
                            }
                            else {
                                echo '<td onclick="showBannerInfo('.$row['id'].')"  style="cursor:pointer;">' . $row['bannerTitle'] . '</td>';
                            }

                            if($row['bannerStatus'] == 0) {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                            }
                            else {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" checked/>' . '</td>';
                            }
                            echo '</tr>';

                            $i++;
                        }

                        foreach ($arrCastBanner as $row) {
                            echo '<tr>';
                            echo '<td>' .$i. '</td>';
                            echo '<td> 캐스트'.($i - count($arrMainBanner)). '</td>';
                            echo '<td onclick="showBannerInfo('.$row['id'].')"  style="cursor:pointer;">' . $row['bannerTitle'] . '</td>';
                            if($row['bannerStatus'] == 0) {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                            }
                            else {
                                echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" checked/>' . '</td>';
                            }
                            echo '</tr>';

                            $i++;
                        }

                        echo '<tr>';
                        echo '<td>' .$i. '</td>';
                        echo '<td> 로그아웃 배너</td>';
                        echo '<td onclick="showBannerInfo('.$logoutBanner['id'].')"  style="cursor:pointer;">' . $logoutBanner['bannerTitle'] . '</td>';
                        if($logoutBanner['bannerStatus'] == 0) {
                            echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $logoutBanner['id'] .'"/></td>';
                        }
                        else {
                            echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $logoutBanner['id'] .'" checked/>' . '</td>';
                        }
                        echo '</tr>';
                    }
                    ?>
                    </tbody>
                </table>
            </div>

            <button type="button" class="btn btn-default" onclick="RemoveBanner()">삭제</button>

            <div id="banner_click_title" style="margin-top: 20px;">
                <font size="5"> 배너클릭수 </font>
            </div>

            <div id="from-date-container" style="width: 100%;">
                <table>
                    <tbody>
                    <tr>
                        <td><input type="text" type="text" class="form-control" value="<?php echo $startDate?>" id="fromDate"/></td>
                        <td><label> ~ </label></td>
                        <td><input type="text" type="text" class="form-control"  value="<?php echo $endDate?>" id="toDate"/><td>
                        <td><button type="button" class="btn btn-default  btn-sm" onclick="onSearchBannerClick()" style="margin-left: 5px;">검색</button></td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div id="click_search_result" style="margin-top: 10px;">

            </div>

        </div>

        <div class="col-md-6" id="banner-pannel">

        </div>
    </div>

</div>

<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap-datepicker.js"></script>

<script>

    var selectedBannerID = 0;

    $('#from-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });
    $('#to-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });


    function onSearchBannerClick(id) {
        var startDate = document.getElementById('fromDate').value;
        var endDate = document.getElementById('toDate').value;
        $.ajax({
            url: getBaseURL()+ "/admin/search_life_click",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                banner_id:selectedBannerID,
                start_date:startDate,
                end_date:endDate
            },
            success:function(response){
                $('#click_search_result').html(response);
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function showBannerInfo(id) {
        selectedBannerID = id;
        $.ajax({
            url: getBaseURL()+ "/admin/show_modify_banner",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                banner_id:id
            },
            success:function(response){
                $('#banner-pannel').html(response);
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

    function RemoveBanner() {

        var strJSONIDArray = getCheckedIDArray();

        var arrCastID = JSON.parse(strJSONIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: getBaseURL()+ "/admin/remove_banner",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    arr_banner_id:strJSONIDArray
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
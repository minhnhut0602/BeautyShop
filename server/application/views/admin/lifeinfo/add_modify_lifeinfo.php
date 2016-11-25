
<table>
    <colgroup>
        <col width="20%">
        <col width="80%">
    </colgroup>

    <tbody>
        <tr>
            <td>
                <select class="combobox" id="write_lifeinfo_category">
                    <?php
                    $cnt = count($arrCategory);
                    for($i = 0; $i < $cnt; $i++) {
                        if($lifeinfo['lifeCategoryID'] != null && $arrCategory[$i]['id'] == $lifeinfo['lifeCategoryID']) {
                            echo '<option value="' . $arrCategory[$i]['id'] . '" selected>' . $arrCategory[$i]['categoryName'] . '</option>';
                        }
                        else {
                            echo '<option value="' . $arrCategory[$i]['id'] . '">' . $arrCategory[$i]['categoryName'] . '</option>';
                        }
                    }
                    ?>
                </select>
            </td>
            <td></td>
        </tr>

       <tr>
            <td>광고명:   </td>
            <td>
                <input id="lifeinfo_name" type="text" name="lifeinfo_name" size="10" style="font-size:12pt; color:#000000;width: 100%;" value="<?php echo $lifeinfo['lifeTitle']?>"/>
            </td>
       </tr>

        <tr>
            <td>배너클릭수:   </td>
            <td>
                <?php echo '<label>'.$lifeinfo['lifeClickCnt'].'</label>' ?>
            </td>
        </tr>

        <tr>
                <td>배너등록:   </td>
                <td> (배너크기:660*242)  </td>
        </tr>

        <tr>
            <td></td>
            <td>
                <form method="post" enctype="multipart/form-data" id="bannerUploadForm">
                    <input id="lifeBannerImg" name = "lifeBannerImg" type="file" value="<?php echo  $lifeinfo['lifeAds']['adsImgID'];?>" style="font-size:12pt; color:#000000;">
                    <Label id="lifeBannerImgLabel"><?php echo $lifeinfo['lifeAds']['adsImgURL'];?></Label>
                    </input>
                    <div class ="btn-group" style="margin-top: 5px;">
                        <input type="submit" value="등록"/>
                    </div>
                </form>
            </td>
        </tr>

        <tr>
        <?php
            echo '<td>배너URL:   </td>';
            echo '<td>';
            if($lifeinfo['lifeAds'] != null) {
                echo '<input id="ads_url" value = "'.$lifeinfo['lifeAds']['adsURL'].'" type="url" size="10" style="font-size:12pt; color:#000000;margin-top: 5px;width: 100%;"/>';
            }
            else {
                echo '<input id="ads_url" type="url" size="10" style="font-size:12pt; color:#000000;margin-top: 5px;width: 100%;"/>';
            }
            echo '</td>';
        ?>
        </tr>

        <tr>
            <td>제목:   </td>
            <td>
            <input id="life_subject" type="url" size="10" style="font-size:12pt; color:#000000;margin-top: 5px;width: 100%;" value="<?php echo $lifeinfo['lifeSubject']?>"/>
            </td>
        </tr>

        <tr>
            <td>소개글:   </td>
            <td>
                 <textarea class="form-control" rows="5" id="life_explain" style="text-align:left;margin-top: 5px;"><?php echo $lifeinfo['lifeExplain']?></textarea>
            </td>
        </tr>

        <tr>
            <td>
                <button type="button" class="btn btn-default" onclick="WriteLifeInfo(<?php echo $lifeinfo['id'] ?> )" style="margin-top: 5px;">수정 및 등록 </button>
            </td>
        </tr>
    </tbody>
</table>

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
            <td><button type="button" class="btn btn-default  btn-sm" onclick="onSearchLifeClick(<?php echo $lifeinfo['id'] ?>)" style="margin-left: 5px;">검색</button></td>
        </tr>
        </tbody>
    </table>
</div>

<div id="click_search_result" style="margin-top: 10px;">

</div>

<script>
    $('#from-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });
    $('#to-date-container input').datepicker({
        format: 'yyyy-mm-dd'
    });

    $("#bannerUploadForm").ajaxForm({
        url : getBaseURL()+'/admin/upload_image?kind=event&file_name=lifeBannerImg',
        dataType : 'json',
        success : function (response) {
            var code = response['result_code'];
            if(code == 0) {
                var var_name = "lifeBannerImg";
                document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                var input_name = '#lifeBannerImgLabel';
                $(input_name).text(response['result_data']['imageURL']);
            }
            else {
                alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
            }
        }
    })
    ;

    function onSearchLifeClick(id) {
        var startDate = document.getElementById('fromDate').value;
        var endDate = document.getElementById('toDate').value;
        $.ajax({
            url: "lifeinfo/search_life_click",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                lifeinfo_id:id,
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
</script>
<div class="container" xmlns="http://www.w3.org/1999/html" id="root">
    <?php
        if($isHorizental == true) {
            echo '<div class="row" >';
            echo '<div class="col-md-6">';
        }
        else {
            echo '<div  class="col-md-6">';
            echo '<div>';
        }
    ?>
            <table class="table table-bordered table-scrollable">
                <tbody>
                <colgroup>
                    <col width="20%">
                    <col width="80%">
                </colgroup>
                <tr>
                    <td class="base-table-header">카테고리</td>
                    <td>
                    <select class="combobox" id="write_freetalk_category">
                        <?php
                        $cnt = count($arrCategory);
                        for($i = 0; $i < $cnt; $i++) {
                            if($freetalk['freetalkCategoryID'] != null && $arrCategory[$i]['id'] == $freetalk['freetalkCategoryID']) {
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

                <tr>
                    <td class="base-table-header">작성자ID</td>
                    <td><input id="userID" type="text" style="width: 100%;" value="<?php echo $freetalk['freetalkPostManUserID']; ?>"/></td>
                </tr>

                <tr>
                    <td class="base-table-header">내용</td>
                    <td>
                        <textarea class="form-control" rows="10" id="freetalk_content" style="text-align:left;" value = "<?php echo $freetalk['freetalkContent']?>"><?php echo $freetalk['freetalkContent']?></textarea>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td>
                    <button type="button" class="btn btn-default" onclick="WriteFreetalk(<?php echo $freetalk['id'] ?> )" style="margin-top: 5px;">수정 및 등록 </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

<?php
if($isHorizental == true) {
    echo '<div class="col-md-6">';
}
else {
    echo '<div>';
}
?>
<table class="table table-bordered" style="margin: 5px; width: 460px" id="tb_freetalk_image">
    <colgroup>
        <col width="85%">
        <col width="15%">
    </colgroup>

    <tbody id="freetalk_image_list"  value="<?php echo $freetalk['id']?>">
    <?php
    function url(){
        if(isset($_SERVER['HTTPS'])){
            $protocol = ($_SERVER['HTTPS'] && $_SERVER['HTTPS'] != "off") ? "https" : "http";
        }
        else{
            $protocol = 'http';
        }
        return $protocol."://".$_SERVER['SERVER_NAME'].":".$_SERVER['SERVER_PORT'];
    }

    $isExitDetail = false;
    if($freetalk != null) {
        $cnt = count($freetalk['arrFreetalkImg']);
        for ($i = 0; $i < $cnt; $i++) {
            $image = $freetalk['arrFreetalkImg'][$i];
            echo '<tr value="'.($i+1).'">';
            if ($image['id'] != null) {
                echo '<td value="'.$image['id'] .'">
                            <form method="post" enctype="multipart/form-data" id="imgUploadForm'.($i+1).'" value="'.($i+1).'">
                            <input id="freetalkImg' . ($i + 1) . '" name="freetalkImg' . ($i + 1) . '"  type="file" size="10" value="' . $image['id'] . '" style="font-size:12pt; color:#000000;width:380px">
                            <Label id="freetalkImgLabel'.($i+1).'">'.$image['imageURL'].'</Label></input><input type="submit" value="등록"/> </form></td>';
                $isExitDetail = true;
            } else {
                echo '<td>
                            <form method="post" enctype="multipart/form-data" id="imgUploadForm'.($i+1).'" value="'.($i+1).'">
                            <input id="freetalkImg'.($i+1).'"  name = "freetalkImg'.($i+1).'" value="0" type="file" style="font-size:12pt; color:#000000;width:320px">
                            <Label id="freetalkImgLabel'.($i+1).'"></Label></input><input type="submit" value="등록"/>
                            </form>
                            </td>';

            }
            echo '<script>';
            $form_name = "'#imgUploadForm".($i+1)."'";
            $url = "'".url()."/admin/upload_image?kind=freetalk&file_name=freetalkImg".($i+1)."&index=".($i+1)."'";
            echo "$(".$form_name.").ajaxForm({
                        url : ".$url.",
                        dataType : 'json',
                        success : function (response) {
                            var code = response['result_code'];
                            if(code == 0) {
                                var ret_id = response['result_data']['index'];
                                var var_name = 'freetalkImg'+ret_id;
                                document.getElementById(var_name).setAttribute('value', response['result_data']['id']);
                                var input_name = '#freetalkImgLabel'+(ret_id);
                                $(input_name).text(response['result_data']['imageURL']);
                            }
                            else {
                                alert('저장 실패!지정한 사용자가 유효한지 확인해주세요.');
                            }
                        }
                    });";
            echo '</script>';
            echo '<td> <button onclick="onRemoveFreetalkImage(' . ($i+1) . ', false)">삭제</button>  </td>';
            echo '</tr>';
        }
    }

    if($isExitDetail == false) {
        echo '<tr class="no_data">';
        echo '<td colspan="10">없습니다.</td>';
        echo '</tr>';
    }
    ?>
    </tbody>
</table>

        <div>
            <button type="button" class="btn btn-default" onclick="WriteFreetalk(<?php echo $freetalk['id'] ?> )" >수정 및 등록 </button>
            <button type="button" class="btn btn-default" onclick="AddNewImage(<?php echo $freetalk['id'] ?>)" style="margin-left: 5px;">새 이미지 추가</button>
        </div>

        </div>
    </div>
</div>

<script>
    function addCode(script) {
        var JS= document.createElement('script');
        JS.text= script;
        document.body.appendChild(JS);
    }

    function AddNewImage(id) {
        var table = document.getElementById("tb_freetalk_image");

        var lastIdx = table.rows.length;

        if(lastIdx == 1) {
            var row = table.rows[0];
            if(row.getAttribute("class") == "no_data") {
                table.deleteRow(0);
                lastIdx = 0;
            }
        }
        else {
            var lastCell = table.rows[lastIdx - 1];
            lastIdx = parseInt(lastCell.getAttribute("value"));
        }

        //
        // add image row
        //

        // Create an empty <tr> element and add it to the last position of the table:
        var row = table.insertRow(-1);

        // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);

        var newIdx = lastIdx+1;
        row.setAttribute("value", newIdx); // cast id

        var castImgVarName = "freetalkImg"+newIdx;
        var url = getBaseURL()+"/admin/upload_image?kind=freetalk&file_name=freetalkImg"+(newIdx)+"&index="+(newIdx);

        cell1.innerHTML = '<td>' +
        '<form method="post" enctype="multipart/form-data" id="imgUploadForm'+newIdx+'" value="'+newIdx+'">'+
        '<input id= "'+castImgVarName+'" name="'+castImgVarName+'" type="file" size="10" style="font-size:12pt; color:#000000;width:380px"/>' +
        '<label id="freetalkImgLabel'+newIdx+'">'+'이미지를 입력해주세요.</label>'+
        '<input type="submit" value="등록"/></form></td>';

        var script =  "$('#imgUploadForm"+newIdx+"').ajaxForm({"+
            "url : '"+url+"',"+
            "dataType : 'json',"+
            "success : function (response) {"+
            "var code = response['result_code'];"+
            "if(code == 0) {"+
            "var ret_id = response['result_data']['index'];"+
            "var var_name = 'freetalkImg'+ret_id;"+
            "document.getElementById(var_name).setAttribute('value', response['result_data']['id']);"+
            "var input_name = '#freetalkImgLabel'+(ret_id);"+
            "$(input_name).text(response['result_data']['imageURL']);"+
            "}"+
            "else {"+
            "alert('저장 실패!지정한 사용자가 유효한지 확인해주세요.');"+
            "}"+
            "}"+
            "});";
        addCode(script);

        cell2.innerHTML = '<button onclick="onRemoveFreetalkImage('+newIdx+', true)">삭제</button>';
    }

    function onRemoveFreetalkImage(id, isNewCell) {
        var table = document.getElementById("tb_freetalk_image");
        var id = parseInt(id) - 1;
        var del_idx = parseInt(id);
        table.deleteRow(del_idx);

        refresh();
    }

    function refresh () {
        var table = document.getElementById("tb_freetalk_image");
        if(table.rows.length == 0) {
            var row = table.insertRow(0);
            var cell1 = row.insertCell(0);

            row.setAttribute("class", "no_data");
            cell1.innerHTML = '<td colspan="10">없습니다.</td>';
        }
    }

    function getFreetalkImageArray() {
        var tvFreetalkImage = document.getElementById("tb_freetalk_image");
        var length = tvFreetalkImage.rows.length;
        var arrFreetalkImage = new Array;

        if(length == 1) {
            var className = tvFreetalkImage.rows[0].getAttribute("class");
            if(className != null && isUndifined(className) == false && className == "no_data") {
                return;
            }
        }

        var i = 0;
        while(i < length) {

            var idx = tvFreetalkImage.rows[i].getAttribute("value");
            var varName = "freetalkImg"+(idx);

            var detailImgID = document.getElementById(varName).getAttribute("value");

            arrFreetalkImage.push(detailImgID);

            i++;
        }

        return arrFreetalkImage;
    }


    function WriteFreetalk(id) {
        var freetalkID = id;
        var freetalkContent = $('#freetalk_content').val();
        var freetalkUserID =  $('#userID').val();
        var categoryID = $('#write_freetalk_category').val();

        if(freetalkContent == null || freetalkContent == "") {
            alert("내용을 입력해주세요.");
            return;
        }

        if(freetalkUserID == null || freetalkUserID == "") {
            alert("작성자ID를 입력해주세요.");
            return;
        }

        if (isUndifined(freetalkID) == true) { // Any scope
            freetalkID  = null;
        }

        if(categoryID == 0) {
            categoryID = null;
        }

        var freetalkImageArray = getFreetalkImageArray();
        var json = JSON.stringify(freetalkImageArray);

        $.ajax({
            url: getBaseURL() + "/freetalk/write_freetalk",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                freetalk_content:freetalkContent,
                freetlk_img_array:json,
                freetalk_id:freetalkID,
                freetalk_user_id:freetalkUserID,
                freetalk_category_id:categoryID
            },
            success:function(response){
                if(response['result_code'] == 0) {
                    location.reload(true);
                }
                else {
                    alert(response['result_msg']);
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

</script>
<div class="input-group">
    <select class="combobox" id="write_cast_category">
        <?php
        $cnt = count($arrCategory);
        for($i = 0; $i < $cnt; $i++) {
            if($cast['castCategoryID'] != null && $arrCategory[$i]['id'] == $cast['castCategoryID']) {
                echo '<option value="' . $arrCategory[$i]['id'] . '" selected>' . $arrCategory[$i]['categoryName'] . '</option>';
            }
            else {
                echo '<option value="' . $arrCategory[$i]['id'] . '">' . $arrCategory[$i]['categoryName'] . '</option>';
            }
        }
        ?>
    </select>
</div>

<p/>

<div class="input-group">
    <label >제목:   </label>
    <?php
    if($cast != null) {
        echo '<input id = "castTitle" type = "text" name = "castTitle" size = "10" value = "'.$cast['castTitle'].'" style = "font-size:12pt; color:#000000; width: 300px">  (배너크기:   임의의 크기)</input>';
    }
    else {
        echo '<input id = "castTitle" type = "text" name = "castTitle" size = "10" style = "font-size:12pt; color:#000000; width: 300px" >    (배너크기:   임의의 크기)</input>';
    }
    ?>
</div>

<div  class="input-group">
    <table class="table table-bordered" style="margin: 5px; width: 460px" id="tb_cast_detail">
        <colgroup>
            <col width="85%">
            <col width="15%">
        </colgroup>

        <tbody id="cast_detail_list"  value="<?php echo $cast['id']?>">
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
        if($cast != null) {
            $cnt = count($cast['arrCastDetail']);
            for ($i = 0; $i < $cnt; $i++) {
                echo '<tr value="'.$cast['arrCastDetail'][$i]['id'].'">';
                if ($cast['arrCastDetail'][$i] != null) {
                    echo '<td value="'. $cast['arrCastDetail'][$i]['castdetailImgID'] .'">
                            <form method="post" enctype="multipart/form-data" id="imgUploadForm'.($i+1).'" value="'.($i+1).'">
                            <input id="castdetailImg' . ($i + 1) . '" name="castdetailImg' . ($i + 1) . '"  type="file" size="10" value="' . $cast['arrCastDetail'][$i]['castdetailImgID'] . '" style="font-size:12pt; color:#000000;width:380px">
                            <Label id="castdetailImgLabel'.($i+1).'">'.$cast['arrCastDetail'][$i]['castdetailImgURL'].'</Label></input><input type="submit" value="등록"/> </form></td>';
                    $isExitDetail = true;
                } else {
                    echo '<td>
                            <form method="post" enctype="multipart/form-data" id="imgUploadForm'.($i+1).'" value="'.($i+1).'">
                            <input id="castdetailImg'.($i+1).'"  name = "castdetailImg'.($i+1).'" value="0" type="file" style="font-size:12pt; color:#000000;width:320px">
                            <Label id="castdetailImgLabel'.($i+1).'"></Label></input><input type="submit" value="등록"/>
                            </form>
                            </td>';

                }
                echo '<script>';
                $form_name = "'#imgUploadForm".($i+1)."'";
                $url = "'".url()."/admin/upload_image?kind=cast&file_name=castdetailImg".($i+1)."&index=".($i+1)."'";
                echo "$(".$form_name.").ajaxForm({
                        url : ".$url.",
                        dataType : 'json',
                        success : function (response) {
                            var code = response['result_code'];
                            if(code == 0) {
                                var ret_id = response['result_data']['index'];
                                var var_name = 'castdetailImg'+ret_id;
                                document.getElementById(var_name).setAttribute('value', response['result_data']['id']);
                                var input_name = '#castdetailImgLabel'+(ret_id);
                                $(input_name).text(response['result_data']['imageURL']);
                            }
                            else {
                                alert('저장 실패!지정한 사용자가 유효한지 확인해주세요.');
                            }
                        }
                    });";
                echo '</script>';
                echo '</tr>';

                echo '<tr value="'.($i+1).'">';
                if ($cast['arrCastDetail'][$i] != null) {
                    $isExitDetail = true;
                    echo '<td>
                            <input id="castdetailPublish' . ($i + 1) . '" type="url" size="10" value="' . $cast['arrCastDetail'][$i]['castdetailPublish'] . '" style="font-size:12pt; color:#000000;width:420px">:출처</input>
                            <input id="castdetailContent' . ($i + 1) . '" type="url" size="10" value="' . $cast['arrCastDetail'][$i]['castdetailContent'] . '" style="font-size:12pt; color:#000000;width:480px"/>
                            </td>';
                } else {
                    echo '<td>
                            <input id="castdetailPublish' . ($i + 1) . '" type="url" size="10" style="font-size:12pt; color:#000000;width:420px">:출처</input>
                            <input id="castdetailContent' . ($i + 1) . '" type="url" size="10" style="font-size:12pt; color:#000000;width:480px"/>
                            </td>';
                }
                echo '<td> <button onclick="onRemoveCastDetail(' . $cast['arrCastDetail'][$i]['id'] . ', false)">삭제</button>  </td>';
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
</div >

<button type="button" class="btn btn-default" onclick="WriteCast(<?php echo $cast['id'] ?> )">수정 및 등록 </button>
<button type="button" class="btn btn-default" onclick="AddNewPage(<?php echo $cast['id'] ?>)">페이지추가</button>


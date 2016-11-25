<style>
    .select_gallary_item {
        width: 200px; height: 200px;border-style: solid;border-width: 3px;border-color: #ff0000;
    }
    .select_not_gallary_item {
        width: 200px; height: 200px;
    }

    ul.nav li.dropdown:hover ul.dropdown-menu{
        display: block;
    }
</style>

<div class="container">
    <div class="row">

        <div class="col-md-8"  style="height:480px;">
            <div class="bodyfullcontainer scrollable">
                <table class="table table-bordered table-scrollable">
                    <tbody id="gallery_list" value="<?php echo $selectImageIdx;?>">
                    <?php
                    $cnt = count($arrImage);

                    if($cnt == 0) {
                        echo '<tr class="__oldlist" style="">';
                        echo '<td colspan="9">없습니다.</td>';
                        echo '</tr>';
                    }
                    else {
                       for($i = 0; $i < $cnt; $i +=  ADMIN_IMAGE_ROW_CNT) {
                           echo '<tr>';
                           for($j = $i; $j < ($i + ADMIN_IMAGE_ROW_CNT); $j++) {
                               $row = $arrImage[$j];
                               echo '<td>';
                               if($j == $selectImageIdx) {
                                   echo '<div class="select_gallary_item" id="gallery_item'.$j.'" value="'.$row['id'].'">';
                               }
                               else {
                                   echo '<div class="select_not_gallary_item" id="gallery_item'.$j.'"  value="'.$row['id'].'">';
                               }
                               echo '<img src="'.$row['imageURL'].'" style="width: 100%;max-height: 100%"  onclick="selectImage('.$row['id'].','.$j.')"  style="cursor:pointer;"/>';
                               echo '</div>';
                               echo '</td>';
                           }
                           echo '</tr>';
                       }
                    }
                    ?>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-md-4">
            <table class="table table-bordered table-scrollable" id="tb_info">
                <tbody id="list">
                <?php
                echo '<tr>';
                echo '<td class="base-table-header" >작성자</td>';
                echo '<td id="uploadUserID">'.$selectImage['imageUploadRealUserID'].'</td>';
                echo '</tr>';

                echo '<tr>';
                echo '<td class="base-table-header" >등록일</td>';
                echo '<td  id="uploadTime">'.$selectImage['imageUploadTime'].'</td>';
                echo '</tr>';

                echo '<tr>';
                echo '<td class="base-table-header">등록위치</td>';
                $arrLocation = Array("상점", "자유톡", "캐스트", "이벤트", "유저", "일반");
                echo '<td  id="uploadLocation">'.$arrLocation[$selectImage['imageUploadLocation']].'</td>';
                echo '</tr>';
                ?>
                </tbody>
            </table>

            <button type="button" class="btn btn-default" onclick="RemoveImage()">삭제</button>
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
</div>

<script>

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;

            searchPage(currentPage);

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

        searchPage(currentPage);
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        searchPage(currentPage);
    }

    function selectImage(id, index) {
        $.ajax({
            url: "image/select_image",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                image_id:id,
                image_index:index
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    var select_image_index = response['result_data']['image_index'];
                    var last_select_image_index = document.getElementById("gallery_list").getAttribute("value");
                    var gallery_list = document.getElementById("gallery_list").setAttribute("value", select_image_index);

                    var last_select_image_cell = $('#gallery_item'+last_select_image_index);
                    last_select_image_cell.removeClass("select_gallary_item");
                    last_select_image_cell.addClass("select_not_gallary_item");

                    var select_image_cell = $('#gallery_item'+select_image_index);
                    select_image_cell.removeClass("select_not_gallary_item");
                    select_image_cell.addClass("select_gallary_item");

                    updateImageInfo(response['result_data']['image_info']);
                }
                else {
                    alert("선택된 이미지가 없습니다.");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function updateImageInfo(imageInfo) {
        $('#uploadUserID').html(imageInfo['imageUploadRealUserID']);
        $('#uploadTime').html(imageInfo['imageUploadTime']);

        switch (parseInt(imageInfo['imageUploadLocation'])) {
            case 0:
                $('#uploadLocation').html('상점');
                break;
            case 1:
                $('#uploadLocation').html('자유톡');
                break;
            case 2:
                $('#uploadLocation').html('캐스트');
                break;
            case 3:
                $('#uploadLocation').html('이벤트');
                break;
            case 4:
                $('#uploadLocation').html('유저');
                break;
            default:
                $('#uploadLocation').html('일반');
                break;
        }

    }

    function searchPage(page_num) {
        searchWithCont(page_num, null, null);
    }

    function searchWithCont(page_num, id, index) {
        if(page_num == null) {
            page_num = 1;
        }

        var url  = getBaseURL()+'/image?page_num='+page_num;

        if(id != null) {
            url += '&image_id='+id;
        }

        if(index != null) {
            url += '&image_index='+index;
        }

        location.href = url;
    }

    function RemoveImage() {
        var last_select_image_index = document.getElementById("gallery_list").getAttribute("value");

        if(last_select_image_index == null || last_select_image_index == "") {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        var image_id = document.getElementById("gallery_item"+last_select_image_index).getAttribute("value");

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: "image/remove_image",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    image_id:parseInt(image_id)
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
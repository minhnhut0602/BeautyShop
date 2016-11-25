<div class="container" xmlns="http://www.w3.org/1999/html">

    <div class="col-md-12">
        <div class="row" style="margin-left: 5px;">
            <table>
                <tbody>
                <tr>
                    <td>주소</td>
                    <td>
                        <input id="address" type="text" style="width: 100%;margin-left: 10px;" value="<?php echo $address;?>"/>
                    </td>
                    <td>
                        <div class="input-group input-group-sm" style="margin-left: 20px;">
                            <button type="button" class="btn btn-default" onclick="onClickSearch()"/> 검색</button>
                            <button type="button" class="btn btn-default" onclick="onClickSearchAll()"/> 전체목록</button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div style="margin-top: 10px;">
            <table class="table table-bordered table-scrollable">
                <colgroup>
                    <col width="10%">
                    <col width="10%">
                    <col width="20%">
                    <col width="50%">
                    <col width="10%">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col">등록순서</th>
                    <th scope="col">상점번호</th>
                    <th scope="col">상점이름</th>
                    <th scope="col">상점주소</th>
                    <th scope="col">상태</th>
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
                        echo '<td>' .( $i + ($currentPageNum - 1) * 20) . '</td>';
                        echo '<td>' . $row['id'] . '</td>';
                        echo '<td>' . $row['shopName'] . '</td>';
                        echo '<td>' . $row['shopAddress'] . '</td>';
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                        echo '</tr>';
                        $i++;
                    }
                }
                ?>
                </tbody>
            </table>
        </div>
    </div>

    <div class="cb-row">
        <button type="button" class="btn btn-default" onclick="onClickAdd()">추가</button>
        <button type="button" class="btn btn-default" onclick="onClickRemove()" style="margin-right: 5px;">삭제</button>
    </div>
</div>

<!-- Simple pop-up dialog box, containing a form -->
<dialog id="favDialog" style="width: 220px;">
    <form method="dialog">
        <section>
            <table>
            <tr>
                <td>
                    <input id="consequence" type="text" name="consequence" size="10" style="font-size:12pt; color:#000000; width: 100px">순서</input>
                </td>
            </tr>
                <tr >
                <td>
                    <input id="shop_id" type="text" name="consequence" size="10" style="font-size:12pt; color:#000000; width: 100px;margin-top: 3px;">상점넘버</input>
                </td>
                </tr>
            </table>
        </section>
        <menu>
            <button id="cancel" type="reset">취소</button>
            <button id="ok" type="button">확인</button>
        </menu>
    </form>
</dialog>

<script>

    function onClickAdd() {
        var favDialog = document.getElementById('favDialog');
        var cancelButton = document.getElementById('cancel');
        var okButton = document.getElementById('ok');

        // Form cancel button closes the dialog box
        cancelButton.addEventListener('click', function() {
            favDialog.close();
        });

        okButton.addEventListener('click', function() {
            var consequenc = document.getElementById('consequence').value;
            var shop_id = document.getElementById('shop_id').value;

            if(isNaN(consequenc) == true || isNaN(shop_id) == true) {
                alert("수자를 입력해주세요.");
                return;
            }

            var consequence = parseInt(consequenc);
            var shop_id = parseInt(shop_id);
            $.ajax({
                url: "add_shop_consequence",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    id:shop_id,
                    consequence:consequence
                },
                success: function (response) {

                    if(response['result_code'] == 0) {
                        location.reload(true);
                        favDialog.close();
                    }
                    else {
                        alert("추가실패!");
                    }
                },
                error: function (jqXHR, msg, erro) {
                    alert("전송 실패! 망연결을 확인해주세요.");
                }
            });
        });

        favDialog.showModal();
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

    function onClickRemove() {
        var arrID = getCheckedIDArray();
        $.ajax({
            url: "remove_shop_consequence",
            type: "post", // To protect sensitive data
            data: {
                ajax: true,
                arr_id:arrID
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
    }

    function onClickSearch() {
        search();
    }

    function onClickSearchAll() {
        $('#address').val('');
        search();
    }

    function search() {
        var address = document.getElementById("address").value;

        var url  = getBaseURL()+'/shop/shop_consequence';

        if(address != null && address != "") {
            url = url + '?address='+encodeURI(address);
        }

        location.href = url;
    }

</script>
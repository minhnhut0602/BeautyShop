<div class="container" xmlns="http://www.w3.org/1999/html" id="root">

    <table class="table table-bordered table-scrollable">
        <tbody>
        <colgroup>
            <col width="20%">
            <col width="80%">
        </colgroup>
        <tr>
            <td class="base-table-header">상점</td>
            <td>
               <label id="shopID"></label>
                <button type="button" class="btn btn-default" onclick="FindShop()" >찾기 </button>
            </td>
        </tr>

        <tr>
            <td class="base-table-header">별점</td>
            <td>
                <select class="combobox" id="mark">
                    <?php
                    for($i = 0; $i < 5; $i++) {
                        echo '<option value="' .($i+1). '">' . ($i+1) . '</option>';
                    }
                    ?>
                </select>
            </td>
        </tr>

        <tr>
            <td class="base-table-header">작성자ID</td>
            <td><input id="userID" type="text" style="width: 100%;"</td>
        </tr>

        <tr>
            <td class="base-table-header">내용</td>
            <td>
                <textarea class="form-control" rows="10" id="content" style="text-align:left;"></textarea>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div>
    <button type="button" class="btn btn-default" onclick="WriteUserRemark()" >수정 및 등록 </button>
</div>

<script>

    var arrSelectedShop = null;

    function HandlePopupResult(arrShop){
        if(arrShop == null || arrShop.length == 0) {
            return;
        }

        var strArrShop = JSON.stringify(arrShop);

        arrSelectedShop = arrShop;

        $.ajax({
            url: "get_shop_array",
            type: "get", // To protect sensitive data
            data: {
                ajax:true,
                arr_shop:strArrShop
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                   var shopID = document.getElementById("shopID");
                    var arrShop =  response['result_data'];
                    var strShopName = arrShop[0]['shopName'];
                    var i = 1;
                    while(i < arrShop.length) {
                        strShopName = strShopName + "," + arrShop[i]['shopName'];
                        i++;
                    }
                    shopID.innerHTML = strShopName;
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

    function WriteUserRemark() {
        if(arrSelectedShop == null || arrSelectedShop.length == 0) {
            alert("상점을 선택해주세요.");
            return;
        }

        var userID = $('#userID').val();
        var mark = document.getElementById("mark").value;
        var content = document.getElementById("content").value;

        if(userID == null) {
            alert("작성자ID를 입력해주세요.");
            return;
        }

        if(content == null) {
            alert("내용을 입력해주세요.");
            return;
        }

        $.ajax({
            url: "write_user_remark",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                arr_shop:JSON.stringify(arrSelectedShop),
                user_id:userID,
                mark:mark,
                content:content
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    alert("성공!");
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

    function FindShop() {
        window.open( getBaseURL()+'/shop/find_shop', '상점찾기', 'width=840,height=720');
    }


</script>
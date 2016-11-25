<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>미미샵</title>
    <link href="<?php echo base_url(); ?>assets/css/admin/global.css" rel="stylesheet" type="text/css">
    <link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap.css" rel="stylesheet" type="text/css">
    <style type="text/css">

        ::selection{ background-color: #E13300; color: white; }
        ::moz-selection{ background-color: #E13300; color: white; }
        ::webkit-selection{ background-color: #E13300; color: white; }

        body {
            background-color: #fff;
            margin: 40px;
            font: 13px/20px normal Helvetica, Arial, sans-serif;
            color: #4F5155;
        }

        a {
            color: #003399;
            background-color: transparent;
            font-weight: normal;
        }

        h1 {
            color: #444;
            background-color: transparent;
            border-bottom: 1px solid #D0D0D0;
            font-size: 19px;
            font-weight: normal;
            margin: 0 0 14px 0;
            padding: 14px 15px 10px 15px;
        }

        code {
            font-family: Consolas, Monaco, Courier New, Courier, monospace;
            font-size: 12px;
            background-color: #f9f9f9;
            border: 1px solid #D0D0D0;
            color: #002166;
            display: block;
            margin: 14px 0 14px 0;
            padding: 12px 10px 12px 10px;
        }

        #body{
            margin: 0 15px 0 15px;
        }

        p.footer{
            text-align: right;
            font-size: 11px;
            border-top: 1px solid #D0D0D0;
            line-height: 32px;
            padding: 0 10px 0 10px;
            margin: 20px 0 0 0;
        }

        #container{
            margin: 10px;
            border: 1px solid #D0D0D0;
            -webkit-box-shadow: 0 0 8px #D0D0D0;
        }

        #submit_container {
            width: 400px;
            height: 30px;
            text-align:center;
        }
        #btn_transfer {
            width: 70px;
            height: 30px;
        }
    </style>
</head>

<body>

<div class="container top">
    <button type="button" class="btn btn-default" onclick="addProduct()">리스트추가</button>
    <p/>
    <div class="row">
        <div id = "panel_comment" value="<?php echo $shopID?>">
            <table class="table table-bordered table-scrollable">
                <colgroup>
                    <col width="10%">
                    <col width="30%">
                    <col width="25%">
                    <col width="25%">
                    <col width="10%">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"  class="base-table-header">번호</th>
                    <th scope="col"  class="base-table-header">상품명</th>
                    <th scope="col"  class="base-table-header">정상가</th>
                    <th scope="col"  class="base-table-header">미미샵회원가</th>
                    <th scope="col"  class="base-table-header">선택</th>
                </tr>
                </thead>

                <tbody id="list">
                <?php
                $cnt = count($arrProduct);

                if($cnt == 0) {
                    echo '<tr class="__oldlist" style="">';
                    echo '<td colspan="9">없습니다.</td>';
                    echo '</tr>';
                }
                else {
                    foreach ($arrProduct as $row) {
                        echo '<tr>';
                        echo '<td>' . $row['id'] . '</td>';
                        echo '<td onclick="showProduct('.$row['id'].')">' . $row['productName'] . '</td>';
                        echo '<td>' . $row['productPrice'] . '</td>';
                        echo '<td>' . $row['productEventPrice'] . '</td>';
                        echo '<td><select class="combobox" id="productStatus" onchange="onChangeStatus('.$row['id'].', this)">';
                        if($row['productStatus'] == STATUS_LIFE) {
                            echo '<option value="' . STATUS_LIFE . '" selected> 일반 </option>';
                        }
                        else {
                            echo '<option value="' . STATUS_LIFE . '"> 일반 </option>';
                        }
                        if($row['productStatus'] == STATUS_DELETE) {
                            echo '<option value="' . STATUS_DELETE . '" selected> 삭제 </option>';
                        }
                        else {
                            echo '<option value="' . STATUS_DELETE . '"> 삭제 </option>';
                        }
                        echo '</select></td>';
                        echo '</tr>';
                    }
                }
                ?>
                </tbody>
            </table>
        </div>
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

<!-- Simple pop-up dialog box, containing a form -->
<dialog id="productDialog"  style="width: 300px;">
    <form method="dialog">
        <section>
            <table class="table table-bordered" style="margin: 5px;">
                <thead>
                <tr>
                    <th scope="col"  class="base-table-header">상품추가</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td class="base-table-header">상품이름</td>
                    <td><input id="product_name" type="text" size="10" style="font-size:12pt; color:#000000;"/></td>
                </tr>
                <tr>
                    <td class="base-table-header">정가격</td>
                    <td><input id="product_price" type="text" size="10" style="font-size:12pt; color:#000000;"/></td>
                </tr>
                <tr>
                    <td class="base-table-header">상품이벤트가격</td>
                    <td><input id="product_event_price" type="text" size="10" style="font-size:12pt; color:#000000;"/></td>
                </tr>
                </tbody>
            </table>
        </section>
        <menu>
            <div style="text-align: center;">
                <button id="cancel" type="reset">취소</button>
                <button id="ok" type="button">등록</button>
            </div>
        </menu>
    </form>
</dialog>

<script>

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            var shop_id = document.getElementById('panel_comment').getAttribute("value");
            location.href = 'shop_product_list?shop_id='+shop_id+'&page_num='+currentPage;
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
        var shop_id = document.getElementById('panel_comment').getAttribute("value");
        location.href = 'shop_product_list?shop_id='+shop_id+'&page_num='+currentPage;
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }
        var shop_id = document.getElementById('panel_comment').getAttribute("value");
        location.href = 'shop_product_list?shop_id='+shop_id+'&page_num='+currentPage;
    }

    function onChangeStatus(id, control) {
        var selectedValue = control.value;

        $.ajax({
            url: "update_product",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                product_id:id,
                product_status:selectedValue
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    alert("저장되었습니다.");
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

    function addProduct() {
        var favDialog = document.getElementById('productDialog');
        var cancelButton = document.getElementById('cancel');
        var okButton = document.getElementById('ok');

        // Form cancel button closes the dialog box
        cancelButton.addEventListener('click', function() {
            favDialog.close();
        });

        okButton.addEventListener('click', function() {
            var name = document.getElementById('product_name').value;
            var price = document.getElementById('product_price').value;
            var event_price = document.getElementById('product_event_price').value;

            if(name == "" || price== null) {
                alert("상품이름과 가격을 입력해주세요.");
                return;
            }

            if(event_price == null) {
                event_price = price;
            }

            var shop_id = document.getElementById('panel_comment').getAttribute("value");

            $.ajax({
                url: "add_product",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    product_name:name,
                    product_price:price,
                    product_event_price:event_price,
                    product_shop_id:shop_id
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

    function showProduct(idx) {
        window.open('update_shop_product?product_id='+idx, '삼품변경', 'width=720,height=720');
    }

    function HandlePopupResult(){
        location.reload();
    }

</script>


</body>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

</html>
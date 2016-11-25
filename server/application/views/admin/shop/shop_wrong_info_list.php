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
    <div class="row">
        <div id = "panel_comment" value="<?php echo $shopID?>">
            <table class="table table-bordered table-scrollable">
                <colgroup>
                    <col width="5%">
                    <col width="10%">
                    <col width="15%">
                    <col width="30%">
                    <col width="20%">
                    <col width="10%">
                    <col width="5%">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"  class="base-table-header">번호</th>
                    <th scope="col"  class="base-table-header">아이디</th>
                    <th scope="col"  class="base-table-header">상점이름</th>
                    <th scope="col"  class="base-table-header">수정요청내용</th>
                    <th scope="col"  class="base-table-header">보낸날자</th>
                    <th scope="col"  class="base-table-header">상태</th>
                    <th scope="col"  class="base-table-header">선택</th>
                </tr>
                </thead>

                <tbody id="list">
                <?php
                $cnt = count($arrShopComment);

                if($cnt == 0) {
                    echo '<tr class="__oldlist" style="">';
                    echo '<td colspan="9">없습니다.</td>';
                    echo '</tr>';
                }
                else {
                    foreach ($arrShopComment as $row) {
                        echo '<tr>';
                        echo '<td>' . $row['id'] . '</td>';
                        echo '<td>' . $row['userID'] . '</td>';
                        echo '<td>' . $row['shopName'] . '</td>';
                        if(empty($row['shopcommentContent'])) {
                        	$comment = array('상점이 폐업했어요', '상점이름이 잘못되었어요', '주소가 잘못되었어요', '전화번호가 잘못되었어요','영업시간이 잘못되었어요','할인정보가 잘못되었어요');                        	
                        	echo '<td>' . $comment[$row['shopcommentShopStatus']] . '</td>';
                        } else                         	
                        	echo '<td>' . $row['shopcommentContent'] . '</td>';
                        
                        echo '<td>' . $row['shopcommentPostTime'] . '</td>';

                        if($row['shopcommentStatus'] == STATUS_MANAGE) {
                            echo '<td>확인</td>';
                        }
                        else {
                            echo '<td>미확인</td>';
                        }

                        if($row['shopcommentStatus'] == STATUS_LIFE || $row['shopcommentStatus'] == STATUS_MANAGE) {
                            echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                        }
                        else {
                            echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'" checked/>' . '</td>';
                        }
                        echo '</tr>';
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

    <button type="button" class="btn btn-default" onclick="RemoveQuestion()">삭제</button>
    <button type="button" class="btn btn-default" onclick="ManageQuestion()">확인</button>

</div>

<dialog id="confirmBox" style="width: 320px;">
    <form method="dialog">
        <section>
            <div class="message" id="message"></div>
        </section>

        <div  style="display: flex;justify-content: center;">
            <button class="yes" id="ok" type="reset">확인</button>
            <button  class="no" id="cancel" type="button" style="margin-left: 5px;">취소</button>
        </div>
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
            search(currentPage);
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
        search(currentPage);
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }
        search(currentPage);
    }

    function search(page_num) {
        var url = getBaseURL()+'/shop/shop_wrong_info_list?page_num='+page_num;
        location.href = url;
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

    function doConfirm(msg, yesFn, noFn)
    {
        var confirmBox = document.getElementById('confirmBox');
        var cancelButton = document.getElementById('cancel');
        var okButton = document.getElementById('ok');
        var message = document.getElementById('message');
        message.textContent=msg;

        // Form cancel button closes the dialog box
        cancelButton.addEventListener('click', function() {
            confirmBox.close();
        });

        okButton.addEventListener('click',yesFn);

        confirmBox.showModal();
    }

    function RemoveQuestion() {

        var strJSONIDArray = getCheckedIDArray();

        var arrCastID = JSON.parse(strJSONIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 삭제하시겠습니까?", function yes()
        {
            $.ajax({
                url: getBaseURL()+"/shop/remove_shop_comment",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    comment_id_array:strJSONIDArray
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

    function ManageQuestion() {
        var strJSONIDArray = getCheckedIDArray();

        var arrCastID = JSON.parse(strJSONIDArray);

        if(arrCastID.length == 0) {
            alert("선택한 게시물이 없습니다.");
            return;
        }

        doConfirm("선택하신 게시물을 확인 상태로 하겠습니까?", function yes()
        {
            $.ajax({
                url: getBaseURL()+"/shop/manage_shop_comment",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    comment_id_array:strJSONIDArray
                },
                success: function (response) {

                    if(response['result_code'] == 0) {
                        location.reload(true);
                    }
                    else {
                        alert("확인실패!");
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


</body>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

</html>
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
        <div id = "panel_comment">
            <table class="table table-bordered table-scrollable">
                <colgroup>
                    <col width="10%">
                    <col width="20%">
                    <col width="50%">
                    <col width="20%">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col"  class="base-table-header" >번호</th>
                    <th scope="col"  class="base-table-header" >유저</th>
                    <th scope="col"  class="base-table-header" >내용</th>
                    <th scope="col"  class="base-table-header" >날짜</th>
                </tr>
                </thead>

                <tbody id="list">
                <?php
                $cnt = count($arrQuestion);

                if($cnt == 0) {
                    echo '<tr class="__oldlist" style="">';
                    echo '<td colspan="9">없습니다.</td>';
                    echo '</tr>';
                }
                else {
                    foreach ($arrQuestion as $row) {
                        echo '<tr>';
                        echo '<td>' . $row['id'] . '</td>';
                        echo '<td>' . $row['userID'] . '</td>';
                        echo '<td>' . $row['questionContent'] . '</td>';
                        echo '<td>' . $row['questionPostTime'] . '</td>';
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

<script>

    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            location.href = 'question_list?page_num='+currentPage;
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

        location.href = 'question_list?page_num='+currentPage;
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        location.href = 'question_list?page_num='+currentPage;
    }

</script>


</body>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

</html>
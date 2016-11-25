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

<body onload="onLoad()">

<div class="container top">
    <div class="row">

        <!--
        <div class="col-md-6" id = "panel_logout_banner">
            <div>
                <table class="table table-bordered table-scrollable">

                    <tbody>

                    <tr>
                        <td class="base-table-header">  메인배너등록  (배너크기:720*262) </td>
                    </tr>
                    <tr>
                        <td>
                        <div>
                            <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm1" style="margin-left: 20px;" value="<?php echo $arrBanner[0]['id'];?>">
                                <input id="mainBannerImg1" name = "mainBannerImg1" type="file" value="<?php echo $arrBanner[0]['adsImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                                <Label id="mainBannerImgLabel1"><?php echo $arrBanner[0]['adsImgURL'];?></Label>
                                </input>
                                <div class ="btn-group">
                                    <input type="submit" value="등록"/>
                                </div>
                            </form>
                            <input id="mainBannerURL1" type="url" size="10" style="font-size:12pt; color:#000000;width:380px" value="<?php echo $arrBanner[0]['adsURL']?>"/>
                        </div>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        <div>
                            <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm2" style="margin-left: 20px;" value="<?php echo $arrBanner[1]['id'];?>">
                                <input id="mainBannerImg2" name = "mainBannerImg2" type="file" value="<?php echo $arrBanner[1]['adsImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                                <Label id="mainBannerImgLabel2"><?php echo $arrBanner[1]['adsImgURL'];?></Label>
                                </input>
                                <div class ="btn-group">
                                    <input type="submit" value="등록"/>
                                </div>
                            </form>
                            <input id="mainBannerURL2" type="url" size="10" style="font-size:12pt; color:#000000;width:380px" value="<?php echo $arrBanner[1]['adsURL']?>"/>
                        </div>
                        </td>
                    </tr>

                    <tr>
                        <td>
                         <div>
                            <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm3" style="margin-left: 20px;" value="<?php echo $arrBanner[2]['id'];?>">
                                <input id="mainBannerImg3" name = "mainBannerImg3" type="file" value="<?php echo $arrBanner[2]['adsImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                                <Label id="mainBannerImgLabel3"><?php echo $arrBanner[2]['adsImgURL'];?></Label>
                                </input>
                                <div class ="btn-group">
                                    <input type="submit" value="등록"/>
                                </div>
                            </form>
                            <input id="mainBannerURL3" type="url" size="10" style="font-size:12pt; color:#000000;width:380px" value="<?php echo $arrBanner[2]['adsURL']?>"/>
                        </div>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header"> 생활정보배너등록 (배너크기:720*140)</td>
                    </tr>

                    <tr>
                        <td>
                        <div>
                            <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm4" style="margin-left: 20px;" value="<?php echo $lifeBannerAds['id'];?>">
                                <input id="mainBannerImg4" name = "mainBannerImg4" type="file" value="<?php echo $lifeBannerAds['adsImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                                <Label id="mainBannerImgLabel4"><?php echo $lifeBannerAds['adsImgURL'];?></Label>
                                </input>
                                <div class ="btn-group">
                                    <input type="submit" value="등록"/>
                                </div>
                            </form>
                            <input id="mainBannerURL4" type="url" size="10" style="font-size:12pt; color:#000000;width:380px" value="<?php echo $lifeBannerAds['adsURL']?>"/>
                        </div>
                        </td>
                    </tr>

                    <tr>
                        <td class="base-table-header">캐스트배너등록  (배너크기:720*240)</td>
                    </tr>

                    <tr>
                        <td>
                            <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm5" style="margin-left: 20px;" value="<?php echo $arrCast[0]['id'];?>">
                                <input id="mainBannerImg5" name = "mainBannerImg5" type="file" value="<?php echo $arrCast[0]['castBannerImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                                <Label id="mainBannerImgLabel5"><?php echo $arrCast[0]['castBannerImgURL'];?></Label>
                                </input>
                                <div class ="btn-group">
                                    <input type="submit" value="등록"/>
                                </div>
                            </form>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm6" style="margin-left: 20px;"  value="<?php echo $arrCast[1]['id'];?>">
                                <input id="mainBannerImg6" name = "mainBannerImg6" type="file" value="<?php echo $arrCast[1]['castBannerImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                                <Label id="mainBannerImgLabel6"><?php echo $arrCast[1]['castBannerImgURL'];?></Label>
                                </input>
                                <div class ="btn-group">
                                    <input type="submit" value="등록"/>
                                </div>
                            </form>

                        </td>
                    </tr>

                    <tr>
                        <td>
                        <form method="post" enctype="multipart/form-data" id="mainBannerUploadForm7" style="margin-left: 20px;"  value="<?php echo $arrCast[2]['id'];?>">
                            <input id="mainBannerImg7" name = "mainBannerImg7" type="file" value="<?php echo $arrCast[2]['castBannerImgID'];?>" style="font-size:12pt; color:#000000;width:320px">
                            <Label id="mainBannerImgLabel7"><?php echo $arrCast[2]['castBannerImgURL'];?></Label>
                            </input>
                            <div class ="btn-group">
                                <input type="submit" value="등록"/>
                            </div>
                        </form>

                        </td>
                    </tr>

                    </tbody>
                    </table>

                <div id="submit_container">
                    <button onclick="onRegisterMainBanner()">수정 및 등록</button>
                </div>
            </div>
        </div>
        -->

        <div class="col-md-6" >
            <table class="table table-bordered table-scrollable">
                <colgroup>
                    <col width="45%">
                    <col width="45%">
                    <col width="10%">
                </colgroup>
                <thead>
                <tr>
                    <th scope="col" class="base-table-header">노출순서</th>
                    <th scope="col" class="base-table-header">이벤트넘버</th>
                    <th scope="col" class="base-table-header">상태</th>
                </tr>
                </thead>

                <tbody id="list">
                <?php
                $cnt = count($arrEvent);

                if($cnt == 0) {
                    echo '<tr class="__oldlist" style="">';
                    echo '<td colspan="10">없습니다.</td>';
                    echo '</tr>';
                }
                else {
                    $i = 1;
                    foreach ($arrEvent as $row) {
                        echo '<tr>';
                        echo '<td>' .( $i + ($currentPageNum - 1) * 20) . '</td>';
                        echo '<td>' . $row['id'] . '</td>';
                        echo '<td>' .' <input type="checkbox" id="checked" name="checked" value="'. $row['id'] .'"/></td>';
                        echo '</tr>';
                        $i++;
                    }
                }
                ?>
                </tbody>
            </table>

            <div class="cb-row">
                <button type="button" class="btn btn-default" onclick="onClickAdd()">추가</button>
                <button type="button" class="btn btn-default" onclick="onClickRemove()" style="margin-right: 5px;">삭제</button>
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
                        <input id="event_id" type="text" name="consequence" size="10" style="font-size:12pt; color:#000000; width: 100px;margin-top: 3px;">이벤트 넘버</input>
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

<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

<script>

    function onLoad() {
        var i = 1;
        while(i <= 7) {
            var formName = '#mainBannerUploadForm'+(i);
            var kind = 'app';
            if(i == 4) {
                kind = 'event';
            }
            else if(i >= 5) {
                kind = 'cast';
            }
            var url = getBaseURL()+'/admin/upload_image?kind='+kind+'&file_name=mainBannerImg'+(i)+'&index='+(i);
            $(formName).ajaxForm({
                    url :url,
                    dataType : 'json',
                    success : function (response) {
                        var code = response['result_code'];
                        if(code == 0) {
                            var ret_id = response['result_data']['index'];
                            var var_name = "mainBannerImg"+ret_id;
                            document.getElementById(var_name).setAttribute("value", response['result_data']['id']);
                            var input_name = '#mainBannerImgLabel'+(ret_id);
                            $(input_name).text(response['result_data']['imageURL']);
                        }
                        else {
                            alert("저장 실패!지정한 사용자가 유효한지 확인해주세요.");
                        }
                    }
                })
            ;
            i++;
        }
    }


    function searchKeyPress(e)
    {
        // look for window.event in case event isn't passed in
        e = e || window.event;
        if (e.keyCode == 13)
        {
            var currentPage = document.getElementById('currentPage').value;
            location.href = getBaseURL()+'/admin/manage_main_page?page_num='+currentPage;
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

        location.href = getBaseURL()+'/admin/manage_main_page?page_num='+currentPage;
    }

    function nextPage() {
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPage = parseInt(document.getElementById('totalPage').innerHTML);

        currentPage = currentPage + 1;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }

        location.href = getBaseURL()+'/admin/manage_main_page?page_num='+currentPage;
    }


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
            var event_id = document.getElementById('event_id').value;

            if(isNaN(consequenc) == true || isNaN(event_id) == true) {
                alert("수자를 입력해주세요.");
                return;
            }

            var consequence = parseInt(consequenc);
            var shop_id = parseInt(shop_id);
            $.ajax({
                url: getBaseURL()+"/admin/add_event_consequence",
                type: "post", // To protect sensitive data
                data: {
                    ajax: true,
                    id:event_id,
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

    function onClickRemove() {
        checkboxes = document.getElementsByName('checked');
        var n = checkboxes.length;
        for (var i = (n - 1); i >= 0; i--) {
            if(checkboxes[i].checked == true) {
                $.ajax({
                    url: "remove_event_consequence",
                    type: "post", // To protect sensitive data
                    data: {
                        ajax: true,
                        id:checkboxes[i].value,
                        table_cell_id:i
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
        }
    }

    function onRegisterMainBanner() {
        var bannerArray = new Array;
        var i = 1;
        while(i <= 7) {
            var bannerImgID = document.getElementById('mainBannerImg'+i).getAttribute("value");
            var bannerURL = $('#mainBannerURL'+i).val();
            var id = document.getElementById('mainBannerUploadForm'+i).getAttribute("value");

            if(isUndifined(id) == true) {
                id = null;
            }

            var banner = new Object;
            banner['adsImgID'] = bannerImgID;
            banner['adsURL'] = bannerURL;
            banner['id'] = id;

            bannerArray.push(banner);
            i++;
        }

        var strPoint = JSON.stringify(bannerArray);
        $.ajax({
            url: "set_main_banner",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                banner_array:strPoint
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    isCreateShop = true;
                    alert("저장되었습니다..");
                    //location.href ='admin';
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

</script>


</body>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/jquery.form.js"></script>

</html>
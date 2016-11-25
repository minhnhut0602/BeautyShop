<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>미미샵</title>
    <link href="<?php echo base_url(); ?>assets/css/admin/global.css" rel="stylesheet" type="text/css">
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

<body onunload="onExit()">

<div class="container top">
    <div class="input-group">
        <label>팁쓰기:   </label>
        <input id="tip" type="text" name="tip" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['0']?>"/>
    </div>

    <p/>

    <div class="input-group">
        <label>글쓰기:   </label>
        <input id="write" type="text" name="write" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['1']?>"/>
    </div>

    <p/>

    <div class="input-group">
        <label>평점쓰기:   </label>
        <input id="remark" type="text" name="remark" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['2']?>"/>
    </div>

    <p/>

    <div class="input-group">
        <label>문의하기:   </label>
        <input id="question" type="text" name="question" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['3']?>"/>
    </div>

    <p/>

    <div class="input-group">
        <label>댓글쓰기:   </label>
        <input id="comment" type="text" name="comment" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['4']?>"/>
    </div>

    <p/>

    <div class="input-group">
        <label>좋아요:   </label>
        <input id="like" type="text" name="like" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['5']?>"/>
    </div>

    <p/>

    <div class="input-group">
        <label>SNS공유:   </label>
        <input id="sns" type="text" name="sns" size="10" style="font-size:12pt; color:#000000; width: 300px" value="<?php echo $point['6']?>"/>
    </div>

    <p/>
    <p/>

    <button type="button" class="btn btn-default" onclick="saveInfo()" id="btn_transfer" style="width: auto;"/> <span aria-hidden="true">저장</span></button>

</div>

<script>
    function saveInfo() {
        var pointArray = new Array;
        var tip = $("#tip").val();
        var write = $("#write").val();
        var remark = $("#remark").val();
        var question = $("#question").val();
        var comment = $("#comment").val();
        var like = $("#like").val();
        var sns = $("#sns").val();

        if(isNaN(tip) == true || isNaN(write) == true || isNaN(remark) == true
            || isNaN(question) == true || isNaN(comment) == true || isNaN(like) == true || isNaN(sns) == true) {
            alert("수값을 입력해주세요.");
            return;
        }

        pointArray.push(tip);
        pointArray.push(write);
        pointArray.push(remark);
        pointArray.push(question);
        pointArray.push(comment);
        pointArray.push(like);
        pointArray.push(sns);

        var strPoint = JSON.stringify(pointArray);
        $.ajax({
            url: "set_point",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                point_array:strPoint
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    isCreateShop = true;
                    alert("저장되었습니다..");
                    location.href ='admin';
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

</html>
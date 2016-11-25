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
<div  style="min-width: 300px;max-width: 640px;">		
<section >
    <table class="table table-bordered" style="margin: 5px;">
        <tbody>
        <tr>
    		<?php
				if($type == 0) echo '<td class="base-table-header">개인정보약관</td>';
				else if($type == 1) echo '<td class="base-table-header">미미샵이용약관</td>';
				else if($type == 2) echo '<td class="base-table-header">위치기반이용약관</td>';
			?>
        </tr>
        <tr>
            <td id="modifytime"  align="right"> 
            	<?php 
            		echo '최종수정일:'.$modify_time;
            	?> 
            </td>
        </tr>		
        <tr>
			<td><textarea class="form-control" id="content"  rows="5"  style="font-size:10pt; color:#000000; height: 200px"/><?php echo $content;?></textarea></td>
        </tr>
        </tbody>
    </table>
</section>
<menu>
    <div style="text-align: center;">
        <button id="agreement_ok" type="button" onclick="saveInfo(<?php echo $type?>)">등록</button>
    </div>
</menu>		
</div>		
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

<script>
	function onLoad() {
		
	}
	
	function saveInfo(tp) {
		
		var content = document.getElementById("content").value;
		$.ajax({
            url: "register_agreement",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                type:tp,
                content:content
            },
            success:function(response){
               location.href = getBaseURL()+'/admin/agreement?type='+tp;
               alert("약관이 등록되었습니다.");               
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
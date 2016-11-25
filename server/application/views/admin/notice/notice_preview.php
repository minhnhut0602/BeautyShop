<html><head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=0, user-scalable=no, target-densitydpi=medium-dpi">
	<title>미미샵 공지사항</title>
    <link href="<?php echo base_url(); ?>assets/css/admin/jquery-ui.min.css" rel="stylesheet" type="text/css">
    <link href="<?php echo base_url(); ?>assets/css/admin/style-hib.css" rel="stylesheet" type="text/css">
    <script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
    <script src="<?php echo base_url(); ?>assets/js/prototype.js"></script>
    <script src="<?php echo base_url(); ?>assets/js/template.js"></script>
    <script src="<?php echo base_url(); ?>assets/js/common.js"></script>
<body>

<div class="notice-list">
    <?php
        $cnt = count($arrNotice);
        foreach ($arrNotice as $row) {
            $d = new DateTime($row['noticeTime']);
            $timestamp = $d->getTimestamp(); // Unix timestamp
            $formatted_date = $d->format('Y-m-d'); // 2003-10-16
            echo '<div class="notice-header" value="'.$row['id'].'">';
            echo '<span class="subject"></span>';
            echo '<span class="title"><font size="4" color="#444444">'.$row['noticeTitle'].'</font> <br/><font size="3" color="#c1c1c1">'.$formatted_date.'</font></span>';
            echo '<span class="plus"></span>';
            echo '</div>';

            echo '<div class="notice-contents">';
            echo '<span class="content"><font size="3">'.$row['noticeContent'].'</font></span>';
            echo '<br/><br/>';
            echo '<span class="reporter"><font size="3" color="#ff4973">미미샵</font></span>';
            echo '</div>';
        }
    ?>
</div>

<script type="text/javascript">
	var data = {
		page : 0,
		endFl : 'N'
	};
	
	$(document).ready(function(){	
		$(document).scroll(function(){
			if($(window).scrollTop() == $(document).height() - $(window).height() ){
				data.page = data.page + 1;
				page();
			}
		});
				
		if($(document).height() <= $(window).height()){
			data.page = data.page + 1;
			page();
		}
	});
	
	function page(){
		
	}
	
	$("div.notice-header").on("click", function(){
		$(this).find("span.plus").toggleClass("minus");
		$(this).next("div.notice-contents").slideToggle(0);

        clickNotice(this.getAttribute("value"));
	});

    function clickNotice(id) {
        $.ajax({
            url: "click_notice",
            type: "post", // To protect sensitive data
            data: {
                ajax: true,
                id:id
            },
            success: function (response) {

                if(response['result_code'] == 0) {

                }
                else {
                    alert("조회수 확인 실패!");
                }
            },
            error: function (jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }
</script>
<script type="text/html" id="template">
	{for item in list}
		<div class="notice-header">
			<span class="subject">${item.subjectNm}</span>
			<span class="title">${item.title}</span>
			<span class="plus"></span>
		</div>
		<div class="notice-contents">${item.content}</div>
	{/for}
</script>
</body>
</html>
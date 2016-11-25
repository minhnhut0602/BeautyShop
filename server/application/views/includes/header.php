<!DOCTYPE html> 
<html lang="en-US">
<head>
  <title>미끌 어드민</title>
  <meta charset="utf-8">
    <link href="<?php echo base_url(); ?>assets/css/admin/global.css" rel="stylesheet" type="text/css">
    <link href="<?php echo base_url(); ?>assets/css/bootstrap/bootstrap.css" rel="stylesheet" type="text/css">
</head>

<style>
    .navbar .dropdown-menu {
        margin-top: 0px;
    }

    ul.nav li.dropdown:hover ul.dropdown-menu{
        display: block;
    }
</style>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/jquery.form.js"></script>

<script>
    $(function() {
        var tab_id = document.getElementById("header-tabs" ).getAttribute("value");
        switch (tab_id) {
            case "0":
                $("#tab_member").toggleClass("active");
                break;
            case "1":
                $("#tab_shop").toggleClass("active");
                break;
            case "2":
                $("#tab_cast").toggleClass("active");
                break;
            case "3":
                $("#tab_talk").toggleClass("active");
                break;
            case "4":
                $("#tab_life").toggleClass("active");
                break;
            case "5":
                $("#tab_push").toggleClass("active");
                break;
            case "6":
                $("#tab_notice").toggleClass("active");
                break;
            case "7":
                $("#tab_shop_consequence").toggleClass("active");
                break;
            case "8":
                $("#tab_navor").toggleClass("active");
                break;
            case "9":
                $("#tab_monitor").toggleClass("active");
                break;
            case "10":
                $("#tab_banner").toggleClass("active");
                break;
            case "11":
                $("#tab_logout").toggleClass("active");
                break;
            default:
                break;
        }
    });
</script>

<body>
      <div class="navbar navbar-default" style="background: aliceblue;">
        <div class="container"  style="margin: 0 auto; width:100%; text-align: center;">
            <a class="navbar-brand" href="<?php echo base_url(); ?>admin" style="width:100%;text-align: center;"><b>미미샵</b></a>
        </div>

        <div class="container">
          <ul class="nav nav-pills" id="header-tabs" value="<?php echo $tab_id;?>">
              <li class="dropdown" id="tab_member">
                <a href="<?php echo base_url(); ?>member">회원관리</a>
                  <ul class="dropdown-menu">
                      <li><a href="<?php echo base_url(); ?>member/shop_member_list">상점회원</a></li>
                      <li><a href="<?php echo base_url(); ?>push/envelop_list">보낸쪽지리스트</a></li>
                  </ul>
              </li>
              <li class="dropdown" id="tab_shop">
                  <a href="<?php echo base_url(); ?>shop">
                      점포관리
                  </a>
                  <ul class="dropdown-menu">
                      <li><a href="<?php echo base_url(); ?>shop/request_shop_list">신청중</a></li>
                  </ul>
              </li>
              <li role="presentation" id="tab_cast">
                  <a href="<?php echo base_url(); ?>cast">캐스트</a>
              </li>
              <li class="dropdown" role="presentation" id="tab_talk">
                  <a href="<?php echo base_url(); ?>freetalk">토크관리</a>
                  <ul class="dropdown-menu">
                      <li><a href="<?php echo base_url(); ?>freetalk/manage_freetalk">게시글관리</a></li>
                      <li><a href="<?php echo base_url(); ?>freetalk/manage_comment">댓글관리</a></li>
                  </ul>
              </li>
              <li role="presentation" id="tab_life">
                  <a href="<?php echo base_url(); ?>lifeinfo">생활정보</a>
              </li>
              <!--<li role="presentation">
                  <a href="<?php echo base_url(); ?>index.php/register_shopgroup">점포대량등록</a>
              </li> -->
              <li role="presentation" id="tab_push">
                  <a href="<?php echo base_url(); ?>push">푸시보내기</a>
              </li>
              <li role="presentation" id="tab_notice">
                  <a href="<?php echo base_url(); ?>notice">공지사항</a>
              </li>
              <li role="presentation" id="tab_shop_consequence">
                  <a href="<?php echo base_url(); ?>shop/shop_consequence">상점노출순서</a>
              </li>
              <li role="presentation" id="tab_navor">
                  <a href="<?php echo base_url(); ?>naver_shop">네어버상점얻기</a>
              </li>
              <li  class="dropdown"  id="tab_monitor">
                  <a>모니터링</a>
                  <ul class="dropdown-menu">
                      <li><a href="<?php echo base_url(); ?>shop/use_remark">이용후기</a></li>
                      <li><a href="<?php echo base_url(); ?>image">이미지</a></li>
                  </ul>
              </li>
              <li role="presentation" id="tab_banner">
                  <a href="<?php echo base_url(); ?>admin/manage_banner">배너관리</a>
              </li>
              <li role="presentation" id="tab_logout">
                  <a href="<?php echo base_url(); ?>admin?log_out=1">로그아웃</a>
              </li>
          </ul>
        </div>
      </div>
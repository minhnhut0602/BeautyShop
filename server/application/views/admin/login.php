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

<div class="container">

    <table width="509" height="364" align="center" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
        <tbody><tr>
            <td align="CENTER" valign="middle">
                <form name="login" method="post" action="set_session.php"><input type="hidden" name="PHPSESSID" value="ccd2d1164b97e71706160702275717d9">
                    <input type="hidden" name="mode" value="login">
                    <table width="509" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                        <tbody><tr>
                            <td bgcolor="#FFFFFF" align="center" valign="middle">
                                <table width="509" border="0" cellspacing="0" cellpadding="0">
                                    <tbody>

                                    <tr>
                                        <td height="20"></td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <table width="509" border="0" cellspacing="0" cellpadding="0">
                                                <tbody><tr>
                                                    <td width="150"><strong><font color="125191" size="3">관리자 로그인</font></strong></td>
                                                </tr>
                                                </tbody></table>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                        <td align="left" valign="top">
                                            <table width="509" border="0" cellspacing="0" cellpadding="0"  background="<?php echo base_url(); ?>assets/img/admin/login_bg.png">
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <table width="509">
                                                            <tbody>
                                                            <tr height="60"/>
                                                            </tr>
                                                            <tr>
                                                                <td width="123" height="73"><img src="<?php echo base_url(); ?>assets/img/admin/login_key.jpg" style="margin-left: 130px;"></td>
                                                                <td><strong><font color="#000000" size="3"  style="margin-left: 10px;">관리자만 접속이 가능합니다.</font></strong></td>
                                                            </tr>
                                                            </tbody></table>
                                                    </td>
                                                </tr>
                                                <tr height="30"/>
                                                </tr>
                                                <tr>
                                                    <td height="98" align="center">
                                                        <table width="335" border="0" cellspacing="0" cellpadding="0" style="border-style: solid;border-color: #ff0000 #0000ff;">
                                                            <tbody>
                                                            <tr>
                                                                <td width="225" height="54">
                                                                    <table width="215" border="0" cellspacing="0" cellpadding="0">
                                                                        <tbody>
                                                                        <tr>
                                                                            <td width="120" height="22" style="text-align:left"><label>아이디</label></td>
                                                                            <td><strong><input name="i_d" id="i_d" style="width:120px; ime-mode:disabled"></strong></td>
                                                                        </tr>
                                                                        <tr >
                                                                            <td height="22" style="text-align:left;" ><label style="margin-top: 10px;">패스워드</label></td>
                                                                            <td><strong><input type="password" style="width:120px; margin-top: 10px;" name="p_w" id="p_w" onkeydown="if(event.keyCode==13) {loginAdmin();}"></strong></td>
                                                                        </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </td>
                                                                <td width="67">
                                                                    <img src="<?php echo base_url(); ?>assets/img/admin/login_btn.png" width="67" height="45" onclick="loginAdmin()" style="cursor: pointer;"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td height="39" align="center">
                                                                    <input type="checkbox" id="save_login" name="save_login" style="margin-top:10px;margin-left: 10px;"> 아이디저장 </input>
                                                                </td>
                                                            </tr>

                                                            <tr>
                                                                <td height="100">
                                                                    <img src="<?php echo base_url(); ?>assets/img/admin/login_back.png" width="72" height="24" onclick="backAmin()" style="cursor: pointer;margin-left: 120px;" />
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    </tbody></table>
                            </td>
                        </tr>
                        </tbody></table>
                </form>
                <br><br>

            </td>
        </tr>
        </tbody>
    </table>

</div>

<script>

    function loginAdmin() {
        var id = $('#i_d').val();
        var pw = $('#p_w').val();
        var save_login = $('#save_login').is(':checked');

        if(save_login == false) {
            save_login = 0;
        }
        else {
            save_login = 1;
        }

        $.ajax({
            url: "admin/login_admin",
            type: "post", // To protect sensitive data
            data: {
                ajax:true,
                id:id,
                password:pw,
                save_info:save_login
            },
            success:function(response){
                var code = response['result_code'];

                if(code == 0) {
                    location.href = 'admin';
                }
                else {
                    alert("관리자 아이디와 패스워드가 일치하지 않습니다.");
                }
            },
            error:function(jqXHR, msg, erro) {
                alert("전송 실패! 망연결을 확인해주세요.");
            }
        });
    }

    function backAmin() {
        $('#i_d').val('');
        $('#p_w').val('');
    }

</script>


</body>

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.1.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
<script src="<?php echo base_url(); ?>assets/js/admin.min.js"></script>

</html>
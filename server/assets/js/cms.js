var admin = {
		logout : function(){
			common.callAjax("/logout.cms", 
					{},
					function(res){
						if(res.code == "000"){
							alert("로그아웃되었습니다.");
							location.href="/login.cms";
						}else{
							common.showMessage(res.msg, 200, 50);
						}
					}
				);
		}
};
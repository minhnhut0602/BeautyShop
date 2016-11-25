
var common = {
		showMessage : function(msg, width, height){
			$("#msgbox").width(width + "px");
			$("#msgbox").height(height + "px");
			$("#msgbox").html(msg);
			$("#msgbox").show();
		},
		callAjax : function(callUrl, callData, callback){
			$.ajax({
				url : callUrl,
				type : "POST",
				dataType : "json",
				data :  callData,
				success : function(res){
					callback.call(this, res);
				}
			});
		}
};


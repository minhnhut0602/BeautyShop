

<script src="<?php echo base_url(); ?>assets/js/jquery-1.11.2.js"></script>

<!-- 
<script type="text/javascript" src="http://openapi.map.naver.com/openapi/naverMap.naver?ver=2.0&key=5479cbd871f482ad0871b0aec43dbea8"></script>
 -->

<div class="container" xmlns="http://www.w3.org/1999/html" id="root">
    <div class="search_shop">
	    <fieldset class="srch">
	            <legend>검색영역</legend>
	            <br/>
	            <label for="html">키워드파일</label>
	            <input type="file" id="btn_file" name="search" alt="파일" value="파일" />
	            <br/>
	            <br/>
	            <input type="button" id="btn_search" name="search" alt="상점자료 긁어오기" value="상점자료 긁어오기" onclick="search()"/>
	            <span class="tab"></span>
	            <input type="button" id="btn_location" name="search" alt="지역자료 긁어오기" value="지역자료 긁어오기" onclick="searchLocation()"/>
	            <span class="tab"></span>
	            <label for="status" id="status">상태: </label>
	            <span class="tab"></span>
	            <label for="html"><input type="checkbox" id="cb_all_selection" name="selection" checked onclick='checkAll(this);'/>전체선택/해제</label>
	    </fieldset>
    </div>
    
    <br/>
    <br/>
    
    <table cellspacing="0" border="1" summary="검색 API 결과" class="tbl_type">
        <caption>검색  결과</caption>
                <colgroup>
                        <col width="10%">
                        <col width="10%">
                        <col width="60%">
                        <col width="10%">
                        <col width="10%">
                </colgroup>
                <thead>
                        <tr>
                        <th scope="col">번호</th>
                        <th scope="col">개수</th>
                        <th scope="col">분류</th>
                        <th scope="col">페지</th>
                        <th scope="col">선택</th>
                        </tr>
                </thead>
                
                <tbody id="list">
                        <tr class="__oldlist" style="">
                                <td colspan="9">검색 결과가 없습니다.</td>
                        </tr>
                        <tr class="__template" style="display: none">
                                <td>#no#</td>
                                <td>#length#</td>
                                <td>#query#</td>
                                <td>#page#</td>
                                <td><input type="checkbox" id="checked" name="checked" checked/> </td>
                        </tr>
                </tbody>
    </table>
</div>

<script type="text/javascript">	
	
//	var arrShopKind = ["네일샵" , "미용실", "속눈썹연장샵", "왁싱샵", "피부샵", "마사지샵"];
//	var arrShopKind = ["속눈썹연장샵", "왁싱샵", "피부샵", "마사지샵"];
    var arrShopKind = ["반영구화장", "타투"];
	var nStartPageNumber = 1;
	var nPageItemCnt = 10;
	var nCurrentPercent = 0;
	var nTotalSearchCnt = 0;
	var nCurrentQuery = "";
	var itemIdx = 0;
	var finishWrite = false;
	var isDebug = true;
	var errText = "";
	
	// s20089001 -> 20089001
	function convShopID(id) {
		var number = id.substring(1);
		return number;
	}
	
	function sleep(milliseconds) {
		  var start = new Date().getTime();
		  for (var i = 0; i < 1e7; i++) {
		    if ((new Date().getTime() - start) > milliseconds){
		      break;
		    }
		  }
		}
	
	function searchLocation() {

        //if(true) {
        //    alert("정검중입니다. 운영자에게 문의해주십시오.");
        //    return;
        //}

		 var strFilePath = $('#btn_file').val();
		 var extension = strFilePath.substr(strFilePath.lastIndexOf('.') + 1).toLowerCase();
		 var allowedExtensions = ['txt'];
		  
		 if (strFilePath.length > 0)
		 {
		     if (allowedExtensions.indexOf(extension) == -1) 
		     {
              	 alert('파일형식이 틀립니다. 오직 ' + allowedExtensions.join(', ') + '만  가능합니다.');
               	 return false;
             }
		 }
		 else {
			 alert('파일을 입력하세요.');
			 return false;
		 }
		 
		 var fileInput = document.getElementById('btn_file');
		 
		 if (!fileInput) {
		    return false;
		 }
		 var file = fileInput.files[0];
		 var reader = new FileReader();

		 reader.onload = function(e) {
			var contents = reader.result;
			var res  = contents.split("\n");
			
			for (a in res ) {
				getLocationInfo(res[a]);
			}
		 }
		 
		 reader.readAsText(file);
	}
		

	function search() {
        //if(true) {
        //    alert("정검중입니다. 운영자에게 문의해주십시오.");
        //    return;
        //}

        var strFilePath = $('#btn_file').val();
		 var extension = strFilePath.substr(strFilePath.lastIndexOf('.') + 1).toLowerCase();
		 var allowedExtensions = ['txt'];
		  
		 if (strFilePath.length > 0)
		 {
		     if (allowedExtensions.indexOf(extension) == -1) 
		     {
              	 alert('파일형식이 틀립니다. 오직 ' + allowedExtensions.join(', ') + '만  가능합니다.');
               	 return false;
             }
		 }
		 else {
			 alert('파일을 입력하세요.');
			 return false;
		 }
		 
		 var fileInput = document.getElementById('btn_file');
		 
		 if (!fileInput) {
		    return false;
		 }
		 var file = fileInput.files[0];
		 var reader = new FileReader();

		 reader.onload = function(e) {
			var contents = reader.result;
			var res  = contents.split("\n");
			    
			arrResultItems = [];
			nCurrentPercent = 0;
			nTotalSearchCnt = res.length * arrShopKind.length;
			finishWrite = false;
			updateStatus();
			itemIdx = 0;

			alert("시작! 1시간정도 기다리세요! 5만여개의 헤어샵,네일샵,속눈썹연장샵,왁싱샵,피부샵,마사지샵 들을 업데이트 해드립니다.");
			$('.__oldlist').remove();
			
		 	for (a in res ) {
		 		var category_id = 0;
		 		for(b in arrShopKind) {
		  			
		 			if(finishWrite == true) {
		 				break;
		 			}
		 			
		 			getShopInfoWithKindAndLocation(res[a] + " " + arrShopKind[b], nStartPageNumber, arrShopKind[b]);
		 			category_id++;
		 		}
		    }
		 }
		 
		 reader.readAsText(file);
	}
	
	function getLocationInfo(query) {
		$.ajax({	
			 url:'naver_shop/writeLocationList',
	            dataType:'json',
	            type:'POST',
	            data:{'query':query},
	            success:function(result) {
                	itemIdx++;
	            	$html = template('__template', result['result']);
	                $html.addClass('__oldlist');
	                $('#list').append($html);
	                $('.__oldlist').show();
	                
	                updateStatus();
	                if(nCurrentPercent == 100) {
	                	alert("완료!");
	                }
	            },
	            error:function(jqXHR, msg, erro) {
	                
	                if(isDebug == true) {
	                	errText += jqXHR['responseText'];
	                	updateStatus();
	                }
	                else {
	                	finishWrite = true;
	            		nCurrentPercent = -1;   
	                	updateStatus();
	                	alert("이상!"); 
	                }
	            }   
 	  });
 	  
 	  sleep(3000); // delay
	}
	
	function getShopInfoWithKindAndLocation(query, pageNum, category) {
				
				nCurrentQuery = query;
				
				$.ajax({	
					 url:'naver_shop/writeShopList',
			            dataType:'json',
			            type:'POST',
			            data:{'query':query, 'page_num':pageNum, 'category':category},
			            success:function(result) {
                            if(result['result']['length'] >= nPageItemCnt) {
                            		var pagenum = result['result']['page'];
                            		pagenum = pagenum + 1;
                            		var strCategory = result['result']['category'];
                            		var strQuery = result['result']['query'];
                            		getShopInfoWithKindAndLocation(strQuery, pagenum, strCategory);
                         	}
                         	else {
                         		
									nCurrentPercent += (1.0/nTotalSearchCnt) * 100;
									
									if(nCurrentPercent >= 100) {
										nCurrentPercent = 100;
									}
							}
		                         	
                         	itemIdx++;
			            	$html = template('__template', result['result']);
			                $html.addClass('__oldlist');
			                
			                $('#list').append($html);
			                $('.__oldlist').show();
			                
			                updateStatus();
			                if(nCurrentPercent == 100) {
			                	alert("완료!");
			                }
			            },
			            error:function(jqXHR, msg, erro) {
			            	if(isDebug == true) {
			                	//errText += jqXHR['responseText'];
			                	errText += JSON.stringify(jqXHR) + ' ' + msg +'  '+erro; 
			                	updateStatus();
			                }
			                else {
			                	finishWrite = true;
			            		nCurrentPercent = -1;   
			                	updateStatus();
			                	alert("이상!"); 
			                }
			            },
			            httpData: function( xhr, type, s ) {
			            	jQuery.error( xhr.getResponseHeader("content-type") );
						}
		  	  });
		  	  
		  	  sleep(3000); // delay
	}
	
	function template(template_id, params){
	        var c = $('.' + template_id).clone();
	        var html = $('<div>').append(c).html();
	        for(var key in params){
	            html = html.replace(new RegExp('#' + key + '#', 'g'), params[key]);
	        }
	        html = html.replace(new RegExp('#no#', 'g'), itemIdx);
	        
	        html = html.replace(/#template_([^#]*)#/g, '$1');
	        var $html = $(html).removeClass(template_id).removeClass('__template');
	        return $html;
	}
	
	function checkAll(cb) {
		checkboxes = document.getElementsByName('checked');
        for (var i = 0, n = checkboxes.length; i < n; i++) {
            checkboxes[i].checked = cb.checked;
        }
	}
	
	function updateStatus() {
		
		if(isDebug == true) {
			if(errText != "") {
				$("#status").html(errText);
			}
		}
		else {
			if(nCurrentPercent == 100) {
				$("#status").html("상태: 완료되었습니다.");
			}
			else if (nCurrentPercent == -1) {
				$("#status").html("상태: 실패되었습니다..");
			}
			else {
				$("#status").html("상태: 현재 " + nCurrentPercent+ "% 진행중입니다.");
			}
		}
	}
	
</script>

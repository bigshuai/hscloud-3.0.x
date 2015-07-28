$(document).ready(function() {
	var currentLang="zh_CN";
	if (getCookie("lang") != null) {
		currentLang = getCookie("lang");
	}
	initCss('col/footer.css');
	$("#footer").css("background","url(../../../skin/"+customerPath+"/images/col/footer_bg.png)");
	/*if($("#content").height()+127+50>document.body.clientHeight){
		$("#footer").css("position","relative");
	}else{
		$("#footer").css("position","absolute");
	}*/
	var copyRight={
			data:'',
			context:new Array(),
			load:function(domainCode){
				$.ajax({
					url : rootPath+'/user_mgmt/user!readCopyright.action?domainCode=' +domainCode,
					type: 'GET',
					dataType: 'json',
					contentType: "application/json; charset=utf-8",
					success: function(data){
						if(data.success == true) {
							copyRight.data=data.resultObject;
							copyRight.context.push(data.resultObject['zh_CN']);
							copyRight.context.push(data.resultObject['en_US']);
							copyRight.renderPage(currentLang);
							setCookie("zh_CN",data.resultObject['zh_CN']);
							setCookie("en_US",data.resultObject['en_US']);
						} else {
							alert(data.resultMsg);
						}
					}
				});
			},
			renderPage:function(currentLang){
				var context = copyRight.data[currentLang];
				if(currentLang=='zh_CN'){
					context = copyRight.context[0];
				}else{
					context = copyRight.context[1];
				}
				$('#footer').html(context);
			},
			renderPageByCookie:function(currentLang){
				var context = "";
				if(currentLang=='zh_CN'){
					context = getCookie("zh_CN");
				}else{
					context = getCookie("en_US");
				}
				$('#footer').html(context);
			}
		};
	if(getCookie("zh_CN")==null || getCookie("en_US")==null){
		copyRight.load(domainCode);
	}else{
		copyRight.renderPageByCookie(currentLang);
	}
//	$("#footer").html("footer");
});
var brandCode="";
$(document).ready(function() {
	initShortcut();
	initCss('col/header.css');
	$("#logoLoginDIV").css("background","url("+rootPath+"/skin/"+customerPath+"/images/col/header_bg.png)");
//	$("#menuDIV").css("background","url("+rootPath+"/skin/"+customerPath+"/images/col/menu_bg.jpg)");
	
	//加载head的logo一行DIV
	var logoLoginHTML = "<div style='margin: 0 auto;width: 940px;height:87px;'><div id='logoDIV'>" +
			"<img src='"+rootPath+"/resources/platform/"+domainCode+"/images/logo_main.png' style='margin-left: 0;'/>" +
			"</div>" +
			"<div id='loginDIV' style='margin-right: 0;width: auto;'>" +
			"<img onclick='userCenter()' src='"+rootPath+"/skin/"+customerPath+"/images/col/head_pic.png' />"+
			"<span onclick='userCenter()' id='user_span' style='margin-left: 5px;'></span>　"+	
//			"<img src='"+rootPath+"/skin/"+customerPath+"/images/col/tag_2.png' />"+
			"<span id='logout_span'><img src='"+rootPath+"/skin/"+customerPath+"/images/col/power.png'/>"+
			"<span>退出</span></span>"+
			"<img src='"+rootPath+"/skin/"+customerPath+"/images/col/parting_line.png' />"+
//			"<img src='"+rootPath+"/skin/"+customerPath+"/images/col/tag.png' />"+			
			"<span onclick='zh()'>简体中文</span>　<span onclick='en()'>English</span>" +
			"</div></div>";
	$("#logoLoginDIV").html(logoLoginHTML);
	$("#logout_span span").text(i18n.get("logout"));
	$("#logout_span").bind('click',function(){
		$.ajax({
			url : rootPath+"/user_mgmt/user!logout.action",
			type: 'POST',
			success: function(data){
				if(data.success == true) {
					window.location.href = rootPath+"/index.html";
//					window.location.reload();
				}
			}
		});
//		window.location.href=rootPath+"/index.html";
	});	
	var userInfo={
			name:'',
			data:null,
			load:function(){
				$.ajax({
					url : rootPath+'/user_mgmt/user!getSessionUser.action',
					type: 'GET',
					dataType: 'json',
					contentType: "application/json; charset=utf-8",
					success: function(data){
						if(data.success == true) {
							userInfo.data=data.resultObject;
							userInfo.name=data.resultObject.name;
							brandCode =data.resultObject.level;
							userInfo.renderPage(userInfo.name);
						} else {
//							alert(data.resultMsg);
							window.location.href=rootPath+"/index.html";
						}
					}
				});
			},
			renderPage:function(name){
				$("#user_span").text(name);
			}
	};
	userInfo.load();
});

//进入进入用户中心
function userCenter() {
	window.location = rootPath+"/function/user/info/index.html?LIIndex=1";
}
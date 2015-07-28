function  tran(){
	//按钮
	var login_btn = $("#login_btn");
	login_btn.click(function() {
		login();
	});
	
	$(document).keydown(function(event){
		if(event.keyCode==13){
			login();
		}
	});
	
	//看不清换一个验证码。
	$("#imageCode").click(function(){
		var time = Math.random();
		var url = "../../user_mgmt/webSite!getImageCode.action"+"?time="+time;
	    $(this).attr("src", url);
	});
}

/*
 * 验证码输入框获取焦点时，更新验证码
 */
function getImageCode(){
	var time = Math.random();
	var url = "../../user_mgmt/webSite!getImageCode.action"+"?time="+time;
    $("#imageCode").attr("src", url);
}

function gotoInit(){
	gotoRunning();
}
function gotoRunning(){
	document.title = lang.user.login.title;
	tran();
}

$("document").ready(gotoInit);

function login(){
	var loginName = $('#name').val();
	var loginPass = $('#pass').val();
	var enCode = $.md5(loginPass);
	var validCode = $("#validCode").val();
	if(""== loginName || null==loginName || "用户名"==loginName || ""== loginPass || null==loginPass || "密码"==loginPass){
		customizeAlert("用户名或密码不能为空！");
		return ;	
	}
	if(null == validCode || "" == validCode){
		customizeAlert("请输入验证码！");
		return ;	
	}
	var jsonRequest = "{\"email\":\""+loginName+"\",\"password\":\""+enCode+"\",\"domain\":{\"id\":"+config.platformid+"}}";
	ajaxPost("head.default", "user.userLoginByEmail", jsonRequest,"code="+validCode,function(data) {
		if(data['success']){
			if(data["resultObject"] != null){
				window.location = data["resultObject"];
			}else{
				window.location.href = "../../VPDC/center/index.html";
			}
		}else{
			customizeAlert(data['resultMsg']);
		}
	});
}
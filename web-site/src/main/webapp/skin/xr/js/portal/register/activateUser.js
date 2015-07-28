
function getParam() {  
	var url = location.search; //获取url中"?"符后的字串  
	url = decodeURI(url);
	if(url.indexOf("?") != -1)  
	{  
	  var str = url.substr(1);  
	  var array = str.split("=")[1].split('?_?');
	  return array;
	}  
}

/**
 * 初始化注册成功页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('portal/col/header.js');
	initJs('portal/col/footer.js');
	initCss('portal/register/registerSuccess.css');
	i18n.init();//国际化初始化
	
	var paramArray = getParam();
	if(paramArray[0]=='1') {
		$('#registerTitle').text(i18n.get("activate_success"));
		var message = i18n.get("activate_success_message");
		message = message.replace('$name$', paramArray[1]);
		$('#registerSuccess').text(message);
		
		$("#loginEmail").hide();
		$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/register/registerSuccess.png");
	} else if(paramArray[0]=='2'){
		$('#registerTitle').text(i18n.get("activate_success"));
		var message = i18n.get("activate_repeat_message");
		message = message.replace('$name$', paramArray[1]);
		$('#registerSuccess').text(message);
		
		$('.get_passwd_button').text(i18n.get('active_login'));
		$('.get_passwd_button').click(function(){
			window.parent.location.href="login.html";
		});
		
		$("#loginEmail").hide();
		$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/register/registerSuccess.png");
	} else {
		$('#registerTitle').text(i18n.get("activate_fail"));
		var message = i18n.get("activate_fail_message");
		message = message.replace('$name$', paramArray[1]);
		$('#registerSuccess').text(message);
		
	//	gotoEmail(paramArray[2]);
		
		$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/register/error.png");
		loginEmail(paramArray[2]);
		$("#loginEmail").text(i18n.get("loginEmail"));	
	}
	
	
//	i18nPage();//国际化当前页面
	$("#registerDiv").css("background","url("+rootPath+"/skin/"+customerPath+"/images/portal/register/table.png"+") repeat-x scroll 0 0 #FFFFFF");
//	$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/register/registerSuccess.png");
	
});

function loginEmail(registerEmail) {

	var emailSiteList = [ "163.com", "126.com", "qq.com", "yeah.net",
			"136.com", "gmail.com", "yahoo.cn", "yahoo.com.cn", "foxmail.com",
			"sohu.com", "sina.com", "eyou.com", "wo.com.cn", "tom.com",
			"hotmail.com", "21cn.com", "189.cn", "sogou.com", "188.com",
			"139.com" ];
	var strArr = registerEmail.split('@');
	var containFlag = false;
	for ( var i = 0; i < emailSiteList.length; i++) {
		if (emailSiteList[i] == strArr[1]) {
			containFlag = true;
			break;
		}
	}
	var emailSite = '';
	if (containFlag) {
		if (strArr[1] == 'gmail.com' || strArr[1] == 'eyou.com'
				|| strArr[1] == 'hotmail.com') {
			emailSite = "www." + strArr[1];
		} else {
			emailSite = "mail." + strArr[1];
		}
		emailSite = "http://" + emailSite;
	} else {
		$("#loginEmail").hide();
		return;
	}
	
	$('#loginEmail').click(function(){
		window.location.href = emailSite;
	});
	
	return;
}
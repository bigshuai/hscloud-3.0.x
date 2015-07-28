/**
 * 初始化注册成功页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('portal/col/header.js');
	initJs('portal/col/footer.js');
	initCss('portal/register/registerSuccess.css');
	i18n.init();//国际化初始化
	i18nPage();//国际化当前页面
	$("#registerDiv").css("background","url("+rootPath+"/skin/"+customerPath+"/images/portal/register/table.png"+") repeat-x scroll 0 0 #FFFFFF");
	$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/register/registerSuccess.png");
	loginEmail();
});

function i18nPage() {
	$("#registerTitle").text(i18n.get("registerTitle"));
	$("#registerSuccess").text(i18n.get("registerSuccess"));
	$("#emailPrompt").text(i18n.get("emailPrompt"));
	$("#loginEmail").text(i18n.get("loginEmail"));	
}

function loginEmail() {
	var href = window.location.href;
	var registerEmail = href.split("?");
	if (registerEmail.length > 1) {
		registerEmail = registerEmail[1].split("=")[1];
	} else {
		registerEmail = "";
	}
	var emailSiteList = [ "163.com", "126.com", "qq.com", "yeah.net", "136.com", "gmail.com", "yahoo.cn",
			"yahoo.com.cn", "foxmail.com", "sohu.com", "sina.com", "eyou.com", "wo.com.cn", "tom.com",
			"hotmail.com", "21cn.com", "189.cn", "sogou.com", "188.com", "139.com" ];
	var strArr = registerEmail.split('@');
	var containFlag = false;
	for (var i = 0; i < emailSiteList.length; i++) {
		if (emailSiteList[i] == strArr[1]) {
			containFlag = true;
			break;
		}
	}
	var emailSite = '';
	if (containFlag) {
		if (strArr[1] == 'gmail.com' || strArr[1] == 'eyou.com' || strArr[1] == 'hotmail.com') {
			emailSite = "www." + strArr[1];
		} else {
			emailSite = "mail." + strArr[1];
		}
		emailSite = "http://" + emailSite;
	} else {
		$("#loginEmail").hide();
	}
	$("#loginEmail").click(function() {
		window.location.href = emailSite;
	});
}
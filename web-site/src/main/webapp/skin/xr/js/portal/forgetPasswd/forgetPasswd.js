/**
 * 初始化密码找回页面(找回密码成功页面也在这里一起处理)
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('portal/col/header.js');
	initJs('portal/col/footer.js');
	initCss('portal/forgetPasswd/forgetPasswd.css');	
	i18n.init();//国际化初始化
	i18nPage();//国际化当前页面
	$("#forgetPasswordDiv").css("background","url("+rootPath+"/skin/"+customerPath+"/images/portal/register/table.png"+") repeat-x scroll 0 0 #FFFFFF");
	$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/register/registerSuccess.png");
	$(".input_email").focusout(validfun);
	$(".get_passwd_button").click(function() {
		if (validfun() == true) {
			var mailAddress = $(".input_email").val();
			var jsonRequest = "{\"email\":\"" + mailAddress + "\",\"domain\": {\"code\":\"" + domainCode + "\"}}";
			$.ajax({
				url : '../user_mgmt/user!getPassword.action',
				type : 'POST',
				dataType : 'json',
				async : false,
				contentType : "application/json; charset=utf-8",
				data : jsonRequest,
				success : function(data) {
					if (data["success"]) {
						verifyStyle(true, "emailMes", "");
						window.location.href = "../portal/getPasswordSuccess.html?email="+mailAddress;
					} else {
						verifyStyle(false, "emailMes", data['resultMsg']);
					}
				}
			});		
		}
	});
	loginEmail();//密码找回成功页面,跳转到邮箱
});

function i18nPage() {
	$("#forgetPassword").text(i18n.get("forget_passwd"));
	$("#forget_passwd_email").text(i18n.get("forget_passwd_email"));
	$(".input_email_tip").text(i18n.get("input_email_tip"));
	$(".get_passwd_button").text(i18n.get("get_passwd_button"));
	$(".findPasswd_success_content").text(i18n.get("findPasswd_success_content"));
	$("#loginEmail").text(i18n.get("loginEmail"));
}

function validfun() {
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if ("" == $(".input_email").val() || null == $(".input_email").val()) {
		verifyStyle(false, "emailMes", i18n.get('mail_none_tip'));
		return false;
	} else if (reg.test($(".input_email").val())) {
		verifyStyle(true, "emailMes", "");
		return true;
	} else {
		verifyStyle(false, "emailMes", i18n.get('mail_format_error'));
		return false;
	}
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
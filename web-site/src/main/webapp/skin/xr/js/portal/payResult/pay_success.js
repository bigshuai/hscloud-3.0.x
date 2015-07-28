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
	$("#successImage").attr("src",rootPath+"/skin/"+customerPath+"/images/common/paySuccess.png");
});

function i18nPage() {
	$("#forgetPassword").text(i18n.get("account_accountRecharge"));
	$(".findPasswd_success_content").text(i18n.get("recharge_success"));
}




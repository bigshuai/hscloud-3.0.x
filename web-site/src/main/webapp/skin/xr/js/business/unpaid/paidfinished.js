$("document").ready(function(){
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/unpaid/paidfinished.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	fillContext();
});

function fillContext(){
	$("#content_congraduation").text("您已成功支付订单");
	$("#content_goto_1").text("现在您可以 ");
	$("#content_goto_2").text(" 或 ");
}

function goToVpdc(){
	window.location.href="../../vpdc/center/vmList.html";
}

function goToPaidOrder(){
	window.location.href="../orderpaid/index.html?LIIndex=1";
}
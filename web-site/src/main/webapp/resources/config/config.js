﻿﻿﻿var path = window.document.location.pathname;
var defaultLang = 'zh_CN';//默认国际化语言
var customerPath = 'xr';//客户路径
var domainCode = 'XR';//客户分平台CODE
var orderPayInterval=60; //订单连续提交间隔(时间为s)
//var billingServer = 'http://192.168.177.10:8070/billing.web/';//billing服务器请求地址
var billingServer = 'http://172.16.15.70:8081/billing.web/';//billing服务器请求地址
var planId=519082185;//bpms营销计划中planId-佛山分平台
var vncURL = {
		"host":'172.16.15.70',
		"port":6080
};
/************************** 菜单配置 ******************************/
var menuNameArr=['mycenter','buy','servicecenter','usercenter','accountcenter','messagecenter'];
var menuPageUrls=['../../vpdc/center/vmList.html','../../shop/package/index.html',
                  '../../business/orderunpaid/index.html','../../user/info/index.html',
                  '../../account/account/account.html','../../message/noread/index.html'];
var menuWidth=942;

/**
 * 请按模块添加对应的tab名称和页面，方便以后维护(每个模块都放在一起，空一行)
 * tab名称第一个元素存放tab对应的menu index 用于页面跳转时定位menu
 */
/************  1.我的虚拟中心  *************/
var vpdcCenterNameArr=[0,"publicResources"];
var vpdcCenterPageUrls=["../../vpdc/center/vmList.html"];
//var vpdcCenterNameArr=[0,"publicResources","VPDC"];
//var vpdcCenterPageUrls=["../../vpdc/center/vmList.html","../../vpdc/vcenter/VPDC.html"];

/**************  2.购买中心  **************/
var buyTagNameArr=[1,"package","demand"];//购买云主机
var buyPageUrls=["../../shop/package/index.html","../../shop/demand/index.html"];

/**************  3.服务中心  **************/
var busiTagNameArr=[2,"buss_upaid_order","buss_paid_order","buss_cancel_order",
                    "buss_try_vm","buss_normal_vm","vmbuss_renewal","buss_refund"];//续订业务
var busiPageUrls=["../../business/orderunpaid/index.html","../orderpaid/index.html",
                  "../../business/ordercancel/ordercancel.html","../vmtry/vmtry.html",
                  "../vmregular/vmregular.html","../../business/servicerenewal/renewalservice.html",
                  "../../business/servicerefund/index.html"];

/**************  4.用户中心  **************/
var userTagNameArr=[3,"p/e information"];
var userPageUrls=["../info/index.html"];

/**************  5.账户中心  **************/
var accountTagNameArr=[4,"account_accountRecharge","account_logDetail","applyForInvoices"];
var accountPageUrls=["../account/account.html","../deals/deals.html","../applyInvoice/applyInvoices.html"];

/**************  6.消息中心  **************/
var messageTagArr=[5,"unread","readed"];
var messagePageUrls=["../../message/noread/index.html","../../message/readed/index.html"];
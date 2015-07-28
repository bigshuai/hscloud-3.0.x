var userJson;
var domainInfo;
var dialog = null;
var exteranalUser = null;
function selectRadio(str) {
	$('input[name=pay_bank]').each(function(){
		if(this.value == str) {
			this.checked = true;
		}
	});
}

function get(str) {
	return i18n.get(str);
}

function refresh() {
	//dialog.close();
//	var url = '../cloud/account/account/account.html';
//	parent.document.getElementById("main").src = url;
	window.location="../account/account.html?LIIndex=0";
}

function selectRecharge(obj) {
	$("input[name='pay_bank']:checked").each(function(i,domObj){
        domObj.checked=false;
    });
    $('.rechargeTypeCss').each(function(index){
        if(this == obj) {
            $(this).addClass("td1_css").removeClass("td_css");
            $('#td' + (index + 1) + '_pay').show();
        } else {
        	$(this).addClass("td_css").removeClass("td1_css");
            $('#td' + (index + 1) + '_pay').hide();
        }
    });
    if(obj.id == 'td3') {
        $('#tdLine').hide();
        $('#tdButton').hide();
    } else {
        $('#tdLine').show();
        $('#tdButton').show();
    }
}

function getAlertHtml(serviceHotline){
	var htmlStr='<div id="winDiv">'+
	              '<div id="failDiv">'+
	                '<img src="../../../skin/'+customerPath+'/images/account/recharge_fail.png" style="left:0px;"/><span class="rc_span gray_color">充值失败...</span>'+
	                '<span class="rc_tip rc_fail_tip">'+
	                  '<span>充值失败，请拨打电话</span>'+
	                  '<span class="phone_class orange_color">'+serviceHotline+'</span>'+
	                '<br/>'+'<span>或者直接联系负责贵公司的客户经理</span>'+
	                '<br/><span>协助处理</span>'+
	                '</span>' +
	              '</div>'+
	              '<div id="succDiv">'+
	                 '<img src="../../../skin/'+customerPath+'/images/account/recharge_succ.png" style="left:0px;"/><span class="rc_span orange_color">充值成功！</span>'+
	                 '<span class="rc_tip rc_succ_tip">'+
	                   '<span>立即查看</span><a class="orange_color" href="javascript:refresh()" onclick="refresh()">查看账户余额</a>'+
	                 '</span>'+
	              '</div>'+
	            '</div>';
	return htmlStr;
}

function payment(){
	/*if(!userJson['resultObject']["domain"]['onlinePayFlag']){
		openDialog(get('account_notSupport'));
		return ;
	}
	var serviceHotline=userJson['resultObject']["domain"]['serviceHotline'];*/
	
	if(!domainInfo.onlinePayFlag){
		openDialog(get('account_notSupport'));
		return ;
	}
	var serviceHotline=domainInfo.serviceHotline;
	
	var payBank="";
	if($("input[name='pay_bank']:checked").length>0){
		payBank=$("input[name='pay_bank']:checked").val();
	}else{
		openDialog(get('account_payTypeAlert'));
		return ;
	}
	
	var totalFee = $("#bankTotalFee").val();
	if(totalFee){
		if(totalFee.indexOf(".")!=-1){
			if(!(/^(\d+\.\d{1,2})$/g.test(totalFee))){//
				openDialog(get('account_amountAlert'));
				return;
			}
		}else{
			if(!(/^\d+$/.test(totalFee))){
				openDialog(get('account_amountAlert'));
				return;
			}
		}
		if(totalFee<0.01){
			openDialog(get('account_amountAlert2'));
			return;
		}
	}else{
		openDialog(get('account_amountAlert'));
		return;
	}
	
	dialog = openDialog(getAlertHtml(serviceHotline), get('account_prompt'), 220, 480);
	var url="./paying.html?totaoFee="+totalFee+"&payBank="+payBank;
	window.open(url);
}

function usertip(){
 	Dialog.alert("您尚未与老平台建立关联账户，请联系客服人员！");
}

function transferShow(){
	var html = '<div style="float:left;">'
			+'<div id="externalUser_div" class="transfershow_div">'+get('account_external')//老平台名称:
			+'    <a ></a>'
			+'</div>'
			+'<div id="accountBalance2_div" class="transfershow_div">'+get('account_accountBalance')//账户余额:
			+'	<a ></a>'+get('account_yuan')//元
			+'</div>'
			+'<div id="pointsBalance2_div" class="transfershow_div">'+get('account_couponsBalance_div')//返点余额:
			+'	<a ></a>'+get('vmbuss_point')//点
			+'</div>'
			+'<div id="accountType_div" class="transfershow_div">'+get('account_amount_type')//金额类别:
			+'		<input type="radio" name="feeType" value="1" checked="checked"/>'
			+'		<span id="express_span">'+get('order_amount')+'</span>'//现金
			+'		<input type="radio" name="feeType" value="2"/>'
			+'		<span id="express_span">'+get('point_amount')+'</span>'//返点
			+'</div>'
			+'<div id="transferType_div" class="transfershow_div">'+get('account_transfer_type')//转账类别:
			+'		<input type="radio" name="transferMode" value="2" checked="checked"/>'
			+'		<span id="express_span">'+get('account_turn_into')+'</span>'//转入
			+'		<input type="radio" name="transferMode" value="1"/>'
			+'		<span id="express_span">'+get('account_turn_out')+'</span>'//转出1：（老平台转入，新平台转出）
			+'</div>'
			+'<div id="transferBalance_div" class="transfershow_div">'+get('account_transfer_amount')//转账金额:
			+'	<input type="text" id="transferFee" style="width: 80px" class="login_input0" onkeyup="clearNoNum(this)"/>'+get('account_yuan')//元
			+'</div> '
		+'</div>' ;
	var dialog = new Dialog("dialog");
    dialog.Width = 430;
    dialog.Height = 200;
    dialog.Title = i18n.get("account_transfer");
    dialog.innerHTML=html;
    dialog.OKEvent = function() {
    	var feeType = 1;
    	var transferMode = 1;
    	var fee = $("#transferFee").val();
    	fee = fee.replace(/\.$/g,""); 
    	//alert(fee);
    	if($("input[name='feeType']:checked").length>0){
    		feeType=$("input[name='feeType']:checked").val();
    	}
    	if($("input[name='transferMode']:checked").length>0){
    		transferMode=$("input[name='transferMode']:checked").val();
    	}
    	if(exteranalUser != null && exteranalUser !=""){
    		if(!("SDHY"==domainCode && "2"==feeType)){
    		var url = "../../../bss/account!transfer.action";
    		$.ajax({
    			url:url,
    			type:'GET',
    			dataType:'json',
    			contentType:"application/json;charset=UTF-8",
    			data : {fee:fee,feeType:feeType,transferMode:transferMode,domainCode:domainCode},
    			async:false,
    			success:function(data){
    	             if(!data["success"]){
    	                 Dialog.alert(data["resultMsg"]);
    	             }else{
    	            	 dialog.close();
    	            	 refresh();
    	             }
    			}
    		});
    	}else{
    		Dialog.alert("时代宏远未开通返点互转功能");
    	}
    	}
    };    
    dialog.show();
    $("#accountBalance2_div a").text($("#accountBalance_div a").text());
    $("#pointsBalance2_div a").text($("#pointsBalance_div a").text());
    $("#externalUser_div a").text(exteranalUser);    
}


function clearNoNum(obj)
{
	//先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/[^\d.]/g,"");
	//必须保证第一个为数字而不是.
	obj.value = obj.value.replace(/^\./g,"");
	//保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(/\.{2,}/g,".");
	//保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
};

$("document").ready(function(){
	i18n.init();//国际化初始化
	initTagAndMenu(accountTagNameArr,accountPageUrls);
	
	$('#td1').text(get('account_online'));
	$('#td2').text(get('account_platform'));
	$('#td3').text(get('account_offline'));
	$('#payAmountId').text(get('account_payAmount') + "：");
	
	$('#yuan_div').text(i18n.get('account_yuan'));
	
	$('#offlineBank_div').text(i18n.get('account_offlineBank'));
	$('#offlineCardId_div').text(i18n.get('account_offlineCardId'));
	$('#offlineCompanyName_div').text(i18n.get('account_offlineCompanyName'));
	$('#offlinePayTitle_div').text(i18n.get('account_offlinePayTitle'));
	$('#offlinePayDesc_div').text(i18n.get('account_offlinePayDesc'));
	
	$('#submit_btn').text(i18n.get('account_payment'));
	
	$("#transferShow_div").text(i18n.get('account_transfer'))
	
	$('#payImgId').attr('src', '../../../skin/'+customerPath+'/images/account/alipay.gif');
	//$("#transferShow_div").bind('click',transferShow);
	$.ajax({
		async : true,
		type : "get",
		url : "../../../user_mgmt/user!getDomainByDomainId.action",
		dataType : 'json',
		success : function(json) {
			if (json.success) {
				if(json.resultObject){
					domainInfo = json.resultObject;
					$("#offlineBank").text(json.resultObject.bank);
					$("#offlineCardId").text(json.resultObject.cardNo);
					$("#offlineCompanyName").text(json.resultObject.name);
					if(json.resultObject.transferFlag==1){//为1时，允许跨平台转账。显示按钮
						$("#transferShow_div").show();
					}
				}
				
			}
		}
	});
	
	var urlStr = '../../../user_mgmt/user!getSessionUser.action';
	$.ajax({
		async : true,
		type : "get",
		url : urlStr,
		dataType : 'json',
		success : function(json) {
			userJson= json;
			$('#accountId_div').text(i18n.get('account_chargeAccount') + userJson['resultObject']['name']);
		},
		error : function(json) {
			//openDialog(get('account_readUserAlert'));
		}
	});
	
	$.ajax({
		async : true,
		type : "get",
		url : "../../../bss/account!getAccountBalance.action",
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
			}else{
				var accountBalance = json.resultObject['accountBalance'];
				var balance = '<a style="color:#390; font-size:22px; font-weight:bold;font-family:微软雅黑;">' + json.resultObject['accountBalance'] + '</a>';
				var balanceText = i18n.get('account_accountBalance') + balance + i18n.get('account_yuan');
				if(parseInt(accountBalance) < 100){
					 balance = '<a style="color:red; font-size:22px; font-weight:bold;font-family:微软雅黑;">' + json.resultObject['accountBalance'] + '</a>';
					 balanceText = i18n.get('account_accountBalance') + balance + i18n.get('account_yuan');
					var balance_alert = '  <a style="font-size:12px;color:red;">(' + i18n.get('account_balanceAlert') + ')</a>';
					balanceText = balanceText + balance_alert;
				}
				$('#accountBalance_div').html(balanceText);
				
				var couponsBalance = '<a style="color:#390; font-size:22px; font-weight:bold;font-family:微软雅黑;">' + json.resultObject['couponsBalance'] + '</a>';
				$("#pointsBalance_div").html(i18n.get('account_couponsBalance_div') + couponsBalance + i18n.get('account_coupons'));
				
				var giftsBalance = '<a style="color:#390; font-size:22px; font-weight:bold;font-family:微软雅黑;">' + json.resultObject['giftsBalanceStr'] + '</a>';
				$("#giftsBalance_div").html(i18n.get('account_giftsBalance_div') + giftsBalance+ i18n.get('account_yuan'));
			}
		},
		error : function() {
			$("#charge_balance").html(0);
			$("#charge_user").html("<span style='color: #dd2f39; font-weight: bold;'>"+get('account_getAccountError')+"</span>");
		}
	});
	//获取客户平台账户信息
	var urlStr = '../../../user_mgmt/user!getExternalUserOfLocal.action';
	$.ajax({
		async : true,
		type : "GET",
		url : urlStr,
		dataType : 'json',
		success : function(data) {
			exteranalUser= data.resultObject;
			if(exteranalUser != null && exteranalUser !=""){
				$("#transferShow_div").bind('click',transferShow);
			}else{
				$("#transferShow_div").bind('click',usertip);
			}
		},
		error : function(json) {
			//openDialog(get('account_readUserAlert'));
		}
	});
});

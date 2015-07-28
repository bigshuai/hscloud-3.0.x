var path="../../../";
var pageObje = new PageObje('tableId');
var balance = 0; //账户余额
var couponAccount = 0; //账户返点数
var giftAccount = 0;//账户礼金数
var couponsRebateRate = 0; //返点率
var giftsRebateRate = 0; //礼金折扣
var maxCoupon = 0; //最多返点
var maxGift = 0; //最多礼金
var tempFeeType = ''; //临时缴费类型
var currentStatus="";
var vmNoParam="";//续费时用
var globalVmNo = null;
var globalVmName = null;
var globalOrderNo = null;
var globalIpVal = null;
var refundApplyDIV;

$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initDialog();
	initCss('business/vmregular/vmregular.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	i18nPage();//国际化当前页面
    tryVMQuery();
	getBalance();
	initQueryHtml();
});

function i18nPage(){
	$("#vmbuss_name").text(i18n.get("vmbuss_name"))
	$("#vmbuss_ip").text(i18n.get("vmbuss_ip"))
	$("#vmbuss_open").text(i18n.get("vmbuss_open"))
	$("#vmbuss_expire").text(i18n.get("vmbuss_expire"))
	$("#vmbuss_status").text(i18n.get("vmbuss_status"))
	$("#allOperations").text(i18n.get("allOperations"))

}

function vmNameRender(value){
	var result='';
	var title='';
	if(value){
		title=value;
		if(value.length>15){
			result=value.substring(0,15)+"...";
		}
		else {
			result=value;
		}
	}
	return '<span title="'+title+'">'+result+'</span>';
}

function ipRender(value){
	var result='';
	var title='';
	if(value){
		title=value;
		if(value.length>15){
			result=value.substring(0,15)+"...";
		}else{
			result=value;
		}
	}
	return '<span title="'+title+'">'+result+'</span>';
}

function timeRender(value){
	if(value){
		return formatDateTime(new Date(parseInt(value)));
	}else{
		return '';
	}
}

function statusRender(value,record){
	var comments='';
	if(record["comments"]){
		comments=record["comments"];
	}
	var statusRenderStr="<span title='"+comments+"'>";
	statusRenderStr+=(vm_status_buss.i18n(value)+'</span>');
	statusRenderStr+="</span>";
	return statusRenderStr;
}


function operationRender(value,record){
	var renderStr="";	
	var vmNo=record["id"]
	var vmName = record["name"];
	var ipVal=record["ip"]
	var createTime = timeRender(record["createTime"]);
	var expireTime = timeRender(record["expireTime"]);
	var vmBuyType=record["buyType"];
	if (vmNo != null) {
		renderStr+='<span class="button button-action button-rounded button-small button_margin_left" onclick="showRenewFeeVM(\''+vmNo+'\','+record["scId"]+','+record["isEnable"]+','+vmBuyType+')">'+i18n.get("vmbuss_renew")+'</span>';
		/*if(vmBuyType==1){
			renderStr+='<a onclick="upgradeVM(\''+vmNo+'\')">'+i18n.get("vmbuss_upgrade")+'</a>';
		}*/  //屏蔽升级按钮
		renderStr+='<a onclick="showRefundApply(\''+vmNo+'\',\''+vmName+'\',\''+ipVal+'\',\''+createTime+'\',\''+expireTime+'\')">'+i18n.get("vmbuss_refundApply")+'</a>';
		renderStr+='<a onclick="showHistoryOrders('+record["referenceId"]+')">'+i18n.get("vmbuss_historyOrder")+'</a>';
	}
	return renderStr;
}

function cancelVM(referenceId){
	var param ={
			"referenceId":referenceId
	};
	var url=path+"ops/ops!cancelApplyTryVm.action";	
	$.ajax({
		url : path+'ops/ops!cancelApplyTryVm.action',
		type : 'Post',
		dataType : 'json',
		async : true,
		data : param,
		success : function(data) {
			if (!data.success) {
				Dialog.alert(data.resultMsg);
			}else{
				tryVMQuery();
			}
		}
	});
}

var dialog = null;
function showRenewFeeVM (vmNo,scId,isEnable,vmBuyType){
	if(1==isEnable){
		 Dialog.alert("服务器用于非法用途-停止服务!");
		 return;
	}
	vmNoParam=vmNo;
	$("#vmNoParam").text(vmNo);
	$("#regularFeeTypeTd").text(i18n.get("vmbuss_regularFeeType"));
	$("#accountBalanceTd").text(i18n.get("vmbuss_accountBalance"));
	$("#couponBalanceTd").text(i18n.get("vmbuss_couponBalance"));
	$("#giftBalanceTd").text(i18n.get("vmbuss_giftBalance"));
	$("#renewFeeAmountTd").text(i18n.get("vmbuss_renewFeeAmount"));
	if(vmBuyType==0){
		if(getVMFeeType(scId)){
			if(dialog != null) {
				$("#_DialogDiv_dialog").show();
		    	$("#_DialogBGDiv").show();
		    	return;
			}
			
			dialog = new Dialog("dialog");
		    dialog.Width = 550;
		    dialog.Height = 250;
		    dialog.Title = i18n.get('vmbuss_renew');
		    dialog.innerHTML=$('#regularDIV').html();
		    $('#regularDIV').remove();
		    dialog.close = function() {
		    	$("#_DialogDiv_dialog").hide();
		    	$("#_DialogBGDiv").hide();
		    };
		    dialog.OKEvent = function() {
		    	var id_price = $("#feeType").val();
		    	var feeTypeId = id_price.split("@")[0];	
		    	var couponAmount = $('#couponAmount').val();
		    	// 是否使用返点
		    	if($("#doUseCoupon")[0].checked == true && couponAmount != undefined) {
		    		if(couponAmount == '') {
		    			Dialog.alert(i18n.get('Rebate number can not be empty!'));
		    	    	return false;
		    		}
		    		if(parseFloat(couponAmount) > parseFloat(maxCoupon)) {
		    			var text = i18n.get('The max rebate is')+"xxmaxxx"+i18n.get("vmbuss_point");
		    			text = text.replace('xxmaxxx', maxCoupon);
		    			Dialog.alert(text);
		    			return false;
		    		}
		    		
		    		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
		    	    if(!reg.test(couponAmount)){
		    	    	Dialog.alert(i18n.get('Rebate malformed!'));
		    	    	return false;
		    	    }
		    	} else {
		    		couponAmount = 0;
		    	}
		    	
		    	var giftAmount = $('#giftAmount').val();
		    	//是否使用礼金
		    	if($("#doUseGift")[0].checked == true && giftAmount != undefined) {
		    		if(giftAmount == '') {
		    			Dialog.alert(i18n.get('Gift number can not be empty!'));
		    	    	return false;
		    		}
		    		if(parseFloat(giftAmount) > parseFloat(maxGift)) {
		    			var text = i18n.get('The max gift is')+"xxmaxxx";
		    			text = text.replace('xxmaxxx', maxGift);
		    			Dialog.alert(text);
		    			return false;
		    		}
		    		
		    		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
		    	    if(!reg.test(giftAmount)){
		    	    	Dialog.alert(i18n.get('Gift malformed!'));
		    	    	return false;
		    	    }
		    	} else {
		    		giftAmount = 0;
		    	}
		    		
		    	submitRenewRequest(vmNoParam,feeTypeId,couponAmount,giftAmount,dialog);
		    	
			};
		    dialog.show();
		    $("#feeType").val(tempFeeType);
		}
	}else if(vmBuyType==1){
		var dialog1 = new Dialog("dialog1");
	    dialog1.Width = 550;
	    dialog1.Height = 100;
	    dialog1.Title = i18n.get('vmbuss_renew');
	    dialog1.innerHTML=displayBuyPeriod4Demand();
	    dialog1.OKEvent = function() {
	    	if($("#buy_period").val()==0){
	    		Dialog.alert("请选择购买时长...");
	    		return;
	    	}
	    	var arr = [];
	    	arr.push("<table height='100%' border='0' cellpadding='10' cellspacing='0'>");
	    	arr.push("<tr><td width='60' align='right'><img id='Icon' src='"+IMGFOLDERPATH+"icon_confirm.png' width='44' height='44' align='absmiddle'></td>");
	    	arr.push("<td align='left' id='Message' style='font-family:Microsoft Yahei,verdana;font-size: 14px;font-weight: bold;color: #555555;'>"+"续费请求已发送，请稍候..."+"</td></tr></table>");
	    	var dialog2 = new Dialog("dialog2");
		    dialog2.Width = 550;
		    dialog2.Height = 100;
		    dialog2.Title = i18n.get('systemPrompt');
		    dialog2.innerHTML=arr.join('');
		    dialog2.show();
	    	submitRenewRequest(vmNo,$("#buy_period").val(),0,0,dialog1);
	    	dialog2.close();
	    }
		dialog1.show();
		$("#renew_for_need_label").text(i18n.get("vmbuss_regularFeeType"));
		$("#price_label").text(i18n.get("vmbuss_renewFeeAmount"));
		$("#account_balance_label").text(i18n.get("vmbuss_accountBalance"));
		$("#account_balance_amount").text(balance+i18n.get("yuan"));
		$("#price_unit").text(i18n.get("yuan"));
		$("#buy_period").change(function(){
			$.ajax({
				url : "../../../ops/ops!findDetailVmById.action",
				type : "get",
				dataType : 'json',
				async : false,
				data : "id=" + vmNo,
				success : function(json) {
					if (json.success) {
						var extDisks=json.resultObject.extdisks;// 扩展盘
						var extDiskArr=[];
						if(extDisks){
							for(var i=0;i<extDisks.length;i++){
								extDiskArr.push(extDisks[i].ed_capacity);
							}
						}
						var extDiskStr="";
						if(extDiskArr!=null&&extDiskArr.length>0){
							extDiskStr=extDiskArr.join(",");
						}
						$.ajax({
							url : billingServer+"calculatePrice",
							type : 'GET',
							dataType : 'jsonp',
							jsonp:'callback',
							jsonpCallback:'requestData',
							contentType: 'application/json; charset=utf-8',
							async : false,
							data : {
								domainCode:domainCode,//config.js中配置
								brandCode:brandCode,
								zoneGroupCode:getZoneGroupCode(json.resultObject.referenceId),
								CPU:json.resultObject.cpu,
								memory:json.resultObject.memory,
								systemDisk:json.resultObject.disk,
								extDisk:extDiskStr,
								bandwidth:json.resultObject.network,
								payMonth:$("#buy_period").val(),
								demondCount:1,
								IP:json.resultObject.ipNum
							},
							success : function(data) {	
								
								if(data == undefined){
									Dialog.alert("vmbuss_error");
								}else{	
									var totalPrice = parseFloat(data);
									$("#price_amount").text(totalPrice);
								}
							}
						});
					} 
					else {
						Dialog.alert(json.resultMsg);
					}
				}
			});
			
		});
	
	}
}

function getZoneGroupCode(referenceId){
	var zoneGroupCode='';
	$.ajax({
		url:path+'/sc/zoneGroup!getZoneGroupCodeByReferenceId.action?',
		data:{
			'referenceId':referenceId
		},
		method:"POST",
		dataType:"json",
		async:false,
		success:function(data){
			if (data.success) {
				zoneGroupCode=data.resultObject;
			}
		}
	})   
	
	return zoneGroupCode;
}

function submitRenewRequest(vmNo,feeTypeId,couponAmount, giftAmount,dialog){
	var url=path+"ops/ops!renewFeeVM.action";
	var param ={
			"id":vmNo,
			"feeTypeId":feeTypeId,
			"couponAmount":couponAmount,
			"giftAmount":giftAmount,
			"planId":planId
	}
	$.ajax({
		url:url,
		dataType:"json",
		data:param,
		method:"Post",
		success:function(data){
			if (!data.success) {
				if(data.resultMsg==i18n.get('Operation failed, insufficient account balance!')){
					var htmlStr="<div id='needRechargeTip'>"+
					"<div>"+data.resultMsg
					   +
					"</div>"+
					"<div>"+
						"<a onclick='goToCharge()'>"+i18n.get('go to recharge')+"</a>"+
					"</div>"+
				    "</div>";
				Dialog.alert(htmlStr);
				}else{
					Dialog.alert(data.resultMsg);
				}
				
			}else{
				tryVMQuery();
				Dialog.alert("续费成功！");
			}
			dialog.close();
		}
	});
}

function getVMFeeType(scId){
	$("#feeType").empty();
	var bl = true;
	$.ajax({
		async : false,
		type : "get",
		url : path
				+ "ops/ops!getVMFeeType.action",
		data : "scId=" + scId,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				Dialog.alert(json.resultMsg);
			} else {
				for(var i=0;i<json.resultObject.length;i++){
					$("#feeType").append("<option value='"+json.resultObject[i].id+"@"+json.resultObject[i].price+"'>"+json.resultObject[i].period+i18n.get("month")+"</option>"); 
				}
				tempFeeType = json.resultObject[json.resultObject.length-1].id+"@"+json.resultObject[json.resultObject.length-1].price;
				$("#feeType").val(json.resultObject[json.resultObject.length-1].id+"@"+json.resultObject[json.resultObject.length-1].price); 
				$("#feePrice").html(json.resultObject[json.resultObject.length-1].price+i18n.get("yuan"));
				$("#balanceId").html(balance + i18n.get("yuan"));
				
				var feePrice = json.resultObject[json.resultObject.length-1].price;
				
				couponChange(feePrice);
				var usePointOrNot = json.resultObject[json.resultObject.length-1].usePointOrNot;
				if(usePointOrNot != true|| couponAccount <= 0) {
					$("#couponTr").hide();
					$("#doUseCoupon")[0].checked = false;
				}  else {
					$("#couponTr").show();
					$("#doUseCoupon")[0].checked = true;
				}
				
				giftChange(feePrice);
				var useGiftOrNot = json.resultObject[json.resultObject.length-1].useGiftOrNot;
				if(useGiftOrNot != true|| giftAccount <= 0) {
					$("#giftTr").hide();
					$("#doUseGift")[0].checked = false;
				}  else {
					$("#giftTr").show();
					$("#doUseGift")[0].checked = true;
				}
			}
		},
		error : function() {
			Dialog.alert(i18n.get("vmbuss_error"));
			bl = false;
		}
	});
	return bl;
}

function getBalance(){
	//账户余额
	var requstUrl = path+"bss/account!getAccountBalanceAndRealRebate.action";
	$.ajax({
		url:requstUrl,
		async:false,
		error:function(){
			balance = 0;
			couponAccount = 0;
			giftAccount = 0;
			couponsRebateRate = 0;
			giftsRebateRate = 0;
		},
		success:function(data){
			if(data.success == true){
				json = data.resultObject;
				balance = json.balance;
				couponAccount=json.coupons;
				giftAccount = json.giftsBalance;
				couponsRebateRate = json.couponsRebateRate == null ? 0 : json.couponsRebateRate;
				giftsRebateRate = json.giftsRebateRate== null ? 0 : json.giftsRebateRate;
			}else{
				balance = 0;
				couponAccount = 0;
				giftAccount = 0;
				couponsRebateRate = 0;
				giftsRebateRate = 0;
			}
		}
	});
}

function couponChange(feePrice) {
	var rebateRateFloat = getRebateRateFloat(couponsRebateRate);
	var value = floatMulti(feePrice, rebateRateFloat);
	if(value > couponAccount) {
		value = couponAccount;
	}
	value = rounding(value);
	maxCoupon = value;
	
	var text = i18n.get('vmbuss_doUseCouponText');
	text = text.replace('xxmaxxx', value);
	text = text.replace('xxinputxx', '<input class="couponInput" style=\"width: 50px;\" type="text" id="couponAmount" value="' + value + '"/>');
	
	var checkboxText = '<input type="checkbox" id="doUseCoupon" checked=true/>';
	var couponAccountText = "<div style=\"width: 80px;float:left;\">"+couponAccount + i18n.get('vmbuss_point') + "</div><div style=\"float:left;\">"+
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + 
		checkboxText + i18n.get('vmbuss_useCoupon') + 
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + text+"</div>";
	$("#couponAccountId").html(couponAccountText);
}

function giftChange(feePrice) {
	var rebateRateFloat = getRebateRateFloat(giftsRebateRate);
	var value = floatMulti(feePrice, rebateRateFloat);
	if(value > giftAccount) {
		value = giftAccount;
	}
	value = rounding(value);
	maxGift = value;
	
	var text = i18n.get('vmbuss_doUseGiftText');
	text = text.replace('xxmaxxx', value);
	text = text.replace('xxinputxx', '<input class="couponInput" style=\"width: 50px;\" type="text" id="giftAmount" value="' + value + '"/>');
	
	var checkboxText = '<input type="checkbox" id="doUseGift" checked=true/>';
	var couponAccountText = "<div style=\"width: 80px;float:left;\">"+giftAccount + i18n.get('account_yuan') + "</div><div style=\"float:left;\">"+
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + 
		checkboxText + i18n.get('vmbuss_useGift') + 
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + text+"</div>";
	$("#giftAccountId").html(couponAccountText);
}

/*----------各个字段所用renderer结束-------------------*/

function splitExtDisk(addDisk){
	if(addDisk){
		var extDiskNameArr=addDisk.split(",");
		for(var i in extDiskNameArr){
			extDiskNameArr[i]=extDiskNameArr[i]+'G';
		}
		return extDiskNameArr.join(',');
		}else{
			return '';
		}
}

//未支付订单分页展示
function tryVMQuery() {
	pageObje.size = 8
	pageObje.methodArray = [ [ 'name', 'vmNameRender' ],
	                         ['ip','ipRender'],
	                         ['expireTime','timeRender'],
	                         ['createTime','timeRender'],
							 ['referenceId','operationRender']
						   ];
	pageObje.column = [ 'name','ip','referenceId','expireTime','createTime'];		
	pageObje.jsonRequest = {
		"type":1,
		"page" : pageObje.current,
		"limit" : pageObje.size
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

function pageChange(pageObje) {
	pageObje.jsonRequest['page']=pageObje.current;
	url = path+'ops/ops!getVmsByUser.action'
	ajaxPost(url,false,pageObje.jsonRequest,function(data){
		if (data.success == true) {
			pageCreatorR2(pageObje, data.resultObject);
		} else {
			Dialog.alert(data.resultMsg);
		}
	},false);
}


function query(){	
	var queryType=$("#queryType").val();
	var queryValue=$("#query").val();
	pageObje.jsonRequest = {
			"status_buss":currentStatus,
			"query":queryValue,
			"queryType":queryType,
			"type":1,
			"page" : pageObje.current,
			"limit" : pageObje.size,
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

function initQueryHtml(){
	var optionStr='<option value="" select="select">'+i18n.get("vmbuss_all")+'</option>'+
		'<option value="ipOuter">'+i18n.get("outerip")+'</option>'+
		'<option value="ipInner">'+i18n.get("innerip")+'</option>'+
		'<option value="vmName">'+i18n.get("vmbuss_name")+'</option>';
    $("#queryType").append(optionStr);
}

function displayBuyPeriod4Demand(){
	var optionStr='<option value="0">'+i18n.get("choose")+'...</option>'+
	'<option value="12">'+12+i18n.get("timeDesc")+'</option>'+
	'<option value="6">'+6+i18n.get("timeDesc")+'</option>'+
	'<option value="3">'+3+i18n.get("timeDesc")+'</option>'+
	'<option value="1">'+1+i18n.get("timeDesc")+'</option>';
	var displayStr=
	'<div id="renew4Need">'+
    '<span id="renew_for_need_label"></span>'+
	'<select id="buy_period">'+optionStr+'</select>'+
	'<div id="account_balance_div">'+
	'<span id="account_balance_label">'+
	'</span>'+
	'<span id="account_balance_amount">'+
	'</span>'+
	'</div>'+
	'<div id="renew_for_need_price">'+
		'<span id="price_label"></span>'+
		'<span id="price_amount">0</span>'+
		'<span id="price_unit"></span>'+
	'</div>'+
    '</div>';
	return displayStr;
}


function changeFee(obj){
	var id_price = obj.value;
	var price = id_price.split("@")[1];
	$("#feePrice").html(price+i18n.get("yuan"));
	couponChange(price);
	giftChange(price);
}

function getRebateRateFloat(arg) {
	if(arg >= 100) {
		return 1;
	}
	var length = arg.toString().split(".")[0].length;
	var value = arg.toString().replace(".","");
	var result = "0.";
	for(var i = length; i < 2; i++) {
		result = result + '0';
	}
	result = result + value;
	return result;
}
//四舍五入
function rounding(param) {
	if(!param){
		return param;
	}
	var floatValue = param.toString().split(".")[1];
	if(floatValue != null && floatValue.length >= 3) {
		var char = floatValue.charAt(2);
		if(char > 4) {
			floatValue = floatAdd(floatValue.substring(0, 2), 1);
		}else{
			floatValue = floatValue.substring(0,2);
		}
		return param.toString().split(".")[0] + '.' + floatValue;
	}
	return param;
}


function goToCharge(){
	window.location.href="../../account/account/account.html";
}

//查看VM历史订单
function showHistoryOrders(referenceId){
	var param ={
			"referenceId":referenceId
	}
	$.ajax({
		url:path+"order/order!getVmRelatedPaidOrder.action",
		dataType:"json",
		data:param,
		method:"Post",
		success:function(data){
			if (!data.success) {
				(data.resultMsg);
			}else{
				var dataList = data['resultObject'];
				loadOrders(dataList);
				var dialog = new Dialog("historyDetailDialog");
			    dialog.Width = 650;
			    dialog.Height = 400;
			    dialog.Title = i18n.get('vmbuss_historyOrder');
			    dialog.innerHTML=$('#historyOrdersDIV').html();
			    dialog.show();
                $("#_ButtonOK_historyDetailDialog").hide();
			}
		}
    });
}

//获取VM历史订单
function loadOrders(dataList){
	$("#historyOrdersDIV").empty();
	var start="<div style='height:400px;overflow-y:scroll;'><table cellpadding=\"0\" cellspacing=\"0\"  width=\"100%\" style=\"font-size: 12px;\">"+
			"<tr height=\"30\">"+
			"<td width=\"150\" align=\"center\" style=\"font-weight: bold;\">"+i18n.get("orderNo")+"</td>"+
			"<td align=\"center\" style=\"font-weight: bold;\">"+i18n.get("totalPrice")+"</td>"+
			"<td align=\"center\" style=\"font-weight: bold;\">"+i18n.get("payTime")+"</td>"+
			//"<td align=\"center\" style=\"font-weight: bold;\">"+lang.user.trlog.remark+"</td>"+
			"</tr>";
	var end = "</table></div>";
	var output = "";
	var payDate = "";
	var remark = "";
	var remark_ = "";
	for(var i=0;i<dataList.length;i++){
		payDate = dataList[i].payDate==null?"":formatDateTime(new Date(dataList[i].payDate));
		remark_ = dataList[i].remark;
		remark = remark_;
		if(remark_.length>15){
			remark = remark_.substring(0,15)+"...";
		}
		output += '<tr height="30">'+
					'<td width="150" align="center" style="border-top:1px #CCCCCC solid;">'+dataList[i].orderNo+'</td>'+
					'<td align="center" style="border-top:1px #CCCCCC solid;">'+dataList[i].totalPrice+i18n.get("yuan")+'</td>'+
					'<td align="center" style="border-top:1px #CCCCCC solid;">'+payDate+'</td>'+
					//'<td align="center" style="border-top:1px #CCCCCC solid;" title="'+remark_+'">'+remark+'</td>'+
				 '</tr>';
	}
	var div = $(start+output+end);
	div.appendTo($("#historyOrdersDIV"));
	return true;
}

//申请退款窗口
function showRefundApply(vmNo, vmName, ipVal, createTime, expireTime) {
	// 判断机器号为vmNo的这台虚拟机当前是否被禁用(禁用包含手动禁用和到期禁用)
	if (!isVmDisabled(vmNo)) {
		// 判断机器号为vmNo的这台虚拟机是否已经申请过退款
		//获取当前日期是否为月末
		if(getLastDayOfMonth()){
			if (!doesHaveApplyForRefund(vmNo)) {
				// 如果没有申请过退款，就加载申请退款窗口的详细信息
				if (getRefundApplyInfo(vmNo, vmName, ipVal, createTime, expireTime)) {
					var dialog = new Dialog("showRefundApplyDialog");
				    dialog.Width = 650;
				    dialog.Height = 280;
				    dialog.Title = i18n.get('vmbuss_refundApply');
				    dialog.innerHTML = $('#refundApplyDIV').html();
				    dialog.OKEvent=function() {
				    	var uuid = globalVmNo;
				    	var vmName = globalVmName;
				    	var orderNo = globalOrderNo;
				    	var ipVal = globalIpVal;
				    	var refundReasonType = $("#refundReasonType").val();
				    	var applyReason = $("#applyReason").val();
				    	if (applyReason.length > 200) {
				    		Dialog.alert("最多输入200个字符!");
				    		return;
				    	}
				    	// 如果不存在uuid的那条记录，向数据库中插入一条新的记录
				    	var url =path+"order/refundManagement!submitRefundApply.action";
				    	var jsonRequest = {
				    			"uuid":uuid,
				    			"vmName":vmName,
				    			"orderNo":orderNo,
				    			"ipVal":ipVal,
				    			"refundReasonType":refundReasonType,
				    			"applyReason":applyReason
				    	};
				    	//  弹出退款申请页面,填写申请原因，点击确定按钮提交数据:
				        //  1)如果存在uuid且status为3或者4，那么再次申请，此时注意：
				        //    再次申请是去把数据库中的uuid且status为3或者4的那条记录更新为status为1，退款原因类型和退款申请原因也会更新 	
				        //  2)如果不存在uuid的那条记录，向数据库中插入一条新的记录
				    	if(isExistedVMStatusEquals3Or4ByUuid(uuid)){
				    		// 如果isExistedVMStatusEquals4ByUuid(uuid)为true也就是说存在存在uuid且status为4
				    		// 再次申请是去把数据库中的uuid且status为3或者4的那条记录更新为status为1，退款原因类型和退款申请原因也会更新
				    		url = path+"order/refundManagement!submitRefundApplyOnceAgain.action";
				    		jsonRequest = {
				    				"uuid":uuid,
				    				"refundReasonType":refundReasonType,
				    				"applyReason":applyReason
				    		};
				    	}
				    	// 如果不存在uuid的那条记录，向数据库中插入一条新的记录
				    	$.ajax({
				    		url:url,
				    		method:"Post",
				    		dataType:"json",
				    		data:jsonRequest,
				    		success:function(data){
				    			if (!data.success) {
				    				Dialog.alert(data.resultMsg);
				    			} else {
				    				Dialog.alert(i18n.get("vmbuss_refundApplySuceess"));
				    			}
				    		}
				    	});
				    	dialog.close();
				    }
				    dialog.show();
				    // dialog有bug,把vmregular.html页面的代码放在dialog里面直接引用页面会出现2个相同的refundApplyDIV
				    $('#refundApplyDIV').remove();
					$("#refundReasonType").val("1");
					$("#refundReasonType_description").html(" ");
					$("#applyReason").val("");
				}
			}
		}
	}
}
//判断机器号为vmNo的这台虚拟机当前是否被禁用(禁用包含手动禁用和到期禁用)
function isVmDisabled(vmNo){
	var flag = false;
	$.ajax({
		async : false,
		type : "post",
		url : path + "order/refundManagement!judgeWhetherVmIsDisabledByUUID.action",
		data : "uuid=" + vmNo,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				Dialog.alert(json.resultMsg);
			} else {
				if(json.resultObject){
					Dialog.alert("服务器用于非法用途-停止服务!");
				}
				flag = json.resultObject;
			}
		},
		error : function() {
			Dialog.alert(i18n.get("vmbuss_error"));
		}
	});
	return flag;
}
// 判断机器号为vmNo的这台虚拟机是否已经申请过退款
function doesHaveApplyForRefund(vmNo) {
	var flag = false;
	$.ajax({
		async : false,
		type : "post",
		url : path + "order/refundManagement!isVMApplyingRefundByUuid.action",
		data : "uuid=" + vmNo,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				Dialog.alert(json.resultMsg);
			} else {
				if(json.resultObject){
					Dialog.alert("对不起，您已经做过退款操作!");
				}
				flag = json.resultObject;
			}
		},
		error : function() {
			Dialog.alert(i18n.get("vmbuss_error"));
		}
	});
	return flag;
}
// 获得申请退款窗口的详细信息
function getRefundApplyInfo(vmNo, vmName, ipVal, createTime, expireTime) {
	if (typeof $('#refundApplyDIV').html() == "string") {
		refundApplyDIV = $('#refundApplyDIV').html();
	}
	else {
		var appendDiv = '<div style="width: 70%; display: none" id="refundApplyDIV">' + refundApplyDIV + '</div>';
	    $("#wapper").append(appendDiv);
	}
	
	var flag = true;
	$.ajax({
		async : false,
		type : "get",
		url : path + "ops/ops!findDetailVmById.action",
		data : "id=" + vmNo,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				Dialog.alert(json.resultMsg);
			} else {
				// 显示机器号
				$("#vmNo").html("　" + json.resultObject.vmId);
				// 显示开通时间
				var vmCreateTime = createTime;
				if (vmCreateTime == null) {
					vmCreateTime = "--";
				}
				$("#vmCreateTime").html("　" + vmCreateTime);
				// 显示到期时间
				var vmExpireTime = expireTime;
				if (vmExpireTime == null) {
					vmExpireTime = "--";
				}
				$("#vmExpireTime").html("　" + vmExpireTime);
				// 显示订单编号
				var orderNo = json.resultObject.orderNo;
				if (orderNo == null) {
					orderNo = "--";
				}
				$("#orderNo").html("　" + orderNo);
				// 传给submitRefundApply方法4个参数
				globalVmNo = vmNo;
				globalVmName = vmName;
				globalOrderNo = orderNo;
				globalIpVal = ipVal;
			}
		},
		error : function() {
			Dialog.alert(i18n.get("vmbuss_error"));
			flag = false;
		}
	});
	return flag;
}
// 点击退款申请窗口的确定按钮
function submitRefundApply() {
	var uuid = globalVmNo;
	var vmName = globalVmName;
	var orderNo = globalOrderNo;
	var ipVal = globalIpVal;
	var refundReasonType = $("#refundReasonType").val();
	var applyReason = $("#applyReason").val();
	if (applyReason.length > 200) {
		Dialog.alert("最多输入200个字符!");
		return;
	}
	// 如果不存在uuid的那条记录，向数据库中插入一条新的记录
	var url =path+"order/refundManagement!submitRefundApply.action";
	var jsonRequest = {
			"uuid":uuid,
			"vmName":vmName,
			"orderNo":orderNo,
			"ipVal":ipVal,
			"refundReasonType":refundReasonType,
			"applyReason":applyReason
	};
	//  弹出退款申请页面,填写申请原因，点击确定按钮提交数据:
    //  1)如果存在uuid且status为3或者4，那么再次申请，此时注意：
    //    再次申请是去把数据库中的uuid且status为3或者4的那条记录更新为status为1，退款原因类型和退款申请原因也会更新 	
    //  2)如果不存在uuid的那条记录，向数据库中插入一条新的记录
	if(isExistedVMStatusEquals3Or4ByUuid(uuid)){
		// 如果isExistedVMStatusEquals4ByUuid(uuid)为true也就是说存在存在uuid且status为4
		// 再次申请是去把数据库中的uuid且status为3或者4的那条记录更新为status为1，退款原因类型和退款申请原因也会更新
		url = path+"order/refundManagement!submitRefundApplyOnceAgain.action";
		jsonRequest = {
				"uuid":uuid,
				"refundReasonType":refundReasonType,
				"applyReason":applyReason
		};
	}
	// 如果不存在uuid的那条记录，向数据库中插入一条新的记录
	$.ajax({
		url:url,
		method:"Post",
		dataType:"json",
		data:jsonRequest,
		success:function(data){
			if (!data.success) {
				Dialog.alert(data.resultMsg);
			} else {
				Dialog.alert(i18n.get("vmbuss_refundApplySuceess"));
			}
		}
	})
	$('#refundApplyDIV').dialog('close');

}
// 根据uuid去获得虚拟机的状态：1.申请中2.已审批3.拒绝4.用户已取消
// 判断虚拟机的状态是为(3.拒绝4.用户已取消)的记录在hc_vm_refund_log表中是否存在
function isExistedVMStatusEquals3Or4ByUuid(uuid){
	var flag = false;
	$.ajax({
		async : false,
		type : "get",
		url : path + "order/refundManagement!isExistedVMStatusEquals3Or4ByUuid.action",
		data : "uuid=" + uuid,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				Dialog.alert(json.resultMsg);
			} else {
				flag = json.resultObject;
			}
		},
		error : function() {
			Dialog.alert(i18n.get("vmbuss_error"));
			flag = false;
		}
	});
	return flag;
}
// 改变退款申请页面的退款原因类型触发onchange事件
function changeRefundReasonType(){
	// 对退款原因类型的描述
	var refundReasonType_description = "";
	// 退款类型
	var refundReasonType = $("#refundReasonType").val();
	if(refundReasonType == "2"){
		refundReasonType_description = "丢包严重，时延大，网络抖动";
	}else if(refundReasonType == "3"){
		refundReasonType_description = "硬盘读写卡";
	}else if(refundReasonType == "5"){
		refundReasonType_description = "开通VM失败、丢失IP，扩展盘丢失";
	}else if(refundReasonType == "6"){
		refundReasonType_description = "经常无故重启、软件无法使用，无法访问外网，客户测试使用";
	}else if(refundReasonType == "7"){
		refundReasonType_description = "受到攻击";
	}
	$("#refundReasonType_description").html(" "+refundReasonType_description);
}
//正式云主机升级功能
function upgradeVM(vmNo,isEnable){
	if(isEnable==1){
		Dialog.alert("服务器用于非法用途-停止服务!");
		return;
	}
	var extDiskArray = new Array();
	var extDiskItemIdArray = new Array();
	var serviceItemIds = new Array();
	var periodArray = [1,3,6,12];
	var IPCount = 0;
	var extDisk = 0;
	var extDiskUsed=0;
	var cpuCoreG=0;
	var memCapacityG=0;
	var bandWidthG=0;
	var ipNumG=0;
	var referenceId=0;
	var effectiveDate=new Date();
	var expirationDate=new Date();
	var extDisksG=[];
	var demandSubmitData={
			vmNum:1,
			ipNum:0,
			zoneGroupId:1,
			buyPeriod:1,
			serviceItemIds:new Array(),
			CPU:1,
			memory:0,
			extDiskExtend:'',
			disk:new Array(),
			bandwidth:0
	};
	function getHostDetailInfo(vmNo) {
		$.ajax({
			url : "../../../ops/ops!findDetailVmById.action",
			type : "get",
			dataType : 'json',
			async : false,
			data : "id=" + vmNo,
			success : function(json) {
				if (json.success) {
					cpuCoreG=json.resultObject.cpu;
					memCapacityG=json.resultObject.memory;
					effectiveTime=json.resultObject.effectiveDate;
					expirationTime=json.resultObject.expireDate;
					extDisksG = json.resultObject.extdisks;// 扩展盘
					bandWidthG=json.resultObject.network;
					referenceId=json.resultObject.referenceId;
				} 
				else {
					Dialog.alert(json.resultMsg);
				}
			}
		});
	}
	function initBuyPage(){
		CPUStore.load();
		memoryStore.load();
		extDiskStore.load();
		netStore.load();
		//购买时长初始化
		//事件绑定
		$("#add_extDisk-btn").bind('click',addExtDisk);
		$("#add_IP-btn").bind('click',addIP);
		$("#reduce_IP-btn").bind('click',reduceIP);
		$("#amount_IP").val(0);
		$(".cpu-btn").bind('click',cpuSelect);
		$("#sc-count-text").val(1);
		$("#sc-count-text").focus(function(){
			$("#sc-count-text").val("");
		}).keydown(function(event){
			if(event.keyCode==13){
				event.keyCode=9;
			}			
		}).keypress(function(event){
			if((event.keyCode <47) || (event.keyCode > 58)){
				return false ;
			}
		}).blur(function(event){	
			if($("#sc-count-text").val()=="" || $("#sc-count-text").val()==null){
				$("#sc-count-text").val(1);
			}
			var value=new String($("#sc-count-text").val());
			if(value[0]==0){
				value=value.replace("0",""); 
				$("#sc-count-text").val(value);
			}
			demandSubmitData.vmNum=value;
		});
		$("#btn-count").bind('click',calculatePrice);
		
		
		//升级内容国际化
		$("#memory-title").text(i18n.get("memory-title"));// 内存：
		$("#add_extDisk-tip").text(i18n.get("Add a data disk"));//添加一块数据盘
		$("#net-title").text(i18n.get("bandwidthL"));//带宽：
		$("#AssignIP-tip").text(i18n.get("Assign a new IP"));//分配新IP
		$("#timer-title").text(i18n.get("buy-timer"));//购买时长：
		$("#price-label").text(i18n.get("Cost"));//费用：
		$("#price-unit-label").text(i18n.get("yuan"));//元
		$("#btn-count").text(i18n.get("Calculate"));//计算价格
		$("#price-tip-label").text(i18n.get("Depending on your configuration, auto quotes."));//(根据您的配置，自动报价)
		$("#btn-add").text(i18n.get("Buy"));//购买
	};

	//CPU数据请求
	var CPUStore={
			url:rootPath+'/sc/serviceItem!listServiceItem.action?',
			type:'GET',
			dataType:'json',
			data:{'serviceType':1,sort:'[{"property":"coreNum","direction":"ASC"}]'},
			contentType: 'application/json; charset=utf-8',
			load:function(){
				$.ajax({
					url : this.url,
					type: this.type,
					dataType:this.dataType,
					data:this.data,
					async : false,
					contentType: this.contentType,
					success:function(data,textStatus){
						if(data.success == true) {
							var result = data.resultObject;
							CPUStore.fillData(result);
						} else {
							Dialog.alert(data.resultMsg);
						}
					}
				});
			},
			fillData:function(result){
				//fields : [ 'id','serviceType','name','coreNum','status','model','frequency' ],		
				var CPUspan="";
				if(result!=null && result.length>0){
					$.each(result, function(index, value) {
						if(value['coreNum']>=cpuCoreG){
							CPUspan +="<span id='"+value['id']+"'core='"+value['coreNum']+"' class='cpu-btn'>"+value['coreNum']+i18n.get('core')+"</span>";
						}
					});	
					$("#cpu-label").after(CPUspan);	
					$(".cpu-btn").bind('click',cpuSelect);
					$(".cpu-btn")[0].click();
				}
			}
	};
	
	//memory数据请求
	var memoryStore={
			url:rootPath+'/sc/serviceItem!listServiceItem.action?',
			type:'GET',
			dataType:'json',
			data:{serviceType:2,sort:'[{"property":"size","direction":"ASC"}]'},//,sort:[{property:"size",direction:"ASC"}]
			contentType: 'application/json; charset=utf-8',
			load:function(){
				$.ajax({
					url : this.url,
					type: this.type,
					dataType:this.dataType,
					data:this.data,
					async : false,
					contentType: this.contentType,
					success:function(data,textStatus){
						if(data.success == true) {
							var result = data.resultObject;
							memoryStore.fillData(result);
						} else {
							Dialog.alert(data.resultMsg);
						}
					}
				});
			},
			fillData:function(result){
				//fields : [ 'id','serviceType','size','model','status' ],
				if(result!=null && result.length>0){
					//内存
					var index=0;
					for(var i=0;i<result.length;i++){
						if(result[i].size<memCapacityG){
							++index;
						}
					}
					result.splice(0,index);
					$( "#slider-mem" ).slider({
						range: "min",
						step: 1,
						min: 1,
						max: result.length,
						slide: function( event, ui ) {
							$( "#amount_mem" ).val(result[ui.value-1].size );
							$("#mem_itemid").val(result[ui.value-1].id);
						}
					});
					$( "#amount_mem" ).val(result[0].size);		
					$("#mem_itemid").val(result[0].id);
				}
			}
	};
	//extDisk数据请求
	var extDiskStore={
			url:rootPath+'/sc/serviceItem!listServiceItem.action?',
			type:'GET',
			dataType:'json',
			data:{'serviceType':8,sort:'[{"property":"capacity","direction":"ASC"}]'},
			contentType: 'application/json; charset=utf-8',
			load:function(){
				$.ajax({
					url : this.url,
					type: this.type,
					dataType:this.dataType,
					data:this.data,
					async : false,
					contentType: this.contentType,
					success:function(data,textStatus){
						if(data.success == true) {
							var result = data.resultObject;
							extDiskArray=copyArray(result);
							if(extDisksG&&extDisksG.length>0){
								for(var i=0 ;i<extDisksG.length;i++){
									var localDiskArray=copyArray(extDiskArray);
									var diskSize=extDisksG[i].ed_capacity;
									var diskId=extDisksG[i].volumnId;
									var index=0;
									for(var j=0;j<localDiskArray.length;j++){
										if(localDiskArray[j].capacity<diskSize){
											++index;
										}
									}
									localDiskArray.splice(0,index);
									displayUsedExtDisk(localDiskArray,diskId);
								}
							}
							extDiskStore.fillData(result);
						} else {
							Dialog.alert(data.resultMsg);
						}
					}
				});
			},
			fillData:function(result){
				//fields : [ 'id','serviceType','capacity','model','status' ],
				if(result!=null && result.length>0){
					var extId_amount = '#ext_'+extDisk+' .amount_extdisk';
					var extId_item = '#ext_'+extDisk+' .extdisk_itemid';
					$(".slider-extdisk" ).slider({
						range: "min",
						step: 1,
						min: 1,
						max: result.length,
						slide: function( event, ui ) {
							extId_amount = '#ext_'+$(this).attr("id")+" .amount_extdisk";
							extId_item = '#ext_'+$(this).attr("id")+" .extdisk_itemid";
							$(extId_amount).val(result[ui.value-1].capacity );
							$(extId_item).val(result[ui.value-1].id);
						}
					});
					$(extId_amount).val(result[0].capacity);		
					$(extId_item).val(result[0].id);				
				}
			}
	};
	//net数据请求
	var netStore={
			url:rootPath+'/sc/serviceItem!listServiceItem.action?',
			type:'GET',
			dataType:'json',
			data:{'serviceType':5,sort:'[{"property":"bandWidth","direction":"ASC"}]'},
			contentType: 'application/json; charset=utf-8',
			load:function(){
				$.ajax({
					url : this.url,
					type: this.type,
					dataType:this.dataType,
					data:this.data,
					async : false,
					contentType: this.contentType,
					success:function(data,textStatus){
						if(data.success == true) {
							var result = data.resultObject;
							netStore.fillData(result);
						} else {
							Dialog.alert(data.resultMsg);
						}
					}
				});
			},
			fillData:function(result){
				//fields : [ 'id','serviceType','bandWidth','type','status' ],
				if(result!=null && result.length>0){
					var index=0;
					for(var i=0;i<result.length;i++){
						if(result[i].bandWidth<bandWidthG){
							++index;
						}
					}
					result.splice(0,index);
					$( "#slider-net" ).slider({
						range: "min",
						step: 1,
						min: 1,
						max: result.length,
						slide: function( event, ui ) {
							$( "#amount_net" ).val(result[ui.value-1].bandWidth );
							$("#net_itemid").val(result[ui.value-1].id);
						}
					});
					$( "#amount_net" ).val(result[0].bandWidth);		
					$("#net_itemid").val(result[0].id);
				}
			}
	};
	//CPU选择
	function cpuSelect(){
		$(".cpu-btn").removeClass("cpu-selected-btn");
		$(this).addClass("cpu-selected-btn");
	};
	//添加扩展盘
	function addExtDisk(){
		if(extDisk<4){
			extDisk +=1;
		}else{
			Dialog.alert(i18n.get("addExtDiskTip"));//至多添加4块扩展盘
			return;
		}	
		var divStr = '<div style="float:left;width:100%;margin-top:10px;overflow:auto;">'+
						'<div class="damand-label">'+i18n.get("dataDiskL")+'</div>'+
	                    '<div id="'+extDisk+'" class="slider-div slider-extdisk">'+
	                    '    <span class="ui-slider-handle ui-state-default ui-corner-all " style=" width:18px; height:24px; "></span>'+
	                    '</div>'+
	                    '<div id="ext_'+extDisk+'" style="float:left;">'+
	                    '	<input class="extdisk_itemid"  type="hidden" />'+
	                    '	<input  disabled="disabled" class="slider-input amount_extdisk"/>'+
	                    '</div>'+
	                    '<div class="cn-2" style=" float:left; margin-top:5px; margin-left:5px;">GB</div>'+
					'</div>';
		$("#add_extDisk-btn").parent().after(divStr);
		extDiskStore.fillData(extDiskArray);
	};

	function displayUsedExtDisk(result,id){
		if(extDisk<4){
			extDisk +=1;
			extDiskUsed+=1;
		}else{
			Dialog.alert(i18n.get("addExtDiskTip"));//至多添加4块扩展盘
			return;
		}	
		var divStr = '<div style="float:left;width:100%;margin-top:10px;overflow:auto;">'+
						'<div class="damand-label">'+i18n.get("dataDiskL")+'</div>'+
	                    '<div id="'+extDisk+'" class="slider-div slider-extdisk'+extDiskUsed+'">'+
	                    '    <span class="ui-slider-handle ui-state-default ui-corner-all " style=" width:18px; height:24px; "></span>'+
	                    '</div>'+
	                    '<div id="ext_'+extDisk+'" style="float:left;">'+
	                    '	<input class="extdisk_itemid_used" type="hidden"/>'+
	                    '	<input class="slider-input amount_extdisk_used"/>'+
	                    '</div>'+
	                    '<div class="cn-2" style=" float:left; margin-top:5px; margin-left:5px;">GB</div>'+
					'</div>';
		$("#add_extDisk-btn").parent().after(divStr);
		
		if(result!=null && result.length>0){
			var extId_amount = '#ext_'+extDisk+' .amount_extdisk_used';
			var extId_item = '#ext_'+extDisk+' .extdisk_itemid_used';
			$(".slider-extdisk"+extDiskUsed ).slider({
				range: "min",
				step: 1,
				min: 1,
				max: result.length,
				slide: function( event, ui ) {
					extId_amount = '#ext_'+$(this).attr("id")+" .amount_extdisk_used";
					extId_item = '#ext_'+$(this).attr("id")+" .extdisk_itemid_used";
					$(extId_amount).val(result[ui.value-1].capacity );
					$(extId_item).val(result[ui.value-1].id);
					$(extId_item).attr("disk_id",id);
				}
			});
			$(extId_amount).val(result[0].capacity);	
			$(extId_item).val(result[0].id);
			$(extId_item).attr("disk_id",id);
			$(extId_item).attr("original_disk",result[0].id);
			$(extId_item).attr("original_disk_size",result[0].capacity);
		}
	}

	function resizeExtDisk(){
		
	}
	//添加IP
	function addIP(){
		IPCount+=1;
		$("#amount_IP").val(IPCount);
		var $events = $("#reduce_IP-btn").data("events");
		if(($events && $events["click"].length===0) ){
			$("#reduce_IP-btn").bind('click',reduceIP);
		}
	};
	//
	function reduceIP(){
		if(IPCount<=0){
			return;
		}
		IPCount=parseInt($("#amount_IP").val())-1;
		$("#amount_IP").val(IPCount);
	};
	//计算价格
	function calculatePrice(){
		var zoneId=$("#zone-select").find('option:selected').val();
		var osId=$("#os-select").find('option:selected').val();
		var period =$("#timer-select").find('option:selected').val();
		if(zoneId<=0){
			Dialog.alert(i18n.get("Please select Zone-Group!"));
			$("#zone-select").focus();
			return;
		}
		if(osId<=0){
			Dialog.alert(i18n.get("Please select OS!"));
			$("#os-select").focus();
			return;
		}
		//CPU
		var cpuCore = $(".cpu-selected-btn").text();
		if(cpuCore != undefined){
			demandSubmitData.CPU=parseInt(cpuCore);
		}
		//内存
		var memory = $("#amount_mem").val();
		if(memory != undefined){
			demandSubmitData.memory=memory;
		}	
		//数据盘
		for(var i=0;i<$(".amount_extdisk").length;i++){
			demandSubmitData.disk.push($(".amount_extdisk")[i].value);
		}
		//带宽
		var bandwidth = $("#amount_net").val();
		if(bandwidth != undefined){
			demandSubmitData.bandwidth=bandwidth;
		}	
		//数据中心
		demandSubmitData.zoneGroupId = zoneId;
		//IP
		var ipCount = $("#amount_IP").val();
		if(ipCount != undefined){
			demandSubmitData.ipNum = ipCount;
		}	
		//购买时长
		if(period != undefined){
			demandSubmitData.buyPeriod = period;
		}
		var unUsedDaysInt=unUsedDays(new Date().getTime(),expirationTime);
		$.ajax({
			url : getPriceRequestURL(),
			type : 'GET',
			dataType : 'jsonp',
			jsonp:'callback',
			jsonpCallback:'requestData',
			contentType: 'application/json; charset=utf-8',
			async : false,
//			data : {
//				planId:planId,//config.js中配置
//				domainCode:domainCode,//config.js中配置
//				CPU:demandSubmitData.CPU,
//				memory:demandSubmitData.memory,
//				extDisk:demandSubmitData.disk.join(","),
//				bandwidth:demandSubmitData.bandwidth,
//				payMonth:demandSubmitData.buyPeriod,
//				IP:demandSubmitData.ipNum
//			},
			success : function(data) {			
				if(data == undefined){
					Dialog.alert("vmbuss_error");
				}else{	
					var totalPrice =rounding(floatMulti(unUsedDaysInt,parseFloat(data)));
					$("#price").text(totalPrice);
				}
			}
		});
	};
	
	function unUsedDays(effectiveTime,expirationTime){
		var diff = expirationTime - effectiveTime;
		var oneDayTime = 24 * 60 * 60 * 1000;
		if (diff % oneDayTime == 0) {
			days = diff / (oneDayTime);
		} else {
			days = parseInt(diff/oneDayTime)+1;
		}
		return days;
	}
	
	function getPriceRequestURL(){
		var requestURL=billingServer+"calculatePrice?";
		var selectCoreNum=$(".cpu-selected-btn").attr("core");
    	if(selectCoreNum>cpuCoreG){
    		requestURL+=("CPU="+(selectCoreNum-cpuCoreG)+"&");
    	}
    	//内存
    	var selectMem=$("#amount_mem").val();
    	if(parseInt(selectMem)>parseInt(memCapacityG)){
    		requestURL+=("memory="+(selectMem-memCapacityG)+"&");
    	}	
    	//新添加的数据盘
    	var addDisk=[];
    	for(var i=0;i<$(".extdisk_itemid").length;i++){
    		addDisk.push($($(".extdisk_itemid")[i]).next().val());
    	}
    	//原扩展盘扩容
    	for(var i=0;i<$(".extdisk_itemid_used").length;i++){
    		var extendedDiskId=$(".extdisk_itemid_used")[i].value;
    		var extenddeDiskSize=$($(".extdisk_itemid_used")[i]).next().val();
    		var originalDiskId=$($(".extdisk_itemid_used")[i]).attr("original_disk");
    		var originalDiskSize=$($(".extdisk_itemid_used")[i]).attr("original_disk_size");
    		if(extendedDiskId!=originalDiskId){
    			addDisk.push(extenddeDiskSize-originalDiskSize);
    		}
    	}
    	if(addDisk.length>0){
    		requestURL+=("extDisk="+(addDisk.join(","))+"&");
    	}
    	//带宽
    	var selectNetwork=$("#amount_net").val();
    	if(parseInt(bandWidthG)<parseInt(selectNetwork)){
    		requestURL+=("bandwidth="+(selectNetwork-bandWidthG)+"&");
    	}	
    	
    	//IP
    	var ipCount = $("#amount_IP").val();
    	if(ipCount != undefined&&ipCount>0){
    		requestURL+=("IP="+ipCount+"&");
    	}
    	requestURL+=("domainCode="+domainCode+"&");
    	requestURL+=("brandCode="+brandCode+"&");
    	requestURL+=("zoneGroupCode="+getZoneGroupCode(referenceId)+"&");
    	requestURL+=("payMonth="+1+"&");
    	requestURL+=("upgrade=1&");
    	requestURL+=("planId="+1319089922);
    	return requestURL;
	}

	
	var dialog = new Dialog("dialog");
    dialog.Width = 850;
    dialog.Height = 350;
    dialog.Title = i18n.get('vmbuss_upgrade');
    dialog.innerHTML=getShowHtml();
    dialog.OKEvent = function() {
    	//CPU
    	var submitFlag=false;
    	var itemId_cpu = $(".cpu-selected-btn").attr("id");
    	var selectCoreNum=$(".cpu-selected-btn").attr("core");
    	if(itemId_cpu != undefined&&parseInt(selectCoreNum)>parseInt(cpuCoreG)){
    		submitFlag=true;
    		serviceItemIds.push(itemId_cpu);
    	}
    	//内存
    	var itemId_memory = $("#mem_itemid").val();
    	var selectMem=$("#amount_mem").val();
    	
    	if(itemId_memory != undefined&&parseInt(selectMem)>parseInt(memCapacityG)){
    		submitFlag=true;
    		serviceItemIds.push(itemId_memory);
    	}	
    	//新添加的数据盘
    	for(var i=0;i<$(".extdisk_itemid").length;i++){
    		submitFlag=true;
    		extDiskItemIdArray.push($(".extdisk_itemid")[i].value);
    		serviceItemIds.push($(".extdisk_itemid")[i].value);
    	}
    	//原扩展盘扩容
    	var extDiskExtend='';
    	for(var i=0;i<$(".extdisk_itemid_used").length;i++){
    		var extendedDiskId=$(".extdisk_itemid_used")[i].value;
    		var originalDiskId=$($(".extdisk_itemid_used")[i]).attr("original_disk");
    		var volumnId=$($(".extdisk_itemid_used")[i]).attr("disk_id");
    		if(extendedDiskId!=originalDiskId){
    			extDiskExtend+=(volumnId+'#'+extendedDiskId+";");
    			
    		}
    	}
    	if(extDiskExtend){
    		submitFlag=true;
    		var extDisksStrL=extDiskExtend.length;
    		demandSubmitData.extDiskExtend=extDiskExtend.substring(0,extDisksStrL-1);
    	}
    	//带宽
    	var itemId_net = $("#net_itemid").val();
    	var selectNetwork=$("#amount_net").val();
    	if(itemId_net != undefined&&parseInt(bandWidthG)<parseInt(selectNetwork)){
    		submitFlag=true;
    		serviceItemIds.push(itemId_net);
    	}	
    	//IP
    	var ipCount = $("#amount_IP").val();
    	if(ipCount != undefined&&ipCount>0){
    		submitFlag=true;
    		demandSubmitData.ipNum = ipCount;
    	}	
    	if(!submitFlag){
    		Dialog.alert("配置没有发生变化，无法进行升级操作。");
    		return;
    	}
    	demandSubmitData.serviceItemIds = serviceItemIds;
    	$.ajax({
    		url : rootPath+"/order/order!upgradeVM.action",
    		type : 'POST',
    		dataType : 'json',
    		async : false,
    		traditional: true,
    		data : {
    			"submitData.ipNum":demandSubmitData.ipNum,
    			"submitData.serviceItemIds":demandSubmitData.serviceItemIds,
    			"submitData.extDiskExtend":demandSubmitData.extDiskExtend,
    			"submitData.planId":1319089922,
    			"vmId":vmNo
    		},
    		success : function(data) {
    			if(!data['success']){
    				Dialog.alert(data.resultMsg);
    			}
    			dialog.close();
    		}
    	});
    	
    }
    dialog.show();
    getHostDetailInfo(vmNo);
    initBuyPage();
}


function getShowHtml(){
	var upgradeDivStr=
		' <div id="upgradeDiv">                                              	  '+
		'	<div style="float: left; width: 100%; margin-top: 20px;">					  '+
		'		<span id="cpu-label" class="damand-label">CPU：</span>					  '+
		'	</div>												  '+
		'	<div style="float: left; width: 100%; margin-top: 20px;">					  '+
		'		<div id="memory-title" class="damand-label">						  '+
		'			<!-- 内存： -->									  '+
		'		</div>											  '+
		'		<div id="slider-mem" class="slider-div">						  '+
		'			<span class="ui-slider-handle ui-state-default ui-corner-all "			  '+
		'				style="width: 18px; height: 24px;"></span>				  '+
		'		</div>											  '+
		'		<div style="float: left;">								  '+
		'			<input id="mem_itemid" type="hidden" /> <input disabled="disabled"		  '+
		'				id="amount_mem" class="slider-input" />					  '+
		'		</div>											  '+
		'		<div class="cn-2"									  '+
		'			style="float: left; margin-top: 5px; margin-left: 5px;">MB</div>		  '+
		'	</div>												  '+
		'	<div												  '+
		'		style="float: left; margin-left: 160px; width: 100%; margin-top: 20px;">		  '+
		'		<span id="add_extDisk-btn" class="add-btn">+</span> <span				  '+
		'			id="add_extDisk-tip"								  '+
		'			style="font-size: 14px; float:left;font-family: 微软雅黑; margin-left: 5px;">	  '+
		'			<!-- 添加一块数据盘 -->								  '+
		'		</span>											  '+
		'	</div>												  '+
		'	<div style="float: left; margin-top: 20px;">							  '+
		'		<div id="net-title" class="damand-label">						  '+
		'			<!-- 带宽： -->									  '+
		'		</div>											  '+
		'		<div id="slider-net" class="slider-div">						  '+
		'			<span class="ui-slider-handle ui-state-default ui-corner-all "			  '+
		'				style="width: 18px; height: 24px;"></span>				  '+
		'		</div>											  '+
		'		<div style="float: left;">								  '+
		'			<input id="net_itemid" type="hidden" /> <input disabled="disabled"		  '+
		'				id="amount_net" class="slider-input" />					  '+
		'		</div>											  '+
		'		<div class="cn-2"									  '+
		'			style="float: left; margin-top: 5px; margin-left: 5px;">Mbps</div>		  '+
		'	</div>												  '+
		'	<div												  '+
		'		style="float: left; width: 100%; margin-top: 20px; margin-left: 160px;">		  '+
		'		<span id="AssignIP-tip"									  '+
		'			style="font-size: 14px; float:left; font-family: 微软雅黑; margin-left: 5px;">	  '+
		'			<!-- 分配新IP -->								  '+
		'		</span> <span id="reduce_IP-btn" class="add-btn">-</span> <input			  '+
		'			disabled="disabled" id="amount_IP" class="slider-input"				  '+
		'			style="margin-left: 0px;" /> <span id="add_IP-btn" class="add-btn">+</span>	  '+
		'	</div>												  '+
		'	<div class="cost-div">										  '+
		'		<span id="btn-count"									  '+
		'			class="button button-rounded button-action button-small btn-count">计算价格</span>'+
		'		<span id="price-label">'+
		'			<!-- 费用： -->'+
		'		</span> <span id="price">0.0</span> <span id="price-unit-label"></span> <span'+
		'			id="price-tip-label">'+
		'			<!-- 元/小时 -->'+
		'		</span>'+
		'	</div>'+
		'	</div>'
		return upgradeDivStr;
}

function getRenew4DemandDisplayHtml(){
	var optionStr='<option value="1">'+1+i18n.get("timeDesc")+'</option>'+
	'<option value="3">'+3+i18n.get("timeDesc")+'</option>'+
	'<option value="6">'+6+i18n.get("timeDesc")+'</option>'+
	'<option value="12">'+12+i18n.get("timeDesc")+'</option>';
	var renewForDemandStr='<div id="regularDIV" style="width: 70%; display: none">                                       '  
		'		<table cellpadding="10" cellspacing="0" width="100%" height="100"	       '
		'            style="font-size: 12px" class="renewFeeTable" >				       '
		'            <tr height="10px">								       '
		'                <td colspan="2">&nbsp;							       '
		'                </td>									       '
		'            </tr>									       '
		'            <tr height="15px" >							       '
		'              <td width="60px" class="renewFeeTdLeft" id="regularFeeTypeTd">付费时长:	       '
		'              </td>									       '
		'              <td>									       '
		'                  <select id="feeType" style="width: 80px" onchange="changeFee(this)">'+optionStr+'</select>'
		'              </td>									       '
		'            </tr>									       '
		'            <tr >									       '
		'              <td class="renewFeeTdLeft" id="accountBalanceTd">账户余额:		       '
		'              </td>									       '
		'              <td id="balanceId">							       '
		'              </td>									       '
		'            </tr>									       '
		'            <tr id="couponTr">								       '
		'              <td class="renewFeeTdLeft" id="couponBalanceTd">返点余额:		       '
		'              </td>									       '
		'              <td id="couponAccountId">						       '
		'              </td>									       '
		'            </tr>									       '
		'            <tr>									       '
		'              <td class="renewFeeTdLeft" id="renewFeeAmountTd">续费金额:		       '
		'              </td>									       '
		'              <td id="feePrice" align="left">							       '
		'              </td>									       '
		'            </tr>									       '
		'        </table>									       '
		'	</div>	'
	
	return renewForDemandStr;
};
function getLastDayOfMonth(){
	var dateNow = new Date();
	var new_year = dateNow.getFullYear();
	var month = dateNow.getMonth()+1
	var new_month = month++;
	var new_date = dateNow.getDate();
	if(month>12){
		alert("month>12")
		new_month -=12;
		new_year++;
	}
	var renew_date=new Date(new_year,new_month,1);
	var lastDayOfMonth = (new Date(renew_date.getTime()-1000*60*60*24)).getDate();
	var lastDayOfMonth2 = lastDayOfMonth-27;
	if(new_date == lastDayOfMonth){
		Dialog.alert(i18n.get("stopBusiness"));
		return false;
	}else{
		return true;
	}
};


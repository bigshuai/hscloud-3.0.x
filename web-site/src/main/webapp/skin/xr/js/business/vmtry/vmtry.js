var path="../../../";
var pageObje = new PageObje('tableId');
var balance = 0; //账户余额
var couponAccount = 0; //账户返点数
var giftAccount = 0;//账户礼金数
var couponsRebateRate = 0; //返点率
var giftsRebateRate = 0; //礼金折扣
var maxCoupon = 0; //最多返点
var maxGift = 0; //最多礼金
var currentStatus="";
var tempFeeType = ''; //临时缴费类型
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initDialog();
	initCss('business/vmtry/vmtry.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	i18nPage();//国际化当前页面
    tryVMQuery();
	getBalance();
	initQueryHtml();
});

function i18nPage(){
	$("#vmbuss_name").text(i18n.get("vmbuss_name"));
	$("#vmbuss_ip").text(i18n.get("vmbuss_ip"));
	$("#vmbuss_open").text(i18n.get("vmbuss_open"));
	$("#vmbuss_expire").text(i18n.get("vmbuss_expire"));
	$("#vmbuss_status").text(i18n.get("vmbuss_status"));
	$("#allOperations").text(i18n.get("allOperations"));
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
    var comments='';
    var tryCreateTime=record["createTime"];
    var tryExpireTime=record["expireTime"];
    var TryTime=(tryExpireTime-tryCreateTime)/(24*3600*1000);
	if(record["comments"]){
		comments=record["comments"];
	}
	if("TRYWAIT"==record["status_buss"]){
		renderStr+='<span class="button button-action button-rounded button-small button_margin_center" onclick="cancelVM('
			+record["referenceId"]+')">'+i18n.get('Cancel')+'</span>';
	}else if("CANCEL"==record["status_buss"]){
		renderStr+='<span>&nbsp;</span>'+
		'<span">&nbsp;</span>';
	}else if("DELAYWAIT"==record["status_buss"]){
		renderStr+='<span class="button button-rounded button-small button_margin_left">'
			+i18n.get("vmbuss_delay") +'</span>'+
		'<span class="button button-rounded button-primary button-small button_margin_left" onclick="showApplyNormal('
			+record["referenceId"]+','+record["scId"]+','+record["isEnable"]+')">'+i18n.get("vmbuss_regular")+'</span>';
	}else if("DELAY"==record["status_buss"] || "EXPIRE_DELAY"==record["status_buss"]){
		renderStr+='<span class="button button-rounded button-primary button-small button_margin_left" onclick="showApplyNormal('
			+record["referenceId"]+','+record["scId"]+','+record["isEnable"]+')">'+i18n.get("vmbuss_regular")+'</span>';
	}else if(TryTime>=30){
		renderStr+='<span class="button button-rounded button-small button_margin_left">'
			+i18n.get("vmbuss_delay") +'</span>'+
		'<span class="button button-rounded button-primary button-small button_margin_left" onclick="showApplyNormal('
			+record["referenceId"]+','+record["scId"]+','+record["isEnable"]+')"'+' title="'+comments+'">'+i18n.get("vmbuss_regular")+'</span>';
	}else{
		renderStr+='<span class="button button-action button-rounded button-small button_margin_left" onclick="applyDefer('
			+record["referenceId"]+','+record["isEnable"]+')">'+i18n.get("vmbuss_delay") +'</span>'+
		'<span class="button button-rounded button-primary button-small button_margin_left" onclick="showApplyNormal('
			+record["referenceId"]+','+record["scId"]+','+record["isEnable"]+')"'+' title="'+comments+'">'+i18n.get("vmbuss_regular")+'</span>';
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
function showApplyNormal(referenceId,scId,isEnable){

	//if(0!=isEnable){
	//	customizeAlert("服务器用于非法用途-停止服务!");
	//	return;
	//}
	$("#regularFeeTypeTd").text(i18n.get("vmbuss_regularFeeType"));
	$("#accountBalanceTd").text(i18n.get("vmbuss_accountBalance"));
	$("#couponBalanceTd").text(i18n.get("vmbuss_couponBalance"));
	$("#giftBalanceTd").text(i18n.get("vmbuss_giftBalance"));
	$("#renewFeeAmountTd").text(i18n.get("vmbuss_regularAmount"));
	if(getVMFeeType(scId)){
		if(dialog != null) {
			$("#_DialogDiv_dialog").show();
	    	$("#_DialogBGDiv").show();
	    	return;
		}
		
		dialog = new Dialog("dialog");
	    dialog.Width = 600;
	    dialog.Height = 250;
	    dialog.Title = i18n.get('try-to-normal');
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
	    	var giftAmount = $('#giftAmount').val();
	    	//是否使用返点
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
	    		
	    	var url=path+"ops/ops!regularTryVm.action";
	    	var param ={
	    			"referenceId":referenceId,
	    			"feeTypeId":feeTypeId,
	    			"couponAmount":couponAmount,
	    			"giftAmount":giftAmount
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
	    			}
	    			dialog.close();
	    		}
	    	});
		};
	    dialog.show();
	    $("#feeType").val(tempFeeType);
	}
}

function applyNormal(){
 
	var referenceId = $("#referenceId").val();
	var id_price = $("#feeType").val();
	var feeTypeId = id_price.split("@")[0];	
	var couponAmount = $('#couponAmount').val();
	var giftAmount = $('#giftAmount').val();
	//是否使用返点
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
		
	var url=path+"ops/ops!regularTryVm.action";
	var param ={
			"referenceId":referenceId,
			"feeTypeId":feeTypeId,
			"couponAmount":couponAmount,
			"giftAmount":giftAmount
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
			}
			$('#regularDIV').dialog('close');
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
				if(usePointOrNot != true|| couponAccount <= 0 ){
					$("#couponTr").hide();
					$("#doUseCoupon")[0].checked = false;
				} else {
					$("#couponTr").show();
					$("#doUseCoupon")[0].checked = true;
				}
				
				giftChange(feePrice);
				var useGiftOrNot = json.resultObject[json.resultObject.length-1].useGiftOrNot;
				if(useGiftOrNot != true || giftAccount <= 0) {
					$("#giftTr").hide();
					$("#doUseGift")[0].checked = false;
				}  else {
					$("#giftTr").show();
					$("#doUseGift")[0].checked = true;
				}
			}
		},
		error : function() {
			Dialog.alert(lang.vpdc.center.error);
			bl = false;
		}
	});
	return bl;
}

function getBalance(){
	//账户余额
	var requstUrl = path+"bss/account!getAccountBalance.action";
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

function applyDefer(referenceId,isEnable){
	if(1==isEnable){
		Dialog.alert("服务器被手动禁用，禁止操作!");
		return;
	}
	var param ={
			"referenceId":referenceId
	};
	var url=path+"ops/ops!applyForDelayTryVm.action";	
	$.ajax({
		url : url,
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
	text = text.replace('xxinputxx', '<input class="couponInput" type="text" id="couponAmount" value="' + value + '"/>');
	
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
	text = text.replace('xxinputxx', '<input class="couponInput" type="text" id="giftAmount" value="' + value + '"/>');
	
	var checkboxText = '<input type="checkbox" id="doUseGift" checked=true/>';
	var couponAccountText = "<div style=\"width: 80px;float:left;\">"+giftAccount + i18n.get('account_yuan') + "</div>"+
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + 
		checkboxText + i18n.get('vmbuss_useGift') + 
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + text;
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
							 ['status_buss','statusRender'],
							 ['referenceId','operationRender']
						   ];
	pageObje.column = [ 'name','ip','status_buss','referenceId','expireTime','createTime'];		
	pageObje.jsonRequest = {
		"type":0,
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
			"type":0,
			"page" : pageObje.current,
			"limit" : pageObje.size,
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

function initQueryHtml(){
	var statusStr="<a value=''>"+i18n.get("vmbuss_all")+"</a>"+
	"<a value='0'>"+i18n.get("vmbuss_TryWait")+"</a>"+
	"<a value='1'>"+i18n.get("vmbuss_Try")+"</a>"+
	"<a value='2'>"+i18n.get("vmbuss_DelayWait")+"</a>"+
	"<a value='3'>"+i18n.get("vmbuss_Delay")+"</a>"+
	"<a value='5'>"+i18n.get("vmbuss_Cancel")+"</a>";
	$(".vm_status").append(statusStr);
	$(".vm_status a").click(function(){
		$("a[class='font_bold']").removeClass("font_bold");
		$(this).addClass("font_bold");
		var vmStatus=$(this).attr("value");
		currentStatus=vmStatus;
		query();
	});
	var optionStr='<option value="" select="select">'+i18n.get("vmbuss_all")+'</option>'+
		'<option value="ipOuter" select="select">'+i18n.get("outerip")+'</option>'+
		'<option value="ipInner" select="select">'+i18n.get("innerip")+'</option>'+
		'<option value="vmName" select="select">'+i18n.get("vmbuss_name")+'</option>';
    $("#queryType").append(optionStr);
}

//乘法
function floatMulti(arg1,arg2){

   var precision1=getPrecision(arg1);
   var precision2=getPrecision(arg2);
   var tempPrecision=0;
   
   tempPrecision+=precision1;
   tempPrecision+=precision2;
   var int1=getIntFromFloat(arg1);
   var int2=getIntFromFloat(arg2);
   
   var result = int1 * int2 + '';
   if(result.length > tempPrecision) {
	   var length = result.length - tempPrecision;
	   result = result.substring(0, length) + '.' + result.substring(length);
   } else {
	   var temp = '0.';
	   for(var i = result.length; i < tempPrecision; i++) {
		   temp = temp + '0';
		}
	   temp = temp + result;
	   result = temp;
   }
   return parseFloat(result); 
}

function changeFee(obj){
	var id_price = obj.value;
	var price = id_price.split("@")[1];
	$("#feePrice").html(price+i18n.get("yuan"));
	couponChange(price);
	giftChange(price);
}
//加法
function floatAdd(arg1,arg2){
   var precision1=getPrecision(arg1);
   var precision2=getPrecision(arg2);
   var temp=Math.pow(10,Math.max(precision1,precision2));
   
   var result = (floatMulti(arg1,temp)+floatMulti(arg2,temp)) + '';
   var tempPrecision = temp.toString().length - 1;
   if(result.length > tempPrecision) {
	   var length = result.length - tempPrecision;
	   result = result.substring(0, length) + '.' + result.substring(length);
   } else {
	   var temp = '0.';
	   for(var i = result.length; i < tempPrecision; i++) {
		   temp = temp + '0';
		}
	   temp = temp + result;
	   result = temp;
   }
   return parseFloat(result); 
}

function getPrecision(arg){
   if(arg.toString().indexOf(".")==-1){
      return 0;
   }else{
      return arg.toString().split(".")[1].length;
   }
   
}

function getIntFromFloat(arg){
   if(arg.toString().indexOf(".")==-1){
      return arg;
   }else{
      return Number(arg.toString().replace(".",""));
   }
   
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
	var floatValue = param.toString().split(".")[1];
	if(floatValue != null && floatValue.length >= 3) {
		var char = floatValue.charAt(2);
		if(char > 4) {
			floatValue = floatAdd(floatValue.substring(0, 2), 1);
		}
		return param.toString().split(".")[0] + '.' + floatValue;
	}
	return param;
}


function goToCharge(){
	window.location.href="../../account/account/account.html";
}


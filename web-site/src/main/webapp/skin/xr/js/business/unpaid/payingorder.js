var payingOrderID = $.cookie("orderID");
var payingOrderType = $.cookie("orderType");
var couponAccount=0; //账户返点
var giftAccount=0; //账户礼金
var maxCoupon = 0; //最多返点
var maxGift = 0; //最多礼金
var orderRMB = 0;

$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/unpaid/payingorder.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	i18nPage();//国际化当前页面
	initDialog();
	gotoRunning();
	orderDetail();
});

function i18nPage(){
   
	$("#yuan").html(i18n.get("account_yuan"));
	$("#payImagId").html(i18n.get("operation_pay"));
	$("#s_orderNo").text(i18n.get("orderNo"));
	$("#s_dealtime").text(i18n.get("order-Time"));
	$("#account_offlineBank").text(i18n.get("account_offlineBank"));
	$("#account_offlineCardId").text(i18n.get("account_offlineCardId"));
	$("#account_offlineCompanyName").text(i18n.get("account_offlineCompanyName"));
	$("#account_online").text(i18n.get("account_online"));
	$("#account_platform").text(i18n.get("account_platform"));
	$("#account_offline").text(i18n.get("account_offline"));
	$("#comingsoon1").text(i18n.get("comingsoon"));
	$("#comingsoon2").text(i18n.get("comingsoon"));
	$("#account_offlinePayTitle").text(i18n.get("account_offlinePayTitle"));
	$("#account_offlinePayDesc").text(i18n.get("account_offlinePayDesc"));
	$("#payment").text(i18n.get("payment"));
	
}

function gotoRunning(){
	$.ajax({
		url : '../../../user_mgmt/user!getSessionUser.action',
		type: 'GET',
		dataType: 'json',
		contentType: "application/json; charset=utf-8",
		async:false,
		success: function(data){
			if(data.success == true) {
				$(".bal_user_content","#item1Content").html(i18n.get("currentaccount")+": "+data.resultObject.name);
			}
		}
	});
	
	$(".bal_val_content","#item1Content").html(i18n.get("account_balance"));
	$(".yuan","#item1Content").html(i18n.get("account_yuan"));
	
	$("#contentMoney").html($("#item1Content").html());
	 $(".item1").click(function() {
		orderDetail();
	 	$(".item1").addClass("checked");
	 	$("#contentMoney").html($("#item1Content").html());
	 	$(".item2").removeClass("checked");
	 	$(".item3").removeClass("checked");
	 	$(".item4").removeClass("checked");
	 });
	
	 $(".item2").click(function() {
		 $(".item2").addClass("checked");
		 	$("#contentMoney").html($("#item2Content").html());
		 	$(".item1").removeClass("checked");
		 	$(".item3").removeClass("checked");
		 	$(".item4").removeClass("checked");
	 });
	 
	 $(".item3").click(function() {
		 $(".item3").addClass("checked");
		 	$("#contentMoney").html($("#item3Content").html());
		 	$(".item2").removeClass("checked");
		 	$(".item1").removeClass("checked");
		 	$(".item4").removeClass("checked");
	 });
	 
	 $(".item4").click(function() {
		 $(".item4").addClass("checked");
		    getDomainOffLinePayment();
		 	$("#contentMoney").html($("#item4Content").html());
		 	$(".item2").removeClass("checked");
		 	$(".item3").removeClass("checked");
		 	$(".item1").removeClass("checked");
	 });
		
}

Date.prototype.pattern=function(fmt) {        
    var o = {        
    "M+" : this.getMonth()+1, //月份        
    "d+" : this.getDate(), //日        
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时        
    "H+" : this.getHours(), //小时        
    "m+" : this.getMinutes(), //分        
    "s+" : this.getSeconds(), //秒        
    "q+" : Math.floor((this.getMonth()+3)/3), //季度        
    "S" : this.getMilliseconds() //毫秒        
    };        
    var week = {        
    "0" : "\u65e5",        
    "1" : "\u4e00",        
    "2" : "\u4e8c",        
    "3" : "\u4e09",        
    "4" : "\u56db",        
    "5" : "\u4e94",        
    "6" : "\u516d"       
    };        
    if(/(y+)/.test(fmt)){        
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));        
    }        
    if(/(E+)/.test(fmt)){        
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);        
    }        
    for(var k in o){        
        if(new RegExp("("+ k +")").test(fmt)){        
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));        
        }        
    }        
    return fmt;        
};

//返点率转成小数
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

function orderDetail() {
	getBalance();
	
	var jsonRequest = {
		"order.id": payingOrderID,
		"orderType":payingOrderType
	};
	$.ajax({
		url : '../../../order/order!orderDetail.action',
		type : 'Post',
		dataType : 'json',
		data : jsonRequest,
		success : function(data) {
			if (data.success == true) {
				obj = data.resultObject;
				if(null != obj && "" != obj){
				$("#orderNo").text(obj[0].orderNo);
				$("#orderTime").text((new Date(parseInt(obj[0].createDate))).pattern("yyyy-MM-dd HH:mm:ss"));
				
				var detail = [];
				var temp = {};
				var couponValue = 0;
				var giftValue = 0;
				var couponFlag = false;
				var giftFlag = false;
				$(obj).each(function(i,it){
					//var key = it['serviceCatalogName']+'('+it['pricePeriod']+i18n.get("byMonth")+" ";
					var key = (1 == it["orderType"]?it['serviceCatalogName']:"")+'('+it['pricePeriod']+i18n.get("byMonth")+" ";
					if(key in temp){
						temp[key].push(it['price']);
					}else{
						temp[key] = [];
						temp[key].push(it['price']);
					}
			
					if(it['rebateRate'] != null) {
						
						var rebateRate = getRebateRateFloat(it['rebateRate']);
						if(it['usePointOrNot'] == true && rebateRate != 0) {
							var totalPrice = it['price'];
							var result = floatMulti(totalPrice, rebateRate);
							couponValue = floatAdd(couponValue, result);
							couponValue = rounding(couponValue);
							couponFlag = true;
						}
					}
					if(it['giftRebateRate'] != null) {
						var giftRebateRate =  getRebateRateFloat(it['giftRebateRate']);
						if(it['useGiftOrNot'] == true && giftRebateRate != 0) {
							var totalPrice = it['price'];
							var result = floatMulti(totalPrice, giftRebateRate);
							giftValue = floatAdd(giftValue, result);
							giftValue = rounding(giftValue);
							giftFlag = true;
						}
					}
				});
				
				//返点处理 start
				if(couponFlag == true && couponAccount > 0) {
					$('#doUseCouponText').text(i18n.get("vmbuss_useCoupon"));
					//var text = "您共有xxaccountxx点返点，此次最多使用xxmaxxx点返点，此次使用xxinputxx点返点";
					var text = i18n.get("useCoupondescription");
					text = text.replace('xxaccountxx', '<span class="couponIntCss">' + couponAccount + '</span>');
					text = text.replace('xxmaxxx', '<span class="couponIntCss">' + couponValue + '</span>');
					var result = 0;
					if(couponAccount > couponValue) {
						result = couponValue;
					} else {
						result = couponAccount;
					}
					result = rounding(result);
					
					maxCoupon = result;
					text = text.replace('xxinputxx', '<input class="couponInput" onblur="checkCoupon(this)" type="text" id="couponAmount" value="' + result + '"/>');
					$('#couponInfo').html(text);
					$('#couponP').show();
				}
				//返点处理 end
				//礼金处理 start
				if(giftFlag == true && giftAccount > 0) {
					$('#doUseGiftText').text(i18n.get("vmbuss_useGift"));
					//var text = "您共有xxaccountxx元礼金，此次最多使用xxmaxxx元礼金，此次使用xxinputxx元礼金";
					var text = i18n.get("useGiftDescription");
					text = text.replace('xxaccountxx', '<span class="couponIntCss">' + giftAccount + '</span>');
					text = text.replace('xxmaxxx', '<span class="couponIntCss">' + giftValue + '</span>');
					var result = 0;
					if(giftAccount > giftValue) {
						result = giftValue;
					} else {
						result = giftAccount;
					}
					result = rounding(result);
					maxGift = result;
					text = text.replace('xxinputxx', '<input class="couponInput" onblur="checkGift(this)" type="text" id="giftAmount" value="' + result + '"/>');
					$('#giftInfo').html(text);
					$('#giftP').show();
				}
				//礼金处理 end
				
				
				for(var i in temp){
					var temp2 = i + temp[i][0] + i18n.get("account_yuan") +") x " + temp[i].length +" = " + (temp[i][0] * temp[i].length) + i18n.get("account_yuan");
					detail.push(temp2);
				}
				
				var mes = detail.join("<br/>");
				$("#detailMes").html(mes);
				$("#totalPrice").text(obj[0].totalPrice);
				orderRMB = obj[0].totalPrice;
				
				var bal = $("#balance").text();
				var bill = $("#totalPrice").text();
				
				//余额与订单金额比较
				var totalAmount=parseFloat(bill);
				totalAmount=floatAdd(totalAmount,-(maxCoupon));
				totalAmount=floatAdd(totalAmount,-(maxGift));					}
				if(parseFloat(bal) >= totalAmount ){
					$("#payImagId").addClass("button_d");
				  	$(".promptMes").hide();
				  	$("#order_submit_1").attr("onclick","orderSubmit()");
				  	
				}else{
					$("#payImagId").addClass("button_g");
				  	$(".D_value").text((totalAmount-parseFloat(bal)).toFixed(2));
				}
			} else {
				Dialog.alert(data.resultMsg);
			}
		}
	});
}

function checkCoupon(coupon){
	var c = $(coupon).val();
	if(null == c || ""==c ){
		$(coupon).val(maxCoupon);
	}else{
		var reg = /^\d+(\.{0,1}\d+){0,1}$/;
		if (!reg.test($(coupon).val())) {
			$(coupon).val(maxCoupon);
		}else{
		  var uc=0;
		  if(maxCoupon<c){
			 uc=maxCoupon;
		  }else{
			 uc=c;
		  }
		  var g = $("#giftAmount").val();
		  var ug = 0;
		  if(maxGift<g){
			 ug = maxGift;
		  }else{
			 ug = g; 
		  }
		  var blance = orderRMB-ug;
		  if(blance<uc){
			  $("#couponAmount").val(blance>0?blance:0); 
		  }else{
			  $("#couponAmount").val(uc);  
		  }
		}
	}
}

function checkGift(gift){
	var reg = /^\d+(\.{0,1}\d+){0,1}$/;
	var g = $(gift).val();
	if(null == g || ""==g ){
		$(gift).val(maxGift);
	}else{
		var reg = /^\d+(\.{0,1}\d+){0,1}$/;
		if (!reg.test($(gift).val())) {
			$(gift).val(maxGift);
		}else{
			var ug=0;
			if(maxGift<g){
			  ug=maxGift;
			}else{
			  ug=g;
			}
		    var c = $("#couponAmount").val();
			var uc = 0;
			if(maxCoupon<c){
				uc = maxCoupon;
			}else{
				uc = c; 
			}
			var blance = orderRMB-uc;
			if(blance<ug){
			    $("#giftAmount").val(blance>0?blance:0); 
			}else{
		        $("#giftAmount").val(ug);  
			}
		}
	}
}

function getBalance(){ //	账户余额
	$.ajax({
		url: "../../../bss/account!getAccountBalance.action",
		async:false,
		error:function(){
			$("#balance").text(0);
		},
		success:function(data){
			if(data.success == true){
				json = data.resultObject;
				var balance = json.balance;
				couponAccount=json.coupons;
				giftAccount = json.giftsBalance;
				$("#balance").text(balance || 0);
			}else{
				$("#balance").text(0);
			}
		}
	});

}

function orderSubmit() {
	$("#payImagId").removeClass("button_d");
	$("#payImagId").addClass("button_g");
	$("#order_submit_1").removeAttr("onclick");
	
	var couponAmount = $('#couponAmount').val();
	
	//是否使用返点
	if($("#doUseCoupon")[0].checked == true && couponAmount != undefined) {
		if(couponAmount == '') {
			Dialog.alert(i18n.get("Rebate number can not be empty!"));
			$("#payImagId").removeClass("button_g");
			$("#payImagId").addClass("button_d");
			$("#order_submit_1").attr("onclick","orderSubmit()");
	    	return;
		}
		if(parseFloat(couponAmount) > parseFloat(maxCoupon)) {
			var text = i18n.get("maxUseRebate");//此次最多使用xxmaxxx点返点！
			text = text.replace('xxmaxxx', maxCoupon);
			Dialog.alert(text);
			$("#payImagId").addClass("button_d");
			$("#payImagId").removeClass("button_g");
			$("#order_submit_1").attr("onclick","orderSubmit()");
			return;
		}
		
		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
	    if(!reg.test(couponAmount)){
	    	Dialog.alert(i18n.get("Rebate malformed!"));
	    	$("#payImagId").addClass("button_d");
	    	$("#payImagId").removeClass("button_g");
			$("#order_submit_1").attr("onclick","orderSubmit()");
	    	return;
	    }
	} else {
		couponAmount = 0;
	}
	
	var giftAmount = $('#giftAmount').val();
	
	//是否使用礼金
	if($("#doUseGift")[0].checked == true && giftAmount != undefined) {
		if(giftAmount == '') {
			Dialog.alert(i18n.get("Gift number can not be empty!"));
			$("#payImagId").removeClass("button_g");
			$("#payImagId").addClass("button_d");
			$("#order_submit_1").attr("onclick","orderSubmit()");
	    	return;
		}
		if(parseFloat(giftAmount) > parseFloat(maxGift)) {
			var text = i18n.get("maxUseGift");//此次最多使用xxmaxxx元礼金！
			text = text.replace('xxmaxxx', maxGift);
			Dialog.alert(text);
			$("#payImagId").addClass("button_d");
			$("#payImagId").removeClass("button_g");
			$("#order_submit_1").attr("onclick","orderSubmit()");
			return;
		}
		
		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
	    if(!reg.test(giftAmount)){
	    	Dialog.alert(i18n.get("Gift malformed!"));
	    	$("#payImagId").addClass("button_d");
	    	$("#payImagId").removeClass("button_g");
			$("#order_submit_1").attr("onclick","orderSubmit()");
	    	return;
	    }
	} else {
		giftAmount = 0;
	}
	
	var jsonRequest = {
		"order.id" : window.payingOrderID,
		"couponAmount": couponAmount,
		"giftAmount": giftAmount
	};
	
	$.ajax({
		url: "../../../order/order!payV2.action",
		dataType:"json",
		data:jsonRequest,
		error:function(){
			$("#balance").text(0);
		},
		success:function(data){
			if (data.success == true && data.resultObject == "success") {
				window.location.href="../orderunpaid/paidfinished.html?index=1";
			} else {
				Dialog.alert(data.resultMsg);
				$("#payImagId").removeClass("button_g");
				$("#payImagId").addClass("button_d");
				$("#order_submit_1").attr("onclick","orderSubmit()");
			}
		}
	});
}

function goToCharge(){
	//openDialog("此功能正在建设中，暂时请联系管理员进行人工充值，由此带来的不便敬请谅解");
	window.location.href="../../account/account/account.html";
}

function getDomainOffLinePayment(){
	$.ajax({
		async : false,
		type : "get",
		url : path
		+ "user_mgmt/user!getDomainByDomainId.action",
		dataType : 'json',
		success : function(json) {
			if (json.success) {
				if(json.resultObject){
					$("#offlineBank").text(json.resultObject.bank);
					$("#offlineCardId").text(json.resultObject.cardNo);
					$("#offlineCompanyName").text(json.resultObject.name);
				}
			}
		}
	});
}

function doUseCouponFunc(obj) {
	if(obj.checked == true) {
		$("#couponInfo").show();
	} else {
		$("#couponInfo").hide();
	}
}

function doUseGiftFunc(obj) {
	if(obj.checked == true) {
		$("#giftInfo").show();
	} else {
		$("#giftInfo").hide();
	}
}

function getDomainOffLinePayment(){
	$.ajax({
		async : false,
		type : "get",
		url : "../../../user_mgmt/user!getDomainByDomainId.action",
		dataType : 'json',
		success : function(json) {
			if (json.success) {
				if(json.resultObject){
					$("#offlineBank").text(json.resultObject.bank);
					$("#offlineCardId").text(json.resultObject.cardNo);
					$("#offlineCompanyName").text(json.resultObject.name);
				}
			}
		}
	});
}
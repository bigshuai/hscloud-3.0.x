/**
 * 续订业务
 */
var pageObje = new PageObje('tableId');
var type = '';// 试用:0 正式:1
var que = '';
var feeTypeArray;
var rootPath=rootPath;//当前服务根路径
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/servicerenewal/renewalservice.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	i18nPage();//国际化当前页面
	initDialog();
	$("#query").blur(function(){
		if(this.value==''){
			this.value = i18n.get("input-machine-number");
		} 
	});
	
	$("#query").focus(function(){
		if(this.value==i18n.get("input-machine-number")){
			this.value="";
		}
	});



	renewalQuery();

});
//账户余额
var accountStore={
	balance:0,
	coupon:0,
	gift:0,
	couponsRebateRate:0,
	giftsRebateRate:0,
	load:function(){
		$.ajax({
			url:"../../../bss/account!getAccountBalanceAndRealRebate.action",
			async:false,
			success:function(data,textStatus){
				if(data.success == true){
					var result = data.resultObject;
					accountStore.balance = result.balance;
					accountStore.coupon = result.coupons;
					accountStore.gift = result.giftsBalance;
					accountStore.couponsRebateRate = result.couponsRebateRate;
					accountStore.giftsRebateRate = result.giftsRebateRate;
				}
			}
		});
	}
};
//获取VM续费计费方式信息
var feeTypeStore={
	feeTypeArray:new Array(),
	load:function(referenceId){
		$.ajax({
			async : false,
			type : "GET",
			url : "../../../sc/sc!getScFeeTypeByReferenceId.action",
			data : "referenceId=" + referenceId,
			dataType : 'json',
			success : function(data,textStatus) {
				if (data.success==true) {
					feeTypeStore.feeTypeArray = data.resultObject;
				}
			}
		});
	}
};
//获取VM转正计费方式信息
var feeTypeForTryStore={
	feeTypeArray:new Array(),
	load:function(scId){
		$.ajax({
			async : false,
			type : "GET",
			url : "../../../ops/ops!getVMFeeType.action",
			data : "scId=" + scId,
			dataType : 'json',
			success : function(data,textStatus) {
				if (data.success==true) {
					feeTypeForTryStore.feeTypeArray = data.resultObject;
				}
			}
		});
	}
};
//获取用户返点率
var rebateRateStore={
	rebateRate:0,
	load:function(){
		$.ajax({
			url:"../../../user_mgmt/userBrank!getRebateRate.action",
			async:false,
			success:function(data,textStatus){
				if(data.success == true){
					rebateRateStore.rebateRate = data.resultObject;
				}
			}
		});
	}
};
function i18nPage(){
	$("#query").val(i18n.get("input-machine-number"));
	$("#status").html(i18n.get("refundManagement_businessStatus"));
	$("#all").html(i18n.get("refundManagement_all"));
	$("#buss_try_vm").html(i18n.get("buss_try_vm"));
	$("#buss_normal_vm").html(i18n.get("buss_normal_vm"));
	$("#vmbuss_businessName").html(i18n.get("vmbuss_businessName"));
	$("#vmbuss_type").html(i18n.get("vmbuss_type"));
	$("#start").html(i18n.get("start"));
	$("#end").html(i18n.get("end"));
	$("#bu_status").html(i18n.get("refundManagement_businessStatus"));
	$("#allOperations").html(i18n.get("refundManagement_operation"));
	$("#no").html(i18n.get("no."));
	
}

//未支付订单分页展示
function renewalQuery() {
	pageObje.size = 8;
	pageObje.methodArray = [['name','nameRender'],
	                        ['businessStatus','businessStatusRender'],
	                        ['startTime','dateTimeRender'],
	                        ['endTime','dateTimeRender'],
	                        ['businessType','businessTypeRender'],
	                        ['referenceId','actionRender']];
	pageObje.column = ['id','name','referenceId','vmUUID','businessType','businessStatus',
	                   'startTime','endTime','serviceCatalogId'];
	pageObje.jsonRequest = {
		"type":type,
		"page" : pageObje.current,
		"limit" : pageObje.size,
		"status":0,
		"query":que
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

function nameRender(value,record){
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
};

function dateTimeRender(value){
	
	if(null != value){
		return formatDateTime(new Date(value));
	}else{
		return "";
	}
	
}

function businessTypeRender(value,record){
	var businessType='';
	switch(value){
		case 0: businessType=i18n.get("vmbuss_trytitle");break;//试用云主机
		case 1: businessType=i18n.get("vmbuss_regulartitle");break;//正式云主机
		default:businessType='';
	}
	return businessType;
}


function actionRender(value,record){
	var referenceId = value;
	var serviceCatalogId = record['serviceCatalogId'];
	var businessType = record['businessType'];
	var businessStatus = record['businessStatus'];
	var isEnable = record['isEnable'];
	var vmuuid = record['vmUUID'];
	var vmBuyType = record['buyType'];
    var tryCreateTime=record["startTime"];
    var tryExpireTime=record["endTime"];
    var TryTime=(tryExpireTime-tryCreateTime)/(24*3600*1000);
	var renderString = '';
	if(businessType==1){
		renderString+='<span class="re_button re_button-rounded re_button-small re_button-primary_renew reclearfloat" onclick="showRenewFeeVM('+referenceId+','+isEnable+',\''+vmuuid+'\','+vmBuyType+')">'+i18n.get("vmbuss_renew")+'</span>'+//续费
		'<span class="re_button re_button-rounded re_button-small re_button-primary_normal reclearfloat" onclick="showHistoryOrders('+referenceId+')">'+i18n.get("vmbuss_history")+'</span>';//历史
	}else{
//		if(0==businessStatus){//试用待审核状态，无操作
//			renderString+='<span class="re_button re_button-rounded re_button-small reclearfloat" onclick="cancelVM('+referenceId+')">'+i18n.get("取消")+'</span>';
//		}else 
		if(2==businessStatus){//延期待审核状态，可转正
			renderString+='<span class="re_button re_button-rounded re_button-small re_button-primary_disable reclearfloat" >'+i18n.get("vmbuss_delay")+'</span>'+//延期
			'<span class="re_button re_button-rounded re_button-small re_button-primary_normal reclearfloat" onclick="showApplyNormal('+referenceId+','+serviceCatalogId+')">'+i18n.get("vmbuss_regular")+'</span>';//转正
		}else if(3==businessStatus){//已延期状态，可转正
			renderString+='<span class="re_button re_button-rounded re_button-small  re_button-primary_disable reclearfloat" >'+i18n.get("vmbuss_delay")+'</span>'+//延期
				'<span class="re_button re_button-rounded re_button-small re_button-primary_normal reclearfloat" onclick="showApplyNormal('+referenceId+','+serviceCatalogId+')">'+i18n.get("vmbuss_regular")+'</span>';//转正
		}else if(1==businessStatus && TryTime<30){//试用中状态，可转正，可延期
			renderString+='<span class="re_button re_button-rounded re_button-small re_button-primary_renew reclearfloat" onclick="applyDefer('+referenceId+')">'+i18n.get("vmbuss_delay")+'</span>'+//延期
			'<span class="re_button re_button-rounded re_button-small re_button-primary_normal reclearfloat" onclick="showApplyNormal('+referenceId+','+serviceCatalogId+')">'+i18n.get("vmbuss_regular")+'</span>';//转正
		}else if(1==businessStatus && TryTime>=30){
			renderString+='<span class="re_button re_button-rounded re_button-small  re_button-primary_disable reclearfloat" >'+i18n.get("vmbuss_delay")+'</span>'+//延期
			'<span class="re_button re_button-rounded re_button-small re_button-primary_normal reclearfloat" onclick="showApplyNormal('+referenceId+','+serviceCatalogId+')">'+i18n.get("vmbuss_regular")+'</span>';//转正
		}
	}
	return renderString;
}

function pageChange(pageObje) {
	pageObje.jsonRequest['page']=pageObje.current;
	var url = '../../../ops/ops!getRenewalVm.action';
	$.ajax({
		url:url,
		type:'POST',
		dataType:'json',
		data:pageObje.jsonRequest,
		async:false,
		success:function(data){
			if(!data["success"]){
				Dialog.alert(data["resultMsg"]);
			}else{
				pageCreatorR2(pageObje,data["resultObject"]);
			}
		}
	});
}

function query() {
	var queryType = type;
	$(".checked",$("#queryType")).each(function(){
		queryType = $(this).attr("value");
	});
	var queryValue = $.trim($("#query").val());
	// 去除提示信息,过滤信息
	if (queryValue == i18n.get("input-machine-number")) {
		que = "";
	}else{
	    que = queryValue;
	}
	renewalQuery();
}

function formatDateTime(date){
	if("Invalid Date" == date){
		return "";
	}
	return date.format("yyyy-MM-dd hh:mm:ss");
}

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

function businessStatusRender(value,record){
	var businessStatus = '';
	switch(value){
		case 0: businessStatus=i18n.get("vmbuss_TryWait");break;//试用待审核
		case 1: businessStatus=i18n.get("vmbuss_Try");break;//试用中
		case 2: businessStatus=i18n.get("vmbuss_DelayWait");break;//延期待审核
		case 3: businessStatus=i18n.get("vmbuss_Delay");break;//已延期
		case 4: businessStatus=i18n.get("vmbuss_regular");break;//转正
		default:businessStatus='';
	}
	return businessStatus;
};

function changeStatus(doc,value){
	
	$(doc).siblings().each(function(){
		$(this).removeClass("checked");
	});
	$(doc).addClass("checked");
	type = value;
		var queryType = type;
	$(".checked",$("#queryType")).each(function(){
		queryType = $(this).attr("value");
	});
	var queryValue = $.trim($("#query").val());
	// 去除提示信息,过滤信息
	if (queryValue == i18n.get("input-machine-number")) {
		que = "";
	}else{
	    que = queryValue;
	}
	renewalQuery();
}
function getRebateRateFloat(arg) {
	var length = arg.toString().split(".")[0].length;
	var value = arg.toString().replace(".","");
	var result = "0.";
	for(var i = length; i < 2; i++) {
		result = result + '0';
	}
	result = result + value;
	return result;
};


function goToCharge(){
	window.location.href="../../account/account/account.html";
}

//续费提交
function applyRenewal(vmuuid,dialog){
	var feeTypeId=$("#temer-select").find('option:selected').val();
	var couponAmount = $('#couponUse').val();
	var maxCoupon = $('#coupon2').text();
	//是否使用返点
	if($("#doUseCoupon")[0].checked == true && couponAmount != undefined) {
		if(couponAmount == '') {
			Dialog.alert(i18n.get("Rebate number can not be empty!"));//返点数量不能为空！
	    	return false;
		}
		if(parseFloat(couponAmount) > parseFloat(maxCoupon)) {
			var text = i18n.get("The max rebate is")+maxCoupon;//"返点最大为xxmaxxx点！";
			Dialog.alert(text);
			return false;
		}
		
		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
	    if(!reg.test(couponAmount)){
	    	$('#couponUse').css('border-radius', '2px');
	    	$('#couponUse').css('border', '1px solid red');
	    	return false;
	    }else{
	    	$('#couponUse').css('border-radius', '2px');
	    	$('#couponUse').css('border', '1px solid #a4a4a4');
	    }
	} else {
		couponAmount = 0;
	}
	
	var giftAmount = $('#giftUse').val();
	var maxGift = $('#gift2').text();
	//是否使用返点
	if($("#doUseGift")[0].checked == true && giftAmount != undefined) {
		if(giftAmount == '') {
			Dialog.alert(i18n.get("Gift number can not be empty!"));//返点数量不能为空！
	    	return false;
		}
		if(parseFloat(giftAmount) > parseFloat(maxGift)) {
			var text = i18n.get("The max gift is")+maxGift;//"返点最大为xxmaxxx点！";
			Dialog.alert(text);
			return false;
		}
		
		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
	    if(!reg.test(giftAmount)){
	    	$('#giftUse').css('border-radius', '2px');
	    	$('#giftUse').css('border', '1px solid red');
	    	return false;
	    }else{
	    	$('#giftUse').css('border-radius', '2px');
	    	$('#giftUse').css('border', '1px solid #a4a4a4');
	    }
	} else {
		giftAmount = 0;
	}
	var param ={
			"id":vmuuid,
			"feeTypeId":feeTypeId,
			"couponAmount":couponAmount,
			"giftAmount":giftAmount
	};
	$.ajax({
		url:"../../../ops/ops!renewFeeVM.action",
		dataType:"json",
		data:param,
		method:"POST",
		success:function(data){
			dialog.close();
			if (!data.success) {
				if(data.resultMsg==i18n.get("Operation failed, insufficient account balance!")){//操作失败，账户余额不足!
					var htmlStr="<div id='needRechargeTip' style=\"float:left;\">"+
					"<div style=\"float:left;\">"+data.resultMsg
					   +
					"</div>"+
					"<div style=\"float:left;\">"+
					"<a onclick=\"top.window.location.href='../../account/account/account.html';\"  style=\"color: #09d;text-decoration: none;border: 0;background-color: transparent;text-decoration: none;cursor: pointer;\">"+i18n.get("go to recharge")+"</a>"+//现在去充值
					"</div>"+
				    "</div>";
					Dialog.alert(htmlStr);
				}else{
					Dialog.alert(data.resultMsg);
				}
				
			}else{
				renewalQuery();
			}
			
		}
	});
};


//显示续费表单
function showRenewFeeVM(referenceId,isEnable,vmuuid,vmBuyType){	
	if(1==isEnable){
		 Dialog.alert("服务器用于非法用途-停止服务!");
		 return;
	}
	
	accountStore.load();
	feeTypeStore.load(referenceId);
//	rebateRateStore.load();
	
	var balance=accountStore.balance;
	var coupon=accountStore.coupon;
	var gift = accountStore.gift;
	
	var couponsRebateRate = getRebateRateFloat(accountStore.couponsRebateRate);
	var giftsRebateRate = getRebateRateFloat(accountStore.giftsRebateRate);
//	alert(couponsRebateRate+'**'+giftsRebateRate);
	feeTypeArray=feeTypeStore.feeTypeArray;
	feeTypeArray.reverse();
//	var rebateRate=getRebateRateFloat(couponsRebateRate);
	
	var couponUse = 0;
	var giftUse = 0;
	$("#balance").text(balance);
	$("#coupon").text(coupon);
	$("#gift").text(gift);
	var feeTypeLength=0;
	//
	if(vmBuyType==1){
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
	    	submitRenewRequest(vmuuid,$("#buy_period").val(),0,0,dialog1);
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
				data : "id=" + vmuuid,
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
								planId:planId,//config.js中配置
								domainCode:domainCode,//config.js中配置
								brandCode:brandCode,
								zoneGroupCode:getZoneGroupCode(referenceId),
								CPU:json.resultObject.cpu,
								memory:json.resultObject.memory,
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
	}else{
		if(feeTypeArray!=null && feeTypeArray.length>0){		
			var show = false;
			//feeTypeLength = feeTypeArray.length-1;
			feeTypeLength = 0;
			var option ="";
			$.each(feeTypeArray, function(index, value) {
				option = option + "<option value =\""+value['id']+"\">"+value['period']+i18n.get("month")+"</option>";
			});
			feetypeId=feeTypeArray[feeTypeLength].id;
			var couponDIV=""; 
			if(feeTypeArray[feeTypeLength].usePointOrNot==true && coupon > 0 && couponsRebateRate>0){
				couponUse = floatMulti(feeTypeArray[feeTypeLength].price, couponsRebateRate);
				show = true;
			}
			couponDIV = "<div id=\"coupon-div\" style=\"float:left;"+(true==show?"display:block":"display:none;")+"\">&nbsp;<input id=\"doUseCoupon\" type=\"checkbox\" onclick=\"userCoupon(this,"+couponUse+");\" style=\"cursor:pointer;\""+(true==show?"checked=\"checked\"":"")+"/>&nbsp;<span id=\"pointuse-check-label\">"+i18n.get("vmbuss_useCoupon")+
	        "</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id=\"pointmax-label\">"+i18n.get("max-rebate")+"</span>&nbsp;<span id=\"coupon2\">"+couponUse+"</span>&nbsp;<span id=\"pointuse-label\">"+i18n.get("rebateToUse")+
	        "</span><input id=\"couponUse\" onchange=\"checkCoupon(this);\" value=\""+couponUse+"\" style=\"width:70px;border:1px solid #a4a4a4;border-radius:2px;\" type=\"text\"><span class=\"point-label\">"+i18n.get("vmbuss_point")+"</div>";
			
			var giftDIV="";
			show = false;
			if(feeTypeArray[feeTypeLength].useGiftOrNot==true && gift > 0 && giftsRebateRate>0){
				giftUse = floatMulti(feeTypeArray[feeTypeLength].price, giftsRebateRate);
				show = true;
			}
			giftDIV = "<div id=\"gift-div\" style=\"float:left;"+(true==show?"display:block":"display:none;")+"\">&nbsp;<input id=\"doUseGift\" type=\"checkbox\" onclick=\"userGift(this,"+giftUse+");\" style=\"cursor:pointer;\""+(true==show?"checked=\"checked\"":"")+"/>&nbsp;<span id=\"pointuse-check-label\">"+i18n.get("vmbuss_useGift")+
	        "</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id=\"pointmax-label\">"+i18n.get("max-rebate")+"</span>&nbsp;<span id=\"gift2\">"+giftUse+i18n.get("yuan")+"</span>&nbsp;<span id=\"pointuse-label\">"+i18n.get("giftRebateToUse")+
	        "</span><input id=\"giftUse\" onchange=\"checkCoupon(this);\" value=\""+giftUse+"\" style=\"width:70px;border:1px solid #a4a4a4;border-radius:2px;\" type=\"text\"><span class=\"point-label\">"+i18n.get("yuan")+"</span></div>";

	        var html = "<div style=\"width:90%;padding:1em 0.5em 1em 0.5em;line-height:4em;\"><div style=\"float:left;width:500px;text-align: left;\"><span id=\"timer-label\">"+i18n.get("vmbuss_regularFeeType")
	          +"</span><select id=\"temer-select\" class=\"select\">"+option+"</select></div><div style=\"float:left;width:500px;text-align: left;\"><span id=\"balance-label\">"+i18n.get("vmbuss_accountBalance")
	        +"</span><span id=\"balance\">"+balance+"</span>&nbsp;<span class=\"yuan-label\">"+i18n.get("yuan")
	        +"</span></div>"
	        +"<div  style=\"float:left;width:500px;text-align: left;display:"+(couponsRebateRate>0?"block":"none")+";\"><span style=\"float:left;\" id=\"coupon-label\">"+i18n.get("vmbuss_couponBalance")
	        +"</span><span style=\"float:left;width:80px;\" id=\"coupon\">"+coupon+i18n.get("vmbuss_point")+"</span>"
	        +"</span>&nbsp; "+couponDIV 
	        +"</span></div>"
	        +"<div  style=\"float:left;width:500px;text-align: left;display:"+(giftsRebateRate>0?"block":"none")+"\"><span style=\"float:left;\" id=\"coupon-label\">"+i18n.get("vmbuss_giftBalance")
	        +"</span><span style=\"float:left;width:80px;\" id=\"gift\">"+gift+i18n.get("gifts")+"</span>&nbsp;<span class=\"point-label\" style=\"float:left;\">"
	        +"</span>&nbsp; " + giftDIV
	        +"</span></div>"
	        +"<div style=\"float:left;width:500px;text-align: left;\"><span id=\"price-label\">"+i18n.get("vmbuss_renewFeeAmount")
	        +"</span><span id=\"money\">"+feeTypeArray[feeTypeLength].price+"</span>&nbsp;<span class=\"yuan-label\">"+i18n.get("yuan")
	        +"</span><input type=\"hidden\" id=\"referenceId\"></div></div>";
	        
			var dialog = new Dialog("dialog");
		    dialog.Width = 550;
		    dialog.Height = 200;
		    dialog.Title = i18n.get("vmbuss_renew");
		    dialog.innerHTML=html;
		    dialog.OKEvent = function() {
		    	applyRenewal(vmuuid,dialog);
		    };
		    dialog.show();
		}
	}	
	
	$("#temer-select").change(function(){
		feetypeId=$("#temer-select").find('option:selected').val();
		for(var i=0;i<feeTypeArray.length;i++){
			var id=feeTypeArray[i].id;
			if(feetypeId==id){
				$("#money").text(feeTypeArray[i].price);
				if(feeTypeArray[i].usePointOrNot==true){
					$("#doUseCoupon")[0].checked=true;					
					couponUse = floatMulti(feeTypeArray[i].price, couponsRebateRate);
					$('#coupon2').text(couponUse);
					$('#couponUse').val(couponUse);
					$("#coupon-div").show();
					
					$("#doUseGift")[0].checked=true;
					giftUse = floatMulti(feeTypeArray[i].price, giftsRebateRate);
					$('#gift2').text(giftUse);
					$('#giftUse').val(giftUse);
					$("#gift-div").show();
				}else{
					$("#coupon-div").hide();
					$("#doUseCoupon")[0].checked=false;
					$('#coupon2').text(0);
					$('#couponUse').val(0);
					
					$("#gift-div").hide();
					$("#doUseGift")[0].checked=false;
					$('#gift2').text(0);
					$('#giftUse').val(0);
				}
			}
		}
	});
}

function userCoupon(val,couponUse){
	if($(val).is(":checked")){
		$("#couponUse").val(couponUse);
	}else{
		$("#couponUse").val(0);
	}
}
function userGift(val,giftUse){
	if($(val).is(":checked")){
		$("#giftUse").val(giftUse);
	}else{
		$("#giftUse").val(0);
	}
}

function checkCoupon(val){
	var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
    if(!reg.test($(val).val())){
        $(val).css('border-radius', '2px');
    	$(val).css('border', '1px solid red');
    }else{
        $(val).css('border-radius', '2px');
    	$(val).css('border', '1px solid #a4a4a4');
    }
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
};

function getPrecision(arg){
	   if(arg.toString().indexOf(".")==-1){
	      return 0;
	   }else{
	      return arg.toString().split(".")[1].length;
	   }
	   
};
	
function getIntFromFloat(arg){
		   if(arg.toString().indexOf(".")==-1){
		      return arg;
		   }else{
		      return Number(arg.toString().replace(".",""));
		   }
		   
};

//查看VM历史订单
function showHistoryOrders(referenceId){
	$.ajax({
		url:"../../../order/order!getVmRelatedPaidOrder.action",
		dataType:"json",
		data:"referenceId=" + referenceId,
		method:"GET",
		success:function(data,textStatus){
			if (!data.success) {
				Dialog.alert(data.resultMsg);
			}else{
				var dataList = data['resultObject'];
				var html = loadOrders(dataList);
				var dialog = new Dialog("dialog");
			    dialog.Width = 530;
			    dialog.Height = 200;
			    dialog.Title = i18n.get("vmbuss_historyOrder");
			    dialog.innerHTML=html;
			    dialog.OKEvent = function() {
			    	dialog.close();
			    };
			    dialog.show();
			}
		}
    });
};
//获取VM历史订单
function loadOrders(dataList){
	var start="<div style='height:200px;overflow-y:scroll;'><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-size: 12px;\">"+
			"<tr height=\"30\">"+
			"<td width=\"150\" align=\"center\" style=\"font-weight: bold;\">"+i18n.get("orderNo")+"</td>"+
			"<td align=\"center\" style=\"font-weight: bold;\">"+i18n.get("totalPrice")+"</td>"+
			"<td align=\"center\" style=\"font-weight: bold;\">"+i18n.get("payTime")+"</td>"+
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
				 '</tr>';
	}
	var div = start+output+end;
	return div;
};
//显示转正表单
function showApplyNormal(referenceId,scId){
	
	accountStore.load();
//	feeTypeStore.load(referenceId);
	feeTypeForTryStore.load(scId);
//	rebateRateStore.load();
//	alert("***");
	var balance=accountStore.balance;
	var coupon=accountStore.coupon;
	var gift = accountStore.gift;
	
	var couponsRebateRate = getRebateRateFloat(accountStore.couponsRebateRate);
	var giftsRebateRate = getRebateRateFloat(accountStore.giftsRebateRate);
	
	feeTypeArray=feeTypeForTryStore.feeTypeArray;
	feeTypeArray.reverse();
//	var rebateRate=getRebateRateFloat(couponsRebateRate);
	var couponUse = 0;
	var giftUse = 0;
	$("#balance").text(balance);
	$("#coupon").text(coupon);
	$("#gift").text(gift);
	var feeTypeLength=0;
	if(feeTypeArray!=null && feeTypeArray.length>0){
		var show = false;
		feeTypeLength = feeTypeArray.length-1;
		var option ="";
		$.each(feeTypeArray, function(index, value) {
			option = option + "<option value =\""+value['id']+"\">"+value['period']+i18n.get("month")+"</option>";
		});
		feetypeId=feeTypeArray[feeTypeLength].id;
		
		var couponDIV=""; 
		if(feeTypeArray[feeTypeLength].usePointOrNot==true && coupon>0 && couponsRebateRate>0){
			couponUse = floatMulti(feeTypeArray[feeTypeLength].price, couponsRebateRate);
			show = true;
		}
		couponDIV = "<div id=\"coupon-div\" style=\"float:left;"+(true==show?"display:block":"display:none;")+"\">&nbsp;<input id=\"doUseCoupon\" type=\"checkbox\" onclick=\"userCoupon(this,"+couponUse+");\" style=\"cursor:pointer;\""+(true==show?"checked=\"checked\"":"")+"/>&nbsp;<span id=\"pointuse-check-label\">"+i18n.get("vmbuss_useCoupon")+
        "</span><span id=\"pointmax-label\">"+i18n.get("max-rebate")+"</span>&nbsp;<span id=\"coupon2\">"+couponUse+"</span>&nbsp;<span id=\"pointuse-label\">"+i18n.get("rebateToUse")+
        "</span><input id=\"couponUse\" onchange=\"checkCoupon(this);\" value=\""+couponUse+"\" style=\"width:70px;border:1px solid #a4a4a4;border-radius:2px;\" type=\"text\"><span class=\"point-label\">"+i18n.get("vmbuss_point")+"</div>";
		
		var giftDIV="";
		show = false;
		if(feeTypeArray[feeTypeLength].useGiftOrNot==true && gift>0 && giftsRebateRate>0){
			giftUse = floatMulti(feeTypeArray[feeTypeLength].price, giftsRebateRate);
			show = true;
		}
		giftDIV = "<div id=\"gift-div\" style=\"float:left;"+(true==show?"display:block":"display:none;")+"\">&nbsp;<input id=\"doUseGift\" type=\"checkbox\" onclick=\"userCoupon(this,"+couponUse+");\" style=\"cursor:pointer;\""+(true==show?"checked=\"checked\"":"")+"/>&nbsp;<span id=\"pointuse-check-label\">"+i18n.get("vmbuss_useGift")+
        "</span><span id=\"pointmax-label\">"+i18n.get("max-rebate")+"</span>&nbsp;<span id=\"gift2\">"+giftUse+i18n.get("yuan")+"</span>&nbsp;<span id=\"pointuse-label\">"+i18n.get("giftRebateToUse")+
        "</span><input id=\"giftUse\" onchange=\"checkCoupon(this);\" value=\""+giftUse+"\" style=\"width:70px;border:1px solid #a4a4a4;border-radius:2px;\" type=\"text\"><span class=\"point-label\">"+i18n.get("yuan")+"</span></div>";

		
        var html = "<div style=\"width:90%;padding:1em 0.5em 1em 0.5em;line-height:4em;\"><div style=\"float:left;width:500px;text-align: left;\"><span id=\"timer-label\">"+i18n.get("vmbuss_regularFeeType")
          +"</span><select id=\"temer-select\" class=\"select\">"+option+"</select></div><div style=\"float:left;width:500px;text-align: left;\"><span id=\"balance-label\">"+i18n.get("vmbuss_accountBalance")
        +"</span><span id=\"balance\">"+balance+"</span>&nbsp;<span class=\"yuan-label\">"+i18n.get("yuan")
        +"</span></div><div  style=\"float:left;width:500px;text-align: left;\"><span style=\"float:left;\" id=\"coupon-label\">"+i18n.get("vmbuss_couponBalance")
        +"</span><span style=\"float:left;\" id=\"coupon\">"+coupon+"</span>&nbsp;<span class=\"point-label\" style=\"float:left;\">"+i18n.get("vmbuss_point")
        +"</span>&nbsp; "+couponDIV
        +"</span></div>"
        +"<div  style=\"float:left;width:500px;text-align: left;\"><span style=\"float:left;\" id=\"coupon-label\">"+i18n.get("vmbuss_giftBalance")
        +"</span><span style=\"float:left;\" id=\"coupon\">"+gift+i18n.get("yuan")+"</span>&nbsp;<span class=\"point-label\" style=\"float:left;\">"
        +"</span>&nbsp; " + giftDIV
        +"</span></div>"
        +"<div style=\"float:left;width:500px;text-align: left;\"><span id=\"price-label\">"+i18n.get("vmbuss_regularAmount")
        +"</span><span id=\"money\">"+feeTypeArray[feeTypeLength].price+"</span>&nbsp;<span class=\"yuan-label\">"+i18n.get("yuan")
        +"</span><input type=\"hidden\" id=\"referenceId\"></div></div>";
        
		var dialog = new Dialog("dialog");
	    dialog.Width = 550;
	    dialog.Height = 200;
	    dialog.Title = i18n.get("try-to-normal");
	    dialog.innerHTML=html;
	    dialog.OKEvent = function() {
	    	applyNormal(referenceId,dialog);
	    };
	    dialog.show();
	}
	
	$("#temer-select").change(function(){
		feetypeId=$("#temer-select").find('option:selected').val();
		for(var i=0;i<feeTypeArray.length;i++){
			var id=feeTypeArray[i].id;
			if(feetypeId==id){
				$("#money").text(feeTypeArray[i].price);
				if(feeTypeArray[i].usePointOrNot==true){
					$("#doUseCoupon")[0].checked=true;					
					couponUse = floatMulti(feeTypeArray[i].price, couponsRebateRate);
					$('#coupon2').text(couponUse);
					$('#couponUse').val(couponUse);
					$("#coupon-div").show();
					
					$("#doUseGift")[0].checked=true;
					giftUse = floatMulti(feeTypeArray[i].price, giftsRebateRate);
					$('#gift2').text(giftUse);
					$('#giftUse').val(giftUse);
					$("#gift-div").show();
				}else{
					$("#coupon-div").hide();
					$("#doUseCoupon")[0].checked=false;
					$('#coupon2').text(0);
					$('#couponUse').val(0);
					
					$("#gift-div").hide();
					$("#doUseGift")[0].checked=false;
					$('#gift2').text(0);
					$('#giftUse').val(0);
				}
			}
		}
	});
	
};

//转正提交
function applyNormal(referenceId,dialog){
	var feeTypeId =$("#temer-select").find('option:selected').val();
	
	var couponAmount = $('#couponUse').val();
	var maxCoupon = $('#coupon2').text();
	//是否使用返点
	if($("#doUseCoupon")[0].checked == true && couponAmount != undefined) {
		if(couponAmount == '') {
			Dialog.alert("Rebate number can not be empty!");//返点数量不能为空！
	    	return false;
		}
		if(parseFloat(couponAmount) > parseFloat(maxCoupon)) {
			var text = i18n.get("The max rebate is")+maxCoupon;//"返点最大为xxmaxxx点！";
			Dialog.alert(text);
			return false;
		}		
		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
	    if(!reg.test(couponAmount)){
	    	$('#couponUse').css('border-radius', '2px');
	    	$('#couponUse').css('border', '1px solid red');
	    	return false;
	    }else{
	    	$('#couponUse').css('border-radius', '2px');
	    	$('#couponUse').css('border', '1px solid #a4a4a4');
	    }
	} else {
		couponAmount = 0;
	}
	
	var giftAmount = $('#giftUse').val();
	var maxGift = $('#gift2').text();
	//是否使用返点
	if($("#doUseGift")[0].checked == true && giftAmount != undefined) {
		if(giftAmount == '') {
			Dialog.alert(i18n.get("Gift number can not be empty!"));//返点数量不能为空！
	    	return false;
		}
		if(parseFloat(giftAmount) > parseFloat(maxGift)) {
			var text = i18n.get("The max gift is")+maxGift;//"返点最大为xxmaxxx点！";
			Dialog.alert(text);
			return false;
		}
		
		var reg = new RegExp("^(([1-9]{1}[0-9]*|[0-9]{1})(\\.[0-9]{1,2})|([1-9]{1}[0-9]*|[0]{1}))$");
	    if(!reg.test(giftAmount)){
	    	$('#giftUse').css('border-radius', '2px');
	    	$('#giftUse').css('border', '1px solid red');
	    	return false;
	    }else{
	    	$('#giftUse').css('border-radius', '2px');
	    	$('#giftUse').css('border', '1px solid #a4a4a4');
	    }
	} else {
		giftAmount = 0;
	}
	var param ={
			"referenceId":referenceId,
			"feeTypeId":feeTypeId,
			"couponAmount":couponAmount,
			"giftAmount":giftAmount
	};
	$.ajax({
		url:"../../../ops/ops!regularTryVm.action",
		dataType:"json",
		data:param,
		method:"POST",
		success:function(data){
			if (!data.success) {
				if(data.resultMsg==i18n.get("Operation failed, insufficient account balance!")){//操作失败，账户余额不足!
					var htmlStr="<div id='needRechargeTip' style=\"float:left;\">"+
					"<div style=\"float:left;\">"+data.resultMsg
					   +
					"</div>"+
					"<div style=\"float:left;\">"+
					"<a onclick=\"top.window.location.href='../../account/account/account.html';\"  style=\"color: #09d;text-decoration: none;border: 0;background-color: transparent;text-decoration: none;cursor: pointer;\">"+i18n.get("go to recharge")+"</a>"+//现在去充值
					"</div>"+
				    "</div>";
					Dialog.alert(htmlStr);
				}else{
					Dialog.alert(data.resultMsg);
				}
				
			}else{
				dialog.close();
				renewalQuery();
			}
		}
	});
};

//申请延期
function applyDefer(referenceId){
	Dialog.confirm(i18n.get("confirmedextension"), function(){
		$.ajax({
			url : "../../../ops/ops!applyForDelayTryVm.action",
			type : "POST",
			dataType : 'json',
			async : false,
			data : {"referenceId":referenceId},
			success : function(data,textStatus) {
				if (!data.success) {
					Dialog.alert(data.resultMsg);
				}else{
					renewalQuery();
				}		
			}
		});
	}, null, 400, 100);
};
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
};
function submitRenewRequest(vmNo,feeTypeId,couponAmount, giftAmount,dialog){
	var url=rootPath+"/ops/ops!renewFeeVM.action";
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
			dialog.close();
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
				//tryVMQuery();
				renewalQuery();
				Dialog.alert("续费成功！");
			}
		}
	});
};
function getZoneGroupCode(referenceId){
	var zoneGroupCode='';
	$.ajax({
		url:rootPath+'/sc/zoneGroup!getZoneGroupCodeByReferenceId.action?',
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
};
var path="";
var pageObje = new PageObje('tableId');
orderDetailFields={};
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/unpaid/unpaid.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	orderDetailFields.totalPrice = i18n.get('totalPrice');
	orderDetailFields.serviceCatalogName = i18n.get('sc_name');
	orderDetailFields.price = i18n.get('order_price');
	orderDetailFields.amount= i18n.get('order_amount');
	orderDetailFields.pointAmount=i18n.get('point_amount');
	orderDetailFields.quantity = i18n.get("order_quantity");
	orderDetailFields.os=i18n.get('order_os');
	orderDetailFields.cpu="CPU";
	orderDetailFields.memory=i18n.get('memory');
	orderDetailFields.disk=i18n.get('disk');
	orderDetailFields.machineNum = i18n.get('vmbuss_no');
	orderDetailFields.ipNum = i18n.get('ip_num');
	orderDetailFields.addDisk=i18n.get('ext_disk');
	orderDetailFields.network=i18n.get('bandwidth');
	orderDetailFields.serviceDesc=i18n.get('desc');
	orderDetailFields.effectiveDate=i18n.get('vm_effectiveDate');
	orderDetailFields.expirationDate=i18n.get('vm_expirationDate');
	i18nPage();//国际化当前页面
	initDialog();
	initDatePicker();
	$("#cancelall").click(cancelAll);
	unpaidOrderQuery();
});
function i18nPage(){
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#order_search_date_to").val(i18n.get("date_to"));
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#query").attr("placeholder",i18n.get('orderno_tip'));
	$("#no").text(i18n.get("no."));
	$("#order-Number").text(i18n.get("orderNo"));
	$("#order_create_date").text(i18n.get("order_create_date"));
	$("#money").text(i18n.get("money"));
	$("#allOperations").text(i18n.get("allOperations"));
	$("#cancel_title").text(i18n.get("Cancel"));
	$("#detail").text(i18n.get("detail"));
	$("#order_content").text(i18n.get("order_content"));
	$("#operation_pay").text(i18n.get("operation_pay"));
	$("#cancelall").html(i18n.get("cancelall"));
	$("#to_span").html(i18n.get("to"));
	$("#searchorder").text(i18n.get("searchorder"));
	
	
}

function cancelAll(){
	Dialog.confirm(i18n.get("cancleallorder"), function(){
		$.ajax({
			url : '../../../order/order!cancelUnpaidOrderByUser.action',
			type : 'Post',
			dataType : 'json',
			async : true,
			success : function(data) {
				if (data.success == true) {
					unpaidOrderQuery();
				} else {
					Dialog.alert(data.resultMsg);
				}
			}
		});
	}, null, 400, 100);
}
function cancel(id){
	var jsonRequest = {
			"order.id":id
	};
	Dialog.confirm(i18n.get("cancel_tip"), function(){
		$.ajax({
			url : '../../../order/order!cancel.action',
			type : 'Post',
			dataType : 'json',
			async : true,
			data : jsonRequest,
			success : function(data) {
				if (data.success == true) {
					unpaidOrderQuery();
				} else {
					Dialog.alert(data.resultMsg);
				}
			}
		});
	}, null, 400, 100);

}

function detail(id,orderType){
	var jsonRequest = {
		"order.id":id,
		"orderType":orderType
	};
	$.ajax({
		url : '../../../order/order!orderDetail.action',
		type : 'Post',
		dataType : 'json',
		data : jsonRequest,
		success : function(data) {
			if (data.success == true) {
				var displayList = ["serviceCatalogName","price","quantity","os","cpu","memory","disk","ipNum","addDisk","network","serviceDesc","effectiveDate","expirationDate","machineNum"];
					var resultObjs = data.resultObject;
					var orderType= resultObjs[0]['orderType'];//orderType:套餐 
					var orderGeneral = "<div class='order_detail_div' style='height:380px;overflow-y:scroll;'><table class='order_detail_table'>";
					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('order_no')+": </td>";
					orderGeneral += "<td class='order_detail_table_right'>" + resultObjs[0]['orderNo'] + "</td></tr>";
					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('order_create_date') +": </td>";
					orderGeneral += "<td class='order_detail_table_right'>" + formatDateTime(new Date(parseInt(resultObjs[0]['createDate']))) + "</td></tr>";
					
					var detail = [];
					var temp = {};
					$(resultObjs).each(function(i,it){
						var key = (1 == orderType?it['serviceCatalogName']+'['+it['os']+']':"")+'('+it['pricePeriod']+i18n.get('month')+" ";
						//var key = it['serviceCatalogName']+'['+it['os']+']'+'('+it['pricePeriod']+i18n.get('month')+" ";
						if(key in temp){
							temp[key]["quantity"] ++;
						}else{
							it["memory"] += "M";
							it["disk"] += "G";
							it["network"] += "M";
							it["addDisk"]=splitExtDisk(it["addDisk"]);
							if(it["effectiveDate"]){
								it["effectiveDate"] = formatDateTime(new Date(parseInt(it['effectiveDate'])));
							}
							if(it["expirationDate"]){
								it["expirationDate"] = formatDateTime(new Date(parseInt(it['expirationDate'])));
							}
							temp[key] = it;
						}
					});
					
					for(var i in temp){
						var temp2 = i + temp[i]["price"] +i18n.get("yuan")+") x " + temp[i]["quantity"] +" = " + ( temp[i]["price"] * temp[i]["quantity"]) + i18n.get("yuan");
						temp[i]["price"]+=i18n.get("yuan");
						detail.push(temp2);
					}
					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get("total-perHour") +" </td>";
					orderGeneral += "<td class='order_detail_table_right'>" + detail.join(";<br/>") + "</td></tr>";
					orderGeneral += "</table>";
					
					var conthr = "<hr class='order_detail_hr'>";
					
					var contMes = "";
					for(var j in temp){
						var resultObj = temp[j];
						contMes += "<table class='order_detail_table'>";
						for(var i=0;i<displayList.length;i++){
							if((2 == orderType || 3 == orderType) && ("serviceCatalogName" == displayList[i] || "serviceDesc" == displayList[i] || "disk" == displayList[i])){
								continue;
							}
							if(1 == orderType && ("ipNum" == displayList[i])){
								continue;
							}
							contMes += "<tr><td class='order_detail_table_left'>"+orderDetailFields[displayList[i]]+": </td>";
							contMes += "<td class='order_detail_table_right'>"+(resultObj[displayList[i]] || "-")+"</td></tr>";
						}
						contMes += "</table>";
				    }
					
					var orderDetailRenderStr=orderGeneral + conthr + contMes+"</div>";
					var dialog = new Dialog("dialog");
				    dialog.Width = 530;
				    dialog.Height = 380;
				    dialog.OKEvent = function() {
				    	dialog.close();
				    };
				    dialog.Title = i18n.get("order_detail");
				    dialog.innerHTML=orderDetailRenderStr;
				    dialog.show();
			} else {
				Dialog.alert(data["resultMsg"]);
			}
		}
	});
}

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

function getRebateRateFloat(arg) {
	if (arg < 100) {
		var length = arg.toString().split(".")[0].length;
		var value = arg.toString().replace(".", "");
		var result = "0.";
		for ( var i = length; i < 2; i++) {
			result = result + '0';
		}
		result = result + value;
	} else {
		result = 1;
	}
	//alert(result);      
	return result;
}


function toPay(id,totalPrice,orderType){
	var requstUrl = "../../../bss/account!getAccountByUserId.action";
	$.ajax({
		url:requstUrl,
		async:false,
		error:function(){
			balance=0;
		},
		success:function(data){
			if(data.success == true){
				dataObj = data.resultObject;
				balance=dataObj.balance;
				coupons=dataObj.coupons;
				gifts=dataObj.giftsBalance;
				var totalAmount=parseFloat(totalPrice);
				var couponsRebateRate=dataObj.couponsRebateRate;
				var giftsRebateRate=dataObj.giftsRebateRate;
				
				if(couponsRebateRate){
					var pointAmount=(floatMulti(getRebateRateFloat(couponsRebateRate),parseFloat(totalPrice)));
					var pointBalance=dataObj.couponsBalance;
					var actualPointAmount=Math.min(pointAmount,pointBalance);
					totalAmount=floatAdd(totalAmount,-(actualPointAmount));
				}
				
				if(giftsRebateRate){
					var giftAmount=floatMulti(getRebateRateFloat(giftsRebateRate),parseFloat(totalPrice));
					var giftBalance=dataObj.giftsBalance;
					var actualGiftAmount=Math.min(giftAmount,giftBalance);
					totalAmount=floatAdd(totalAmount,-(actualGiftAmount));
				}
				if(parseFloat(balance)>=totalAmount){
					        
					$.cookie('orderID',null);
					$.cookie("orderType",null);
					var expireDate = new Date();
					expireDate.setHours(expireDate.getHours()+1);
					$.cookie('orderID', id,{path:"/"});//expires:expireDate,domain:window.location.hostname,
					$.cookie('orderType', orderType,{path:"/"});
					window.location.href="payingorder.html?index=0";
				}else{
		        
					var htmlStr="<div id='needRechargeTip'>"+
									"<div>"+
									   "<span>"+i18n.get("vmbuss_accountBalance")+"</span>"+
									   "<span id='currentBanlance'>"+balance+"</span>"+
									   "<span class='yuan'>"+i18n.get("yuan")+"</span>"+
									"</div>"+
									"<div>"+
									    "<span>"+i18n.get("sorry")+"，"+i18n.get("nomoney")+"，"+i18n.get("needrecharge")+"：</span>"+
									    "<span id='needRecharge'>"+(totalAmount-parseFloat(balance)).toFixed(2)+"</span>"+
									    "<span class='yuan'>"+i18n.get("yuan")+"</span>"+
									    "<span>，"+i18n.get("completetransaction")+"。</span>"+
									"</div>"+
									"<div>"+
										"<a onclick='goToCharge()' style='cursor: pointer;'>"+i18n.get("recharge")+"</a>"+
									"</div>"+
								"</div>";
					var dialog = new Dialog("dialog");
				    dialog.Width = 500;
				    dialog.Height = 70;
				    dialog.Title = i18n.get("order_detail");
				    dialog.OKEvent = function() {
				    	dialog.close();
				    };
				    dialog.innerHTML=htmlStr;
				    dialog.show();
				}
			}else{
				Dialog.alert(data["resultMsg"]);
			}
		}
	});
}

function goToCharge(){
	window.location.href="../../account/account/account.html";
}

function query(){
	var searchDateFrom = $("#order_search_date_from").val();
	var searchDateTo = $("#order_search_date_to").val();
	var searchOrderNo = $.trim($("#query").val());
	var dateFrom4Query = "";
	var dateTo4Query = "";
	var orderNo4Query = "";
	if (searchDateFrom != i18n.get("date_from")) {
		dateFrom4Query = searchDateFrom;
	}
	if (searchDateTo != i18n.get("date_to")) {
		dateTo4Query = searchDateTo;
	}
	if (searchOrderNo != i18n.get('orderno_tip')) {
		orderNo4Query = searchOrderNo;
	}
	if (dateFrom4Query != "" && dateTo4Query != "") {
		if (dateFrom4Query > dateTo4Query) {
			Dialog.alert(i18n.get("date_error_tip"));  
			return ;
		}
	}
	pageObje.jsonRequest = {
		"order.status" : "Unpaid",
		"order.orderNo" : orderNo4Query,
		"dateFrom" : dateFrom4Query,
		"dateTo" : dateTo4Query,
		"page" : pageObje.current,
		"limit" : pageObje.size,
		"sort" : "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
	//resetSearchBar();
	pageChange(pageObje);
}

function resetSearchBar(){
	$("#order_search_date_to").val(i18n.get("date_to"));
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#query").attr("placeholder",i18n.get('orderno_tip'));
}

function unpaidOrderQuery() {
	pageObje.size = 8;
	
	pageObje.methodArray = [
	[ 'createDate', 'formatCreateDate' ],
    ['totalPriceStr','totalPriceStrRender'],
    ['orderContent','renderOrderDesc'],
    ['orderNo','orderNoRender']];
	pageObje.column = [ 'createDate', 'orderNo',
			'totalPrice', 'id','orderContent','totalPriceStr','orderType'];
	var query = $('#query').val();
	if (query == i18n.get('orderno_tip')) {
		query = '';
	}
	pageObje.jsonRequest = {
		//"query" : query,
		"order.status": "Unpaid",
		"page" : pageObje.current,
		"limit" : pageObje.size,
		"sort" : "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

function orderNoRender(value){
	var result=value;
	var title='';
	if(value){
		title=value;
		if(value.length>15){
			result=value.substring(0,12)+"...";
		}
	}
	return '<span title="'+title+'">'+result+'</span>';
}

function pageChange(pageObje) {
	pageObje.jsonRequest['page']=pageObje.current;
	var url='../../../order/order!queryOrderByDate.action';
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
				pageCreatorR2(pageObje, data.resultObject);
			}
		}
	});
	
}

function formatCreateDate(value){
	return formatDateTime(new Date(value));
}
function totalPriceStrRender(value,record){
	if(record["totalAmount"]){
		var m = record["totalAmount"];
		return '<span class="price">'+m.toFixed(2)+'</span>'+'<span>'+" "+i18n.get('yuan')+'</span>';
	}
}

function initDatePicker(){
	$( "#message_search_date_from" ).text = "";
	var currentLang = "zh_CN";
	if (getCookie("lang") != null) {
		currentLang = getCookie("lang");
	}
	if(currentLang =="zh_CN"){
		$.datepicker.regional['zh_CN'] =
		{
		   clearText: '清除',
		   clearStatus: '清除已选日期',
		   closeText: '关闭',
		   closeStatus: '不改变当前选择',
		   prevText: '&lt;上月',
		   prevStatus: '显示上月',
		   nextText: '下月&gt;',
		   nextStatus: '显示下月',
		   currentText: '今天',
		    currentStatus: '显示本月',
		   monthNames: ['一月','二月','三月','四月','五月','六月',
		   '七月','八月','九月','十月','十一月','十二月'],
		   monthNamesShort: ['一','二','三','四','五','六',
		   '七','八','九','十','十一','十二'],
		   monthStatus: '选择月份', yearStatus: '选择年份',
		   weekHeader: '周', weekStatus: '年内周次',
		   dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
		   dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
		   dayNamesMin: ['日','一','二','三','四','五','六'],
		   dayStatus: '设置 DD 为一周起始',
		   dateStatus: '选择 m月 d日, DD',
		   dateFormat: 'yy/mm/dd',   //日期格式化形式
		   firstDay: 1, 
		   initStatus: '请选择日期',
		       isRTL: false
		};
		$.datepicker.setDefaults($.datepicker.regional['zh_CN']);
	}
	$( "#order_search_date_from" ).datepicker();
	$( "#order_search_date_to" ).datepicker();

};

function renderOrderDesc(value,record){
	var detail = [];
	var title=[];
	if(1 == record["orderType"]){
		var temp = {};
		$(record["items"]).each(function(i,it){
			var scName=it['serviceCatalogName'];
			if(scName.length>20){
				scName=scName.substr(0,20)+"...";
			}
			var key = scName;
			if(key in temp){
				temp[key].push(it['price']);
			}else{
				temp[key] = [];
				temp[key].push(it['price']);
			}
		});
		
		var index=0;
		for(var i in temp){
			++index;
			var temp2 = i+' x '+ temp[i].length +" 台  ";
			title.push(temp2);
			if(index<=2){
				if(index==2){
					temp2+=" ...";
				}
				detail.push(temp2);
			}
			
		}
		var renderStr='<div title="'+detail+'">';
		var d = ""+detail;
		if(d.length>15){
			renderStr+=d.substring(0,15)+"...";
		}else{
			renderStr+=d;
		}
		renderStr+="</div>";
		return renderStr;
		
	}else{
		var temp={};
		$(record["items"]).each(function(i,it){
			var flag = (0==it["orderProducts"].length)?false:true;
			var tempDetail="";
			var key ="";
			$(it["orderProducts"]).each(function(index,value){
				if(1==value["type"]){//cpu
					key = key +"CPU "+value["spec"]+value["unit"];
				}else if(2==value["type"]){//mem
					key = key +" MEM "+value["spec"]+value["unit"];
				}else if(5==value["type"]){//bd
					key = key +" BD "+value["spec"]+value["unit"];
				}
			});
			if(key in temp){
				temp[key]++;
			}else{
				temp[key] = 1;
			}
		});
		
		for(var i in temp){
			detail = detail +"( "+i+" ) x " +temp[i]+" 台  ";
		}
		if(null!=detail && ""!=detail && detail.length>15){
			var renderStr='<div style="width:170px;" title="'+detail+'">' + detail.substring(0,15)+"..." + "</div>";
		}else{
			var renderStr='<div style="width:170px;" title="'+detail+'">' + detail + "</div>";
		}
		return renderStr;
	}
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
}

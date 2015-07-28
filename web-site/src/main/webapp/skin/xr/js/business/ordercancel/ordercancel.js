var path="";
var pageObje = new PageObje('tableId');
orderDetailFields={};
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/ordercancel/ordercancel.css');
	i18n.init();//国际化初始化
	initTagAndMenu(busiTagNameArr,busiPageUrls);
	orderDetailFields.totalPrice = i18n.get('totalPrice');
	orderDetailFields.serviceCatalogName = i18n.get('sc_name');
	orderDetailFields.price = i18n.get('order_price');
	orderDetailFields.amount= i18n.get('order_amount');
	orderDetailFields.pointAmount=i18n.get('point_amount');
	orderDetailFields.giftAmount=i18n.get('gifts');
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
	cancelOrderQuery();
});
function i18nPage(){
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#order_search_date_to").val(i18n.get("date_to"));
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#query").attr("placeholder",i18n.get('orderno_tip'));
	$("#no").text(i18n.get("no."));
	$("#order-Number").text(i18n.get("orderNo"));
	$("#order_cancel_date").text(i18n.get("order_cancel_date"));
	$("#money").text(i18n.get("money"));
	$("#order_detail").text(i18n.get("order_detail"));
	$("#cancel_title").text(i18n.get("Cancel"));
	$("#detail").text(i18n.get("detail"));
	$("#order_content").text(i18n.get("order_content"));
	$("#operation_pay").text(i18n.get("operation_pay"));
	$("#cancelall").html(i18n.get("cancelall"));
	$("#to_span").html(i18n.get("to"));
	$("#searchorder").text(i18n.get("searchorder"));
	
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
			
			if (dateFrom4Query > dateTo4Query) {
				Dialog.alert(i18n.get("date_error_tip"));  
				return ;
			}
		}
	}
	pageObje.jsonRequest = {
		"order.status" : "Cancelled",
		"order.orderNo" : orderNo4Query,
		"dateFrom" : dateFrom4Query,
		"dateTo" : dateTo4Query,
		"page" : pageObje.current,
		"limit" : pageObje.size,
		"sort" : "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
	pageChange(pageObje);
}

function resetSearchBar(){
	$("#order_search_date_to").val(i18n.get("date_to"));
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#query").attr("placeholder",i18n.get('orderno_tip'));
}

function cancelOrderQuery() {
	pageObje.size = 8;
	pageObje.methodArray = [['updateDate','formatCreateDate'],['id','operationRender'],['remark','remarkRender'],['totalPrice','totalPriceRender'],['orderNo','orderNoRender']];
	pageObje.column = [ 'updateDate', 'id','remark','totalPrice','orderNo'];
	var query = $('#query').val();
	if (query == i18n.get('orderno_tip')) {
		query = '';
	}
	pageObje.jsonRequest = {
		"order.status": "Cancelled",
		"page" : pageObje.current,
		"limit" : pageObje.size,
		"sort" : "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
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
	if(""!=value && null !=value){
		return formatDateTime(new Date(value));
	}else{
		return "";
	}
}

function orderNoRender(value){
	var result=value;
	var title='';
	if(value){
		title=value;
		if(value.length>15){
			result=value.substring(0,15)+"...";
		}
	}
	return '<span title="'+title+'">'+result+'</span>';
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

function operationRender(value,record){
	var renderStr="";
	var orderType=record.orderType;
	//var detailIcon='<span style="text-align: center;" onClick="vieworder('+value+','+orderType+')" title="'+i18n.get("order_detail")+'"class="icon"><div class="infor"></div></span>';
	//var detailIcon='<div style="text-align: center;" class="infor" onClick="vieworder('+value+','+orderType+')" title="'+i18n.get("order_detail")+'"></div>'
	var detailIcon ='<a class="button button-rounded button-primary button-small" style="text-align: center;float: none;" id="detail" onclick="vieworder('+value+','+orderType+')">'+i18n.get("order_detail")+'</a>';
	renderStr=detailIcon;
	return renderStr;
}

function remarkRender(value,record){
	var remark=value;
	var cancelType='';
	if(remark&&remark.indexOf("退款")!=-1){
		cancelType=i18n.get("refundcanceled");
	}else{
		cancelType=i18n.get("ordercanceled");
	}
	var remarkRenderStr='<span'+(null==remark || ""==remark?"":' style="cursor:pointer;"')+' title="'+(null==remark || ""==remark?"":remark)+'" onclick="viewRemark(\''+(null==remark || ""==remark?"":remark) + '\')">'+cancelType+'</span>';
	return remarkRenderStr;
}

function totalPriceRender(value,record){
//	var totalPriceDisplayStr='<span class="price">'+record['totalPrice'].toFixed(2)+'</span>'+'<span>'+i18n.get('yuan')+'</span>';
//	if(record['totalPointAmount']&&record['totalAmount']){
//		totalPriceDisplayStr='<span>'+
//		record['totalAmount']+i18n.get('yuan')+'+'+record['totalPointAmount'].toFixed(2)+
//		i18n.get('vmbuss_point')+" = </span><span class='price'>"
//		+record['totalPrice'].toFixed(2)+'</span>'+'<span>'+i18n.get('yuan')+'</span>';
//	}
//	var total =0;
//    var totalPriceDisplayStr;
//	if(record['totalAmount']){
//	    var amount = '<span class="price">'+record['totalAmount'].toFixed(2)+'</span>'+'<span>'+i18n.get('yuan')+'</span>';
//		totalPriceDisplayStr = amount;
//		total = record['totalAmount'];
//	}
//	if(record['totalPointAmount']){
//	    var point = '<span>+'+record['totalPointAmount'].toFixed(2)+i18n.get('vmbuss_point')+"</span>"
//		totalPriceDisplayStr= totalPriceDisplayStr+point;
//		total = total + record['totalPointAmount'];
//	}
//	if(record['totalGiftAmount']){
//	    var gift = '<span>+'+record['totalGiftAmount'].toFixed(2)+i18n.get('gifts')+"</span>";
//		totalPriceDisplayStr= totalPriceDisplayStr+gift;
//		total = total + record['totalGiftAmount'];
//	}
//	totalPriceDisplayStr = totalPriceDisplayStr + "<span>="+total.toFixed(2)+i18n.get('yuan')+"</span>";
    
	var totalPriceDisplayStr ="";
	var flag=0;
	if(record['totalAmount']){
		total = record['totalAmount'];
		totalPriceDisplayStr = totalPriceDisplayStr + record['totalAmount'].toFixed(2)+i18n.get('yuan');
		flag = flag +1;
	}
	if(record['totalPointAmount']){
		totalPriceDisplayStr= totalPriceDisplayStr +(flag>0?'+':'')+ record['totalPointAmount'].toFixed(2)+i18n.get('vmbuss_point');
		total = total + record['totalPointAmount'];
		flag = flag +1;
	}
	if(record['totalGiftAmount']){
		totalPriceDisplayStr= totalPriceDisplayStr+(flag>0?'+':'')+record['totalGiftAmount'].toFixed(2)+i18n.get('gifts');
		total = total + record['totalGiftAmount'];
		flag = flag +1;
	}
	
	totalPriceDisplayStr = totalPriceDisplayStr + "="+total.toFixed(2)+i18n.get('yuan');
	totalPriceDisplayStr = "<div class='price' title='"+(flag<=1?"":totalPriceDisplayStr)+"'>"+total.toFixed(2)+" "+i18n.get('yuan')+"</div>";
	return totalPriceDisplayStr;
}

function viewRemark(remark){
	if(""==remark || null ==remark){
	    return ;
	}
	var html = "<div style='height:100px;overflow-y:scroll;text-align:left;' >"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+remark+"</div>";
	var dialog = new Dialog("dialog");
    dialog.Width = 500;
    dialog.Height = 100;
    dialog.Title = i18n.get("detail");
    dialog.OKEvent = function() {
    	dialog.close();
    };
    dialog.innerHTML=html;
    dialog.show();
}

//展示订单详情
function vieworder(id,orderType){
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
					var displayList = ["serviceCatalogName","price","amount","pointAmount","giftAmount","quantity","os","cpu","memory","disk","ipNum","addDisk","network","serviceDesc","effectiveDate","expirationDate","machineNum"];
					var resultObjs = data.resultObject;
					var orderGeneral = "<div class='order_detail_div' style='height:380px;overflow-y:scroll;'><table class='order_detail_table'>";
					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('orderNo') +": </td>";
					orderGeneral += "<td class='order_detail_table_right'>" + resultObjs[0]['orderNo'] + "</td></tr>";
					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('order_create_date') +": </td>";
					orderGeneral += "<td class='order_detail_table_right'>" + formatDateTime(new Date(parseInt(resultObjs[0]['createDate']))) + "</td></tr>";
					
					var detail = [];
					var temp = {};
					$(resultObjs).each(function(i,it){
						var key = (1 == orderType?it['serviceCatalogName']+'['+it['os']+']':"")+'('+it['pricePeriod']+i18n.get('month')+" ";
							it["memory"] += "M";
							it["disk"] += "G";
							it["network"] += "M";
							it["giftAmount"]=(null == it["giftAmount"]?it["giftAmount"]:it["giftAmount"]+i18n.get("gifts"));
							it["addDisk"]=splitExtDisk(it["addDisk"]);
							it["effectiveDate"] = formatDateTime(new Date(parseInt(it['effectiveDate'])));
							it["expirationDate"] = formatDateTime(new Date(parseInt(it['expirationDate'])));
							it["nodeName"]=key;
							temp[i] = it;
					});
					
					for(var i in temp){
						var temp2 = temp[i]["nodeName"]+ temp[i]["price"] +"元"+") x " + temp[i]["quantity"] +" = " + ( temp[i]["price"] * temp[i]["quantity"]) + "元";
						temp[i]["price"]+="元";
						temp[i]["amount"]+="元";
						if(temp[i]['pointAmount']){
							temp[i]["pointAmount"]+=i18n.get('vmbuss_point');
						}else{
							temp[i]["pointAmount"]="0"+i18n.get('vmbuss_point');
						}
						detail.push(temp2);
					}
					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('totalPrice') +": </td>";
					orderGeneral += "<td class='order_detail_table_right'>" + detail.join(";<br/>") + "</td></tr>";
					orderGeneral += "</table>";
					
					var contMes = "";
					for(var j in temp){
						var resultObj = temp[j];
						contMes += "<hr class='order_detail_hr'><table class='order_detail_table'>";
						for(var i=0;i<displayList.length;i++){
							if(2 == orderType && ("serviceCatalogName" == displayList[i] || "serviceDesc" == displayList[i] || "disk" == displayList[i])){
								continue;
							}
							if(1 == orderType && ("ipNum" == displayList[i])){
								continue;
							}
							contMes += "<tr><td class='order_detail_table_left'>"+orderDetailFields[displayList[i]]+": </td>";
							//contMes += "<td class='order_detail_table_right'>"+(resultObj[displayList[i]] || "0"+i18n.get("gifts"))+"</td></tr>";
							contMes += "<td class='order_detail_table_right'>"+(resultObj[displayList[i]] || "-")+"</td></tr>";
						}
						contMes += "</table>";
					}
						
					var orderDetailRenderStr=orderGeneral + contMes+"</div>";
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
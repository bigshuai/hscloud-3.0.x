var pageObje = new PageObje('tableId');
orderDetailFields={};
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/paid/paid.css');
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
	paidOrderQuery();
	initDatePicker();
});


function i18nPage(){
	
	$("#searchorder").text(i18n.get("searchorder"));
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#order_search_date_to").val(i18n.get("date_to"));
	$("#order_search_date_from").val(i18n.get("date_from"));
	$("#query").html(i18n.get('messageSearch'));
	$("#no").text(i18n.get("no."));
	$("#query").attr("placeholder",i18n.get('orderno_tip'));
	$("#order-Number").text(i18n.get("orderNo"));
	$("#dealtime").text(i18n.get("dealtime"));
	$("#money").text(i18n.get("money"));
	$("#allOperations").text(i18n.get("allOperations"));
	$("#cancel_title").text(i18n.get("Cancel"));
	$("#detail").text(i18n.get("order_detail"));
	$("#order_content").text(i18n.get("order_content"));
	$("#operation_pay").text(i18n.get("operation_pay"));
	$("#cancelall").html(i18n.get("cancelall"));
	$("#to_span").html(i18n.get("to"));
	
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
		"order.status" : "Paid",
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

function paidOrderQuery() {
	pageObje.size = 8;
	pageObje.methodArray = [[ 'payDate', 'formatCreateDate' ],[ 'totalPrice', 'totalPriceStr' ],['orderContent','renderOrderDesc'],['orderNo','orderNoRender']];
	pageObje.column = ["id","payDate","orderNo","totalPrice","orderContent",'orderType'];
	pageObje.jsonRequest = {
			"order.status": "Paid",
			"page" : pageObje.current,
			"limit" : pageObje.size,
			"sort" : "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

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
				pageCreatorR2(pageObje,data["resultObject"]);
			}
		}
	});
	
	
	
}

function totalPriceStr(value,record){
//	var html ="";
//	if(value){
//		html = html +  '<span class="price">'+value.toFixed(2)+'</span>'+'<span>'+i18n.get('yuan')+'</span>';
//	}
//	if(record["totalPointAmount"] && value){
//		html = html +  '+<span >'+record["totalPointAmount"].toFixed(2)+'</span>'+'<span>'+i18n.get('vmbuss_point')+'=</span>'+'<span class='price'>"
//		+record['totalPrice']+'</span>'+'<span>'+get('yuan')+'</span>';
//	}
//	return html;
	
//	var totalPriceDisplayStr='<span class="price">'+record['totalPrice'].toFixed(2)+'</span>'+'<span>'+i18n.get('yuan')+'</span>';
//	if(record['totalPointAmount']&&record['totalAmount']){
//		totalPriceDisplayStr='<span>'+
//		record['totalAmount']+i18n.get('yuan')+'+'+record['totalPointAmount'].toFixed(2)+
//		i18n.get('vmbuss_point')+" = </span><span class='price'>"
//		+record['totalPrice'].toFixed(2)+'</span>'+'<span>'+i18n.get('yuan')+'</span>';
//	}
	
	var total =0;
    var totalPriceDisplayStr;
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

function formatCreateDate(value){
	return formatDateTime(new Date(value));
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
						it["addDisk"]=splitExtDisk(it["addDisk"]);
						it["giftAmount"]=(null == it["giftAmount"]?it["giftAmount"]:it["giftAmount"]+i18n.get("gifts"));
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
				
				//var conthr = "<hr class='order_detail_hr'>";
				
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
//				
//					var resultObjs = data.resultObject;
//					var orderType= resultObjs[0]['orderType'];//orderType:套餐 
//					var orderGeneral = "<div class='order_detail_div' style='height:380px;overflow-y:scroll;'><table class='order_detail_table'>";
//					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('order_no')+": </td>";
//					orderGeneral += "<td class='order_detail_table_right'>" + resultObjs[0]['orderNo'] + "</td></tr>";
//					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get('order_create_date') +": </td>";
//					orderGeneral += "<td class='order_detail_table_right'>" + formatDateTime(new Date(parseInt(resultObjs[0]['createDate']))) + "</td></tr>";
//					
//					var detail = [];
//					var temp = {};
//					$(resultObjs).each(function(i,it){
//						var key = (1 == orderType?it['serviceCatalogName']+'['+it['os']+']':"")+'('+it['pricePeriod']+i18n.get('month')+" ";
//						//var key = it['serviceCatalogName']+'['+it['os']+']'+'('+it['pricePeriod']+i18n.get('month')+" ";
//						if(key in temp){
//							temp[key]["quantity"] ++;
//						}else{
//							it["memory"] += "M";
//							it["disk"] += "G";
//							it["network"] += "M";
//							it["addDisk"]=splitExtDisk(it["addDisk"]);
//							if(it["effectiveDate"]){
//								it["effectiveDate"] = formatDateTime(new Date(parseInt(it['effectiveDate'])));
//							}
//							if(it["expirationDate"]){
//								it["expirationDate"] = formatDateTime(new Date(parseInt(it['expirationDate'])));
//							}
//							temp[key] = it;
//						}
//					});
//					
//					for(var i in temp){
//						var temp2 = i + temp[i]["price"] +i18n.get("yuan")+") x " + temp[i]["quantity"] +" = " + ( temp[i]["price"] * temp[i]["quantity"]) + i18n.get("yuan");
//						temp[i]["price"]+=i18n.get("yuan");
//						detail.push(temp2);
//					}
//					orderGeneral += "<tr><td class='order_detail_table_left'>" + i18n.get("total-perHour") +" </td>";
//					orderGeneral += "<td class='order_detail_table_right'>" + detail.join(";<br/>") + "</td><td></td></tr>";
//					orderGeneral += "</table>";
//					
//					//var conthr = "<hr class='order_detail_hr'>";
//					
//					var contMes = "";
//					for(var j in temp){
//						var resultObj = temp[j];
//						contMes += "<hr class='order_detail_hr'><table class='order_detail_table'>";
//						for(var i=0;i<displayList.length;i++){
//							if(2 == orderType && ("serviceCatalogName" == displayList[i] || "serviceDesc" == displayList[i] || "disk" == displayList[i])){
//								continue;
//							}
//							if(1 == orderType && ("ipNum" == displayList[i])){
//								continue;
//							}
//							contMes += "<tr><td class='order_detail_table_left'>"+orderDetailFields[displayList[i]]+": </td>";
//							contMes += "<td class='order_detail_table_right'>"+(resultObj[displayList[i]] || "-")+"</td></tr>";
//						}
//						contMes += "</table>";
//				    }
					
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

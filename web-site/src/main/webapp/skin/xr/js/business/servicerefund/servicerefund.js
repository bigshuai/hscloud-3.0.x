var pageObje = new PageObje('tableId');
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('business/servicerefund/servicerefund.css');
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



	refundManagementQuery();

});
function i18nPage(){
	$("#query").val(i18n.get("input-machine-number"));
	$("#status").html(i18n.get("refundManagement_businessStatus"));
	$("#all").html(i18n.get("refundManagement_all"));
	$("#applying").html(i18n.get("refundManagement_applying"));
	$("#approved").html(i18n.get("refundManagement_approved"));
	$("#refused").html(i18n.get("refundManagement_refused"));
	$("#canceled").html(i18n.get("refundManagement_canceled"));
	$("#dealtime").html(i18n.get("refundManagement_auditTime"));
	$("#operationstime").html(i18n.get("refundManagement_applyTime"));
	$("#bu_status").html(i18n.get("refundManagement_businessStatus"));
	$("#allOperations").html(i18n.get("refundManagement_operation"));
	$("#ipaddress").html(i18n.get("refundManagement_ipAddress"));
	$("#vmmachine").html(i18n.get("hostName"));
	$("#reason").html(i18n.get("reason"));
	$("#no").html(i18n.get("no."));
	
}

//未支付订单分页展示
function refundManagementQuery() {
	pageObje.size = 8;
	pageObje.methodArray = [
	                         ['uuid','uuidRender'],
	                         ['outerIp','ipRender'],
							 ['status','statusRender'],
							 ['id','operationRender'],
							 ['applyDate','applyDateRender'],
							 ['refundDate','refundDateRender'],
							 ['vmName','vmNameRender'],
							 ['refundReasonType','refundReasonTypeRender']
						   ];
	pageObje.column = [ 'vmName','uuid','outerIp','status','id','applyDate','refundDate','refundReasonType'];		
	pageObje.jsonRequest = {
		"type":1,
		"page" : pageObje.current,
		"limit" : pageObje.size,
		"status":0,
		"query":""
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}


function pageChange(pageObje) {
	pageObje.jsonRequest['page']=pageObje.current;
	url = '../../../order/refundManagement!getVmRefundLogByPage.action';
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
	var queryType = 0;
	$(".checked",$("#queryType")).each(function(){
		queryType = $(this).attr("value");
	});
	var queryValue = $.trim($("#query").val());
	// 去除提示信息,过滤信息
	if (queryValue == i18n.get("input-machine-number")) {
		queryValue = "";
	}
	
	pageObje.jsonRequest = {
		"status" : queryType,
		"query" : queryValue,
		"page" : pageObje.current,
		"limit" : pageObje.size
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

function uuidRender(value,record){
	var statusRenderStr="<span>";
	var refundDate=formatDateTime(new Date(parseInt(record['refundDate'])));
	if(refundDate){
		refundDate+=i18n.get("refundManagement_approved");
	}
	statusRenderStr+=(i18n.get('vm_no_title')+(value==null?"":value)+'<br/><span class="list_double2">'+refundDate+'</span>');
	statusRenderStr+="</span>";
	return statusRenderStr;
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
function operationRender(value,record){
	var businessStatus = record["status"];
	var renderStr="";
	if(businessStatus == 1){
		renderStr+='<span class="refund_button refund_button-rounded refund_button-small refund_button-primary" onclick="cancelRefund('+value+')">'+i18n.get("refundManagement_cancelRefund")+'</span>';
	}
	return renderStr;
}

function applyDateRender(value){
	if(null != value){
		return formatDateTime(new Date(value));
	}else{
		return "";
	}
	
}

function refundDateRender(value){
	if(null != value){
		return formatDateTime(new Date(value));
	}else{
		return "";
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


function ipRender(value,record){
	var ipVal = (value == null ? "" : value);
	var ip = ipVal;
	if (ipVal.split(",").length > 2) {
		ipVal = ipVal.split(",")[0] + "," + ipVal.split(",")[1] + "...";
	}
	var ipRenderStr="<span class='list_double2' title='"+ip+"'>"+ipVal+"</span></span>";
	return ipRenderStr;
}

function refundReasonTypeRender(refundReasonType){
	if (refundReasonType == 1) {
		refundReason = i18n.get("refundManagement_refundtype_1");
	} else if (refundReasonType == 2) {
		refundReason = i18n.get("refundManagement_refundtype_2");
	} else if (refundReasonType == 3) {
		refundReason = i18n.get("refundManagement_refundtype_3");
	} else if (refundReasonType == 4) {
		refundReason = i18n.get("refundManagement_refundtype_4");
	} else if (refundReasonType == 5) {
		refundReason = i18n.get("refundManagement_refundtype_5");
	} else if (refundReasonType == 6) {
		refundReason = i18n.get("refundManagement_refundtype_6");
	} else if (refundReasonType == 7) {
		refundReason = i18n.get("refundManagement_refundtype_7");
	} else if (refundReasonType == 8) {
		refundReason =i18n.get("refundManagement_refundtype_8");
	}else if (refundReasonType == 9) {
		refundReason = i18n.get("refundManagement_refundtype_9");
	}else if (refundReasonType == 10) {
		refundReason = i18n.get("refundManagement_refundtype_10");
	}else{
		refundReason = "";
	}
	var short = "";
	if("en_US"==getCookie('lang')){
		if(refundReason){
			short=refundReason;
			if(short.length>10){
				short=short.substring(0,10)+"...";
			}
		}
	}else{
		if(refundReason){
			short=refundReason;
			if(short.length>4){
				short=short.substring(0,4)+"...";
			}
		}
	}
	return '<span title="'+refundReason+'">'+short+'</span>';
}

function statusRender(value,record){
	var rejectRefundReason = record["rejectRefundReason"];
	var businessStatus = record["status"];
	var statusRenderStr="";
	// 判断业务状态 1.申请中 2.已审批3.拒绝4.已取消
	if(businessStatus == 1){
		businessStatus = i18n.get("refundManagement_applying");
		// 当业务状态这一列显示1.申请中 2.已审批4.已取消时鼠标放在td上面不显示任何东西
		rejectRefundReason = "";
		auditTime = "";
	}else if(businessStatus == 2){
		businessStatus = i18n.get("refundManagement_approved");
		// 当业务状态这一列显示1.申请中 2.已审批4.已取消时鼠标放在td上面不显示任何东西
		rejectRefundReason = "";
	}else if(businessStatus == 3){
		businessStatus = i18n.get("refundManagement_refused");
		// 当业务状态这一列显示为3.拒绝时才显示原因
		rejectRefundReason = 'title="'+rejectRefundReason+'"';
	}else if(businessStatus == 4){
		businessStatus = i18n.get("refundManagement_canceled");
		// 当业务状态这一列显示1.申请中 2.已审批4.已取消时鼠标放在td上面不显示任何东西
		rejectRefundReason = "";
	}
	statusRenderStr+=('<span '+rejectRefundReason+' >'+businessStatus+'</span>');
	
	return statusRenderStr;
}

function changeStatus(doc,value){
	
	$(doc).siblings().each(function(){
		$(this).removeClass("checked");
	});
	$(doc).addClass("checked");
	query();
}

//取消退款申请方法
function cancelRefund(id){
	
	Dialog.confirm(i18n.get("refundManagement_cacel_mesg"), function(){
		$.ajax({
			url : '../../../order/refundManagement!cancelRefundApply.action',
			type : 'Post',
			dataType : 'json',
			data : "vmRefundId=" + id,
			async : true,
			success : function(data) {
				if (data.success == true) {
					query();
				} else {
					Dialog.alert(data.resultMsg);
				}
			},
			error : function() {
				openDialog(i18n.get("vmbuss_error"));
			}
		});
	}, null, 400, 100);
	
}
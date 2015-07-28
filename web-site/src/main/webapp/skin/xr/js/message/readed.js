$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('message/message.css');
	i18n.init();//国际化初始化
	initTagAndMenu(messageTagArr,messagePageUrls);
	i18nPage();//国际化当前页面
	initDialog();
});
function i18nPage(){
	$("#readed").text(i18n.get("readed"));
	$("#toRead").text(i18n.get("toRead"));
	$("#message_content").text(i18n.get("messageSearchTitle"));
	$("#to_span").text(i18n.get("to"));
	$("#btn_message_search").text(i18n.get("messageSearch"));
	$("#message_delete_all_read").text(i18n.get("messageAllDelete"));
	$("#btn_delete").text(i18n.get("toDelete"));
	$("#messageTime").text(i18n.get("messageTime"));
	$("#messageContent").text(i18n.get("messageContent"));
	$("#messageOperating").text(i18n.get("messageOperating"));
}

var pageObje = new PageObje('tableId');
pageObje.size = 8;
pageObje.methodArray = [['createTime','dateFormat'],['tempValue','messageFormat'],['message','resultNullCheck']];
pageObje.column = ['id','status','tempValue','createTime','userId','message'];

var showFlag = false;

function dateFormat(str, array) {
	return formatDateTime(new Date(parseInt(str)));
};

function messageFormat(str, array){
	var messageShort=array['message'];
	if(messageShort.length > 42){
		messageShort=messageShort.substr(0,42)+" ...";
	}
	return messageShort;
};


function resultNullCheck(str, array){
	var messageValue=array['message'];
	if(messageValue == "" || messageValue == null){
		messageValue="ddd";
		return messageValue;
	}else{
		return messageValue;
	}
};

function pageChange(pageObje) {
	pageObje.jsonRequest['pageNo'] = pageObje.current;
	$.ajax({
		url : rootPath + '/message/message!findReadedMessage.action',
		type: 'GET',
		dataType: 'json',
		async : false,
		contentType: "application/json; charset=utf-8",
		data: pageObje.jsonRequest,
		success: function(data){
			if(data.success == true) {   
				pageObje.initFlag = true; 
				pageCreatorR2(pageObje, data.resultObject);
			} else {
				Dialog.alert(data.resultMsg, i18n.get('account_prompt'));
//				alert(data.resultMsg, i18n.get('account_prompt'));
			}
		}
	});
}

function initDatePicker(){
//	if(lang()=="zh_CN"){
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
//	}
	$( "#message_search_date_from" ).datepicker({
		showOn: "button",
		buttonImage: "../../../skin/"+customerPath+"/images/common/calendar.png",
		buttonImageOnly: true
	});
	$( "#message_search_date_to" ).datepicker({
		showOn: "button",
		buttonImage: "../../../skin/"+customerPath+"/images/common/calendar.png",
		buttonImageOnly: true
	});

};

function findReadedMessage(){
	var searchDateFrom=$("#message_search_date_from").val();
	var searchDateTo=$("#message_search_date_to").val();
	var pageno=pageObje.current;
	var limit=pageObje.size;
	if(searchDateFrom == i18n.get("date_from") || searchDateTo ==i18n.get("date_to")){
		Dialog.alert(i18n.get("dateFormateIsError"));
		$('#tableId').show();
		return;
	}
	if(searchDateTo < searchDateFrom){
		Dialog.alert(i18n.get("dateCompare"));
		$('#tableId').show();
	}
	if(searchDateTo !="" && searchDateFrom != ""){
		pageObje.jsonRequest = {
				"pageNo" : pageno,
				"limit" : limit,
				"dateFrom":searchDateFrom,
				"dateTo":searchDateTo,
				"order": 'desc'
		};
		pageObje.initFlag = true;
		pageObje.pageFirst();
		$('#tableId').show();
	}else{
		Dialog.alert(i18n.get("dateSearchNotNull"));
	}
	
	
};

function loadFindReadedMessage(){
	var searchDateFrom=$("#message_search_date_from").val();
	var searchDateTo=$("#message_search_date_to").val();
	var pageno=pageObje.current;
	var limit=pageObje.size;
	if(searchDateTo < searchDateFrom){
		Dialog.alert(i18n.get("dateCompare"));
		$('#tableId').show();
	}
	pageObje.jsonRequest = {
			"pageNo" : pageno,
			"limit" : limit,
			"dateFrom":searchDateFrom,
			"dateTo":searchDateTo,
			"order": 'desc'
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
	$('#tableId').show();
	
};

function messageAllDelete(){
	var Id=id;
	$.ajax({
		url: rootPath+'/message/message!deleteAllMessage.action',
		type:'GET',
		dataType:'json',
		async : false,
		contentType:"application/json; charset=utf-8",
		success:function(data){
			if(data.success == true){
				loadData();
				$('#tableId').show();
			}
			
		}
	});
};

function messageDelete(id){
	var Id=id;
	$.ajax({
		url: rootPath+ '/message/message!deleteMessage.action',
		type:'GET',
		dataType:'json',
		contentType:"application/json; charset=utf-8",
		data:{'id':Id},
		success:function(data){
			if(data.success == true){
				loadData();
				$('#tableId').show();
			}
			
		}
	});
};

function messageAllDelete(){
	$.ajax({
		url: rootPath + '/message/message!deleteAllMessage.action',
		type:'GET',
		dataType:'json',
		contentType:"application/json; charset=utf-8",
		success:function(data){
			if(data.success == true){
//				alert("delete all message success!");
				loadFindReadedMessage();
				initDatePicker();
				$('#tableId').show();
			}
		}
	});
}

function loadData(){
	pageChange(pageObje);
	initDatePicker();
	$("#message_search_date_from").val("");
	$("#message_search_date_to").val("");
//	findReadedMessage();
//	bssInnerShow();
//	AnnoucementInnerAllShow.load();
//	AnnouncementInnerShow(showFlag);
	
};

function formatDateTime(date){
	if("Invalid Date" == date){
		return "";
	}
	/*var locale = top.i18n._lang;
	if (locale == 'zh_CN') {
		return date.format("yyyy年MM月dd日 hh时mm分ss秒");
	}*/
	return date.format("yyyy-MM-dd hh:mm:ss");
};
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

$(function(){
	$("#message_search_date_from").val("");
	$("#message_search_date_to").val("");
//	initDatePicker();
	loadFindReadedMessage();
	initDatePicker();
//	loadData();
});
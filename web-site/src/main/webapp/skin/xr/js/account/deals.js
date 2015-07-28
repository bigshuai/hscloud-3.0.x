var pageObje = new PageObje('tableId');
pageObje.size = 8;
pageObje.methodArray = [['transcationOn','nameMethod'], ['orderNo','orderMethod'], 
                        ['briefDescription', 'briefDescFunc'], ['businessType', 'businessTypeMethod']];
pageObje.column = ['orderNo','transcationOn','businessType','absTrAmount','absBalance','absTrCoupons',
                   'absTrCouponsBalance','description','briefDescription','absTrGifts'];

function get(str) {
	return i18n.get(str);
}

function businessTypeMethod(str, array) {
	/*if(str == '充返点') {
		return '存款';
	}*/
	return str;
}

function orderMethod(str, array) {
	if(str == 'null' || str == null) {
		return '';
	}
	return str;
}

function briefDescFunc(str, array) {
	var description = array['description'];
	if(description!=null && description.length > 10) {
		description = description.substring(0, 18) + '...';
	}
	return description;
}

function nameMethod(str, array) {
	return formatDateTime(new Date(parseInt(str)));
}

function search_onfocus(obj) {
	if(obj.value==get('account_inputOrder')) obj.value='';
}

function search_onblur(obj) {
	if(obj.value=='') obj.value=get('account_inputOrder');
}

function search_onkeydown(keyCode) {
	if (keyCode==13) {query();}
}

function pageChange(pageObje) {
	pageObje.jsonRequest['page'] = pageObje.current;
	$.ajax({
		url : '../../../bss/log!pageLog.action',
		type: 'GET',
		dataType: 'json',
		async : false,
		contentType: "application/json; charset=utf-8",
		data: pageObje.jsonRequest,
		success: function(data){
			if(data.success == true) {
				pageCreatorR2(pageObje, data.resultObject);
			} else {
				openDialog(data.resultMsg, get('account_prompt'));
			}
		}
	});
}

function query() {
	var query = $('#query').val();
	if(query == get('account_inputOrder')) {
		query = '';
	}
	pageObje.jsonRequest = {
		"query" : query,
		"page" : pageObje.current,
		"limit" : pageObje.size,
	    "sort": "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

$(document).ready(function() {
	i18n.init();//国际化初始化
	initTagAndMenu(accountTagNameArr,accountPageUrls);
	$("#unreaded_search_btn_by_date").attr("src",rootPath+"/skin/"+customerPath+"/images/message/search.png");
	
	$('#indexPage_tit').text(get('account_no'));
	$('#transcationOn_tit').text(get('account_transcationOn'));
	$('#businessType_tit').text(get('account_businessType'));
	$('#absTrAmount_tit').text(get('account_amount'));
	$('#absBalance_tit').text(get('account_accountBalance2'));
	$('#absTrCoupons_tit').text(get('account_trans_coupons'));
//	$('#absTrCouponsBalance_tit').text(get('account_couponsBalance2'));
	$('#absTrGifts_tit').text(get('account_trans_gifts'));
	$('#briefDescription_tit').text(get('account_description'));
	
	$('#query').val(get('account_inputOrder'));
	query();
});

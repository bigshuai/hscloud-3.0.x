$("document").ready(function() {
	var url = window.location.search;
	var totalFee = '';
	var defaultbank = '';
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		var paramsArr = str.split("&");
		if (paramsArr.length == 2) {
			totalFee = paramsArr[0].split('=')[1];
			defaultbank = paramsArr[1].split('=')[1];
		} else {
			alert('request parameter error');
			return;
		}

	}
	var jsonRequest = {
		"total_fee" : totalFee,
		"defaultbank" : defaultbank
	};
	var requstUrl = '../../../bss/payment!alipayDirectPay.action';
	$.ajax({
		url : requstUrl,
		data : jsonRequest,
		async : false,
		dataType : "text",
		error : function() {
			alert("failure_error");
		},
		success : function(responseText) {
			if (responseText != "failure") {
				$("#contentDiv").append(responseText);
			} else {
				alert(lang.user.account.accountRechargeFailureTip);
			}
		}
	});
});


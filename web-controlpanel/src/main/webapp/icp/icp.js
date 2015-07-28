var REMOTE_URL = "../";
var timeOutUrl =  REMOTE_URL+"index.html";
$(function(){
	registeredClick();
	
	var url = "../icp/icp!initIcp.action";
	$.ajax({
		async : false,
		type : "post",
		url : url,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
				return;
			}
			var ipList = json.resultObject.ipList;
			if(ipList.length < 1) {
				$("#buttonDivId").hide();
				$("#icpTdId").hide();
				$("#noIP").show();
				return;
			}
			$.each(ipList, function(index, value) {
				$("#ip")[0].options.add(new Option(ipList[index], ipList[index], false, false));
			});
			var provinceList = json.resultObject.provinceList;
			$.each(provinceList, function(index, value) {
				$("#province")[0].options.add(new Option(provinceList[index].provinceName, 
						provinceList[index].provinceCode, false, false));
			});
			var cityList = json.resultObject.cityList;
			$.each(cityList, function(index, value) {
				$("#city")[0].options.add(new Option(cityList[index].cityName, 
						cityList[index].cityCode, false, false));
			});
			
			/*var icp = json.resultObject.icpVO;
			$('#name').val(icp.name);
			$('#contactName').val(icp.contactName);
			$('#idType').val(icp.idType);
			$('#idNumber').val(icp.idNumber);
			$('#email').val(icp.email);
			$("#province").val(icp.province);
			$('#telno').val(icp.telno);
			$("#mobile").val(icp.mobile);
			$("#address").val(icp.address);
			
			$("input[name=memberType]").each(function(index){
				if(this.value == icp.memberType) {
					this.checked = true;
				}
			});*/
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
});

function submitClick() {
	var domain = $("#domain").val();
	var ip = $("#ip").val();
	var registered = '';
	$("input[name=registered]").each(function(index){
		if(this.checked == true) {
			registered = this.value;
		}
	});
	var memberLogin = $("#memberLogin").val();
	var memberPwd = $("#memberPwd").val();
	var rePwd = $("#rePwd").val();
	var memberType = '';
	$("input[name=memberType]").each(function(index){
		if(this.checked == true) {
			memberType = this.value;
		}
	});
	var name = $("#name").val();
	var orgcertId = $("#orgcertId").val();
	var icpcertId = $("#icpcertId").val();
	var icpUrl = $("#icpUrl").val();
	
	var icpwebTitle = $("#icpwebTitle").val();
	var contactName = $("#contactName").val();
	var idType = $("#idType").val();
	var idNumber = $("#idNumber").val();
	var country = $("#country").val();
	var province = $("#province").val();
	var city = $("#city").val();
	
	var address = $("#address").val();
	var postcode = $("#postcode").val();
	var telno = $("#telno").val();
	var mobile = $("#mobile").val();
	var email = $("#email").val();
	
	/*var data = "icpVO.domain=" + domain + "&icpVO.ip=" + ip + "&icpVO.registered=" + registered + 
				"&icpVO.memberLogin=" + memberLogin + "&icpVO.memberPwd=" + memberPwd + 
				"&icpVO.memberType=" + memberType + "&icpVO.name=" + name + "&icpVO.orgcertId=" + orgcertId + 
				"&icpVO.icpcertId=" + icpcertId + "&icpVO.icpUrl=" + icpUrl +
				"&icpVO.icpwebTitle=" + icpwebTitle + "&icpVO.contactName=" + contactName + "&icpVO.idType=" + idType + 
				"&icpVO.idNumber=" + idNumber + "&icpVO.country=" + country + 
				"&icpVO.province=" + province + "&icpVO.city=" + city + "&icpVO.address=" + address + 
				"&icpVO.postcode=" + postcode + "&icpVO.telno=" + telno + 
				"&icpVO.mobile=" + mobile + "&icpVO.email=" + email;*/
	var data = '{"domain":"' + domain + '","ip":"' + ip + '","registered":"' + registered + 
				'","memberLogin":"' + memberLogin + '","memberPwd":"' + memberPwd + 
				'","memberType":"' + memberType + '","name":"' + name + '","orgcertId":"' + orgcertId + 
				'","icpcertId":"' + icpcertId + '","icpUrl":"' + icpUrl +
				'","icpwebTitle":"' + icpwebTitle + '","contactName":"' + contactName + '","idType":"' + idType + 
				'","idNumber":"' + idNumber + '","country":"' + country + 
				'","province":"' + province + '","city":"' + city + '","address":"' + address + 
				'","postcode":"' + postcode + '","telno":"' + telno + 
				'","mobile":"' + mobile + '","email":"' + email +'"}';
//	var data = '{"domain":"' + domain + '"}';
	if(validateData(registered) == true) {
		return;
	}
	
	var url = "../icp/icp!icpPutOnRecord.action";
	$.ajax({
		async : false,
		type : "post",
		url : url,
		dataType : 'json',
		contentType: "application/json; charset=utf-8",
		data : data,
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			} else {
				customizeAlert("备案申请已受理，请查看邮箱回复！");
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
}

function registeredClick() {
	$("input[name=registered]").each(function(index){
		if(this.checked == true && this.value == 1) {
			$("#confirmTrId").css('display', 'none');
			$(".formTableId2").css('display', 'none');
			$("#registeredMsgDivId").show();
			$("#unRegisteredMsgDivId").hide();
		}
		if(this.checked == true && this.value == 0) {
			$("#confirmTrId").css('display', '');
			$(".formTableId2").css('display', '');
			$("#registeredMsgDivId").hide();
			$("#unRegisteredMsgDivId").show();
		}
	});
}

function validateData(registered) {
	
	var msgFlag = checkDomain();
	var msgFlag = checkIp() || msgFlag;
	var msgFlag = checkMemberLogin() || msgFlag;
	var msgFlag = checkMemberPwd() || msgFlag;
	
	if(registered == 1) {
		return msgFlag;
	}
	var msgFlag = checkRePwd() || msgFlag;
	var msgFlag = checkName() || msgFlag;
	var msgFlag = checkOrgcertId() || msgFlag;
	var msgFlag = checkIcpcertId() || msgFlag;
	var msgFlag = checkIcpUrl() || msgFlag;
	var msgFlag = checkIcpwebTitle() || msgFlag;
	var msgFlag = checkContactName() || msgFlag;
	var msgFlag = checkIdNumber() || msgFlag;
	var msgFlag = checkAddress() || msgFlag;
	
	var msgFlag = checkPostcode() || msgFlag;
	var msgFlag = checkTelno() || msgFlag;

	var msgFlag = checkMobile() || msgFlag;
	var msgFlag = checkEmail() || msgFlag;
	
	return msgFlag;
}

function getMemberType() {
	var memberType = '';
	$("input[name=memberType]").each(function(index){
		if(this.checked == true) {
			memberType = this.value;
		}
	});
	return memberType;
}

function checkDomainFormat(id, name) {
	var str = $("#" + id).val()
	//var pattern = /^([\w-]+\.)+((ph)|(so)|(asia)|(ru)|(com)|(net)|(org)|(gov\.cn)|(info)|(cc)|(com\.cn)|(net\.cn)|(org\.cn)|(name)|(biz)|(tv)|(cn)|(mobi)|(name)|(sh)|(ac)|(io)|(tw)|(com\.tw)|(hk)|(com\.hk)|(ws)|(travel)|(us)|(tm)|(la)|(me\.uk)|(org\.uk)|(ltd\.uk)|(plc\.uk)|(in)|(eu)|(it)|(jp))$/;
	
	//if(!pattern.test(str)){
	//	var msg = name + "格式错误。"
	//	$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
	//	return true;
	//}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return false;
}

function checkDomain() {
	var msgFlag = checkNull("domain", "域名不能为空。");
	msgFlag = msgFlag || checkLength("domain", 255, "域名过长。");
	msgFlag = msgFlag || checkDomainFormat("domain", "域名");
	return msgFlag;
}

function checkIp() {
	var msgFlag = checkNull("ip", "IP地址不能为空。");
	return msgFlag;
}

function checkMemberLogin() {
	var msgFlag = checkNull("memberLogin", "www.xrnet.cn会员登录名称不能为空。");
	msgFlag = msgFlag || checkEmailBase("memberLogin", "www.xrnet.cn会员登录名称格式不合规范。");
	msgFlag = msgFlag || checkLength("memberLogin", 255, "www.xrnet.cn会员登录名称过长。");
	return msgFlag;
}

function checkMemberPwd() {
	var id = "memberPwd";
	var msgFlag = checkNull(id, "www.xrnet.cn会员登录密码不能为空。");
	msgFlag = msgFlag || checkLength("memberPwd", 32, "www.xrnet.cn会员登录密码过长。");
	if(msgFlag == true) {
		return msgFlag;
	}
	var memberPwd = $("#" + id).val();
	if(memberPwd.length < 6) {
		var msg = "www.xrnet.cn会员登录密码不能小于6位。";
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return msgFlag;
}

function checkRePwd() {
	var id = "rePwd";
	var msgFlag = checkNull(id, "确认密码不能为空。");
	msgFlag = msgFlag || checkLength("rePwd", 32, "确认密码过长。");
	if(msgFlag == true) {
		return msgFlag;
	}
	var rePwd = $("#" + id).val();
	var memberPwd = $("#memberPwd").val();
	if(memberPwd != rePwd) {
		var msg = "确认密码和www.xrnet.cn会员登录密码不一致。"
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return msgFlag;
}

function checkName() {
	var msgFlag = checkNull("name", "公司名称/姓名不能为空。");
	msgFlag = msgFlag || checkLength("name", 255, "公司名称/姓名过长。");
	return msgFlag;
}


function checkOrgcertId() {
	var msgFlag = checkLength("orgcertId", 50, "组织机构代码证过长。");
	
	return msgFlag;
	/*var memberType = getMemberType();
	if(memberType == "2") {
		return msgFlag;
	}
	msgFlag = msgFlag || checkNull("orgcertId", "组织机构代码证不能为空。");
	return msgFlag;*/
}

function checkIcpcertId() {
	var msgFlag = checkLength("icpcertId", 50, "ICP证号代码证过长。");
	return msgFlag;
	/*var memberType = getMemberType();
	if(memberType == "2") {
		return msgFlag;
	}
	msgFlag = msgFlag || checkNull("icpcertId", "ICP证号不能为空。");
	return msgFlag;*/
}


function checkIcpUrl() {
	var msgFlag = checkLength("icpUrl", 255, "（ICP）网址过长。");
	return msgFlag;
	/*var memberType = getMemberType();
	if(memberType == "2") {
		return msgFlag;
	}
	msgFlag = msgFlag || checkNull("icpUrl", "（ICP）网址不能为空。");
	return msgFlag;*/
}


function checkIcpwebTitle() {
	var msgFlag = checkLength("icpwebTitle", 50, "（ICP）网站名称过长。");
	return msgFlag;
	/*var memberType = getMemberType();
	if(memberType == "2") {
		return msgFlag;
	}
	msgFlag = msgFlag || checkNull("icpwebTitle", "（ICP）网站名称不能为空。");
	return msgFlag;*/
}

function checkContactName() {
	var msgFlag = checkLength("contactName", 255, "联系人姓名过长。");
	var memberType = getMemberType();
	if(memberType == "2") {
		return msgFlag;
	}
	msgFlag = msgFlag || checkNull("contactName", "联系人姓名不能为空。");
	return msgFlag;
}

//去掉字符串左边的空格 
function ltrim(s) {
    if (s == null) return "";
    var whitespace = new String(" \t\n\r");
    var str = new String(s);
    if (whitespace.indexOf(str.charAt(0)) != -1) {
        var j = 0, i = str.length;
        while (j < i && whitespace.indexOf(str.charAt(j)) != -1) {
            j++;
        }
        str = str.substring(j, i);
    }
    return str;
}

//去掉字符串右边的空格 
function rtrim(s) {
    if (s == null) return "";
    var whitespace = new String(" \t\n\r");
    var str = new String(s);
    if (whitespace.indexOf(str.charAt(str.length - 1)) != -1) {
        var i = str.length - 1;
        while (i >= 0 && whitespace.indexOf(str.charAt(i)) != -1) {
            i--;
        }
        str = str.substring(0, i + 1);
    }
    return str;
} 

function checkIdcard(idcard1) {
    //debugger;
    var idcard = rtrim(ltrim(idcard1)); // 对身份证号码做处理。去除头尾空格。
    var area = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" }

    var idcard, Y, JYM;
    var S, M;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    /*基本校验*/
    if (idcard == "" || idcard == null || idcard.length == 0) {
        return false;
    }
    /*地区检验*/
    if (area[parseInt(idcard.substr(0, 2))] == null) {
        return false;
    }
    /*身份号码位数及格式检验*/
    switch (idcard.length) {
        /*case 15:
            if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; //测试出生日期的合法性 
            } else {
                ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; //测试出生日期的合法性 
            }
            if (ereg.test(idcard)) {
                // return true; //15位验证通过 
                return true;
            }
            else {
                return false;
            }
            break;*/

        case 18:
            //18位身份号码检测 
            //出生日期的合法性检查 
            //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9])) 
            //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8])) 
            if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
                ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; //闰年出生日期的合法性正则表达式 
            } else {
                ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; //平年出生日期的合法性正则表达式 
            }
            if (ereg.test(idcard)) {//测试出生日期的合法性 
                //计算校验位 
                S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
            + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
            + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
            + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
            + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
            + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
            + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
            + parseInt(idcard_array[7]) * 1
            + parseInt(idcard_array[8]) * 6
            + parseInt(idcard_array[9]) * 3;
                Y = S % 11;
                M = "F";
                JYM = "10X98765432";
                M = JYM.substr(Y, 1); /*判断校验位*/
                if (M == idcard_array[17].toUpperCase()) {
                    //alert(Errors[0]+"18");
                    //  return true; /*检测ID的校验位false;*/
                    return true;
                }
                else {
                    return false;
                }
            } else {
                return false;
            }
            break;
        default:
            return false;
    }
}

function checkIdNumber() {
	var id = "idNumber"
	var msgFlag = checkNull(id, "身份证件号码不能为空。");
	msgFlag = msgFlag || checkLength("idNumber", 40, "身份证件号码过长。");
	if(msgFlag == true) {
		return msgFlag;
	}
	
	var idType = $("#idType").val();
	var idNumber = $("#idNumber").val();
	if(idType == "1") {
		if(!checkIdcard(idNumber)) {
			var msg = "身份证格式不合规范。"
    		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
    		return true;
		}
	}
	if(idType == "2") {
		reg = /(P\d{7})|(G\d{8})/;
        if(!reg.test(idNumber)) {
    		var msg = "护照格式不合规范。"
    		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
    		return true;
    	}
	}
	if(idType == "3") {
		reg = /^\d{1,}$/;
        if(!reg.test(idNumber)) {
    		var msg = "学生证格式不合规范。"
    		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
    		return true;
    	}
	}
	if(idType == "4") {
		reg = /^\d{1,}$/;
        if(!reg.test(idNumber)) {
    		var msg = "军官证格式不合规范。"
    		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
    		return true;
    	}
	}
	
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return msgFlag;
}


function checkAddress() {
	var msgFlag = checkNull("address", "地址不能为空。");
	msgFlag = msgFlag || checkLength("address", 80, "地址过长。");
	return msgFlag;
}


function checkPostcode() {
	var id = "postcode"
	var msgFlag = checkNull(id, "邮政编码不能为空。");
	if(msgFlag == true) {
		return msgFlag;
	}
	var reg =/^[0-9]{6}$/;
	if(!reg.test($("#" + id).val())) {
		var msg = "邮政编码格式不合规范。"
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return msgFlag;
}


function checkTelno() {
	var id = "telno";
	var msgFlag = checkNull(id, "电话不能为空。");
	if(msgFlag == true) {
		return;
	}
	var reg = /(^[0-9]{3,4}\-[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{3,4}\-[0-9]{7,8}$)/;
	if(!reg.test($("#" + id).val())) {
		var msg = "电话格式不合规范。正确格式为：888-8888888,888-88888888-1234。"
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return msgFlag;
}


function checkMobile() {
	var id = "mobile";
	var msgFlag = checkNull(id, "手机号不能为空。");
	if(msgFlag == true) {
		return;
	}
	var reg = /^0{0,1}1[0-9]{10}$/;
	if(!reg.test($("#" + id).val())) {
		var msg = "手机号格式不合规范。";
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return msgFlag;
}


function checkEmail() {
	if($("#email").val() == '' || $("#email").val() == null) {
		return false;
	}
	var msgFlag =  checkEmailBase("email", "Email地址格式不合规范");
	msgFlag = msgFlag || checkLength("email", 255, "Email地址过长。");
	return msgFlag;
}

function checkEmailBase(id, msg) {
	var email = $("#" + id).val();
	//var reg = /^([a-zA-Z0-9_-])+.?([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(!reg.test(email)) {
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return false;
}

function checkNull(id, msg) {
	if($("#" + id).val() == '' || $("#" + id).val() == null) {
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return false;
}

function checkLength(id, maxLength, msg) {
	var str = $("#" + id).val();
	var length = str.replace(/[^\x00-\xff]/g, "**").length;
	if(length > maxLength) {
		$("#" + id + "Msg").show().attr("alt", msg).attr("title", msg).attr("src","../images/error.png");
		return true;
	}
	$("#" + id + "Msg").attr("alt", "").attr("title", "").hide();
	return false;
}

function resetForm() {
	$("form")[0].reset();
	registeredClick();
	$("img[id$=Msg]").hide();
	changeProvince($("#province")[0]);
}

function changeProvince(obj) {
	var provinceCode = obj.value;
	
	var url = "../icp/icp!changeProvince.action";
	$.ajax({
		async : false,
		type : "post",
		url : url,
		dataType : 'json',
		data : "icpVO.province=" + provinceCode,
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
				return;
			}
			$("#city").empty();
			
			var cityList = json.resultObject;
			$.each(cityList, function(index, value) {
				$("#city")[0].options.add(new Option(cityList[index].cityName, 
						cityList[index].cityCode, false, false));
			});
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
}
/**
 * 超时处理，跳转重新登录
 * @param XMLHttpRequest
 */
function redirectLogin(XMLHttpRequest){
	var message = XMLHttpRequest.responseText;
	if(message.indexOf('登录')>0){ 
		customizeAlert(lang.vpdc.center.timeout);
		window.location.href = timeOutUrl;
	}
}
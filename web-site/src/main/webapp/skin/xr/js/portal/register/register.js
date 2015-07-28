/**
 * 初始化注册页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('portal/col/header.js');
	initJs('portal/col/footer.js');
	initCss('portal/register/register.css');
	initDialog();//加载dialog的css和Js
	i18n.init();//国际化初始化
	initI18nPage();//国际化当前页面
	$("#registerDiv").css("background","url("+rootPath+"/skin/"+customerPath+"/images/portal/register/table.png"+") repeat-x scroll 0 0 #FFFFFF");
	gotoRunning();//加载国家,地区,行业后台数据，以及初始化界面功能
});
var localUser;

function initI18nPage() {
	$("#userRegister").text(i18n.get("userRegister"));
	$("#login-email").text(i18n.get("login-email"));
	$("#emailmessage").text(i18n.get("emailmessage"));
	$("#login-password").text(i18n.get("login-password"));
	$("#pwdmessage").text(i18n.get("pwdmessage"));
	$("#confirm-password").text(i18n.get("confirm-password"));
	$("#repwdmessage").text(i18n.get("repwdmessage"));
	$("#sex-label").text(i18n.get("sex-label"));
	$("#male-label").text(i18n.get("male-label"));
	$("#female-label").text(i18n.get("female-label"));
	$("#usertype-label").text(i18n.get("usertype-label"));
	$("#choose").text(i18n.get("choose"));
	$("#personal").text(i18n.get("personal"));
	$("#enterprise").text(i18n.get("enterprise"));
	$("#usertypemessage").text(i18n.get("usertypemessage"));
	$("#company-label").text(i18n.get("company-label"));
	$("#companymessage").text(i18n.get("companymessage"));
	$("#username-label").text(i18n.get("username-label"));
	$("#usernamemessage").text(i18n.get("usernamemessage"));
	$("#idCard-label").text(i18n.get("idCard-label"));
	$("#idcardmessage").text(i18n.get("idcardmessage"));
	$("#country-label").text(i18n.get("country-label"));
	$("#region-label").text(i18n.get("region-label"));
	$("#address-label").text(i18n.get("address-label"));
	$("#addressmessage").text(i18n.get("addressmessage"));
	$("#industry-label").text(i18n.get("industry-label"));
	$("#contactway-label").text(i18n.get("contactway-label"));
	$("#contactwaymessage").text(i18n.get("contactwaymessage"));
	$("#readAndAgree").text(i18n.get("readAndAgree"));
	$("#serviceTerm").text(i18n.get("serviceTerm"));
	$("#submit").text(i18n.get("submit"));
	$("#reset").text(i18n.get("reset"));
}

var regionStore = null;
/**
 * 初始化加载后台数据:从后台数据库加载所在国家,所在地区,所在行业到下拉菜单中
 */
function gotoRunning() {
	// 加载国家和地区
	$.ajax({
		url : '../user_mgmt/basicData!loadCountry.action',
		type : 'POST',
		dataType : 'json',
		async : false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			var countrys = data['resultObject'];
			regionStore = countrys;
			$("#country").empty();
			$.each(countrys, function(index, value) {
				$("#country")[0].options.add(new Option(i18n.get("country" + value['nameCode']), value['id'], false, false));
			});
			reloadRegion();
		}
	});
	// 加载行业
	$.ajax({
		url : '../user_mgmt/basicData!loadIndustry.action',
		type : 'POST',
		dataType : 'json',
		async : false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			var industry = data['resultObject'];
			$("#industry").empty();
			$.each(industry, function(index, value) {
				$("#industry")[0].options.add(new Option(i18n.get("industry" + value['nameCode']), value['id'], false, false));
			});
			reloadRegion();
		}
	});	
	$("#country").change(reloadRegion);//国家地区2级联动
	//按从上到下顺序检查相关的输入是否符合规范
	$("#email").blur(checkEmail);
	$("#password").blur(checkPassword);
	$("#repassword").blur(checkRePassword);
	$("#company").blur(checkCompany);
	$("#username").blur(checkUsername);
	$("#idCard").blur(checkIDCard);
	$("#address").blur(checkeAddress);
	$("#contactway").blur(checkContactway);
	$("#agreeTerm").click(function() {//勾选了同意条款
		if ($("#agreeTerm").attr("value") == "false") {
			$("#agreeTerm").attr("value", "true");
			$("#serviceTermMessage").text("");
		} 
		else {
			$("#agreeTerm").attr("value", "false");
			verifyStyle(false, "serviceTermMessage", i18n.get("agreeServiceTerm"));
		}
	});
	var userId = getUrlParam("userId");
	//alert('##'+userId);
	// 加载客户信息,如果存在从客户方跳转过来的请求，并且能请求到客户信息，则进行相关条目的填充
	if(userId !=undefined && userId != null &&  userId !=""){
		$.ajax({
			url : '../user_mgmt/user!getOuterInfomation.action',
			type : 'GET',
			dataType : 'json',
			data: {userId:userId,domainCode:domainCode},
			async : false,
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				var resultObject = data['resultObject'];
				var user=resultObject['user'];//后面的邮箱校验也要用
				var outInformation=resultObject['outInformation'];
				localUser=user;
				if(user!=null){
				if(user.enable!="3"){				
				//alert(outInformation);
				if(outInformation != null && outInformation !=""){				
					$("#email").val(outInformation.email);
					$("#username").val(outInformation.name);
					$("#address").val(outInformation.address);
					$("#contactway").val(outInformation.phone);
					if(user != null && user !=""){
					$("#company").val(user['userProfile'].company);
					$("#idCard").val(user['userProfile'].idCard);
					$("#usertype").val(user.userType);
					$("#country").val(user['userProfile'].country.id);
					$("#region").val(user['userProfile'].region.id);
					$("#industry").val(user['userProfile'].industry.id);
					//alert(user['domain'].bank);
					}
				}
				}else{
					alert("用户已注册，并已审批通过，请直接登陆");
					$("#email").val("");
					$("#password").val("");
					$("#repassword").val("");
					$('#usertype option:first').prop("selected", 'selected');
					$("#company").val("");
					$("#username").val("");
					$("#idCard").val("");
					$('#country option:first').prop("selected", 'selected');
					reloadRegion();
					$('#region option:first').prop("selected", 'selected');
					$("#address").val("");
					$('#industry option:first').prop("selected", 'selected');
					$("#contactway").val("");
					$("#serviceTermMessage").text("");
					$("#email").css('border', '1px solid #CCCCCC');		
					$("#password").css('border', '1px solid #CCCCCC');		
					$("#repassword").css('border', '1px solid #CCCCCC');
					$("#usertype").css('border', '1px solid #CCCCCC');
					$("#company").css('border', '1px solid #CCCCCC');		
					$("#username").css('border', '1px solid #CCCCCC');
					$("#idCard").css('border', '1px solid #CCCCCC');		
					$("#address").css('border', '1px solid #CCCCCC');		
					$("#contactway").css('border', '1px solid #CCCCCC');		
					$("#emailmessage").empty().removeAttr("style").html(i18n.get("emailmessage"));
					$("#pwdmessage").empty().removeAttr("style").html(i18n.get("pwdmessage"));
					$("#repwdmessage").empty().removeAttr("style").html(i18n.get("repwdmessage"));
					$("#usertypemessage").empty().removeAttr("style").html(i18n.get("usertypemessage"));
					$("#companymessage").empty().removeAttr("style").html(i18n.get("companymessage"));
					$("#usernamemessage").empty().removeAttr("style").html(i18n.get("usernamemessage"));
					$("#idcardmessage").empty().removeAttr("style").html(i18n.get("idcardmessage"));
					$("#addressmessage").empty().removeAttr("style").html(i18n.get("addressmessage"));
					$("#contactwaymessage").empty().removeAttr("style").html(i18n.get("contactwaymessage"));
				}
				}else{
					if(outInformation != null && outInformation !=""){				
						$("#email").val(outInformation.email);
						$("#username").val(outInformation.name);
						$("#address").val(outInformation.address);
						$("#contactway").val(outInformation.phone);}
				}
			}
		});	
	}	
	$("#submit").click(function() {//点击提交按钮
		var href = window.location.href;
		//var user_source = href.split("?");
		var user_source = "";
//		if (user_source.length > 1) {
//			user_source = user_source[1].split("=")[1];
//		} else {
//			user_source = "";
//		}
		var email = $("#email").val();
		var username = $("#username").val();
		var companyName = $("#company").val();
		var password = $("#password").val();
		var enCode = $.md5(password);
		var sex = $('input:radio[name="sex"]:checked').val();
		var usertype = $("#usertype").find('option:selected').val();
		var idCard = $("#idCard").val();
		var country = $("#country").find('option:selected').val();
		var industry = $("#industry").find('option:selected').val();
		var region = $("#region").find('option:selected').val();
		var contactway = $("#contactway").val();
		var address = $("#address").val();
		var domainId;
		$.ajax({// 获取damainId
			url : '../user_mgmt/basicData!loadDomain.action?code=' + domainCode,
			type : 'POST',
			dataType : 'json',
			async : false,
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				domainId = data.resultObject.id;
			}
		});		
		var jsonRequest = "{\"name\":\"" + username + "\",\"email\":\"" + email
						+ "\",\"user_source\":\"" + user_source	+ "\",\"password\":\"" + enCode
						+ "\",\"domain\": {\"id\":\"" + domainId + "\"},\"userType\":\"" + usertype
						+ "\",\"userProfile\":{\"sex\":\"" + sex + "\",\"idCard\":\"" + idCard
						+ "\",\"address\":\"" + address + "\",\"telephone\":\"" + contactway
						+ "\",\"industry\": {\"id\":\"" + industry + "\"},\"country\":{\"id\":\""
						+ country + "\"}"+(("undefined"==region || null ==region)?"":",\"region\":{\"id\" : \""
						+ region + "\"}")+"}}";	
//		$("#email").val(outInformation.email);
//		$("#username").val(outInformation.name);
//		$("#address").val(outInformation.address);
//		$("#contactway").val(outInformation.phone);
//		if(user != null && user !=""){
//		$("#company").val(user['userProfile'].company);
//		$("#idCard").val(user['userProfile'].idCard);
//		$("#usertype").val(user.userType);
//		$("#country").val(user['userProfile'].country.id);
//		$("#region").val(user['userProfile'].region.id);
//		$("#industry").val(user['userProfile'].industry.id);
		/*user.email=email;
		user.name=username*/
		/*alert(user.name);
		user.address=address;
		user.phone=contactway;
		user.userProfile.company=companyName;
		alert(user.userProfile.company);
		user.userProfile.country=country;
		user.userProfile.region=region;*/
		//alert(user.userProfile.region);
		
		/*user.userProfile.industry=industry;*///var us = JSON.stringify(user).replace(',', ', ').replace('[', '').replace(']', '');
		
		//user.oldPassword=
		if (checkEmail() & checkPassword() & checkRePassword() & checkUsertype() & checkCompany() & checkUsername() 
				& checkIDCard() & checkeAddress() & checkContactway() & checkServiceTerm()) {
			if(localUser==null || localUser.email==null || localUser.email==""){
				$.ajax({
					url : "../user_mgmt/user!createUser.action?user_type="+usertype+"&companyName="+encodeURIComponent(companyName)+"&domainCode="+domainCode+"&externalUser="+getUrlParam("userId")+"&localUser="+email,
					type : 'POST',
					dataType : 'json',
					async : false,
					contentType : "application/json; charset=utf-8",
					data: jsonRequest,
					success : function(data) {
						if (data.success){
							window.location.href = "../portal/registerSuccess.html?email="+email;
						}
						else
							alert(data.resultMsg);
					}
				});
			}else if(localUser.email!=null && localUser.email!=""){
				$.ajax({
					url : "../user_mgmt/user!modifyUserInf.action?userId="+localUser.id+"&companyName="+encodeURIComponent(companyName)+"&domainCode="+domainCode,
					type : 'POST',
					dataType : 'json',
					async : false,
					contentType : "application/json; charset=utf-8",
					//data: {"user.id":user.id,"user.name":username,"user.email":email,"user.address":address,"user.phone":contactway,"user.userProfile.company":companyName,"user.userProfile.country":country,"user.userProfile.region":region,"user.userProfile.industry":industry,"user.userProfile.company":user.userProfile.company
					data: jsonRequest,
					success : function(data) {
						if (data.success){					
							alert("修改用户信息成功");
							window.location.href = "../portal/registerSuccess.html?email="+email;
						}
						else
							alert(data.resultMsg);
					}
				});
			}			
		}
	});	
	$("#reset").click(function() {//重置注册页面
		$("#email").val("");
		$("#password").val("");
		$("#repassword").val("");
		$('#usertype option:first').prop("selected", 'selected');
		$("#company").val("");
		$("#username").val("");
		$("#idCard").val("");
		$('#country option:first').prop("selected", 'selected');
		reloadRegion();
		$('#region option:first').prop("selected", 'selected');
		$("#address").val("");
		$('#industry option:first').prop("selected", 'selected');
		$("#contactway").val("");
		$("#serviceTermMessage").text("");
		$("#email").css('border', '1px solid #CCCCCC');		
		$("#password").css('border', '1px solid #CCCCCC');		
		$("#repassword").css('border', '1px solid #CCCCCC');
		$("#usertype").css('border', '1px solid #CCCCCC');
		$("#company").css('border', '1px solid #CCCCCC');		
		$("#username").css('border', '1px solid #CCCCCC');
		$("#idCard").css('border', '1px solid #CCCCCC');		
		$("#address").css('border', '1px solid #CCCCCC');		
		$("#contactway").css('border', '1px solid #CCCCCC');		
		$("#emailmessage").empty().removeAttr("style").html(i18n.get("emailmessage"));
		$("#pwdmessage").empty().removeAttr("style").html(i18n.get("pwdmessage"));
		$("#repwdmessage").empty().removeAttr("style").html(i18n.get("repwdmessage"));
		$("#usertypemessage").empty().removeAttr("style").html(i18n.get("usertypemessage"));
		$("#companymessage").empty().removeAttr("style").html(i18n.get("companymessage"));
		$("#usernamemessage").empty().removeAttr("style").html(i18n.get("usernamemessage"));
		$("#idcardmessage").empty().removeAttr("style").html(i18n.get("idcardmessage"));
		$("#addressmessage").empty().removeAttr("style").html(i18n.get("addressmessage"));
		$("#contactwaymessage").empty().removeAttr("style").html(i18n.get("contactwaymessage"));
	});
}

/**
 * 动态加载地区,2级联动
 */
function reloadRegion() {
	var countryid = $("#country").val();
	$("#region").empty();
	var regions = null;
	$.each(regionStore, function(index, value) {
		if (value['id'] == countryid)
			regions = value['regions'];
	});
	$.each(regions, function(index, value) {
		$("#region")[0].options.add(new Option(i18n.get("region" + value['nameCode']), value['id'], false, false));
	});
}

/**
 * 检查是否点击了我已阅读并同意《HS Cloud 服务条款》这个checkbox
 */
function checkServiceTerm() {
	if ($("#agreeTerm").attr("value") == "true") {
		$("#serviceTermMessage").text("");
		return true;
	} 
	else {
		verifyStyle(false, "serviceTermMessage", i18n.get("agreeServiceTerm"));
		return false;
	}
}

/**
 * 检查邮箱
 */
function checkEmail() {
	//alert(getUrlParam("userId"));
	//alert(localUser.email);
	var flag = false;
	var email = $("#email").val();
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (null == email || "" == email) {
		verifyStyle(false, "emailmessage", i18n.get("emailErrorMessage1"));
		$("#email").css('border', '1px solid red');
		return false;
	}
	if (!reg.test(email)) {
		verifyStyle(false, "emailmessage", i18n.get("emailErrorMessage2"));
		$("#email").css('border', '1px solid red');
		return false;
	}
	if(localUser==null || localUser.email==null || localUser.email==""){
	$.ajax({
		url : "../user_mgmt/user!duplicateEmail.action?mailAddress=" + email,
		type : 'POST',
		dataType : 'json',
		async : false,
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if (data.success) {
				verifyStyle(true, "emailmessage", "");
				$("#email").css('border', '1px solid #CCCCCC');
				flag = true;
			} 
			else {
				verifyStyle(false, "emailmessage", data.resultMsg);
				$("#email").css('border', '1px solid red');
			}
		}
	});
	}else if(localUser.email!=null && localUser.email!=""){
		flag=true;
	}
	return flag;
}

/**
 * 检查密码
 */
function checkPassword() {
	var password = $("#password").val();
	if (null == password || "" == password) {
		verifyStyle(false, "pwdmessage", i18n.get("pwdErrorMessage1"));
		$("#password").css('border', '1px solid red');
		return false;
	}
	if (/[a-z]{1,}/.test(password) && /[A-Z]{1,}/.test(password) && /[0-9]{1,}/.test(password) && /^\w{6,20}$/.test(password)) {
		verifyStyle(true, "pwdmessage", "");
		$("#password").css('border', '1px solid #CCCCCC');
		var repassword = $("#repassword").val();
		if (null != repassword && "" != repassword) {
			if (repassword != password) {
				verifyStyle(false, "repwdmessage", i18n.get("pwdErrorMessage2"));
				return false;
			}
			verifyStyle(true, "repwdmessage", "");
			$("#password").css('border', '1px solid #CCCCCC');
			return true;
		}
		return true;
	} 
	else {
		verifyStyle(false, "pwdmessage", i18n.get("passwordverification"));
		$("#password").css('border', '1px solid red');
		return false;
	}
}

/**
 * 检查确认密码
 */
function checkRePassword() {
	var repassword = $("#repassword").val();
	if (null == repassword || "" == repassword) {
		verifyStyle(false, "repwdmessage", i18n.get("repwdErrorMessage"));
		$("#repassword").css('border', '1px solid red');
		return false;
	}
	if (/[a-z]{1,}/.test(repassword) && /[A-Z]{1,}/.test(repassword) && /[0-9]{1,}/.test(repassword) && /^\w{6,20}$/.test(repassword)) {
		var password = $("#password").val();
		if (repassword != password) {
			verifyStyle(false, "repwdmessage", i18n.get("pwdErrorMessage2"));
			$("#repassword").css('border', '1px solid red');
			return false;
		}
		verifyStyle(true, "repwdmessage", "");
		$("#repassword").css('border', '1px solid #CCCCCC');
		return true;		
	} 
	else {
		verifyStyle(false, "repwdmessage", i18n.get("passwordverification"));
		$("#repassword").css('border', '1px solid red');
		return false;
	}
}

/**
 *  检查用户类型
 */
function checkUsertype() {
	if ($("#usertype").val() == "" || $("#usertype").val() == null) {
		verifyStyle(false, "usertypemessage", i18n.get("usertypeErrorMessage"));
		$("#usertype").css('border', '1px solid red');
		return false;
	} 
	else {
		verifyStyle(true, "usertypemessage", "");
		$("#usertype").css('border', '1px solid #CCCCCC');
		return true;
	}
}

/**
 * 检查公司名称
 */
function checkCompany() {   
	if ($("#company").val() == "" || $("#company").val() == null||$("#company").val().trim()=="") {
		verifyStyle(false, "companymessage", i18n.get("companyErrorMessage"));
		$("#company").css('border', '1px solid red');
		return false;
	}    
	verifyStyle(true, "companymessage", "");
	$("#company").css('border', '1px solid #CCCCCC');
	return true;
}
      
/**
 * 检查用户名
 */         
function checkUsername() {
	var username = $('#username').val();
	 if (username.length > 0 && username.length < 20&&username.trim()!="") {
		verifyStyle(true, "usernamemessage", "");
		$("#username").css('border', '1px solid #CCCCCC');
		return true;
	} 
	else {
		verifyStyle(false, "usernamemessage", i18n.get("usernameErrorMessage"));
		$("#username").css('border', '1px solid red');
		return false;
	}
}

/**
 * 检查身份证号码
 */
function checkIDCard() {
	var idCard = $("#idCard").val();
	if (idCard == "" || idCard == null||idCard.trim()=="") {
		verifyStyle(false, "idcardmessage", i18n.get("idcardErrorMessage1"));
		$("#idCard").css('border', '1px solid red');
		return false;
	}
	if (idCard.length>18||idCard.length<15) {
		verifyStyle(false, "idcardmessage", i18n.get("idcardErrorMessage4"));
		$("#idCard").css('border', '1px solid red');
		return false;
	}
	if(idCard.length==18){
		var a, b, c;
		if (!isNumber(idCard.substr(0, 17))) {
			verifyStyle(false, "idcardmessage", i18n.get("idcardErrorMessage3"));
			$("#idCard").css('border', '1px solid red');
			return false;
		}
		a = parseInt(idCard.substr(0, 1)) * 7 + parseInt(idCard.substr(1, 1)) * 9
				+ parseInt(idCard.substr(2, 1)) * 10;
		a = a + parseInt(idCard.substr(3, 1)) * 5 + parseInt(idCard.substr(4, 1)) * 8
				+ parseInt(idCard.substr(5, 1)) * 4;
		a = a + parseInt(idCard.substr(6, 1)) * 2 + parseInt(idCard.substr(7, 1)) * 1
				+ parseInt(idCard.substr(8, 1)) * 6;
		a = a + parseInt(idCard.substr(9, 1)) * 3 + parseInt(idCard.substr(10, 1))
				* 7 + parseInt(idCard.substr(11, 1)) * 9;
		a = a + parseInt(idCard.substr(12, 1)) * 10 + parseInt(idCard.substr(13, 1))
				* 5 + parseInt(idCard.substr(14, 1)) * 8;
		a = a + parseInt(idCard.substr(15, 1)) * 4 + parseInt(idCard.substr(16, 1)) * 2;
		b = a % 11;
		if (b == 2) // 最后一位为校验位
		{
			c = parseInt(idCard.substr(17, 1));
		   
			 
		} else {
			c = idCard.substr(17, 1).toUpperCase(); // 转为大写X
		 
		}
		   
		// 最后一位不为0-9以及X的话,提示身份证号码格式不正确
		var defaultStr = ["0","1","2","3","4","5","6","7","8","9","X"];
		var flag = false;
		for(var i=0;i<defaultStr.length;i++){
			if(c == defaultStr[i])
				flag =true;
		}
		if (!flag) {
			verifyStyle(false, "idcardmessage", i18n.get("idcardErrorMessage3"));
			$("#idCard").css('border', '1px solid red');
			return false;
		}
		verifyStyle(true, "idcardmessage", "");
		$("#idCard").css('border', '1px solid #CCCCCC');
	}
	verifyStyle(true, "idcardmessage", "");
	$("#idCard").css('border', '1px solid #CCCCCC');
	return true;     
	
}

/**
 * 检查是否是数字
 */
function isNumber(oNum) {
	if (!oNum)
		return false;
	var strP = /^\d+(\.\d+)?$/;
	if (!strP.test(oNum))
		return false;
	try {
		if (parseFloat(oNum) != oNum)
			return false;
	} catch (ex) {
		return false;
	}
	return true;
}

/**
 * 检查地址
 */
function checkeAddress() {
	var address = $("#address").val();
	if (null == address || "" == address||address.trim()=="") {
		verifyStyle(false, "addressmessage", i18n.get("addressErrorMessage"));
		$("#address").css('border', '1px solid red');
		return false;
	}   
	verifyStyle(true, "addressmessage", "");
	$("#address").css('border', '1px solid #CCCCCC');
	return true;
}

/**
 * 检查联系方式
 */
function checkContactway() {
	var contactway = $('#contactway').val();
	var reg = /^([0-9]\d*)$/;
	if ("" == contactway) {
		verifyStyle(false, "contactwaymessage", i18n.get("contactwayErrorMessage1"));
		$("#contactway").css('border', '1px solid red');
		return false;
	} 
	else if (reg.test(contactway)) {
		verifyStyle(true, "contactwaymessage", "");
		$("#contactway").css('border', '1px solid #CCCCCC');
		return true;
	} 
	else {
		verifyStyle(false, "contactwaymessage", i18n.get("contactwayErrorMessage2"));
		$("#contactway").css('border', '1px solid red');
		return false;
	}
}

/**
 * 点击《HSCloud服务条款》
 */
function readServiceTerms() {
	var currentLanguage = "zh_CN";
	if (getCookie('lang') != "zh_CN") {
		currentLanguage = "en_US";
	}
	var serviceTerms = '<iframe width="690" height="400px" frameborder="0" scroll="no" src="../resources/platform/' + domainCode + '/license/license_'+domainCode+'_' + currentLanguage + '.html"></iframe>';
	openDialog(serviceTerms, i18n.get("term"), 400, 690);
}
function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]); return null; //返回参数值
};
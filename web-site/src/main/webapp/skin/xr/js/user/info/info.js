var user;
var countryStore;
$(document).ready(function() {
	
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	i18n.init();//国际化初始化
	load();
	initTagAndMenu(userTagNameArr,userPageUrls);
	initUserPage();
	initDialog();
	$("#country").change(function(){
		setRegion();
		save("country");
		var region = $("#region").find("option:selected").text();
		if(""== region || null == region){
			$("#u_region").html("&nbsp;");
		}else{
			$("#u_region").html($("#region").find("option:selected").text());
		}
	});
	
});

function initUserPage(){
	$("#text_password").text(i18n.get("modifypassword"));
	$("#text_realname").text(i18n.get("realname"));
	$("#text_welcome").text(i18n.get("welcome"));
	$("#text_telephone").text(i18n.get("contactway-label"));
	$("#text_country").text(i18n.get("country-label"));
	$("#text_region").text(i18n.get("region-label"));
	$("#text_industry").text(i18n.get("industry-label"));
	$("#text_login").text(i18n.get("loginname"));
	$("#text_type").text(i18n.get("usertype-label"));
	$("#text_sex").text(i18n.get("sex-label"));
	$("#text_registerdate").text(i18n.get("registerdate"));
	$("#text_lastlogin").text(i18n.get("lastlogin"));
	$("#text_female").text(i18n.get("female-label"));
	$("#text_male").text(i18n.get("male-label"));
	$("#text_infor").text(i18n.get("p/e information"));
	$("#text_address").text(i18n.get("p/e address"));
	$("#text_idcard").text(i18n.get("idCard-label"));
	$('*[name="modify"]').text(i18n.get("modify"));
	$('*[name="cancel"]').text(i18n.get("operation_cancel"));
	
}


function load(){
	$.ajax({
		url:'../../../user_mgmt/user!getUserInfor.action',
		type:'GET',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		async:false,
		success:function(data){
			if(!data["success"]){
				Dialog.alert(data["resultMsg"]);
			}
			user = data["resultObject"];
			var userProfile = user["userProfile"];
			$("#u_name_1").html(user["name"]);
			$("#u_name_2").html(user["name"]);
			$("#name").val(user["name"]);
			$("#u_email").html(user["email"]);
			$("#u_type").html(i18n.get(user["userType"]));
			$("#u_telephone").html(userProfile["telephone"]);
			$("#telephone").val(userProfile["telephone"]);
			$("#u_company").html(userProfile["company"]);
			$("#company").val(userProfile["company"]);
			$("#u_industry").html(i18n.get("industry" + userProfile["industry"]["nameCode"]));
			$("#u_country").html(i18n.get("country" + userProfile["country"]["nameCode"]));
			if(null != userProfile["region"]){
				$("#u_region").html(i18n.get("region" + userProfile["region"]["nameCode"]));
			}else{
				$("#u_region").html("&nbsp;");
			}
			$("#u_createDate").html(formatDateTime(new Date(user["createDate"])));
			$("#u_sex").html(userProfile["sex"]==true?i18n.get("male-label"):i18n.get("female-label"));
			var sex = (userProfile["sex"]==true?1:0);
			$("#sex"+sex).attr("checked",true);
			$("#u_lastLoginDate").html(formatDateTime(new Date(user["lastLoginDate"])));
			$("#u_address").html(userProfile["address"]);
			$("#address").val(userProfile["address"]);
			$("#u_idcard").html(userProfile["idCard"]);
			$("#idcard").val(userProfile["idCard"]);
		}
	});
	/*loadCountry();
	loadIndustry();*/
	
}


function switchShow(tag,show){

	loadCountry();
	loadIndustry();
	if(show){
		$("#m_"+tag).hide();
		$("#s_"+tag).show();
	}else{
		$("#s_"+tag).hide();
		$("#m_"+tag).show();
	}
	
}

function loadCountry(){
	var url = "../../../user_mgmt/basicData!loadCountry.action";
	$.ajax({
		url:url,
		type:'GET',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		async:false,
		success:function(data){
			if(!data["success"]){
				Dialog.alert(data["resultMsg"]);
			}else{
				countryStore = data['resultObject'];
				$("#country").empty();
				$("#region").empty();
				$.each(countryStore, function(index, value) {
					$("#country")[0].options.add(new Option(i18n.get("country"+value['nameCode']), value['id'], false, false));
				});
				$("#country").val(user["userProfile"]["country"]["id"]);
				setRegion();
			}
		}
	});

}

function loadIndustry(){
	var url = "../../../user_mgmt/basicData!loadIndustry.action";
	$.ajax({
		url:url,
		type:'POST',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		async:false,
		success:function(data){
			if(!data["success"]){
				Dialog.alert(data["resultMsg"]);
			}else{
				var industry = data['resultObject'];
				$("#industry").empty();
				$.each(industry, function(index, value) {
					$("#industry")[0].options.add(new Option(i18n.get("industry"+value['nameCode']), value['id'], false, false));
				});
				$("#industry").val(user["userProfile"]["industry"]["id"]);
			}
		}
	});
}

function setRegion(){
	var countryid = $("#country").val();
	$("#region").empty();
	var regions = null;
	$.each(countryStore, function(index, value) {
		if (value['id'] == countryid) {
			regions = value['regions'];
		}
	});
	$.each(regions, function(index, value) {
	 $("#region")[0].options.add(new Option(i18n.get("region"+value['nameCode']), value['id'], false, false));
	});
	if(null != user && null!=user["userProfile"] && null != user["userProfile"]["region"] && null!=user["userProfile"]["region"]["id"] && ""!=user["userProfile"]["region"]["id"]){
		$("#region").val(user["userProfile"]["region"]["id"]);
	}
	
}

function save(field){
	var name = $("#name").val();
	var company = $("#company").val();
	var telephone = $("#telephone").val();
	var address = $("#address").val();
	var idcard = $("#idcard").val();
	var country = $("#country").val();
	var region = $("#region").val();
	var industry = $("#industry").val();
	var sex=$('input:radio[name="sex"]:checked').val();
	if(null == name || "" == name||name.trim()==""){
		Dialog.alert(i18n.get("realname")+i18n.get("cannotempty"));
		return false;
	}      
	if(null == company || "" == company||company.trim()==""){
		Dialog.alert(i18n.get("p/e information")+i18n.get("cannotempty"));
		return false;
	}
	if(null == address || "" == address||address.trim()==""){
		Dialog.alert(i18n.get("p/e address")+i18n.get("cannotempty"));
		return false;
	}
	if(null == idcard || "" == idcard||idcard.trim()==""||idcard.length > 18||idcard.length<15){
		Dialog.alert(i18n.get("idCard-label")+i18n.get("idcardErrorMessage4"));
		return false;    
	}
	if(null == telephone || ""==telephone||telephone.trim()==""){
		Dialog.alert(i18n.get("contactway-label")+i18n.get("cannotempty"));
		return false;    
	}
	  var reg=/(^[0-9]+$)/;
	if (!reg.test(telephone)) {
		Dialog.alert(i18n.get("contactway-label")+i18n.get("onlynum"));	
		return false;
	}
	var url = "../../../user_mgmt/user!modify.action";
	jsonRequest = "{\"name\":\""+name+"\",\"userProfile\":{\"company\":\""+company+"\",\"sex\":"+sex+",\"telephone\":\""+telephone+"\",\"address\":\""+address+"\",\"idCard\":\""+idcard+"\",\"country\":{\"id\":"+country+"}"+(null == region || ""==region?"":",\"region\":{\"id\":"+region+"}")+",\"industry\":{\"id\":"+industry+"}}}";
	$.ajax({
		url:url,
		type:'POST',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		data : jsonRequest,
		async:false,
		success:function(data){
			if(!data["success"]){
				Dialog.alert(data["resultMsg"]);
			}
		}
	});
	load();
	switchShow(field,true);
	
}

function changePassword(){
	var html = "<table style=\"width: 570px;\"><tr><td style=\"width:130px;text-align:right;padding-right: 15px;line-height: 25px;\"><font>"+i18n.get("oldpassword")+"</font></td><td style=\"width:440px;text-align:left;line-height: 25px;\"><input id=\"oldpwd\" onblur=\"checkNull('oldpwd','olepwdmessage')\" type=\"password\"><span id=\"olepwdmessage\" style=\"color: red;padding-left: 15px;\"></span></td></tr><tr><td style=\"width:130px;text-align:right;padding-right: 15px;line-height: 25px;\"><font>"+i18n.get("newpassword")+"</font></td><td style=\"width:440px;text-align:left;line-height: 25px;\"><input id=\"newpwd\" onblur=\"checkPassword()\"  maxlength=\"20\" type=\"password\"><span id=\"newpwdmessage\" style=\"color: red;padding-left: 15px;\"></span></td></tr><tr><td style=\"width:130px;text-align:right;padding-right: 15px;line-height: 25px;\"><font>"+i18n.get("confirmpassword")+"</font></td><td style=\"width:440px;text-align:left;line-height: 25px;\"><input id=\"confirmpwd\" onblur=\"checkRePassword()\" maxlength=\"20\" type=\"password\"><span id=\"confirmpwdmessage\" style=\"color: red;padding-left: 15px;\"></span></td></tr></table>";
		
		var dialog = new Dialog("dialog");
	    dialog.Width = 530;
	    dialog.Height = 100;
	    dialog.Title = i18n.get("modifypassword");
	    dialog.innerHTML=html;
	    dialog.OKEvent = function() {
	    	if(!(checkNull("oldpwd","olepwdmessage") & checkPassword() & checkRePassword())){
				return false;
			}
			var oldpwd = $("#oldpwd").val();
			var newpwd = $("#newpwd").val();
			var comfirpwd = $("#confirmpwd").val();
			var enOld = $.md5(oldpwd);
			var enNew = $.md5(newpwd);
			if(comfirpwd != newpwd){
				$("#confirmpwdmessage").html("* "+get("differ"));
				return false;
			}
			jsonRequest = "{\"password\" : \""+enNew + "\"}";
			var url = "../../../user_mgmt/user!resetPassword.action";
		    url = url+"?oldPwd="+enOld;
			$.ajax({
				url:url,
				type:'POST',
				dataType:'json',
				contentType:"application/json;charset=UTF-8",
				data : jsonRequest,
				async:false,
				success:function(data){
		             if(!data["success"]){
		                 Dialog.alert(data["resultMsg"]);
		             }else{
		            	 dialog.close();
		             }

				}
			});
	    };
	    dialog.show();
}

function checkNull(valID,mesID){
	
	var $value = $("#"+valID);
	var value = $value.val();
	if(null == value || "" == value){
		$value.css('border', '1px solid red');
		$("#"+mesID).html("* "+i18n.get("cannotempty"));
		return false;
	}
	$value.css('border', '1px solid #a4a4a4');
	$("#"+mesID).html("");
	return true;
	
}

function checkPassword() {

	if(!checkNull("newpwd","newpwdmessage")){
		return false;
	}
	var password = $("#newpwd").val();
	if (!(/[a-z]{1,}/.test(password) && /[A-Z]{1,}/.test(password) && /[0-9]{1,}/.test(password) && /^\w{6,20}$/.test(password))) {
		$("#newpwdmessage").html("*"+i18n.get("passwordverification"));
		$("#newpwd").css('border', '1px solid red');
		return false;
		
	}
	$("#password").css('border', '1px solid #a4a4a4');
	$("#newpwdmessage").html("");
	return true;
	
}

/**
 * 检查确认密码
 */
function checkRePassword() {
	if(!checkNull("confirmpwd","confirmpwdmessage")){
		return false;
	}
	var newpassword = $("#newpwd").val();
	var confirmpwd = $("#confirmpwd").val();
	if (!(/[a-z]{1,}/.test(confirmpwd) && /[A-Z]{1,}/.test(confirmpwd) && /[0-9]{1,}/.test(confirmpwd) && /^\w{6,20}$/.test(confirmpwd))) {
		$("#confirmpwd").css('border', '1px solid red');
		$("#confirmpwdmessage").html("*"+i18n.get("passwordverification"));
		return false;
	}
	if(confirmpwd!=newpassword){
		$("#confirmpwd").css('border', '1px solid red');
		$("#confirmpwdmessage").html("*"+i18n.get("differ"));
		return false;
	}
	$("#confirmpwd").css('border', '1px solid #a4a4a4');
	$("#confirmpwdmessage").html("");
	return true;
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
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

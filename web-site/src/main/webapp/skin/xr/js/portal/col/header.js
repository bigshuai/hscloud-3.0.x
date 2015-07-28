/**
 * 初始化header
 */
$(document).ready(function() {
	initShortcut();
	initCss('portal/col/header.css');
	$("#header").css("background","url(../skin/"+customerPath+"/images/portal/col/header_bg.gif)");	
	//页面框架指定DIV内的设计代码
	var headerHTML = "<div id=\"logoDIV\">" +
			"<img src=\"../resources/platform/"+domainCode+"/images/logo_portal.png\">" +
			"</div>" +
			"<div id=\"loginDIV\">" +
			"<div id='btn_login' class='loginbutton'>"+i18n.get("login-console")+"</div>"+
			"<div id='btn_register' class='r_button'>"+i18n.get("register")+"</div>"+
//			"<img src=\"../skin/"+customerPath+"/images/portal/col/tag.png\" />"+
			"<span style='margin-left:20px;' onclick=\"zh()\">简体中文</span>　<span onclick=\"en()\">English</span>" +
			"</div>";
//	"<img id=\"btn_login\" src=\"../skin/"+customerPath+"/images/portal/col/login.png\" />"+
//	"<img id=\"btn_register\" src=\"../skin/"+customerPath+"/images/portal/col/register.png\"/>"+
	$("#header").html(headerHTML);
	$("#btn_register").bind('click',function(){
		window.parent.location.href="register.html";
	});

	var text = "<table id='dlg_div' class='cn-login' style='display:none;'>"
			+ "    <tr>"
			+ "        <td colspan='2' width='100%'>"
			+ "            &nbsp;"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr>"
			+ "        <td align='right' width='31%'>"
			+ "            <span id='userNameLabel'>用户名：</span>"
			+ "        </td>"
			+ "        <td width='69%'>"
			+ "            <input id='username2'  type='text' class='login_input' onfocus='reset_input(this);' onblur='reset_onblur_input(this);'/>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr class='msgCss'>"
			+ "        <td width='31%'></td>"
			+ "        <td id='username2_msg' width='69%'>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr>"
			+ "        <td align='right' width='31%'>"
			+ "            <span id='passwordLabel'>密　码：</span>"
			+ "        </td>"
			+ "        <td width='69%'>"
			+ "            <input id='password2' type='password' class='login_input' onfocus='reset_input(this);' onblur='reset_onblur_input(this);'/>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr class='msgCss'>"
			+ "        <td width='31%'></td>"
			+ "        <td id='password2_msg' width='69%'>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr>"
			+ "        <td align='right' width='31%'>"
			+ "            <span id='codeLabel'>验证码：</span>"
			+ "        </td>"
			+ "        <td width='69%'>"
			+ "            <input id='validCode'maxlength='5' type='text' class='login_input' onfocus='getImage();reset_input(this);' onblur='reset_onblur_input(this);' style='width: 80px;' />"
			+ "            <span><img id='imageCode' src='" + rootPath+"/skin/"+customerPath+"/images/portal/login/icode.png' style='margin-top:-10px;cursor:pointer;' onclick='getImage()' height='26' align='middle' alt='' /></span>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr class='msgCss'>"
			+ "        <td width='31%'></td>"
			+ "        <td id='validCode_msg' width='69%'>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr>"
			+ "        <td width='31%'></td>"
			+ "        <td align='left' width='69%'>"
			+ "            <span id='login_btn' class='button button-rounded loginButton' onclick='login();' style='cursor:pointer;width:60px;'>登 录</span>"
			+ "        </td>"
			+ "    </tr>"
			+ "    <tr>"
			+ "        <td width='31%'>&nbsp;</td>"
			+ "        <td width='69%'>"
			+ "            <span id='forget-password' style='cursor:pointer'>忘记密码？ </span>"
			+ "        </td>"
			+ "    </tr>"
			+ "</table>";
	$("body").append(text);
	$("#forget-password").bind('click',function(){
		window.location.href = rootPath+"/portal/forgetPassword.html";
	});
	i18nPage();//国际化当前页面
	
	$("#btn_login").bind('click',function(){
		var obj = $("#btn_login")[0];
		var top = obj.getBoundingClientRect().top + 40;
		var left = obj.getBoundingClientRect().left - 2;//modifier:liyunhui 对齐dialog
		$.layer({
			type: 1,
			fix:false,
			title: false,
			closeBtn: false,
			shade : [0.0 , '#000' , true],
			shadeClose : true,
			border : [2, 0.2, '#666', true],
			offset: [ top + 'px', left + 'px'],
			area: ['300px','250px'],
			page: {
				dom: '#dlg_div'}
		});
	});
	
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			login();
		}
	});
});

function i18nPage() {
	$("#userNameLabel").text(i18n.get("userName"));
	$("#passwordLabel").text(i18n.get("password"));
	$("#codeLabel").text(i18n.get("code"));
	$("#forget-password").text(i18n.get("forget-password"));
	$("#login_btn").text(i18n.get("LoginButton"));
};

function getImage() {
	var time = Math.random();
	var url = rootPath+"/user_mgmt/webSite!getImageCode.action" + "?time=" + time;
	$('#imageCode').attr("src", url);
};

function reset_input(obj) {
	$('#' + obj.id).css('border', '1px solid rgba(15, 161, 253, 0.8)');
	$('#' + obj.id + '_msg').text('');
};

function reset_onblur_input(obj) {
	$('#' + obj.id).css('border', '1px solid #CCCCCC');
	$('#' + obj.id + '_msg').text('');
};

function login(){
	var username = $("#username2").val();
	if(username == '') {
		$('#username2_msg').text(i18n.get("Please enter your user name!"));//请输入用户名!
		$("#username2").css('border', '1px solid red');
		return;
	}
	var password = $("#password2").val();
	if(password == '') {
		$('#password2_msg').text(i18n.get("Please enter your password!"));//请输入密码!
		$("#password2").css('border', '1px solid red');
		return;
	}
	var enPassword = $.md5(password);
	var validCode = $("#validCode").val();
	if(validCode == '') {
		$('#validCode_msg').text(i18n.get("Please enter your code!"));//请输入验证码!
		$("#validCode").css('border', '1px solid red');
		return;
	}
	var jsonRequest = "{\"email\":\""+username+"\",\"password\":\""+enPassword+"\",\"domain\":{\"code\":\""+domainCode+"\"}}";
	var url = rootPath+"/user_mgmt/user!userLoginByEmail.action?code="+validCode;
	$.ajax({
		  url: url,
		  type: 'POST',
		  dataType: 'json',
		  contentType: "application/json; charset=utf-8",
		  data: jsonRequest,
		  success: function(data){
			  if(data['success']){
				  window.location.href = "../function/vpdc/center/vmList.html";
			  } else {
				  if('01006' == data['resultCode']){
					  $('#validCode_msg').text(data['resultMsg']);
					  $("#validCode").css('border', '1px solid red');
				  } else {
					  $('#username2_msg').text(data['resultMsg']);
					  $("#username2").css('border', '1px solid red');
				  }
				  return;
			  }
		  }
	});
};
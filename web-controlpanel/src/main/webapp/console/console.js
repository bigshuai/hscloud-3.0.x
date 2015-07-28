/****************************/
$("document").ready(gotoInit);
var REMOTE_URL = "../";
var timeOutUrl =  REMOTE_URL+"index.html";
var vmtimers = null;
//var optType=[["1","启动"],[2,"重启"],[3,"关机"],[6,"生产快照"],[7,"还原快照"],[8,"重装系统"]];
//var optType=["启动","重启","关机","","","生产快照","还原快照","重装系统"];
var optType=[{0:"未知"},{1:"启动"},{2:"重启"},{3:"关机"},{4:""},{5:""},{6:"生成快照"},{7:"还原快照"},{8:"重装系统"},{9:"密码重置"},{10:"主机修复"}];
var res =[{0:"未知"},{1:"成功"},{2:"失败"},{3:"操作中"},{4:"绑定IP失败"},{5:"主机修复完成"}];
function gotoInit(){
	if(i18n.checkLocale() == 'zh_CN') {
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
		   monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
		   monthNamesShort: ['一','二','三','四','五','六','七','八','九','十','十一','十二'],
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
	$("#search_date_from").val("");
	$("#search_date_to").val("");
	$("#opsType").val(0);
	$( "#search_date_from" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.png",
		buttonImageOnly: true
	});
	$( "#search_date_to" ).datepicker({
		showOn: "button",
		buttonImage: "images/calendar.png",
		buttonImageOnly: true
	});
	$("#search_bar").click(function(){
		if($("#search_date_from").val()!="" && $("#search_date_to").val()!=""){
			if($("#search_date_from").val() > $("#search_date_to").val()){
				customizeAlert(lang.order.paidorder.timeWarn);
				return "";
			}
		}
		dateFromTo = [$("#search_date_from").val(),$("#search_date_to").val()];
		if("rgb(204, 204, 204)"==$("#order_search_orderNo").css("color")){
			orderNo = "";
		}else{
			orderNo = $("#order_search_orderNo").val();
		}
		pager = new Pagination();
		comp_dataContainer= $("#dataContainer");
		comp_pager = $("#pager");
		comp_pager.html("");
		comp_pager.empty();		
		findOperation();
		$("#order_search_date_from").val("");
		$("#order_search_date_to").val("");
		$("#order_search_orderNo").val("");
	});

	$("div.left > div").eq(0).click();
	$("#newpassword").blur(checkNewPassword);
	$("#oldpassword").blur(checkOldPassword);
	$("#comfirpassword").blur(checkComfirmPassword);
	$("#modpassword").click(modpassword);
	$("#quit").click(quit);
	$("#week_time").timepicker({
		hourGrid: 4,
		minuteGrid: 10
	});
	$("#month_time").timepicker({
		hourGrid: 4,
		minuteGrid: 10
	});
	
	$("#autoback").click(function(){
		var type = $("#type0_h").val();
		var checked = $(this).is(":checked") ;
		reset();
		checked?$(this).attr("checked","checked"):$(this).removeAttr("checked");
		if(checked){
	    	var radios = $("input[type='radio'][name='auto_type']");
	    	$.each(radios, function(index, radio) {
	    		$(radio).removeAttr("disabled");
			});
	    }else{
	    	var radios = $("input[type='radio'][name='auto_type']");
	    	$.each(radios, function(index, radio) {
	    		$(radio).attr("disabled","disabled");
			});
	    	$("#month").attr("disabled","disabled");
			$("#month_time").attr("disabled","disabled");
	    	$("#week").attr("disabled","disabled");
			$("#week_time").attr("disabled","disabled");
			$("#type0_h").val("");
			  
	    }
		checkAutoBackup(checked,type);                             
	});    
	$("#week_r").click(function(){
		$("#type0_h").val(1);
		checkAutoBackup(checked,type);
		$("#week").removeAttr("disabled");
		$("#week_time").removeAttr("disabled");
		$("#month").attr("disabled","disabled");
		$("#month_time").attr("disabled","disabled");
		var checked = $("#autoback").is(":checked") ;
		var type=$("#type0_h").val();
		checkAutoBackup(checked,type);
		   
	});	                    
	$("#month_r").click(function(){
		$("#type0_h").val(2);
		$("#month").removeAttr("disabled");
		$("#month_time").removeAttr("disabled");
		$("#week").attr("disabled","disabled");
		$("#week_time").attr("disabled","disabled");
		var checked = $("#autoback").is(":checked") ;
		var type=$("#type0_h").val();  
		checkAutoBackup(checked,type);
		
	});
};

function checkAutoBackup(isCheck,type){
    
	if(isCheck && ""!=type){
		$("#submitplan").removeClass().addClass("button_b").unbind().bind("click",timingSnapshot);
		$("#resetplan").removeClass().addClass("button_b").unbind().bind("click",reset);
	}else{
		$("#submitplan").removeClass().addClass("button_gray").unbind();
		$("#resetplan").removeClass().addClass("button_gray").unbind();			
	}
}
//获取虚拟机信息请求
function getVmDetailInfo(){
	var vmJson = null;
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_URL + "ops/ops!findDetailVmById.action",
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			}else{
				vmJson = json;
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	return vmJson;
}
//获取虚拟机OS集合请求
function getVmOsList(){
	var vmJson = null;
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_URL + "ops/ops!getVmOsListByVmId.action",
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			}else{
				vmJson = json;
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	return vmJson;
}
//退出系统
function Quit(){
	var location = "";
	if($("#homepage").size()== 0){
		url = REMOTE_URL +  REMOTE_AGENT_LIST["user.logout"]["logout"];
		location = "../../user/login/index.html";
	}else{
		url = REMOTE_AGENT_LIST["user.logout"]["logout"];
		location = "index.html";
	}
	$.ajax({
		  url: url,
		  type: 'POST',
		  dataType: 'json',
		  contentType: "application/json; charset=utf-8",
		  data: null,
		  statusCode: {
			  200: function() {
				  window.location.href = location;
			  }
		  }
	});
};
// 选中VM--获取status@task信息
function getVM_info() {
	var state_task = "";
	var json = getVmDetailInfo();
	if(json!=null){
		state_task = json.resultObject.vmStatus+"@"+json.resultObject.vmTask+"@"+json.resultObject.vmIsEnable;
	}
	return state_task;
};
//获取选中VMstatus信息
function getVM_status(vminfo) {
	return vminfo.split("@")[0];
}
// 获取选中VMtask信息
function getVM_task(vminfo) {
	return vminfo.split("@")[1];
}
//获取选中VMisEnable信息
function getVM_isEnable(vminfo) {
	return vminfo.split("@")[2];
}
// 操控VM(启动/关闭/重启)
function operateVM(command) {	
	var vminfo = getVM_info();
	var url = "ops/ops!" + command + ".action";
	if("noInstance" == getVM_status(vminfo)){
		customizeAlert(lang.vpdc.center.noInstance);
		return;
	}
	if ("startVM" == command && ("ACTIVE" == getVM_status(vminfo) || (getVM_task(vminfo) != "null" && getVM_task(vminfo) != ""))) {
		customizeAlert(lang.vpdc.center.startAlert);
		return;
	} else if ("closeVM" == command 
			&& ("SUSPENDED" == getVM_status(vminfo) || (getVM_task(vminfo) != "null" && getVM_task(vminfo) != ""))) {
		customizeAlert(lang.vpdc.center.stopAlert);
		return;
	} else if ("rebootVM" == command
			&& ("SUSPENDED" == getVM_status(vminfo) || (getVM_task(vminfo) != "null" && getVM_task(vminfo) != ""))) {
		customizeAlert(lang.vpdc.center.rebootAlert);
		return;
	}
	var arr = new Array();
	arr[0] = url;
	if("closeVM" == command){
		customizeConfirm(lang.vpdc.center.stopPrompt,operateVMAjax,arr);
	}else if("rebootVM" == command){
		customizeConfirm(lang.vpdc.center.rebootPrompt,operateVMAjax,arr);
	}else{
		operateVMAjax(arr);
	}
};
//// 操控VM(启动/关闭/重启)的ajax请求
function operateVMAjax(paramArr){
	var	url = paramArr[0];
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_URL + url,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
				return ;   
			}
			//更新VM状态
			getVmStatus();
			//获取当前改变后的VM状态
			vmtimers = setInterval("autogetVmStatus()",2000);
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	return true;
};
//更新界面VM状态
function getVmStatus(){
	var json = getVmDetailInfo();
	if(json==null){
		return;
	}
	var vmTaskStatus = json.resultObject.vmTask;
	if(vmTaskStatus!=null && vmTaskStatus!=""){
		$("#vmStatus").html(vm_status.i18n(vmTaskStatus)+"...");
	}else{
		$("#vmStatus").html(vm_status.i18n(json.resultObject.vmStatus));
	}
	return json;
}          
//实时获得操作VM后产生的新状态
function autogetVmStatus(){
	var vminfo = getVmStatus();
	var vmTaskStatus = vminfo.resultObject.vmTask;
	if(vmTaskStatus == null){
		clearInterval(vmtimers);
	}
}
//启动虚拟机
function vmStart(){
	operateVM('startVM');
};
//重启虚拟机
function vmRestart(){
	operateVM('rebootVM');
};
//关闭虚拟机
function vmShutdown(){
	operateVM('closeVM');
};
//主机修复
function repairOS() {
	var vminfo = getVM_info();
	var isEnable = getVM_isEnable(vminfo);
	if(1==isEnable){
		customizeAlert(lang.vpdc.center.freezePrompt);
		return;
	}
	if("noInstance" == getVM_status(vminfo)){
		customizeAlert(lang.vpdc.center.noInstance);
		return;
	}
	var vmid = "";
	customizeConfirm(lang.vpdc.center.repairOSTip, repairOSAjax, vmid);
}
//提交请求
function repairOSAjax(vmid) {
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_URL+ "ops/ops!repairOS.action",
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			} else {
				customizeAlert(lang.vpdc.center.repairOsSuccess);
			}
		},
		error : function() {
			alert(lang.vpdc.center.error);
		}
    });	
	return true;
}
//远程访问虚拟机
function vmRemote(){
	var vminfo = getVM_info();
	var isEnable = getVM_isEnable(vminfo);
	if(1==isEnable){
		customizeAlert(lang.vpdc.center.freezePrompt);
		return;
	}
	if("noInstance" == getVM_status(vminfo)){
		customizeAlert(lang.vpdc.center.noInstance);
		return;
	}else if ("SUSPENDED" == getVM_status(vminfo)) {
		customizeAlert(lang.vpdc.center.remoteAlert);
		return;
	}
	//判断浏览器类型
	if(getBrowser().indexOf("Firefox")<0 && getBrowser().indexOf("Chrome")<0){
		customizeAlert(lang.vpdc.center.browerAlert_monitor);
		return;
	}
	getImageCode();
	$("#vncTip4").html("");
	$("#validCode").val("");
	$("#vnc_image").html('<img src="images/load_wait.gif"/>');
	
	//显示等待进度框
	$("#vnc_waitDIV").dialog({
		hide : true, // 点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错.
		autoOpen : true,
		width : 450,
		modal : true, // 蒙层（弹出会影响页面大小）
		title : lang.vpdc.center.vnc,
		overlay : {
			opacity : 0.5,
			background : "white",
			overflow : 'auto'
		}
	});
	$("#download").css({"text-decoration":"underline","color":"blue"});
	$.ajax({
			async : true,
			type : "post",
			url : REMOTE_URL+ "ops/ops!getVNC.action",
			dataType : 'json',
			success : function(json) {
				if (!json.success) {
					$('#vnc_image').html('<font color="red" style="font-weight: bold;">VNC连接失败，您可以尝试以下方式连接</font>');
					return;
				} else {
					$("#vnc_image").html('<img id="enterVNC" src="images/EnterVNC.png" style="cursor: pointer;"/>');
					// 确定进入VNC
					$("#enterVNC").click(function(){
						if (json.resultObject != null) {
							var url = json.resultObject;
							var start = url.indexOf("//");
							var end = url.indexOf("/vnc");
							var ip_port = url.substring(start+2, end);
							var ip = ip_port.split(":")[0];
							token = url.split("=")[1];
							var key = encode(ip);
							var width = screen.width;
							var height = screen.height;
							var vncURL = "novnc/vnc_auto.html?token="+token+"&key="+key;
							window.open(vncURL,'vm',
									'fullscreen=yes,width='
									+ width
									+ ',height='
									+ height
									+ ',top=0,left=0,toolbar=yes,menubar=yes,scrollbars=yes,resizable=no,location=yes,status=yes');
						}
					});
				}
			},
			error : function() {
				alert(lang.vpdc.center.error);
			}
    });
};
//点击获取客户端连接参数
function getConnectionParam() {
	$("#vncTip4").html("");
	var validCode = $("#validCode").val();
	if (validCode == "" || validCode == null) {
		customizeAlert(lang.vpdc.center.inputValidCode);
		return ;	
	}	
	var connectionParam = "";
	$.ajax({
			async : false,
			type : "post",
			url : REMOTE_URL + "ops/ops!getClientVNC.action",
			data : "code=" + validCode,
			dataType : 'json',
			success : function(json) {
				if (json.success) {
					connectionParam = "远程连接：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" 
						+ json.resultObject.proxyIP + ":" + json.resultObject.proxyPort + "&nbsp;" + lang.vpdc.center.vncTip4;
					$("#vncTip4").html(connectionParam);
				} else {
					customizeAlert(json.resultMsg);
				}
			},
			error : function() {
				alert(lang.vpdc.center.error);
			}
	});
	$("#validCode").val("");
}
/*
 * 验证码输入框获取焦点时，更新验证码
 */
function getImageCode(){
	var time = Math.random();
	var url = "console/login!getImageCode.action"+"?time="+time;
    $("#imageCode").attr("src", url);
}
//重装虚拟机操作系统
function vmReset(osTableId){
	var vminfo = getVM_info();
	if("SUSPENDED" == getVM_status(vminfo) || (getVM_task(vminfo) != "null" && getVM_task(vminfo) != "")){
		customizeAlert(lang.vpdc.center.resetOsAlert);
		return;
	}
	var osId = $("#select").val();
	var osUser = $("#os_name").html();
	var osPwd = $("#os_pwd").val();
	var osPwd2 = $("#os_pwd2").val();
	if(verifyPwd(osPwd,osPwd2)==false){
		return;
	}
	if(osId==""){
		customizeAlert(lang.vpdc.center.osAlert);
		return;
	}
	var arr = new Array();
	arr[0] = osId;
	arr[1] = osUser;
	arr[2] = osPwd;
	customizeConfirm(lang.vpdc.center.osPrompt,resetOsAjax,arr);
}

//重装虚拟机操作系统Ajax请求
function resetOsAjax(paramArr){
	var osid = paramArr[0];
	var osUser = paramArr[1];
	var osPwd = paramArr[2];
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_URL + "ops/ops!resetVmOS.action",
		data : "osId="+osid+"&name="+osUser+"&password="+osPwd,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			}
			$("#dialog-vmInstall").dialog("close");
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	return true;
};
//修改登录密码
function passwordReset(){
	alert("passwordReset!");
};

//主机信息模块信息显示
function showVmInfo(){
	var json = getVmDetailInfo();
	if(json==null){
		return;
	}
	$("#vmInfo_Name").html(json.resultObject.vmName);
	var ipVal = json.resultObject.ip;
	if (ipVal.split(",").length > 2) {
		ipVal = ipVal.split(",")[0] + "," + ipVal.split(",")[1] + "...";
	}
	$("#vmInfo_IP").html(ipVal);
	$("#vmName").html(json.resultObject.vmName);
	$("#vmCPU").html(json.resultObject.cpu+"核");
	$("#vmMEM").html(json.resultObject.memory+"M");
	$("#vmDisk").html(json.resultObject.disk+"G");
	var extdisks = json.resultObject.extdisks;
	if(extdisks==null || extdisks.length==0){
		$("#vmExtDisk").html("--");
	}else{
		var extdisks = json.resultObject.extdisks;
		var extdisksStr = "";
		for(var i = 0;i<extdisks.length;i++){
			extdisksStr += extdisks[i].ed_capacity+"G;";
		}
		$("#vmExtDisk").html(extdisksStr);
	}
	$("#vmDisk").html(json.resultObject.disk+"G");
	$("#vmNetwork").html(json.resultObject.network+"M");
	if (json.resultObject.ip != "" && json.resultObject.ip != null 
			&& json.resultObject.ip.split(",").length > 14) {
		var ipVal_1_7 = "";
		for ( var i = 0; i < 7; i++) {
			ipVal_1_7 += json.resultObject.ip.split(",")[i] + ",";
		}					
		$("#vmIP").html(ipVal_1_7);	
		// 增加一行存放8-14的IP
		var ipVal_8_14 = "";
		for ( var i = 7; i < 14; i++) {
			ipVal_8_14 += json.resultObject.ip.split(",")[i] + ",";
		}					
		$("#vmIP").append("<br/>" + ipVal_8_14);
		// 增加一行存放第14个以后的IP
		var ipVal_after_14 = "";
		for ( var i = 14; i < json.resultObject.ip.split(",").length; i++) {
			ipVal_after_14 += json.resultObject.ip.split(",")[i] + ",";
		}
		ipVal_after_14 = ipVal_after_14.substr(0,ipVal_after_14.length-1);
		$("#vmIP").append("<br/>" + ipVal_after_14);
	} else if (json.resultObject.ip != "" && json.resultObject.ip != null 
			&& json.resultObject.ip.split(",").length > 7) {
		var ipVal_1_7 = "";
		for ( var i = 0; i < 7; i++) {
			ipVal_1_7 += json.resultObject.ip.split(",")[i] + ",";
		}	
		$("#vmIP").html(ipVal_1_7);	
		// 增加一行存放第7个以后的IP
		var ipVal_after_7 = "";
		for ( var i = 7; i < json.resultObject.ip.split(",").length; i++) {
			ipVal_after_7 += json.resultObject.ip.split(",")[i] + ",";
		}
		ipVal_after_7 = ipVal_after_7.substr(0,ipVal_after_7.length-1);
		$("#vmIP").append("<br/>" + ipVal_after_7);
	} else {
		$("#vmIP").html(json.resultObject.ip);
	}	
	$("#vmOS").html(json.resultObject.os);
	var effectiveDate = json.resultObject.effectiveDate;
	if(effectiveDate!=null){
		$("#vmBuydate").html(new Date(effectiveDate).format("yyyy-MM-dd"));
	}
	var expireDate = json.resultObject.expireDate;
	if(expireDate!=null){
		$("#vmExpiredate").html(new Date(expireDate).format("yyyy-MM-dd"));
	}
}
//主机管理模块信息显示
function showVmManage(){
	
	var json = getVmDetailInfo();
	if(json==null){
		return;
	}
	$("#vmInfo_Name").html(json.resultObject.vmName);
	var ipVal = json.resultObject.ip;
	if (ipVal.split(",").length > 2) {
		ipVal = ipVal.split(",")[0] + "," + ipVal.split(",")[1] + "...";
	}
	$("#vmInfo_IP").html(ipVal);
	var vmTaskStatus = json.resultObject.vmTask;
	if(vmTaskStatus!=null && vmTaskStatus!=""){
		$("#vmStatus").html(vm_status.i18n(vmTaskStatus)+"...");
	}else{
		$("#vmStatus").html(vm_status.i18n(json.resultObject.vmStatus));
	}
}
//重装操作系统模块信息显示
function showVmResetOS(){
	$("#select").empty();
	var json = getVmOsList();
	var select = null;
	if(json==null){
		return;
	}
	$("#os_pwd").val("");
	$("#os_pwd2").val("");
	$("#os_name").html(json.resultObject.osLoginUser);
	var currOsId = json.resultObject.osId;
	var osJson = json.resultObject.osList;
	if(osJson==null){
		$("#select").append("<option value=\""+currOsId+"\">"+json.resultObject.os+"</option>");
	}else{
		for(var j=0;j<osJson.length;j++){
			select = ""
			if(currOsId == osJson[j].id){
				select="selected=\"selected\"";
			}
			$("#select").append("<option "+select+" value=\""+osJson[j].id+"\">"+osJson[j].name+"</option>");
		}
	}
}
//改变操作系统同时加载新系统用户名
function loadOsUser(obj){
	var osid = obj.value;
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_AGENT_PREFIX + "ops/ops!getVmOSUser.action",
		data : "osId="+osid,
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			}else{
				$("#os_name").html(json.resultObject);
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
}
//主机信息显示
function vmInfoShow(){	
	clearPassword();
	showVmInfo();
	$("div.dataContainer > div").eq(0).show().siblings().hide();	
	$("div.left > div").removeClass().addClass("left_bg");
	$("div.left > div").eq(0).removeClass().addClass("left_bg_selected");
};
//主机管理显示
function vmManageShow(){
	clearPassword();
	showVmManage();	
	$("div.dataContainer > div").eq(1).show().siblings().hide();
	$("div.left > div").removeClass().addClass("left_bg");
	$("div.left > div").eq(1).removeClass().addClass("left_bg_selected");
};
//重装系统显示
function vmInstallShow(){
	clearPassword();	$("#dialog-vmInstall").dialog({		autoOpen : true,		modal : true,		width : 700,		title : '重装系统',		overlay : {			opacity : 0.5,			background : "black",			overflow : 'auto'		},		close:function(){		}	});
	showVmResetOS();
};
function passwordEditShow(){		$("#dialog-passwordEdit").dialog({		autoOpen : true,		modal : true,		width : 700,		title : '重置密码',		overlay : {			opacity : 0.5,			background : "black",			overflow : 'auto'		},		close:function(){		}	});
};
//快照管理展示
function snapshotManageShow() {
    $("div.dataContainer > div").eq(3).show().siblings().hide();
    $("div.left > div").removeClass().addClass("left_bg");
    $("div.left > div").eq(3).removeClass().addClass("left_bg_selected");
    clearPassword();
    lastestSnapshot();
};
function eventManageShow(){
    $("div.dataContainer > div").eq(4).show().siblings().hide();
    $("div.left > div").removeClass().addClass("left_bg");
    $("div.left > div").eq(4).removeClass().addClass("left_bg_selected");
    findOperation();
}//icp信息展示function icpEditShow() {    $("div.dataContainer > div").eq(5).show().siblings().hide();
    $("div.left > div").removeClass().addClass("left_bg");
    $("div.left > div").eq(5).removeClass().addClass("left_bg_selected");    clearPassword();};
//手动快照页显示
function manualSnapshotShow(){
	lastestSnapshot();
	$("div.TabbedPanelsContentGroup > div").eq(0).show().siblings().hide();
};
//定时快照页显示
function timingSnapshotShow(){
	$("div.TabbedPanelsContentGroup > div").eq(1).show().siblings().hide();
	getVmBackupPlan();
};
//手动生成快照设置对话框
function createSnapshotDialog(){
	var buttons_ = {
			'确定' : function() {
				if(checkedsnapshot()){
					var name = $("#snapshot_name").val();
					var comm = $("#snapshot_comments").val();
					$(this).dialog("close");	
					backupVM(name,comm);
				}
			},
			'取消' : function() {
				// 关闭当前Dialog
				$(this).dialog("close");
			}
	};	
	$("#dialog-createSnapshot").dialog({
		autoOpen : true,		modal : true,
		width : 400,
		title : '生成快照',
		overlay : {
			opacity : 0.5,
			background : "black",
			overflow : 'auto'
		},
		buttons : buttons_,
		close:function(){
			$("#snapshot_name").val("");
			$("#snapshot_comments").val("");
		}
	});
};
//手动取消快照
function stopSnapshotDialog() {
	$.ajax({
		async : true,
		type : "post",
		url : REMOTE_URL + "ops/ops!stopSnapshot.action",
		dataType : 'json',
		success : function(json) {
		  if (json.success){
			  snapshotManageShow();
			  hsAlert("msg","终止快照成功。",null);
		  } else {
			  hsAlert("msg","终止快照失败。",null);
		  }
        },
        error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});	
}
//手动还原快照设置对话框
function revertSnapshotDialog(){
	var flag = false;
	var buttons_ = {
			'确定' : function() {
				$(this).dialog("close");
				renewVm();
			},
			'取消' : function() {
				// 关闭当前Dialog
				$(this).dialog("close");
			}
	};	
	$("#dialog-revertSnapshot").dialog({
		autoOpen : true,
		modal : true,
		width : 400,
		title : '还原快照',
		overlay : {
			opacity : 0.5,
			background : "black",
			overflow : 'auto'
		},		buttons : buttons_
	});
};
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()	// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}
function findOperation(){
	  if($("#search_date_from").val()!="" && $("#search_date_to").val()!=""){
		   if($("#search_date_from").val() > $("#search_date_to").val()){
			   return "";
		    }
	  }
	  var page = 1;
	  var start = 1;
	  var limit = 7;
	  var query = "";
	  search(query, null, page, start, limit, REMOTE_URL + "ops/ops!pageVmOpsLog.action", function(data) {
		  var pageNo = data.resultObject.pageNo;
		  var pageSize = data.resultObject.pageSize;
		  var totalPages = data.resultObject.totalPages;
		  var totalCount = data.resultObject.totalCount;
		  var result = data.resultObject.result;
		  $("#listOPSTable").empty();
		  $("#listOPSTable").append("<tr background='images/1_02.png' height=\"35px;\"><th style=\"text-align: center;width: 25%\"><font color=\"#3c6ab9\">操作时间</font></th><th style=\"text-align: center;width: 25%\"><font color=\"#3c6ab9\">完成时间</font></th><th style=\"text-align: center;width: 15%\"><font color=\"#3c6ab9\">事件类型</font></th><th style=\"text-align: center;width: 10%\"><font color=\"#3c6ab9\">结果</font></th><th style=\"text-align: center;width: 25%\"><font color=\"#3c6ab9\">备注</font></th></tr>");
		  $.each(result, function(index, value) {
		     $("#listOPSTable").append("<tr><td  align=\"center\">"+formatDatetime(new Date(parseInt(value.event_time)))+"</td><td align=\"center\">"+formatDatetime(new Date(parseInt(value.update_time)))+"</td><td align=\"center\">"+optType[value.ops][value.ops]+"</td><td align=\"center\">"+res[value.result][value.result]+"</td><td  align=\"center\" style=\"text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space: nowrap;\" title=\""+(null == value.remark?"":value.remark)+"\">"+(null == value.remark?"":value.remark)+"</td></tr>");
		  });
		  $("#listOPSTable tr:even").addClass("even");
		  $("#listOPSTable tr:odd").addClass("odd");
	      var pager = new Pagination(totalPages, pageSize, pageNo, totalCount);
		  $("#pager").empty();
		  pager.render(transData).appendTo($("#pager"));
	 });
}
function transData(param1, param2, param3, param4) {
	var page = param3;
	var start = param2 * (param3 - 1) + 1;
	var limit = param2;
	var query = "";
	search(query, null, page, start, limit, REMOTE_URL +"ops/ops!pageVmOpsLog.action", function(data) {
		var result = data.resultObject.result;
		  $("#listOPSTable").empty();
		  $("#listOPSTable").append("<tr background='images/1_02.png' height=\"35px;\"><th style=\"text-align: center;width: 25%\"><font color=\"#646464\">操作时间</font></th><th style=\"text-align: center;width: 25%\"><font color=\"#646464\">完成时间</font></th><th style=\"text-align: center;width: 15%\"><font color=\"#646464\">事件类型</font></th><th style=\"text-align: center;width: 10%\"><font color=\"#646464\">结果</font></th><th style=\"text-align: center;width: 25%\"><font color=\"#646464\">备注</font></th></tr>");
		  $.each(result, function(index, value) {
		     $("#listOPSTable").append("<tr><td  align=\"center\">"+formatDatetime(new Date(parseInt(value.event_time)))+"</td><td align=\"center\">"+formatDatetime(new Date(parseInt(value.update_time)))+"</td><td align=\"center\">"+optType[value.ops][value.ops]+"</td><td align=\"center\">"+res[value.result][value.result]+"</td><td  align=\"center\" style=\"text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space: nowrap;\" title=\""+(null == value.remark?"":value.remark)+"\">"+(null == value.remark?"":value.remark)+"</td></tr>");
		  });
		  $("#listOPSTable tr:even").addClass("even");
		  $("#listOPSTable tr:odd").addClass("odd");
	});
}
function search(query, type, page, start, limit, url, callback) {
	var type = $("#opsType").find("option:selected").val();
	var startTime = $("#search_date_from").val();
	var endTime = $("#search_date_to").val();
	while(startTime.indexOf( "/" ) != -1 ){
		startTime = startTime.replace("/","-"); 
	}
	while(endTime.indexOf( "/" ) != -1 ){
		endTime = endTime.replace("/","-"); 
	}
	var jsonRequest;
    if(""!=startTime && ""!=endTime){
		 jsonRequest = {
					"logQueryVO.ops":type,
					"logQueryVO.startTime":startTime,
					"logQueryVO.endTime": endTime+" 23:59:59",
					"query" : query,
					"page" : page,
					"start" : start,
					"limit" : limit,
				    "sort": "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
		 };
	}else if(""!=startTime){
		 jsonRequest = {
					"logQueryVO.ops":type,
					"logQueryVO.startTime":startTime,
					"query" : query,
					"page" : page,
					"start" : start,
					"limit" : limit,
				    "sort": "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
		 };
	}else if(""!=endTime){
		 jsonRequest = {
					"logQueryVO.ops":type,
					"logQueryVO.endTime":endTime+" 23:59:59",
					"query" : query,
					"page" : page,
					"start" : start,
					"limit" : limit,
				    "sort": "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
		 };
	}else{
		 jsonRequest = {
					"logQueryVO.ops":type,
					"query" : query,
					"page" : page,
					"start" : start,
					"limit" : limit,
				    "sort": "[{\"property\":\"createDate\",\"direction\":\"DESC\"}]"
		 };
	}
	$.ajaxSettings.async = false;
	$.getJSON(url, jsonRequest, callback);
};
function checkNewPassword(){
	var newpassword = $("#newpassword").val();
	if(null == newpassword || "" == newpassword){
		$("#newpasswordmsg").attr("alt","新密码不能为空。").attr("title","新密码不能为空。").attr("src","../images/error.png");
		return false;
	}
	var pattern = new RegExp("['\"\\\\{}()]");
	if(pattern.test(newpassword)){
		$("#newpasswordmsg").attr("alt","不能含有特殊字符。").attr("title","不能含有特殊字符。").attr("src","../images/error.png");
		return false;
	}
	// by liyunhui 2013-05-21 bugId:0001871
	// 测试人员预期：控制面板密码不低于六位，必须包含数字、字母，大小写
	if (!(/[a-z]{1,}/.test(newpassword) && /[A-Z]{1,}/.test(newpassword)
			&& /[0-9]{1,}/.test(newpassword) && /^\w{6,}$/.test(newpassword))) {
		$("#newpasswordmsg").attr("alt", "密码至少为6位，必须包含大小写字母和数字").attr("title",
				"密码至少为6位，必须包含大小写字母和数字").attr("src", "../images/error.png");
		return false;
	}
	// end bugId:0001871
	$("#newpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
	return true;
}
function checkComfirmPassword(){
	var comfirpassword = $("#comfirpassword").val();
	var newpassword = $("#newpassword").val();
	if(null == comfirpassword || "" == comfirpassword){
		$("#comfirpasswordmsg").attr("alt","确认密码不能为空。").attr("title","确认密码不能为空。").attr("src","../images/error.png");
		return false;
	}
	var pattern = new RegExp("['\"\\\\{}()]");
	if(pattern.test(newpassword)){
		$("#comfirpasswordmsg").attr("alt","不能含有特殊字符。").attr("title","不能含有特殊字符。").attr("src","../images/error.png");
		return false;
	}
	if (!(/[a-z]{1,}/.test(comfirpassword) && /[A-Z]{1,}/.test(comfirpassword)
			&& /[0-9]{1,}/.test(comfirpassword) && /^\w{6,}$/.test(comfirpassword))) {
		$("#comfirpasswordmsg").attr("alt", "密码至少为6位，必须包含大小写字母和数字").attr("title",
				"密码至少为6位，必须包含大小写字母和数字").attr("src", "../images/error.png");
		return false;
	}	
	if(newpassword != comfirpassword){
		$("#comfirpasswordmsg").attr("alt","两次密码输入不一致").attr("title","两次密码输入不一致").attr("src","../images/error.png");
		return false;
	}	
	$("#comfirpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
	return true;
}
function checkOldPassword(){
	var oldpasswordmsg = $("#oldpassword").val();
	if(null == oldpasswordmsg || "" == oldpasswordmsg){
		$("#oldpasswordmsg").attr("alt","旧密码不能为空。").attr("title","旧密码不能为空。").attr("src","../images/error.png");
		return false;
	}
	$("#oldpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
	return true;
}
function modpassword(){
	if(!checkOldPassword() | !checkNewPassword() | !checkComfirmPassword()){
		return false;
	}
	var newpassword = $("#newpassword").val();
	var oldpassword = $("#oldpassword").val();
	var enNew=$.md5(newpassword);
	var enOld=$.md5(oldpassword);	
	var data = {'oldPassword':enOld,'newPassword':enNew};
    var url ="../controlpanel/login!resetPassword.action";
	$.ajax({
		  url: url,
		  type: 'POST',
		  dataType: 'json',
		  data: data,
		  success: function(data){
			  if(data.success){
					$("#oldpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
					hsCue("modpasswordmsg",lang.hscommon.success,function(){$("#dialog-passwordEdit").dialog("close")});
				}else{
					$("#oldpasswordmsg").attr("alt",data.resultMsg).attr("title",data.resultMsg).attr("src","../images/error.png");
					return false;
				}
		   },
		   error : function(XMLHttpRequest) {
				redirectLogin(XMLHttpRequest);
			}
	 });
}
function quit(){
	var location = "../index.html";
	var url = "../controlpanel/login!logout.action";
	$.ajax({
		  url: url,
		  type: 'POST',
		  dataType: 'json',
		  contentType: "application/json; charset=utf-8",
		  data: null,
		  success:function(data){
			  if(data['success']){
				  window.location.href = location;
			  }
		  }
	});
}
function clearPassword(){
	$("#oldpassword").val("");
	$("#oldpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
	$("#newpassword").val("");
	$("#newpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
	$("#comfirpassword").val("");
	$("#comfirpasswordmsg").attr("alt","").attr("title","").attr("src","../images/white.png");
}
function lastestSnapshot(){
	$.ajax({
		async : false,
		type : "post",
		url : REMOTE_URL + "ops/ops!getLastestVmSnapshot.action",
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				customizeAlert(json.resultMsg);
			}else{
				if(1==json.resultObject.result.hasExtDisk){
					$("#c_edisk1").css("display","block");
					$("#r_edisk1").css("display","block");
				}else if(2==json.resultObject.result.hasExtDisk){
					$("#c_edisk2").css("display","block");
					$("#r_edisk2").css("display","block");
				}else if(3==json.resultObject.result.hasExtDisk){
					$("#c_edisk1").css("display","block");
					$("#c_edisk2").css("display","block");
					$("#r_edisk1").css("display","block");
					$("#r_edisk2").css("display","block");
				}
			    if(null != json.resultObject.result.createTime){
			    	$("#snapshotid").val(json.resultObject.result.id);
			    	$("#title").html(json.resultObject.result.snapShot_name);
			    	$("#autotitle").val(json.resultObject.result.snapShot_name);
			    	$("#time").html(new Date(json.resultObject.result.createTime).format("yyyy年MM月dd日  hh点mm分ss秒"));
			    	$("#info").html(json.resultObject.result.snapShot_comments);
			    	$("#autoinfo").val(json.resultObject.result.snapShot_comments);
			    	var nowTime = new Date().getTime();
			    	// 快照创建已经花费的时间(毫秒计算)
			    	var spendTime = nowTime - json.resultObject.result.createTime;
			    	// 1.快照生成成功: 生成成功
			    	if (0 != json.resultObject.result.status) {
			    		$("#status").html("生成成功");
			    		$("#submitsnapshot").attr("src","images/createSnapshot.png").removeAttr("disabled").unbind().bind("click",createSnapshotDialog);
			    		$("#resetsnapshot").attr("src","images/revertSnapshot.png").removeAttr("disabled").unbind().bind("click",revertSnapshotDialog);
			    	}
			    	// 2.快照在30分钟之内状态还是为0:正在生成中
			    	else if (0 == json.resultObject.result.status 
			    			&& spendTime <= 1800000) {
			    		$("#status").html("正在生成中");
			    		$("#submitsnapshot").attr("disabled","disabled").attr("src","images/createSnapshot_gray.png").unbind();
			    		$("#resetsnapshot").attr("disabled","disabled").attr("src","images/revertSnapshot_gray.png").unbind();
			    	}
			    	// 3.快照超过30分钟状态还是为0： 生成失败
			    	else if (0 == json.resultObject.result.status
			    			&& spendTime > 1800000) {
			    		$("#status").html("生成失败");
			    		$("#submitsnapshot").attr("src","images/stopSnapshot.png").removeAttr("disabled").unbind().bind("click",stopSnapshotDialog);
			    		$("#resetsnapshot").attr("src","images/revertSnapshot.png").removeAttr("disabled").unbind().bind("click",revertSnapshotDialog);
			    	}
			    	$("#shotinf").css("display","block");
			    	$("#noshot").css("display","none");
			    }else{
			    	$("#noshot").css("display","block");
			    	$("#shotinf").css("display","none");
			    	//$("#shotreset").attr("disabled","disabled");
			    	$("#submitsnapshot").attr("src","images/createSnapshot.png").removeAttr("disabled").unbind().bind("click",createSnapshotDialog);
			    	$("#resetsnapshot").attr("src","images/revertSnapshot_gray.png").attr("disabled","disabled").unbind();
			    }
				//alert(null == json.resultObject.result.createTime?"ss":new Date(json.resultObject.result.createTime).format("yyyy-MM-dd"));
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
}

function backupVM(name,comm){

	var data = {'sn_name':name,'sn_comments':comm,'sn_type':0};
	hsCue("dialog-alert","备份已开始。",null);
	$.ajax({
		async : true,
		type : "post",
		url : REMOTE_URL + "ops/ops!backupVM.action",
		dataType : 'json',
		data: data,
		success : function(json) {
		  if (!json.success && json.resultCode=="03067"){
		      hsAlert("msg",json.resultMsg+"备份失败!",null);
		      $("#dialog-alert").dialog("close");
		  }else if(json.success){
		     lastestSnapshot();
		  }
        },
        error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	
}

function renewVm(){
	
    var snapshotid =  $("#snapshotid").val();
	var data = {'ssid':snapshotid};
	$.ajax({
		async : true,
		type : "post",
		url : REMOTE_URL + "ops/ops!renewVm.action",
		dataType : 'json',
		 data: data,
		success : function(json) {
			if (!json.success) {
				hsAlert("msg","还原失败,"+json.resultMsg,null);
			}else{
				hsCue("msg","还原成功。",null);
				
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	
}

function nulled(val){
	if(null == val || ""==val){
		return true;
	}else{
		return false;
	}
	
}

function checkedsnapshot(){	var vminfo = getVM_info();	if("SUSPENDED" == getVM_status(vminfo) || (getVM_task(vminfo) != "null" && getVM_task(vminfo) != "")){			customizeAlert(lang.vpdc.center.snapAlert);			return;		}	
	var name = $("#snapshot_name").val();
	var comm = $("#snapshot_comments").val();
	if(nulled(name)){
		hsAlert("msg","标题不能为空。",null);
		return false;
	}
	if(nulled(comm)){
		hsAlert("msg","备注不能为空。",null);
		return false;
	}
	return true;
}

function nosnapshot(){
	hsAlert("msg","您还没有快照。",null);
}

function timingSnapshot(){
	
	var type;
	var selectId;
	var planDate;
	var data;
	var planTime = "00:00";
    var radios = $("input[type='radio'][name='auto_type']:checked");
    if($("#autoback_h").val()=="" && !($("#autoback").is(":checked") && radios.length>0)){     hsAlert("msg","请填写计划信息。",null);
	   return;
    }
    if(radios.length==0){
	     hsAlert("msg","请选择具体时间。",null);
		   return;
	}    
	if($("#autoback").is(":checked")){
		 type = $("input[name='auto_type']:checked");
		 bpType = type.val();
		 
		 if("1"==bpType){
			 planDate = $("#week").val();
			 planTime = $("#week_time").val();
		 }else{
			 planDate = $("#month").val();			 planTime = $("#month_time").val();		 }

		 data = "{\"planType\":"+bpType+",\"planDate\":\""+planDate+"\"}";
	}else{
		data = "{\"planType\":\""+0+"\"}";
	}	
	$.ajax({
		async : true,
		type : "post",
		url : REMOTE_URL + "ops/ops!createBackupVmPlan.action?planTime="+planTime,
		contentType: "application/json; charset=utf-8",
		dataType : 'json',
		data:data,
		success : function(json) {
			if (!json.success) {
				hsAlert("msg","设置失败。",null);
			}else{
				hsCue("msg","设置成功。",null);
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
	
}

function getVmBackupPlan(){
	
	$.ajax({
		async : true,
		type : "post",
		url : REMOTE_URL + "ops/ops!getVmBackupPlan.action",
		dataType : 'json',
		success : function(json) {
			if (!json.success) {
				hsAlert("msg","获取计划失败。",null);
			}else{
			    if(null == json.resultObject || 0==json.resultObject.planType){
			    	return ;
			    }
			    var planType = json.resultObject.planType;
			    var planDate = json.resultObject.planDate;
			    var planTime = json.resultObject.planTime;
			    var pt = planTime;
			    $("#planId").val(json.resultObject.id);
			    $("#autoback_h").val(planType>0?planType:"");
			    $("#type0_h").val(planType);
			    $("#type1_h").val(planDate);
			    $("#type2_h").val(pt==null?"00:00":pt.substring(0,5));
			    
			    
			    
			   
//			    if(planType>0){
//			    	$("#autoback").attr("checked",'checked');
//			    	var radios = $("input[type='radio'][name='auto_type']");
//			    	$.each(radios, function(index, radio) {
//			    		$(radio).removeAttr("disabled");
//					});
//			    	var planDate = json.resultObject.planDate;
//				    $("input[type='radio'][value='"+planType+"']").attr("checked",'checked');
//				    var type = $("input[name='auto_type']:checked");
//					var selectId = type.attr("ptype");
//					$("#"+selectId).val(planDate);
//					$("#week_time").removeAttr("disabled");
//					$("#month_time").removeAttr("disabled");
//					var pt = json.resultObject.planTime;
//					$("#"+selectId+"_time").val(pt==null?"00:00":pt.substring(0,5));
//					
//			    }else{
//			    	$("#autoback").removeAttr("checked");
//			    	$("input[type='radio'][value='"+planType+"']").attr("checked",'checked');
//			    	var radios = $("input[type='radio'][name='auto_type']");
//			    	$.each(radios, function(index, radio) {
//			    		$(radio).removeAttr("checked").attr("disabled","disabled");
//					});
//			    	$("#week").removeAttr("disabled");
//			    	$("#month").removeAttr("disabled");
//					$("#week_time").val("00:00");
//					$("#month_time").val("00:00");
//			    }
			    if(planType>0){
			    	reset();
			    }else{
			    	var radios = $("input[type='radio'][name='auto_type']");
			    	$.each(radios, function(index, radio) {
			    		$(radio).attr("disabled","disabled").removeAttr("checked");
					});
			    	$("#month").attr("disabled","disabled").val(1);
					$("#month_time").attr("disabled","disabled").val("00:00");
			    	$("#week").attr("disabled","disabled").val(1);
					$("#week_time").attr("disabled","disabled").val("00:00");
			    	$("#submitplan").unbind();
			    	$("#submitplan").attr("src","images/submit_gray.png").unbind();
			    	$("#resetplan").attr("src","images/reset_gray.png").unbind();
			    }
			    
				
			}
		},
		error : function(XMLHttpRequest) {
			redirectLogin(XMLHttpRequest);
		}
	});
}

function autoBack(){
	
	if($("#autoback").is(":checked")){
    	$("#autoback").attr("checked",'checked');
    	var radios = $("input[type='radio'][name='auto_type']");
    	$.each(radios, function(index, radio) {
    		$(radio).removeAttr("disabled");
		});
		$("#week").removeAttr("disabled");
    	$("#month").removeAttr("disabled");
    }else{
    	$("#autoback").removeAttr("checked");
    	var radios = $("input[type='radio'][name='auto_type']");
    	$.each(radios, function(index, radio) {
    		$(radio).attr("disabled","disabled");
		});
    	$("#week").attr("disabled","disabled");
    	$("#month").attr("disabled","disabled");
    }
	
}

function reset(){
	var checked = $("#autoback").is(":checked") ;	var type = $("#type0_h").val();
	if(""==type){
		$("#autoback").removeAttr("checked");
        $("#month_r").removeAttr("checked");
        $("#week_r").removeAttr("checked");
        $("#month").attr("disabled","disabled").val(1);
        $("#week").attr("disabled","disabled").val(1);
        $("#month_time").attr("disabled","disabled").val("00:00");
        $("#week_time").attr("disabled","disabled").val("00:00");
        $("#submitplan").attr("src","images/submit_gray.png").unbind();
		$("#resetplan").attr("src","images/reset_gray.png").unbind();
		$("#submitplan").removeClass().addClass("button_gray").unbind();
		
	}else{
		$("#autoback").attr("checked","checked");
	    $("#type0_h").val("");
		var checked = $("input[type='radio'][value='"+type+"']");
		checked.attr("checked",'checked');
    	var radios = $("input[type='radio'][name='auto_type']");
    	$.each(radios, function(index, radio) {
    		$(radio).removeAttr("disabled");
		});
		var id = checked.attr("ptype");
		$("#"+id).val($("#type1_h").val()).removeAttr("disabled");
		$("#"+id+"_time").val($("#type2_h").val()).removeAttr("disabled");
		if(id=="week"){
            $("#month_r").removeAttr("checked");
            $("#month").attr("disabled","disabled").val(1);
            $("#month_time").attr("disabled","disabled").val("00:00");
		}else{
			$("#week_r").removeAttr("checked");
            $("#week").attr("disabled","disabled").val(1);
            $("#week_time").attr("disabled","disabled").val("00:00");
		}       
		$("#submitplan").attr("src","images/submit.png").unbind().bind("click",timingSnapshot);
		$("#resetplan").attr("src","images/reset.png").unbind().bind("click",reset);
	
	}
	
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
//验证密码规则：数字+大小写字母
function verifyPwd(pwd,pwd2){
	if(pwd==null || pwd==""){
		customizeAlert(lang.vpdc.center.CPpwdnull);
		return false;
	}
	if(pwd.length<6){
		customizeAlert(lang.vpdc.center.CPpwdlength);
		return false;
	}
	/*var digitals = [0,1,2,3,4,5,6,7,8,9];
	var lowerCaseLetters = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','v','y','z'];
	var upperCaseLetters = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','V','Y','Z'];
	
	var digitalFlag = false;
	var lowerLetterFlag = false;
	var upperLetterFlag = false;
	
	for(var i = 0; i < pwd.length; i++) {
		if(checkPassword(digitals, pwd[i]) == true) {
			digitalFlag = true;
		} else if(checkPassword(lowerCaseLetters, pwd[i]) == true) {
			lowerLetterFlag = true;
		} else if(checkPassword(upperCaseLetters, pwd[i]) == true) {
			upperLetterFlag = true;
		} else {
			customizeAlert(lang.vpdc.center.pwdStyleErr);
			return false;
		}
	}
	
	if(digitalFlag == false || lowerLetterFlag == false
			|| upperLetterFlag == false) {
		customizeAlert(lang.vpdc.center.pwdStyleErr);
		return false;
	}*/
	
	var pattern = new RegExp("['\"\\\\{}()]");
	if(pattern.test(pwd)){
		customizeAlert(lang.vpdc.center.pwdStyleErr);
		return false;
	}
	if (!(/[a-z]{1,}/.test(pwd) && /[A-Z]{1,}/.test(pwd)
			&& /[0-9]{1,}/.test(pwd) && /^\w{6,}$/.test(pwd))) {
		customizeAlert(lang.vpdc.center.pwdStyleErr);
		return false;
	}
	
	if (pwd != pwd2) {
		customizeAlert(lang.vpdc.center.CPpwdDiff);
		return false;
	}
}

//检查密码
function checkPassword(array, letter) {
	for(var index = 0; index < array.length; index++) {
		if(letter == array[index]){
			return true;
		}
	}
	return false;
}

/**
 * $(document).ready():初始化 修改VPDC页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('vpdc/vcenter/modifyVPDC.css');
	initDialog();//加载dialog的css和Js
	i18n.init();//国际化初始化
	vpdcCenterPageUrls=["../../vpdc/center/vmList.html","../../vpdc/vcenter/modifyVPDC.html"];
	initTagAndMenu(vpdcCenterNameArr, vpdcCenterPageUrls);
	i18nPage();//国际化当前页面
	selectedVpdcId = $.cookie("selectedVpdcId");//当前用户前往VPDC的id
	$.getScript('../../../skin/'+customerPath+'/js/common/dialog/dialog.js', function(){
		initVpdcInfo();//初始化VPDC数据信息
	});
});

var currentMode = "nonRoutingMode";//1.非路由模式 nonRoutingMode 2.路由模式 routingMode
var selectedVpdcId;//当前用户前往VPDC的id

function i18nPage() {
	$("#vpdcNameLabel").text(i18n.get("vpdcNameLabel"));
	$("#chooseModeLabel").text(i18n.get("chooseModeLabel"));
	$("#nonRoutingModeDesc").text(i18n.get("nonRoutingModeDesc"));
	$("#routingModeDesc").text(i18n.get("routingModeDesc"));
	$("#selectRoom1").text(i18n.get("selectRoom"));
	$("#selectRoom2").text(i18n.get("selectRoom"));
	$("#purchaseTime").text(i18n.get("purchaseTime"));
	$("#chooseBandWidth").text(i18n.get("chooseBandWidth"));
	$("#addVLAN").text(i18n.get("addVLAN"));
	$("#scale1").text(i18n.get("scale"));
	$("#tai1").text(i18n.get("tai"));
	$("#totalCostLabel").text(i18n.get("totalCost"));
	$("#yuan").text(i18n.get("yuan"));
	$("#modify").text(i18n.get("modifyVpdc2"));
	$("#cancel").text(i18n.get("operation_cancel"));
	//选择模式的2张大图片的颜色
	$("#nonRoutingMode").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/vpdc/center/nonRoutingMode.png)");
	$("#routingMode").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/vpdc/center/routingMode.png)");
	$("#nonRoutingModeDesc").css({"color": "#3366FF", "font-weight": "bold"});
	$(".selectTag").eq(0).attr("goto", "../../vpdc/vcenter/VPDC.html?LIIndex=1");
}

/**
 * 验证字符串包含非法字符
 */
function checkHasContainedIllegalChar(str) {
	var pattern = new RegExp("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
	if (!pattern.test(str)) {
		return false;
	}
	return true;
}

/**
 * 1.检查VPDC名称是否符合规范
 */
function checkVpdcName() {
	// 检查VPDC名称是否为空
	if ($.trim($("#vpdcName").val()) == null || $.trim($("#vpdcName").val()) == "") {
		verifyStyle(false, "vpdcName_message", i18n.get("enterVpdcName"));
		$("#vpdcName").css("border", "1px solid red");
		return false;
	}
	// 检查VPDC名称是否包含非法字符串
	if (checkHasContainedIllegalChar($.trim($("#vpdcName").val()))) {
		verifyStyle(false, "vpdcName_message", i18n.get("containsIllegalCharacters"));
		$("#vpdcName").css("border", "1px solid red");
		return false;
	}
	$("#vpdcName").css("border", "1px solid #CCCCCC");
	verifyStyle(true, "vpdcName_message", "");
	return true;	
}

/**
 * 2.选择模式：(1).非路由模式 nonRoutingMode (2).路由模式 routingMode
 */
function chooseMode(modeName) {
	if ("nonRoutingMode" == modeName) {//1.非路由模式 nonRoutingMode
		currentMode = "nonRoutingMode";
		$("#nonRoutingModeDiv").css("display", "block");
		$("#routingModeDiv").css("display", "none");
		$("#nonRoutingMode").addClass("lightedStyle");
		$("#nonRoutingModeDesc").css({"color": "#3366FF", "font-weight": "bold"});
		$("#routingMode").removeClass("lightedStyle");
		$("#routingModeDesc").css({"color": "#333333", "font-weight": "normal"});
		$("#main").css("height", "600px");
	} 
	else {//2.路由模式 routingMode
		currentMode = "routingMode";
		$("#nonRoutingModeDiv").css("display", "none");
		$("#routingModeDiv").css("display", "block");
		$("#routingMode").addClass("lightedStyle");
		$("#routingModeDesc").css({"color": "#3366FF", "font-weight": "bold"});
		$("#nonRoutingMode").removeClass("lightedStyle");
		$("#nonRoutingModeDesc").css({"color": "#333333", "font-weight": "normal"});
		$("#main").css("height", "auto");	
	}
}

/**
 * 初始化VPDC数据信息
 */
function initVpdcInfo() {
	$.ajax({
		url : "../../../ops/ops!getVpdcById.action",
		type : "get",
		dataType : 'json',
		async : false,
		data : "vpdcId=" + selectedVpdcId,
		success : function(json) {
			if (json.success) {
				$("#vpdcName").val(json.resultObject.name);//VPDC名称
				var vpdcType = json.resultObject.vpdcType;
				if (vpdcType == 0) {//VPDC类型（0：非路由；1：路由）
					chooseMode("nonRoutingMode");
					var zoneGroup = json.resultObject.zoneGroup;//机房线路
					var zoneGroupName = json.resultObject.zoneGroupName;//机房线路名称
					$("#nonRoutingModeZoneSelect option[value=-1]").remove();
					$("#nonRoutingModeZoneSelect").append("<option value='"+zoneGroup+"'>"+zoneGroupName+"</option>");
				} 
				else {
					chooseMode("routingMode");
					var zoneGroup = json.resultObject.zoneGroup;//机房线路
					var zoneGroupName = json.resultObject.zoneGroupName;//机房线路名称
					$("#routingModeZoneSelect option[value=-1]").remove();
					$("#routingModeZoneSelect").append("<option value='"+zoneGroup+"'>"+zoneGroupName+"</option>");
					var useLong = json.resultObject.useLong;//购买时长
					$("#timerSelect option[value=-1]").remove();
					$("#timerSelect").append("<option value='"+useLong+"'>"+useLong+i18n.get("timeDesc")+"</option>");
					var bandWidth = json.resultObject.bandWidth;//选择带宽
					$("#slider-range-min").slider({
						range : "min",
						value : bandWidth,
						min : bandWidth,
						max : bandWidth,
						slide : function(event, ui) {
							$("#bandWidth").val(ui.value);
						}
					});
					$("#bandWidth").val($("#slider-range-min").slider("value"));
					var vlans = json.resultObject.vlans;//VLAN设置: DNS1 DNS2 规模数量
					for (var i = 0; i < vlans.length; i++) {
						if (i == 0) {//给默认的第1条直接赋值
							$("#DNS1_1").val(vlans[i].dns1);
							$("#DNS2_1").val(vlans[i].dns2);
							$("#amount_1").val(vlans[i].size);
							$("#amount_1").spinner({//VLAN设置的规模数量
								min : vlans[i].size,
								max : vlans[i].size
							});
						}
						else {//第2条以及以上添加tr
							var html = '<tr><td colspan="2" style="height: 20px;"></td></tr>'+
								       '<tr id="vlanSettingTr'+(i+1)+'">'+
							           '   <td id="addVLAN" class="vpdcTd1"><!-- VLAN设置： --></td>'+
							           '    <td class="vpdcTd2">'+
								       '        <div style="float:left;width:500px;height:40px;line-height:26px;border: 2px solid #84C020;padding-top:10px;padding-left:5px;border-radius: 5px;">'+
								       '        	<font class="DNS">DNS1</font><input id="DNS1_'+(i+1)+'" class="DNS_input1" type="text"/>'+
									   '            <font class="DNS">DNS2</font><input id="DNS2_'+(i+1)+'" class="DNS_input2" type="text"/>'+
									   '            <font id="scale'+(i+1)+'" class="DNS"><!-- 规模 --></font>'+
									   '            <input id="amount_'+(i+1)+'" class="amount" style="font-size: 14px;" type="text" readonly="readonly"/>'+
									   '            <font id="tai'+(i+1)+'" style="font-size: 14px;font-family: 微软雅黑;color: black;margin-top: 2px;"><!-- 台 --></font>'+
								       '        </div>'+					                
							           '    </td>'+
							           '</tr>';
							$(html).insertAfter($("#vlanSettingTr"+i));
							$("#DNS1_"+(i+1)).val(vlans[i].dns1);
							$("#DNS2_"+(i+1)).val(vlans[i].dns2);
							$("#amount_"+(i+1)).val(vlans[i].size);
							$("#amount_"+(i+1)).spinner({//VLAN设置的规模数量
								min : vlans[i].size,
								max : vlans[i].size
							});
							$("#scale"+(i+1)).text(i18n.get("scale"));
							$("#tai"+(i+1)).text(i18n.get("tai"));
						}
					}
				}
			} 
			else {
				openDialog(json.resultMsg);
			}
		}
	});	
}

//校验dns是否符合规范
function checkDns() {
	if ($("#DNS1_1").val() == "") {
		verifyStyle(false, "dnsLine1_message", i18n.get("dnsNotEmpty"));
		$("#DNS1_1").css('border', '1px solid red');
		return false;
	}	
	if ($("#DNS1_1").val().split(".").length != 4) {
		verifyStyle(false, "dnsLine1_message", i18n.get("dns_error"));
		$("#DNS1_1").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS2_1").val() == "") {
		verifyStyle(false, "dnsLine1_message", i18n.get("dnsNotEmpty"));
		$("#DNS2_1").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS2_1").val().split(".").length != 4) {
		verifyStyle(false, "dnsLine1_message", i18n.get("dns_error"));
		$("#DNS2_1").css('border', '1px solid red');
		return false;
	}	
	verifyStyle(true, "dnsLine1_message", "");
	$("#DNS1_1").css('border', '1px solid #CCCCCC');		
	$("#DNS2_1").css('border', '1px solid #CCCCCC');		
	return true;
}

// 校验dns1是否符合规范
function checkDns1() {
	if ($("#DNS1_1").val() == "") {
		verifyStyle(false, "dnsLine1_message", i18n.get("dnsNotEmpty"));
		$("#DNS1_1").css('border', '1px solid red');
		return false;
	}	
	if ($("#DNS1_1").val().split(".").length != 4) {
		verifyStyle(false, "dnsLine1_message", i18n.get("dns_error"));
		$("#DNS1_1").css('border', '1px solid red');
		return false;
	}
	verifyStyle(true, "dnsLine1_message", "");
	$("#DNS1_1").css('border', '1px solid #CCCCCC');		
	return true;
}

// 校验dns2是否符合规范
function checkDns2() {
	if ($("#DNS2_1").val() == "") {
		verifyStyle(false, "dnsLine1_message", i18n.get("dnsNotEmpty"));
		$("#DNS2_1").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS2_1").val().split(".").length != 4) {
		verifyStyle(false, "dnsLine1_message", i18n.get("dns_error"));
		$("#DNS2_1").css('border', '1px solid red');
		return false;
	}	
	verifyStyle(true, "dnsLine1_message", "");
	$("#DNS2_1").css('border', '1px solid #CCCCCC');		
	return true;
}

/**
 * 点击了确定按钮的逻辑
 */
function modifyVpdc() {
	if ("nonRoutingMode" == currentMode) {//1.非路由模式 nonRoutingMode
		if (checkVpdcName()) {
			$.ajax({
				url : "../../../ops/ops!updateVPDC.action",
				type : "post",
				dataType : 'json',
				async : false,
				data : "vpdcId="+selectedVpdcId+"&name="+$.trim($("#vpdcName").val())+"&class_dns1=&class_dns2=",
				success : function(json) {
					if (json.success) {
	 					openDialog(i18n.get("modifyVpdcSuccess"));
						// 点击Dialog的确定按钮跳转到VPDC列表
						var closeButtonAttr = $(".dialogButton2").eq(0).attr("onclick")+"turnToVPDC();";
						$(".dialogButton2").eq(0).attr("onclick", closeButtonAttr);
					} 
					else {
						openDialog(json.resultMsg);
					}
				}
			});			
		}
	} 
	else {//2.路由模式 routingMode
		if (checkVpdcName() & checkDns()) {
			var class_dns1 = "";//所有dns1的值
			var DNS_input1 = $(".DNS_input1");
			for (var i = 0; i < DNS_input1.length; i++) {
				class_dns1 += DNS_input1.eq(i).val() + ",";
			}
			class_dns1 = class_dns1.substring(0, class_dns1.length - 1);
			var class_dns2 = "";//所有dns2的值
			var DNS_input2 = $(".DNS_input2");
			for (var i = 0; i < DNS_input2.length; i++) {
				class_dns2 += DNS_input2.eq(i).val() + ",";
			}
			class_dns2 = class_dns2.substring(0, class_dns2.length - 1);
			
			$.ajax({
				url : "../../../ops/ops!updateVPDC.action",
				type : "post",
				dataType : 'json',
				async : false,
				data : "vpdcId="+selectedVpdcId+"&name="+$.trim($("#vpdcName").val())
				     +"&class_dns1="+class_dns1+"&class_dns2="+class_dns2,
				success : function(json) {
					if (json.success) {
	 					openDialog(i18n.get("modifyVpdcSuccess"));
						// 点击Dialog的确定按钮跳转到VPDC列表
						var closeButtonAttr = $(".dialogButton2").eq(0).attr("onclick")+"turnToVPDC();";
						$(".dialogButton2").eq(0).attr("onclick", closeButtonAttr);
					} 
					else {
						openDialog(json.resultMsg);
					}
				}
			});			
		}
	}
}

// 点击确定按钮跳转回到VPDC列表
function turnToVPDC() {
	window.location = rootPath+"/function/vpdc/vcenter/VPDC.html?LIIndex=1";						
}
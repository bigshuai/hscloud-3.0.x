/**
 * $(document).ready():初始化 添加VPDC页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('vpdc/vcenter/createVPDC.css');
	initDialog();//加载dialog的css和Js
	i18n.init();//国际化初始化
	vpdcCenterPageUrls=["../../vpdc/center/vmList.html","../../vpdc/vcenter/createVPDC.html"];
	initTagAndMenu(vpdcCenterNameArr, vpdcCenterPageUrls);
	i18nPage();//国际化当前页面
	$.getScript('../../../skin/'+customerPath+'/js/common/dialog/dialog.js', function(){
		initPageData();//初始化页面默认加载的数据
	});
});

var currentMode = "nonRoutingMode";//1.非路由模式 nonRoutingMode 2.路由模式 routingMode
var periodArray = [1,3,6,12];
var vrouterTemplate;//VrouterTemplate模板
var serviceItemIds = new Array();
var vlanParams = new Array();

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
	$("#scale").text(i18n.get("scale"));
	$("#tai").text(i18n.get("tai"));
	$("#btn-count").text(i18n.get("Calculate"));//计算价格
	$("#price-label").text(i18n.get("Cost"));//费用：
	$("#price-unit-label").text(i18n.get("yuan"));//元
	$("#price-tip-label").text(i18n.get("Depending on your configuration, auto quotes."));//(根据您的配置，自动报价)
	$("#create").text(i18n.get("create"));
	$("#cancel").text(i18n.get("operation_cancel"));
	//选择模式的2张大图片的颜色
	$("#nonRoutingMode").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/vpdc/center/nonRoutingMode.png)");
	$("#routingMode").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/vpdc/center/routingMode.png)");
	$("#nonRoutingModeDesc").css({"color": "#3366FF", "font-weight": "bold"});
	$("#nonRoutingModeZoneSelect")[0].options.add(new Option(i18n.get('zoneGroupSelect'),-1));//请选择机房
	$("#routingModeZoneSelect")[0].options.add(new Option(i18n.get('zoneGroupSelect'),-1));//请选择机房
	$(".selectTag").eq(0).attr("goto", "../../vpdc/vcenter/VPDC.html?LIIndex=1");
}

//初始化页面默认加载的数据
function initPageData() {
	nonRoutingModeZoneGroupStore.load();//非路由模式选择机房
	$("#nonRoutingModeZoneSelect").bind('mousedown', function() {
		$("#nonRoutingModeZoneSelect option[value=-1]").remove();
		checkNonRoutingModeRoomLine();
	});
	routingModeZoneGroupStore.load();//路由模式选择机房
	$("#routingModeZoneSelect").bind('mousedown', function() {
		$("#routingModeZoneSelect option[value=-1]").remove();
		checkRoutingModeRoomLine();
	});
	periodInit();//购买时长初始化
	vrouterTemplateInit();//VrouterTemplate初始化
	netStore.load();//带宽初始化
	$("#amount").spinner({//VLAN设置的规模数量
		min : 32,
		max : 64,
		step : 32
	});
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
		$("#main").css("height", "700px");	
	}
}

/******************************** 1.非路由模式处理逻辑 *********************************/
/**
 * 非路由模式机房线路数据请求
 */
var nonRoutingModeZoneGroupStore={
		url:rootPath+'/sc/zoneGroup!getCustomZoneGroupsByUser.action?',
		type:'GET',
		dataType:'json',
		contentType: 'application/json; charset=utf-8',
		load:function(){
			$.ajax({
				url : this.url,
				type: this.type,
				dataType:this.dataType,
				contentType: this.contentType,
				success:function(data,textStatus){
					if(data.success == true) {
						var result = data.resultObject;
						nonRoutingModeZoneGroupStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#nonRoutingModeZoneSelect").empty();
				$("#nonRoutingModeZoneSelect")[0].options.add(new Option(i18n.get('zoneGroupSelect'),-1));//请选择机房
				$.each(result, function(index, value) {
					$("#nonRoutingModeZoneSelect")[0].options.add(new Option(value['name'], value['id']));
				});				
			}
		}
};

/**
 *  非路由模式:检查是否选择了机房线路
 */
function checkNonRoutingModeRoomLine() {
	if ((typeof $("#nonRoutingModeZoneSelect").val() != "string")
			|| ($("#nonRoutingModeZoneSelect").val() == "-1")) {
		verifyStyle(false, "nonRouting_message", i18n.get("zoneGroupSelect"));
		$("#nonRoutingModeZoneSelect").css('border', '1px solid red');
		return false;
	} 
	else {
		verifyStyle(true, "nonRouting_message", "");
		$("#nonRoutingModeZoneSelect").css('border', '1px solid #CCCCCC');
		return true;
	}
}

/********************************* 2.路由模式处理逻辑 **********************************/
/**
 * 路由模式机房线路数据请求
 */
var routingModeZoneGroupStore={
		url:rootPath+'/sc/zoneGroup!getCustomZoneGroupsByUser.action?',
		type:'GET',
		dataType:'json',
		contentType: 'application/json; charset=utf-8',
		load:function(){
			$.ajax({
				url : this.url,
				type: this.type,
				dataType:this.dataType,
				contentType: this.contentType,
				success:function(data,textStatus){
					if(data.success == true) {
						var result = data.resultObject;
						routingModeZoneGroupStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#routingModeZoneSelect").empty();
				$("#routingModeZoneSelect")[0].options.add(new Option(i18n.get('zoneGroupSelect'),-1));//请选择机房
				$.each(result, function(index, value) {
					$("#routingModeZoneSelect")[0].options.add(new Option(value['name'], value['id']));
				});				
			}
		}
};

//购买时长初始化
function periodInit(){
	$("#timer-select").empty();
	for(var i=0;i<periodArray.length;i++){
		$("#timer-select")[0].options.add(new Option(periodArray[i]+i18n.get('month'), periodArray[i]));
	}
};

// VrouterTemplate初始化
function vrouterTemplateInit() {
	vrouterTemplate = null;//清空以前的数据
	$.ajax({
		url : "../../../ops/ops!getVrouterTemplate.action",
		type : "get",
		dataType : 'json',
		async : false,
		success : function(json) {
			if (json.success) {
				vrouterTemplate = json.resultObject;
			} else {
				openDialog(json.resultMsg);
			}
		}
	});
}

// net数据请求
var netStore={
		url:rootPath+'/sc/serviceItem!listServiceItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':5,sort:'[{"property":"bandWidth","direction":"ASC"}]'},
		contentType: 'application/json; charset=utf-8',
		load:function(){
			$.ajax({
				url : this.url,
				type: this.type,
				dataType:this.dataType,
				data:this.data,
				contentType: this.contentType,
				success:function(data,textStatus){
					if(data.success == true) {
						var result = data.resultObject;
						netStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#slider-range-min").slider({
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function(event, ui) {
						$("#bandWidth").val(result[ui.value-1].bandWidth);
						$("#net_itemid").val(result[ui.value-1].id);
					}
				});
				$("#bandWidth").val(result[0].bandWidth);
				$("#net_itemid").val(result[0].id);
			}
		}
};

/**
 *  路由模式:检查是否选择了机房线路
 */
function checkRoutingModeRoomLine() {
	if ((typeof $("#routingModeZoneSelect").val() != "string")
			|| ($("#routingModeZoneSelect").val() == "-1")) {
		verifyStyle(false, "routing_message", i18n.get("zoneGroupSelect"));
		$("#routingModeZoneSelect").css('border', '1px solid red');
		return false;
	} 
	else {
		verifyStyle(true, "routing_message", "");
		$("#routingModeZoneSelect").css('border', '1px solid #CCCCCC');
		return true;
	}
}

/********************************* 3.点击创建按钮的逻辑 ********************************/
//校验dns是否符合规范
function checkDns() {
	if ($("#DNS1").val() == "") {
		verifyStyle(false, "dns_message", i18n.get("dnsNotEmpty"));
		$("#DNS1").css('border', '1px solid red');
		return false;
	}	
	if ($("#DNS1").val().split(".").length != 4) {
		verifyStyle(false, "dns_message", i18n.get("dns_error"));
		$("#DNS1").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS2").val() == "") {
		verifyStyle(false, "dns_message", i18n.get("dnsNotEmpty"));
		$("#DNS2").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS2").val().split(".").length != 4) {
		verifyStyle(false, "dns_message", i18n.get("dns_error"));
		$("#DNS2").css('border', '1px solid red');
		return false;
	}	
	verifyStyle(true, "dns_message", "");
	$("#DNS1").css('border', '1px solid #CCCCCC');		
	$("#DNS2").css('border', '1px solid #CCCCCC');		
	return true;
}

// 校验dns1是否符合规范
function checkDns1() {
	if ($("#DNS1").val() == "") {
		verifyStyle(false, "dns_message", i18n.get("dnsNotEmpty"));
		$("#DNS1").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS1").val().split(".").length != 4) {
		verifyStyle(false, "dns_message", i18n.get("dns_error"));
		$("#DNS1").css('border', '1px solid red');
		return false;
	}	
	verifyStyle(true, "dns_message", "");
	$("#DNS1").css('border', '1px solid #CCCCCC');		
	return true;
}

// 校验dns2是否符合规范
function checkDns2() {
	if ($("#DNS2").val() == "") {
		verifyStyle(false, "dns_message", i18n.get("dnsNotEmpty"));
		$("#DNS2").css('border', '1px solid red');
		return false;
	}
	if ($("#DNS2").val().split(".").length != 4) {
		verifyStyle(false, "dns_message", i18n.get("dns_error"));
		$("#DNS2").css('border', '1px solid red');
		return false;
	}	
	verifyStyle(true, "dns_message", "");
	$("#DNS2").css('border', '1px solid #CCCCCC');		
	return true;
}

// 计算价格
function calculatePrice() {
	$.ajax({
		url : billingServer+"calculatePrice",
		type : 'GET',
		dataType : 'jsonp',
		jsonp:'callback',
		jsonpCallback:'requestData',
		contentType: 'application/json; charset=utf-8',
		async : false,
		data : {
			planId:planId,//config.js中配置
			domainCode:domainCode,//config.js中配置
			CPU:vrouterTemplate.cpuCore,
			memory:vrouterTemplate.memSize,
			extDisk:"",
			bandwidth:$("#bandWidth").val(),
			payMonth:$("#timer-select").find('option:selected').val(),
			IP:1,
			upgrade:0,
			systemDisk:vrouterTemplate.diskCapacity
		},
		success : function(data) {			
			if (data == undefined) {
				Dialog.alert("vmbuss_error");
			} else {
				var totalPrice = parseFloat(data);
				$("#price").text(totalPrice.toFixed(2));
			}
		}
	});
}

/**
 * 点击了创建按钮的逻辑
 */
function createVpdc() {
	if ("nonRoutingMode" == currentMode) {//1.非路由模式 nonRoutingMode
		if (checkVpdcName() & checkNonRoutingModeRoomLine()) {
			$.ajax({
				url : "../../../ops/ops!createVPDC.action",
				type : "post",
				dataType : 'json',
				async : false,
				data : "name=" + $.trim($("#vpdcName").val()) + "&vpdcType=0&zoneGroup="
						+ $("#nonRoutingModeZoneSelect").val(),
				success : function(json) {
					if (json.success) {
						window.location = rootPath+"/function/vpdc/vcenter/VPDC.html?LIIndex=1";
					} 
					else {
						openDialog(json.resultMsg);
					}
				}
			});			
		}
	} 
	else {//2.路由模式 routingMode
		if (checkVpdcName() & checkRoutingModeRoomLine() & checkDns()) {
			//带宽的id通过前台传过去,CPU 内存   操作系统 这些的id[不是数值]通过后台接口传过去
			var itemId_net = $("#net_itemid").val();// 带宽
			if (itemId_net != undefined) {
				serviceItemIds.push(itemId_net);
			}	
			// 按先后顺序放name,dns1,dns2,vlan的数量这4个参数
			vlanParams.push($("#vpdcName").val());
			vlanParams.push($("#DNS1").val());
			vlanParams.push($("#DNS2").val());
			vlanParams.push($("#amount").val());
			$.ajax({
				url : "../../../order/order!submitRouterOrder.action",
				type : 'POST',
				dataType : 'json',
				async : false,
				traditional: true,
				data : {
					"submitData.zoneGroupId":$("#routingModeZoneSelect").find('option:selected').val(),
					"submitData.buyPeriod":$("#timer-select").find('option:selected').val(),
					"submitData.serviceItemIds":serviceItemIds,
					"submitData.planId":planId,
					"submitData.ipNum":1,
					"submitData.vlanParams":vlanParams
				},
				success : function(data) {
					if (data['success']) {
						window.location = rootPath+"/function/business/orderunpaid/index.html?LIIndex=0";			
					}
					else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		}
	}
}
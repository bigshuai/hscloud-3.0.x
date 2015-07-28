/**
 * $(document).ready():初始化 添加云主机页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('vpdc/vcenter/addVM.css');
	initDialog();//加载dialog的css和Js
	i18n.init();//国际化初始化
	vpdcCenterPageUrls=["../../vpdc/center/vmList.html","../../vpdc/vcenter/addVM.html"];
	initTagAndMenu(vpdcCenterNameArr, vpdcCenterPageUrls);
	selectedVpdcId = $.cookie("selectedVpdcId");//当前用户前往VPDC的id
	selectedVpdcMode = $.cookie("selectedVpdcMode");//当前用户前往VPDC的模式
	selectedVpdcZoneGroupId = $.cookie("selectedVpdcZoneGroupId");//当前用户前往VPDC的zoneGroupId
	i18nPage();//国际化当前页面
	$.getScript('../../../skin/'+customerPath+'/js/common/dialog/dialog.js', function(){
		initAddVMPage();//初始化加载数据
	});
});

var extDisk = 0;
var extDiskArray = new Array();
var IPCount = 1;
var serviceItemIds;
var selectedVpdcId;//当前用户前往VPDC的id
var selectedVpdcMode;//当前用户前往VPDC的模式
var vmAddIndex = 0;//第几次添加云主机(不是云主机的序号)
var selectedVpdcZoneGroupId;//当前用户前往VPDC的zoneGroupId
var periodArray = [1,3,6,12];
var vmArray = new Array();//存放所有云主机的数组

function i18nPage() {
	$("#memoryLabel").text(i18n.get("memory-title"));// 内存：
	$("#addExtDisk").text(i18n.get("Add a data disk"));//添加一块数据盘
	$("#bandwithLabel").text(i18n.get("bandwidthL"));//带宽：
	$("#AssignIP-tip").text(i18n.get("Assign a new IP"));//分配新IP
	$("#turnToStep2").text(i18n.get("nextStep"));
	$("#cancelCreateVM").text(i18n.get("operation_cancel"));
	$("#filtering").text(i18n.get("filtering"));
	$("#OS").text(i18n.get("OS"));
	$("#timer-title").text(i18n.get("buy-timer"));//购买时长：
	$("#timer-title2").text(i18n.get("buy-timer2"));//购买时长
	$("#previousStep").text(i18n.get("previousStep"));
	$("#turnToStep3").text(i18n.get("nextStep"));
	$("#osType_0").text(i18n.get("vmbuss_all"));
	$("#history1").text(i18n.get("history1"));
	$("#vmConfiguration").text(i18n.get("vmConfiguration"));
	$("#OS2").text(i18n.get("OS2"));
	$("#money_yuan").text(i18n.get("money_yuan"));
	$("#addOnceAgain").text(i18n.get("addOnceAgain"));
	$("#generateOrders").text(i18n.get("generateOrders"));
	if (selectedVpdcMode == "routingMode") {// 非路由模式才分配IP
		$("#nonRouterAssignIP").text("");
	}
	$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'), -1));//请选择操作系统
	$(".selectTag").eq(0).attr("goto", "../../vpdc/vcenter/VPDC.html?LIIndex=1");
}

/**
 * 初始化添加云主机页面
 */
function initAddVMPage() {
	CPUStore.load();
	memoryStore.load();
	extDiskStore.load();
	netStore.load();
	OSStore.load();//默认加载全部的操作系统,不区分windows和linux
	periodInit();//购买时长初始化
	$("#add_extDisk-btn").bind('click',addExtDisk);
	$("#add_IP-btn").bind('click',addIP);
	$("#reduce_IP-btn").bind('click',reduceIP);
	$(".os-btn").bind('click',osSelect);
	$("#os-select").bind('mousedown',function(){
		$("#os-select option[value=-1]").remove();
		checkHasSelectOS();// 检查是否选择了操作系统
	});
	$("#osType_0").bind("click", function() {
		$("#os-select").empty();
		$("#os-select_message").text("");
		OSStore.load();// 加载全部的操作系统,不区分windows和linux
	});
	$("#osType_1").bind("click", function() {
		$("#os-select").empty();
		$("#os-select_message").text("");
		windowsStore.load();// 加载windows操作系统
	});
	$("#osType_2").bind("click", function() {
		$("#os-select").empty();
		$("#os-select_message").text("");
		linuxStore.load();// 加载linux操作系统
	});
}

//CPU数据请求
var CPUStore={
		url:rootPath+'/sc/serviceItem!listServiceItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':1,sort:'[{"property":"coreNum","direction":"ASC"}]'},
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
						CPUStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			var CPUspan="";
			if(result!=null && result.length>0){
				$.each(result, function(index, value) {
					CPUspan +="<span id='"+value['id']+"' class='cpu-btn'>"+value['coreNum']+i18n.get('core')+"</span>";
				});	
				$("#cpuCore").append(CPUspan);	
				$(".cpu-btn").bind('click',cpuSelect);
				$(".cpu-btn")[0].click();
			}
		}
};

//CPU选择
function cpuSelect(){
	$(".cpu-btn").removeClass("cpu-selected-btn");
	$(this).addClass("cpu-selected-btn");
};

//memory数据请求
var memoryStore={
		url:rootPath+'/sc/serviceItem!listServiceItem.action?',
		type:'GET',
		dataType:'json',
		data:{serviceType:2,sort:'[{"property":"size","direction":"ASC"}]'},//,sort:[{property:"size",direction:"ASC"}]
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
						memoryStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#slider-mem").slider({//内存
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function( event, ui ) {
						$("#amount_mem").val(result[ui.value-1].size );
						$("#mem_itemid").val(result[ui.value-1].id);
					}
				});
				$("#amount_mem").val(result[0].size);		
				$("#mem_itemid").val(result[0].id);
			}
		}
};

//extDisk数据请求
var extDiskStore={
		url:rootPath+'/sc/serviceItem!listServiceItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':8,sort:'[{"property":"capacity","direction":"ASC"}]'},
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
						extDiskStore.fillData(result);
						extDiskArray = result;
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				var extId_amount = '#ext_'+extDisk+' .amount_extdisk';
				var extId_item = '#ext_'+extDisk+' .extdisk_itemid';
				$(".slider-extdisk").slider({
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function( event, ui ) {
						extId_amount = '#ext_'+$(this).attr("id")+" .amount_extdisk";
						extId_item = '#ext_'+$(this).attr("id")+" .extdisk_itemid";
						$(extId_amount).val(result[ui.value-1].capacity );
						$(extId_item).val(result[ui.value-1].id);
					}
				});
				$(extId_amount).val(result[0].capacity);		
				$(extId_item).val(result[0].id);				
			}
		}
};

//添加扩展盘
function addExtDisk(){
	if (extDisk < 4) {
		extDisk += 1;
		$("#main").height(($("#main").height() + extDisk * 5 + 25));
	} else {
		Dialog.alert(i18n.get("addExtDiskTip"));// 至多添加4块扩展盘
		return;
	}	
	var html =  '<tr class="extDiskTr">'+
			    '    <td class="vpdcTd1">'+i18n.get("dataDiskL")+'</td>'+
			    '    <td class="vpdcTd2">'+
			    '		<div style="float:left;width:100%;margin-left:10px;">'+
				'            <div id="'+extDisk+'" class="slider-div slider-extdisk">'+
				'                <span class="ui-slider-handle ui-state-default ui-corner-all" style="width:18px;height:24px;"></span>'+
				'            </div>'+
				'            <div id="ext_'+extDisk+'" style="float:left;">'+
				'            	<input class="extdisk_itemid" type="hidden" />'+
				'            	<input disabled="disabled" class="slider-input amount_extdisk"/>'+
				'            </div>'+
				'            <div class="cn-2" style="float:left;margin-top:5px;margin-left:5px;">GB</div>'+
				'		</div>'+			                
				'    </td>'+
				'</tr>';
	$("#extDiskTr").after(html);
	extDiskStore.fillData(extDiskArray);
};

//net数据请求
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
				$("#slider-net").slider({
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function( event, ui ) {
						$("#amount_net").val(result[ui.value-1].bandWidth );
						$("#net_itemid").val(result[ui.value-1].id);
					}
				});
				$("#amount_net").val(result[0].bandWidth);		
				$("#net_itemid").val(result[0].id);
			}
		}
};

// 添加IP
function addIP() {
	IPCount += 1;
	$("#amount_IP").val(IPCount);
};

// 减少IP
function reduceIP() {
	if (IPCount <= 1) {
		return;
	}
	IPCount = parseInt($("#amount_IP").val()) - 1;
	$("#amount_IP").val(IPCount);
};

//OS数据请求
var OSStore={
		url:rootPath+'/sc/serviceItem!listServiceItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':4},
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
						OSStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#os-select").empty();
				$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'),-1));//请选择机房
				$.each(result, function(index, value) {
					$("#os-select")[0].options.add(new Option(value['name'], value['id']));
					$("#os-select").find("option").eq(index + 1).attr("id", value['imageSize']);
				});					
			}
		}
};

//windowsStore数据请求
var windowsStore={
		url:rootPath+'/sc/serviceItem!listOSItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':4, 'family':'windows'},
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
						OSStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#os-select").empty();
				$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'),-1));//请选择机房
				$.each(result, function(index, value) {
					$("#os-select")[0].options.add(new Option(value['name'], value['id']));
					$("#os-select").find("option").eq(index + 1).attr("id", value['imageSize']);
				});	
			}
		}
};

//linuxStore数据请求
var linuxStore={
		url:rootPath+'/sc/serviceItem!listOSItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':4, 'family':'linux'},
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
						OSStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#os-select").empty();
				$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'),-1));//请选择机房
				$.each(result, function(index, value) {
					$("#os-select")[0].options.add(new Option(value['name'], value['id']));
					$("#os-select").find("option").eq(index + 1).attr("id", value['imageSize']);
				});
			}
		}
};

// 购买时长初始化
function periodInit(){
	$("#timer-select").empty();
	for(var i=0;i<periodArray.length;i++){
		$("#timer-select")[0].options.add(new Option(periodArray[i]+i18n.get('month'), periodArray[i]));
	}
};

// 点击进入第2步
function turnToStep2() {
	$("#step1").css("display", "none");
	$("#step2").css("display", "block");
	$("#step3").css("display", "none");
	$("#main").height(600);
}

// 取消创建云主机
function cancelCreateVM() {
	window.location = rootPath+"/function/vpdc/vcenter/VPDC.html?LIIndex=1";
}

// 点击返回第1步
function returnToStep1() {
	$("#step1").css("display", "block");
	$("#step2").css("display", "none");
	$("#step3").css("display", "none");
	$("#main").css("height", "auto");
}

// OS操作系统选择
function osSelect() {
	$(".os-btn").removeClass("os-selected-btn");
	$(this).addClass("os-selected-btn");
};

/**
 * 检查是否选择了操作系统
 */
function checkHasSelectOS() {
	if ((typeof $("#os-select").val() != "string")
				|| ($("#os-select").val() == "-1")) {
		verifyStyle(false, "os-select_message", i18n.get("osTypeSelect"));
		$("#os-select").css('border', '1px solid red');
		return false;
	} 
	else {
		verifyStyle(true, "os-select_message", "");
		$("#os-select").css('border', '1px solid #CCCCCC');
		return true;
	}
}

// 点击进入第3步
function turnToStep3() {
	if (!checkHasSelectOS()) {// 检查是否选择了操作系统
		return;
	}
	$("#turnToStep3").removeAttr("onclick");//先不让用户再次点击下一步，防止下面的vmAddIndex增加
	var vmConfiguration = {
			vmNum:1,// 固定为1
			ipNum:0,
			zoneGroupId:1,
			buyPeriod:0,
			serviceItemIds:new Array(),
			CPU:0,
			memory:0,
			disk:new Array(),
			bandwidth:0,
			systemDisk:20
	};
	/*******************************************************************************/
	// 获取step1界面的数据信息
	var cpuCore = $(".cpu-selected-btn").text();//CPU
	if(cpuCore != undefined){
		cpuCore=parseInt(cpuCore);
	}
	var memory = $("#amount_mem").val();//内存
	var extdiskStr = "";// 最多4块扩展盘
	if ($(".amount_extdisk").length > 0) {
		extdiskStr = "<br/>[" + i18n.get("ext_disk");
		for (var i=0; i<$(".amount_extdisk").length; i++) {
			extdiskStr += $(".amount_extdisk")[i].value + "G,";
		}
		extdiskStr = extdiskStr.substring(0, extdiskStr.length-1);
		extdiskStr += "]";
	}
	var bandwidth = $("#amount_net").val();//带宽
	// 获取step2界面的数据信息
	var osId=$("#os-select").find('option:selected').val();
	var osName = $("#os-select option[value="+osId+"]").text();//选中的操作系统的名称
	var buyPeriod = $("#timer-select").find('option:selected').val();;//选中的购买时长
	var index = $(".vmIndex").length + 1;//云主机的序号
	var diskCapacity = $("#os-select option[value="+osId+"]").attr("id");//系统盘大小
	var basicConfiguration = cpuCore+i18n.get("core")+"/"+memory+"M"+i18n.get("systemMemory")+"/"+diskCapacity+"G"+i18n.get("systemDisk")+extdiskStr;

	if (typeof($("#amount_IP").val()) != "undefined") {//非路由模式才可以选择IP数量,路由模式IP数量给0
		vmConfiguration.ipNum = parseInt($("#amount_IP").val());
	}
	vmConfiguration.zoneGroupId = selectedVpdcZoneGroupId;//数据中心或叫机房线路
	vmConfiguration.buyPeriod = buyPeriod;//购买时长
	vmConfiguration.CPU = cpuCore;
	vmConfiguration.memory = memory;
	vmConfiguration.disk = [];//最多4块扩展盘
	for (var i=0; i<$(".amount_extdisk").length; i++) {
		vmConfiguration.disk.push($(".amount_extdisk")[i].value);
	}
	vmConfiguration.bandwidth = bandwidth;	
	var itemId_cpu = $(".cpu-selected-btn").attr("id");//CPU
	serviceItemIds = new Array();
	if (itemId_cpu != undefined) {
		serviceItemIds.push(itemId_cpu);
	}
	var itemId_memory = $("#mem_itemid").val();//内存
	if (itemId_memory != undefined) {
		serviceItemIds.push(itemId_memory);
	}
	for (var i=0;i<$(".extdisk_itemid").length;i++) {//数据盘
		serviceItemIds.push($(".extdisk_itemid")[i].value);
	}
	var itemId_net = $("#net_itemid").val();//带宽
	if (itemId_net != undefined) {
		serviceItemIds.push(itemId_net);
	}	
	serviceItemIds.push(osId);//操作系统
	vmConfiguration.serviceItemIds = serviceItemIds;	
	vmConfiguration.systemDisk = diskCapacity;	
	$.ajax({
		url : billingServer+"calculatePrice",
		type : 'GET',
		dataType : 'jsonp',
		jsonp:'callback',
		jsonpCallback:'requestData',
		contentType: 'application/json; charset=utf-8',
		async : false,
		data : {
			planId: planId,//config.js中配置
			domainCode: domainCode,//config.js中配置
			CPU: vmConfiguration.CPU,
			memory: vmConfiguration.memory,
			extDisk: vmConfiguration.disk.join(","),
			bandwidth: vmConfiguration.bandwidth,
			payMonth: vmConfiguration.buyPeriod,
			demondCount: vmConfiguration.vmNum,
			IP: vmConfiguration.ipNum,
			upgrade:0,
			systemDisk:vmConfiguration.systemDisk
		},
		success : function(data) {			
			if (data == undefined) {
				Dialog.alert("vmbuss_error");
			} else {
				var vmPrice = parseFloat(data);
				// 计算出来价格再去跳转到第三步，防止出现跨域请求数据出现错误或延迟
				$("#step1").css("display", "none");
				$("#step2").css("display", "none");
				$("#step3").css("display", "block");
				$("#main").height(600);
				++vmAddIndex;// 第几次添加云主机(不是云主机的序号)
				vmArray.push([vmAddIndex, vmConfiguration]);//vmArray存放数组, 用于根据vmAddIndex查找vmConfiguration	
				
				var html =  '<tr id="vmTr'+vmAddIndex+'" class="list cn_blue_mormal">'+
							'    <td width="12%" align="right"><span class="vmIndex">'+index+'</span></td>'+
							'    <td width="30%"><span class="vmConfiguration">'+basicConfiguration+'</span></td>'+
							'    <td width="15%"><span class="vmOs">'+osName+'</span></td>'+
							'    <td width="15%"><span class="vmOs">'+buyPeriod+i18n.get("month")+'</span></td>'+
							'    <td width="15%"><span class="vmPrice">'+vmPrice.toFixed(2)+'</span><span class="yuan">'+i18n.get("yuan")+'</span></td>'+
							'    <td width="13%"><a onclick="deleteVM('+vmAddIndex+')" style="color: #FF6600;cursor: pointer;">'+i18n.get("delete")+'</a></td>'+
				            '</tr>';
				$("#vmTable").append(html);
				$("#turnToStep3").attr("onclick", "turnToStep3()");//还原下一步这个按钮的onclick事件
			}
		}
	});
}

// 删除云主机
function deleteVM(vmAddIndex) {
	$("#vmTr" +　vmAddIndex).remove();
	var indexSpans = $(".vmIndex");
	for (var i=0; i<indexSpans.length; i++) {
		indexSpans.eq(i).text(i+1);
	}
	for (var i=0; i<vmArray.length; i++) {
		if (vmArray[i][0] == vmAddIndex) {
			vmArray.splice(i, 1);// 删除当前元素
		}
	}
}

// 点击继续添加
function addOnceAgain() {
	if ($(".vmIndex").length >= 4) {
		Dialog.alert(i18n.get("addVMTip"));// 至多添加4台云主机
	} 
	else {
		$("#step1").css("display", "block");
		$("#step2").css("display", "none");
		$("#step3").css("display", "none");
		$("#main").css("height", "auto");
		$(".cpu-btn").removeClass("cpu-selected-btn");
		$(".cpu-btn").eq(0).addClass("cpu-selected-btn");
		var html1 = '<div style="float:left;width:100%;margin-left:10px;">'+
				    '    <div id="slider-mem" class="slider-div">'+
				    '        <span class="ui-slider-handle ui-state-default ui-corner-all" style="width:18px;height:24px;"></span>'+
				    '    </div>'+
				    '    <div style="float:left;">'+
				    '    	<input id="mem_itemid" type="hidden" />'+
				    '    	<input disabled="disabled" id="amount_mem" class="slider-input"/>'+
				    '    </div>'+
				    '    <div class="cn-2" style="float:left;margin-top:5px;margin-left:5px;">MB</div>'+
				    '</div>';
		$("#memoryTr").text("").html(html1);
		memoryStore.load();
		extDisk = 0;
		$(".extDiskTr").remove();
		var html2 = '<div style="float:left;margin-left:10px;">'+
				    '    <div id="slider-net" class="slider-div">'+
				    '        <span class="ui-slider-handle ui-state-default ui-corner-all" style="width:18px;height:24px;"></span>'+
				    '    </div>'+
				    '    <div style="float:left;">'+
				    '    	<input id="net_itemid" type="hidden" />'+
				    '    	<input disabled="disabled" id="amount_net" class="slider-input"/>'+
				    '    </div>'+
				    '    <div class="cn-2" style="float:left;margin-top:5px;margin-left:5px;">Mbps</div>'+
					'</div>';
		$("#bandwidthTr").text("").html(html2);
		netStore.load();
		IPCount = 1;
		$("#amount_IP").val(IPCount);
		$(".os-btn").removeClass("os-selected-btn");
		$(".os-btn").eq(0).addClass("os-selected-btn");
		$("#os-select").empty();
		OSStore.load();//默认加载全部的操作系统,不区分windows和linux
		$("#os-select_message").text("");
		periodInit();// 购买时长初始化
	}
}

// 完成并生成订单
function generateOrders() {
	for (var i=0; i<vmArray.length; i++) {//先把不同配置的云主机放在session
		$.ajax({
			url : rootPath+"/order/order!addVMItem.action",
			type : 'POST',
			dataType : 'json',
			async : false,
			traditional: true,
			data : {
				"submitData.vmNum":vmArray[i][1].vmNum,
				"submitData.ipNum":vmArray[i][1].ipNum,
				"submitData.zoneGroupId":vmArray[i][1].zoneGroupId,
				"submitData.buyPeriod":vmArray[i][1].buyPeriod,
				"submitData.serviceItemIds":vmArray[i][1].serviceItemIds,
				"submitData.planId":planId
			},
			success : function(data) {
				if(!data['success']){
					Dialog.alert(data.resultMsg);
				}
			}
		});
	}
	$.ajax({//再去后台把session中不同配置的云主机一次性提交到数据库
		url : rootPath+"/order/order!submitVMOrder.action",
		type : 'POST',
		dataType : 'json',
		async : false,
		data : "id=" + selectedVpdcId,
		success : function(data) {
			if(data['success']){
				window.location = rootPath+"/function/business/orderunpaid/index.html?LIIndex=0";				
			}
			else {
				openDialog(data.resultMsg);
			}
		}
	});
}
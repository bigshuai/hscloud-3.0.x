/**
 * demand
 */
var rootPath=rootPath;//当前服务根路径
var billingServer=billingServer;//billing服务器请求地址
var extDiskArray = new Array();
var extDiskItemIdArray = new Array();
var serviceItemIds = new Array();
var sizeOfOSMap = new Map();
var descOfOSMap = new Map();
var zoneGroupMap = new Map();
var periodArray = [1,3,6,12];
var IPCount = 1;
var extDisk = 0;
var vmNum = 1;
var osSelectByZoneGroup = 0;
var demandSubmitData={
		vmNum:1,
		ipNum:1,
		zoneGroupId:1,
		buyPeriod:1,
		serviceItemIds:new Array(),
		CPU:1,
		memory:0,
		disk:new Array(),
		bandwidth:0,
		systemDisk:20
};
$(document).ready(function() {	
	initCss('common/common.css');
//	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('shop/demand.css');	
	i18n.init();//国际化初始化
	initTagAndMenu(buyTagNameArr,buyPageUrls);
	i18nPage();//国际化当前页面	
	initBuyPage();
	initDialog();
});
function i18nPage(){
	$("#memory-title").text(i18n.get("memory-title"));// 内存：
	$("#add_extDisk-tip").text(i18n.get("Add a data disk"));//添加一块数据盘
	$("#net-title").text(i18n.get("bandwidthL"));//带宽：
	$("#zone-title").text(i18n.get("dataCenter"));//数据中心：
	$("#AssignIP-tip").text(i18n.get("Assign a new IP"));//分配新IP
	$("#os-title").text(i18n.get("OS"));//操作系统：
	$("#timer-title").text(i18n.get("buy-timer"));//购买时长：
	$("#price-label").text(i18n.get("Cost"));//费用：
	$("#price-unit-label").text(i18n.get("yuan"));//元
	$("#sc-count-label").text(i18n.get("quantity"));//购买数量：
	$(".count-unit-span").text(i18n.get("tai"));//台
	$("#btn-count").text(i18n.get("Calculate"));//计算价格
	$("#price-tip-label").text(i18n.get("Depending on your configuration, auto quotes."));//(根据您的配置，自动报价)
	$("#btn-add").text(i18n.get("Buy"));//购买
};
function initBuyPage(){
	CPUStore.load();
	memoryStore.load();
	diskStore.load();
	extDiskStore.load();
	zoneGroupStore.load();
	netStore.load();
//	OSStore.load();	
	/*var liString="";
	liString+='<li><img class="pic_sys" src="'+rootPath+'/skin/'+customerPath+'/images/shop/demand/win.png"'+'>Windows</li>';
	liString+='<li><img class="pic_sys" src="'+rootPath+'/skin/'+customerPath+'/images/shop/demand/ubuntu.png"'+'>Ubuntu</li>';
	liString+='<li><img class="pic_sys" src="'+rootPath+'/skin/'+customerPath+'/images/shop/demand/redhat.png"'+'>Redhat</li>';
	liString+='<li><img class="pic_sys" src="'+rootPath+'/skin/'+customerPath+'/images/shop/demand/opensuse.png"'+'>Opensuse</li>';
	liString+='<li><img class="pic_sys" src="'+rootPath+'/skin/'+customerPath+'/images/shop/demand/fedora.png"'+'>Windows</li>';
	liString+='<li><img class="pic_sys" src="'+rootPath+'/skin/'+customerPath+'/images/shop/demand/debian.png"'+'>Debian</li>';
	$("#system-ul").append(liString);
	$('#system-ul').bxCarousel({
		display_num: 4, 
		move: 1,
		margin: 10 
	});
	$(".bx_container").css("width","500px");*/
	//购买时长初始化
	periodInit();
	//事件绑定
//	$("#zone-select").bind('mousedown',function(){
//		$("#zone-select option[value=-1]").remove();
//		osSelectByZoneGroup=$("#zone-select").find('option:selected').val();
////		alert("osSelectByZoneGroup:"+osSelectByZoneGroup);
//		OSStore.load();
//	});
	
	$("#zone-select").change(function(){
		$("#zone-select option[value=-1]").remove();
		osSelectByZoneGroup=$("#zone-select").find('option:selected').val();
//		alert("osSelectByZoneGroup:"+osSelectByZoneGroup);
		OSStore.load();
	});
	
	$("#os-select").bind('click',function(){
		$("#os-select option[value=-1]").remove();
		$("#icon-div").empty();
		var osId=$("#os-select").find('option:selected').val();
		if(osId != undefined){
			$("#icon-div").append("<img src='"+rootPath+"/user_mgmt/webSite!getIcon.action?siId="+osId+"'/>");
			$("#systemSize-div").html(sizeOfOSMap.get(osId)+"G");
			$("#osDes-div").html(descOfOSMap.get(osId));
			$("#main").height(($("#main").height()+80));
		}else{
			Dialog.alert(i18n.get("Please select Zone-Group!"));
			$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'),-1));
			return;
		}
		
//		$("#main").css("height", "700px");
	});
	$("#add_extDisk-btn").bind('click',addExtDisk);
	$("#add_IP-btn").bind('click',addIP);
	$("#reduce_IP-btn").bind('click',reduceIP);
	$("#amount_IP").val(1);
	$("#btn-add").bind('click',demandSubmit);
	$(".cpu-btn").bind('click',cpuSelect);
	$("#btn-count").bind('click',calculatePrice);
	$("#add_count-btn").bind('click',addCount);
	$("#reduce_count-btn").bind('click',reduceCount);
	$("#sc-count-text").val(1);
	$("#sc-count-text").focus(function(){
		$("#sc-count-text").val("");
	}).keydown(function(event){
		if(event.keyCode==13){
			event.keyCode=9;
		}			
	}).keypress(function(event){
		var event = event||window.event;
		var eventCode = event.which||event.keyCode;
		if((eventCode <47) || (eventCode > 58)){
			return false ;
		}
	}).blur(function(event){	
		if($("#sc-count-text").val()=="" || $("#sc-count-text").val()==null){
			$("#sc-count-text").val(vmNum);
		}
		var value=new String($("#sc-count-text").val());
		if(value[0]==0){
			value=value.replace("0",""); 
			$("#sc-count-text").val(value);
		}
		demandSubmitData.vmNum=value;
		vmNum=value;
	});	
};
function periodInit(){
	$("#timer-select").empty();
	for(var i=0;i<periodArray.length;i++){
		$("#timer-select")[0].options.add(new Option(periodArray[i]+i18n.get('month'), periodArray[i], false, false));
	}
};
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
			//fields : [ 'id','serviceType','name','coreNum','status','model','frequency' ],		
			var CPUspan="";
			if(result!=null && result.length>0){
				$.each(result, function(index, value) {
					CPUspan +="<span id='"+value['id']+"' class='cpu-btn'>"+value['coreNum']+i18n.get('core')+"</span>";
				});	
				$("#cpu-label").after(CPUspan);	
				$(".cpu-btn").bind('click',cpuSelect);
				$(".cpu-btn")[0].click();
			}
		}
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
			//fields : [ 'id','serviceType','size','model','status' ],
			if(result!=null && result.length>0){
				//内存
				$( "#slider-mem" ).slider({
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function( event, ui ) {
//						alert(result[ui.value-1].size);
						$( "#amount_mem" ).val(result[ui.value-1].size );
						$("#mem_itemid").val(result[ui.value-1].id);
					}
				});
				
				$( "#amount_mem" ).val(result[0].size);		
				$("#mem_itemid").val(result[0].id);
			}
		}
};
//disk数据请求
var diskStore={
		url:rootPath+'/sc/serviceItem!listServiceItem.action?',
		type:'GET',
		dataType:'json',
		data:{'serviceType':3,sort:'[{"property":"capacity","direction":"ASC"}]'},
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
						diskStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			//fields : [ 'id','serviceType','capacity','model','status' ],
			if(result!=null && result.length>0){
				$( "#slider-disk" ).slider({
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function( event, ui ) {
						$( "#amount_disk" ).val(result[ui.value-1].capacity );
						$("#disk_itemid").val(result[ui.value-1].id);
					}
				});
				$( "#amount_disk" ).val(result[0].capacity);		
				$("#disk_itemid").val(result[0].id);				
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
			//fields : [ 'id','serviceType','capacity','model','status' ],
			if(result!=null && result.length>0){
				var extId_amount = '#ext_'+extDisk+' .amount_extdisk';
				var extId_item = '#ext_'+extDisk+' .extdisk_itemid';
				$(".slider-extdisk" ).slider({
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
			//fields : [ 'id','serviceType','bandWidth','type','status' ],
			if(result!=null && result.length>0){
				$( "#slider-net" ).slider({
					range: "min",
					step: 1,
					min: 1,
					max: result.length,
					slide: function( event, ui ) {
						$( "#amount_net" ).val(result[ui.value-1].bandWidth );
						$("#net_itemid").val(result[ui.value-1].id);
					}
				});
				$( "#amount_net" ).val(result[0].bandWidth);		
				$("#net_itemid").val(result[0].id);
			}
		}
};
//OS数据请求
var OSStore={
		load:function(){
			$.ajax({
				/*url : this.url,
				type: this.type,
				dataType:this.dataType,
				data:this.data,
				contentType: this.contentType,*/
				url:rootPath+'/sc/serviceItem!listServiceItemByZoneGroup.action?',
				type:'GET',
				dataType:'json',
				data:{
					'serviceType':4,
					'zoneGroupId':osSelectByZoneGroup
					},
				contentType: 'application/json; charset=utf-8',
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
			//fields : [ 'id','serviceType','name','imageId', 'imageSize','status' ],
			if(result!=null && result.length>0){
				$("#os-select").empty();
				$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'),-1));//请选择机房
				$.each(result, function(index, value) {
					$("#os-select")[0].options.add(new Option(value['name'], value['id']));
					sizeOfOSMap.put(value['id'],value['imageSize']);
					descOfOSMap.put(value['id'],value['description']);
				});					
			}
		}
};

//CPU选择
function cpuSelect(){
	$(".cpu-btn").removeClass("cpu-selected-btn");
	$(this).addClass("cpu-selected-btn");
};
//添加扩展盘
function addExtDisk(){
	if(extDisk<4){
		extDisk +=1;
		$("#main").height(($("#main").height()+extDisk*5+25));
	}else{
		Dialog.alert(i18n.get("addExtDiskTip"));//至多添加4块扩展盘
		return;
	}	
	var divStr = '<div style="float:left;width:100%;margin-top:10px;overflow:auto;">'+
					'<div class="damand-label">'+i18n.get("dataDiskL")+'</div>'+
                    '<div id="'+extDisk+'" class="slider-div slider-extdisk">'+
                    '    <span class="ui-slider-handle ui-state-default ui-corner-all " style=" width:18px; height:24px; "></span>'+
                    '</div>'+
                    '<div id="ext_'+extDisk+'" style="float:left;">'+
                    '	<input class="extdisk_itemid"  type="hidden" />'+
                    '	<input  disabled="disabled" class="slider-input amount_extdisk"/>'+
                    '</div>'+
                    '<div class="cn-2" style=" float:left; margin-top:5px; margin-left:5px;">GB</div>'+
				'</div>';
	$("#add_extDisk-btn").parent().after(divStr);
	extDiskStore.fillData(extDiskArray);
};
//添加IP
function addIP(){
	IPCount+=1;
	$("#amount_IP").val(IPCount);
};
//减少IP
function reduceIP(){
	if(IPCount<=1){
		return;
	}
	IPCount=parseInt($("#amount_IP").val())-1;
	$("#amount_IP").val(IPCount);
};
//添加数量
function addCount(){
	vmNum = parseInt($("#sc-count-text").val());
	vmNum+=1;
	if(vmNum>10){
		vmNum = 10;
	}
	$("#sc-count-text").val(vmNum);
};
//减少数量
function reduceCount(){
	if(vmNum<=1){
		return;
	}
	vmNum=parseInt($("#sc-count-text").val())-1;
	$("#sc-count-text").val(vmNum);
};
//机房线路数据请求
var zoneGroupStore={
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
						zoneGroupStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#zone-select").empty();
				$("#zone-select")[0].options.add(new Option(i18n.get('zoneGroupSelect'),-1));//请选择机房
				$("#icon-div").empty();
				$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'),-1));
				$.each(result, function(index, value) {
					$("#zone-select")[0].options.add(new Option(value['name'], value['id']));
					zoneGroupMap.put(value['id'],value['code']);
				});				
			}
		}
};
//计算价格
function calculatePrice(){
	var zoneId=$("#zone-select").find('option:selected').val();
	var osId=$("#os-select").find('option:selected').val();
//	alert("osId:"+osId);
	var period =$("#timer-select").find('option:selected').val();
	if(zoneId<=0){
		Dialog.alert(i18n.get("Please select Zone-Group!"));
		$("#zone-select").focus();
		return;
	}
	if(osId<=0){
		Dialog.alert(i18n.get("Please select OS!"));
		$("#os-select").focus();
		return;
	}
	//镜像大小
	var sizeOfOS = sizeOfOSMap.get(osId);
	if(sizeOfOS != undefined){
//		alert('sizeOfOS:'+sizeOfOS);
		demandSubmitData.systemDisk = parseInt(sizeOfOS);
	}
	//CPU
	var cpuCore = $(".cpu-selected-btn").text();
	if(cpuCore != undefined){
		demandSubmitData.CPU=parseInt(cpuCore);
	}
	//内存
	var memory = $("#amount_mem").val();
	if(memory != undefined){
		demandSubmitData.memory=memory;
	}	
	//数据盘
	demandSubmitData.disk=[];
	for(var i=0;i<$(".amount_extdisk").length;i++){
		demandSubmitData.disk.push($(".amount_extdisk")[i].value);
	}
	//带宽
	var bandwidth = $("#amount_net").val();
	if(bandwidth != undefined){
		demandSubmitData.bandwidth=bandwidth;
	}	
	//数据中心
	demandSubmitData.zoneGroupId = zoneId;
	var zoneGroupCode = zoneGroupMap.get(zoneId);
	//IP
	var ipCount = $("#amount_IP").val();
	if(ipCount != undefined){
		demandSubmitData.ipNum = ipCount;
	}else{
		demandSubmitData.ipNum = 1;
	}	
	//购买时长
	if(period != undefined){
		demandSubmitData.buyPeriod = period;
	}
	//购买数量
	demandSubmitData.vmNum = vmNum;
	
   
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
			CPU:demandSubmitData.CPU,
			memory:demandSubmitData.memory,
			extDisk:demandSubmitData.disk.join(","),
			bandwidth:demandSubmitData.bandwidth,
			payMonth:demandSubmitData.buyPeriod,
			demondCount:demandSubmitData.vmNum,
			IP:demandSubmitData.ipNum,
			upgrade:0,
			systemDisk:demandSubmitData.systemDisk,
			zoneGroupCode:zoneGroupCode,
			brandCode:brandCode           
		},        
		success : function(data) {	

			if(data == undefined){
				Dialog.alert("vmbuss_error");
			}else{	
				var totalPrice = vmNum*parseFloat(data);
				$("#price").text(totalPrice.toFixed(2));
			}
		}
	});
};
function requestData(data){
	var totalPrice = vmNum*parseFloat(data);
	$("#price").text(totalPrice);
};
//按需购买提交        
function demandSubmit(){
	var zoneId=$("#zone-select").find('option:selected').val();
	var osId=$("#os-select").find('option:selected').val();
	var period =$("#timer-select").find('option:selected').val();
	if(zoneId<=0){
		Dialog.alert(i18n.get("Please select Zone-Group!"));
		$("#zone-select").focus();
		return;
	}
	if(osId<=0){
		Dialog.alert(i18n.get("Please select OS!"));
		$("#os-select").focus();
		return;
	}
	//CPU
	var itemId_cpu = $(".cpu-selected-btn").attr("id");
	if(itemId_cpu != undefined){
		serviceItemIds.push(itemId_cpu);
	}
	//内存
	var itemId_memory = $("#mem_itemid").val();
	if(itemId_memory != undefined){
		serviceItemIds.push(itemId_memory);
	}	
	//数据盘
	for(var i=0;i<$(".extdisk_itemid").length;i++){
		extDiskItemIdArray.push($(".extdisk_itemid")[i].value);
		serviceItemIds.push($(".extdisk_itemid")[i].value);
	}
	//带宽
	var itemId_net = $("#net_itemid").val();
	if(itemId_net != undefined){
		serviceItemIds.push(itemId_net);
	}	
	//数据中心
	demandSubmitData.zoneGroupId = zoneId;
	//IP
	var ipCount = $("#amount_IP").val();
	if(ipCount != undefined){
		demandSubmitData.ipNum = ipCount;
	}else{
		demandSubmitData.ipNum = 1;
	}		
	//操作系统
	serviceItemIds.push(osId);
	//购买时长
	if(period != undefined){
		demandSubmitData.buyPeriod = period;
	}
	//购买频率间隔60s
	var submitTime = getCookie("submitTime");
	if( submitTime != null){
		var submitNow = getDateNow();
		var timeSplitFromCookie = submitTime.split('-');
		var timeSplitFromNow = submitNow.split('-');
		if(timeSplitFromNow[0] - timeSplitFromCookie[0]	 == 0){
			if(timeSplitFromNow[1] - timeSplitFromCookie[1] == 0 ){
				if(timeSplitFromNow[2]-timeSplitFromCookie[2] == 0){
					if((timeSplitFromNow[3]*60 +timeSplitFromNow[4])- (timeSplitFromCookie[3]*60+timeSplitFromCookie[4]) < orderPayInterval){
						Dialog.alert(i18n.get("sbumitTimeLessThanLimit"));
						return;
					}
				}
			}
		}
	}
	            
	demandSubmitData.serviceItemIds = serviceItemIds;
	$.ajax({
		url : rootPath+"/order/order!submitOrder4Need.action",
		type : 'POST',
		dataType : 'json',
//		contentType: 'application/x-www-form-urlencoded charset=utf-8',
		async : false,
		traditional: true,
		data : {
//			"submitData.user":new Object(),
//			"submitData.user":new Object(),
			"submitData.vmNum":demandSubmitData.vmNum,
			"submitData.ipNum":demandSubmitData.ipNum,
			"submitData.zoneGroupId":demandSubmitData.zoneGroupId,
			"submitData.buyPeriod":demandSubmitData.buyPeriod,
			"submitData.serviceItemIds":demandSubmitData.serviceItemIds,
			"submitData.planId":planId
		},
		success : function(data) {
			if(!data['success']){
				Dialog.alert(data.resultMsg);
			}else{
				window.location = rootPath+"/function/business/orderunpaid/index.html?LIIndex=0";
			}
		}
	});	
	submitTime = getDateNow();
	setCookie("submitTime",submitTime);
};
//试用提交
function trySubmit(){
	
};
function getDateNow(){
	var dateNow = new Date();
	var strTime = (dateNow.getMonth()+1)+'-'+dateNow.getDate()+'-'+dateNow.getHours()+'-'+dateNow.getMinutes()+'-'+dateNow.getSeconds();
	return strTime;
};
/**
 * package
 */
$(document).ready(function() {	
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('shop/package.css');	
	i18n.init();//国际化初始化
	initTagAndMenu(buyTagNameArr,buyPageUrls);
	i18nPage();//国际化当前页面	
	initBuyPage();
	initDialog();
	$("#agreeTerm").attr("checked",false);  
});
function i18nPage(){
	$("#product-img").attr("src",rootPath+"/skin/"+customerPath+"/images/shop/product.png");
	$("#sc-count-text").val(1);
//	$("#accordion" ).accordion({heightStyle: "fill"});
	/******------------页面国际化----------------****/
	$("#selectedVM-label").text(i18n.get("selected-VM"));//已选云主机
	$("#zoneGroup-label").text(i18n.get("dataCenter"));//数据中心：
	$("#vmtype-label").text(i18n.get("VM-Type"));//主机型号：bandwidthL
	$("#os-label").text(i18n.get("OS"));//操作系统：
	$("#bandwidth-label").text(i18n.get("bandwidthL"));//带宽：p1 
	$("#timer-label").text(i18n.get("buy-timer"));//购买时长：
//	$("#p1").text(i18n.get("one-month"));//1个月
//	$("#p3").text(i18n.get("three-month"));//3个月
//	$("#p6").text(i18n.get("six-month"));//6个月
//	$("#p12").text(i18n.get("twelve-month"));//12个月
	$("#sc-detail-label").text(i18n.get("catalog-description"));//套餐简介：
	$("#price-label").text(i18n.get("Cost"));//总价：
	$("#price-unit-label").text(i18n.get("yuan"));//元
	$("#price-tip-label").text(i18n.get("Depending on your configuration, auto quotes."));//(根据您的配置，自动报价)
	$("#zone-select")[0].options.add(new Option(i18n.get('zoneGroupSelect'), -1, false, false));//请选择机房
	$("#vmtype-select")[0].options.add(new Option(i18n.get('vmTypeSelect'), -1, false, false));//请选择云主机
	$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'), -1, false, false));//请选择操作系统
	$("#timer-select")[0].options.add(new Option(i18n.get('timerSelect'), -1, false, false));//请选择操作系统
	$("#btn-add").text(i18n.get("add"));//添加
	$("#btn-submit").text(i18n.get("submit"));//提交
	$("#btn-try").text(i18n.get("try"));//试用
	$("#shop-empty").text(i18n.get("shop-empty"));//购物车为空
	$("#vmtype-label-h3").text(i18n.get("VM-Model"));//云主机型号：
	$(".vmconfig-label").text(i18n.get("configuration"));//配置：
	$(".vmtime-label").text(i18n.get("timer"));//时长：
	$(".vmprice-label").text(i18n.get("Cost"));//费用：
	$("#sc-count-label").text(i18n.get("quantity"));//购买数量：
	$(".count-unit-span").text(i18n.get("tai"));//台
	$(".total-label").text(i18n.get("total-count"));//总计：
	$(".total-unit-span").text(i18n.get("tai"));//台
	$(".totalprice-label").text(i18n.get("total-price"));//总价：
	$(".totalprice-unit-label").text(i18n.get("yuan"));//元
	$(".agreeTerm-span").text(i18n.get("readAndAgree"));//我已阅读并同意
	$("#serviceTerms").text(i18n.get("serviceTerm"));//《HSCloud服务条款》
};
var maxCount = 10;//一次可创建虚拟机数
var period = 0;//时长
var price = 0;//价格
var feeTypeId = 0;//计费Id
var rebateRate = 0;//返点率
var usePointFlagMap = new Map();//是否可使用返点标识
var scTryFlagMap = new Map();
var scStatus = new Map();
var osArray = new Array();
var periodArray = new Array();
var vmtypeOfCPUMap = new Map();
var vmtypeOfMemoryMap = new Map();
var vmtypeOfDiskMap = new Map();
var vmtypeOfOsArray = new Array();
var vmtypeOfNetArray = new Array();
var vmtypeOfExtDisk = new Array();
//var vmtypeOfExtDisk = new Map();
var vmtypeOfTimeArray = new Array();
var vmtypeOfDescArray = new Array();
var timeArray = new Array();
var catalogInfoArray = new Array();
var catalogCountAddArray = new Array();
var rootPath=rootPath;
var vmNum = 1;
var submitTime = "";
//var extdisk = 0;
function initBuyPage(){
	zoneGroupStore.load();	
	/******-------事件绑定-----------*********/
	$("#zone-select").bind('change',zoneSelect);
	$("#vmtype-select").bind('change',vmtypeSelect);
	$("#timer-select").bind('change',periodSelected);
//	$("#progress_bar" ).progressbar({value: 5});
//	$("#btn-add").bind('click',selectVMFill);
	$("#btn-submit").bind('click',submitUnPaidOrder);
	$("#btn-try").bind('click',submitTryOrder);
	$("#serviceTerms").bind('click',readServiceTerms);
/*	$("#agreeTerm").click(function() {//勾选了同意条款
		if ($("#agreeTerm").attr("value") == "false") {
			$("#agreeTerm").attr("value", "true");
			$("#serviceTermMessage").text("");
		} 
		else {
			$("#agreeTerm").attr("value", "false");
			verifyStyle(false, "serviceTermMessage", i18n.get("agreeServiceTerm"));
		}
	});*/
	$( "#slider-range-min" ).slider({
		range: "min",
		step: 1,
		value: 1,
		min: 1,
		max: 2,
		slide: function(event, ui ) {
			$( "#amount" ).val( ui.value );
		}
	});
	$( "#amount" ).val( $( "#slider-range-min" ).slider( "value" ));
	//刷新页面时，清除购物车
	for(var i=0;i<4;i++){
		$.cookie("objItem"+i,null,{path:'/'});
	}
	$("#zone-select").bind('mousedown',function(){
		$("#zone-select option[value=-1]").remove();
		zoneSelect();
	});
	$("#vmtype-select").bind('mousedown',function(){
		$("#vmtype-select option[value=-1]").remove();
		vmtypeSelect();
	});
	$("#os-select").bind('mousedown',function(){
		$("#os-select option[value=-1]").remove();
//		osSelect();
	}).bind('mouseup',function(){
		osSelect();
	});
	$("#timer-select").bind('mousedown',function(){
		$("#timer-select option[value=-1]").remove();
		periodSelected();
	});
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
		var price=$("#price").text();
		if(value[0]==0){
			value=value.replace("0",""); 
			$("#sc-count-text").val(value);
		}
		$("#total-text").text(value);
		vmNum=value;
		price =parseInt(value)*parseFloat(price);
		$("#totalprice-text").text(price);
	});
	$("#add_count-btn").bind('click',addCount);
	$("#reduce_count-btn").bind('click',reduceCount);
};
$("#zone-select").change(zoneSelect);	
$("#vmtype-select").change(vmtypeSelect);
$("#os-select").change(osSelect);
//机房线路选择事件
function zoneSelect(){
	var zoneId=$("#zone-select").find('option:selected').val();
	if(zoneId>0){
		vmTypeStore.data={'zoneGroupId':zoneId};
		vmtypeOfExtDisk.length=0;
		vmTypeStore.load();
	}
};
//主机型号选择事件
function vmtypeSelect(){
	$("#price-point-label").text("");
	$("#price-point").text("");
	$("#price-point-unit").text("");
	$("#sc-count-text").val(1);
	$("#total-text").text(1);
	$("#totalprice-text").text("0");
	vmNum = 1;
	var scId=$("#vmtype-select").find('option:selected').val();
//	alert("scId: " + scId);
	if(scId>0){
		for(var i=0;i<vmtypeOfOsArray.length;i++){
			var vmtypeOfOs = vmtypeOfOsArray[i];			
			if(scId==vmtypeOfOs.id){
				var osItems = vmtypeOfOs.value;
				vmOsStore.fillData(osItems);
			}			
		}
		for(var j=0;j<vmtypeOfNetArray.length;j++){
			var vmtypeOfNet =vmtypeOfNetArray[j];
			if(scId==vmtypeOfNet.id){
				var netItem = vmtypeOfNet.value;
				vmNetStore.fillData(netItem);
			}
		}
		for(var k=0;k<vmtypeOfTimeArray.length;k++){
			var vmtypeOfTime = vmtypeOfTimeArray[k];
			if(scId==vmtypeOfTime.id){
				var feeTypes = vmtypeOfTime.value;
				vmTimeStore.fillData(feeTypes);
			}
		}
		for(var l=0;l<vmtypeOfDescArray.length;l++){
			var vmtypeOfDesc = vmtypeOfDescArray[l];
			if(scId==vmtypeOfDesc.id){
				var description = vmtypeOfDesc.value;
				$("#sc-description").text(description);
				$("#sc-description").attr("title",description);
			}
		}
		var usePointFlag= usePointFlagMap.get(scId);
		if(usePointFlag == true && rebateRate != null && rebateRate > 0) {
			$("#price-point-label").text(i18n.get("can-use-point"));
			$("#price-point").text(rebateRate);
			$("#price-point-unit").text("%");
		}
	}
};
//操作系统选择事件
function osSelect(){
	if($("#timer-select").find('option:selected').val()>0){
		selectVMFill();
	}
};
//机房线路数据请求
var zoneGroupStore={
		url:rootPath+'/sc/zoneGroup!getAllZoneGroupsByUser.action?',
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
				$.each(result, function(index, value) {
					$("#zone-select")[0].options.add(new Option(value['name'], value['id']));
				});				
			}
		}
};	
//套餐数据请求
var vmTypeStore={
		url:rootPath+'/sc/sc!getAllSC.action?',
		type:'GET',
		dataType:'json',
		data:'',
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
						rebateRate = data.resultObject.rebateRate;
						var result = data.resultObject.result;
						vmTypeStore.fillData(result);
					} else {
						Dialog.alert(data.resultMsg);
					}
				}
			});
		},
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#vmtype-select").empty();
				$("#vmtype-select")[0].options.add(new Option(i18n.get('vmTypeSelect'), -1, false, false));//请选择云主机
				$.each(result, function(index, value) {
					$("#vmtype-select")[0].options.add(new Option(value['name'], value['id'], false, false));
					var items=value['items'];
					var scId=value['id'];
					var feeTypes=value['feeTypes'];
					scTryFlagMap.put(scId,value['tryOrNo']);
					usePointFlagMap.put(scId,value['usePointOrNot']);
					scStatus.put(scId,value['status']);
					osArray = new Array();
					for(var i=0;i<items.length;i++){
						var item = items[i];
						if(item.serviceType == '1'){
							vmtypeOfCPUMap.put(scId,item.coreNum);
						}
						if(item.serviceType == '2'){
							vmtypeOfMemoryMap.put(scId,item.size);
						}
						if(item.serviceType == '3'){
							vmtypeOfDiskMap.put(scId,item.capacity);
						}
						if(item.serviceType == '4'){
							osArray.push(item);
						}
						if(item.serviceType == '5'){
							vmtypeOfNetArray.push({id:scId,value:item});
						}
						if(item.serviceType == '8'){
//							alert("serviceType:"+item.serviceType);
//							alert("scId:"+scId);
//							alert("value:"+item.capacity);
							vmtypeOfExtDisk.push({"id":scId,"value":item.capacity});
						}
					}
					periodArray = new Array();
					for(var j=0;j<feeTypes.length;j++){
						periodArray.push(feeTypes[j]);
					}
					vmtypeOfOsArray.push({id:scId,value:osArray});
					vmtypeOfTimeArray.push({id:scId,value:periodArray});
					vmtypeOfDescArray.push({id:scId,value:value['description']});
				});
				
			}
		}
};
//镜像数据整理
var vmOsStore={		
		fillData:function(result){
			if(result!=null && result.length>0){
				$("#os-select").empty();
				$("#os-select")[0].options.add(new Option(i18n.get('osTypeSelect'), -1, false, false));//请选择操作系统
				$.each(result, function(index, value) {
					$("#os-select")[0].options.add(new Option(value['name'], value['id'], false, false));					
				});
				
			}
		}
};
//带宽数据整理
var vmNetStore={		
		fillData:function(result){
			if(result!=null){
				var net=result.bandWidth;
				$("#slider-range-min").slider({
					range: "min",
					step: 1,
					value: 0,
					min: 1,
					max: 2,
					slide: function(event, ui) {
						$("#amount").val(ui.value);
					}
				});
				$("#slider-range-min").slider({disabled: true});
				$("#slider-range-min").slider("value", net);
				$("#amount").val(net);
			}
		}
};

//扩展盘数据整理
var vmExtDiskStore={		
		fillData:function(result){
			if(result!=null){
				var net=result.bandWidth;
				$("#slider-range-min").slider({
					range: "min",
					step: 1,
					value: 0,
					min: 1,
					max: 2,
					slide: function(event, ui) {
						$("#amount").val(ui.value);
					}
				});
				$("#slider-range-min").slider({disabled: true});
				$("#slider-range-min").slider("value", net);
				$("#amount").val(net);
			}
		}
};

//套餐时长数据整理
var vmTimeStore={		
		fillData:function(result){
			timeArray = result;
			if(result!=null && result.length>0){
				$("#timer-select").empty();
				$("#timer-select")[0].options.add(new Option(i18n.get('timerSelect'), -1, false, false));//请选择操作系统
				$.each(result, function(index, value) {
					$("#timer-select")[0].options.add(new Option(value['period']+i18n.get('month'), value['period'], false, false));					
				});
			}
		}
};
//套餐对应周期选择
$("#timer-select").change(periodSelected);
function periodSelected(){
	var id = $("#timer-select").find('option:selected').val();
	for(var i=0;i<timeArray.length;i++){
		var timer = timeArray[i].period;			
		if(id==timer){	
//			alert("id:"+id+'###timer:'+timer);	
			period = timeArray[i].period;
			price = timeArray[i].price;
			feeTypeId = timeArray[i].id;
			$("#price").text(price);	
			selectVMFill();
		}
	}	
};
//添加数量
function addCount(){
	vmNum = parseInt($("#sc-count-text").val());
	vmNum+=1;
	if(vmNum>10){
		vmNum = 10;
	}
	$("#sc-count-text").val(vmNum);
	$("#sc-count-text").blur();
};
//减少数量
function reduceCount(){
	if(vmNum<=1){
		return;
	}
	vmNum=parseInt($("#sc-count-text").val())-1;
	$("#sc-count-text").val(vmNum);
	$("#sc-count-text").blur();
};
//添加套餐到左侧列表
function selectVMFill(){
	var zoneId=$("#zone-select").find('option:selected').val();	
	var scId=$("#vmtype-select").find('option:selected').val();
	var osId=$("#os-select").find('option:selected').val();
	var period=$("#timer-select").find('option:selected').val();
//	alert('period:'+period);
	if(zoneId<=0){
		Dialog.alert(i18n.get("Please select Zone-Group!"));
		$("#zone-select").focus();
		return;
	}
	if(scId<=0){
		Dialog.alert(i18n.get("Please select VM-Type!"));
		$("#vmtype-select").focus();
		return;
	}
	if(osId<=0){
		Dialog.alert(i18n.get("Please select OS!"));
		$("#os-select").focus();
		return;
	}
	if(period==null || period==undefined || period<=0){
		Dialog.alert(i18n.get("Please select timer!"));
		$("#timer-select").focus();
		return;
	}
	catalogInfoArray = [];
	var CPU = vmtypeOfCPUMap.get(scId);
	var memory = vmtypeOfMemoryMap.get(scId);
	var disk = vmtypeOfDiskMap.get(scId);
	
	var extdisk ="";
	
	$.map(vmtypeOfExtDisk,function(extdisk1){
		if(scId == extdisk1.id){
			extdisk += extdisk1.value +"G/";
		}
	});
	if(extdisk == 0 && extdisk ==null){
		extdisk = "-- G";
	}
	if(extdisk != 0){
		extdisk = extdisk.substring(0,extdisk.length-1);
	}
	
	var vmtypeTitle = new String($("#vmtype-select").find('option:selected').text());
//	alert('feeTypeId:'+feeTypeId);
	var catalogInfo ={
		objId:Math.uuid(),
		scId:scId,
		osId:osId,
		feeTypeId:feeTypeId,
		vmtypeTitle:vmtypeTitle,
		zoneGroup:$("#zone-select").find('option:selected').text(),
		CPU:CPU,
		memory:memory,
		disk:disk,
		extdisk:extdisk,
		os:$("#os-select").find('option:selected').text(),
		netWidth:$("#amount").val(),
		netType:$("#amount").val(),
		price:price,
		period:period,
		quantity:1,
		getObjId:function(){
			return this.objId;
		},
		getScId:function(){
			return this.scId;
		},
		getOsId:function(){
			return this.osId;
		},
		getFeeTypeId:function(){
			return this.feeTypeId;
		},
		getVMConfig:function(){
			return this.zoneGroup+','
			+this.CPU+i18n.get('core')+'CPU,'//CPU
			+this.memory+'M'+i18n.get('memory')+','//memory
			+this.disk+'G'+i18n.get('disk')+','//disk
			+this.extdisk+i18n.get('extDisk')+','//extdisk
			+this.netWidth+'Mbps'+i18n.get('bandwidth')+','//bandwidth
			+this.os;
		},
		getPrice:function(){
			return new Number(this.price)*new Number(this.quantity);
		},
		getPeriod:function(){
			return this.period;
		}
	};			
	catalogInfoArray.push(catalogInfo);
	selectVMShow();
};
//已经添加的套餐列表显示
function selectVMShow(){
	var orderList ='';
	$("#orderShow").empty();
	if(catalogInfoArray != null && catalogInfoArray.length>0){		
		for(var i=0;i<catalogInfoArray.length;i++){
			var catalogInfo = catalogInfoArray[i];
			var objId = catalogInfo.objId;
			var count = 1;
			var vmtypeSTitle = catalogInfo.vmtypeTitle;
			if(vmtypeSTitle.length>15){
				vmtypeSTitle = vmtypeSTitle.substring(0,15)+"...";
			}
			$("#vmtype-span-h3").text(vmtypeSTitle);
			$("#vmtype-span-h3").attr("title",catalogInfo.vmtypeTitle);
			$(".vmconfig-text").text(catalogInfo.getVMConfig());
			$(".vmtime-text").text(catalogInfo.getPeriod()+i18n.get('month'));
			$(".vmprice-text").text(catalogInfo.getPrice()+i18n.get('yuan'));
			$("#totalprice-text").text(catalogInfo.getPrice()*vmNum);
//			alert('feeTypeId:'+catalogInfo.getFeeTypeId());
		}			
	}	
};
//试用套餐提交
function submitTryOrder(){
	var zoneId=$("#zone-select").find('option:selected').val();	
	var scId=$("#vmtype-select").find('option:selected').val();
	var osId=$("#os-select").find('option:selected').val();
	var tryFlag = scTryFlagMap.get(scId);
	if(zoneId<=0){
		Dialog.alert(i18n.get("Please select Zone-Group!"));//请选择机房！
		$("#zone-select").focus();
		return;
	}
	if(scId<=0){
		Dialog.alert(i18n.get("Please select VM-Type!"));//请选择云主机！
		$("#vmtype-select").focus();
		return;
	}
	if(osId<=0){
		Dialog.alert(i18n.get("Please select OS!"));//请选择操作系统！
		$("#os-select").focus();
		return;
	}
	if(tryFlag!=true){
		Dialog.alert(i18n.get("Can't try!"));//此云主机不能试用！
		return;
	}
	$("#fold").click();
	Dialog.confirm(i18n.get("Are you sure to choose the current configuration for creating Trial VM?"),function(){//你确定以当前选择配置创建试用云主机？
		//ajax请求到添加try VM记录		
		$.ajax({
			url:rootPath+'/order/order!submitTryVm.action?',
			type: 'POST',
			dataType: 'json',
			async : false,
			data:{"scId":scId,"osId":osId},//var jsonRequest = {"scId":scId,"osId":osId};/"scId="+scId+"&osId="+osId
			success:function(data,textStatus){
				if(data.success == true) {
					window.location = rootPath+"/function/business/vmtry/vmtry.html?LIIndex=3";
				} else {
					alert(data.resultMsg+".");
				}
			}
		});
	});	
};
//正式套餐提交
function submitUnPaidOrder(){
	if(checkSelection()==false){
		return;
	}
	if(checkServiceTerm()==false){
		return;
	}
	//购买频率间隔60s
	var submitTime = getCookie("submitTime");
	if( submitTime != null){
//		alert("orderPayInterval:"+orderPayInterval)
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
	var num = $("#total-text").text();
	var submitArray = [];
	var arrayLength = catalogInfoArray.length;
	var scid=catalogInfoArray[0].getScId();
	var statusFlag=scStatus.get(scid);
	if(statusFlag==3){
		Dialog.alert(i18n.get("Only Try!"));
		return;
	}
	if(arrayLength<=0){
		Dialog.alert(i18n.get("None catalog to submit!"));
		return;
	}
	if(num>maxCount){
		Dialog.alert(i18n.get("Can't buy more vm!"));
		return;
	}
	for(var i=0;i<num;i++){
		var catalogInfo = catalogInfoArray[0];
		var scId = catalogInfo.getScId();
		var osId = catalogInfo.getOsId();
		var feeTypeId = catalogInfo.getFeeTypeId();
		submitArray.push({"id":scId,"osId":osId,"feeTypeId":feeTypeId});
	}
	for(var k=0;k<submitArray.length;k++){
		var scId=submitArray[k].id;
		var osId=submitArray[k].osId;
		var feeTypeId=submitArray[k].feeTypeId;
		var jsonRequest = {"scId":scId,"osId":osId,"feeTypeId":feeTypeId};
		$.ajax({
			url : rootPath+"/order/order!addItem.action",
			type : 'POST',
			dataType : 'json',
			async : false,
			data : jsonRequest,
			success : function(data) {
				if(!data['success']){
					Dialog.alert(data.resultMsg);
				}
			}
		});
	}
	$.ajax({
		url : rootPath+"/order/order!submitOrder.action",
		type : 'POST',
		dataType : 'json',
		async : false,
//		contentType : "application/json; charset=utf-8",
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
	//selectedMenu("@menu_2");	
};
/**
 * 点击《HSCloud服务条款》
 */
function readServiceTerms() {
	var currentLanguage = "zh_CN";
	if (getCookie('lang') != "zh_CN") {
		currentLanguage = "en_US";
	}
	var serviceTerms = '<iframe width="690" height="400px" frameborder="0" scroll="no" src="'+rootPath+'/resources/platform/' + domainCode + '/license/license_'+domainCode+'_' + currentLanguage + '.html"></iframe>';
	openDialog(serviceTerms, i18n.get("term"), 400, 690);
}
/**
 * 检查是否点击了我已阅读并同意《HS Cloud 服务条款》这个checkbox
 */
function checkServiceTerm() {
	if ($("#agreeTerm").is(':checked')){
		$("#serviceTermMessage").text("");
		return true;
	} 
	else {      
		verifyStyle(false, "serviceTermMessage", i18n.get("agreeServiceTerm"));
		return false;
	}
};
function checkSelection(){
	var zoneId=$("#zone-select").find('option:selected').val();	
	var scId=$("#vmtype-select").find('option:selected').val();
	var osId=$("#os-select").find('option:selected').val();
	var period=$("#timer-select").find('option:selected').val();
//	alert('period:'+period);
	if(zoneId<=0){
		Dialog.alert(i18n.get("Please select Zone-Group!"));
		$("#zone-select").focus();
		return false;
	}
	if(scId<=0){
		Dialog.alert(i18n.get("Please select VM-Type!"));
		$("#vmtype-select").focus();
		return false;
	}
	if(osId<=0){
		Dialog.alert(i18n.get("Please select OS!"));
		$("#os-select").focus();
		return false;
	}
	if(period==null || period==undefined || period<=0){
		Dialog.alert(i18n.get("Please select timer!"));
		$("#timer-select").focus();
		return false;
	}
	return true;
};

/* 
 *  方法:Array.remove(dx) 
 *  功能:根据元素值删除数组元素. 
 *  参数:元素值 
 *  返回:在原数组上修改数组  
 */  
Array.prototype.indexOf = function (val) {  
    for (var i = 0; i < this.length; i++) {  
        if (this[i] == val) {  
            return i;  
        }  
    }  
    return -1;  
};  
Array.prototype.removevalue = function (val) {  
    var index = this.indexOf(val);  
    if (index > -1) {  
        this.splice(index, 1);  
    }  
};
function round2(number,fractionDigits){   
    with(Math){   
        return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
    }   
};
function getDateNow(){
	var dateNow = new Date();
	var strTime = (dateNow.getMonth()+1)+'-'+dateNow.getDate()+'-'+dateNow.getHours()+'-'+dateNow.getMinutes()+'-'+dateNow.getSeconds();
	return strTime;
};
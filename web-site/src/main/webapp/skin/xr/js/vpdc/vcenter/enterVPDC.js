/**
 * $(document).ready():初始化进入VPDC页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('vpdc/vcenter/enterVPDC.css');
	initDialog();//加载dialog的css和Js
	i18n.init();//国际化初始化
	vpdcCenterPageUrls=["../../vpdc/center/vmList.html","../../vpdc/vcenter/enterVPDC.html"];		
	initTagAndMenu(vpdcCenterNameArr, vpdcCenterPageUrls);
	selectedVpdcId = $.cookie("selectedVpdcId");//当前用户前往VPDC的id
	selectedVpdcMode = $.cookie("selectedVpdcMode");//当前用户前往VPDC的模式
	selectedVpdcZoneGroupId = $.cookie("selectedVpdcZoneGroupId");//当前用户前往VPDC的zoneGroupId
	i18nPage();//国际化当前页面
	$.getScript('../../../skin/'+customerPath+'/js/common/dialog/dialog.js', function(){
		queryHostInfo();//初始化分页,加载后台主机数据(默认图例模式)
	});
});

var selectedVpdcId;//当前用户前往VPDC的id
var selectedVpdcMode;//当前用户前往VPDC的模式
var selectedVpdcZoneGroupId;//当前用户前往VPDC的zoneGroupId
var currentShowStyle = "legend";//图例显示:legend 列表显示:list
var mouseEventTime = 0;//用于解决鼠标滑入div内时会却调用onmouseout事件
var currentPageHostsResult;//当前这一页所有主机的数据result
var selectedHostResultIndex;//当前选中的主机在result中所处的序号(特别注意：范围为0-7)
var selectedHostResult;//当前选中的主机的result,里面有选中的主机的所有信息
var selectedHostObj;//当前选中的主机这一行tr的obj
var selectedHostVm_id;//当前选中的主机的vm_id
var selectedHostName;//当前选中的主机的name
var selectedHostStatus;//当前选中的主机的status
var selectedHostTask;//当前选中的主机的task
var selectedHostIsEnable;//当前选中的主机是否禁用0:正常；1：手动禁用；2：到期禁用
var selectedHostGroupId;//当前选中的主机的内网安全组的groupId
var selectedHostSysUser;//操作系统用户名
var extranetLines = 0;//外网安全添加过的总行数(以前删除的也算在内)
var selectedHostStatusByTimer = new Map();//存贮定时更新主机的各种状态信息的map
var instanceId_array = new Array();//数据库属同一组的已添加内网安全的主机
var uuid_array = new Array();//数据库属同一组的已添加内网安全的主机
var extranet_array = new Array();//数据库中当前主机的外网安全数据
var hasClickedExtranetTab = false;//是否已经点击了外网安全Tab
var monitorType = 0;//0:实时监控；1：历史监控
var monitorObject = "cpu";
var monitorST = new Date();
var monitorET = new Date();
var catcheRealTimeJson = [];
var stepRealTime = 13;//横轴总的数据点
var stepRealTime2 = 0;//横轴动态
var dateFormatV = 'i:s';
var groupByV = 'year,month,day,hour,minute,second';
var dateStep = 10;
var dateStep2 = 60;
var timer = dateStep * 1000;
var times = 0;
var snapshotAndRestoreDialog;//快照和还原dialog,全局变量

function i18nPage() {
	$("#addHost").text(i18n.get("addHost"));
	$("#query").attr("placeHolder", i18n.get("unaddedHostPrompt"));
	$("#hostSort").text(i18n.get("hostSort"));
	$("#openingTime").text(i18n.get("openingTime"));
	$("#expirationTime").text(i18n.get("expirationTime"));
	$("#showStyle").text(i18n.get("showStyle"));
    $("#history1").text(i18n.get("history1"));
    $("#hostName").text(i18n.get("hostName"));
    $("#refundManagement_ipAddress").text(i18n.get("refundManagement_ipAddress"));
    $("#OS2").text(i18n.get("OS2"));
    $("#remaining").text(i18n.get("remaining"));
    $("#status").text(i18n.get("status"));
    $("#task").text(i18n.get("task"));
    $("#vmbuss_operate").text(i18n.get("vmbuss_operate"));
    $("#hostName2").text(i18n.get("hostName2"));
    $("#ipAddress2").text(i18n.get("ipAddress2"));
    if (getCookie("lang") == "zh_CN" || getCookie("lang") == null) {
    	$(".legendOperateDiv1").css("width", "120px");
    	$(".legendOperateDiv2").css("width", "120px");
    	$(".legendOperateDiv3").css("width", "120px");
    	$(".listOperateDiv1").css("width", "120px");
    	$(".listOperateDiv2").css("width", "120px");
    	$(".listOperateDiv3").css("width", "120px");
    }
    else {
    	$(".legendOperateDiv1").css("width", "200px");
    	$(".legendOperateDiv2").css("width", "200px");
    	$(".legendOperateDiv3").css("width", "200px");
    	$(".listOperateDiv1").css("width", "200px");
    	$(".listOperateDiv2").css("width", "200px");
    	$(".listOperateDiv3").css("width", "200px");    	
    }
	$("#refreshVM").text(i18n.get("refreshVM"));
	$("#startVM").text(i18n.get("startVM"));
	$("#stopVM").text(i18n.get("closeVM"));
	$("#restartVM").text(i18n.get("rebootVM"));
	$("#remoteMonitor").text(i18n.get("remoteMonitor"));
	$("#eventMonitor").text(i18n.get("eventMonitor"));
	$("#modifyName").text(i18n.get("modifyName"));
	$("#resetSystem").text(i18n.get("resetSystem"));
	$("#resetPassword").text(i18n.get("resetPassword"));
	$("#hostRepair").text(i18n.get("hostRepair"));
	$("#snapshotRestore").text(i18n.get("snapshotRestore"));
	$("#getDetailInfo").text(i18n.get("getDetailInfo"));
	$("#setCPPassword").text(i18n.get("setCPPassword"));
	$("#refreshVM2").text(i18n.get("refreshVM"));
	$("#startVM2").text(i18n.get("startVM"));
	$("#stopVM2").text(i18n.get("closeVM"));
	$("#restartVM2").text(i18n.get("rebootVM"));
	$("#remoteMonitor2").text(i18n.get("remoteMonitor"));
	$("#eventMonitor2").text(i18n.get("eventMonitor"));
	$("#modifyName2").text(i18n.get("modifyName"));
	$("#resetSystem2").text(i18n.get("resetSystem"));
	$("#resetPassword2").text(i18n.get("resetPassword"));
	$("#hostRepair2").text(i18n.get("hostRepair"));
	$("#snapshotRestore2").text(i18n.get("snapshotRestore"));
	$("#getDetailInfo2").text(i18n.get("getDetailInfo"));
	$("#setCPPassword2").text(i18n.get("setCPPassword"));
	$("#routerManage").text(i18n.get("routerManage"));
	if ("nonRoutingMode" == selectedVpdcMode) {//如果为路由模式才有路由管理按钮
		$("#routerManage").css("display", "none");
	}
	$(".selectTag").eq(0).attr("goto", "../../vpdc/vcenter/VPDC.html?LIIndex=1");
}

/**
 * 云主机显示方式: 1.图例显示 2.列表显示
 */
function changeShowWay(index) {
	if (1 == index) {//当前云主机图例显示 
		$(".showway1").css("background-color", "#585858");
		$(".showway1").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/common/showway1_.png)");
		$(".showway2").css("background-color", "#FFFFFF");
		$(".showway2").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/common/showway2.png)");
		$("#publicResourcesLegend").css("display", "block");
		$("#publicResourcesList").css("display", "none");
		currentShowStyle = "legend";
		queryHostInfo();
	} 
	else {//当前云主机列表显示
		$(".showway1").css("background-color", "#FFFFFF");
		$(".showway1").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/common/showway1.png)");
		$(".showway2").css("background-color", "#585858");
		$(".showway2").css("background-image", "url("+rootPath+"/skin/"+customerPath+"/images/common/showway2_.png)");
		$("#publicResourcesLegend").css("display", "none");
		$("#publicResourcesList").css("display", "block");
		currentShowStyle = "list";
		queryHostInfo();
	}
}

/**
 * 换页: 1.vpdc主界面换页(列表分页显示) 2.vpdc主界面换页(图例分页显示)
 *      3.操作日志界面换页pageChange
 */
function pageChange(pageObje) {
	if (pageObje.tableId == 'tableId') {//1.vpdc主界面换页(列表分页显示)
		// 换页时清空以前页面总数据和选中的某一台主机的数据
		currentPageHostsResult = selectedHostResultIndex = selectedHostResult = selectedHostObj = selectedHostVm_id = selectedHostName = selectedHostStatus = selectedHostTask = selectedHostIsEnable = selectedHostGroupId = selectedHostSysUser = null;		
		pageObje.jsonRequest['page'] = pageObje.current;
		$.ajax({
			url : '../../../ops/ops!getHostsByVpdcId.action',
			type : 'get',
			dataType : 'json',
			async : false,
			contentType : "application/json; charset=utf-8",
			data : pageObje.jsonRequest,
			success : function(data) {
				if (data.success) {//当模板为table时,使用pageCreatorR2创建
					pageCreatorR2(pageObje, data.resultObject);
					currentPageHostsResult = data.resultObject.result;
					//禁用的主机不显示3个按钮右边的透明div,并且3个按钮变灰色
					for (var i = 0; i < currentPageHostsResult.length; i++) {
						if (currentPageHostsResult[i].isEnable != 0) {// 0:正常；1：手动禁用；2：到期禁用
						    $("#tableId").find("img[class=buttonImage2]").eq(i*3+0).attr({
						    	"src": rootPath+"/skin/"+customerPath+"/images/vpdc/center/1_gray.png"
						    });
						    $("#tableId").find("img[class=buttonImage2]").eq(i*3+1).attr({
						    	"src": rootPath+"/skin/"+customerPath+"/images/vpdc/center/1_gray.png"
						    });
						    $("#tableId").find("img[class=buttonImage2]").eq(i*3+2).attr({
						    	"src": rootPath+"/skin/"+customerPath+"/images/vpdc/center/1_gray.png"
						    });
						    $("#tableId").find("div[class=container]").eq(i*3+0).attr("onmouseover", "");
						    $("#tableId").find("div[class=container]").eq(i*3+1).attr("onmouseover", "");
						    $("#tableId").find("div[class=container]").eq(i*3+2).attr("onmouseover", "");
 						}
					}					
				}
				else {
					openDialog(data.resultMsg);
				}
			}
		});
	}
	else if (pageObje.tableId == 'tableId2') {//2.vpdc主界面换页(图例分页显示)
		// 换页时清空以前页面总数据和选中的某一台主机的数据
		currentPageHostsResult = selectedHostResultIndex = selectedHostResult = selectedHostObj = selectedHostVm_id = selectedHostName = selectedHostStatus = selectedHostTask = selectedHostIsEnable = selectedHostGroupId = selectedHostSysUser = null;
		pageObje.jsonRequest['page'] = pageObje.current;
		$.ajax({
			url : '../../../ops/ops!getHostsByVpdcId.action',
			type : 'get',
			dataType : 'json',
			async : false,
			contentType : "application/json; charset=utf-8",
			data : pageObje.jsonRequest,
			success : function(data) {
				if (data.success) {//当模板为div时,使用pageCreatorR1创建
					pageCreatorR1(pageObje, data.resultObject);
					currentPageHostsResult = data.resultObject.result;
					for (var i = 0; i < currentPageHostsResult.length; i++) {
						//1.图例显示时,云主机的任务如果不为无，优先显示任务图片;如果任务为无，再显示状态图片
						if (currentPageHostsResult[i].task != null && currentPageHostsResult[i].task != "null"
							&& currentPageHostsResult[i].task != "" && currentPageHostsResult[i].task != undefined) {
							$(".statusImage").eq(i).attr("src", rootPath+"/skin/"+customerPath+"/images/vpdc/status/OTHER_STATUS.gif");
						}
						else {
							var hostStatus = currentPageHostsResult[i].status;
							if (hostStatus == null) {
								hostStatus = "NONE";//本来为N/A,因为图片不支持/号,故改为NONE
							}
							if (hostStatus != "ACTIVE" && hostStatus != "SUSPENDED" && hostStatus != "noInstance"
								&& hostStatus != "NONE" && hostStatus != "ERROR" && hostStatus != "SHUTOFF") {
								hostStatus = "OTHER_STATUS";//其他状态都为转圈圈
							}							
							$(".statusImage").eq(i).attr("src", rootPath+"/skin/"+customerPath+"/images/vpdc/status/"+hostStatus+".gif");
						}
						//2.禁用的主机不显示3个按钮右边的透明div,并且3个按钮变灰色
						if (currentPageHostsResult[i].isEnable != 0) {// 0:正常；1：手动禁用；2：到期禁用
						    $(".singleHost").eq(i).find("img").eq(2).attr({
						    	"onmouseover": "",
						    	"src": rootPath+"/skin/"+customerPath+"/images/vpdc/center/1_gray.png"
						    });
						    $(".singleHost").eq(i).find("img").eq(7).attr({
						    	"onmouseover": "",
						    	"src": rootPath+"/skin/"+customerPath+"/images/vpdc/center/2_gray.png"
						    });
						    $(".singleHost").eq(i).find("img").eq(10).attr({
						    	"onmouseover": "",
						    	"src": rootPath+"/skin/"+customerPath+"/images/vpdc/center/3_gray.png"
						    });
 						}
						//3.操作系统图片和被禁用的虚拟机变灰色
						var style = getOSId(currentPageHostsResult[i].osId, null)+isHostEnable2(currentPageHostsResult[i].isEnable, null);
						$(".singleHost").eq(i).find("div").eq(0).attr("style", style);
					}
				}
				else {
					openDialog(data.resultMsg);
				}
				$("#publicResourcesContent").css("display", "block");
			}
		});
	}
	else if (pageObje.tableId == 'tableId3') {//3.操作日志界面换页pageChange
		pageObje.jsonRequest['page'] = pageObje.current;
		$.ajax({
			url : '../../../ops/ops!pageVmOpsLog.action',
			type : 'get',
			dataType : 'json',
			async : false,
			contentType : "application/json; charset=utf-8",
			data : pageObje.jsonRequest,
			success : function(data) {
				if (data.success) {
					pageCreatorR2(pageObje, data.resultObject);
					var logTr = $(".logTr");//操作日志的所有行,单行和双行换色
					for (var i = 0; i < logTr.length; i++) {
						var bgcolor = (i % 2 == 0) ? "#F5F5F5" : "white";
						logTr.eq(i).attr("bgcolor", bgcolor);
					}						
				} 
				else {
					openDialog(data.resultMsg);
				}
			}
		});		
	}
}

/******************************** 1.VPDC全部主机数据分页(1.列表分页显示) ******************************/
var pageObje = new PageObje('tableId');
pageObje.size = 8;
pageObje.methodArray = [ [ 'ip', 'getIntranetIPandExtranetIP' ], [ 'spare', 'getRemainedHours' ],
                         [ 'status', 'getHostStatus' ],          [ 'isEnable', 'isHostEnable' ],
                         [ 'task', 'getHostTask' ]
                       ];
pageObje.column = [ 'name',/* 主机名称 */              'ip',/* ip地址：包含内网IP和外网IP */ 
                    'osName',/* 操作系统名称 */         'cpu_core',/* CPU核数 */ 
                    'memory_size',/* 内存(M为单位) */  'disk_capacity',/* 硬盘(G为单位) */ 
                    'runTime',/* 使用时间 */           'spare',/* 剩余时间 */ 
                    'status',/* 主机状态 */            'task',/* 当前任务 */ 
                    
                    'referenceId',/* referenceId */  'id',/* 主机的 vm_id:hc_vpdc_instance的vm_id*/
                    'applyTime',/*申请时间 */          'createTime',/* 创建时间 */
                    'expireTime',/* 到期时间 */        'flavorId',
                    'hostName',                      'osId',/* 操作系统id */ 
                    'comments',/*备注 */              'isEnable',/* 0:正常；1：手动禁用；2：到期禁用 */
                    'vmtype',/* VM类型(试用、正式)*/    'status_buss',/* VM业务状态 */
                    'createType',/* 创建人类型 */       'scId',
                    'zone',/* 主机所属域  */            'sysUser',/* 操作系统用户名 */
                    'osList',/* 操作系统集合  */         'instanceId'/* hc_vpdc_instance的id */
                   ];
/**
 * 根据1个下拉菜单和1个关键字输入框查询主机信息
 */
function queryHostInfo() {
	if (currentShowStyle == "legend") {//当前云主机图例显示 
		pageObje2.jsonRequest = {
			"sort" : $("#sort").val(),
			"query" : $.trim($("#query").val()),
			"vpdcId" : selectedVpdcId,
			"page" : pageObje2.current,
			"limit" : pageObje2.size
		};
		pageObje2.initFlag = true;
		pageObje2.pageFirst();
	}
	else {//当前云主机列表显示
		pageObje.jsonRequest = {
			"sort" : $("#sort").val(),
			"query" : $.trim($("#query").val()),
			"vpdcId" : selectedVpdcId,			
			"page" : pageObje.current,
			"limit" : pageObje.size
		};
		pageObje.initFlag = true;
		pageObje.pageFirst();		
	}
}

/**
 * 点击添加云主机按钮,跳转到添加云主机界面
 */
function addHost() {
	$.cookie("selectedVpdcId", selectedVpdcId);//添加VPDC id到cookie中
	$.cookie("selectedVpdcMode", selectedVpdcMode);//添加VPDC mode到cookie中
	$.cookie("selectedVpdcZoneGroupId", selectedVpdcZoneGroupId);//添加VPDC zoneGroupId到cookie中
	window.location = rootPath+"/function/vpdc/vcenter/addVM.html?LIIndex=1";
}

/**
 * 点击路由管理按钮,弹出路由管理dialog
 */
function routerManage() {
	$.ajax({
		url : "../../../ops/ops!getRouterByVpdcId.action",
		type : "get",
		dataType : 'json',
		async : false,
		data : "vpdcId=" + selectedVpdcId,
		success : function(json) {
			if (json.success) {
				var routerUUID = json.resultObject.routerUUID;//路由的routerUUID
				var name = json.resultObject.name;
				var intranetIp = json.resultObject.fixIP == null ? i18n.get("Intranet2")+"--":i18n.get("Intranet2")+json.resultObject.fixIP;
				var extranetIp = json.resultObject.floatingIP == null ? i18n.get("Extranet2")+"--":i18n.get("Extranet2")+json.resultObject.floatingIP;
				var osName = json.resultObject.osName;
				var basicConfiguration = "";//CPU核数
				if (json.resultObject.cpuCore == null) //CPU
					basicConfiguration = "--" + i18n.get("core") + "/";
				else
					basicConfiguration = json.resultObject.cpuCore + i18n.get("core") + "/";
				if (json.resultObject.memSize == null) //内存
					basicConfiguration += "--" + "M" + i18n.get("systemMemory") + "/";
				else
					basicConfiguration += json.resultObject.memSize + "M" + i18n.get("systemMemory") + "/";
				if (json.resultObject.diskCapacity == null) //硬盘
					basicConfiguration += "--" + "G" + i18n.get("systemDisk");
				else
					basicConfiguration += json.resultObject.diskCapacity + "G" + i18n.get("systemDisk");
				var remainingTime = getRemainedHours(json.resultObject.remainingTime, null);
				var routerStatus = json.resultObject.routerStatus == null ? i18n.get("N/A") : i18n.get(json.resultObject.routerStatus);
				
				/*var html = '<div id="routerManageDiv">'+
				           '    <table cellspacing="0" cellpadding="0" style="width: 860px;float: left;">'+
				           '        <tr valign="middle" height="36px" align="center">'+
				           '            <td width="150"><span id="routerName" class="tableHeader">'+i18n.get("routerName")+'</span></td>'+
				           '            <td width="140"><span id="routerIp" class="tableHeader">'+i18n.get("routerIp")+'</span></td>'+
				           '            <td width="100"><span id="routerOs" class="tableHeader">'+i18n.get("routerOs")+'</span></td>'+
				           '            <td width="180"><span id="routerConfiguration" class="tableHeader">'+i18n.get("routerConfiguration")+'</span></td>'+
				           '            <td width="100"><span id="routerRemainedTime" class="tableHeader">'+i18n.get("routerRemainedTime")+'</span></td>'+
				           '            <td width="90"><span id="routerStatus" class="tableHeader">'+i18n.get("routerStatus")+'</span></td>'+
				           '            <td width="100"><span id="routerOperate" class="tableHeader">'+i18n.get("routerOperate")+'</span></td>'+
				           '        </tr>'+
				           '        <tr class="list cn_blue_mormal" style="background-color: #FFFFFF;">'+
				           '            <td width="150">'+name+'</td>'+
				           '            <td width="140" align="left"><div class="ipDiv">'+intranetIp+'</div><div class="ipDiv">'+extranetIp+'</div></td>'+
				           '            <td width="100">'+osName+'</td>'+
				           '            <td width="180">'+basicConfiguration+'</td>'+
				           '            <td width="100">'+remainingTime+'</td>'+
				           '            <td width="90">'+routerStatus+'</td>'+
		                   '            <td width="100" style="color: white;">'+
	                       '                <div onmouseover="routerOperateShow(1, this)" class="container" style="width: 29px;height: 24px;float: left;">'+
	                       '                    <img class="buttonImage2" src="../../../skin/xr/images/vpdc/center/1.png">'+
	                       '                    <div class="routerTopTriangle1"></div>'+
	                       '                    <div class="routerOperateDiv1">'+
	                       '                        <table cellspacing="0" cellpadding="0" style="font-family: 微软雅黑;font-size: 14px;margin-top: 4px;">'+
	                       '                            <tr onclick="startRouter('+id+')" style="cursor: pointer;"><td class="operateImage"><img src="../../../skin/xr/images/vpdc/center/operate2.png" />&nbsp;</td><td id="startRouter" align="left">'+i18n.get("startRouter")+'</td></tr>'+
	                       '                            <tr onclick="closeRouter('+id+')" style="cursor: pointer;"><td class="operateImage"><img src="../../../skin/xr/images/vpdc/center/operate3.png" />&nbsp;</td><td id="closeRouter" align="left">'+i18n.get("closeRouter")+'</td></tr>'+
	                       '                            <tr onclick="rebootRouter('+id+')" style="cursor: pointer;"><td class="operateImage"><img src="../../../skin/xr/images/vpdc/center/operate4.png" />&nbsp;</td><td id="rebootRouter" align="left">'+i18n.get("rebootRouter")+'</td></tr>'+
	                       '                        </table>'+
	                       '                    </div>'+
	                       '                </div>'+
	                       '                <div onmouseover="routerOperateShow(2, this)" class="container" style="width: 29px;height: 24px;float: left;">'+
	                       '                    <img class="buttonImage2" src="../../../skin/xr/images/vpdc/center/2.png">'+
	                       '                </div>'+			                                               
	                       '                <div onmouseover="routerOperateShow(3, this)" class="container" style="width: 29px;height: 24px;float: left;">'+
	                       '                    <img class="buttonImage2" src="../../../skin/xr/images/vpdc/center/3.png">'+
	                       '                </div>'+		                                               
	                       '            </td>'+
				           '        </tr>'+
				           '    </table>'+
						   '</div>';*/
				
				var html =  '<div id="routerManageDiv">'+
							'    <table cellspacing="0" cellpadding="0" style="width: 860px;float: left;">'+
							'        <tr valign="middle" height="36px" align="center">'+
							'            <td width="300"><span id="routerName" class="tableHeader">'+i18n.get("routerName")+'</span></td>'+
							'            <td width="170"><span id="routerIp" class="tableHeader">'+i18n.get("routerIp")+'</span></td>'+
							'            <td width="100"><span id="routerOs" class="tableHeader">'+i18n.get("routerOs")+'</span></td>'+
							'            <td width="100"><span id="routerRemainedTime" class="tableHeader">'+i18n.get("routerRemainedTime")+'</span></td>'+
							'            <td width="90"><span id="routerStatus" class="tableHeader">'+i18n.get("routerStatus")+'</span></td>'+
							'            <td width="100"><span id="routerOperate" class="tableHeader">'+i18n.get("routerOperate")+'</span></td>'+
							'        </tr>'+
							'        <tr class="list cn_blue_mormal" style="background-color: #FFFFFF;">'+
							'            <td width="300">'+name+'</td>'+
							'            <td width="170" align="left"><div class="ipDiv">'+intranetIp+'</div><div class="ipDiv">'+extranetIp+'</div></td>'+
							'            <td width="100">'+osName+'</td>'+
							'            <td width="100">'+remainingTime+'</td>'+
							'            <td width="90">'+routerStatus+'</td>'+
							'            <td width="100" style="color: white;">'+
							'                <div onmouseover="routerOperateShow(1, this)" class="container" style="width: 29px;height: 24px;float: left;">'+
							'                    <img class="buttonImage2" src="../../../skin/xr/images/vpdc/center/1.png">'+
							'                    <div class="routerTopTriangle1"></div>'+
							'                    <div class="routerOperateDiv1">'+
							'                        <table cellspacing="0" cellpadding="0" style="font-family: 微软雅黑;font-size: 14px;margin-top: 4px;">'+
							'                            <tr onclick="startRouter(\''+routerUUID+'\')" style="cursor: pointer;"><td class="operateImage"><img src="../../../skin/xr/images/vpdc/center/operate2.png" />&nbsp;</td><td id="startRouter" align="left">'+i18n.get("startRouter")+'</td></tr>'+
							'                            <tr onclick="closeRouter(\''+routerUUID+'\')" style="cursor: pointer;"><td class="operateImage"><img src="../../../skin/xr/images/vpdc/center/operate3.png" />&nbsp;</td><td id="closeRouter" align="left">'+i18n.get("closeRouter")+'</td></tr>'+
							'                            <tr onclick="rebootRouter(\''+routerUUID+'\')" style="cursor: pointer;"><td class="operateImage"><img src="../../../skin/xr/images/vpdc/center/operate4.png" />&nbsp;</td><td id="rebootRouter" align="left">'+i18n.get("rebootRouter")+'</td></tr>'+
							'                        </table>'+
							'                    </div>'+
							'                </div>'+
							'                <div onmouseover="routerOperateShow(2, this)" class="container" style="width: 29px;height: 24px;float: left;">'+
							'                    <img class="buttonImage2" src="../../../skin/xr/images/vpdc/center/2_gray.png">'+
							'                </div>'+			                                               
							'                <div onmouseover="routerOperateShow(3, this)" class="container" style="width: 29px;height: 24px;float: left;">'+
							'                    <img class="buttonImage2" src="../../../skin/xr/images/vpdc/center/3_gray.png">'+
							'                </div>'+		                                               
							'            </td>'+
							'        </tr>'+
							'    </table>'+
							'</div>';
				openDialog(html, i18n.get("routerManage"), 200, 900);
			} 
			else {
				openDialog(json.resultMsg);
			}
		}
	});	
}

/**
 * 路由管理的3个按钮显示div
 */
function routerOperateShow(index, obj) {
	$(".routerTopTriangle1").css("display", "none");
	$(".routerOperateDiv1").css("display", "none");
	$(".routerTopTriangle2").css("display", "none");
	$(".routerOperateDiv2").css("display", "none");
	$(".routerTopTriangle3").css("display", "none");
	$(".routerOperateDiv3").css("display", "none");
	$(obj).find("div").eq(0).css("display", "block");
	$(obj).find("div").eq(1).css("display", "block");
}

/**
 * 启动路由器
 */
function startRouter(routerUUID) {
	$.ajax({
		url : "../../../ops/ops!startRouter.action",
		type : "get",
		dataType : 'json',
		async : false,
		data : "id=" + routerUUID,
		success : function(json) {
			if (json.success) {
				openDialog(i18n.get("startRouterSuccess"));
			} 
			else {
				openDialog(json.resultMsg);
			}
		}
	});		
}

/**
 * 关闭路由器
 */
function closeRouter(routerUUID) {
	 Dialog.confirm(i18n.get("closeRouterPrompt"), function() {
			$.ajax({
				url : "../../../ops/ops!closeRouter.action",
				type : "get",
				dataType : 'json',
				async : false,
				data : "id=" + routerUUID,
				success : function(json) {
					if (json.success) {
						openDialog(i18n.get("closeRouterSuccess"));
					} 
					else {
						openDialog(json.resultMsg);
					}
				}
			});	
	 });
}

/**
 * 重启路由器
 */
function rebootRouter(routerUUID) {
	 Dialog.confirm(i18n.get("rebootRouterPrompt"), function() {
			$.ajax({
				url : "../../../ops/ops!rebootRouter.action",
				type : "get",
				dataType : 'json',
				async : false,
				data : "id=" + routerUUID,
				success : function(json) {
					if (json.success) {
						openDialog(i18n.get("rebootRouterSuccess"));
					} 
					else {
						openDialog(json.resultMsg);
					}
				}
			});	
	 });	
}

//获取主机的内网IP和外网IP,在页面显示出来
function getIntranetIPandExtranetIP(str, array) {
	if (str == null)// 刷新云主机的时候也共用此方法
	　　return '<div class="ipDiv">' + i18n.get("Intranet") + "--" + '</div>' + '<div class="ipDiv">' + i18n.get("Extranet") + "--" + '</div>';
	var ipArray = str.split(",");
	if (ipArray.length == 1) {
		var intranetContent = (ipArray[0] == "") ? "--" : ipArray[0];
		return '<div class="ipDiv">' + i18n.get("Intranet") + intranetContent + '</div>' + '<div class="ipDiv">' + i18n.get("Extranet") + "--" + '</div>';
	}
	else if (ipArray.length == 2)
		return '<div class="ipDiv">' + i18n.get("Intranet") + ipArray[0] + '</div>' + '<div class="ipDiv">' + i18n.get("Extranet") + ipArray[1] + '</div>';
	else
	　　return '<div class="ipDiv">' + i18n.get("Intranet") + ipArray[0] + '</div>' + '<div class="ipDiv">' + i18n.get("Extranet") + ipArray[1] + '...</div>';
}

//获取已使用的小时数量
function getHasUsedHours(second, array) {
	var d = 0;
	var h = 0;
	var m = 0;
	var remainder = 0;
	d = parseInt(second / (60 * 60 * 24));
	remainder = second % (60 * 60 * 24);
	h = parseInt(remainder / (60 * 60));
	remainder = remainder % (60 * 60);
	m = Math.round(remainder / 60);
	return d + i18n.get("day") + h + i18n.get("hour") + m + i18n.get("minute");
}

//获取剩余的小时数量
function getRemainedHours(second, array) {
	if (second > 0)
		return getHasUsedHours(second, array);
	else
		return '\<font color=\"#FF0000\"\>' + i18n.get("expired") + '<\/font\>';
}

//获取剩余的时间
function getRemainedTime(second) {
	if (second > 0)
		return getHasUsedHours(second, null);
	else
		return '\<font color=\"#FF0000\"\>' + i18n.get("expired") + '<\/font\>';
}

//获取主机的状态
function getHostStatus(str, array) {
	if (str == null) {
		str = "N/A";
	}
	return i18n.get(str);
}

//判断主机是否禁用:0:正常；1：手动禁用；2：到期禁用
function isHostEnable(str, array) {
	if (str == 0)
		return "";
	else if (str == 1)
		return '\<font color=\"#FF0000\" style=\"font-size: 12px;\" \>' + i18n.get("manualDisabled") + '<\/font\>';
	else
		return '\<font color=\"#FF0000\" style=\"font-size: 12px;\" \>' + i18n.get("maturityDisabled") + '<\/font\>';
}

//判断主机是否禁用:0:正常；1：手动禁用；2：到期禁用(云主机详情显示主机是否禁用信息)
function isHostEnable3(str, array) {
	if (str == 0)
		return "";
	else if (str == 1)
		return '\<font color=\"#FF0000\" \>' + i18n.get("manualDisabled") + '<\/font\>';
	else
		return '\<font color=\"#FF0000\" \>' + i18n.get("maturityDisabled") + '<\/font\>';
}

//获取主机的当前任务
function getHostTask(str, array) {
	if (str == null || str == "null" || str == "" || str == undefined)
		str = "N/A";
	return i18n.get(str);
}

/******************************** 2.VPDC全部主机数据分页(2.图例分页显示) ******************************/
var pageObje2 = new PageObje('tableId2', 'templateButton2', 'totalPages2');
pageObje2.size = 8;
pageObje2.methodArray = [ [ 'osId', 'getOSId' ],                    [ 'isEnable', 'isHostEnable2' ],
                          [ 'status', 'getHostStatus' ],            [ 'task', 'getHostTask' ],
                          [ 'ip', 'getIntranetIPandExtranetIP2' ],  [ 'vmtype', 'getVmtype' ]
                        ];
pageObje2.column = [ 'name',/* 主机名称 */              'ip',/* ip地址：包含内网IP和外网IP */ 
                    'osName',/* 操作系统名称 */         'cpu_core',/* CPU核数 */ 
                    'memory_size',/* 内存(M为单位) */  'disk_capacity',/* 硬盘(G为单位) */ 
                    'runTime',/* 使用时间 */           'spare',/* 剩余时间 */ 
                    'status',/* 主机状态 */            'task',/* 当前任务 */ 
                    
                    'referenceId',/* referenceId */  'id',/* 主机的 vm_id:hc_vpdc_instance的vm_id*/
                    'applyTime',/*申请时间 */          'createTime',/* 创建时间 */
                    'expireTime',/* 到期时间 */        'flavorId',
                    'hostName',                      'osId',/* 操作系统id */ 
                    'comments',/*备注 */              'isEnable',/* 0:正常；1：手动禁用；2：到期禁用 */
                    'vmtype',/* VM类型(试用、正式)*/    'status_buss',/* VM业务状态 */
                    'createType',/* 创建人类型 */       'scId',
                    'zone',/* 主机所属域  */            'sysUser',/* 操作系统用户名 */
                    'osList',/* 操作系统集合  */         'instanceId'/* hc_vpdc_instance的id */
                   ];
//获取主机操作系统的图片
function getOSId(osId, array) {
	var osImage = "background-image: url(";
	if (osId != null) {
		return osImage += rootPath+"/skin/"+customerPath+"/images/vpdc/os/default.png);";
	}
	else {
		return osImage += rootPath+"/sc/si!getIcon.action?siId="+osId+"&time="+new Date().getTime()+");";
	}
}

//判断主机是否禁用:0:正常；1：手动禁用；2：到期禁用(禁用的主机变灰色)
function isHostEnable2(str, array) {
	if (str == 0) {
		return "";
	}
	else {
		return "filter: Alpha(opacity=10);-moz-opacity:.5;opacity:0.2;";
	}
}

//获取主机的内网IP和外网IP,在页面显示出来
function getIntranetIPandExtranetIP2(str, array) {
	if (str == null)// 刷新云主机的时候也共用此方法
	　　return "--/--";
	var ipArray = str.split(",");
	if (ipArray.length == 1) {
		var intranetContent = (ipArray[0] == "") ? "--" : ipArray[0];
		return intranetContent + "/--";
	}
	else if (ipArray.length == 2)
		return ipArray[0] + "/" + ipArray[1];
	else
	　　return ipArray[0] + "/" + ipArray[1] + "...";
}

//获取云主机的类型:试用云主机 或 正式云主机
function getVmtype(str, array) {
	return '<img src="'+rootPath+'/skin/'+customerPath+'/images/vpdc/center/'+str+'.png" />';
}

/***************************** 3.事件监控:操作日志分页begin *********************************/
var pageObje3 = new PageObje('tableId3', 'templateButton3', 'totalPages3');
pageObje3.size = 8;
pageObje3.methodArray = [ [ 'ops', 'getOperateType' ],      [ 'event_time', 'getStartTime' ],
                          [ 'update_time', 'getEndTime' ],  [ 'result', 'getOperateResult' ]
                        ];
pageObje3.column = [ 'ops',/* 操作类型 */            'event_time',/* 开始时间 */
                     'update_time',/* 结束时间 */    'result'/* 操作结果 */
                    ];
/**
 * 获取操作类型
 */
function getOperateType(str, array) {
	if (str == 1)
		return i18n.get("startHost");
	else if (str == 2)
		return i18n.get("rebootHost");
	else if (str == 3)
		return i18n.get("closeHost");
	else if (str == 6)
		return i18n.get("backupHost");
	else if (str == 7)
		return i18n.get("restoreHost");
	else if (str == 8)
		return i18n.get("resetOS");
	else if (str == 9)
		return i18n.get("resetPwd");
	else if (str == 10)
		return i18n.get("repairHost");
	else
		return i18n.get("UNKNOWN");
}

/**
 * 获取开始时间
 */
function getStartTime(str, array) {
	return (str == null) ? "" : new Date(str).format("yyyy-MM-dd hh:mm:ss");
}

/**
 * 获取结束时间
 */
function getEndTime(str, array) {
	return (str == null) ? "" : new Date(str).format("yyyy-MM-dd hh:mm:ss");
}

/**
 * 获取操作结果
 */
function getOperateResult(str, array) {
	if (str == 1)
		return i18n.get("succeed");
	else if (str == 2)
		return i18n.get("failed");
	else if (str == 3)
		return i18n.get("hostDoing");
	else if (str == 5)
		return i18n.get("repairSuccess");
	else
		return i18n.get("UNKNOWN");
}

/**
 * 根据操作日志的操作类型,开始时间,结束时间 去查询主机信息
 */
function queryOperateLog() {
	if ($("#dateFrom").val() != "" && $("#dateTo").val() != "") {
		if ($("#dateFrom").val() > $("#dateTo").val())
			$("#dateTo").val($("#dateFrom").val());
	}
	pageObje3.jsonRequest = {
		"uuid" : selectedHostVm_id,
		"ops" : $("#operateType").val(),
		"startTime" : $("#dateFrom").val(),
		"endTime" : $("#dateTo").val(),
		"page" : pageObje3.current,
		"limit" : pageObje3.size
	};
	pageObje3.initFlag = true;
	pageObje3.pageFirst();
}
/******************************* 3.事件监控:操作日志分页end *********************************/

/**
 * js 自定义的Map 构造函数内部使用
 */
function Map() {
	this.keys = new Array();
	this.data = new Array();
	// 添加键值对
	this.set = function(key, value) {
		if (this.data[key] == null) {// 如键不存在则身【键】数组添加键名
			this.keys.push(value);
		}
		this.data[key] = value;// 给键赋值
	};
	// 获取键对应的值
	this.get = function(key) {
		return this.data[key];
	};
	// 去除键值，(去除键数据中的键名及对应的值)
	this.remove = function(key) {
		this.keys.remove(key);
		this.data[key] = null;
	};
	// 判断键值元素是否为空
	this.isEmpty = function() {
		return this.keys.length == 0;
	};
	// 获取键值元素大小
	this.size = function() {
		return this.keys.length;
	};
}

/**
 * 格式化时间的方法
 */
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds() // millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

/**
 * 当用户鼠标浮在某台云主机上面,就选中了这台云主机:
 */
function selectCurrentHost(obj) {
	clearTimeout(mouseEventTime);//当触发onmouseover事件时，先清除鼠标移出事件的函数。
	// 初始化数据,清空以前选中某一台主机的数据
	selectedHostResultIndex = selectedHostResult = selectedHostObj = selectedHostVm_id = selectedHostName = selectedHostStatus = selectedHostTask = selectedHostIsEnable = selectedHostGroupId = selectedHostSysUser = null;
	/**
	 * 1.当前选中的主机在result中所处的序号(特别注意：范围为0-7)
	 * 2.思路：找出当前选中的主机的在页面显示的index,再减去1,再对8求余
	 */
	if (currentShowStyle == "legend") {//图例显示:legend
		selectedHostResultIndex = (parseInt($(obj).find("div").eq(0).attr("id")) - 1) % 8;
		// 选中某一台云主机时,divRight隐藏,divRightHidden显示
		$(obj).find("div").eq(1).css("display", "none");
		$(obj).find("div").eq(4).css("display", "block");	
	}
	else {//列表显示:list
		selectedHostResultIndex = (parseInt($(obj).find("td").eq(0).text()) - 1) % 8;
	}
	selectedHostResult = currentPageHostsResult[selectedHostResultIndex];
	selectedHostObj = obj;
	selectedHostVm_id = selectedHostResult.id;
	selectedHostName = selectedHostResult.name;
	selectedHostIsEnable = selectedHostResult.isEnable;
	selectedHostSysUser = selectedHostResult.sysUser;
	selectedHostStatus = selectedHostResult.status;
	selectedHostTask = (selectedHostResult.task == null || selectedHostResult.task == "null" || selectedHostResult.task == "" 
		             || selectedHostResult.task == undefined) ? "N/A" : selectedHostResult.task;
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
 * 当用户鼠标离开某台云主机上面,恢复这台云主机以前的布局:
 */
function restoreCurrentHost(obj) {
	if (currentShowStyle == "legend") {//图例显示:legend
		//先将mouseout要执行的函数延时执行。这段时间是为了判断鼠标是否移动到了其内部元素。
		mouseEventTime = setTimeout(function() {
			$(".leftTriangle1").css("display", "none");
			$(".legendOperateDiv1").css("display", "none");
			$(".leftTriangle2").css("display", "none");
			$(".legendOperateDiv2").css("display", "none");
			$(".leftTriangle3").css("display", "none");
			$(".legendOperateDiv3").css("display", "none");		
		}, 1);
		// 选中某一台云主机时,divRight隐藏,divRightHidden显示
		$(obj).find("div").eq(1).css("display", "block");
		$(obj).find("div").eq(4).css("display", "none");
	}
	else {//列表显示:list
		//先将mouseout要执行的函数延时执行。这段时间是为了判断鼠标是否移动到了其内部元素。
		mouseEventTime = setTimeout(function() {
			$(".topTriangle1").css("display", "none");
			$(".listOperateDiv1").css("display", "none");
			$(".topTriangle2").css("display", "none");
			$(".listOperateDiv2").css("display", "none");
			$(".topTriangle3").css("display", "none");
			$(".listOperateDiv3").css("display", "none");		
		}, 1);
	}
}

/**
 * 图例显示:当用户鼠标浮动在某台云主机的操作按钮上面
 */
function legendOperateShow(index) {
	$(".leftTriangle1").css("display", "none");
	$(".legendOperateDiv1").css("display", "none");
	$(".leftTriangle2").css("display", "none");
	$(".legendOperateDiv2").css("display", "none");
	$(".leftTriangle3").css("display", "none");
	$(".legendOperateDiv3").css("display", "none");
	$(".leftTriangle" + index).css("display", "block");
	$(".legendOperateDiv" + index).css("display", "block");
}

/**
 * 列表显示:当用户鼠标浮动在某台云主机的操作按钮上面
 */
function listOperateShow(index, obj) {
	$(".topTriangle1").css("display", "none");
	$(".listOperateDiv1").css("display", "none");
	$(".topTriangle2").css("display", "none");
	$(".listOperateDiv2").css("display", "none");
	$(".topTriangle3").css("display", "none");
	$(".listOperateDiv3").css("display", "none");
	$(obj).find("div").eq(0).css("display", "block");
	$(obj).find("div").eq(1).css("display", "block");
}

/***************************     下面是对选中某一台主机的13种操作     *****************************/
/**
 * 查看主机是否正常,是否有实例
 */
function isHostRegular() {
	if (selectedHostVm_id == "null") {
		openDialog(i18n.get("hostHasNoInstance"));
		return false;
	}
	return true;
}

/**
 * 检查主机的状况,看是否符合操作的基本要求
 */
function checkHostCondition() {
	if (selectedHostVm_id == "null") {
		openDialog(i18n.get("hostHasNoInstance"));
		return false;
	}
	if (selectedHostIsEnable != 0) {
		openDialog(i18n.get("hostHasBeenFrozen"));
		return false;
	}
	return true;
}

/**
 * 1.刷新云主机: 更新当前选中的主机的信息
 */
function refreshHost() {
	if (!isHostRegular()) {
		return;
	}
	$.ajax({
		url : "../../../ops/ops!findDetailVmById.action",
		type : "post",
		dataType : 'json',
		async : false,
		data : "id=" + selectedHostVm_id,
		success : function(json) {
			if (json.success) {
				if (currentShowStyle == "legend") {//图例显示:legend
					//操作系统图片
					var osImage = getOSId(selectedHostResult.osId, null);
					$(selectedHostObj).find("div").eq(0).css("background-image", "url("+osImage+")");
					//试用云主机还是正式云主机
					var vmtypeImage = rootPath+"/skin/"+customerPath+"/images/vpdc/center/"+selectedHostResult.vmtype+".png";
					$(selectedHostObj).find("div").eq(0).find("img").eq(0).attr("src", vmtypeImage);
					$(selectedHostObj).find("span").eq(0).text(getHostStatus(selectedHostResult.status, null));
					$(selectedHostObj).find("span").eq(1).text(getHostTask(selectedHostResult.task, null));
					$(selectedHostObj).find("span").eq(3).text(selectedHostResult.name);
					$(selectedHostObj).find("span").eq(5).text(getIntranetIPandExtranetIP2(selectedHostResult.ip, null));
					//图例显示时,云主机的任务如果不为无，优先显示任务图片;如果任务为无，再显示状态图片
					if (selectedHostResult.task != null && selectedHostResult.task != "null"
						&& selectedHostResult.task != "" && selectedHostResult.task != undefined) {
						$(selectedHostObj).find("div").eq(2).find("img").eq(0).attr("src", rootPath+"/skin/"+customerPath+"/images/vpdc/status/OTHER_STATUS.gif");
					}
					else {
						var hostStatus = selectedHostResult.status;
						if (hostStatus == null) {
							hostStatus = "NONE";//本来为N/A,因为图片不支持/号,故改为NONE
						}
						if (hostStatus != "ACTIVE" && hostStatus != "SUSPENDED" && hostStatus != "noInstance"
							&& hostStatus != "NONE" && hostStatus != "ERROR" && hostStatus != "SHUTOFF") {
							hostStatus = "OTHER_STATUS";//其他状态都为转圈圈
						}							
						$(selectedHostObj).find("div").eq(2).find("img").eq(0).attr("src", rootPath+"/skin/"+customerPath+"/images/vpdc/status/"+hostStatus+".gif");
					}
				}
				else {//列表显示:list
					$(selectedHostObj).find("td").eq(1).text(selectedHostResult.name);
					$(selectedHostObj).find("td").eq(2).html(getIntranetIPandExtranetIP(selectedHostResult.ip, null));
					$(selectedHostObj).find("td").eq(3).text(selectedHostResult.osName);
					$(selectedHostObj).find("td").eq(4).html(getRemainedHours(selectedHostResult.spare, null));
					$(selectedHostObj).find("span").eq(1).text(getHostStatus(selectedHostResult.status, null));
					$(selectedHostObj).find("span").eq(2).html(isHostEnable(selectedHostResult.isEnable, null));
					$(selectedHostObj).find("td").eq(6).text(getHostTask(selectedHostResult.task, null));
				}
			}
			else {
				openDialog(json.resultMsg);
			}
		}
	});
}

/**
 * 2.3.4.启动主机/关闭主机/重启主机: command分别为startVM，closeVM，rebootVM
 */
function operateHost(command) {
	if (!checkHostCondition()) {
		return;
	}
	// 主机已经是启动状态或正在运行任务，请稍后再试！
	if ("startVM" == command && ("ACTIVE" == selectedHostStatus || selectedHostTask != "N/A")) {
		openDialog(i18n.get("hostCannotStart"));
		return;
	} 
	else if ("closeVM" == command && ("SUSPENDED" == selectedHostStatus || selectedHostTask != "N/A")) {
		openDialog(i18n.get("hostCannotClose"));
		return;
	} 
	else if ("rebootVM" == command && "SUSPENDED" == selectedHostStatus) {
		openDialog(i18n.get("hostCannotReboot"));
		return;
	}	
	if ("startVM" == command) {//启动不需要确认,关闭和重启都需要提示确认
		operateHostAjax(command);
	} 
	else if ("closeVM" == command) {
		Dialog.confirm(i18n.get("confirmToClose"), function() {
			operateHostAjax(command);
		});
	} 
	else {
		Dialog.confirm(i18n.get("confirmToReboot"), function() {
			operateHostAjax(command);
		});		
	}
}

/**
 * 操控主机(启动主机/关闭主机/重启主机)的ajax请求
 */
function operateHostAjax(command) {
	var promptMessage = "";
	if ("startVM" == command) {
		promptMessage = i18n.get("startHostAccepted");
	}	
	else if ("closeVM" == command) {
		promptMessage = i18n.get("closeHostAccepted");
	} 
	else {
		promptMessage = i18n.get("rebootHostAccepted");
	}
	$.ajax({
		url : "../../../ops/ops!" + command + ".action",
		type : "post",
		dataType : 'json',
		async : true,
		data : "id=" + selectedHostVm_id,
		success : function(json) {
			if (json.success) {
				refreshHost();//更新当前选中的主机的信息
				// 获取当前选中的主机在新操作之后的新状态
				selectedHostStatusByTimer.set(selectedHostVm_id, setInterval("autoGetHostStatus()", 2000));
				openDialog(promptMessage);
			}
			else {
				clearInterval(selectedHostStatusByTimer.get(selectedHostVm_id));
				openDialog(json.resultMsg);
			}
		}
	});
}

/**
 * 实时获得操作主机后产生的新状态
 */
function autoGetHostStatus() {
	refreshHost();// 更新当前选中的主机的信息
	if (selectedHostTask == "N/A") {
		clearInterval(selectedHostStatusByTimer.get(selectedHostVm_id));
	}
}

/************************************  5.远程监控  ***************************************/
/**
 * 远程监控
 */
function remoteMonitoring() {
	if (!checkHostCondition()) {
		return;
	}
	var paddingLeft = (getCookie("lang") == "zh_CN" || getCookie("lang") == null) ? "50px" : "42px";
	var html = '<table id="remoteTable" cellpadding="0" cellspacing="0">'
			 + '    <tr><td id="remoteImgTd" colspan="2"><img src="'+rootPath+"/skin/"+customerPath+"/images/vpdc/center/loading.gif"+'" /></td></tr>'
			 + '    <tr><td style="text-align: left;padding-left: ' + paddingLeft + '" colspan="2">' + i18n.get("remotePrompt1") + '</td></tr>'
			 + '    <tr><td class="remoteTd1">' + i18n.get("remotePrompt2") + '</td><td class="remoteTd2"><a href="'+rootPath+'/resources/download/vncviewer/tvnviewer.exe" style="text-decoration: underline;color: blue;">VNC Viewer Client</a></td></tr>'
			 + '    <tr>'
			 + '        <td class="remoteTd1">' + i18n.get("remotePrompt3") + '</td>'
			 + '        <td class="remoteTd2">'
			 + '            <div id="validCodeDiv"><input id="validCode" type="text" maxlength="5" onfocus="getImageCode()" onkeydown="if(event.keyCode == 13)getConnectParam()"/></div>'
			 + '            <div class="imageCodeDiv"><img id="imageCode" onclick="getImageCode()" title="' + i18n.get("remotePrompt4") + '" src="../../../user_mgmt/webSite!getImageCode.action"></div>'
			 + '            <div class="imageCodeDiv"><a class="dg_btn_no" onclick="getConnectParam()">' + i18n.get("remotePrompt5") + '</a></div>'
			 + '        </td>'
			 + '    </tr>'
			 + '    <tr><td id="prompt" class="remoteTd1">&nbsp;</td><td id="connectParamMsg" class="remoteTd2"></td></tr>'
			 + '</table>';
	openDialog(html, i18n.get("remoteMonitor"), 190, 500);
	setTimeout(function() {//解决IE浏览器下面远程直接卡死bug
		$.ajax({ //请求远程监控的结果,成功或失败都在dialog里面显示
			url : "../../../ops/ops!getVNC.action",
			type : "post",
			dataType : 'json',
			async : false,
			data : "id=" + selectedHostVm_id,
			success : function(json) {
				$("#remoteImgTd").text("");//清空以前的loading.gif图片
				if (json.success) {
					$("#remoteImgTd").append('<input id="enterRemoteMonitoring" type="button" value="' + i18n.get("remotePrompt6") + '">');
					$("#enterRemoteMonitoring").click(function() {//进入远程监控
						if (navigator.userAgent.indexOf("Firefox") < 0 && navigator.userAgent.indexOf("Chrome") < 0) {
							$("#remoteImgTd").text("").append('<font color="red" style="font-weight: bold;font-size: 14px;">' + i18n.get("remotePrompt7") + '</font>');
							return;						
						}
						if (json.resultObject != null) {
							var url = json.resultObject;
							var start = url.indexOf("//");
							var end = url.indexOf("/vnc");
							var ip_port = url.substring(start + 2, end);
							var ip = ip_port.split(":")[0];
							var token = url.split("=")[1];
							var key = encode(ip);
							var width = screen.width;
							var height = screen.height;
							var vncURL = "../novnc/vnc_auto.html?token=" + token + "&key=" + key;
							window.open(vncURL, selectedHostVm_id, 'fullscreen=yes,width=' + width + ',height=' + height + ',top=0,left=0,toolbar=yes,menubar=yes,scrollbars=yes,resizable=no,location=yes,status=yes');
						}
					});
				} 
				else {
					$("#remoteImgTd").append('<font color="red" style="font-weight: bold;font-size: 14px;">' + i18n.get("remotePrompt8") + '</font>');
				}
			}
		});		
	}, 100);
}

/**
 * 验证码输入框获取焦点时，更新验证码
 */
function getImageCode() {
	var time = Math.random();
	var url = "../../../user_mgmt/webSite!getImageCode.action" + "?time=" + time;
	$("#imageCode").attr("src", url);
}

/**
 * 检查是否输入了验证码
 */
function checkValidCode() {
	$("#prompt").text(""); // 清空以前的数据
	var validCode = $("#validCode").val();
	if (validCode == null || validCode == "") {
		$("#connectParamMsg").text(i18n.get("remotePrompt9"));
		$("#connectParamMsg").css("color", "green");
		return false;
	} 
	else {
		$("#connectParamMsg").text("");
		$("#connectParamMsg").css("color", "green");
		return true;
	}
}

/**
 * 获取客户端连接参数
 */
function getConnectParam() {
	if (!checkValidCode()) {//检查是否输入了验证码
		return;
	}
	$.ajax({ //获取客户端连接参数
		url : "../../../ops/ops!getClientVNC.action",
		type : "post",
		dataType : 'json',
		async : false,
		data : "id=" + selectedHostVm_id + "&code=" + $("#validCode").val(),
		success : function(json) {
			if (json.success) {
				$("#prompt").text(i18n.get("remotePrompt10"));
				$("#prompt").css("color", "green");
				$("#connectParamMsg").text(json.resultObject.proxyIP + ':' + json.resultObject.proxyPort + i18n.get("remotePrompt11"));
				$("#connectParamMsg").css("color", "green");
			}
			else {
				$("#connectParamMsg").text(json.resultMsg);
				$("#connectParamMsg").css("color", "red");
				$("#validCode").val("");// 清空验证码
			}
		}
	});
	getImageCode();// 更新验证码
}

/**********************************  6.事件监控:begin  *************************************/
/**
 * 事件监控
 */
function eventMonitoring() {
	if (!checkHostCondition()) {
		return;
	}
	var html = '<table id="eventMonitoringTable" cellpadding="0" cellspacing="0"><tr><td>'
		     + '    <div class="m2yw2_right">'
		     + '        <div class="m2yw2_tab">'
		     + '            <ul id="tab2">'
		     + '                <li class="m2yw2_cutli" style="margin-left:-20px;" onclick="changeMonitorTab(2,1)">' + i18n.get("monitoring") + '</li>'
		     + '                <li onclick="changeMonitorTab(2,2)">' + i18n.get("operationLog") + '</li>'
		     + '            </ul>'
		     + '        </div>'
		     + '        <div id="tablist2">'
/*************************************** 详细监控 ***************************************/
		     + '            <div class="m2yw2_pic">'
		     + '                <div class="m2yw2_piclist">'
		     + '                    <table id="detailMonitoring" cellpadding="0" cellspacing="0">'
		     + '                        <tr>'
		     + '                            <td width="460">'
		     + '                                <table id="dmType" cellpadding="0" cellspacing="0">'
		     + '                                    <tr>'
		     + '                                        <td id="monitorMenu1Td1"></td>'
		     + '                                        <td id="monitorMenu1Td2"><div class="select_dm" onclick="dmselect(\'dmType\',this,\'realtime\')">' + i18n.get("realTimeMonitoring") + '</div></td>'
		     + '                                        <td id="monitorMenu1Td3">|</td>'
		     + '                                        <td id="monitorMenu1Td4"><div class="unselect_dm" onclick="dmselect(\'dmType\',this,\'history\')">' + i18n.get("historyMonitoring") + '</div></td>'
		     + '                                        <td id="historyMonitorTD"><input id="mdstart" readonly="readonly"/>' + i18n.get("to") + '<input id="mdend" readonly="readonly"/><img id="searchMonitor" src="'+rootPath+"/skin/"+customerPath+"/images/vpdc/center/searchMonitor.png"+'" onclick="queryMonitor()"/></td>'
		     + '                                        <td id="monitorMenu1Td5"></td>'
		     + '                                    </tr>'
		     + '                                </table>'
		     + '                            </td>'
		     + '                            <td style="padding-left: 20px;">'
		     + '                                <table id="dmObject" cellpadding="0" cellspacing="0">'
		     + '                                    <tr>'
		     + '                                        <td id="monitorMenu2Td1"></td>'
		     + '                                        <td id="monitorMenu2Td2"><div id="cpu" class="select_dm" onclick="dmselect(\'dmObject\',this,\'cpu\')">' + i18n.get("CPUMonitor") + '</div></td>'
		     + '                                        <td id="monitorMenu2Td3">|</td>'
		     + '                                        <td id="monitorMenu2Td4"><div id="mem" class="unselect_dm">' + i18n.get("MemoryMonitor") + '</div></td>'
		     + '                                        <td id="monitorMenu2Td5">|</td>'
		     + '                                        <td id="monitorMenu2Td6"><div id="disk" class="unselect_dm" onclick="dmselect(\'dmObject\',this,\'disk\')">' + i18n.get("DiskMonitor") + '</div></td>'
		     + '                                        <td id="monitorMenu2Td7">|</td>'
		     + '                                        <td id="monitorMenu2Td8"><div id="network" class="unselect_dm" onclick="dmselect(\'dmObject\',this,\'network\')">' + i18n.get("NetworkMonitor") + '</div></td>'
		     + '                                        <td id="monitorMenu2Td9"></td>'
		     + '                                    </tr>'
		     + '                                </table>'
		     + '                            </td>'
		     + '                        </tr>'
		     + '                    </table>'
		     + '                    <div id="container"></div>'
		     + '                </div>'
		     + '            </div>'
/*************************************** 操作日志 ***************************************/
		     + '            <div class="m2yw2_pic hidden">'
		     + '                <div class="m2yw2_piclist">'
		     + '                    <table id="logSearch">'
		     + '                        <tr>'
		     + '                            <td width="50%" style="padding-left: 36px;">' + i18n.get("operationType") + '： <select id="operateType"><option value="0">' + i18n.get("allOperations") + '</option><option value="1">' + i18n.get("startHost") + '</option><option value="2">' + i18n.get("rebootHost") + '</option><option value="3">' + i18n.get("closeHost") + '</option><option value="6">' + i18n.get("backupHost") + '</option><option value="7">' + i18n.get("restoreHost") + '</option><option value="8">' + i18n.get("resetOS") + '</option><option value="9">' + i18n.get("resetPwd") + '</option><option value="10">' + i18n.get("repairHost") + '</option></select></td>'
		     + '                            <td width="45%"><input id="dateFrom" placeholder="' + i18n.get("date_from") + '" class="box_datepicker" disabled="disabled"/><span style="line-height:33px;float:left;margin-left: -20px">' + i18n.get("to") + '</span><input id="dateTo" placeholder="' + i18n.get("date_to") + '" class="box_datepicker" disabled="disabled"/></td>'
		     + '                            <td width="5%"><div style="float: left;margin-left: -25px;"><img onclick="queryOperateLog()" src="'+rootPath+"/skin/"+customerPath+"/images/vpdc/center/search.png"+'" style="cursor:pointer;"/></div></td>'
		     + '                        </tr>'
		     + '                    </table>'
/****************************** 操作日志的需要替换的模板数据 ********************************/
		     + '                    <div id="logDiv">'
		     + '                        <table style="display:none" id="tableId3" cellspacing="0" cellpadding="0">'
		     + '                            <tr id="logHeader" class="logTr">'
		     + '                                <td class="logTd1" style="padding-right: 5px;">' + i18n.get("history1") + '</td>'
		     + '                                <td class="logTd2">' + i18n.get("operationType") + '</td>'
		     + '                                <td class="logTd3">' + i18n.get("beginTime") + '</td>'
		     + '                                <td class="logTd4">' + i18n.get("endTime") + '</td>'
		     + '                                <td class="logTd5">' + i18n.get("operateResult") + '</td>'
		     + '                                <td class="logTd6"></td>'
		     + '                            </tr>'
		     + '                            <tr id="templateId1" class="logTr">'
		     + '                                <td class="logTd1">$indexPage$</td>'
		     + '                                <td class="logTd2">$ops$</td>'
		     + '                                <td class="logTd3">$event_time$</td>'
		     + '                                <td class="logTd4">$update_time$</td>'
		     + '                                <td class="logTd5">$result$</td>'
		     + '                                <td class="logTd6"></td>'
		     + '                            </tr>'
		     + '                        </table>'
		     + '                    </div>'
		     + '                    <div id="pageDivId" class="page_div3" style="width: 790px;margin-top: 30px;">'
		     + '                        <div id="totalPages3"></div>'
		     + '                        <div class="page_last" onclick="pageObje3.pageLast()"></div>'
		     + '                        <div class="page_next" onclick="pageObje3.pageRight()"></div>'
		     + '                        <div id="templateButton3">'
		     + '                            <div class="page" onclick="pageChangeService(pageObje3, $pageNum$)">$pageNum$</div>'
		     + '                        </div>'
		     + '                        <div class="page_before" onclick="pageObje3.pageLeft()"></div>'
		     + '                        <div class="page_first" onclick="pageObje3.pageFirst()"></div>'
		     + '                    </div>'		     
		     + '                </div>'
		     + '            </div>'
		     + '        </div>'
		     + '    </div>'
		     + '</td></tr></table>';
	openDialog(html, i18n.get("eventMonitor"), 542, 810);
	setTimeout(function() {//解决IE浏览器下面直接卡死bug
		loadRealtimeMonitorInfo();// 默认获取实时cpu监控图
		times = setInterval("loadRealtimeMonitorInfo()", timer);		
	}, 100);
}

/**
 * 事件监控的tab选项卡，切换详细监控,操作日志2个界面
 */
function changeMonitorTab(m, n) {
	var menu = document.getElementById("tab" + m).getElementsByTagName("li");
	var div = document.getElementById("tablist" + m).getElementsByTagName("div");
	var showdiv = [];
	for (i = 0; j = div[i]; i++) {
		if ((" " + div[i].className + " ").indexOf(" m2yw2_pic ") != -1)
			showdiv.push(div[i]);
	}
	for (i = 0; i < menu.length; i++) {
		menu[i].className = i == (n - 1) ? "m2yw2_cutli" : "";
		showdiv[i].style.display = i == (n - 1) ? "block" : "none";
	}
	if (m == 2 && n == 1) {// 点击了事件监控的第1个tab
		loadRealtimeMonitorInfo();// 默认获取实时cpu监控图
		times = setInterval("loadRealtimeMonitorInfo()", timer);
	}
	if (m == 2 && n == 2) {// 点击了事件监控的第2个tab操作日志界面
		clearInterval(times);// 详细监控定时加载去掉
		/*********************** 操作日志Tab日期控件初始化: begin *************************/	
		if (getCookie("lang") == "zh_CN" || getCookie("lang") == null) {
			$.datepicker.regional['zh_CN'] = {
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
			   monthNames: ['一月','二月','三月','四月','五月','六月',
			   '七月','八月','九月','十月','十一月','十二月'],
			   monthNamesShort: ['一','二','三','四','五','六',
			   '七','八','九','十','十一','十二'],
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
		$("#dateFrom").datepicker({
			showOn : "button",
			buttonImage : rootPath+"/skin/"+customerPath+"/images/vpdc/center/calendar.png",
			buttonImageOnly : true
		});
		$("#dateTo").datepicker({
			showOn : "button",
			buttonImage : rootPath+"/skin/"+customerPath+"/images/vpdc/center/calendar.png",
			buttonImageOnly : true
		});
		/*********************** 操作日志Tab日期控件初始化: end *************************/
		if (navigator.userAgent.indexOf("Firefox") > 0) {//解决firefox下的下拉菜单难看问题
			$("#operateType").css({"padding-top":"3px", "padding-bottom":"3px"});
		}
		queryOperateLog();
	}
}

/**
 * 点击查询按钮获取监控信息
 */
function queryMonitor() {
	if (getCookie("lang") == "zh_CN" || getCookie("lang") == null) {
		monitorST = new Date(formatCnToEn($("#mdstart").val()));
		monitorET = new Date(formatCnToEn($("#mdend").val()));
	} 
	else {
		monitorST = new Date($("#mdstart").val());
		monitorET = new Date($("#mdend").val());
	}
	loadHistoryMonitorInfo();
}

/**
 * 默认获取实时cpu监控图
 */
function loadRealtimeMonitorInfo() {
	ajaxRealtimeMonitorInfo();
	if ("cpu" == monitorObject)
		getCpuMonitor();
	else if ("mem" == monitorObject)
		getMemMonitor();
	else if ("disk" == monitorObject)
		getDiskMonitor();
	else
		getNetworkMonitor();
}

/**
 * 按当前类别（历史），对象（cpu/mem/disk/network）获取监控信息
 */
function loadHistoryMonitorInfo() {
	if ("cpu" == monitorObject)
		getCpuMonitor();
	else if ("mem" == monitorObject)
		getMemMonitor();
	else if ("disk" == monitorObject)
		getDiskMonitor();
	else
		getNetworkMonitor();
}

/**
 * ajax请求服务端实时监控数据
 */
function ajaxRealtimeMonitorInfo(){
	var realTimeJson = null;
	dateFormatV = 'i:s';
	groupByV = 'year,month,day,hour,minute,second';
	$.ajax({
		async : false,
		type : "get",
		url : "../../../monitoring/ossVmRealTime!ossVmRealTime.action",
		data : "vmId=" + selectedHostVm_id,
		dataType : 'json',
		success : function(json) {
			if (json.success)
				realTimeJson = json.resultObject.result;
			else
				openDialog(json.resultMsg);
		}
	});
	// 对disk、net数据进行重新处理
	if (realTimeJson[0].diskMonitorVOList.length > 0) {
		realTimeJson[0].diskMonitorVOList[0].readSpeed = parseFloat((realTimeJson[0].diskMonitorVOList[0].readSpeed / (1024 * 1024)).toFixed(4));
		realTimeJson[0].diskMonitorVOList[0].writeSpeed = parseFloat((realTimeJson[0].diskMonitorVOList[0].writeSpeed / (1024 * 1024)).toFixed(4));
	}
	if (realTimeJson[0].netMonitorVOList.length > 0) {
		realTimeJson[0].netMonitorVOList[0].rxSpeed = parseFloat((realTimeJson[0].netMonitorVOList[0].rxSpeed / (1024 * 1024)).toFixed(4));
		realTimeJson[0].netMonitorVOList[0].txSpeed = parseFloat((realTimeJson[0].netMonitorVOList[0].txSpeed / (1024 * 1024)).toFixed(4));
	}
	// 将数据加载到catch中
	catcheRealTimeJson.push(realTimeJson[0]);
	catcheRealTimeJson[catcheRealTimeJson.length - 1].timestamp = new Date();
	if (catcheRealTimeJson.length > stepRealTime) {
		catcheRealTimeJson = catcheRealTimeJson.slice(catcheRealTimeJson.length - stepRealTime, catcheRealTimeJson.length);
	}
}

/**
 * ajax请求服务端历史监控数据
 */
function ajaxHistoryMonitorInfo(action, jsonData) {
	dateFormatV = 'H:i';
	groupByV = 'year,month,day,hour,minute';
	var timeStart = monitorST.getTime();
	var timeEnd = monitorET.getTime();
	$.ajax({
		async : false,
		type : "get",
		url : "../../../monitoring/ossVmHistory!" + action + ".action",
		data : "vmId=" + selectedHostVm_id + "&fromTime=" + timeStart + "&toTime=" + timeEnd,
		dataType : 'json',
		success : function(json) {
			if (json.success) {
				for (var i = 0; i < json.resultObject.result.length; i++) {
					var timestamp = json.resultObject.result[i].timestamp;
					json.resultObject.result[i].timestamp = new Date(parseInt(timestamp));
				}
				jsonData = json.resultObject.result;
			} 
			else {
				//openDialog(json.resultMsg);
			}
		}
	});
	return jsonData;
}

/**
 * 获取CPU的监控信息
 */
var panelCpu = null;
function getCpuMonitor() {
	var storeCpuJson = [];
	var stepStr = [ Ext.Date.SECOND, 11 ];
	if ('0' == monitorType) {// 查询实时数据
		for (var i = 0; i < catcheRealTimeJson.length; i++) {
			storeCpuJson.push(catcheRealTimeJson[i].cPUMonitorVO);
			storeCpuJson[i].timestamp = catcheRealTimeJson[i].timestamp;
		}
		monitorST = storeCpuJson[0].timestamp;
		if (catcheRealTimeJson.length < stepRealTime)
			monitorET = new Date(monitorST.getTime() + 120 * 1000);
		else
			monitorET = storeCpuJson[catcheRealTimeJson.length - 1].timestamp;
	} 
	else {// 查询历史数据
		storeCpuJson = ajaxHistoryMonitorInfo("ossVmCPUHistory", storeCpuJson);
		if (storeCpuJson.length > 0) {
			monitorST = storeCpuJson[0].timestamp;
			monitorET = storeCpuJson[48].timestamp;
		}
		stepStr = [ Ext.Date.HOUR, 11 ];
	}
	Ext.onReady(function() {
		var storeCpu = new Ext.data.JsonStore({
			fields : [ 'timestamp', 'cpuRate', 'cpuNum' ],
			data : storeCpuJson
		});
		var historyChartCpu = Ext.create('Ext.chart.Chart', {
			id : 'historyChartCpu',
			animate : true,
			shadow : true,
			store : storeCpu,
			loadMask : true,
			background : {
				gradient : {
					angle : 90,
					stops : {
						0 : {
							color : '#E0E0E0'
						},
						100 : {
							color : '#4A4A4A'
						}
					}
				}
			},
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				maximum : 100,
				grid : true,
				minorTickSteps : 1,// 副区间数
				position : 'left',
				fields : [ 'cpuRate' ],
				title : 'CPU(%)'
			}, {
				type : 'Time',
				position : 'bottom',
				fields : [ 'timestamp' ],
				dateFormat : dateFormatV,
				groupBy : groupByV,
				constrain : true,
				fromDate : monitorST,
				toDate : monitorET,
				step : stepStr,
				title : 'Time'
			} ],
			series : [ {
				type : 'line',
				axis : 'left',
				gutter : 80,
				highlight : true,
				smooth : true,// 平滑线条
				xField : 'timestamp',
				yField : [ 'cpuRate' ],
				tips : {
					trackMouse : true,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('timestamp').format('MM/dd/yyyy hh:mm:ss') + '(' + storeItem.get('cpuRate') + '%)');
					}
				},
				markerConfig : {
					type : 'circle',// cross是叉叉，这个是圆圈
					size : 4,
					radius : 4,
					fill : '#ee8800',
					'stroke-width' : 0
				}
			} ]
		});
		if ($("#container").html() != "" && panelCpu != null) {
			panelCpu.remove(historyChartCpu);
			panelCpu.add(historyChartCpu);
			return;
		}
		panelCpu = new Ext.Panel({
			xtype : 'linechart',
			renderTo : 'container',
			width : 800,
			height : 472,
			layout : 'fit',
			items : [ historyChartCpu ]
		});
	});
}

/**
 * 获取内存的监控信息
 */
var panelMem = null;
function getMemMonitor() {
	var storeMemJson = [];
	var stepStr = [ Ext.Date.SECOND, 11 ];
	if ('0' == monitorType) {// 查询实时数据
		for (var i = 0; i < catcheRealTimeJson.length; i++) {
			storeMemJson.push(catcheRealTimeJson[i].memoryMonitorVO);
			storeMemJson[storeMemJson.length - 1].timestamp = catcheRealTimeJson[i].timestamp;
		}
		monitorST = storeMemJson[0].timestamp;
		monitorET = new Date(monitorST.getTime() + 120 * 1000);
	} 
	else {// 查询历史数据
		storeMemJson = ajaxHistoryMonitorInfo("ossVmMemoryHistory", storeMemJson);
		if (storeMemJson.length > 0) {
			monitorST = storeMemJson[0].timestamp;
			monitorET = storeMemJson[48].timestamp;
		}
		stepStr = [ Ext.Date.HOUR, 11 ];
	}
	Ext.onReady(function() {
		var storeMEM = new Ext.data.JsonStore({
			fields : [ 'timestamp', 'ramRate', 'ramTotal' ],
			data : storeMemJson
		});
		var historyChartMEM = Ext.create('Ext.chart.Chart', {
			id : 'historyChartMEM',
			animate : true,
			shadow : true,
			store : storeMEM,
			background : {
				gradient : {
					angle : 90,
					stops : {
						0 : {
							color : '#E0E0E0'
						},
						100 : {
							color : '#4A4A4A'
						}
					}
				}
			},
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				maximum : 100,
				minorTickSteps : 1,
				position : 'left',
				fields : [ 'ramRate' ],
				title : 'Memory(%)',
				grid : true
			}, {
				type : 'Time',
				position : 'bottom',
				fields : [ 'timestamp' ],
				dateFormat : dateFormatV,
				groupBy : groupByV,
				fromDate : monitorST,
				toDate : monitorET,
				step : stepStr,
				title : 'Time'
			} ],
			series : [ {
				type : 'line',
				axis : 'left',
				gutter : 80,
				highlight : {
					size : 20,
					radius : 7
				},
				xField : 'timestamp',
				yField : [ 'ramRate' ],
				tips : {
					trackMouse : true,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('timestamp').format('MM/dd/yyyy hh:mm:ss') + '(' + storeItem.get('ramRate') + '%)');
					}
				},
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					fill : '#ee8800',
					'stroke-width' : 0
				}
			} ]
		});
		if ($("#container").html() != "" && panelMem != null) {
			panelMem.remove(historyChartMEM);
			panelMem.add(historyChartMEM);
			return;
		}
		panelMem = new Ext.Panel({
			title : '',
			renderTo : 'container',
			width : 800,
			height : 472,
			layout : 'fit',
			items : [ historyChartMEM ]
		});
	});
}

/**
 * 获取磁盘的监控信息
 */
var panelDisk = null;
function getDiskMonitor() {
	var storeDiskJson = [];
	var diskMax = 1;
	var stepStr = [ Ext.Date.SECOND, 11 ];
	if ('0' == monitorType) {// 查询实时数据
		for (var i = 0; i < catcheRealTimeJson.length; i++) {
			if (catcheRealTimeJson[i].diskMonitorVOList.length > 0) {
				storeDiskJson.push(catcheRealTimeJson[i].diskMonitorVOList[0]);
				storeDiskJson[storeDiskJson.length - 1].timestamp = catcheRealTimeJson[i].timestamp;
			}
		}
		monitorST = new Date();
		if (storeDiskJson.length > 0) {
			monitorST = storeDiskJson[0].timestamp;
		}
		monitorET = new Date(monitorST.getTime() + 120 * 1000);
	} 
	else {// 查询历史数据
		storeDiskJson = ajaxHistoryMonitorInfo("ossVmDiskHistory", storeDiskJson);
		if (storeDiskJson.length > 0) {
			var maxSpeed = (storeDiskJson[0].readSpeed / (1024 * 1024)).toFixed(4);
			for (var i = 0; i < storeDiskJson.length; i++) {
				var readSpeed = (storeDiskJson[i].readSpeed / (1024 * 1024)).toFixed(4);
				storeDiskJson[i].readSpeed = parseFloat(readSpeed);
				if (parseFloat(readSpeed) > parseFloat(maxSpeed)) {
					maxSpeed = parseFloat(readSpeed);
				}
				var writeSpeed = (storeDiskJson[i].writeSpeed / (1024 * 1024)).toFixed(4);
				storeDiskJson[i].writeSpeed = parseFloat(writeSpeed);
			}
			if (maxSpeed > 1 && maxSpeed <= 10)
				diskMax = 10;
			else if (maxSpeed > 10 && maxSpeed <= 100)
				diskMax = 100;
			monitorST = storeDiskJson[0].timestamp;
			monitorET = storeDiskJson[48].timestamp;
		}
		stepStr = [ Ext.Date.HOUR, 11 ];
	}
	Ext.onReady(function() {
		var storeDisk = new Ext.data.JsonStore({
			fields : [ 'timestamp', 'diskTotal', 'readSpeed', 'writeSpeed' ],
			data : storeDiskJson
		});
		var historyChartDisk = Ext.create('Ext.chart.Chart', {
			id : 'historyChartDisk',
			animate : true,
			shadow : true,
			store : storeDisk,
			background : {
				gradient : {
					angle : 90,
					stops : {
						0 : {
							color : '#E0E0E0'
						},
						100 : {
							color : '#4A4A4A'
						}
					}
				}
			},
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				maximum : diskMax,
				position : 'left',
				fields : [ 'readSpeed', 'writeSpeed' ],
				title : 'Disk(Mb/s)',
				grid : true
			}, {
				type : 'Time',
				position : 'bottom',
				fields : [ 'timestamp' ],
				dateFormat : dateFormatV,
				groupBy : groupByV,
				fromDate : monitorST,
				toDate : monitorET,
				step : stepStr,
				title : 'Time'
			} ],
			series : [ {
						type : 'line',
						axis : 'left',
						gutter : 80,
						highlight : {
							size : 20,
							radius : 7
						},
						xField : 'timestamp',
						yField : [ 'readSpeed' ],
						tips : {
							trackMouse : true,
							renderer : function(storeItem, item) {
								this.setTitle(storeItem.get('timestamp').format('MM/dd/yyyy hh:mm:ss') + '(' + storeItem.get('readSpeed') + 'Mb/s)');
							}
						},
						markerConfig : {
							type : 'circle',
							size : 4,
							radius : 4,
							fill : '#ee8800',
							'stroke-width' : 0
						}
					}, {
						type : 'line',
						axis : 'left',
						gutter : 80,
						highlight : {
							size : 20,
							radius : 7
						},
						xField : 'timestamp',
						yField : [ 'writeSpeed' ],
						tips : {
							trackMouse : true,
							renderer : function(storeItem, item) {
								this.setTitle(storeItem.get('timestamp').format('MM/dd/yyyy hh:mm:ss') + '(' + storeItem.get('writeSpeed') + 'Mb/s)');
							}
						}
					} ]
		});
		if ($("#container").html() != "" && panelDisk != null) {
			panelDisk.remove(historyChartDisk);
			panelDisk.add(historyChartDisk);
			return;
		}
		panelDisk = new Ext.Panel({
			title : '',
			renderTo : 'container',
			width : 800,
			height : 472,
			layout : 'fit',
			items : [ historyChartDisk ]
		});
	});
}

/**
 * 获取网络的监控信息
 */
var panelNetwork = null;
function getNetworkMonitor() {
	var storeNetworkJson = [];
	var netMax = 1;
	var stepStr = [ Ext.Date.SECOND, 11 ];
	if ('0' == monitorType) {// 查询实时数据
		for (var i = 0; i < catcheRealTimeJson.length; i++) {
			if (catcheRealTimeJson[i].netMonitorVOList.length > 0) {
				storeNetworkJson.push(catcheRealTimeJson[i].netMonitorVOList[0]);
				storeNetworkJson[storeNetworkJson.length - 1].timestamp = catcheRealTimeJson[i].timestamp;
			}
		}
		monitorST = new Date();
		if (storeNetworkJson.length > 0) {
			monitorST = storeNetworkJson[0].timestamp;
		}
		monitorET = new Date(monitorST.getTime() + 120 * 1000);
	} 
	else {// 查询历史数据
		storeNetworkJson = ajaxHistoryMonitorInfo("ossVmNetHistory", storeNetworkJson);
		if (storeNetworkJson.length > 0) {
			var maxSpeed = (storeNetworkJson[0].rxSpeed / (1024 * 1024)).toFixed(4);
			for (var i = 0; i < storeNetworkJson.length; i++) {
				var rxSpeed = (storeNetworkJson[i].rxSpeed / (1024 * 1024)).toFixed(4);
				storeNetworkJson[i].rxSpeed = parseFloat(rxSpeed);
				if (parseFloat(rxSpeed) > parseFloat(maxSpeed)) {
					maxSpeed = parseFloat(rxSpeed);
				}
				var txSpeed = (storeNetworkJson[i].txSpeed / (1024 * 1024)).toFixed(4);
				storeNetworkJson[i].txSpeed = parseFloat(txSpeed);
			}
			if (maxSpeed > 1 && maxSpeed <= 10)
				netMax = 10;
			else if (maxSpeed > 10 && maxSpeed <= 100)
				netMax = 100;
			monitorST = storeNetworkJson[0].timestamp;
			monitorET = storeNetworkJson[48].timestamp;
		}
		stepStr = [ Ext.Date.HOUR, 11 ];
	}
	Ext.onReady(function() {
		var storeNetwork = new Ext.data.JsonStore({
			fields : [ 'timestamp', 'rxSpeed', 'txSpeed' ],
			data : storeNetworkJson
		});
		var historyChartNetwork = Ext.create('Ext.chart.Chart', {
			id : 'historyChartNetwork',
			animate : true,
			shadow : true,
			store : storeNetwork,
			background : {
				gradient : {
					angle : 90,
					stops : {
						0 : {
							color : '#E0E0E0'
						},
						100 : {
							color : '#4A4A4A'
						}
					}
				}
			},
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				maximum : netMax,
				position : 'left',
				fields : [ 'rxSpeed', 'txSpeed' ],
				title : 'Network(Mbps)',
				grid : true
			}, {
				type : 'Time',
				position : 'bottom',
				fields : [ 'timestamp' ],
				dateFormat : dateFormatV,
				groupBy : groupByV,
				fromDate : monitorST,
				toDate : monitorET,
				step : stepStr,
				title : 'Time'
			} ],
			series : [ {
						type : 'line',
						axis : 'left',
						gutter : 80,
						highlight : {
							size : 20,
							radius : 7
						},
						xField : 'timestamp',
						yField : [ 'rxSpeed' ],
						tips : {
							trackMouse : true,
							renderer : function(storeItem, item) {
								this.setTitle(storeItem.get('timestamp').format('MM/dd/yyyy hh:mm:ss') + '(' + storeItem.get('rxSpeed') + 'Mbp/s)');
							}
						},
						markerConfig : {
							type : 'circle',
							size : 4,
							radius : 4,
							fill : '#ee8800',
							'stroke-width' : 0
						}
					}, {
						type : 'line',
						axis : 'left',
						gutter : 80,
						highlight : {
							size : 20,
							radius : 7
						},
						xField : 'timestamp',
						yField : [ 'txSpeed' ],
						tips : {
							trackMouse : true,
							renderer : function(storeItem, item) {
								this.setTitle(storeItem.get('timestamp').format('MM/dd/yyyy hh:mm:ss') + '(' + storeItem.get('txSpeed') + 'Mbp/s)');
							}
						}
					} ]
		});
		if ($("#container").html() != "" && panelNetwork != null) {
			panelNetwork.remove(historyChartNetwork);
			panelNetwork.add(historyChartNetwork);
			return;
		}
		panelNetwork = new Ext.Panel({
			title : '',
			renderTo : 'container',
			width : 800,
			height : 472,
			layout : 'fit',
			items : [ historyChartNetwork ]
		});
	});
}

/**
 * type: realtime是实时监控,history是历史监控
 */
function dmselect(containerId, obj, type) {
	var objs = $('#' + containerId + ' div');
	for (var i = 0; i < objs.length; i++) {
		if (obj == objs[i])
			objs[i].className = "select_dm";
		else
			objs[i].className = "unselect_dm";
	}
	if ('realtime' == type) {
		catcheRealTimeJson = [];
		monitorType = 0;
		stepRealTime = 13;
		$("#historyMonitorTD").css("display", "none");
		ajaxRealtimeMonitorInfo();
		times = setInterval("loadRealtimeMonitorInfo()", timer);
	} 
	else if ('history' == type) {
		monitorType = 1;
		stepRealTime = 49;
		$("#historyMonitorTD").css("display", "block");
		selectMonitorTime();
		timeEnd = new Date();
		monitorST = new Date(timeEnd.getTime() - 24 * 3600 * 1000);
		if (getCookie("lang") == "zh_CN" || getCookie("lang") == null) {
			$("#mdstart").val(monitorST.format('yyyy年MM月dd日 hh:mm'));
			$("#mdend").val(timeEnd.format('yyyy年MM月dd日 hh:mm'));
			$("#mdstart").css("font-size", "12px");
			$("#mdend").css("font-size", "12px");
		}
		else {
			$("#mdstart").val(monitorST.format('MM/dd/yyyy hh:mm'));
			$("#mdend").val(timeEnd.format('MM/dd/yyyy hh:mm'));				
		}
		clearInterval(times);
	}
	if ('cpu' == type)
		monitorObject = "cpu";
	else if ('mem' == type)
		monitorObject = "mem";
	else if ('disk' == type)
		monitorObject = "disk";
	else
		monitorObject = "network";
	$("#container").html("");
	if ('cpu' == monitorObject)
		getCpuMonitor();
	else if ('mem' == monitorObject)
		getMemMonitor();
	else if ('disk' == monitorObject)
		getDiskMonitor();
	else if ('network' == monitorObject)
		getNetworkMonitor();
}

function selectMonitorTime() {
	$("#mdstart").datetimepicker({
	    onClose: function(startTime, inst) {
			if (getCookie("lang") == "zh_CN" || getCookie("lang") == null)//起始时间
				startTime = formatCnToEn(startTime);
			var endTime = new Date(new Date(startTime).getTime() + 24 * 3600 * 1000);//结束时间
	        if (endTime > new Date()) {//结束时间超过当前时间,以当前时间为准
				if (getCookie("lang") == "zh_CN" || getCookie("lang") == null) {
					$("#mdstart").val(new Date(new Date().getTime() - 24 * 3600 * 1000).format('yyyy年MM月dd日 hh:mm'));
					$("#mdend").val(new Date().format('yyyy年MM月dd日 hh:mm'));
				}
				else {
					$("#mdstart").val(new Date(new Date().getTime() - 24 * 3600 * 1000).format('MM/dd/yyyy hh:mm'));
					$("#mdend").val(new Date().format('MM/dd/yyyy hh:mm'));
				}
	    		return;
	        }
			if (getCookie("lang") == "zh_CN" || getCookie("lang") == null)//结束时间
				$("#mdend").val(endTime.format('yyyy年MM月dd日 hh:mm'));
			else
				$("#mdend").val(endTime.format('MM/dd/yyyy hh:mm'));
	    }
	});
}

/**
 * 日期将中文格式转为new Date()可识别的MM/dd/yyyy hh:mm格式
 */
function formatCnToEn(dateText) {
	var year = dateText.split("年")[0];
	var month = dateText.split("年")[1].split("月")[0];
	var date = dateText.split("年")[1].split("月")[1].split("日")[0];
	var time = dateText.split("年")[1].split("月")[1].split("日")[1]
	dateText = month + "/" + date + "/" + year + time;
	return dateText;
}
/**********************************  6.事件监控:end  **************************************/

/**
 * 7.修改云主机名
 */
function modifyHostName() {
	if (!checkHostCondition()) {
		return;
	}
	var html = '<table class="dialogTable" style="margin-top: 30px;">'
		     + '    <tr><td class="dialogTd1">' + i18n.get("hostAlias") + '</td><td class="dialogTd2"><input id="newHostName" class="dialogTable_input" type="text" maxlength="40" value="' + selectedHostName + '" onblur="checkNewHostName()"/></td></tr>'
		     + '    <tr><td class="dialogTd1" style="height: 24px;">&nbsp;</td><td class="dialogTd2" style="height: 24px;"><span id="newHostName_message"></span></td></tr>'
		     + '</table>';
	var dialog = new Dialog("Dialog");
	dialog.Title = i18n.get("modifyName");
	dialog.innerHTML = html;
	dialog.Height = 80;
	dialog.Width = 500;
	dialog.OKEvent = function() {//点击确定按钮调用的方法
		if (!checkNewHostName()) {
			return;
		}
		if (selectedHostName == $.trim($("#newHostName").val())) {//主机别名没修改不需要操作
			dialog.close();//关闭修改主机名称这个dialog
			return;
		}
		$.ajax({
			url : "../../../ops/ops!updateVmName.action",
			type : "post",
			dataType : 'json',
			async : false,
			data : "id=" + selectedHostVm_id + "&name=" + $.trim($("#newHostName").val()),
			success : function(json) {
				dialog.close();//关闭修改主机名称这个dialog
				if (json.success) {
					refreshHost();// 更新当前选中的主机的信息
				}
				else {
					openDialog(json.resultMsg);
				}
			}
		});
	};
	dialog.show();
}

/**
 * 检查新的主机别名是否符合规范
 */
function checkNewHostName() {
	// 检查新的主机别名是够为空
	if ($.trim($("#newHostName").val()) == null || $.trim($("#newHostName").val()) == "") {
		verifyStyle(false, "newHostName_message", i18n.get("enterHostAlias"));
		$("#newHostName").css("border", "1px solid red");
		return false;
	}
	// 检查新的主机别名是否包含非法字符串
	if (checkHasContainedIllegalChar($.trim($("#newHostName").val()))) {
		verifyStyle(false, "newHostName_message", i18n.get("containsIllegalCharacters"));
		$("#newHostName").css("border", "1px solid red");
		return false;
	}
	$("#newHostName").css("border", "1px solid #a4a4a4");
	verifyStyle(true, "newHostName_message", "");
	return true;
}

/**
 * 8.重置操作系统
 */
function resetOperatingSystem() {
	if (!checkHostCondition()) {
		return;
	}
	if (selectedHostStatus != "ACTIVE" || selectedHostTask != "N/A") {
		openDialog(i18n.get("resetOSAlert"));
		return;
	}
	var osId = selectedHostResult.osId;// 当前选中的主机操作系统的osId
	var osName = selectedHostResult.osName;// 当前选中的主机操作系统的osName
	var osList = selectedHostResult.osList;// 当前选中的主机的所有可选择的操作系统
	var osListIds_array = new Array();// 当前选中的主机的所有可选择的操作系统id数组
	var osListNames_array = new Array();// 当前选中的主机的所有可选择的操作系统name数组
	if (osList != null) {
		for (var i = 0; i < osList.length; i++) {
			osListIds_array.push(osList[i].id);
			osListNames_array.push(osList[i].name);
		}
	} 
	else {
		osListIds_array.push(osId);
		osListNames_array.push(osName);
	}
	var sysUser = selectedHostResult.sysUser;// 当前选中的主机的操作系统用户名
	var osSelectHtml = '<select id="selectedOS" onchange="changeOSUsername()" class="dialogTable_select">';
	for (var i = 0; i < osListIds_array.length; i++) {
		var isSelected = "";
		if (osListIds_array[i] == osId) {
			isSelected = 'selected="selected"';
		}
		osSelectHtml += '<option ' + isSelected + ' value="' + osListIds_array[i] + '">' + osListNames_array[i] + '</option>';
	}
	osSelectHtml += '</select>';
	var html = '<table class="dialogTable" style="margin-top: 30px;">'
		     + '    <tr><td class="dialogTd1">' + i18n.get("operatingSystem") + '</td><td class="dialogTd2">' + osSelectHtml + '</td></tr>'
		     + '    <tr><td class="dialogTd1">' + i18n.get("userName2") + '</td><td id="sysUser" class="dialogTd2">' + sysUser + '</td></tr>'
		     + '    <tr><td class="dialogTd1">' + i18n.get("newPassword") + '</td><td class="dialogTd2"><input id="OSPassword" class="dialogTable_input" type="password" maxlength="20" onblur="verifyPassword(\'OSPassword_message\', \'OSPassword\')"/></td></tr>'
		     + '    <tr><td class="dialogTd1">' + i18n.get("confirmPassword") + '</td><td class="dialogTd2"><input id="repeatOSPassword" class="dialogTable_input" type="password" maxlength="20" onblur="verifyRepeatPassword(\'OSPassword_message\', \'OSPassword\', \'repeatOSPassword\')"/></td></tr>'
		     + '    <tr><td class="dialogTd1" style="height: 24px;">&nbsp;</td><td class="dialogTd2" style="height: 24px;"><span id="OSPassword_message"></span></td></tr>'
		     + '</table>';
	var dialog = new Dialog("Dialog");
	dialog.Title = i18n.get("resetSystem");
	dialog.innerHTML = html;
	dialog.Height = 195;
	dialog.Width = 500;
	dialog.OKEvent = function() {//点击确定按钮调用的方法
		if (!verifyRepeatPassword("OSPassword_message", "OSPassword", "repeatOSPassword")) {
			return;
		}
		Dialog.confirm(i18n.get("confirmResetOS"), function() {
			var osId = $("#selectedOS").val();
			var name = $("#sysUser").text();
			var password = $.trim($("#repeatOSPassword").val());
			dialog.close();//点击了第二层dialog的确认按钮时关闭重置操作系统这个第一层的dialog
			$.ajax({
				async : true,
				type : "post",
				url : "../../../ops/ops!resetVmOS.action",
				data : "id="+selectedHostVm_id+"&osId="+osId+"&name="+name+"&password="+password,
				dataType : 'json',
				success : function(json) {
					if (json.success) {
						refreshHost();//更新当前选中的主机的信息
					}
					else {
						openDialog(json.resultMsg);
					}
				}
			});
		});		
	};
	dialog.show();
}

/**
 * 下拉菜单选择操作系统时,改变操作系统的用户名
 */
function changeOSUsername() {
	var osId = $("#selectedOS").val();
	$.ajax({
		async : false,
		type : "post",
		url : "../../../ops/ops!getVmOSUser.action",
		data : "osId=" + osId,
		dataType : 'json',
		success : function(json) {
			if (json.success) {
				$("#sysUser").text(json.resultObject);
			}
			else {
				openDialog(json.resultMsg);
			}
		}
	});
}

/**
 * 9.重置系统密码
 */
function resetSystemPassword() {
	if (!checkHostCondition()) {
		return;
	}
	var sysUser = selectedHostResult.sysUser;//操作系统用户名
	var html = '<table class="dialogTable" style="margin-top: 20px;">'
		     + '    <tr><td class="dialogTd1">' + i18n.get("userName2") + '</td><td class="dialogTd2">' + sysUser + '</td></tr>'
		     + '    <tr><td class="dialogTd1">' + i18n.get("newPassword") + '</td><td class="dialogTd2"><input id="systemPassword" class="dialogTable_input" type="password" maxlength="20" onblur="verifyPassword(\'systemPassword_message\', \'systemPassword\')"/></td></tr>'
		     + '    <tr><td class="dialogTd1">' + i18n.get("confirmPassword") + '</td><td class="dialogTd2"><input id="repeatSystemPassword" class="dialogTable_input" type="password" maxlength="20" onblur="verifyRepeatPassword(\'systemPassword_message\', \'systemPassword\', \'repeatSystemPassword\')"/></td></tr>'
		     + '    <tr><td class="dialogTd1" style="height: 24px;">&nbsp;</td><td class="dialogTd2" style="height: 24px;"><span id="systemPassword_message"></span></td></tr>'
		     + '</table>';
	var dialog = new Dialog("Dialog");
	dialog.Title = i18n.get("resetPassword");
	dialog.innerHTML = html;
	dialog.Height = 155;
	dialog.Width = 500;
	dialog.OKEvent = function() {//点击确定按钮调用的方法
		if (!verifyRepeatPassword("systemPassword_message", "systemPassword", "repeatSystemPassword")) {
			return;
		}
		Dialog.confirm(i18n.get("confirmResetPassword"), function() {
			var systemPassword = $.trim($("#systemPassword").val());
			dialog.close();//点击了第二层dialog的确认按钮时关闭重置系统密码这个第一层的dialog
			$.ajax({
				url : "../../../ops/ops!resetSystemPwd.action",
				type : "post",
				dataType : 'json',
				async : true,
				data : "id="+selectedHostVm_id+"&password="+systemPassword,
				success : function(json) {
					if (json.success) {
						openDialog(i18n.get("acceptResetPassword"));
					}
					else {
						openDialog(json.resultMsg);
					}
				}
			});
		});
	};
	dialog.show();
}

/**
 * 校验密码是否符合要求
 */
function verifyPassword(errorMessageId, inputPasswordId) {
	var newPassword = $.trim($("#" + inputPasswordId).val());
	if (newPassword == null || newPassword == "") {
		verifyStyle(false, errorMessageId, i18n.get("enterNewPassword"));
		return false;
	}	
	var pattern = /^([A-Za-z0-9]{1,})$/;// 判断是否包含非法字符
	if (!pattern.test(newPassword)) {
		verifyStyle(false, errorMessageId, i18n.get("containsIllegalCharacters"));
		return false;
	}
	if (!(/[a-z]{1,}/.test(newPassword) && /[A-Z]{1,}/.test(newPassword)
			&& /[0-9]{1,}/.test(newPassword) && /^\w{6,20}$/.test(newPassword))) {
		verifyStyle(false, errorMessageId, i18n.get("passwordRequired"));
		return false;
	}
	verifyStyle(true, errorMessageId, "");
	return true;
}

/**
 * 校验确认密码
 */
function verifyRepeatPassword(errorMessageId, inputPasswordId, repeatPasswordId) {
	var newPassword = $.trim($("#" + inputPasswordId).val());
	var repeatPassword = $.trim($("#" + repeatPasswordId).val());
	if (newPassword == null || newPassword == "") {
		verifyStyle(false, errorMessageId, i18n.get("enterNewPassword"));
		return false;
	}
	if (repeatPassword == null || repeatPassword == "") {
		verifyStyle(false, errorMessageId, i18n.get("enterConfirmPassword"));
		return false;
	}	
	var pattern = /^([A-Za-z0-9]{1,})$/;// 判断是否包含非法字符
	if (!pattern.test(repeatPassword)) {
		verifyStyle(false, errorMessageId, i18n.get("containsIllegalCharacters"));
		return false;
	}
	if (!(/[a-z]{1,}/.test(repeatPassword) && /[A-Z]{1,}/.test(repeatPassword)
			&& /[0-9]{1,}/.test(repeatPassword) && /^\w{6,20}$/.test(repeatPassword))) {
		verifyStyle(false, errorMessageId, i18n.get("passwordRequired"));
		return false;
	}
	if (newPassword != repeatPassword) {
		verifyStyle(false, errorMessageId, i18n.get("passwordInconsistent"));
		return false;
	}
	verifyStyle(true, errorMessageId, "");
	return true;
}

/**
 * 10.主机修复功能
 */
function repairHost() {
	if (!checkHostCondition()) {
		return;
	}
	Dialog.confirm(i18n.get("confirmRepairHost"), function() {
		$.ajax({
			url : "../../../ops/ops!repairOS.action",
			type : "post",
			dataType : 'json',
			async : true,
			data : "id=" + selectedHostVm_id,
			success : function(json) {
				if (json.success) {
					openDialog(i18n.get("repairHostSuccess"));
				}
				else {
					openDialog(json.resultMsg);
				}
			}
		});
	});
}

/******************************** 11.快照与还原:begin *************************************/
/**
 * 快照与还原的tab选项卡，切换新建快照,历史快照,定时备份3个界面
 */
function changeTab(m, n) {
	var menu = document.getElementById("tab" + m).getElementsByTagName("li");
	var div = document.getElementById("tablist" + m).getElementsByTagName("div");
	var showdiv = [];
	for (i = 0; j = div[i]; i++) {
		if ((" " + div[i].className + " ").indexOf(" m2yw_pic ") != -1) {
			showdiv.push(div[i]);
		}
	}
	for (i = 0; i < menu.length; i++) {
		menu[i].className = i == (n - 1) ? "m2yw_cutli" : "";
		showdiv[i].style.display = i == (n - 1) ? "block" : "none";
	}
	/************************ 点击快照和还原的第2个tab:历史快照页面  ***********************/
	if (m == 2 && n == 2) {
		initHisotrySnapShot();//初始化历史快照
	}
	/************************ 点击快照和还原的第3个tab:定时备份页面  ***********************/
	if (m == 2 && n == 3) {
		$("#week_time").timepicker({
			hourGrid : 4,
			minuteGrid : 10
		});
		$("#month_time").timepicker({
			hourGrid : 4,
			minuteGrid : 10
		});
		var backupPlanJson = null;
		$.ajax({ // 自动备份界面加载主机过去已经有的备份时间计划
			url : "../../../ops/ops!getVmBackupPlan.action",
			type : "get",
			dataType : 'json',
			async : false,
			data : "id=" + selectedHostVm_id,
			success : function(json) {
				if (json.success) {
					backupPlanJson = json;
				}
			}
		});
		if (backupPlanJson != null && backupPlanJson.resultObject != null) {//有备份计划就显示自动定时备份的时间和日期
			var planType = backupPlanJson.resultObject.planType;
			var planDate = backupPlanJson.resultObject.planDate;
			var planTime = backupPlanJson.resultObject.planTime.substring(0, 5);
			$('#snapshotTable input[type=radio]').each(function(i) {
				if (planType == this.value) {
					this.checked = true;
				}
			});
			if (planType == 1) {// 主机按每周备份
				$('#week_select option[value=' + planDate + ']').attr("selected", true);
				$("#week_time").val(planTime);
			} 
			else if (planType == 2) {// 主机按每月备份
				$('#month_select option[value=' + planDate + ']').attr("selected", true);
				$("#month_time").val(planTime);
			}
		}
	}
}

/**
 * 快照和还原
 */
function snapshotAndRestore() {
	if (!checkHostCondition()) {
		return;
	}
	var nowTime = new Date().format("yyyyMMddhhmmss");// 快照界面的当前时间
	var html = '<table width="500" cellpadding="0" cellspacing="0" border="0"><tr><td>'
		     + '    <div class="m2yw_right">'
		     + '        <div class="m2yw_tab">'
		     + '            <ul id="tab2">'
		     + '                <li class="m2yw_cutli" style="margin-left:-20px;" onclick="changeTab(2,1)">' + i18n.get("createSnapshot") + '</li>'
		     + '                <li onclick="changeTab(2,2)">' + i18n.get("historySnapshot") + '</li>'
		     + '                <li onclick="changeTab(2,3)">' + i18n.get("timingBackup") + '</li>'
		     + '            </ul>'
		     + '        </div>'
		     + '        <div id="tablist2">'
/*************************************** 新建快照 ***************************************/
		     + '            <div class="m2yw_pic">'
		     + '                <div class="m2yw_piclist">'
		     + '                    <table id="snapshotTable">'
		     + '                        <tr><td class="snapshot_td1"><font color="red">*</font>' + i18n.get("snapshotName") + '</td><td class="snapshot_td2"><input id="snapshotName" type="text" maxlength="20" onblur="checkSnapshotName()" value="' + nowTime + '"/></td></tr>'
		     + '                        <tr><td class="snapshot_td1">' + i18n.get("snapshotComment") + '</td><td class="snapshot_td2"><textarea id="snapshotComments"></textarea></td></tr>'
		     + '                        <tr><td class="snapshot_td1">&nbsp;</td><td id="snapshotMessage" class="snapshot_td2"></td></tr>'
		     + '                        <tr><td class="snapshot_td1">&nbsp;</td><td class="snapshot_td2"><input id="createSnapshot" class="page_button" type="button" value="' + i18n.get("confirmCreate") + '"></td></tr>'
		     + '                    </table>'
		     + '                </div>'
		     + '            </div>'
/*************************************** 历史快照 ***************************************/
		     + '            <div class="m2yw_pic hidden">'
		     + '                <div class="m2yw_piclist">'
		     + '                    <table class="hSTable" style="margin-top: 2px;">'
		     + '                        <tr><td class="hSTd1">' + i18n.get("history1") + '</td><td class="hSTd2">' + i18n.get("history2") + '</td><td class="hSTd3">' + i18n.get("history3") + '</td><td class="hSTd4">' + i18n.get("history4") + '</td><td class="hSTd5">' + i18n.get("history5") + '</td></tr>'
		     + '                    </table>'
		     + '                    <div id="historySnapshots">'
		     + '                    </div>'
		     + '                </div>'
		     + '            </div>'
/*************************************** 定时备份 ***************************************/
		     + '            <div class="m2yw_pic hidden">'
 		     + '                <div class="m2yw_piclist">'
		     + '                    <table id="snapshotTable">'
		     + '                        <tr><td class="restore_td1">' + i18n.get("restore1") + '</td><td class="restore_td2" colspan="2"><input type="radio" name="backup_type" value="0" checked="checked"/>' + i18n.get("restore2") + '</td></tr>'
		     + '                        <tr><td class="restore_td1">&nbsp;</td><td class="restore_td2"><input type="radio" name="backup_type" value="1"/>' + i18n.get("everyWeek") + '<select id="week_select"><option value="1">' + i18n.get("monday") + '</option><option value="2">' + i18n.get("tuesday") + '</option><option value="3">' + i18n.get("wednesday") + '</option><option value="4">' + i18n.get("thursday") + '</option><option value="5">' + i18n.get("friday") + '</option><option value="6">' + i18n.get("saturday") + '</option><option value="0">' + i18n.get("sunday") + '</option></select></td><td class="restore_td3"><input id="week_time" type="text" value="00:00" /></td></tr>'
		     + '                        <tr><td class="restore_td1">&nbsp;</td><td class="restore_td2"><input type="radio" name="backup_type" value="2"/>' + i18n.get("everyMonth") + '<select id="month_select"><option value="1">' + i18n.get("day1") + '</option><option value="2">' + i18n.get("day2") + '</option><option value="3">' + i18n.get("day3") + '</option><option value="4">' + i18n.get("day4") + '</option><option value="5">' + i18n.get("day5") + '</option><option value="6">' + i18n.get("day6") + '</option><option value="7">' + i18n.get("day7") + '</option><option value="8">' + i18n.get("day8") + '</option><option value="9">' + i18n.get("day9") + '</option><option value="10">' + i18n.get("day10") + '</option><option value="11">' + i18n.get("day11") + '</option><option value="12">' + i18n.get("day12") + '</option><option value="13">' + i18n.get("day13") + '</option><option value="14">' + i18n.get("day14") + '</option><option value="15">' + i18n.get("day15") + '</option><option value="16">' + i18n.get("day16") + '</option><option value="17">' + i18n.get("day17") + '</option><option value="18">' + i18n.get("day18") + '</option><option value="19">' + i18n.get("day19") + '</option><option value="20">' + i18n.get("day20") + '</option><option value="21">' + i18n.get("day21") + '</option><option value="22">' + i18n.get("day22") + '</option><option value="23">' + i18n.get("day23") + '</option><option value="24">' + i18n.get("day24") + '</option><option value="25">' + i18n.get("day25") + '</option><option value="26">' + i18n.get("day26") + '</option><option value="27">' + i18n.get("day27") + '</option><option value="28">' + i18n.get("day28") + '</option></select></td><td class="restore_td3"><input id="month_time" type="text" value="00:00" /></td></tr>'
		     + '                        <tr><td class="restore_td1">&nbsp;</td><td id="restoreMessage" colspan="2">' + i18n.get("restorePrompt") + '</td></tr>'
		     + '                        <tr><td class="restore_td1">&nbsp;</td><td class="restore_td2"><input id="restoreButton" type="button" value="' + i18n.get("save") + '"></td></tr>'
		     + '                    </table>' 		     
 		     + '                </div>'
 		     + '            </div>'		     
		     + '        </div>'
		     + '    </div>'
		     + '</td></tr></table>';
	snapshotAndRestoreDialog = new Dialog("Dialog");//第一层dialog
	snapshotAndRestoreDialog.Title = i18n.get("snapshotRestore");
	snapshotAndRestoreDialog.innerHTML = html;
	snapshotAndRestoreDialog.Height = 393;
	snapshotAndRestoreDialog.Width = 510;
	snapshotAndRestoreDialog.ShowButtonRow = false;//第一层dialog不需要确定和取消按钮
	snapshotAndRestoreDialog.show();
	$("#createSnapshot").click(function() {//点击了tab1(新建快照)的确认创建快照按钮
		if (checkSnapshotName()) {
			Dialog.confirm(i18n.get("snapshotPrompt"), function() {
				var snapshotName = $.trim($("#snapshotName").val());
				var snapshotComments = $.trim($("#snapshotComments").val());
				snapshotAndRestoreDialog.close();//关闭快照与还原这个第一层dialog
				$.ajax({
					url : "../../../ops/ops!backupVM.action",
					type : "post",
					dataType : 'json',
					async : true,
					data : "id="+selectedHostVm_id+"&sn_name="+snapshotName+"&sn_comments="+snapshotComments+"&sn_type=0",
					success : function(json) {
						if (json.success)
							openDialog(i18n.get("snapshotSuccess"));
						else
							openDialog(json.resultMsg);
					}
				});
			});
		}		
	});
	$("#restoreButton").click(function() {//点击了tab3(定时备份)的保存按钮
		var planType = "0";//默认不自动备份
		var planDate = "1";
		var planTime = "00:00";
		$("#snapshotTable input[type=radio]").each(function(i) {
			if (this.checked == true) {
				planType = this.value;
			}
		});
		if (planType == "1") {
			planDate = $("#week_select").val();
			planTime = $("#week_time").val();
		} 
		else if (planType == "2") {
			planDate = $("#month_select").val();
			planTime = $("#month_time").val();
		}
		Dialog.confirm(i18n.get("storePlan"), function() {
			snapshotAndRestoreDialog.close();//关闭快照与还原这个第一层dialog
			$.ajax({
				url : "../../../ops/ops!createBackupVmPlan.action?id=" + selectedHostVm_id + "&planTime=" + planTime,
				type : "post",
				dataType : "json",
				data : "{\"planType\":" + planType + ",\"planDate\":" + planDate + ",\"vmId\":\"" + selectedHostVm_id + "\"}",
				contentType : "application/json; charset=utf-8",
				success : function(json) {
					if (json.success)
						openDialog(i18n.get("storePlanSuccess"));
					else
						openDialog(json.resultMsg);
				}
			});
		});		
	});
}

/**
 * 检查快照名
 */
function checkSnapshotName() {
	var snapshotName = $.trim($("#snapshotName").val());
	if (snapshotName == null || snapshotName == "") {
		$("#snapshotMessage").text(i18n.get("snapshotNameEmpty"));
		$("#snapshotMessage").css("color", "red");
		return false;
	}
	if (checkHasContainedIllegalChar(snapshotName)) {
		$("#snapshotMessage").text(i18n.get("snapshotNameIllegal"));
		$("#snapshotMessage").css("color", "red");
		return false;
	}
	$("#snapshotMessage").text("");
	return true;
}

/**
 * 初始化历史快照
 */
function initHisotrySnapShot() {
	$("#historySnapshots").html("");//清空以前的数据
	$.ajax({ // 加载当前主机的以前的所有的快照
		url : "../../../ops/ops!findAllBackups.action",
		type : "get",
		dataType : 'json',
		async : false,
		data : "id=" + selectedHostVm_id,
		success : function(json) {
			if (json.success) {
				for (var i = 0; i < json.resultObject.length; i++) {
					var index = i + 1;//序号
					var bgcolor = index % 2 == 1 ? "#E1E1FF" : "#FFFFFF";//背景颜色
					var comment = json.resultObject[i].snapShot_comments == null ? "" : json.resultObject[i].snapShot_comments;//备注
					var snapshotName = json.resultObject[i].snapShot_name;
					var snapshotType = json.resultObject[i].snapShot_type  == 1 ? i18n.get("timing") : i18n.get("manual");
					var createtime = new Date(json.resultObject[i].createTime).format("yyyy-MM-dd hh:mm:ss");
					var snapshotId = json.resultObject[i].id;//快照在hc_snapshot的id
					var html = '<table class="hSTable" cellpadding="0" cellspacing="0">'
			                 + '    <tr bgcolor="' + bgcolor + '">'
			                 + '        <td class="hSTd1">' + index + '</td>'
			                 + '        <td class="hSTd2" title="' + comment + '">' + snapshotName + " (" + snapshotType + ")" + '</td>'
			                 + '        <td class="hSTd3">' + createtime + '</td>'
			                 + '        <td class="hSTd4"><img onclick="restoreSnapshot(' + snapshotId + ')" src="'+rootPath+"/skin/"+customerPath+"/images/vpdc/center/backup_back.png"+'" class="hSTdRestore"></td>'
			                 + '        <td class="hSTd5"><img onclick="deleteSnapshot(' + snapshotId + ')" src="'+rootPath+"/skin/"+customerPath+"/images/vpdc/center/backup_dele.png"+'" class="hSTdDelete"></td>'
			                 + '    </tr>'
			                 + '</table>';
					$("#historySnapshots").append(html);
				}
			}
			else {
				openDialog(json.resultMsg);
			}
		}
	});	
}

/**
 * 还原快照
 */
function restoreSnapshot(snapshotId) {
	Dialog.confirm(i18n.get("confirmRestoreHost"), function() {
		snapshotAndRestoreDialog.close();//关闭快照与还原这个第一层dialog
		$.ajax({
			url : "../../../ops/ops!renewVm.action",
			type : "post",
			dataType : 'json',
			async : true,
			data : "id=" + selectedHostVm_id + "&ssid=" + snapshotId,
			success : function(json) {
				if (json.success)
					openDialog(i18n.get("restoreSuccess"));
				else
					openDialog(json.resultMsg);
			}
		});
	});	
}

/**
 * 删除快照
 */
function deleteSnapshot(snapshotId) {
	Dialog.confirm(i18n.get("deleteSnapshot"), function() {
		$.ajax({
			url : "../../../ops/ops!deleteBackup.action",
			type : "post",
			dataType : 'json',
			async : true,
			data : "id=" + selectedHostVm_id + "&ssid=" + snapshotId,
			success : function(json) {
				if (json.success)
					initHisotrySnapShot();
				else
					openDialog(json.resultMsg);
			}
		});
	});
}
/********************************* 11.快照与还原:end **************************************/

/**
 * 双击主机,弹出主机详情
 */
function getCurrentHostDetailInfo(obj) {
	selectCurrentHost(obj);
	getHostDetailInfo();
}

/**
 * 12.获得主机详细信息
 */
function getHostDetailInfo() {
	if (!isHostRegular()) {
		return;
	}
	$.ajax({
		url : "../../../ops/ops!findDetailVmById.action",
		type : "get",
		dataType : 'json',
		async : false,
		data : "id=" + selectedHostVm_id,
		success : function(json) {
			if (json.success) {
				var basicConfiguration = "";//基本配置: CPU/内存/系统盘
				if (json.resultObject.cpu == null) //CPU
					basicConfiguration += "CPU --/";
				else
					basicConfiguration += "CPU " + json.resultObject.cpu + i18n.get("core") + (json.resultObject.cpuName == null ? "" : "(" + json.resultObject.cpuName + ")/");
				if (json.resultObject.memory == null) // 内存
					basicConfiguration += i18n.get("systemMemory") + " --/";
				else
					basicConfiguration += i18n.get("systemMemory") + " " + json.resultObject.memory + "M" + (json.resultObject.memName == null ? "" : "(" + json.resultObject.memName + ")/");
				if (json.resultObject.disk == null) //系统盘
					basicConfiguration += i18n.get("systemDisk") + " --";
				else
					basicConfiguration += i18n.get("systemDisk") + " " + json.resultObject.disk + "G" + (json.resultObject.diskName == null ? "" : "(" + json.resultObject.diskName + ")");
				
				var extdisks = json.resultObject.extdisks;// 扩展盘
				if (extdisks == null || extdisks.length == 0) {
					extdisks = "--";
				} 
				else {
					var extdisksStr = "";
					for (var i = 0; i < extdisks.length; i++)
						extdisksStr += extdisks[i].ed_capacity + "G/";
					extdisks = extdisksStr.substring(0, extdisksStr.length - 1);
				}			
				
				var bandwidth = json.resultObject.network;//带宽
				if (bandwidth == null) //带宽
					bandwidth = "--";
				else
					bandwidth = json.resultObject.network + "M " + (json.resultObject.networkName == null ? "" : "(" + json.resultObject.networkName + ")");

				var intranetIPandExtranetIP = "";
				if (json.resultObject.ip == null) {
					intranetIPandExtranetIP = "--/--";
				}
				else {
					var hostIPArray = json.resultObject.ip.split(",");
					if (hostIPArray.length == 1) {
						var intranetContent = hostIPArray[0] == "" ? "--" : hostIPArray[0];
						intranetIPandExtranetIP = intranetContent + "/--";
					} 
					else if (hostIPArray.length == 2)
						intranetIPandExtranetIP = hostIPArray[0] + "/" + hostIPArray[1];
					else
						intranetIPandExtranetIP = hostIPArray[0] + "/" + hostIPArray[1] + "...";
				}
				
				var effectiveTime = new Date(json.resultObject.effectiveDate).format('yyyy-MM-dd hh:mm:ss');
				var orderNo = json.resultObject.orderNo;// 订单编号
				if (orderNo == null)
					orderNo = "--";
				var orderTime = json.resultObject.orderDate;// 订单时间
				if (orderTime == null)
					orderTime = "--";
				else
					orderTime = new Date(json.resultObject.orderDate).format('yyyy-MM-dd hh:mm:ss');
				
				var priceAndType = "";//套餐价格
				if (json.resultObject.price == null)
					priceAndType = "--";
				else
					priceAndType = json.resultObject.price + i18n.get("yuan") + "/";
				if (json.resultObject.pricePeriodType == 0)
					priceAndType += i18n.get("byYear");
				else if (json.resultObject.pricePeriodType == 1)
					priceAndType += i18n.get("byMonth");
				else if (json.resultObject.pricePeriodType == 2)
					priceAndType += i18n.get("byHour");
				
				var remainedHours = getRemainedTime(json.resultObject.spare) + isHostEnable3(json.resultObject.vmIsEnable, null);
				
				var html = '<table class="dialog2Table" style="font-size: 14px;margin-top: 10px;">'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("host-Name") + '</td><td class="dialog2Td2">' + json.resultObject.vmName + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("machine-Number") + '</td><td class="dialog2Td2">' + selectedHostVm_id + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("basic-Configuration") + '</td><td class="dialog2Td2">' + basicConfiguration + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("extended-Disk") + '</td><td class="dialog2Td2">' + extdisks + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("network-Bandwidth") + '</td><td class="dialog2Td2">' + bandwidth + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("host-IP") + '</td><td class="dialog2Td2" title="' + json.resultObject.ip + '">' + intranetIPandExtranetIP + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("operating-System") + '</td><td class="dialog2Td2">' + json.resultObject.os + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("package-Name") + '</td><td class="dialog2Td2">' + json.resultObject.scname + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("effective-Time") + '</td><td class="dialog2Td2">' + effectiveTime + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("used-Time") + '</td><td class="dialog2Td2">' + getHasUsedHours(json.resultObject.runTime, null) + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("remaining-Time") + '</td><td class="dialog2Td2">' + remainedHours + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("order-Number") + '</td><td class="dialog2Td2">' + orderNo + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("order-Time") + '</td><td class="dialog2Td2">' + orderTime + '</td></tr>'
					     + '    <tr><td class="dialog2Td1">' + i18n.get("package-Price") + '</td><td class="dialog2Td2">' + priceAndType + '</td></tr>'
					     + '</table>';
				openDialog(html, i18n.get("getDetailInfo"), 430, 600);
			} 
			else {
				openDialog(json.resultMsg);
			}
		}
	});
}

/**
 * 13.设置控制面板密码
 */
function setControlPanelPassword() {
	if (!checkHostCondition()) {
		return;
	}
	var html = '<table class="dialogTable" style="margin-top: 30px;">'
		     + '    <tr><td class="dialogTd1">' + i18n.get("newPassword") + '</td><td class="dialogTd2"><input id="CPPassword" class="dialogTable_input" type="password" maxlength="20" onblur="verifyPassword(\'CPPassword_message\', \'CPPassword\')"/></td></tr>'
		     + '    <tr><td class="dialogTd1">' + i18n.get("confirmPassword") + '</td><td class="dialogTd2"><input id="repeatCPPassword" class="dialogTable_input" type="password" maxlength="20" onblur="verifyRepeatPassword(\'CPPassword_message\', \'CPPassword\', \'repeatCPPassword\')"/></td></tr>'
		     + '    <tr><td class="dialogTd1" style="height: 24px;">&nbsp;</td><td class="dialogTd2" style="height: 24px;"><span id="CPPassword_message"></span></td></tr>'
		     + '</table>';
	var dialog = new Dialog("Dialog");
	dialog.Title = i18n.get("setCPPassword2");
	dialog.innerHTML = html;
	dialog.Height = 115;
	dialog.Width = 500;
	dialog.OKEvent = function() {//点击确定按钮调用的方法
		if (!verifyRepeatPassword("CPPassword_message", "CPPassword", "repeatCPPassword")) {
			return;
		}
		var password = $.md5($.trim($("#CPPassword").val()));
		dialog.close();//关闭设置控制面板密码这个dialog
		$.ajax({
			url : "../../../ops/ops!setCPpwd.action",
			type : "post",
			dataType : 'json',
			async : false,
			data : "id=" + selectedHostVm_id + "&CPpwd=" + password,
			success : function(json) {
				if (json.success) {
					openDialog(i18n.get("resetPasswordSuccess"));
				}
				else {
					openDialog(json.resultMsg);
				}
			}
		});	
	};
	dialog.show();
}

//操作按钮高亮显示，操作按钮低亮显示
function highlightLowlight(imageName, obj) {
	var src = rootPath+"/skin/"+customerPath+"/images/vpdc/center/"+imageName+".png";
	$(obj).find("img").eq(0).attr("src", src);
	if (imageName.indexOf("_") > -1) {
		$(obj).find("td").eq(1).attr("style", "color: #EE7700;");
	}
	else {
		$(obj).find("td").eq(1).attr("style", "color: white;");
	}
}
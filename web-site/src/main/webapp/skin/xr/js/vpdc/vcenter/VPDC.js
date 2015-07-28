/**
 * $(document).ready():初始化 VPDC页面
 */
$(document).ready(function() {
	initCss('common/common.css');
	initJs('col/header.js');
	initJs('col/footer.js');
	initCss('vpdc/vcenter/VPDC.css');
	initDialog();//加载dialog的css和Js
	i18n.init();//国际化初始化
	vpdcCenterPageUrls=["../../vpdc/center/vmList.html","../../vpdc/vcenter/VPDC.html"];	
	initTagAndMenu(vpdcCenterNameArr, vpdcCenterPageUrls);
	i18nPage();//国际化当前页面
	$.getScript('../../../skin/'+customerPath+'/js/common/dialog/dialog.js', function(){
		queryVpdcInfo();
	});
});

var currentPageVpdcResult;//当前这一页所有VPDC的数据result
var selectedVpdcId;//当前选中的VPDC的id
var selectedVpdcMode;//当前选中的VPDC的模式
var selectedVpdcZoneGroupId;//当前选中的VPDC的ZoneGroupId

function i18nPage() {
	$("#createVPDC").text(i18n.get("createVPDC"));
	$("#hostSort").text(i18n.get("hostSort"));
	$("#query").attr("placeHolder", i18n.get("enterVpdcNamePrompt"));
	$("#allMode").text(i18n.get("allMode"));
	$("#nonRoutingMode").text(i18n.get("nonRoutingMode"));
	$("#routingMode").text(i18n.get("routingMode"));
	$("#vpdcName").text(i18n.get("vpdcName"));
	$("#vpdcRoomLine").text(i18n.get("vpdcRoomLine"));
	$("#vpdcMode").text(i18n.get("vpdcMode"));
	$("#vpdcTime").text(i18n.get("vpdcTime"));
	$("#enterVpdc").text(i18n.get("enterVpdc"));
	$("#modifyVpdc").text(i18n.get("modifyVpdc"));
	$("#deleteVpdc").text(i18n.get("deleteVpdc"));
}

/**
 * 点击添加VPDC按钮,跳转到添加VPDC页面
 */
function createVPDC() {
	window.location = rootPath+"/function/vpdc/vcenter/createVPDC.html?LIIndex=1";
}

/****************************** 1.分页查询VPDC数据 *********************************/
var pageObje = new PageObje('tableId');
pageObje.size = 3;
pageObje.methodArray = [ [ 'vpdcType', 'getVpdcType' ],    [ 'useLong', 'getUseLong' ]
                       ];
pageObje.column = [ 'id',/* VPDC的id */              'name',/* VPDC名称 */ 
                    'vpdcType',/* VPDC类型（0：非路由；1：路由） */ 
                    'zoneGroup',/* 机房线路id */      'zoneGroupName',/* 机房线路名称 */   
                    'zones',/* 机房下的所有zone(格式：BJ,SH,GZ) */
                    'useLong',/* 时长 */              'routerTemplateId',/* router配置模板ID */ 
                    'cpuCore',/* router CPU核数 */    'diskCapacity',/* router磁盘容量 */ 
                    'memSize',/* router内存大小 */     'bandWidth',/* router带宽 */ 
                    'osId',/* router操作系统ID */     'routerImage',/* router的镜像 */ 
                    'vlans'/* 集合ArrayList<VlanNetwork> */
                   ];
/**
 * 根据1个下拉菜单和1个关键字输入框查询VPDC全部信息
 */
function queryVpdcInfo() {
	pageObje.jsonRequest = {
		"vpdcType" : $("#sort").val(),//-1:全部模式   0:非路由模式  1:路由模式
		"name" : $.trim($("#query").val()),
		"page" : pageObje.current,
		"limit" : pageObje.size
	};
	pageObje.initFlag = true;
	pageObje.pageFirst();
}

/**
 * 换页: VPDC主界面换页
 */
function pageChange(pageObje) {
	currentPageVpdcResult = selectedVpdcId = selectedVpdcMode = selectedVpdcZoneGroupId = null;// 换页时清空以前页面的数据
	pageObje.jsonRequest['page'] = pageObje.current;
	$.ajax({
		url : '../../../ops/ops!findVpdcsByUser.action',
		type : 'get',
		dataType : 'json',
		async : false,
		contentType : "application/json; charset=utf-8",
		data : pageObje.jsonRequest,
		success : function(data) {
			if (data.success) {//当模板为div时,使用pageCreatorR1创建
				pageCreatorR1(pageObje, data.resultObject);
				currentPageVpdcResult = data.resultObject.result;
				for (var i = 0; i < currentPageVpdcResult.length; i++) {
					$(".vpdcImage").eq(i).attr("id", currentPageVpdcResult[i].zoneGroup);//区域组的id
					if (currentPageVpdcResult[i].vpdcType == 0) {//0:非路由模式
						$(".vpdcImage").eq(i).attr("name", 0);//vpdc的时长
						$(".vpdcImage").eq(i).attr("src", rootPath+"/skin/"+customerPath+"/images/vpdc/center/nonRoutingMode.png");
					}
					else {//1:路由模式
						$(".vpdcImage").eq(i).attr("name", currentPageVpdcResult[i].useLong);//vpdc的时长
						$(".vpdcImage").eq(i).attr("src", rootPath+"/skin/"+customerPath+"/images/vpdc/center/routingMode.png");
					}
				}
			}
			else {
				openDialog(data.resultMsg);
			}
			$("#vpdcContent").css("display", "block");
		}
	});
}

// 获取VPDC类型（0：非路由；1：路由）
function getVpdcType(str, array) {
	if (0 == str) {
		return i18n.get("nonRoutingMode");
	} 
	else {
		return i18n.get("routingMode");
	}
}

//获取时长
function getUseLong(str, array) {
	if (null == str) {
		return i18n.get("unlimited");
	} 
	else {
		return str + i18n.get("timeDesc");
	} 
}

/**
 * 当用户鼠标浮在某VPDC上面,就选中了这VPDC:
 */
function selectCurrentVpdc(obj) {
	selectedVpdcId = selectedVpdcMode = selectedVpdcZoneGroupId = null;//清空以前的数据
	selectedVpdcId = $(obj).find("div").eq(0).attr("id");
	if ($(obj).find("img").eq(0).attr("src").indexOf("nonRoutingMode") == -1) {
		selectedVpdcMode = "routingMode";
	} else {
		selectedVpdcMode = "nonRoutingMode";
	}
	selectedVpdcZoneGroupId = $(obj).find("img").eq(0).attr("id");
}

//进入VPDC
function enterVpdc() {
	$.cookie("selectedVpdcId", selectedVpdcId);//添加VPDC id到cookie中
	$.cookie("selectedVpdcMode", selectedVpdcMode);//添加VPDC mode到cookie中
	$.cookie("selectedVpdcZoneGroupId", selectedVpdcZoneGroupId);//添加VPDC zoneGroupId到cookie中
	window.location = rootPath+"/function/vpdc/vcenter/enterVPDC.html?LIIndex=1";
}

//修改VPDC
function modifyVpdc() {
	$.cookie("selectedVpdcId", selectedVpdcId);//添加VPDC id到cookie中
	window.location = rootPath+"/function/vpdc/vcenter/modifyVPDC.html?LIIndex=1";
}

//删除VPDC
function deleteVpdc() {
	Dialog.confirm(i18n.get("deleteVpdcPrompt"), function() {
		$.ajax({
			url : "../../../ops/ops!deleteVPDC.action",
			type : "get",
			dataType : 'json',
			async : false,
			data : "vpdcId=" + selectedVpdcId,
			success : function(json) {
				if (json.success) {
					window.location = rootPath+"/function/vpdc/vcenter/VPDC.html?LIIndex=1";
				} else {
					openDialog(json.resultMsg);
				}
			}
		});
	});
}
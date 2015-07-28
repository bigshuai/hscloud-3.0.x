/**
 * global varibles
 */
//当前已登录的用户的信息
var user = null;
var i18n = {
	resourceBundle : {},

	checkLocale : function() {
		/* get browser default lang */
		if (jQuery.cookie("prefer_locale")) {
			return jQuery.cookie("prefer_locale");
		}
		var baseLang = "en_US";
		if (navigator.userLanguage) {
			baseLang = navigator.userLanguage.substring(0, 5);
		} else {
			baseLang = navigator.language.substring(0, 5);
		}
		var lan = baseLang.split("-");
		if(lan.length==1){
			if("en"==lan[0].toLocaleLowerCase()){
				lang = lan[0].toLocaleLowerCase()+"_US";
			}else{
				lang = lan[0].toLocaleLowerCase()+"_CN";
			}
		}else if(lan.length==2){
			var lang = lan[0].toLocaleLowerCase()+"_"+lan[1].toLocaleUpperCase();
		}else{
			lang="en_US";
		}
		return lang;
	},

	init : function(resource_url) {
		if (!resource_url) {
			resource_url = "../../resources";
		}

		var lang = this.checkLocale();
		jQuery.ajax({
			url : resource_url + "/" + lang + ".js",
			async : false,
			dataType : 'json',
			success : function(data) {
				i18n.resourceBundle = data;
			}
		});
	},
	//普通页面国际化
	_ : function(key) {
		document.write(key);
	},
	//弹出页面国际化
	_dialog : function(id,key) {
			document.getElementById(id).innerHTML=key;
		}
}

function changeLocale(locale){
	var cl = jQuery.cookie('prefer_locale');
	
	if(locale && (locale == "en_US" || locale == "zh_CN")){
		
			if(jQuery("#homepage").size()== 0){
				url = REMOTE_AGENT_PREFIX +  REMOTE_AGENT_LIST["user.setLocale"]["user.setLocale"];
			}else{
				url = REMOTE_AGENT_LIST["user.setLocale"]["user.setLocale"];
			}
			jQuery.ajax({
				  url: url,
				  type: 'POST',
				  data: "request_locale="+locale,
				  success:function(da){
					  //if(da['success']){
						  if(!cl || cl != locale){
							jQuery.cookie("prefer_locale",null);
							jQuery.cookie('prefer_locale',locale,{path:'/'});
							window.location.reload(); 
						  }
					 // }
				  }
			});
	}
	
}
/**
 * 虚拟机状态的国际化（独立开发）
 */
var vm_status = {
		'ACTIVE'  : '活动',
		'BUILD' : '新建',
		'BUILDING':'生成中',
		'REBOOT'  : '重启',
		'HARD_REBOOT' : '冷重启',
		'REBUILD'  : '重建',
		'SUSPENDED'  : '关闭',
		'UNKNOWN'  : '未知',
		'SHUTOFF'  : '关闭电源',
		'REBOOTING' : '重启中',
		'REBOOTING_HARD' : '冷重启中',
		'SUSPENDING' : '关闭中',
		'RUNNING' : '运行中',
		"SCHEDULING" : "调度中",
		"NETWORKING" : "通信中",
		"ERROR" : "错误",
		"RESUMING" : "唤醒中",
		"REBUILDING" : "重建",
		"IMAGE_SNAPSHOT" : "备份中",
		"SPAWNING" : "还原中",
		"N/A" : "无",
		"unDeployed" : "未发布",
		"DELETING" : "删除中",
		"noInstance" : "无实例",
		"RESIZE_PREP" : "调整中",
		"RESIZE" : "迁移中...",
		"VERIFY_RESIZE" : "确认调整",
		"RESIZE_VERIFY" : "调整确认",
		"RESIZE_CONFIRM" : "调整待确认",
		"REVERT_RESIZE" : "还原调整",
		"RESIZE_REVERTING" : "调整还原中",
		"RESIZE_CONFIRMING":"调整确认中",
		"BLOCK_DEVICE_MAPPING" : "块设备映射",
		"snapshot_wait" : "待备份",
		"UPDATING_PASSWORD":"密码修改中",
		"IMAGE_EXPEND":"磁盘扩容中",
		"POWERING-OFF":"电源关闭中",
		"POWERING-ON":"电源开启中",
		"OS_REPAIR":"系统修复中",
	i18n : function (key){
		if("zh_CN"==i18n.checkLocale()){
			return vm_status[key];
		}else{
			return key;
		}
	}
}
/**
 *Ajax
 * 请求远程的服务器数据
 * @param {Object} module 参见 config.js 中的 REMOTE_AGENT_LIST
 * @param {Object} operation 参见 config.js 中的 REMOTE_AGENT_LIST
 * @param {Object} jsonRequest JSON 请求数据
 * @param {Object} callback 回调函数，格式为: function(data); data 为Object类型
 */
function ajax(module, operation, jsonRequest, callback) {
	var url = "";
	if (DEBUG_MODE) {
		url = REMOTE_AGENT_PREFIX + "/"+module + "/" + operation + "/response.json";
		$.getJSON(url, null, callback);
	} else {
		url = REMOTE_AGENT_PREFIX +  REMOTE_AGENT_LIST[module][operation];
		$.getJSON(url, jsonRequest, callback);
	}
}

function ajaxPost(module, operation,jsonRequest,param,callback) {
	var url = "";
	if (DEBUG_MODE) {
		url = REMOTE_AGENT_PREFIX + module + "/" + operation + "/response.json";
		$.ajax({
			  url: url,
			  type: 'POST',
			  dataType: 'json',
			  contentType: "application/json; charset=utf-8",
			  data: null,
			  success: callback
		});
	} else {
		if(null != param && ""!=param){
			url = REMOTE_AGENT_PREFIX + REMOTE_AGENT_LIST[module][operation]+"?"+param;
		}else{
			url = REMOTE_AGENT_PREFIX +  REMOTE_AGENT_LIST[module][operation];
		}
		$.ajax({
			  url: url,
			  type: 'POST',
			  dataType: 'json',
			  contentType: "application/json; charset=utf-8",
			  data: jsonRequest,
			  success: callback
		});
	}
}

formatDate = function(date) {
	var locale = i18n.checkLocale();
	if("Invalid Date" == date){
		return "";
	}
/*	if (locale == 'zh_CN') {
		return date.getFullYear() + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日";
	}*/
	return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "";
};

formatTime = function(date) {
	if("Invalid Date" == date){
		return "";
	}
	var locale = i18n.checkLocale();
	/*if (locale == 'zh_CN') {
		return date.getHours() + "点" + date.getMinutes() + "分" + date.getSeconds() + "秒";
	}*/
	return date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "";
};

formatDatetime = function(date) {
	return formatDate(date) + " " + formatTime(date);
};
function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}
function fileter(){
	var localUrl = location.href;
	$.ajax({
		async : false,
		type : "get",
		url : "../controlpanel/login!getSessionUser.action",//REMOTE_AGENT_PREFIX
		dataType : 'json',
		success : function(json) {
			userJson = json;
			if(!json.success || json.resultObject==null){
				if(window.location.href.indexOf("/console")<0){
					window.location.href="../index.html";
				}
			}
		},
		error : function() {
			//alert("error");
			window.location.href="../index.html";
		}
	})
}
//初始化menu
function initMenu(tab) {
	var menuStr = "<a href=\"../../VPDC/center/index.html\" ><img border=\"0\" src=\"../../images/shared/nav_vpdc_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\" onmouseover=\"over_manueIMG(this)\" onmouseout=\"out_manueIMG(" + tab + ")\"></a>";
	fileter();
	if("SubUser"==userJson.resultObject.userType){
		menuStr +="<img border=\"0\" src=\"../../images/shared/nav_sc_invalid_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\">" + 
		"<img border=\"0\" src=\"../../images/shared/nav_order_invalid_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\">" + 
		"<img border=\"0\" src=\"../../images/shared/nav_user_invalid_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\">" + 
		"<img border=\"0\" src=\"../../images/shared/nav_account_invalid_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\">"+
		"<img border=\"0\" src=\"../../images/shared/nav_message_invalid_en_US.png\" width=\"163\" height=\"51\"" + "alt=\"\" style=\"float: rigth;\">";
	}else{
		menuStr +="<a href=\"../../sc/sc/index.html\"><img border=\"0\" src=\"../../images/shared/nav_sc_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\" onmouseover=\"over_manueIMG(this)\" onmouseout=\"out_manueIMG(" + tab + ")\"></a>" + 
		"<a href=\"../../order/unpaidorder/index.html\"><img border=\"0\" src=\"../../images/shared/nav_order_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\" onmouseover=\"over_manueIMG(this)\" onmouseout=\"out_manueIMG(" + tab + ")\"></a>" + 
		"<a href=\"../../user/info/index.html\" ><img border=\"0\" src=\"../../images/shared/nav_user_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\" onmouseover=\"over_manueIMG(this)\" onmouseout=\"out_manueIMG(" + tab + ")\"></a>" + 
		"<a href=\"../../user/account/index.html\"><img border=\"0\" src=\"../../images/shared/nav_account_en_US.png\" width=\"162\" height=\"51\"" + "alt=\"\" style=\"float: left;\" onmouseover=\"over_manueIMG(this)\" onmouseout=\"out_manueIMG(" + tab + ")\"></a>"+
		"<a href=\"../../message/readed/index.html\" ><img border=\"0\" src=\"../../images/shared/nav_message_en_US.png\" width=\"163\" height=\"51\"" + "alt=\"\" style=\"float: rigth;\" onmousemove=\"over_manueIMG(this)\" onmouseout=\"out_manueIMG(" + tab + ")\"></a>";
	}
	document.write(menuStr);
	out_manueIMG(tab);
}

//鼠标离开menu
function out_manueIMG(tab) {
	var comp_nav_div = $("#navDiv");
	var imgs = $("img", comp_nav_div);
	for (var i = 0; i < imgs.length; i++) {
		var url = imgs[i].src;
		if (tab == i) {
			if (url.indexOf("_1") < 0) {
				var url1 = url.substring(0, imgs[i].src.length - 10);
				var url2 = url.substring(imgs[i].src.length - 10, imgs[i].src.length);
				var new_url = url1 + "_1" + url2;
				imgs[i].src = new_url;
				//break;
			}
		} else {
			if (url.indexOf("_1") > 0) {
				var url1 = url.substring(0, url.indexOf("_1"));
				var url2 = url.substring(url.indexOf("_1") + 2, imgs[i].src.length);
				var new_url = url1 + url2;
				imgs[i].src = new_url;
			}
		}
	}
}

//鼠标放在manue
function over_manueIMG(obj) {
	var comp_nav_div = $("#navDiv");
	var imgs = $("img", comp_nav_div);
	for (var i = 0; i < imgs.length; i++) {
		var url = imgs[i].src;
		if (obj == imgs[i]) {
			if (url.indexOf("_1") < 0) {
				var url1 = url.substring(0, imgs[i].src.length - 10);
				var url2 = url.substring(imgs[i].src.length - 10, imgs[i].src.length);
				var new_url = url1 + "_1" + url2;
				imgs[i].src = new_url;
			}
		} else {
			if (url.indexOf("_1") > 0) {
				var url1 = url.substring(0, url.indexOf("_1"));
				var url2 = url.substring(url.indexOf("_1") + 2, imgs[i].src.length);
				var new_url = url1 + url2;
				imgs[i].src = new_url;
			}
		}
	}
}
//初始化bottom
function initFooter(){
	/*var top = window.innerHeight-document.body.clientHeight;
	document.getElementById("bottom_bottomLine").style.top=top+"px";*/
	
	if("zh_CN"==i18n.checkLocale()){
		document.write("<center>© 2012海辉软件（国际）有限公司版权所有。   服务电话：+86 010 59875864  服务邮箱：hscloud@hisoft.com</center>");
	}else{
		document.write("<center>© 2012 hiSoft Technology International Limited. All rights reserved.TEL:+86 010 59875864  MAIL:hscloud@hisoft.com</center>");
	}

	
}
var urlParams = {};
(function() {
	var match, pl = /\+/g, // Regex for replacing addition symbol with a space
	search = /([^&=]+)=?([^&]*)/g, decode = function(s) {
		return decodeURIComponent(s.replace(pl, " "));
	}, query = window.location.search.substring(1);

	while ( match = search.exec(query))
	urlParams[decode(match[1])] = decode(match[2]);
})(); 
//校验统一样式：result为boolean值
function verifyStyle(result,spanID,msg){
	if(!result){
		$('#'+spanID).attr("style",'color:red;font-size:12px');
		$('#'+spanID).html("<img src='../../images/shared/error.png'/>&nbsp;"+msg);
	}else{
		$('#'+spanID).html("<img src='../../images/shared/ok.png'/>");
	}
}
//jquery提示alter统一样式
function customizeAlert(msg){
	$('#dialog-message-alert').text(msg);
	var dialog_buttons = {
			
			"OK": function() {
				$(this).dialog("destroy");
				}
			};
	if(i18n.checkLocale() != 'en_US'){
		var dialog_buttons = {
			
			"确认": function() {
				$(this).dialog("destroy");
				}
			};
	}
	
	$('#dialog-alert').dialog({
		width: 400,
		resizable: false,
		title : lang.vpdc.center.notice,
		modal: true,
		buttons: dialog_buttons
	});
}

function hsAlert(div,msg,callback){
	
	//$("#"+div).append("<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span><span style=\"align:center;\"></span></p>");
	$("#"+div).empty();
	$("#"+div).append("<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span><span ></span></p>");
	$("p span:last",$("#"+div)).text(msg);
	var dialog_buttons = {
			
			"OK": function() {
				$(this).dialog("destroy");
		        if(null !== callback){
		        	callback();
		        }
			  }
			};
	if(i18n.checkLocale() != 'en_US'){
		var dialog_buttons = {
			"确认": function() {
				$(this).dialog("destroy");
			        if(null !== callback){
			        	callback();
			        }
			   }
			};
	}
	
	$("#"+div).dialog({
		width: 320,
		height: 50,
		title:lang.hscommon.alert,
		resizable: false,
		modal: true,
		buttons: dialog_buttons,
		close:function(){
			$("#"+div).empty();
	        if(null !== callback){
	        	callback();
	        }
		}
	});
	
}

function hsCue(div,msg,callback){
	
	//$("#"+div).append("<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span><span style=\"align:center;\"></span></p>");
	$("#"+div).empty();
	$("#"+div).append("<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span><span ></span></p>");
	$("p span:last",$("#"+div)).text(msg);
	var dialog_buttons = {
			
			"OK": function() {
				$(this).dialog("destroy");
		        if(null !== callback){
		        	callback();
		        }
			  }
			};
	if(i18n.checkLocale() != 'en_US'){
		var dialog_buttons = {
			"确认": function() {
				$(this).dialog("destroy");
			        if(null !== callback){
			        	callback();
			        }
			   }
			};
	}
	
	$("#"+div).dialog({
		width: 320,
		height: 50,
		title:lang.hscommon.cue,
		resizable: false,
		modal: true,
		buttons: dialog_buttons,
		close:function(){
			$("#"+div).empty();
	        if(null !== callback){
	        	callback();
	        }
		}
	});
	
}



function hsConfirm(div,width,height,msg,param,callback){
	if(0==width || null == width){
		width=320;
	}
	if(0==height || null == height){			   
		height=50;
	}
	$("#"+div).append("<span  style=\"float: left; margin: 0 7px 20px 0;\"></span><br><span> <table><tr><td style=\"text-align: center;\">"+msg+"</td></tr></table></span></p>");
	var buttons_ = {
			'Ok' : function() {
				$(this).dialog("close");
				$("#"+div).empty();
				if(null != callback){
					callback(param);
				}
			},
			'Cancel' : function() {
				$("#"+div).empty();
				// 关闭当前Dialog
				$(this).dialog("close");
			}
	};
	if(i18n.checkLocale() == 'zh_CN'){
		buttons_ = {
				'确定' : function() {
					$(this).dialog("close");
					$("#"+div).empty();
					if(null != callback){
						callback(param);
					}
				},
				'取消' : function() {
					$("#"+div).empty();
					// 关闭当前Dialog
					$(this).dialog("close");
				}
		};
	};
	$("#"+div).dialog({
		autoOpen : true,
		modal : true,
		width : width,
		height:height,
		title: lang.hscommon.confirm,
		overlay : {
			opacity : 0.5,
			background : "black",
			overflow : 'auto'
		},
		buttons : buttons_,
		close:function(){
			$("#"+div).empty();
		}
	});
}

function hsSubDia(div,width,height,title,$html,val,param,callback){
	
	var $div=$("#"+div);
	$html.appendTo($div);
	if(0==width || null == width){
		width=450;
	}
	if(0==height || null == height){			   
		height=50;
	}
	var buttons_ = {
			'Ok' : function() {
				if(null != val && val()){
					var p = null;
					if(null != param&&""!=param){
						 p = param();
					}
					$(this).dialog("close");
					$div.empty();
					if(null != callback){
						callback(p);
					}
					
				}
				
			},
			'Cancel' : function() {
				// 关闭当前Dialog
				$(this).dialog("close");
				$div.empty();
			}
	};
	if(i18n.checkLocale() == 'zh_CN'){
		buttons_ = {
				'确定' : function() {
					if(null != val && val()){
						var p = null;
						if(null != param&&""!=param){
							 p = param();
						}
						$(this).dialog("close");
						$div.empty();
						if(null != callback){
							callback(p);
						}
					}
					
				},
				'取消' : function() {
					// 关闭当前Dialog
					$(this).dialog("close");
					$div.empty();
				}
		};
	};
	$("#"+div).dialog({
		autoOpen : true,
		modal : true,
		width : width,
		height:height,
		title:title,
		overlay : {
			opacity : 0.5,
			background : "black",
			overflow : 'auto'
		},
		buttons : buttons_,
		close:function(){
			$div.empty();
		}
	});
	
}
function customizeConfirm(msg,confrimFunc,paramArr){
	var flag = false;
	$("#dialog-message-confirm").text(msg);
	var buttons_ = {
			'Ok' : function() {
				if(paramArr!=null){
					flag = confrimFunc(paramArr);
				}else{
					flag = confrimFunc();
				}
				if(flag){
					$(this).dialog("close");
				}
			},
			'Cancel' : function() {
				// 关闭当前Dialog
				$(this).dialog("close");
			}
	};
	if(i18n.checkLocale() == 'zh_CN'){
		buttons_ = {
				'确定' : function() {
					if(paramArr!=null){
						flag =  confrimFunc(paramArr);
					}else{
						flag =  confrimFunc();
					}
					if(flag){
						$(this).dialog("close");
					}
				},
				'取消' : function() {
					// 关闭当前Dialog
					$(this).dialog("close");
				}
		};
	};
	$("#dialog-confirm").dialog({
		autoOpen : true,
		modal : true,
		width : 400,
		title : lang.vpdc.center.notice,
		overlay : {
			opacity : 0.5,
			background : "black",
			overflow : 'auto'
		},
		buttons : buttons_
	});
}

function obj2Str(obj){ 
	 switch(typeof(obj)){ 
	    case 'object': 
		     var ret = []; 
		     if (obj instanceof Array){ 
			      for (var i = 0, len = obj.length; i < len; i++){ 
			       	ret.push(obj2Str(obj[i])); 
			      } 
			      return '[' + ret.join(',') + ']'; 
		     }else if (obj instanceof RegExp){ 
		    	 return obj.toString(); 
		     }else{ 
			      for (var a in obj){ 
			    	  if((obj2Str(obj[a])).indexOf('"') != -1){
			    		  ret.push('"'+a+'":' + obj2Str(obj[a]));
			    	  }else if(a == "price" || a == "quantity" || a == "serviceLevelId"){
			    		  ret.push('"'+a+'":' + obj2Str(obj[a]));
			    	  }else{
			    		  ret.push('"'+a+'":"' + obj2Str(obj[a])+'"');
			    	  }
			      } 
		      	  return '{' + ret.join(',') + '}'; 
		     } 
	     case 'function': 
	    	 return 'function() {}'; 
	     case 'number': 
	    	 return obj.toString(); 
	     case 'string': 
	    	 return "\"" + obj.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function(a) {return ("\n"==a)?"\\n":("\r"==a)?"\\r":("\t"==a)?"\\t":"";}) + "\""; 
	     case 'boolean': 
	    	 return obj.toString(); 
	     default: 
	    	 return obj.toString(); 
	 } 
 }
function getBrowser(){
	return navigator.userAgent;
}

function checkedBrowser(){

	window["MzBrowser"]={};
	(function()
		{
			if(MzBrowser.platform) return;
			var ua = window.navigator.userAgent.toUpperCase();
			MzBrowser.platform = window.navigator.platform;
			MzBrowser.firefox = ua.indexOf("FIREFOX")>0;
			MzBrowser.opera = typeof(window.opera)=="OBJECT";
			MzBrowser.ie = !MzBrowser.opera && ua.indexOf("MSIE")>0;
			MzBrowser.mozilla = window.navigator.product == "GECKO";
			MzBrowser.netscape= window.navigator.vendor=="NETSCAPE";
			MzBrowser.safari= ua.indexOf("SAFARI")>-1;
			MzBrowser.chrome = ua.indexOf("CHROME") !== -1;
		
			if(MzBrowser.ie) 
			    var b = /MSIE( )(\d+(\.\d+)?)/;
			else if(MzBrowser.firefox) 
				var b = /Firefox(\s|\/)(\d+(\.\d+)?)/;
			else if(MzBrowser.opera) 
				var b = /Opera(\s|\/)(\d+(\.\d+)?)/;
			else if(MzBrowser.netscape) 
				var b = /Netscape(\s|\/)(\d+(\.\d+)?)/;
			else if(MzBrowser.safari) 
				var b = /Version(\/)(\d+(\.\d+)?)/;
			else if(MzBrowser.mozilla) 
				var b = /rv(\:)(\d+(\.\d+)?)/;
			else if(MzBrowser.chrome)  
				var b =  /chrome\/([\d.]+)/; 
			if("undefined"!=typeof(b)&&b.test(ua))
			   MzBrowser.version = parseFloat(RegExp.$2);
			   testBrowser();
		})(); 
		function testBrowser()
		{
			if(!MzBrowser.ie && !MzBrowser.firefox && !MzBrowser.chrome){
				alert("为了你更好的体验该产品，请使用 IE Firefor Chrome浏览器。");
				return false;
			}
			if(MzBrowser.ie && MzBrowser.version<9)
			{
				alert("你使用的是ie内核浏览器，为了你更好的体验该产品，请升级到IE8以上版本。");
			}
		}
	
}
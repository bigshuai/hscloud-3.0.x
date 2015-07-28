var rootPath = path.substring(0, path.substr(1).indexOf('/') + 1);

//设置cookie
function setCookie(name,value){
	$.cookie(name,value,{expires:7,path:'/'});
}

//获取cookie
function getCookie(name){
	return $.cookie(name);
}

function getLang(){
	var lang=getCookie("lang");
	if(lang){
		return lang;
	}else{
		return defaultLang;
	}
}

//加载js
function initJs(jsUrl){
	var jsObj=document.createElement("script");
	jsObj.setAttribute("type", "text/javascript");
	jsObj.setAttribute("src", rootPath+'/skin/'+customerPath+'/js/'+jsUrl+'');
	document.getElementsByTagName("head")[0].appendChild(jsObj);
}

//加载css
function initCss(cssUrl){
	var cssObj=document.createElement("link");
	cssObj.setAttribute("rel", "stylesheet");
	cssObj.setAttribute("type", "text/css");
	cssObj.setAttribute("href", rootPath+'/skin/'+customerPath+'/css/'+cssUrl+'');
	document.getElementsByTagName("head")[0].appendChild(cssObj);
}

//加载dialog的css和Js
function initDialog() {
	var cssObj=document.createElement("link");
	cssObj.setAttribute("rel", "stylesheet");
	cssObj.setAttribute("type", "text/css");
	cssObj.setAttribute("href", rootPath+'/skin/'+customerPath+'/js/common/dialog/dialog.css');
	document.getElementsByTagName("head")[0].appendChild(cssObj);
	var jsObj=document.createElement("script");
	jsObj.setAttribute("type", "text/javascript");
	jsObj.setAttribute("src", rootPath+'/skin/'+customerPath+'/js/common/dialog/dialog.js');
	document.getElementsByTagName("head")[0].appendChild(jsObj);
}

//加载网站图标
function initShortcut(){
	var shortcutObj=document.createElement("link");
	shortcutObj.setAttribute("rel", "shortcut icon");
	shortcutObj.setAttribute("href", rootPath+'/skin/'+customerPath+'/images/common/favicon.ico');
	document.getElementsByTagName("head")[0].appendChild(shortcutObj);
}

//国际化中文
function zh(){
	i18n.changeLang('zh_CN');
}

//国际化English
function en(){
	i18n.changeLang('en_US');
}

//初始化行如<ul><li><a></a></li></ul>
//ULId ul的id
//LiTextArr 数组
//selectClass 选中的class
//selectIndex选中的li的index
//pageUrlArr单击跳转URL的数组
//ULWidth 用于表示是否需要固定宽度
function initULLIElement(ULId,LITextArr,selectClass,selectIndex,pageUrlArr,type,ULWidth){
	var pageUrlsNum=pageUrlArr.length; 
	var LINum=LITextArr.length;
	var selectIndexLocal=0
	if(selectIndex){
		selectIndexLocal=selectIndex;
	}
	var to_append_str='<ul id="'+ULId+'">';
	var localLIWidth='';
	if(ULWidth){
		var LIWidth=parseInt(ULWidth/LITextArr.length);
		localLIWidth=' style="width:'+LIWidth+'px"';
		to_append_str='<ul id="'+ULId+'"'+'style="width:'+ULWidth+'px"'+'>';
	}
	for(var i=0;i<LINum;i++){
		var selectLI='';
		var goToStr=' goTo="'+pageUrlArr[i]+'"';
		var liImage='';
		if(type==2){
			goToStr=' goTo="'+pageUrlArr[i]+'?LIIndex='+i+'" ';
		}
		if(type==1){
			var liImage='<img src="'+rootPath + '/skin/' + customerPath + '/images/common/'+LITextArr[i]+'.png"/>';
		}
		
		if(i==selectIndexLocal){
			selectLI=' class="'+selectClass+'" ';
			if(type==1){
				var liImage='<img src="'+rootPath + '/skin/' + customerPath + '/images/common/'+LITextArr[i]+'_.png"/>';
			}
		}
		to_append_str+="<li"+selectLI+goToStr+localLIWidth+
		">"+liImage+"<a onClick=\"selectLI(this)\" href='javascript:void(0)'>"+
		i18n.get(LITextArr[i])+"</a></li>";
	}
	to_append_str+="</ul>"
		
  return to_append_str;
}

//单击标签触发函数，跳转到响应的页面
function selectLI(selfObj) {
	// 操作标签
	var goTo=$(selfObj).parent().attr("goTo");
	window.location.href=goTo;
}

/*-----------------------------标签菜单功能开始-----------------------------*/
function initTagAndMenu(tagNameArr,tagPageUrls){
	var pageUrl=window.location.href;
	var pageUrlSplit=pageUrl.split("?");
	var selectIndex=0;
	if(pageUrlSplit.length>1){
		var paramsAndValue=pageUrlSplit[1].split("=");
		if(paramsAndValue.length>1){
			selectIndex=paramsAndValue[1];
		}
	}
	
	//拿tagNameArr第一个元素来标示menu用于页面跳转定位当前menu
	var menuSelectIndex=0;
	var menuSelectIndexTmp=tagNameArr.shift();
	if(menuSelectIndexTmp){
		menuSelectIndex=menuSelectIndexTmp
	}
	//初始化tag html 
	var tagDisplayStr=initULLIElement("tag_ul",tagNameArr,'selectTag',selectIndex,tagPageUrls,2);
	$("#tab").append(tagDisplayStr)
	//初始化menu html
	
	var menuDisplayStr=initULLIElement("menu_ul",menuNameArr,'selectMenu',menuSelectIndex,menuPageUrls,1,menuWidth);
	
	$("#menuDIV").append(menuDisplayStr);
	
	$("#menu_ul li").mouseover(function(){
		if($(this).attr("class")=="selectMenu"){
			return;
		}
		var imgDom=$(this).children("img");
		var srcStr=imgDom.attr("src");
		var subSrcStr=srcStr.substring(0,srcStr.length-4);
		imgDom.attr("src",subSrcStr+"_.png");
	})
	$("#menu_ul li").mouseout(function(){
		if($(this).attr("class")=="selectMenu"){
			return;
		}
		var imgDom=$(this).children("img");
		var srcStr=imgDom.attr("src");
		var subSrcStr=srcStr.substring(0,srcStr.length-5);
		imgDom.attr("src",subSrcStr+".png");
	})
	
}

/*-----------------------------标签菜单功能结束-----------------------------*/

//校验提示信息
function verifyStyle(result, spanID, msg) {
	if (!result) {
		$('#' + spanID).attr("style", 'color:red;font-size:12px');
		$('#' + spanID).html("<img src='" + rootPath + "/skin/" + customerPath + "/images/common/error.png'/>&nbsp;" + msg);
	} 
	else {
		$('#' + spanID).html("<img src='" + rootPath + "/skin/" + customerPath + "/images/common/ok.png'/>");
	}
}

/*-----------------------------时间格式转换 start-----------------------------*/
function formatDateTime(date){
	if("Invalid Date" == date){
		return "";
	}
//	if (getLang() == 'zh_CN') {
//		return date.format("yyyy年MM月dd日 hh时mm分ss秒");
//	}
	return date.format("yyyy-MM-dd hh:mm:ss");
}
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

/*-----------------------------时间格式转换 end-----------------------------*/

/**
 * ajax提交 返回json格式数据
 * @param url 请求地址
 * @param async 是否异步（异步：true,同步:false），默认为同步。
 * @param param 请求参数
 * @param data 请求返回数据
 * @param Callback 请求返回执行js
 */
function ajaxPost(url, async, param, callback, loading_flag, img_src) {
	if ("" == async || null == async) {
		async = false;
	}
	$.ajax({
		url : url,
		type : 'POST',
		dataType : 'json',
		async : async,
		beforeSend : function() {
			if (loading_flag) {
				var path = window.document.location.pathname;
				var _root = path.substring(0, path.substr(1).indexOf('/') + 1);				
				var img_src_str = _root + "/images/laoding.gif";
				if (img_src) {
					img_src_str = img_src;
				}
				$(".tagContent").append("<div class='load_overlay'></div><div class='load_content'>"
								+ "<img src='" + img_src_str + "'/><div class='loading_text'>"
								+ get("loadingPrompt") + "</div></div>");
			}
		},
		data : param,
		complete : function() {
			if (loading_flag) {
				$(".load_overlay").remove();
				$(".load_content").remove();
			}
		},
		success : function(data) {
			if (!data["success"]) {
//				var _code = top._error[data["resultCode"]];
//				if (typeof (_code) != "undefined" && typeof (_code) == "string") {
//					top.window.location.href = top._root + _code;
//				}
				return;
			}
			if (null != callback) {
				callback(data);
			}
		}
	});
};

var vm_status_buss = {
		'TRYWAIT'  : '试用待审核',
		'TRY' : '试用中',
		'DELAYWAIT'  : '延期待审核',
		'DELAY' : '已延期',
		'REGULAR'  : '转正',
		'CANCEL'  : '已取消',
		'NULL' : '无',
		'EXPIRE_TRY' : '试用中(已到期)',
		'EXPIRE_DELAY' : '已延期(已到期)',
		'EXPIRE' : '已到期',
	i18n : function (key){
		if("zh_CN"==getLang()){
			return vm_status_buss[key];
		}else{
			return key;
		}
	}
};

function copyArray(sArr,dArr){
	if(dArr){
		for(var i=0;i<dArr.length;i++){
			dArr[i]=sArr[i];
		}
		return dArr;
	}else{
		localArr=new Array();
		if(sArr&&sArr.length>0){
			for(var i=0;i<sArr.length;i++){
				localArr.push(sArr[i]);
			}
			
		}
		return localArr;
	}
}

/*---------------------------站内公告start---------------------------------*/

function AnnouncementInner(){
	var cookieUpdateTime = getCookie("innerAnnouncementUpdateTime");
	var currentUpdateTime = 0;
	$.ajax({
		url:'../../../message/announcement!showInnerAnnouncement.action',
		type:'GET',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		async:false,
		data:{'domainCode':domainCode},
		success:function(data) {
			if (data.resultObject != null && data.resultObject !="") {
				var announcementArray = data.resultObject;
				var html = '<div><div class = "announcement_num" style="font-weight: bold;"><image src= "../../../skin/'+customerPath+'/images/common/notice_icon.png" style="vertical-align: text-top;"/>'+' '+ i18n.get("now") +announcementArray.length +i18n.get("announcementUnit")+i18n.get("siteNotice")+'</div>'
		                 + '<div style="height:210px;overflow-y:auto;">';
				for(var i=0;i<announcementArray.length;i++){
					var content = announcementArray[i].content.replace('\n','');
					var title = announcementArray[i].title;
					var num=(i+1).toString();
					html +='<div class = "announcemnet_title"><span>'+i18n.get("Notice")+i18n.get(num)+':'+title +'</span></div>';
					html +='<div class = "announcemnet_content"><span >'+ content +'</span></div>';
					if (currentUpdateTime < announcementArray[i].updateTime) {
						currentUpdateTime = announcementArray[i].updateTime;
					}
				}				
				html += '</div></div>';
				if (cookieUpdateTime == null || cookieUpdateTime != currentUpdateTime){
					openDialog(html,i18n.get("siteNotice"),300,600);
					setCookie('innerAnnouncementUpdateTime',currentUpdateTime);
				}				
			}
		}
	}); 
}
/*---------------------------站内公告end---------------------------------*/

//乘法
function floatMulti(arg1,arg2){
   var precision1=getPrecision(arg1);
   var precision2=getPrecision(arg2);
   var tempPrecision=0;
   
   tempPrecision+=precision1;
   tempPrecision+=precision2;
   var int1=getIntFromFloat(arg1);
   var int2=getIntFromFloat(arg2);
   
   var result = int1 * int2 + '';
   if(result.length > tempPrecision) {
	   var length = result.length - tempPrecision;
	   result = result.substring(0, length) + '.' + result.substring(length);
   } else {
	   var temp = '0.';
	   for(var i = result.length; i < tempPrecision; i++) {
		   temp = temp + '0';
		}
	   temp = temp + result;
	   result = temp;
   }
   return parseFloat(result); 
}

//加法
function floatAdd(arg1,arg2){
   var precision1=getPrecision(arg1);
   var precision2=getPrecision(arg2);
   var temp=Math.pow(10,Math.max(precision1,precision2));
   
   var result = (floatMulti(arg1,temp)+floatMulti(arg2,temp)) + '';
   var tempPrecision = temp.toString().length - 1;
   if(result.length > tempPrecision) {
	   var length = result.length - tempPrecision;
	   result = result.substring(0, length) + '.' + result.substring(length);
   } else {
	   var temp = '0.';
	   for(var i = result.length; i < tempPrecision; i++) {
		   temp = temp + '0';
		}
	   temp = temp + result;
	   result = temp;
   }
   return parseFloat(result); 
}

function getPrecision(arg){
   if(arg.toString().indexOf(".")==-1){
      return 0;
   }else{
      return arg.toString().split(".")[1].length;
   }
}

function getIntFromFloat(arg){
   if(arg.toString().indexOf(".")==-1){
      return arg;
   }else{
      return Number(arg.toString().replace(".",""));
   }
}

/******************************* VNC加密算法: begin **************************************/
var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                  -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
                                  58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0,  1,  2,  3,  4,  5,  6,
                                  7,  8,  9,  10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
                                  25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
                                  37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
                                  -1, -1);
function encode(str) {
    var out, i, len;
    var c1, c2, c3;
    len = str.length;
    i = 0;
    out = "";

    while (i < len) {
        c1 = str.charCodeAt(i++) & 0xff;
        if (i == len) {
            out += base64EncodeChars.charAt(c1 >> 2);
            out += base64EncodeChars.charAt((c1 & 0x3) << 4);
            out += "==";
            break;
        }
        c2 = str.charCodeAt(i++);
        if (i == len) {
            out += base64EncodeChars.charAt(c1 >> 2);
            out += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
            out += base64EncodeChars.charAt((c2 & 0xF) << 2);
            out += "=";
            break;
        }
        c3 = str.charCodeAt(i++);
        out += base64EncodeChars.charAt(c1 >> 2);
        out += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
        out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
        out += base64EncodeChars.charAt(c3 & 0x3F);
    }
    return out;
}

function decode(str) {
    var c1, c2, c3, c4;
    var i, len, out;
    len = str.length;
    i = 0;
    out = "";

    while (i < len) {
        do {
            c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
        } while (i < len && c1 == -1);
        if (c1 == -1)
            break;
        do {
            c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
        } while (i < len && c2 == -1);
        if (c2 == -1)
            break;
        out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
        do {
            c3 = str.charCodeAt(i++) & 0xff;
            if (c3 == 61)
                return out;
            c3 = base64DecodeChars[c3];
        } while (i < len && c3 == -1);
        if (c3 == -1)
            break;
        out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
        do {
            c4 = str.charCodeAt(i++) & 0xff;
            if (c4 == 61)
                return out;
            c4 = base64DecodeChars[c4];
        } while (i < len && c4 == -1);
        if (c4 == -1)
            break;
        out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
    }
    return out;
}
/*********************************  VNC加密算法: end   *********************************/
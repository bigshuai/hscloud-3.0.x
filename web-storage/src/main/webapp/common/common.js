var currentLanguage="zh_CN";
function getCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return unescape(arr[2]);
	 return null;
};
function setCookie(value){
    document.cookie = "lang="+ value;
};
function init_lang(value){
	if(value == ""){
		var lang = getCookie("lang");
		if(lang==null||lang=="undefined"){
			var language_other = window.navigator.language;
			var language_ie = window.navigator.browserLanguage;
			if(language_other=="zh-cn"||language_other=="zh-CN"||language_other=="zh_cn"||language_other=="zh_CN"){
				language_other="zh_CN"
			}

			if(language_ie=="zh-cn"||language_ie=="zh-CN"||language_ie=="zh_cn"||language_ie=="zh_CN"){
				language_ie="zh_CN"
			}

			if(language_other=="en-us"||language_other=="en-US"||language_other=="en_us"||language_other=="en_US"||language_other=="en"){
				language_other="en_US"
			}

			if(language_ie=="en-us"||language_ie=="en-US"||language_ie=="en_us"||language_ie=="en_US"){
				language_ie="en_US"
			}
				
			if(language_ie){
				currentLanguage=language_ie;
				setCookie(language_ie);
			}else if(language_other){
				currentLanguage=language_other;
				setCookie(language_other);
			}
	    }
	 }else{
	    setCookie(value);
	 }
};
init_lang("");
function getSessionUser(){
	Ext.Ajax.request({
		url: '../login!getSessionUser.action',
	    success: function(response){
	    	var obj = Ext.decode(response.responseText);
	    	if(obj.resultObject==null ||obj.resultObject==''){
	    		document.location.href="../index.html";
	    	}
	    	if(obj.success && obj.resultObject.name!=null && obj.resultObject.name!='' ){
	    		document.getElementById('CurrentUser').innerHTML =obj.resultObject.name;
	    		if(obj.resultObject.img != null && obj.resultObject.img != '') {
	    			$("#logoId").attr("background", "../images/head/" + obj.resultObject.img);
	    		}
	    	}else{
	    		document.location.href="../index.html";
	    	}            	        
	    }
	});
};
function userlogout(){
	Ext.Ajax.request({
		url:'../login!logout.action',
		success: function(response){
			if(response.status == 200){
				document.location.href="../index.html";
			}else{
				Ext.MessageBox.show({
					title: i18n._("notice"),
					msg: obj.resultMsg,
					icon:Ext.MessageBox.INFO,
					buttons: Ext.MessageBox.OK
				});
				return;
			}
		}
	});
};

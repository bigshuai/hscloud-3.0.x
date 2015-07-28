var rootPath = path.substring(0, path.substr(1).indexOf('/') + 1);
//加载国际化JS文件
loadI18nResource();
//国际化存储对象
var i18nStorage = window.localStorage;
//语言变量
var lang = null;;
//国际化JS文件内的对象命名
var i18nConfig;
//国际化对象
var i18n = {
		init : function() {
			if(window.localStorage){
				getI18nObj_storage(i18nConfig);
			}else{
				getI18nObj_js();
			}
		},
		get : function(key){
			if(window.localStorage){
				return getI18nValue_storage(key);
			}else{
				return getI18nValue_js(key);
			}
		},
		changeLang : function (lang_) {
			if(window.localStorage){
				i18nStorage.clear();
			}
			setCookie('lang',lang_);
			window.location.reload();
		}
};

//以localStorage形式加载国际化存储对象（用于支持HTML5）
function getI18nObj_storage(obj){
	var i18nVersion = 0;
	for (key in obj) {
		if ('i18nV' == key && obj.hasOwnProperty(key)) {
			i18nVersion = obj[key];
			break;
		}
	}
	if(i18nStorage.length==0 || (i18nVersion > i18nStorage.getItem('i18nV'))){
		for (key in obj) {
			if (obj.hasOwnProperty(key)) {
				i18nStorage.setItem(key,obj[key]);
			}
		}
	}
}	

//以传统JS形式加载国际化存储对象（用于不支持HTML5）
function getI18nObj_js(){
	i18nStorage = i18nConfig;
}

//以localStorage形式获取存储对象某一key的value值（用于支持HTML5）
function getI18nValue_storage(key){
	return i18nStorage.getItem(key);
}

//以JS对象形式获取存储对象某一key的value值（用于不支持HTML5）
function getI18nValue_js(key){
	return i18nStorage[key];
}

//动态加载国际化翻译文件
function loadI18nResource(){
	if(!window.localStorage || window.localStorage.length==0){
		var	lang = defaultLang;
		if(getCookie('lang') != null){
			lang = getCookie('lang');
		}
		document.write("<script src='"+rootPath+"/resources/i18n/"+lang+".js'><\/script>"); 
	}
}



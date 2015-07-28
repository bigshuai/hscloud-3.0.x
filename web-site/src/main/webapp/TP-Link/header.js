

$("document").ready(function(){
	
	var iframeNmae = "@header";
	top._i18n._image(iframeNmae);
	/**
	 * 切换语言
	 */
	$("#zh_CN").click(function(){
		choice("zh_CN");
		refresh("zh_CN");
	});
	$("#en_US").click(function(){
		choice("en_US");
		refresh("en_US");
	});
	
	/**
	 * 退出登录
	 */
	$("#logout").click(function(){
		var url = "../../user_mgmt/user!logout.action";
		ajaxPost(url,
				null,
				null,
				function(data){
			        if(data["success"]){
			        	top.window.location.href = top._root+"/index.html";
			        }
	    		},
				null
		);
	});
});

/**
 * locale 语言
 * @param locale
 */
function choice(locale){
	var clang = parent._i18n._lang;
	if(clang || (!clang && clang != locale)){
		top._i18n._change(locale,$("document"));
	}
}

function refresh(locale){
	$(top.document).contents().find("iframe").each(function(){
		$(this).attr("src",$(this).attr("src"));
	});
}
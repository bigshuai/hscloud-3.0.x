/*
 * noVNC: HTML5 VNC client
 * Copyright (C) 2011 Joel Martin
 * Licensed under LGPL-3 (see LICENSE.txt)
 *
 * See README.md for usage and integration instructions.
 */

/*jslint evil: true */
/*global window, document, INCLUDE_URI */

/*
 * Load supporting scripts
 */
//$("document").ready(load_remoteVNC);
function get_INCLUDE_URI() {
    return (typeof INCLUDE_URI !== "undefined") ? INCLUDE_URI : "include/";
}
//刷新VNC
function refreshVNC(){
	var url = location.href;
	var url_=url.split("=")[0];
	var key =WebUtil.getQueryVar('key');
	$.ajax({
		async:false,
		type : "post",
		url : REMOTE_URL+ "../ops/ops!getVNC.action",
		dataType : 'json',
		success : function(json) {
			if(json.resultObject!=null){
				var url=json.resultObject;
				var token = url.split("=")[1];
				url= url_+"="+token+"&key="+key;
				self.location.href = url;
				//alert(location.href);
				window.location.load();
			}
		},
		error : function() {
			alert("error");
		},
	});
}
//重启VNC
function rebootVNC(){
	if(confirm("是否需要重启当前计算机?")){
		$.ajax({
			async:false,
			type : "post",
			url : REMOTE_URL+ "../ops/ops!rebootVM.action",
			dataType : 'json',
			success : function(json) {
				if(json.success){
					alert("虚拟机重启成功!");
				}else{
					alert(json.resultMsg);
				}
			},
			error : function() {
				alert("error");
			},
		});
	}
}
function load_remoteVNC(){
	/*
	$("#remote_vnc")[0].src = location.href;
	*/
	
	var token=location.href.split("=")[1].substring(0,36);
	var key = decode(WebUtil.getQueryVar('key'));
	var url = "http://"+key+":"+vncURL.port+"/vnc_auto.html?token=";
	$("#remote_vnc")[0].src = url+token;
	$("#remote_vnc")[0].location.reload();
}
(function () {
    "use strict";

    var extra = "", start, end;

    start = "<script src='" + get_INCLUDE_URI();
    end = "'><\/script>";

    // Uncomment to activate firebug lite
    //extra += "<script src='http://getfirebug.com/releases/lite/1.2/" + 
    //         "firebug-lite-compressed.js'><\/script>";

    extra += start + "util.js" + end;
    extra += start + "webutil.js" + end;
    extra += start + "logo.js" + end;
    extra += start + "base64.js" + end;
    extra += start + "websock.js" + end;
    extra += start + "des.js" + end;
    extra += start + "input.js" + end;
    extra += start + "display.js" + end;
    extra += start + "rfb.js" + end;

    document.write(extra);
}());


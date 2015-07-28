$(document).ready(function() {
	initCss('common/common.css');
	initJs('portal/col/header.js');
	initJs('portal/col/footer.js');
	initCss('portal/login/login.css');
	i18n.init();//国际化初始化
	$("#flash1 img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/portal_05.png");
	$("#flash2 img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/portal_04.png");
	$("#flash3 img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/portal_06.png");
	$("#flash4 img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/portal_05.png");
	$("#flash5 img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/portal_06.png");
	$("#mp-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/mp.png");
	$("#vm-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/vm.png");
	$("#mem-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/mem.png");
	$("#dot-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/dot.png");
	$("#dot2-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/dot.png");
	$(".flash_bar div").mouseover(function() {
		stopAm();
	}).mouseout(function() {
		startAm();
	});
	startAm();	
	AnnouncementOuterShow();
	setCookie("innerAnnouncementFlag",null);
});

var currentindex = 1;
$("#flashBg").css("background-color", $("#flash1").attr("name"));
function changeflash(i) {
	currentindex = i;
	for (var j = 1; j <= 5; j++) {//此处的5代表你想要添加的幻灯片的数量与下面的5相呼应
		if (j == i) {
			$("#flash" + j).fadeIn("normal");
			$("#flash" + j).css("display", "block");
			$("#f" + j).removeClass();
			$("#f" + j).addClass("dq");
			$("#flashBg").css("background-color", $("#flash" + j).attr("name"));
		} else {
			$("#flash" + j).css("display", "none");
			$("#f" + j).removeClass();
			$("#f" + j).addClass("no");
		}
	}
};
function startAm(){
	timerID = setInterval("timer_tick()",4000);//8000代表间隔时间设置
};
function stopAm(){
	clearInterval(timerID);
};
function timer_tick() {
    currentindex=currentindex>=5?1:currentindex+1;//此处的5代表幻灯片循环遍历的次数
	changeflash(currentindex);
};

function AnnouncementOuterShow(){
	$.ajax({
		url:'../message/announcement!showOuterAnnouncement.action',
		type:'GET',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		async:false,
		data:{'domainCode':domainCode},
		success:function(data){
			if(data.success == true){
				$('#outer').empty();
				$('#bssDisplay_second').empty();
				var string1 = "";
				string1 += '<div id="bssDisplay_third" class="div_third_class" ><div id="ProductBulletin" style="padding-right:15px;">';
//				string1 += i18n.get("BulletinBoard")+'<img id="mm-img" src="" align="right" style="cursor: pointer;" onclick="AnnouncementOuterShowMore()"/></div><div id="bssDisplay_line" class="div_line_class" >';
				string1 += i18n.get("BulletinBoard")+'<span id="btn_message_more" class="button button-rounded button-primary button-small" onclick="AnnouncementOuterShowMore()" >'+ i18n.get("moreMessage") +'</span></div><div id="bssDisplay_line" class="div_line_class" >';
				string1 +='</div><div id="outer" style="width:455px;height:80px;overflow-y:auto;"></div></div>';
				$("#bssDisplay_second").append(string1);
				if(data.resultObject != null && data.resultObject.length > 0){
					var announcementArray = data.resultObject;
					var string = "";
					for(var i=0;i<announcementArray.length;i++){
						//var content = announcementArray[i].content.replace('\n','');
						var content = announcementArray[i].content;
						var title = announcementArray[i].title;
						var num=(i+1).toString();
						string += '<div style="margin-top:1%;float:left; " ><div class="div_announcement_title_class" >';
						string += '<img id="dot'+ (i+1) +'-img" src=""/></div><div class="div_announcement_content_class" ><span class="span_announcement_class" >'+i18n.get("Notice")+i18n.get(num) +': </span>';
						string += '<span title="'+ content +'">'+ title +'</span></div></div>';
					}
					$('#outer').append(string);
					$('#bssDisplay').show();
					for(var i=0;i<announcementArray.length;i++){
						var num=i+1;
						$("#dot"+num+"-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/dot.png");
					}
				} else {
					var string="";
					string += '<div class="div_announcement_content_class" ><span class="span_announcement_class" >';
					string = i18n.get("noAnnouncement")+'</span></div>';
					$('#outer').append(string);
					$('#bssDisplay').show();
				}
//				$("#mm-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/mm.png");
			}
		}
	});
};
function AnnouncementOuterShowMore(){
	$.ajax({
		url:'../message/announcement!showMoreOuterAnnouncement.action',
		type:'GET',
		dataType:'json',
		contentType:"application/json;charset=UTF-8",
		async:false,
		data:{'domainCode':domainCode},
		success:function(data){
			if(data.success == true){
				$('#outer').empty();
				$('#bssDisplay_second').empty();
				var string1 = "";
				string1 += '<div id="bssDisplay_third" class="div_third_class" ><div id="ProductBulletin" style="padding-right:15px;">';
				string1 += i18n.get("BulletinBoard")+'<span id="btn_message_less" class="button button-rounded button-primary button-small">'+ i18n.get("moreMessage") +'</span></div><div id="bssDisplay_line" class="div_line_class" >';
				string1 +='</div><div id="outer" style="width:455px;height:80px;overflow-y:auto;"></div></div>';
				$("#bssDisplay_second").append(string1);
				if(data.resultObject != null && data.resultObject.length > 0){
					var announcementArray = data.resultObject;
					var string = "";
					for(var i=0;i<announcementArray.length;i++){
						var content = announcementArray[i].content.replace('\n','');
						var title = announcementArray[i].title;
						var num=(i+1).toString();
						string += '<div style="margin-top:1%;float:left; " ><div class="div_announcement_title_class" >';
						string += '<img id="dot'+ (i+1) +'-img" src=""/></div><div class="div_announcement_content_class" ><span class="span_announcement_class" >'+i18n.get("Notice")+i18n.get(num) +': </span>';
						string += '<span title="'+ content +'">'+ title +'</span></div></div>';
					}
					$('#outer').append(string);
					$('#bssDisplay').show();
					for(var i=0;i<announcementArray.length;i++){
						var num=i+1;
						$("#dot"+num+"-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/dot.png");
					}
				} else {
					var string="";
					string += '<div class="div_announcement_content_class" ><span class="span_announcement_class" >';
					string = i18n.get("noAnnouncement")+'</span></div>';
					$('#outer').append(string);
					$('#bssDisplay').show();
				}
				$("#mm-img").attr("src",rootPath+"/skin/"+customerPath+"/images/portal/login/mm.png");
			}
		}
	});
};
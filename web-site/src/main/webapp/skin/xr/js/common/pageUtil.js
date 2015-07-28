var g_start = 1;
var g_end = 10;

var PageObje = function(tableId, buttonId, totalPagesId, groupCount) {
	this.tableId = tableId; // 表id
	this.buttonId = (buttonId == null ? 'templateButton' : buttonId);
	this.totalPagesId = (totalPagesId == null ? 'totalPages' : totalPagesId);
	this.groupCount = (groupCount == null ? 10 : groupCount); // 组页数
	this.pageDivId = 'pageDivId';
	this.total = 1; // 总数
	this.totalPages = 1; // 总页数
	this.current = 1; // 当前页
	this.size = 1; // 每页条数
	this.group = 1; // 组
	this.template1 = '';         //数据模板
	this.methodArray = new Array();  //需要处理方法数组
	this.jsonRequest = {};     //参数对象
	this.initFlag = true;     //初始标记，初始时需要设置页码
	this.titleFlag = true;   //表头标记，初始默认为false
	this.templateButton = ''; //页码模板
	this.pageFirst = function() {
		this.initFlag = true;
		this.current = 1;
		pageChange(this);
		currentPageRendering(this);
	};
	this.pageLast = function() {
		this.current = this.totalPages;
		pageChange(this);
		this.group = parseInt(this.totalPages / this.groupCount);
		if (this.totalPages % this.groupCount != 0) {
			this.group = this.group + 1;
		}
		pagination2(this);
		writePageNum(this);
		this.current = this.totalPages;
		currentPageRendering(this);
	};
	this.pageLeft = function() {
		if(this.current != g_start) {
			pageChangeService(this, this.current - 1);
		} else if(this.current != 1) {
			this.group = this.group - 1;
			pagination2(this);
			this.current = g_end;
			pageChange(this);
			writePageNum(this);
			currentPageRendering(this);
		}
	};
	this.pageRight = function() {
		if(this.current != g_end) {
			pageChangeService(this, this.current + 1);
		} else if(this.current != this.totalPages) { 
			this.group = this.group + 1;
			pagination2(this);
			this.current = g_start;
			pageChange(this);
			writePageNum(this);
			currentPageRendering(this);
		}
	};
};

/**
 * 当模板为div时,使用pageCreatorR1创建
 */
function pageCreatorR1(pageObje, resultObject, titleFlag) {
	var table = $('#' + pageObje.tableId);
	if (pageObje.template1 == '') {
		var str = table.find('#templateId1')[0].outerHTML;
		var array = str.match(/\_\([a-zA-Z0-9-_]*\)+/gi);
		if(array != null) {
			for(var i = 0; i < array.length; i++) {
				var temp = array[i].substring(2, array[i].length - 1);
				str = str.replace(new RegExp('_\\(' + temp + '\\)', 'gm'), temp);
			}
		}
		pageObje.template1 = str;
	}
	table.find("div").remove();
	table.show();
	var array = resultObject.result;
	var column = pageObje.column;
	for ( var i = 0; i < array.length; i++) {
		var row = pageObje.template1;
		for ( var j = 0; j < column.length; j++) {
			var p_column = '';
			if(column.join("").indexOf('.')!= -1) {
				var cols = column.join("").split(".");
				p_column = array[i][cols[0]][cols[1]];
			} else {
				p_column = array[i][column[j]];
			}
			var methodArray = pageObje.methodArray;
			for ( var index_method = 0; index_method < methodArray.length; index_method++) {
				if (methodArray[index_method][0] == column[j]) {
					p_column = eval(methodArray[index_method][1])(p_column, array[i]);
				}
			}
			row = row.replace(new RegExp('\\$' + column[j] + '\\$', 'gm'), p_column);
		}
		row = row.replace('$indexPage$', (pageObje.current - 1) * pageObje.size + i + 1);
		table.append(row);
	}
	if(pageObje.initFlag == true) {
		pageObje.current = resultObject.pageNo;
		pageObje.total = resultObject.totalCount;
		pageObje.totalPages = resultObject.totalPages;
		
		pageObje.group = 1;
		pagination2(pageObje);
		writePageNum(pageObje);
		currentPageRendering(pageObje);
		pageObje.initFlag = false;
	}
}

/**
 * 当模板为table时,使用pageCreatorR2创建
 */
function pageCreatorR2(pageObje, resultObject, titleFlag) {
	var table = $('#' + pageObje.tableId);
	if (pageObje.template1 == '') {
		var str = table.find('#templateId1')[0].outerHTML;
		var array = str.match(/\_\([a-zA-Z0-9-_]*\)+/gi);
		if(array != null) {
			for(var i = 0; i < array.length; i++) {
				var temp = array[i].substring(2, array[i].length - 1);
				str = str.replace(new RegExp('_\\(' + temp + '\\)', 'gm'), temp);
			}
		}
		pageObje.template1 = str;
	}
	if (titleFlag == true || pageObje.titleFlag == true) {
		table.find("tr:not(:first)").remove();
	} else {
		table.find("tr").remove();
	}
	table.show();
	var array = resultObject.result;
	var column = pageObje.column;
	for ( var i = 0; i < array.length; i++) {
		var row = pageObje.template1;
		for ( var j = 0; j < column.length; j++) {
			var p_column = '';
			if(column.join("").indexOf('.') != -1) {
				var cols = column.join("").split(".");
				p_column = array[i][cols[0]][cols[1]];
			} else {
				p_column = array[i][column[j]];
			}
			var methodArray = pageObje.methodArray;
			for ( var index_method = 0; index_method < methodArray.length; index_method++) {
				if (methodArray[index_method][0] == column[j]) {
					p_column = eval(methodArray[index_method][1])(p_column, array[i]);
				}
			}
			row = row.replace(new RegExp('\\$' + column[j] + '\\$', 'gm'), p_column);
		}
		row = row.replace('$indexPage$', (pageObje.current - 1) * pageObje.size + i + 1);
		table.append(row);
	}
	if(pageObje.initFlag == true) {

		pageObje.current = resultObject.pageNo;
		pageObje.total = resultObject.totalCount;
		pageObje.totalPages = resultObject.totalPages;
		
		pageObje.group = 1;
		pagination2(pageObje);
		writePageNum(pageObje);
		currentPageRendering(pageObje);
		pageObje.initFlag = false;
	}
}

function pageChangeService(pageObje, current) {
	pageObje.current = current;
	pageChange(pageObje);
	currentPageRendering(pageObje);
}

function pagination2(pageObje) {

	var group = pageObje.group;
	if (group < 1) {
		group = 1;
	}
	//g_start = (group - 1) * pageObje.groupCount + 1;
	g_end = group * pageObje.groupCount;

	var maxGroup = parseInt(parseInt(pageObje.total) / pageObje.size);
	maxGroup = maxGroup + 1;
	var remainder = parseInt(pageObje.total) % pageObje.size;
	if (remainder == 0) {
		maxGroup = maxGroup - 1;
	}

	if (g_end > maxGroup) {
		g_end = maxGroup;
		group = parseInt(maxGroup / pageObje.groupCount);
		var temp = parseInt(maxGroup % pageObje.groupCount);
		if (temp != 0) {
			group = group + 1;
		}
	}
	
	g_start = (group - 1) * pageObje.groupCount + 1;
	if(g_end <= pageObje.groupCount) {
		g_start = 1;
	}
	
	pageObje.group = group;
//	pageObje.current = g_start;
}

function writePageNum(obj) {
	var divObj = $('#' + obj.buttonId);
	if (obj.templateButton == '') {
		obj.templateButton = divObj.html();
	}
	divObj.html('');
	for ( var i = g_start; i <= g_end; i++) {
		divObj.append(obj.templateButton.replace(new RegExp('\\$pageNum\\$', 'gm'), i));
	}
}

function currentPageRendering(obj) {
	  //当当前页面记录为0时跳到下一页  
	if(obj.current!=1){
		if((obj.current-1)*obj.size==obj.total){   
			obj.current=obj.current-1;   
			pageChangeService(obj,obj.current );    
		}
		
	}
	
	$('#' + obj.buttonId).find('.page_select').removeClass().addClass('page');
	$('#' + obj.buttonId).find('.page').each(function(index) {
		var objTemp = $(this);
		if(objTemp.text() == obj.current) {
			objTemp.removeClass();
			objTemp.addClass('page_select');
		}
	});
	if (getCookie("lang") == "zh_CN" || getCookie("lang") == null) {
		$('#' + obj.totalPagesId).text('共' + obj.total + '条记录　' + obj.current + '/' + obj.totalPages + '页');
	} else {
		$('#' + obj.totalPagesId).text(obj.total + ' records in total. Page' + obj.current + '/' + obj.totalPages);
	}
	$('#' + obj.pageDivId).show();
}
/************************/
var params = getCookie("lang");
i18n.set({
  lang: params, 
  path: '../resources'
});
var v_mask = null;
Ext.Ajax.timeout=180000;
Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', '../extjs-4.2/ux');
Ext.require([
             'Ext.data.*',
             'Ext.panel.Panel',
             'Ext.view.View',
             'Ext.layout.container.Fit',
             'Ext.toolbar.Paging',
             'Ext.ux.form.SearchField'
]);
function changeGrid(condition) {
	Ext.getCmp('mainPanel').removeAll(false);
	if(condition == 'operationLog') {
		startDate.setValue('');
		endDate.setValue('');
		moduleCombo.setValue('');
		Ext.getCmp('vmSearchfield').setValue('');
		
		
		var proxy=operationLogStore.getProxy();
        proxy.setExtraParam('startDate', oStartDate.getValue());
        proxy.setExtraParam('endDate', oEndDate.getValue());
        proxy.setExtraParam('query', '');
        operationLogStore.loadPage(1,null);
		Ext.getCmp('mainPanel').add(operationLogGrid);
	} else {
		oStartDate.setValue('');
		oEndDate.setValue('');
		Ext.getCmp('operationLogSearch').setValue('');
		
		var proxy=storageStore.getProxy();
        proxy.setExtraParam('startDate', startDate.getValue());
        proxy.setExtraParam('endDate', endDate.getValue());
        proxy.setExtraParam('query', '');
        proxy.setExtraParam('module', moduleCombo.getValue());
        storageStore.loadPage(1,null);
		Ext.getCmp('mainPanel').add(storageGrid);
	}
	
}

//Ext.QuickTips.init();
var aitem1 = Ext.create('Ext.panel.Panel',{
	id:"aitem1",
	margin:'10 0 0 0',
	header:{
		title:"<center ><a href='javascript:void(0)' onclick='changeGrid(\"storage\");'  class='titleCss'><span id='m1' style='width:100%;color:#FFFFFF;font-size:12;font-weight:bold'>"+i18n._('StorageManagement')+"</span></a></center>"
	},
	layout:'fit',
	bodyStyle: {
		background: '#bbe2ff'
	}
});
var aitem2 = Ext.create('Ext.panel.Panel',{
	id:"aitem2",
	margin:'10 0 0 0',
	header:{
		title:"<center ><a href='javascript:void(0)' onclick='changeGrid(\"operationLog\");'  class='titleCss'>" +
				"<span id='m1' style='width:100%;color:#FFFFFF;font-size:12;font-weight:bold'>"+i18n._('operationLog')+"</span></a></center>"
	},
	layout:'fit',
	bodyStyle: {
		background: '#bbe2ff'
	}
});
var storageModel=Ext.define('storageModel',{
	extend:'Ext.data.Model',
	fields:[
	   {name:'id' ,type:'string'},
	   {name:'lengthStatus', type:'string'},   
	   {name:'channel', type:'string'},
	   {name:'planStatus', type:'string'},
	   {name:'date', type:'string'},
	   {name:'time', type:'string'},
	   {name:'dateShow',mapping: 'dateShow', type: 'date', dateFormat: 'time'},
	   {name:'device', type:'string'},
	   {name:'timestamp', type:'string'},
	   {name:'fileSize', type:'string'},
	   {name:'module', type:'string'},
	   {name:'name', type:'string'}
	]
});


function getSessionUser(){
	Ext.Ajax.request({
		url: '../login!getSessionUser.action',
	    success: function(response){
	    	var obj = Ext.decode(response.responseText);
	    	if(obj.resultObject==null ||obj.resultObject==''){
	    		document.location.href="../index.html";
	    	}
	    	if(obj.success && obj.resultObject.name!=null && obj.resultObject.name!='' ){
	    		//document.getElementById('CurrentUser').innerHTML =obj.resultObject.name;
	    	}else{
	    		document.location.href="../index.html";
	    	}            	        
	    }
	});
};
var storageStore=Ext.create('Ext.data.Store', {
	model: 'storageModel',
	timeout:180000,
	pageSize:16,
	remoteSort:true,
	proxy: {
		type: 'ajax',
        url : '../storage!findCloudStorage.action',
        timeout:180000,
		reader: {
            type: 'json',
			root:'resultObject.result',
			totalProperty: 'resultObject.totalCount'
        }
    },
    listeners : {
		beforeload : function(store,operation, eOpts ){
		},
		load : function(store, records, successful, eOpts ){
			if(storageStore.data.items.length == 0) {
				getSessionUser();
			}
		}
    }
});

storageStore.load();
//分页序号
Ext.grid.PageRowNumberer = Ext.extend(Ext.grid.RowNumberer, { 
	baseCls:Ext.baseCSSPrefix + 'column-header ' + Ext.baseCSSPrefix + 'unselectable',
	cls:Ext.baseCSSPrefix + 'row-numberer',
	tdCls:Ext.baseCSSPrefix+"grid-cell-special",
    renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){   
        return (store.currentPage - 1) * store.pageSize + rowIndex + 1;  
    }       
});

var startDate = Ext.create('Go.form.field.DateTime',{   
    fieldLabel:i18n._('startTime'),
    labelWidth: 60,
    format:'Y-m-d H:i:s'
}); 
var endDate = Ext.create('Go.form.field.DateTime',{ 
    fieldLabel:i18n._('endTime'),  
    labelWidth: 60,
    format:'Y-m-d H:i:s'  
});


//定义Zone
Ext.define('Module',{
			 extend: 'Ext.data.Model',
			 fields:[
			 {name:'id',type:'long'},
			 {name:'moduleName',type:'string'}
			 ]
});

var moduleStore=Ext.create('Ext.data.Store',{
    model: 'Module',
    proxy: new Ext.data.proxy.Ajax({
    	url: '../storage!loadModule.action',
		reader: {
            type: 'json',
            root: 'resultObject',
            totalProperty: 'resultObject.totalCount'
      }
    })
});
moduleStore.load();

//创建虚拟机节点下拉列表框
var moduleCombo = Ext.create('Ext.form.ComboBox', {
	fieldLabel : i18n._('module'),
	width : 160,
	style:{
		marginLeft:'5px'
	},
	labelWidth : 30,
	emptyText:i18n._('Please Select'),
	store : moduleStore,
	editable : false,
	multiSelect:false,
	allowBlank:true,
	queryMode : 'local',
	displayField : 'moduleName',
	valueField : 'moduleName'
});

var storageGrid=Ext.create('Ext.grid.Panel', {
    layout:'fit',
	title:i18n._('StorageManagement'),
	margin:'20 10 0 15',
    store:storageStore,
	simpleSelect:true,
	selModel: Ext.create('Ext.selection.RowModel'),
    columns: [Ext.create('Ext.grid.PageRowNumberer',{width:50}),//Ext.create('Ext.grid.RowNumberer',{header:i18n._('row_Num'),dataIndex:'rowId',align:'left',flex:0.1}),	    
	    {
    		header: i18n._('fileName'), 
    		dataIndex: 'name',
    		sortable: true,
    		flex : 0.4,
    		renderer:function(value){
    			return i18n._(value);
    		}
    	},	//模块名        
		{
        	header: i18n._('createTime'), //上次启动时间
        	dataIndex: 'dateShow',
        	width:160,
        	sortable: true,
        	renderer: Ext.util.Format.dateRenderer("Y-m-d H:i:s")
        },//
		{
	    	header: i18n._('fileSize'), 
	    	dataIndex: 'fileSize',
	    	sortable: true,
	    	width:120
	    },//
	    {
	    	header: i18n._('module'), 
	    	dataIndex: 'module',
	    	sortable: true,
	    	width:200
	    },//
		{
			header: i18n._('Operation'), //操作
			dataIndex: 'id',
			width:130,
			sortable: false,
			renderer : renderOperationStatus
		}		
	],	           	
    viewConfig: {
    	stripeRows: true,
    	loadMask: false
    	/*loadMask: {
            msg: i18n._('please wait')
        }*/
    },            		
	dockedItems:[
	{
		xtype:'toolbar',
		dock: 'top',			
		items:[
		       moduleCombo,
			   startDate,
			   endDate,
		       {xtype:'tbfill'},
		       {
		    	   xtype: 'searchfield',
		    	   width: 300,	                
		    	   store: storageStore,
		    	   emptyText: i18n._('fileName'),
                   id : 'vmSearchfield',
                   onTrigger1Click : function() {
                	   var proxy=storageStore.getProxy();
                       proxy.setExtraParam('startDate', startDate.getValue());
                       proxy.setExtraParam('endDate', endDate.getValue());
                       proxy.setExtraParam('query', this.getValue());
                       proxy.setExtraParam('module', moduleCombo.getValue());
                       storageStore.loadPage(1,null);
                    },
                    onTrigger2Click : function() {// 点击查询按钮或回车调用该方法
                    	if(!startDate.isValid() || !endDate.isValid()) {
                    	    return;
                        }
                    	var startValue = startDate.getValue();
                    	var endValue = endDate.getValue();
                    	if(null != startValue && null != endValue){
                            if(endValue.getTime()-startValue.getTime() < 0){
                                Ext.MessageBox.show({
                                      title : i18n._('errorNotice'),
                                      msg : i18n._('the expiry date should be later than effective date'),
                                      buttons : Ext.MessageBox.OK,
                                      icon : Ext.MessageBox.ERROR,
                                      fn:function(){
                                    	  startDate.focus();
                                      }
                                });
                                fromDateBill = '';
                                toDateBill = '';
                                return null;
                            }
                        }
                    	
                    	var value = this.getValue();
                        if (value.length < 1) {
                           this.onTrigger1Click();
                           return;
                        }
                        
                        var proxy=storageStore.getProxy();
                        proxy.setExtraParam('startDate', startDate.getValue());
                        proxy.setExtraParam('endDate', endDate.getValue());
                        proxy.setExtraParam('query',value);
                        proxy.setExtraParam('module', moduleCombo.getValue());
                        storageStore.loadPage(1,null);
                    }
		       }
		       ]
	},{
        xtype: 'pagingtoolbar',
        store: storageStore,   // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true,
        inputItemWidth:40,
        beforePageText:i18n._('beforePageText'),//"第"
		firstText: i18n._('firstText'),//"第一页"
        prevText: i18n._('prevText'),//"上一页"
        nextText: i18n._('nextText'),//"下一页"
        lastText: i18n._('lastText'),//"最后页"
        refreshText: i18n._('refreshText')//"刷新"
    }]
});

function closeWin() {
	if($("#openWinPlayer")[0] != null && $("#openWinPlayer")[0] != 'undefined') {
		$("#openWinPlayer").remove();
	}
}

function openWin() {
	closeWin();
	
	var imgCloseSrc = './images/close.png';
	
	var announcementList = "<table width='100%' border=0 cellspacing=0 cellpadding=0 >" +
	"<tr class='announceTr' style='background:#157fcc;' height='24'>" +
	"<td align='right'>&nbsp;" +
	"<img onclick='closeWin()' stype='margin:0px;' src='./images/close.png'>&nbsp;&nbsp;</td></tr></table>"
	//alert(announcementList);
	
	var today = new Date();
	var url = './play_window.html?' + today.getTime();
	var w = 800;
	var h = 600;
	var l = (screen.width - w) / 2;
    var t = (screen.height - h) / 2 - 70;
    var style = 'height:' + h + 'px;width:' + w + 'px;top:' + t +  'px;left:' + l + 'px;position:absolute;z-index:99999;';
	var parentdiv=$('<div style="' + style + '" id="openWinPlayer">' + announcementList + '<iframe name="childWin" id="childWin" src="' + url + '" width="100%" height="100%" style="border-width: 1px;"></div>');
	parentdiv.appendTo('body');
}

//位子窗口播放视频提供链接
var vdUrl='';
//下载媒体
function playMedia(date, time, name){
	Ext.Ajax.request({
        url: '../storage!getPlayUrl.action',
        jsonData:{
            "date": date,
            "time": time,
            "name": name,
            "operationType":3
        },
        timeout:180000,
        success: function(response){
            var obj = Ext.decode(response.responseText);
            if(obj.success){
            	if(obj.resultObject == 'login') {
            		document.location.href="../index.html";
            	} else {
            		vdUrl = obj.resultObject;
                  //  openWin();
            		var today = new Date();
            		var url = './play_window.html?' + today.getTime();
            		var w = 680;
            		var h = 600;
            		var l = (screen.width - w) / 2;
            	    var t = (screen.height - h) / 2 - 70;
            		window.open ('./play_window.html','newwindow',
            				'height=' + h + ',width=' + w + ',top=' + t +  ',left=' + l + ',toolbar=no,'
            				+'menubar=no,scrollbars=no, resizable=no,location=no, status=no');
            	}
           } else{
                Ext.MessageBox.show({
                    title:i18n._('notice'),
                    msg: obj.resultMsg,
                    icon:Ext.MessageBox.WARNING,
                    buttons: Ext.MessageBox.OK
                });
           }
            return;
        }
    });
};
//播放媒体
function downloadMedia(date, time, name){
	Ext.Ajax.request({
        url: '../storage!getPlayUrl.action',
        jsonData:{
            "date": date,
            "time": time,
            "name": name,
            "operationType":4
        },
        timeout:180000,
        success: function(response){
            var obj = Ext.decode(response.responseText);
            if(obj.success){
            	if(obj.resultObject == 'login') {
            		document.location.href="../index.html";
            	} else {
            		window.location.href = obj.resultObject;
            	}
           } else{
                Ext.MessageBox.show({
                    title:i18n._('notice'),
                    msg: obj.resultMsg,
                    icon:Ext.MessageBox.WARNING,
                    buttons: Ext.MessageBox.OK
                });
           }
            return;
        }
    });
}

//位子窗口播放视频提供链接
var copyUrl='';
//复制链接到剪贴板
function copyToClipBoard(date, time, name) {
	Ext.Ajax.request({
        url: '../storage!getPlayUrl.action',
        jsonData:{
            "date": date,
            "time": time,
            "name": name
        },
        success: function(response){
            var obj = Ext.decode(response.responseText);
            if(obj.success){
            	if(obj.resultObject == 'login') {
            		document.location.href="../index.html";
            	} else {
            		copyUrl = obj.resultObject;
            		var w = 660;
            		var h = 200;
            		var l = (screen.width - w) / 2; 
                    var t = (screen.height - h) / 2; 
            		window.open ('./copy_window.html','newwindow2',
            				'height=' + h + ',width=' + w + ',top=' + t +  ',left=' + l + ',toolbar=no,'
            				+'menubar=no,scrollbars=no, resizable=no,location=no, status=no');
            	}
           } else{
                Ext.MessageBox.show({
                    title:i18n._('notice'),
                    msg: obj.resultMsg,
                    icon:Ext.MessageBox.WARNING,
                    buttons: Ext.MessageBox.OK
                });
           }
            return;
        }
    });
}

//渲染操作状态
function renderOperationStatus(value, cellmeta, record, rowIndex, columnIndex, store) {
		var date = record.get("date");
		var time = record.get("time");
		var name = record.get("name");
		var spans='<span>';
		var spane='</span>';
		var play='<span><a href ="javascript:void(0)" onclick=playMedia("' + date + '","' + time + '","' + name + '") style="text-decoration: none" ><img src="../images/center/play.png" width="16" height="16" alt="">&nbsp;'+i18n._('play')+'</a></span>';
		var download='<span><a href ="javascript:void(0)" style="text-decoration: none" onclick=downloadMedia("' + date + '","' + time + '","' + name + '")><img src="../images/center/download.png" width="16" height="16" alt="">&nbsp;'+i18n._('download')+'</a></span>';
		return spans+play+'&nbsp;&nbsp;'+download+spane;
};


var oStartDate = Ext.create('Go.form.field.DateTime',{   
    fieldLabel:i18n._('startTime'),
    labelWidth: 60,
    format:'Y-m-d H:i:s'
}); 
var oEndDate = Ext.create('Go.form.field.DateTime',{ 
    fieldLabel:i18n._('endTime'),  
    labelWidth: 60,
    format:'Y-m-d H:i:s'  
});

var operationLogModel=Ext.define('operationLogModel',{
	extend:'Ext.data.Model',
	fields:[
	   {name:'id' ,type:'string'},
	   {name:'operationType', type:'string'},
	   {name:'operationTypeText', type:'string'},
	   {name:'ip', type:'string'}, 
	   {name:'date',mapping: 'date', type: 'date', dateFormat: 'time'},
	   {name:'operator', type:'string'}
	]
});


var operationLogStore=Ext.create('Ext.data.Store', {
	model: 'operationLogModel',
	pageSize:16,
	remoteSort:true,
	proxy: {
		type: 'ajax',
        url : '../operationLog!findPage.action',
		reader: {
            type: 'json',
			root:'resultObject.result',
			totalProperty: 'resultObject.totalCount'
        }
    },
    listeners : {
		beforeload : function(store,operation, eOpts ){
		},
		load : function(store, records, successful, eOpts ){
			if(operationLogStore.data.items.length == 0) {
				getSessionUser();
			}
		}
    }
});

operationLogStore.load();

var operationLogGrid=Ext.create('Ext.grid.Panel', {
    layout:'fit',
	title:i18n._('operationLog'),
	margin:'20 10 0 15',
    store:operationLogStore,
	simpleSelect:true,
	selModel: Ext.create('Ext.selection.RowModel'),
    columns: [Ext.create('Ext.grid.PageRowNumberer',{width:50}),//Ext.create('Ext.grid.RowNumberer',{header:i18n._('row_Num'),dataIndex:'rowId',align:'left',flex:0.1}),	    
	    {
    		header: i18n._('username'), 
    		dataIndex: 'operator',
    		sortable: true,
    		flex : 0.25
    	},	//模块名        
		{
        	header: i18n._('operationTime'), 
        	dataIndex: 'date',
        	flex : 0.25,
        	sortable: true,
        	renderer: Ext.util.Format.dateRenderer("Y-m-d H:i:s")
        },//
		{
	    	header: i18n._('operationType'), 
	    	dataIndex: 'operationTypeText',
	    	sortable: true,
	    	flex : 0.25,
	    	renderer:function(value){
    			return i18n._(value);
    		}
	    },//
	    {
	    	header: i18n._('ip'), 
	    	dataIndex: 'ip',
	    	sortable: true,
	    	flex : 0.25
	    }
	],	           	
    viewConfig: {
    	stripeRows: true,
    	loadMask: false
    	/*loadMask: {
            msg: i18n._('please wait')
        }*/
    },            		
	dockedItems:[
	{
		xtype:'toolbar',
		dock: 'top',			
		items:[
			   oStartDate,
			   oEndDate,
		       {xtype:'tbfill'},
		       {
		    	   xtype: 'searchfield',
		    	   width: 300,	                
		    	   store: operationLogStore,
		    	   emptyText: i18n._('ip'),
                   id : 'operationLogSearch',
                   onTrigger1Click : function() {
                	   var proxy=operationLogStore.getProxy();
                       proxy.setExtraParam('startDate', oStartDate.getValue());
                       proxy.setExtraParam('endDate', oEndDate.getValue());
                       proxy.setExtraParam('query', this.getValue());
                       operationLogStore.loadPage(1,null);
                    },
                    onTrigger2Click : function() {// 点击查询按钮或回车调用该方法
                    	if(!oStartDate.isValid() || !oEndDate.isValid()) {
                    	    return;
                        }
                    	var startValue = oStartDate.getValue();
                    	var endValue = oEndDate.getValue();
                    	if(null != startValue && null != endValue){
                            if(endValue.getTime()-startValue.getTime() < 0){
                                Ext.MessageBox.show({
                                      title : i18n._('errorNotice'),
                                      msg : i18n._('the expiry date should be later than effective date'),
                                      buttons : Ext.MessageBox.OK,
                                      icon : Ext.MessageBox.ERROR,
                                      fn:function(){
                                    	  startDate.focus();
                                      }
                                });
                                fromDateBill = '';
                                toDateBill = '';
                                return null;
                            }
                        }
                    	
                    	var value = this.getValue();
                        if (value.length < 1) {
                           this.onTrigger1Click();
                           return;
                        }
                        
                        var proxy=operationLogStore.getProxy();
                        proxy.setExtraParam('startDate', oStartDate.getValue());
                        proxy.setExtraParam('endDate', oEndDate.getValue());
                        proxy.setExtraParam('query',value);
                        operationLogStore.loadPage(1,null);
                    }
		       }
		       ]
	},{
        xtype: 'pagingtoolbar',
        store: operationLogStore,   // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true,
        inputItemWidth:40,
        beforePageText:i18n._('beforePageText'),//"第"
		firstText: i18n._('firstText'),//"第一页"
        prevText: i18n._('prevText'),//"上一页"
        nextText: i18n._('nextText'),//"下一页"
        lastText: i18n._('lastText'),//"最后页"
        refreshText: i18n._('refreshText')//"刷新"
    }]
});
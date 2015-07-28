//***nodeHistory
var params = getCookie("lang");
i18n.set({
  lang: params, 
  path: '../../resources'
});
var dateFormat='Y-m-d H:i:s';
if(params=='en_US'){
	dateFormat='m-d-Y H:i:s';
}
var nodeName=getCookie("nodeName");
if(getQuery("nodeName")!=null){
	nodeName=getQuery("nodeName");
}
Ext.Loader.setConfig({enabled: true});
var buttonUpCls="background-color:'#cbcbcb'";
var buttonDownCls="background-color:'#ee8800'";
var buttonUpStyle={backgroundColor:'#cbcbcb'};
var buttonDownStyle={backgroundColor:'#ee8800'};
var chartHostDiskmaximum = 1;
var chartHostNetworkmaximum = 1;
var beginDate = Ext.create('Ext.form.datetime.DateTime', {		
	margin : '0 5 0 0',
	fieldLabel : i18n._('beginDate'),
	labelAlign: 'center', 
	labelWidth: 80, 
	margin:'0 0 0 30',
	id : 'beginDate',
	format:dateFormat,
	value: new Date(),
	maxValue : Ext.Date.add(new Date(),Ext.Date.DAY, 1)
});
var endDate = Ext.create('Ext.form.field.Date', {
	margin : '0 5 0 0',		
	fieldLabel : i18n._('endDate'),
	labelAlign: 'center', 
	labelWidth: 55, 
	 margin:'0 0 0 40',
	id : 'endDate',
	format:'Y-m-d',
	maxValue : Ext.Date.add(new Date(),Ext.Date.DAY, 1)
});
var tipStep = 5;
//var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
//var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
var historyHostDiskJsonStore=Ext.create('Ext.data.JsonStore',{	
	fields:['timestamp','readSpeed','writeSpeed']
});
var historyHostNetJsonStore=Ext.create('Ext.data.JsonStore',{	
	fields:['timestamp','rxSpeed','txSpeed']
});
var historyHostCpuStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','cpuRate','cpuNum'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossHostCPUHistory.action',			
		reader: {
			type: 'json',
			root: 'resultObject.result',				
			totalProperty: 'resultObject.totalCount'
		},
		listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		load : function(historyHostCpuStore, records, successful, eOpts ){
			if(successful && historyHostCpuStore.getCount()>0){
				var count = historyHostCpuStore.getCount()-1;
				var timeAxis = chartHostCpu.axes.get(1);
				timeAxis.fromDate=historyHostCpuStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyHostCpuStore.getAt(count).get('timestamp');					
				chartHostCpu.redraw();			
			}
		}
	}
});
var historyHostMemoryStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','ramRate','ramTotal'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossHostMemoryHistory.action',			
		reader: {
			type: 'json',
			root: 'resultObject.result',				
			totalProperty: 'resultObject.totalCount'
		},
		listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		load : function(historyHostMemoryStore, records, successful, eOpts ){
			if(successful && historyHostMemoryStore.getCount()>0){
				var count = historyHostMemoryStore.getCount()-1;
				var timeAxis = chartHostMemory.axes.get(1);
				timeAxis.fromDate=historyHostMemoryStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyHostMemoryStore.getAt(count).get('timestamp');					
				chartHostMemory.redraw();			
			}
		}
	}
});
var historyHostDiskStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','diskTotal','readSpeed','writeSpeed'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossHostDiskHistory.action',			
		reader: {
			type: 'json',
			root: 'resultObject.result',				
			totalProperty: 'resultObject.totalCount'
		},
		listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		load : function(historyHostDiskStore, records, successful, eOpts ){			
			if(successful && historyHostDiskStore.getCount()>0){
				historyHostDiskJsonStore.removeAll();
				for(var i=0;i<historyHostDiskStore.getCount();i++){
					historyHostDiskJsonStore.add(
							{
								timestamp:historyHostDiskStore.getAt(i).get('timestamp'),
								readSpeed:historyHostDiskStore.getAt(i).get('readSpeed'),
								writeSpeed:historyHostDiskStore.getAt(i).get('writeSpeed')
							}
					);
					if(chartHostDiskmaximum<(historyHostDiskStore.getAt(i).get('readSpeed'))){
						chartHostDiskmaximum=(historyHostDiskStore.getAt(i).get('readSpeed'));
					}
					if(chartHostDiskmaximum<(historyHostDiskStore.getAt(i).get('writeSpeed'))){
						chartHostDiskmaximum=(historyHostDiskStore.getAt(i).get('writeSpeed'));
					}
				}
				chartHostDiskmaximum = Math.ceil(chartHostDiskmaximum);
				if(chartHostDiskmaximum>1){
					chartHostDiskmaximum = Math.ceil(chartHostDiskmaximum/100)*100;
				}				
				var count = historyHostDiskStore.getCount()-1;
				var timeAxis = chartHostDisk.axes.get(1);
				timeAxis.fromDate=historyHostDiskStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyHostDiskStore.getAt(count).get('timestamp');
				var numericAxis = chartHostDisk.axes.get(0);
				numericAxis.maximum=chartHostDiskmaximum;			
				chartHostDisk.redraw();
			}			
			//chartHostDisk.axes.setMaximum(chartHostDiskmaximum);
			//chartHostDisk.axes.maximum=chartHostDiskmaximum;
		}
	}
});
var historyHostNetStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','rxSpeed','txSpeed'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossHostNetHistory.action',			
		reader: {
			type: 'json',
			root: 'resultObject.result',				
			totalProperty: 'resultObject.totalCount'
		},
		listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		load : function(historyHostNetStore, records, successful, eOpts ){			
			if(successful && historyHostNetStore.getCount()>0){
				historyHostNetJsonStore.removeAll();
				for(var i=0;i<historyHostNetStore.getCount();i++){
					historyHostNetJsonStore.add(
							{
								timestamp:historyHostNetStore.getAt(i).get('timestamp'),
								rxSpeed:historyHostNetStore.getAt(i).get('rxSpeed')/1000,
								txSpeed:historyHostNetStore.getAt(i).get('txSpeed')/1000
							}
					);
					if(chartHostNetworkmaximum<(historyHostNetStore.getAt(i).get('rxSpeed')/1000)){
						chartHostNetworkmaximum=(historyHostNetStore.getAt(i).get('rxSpeed')/1000);
					}
					if(chartHostNetworkmaximum<(historyHostNetStore.getAt(i).get('txSpeed')/1000)){
						chartHostNetworkmaximum=(historyHostNetStore.getAt(i).get('txSpeed')/1000);
					}
				}
				chartHostNetworkmaximum = Math.ceil(chartHostNetworkmaximum);
				if(chartHostNetworkmaximum>1){
					chartHostNetworkmaximum = Math.ceil(chartHostNetworkmaximum/100)*100;
				}
				var count = historyHostNetStore.getCount()-1;
				var timeAxis = chartHostNetwork.axes.get(1);
				timeAxis.fromDate=historyHostNetStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyHostNetStore.getAt(count).get('timestamp');
				var numericAxis = chartHostNetwork.axes.get(0);
				numericAxis.maximum = chartHostNetworkmaximum;			
				chartHostNetwork.redraw();
			}			
			//chartHostNetwork.axes.maximum=chartHostNetworkmaximum;
		}
	}
});
var chartHostCpu = Ext.create('Ext.chart.Chart',{
	itemId : 'chartHostCpu',
	animate : true,
	//shadow : true,
	store : historyHostCpuStore,
	background:{
		//image:'images/hostMonitor.jpg'
		gradient:{
			angle:90,
			stops:{
				0:{
					color:'#ddd'
				},
				100:{
					color:'#555'
				}
			}
		}
	},
	axes : [
	{
		type : 'Numeric',
		minimum : 0,
		maximum : 100,
		decimals:0,
		position : 'left',
		fields : [ 'cpuRate' ],
		title : 'CPU(%)',
		grid : true
	},
	{
		type: 'Time',
		position: 'bottom',
		fields: ['timestamp'],
		//fromDate:startdate,
		//toDate:enddate,
		dateFormat: 'H',
		step:[Ext.Date.HOUR,1],
		groupBy: 'year,month,day,hour',
		title : 'Time'
	}],
	series : [
	{
		type : 'line',
		axis : 'left',
		gutter : 80,
		xField : 'timestamp',
		yField : [ 'cpuRate' ],
		tips: {
            trackMouse: true,
            width: 200,
            renderer: function (storeItem, item) {
                this.setTitle(Ext.Date.format(new Date(storeItem.get('timestamp')),'m/d/Y H:i:s')+ '(' + Ext.util.Format.round(storeItem.get('cpuRate'),2)+'%)');
            }
		 },
		markerConfig : {
			radius : 3,
			size : 1,
			fill:'#ee8800'
		}
	}]
});
var chartHostMemory = Ext.create('Ext.chart.Chart', {
	itemId : 'chartHostMemory',
	animate : true,
	shadow : true,
	store : historyHostMemoryStore,
	background:{
		//image:'images/hostMonitor.jpg'
		gradient:{
			angle:90,
			stops:{
				0:{
					color:'#ddd'
				},
				100:{
					color:'#555'
				}
			}
		}
	},
	axes : [
	{
		type : 'Numeric',
		position : 'left',
		minimum : 0,
		maximum : 100,
		decimals:0,
		fields : [ 'ramRate' ],
		title : 'Memory(%)',
		grid : true
	},
	{
		type: 'Time',
		position: 'bottom',
		fields: ['timestamp'],
		//fromDate:startdate,
		//toDate:enddate,
		dateFormat: 'H',
		step:[Ext.Date.HOUR,1],		
		groupBy: 'year,month,day,hour',
		title : 'Time'
	}],
	series : [{
		type : 'line',
		axis : 'left',
		gutter : 80,
		xField : 'timestamp',
		yField : [ 'ramRate' ],
		tips: {
            trackMouse: true,
            width: 200,
            renderer: function (storeItem, item) {
                this.setTitle(Ext.Date.format(new Date(storeItem.get('timestamp')),'m/d/Y H:i:s') + '(' + Ext.util.Format.round(storeItem.get('ramRate'),2)+'%)');
            }
		 },
		markerConfig : {
			radius : 3,
			size : 1,
			fill:'#ee8800'
		}
	}]
});
var chartHostDisk = Ext.create('Ext.chart.Chart', {
	itemId : 'chartHostDisk',
	animate : true,
	shadow : true,
	store : historyHostDiskJsonStore,//historyHostDiskStore,
	background:{
		//image:'images/hostMonitor.jpg'
		gradient:{
			angle:90,
			stops:{
				0:{
					color:'#ddd'
				},
				100:{
					color:'#555'
				}
			}
		}
	},
	legend: {
        position: 'right'//'right'
    },
	axes : [
	{
		type : 'Numeric',
		position : 'left',
		fields : [ 'readSpeed','writeSpeed' ],
		minimum : 0,
		maximum : chartHostDiskmaximum,
		//decimals:0,
		title : 'Disk(Mb/s)',
		grid : true
	},
	{
		type: 'Time',
		position: 'bottom',
		fields: ['timestamp'],
		//fromDate:startdate,
		//toDate:enddate,
		dateFormat: 'H',
		step:[Ext.Date.HOUR,1],
		groupBy: 'year,month,day,hour',
		title : 'Time'
	}],
	series : [		          
	{
		type : 'line',
		axis : 'left',
		gutter : 80,
		xField : 'timestamp',
		yField : [ 'readSpeed' ],
		tips: {
            trackMouse: true,
            width: 200,
            renderer: function (storeItem, item) {
                this.setTitle(Ext.Date.format(new Date(storeItem.get('timestamp')),'m/d/Y H:i:s') + '(' + Ext.util.Format.round(storeItem.get('readSpeed'),2)+'Mb/s)');
            }
		 },
		markerConfig : {
			type: 'circle',
			radius : 3,
			size : 1,
			fill:'#26c648'//'#ee8800'
		}
	},{
		type : 'line',
		axis : 'left',
		gutter : 80,
		xField : 'timestamp',
		yField : [ 'writeSpeed' ],
		tips: {
            trackMouse: true,
            width: 200,
            renderer: function (storeItem, item) {
                this.setTitle(Ext.Date.format(new Date(storeItem.get('timestamp')),'m/d/Y H:i:s') + '(' + Ext.util.Format.round(storeItem.get('writeSpeed'),2)+'Mb/s)');
            }
		 },
		markerConfig : {
			type: 'circle',
			radius : 3,
			size : 1,
			fill:'#ee8800'//'#26c648'
		}
	}]
});
var chartHostNetwork = Ext.create('Ext.chart.Chart', {
	itemId : 'chartHostNetwork',
	animate : true,
	shadow : true,
	store : historyHostNetJsonStore,//historyHostNetStore,
	background:{
		//image:'images/hostMonitor.jpg'
		gradient:{
			angle:90,
			stops:{
				0:{
					color:'#ddd'
				},
				100:{
					color:'#555'
				}
			}
		}
	},
	legend: {
        position: 'right'//'right'
    },
	axes : [
	{
		type : 'Numeric',
		position : 'left',
		fields : [ 'rxSpeed','txSpeed' ],
		minimum : 0,
		maximum : chartHostNetworkmaximum,
		//decimals:0,
		title : 'NetWork(Mbps)',
		grid : true
	},
	{
		type: 'Time',
		position: 'bottom',
		fields: ['timestamp'],
		//fromDate:startdate,
		//toDate:enddate,
		dateFormat: 'H',
		step:[Ext.Date.HOUR,1],
		groupBy: 'year,month,day,hour',
		title : 'Time'
	}],
	series : [
	{
		type : 'line',
		axis : 'left',
		gutter : 80,
		xField : 'timestamp',
		yField : [ 'rxSpeed'],
		tips: {
            trackMouse: true,
            width: 200,
            renderer: function (storeItem, item) {
                this.setTitle(Ext.Date.format(new Date(storeItem.get('timestamp')),'m/d/Y H:i:s') + '(' + Ext.util.Format.round(storeItem.get('rxSpeed'),2)+'Mbp/s)');
            }
		 },
		markerConfig : {
			type: 'circle',
			radius : 3,
			size : 1,
			fill:'#26c648'//'#ee8800'
		}
	},{
		type : 'line',
		axis : 'left',
		gutter : 80,
		xField : 'timestamp',
		yField : [ 'txSpeed' ],
		tips: {
            trackMouse: true,
            width: 200,
            renderer: function (storeItem, item) {
                this.setTitle(Ext.Date.format(new Date(storeItem.get('timestamp')),'m/d/Y H:i:s') + '(' + Ext.util.Format.round(storeItem.get('txSpeed'),2)+'Mbp/s)');
            }
		 },
		markerConfig : {
			type: 'circle',
			radius : 3,
			size : 1,
			fill:'#ee8800'//'#26c648'
		}
	}]
});
var cpuButton=new Ext.create('Ext.button.Button',{
	text :i18n._('CPUMonitor'),//'CPU监控'
	handler: function(){
		monitorHostCPUHistory();			
		nodeHistoryPanel.remove(chartHostMemory,false);
		nodeHistoryPanel.remove(chartHostDisk,false);
		nodeHistoryPanel.remove(chartHostNetwork,false);
		nodeHistoryPanel.add(chartHostCpu);
	}
});
var memoryButton=new Ext.create('Ext.button.Button',{
	text :i18n._('MemoryMonitor'),//'内存监控',
	handler: function(){
		//historyHostMemoryStore.removeAll();
		var proxy=historyHostMemoryStore.getProxy();
		proxy.setExtraParam('hostName',null) ;
		proxy.setExtraParam('fromTime',null);
		proxy.setExtraParam('toTime',null);
		if(beginDate.getValue()==null){
			Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
			beginDate.focus();
		}else{
			var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
			var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
			proxy.setExtraParam('hostName',nodeName) ;
			proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
			proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000
			historyHostMemoryStore.load();
			//chartHostMemory.redraw();
		}		
		nodeHistoryPanel.remove(chartHostCpu,false);
		nodeHistoryPanel.remove(chartHostDisk,false);
		nodeHistoryPanel.remove(chartHostNetwork,false);
		nodeHistoryPanel.add(chartHostMemory);
	}
});
var diskButton=new Ext.create('Ext.button.Button',{
	text :i18n._('DiskMonitor'),//'磁盘监控',
	handler: function(){
		//historyHostDiskStore.removeAll();
		var proxy=historyHostDiskStore.getProxy();
		proxy.setExtraParam('hostName',null) ;
		proxy.setExtraParam('fromTime',null);
		proxy.setExtraParam('toTime',null);
		if(beginDate.getValue()==null){
			Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
			beginDate.focus();
		}else{
			var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
			var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
			proxy.setExtraParam('hostName',nodeName) ;
			proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
			proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000
			historyHostDiskStore.load();
			//chartHostDisk.redraw();
		}			
		nodeHistoryPanel.remove(chartHostCpu,false);
		nodeHistoryPanel.remove(chartHostMemory,false);
		nodeHistoryPanel.remove(chartHostNetwork,false);
		nodeHistoryPanel.add(chartHostDisk);
	}
});
var bandWidthButton=new Ext.create('Ext.button.Button',{
	text :i18n._('NetworkMonitor'),//'网络监控',
	handler: function(){					
		//historyHostNetStore.removeAll();
		var proxy=historyHostNetStore.getProxy();
		proxy.setExtraParam('hostName',null) ;
		proxy.setExtraParam('fromTime',null);
		proxy.setExtraParam('toTime',null);
		if(beginDate.getValue()==null){
			Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
			beginDate.focus();
		}else{
			var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
			var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
			proxy.setExtraParam('hostName',nodeName) ;
			proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
			proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000					
			historyHostNetStore.load();
			//chartHostNetwork.redraw();
		}		
		nodeHistoryPanel.remove(chartHostCpu,false);
		nodeHistoryPanel.remove(chartHostMemory,false);
		nodeHistoryPanel.remove(chartHostDisk,false);
		nodeHistoryPanel.add(chartHostNetwork);
	}
});
var nodeHistoryPanel = new Ext.create('Ext.panel.Panel',{
    layout: 'fit',//'fit' 
    dockedItems: [
    {
    	xtype: 'toolbar',		
        items:[
		{
			xtype : 'tbfill'
		},
		beginDate,
		{
			xtype : 'tbfill'
		},
		cpuButton,memoryButton,diskButton,bandWidthButton
		]
	}],
	items : [
	         chartHostCpu//,chartHostMemory,chartHostDisk,chartHostNetwork
	         ]
});
function monitorHostCPUHistory(){
	//historyHostCpuStore.removeAll();
	var proxy=historyHostCpuStore.getProxy();
	proxy.setExtraParam('hostName',null) ;
	proxy.setExtraParam('fromTime',null);
	proxy.setExtraParam('toTime',null);
	if(beginDate.getValue()==null){
		Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
		beginDate.focus();
	}else{
		var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
		var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
		proxy.setExtraParam('hostName',nodeName) ;
		proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
		proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000
		historyHostCpuStore.load();
		//chartHostCpu.redraw();
	}
};
function getCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return unescape(arr[2]);
	 return null;
};
function   getQuery(name) 
{ 
        var reg=new RegExp("" +name+ "=([^&?]*)");        
        var keyVal=window.location.search.substr(1).match(reg);         
       //alert(keyVal[1]);        
        if   (keyVal!=null)   return   unescape(keyVal[1]);  
        return null;
};
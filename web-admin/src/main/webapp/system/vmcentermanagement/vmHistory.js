//***vmHistory
var params = getCookie("lang");
i18n.set({
  lang: params, 
  path: '../../resources'
});
var dateFormat='Y-m-d H:i:s';
if(params=='en_US'){
	dateFormat='m-d-Y H:i:s';
}
var vmID=getCookie("vmID");
if(getQuery("vmID")!=null){
	vmID=getQuery("vmID");
}
Ext.Loader.setConfig({enabled: true});
var chartVMDiskmaximum = 1;
var chartVMNetworkmaximum = 1;
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
var endDate=Ext.create('Ext.form.field.Date',{                 
	fieldLabel: i18n._('endDate'),
	labelAlign: 'center', 
	labelWidth: 55, 
	margin:'0 0 0 40',
	id:'endDate',
	maxValue: Ext.Date.add(new Date(),Ext.Date.DAY, 1)
});
var tipStep=5;
var i = 0;
var historyVMDiskJsonStore=Ext.create('Ext.data.JsonStore',{	
	fields:['timestamp','readSpeed','writeSpeed']
});
var historyVMNetJsonStore=Ext.create('Ext.data.JsonStore',{	
	fields:['timestamp','rxSpeed','txSpeed']
});
var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
var historyVMCpuStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','cpuRate','cpuNum'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossVmCPUHistory.action',			
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
		load : function(historyVMCpuStore, records, successful, eOpts ){
			if(successful && historyVMCpuStore.getCount()>0){
				var count = historyVMCpuStore.getCount()-1;
				var timeAxis = chartVMCpu.axes.get(1);
				timeAxis.fromDate=historyVMCpuStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyVMCpuStore.getAt(count).get('timestamp');					
				chartVMCpu.redraw();			
			}
		}
	}
});
var historyVMMemoryStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','ramRate','ramTotal'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossVmMemoryHistory.action',			
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
		load : function(historyVMMemoryStore, records, successful, eOpts ){
			if(successful && historyVMMemoryStore.getCount()>0){
				var count = historyVMMemoryStore.getCount()-1;
				var timeAxis = chartVMMemory.axes.get(1);
				timeAxis.fromDate=historyVMMemoryStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyVMMemoryStore.getAt(count).get('timestamp');					
				chartVMMemory.redraw();			
			}
		}
	}
});
var historyVMDiskStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','diskTotal','readSpeed','writeSpeed'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossVmDiskHistory.action',			
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
		load : function(historyVMDiskStore, records, successful, eOpts ){			
			if(successful && historyVMDiskStore.getCount()>0){
				historyVMDiskJsonStore.removeAll();
				for(var i=0;i<historyVMDiskStore.getCount();i++){
					historyVMDiskJsonStore.add(
							{
								timestamp:historyVMDiskStore.getAt(i).get('timestamp'),
								readSpeed:historyVMDiskStore.getAt(i).get('readSpeed'),
								writeSpeed:historyVMDiskStore.getAt(i).get('writeSpeed')
							}
					);
					if(chartVMDiskmaximum<(historyVMDiskStore.getAt(i).get('readSpeed'))){
						chartVMDiskmaximum=(historyVMDiskStore.getAt(i).get('readSpeed'));
					}
					if(chartVMDiskmaximum<(historyVMDiskStore.getAt(i).get('writeSpeed'))){
						chartVMDiskmaximum=(historyVMDiskStore.getAt(i).get('writeSpeed'));
					}
				}
				chartVMDiskmaximum = Math.ceil(chartVMDiskmaximum);
				var numericAxis = chartVMDisk.axes.get(0);
				numericAxis.maximum = chartVMDiskmaximum;	
				var count = historyVMDiskStore.getCount()-1;
				var timeAxis = chartVMDisk.axes.get(1);
				timeAxis.fromDate=historyVMDiskStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyVMDiskStore.getAt(count).get('timestamp');
				chartVMDisk.redraw();
			}
		}
	}
});
var historyVMNetStore=Ext.create('Ext.data.Store',{
	autoLoad:false,
	fields:['timestamp','rxSpeed','txSpeed'],
	proxy: new Ext.data.proxy.Ajax({
		url:path+'/../monitoring/monitor!ossVmNetHistory.action',			
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
		load : function(historyVMNetStore, records, successful, eOpts ){			
			if(successful && historyVMNetStore.getCount()>0){
				historyVMNetJsonStore.removeAll();
				for(var i=0;i<historyVMNetStore.getCount();i++){
					historyVMNetJsonStore.add(
							{
								timestamp:historyVMNetStore.getAt(i).get('timestamp'),
								rxSpeed:historyVMNetStore.getAt(i).get('rxSpeed')/1000,
								txSpeed:historyVMNetStore.getAt(i).get('txSpeed')/1000
							}
					);
					if(chartVMNetworkmaximum<(historyVMNetStore.getAt(i).get('rxSpeed')/1000)){
						chartVMNetworkmaximum=(historyVMNetStore.getAt(i).get('rxSpeed')/1000);
					}
					if(chartVMNetworkmaximum<(historyVMNetStore.getAt(i).get('txSpeed')/1000)){
						chartVMNetworkmaximum=(historyVMNetStore.getAt(i).get('txSpeed')/1000);
					}
				}
				chartVMNetworkmaximum = Math.ceil(chartVMNetworkmaximum);
				var numericAxis = chartVMNetwork.axes.get(0);
				numericAxis.maximum = chartVMNetworkmaximum;
				var count = historyVMNetStore.getCount()-1;
				var timeAxis = chartVMNetwork.axes.get(1);
				timeAxis.fromDate=historyVMNetStore.getAt(0).get('timestamp');
				timeAxis.toDate=historyVMNetStore.getAt(count).get('timestamp');
				chartVMNetwork.redraw();
			}
		}
	}
});
var chartVMCpu = Ext.create('Ext.chart.Chart',{
	id:'chartVMCpu',
	animate: true,
	shadow: true,
	store: historyVMCpuStore,
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
	axes: [{
		type: 'Numeric',
		minimum: 0,
		maximum: 100,
		decimals:0,
		position: 'left',
		fields: ['cpuRate'],
		title : 'CPU(%)',
		grid: true
	},{
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
	series: [{
		type: 'line',
		axis: 'left',
		gutter: 80,
		xField: 'timestamp',
		yField: ['cpuRate'],
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

var chartVMMemory = Ext.create('Ext.chart.Chart',{
	id:'chartVMMemory',
	animate: true,
	shadow: true,
	store: historyVMMemoryStore,
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
	axes: [{
		type: 'Numeric',
		position: 'left',
		minimum: 0,
		maximum: 100,
		decimals:0,
		fields: ['ramRate'],
		title : 'Memory(%)',
		grid: true
	},{
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
	series: [{
		type: 'line',
		axis: 'left',
		gutter: 80,
		xField: 'timestamp',
		yField: ['ramRate'],
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
var chartVMDisk = Ext.create('Ext.chart.Chart',{
	id:'chartVMDisk',
	animate: true,
	shadow: true,
	store: historyVMDiskJsonStore,//historyVMDiskStore,
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
	axes: [{
		type: 'Numeric',
		position: 'left',
		minimum: 0,
		maximum: chartVMDiskmaximum,
		//decimals:0,
		fields: ['readSpeed','writeSpeed'],
		title : 'Disk(Mb/s)',
		grid: true
	},{
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
	series: [{
		type: 'line',
		axis: 'left',
		gutter: 80,
		xField: 'timestamp',
		yField: ['readSpeed'],
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
		type: 'line',
		axis: 'left',
		gutter: 80,
		xField: 'timestamp',
		yField: ['writeSpeed'],
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
	
var chartVMNetwork = Ext.create('Ext.chart.Chart',{
	id:'chartVMNetwork',
	animate: true,
	shadow: true,
	store: historyVMNetJsonStore,//historyVMNetStore,
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
	axes: [{
		type: 'Numeric',
		minimum: 0,
		maximum: chartVMNetworkmaximum,
		//decimals:0,
		position: 'left',
		fields: ['rxSpeed','txSpeed'],
		title : 'NetWork(Mbps)',
		grid: true
	},{
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
	series: [{
		type: 'line',
		axis: 'left',
		gutter: 80,
		xField: 'timestamp',
		yField: ['rxSpeed'],
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
		type: 'line',
		axis: 'left',
		gutter: 80,
		xField: 'timestamp',
		yField: ['txSpeed'],
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
		monitorVMCPUHistory();		
		vmHistoryPanel.remove(chartVMMemory,false);
		vmHistoryPanel.remove(chartVMDisk,false);
		vmHistoryPanel.remove(chartVMNetwork,false);
		vmHistoryPanel.add(chartVMCpu);
	}
});
var memoryButton=new Ext.create('Ext.button.Button',{
	text :i18n._('MemoryMonitor'),//'内存监控',
	disabled:true,
	handler: function(){
		//historyVMMemoryStore.removeAll();
		var proxy=historyVMMemoryStore.getProxy();
		proxy.setExtraParam('vmId',null) ;
		proxy.setExtraParam('fromTime',null);
		proxy.setExtraParam('toTime',null);
		if(beginDate.getValue()==null){
			Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
			beginDate.focus();
		}else{
			var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
			var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
			proxy.setExtraParam('vmId',vmID) ;
			proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
			proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000
			historyVMMemoryStore.load();
		}			
		vmHistoryPanel.remove(chartVMCpu,false);
		vmHistoryPanel.remove(chartVMDisk,false);
		vmHistoryPanel.remove(chartVMNetwork,false);
		vmHistoryPanel.add(chartVMMemory);
	}
});
var diskButton=new Ext.create('Ext.button.Button',{
	text :i18n._('DiskMonitor'),//'磁盘监控',
	handler: function(){
		//historyVMDiskStore.removeAll();
		var proxy=historyVMDiskStore.getProxy();
		proxy.setExtraParam('vmId',null) ;
		proxy.setExtraParam('fromTime',null);
		proxy.setExtraParam('toTime',null);
		if(beginDate.getValue()==null){
			Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
			beginDate.focus();
		}else{
			var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
			var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
			proxy.setExtraParam('vmId',vmID) ;
			proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
			proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000
			historyVMDiskStore.load();
		}		
		vmHistoryPanel.remove(chartVMCpu,false);
		vmHistoryPanel.remove(chartVMMemory,false);
		vmHistoryPanel.remove(chartVMNetwork,false);
		vmHistoryPanel.add(chartVMDisk);
	}
});
var bandWidthButton=new Ext.create('Ext.button.Button',{
	text :i18n._('NetworkMonitor'),//'网络监控',
	//icon:'../../images/zoom.png',
	handler: function(){					
		//historyVMNetStore.removeAll();
		var proxy=historyVMNetStore.getProxy();
		proxy.setExtraParam('vmId',null) ;
		proxy.setExtraParam('fromTime',null);
		proxy.setExtraParam('toTime',null);
		if(beginDate.getValue()==null){
			Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
			beginDate.focus();
		}else{
			var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
			var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
			proxy.setExtraParam('vmId',vmID) ;
			proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
			proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000					
			historyVMNetStore.load();
		}	
		vmHistoryPanel.remove(chartVMCpu,false);
		vmHistoryPanel.remove(chartVMMemory,false);
		vmHistoryPanel.remove(chartVMDisk,false);
		vmHistoryPanel.add(chartVMNetwork);
	}
});
var vmHistoryPanel = new Ext.create('Ext.panel.Panel',{	
	//renderTo : Ext.getBody(),	
    resizable:false,    
	constrain : true,  
    plain: true,
    layout : 'fit',
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
	items : [chartVMCpu]//,chartVMMemory,chartVMDisk,chartVMNetwork]
});

function monitorVMCPUHistory(){
	//historyVMCpuStore.removeAll();
	var proxy=historyVMCpuStore.getProxy();
	proxy.setExtraParam('vmId',null) ;
	proxy.setExtraParam('fromTime',null);
	proxy.setExtraParam('toTime',null);
	if(beginDate.getValue()==null){
		Ext.Msg.alert(i18n._('notice'), i18n._('time_cant_null'));
		beginDate.focus();
	}else{
		var startdate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, -1);
		var enddate=Ext.Date.add(beginDate.getValue(),Ext.Date.DAY, 0);
//		var timeAxis = chartVMCpu.axes.get(1);
//		timeAxis.fromDate=startdate;
//		timeAxis.toDate=enddate;					
//		chartVMCpu.redraw();
		proxy.setExtraParam('vmId',vmID) ;
		proxy.setExtraParam('fromTime',Ext.Date.format(startdate, 'U')*1000);//Y-m-d H:i:s//Ext.Date.format(startdate, 'U')*1000
		proxy.setExtraParam('toTime',Ext.Date.format(enddate, 'U')*1000);//Y-m-d//Ext.Date.format(enddate, 'U')*1000
		historyVMCpuStore.load();
//		chartVMCpu.redraw();
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
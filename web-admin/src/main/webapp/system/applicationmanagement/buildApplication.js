//***buildApplication
Ext.Loader.setConfig({enabled: true});
var issueAppSelectValue;
var states = Ext.create('Ext.data.Store', {
    fields: ['id', 'name'],
    proxy : new Ext.data.proxy.Ajax(
			{
				url:path+"/../application_mgmt/application!getApplicationTypes.action",
				reader : {
					type : 'json',
					root : 'resultObject'									
				},
				listeners:{
					exception:function(reader, response, error, eOpts){
						ajaxException(reader, response, error, eOpts );										
					}
				}							
			})
    
    
});
states.load();
var availableSupplierStore = Ext.create('Ext.data.Store', {
    fields: ['id', 'name'],
    proxy : new Ext.data.proxy.Ajax(
			{
				url:path+"/../application_mgmt/application!getAvailableSupplier.action",
				reader : {
					type : 'json',
					root : 'resultObject'									
				},
				listeners:{
					exception:function(reader, response, error, eOpts){
						ajaxException(reader, response, error, eOpts );										
					}
				}							
			})
});
availableSupplierStore.load();

var formAppTypeStore = Ext.create('Ext.data.Store', {
    fields: ['id', 'name'],
    proxy : new Ext.data.proxy.Ajax(
			{
				url:path+"/../application_mgmt/application!getApplicationTypes.action",
				reader : {
					type : 'json',
					root : 'resultObject'									
				},
				listeners:{
					exception:function(reader, response, error, eOpts){
						ajaxException(reader, response, error, eOpts );										
					}
				}							
			})
    
    
});
formAppTypeStore.load();
var appCategoryCombox = Ext.create('Ext.form.field.ComboBox',{
	multiSelect: false,
	fieldLabel:'应用类型',
	    displayField: 'name',
		valueField:'id',
	    width: 230,
	    editable:false,
	    labelWidth: 80,
	    store: states,
	 listeners:{
	    	select:function( combo, records, eOpts ){
	    		if(combo != null || combo != ""){
	    			issueAppSelectValue="";
	    			issueAppSelectValue = appCategoryCombox.getValue();
	    		}
	    	}
	    }
});

var payMethodStore = new Ext.data.JsonStore( {  
    fields : [ 'id', 'name' ],  
    data : [ {  
        id : '0',  
        name : '预付款'  
    }, {  
        id : '1',  
        name : '后付款'  
    }]  
});

var needVmStore = new Ext.data.JsonStore( {  
    fields : [ 'id', 'name' ],  
    data : [ {  
        id : '0',  
        name : '不需要'  
    }, {  
        id : '1',  
        name : '需要'  
    }]  
});

var buildAppForm=Ext.create('Ext.form.FormPanel', {
    width: '440',
    height: '300',
    border:false,
    bodyPadding : 10,
    //fileUpload:true,
    autoScroll:true,
    fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 80,
		anchor : '100%'
	},  
    items: [
		{
			xtype:'textfield',
			fieldLabel:'应用Id',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationVo.id',
			id:'applicationId',
			hidden: true,
            hideLabel: true
		},
		{
			xtype:'textfield',
			fieldLabel:'应用详细Id',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationDetail.id',
			id:'applicationDetailId',
			hidden: true,
            hideLabel: true
		},
        {
			xtype:'textfield',
			fieldLabel:'应用名称',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationVo.name',
			maxLength: 30,
			enforceMaxLength:true,
			allowBlank: false,
			id:'appName'
		},
		{
            xtype: 'filefield',
            id: 'appLogo',
            emptyText: '选择图片',
            fieldLabel: '应用LOGO',
            labelAlign: 'right',
            name: 'appLogo',
            buttonText: '浏览',
            msgTarget: 'side',
            style:'margin-left:10px;margin-right:70px'
        },
		{
			xtype:'textfield',
			fieldLabel:'官网地址',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationDetail.officialUrl',
			maxLength: 50,
			enforceMaxLength:true,
			//allowBlank: false,
			id:'officialUrl'
		},
		{
			xtype:'textfield',
			fieldLabel:'官方微信',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationDetail.wechatQrcode',
			maxLength: 20,
			enforceMaxLength:true,
			//allowBlank: false,
			id:'wechatQrcode'
		},
		{
            xtype: 'filefield',
            id: 'weChat-file',
            emptyText: '选择图片',
            fieldLabel: '微信图片',
            labelAlign: 'right',
            name: 'weChat',
            buttonText: '浏览',
            msgTarget: 'side',
            style:'margin-left:10px;margin-right:70px'
        },
		{
			xtype:'textfield',
			fieldLabel:'官方微博',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationDetail.weiboQrcode',
			maxLength: 50,
			enforceMaxLength:true,
			//allowBlank: false,
			id:'weiboQrcode'
		},
		{
			xtype:'textarea',
			fieldLabel:'应用简介',
			style:'margin-left:10px;margin-right:70px',
			name:'applicationDetail.introduction',
			width:400,
    	    height:150,
			maxLength: 1500,
			enforceMaxLength:true,
			//allowBlank: false,
			id:'introduction'
		},
	    {
    	    style:'margin-left:10px;margin-right:70px',
			fieldLabel:'应用类型',
			name:'applicationVo.categoryId',
			xtype:'combo',
			allowBlank: false,
			id:'appCategoryId',
			mode:'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			store:formAppTypeStore
		 },
	     {
    	    style:'margin-left:10px;margin-right:70px',
			fieldLabel:'支付类型',
			name:'applicationDetail.payMethod',
			xtype:'combo',
			allowBlank: false,
			id:'payMethod',
			mode: 'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			store:payMethodStore
	   },
	   {
   	    style:'margin-left:10px;margin-right:70px',
			fieldLabel:'供应商',
			name:'applicationVo.supplierId',
			xtype:'combo',
			id:'supplierId',
			mode: 'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			allowBlank: false,
			store:availableSupplierStore
	   },
	   {
   	        style:'margin-left:10px;margin-right:70px',
			fieldLabel:'虚拟机',
			name:'applicationVo.needVm',
			xtype:'combo',
			id:'appNeedVm',
			mode: 'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			allowBlank: false,
			store:needVmStore
		}
       ]
    
});

var buildAppPanel = new Ext.create('Ext.panel.Panel',{	
    resizable:false,    
	constrain : true,  
    plain: true,
    layout : 'fit',
	items : [buildAppForm],
	buttons: [{  
	    text: '保存',
	    id:'buildAppInfoBtn',
	    handler: function() {
	    if(buildAppForm.form.isValid()){
	    	   Ext.getCmp('buildAppInfoBtn').setDisabled(true);
	    	   buildAppForm.getForm().submit({      
	    		    url:path+"/../application_mgmt/upload!saveApplicationInfo.action",
	          	    method:"POST", 
	                success: function(form, action){
	                	Ext.getCmp('buildAppInfoBtn').setDisabled(false);
	                    showOk(i18n._('Prompt'),'保存成功！');
	                    appId=action.result.applicationId;
	                    GpayMethod=action.result.payMethod;
	                    GneedVm=action.result.needVm;
	                    Ext.getCmp('applicationId').setValue(appId);
	                    Ext.getCmp('applicationDetailId').setValue(action.result.applicationDetailId);
	                },      
	                failure: function(form, action){    
	                	 Ext.getCmp('buildAppInfoBtn').setDisabled(false);
	                     Ext.Msg.alert('Error', action.result.msg);
	                }  
	            });
	         } 
	     }  
	  }] 
});

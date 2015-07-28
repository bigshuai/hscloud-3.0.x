//***vmList
var params = getCookie("lang");
i18n.set({
	lang : params,
	path : '../../resources'
});
var v_mask =null;
Ext.Loader.setConfig({
	enabled : true
});
Ext.require([ 'Ext.data.*', 'Ext.panel.Panel', 'Ext.view.View',
		'Ext.layout.container.Fit', 'Ext.toolbar.Paging',
		'Ext.selection.CheckboxModel', 'Ext.ux.data.PagingMemoryProxy','Ext.tip.*' ]);
Ext.QuickTips.init();
Ext.apply(Ext.QuickTips.getQuickTip(), {
    maxWidth: 500,
    trackMouse: true,
    dismissDelay: 0
});
//价格体系列表模板
var durationManagementModel = Ext.define('durationManagementModel', {
	extend : 'Ext.data.Model',
	fields : [ 'id', 'appId','duration', 'threshold'
	],//
	idProperty : 'id'
});

var durationStore = new Ext.data.JsonStore( {  
    fields : [ 'id', 'name' ],  
    data : [ {  
        id : '1',  
        name : '一个月'  
    },{  
        id : '3',  
        name : '三个月'  
    },{  
        id : '6',  
        name : '六个月'  
    },{  
        id : '12',  
        name : '十二个月'  
    }]  
}); 

//价格体系列表Store
var durationManagementStore = Ext.create('Ext.data.Store', {
	model : 'durationManagementModel',
	pageSize : 16,//每页显示16条数据
	//remoteSort : true,
	autoLoad : true,
	proxy : new Ext.data.proxy.Ajax({
		url : path + '/../application_mgmt/application!findDurationManagementPage.action',
		reader : {
			type : 'json',
			root : 'resultObject.result',
			totalProperty : 'resultObject.totalCount'
		},
		//timeout:60000,//请求超时时间：由30秒调整为60秒
		listeners:{
			exception:function(reader, response, error, eOpts){
				v_mask.hide();
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		beforeload : function(durationManagementStore,operation, eOpts ){	
			//遮罩层
			v_mask = new Ext.LoadMask(Ext
					.getBody(), {
				msg : i18n._('please wait'),
				removeMask : true			
			});
			v_mask.show();
		},
		load : function(durationManagementStore, records, successful, eOpts ){
			//遮罩层
			var v_mask = new Ext.LoadMask(Ext
					.getBody(), {
				msg : i18n._('please wait'),
				removeMask : true			
			});
			v_mask.hide();
		}
	}
});
//分页序号
Ext.grid.PageRowNumberer = Ext.extend(Ext.grid.RowNumberer, { 
	baseCls:Ext.baseCSSPrefix + 'column-header ' + Ext.baseCSSPrefix + 'unselectable',
	cls:Ext.baseCSSPrefix + 'row-numberer',
	tdCls:Ext.baseCSSPrefix+"grid-cell-special",
    renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){   
        return (store.currentPage - 1) * store.pageSize + rowIndex + 1;  
    }       
});
var durationManagementGrid = Ext.create('Ext.grid.Panel',{
	autoWidth : true,
	store : durationManagementStore,
	simpleSelect : true,
	selModel : Ext.create('Ext.selection.RowModel'),					
	selType : 'cellmodel',
	iconCls: 'icon-grid',
	columnLines: true,
	bbar : Ext.create('Ext.toolbar.Paging', {
		store : durationManagementStore,
		displayInfo : true,
		inputItemWidth:pagingBarPageNumWidth,
		beforePageText:i18n._('beforePageText'),//"第"
		firstText: i18n._('firstText'),//"第一页"
        prevText: i18n._('prevText'),//"上一页"
        nextText: i18n._('nextText'),//"下一页"
        lastText: i18n._('lastText'),//"最后页"
        refreshText: i18n._('refreshText')//"刷新"
       
	}),

	viewConfig: {
		stripeRows: true,
		forceFit: true						
	},
	dockedItems : [ {
		xtype : 'toolbar',
		layout: {
	        overflowHandler: 'Menu'
	    },
		cls: 'toolbarCSS',
		items : [
		         {
					xtype : 'button',
					text: '<font id="vmcreate" color="#ffffff" >' + i18n._('Create') + '</font>',//创建
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("vmcreate").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("vmcreate").style.color = "black";
						 }
					},
					icon : 'images/add.png',// ../../images/control_play_blue.png
					handler : function() {	
						if(appId==''){
							Ext.MessageBox.show({
								title : i18n._('notice'),
								msg :"请先添加应用基本信息！",
								icon : Ext.MessageBox.INFO,
								buttons : Ext.MessageBox.OK,
								fn: afterDeleteCouponPolicy
							});	
						}else{
							if(GpayMethod=='0'){//预付款可以添加时长管理信息
								getSessionUser();
								addDurationManagementForm.getForm().reset();
								addDurationManagementWin.show();
							}else{
								Ext.MessageBox.show({
									title : i18n._('notice'),
									msg :"预付款的应用可以添加！",
									icon : Ext.MessageBox.INFO,
									buttons : Ext.MessageBox.OK,
									fn: afterDeleteCouponPolicy
								});	
							}
						}
					}
		         },
				{
					xtype : 'button',
					text: '<font id="vmdelete" color="#ffffff" >' + i18n._('delete') + '</font>',//删除	
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("vmdelete").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("vmdelete").style.color = "black";
						 }
							
					},
					icon : 'images/del.png',
					handler : function() {
						getSessionUser();
						//删除时长管理
						var rows = durationManagementGrid.getSelectionModel().getSelection();
						var Id;
						if (rows.length > 0) {
							Id = rows[0].get('id');
							Ext.MessageBox.show({				                  
								title: i18n._('notice'),
								msg:i18n._('Are you sure to delete'),
								buttons: Ext.MessageBox.YESNO,
								icon: Ext.MessageBox.QUESTION,
								fn:function(e){
									if(e=='yes'){
										Ext.Ajax.request({
											url:path+"/../application_mgmt/application!delDurationManagement.action",
											method: 'POST',
											params:{
												'priceCloudThresholdVo.id':Id
											},
											success: function (response) {
												var result=Ext.JSON.decode(response.responseText);
												if(result.success==true){
													Ext.MessageBox.show({
														title : i18n._('notice'),
														msg : i18n._('Operating')+i18n._('Successful'),
														icon : Ext.MessageBox.INFO,
														buttons : Ext.MessageBox.OK,
														fn: afterDeleteDuration
													});																							
												}else{
													Ext.MessageBox.show({
														title : i18n._('notice'),
														msg : result.resultMsg,
														icon : Ext.MessageBox.INFO,
														buttons : Ext.MessageBox.OK
													});
													return;
												}
											}
										});
									}
								}
				    		});	
					}else {
						Ext.MessageBox.show({
							title : i18n._('Prompt'),
							msg : i18n._('selectOne'),
							icon : Ext.MessageBox.INFO,
							buttons : Ext.MessageBox.OK
						});
						return;
					}
					}
				}]
			} ],
					
	columns : [ Ext.create('Ext.grid.PageRowNumberer',{flex : 0.1,width:5}),
	{
		text :'id',
		flex : 0.4,
		hidden:true,
		dataIndex : 'id'
	},{
		text : '时长',
		flex : 0.4,
		sortable: true,
		dataIndex : 'duration'
	},{
		text :'折扣',
		flex : 0.5,
		sortable: true,
		dataIndex : 'threshold'
	}
	]
});// 创建grid


//保存优惠政策
function saveDurationManagement(){
		if(!addDurationManagementForm.form.isValid()){
			 return;
		 }
	    var drationTime = Ext.getCmp('drationTime').getValue();
	    var discountValue = Ext.getCmp('discountValue').getValue();
	    Ext.getCmp('saveDurationBtn').setDisabled(true);
	    var progress=Ext.Ajax.request({
	        url:path+"/../application_mgmt/application!saveDurationManagementInfo.action",
	        method:'POST',
	        params:{
	        	'priceCloudThresholdVo.appId': appId,
	            'priceCloudThresholdVo.duration': drationTime,
	            'priceCloudThresholdVo.threshold':discountValue
	        },
	        success:function(form,action){
	        	Ext.getCmp('saveDurationBtn').setDisabled(false);
	            var obj = Ext.decode(form.responseText);
	                if(obj==null || obj.success==null){
	                    Ext.MessageBox.show({
	                       title: i18n._('errorNotice'),
	                       msg: i18n._('returnNull'),
	                       buttons: Ext.MessageBox.OK,
	                       icon: Ext.MessageBox.ERROR
	                    });
	                    return;
	                }
	                if(!obj.success){
	                    Ext.MessageBox.show({
	                       title: i18n._('errorNotice'),
	                       msg: obj.resultMsg,
	                       buttons: Ext.MessageBox.OK,
	                       icon: Ext.MessageBox.ERROR
	                    });
	                    return;
	                }
	                Ext.MessageBox.show({
	                	title: i18n._('notice'),
	                    msg: '保存成功！',
	                    buttons: Ext.MessageBox.OK,
	                    icon: Ext.MessageBox.INFO,
	                    fn: reLoadDrationData
	                });
	                durationManagementStore.load();
	                addDurationManagementForm.getForm().reset();
	                addDurationManagementWin.hide();
	        },   
	        failure:function(form,action){ 
	        	Ext.getCmp('saveDurationBtn').setDisabled(false);
	            Ext.MessageBox.show({
	                title: i18n._('errorNotice'),
	                msg: i18n._('operateFail'),
	                buttons: Ext.MessageBox.OK,
	                icon: Ext.MessageBox.ERROR
	            });  
	        }
	    });
}

//删除后刷新页面
function afterDeleteDuration(){
    var count = durationManagementStore.data.length;
    var total = durationManagementStore.getTotalCount();
    if(total != 1 && count == 1) {
    	durationManagementStore.previousPage();
    } else {
    	durationManagementStore.load();
    }
}

//优惠政策表单
var addDurationManagementForm=Ext.create('Ext.form.FormPanel', {
    width: '440',
    height: '400',
    border:false,
    bodyPadding : 10,
    autoScroll:true,
    fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 80,
		anchor : '100%'
	},  
    items: [
       {
            fieldLabel:'时长',
            style:'margin-left:10px;margin-right:70px',
			name:'drationTime',
			xtype:'combo',
			allowBlank: false,
			id:'drationTime',
			mode: 'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			store:durationStore
       },
       {
			xtype:'textfield',
			fieldLabel:'折扣',
			style:'margin-left:10px;margin-right:70px',
			name:'discountValue',
			maxLength: 20,
			enforceMaxLength:true,
			allowBlank: false,
			//validator : vd,
			id:'discountValue'
		}
       
       ],
    buttons:[
        {text:"保存", id:'saveDurationBtn', handler:saveDurationManagement},
        {text:i18n._('reset'), handler:function(){addDurationManagementForm.getForm().reset();}}
    ]
    
});

//优惠政策
var addDurationManagementWin = Ext.create('Ext.window.Window', {
	width : 400,// 400
	height : 300,
	closable : false,
	constrain : true,
	modal : true,
	tools : [ {
		type : 'close',
		handler : function() {
			addDurationManagementForm.getForm().reset();
			addDurationManagementWin.hide();
		}
	} ],
	layout : 'fit',
	defaults : {
		split : false
	},
	items : [addDurationManagementForm]
});



//操作完成后点确认刷新数据操作
function reLoadDrationData(btn){
	durationManagementStore.load();
};

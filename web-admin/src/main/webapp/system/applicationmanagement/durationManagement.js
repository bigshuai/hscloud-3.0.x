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
	fields : [ 'id', 'appId','duration', 'threshold','thresholdType'
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


var durationTypeStore = new Ext.data.JsonStore( {  
    fields : [ 'id', 'name' ],  
    data : [ {  
        id : '0',  
        name : '折扣'  
    }, {  
        id : '1',  
        name : '固值'  
    }]  
}); 

//价格体系列表Store
var durationManagementStore = Ext.create('Ext.data.Store', {
	model : 'durationManagementModel',
	pageSize : 16,//每页显示16条数据
	sorters : [ {
		property : 'id',
		direction : 'DESC'
	} ],
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
	},
	{
		text : '时长',
		flex : 0.4,
		sortable: true,
		dataIndex : 'duration'
	},
	{
		text :'优惠类型',
		flex : 0.4,
		sortable: true,
		dataIndex : 'thresholdType',
		renderer:function(value){
			if(value== '0'){
				return "折扣";
			}else if(value== '1'){
				return "固值";
			}
		}
	},
	{
		text :'优惠值',
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
	    var drationType=Ext.getCmp('drationType').getValue();
	    var type="^[1-9]([0-9])*$"; 
	    var flag=true;
	    var re =new RegExp(type); 
	    if(!re.test(discountValue)){
	    	Ext.MessageBox.show({
            	title: i18n._('notice'),
                msg: '只能输入正整数！',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO,
                fn: reLoadDrationData
            });
	    	return;
	    }
	    if(drationType==0&&discountValue>=100){
	    	Ext.MessageBox.show({
            	title: i18n._('notice'),
                msg: '折扣需要小于100！',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO,
                fn: reLoadDrationData
            });
	    	return;
	    }
	    
	    if(drationType==1){
	    	Ext.Ajax.request({
				url:path+"/../application_mgmt/application!findMinPriceByAppId.action",
				method: 'POST',
				async:false,
				params:{
					'appId':appId
				},
				success: function (response) {
					var result=Ext.JSON.decode(response.responseText);
					if(result.resultObject==null){
						Ext.MessageBox.show({
							title : i18n._('notice'),
							msg : '请添加价格体系！',
							icon : Ext.MessageBox.INFO,
							buttons : Ext.MessageBox.OK
						});
						flag=false;
					}else if(discountValue>result.resultObject){
							Ext.MessageBox.show({
								title : i18n._('notice'),
								msg : '优惠值不能大于该应用的最低价'+result.resultObject+'!',
								icon : Ext.MessageBox.INFO,
								buttons : Ext.MessageBox.OK
							});
							flag=false;
					}
				},
				failure:function(form,action){
			            Ext.MessageBox.show({
			                title: i18n._('errorNotice'),
			                msg: i18n._('operateFail'),
			                buttons: Ext.MessageBox.OK,
			                icon: Ext.MessageBox.ERROR
			            });  
			            flag=false;
			        }
			});
	    }
	    if(flag){
	        Ext.getCmp('saveDurationBtn').setDisabled(true);
			Ext.Ajax.request({
				url:path+"/../application_mgmt/application!ifDurationSystemInfo.action",
				method: 'POST',
				async:false,
				params:{
					'appId':appId,
					'duration':drationTime
				},
				success: function (response) {
					var result=Ext.JSON.decode(response.responseText);
					if(result.resultObject){
					    var progress=Ext.Ajax.request({
					        url:path+"/../application_mgmt/application!saveDurationManagementInfo.action",
					        method:'POST',
					        async:false,
					        params:{
					        	'priceCloudThresholdVo.appId': appId,
					            'priceCloudThresholdVo.duration': drationTime,
					            'priceCloudThresholdVo.threshold':discountValue,
					            'priceCloudThresholdVo.thresholdType':drationType
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
					}else{
						Ext.MessageBox.show({
							title : i18n._('Prompt'),
							msg : '该应用已经存在您要添加的时长,请重新选择！',
							icon : Ext.MessageBox.INFO,
							buttons : Ext.MessageBox.OK
						});
						 Ext.getCmp('saveDurationBtn').setDisabled(false);
						return;
					}
				}
			});
	    }
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
            fieldLabel:'优惠类型',
            style:'margin-left:10px;margin-right:70px',
			name:'drationType',
			xtype:'combo',
			allowBlank: false,
			id:'drationType',
			mode: 'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			store:durationTypeStore
       },
       {
			xtype:'textfield',
			fieldLabel:'优惠值',
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

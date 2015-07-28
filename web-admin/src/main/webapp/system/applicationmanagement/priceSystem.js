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
var priceSystemModel = Ext.define('priceSystemVo', {
	extend : 'Ext.data.Model',
	fields : [ 'id','appId','name','peopleNum','price','serviceCatalogDescription','serviceCatalogName','serviceCatalogId'
	],//
	idProperty : 'id'
});



//价格体系列表Store
var priceSystemStore = Ext.create('Ext.data.Store', {
	model : 'priceSystemVo',
	pageSize : 16,//每页显示16条数据
	//remoteSort : true,
	autoLoad : false,
	proxy : new Ext.data.proxy.Ajax({
		url:path+"/../application_mgmt/application!findPriceSystemPage.action",
		reader : {
			type : 'json',
			root : 'resultObject.result',
			totalProperty : 'resultObject.totalCount'
		},
		timeout:60000,//请求超时时间：由30秒调整为60秒
		listeners:{
			exception:function(reader, response, error, eOpts){
				v_mask.hide();
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		beforeload : function(priceSystemStore,operation, eOpts ){	
			//遮罩层
			v_mask = new Ext.LoadMask(Ext
					.getBody(), {
				msg : i18n._('please wait'),
				removeMask : true			
			});
			v_mask.show();
		},
		load : function(priceSystemStore, records, successful, eOpts ){
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
var priceSystemGrid = Ext.create('Ext.grid.Panel',{
	autoWidth : true,
	store : priceSystemStore,
	simpleSelect : true,
	selModel : Ext.create('Ext.selection.RowModel'),					
	selType : 'cellmodel',
	iconCls: 'icon-grid',
	columnLines: true,
	bbar : Ext.create('Ext.toolbar.Paging', {
		store : priceSystemStore,
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
					text: '<font id="addPriceSystem" color="#ffffff" >' + i18n._('Create') + '</font>',//创建
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("addPriceSystem").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("addPriceSystem").style.color = "black";
						 }
					},
					icon : 'images/add.png',
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
							addPriceSystemForm.getForm().reset();
							if(GneedVm=='0'){
								Ext.getCmp('priceSystemConfig').setDisabled(true);
							}else{
								Ext.getCmp('priceSystemConfig').setDisabled(false);
							}
							Ext.getCmp('configMes').setText('');
							addPriceSystemWin.setTitle("添加价格体系");//创建虚拟机										
							addPriceSystemWin.show();
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
					icon : 'images/del.png',// ../../images/cross.gif
					handler : function() {
						getSessionUser();
						//删除价格体系
						var rows = priceSystemGrid.getSelectionModel().getSelection();
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
											url:path+"/../application_mgmt/application!delPriceSystem.action",
											method: 'POST',
											params:{
												'appPriceSystemVo.id':Id
											},
											success: function (response) {
												var result=Ext.JSON.decode(response.responseText);
												if(result.success==true){
													Ext.MessageBox.show({
														title : i18n._('notice'),
														msg : i18n._('Operating')+i18n._('Successful'),
														icon : Ext.MessageBox.INFO,
														buttons : Ext.MessageBox.OK,
														fn: afterDeletePriceSystem
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
				}
		         ]
			} ],
					
	columns : [ Ext.create('Ext.grid.PageRowNumberer',{flex : 0.2,width:50}),
	{
		text :'id',
		flex : 0.4,
		hidden:true,
		//sortable : false,
		dataIndex : 'id'
	},{
		text :'名称',
		flex : 0.4,
		sortable: true,
		dataIndex : 'name'
	},  {
		text : '人数',
		flex : 0.5,
		sortable: false,
		dataIndex : 'peopleNum'
	}, {
		text : '价格',//操作系统
		flex : 0.4,
		sortable: true,
		dataIndex : 'price'
	}, {
		text : '套餐名称',
		flex : 0.4,
		sortable: true,
		dataIndex : 'serviceCatalogName'
	},{
		text :'套餐描述',//
		flex : 0.9,
		sortable: true,
		dataIndex : 'serviceCatalogDescription'
	}
	]
});// 创建grid

var scStore = Ext.create('Ext.data.Store', {
    fields: ['id', 'name','description'],
    proxy : new Ext.data.proxy.Ajax(
			{
				url:path+"/../application_mgmt/application!findAllScInfo.action",
				reader : {
					type : 'json',
					root : 'resultObject'									
				},
				listeners:{
					exception:function(reader, response, error, eOpts){
						//v_mask.hide();
						ajaxException(reader, response, error, eOpts );										
					}
				}							
			})
    
    
});

scStore.load();

/*var scCombox = Ext.create('Ext.form.field.ComboBox',{
	    multiSelect: false,
	    fieldLabel:'推荐配置',
	    displayField: 'name',
		valueField:'id',
	    width: 230,
	    editable:false,
	    allowBlank: false,
	    labelWidth: 80,
	    store: scStore,
	    emptyText:'请选择',
	 listeners:{
	    	select:function( combo, records, eOpts ){
	    		if(combo != null || combo != ""){
	    			Ext.getCmp('configMes').setText(records[0].data.description);
	    		}
	    	}
	    }
});*/

//保存价格体系
function savePriceSystem(){
	
		 if(!addPriceSystemForm.form.isValid()){
			 return;
		 }
	    var priceSystemName = Ext.getCmp('priceSystemName').getValue();
	    var priceSystemPeople = Ext.getCmp('priceSystemPeople').getValue();
	    var priceSystemValue = Ext.getCmp('priceSystemValue').getValue();
	    var priceSystemUnit = Ext.getCmp('priceSystemUnit').getValue();
	    var scId =Ext.getCmp('priceSystemConfig').getValue();
	    Ext.getCmp('addPriceSystemBtn').setDisabled(true);
	    var progress=Ext.Ajax.request({
	        url:path+"/../application_mgmt/application!savePriceSysteminfo.action",
	        method:'POST',
	        params:{
	        	'appPriceSystemVo.appId': appId,
	            'appPriceSystemVo.name': priceSystemName,
	            'appPriceSystemVo.peopleNum':priceSystemPeople,
	            'appPriceSystemVo.price':priceSystemValue,
	            'appPriceSystemVo.configId':scId,
	            'appPriceSystemVo.unit':priceSystemUnit
	            
	        },
	        success:function(form,action){
	        	Ext.getCmp('addPriceSystemBtn').setDisabled(false);
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
	                    fn: reLoadPriceSystemData
	                });
	                priceSystemStore.load();
	                addPriceSystemForm.getForm().reset();
	                addPriceSystemWin.hide();
	        },   
	        failure:function(form,action){
	        	Ext.getCmp('addPriceSystemBtn').setDisabled(false);
	            Ext.MessageBox.show({
	                title: i18n._('errorNotice'),
	                msg: i18n._('operateFail'),
	                buttons: Ext.MessageBox.OK,
	                icon: Ext.MessageBox.ERROR
	            });  
	        }
	    });
}

//操作完成后点确认刷新数据操作
function reLoadPriceSystemData(){
	priceSystemStore.load();
};

//删除后刷新页面
function afterDeletePriceSystem(){
    var count = priceSystemStore.data.length;
    var total = priceSystemStore.getTotalCount();
    if(total != 1 && count == 1) {
    	priceSystemStore.previousPage();
    } else {
    	priceSystemStore.load();
    }
}

var addPriceSystemForm=Ext.create('Ext.form.FormPanel', {
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
			xtype:'textfield',
			fieldLabel:'名称',
			style:'margin-left:10px;margin-right:70px',
			name:'priceSystemName',
			maxLength: 20,
			enforceMaxLength:true,
			allowBlank: false,
			id:'priceSystemName'
		},
		{
			xtype:'textfield',
			fieldLabel:'小于等于',
			style:'margin-left:10px;margin-right:70px',
			name:'priceSystemPeople',
			maxLength: 20,
			enforceMaxLength:true,
			allowBlank: false,
			id:'priceSystemPeople'
		},
		{
			xtype:'textfield',
			fieldLabel:'单位',
			style:'margin-left:10px;margin-right:70px',
			name:'priceSystemUnit',
			maxLength: 3,
			enforceMaxLength:true,
			allowBlank: false,
			id:'priceSystemUnit'
		},
		{
			xtype:'textfield',
			fieldLabel:'元/月',
			style:'margin-left:10px;margin-right:70px',
			name:'priceSystemValue',
			maxLength: 20,
			enforceMaxLength:true,
			allowBlank: false,
			id:'priceSystemValue'
		},
		/*{
	    	xtype:'panel',
	    	style:'margin-left:10px;margin-right:70px',
	    	border:false,
	    	fieldLabel:'推荐配置',
	    	width:300,
	    	height:30,
	    	layout:'hbox',
	    	items:[scCombox]
	   },*/
	  {
	     	   
  			fieldLabel:'推荐配置',
  			name:'推荐配置',
  			xtype:'combo',
  			style:'margin-left:10px;margin-right:70px',
  			id:'priceSystemConfig',
  			mode: 'local',
  			editable: false,
  			emptyText:'请选择',
  			displayField: 'name',
  			valueField: 'id',
  			queryMode: 'local',
  			allowBlank: false,
  			store:scStore,
  			listeners:{
  		    	select:function( combo, records, eOpts ){
  		    		if(combo != null || combo != ""){
  		    			Ext.getCmp('configMes').setText(records[0].data.description);
  		    		}
  		    	}
  		    }
 	    },
       {
	       xtype:'label',
           fieldLabel:'详细配置',
           style:'margin-left:30px;word-break: break-all; word-wrap:break-word;',
           id:'configMes',
           name:'configMes'
       }],
    buttons:[
        {text:"保存",id:'addPriceSystemBtn', handler:savePriceSystem},
        {text:i18n._('reset'), handler:function(){
			        	addPriceSystemForm.getForm().reset();
					    Ext.getCmp('configMes').setText('');
					    }
        }
    ]
    
});
//添加价格体系弹出窗
var addPriceSystemWin = Ext.create('Ext.window.Window', {
	width : 440,// 400
	height : 400,
	closable : false,
	constrain : true,
	modal : true,
	tools : [ {
		type : 'close',
		handler : function() {
			addPriceSystemForm.getForm().reset();
			addPriceSystemWin.hide();
		}
	} ],
	layout : 'fit',
	defaults : {
		split : false
	},
	items : [addPriceSystemForm]
});

function getCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return unescape(arr[2]);
    return null;
};
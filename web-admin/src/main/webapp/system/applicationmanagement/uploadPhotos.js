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
//上传界面截图列表模板
var  uploadPhotosModel = Ext.define('uploadPhotosModel', {
	extend : 'Ext.data.Model',
	fields : [ 'id', 'fileName', 'path',{name:'createTime',type: 'date', dateFormat: 'time'}],//
	idProperty : 'id'
});

//上传界面截图列表Store
var uploadPhotosStore = Ext.create('Ext.data.Store', {
	model : 'uploadPhotosModel',
	pageSize : 16,//每页显示16条数据
	sorters : [ {
		property : 'id',
		direction : 'DESC'
	} ],
	autoLoad : true,
	proxy : new Ext.data.proxy.Ajax({
		url : path + '/../application_mgmt/application!findAppUploadPhotosListPage.action',
		reader : {
			type : 'json',
			root : 'resultObject.result',
			totalProperty : 'resultObject.totalCount'
		},
		listeners:{
			exception:function(reader, response, error, eOpts){
				v_mask.hide();
				ajaxException(reader, response, error, eOpts );
			}
		}
	}),
	listeners : {
		beforeload : function(uploadPhotosStore,operation, eOpts ){	
			v_mask = new Ext.LoadMask(Ext
					.getBody(), {
				msg : i18n._('please wait'),
				removeMask : true			
			});
			v_mask.show();
		},
		load : function(uploadPhotosStore, records, successful, eOpts ){
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
//长传应用镜像列表
var uploadPhotosGrid = Ext.create('Ext.grid.Panel',{
	autoWidth : true,
	store : uploadPhotosStore,
	simpleSelect : true,
	selModel : Ext.create('Ext.selection.RowModel'),					
	selType : 'cellmodel',
	iconCls: 'icon-grid',
	columnLines: true,
	bbar : Ext.create('Ext.toolbar.Paging', {
		store : uploadPhotosStore,
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
					text: '<font id="addUploadPhotos" color="#ffffff" >' + i18n._('Create') + '</font>',//创建
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("addUploadPhotos").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("addUploadPhotos").style.color = "black";
						 }
					},
					icon : 'images/add.png',
					handler : function(){
						if(appId==''){
							Ext.MessageBox.show({
								title : i18n._('notice'),
								msg :"请先添加应用基本信息！",
								icon : Ext.MessageBox.INFO,
								buttons : Ext.MessageBox.OK,
								fn: afterDeleteCouponPolicy
							});	
						}else{
							var count = uploadPhotosStore.data.length;
							if(count<5){
								addUploadPhotosForm.getForm().reset();
								addUploadPhotosWin.setTitle("上传界面截图");									
								addUploadPhotosWin.show();
							}else{
								Ext.MessageBox.show({
									title : i18n._('notice'),
									msg :"最多只能添加5张界面截图！",
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
					text: '<font id="deleteUploadPhotos" color="#ffffff" >' + i18n._('delete') + '</font>',//删除	
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("deleteUploadPhotos").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("deleteUploadPhotos").style.color = "black";
						 }
							
					},
					icon : 'images/del.png',
					handler : function() {
						getSessionUser();
						//删除时长管理
						var rows = uploadPhotosGrid.getSelectionModel().getSelection();
						var Id;
						var photoPath;
						if (rows.length > 0) {
							Id = rows[0].get('id');
							photoPath = rows[0].get('path');
							Ext.MessageBox.show({				                  
								title: i18n._('notice'),
								msg:i18n._('Are you sure to delete'),
								buttons: Ext.MessageBox.YESNO,
								icon: Ext.MessageBox.QUESTION,
								fn:function(e){
									if(e=='yes'){
										Ext.Ajax.request({
											url:path+"/../application_mgmt/application!delUploadPhoto.action",
											method: 'POST',
											params:{
												'material.id':Id,
												'material.path':photoPath
											},
											success: function (response) {
												var result=Ext.JSON.decode(response.responseText);
												if(result.success==true){
													Ext.MessageBox.show({
														title : i18n._('notice'),
														msg : i18n._('Operating')+i18n._('Successful'),
														icon : Ext.MessageBox.INFO,
														buttons : Ext.MessageBox.OK,
														fn: afterDeletePhotos
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
			        		text : '名称',
			        		flex : 0.4,
			        		sortable: true,
			        		dataIndex : 'fileName'
			        	},{
			        		text :'创建时间',
			        		flex : 0.5,
			        		sortable: true,
			        		dataIndex : 'createTime',
			        		renderer: Ext.util.Format.dateRenderer("Y-m-d H:i:s")
			        	}
			        	]
});
//删除后刷新页面
function afterDeletePhotos(){
    var count = uploadPhotosStore.data.length;
    var total = uploadPhotosStore.getTotalCount();
    if(total != 1 && count == 1) {
    	uploadPhotosStore.previousPage();
    } else {
    	uploadPhotosStore.load();
    }
}
//优惠政策表单
var addUploadPhotosForm=Ext.create('Ext.form.FormPanel', {
    width: '440',
    height: '400',
    border:false,
    bodyPadding : 10,
    autoScroll:true,
    fileUpload:true,
    fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 80,
		anchor : '100%'
	},  
    items: [
       {
			xtype:'filefield',
			fieldLabel:'界面截图',
			style:'margin-left:10px;margin-right:70px',
			name:'file',
			allowBlank: false,
			id:'photosFile',
			msgTarget: 'side',
			buttonText: i18n._('browse')
		}
       ]
});

function showOk(title, msg) {
    Ext.MessageBox.show({
        title: title,
        msg: msg,
        icon:Ext.MessageBox.INFO,
        buttons: Ext.MessageBox.OK
    });
}
var myVar = null;

function waitForUpload(fileName) {
	Ext.Ajax.request({
        url: path+'/../image/image!waitForUpload.action',
        params: {
            "fileName": fileName
        },
        success: function(response){
            var json = Ext.decode(response.responseText);
            if(json.success){
            	Ext.MessageBox.updateProgress(json.resultObject / 100, json.resultObject + '%');
            }
        }
    });
}

//添加应用界面截图
var addUploadPhotosWin = Ext.create('Ext.window.Window', {
	width : 440,// 400
	height : 200,
	closable : false,
	constrain : true,
	modal : true,
	tools : [ {
		type : 'close',
		handler : function() {
			addUploadPhotosForm.getForm().reset();
			addUploadPhotosWin.hide();
		}
	} ],
	layout : 'fit',
	defaults : {
		split : false
	},
	items : [addUploadPhotosForm],
	buttons: [{  
        text: i18n._('upload'),  
        handler: function() {
            if(addUploadPhotosForm.form.isValid()){
            	var fileName = Ext.getCmp('photosFile').getValue();
            	addUploadPhotosWin.hide();
            	addUploadPhotosForm.getForm().submit({      
              	    url:path+"/../application_mgmt/upload!saveUploadPhotosInfo.action",
              	    method:"POST", 
              	    params: {
	                   "appId": appId
	                },
                    success: function(form, action){
                    	 clearInterval(myVar);
                         Ext.MessageBox.updateProgress(0, '0%');
                         showOk(i18n._('Prompt'), i18n._('uploadSuccess'));
                         uploadPhotosStore.load();
                    },      
                    failure: function(form, action){      
                         Ext.Msg.alert('Error', action.result.msg);
                    }  
                });
                 Ext.MessageBox.show({   
                    title: i18n._('uploading'),   
                    width:240,   
                    progress:true,   
                    closable:false
                }); 
                 myVar=setInterval(function(){waitForUpload(fileName)},1000);
             }  
         }  
      },{  
        text: i18n._('Cancel'),
        handler:function(){addUploadPhotosWin.hide();}  
      }]  
});

//操作完成后点确认刷新数据操作
function reLoadPhotosData(){
	uploadPhotosStore.load();
};


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
var couponPolicyModel = Ext.define('couponPolicyVo', {
	extend : 'Ext.data.Model',
	fields : [ 'id', 'appId', {name:'startTime',type: 'date', dateFormat: 'time'},{name:'endTime',type: 'date', dateFormat: 'time'},'couponType', 'couponValue'
	],
	idProperty : 'id'
});


//价格体系列表Store
var couponPolicyStore = Ext.create('Ext.data.Store', {
	model : 'couponPolicyVo',
	pageSize : 16,//每页显示16条数据
	sorters : [ {
		property : 'id',
		direction : 'DESC'
	} ],
	autoLoad : true,
	proxy : new Ext.data.proxy.Ajax({
		url : path + '/../application_mgmt/application!findAppCouponPolicyPage.action',
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
		beforeload : function(couponPolicyStore,operation, eOpts ){	
			//遮罩层
			v_mask = new Ext.LoadMask(Ext
					.getBody(), {
				msg : i18n._('please wait'),
				removeMask : true			
			});
			v_mask.show();
		},
		load : function(couponPolicyStore, records, successful, eOpts ){
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

var couponTypeStore = new Ext.data.JsonStore( {  
    fields : [ 'id', 'name' ],  
    data : [ {  
        id : '0',  
        name : '折扣'  
    }, {  
        id : '1',  
        name : '固值'  
    }]  
}); 

var couponPolicyGrid = Ext.create('Ext.grid.Panel',{
	autoWidth : true,
	store : couponPolicyStore,
	simpleSelect : true,
	selModel : Ext.create('Ext.selection.RowModel'),					
	selType : 'cellmodel',
	iconCls: 'icon-grid',
	columnLines: true,
	bbar : Ext.create('Ext.toolbar.Paging', {
		store : couponPolicyStore,
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
					text: '<font id="addCouponPolicy" color="#ffffff" >' + i18n._('Create') + '</font>',//创建
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("addCouponPolicy").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("addCouponPolicy").style.color = "black";
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
							addCouponPolicyForm.getForm().reset();
							addCouponPolicyWin.setTitle("添加优惠政策");//创建虚拟机										
							addCouponPolicyWin.show();
						}
					}
		         },
				{
					xtype : 'button',
					text: '<font id="deleteCouponPolicy" color="#ffffff" >' + i18n._('delete') + '</font>',//删除	
					listeners: {
						 "mouseout" : function() {
							 document.getElementById("deleteCouponPolicy").style.color = "white";
						 },
						 "mouseover" : function() {
							 document.getElementById("deleteCouponPolicy").style.color = "black";
						 }
							
					},
					icon : 'images/del.png',
					handler : function() {
						getSessionUser();
						//删除价格体系
						var rows = couponPolicyGrid.getSelectionModel().getSelection();
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
											url:path+"/../application_mgmt/application!delCouponPolicy.action",
											method: 'POST',
											params:{
												'appCouponPolicyVo.id':Id
											},
											success: function (response) {
												var result=Ext.JSON.decode(response.responseText);
												if(result.success==true){
													Ext.MessageBox.show({
														title : i18n._('notice'),
														msg : i18n._('Operating')+i18n._('Successful'),
														icon : Ext.MessageBox.INFO,
														buttons : Ext.MessageBox.OK,
														fn: afterDeleteCouponPolicy
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
					
	columns : [ Ext.create('Ext.grid.PageRowNumberer',{flex : 0.2,width:50}),
	{
		text :'id',
		flex : 0.4,
		hidden:true,
		dataIndex : 'id'
	},{
		text : '开始时间',
		flex : 0.4,
		sortable: true,
		dataIndex : 'startTime',
		renderer: Ext.util.Format.dateRenderer("Y-m-d")
	},{
		text :'结束时间',
		flex : 0.5,
		sortable: true,
		dataIndex : 'endTime',
		renderer: Ext.util.Format.dateRenderer("Y-m-d")
	}, {
		text :'优惠类型',//操作系统
		flex : 0.4,
		sortable: true,
		dataIndex : 'couponType',
		renderer:function(value){
			if(value== '0'){
				return "折扣";
			}else if(value== '1'){
				return "固值";
			}
		}
		
	},{
		text :'优惠值',
		flex : 0.4,
		sortable: true,
		dataIndex : 'couponValue'
	}
	]
});// 创建grid

//删除后刷新页面
function afterDeleteCouponPolicy(){
    var count = couponPolicyStore.data.length;
    var total = couponPolicyStore.getTotalCount();
    if(total != 1 && count == 1) {
    	couponPolicyStore.previousPage();
    } else {
    	couponPolicyStore.load();
    }
}


//保存优惠政策
function saveCouponPolicy(){
	 if(!addCouponPolicyForm.form.isValid()){
		 return;
	 }
	    var startTime = Ext.getCmp('startTime').getValue();
	    var endTime = Ext.getCmp('endTime').getValue();
	    var couponType = Ext.getCmp('couponType').getValue();
	    var couponValue = Ext.getCmp('couponValue').getValue();
	    var flag=true;
	    if(startTime>endTime){
	    	Ext.MessageBox.show({
            	title: i18n._('notice'),
                msg: '开始时间不能大于结束时间！',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO,
                fn: reLoadDrationData
            });
	    	return;
	    }
	    var type="^[1-9]([0-9])*$"; 
	    var re =new RegExp(type); 
	    if(!re.test(couponValue)){
	    	Ext.MessageBox.show({
            	title: i18n._('notice'),
                msg: '只能输入正整数！',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO,
                fn: reLoadDrationData
            });
	    	return;
	    }
	    if(couponType==0&&couponValue>=100){
	    	Ext.MessageBox.show({
            	title: i18n._('notice'),
                msg: '折扣需要小于100！',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO,
                fn: reLoadDrationData
            });
	    	return;
	    }
	    if(couponType==1){
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
					}else if(couponValue>result.resultObject){
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
	    	 Ext.getCmp('addCouponPolicyBtn').setDisabled(true);
	 	    var progress=Ext.Ajax.request({
	 	        url:path+"/../application_mgmt/application!saveCouponPolicyinfo.action",
	 	        method:'POST',
	 	        async:false,
	 	        params:{
	 	        	'appCouponPolicyVo.appId': appId,
	 	            'appCouponPolicyVo.startTime': startTime,
	 	            'appCouponPolicyVo.endTime':endTime,
	 	            'appCouponPolicyVo.couponType':couponType,
	 	            'appCouponPolicyVo.couponValue':couponValue
	 	        },
	 	        success:function(form,action){
	 	        	Ext.getCmp('addCouponPolicyBtn').setDisabled(false);
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
	 	                    fn: reLoadCouponPolicyData
	 	                });
	 	                addCouponPolicyForm.getForm().reset();
	 	                addCouponPolicyWin.hide();
	 	        },   
	 	        failure:function(form,action){
	 	        	Ext.getCmp('addCouponPolicyBtn').setDisabled(false);
	 	            Ext.MessageBox.show({
	 	                title: i18n._('errorNotice'),
	 	                msg: i18n._('operateFail'),
	 	                buttons: Ext.MessageBox.OK,
	 	                icon: Ext.MessageBox.ERROR
	 	            });  
	 	        }
	 	    });
	    }
}


//优惠政策表单
var addCouponPolicyForm=Ext.create('Ext.form.FormPanel', {
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
        fieldLabel:'开始时间',
        style:'margin-left:10px;margin-right:70px',
        id:'startTime',
        name:"startTime",
        xtype:"datefield",
        editable: false,
        allowBlank: false,
        format:'Y-m-d',
        value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
       },
       {
           fieldLabel:'结束时间',
           style:'margin-left:10px;margin-right:70px',
           id:'endTime',
           name:"endTime",
           xtype:"datefield",
           editable: false,
           allowBlank: false,
           format:'Y-m-d',
           value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
       },
       {
            fieldLabel:'优惠类型',
            style:'margin-left:10px;margin-right:70px',
			name:'couponType',
			xtype:'combo',
			allowBlank: false,
			id:'couponType',
			mode: 'local',
			editable: false,
			emptyText:'请选择',
			displayField: 'name',
			valueField: 'id',
			queryMode: 'local',
			store:couponTypeStore
       },
       {
			xtype:'textfield',
			fieldLabel:'值(%或元)',
			style:'margin-left:10px;margin-right:70px',
			name:'couponValue',
			maxLength: 20,
			enforceMaxLength:true,
			allowBlank: false,
			//validator : vd,
			id:'couponValue'
		}
       
       ],
    buttons:[
        {text:"保存", id:'addCouponPolicyBtn', handler:saveCouponPolicy},
        {text:i18n._('reset'), handler:function(){addCouponPolicyForm.getForm().reset();}}
    ]
    
});

//优惠政策
var addCouponPolicyWin = Ext.create('Ext.window.Window', {
	width : 440,// 400
	height : 400,
	closable : false,
	constrain : true,
	modal : true,
	tools : [ {
		type : 'close',
		handler : function() {
			addCouponPolicyForm.getForm().reset();
			addCouponPolicyWin.hide();
		}
	} ],
	layout : 'fit',
	defaults : {
		split : false
	},
	items : [addCouponPolicyForm]
});


//操作完成后点确认刷新数据操作
function reLoadCouponPolicyData(){
	couponPolicyStore.load();
};

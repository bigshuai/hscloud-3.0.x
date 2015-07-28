/******************************vmCreateForm******************************************/
//params = getCookie("lang");
//i18n.set({
//	lang : params,
//	path : '../../resources'
//});
Ext.Loader.setConfig({
	enabled : true
});
var vmCreateDiskCreateCount = 0;
var vmCreateDiskArray = [];
var vmDeleteDiskFlag =false;
//创建虚拟机CPU数据Data
var vmCreateCPUData = [];
//创建虚拟机CPU数据Store
var vmCreateCPUStore = Ext.create('Ext.data.Store', {
	autoLoad : false,//true
	fields : [ 'id','serviceType','name','coreNum','status','model','frequency' ],
	proxy:new Ext.data.proxy.Ajax({
		//model: 'ServiceItem',
        //type: 'ajax',
        url : path+'/../sc/serviceItem!listServiceItem.action?serviceType=1',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        },
        listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
    }),
	//data : vmCreateCPUData,	
	listeners : {
		load : function(vmCreateCPUStore, records, successful, eOpts ){
			if(successful && vmCreateCPUStore.getCount()>0){
				vmCreateCPUComb.setValue(vmCreateCPUStore.getAt(0).get('id'));
			}
		}
	}
});
//创建虚拟机CPU下拉列表框
var vmCreateCPUComb = Ext.create('Ext.form.ComboBox', {
	fieldLabel : i18n._('CPU'),//
	width : 360,
	listConfig:{maxHeight:200},
	labelWidth : 80,
	editable : false,
	allowBlank:false,
	store : vmCreateCPUStore,
	//queryMode : 'local',
	displayField : 'name',
	valueField : 'id'
});
//创建虚拟机Image数据Data
var vmCreateImageData = [];
//创建虚拟机Image数据Store
var imageid = "";
var vmCreateImageStore = Ext.create('Ext.data.Store', {
	fields : [ 'id','serviceType','name','imageId', 'imageSize','status' ],
	proxy:new Ext.data.proxy.Ajax({
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../sc/serviceItem!listServiceItem.action?serviceType=4',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        },
        listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
    }),
	data : vmCreateImageData,
	autoLoad : false,//true
	listeners : {
		load : function(vmCreateImageStore, records, successful, eOpts ){
			if(successful && vmCreateImageStore.getCount()>0){
				vmCreateImageComb.setValue(vmCreateImageStore.getAt(0).get('id'));
				//imageid = vmCreateImageStore.getAt(0).get('imageId');
			}
		}
	}
});
//创建虚拟机Image下拉列表框
var vmCreateImageComb = Ext.create('Ext.form.ComboBox', {
	fieldLabel : i18n._('OS'),//
	width : 360,
	listConfig:{maxHeight:200},
	labelWidth : 80,
	editable : false,
	allowBlank:false,
	store : vmCreateImageStore,
	queryMode : 'local',
	displayField : 'name',
	valueField : 'id'//imageId
});
//创建虚拟机Ram数据Store
var vmCreateRamStore = Ext.create('Ext.data.Store', {
	fields : [ 'id','serviceType','size','model','status' ],
	proxy:new Ext.data.proxy.Ajax({
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../sc/serviceItem!listServiceItem.action?serviceType=2',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        },
        listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
    }),
    sorters: [
              {
                  property : 'size',
                  direction: 'ASC'
              }
          ],
	remoteSort:true,
	autoLoad : false,//true
    listeners : {
		load : function(vmCreateRamStore, records, successful, eOpts ){
			if(successful && vmCreateRamStore.getCount()>0){
				var ramMaxValue = 1;
				if(vmCreateRamStore.getCount()>1){
					ramMaxValue = vmCreateRamStore.getCount()-1;
				}
				vmCreateRAMField.setMaxValue(ramMaxValue);
				vmCreateRAMDisplayField.setValue(vmCreateRamStore.getAt(0).get('size')+'M');
				vmCreateRAMField.setRawValue(vmCreateRamStore.getAt(0).get('size'));
			}else{
				createVMWin.hide();
				Ext.MessageBox.show({
					title : i18n._('notice'),
					msg : i18n._('There is no available memory'),//没有可用的磁盘
					icon : Ext.MessageBox.WARNING,
					buttons : Ext.MessageBox.OK
				});
			}
		}
	}
});
//创建虚拟机Memory显示值
var vmCreateRAMDisplayField = Ext.create('Ext.form.field.Display', {
	//width : 50,
	margin :'0 15 0 5',
	value : 0
});
//创建虚拟机Memory滑块
var vmCreateRAMField = Ext.create('Ext.slider.Single', {
	name : 'RAM',
	fieldLabel : i18n._('MEM'),//
	labelWidth : 80,
	width : 320,
	increment : 1,
	minValue : 0,// 1
	// maxValue: 10,
	value : 0,// 1
	tipText : function(thumb) {
		return Ext.String.format('<b>{0}</b>',vmCreateRamStore.getAt(thumb.value).get('size')+'M');
	},
	listeners : {
		'change' : {
			fn : function() {
				vmCreateRAMDisplayField.setValue(vmCreateRamStore.getAt(this.getValue()).get('size')+'M');
			}
		}
	}
});
//创建虚拟机Memory容器
var vmCreateRAMFieldContainer = Ext.create('Ext.form.FieldContainer', {	
	layout : 'hbox',
	items : [ vmCreateRAMField, vmCreateRAMDisplayField/*, {
		xtype : 'displayfield',
		width : 250,
		value : i18n._('Please selection memory size by slip')//请滑动滑条选择内存容量大小。
	}*/ ]
});
//创建虚拟机Disk数据Store
var vmCreateDiskStore = Ext.create('Ext.data.Store', {
	fields : [ 'id','serviceType','capacity','model','status' ],
	proxy:new Ext.data.proxy.Ajax({
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../sc/serviceItem!listServiceItem.action?serviceType=3',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        },
        listeners:{
			exception:function(reader, response, error, eOpts){
				ajaxException(reader, response, error, eOpts );
			}
		}
    }),
    sorters: [
              {
                  property : 'capacity',
                  direction: 'ASC'
              }
          ],
	remoteSort:true,
	autoLoad : false,//true
    listeners : {
		load : function(vmCreateDiskStore, records, successful, eOpts ){
			if(successful && vmCreateDiskStore.getCount()>0){
				var diskMaxValue = 1;
				if(vmCreateDiskStore.getCount()>1){
					diskMaxValue = vmCreateDiskStore.getCount()-1;
				}
				vmCreateDiskField.setMaxValue(diskMaxValue);
				vmCreateDiskDisplayField.setValue(vmCreateDiskStore.getAt(0).get('capacity')+'G');
				vmCreateDiskField.setRawValue(vmCreateDiskStore.getAt(0).get('capacity'));
			}else{
				createVMWin.hide();
				vmCreateDiskField.setDisabled(true);
				Ext.MessageBox.show({
					title : i18n._('notice'),
					msg : i18n._('There is no available disk'),//没有可用的磁盘
					icon : Ext.MessageBox.WARNING,
					buttons : Ext.MessageBox.OK
				});
			}
		}
	}
});
//创建虚拟机Disk显示值
var vmCreateDiskDisplayField = Ext.create('Ext.form.field.Display', {
	//width : 50,
	margin :'0 15 0 0',
	value : 0
});
//创建虚拟机Disk滑块
var vmCreateDiskField = Ext.create('Ext.slider.Single', {
	name : 'Disk',
	fieldLabel : i18n._('Disk'),//	
	labelWidth : 80,
	width : 210,
	increment : 1,
	minValue : 0,// 1
	// maxValue: 10,
	value : 0,// 1
	tipText : function(thumb) {
		return Ext.String.format('<b>{0}</b>', vmCreateDiskStore.getAt(thumb.value).get('capacity')+'G');
	},
	listeners : {
		'change' : {
			fn : function() {
				vmCreateDiskDisplayField.setValue(vmCreateDiskStore.getAt(this.getValue()).get('capacity')+'G');
			}
		}
	}
});
//创建虚拟机删除Disk按钮
var vmCreateDeleteDiskButton = Ext.create('Ext.Button', {
	text : '×',
	width: 20,
	height:20,
	disabled:true,
	//renderTo : Ext.getBody(),
	handler : function() {
		vmCreateDiskField.setValue(0);
		vmCreateDiskField.setDisabled(true);		
	}
});
//创建虚拟机Disk容器
var vmCreateDiskFieldContainer = Ext.create('Ext.form.FieldContainer', {
	layout : 'hbox',
	items : [ vmCreateDiskField, vmCreateDiskDisplayField/*, vmCreateDeleteDiskButton, {
		xtype : 'displayfield',
		width : 100,
		margin :'0 5 0 5',
		value : i18n._('Delete the disk')//删除该盘
	}, {
		xtype : 'displayfield',
		width : 250,
		value : i18n._('Click on the button to delete extended hard disk')//点击按钮删除扩展硬盘
	}*/ ]
});
var addDiskCount=0;
//创建虚拟机扩展Disk数据Store
var vmCreateExtDiskStore = Ext.create('Ext.data.Store', {
	fields : [ 'id','serviceType','capacity','model','status' ],
	proxy:new Ext.data.proxy.Ajax({
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../sc/serviceItem!listServiceItem.action?serviceType=8',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        },
        listeners:{
			exception:function(reader, response, error, eOpts){
				v_mask.hide();
				if(error.hasException()){
					Ext.MessageBox.show({
						title : i18n._('errorNotice'),
						msg : i18n._('responseError'),
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
				}
				if(response.responseText!=undefined){
					var obj = Ext.decode(response.responseText);
					if (obj==null || !obj.success) {
						Ext.MessageBox.show({
							title : i18n._('errorNotice'),
							msg : obj.resultMsg,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});											
					}
				}
			}
		}
    }),
    sorters: [
              {
                  property : 'capacity',
                  direction: 'ASC'
              }
          ],
	remoteSort:true,
	autoLoad : false//true    
});
//创建虚拟机添加Disk按钮
var vmCreateCreateDiskButton = Ext.create('Ext.Button', {
	text : '+',
	width: 20,
	height:20,
	renderTo : Ext.getBody(),
	handler : function() {
		if(vmCreateExtDiskStore.getCount()<=0){
			Ext.MessageBox.show({
				title : i18n._('notice'),
				msg : i18n._('There is no available expansion disk'),//没有可用的扩展磁盘
				icon : Ext.MessageBox.WARNING,
				buttons : Ext.MessageBox.OK
			});
			return;
		}			
		addDiskCount = addDiskCount + 1;		
		//alert('**'+vmCreateDiskArray);
		//alert('**add'+addDiskCount);
		if(addDiskCount>4){
			addDiskCount = addDiskCount - 1;
			Ext.MessageBox.show({
				title : i18n._('notice'),
				msg : i18n._('addExtDiskTip'),
				icon : Ext.MessageBox.INFO,
				buttons : Ext.MessageBox.OK
			});
			return;
		}
		vmCreateDiskCreateCount = vmCreateDiskCreateCount + 1;
		vmCreateDiskArray.push(vmCreateDiskCreateCount);
		var vmCreateDiskCreateDisplayField2 = Ext.create('Ext.form.field.Display', {
			id : 'diskDisplay' + vmCreateDiskCreateCount,
			width : 50
			//value : 0
		});
		var vmCreateDiskCreateField2 = Ext.create('Ext.slider.Single', {
			id : 'diskSlider' + vmCreateDiskCreateCount,
			name : 'DiskCreate',
			fieldLabel : i18n._('extDisk'),//
			labelWidth : 80,
			width : 210,
			increment : 1,
			minValue : 0,// 1
			maxValue : vmCreateExtDiskStore.getCount()-1,
			value : 0,// 1
			tipText : function(thumb) {
				return Ext.String.format('<b>{0}</b>', vmCreateExtDiskStore.getAt(thumb.value).get('capacity')+'G');
			},
			listeners : {
				'change' : {
					fn : function() {
						vmCreateDiskCreateDisplayField2.setValue(vmCreateExtDiskStore.getAt(this.getValue()).get('capacity')+'G');
					}
				}
			}
		});
		var vmCreateDeleteDiskButton2 = Ext.create('Ext.Button', {
			itemId:vmCreateDiskCreateCount,
			text : '×',
			width: 20,
			height:20,			
			//renderTo : Ext.getBody(),
			handler : function() {	
				addDiskCount = addDiskCount - 1;
				//alert('##'+vmCreateDeleteDiskButton2.getItemId());
				vmCreateDiskArray.removevalue(vmCreateDeleteDiskButton2.getItemId());
				//alert('**array'+vmCreateDiskArray);
				//alert('**del'+addDiskCount);
				//vmDeleteDiskFlag = true;
				Ext.getCmp('diskSlider' + vmCreateDeleteDiskButton2.getItemId()).setValue(0);
				Ext.getCmp('diskSlider' + vmCreateDeleteDiskButton2.getItemId()).setDisabled(true);
				createVMForm.remove(Ext.getCmp('diskContainer' + vmCreateDeleteDiskButton2.getItemId()),true);
				//vmCreateDiskCreateCount = vmCreateDiskCreateCount - 1;
				
			}
		});
		//alert('**'+Ext.getCmp('diskSlider' + vmCreateDeleteDiskButton2.getItemId()).getValue());
		Ext.getCmp('diskDisplay' + vmCreateDeleteDiskButton2.getItemId()).setValue(vmCreateExtDiskStore.getAt(Ext.getCmp('diskSlider' + vmCreateDeleteDiskButton2.getItemId()).getValue()).get('capacity')+'G');
		var vmCreateDiskCreateFieldContainer2 = Ext.create('Ext.form.FieldContainer', {
			id : 'diskContainer' + vmCreateDiskCreateCount,
			width : 400,
			layout : 'hbox',
			items : [ vmCreateDiskCreateField2, vmCreateDiskCreateDisplayField2,vmCreateDeleteDiskButton2,{
				xtype : 'displayfield',
				width : 100,
				margin :'0 0 0 5',
				value : i18n._('Delete the disk')//删除该盘
			},{
				xtype : 'displayfield',
				width : 200,
				value : i18n._('Click on the button to delete extended hard disk')//点击按钮删除扩展硬盘
			} ]//,deleteDiskButton2
		});
		createVMForm.add(vmCreateDiskCreateFieldContainer2);
	}
});
//创建虚拟机添加Disk容器
var vmCreateDiskAddFieldContainer = Ext.create('Ext.form.FieldContainer', {
	layout : 'hbox',
	items : [ {
		xtype : 'label',
		width : 80
	}, vmCreateCreateDiskButton,{
		xtype : 'displayfield',
		margin:'0 5 0 285',
		width : 250,
		value : i18n._('Add a new disk')
	} ]
});
//创建虚拟机虚拟机别名文本框
var vmCreateVmNameField = Ext.create('Ext.form.field.Text', {
	name : 'vmName',
	labelWidth : 80,
	fieldLabel : i18n._('vm_name'),// 主机名称
	allowBlank : false,
	regex:/^[\w-@#$%^ &*\u4e00-\u9fa5]+$/,
	regexText:i18n._('InvalidCharacters'),
	maxLength : 20,
	enforceMaxLength:true,
	width : 360,
	emptyText : i18n._('vm_name'),
	blankText:i18n._('Please input the VM name')
});
//创建虚拟机zone数据Data
var vmCreateZoneData = [];
//创建虚拟机zone数据Store
var vmCreateZoneStore = Ext.create('Ext.data.Store', {
	fields : [ 'id', 'name','code' ],
	proxy:{
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../sc/zone!getAllZones.action',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        }
    },
	data : vmCreateZoneData,
	autoLoad : false,//true
	listeners : {
		load : function(vmCreateZoneStore, records, successful, eOpts ){
			if(successful && vmCreateZoneStore.getCount()>0){
				vmCreateIPComb.store.removeAll();
				vmCreateHostComb.store.removeAll();
				//vmCreateHostComb.setValue(vmCreateHostStore.getAt(0).get('name'));
			}
		}
	}
});
//创建虚拟机zone下拉列表框
var vmCreateZoneComb = Ext.create('Ext.form.ComboBox', {
	fieldLabel : i18n._('Release zone'),//发布区域
	width : 360,
	listConfig:{maxHeight:200},
	labelWidth : 80,//100
	editable : false,
	store : vmCreateZoneStore,
	queryMode : 'local',
	displayField : 'name',
	valueField : 'id',
	emptyText : i18n._('Please select a publishing zone'),
	listeners : {
		select : function(combo, record, index) {
			vmCreateIPComb.store.load({params:{'serverZone.id':combo.value}});
			vmCreateHostComb.store.load({params:{'serverZone.id':combo.value}});
			vmCreateIPComb.setValue(null);
			vmCreateHostComb.setValue(null);
		}
	}
});
//创建虚拟机IP数据Data
var vmCreateIPData = [];
//创建虚拟机IP数据Store
var vmCreateIPStore = Ext.create('Ext.data.Store', {
	fields : [ 'id', 'ip' ],
	proxy:{
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../ip/findAllFreeIPDetail!findAllFreeIPDetail.action',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        }
    },
	data : vmCreateIPData,
	autoLoad : false,//true
	listeners : {
		load : function(vmCreateIPStore, records, successful, eOpts ){
			if(successful && vmCreateIPStore.getCount()>0){
				vmCreateIPComb.setValue(null);
			}
		}
	}
});
//创建虚拟机IP下拉列表框
var vmCreateIPComb = Ext.create('Ext.form.ComboBox', {
	width : 130,
	//editable : false,
	store : vmCreateIPStore,
	listConfig:{maxHeight:200},
	disabled:true,
	//allowBlank : false,
	regex : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,// 禁止输入空白字符
	regexText : i18n._('InvalidIP'),
	queryMode : 'local',
	displayField : 'ip',
	valueField : 'ip',
	emptyText : i18n._('Automatic distribution of IP')//自动分配IP
});
//创建虚拟机是否使用公网IP单选框
var vmCreateIpCheckRadioGroup = Ext.create('Ext.form.RadioGroup', {
	fieldLabel : i18n._('OuterIP'),//公网IP
	labelWidth : 80,
	width : 230,
	columns : 2,
	vertical : true,
	items : [ {		
		boxLabel : i18n._('nodeploy'),//不发布
		width : 80,
		name : 'rb',
		inputValue : '1',		
		checked : true
	}, {		
		boxLabel : i18n._('deploy'),//发布
		//width : 8,
		name : 'rb',
		inputValue : '2'		
	}	
	],
	listeners:{
		change:function(vmCreateIpCheckRadioGroup, newValue, oldValue, eOpts){			
			if(vmCreateIPComb.isDisabled()){
				vmCreateIPComb.setDisabled(false);
			}else{
				vmCreateIPComb.setDisabled(true);
			}						
		}
	}
});
//创建虚拟机节点数据Data
var vmCreateHostData = [];
//创建虚拟机节点数据Store
var vmCreateHostStore = Ext.create('Ext.data.Store', {
	fields : [ 'id', 'name','nodeAliases' ],
	proxy:{
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../sc/node!getAllNodesGroupByZone.action',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        }
    },
	data : vmCreateHostData,
	autoLoad : false,//true
	listeners : {
		load : function(vmCreateHostStore, records, successful, eOpts ){
			if(successful && vmCreateHostStore.getCount()>0){
				vmCreateHostComb.setValue(null);
			}
		}
	}
});
//创建虚拟机节点下拉列表框
var vmCreateHostComb = Ext.create('Ext.form.ComboBox', {
	fieldLabel : i18n._('Release node'),//发布节点
	width : 360,
	listConfig:{maxHeight:200},
	labelWidth : 80,//100
	editable : false,
	store : vmCreateHostStore,
	queryMode : 'local',
	displayField : 'nodeAliases',
	valueField : 'name',
	emptyText : i18n._('Please select a publishing node')
});

//创建虚拟机用户数据Data
var vmCreateUserData = [];
//创建虚拟机用户数据Store
var vmCreateUserStore = Ext.create('Ext.data.Store', {
	fields : [ 'id', 'name','email' ],
	proxy:{
		//model: 'ServiceItem',
        type: 'ajax',
        url : path+'/../admin_mgmt/userManagement!getAllAvailableUser.action',
		//extraParams:{serviceType:1},
		reader: {
            type: 'json',
			root:'resultObject',
			totalProperty: 'resultObject.totalCount'
        }
    },
	data : vmCreateUserData,
	autoLoad : false,//true
	listeners : {
		load : function(vmCreateUserStore, records, successful, eOpts ){
			if(successful && vmCreateUserStore.getCount()>0){
//				vmCreateUserComb.setValue(vmCreateUserStore.getAt(0).get('id'));
			}
		}
	}
});
//创建虚拟机用户数据下拉列表框
var vmCreateUserComb = Ext.create('Ext.form.ComboBox', {
	fieldLabel : i18n._('SpecifiedUser'),//指定用户
	width : 360,
	labelWidth : 100,
	editable : false,
	store : vmCreateUserStore,
	queryMode : 'local',
	displayField : 'name',
	valueField : 'id'
});
//创建虚拟机用户数据文本框
var vmCreateUserText = Ext.create('Ext.form.field.Text', {	
	name : 'ownerEmail',
	vtype: 'email',
	width : 360,
	labelWidth : 80,
	fieldLabel : i18n._('SpecifiedUser'),//指定用户	
	emptyText : i18n._('Please enter an email of owner'),
	listeners : {
		/*blur : function(vmCreateUserText, The, eOpts){
			if(vmCreateUserText.isValid() == false){
				return;
			}
			if(vmCreateUserText.getValue()!= null && vmCreateUserText.getValue()!=""){
				var proxy = vmCreateUserStore.getProxy();
				proxy.setExtraParam('query',vmCreateUserText.getValue());
				vmCreateUserStore.load();
			}
		},*/
		change : function(vmCreateUserText, newValue, oldValue, eOpts){
			if(newValue == oldValue || vmCreateUserText.isValid() == false){
				return;
			}
			if(vmCreateUserText.getValue()!= null && vmCreateUserText.getValue()!=""){
				var proxy = vmCreateUserStore.getProxy();
				proxy.setExtraParam('query',vmCreateUserText.getValue());
				vmCreateUserStore.load();
			}
		}
	}
});
// 创建虚拟机Form
var createVMForm = Ext.create('Ext.form.Panel', {
	frame : true,
	autoScroll :true,
	// title: 'Form Fields',
	//width: 500,
	bodyPadding : 5,
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 60,
		anchor : '100%'
	},
	items : [ {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		items : [ vmCreateVmNameField,{
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 250,
			value : i18n._('Please enter a vm name')//请输入一个虚拟机名称
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		items : [ vmCreateImageComb,{
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 250,
			value : i18n._('Choose an operating system')//请根据下拉列表选择操作系统类别
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		items : [ vmCreateCPUComb,{
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 250,
			value : i18n._('Please select CPU type')//请根据下拉列表选择所需处理器规格
		} ]
	}, vmCreateRAMFieldContainer,{
		xtype:'fieldcontainer',
		layout:'hbox',
		items:[vmCreateZoneComb,{
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 220,
			value : i18n._('Defaults to the main zone, can be manually modified distribution zone')//默认为主区域，可手动修改区域位置。
		}]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		items : [ vmCreateIpCheckRadioGroup, vmCreateIPComb,{
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 220,
			value : i18n._('You can choose to publish or not to publish public IP. The default is the automatic allocation of IP, can also be manually assigned IP.')//可选择发布公网IP或者不发布。默认为自动分配IP，也可手动分配IP。
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		items : [ vmCreateHostComb, {
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 220,
			value : i18n._('Defaults to the current node, can be manually modified distribution location')//默认为当前节点，可手动修改分配位置。
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		items : [ vmCreateUserText,{
			xtype : 'displayfield',
			margin:'0 5 0 25',
			width : 250,
			value : i18n._('Please enter an email of owner')//请输入一个用户的邮箱
		} ]
	},vmCreateDiskFieldContainer,vmCreateDiskAddFieldContainer//
	],
	buttons : [
			{
				text : i18n._('OK'),
				handler : function() {
					var vmName = Ext.String.trim(vmCreateVmNameField.getValue());
					//alert('vcpusid*'+vmCreateCPUComb.getValue());
					var vcpu = vmCreateCPUStore.getAt(vmCreateCPUStore.indexOfId(vmCreateCPUComb.getValue())).get('coreNum');
					var ram = vmCreateRamStore.getAt(vmCreateRAMField.getValue()).get('size');//vmCreateRAMField.getValue();
					var disk = vmCreateDiskStore.getAt(vmCreateDiskField.getValue()).get('capacity');//vmCreateDiskField.getValue();
					var osId = vmCreateImageComb.getValue();
					var imageId_ =vmCreateImageStore.getAt(vmCreateImageStore.indexOfId(osId)).get('imageId');
					var zoneId=vmCreateZoneComb.getValue();
					var zoneCode='';
					if(vmCreateZoneStore.getAt(vmCreateZoneStore.indexOfId(zoneId))!=null){
						zoneCode =vmCreateZoneStore.getAt(vmCreateZoneStore.indexOfId(zoneId)).get('code');
						if(vmCreateHostComb.store.count()<=0){
							Ext.MessageBox.show({
								title : i18n._('notice'),
								msg : i18n._('There is not one available node in this zone,can not create VM!'),//此区域里无可用的节点，无法创建虚拟机！
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});
							return;
						}
					}
					var ip = vmCreateIPComb.getValue();								
					var vcpusType = vmCreateCPUStore.getAt(vmCreateCPUStore.indexOfId(vmCreateCPUComb.getValue())).get('model');
					//alert('vcpusType*'+vcpusType);
					var ramType = vmCreateRamStore.getAt(vmCreateRAMField.getValue()).get('model');
					//alert('ramType*'+ramType);
					var diskType = vmCreateDiskStore.getAt(vmCreateDiskField.getValue()).get('model');
					//alert('diskType*'+diskType);
					var ipDeploy = '1';//发布IP
					if(vmCreateIPComb.isDisabled()){						
						ipDeploy = '0';//不发布IP
						ip = '';
					}else{
						//alert('@@'+ip);
						if(ip==null){
							ip = '';//自动分配IP
						}else{
							//如果不是自动分配ip，则查看输入的ip是否存在于ip池中
							if(vmCreateIPStore.find('ip',ip)==-1){
								Ext.MessageBox.show({
									title : i18n._('notice'),
									msg : i18n._('InvalidIP'),//输入IP无效
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});// INFO,QUESTION,WARNING,ERROR
								return;
							}
						}						
					}					
					var vmNode = vmCreateHostComb.getValue();
					if(vmNode == null){
						vmNode='';
//						if(zoneCode!='' || zoneCode!=null){
//							Ext.MessageBox.show({
//								title : i18n._('notice'),
//								msg : i18n._('There is no available node under this zone'),//此域下没有可用的节点
//								buttons : Ext.MessageBox.OK,
//								icon : Ext.MessageBox.ERROR
//							});// INFO,QUESTION,WARNING,ERROR
//							return;
//						}						
					}
					var vmUserEmail = vmCreateUserText.getValue();					
					var vmUser=0;
					//alert('**'+vmUserEmail);
					if(vmUserEmail != null && vmUserEmail != ''){						
						var vmUserIndex = vmCreateUserStore.find('email',vmUserEmail);						
						if(vmUserIndex <0){
							Ext.MessageBox.show({
								title : i18n._('notice'),
								msg : i18n._('Email does not exist'),//提示邮箱地址不存在
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});// INFO,QUESTION,WARNING,ERROR
							vmCreateUserText.focus();
							return;
						}else{
							vmUser = vmCreateUserStore.getAt(vmUserIndex).get('id');
						}
					}					
					var addDisk ='';					
					if (vmCreateDiskArray.length > 0) {
						for ( var i = 0; i < vmCreateDiskArray.length; i++) {	//vmCreateDiskCreateCount + 1
							if(vmCreateExtDiskStore.getAt(Ext.getCmp('diskSlider' + vmCreateDiskArray[i]).getValue()).get('capacity')>0){
								addDisk = addDisk
								+ vmCreateExtDiskStore.getAt(Ext.getCmp('diskSlider' + vmCreateDiskArray[i]).getValue()).get('capacity')+',';
							}else{
								Ext.MessageBox.show({
									title : i18n._('notice'),
									msg : i18n._('Please set the extended hard disk capacity'),//提示设置扩展盘大小
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});// INFO,QUESTION,WARNING,ERROR
								return;
							}							
						}
					}
					//alert(addDisk);
					// 遮罩层
					var v_mask = new Ext.LoadMask(Ext.getBody(), {
						msg : i18n._('please wait'),
						removeMask : true
					// 完成后移除
					});
					//alert('@@'+vmCreateVmNameField.getValue());
					if (vmName == null ||vmName=='') {
						Ext.MessageBox.show({
							title : i18n._('notice'),
							msg : i18n._('Please input the VM name'),//请输入输入主机名称
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.WARNING
						});// INFO,QUESTION,WARNING,ERROR
						vmCreateVmNameField.focus();
						return;
					}
					// 提交后台之前先做有效性验证
					if (!createVMForm.getForm().isValid()) {
						Ext.MessageBox.show({
							title : i18n._('notice'),
							msg : i18n._('Please input the correct value'),//输入有效值
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});// INFO,QUESTION,WARNING,ERROR
						return;
					} else {

						// if(dId==0){
						v_mask.show();
						var create = Ext.Ajax.request({
							url : path + '/../ops/ops!createVm.action',
							method : 'POST',
							jsonData: {
								'name' : vmName,
								'vcpus' : vcpu,
								'ram' : ram,
								'disk' : disk,
								'addDisk' : addDisk,
								'osId' : osId,
								'imageId' : imageId_,
								'ipDeploy' : ipDeploy,
								'floating_ip' : ip,
								'vmNode' : vmNode,
								'vcpusType':vcpusType,
								'ramType':ramType,
								'diskType':diskType,
								'owner':vmUser,
								'vmZone':zoneCode
							},
							success : function(form, action) {
								v_mask.hide();
								var obj = Ext.decode(form.responseText);
								if (obj == null || obj.success == null) {
									Ext.MessageBox.show({
										title : i18n._('errorNotice'),
										msg : i18n._('returnNull'),
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.ERROR
									});// INFO,QUESTION,WARNING,ERROR
									return;
								}
								if (!obj.success) {
									Ext.MessageBox.show({
										title : i18n._('errorNotice'),
										msg : obj.resultMsg,
										buttons : Ext.MessageBox.OK,
										icon : Ext.MessageBox.ERROR
									});
									return;
								}
								Ext.MessageBox.show({
									title : i18n._('notice'),
									msg : i18n._('operationMessage'),
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.INFO,
									fn: reLoadData
								});								
							},
							failure : function(form, action) {
								v_mask.hide();
								Ext.MessageBox.show({
									title : i18n._('errorNotice'),
									msg : i18n._('operateFail'),
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR,
									fn: reLoadData
								});
							}
						});	
						vmCreateDiskArray=[];
						createVMForm.getForm().reset();
						createVMWin.remove(createVMForm,false);
						createVMWin.hide();
						vmCreateDiskCreateCount=0;
						vmDeleteDiskFlag =false;
						// createVMWin.destroy();
					}

				}
			}, {
				text : i18n._('Cancel'),
				handler : function() {
					vmRefreshReset();//刷新设置重置
					vmCreateDiskArray=[];
					createVMForm.getForm().reset();
					createVMWin.remove(createVMForm,false);
					createVMWin.hide();
					vmCreateDiskCreateCount=0;
					vmDeleteDiskFlag =false;
					// createVMWin.destroy();
				}
			} ]
});
/* 
 *  方法:Array.remove(dx) 
 *  功能:根据元素值删除数组元素. 
 *  参数:元素值 
 *  返回:在原数组上修改数组 
 */  
Array.prototype.indexOf = function (val) {  
    for (var i = 0; i < this.length; i++) {  
        if (this[i] == val) {  
            return i;  
        }  
    }  
    return -1;  
};  
Array.prototype.removevalue = function (val) {  
    var index = this.indexOf(val);  
    if (index > -1) {  
        this.splice(index, 1);  
    }  
};
function resetCreateVMForm(){
	//alert('***'+createVMForm.query('fieldcontainer').length);
	for(var i=createVMForm.query('fieldcontainer').length;i>11;i--){	
		//alert(createVMForm.query('fieldcontainer')[i-1]);
		//alert(createVMForm.query('fieldcontainer')[i-1].is('fieldcontainer'));
		createVMForm.query('fieldcontainer')[i-1].removeAll(true);
		addDiskCount=0;
	}	
	vmCreateImageStore.load();
	vmCreateCPUStore.load();
	vmCreateRamStore.load();										
	vmCreateDiskStore.load();
	vmCreateExtDiskStore.load();
	vmCreateZoneStore.load();
	//vmCreateIPStore.load();
	//vmCreateHostStore.load();
	//vmCreateUserStore.load();
};
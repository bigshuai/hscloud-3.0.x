package com.hisoft.hscloud.crm.usermanager.constant;

public enum ResourceType {
	
	UNKNOW_RESOURCE((short)0,"UNKNOW_RESOURCE","","resource.unknow"),
	VM((short)1,"VM","com.hisoft.hscloud.vpdc.ops.entity.VpdcReference","resource.vm"),
	MENU((short)2,"MENU","com.hisoft.hscloud.crm.usermanager.entity.Menu","resource.menu"),
	IMAGE((short)3,"IMAGE","","resource.image"),
	USER((short)4,"USER","com.hisoft.hscloud.crm.usermanager.entity.User","resource.user"),
	USERGROUP((short)5,"USERGROUP","com.hisoft.hscloud.crm.usermanager.entity.UserGroup","resource.usergroup"),
	ROLE((short)6,"ROLE","com.hisoft.hscloud.crm.usermanager.entity.Role","resource.role"),
	RESOURCE((short)7,"RESOURCE","com.hisoft.hscloud.crm.usermanager.entity.Resource","resource.resource");
	
	private short index;
	private String type;
	private String entityName;
	private String i18n;
	
	
	ResourceType(short index,String type,String entityName,String i18n){
		
		this.index = index;
		this.type = type;
		this.i18n = i18n;
		this.entityName = entityName;
		
	}

	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	
	public static ResourceType getItem(short index){
		
		for (ResourceType resourcetype:ResourceType.values()) {
			if(resourcetype.index==index){
				return resourcetype;
			}
		}
		
		return ResourceType.UNKNOW_RESOURCE;
		
	}
	
	public static String getType(String entityName){
		
		for (ResourceType resourcetype:ResourceType.values()) {
			if(resourcetype.entityName.equals(entityName)){
				return resourcetype.type;
			}
		}
		return ResourceType.UNKNOW_RESOURCE.type;
	}
	


}

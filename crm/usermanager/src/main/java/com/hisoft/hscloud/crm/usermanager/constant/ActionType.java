package com.hisoft.hscloud.crm.usermanager.constant;

public enum ActionType {
	

	UNKNOW_ACTION((short)0,"UNKNOW_ACTION","action.unknow"),
	VM((short)1,"VM","resource.vm"),
	MENU((short)2,"MENU","resource.menu");
	
	private short index;
	private String type;
	private String i18n;
	
	
	ActionType(short index,String type,String i18n){
		
		this.index = index;
		this.type = type;
		this.i18n = i18n;
		
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

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	
	public static ActionType getItem(short index){
		
		for (ActionType actionType:ActionType.values()) {
			if(actionType.index==index){
				return actionType;
			}
		}
		
		return ActionType.UNKNOW_ACTION;
		
	}
	

}

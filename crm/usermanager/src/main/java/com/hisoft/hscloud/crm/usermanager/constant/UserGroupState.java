package com.hisoft.hscloud.crm.usermanager.constant;

public enum UserGroupState {
	
	FREEZE((short)0,"FREEZE","userstate.freeze"),
	ACTIVE((short)1,"ACTIVE","userstate.active"),
	DELETED((short)2,"DELETE","userstate.deleted"),
	UNKNOW_STATE((short)4,"UNKNOW_STATE","usertype.unknow");
	
	
	private short index;
	private String type;
	private String i18n;
	
	UserGroupState(short index,String type,String i18n){
		
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
	
	
	

}

package com.hisoft.hscloud.vpdc.ops.util;

/**
 * 虚拟机类型
 * TRY   试用
 * REGULAR   正式
 * @author Haibin
 *
 */
public enum VMTypeEnum {
	
	TRY(0,"TRY","vmtype.type"),
	REGULAR(1,"REGULAR","vmtype.regular"),
	FORNEED(2,"FORNEED","vmtype.forNeed");
	
	private int code;
	private String name;
	private String i18n;
	
	VMTypeEnum(int code,String name,String i18n){
		this.code = code;
		this.name = name;
		this.i18n = i18n;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getI18n() {
		return i18n;
	}
	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	
	
	

}

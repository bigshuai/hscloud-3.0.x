package com.hisoft.hscloud.bss.billing.constant;


/**
 * 0. 无服务 
 * 1. 云主机服务
 * 2. 云存储服务
 * 3. 云桌面服务
 * 4. 监控云服务
 * 5. 快递服务
 * @author Minggang
 *
 */
public enum ServiceType {
	
	SERVICE_NO((short)0,"SERVICE_NO","service.no"),
	SERVICE_CLOUD_COMPUTER((short)1,"SERVICE_COMPUTER","service.computer"),
	SERVICE_CLOUD_STORAGE((short)2,"SERVICE_STORAGE","service.storage"),
	SERVICE_CLOUD_DESK((short)3,"SERVICE_DESK","service.desk"),
	SERVICE_CLOUD_MONITOR((short)4,"SERVICE_MONITOR","service.monitor"),
	SERVICE_CLOUD_EXPRESS((short)5,"SERVICE_EXPRESS","service.express");
	
	private short index;
	
	private String type;
	
	private String i18n;
	
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

	ServiceType(short index,String type,String i18n){
		
		this.index = index;
		this.type = type;
		this.i18n = i18n;
		
	}

}

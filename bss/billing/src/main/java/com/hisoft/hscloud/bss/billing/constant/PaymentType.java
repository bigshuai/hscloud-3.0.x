package com.hisoft.hscloud.bss.billing.constant;

/**
 * ONLINE:线上
 * OFFLINE:线下
 * @author Minggang
 *
 */
public enum PaymentType {
	
	UNKNOW_PAYMENT((short)0,"UNKNOW_PAYMENT","payment.unknow"),
	PAYMENT_ONLINE((short)1,"PAYMENT_ONLINE","payment.online"),
	PAYMENT_OFFLINE((short)2,"PAYMENT_OFFLINE","payment.offline");
	
	private short index;
	private String type;
	private String i18n;
	
	
	PaymentType(short index,String type,String i18n){
		
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

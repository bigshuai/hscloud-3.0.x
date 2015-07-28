package com.hisoft.hscloud.bss.billing.constant;

/**
 * 1:购买
 * 2.转正
 * 3.续费
 * @author Minggang
 *
 */
public enum ConsumeType {
	
	CONSUME_UNKNOW((short)0,"CONSUME_UNKNOW","payment.unknow"),
	CONSUME_BUY((short)1,"PAYMENT_ONLINE","payment.online"),
	CONSUME_RENEW((short)2,"PAYMENT_OFFLINE","payment.offline"),
	CONSUME_UPGRADE((short)4,"PAYMENT_ONLINE","payment.online"),
	CONSUME_APPROVED((short)3,"PAYMENT_OFFLINE","payment.offline");
	
	private short index;
	private String type;
	private String i18n;
	
	
	private ConsumeType(short index, String type, String i18n) {
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

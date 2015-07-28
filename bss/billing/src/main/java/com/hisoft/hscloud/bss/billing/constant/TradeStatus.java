package com.hisoft.hscloud.bss.billing.constant;

/**
 * 支付状态 
 * 1:未完成
 * 2:完成
 * 3:失败
 * @author Minggang
 *
 */
public enum TradeStatus {
	
	UNKNOW_PAYMENT_LOG((short)0,"UNKNOW_PAYMENT_LOG","tradestatus.unknow"),
	TRADE_UNFINISHED((short)1,"TRADE_UNFINISHED","tradestatus.unfinished"),
	TRADE_FINISHED((short)2,"TRADE_FINISHED","tradestatus.finished"),
	TRADE_FAILED((short)3,"TRADE_FAILED","tradestatus。failed");
	
	private short index;
	private String type;
	private String i18n;
	
	
	TradeStatus(short index,String type,String i18n){
		
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

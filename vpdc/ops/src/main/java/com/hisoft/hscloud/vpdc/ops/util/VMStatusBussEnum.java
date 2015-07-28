package com.hisoft.hscloud.vpdc.ops.util;

/**
 * 虚拟机类型
 * TRYWAIT   试用待审核
 * TRY   试用
 * DELAYWAIT   延期待审核
 * DELAY   已延期
 * REGULAR   转正
 * CANCEL	取消/拒单
 * EXPIRE	试用到期
 * @author Haibin
 *
 */
public enum VMStatusBussEnum {
	
	TRYWAIT(0,"TRYWAIT","vmstatus_buss.trywait"),
	TRY(1,"TRY","vmstatus_buss.try"),
	DELAYWAIT(2,"DELAYWAIT","vmstatus_buss.delaywait"),
	DELAY(3,"DELAY","vmstatus_buss.delay"),
	REGULAR(4,"REGULAR","vmstatus_buss.regular"),
	CANCEL(5,"CANCEL","vmstatus_buss.cancel"),
	EXPIRE(6,"EXPIRE","vmstatus_buss.expire");
	//EXPIRE_TRY(16,"EXPIRE","vmstatus_buss.expire_try"),
	//EXPIRE_DELAY(36,"EXPIRE","vmstatus_buss.expire_delay"),
	//EXPIRE_REGULAR(46,"REGULAR","vmstatus_buss.expire_regular");
	
	private int code;
	private String name;
	private String i18n;
	
	VMStatusBussEnum(int code,String name,String i18n){
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

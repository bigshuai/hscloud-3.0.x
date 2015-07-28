package com.hisoft.hscloud.bss.billing.constant;


/**
 * 1   老平台
 * 2   支付宝
 * 3   快钱
 * 4   网银
 * 5   现金
 * 6   支票
 * @author Minggang
 *
 */
public enum DepositSource {

	UNKNOW((short) 0, "UNKNOW", "unknow"), 
	OLDPLAT((short) 1,"OLDPLAT", "oldplat"),
	APLIPAY((short) 2,"APLIPAY", "aplipay"),
	EASYMONEY((short) 3,"EASYMONEY", "easymoney"), 
	EBANK((short) 4,"EBANK", "ebank"),
	CASH((short) 5,"CASH", "cash"),
	CHEQUE((short) 6,"CHEQUE", "cheque");

	private short index;
	private String type;
	private String i18n;

	private DepositSource(short index, String type, String i18n) {
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

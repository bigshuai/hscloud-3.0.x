package com.hisoft.hscloud.common.entity; 
//1前台 2 后台 3控制面板
public enum LogOperatorType {
	UNKNOWN((short) 0, "UNKNOWN_OPERATOR_TYPE", "operatorType.unknown"), 
	USER((short) 1,"USER", "operatorType.user"),
	ADMIN((short) 2, "ADMIN","operatorType.admin"),
	CP((short) 3, "CP","operatorType.cp"),
	API((short) 4, "API","operatorType.api"),
	PROCESS((short) 5, "PROCESS","operatorType.process");
	
	private short index;
	private String name;
	private String i18n;

	LogOperatorType(short index, String name, String i18n) {
		this.index = index;
		this.name = name;
		this.i18n = i18n;
	}

	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
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

	public static LogOperatorType getItem(short index) {
		for (LogOperatorType item : values()) {
			if (item.getIndex() == index) {
				return item;
			}
		}
		return LogOperatorType.UNKNOWN;
	}

}

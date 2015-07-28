package com.hisoft.hscloud.common.entity;

//1：新增2：修改3：删除4：重置5：迁移
public enum LogResourceOperationType {
	UNKNOWN((short) 0, "UNKNOWN_RESOURCE_OPERATION", "resource.unknown"), 
	ADD((short) 1,"ADD", "resource.add"),
	UPDATE((short) 2, "UPDATE","resource.update"), 
	DELETE((short) 3, "DELETE", "resource.delete"),
	RESET((short) 4, "RESET", "resource.reset"), 
	MIGRATE((short) 5,"MIGRATE", "resource.migrate");

	private short index;
	private String name;
	private String i18n;

	LogResourceOperationType(short index, String name, String i18n) {
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

	public static LogResourceOperationType getItem(short index) {
		for (LogResourceOperationType item : values()) {
			if (item.getIndex() == index) {
				return item;
			}
		}
		return LogResourceOperationType.UNKNOWN;
	}

}

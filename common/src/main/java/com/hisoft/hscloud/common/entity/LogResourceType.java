package com.hisoft.hscloud.common.entity;
//1 zone 2 flavor 3 vm 4 ip 5 extend_disk
public enum LogResourceType {
	UNKNOWN((short) 0, "UNKNOWN_RESOURCE_TYPE", "resourceType.unknown"),
	ZONE((short)1,"ZONE","resourceType.zone"),
	FLAVOR((short)2,"FLAVOR","resourceType.flavor"),
	VM((short)3,"VM","resourceType.vm"),
	IP((short)4,"IP","resourceType.ip"),
	EXTEND_DISK((short)5,"EXTEND_DISK","resourceType.extend_disk"),
	ROUTER((short)6,"ROUTER","resourceType.ROUTER");
	

	private short index;
	private String name;
	private String i18n;

	LogResourceType(short index, String name, String i18n) {
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

	public static LogResourceType getItem(short index) {
		for (LogResourceType item : values()) {
			if (item.getIndex() == index) {
				return item;
			}
		}
		return LogResourceType.UNKNOWN;
	}
}
